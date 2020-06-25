package com.intuit.iam.api.login;

import com.intuit.iam.dba.SessionAccessor;
import com.intuit.iam.dba.UserAccessor;
import com.intuit.iam.exceptions.BuildErrorResponse;
import com.intuit.iam.exceptions.IamErrors;
import com.intuit.iam.model.db.Session;
import com.intuit.iam.model.db.User;
import com.intuit.iam.model.requestbody.LoginRequestBody;
import com.intuit.iam.utils.CryptoUtils;
import com.intuit.iam.validation.Rules.BasicStringRule;
import com.intuit.iam.validation.Rules.Field;
import com.intuit.iam.validation.Rules.PostBodyRule;
import com.intuit.iam.validation.Rules.Rule;
import com.intuit.iam.validation.Validator.Validator;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.time.Instant;

import static com.intuit.iam.utils.CryptoUtils.generateUniqueKeyWithUUIDAndMessageDigest;
import static com.intuit.iam.utils.JsonTransformer.render;

@Singleton
public class LoginApiService {

    private static final Logger logger = LogManager.getLogger(com.intuit.iam.api.login.LoginApiService.class);

    public static Logger getLogger() {
        return logger;
    }

    private UserAccessor userAccessor;
    private SessionAccessor sessionAccessor;

    @Inject
    public LoginApiService(@NonNull UserAccessor userAccessor, @NonNull SessionAccessor sessionAccessor) {
        this.userAccessor = userAccessor;
        this.sessionAccessor = sessionAccessor;
    }
    public Response login(LoginRequestBody loginRequestBody, HttpServletRequest req, HttpServletResponse res) {
        logger.debug("In login method");

        //Validating request body
        Validator<Rule<Field>, Field> validator = new Validator<>();
        validator.addRule(new PostBodyRule(), new Field("loginRequestBody", loginRequestBody,"LoginRequestBody"));
        validator.addRule(new BasicStringRule(), new Field("password", loginRequestBody.getPassword(),"(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20}"));
        validator.addRule(new BasicStringRule(), new Field("userName", loginRequestBody.getUserName(), "^[^±!@£$%^&*_+§¡€#¢§¶•ªº«\\\\/<>?:;|=.,]{1,20}$"));
        try {
            validator.validate();
        } catch (Exception e) {
            logger.error("Post body validation failed.", e);
            return BuildErrorResponse.buildBadRequestResponse(Response.Status.BAD_REQUEST.getStatusCode(), e);
        }

        try {
            User user = userAccessor.get(loginRequestBody.getUserName().toLowerCase()); //While registering, userName was made case insensitive
            if (user == null) {
                logger.error("Username not registered");
                return BuildErrorResponse.buildErrorResponse(IamErrors.eLoginFailure.code(),
                        IamErrors.eLoginFailure.getMsg(),
                        Response.Status.UNAUTHORIZED,
                        IamErrors.eLoginFailure.getMsg());
            }
            //accLockTime < current time then only proceed. Reset failedLoginAttempt to 0
            if(user.getAccLockTime() > Instant.now().getEpochSecond()) {
                return BuildErrorResponse.buildErrorResponse(IamErrors.eAccountLockedOnFailedAttempts.code(),
                        IamErrors.eAccountLockedOnFailedAttempts.getMsg(),
                        Response.Status.UNAUTHORIZED,
                        IamErrors.eAccountLockedOnFailedAttempts.getMsg() + ". Please try again later");
            }
            //validate user entered password
            boolean isValidPassword = CryptoUtils.validatePassword(loginRequestBody.getPassword(), user.getPassword());
            if(isValidPassword) {
                return successCase(user, req, res);
            } else {
                return failureCase(user);
            }

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Error happened in crypto library", e);
            return BuildErrorResponse.buildErrorResponse(e, IamErrors.eCryptoError, true);
        }
    }

    private Response successCase(User user, HttpServletRequest req, HttpServletResponse res) {

        try {
            //Need to set to zero for cases where failedLoginAttempts = 1 or 2 on successful login
            user.setFailedLoginAttempts(0);
            user.setLastLoginTime(Instant.now().getEpochSecond());

            /* Following are required for oauth2, and when app wants to send client id and client secret
             appid, audience, issueby, scope, keyid etc
             I have not used JWT to get runtime flexibility to change the token behaviour like changing the scope of valid token etc.
             Ideally, we should use JWT when we do not need the above case, and can be stateless as token carry user info etc with it.
            */
            //create session (id_token, refresh_token and session_token)
            Session session = new Session();
            String access_token = generateUniqueKeyWithUUIDAndMessageDigest();
            String refresh_token = generateUniqueKeyWithUUIDAndMessageDigest();
            session.setAccess_token(access_token);
            session.setRefresh_token(refresh_token);
            session.setUserName(user.getUserName());

            session.setCreateTime(Instant.now().getEpochSecond());
            session.setIdleTimeExpiration(Instant.now().plus(Duration.ofSeconds(1200))); //Setting 20min expiry to support access_token expiry

            String clientIp = req.getHeader("X-Forwarded-For");
            if(clientIp == null || clientIp.isEmpty())
                clientIp = req.getRemoteAddr();
            else
                clientIp = clientIp.split(",")[0];
            session.setClientIpAddress(clientIp);
            session.setUserAgent(req.getHeader("User-Agent"));

            //Set access_token and refresh_token in response headers
            res.setHeader("access_token", access_token);
            res.setHeader("refresh_token", refresh_token);

            //set session object to persistence store.
            sessionAccessor.add(session);

            //update user on persistence store.
            userAccessor.update(user);
            logger.info("[Audit] UserName {} logged in successfully", user.getUserName());
            String response = render(user);
            return Response.status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON).entity(response).build();
        } catch (NoSuchAlgorithmException |  UnsupportedEncodingException e) {
            logger.error("Error happened in crypto library", e);
            return BuildErrorResponse.buildErrorResponse(e, IamErrors.eCryptoError, true);
        }
    }

    private Response failureCase(User user) {
        //increase failedLoginAttempt and on 3rd failure attempt set the 'accLockTime' to Instant.now() + 5mins
        int failedAttempts = user.getFailedLoginAttempts();

        if (user.getFailedLoginAttempts() == 0) {
            user.setFailedLoginAttempts(failedAttempts + 1);
            user.setAccLockTime(Instant.now().getEpochSecond());
        }
        //Increase failedAttempt account only if loginAttempts are made within last 5 mins for 3 attempts.
        else if(user.getAccLockTime() > (Instant.now().getEpochSecond()-300) && failedAttempts < 3) {
            user.setFailedLoginAttempts(failedAttempts+1);
            if(failedAttempts + 1 == 3) {
                user.setAccLockTime(Instant.now().getEpochSecond() + 300);
                user.setFailedLoginAttempts(0);
            }
        } else { //If user had "< 3 failure attempts and attempted login and failed after long time (after 5mins window)" or came back after 5min locktime, user comes to this block.
            user.setFailedLoginAttempts(1);
            user.setAccLockTime(Instant.now().getEpochSecond());
        }
        userAccessor.update(user);
        logger.info("[Audit] UserName {} failed to login", user.getUserName());

        return BuildErrorResponse.buildErrorResponse(IamErrors.eLoginFailure.code(),
                IamErrors.eLoginFailure.getMsg(),
                Response.Status.UNAUTHORIZED,
                IamErrors.eLoginFailure.getMsg());
    }
}