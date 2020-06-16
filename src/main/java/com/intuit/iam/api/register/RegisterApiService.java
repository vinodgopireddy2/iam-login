package com.intuit.iam.api.register;

import com.intuit.iam.dba.UserAccessor;
import com.intuit.iam.exceptions.BuildErrorResponse;
import com.intuit.iam.exceptions.IamErrors;
import com.intuit.iam.model.db.LicenseTier;
import com.intuit.iam.model.db.Product;
import com.intuit.iam.model.db.User;
import com.intuit.iam.model.requestbody.RegisterRequestBody;
import com.intuit.iam.validation.Rules.BasicStringRule;
import com.intuit.iam.validation.Rules.Field;
import com.intuit.iam.validation.Rules.PostBodyRule;
import com.intuit.iam.validation.Rules.Rule;
import com.intuit.iam.validation.Validator.Validator;
import com.mongodb.MongoWriteException;
import lombok.NonNull;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.intuit.iam.utils.CryptoUtils.generateStorngPasswordHash;
import static com.intuit.iam.utils.JsonTransformer.render;

@Singleton
public class RegisterApiService {

    private static final Logger logger = LogManager.getLogger(RegisterApiService.class);

    private UserAccessor userAccessor;

    @Inject
    public RegisterApiService(@NonNull UserAccessor userAccessor) {
        this.userAccessor = userAccessor;
    }

    public Response register(RegisterRequestBody registerRequestBody) {
        logger.debug("In register method");

        //Validating request body
        Validator<Rule<Field>, Field> validator = new Validator<>();
        validator.addRule(new PostBodyRule(), new Field("registerRequestBody", registerRequestBody,"RegisterRequestBody"));
        /* We can add password entropy as well here
          (?=.*\d)		    #   must contains one digit from 0-9
          (?=.*[a-z])		#   must contains one lowercase characters
          (?=.*[A-Z])		#   must contains one uppercase characters
          (?=.*[@#$%])		#   must contains one special symbols in the list "@#$%"
          .		            #   match anything with previous condition checking
          {6,20}            #   length at least 6 characters and maximum of 20
         */
        validator.addRule(new BasicStringRule(), new Field("password", registerRequestBody.getPassword(),"(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20}"));
        validator.addRule(new BasicStringRule(), new Field("firstName", registerRequestBody.getFirstName(), "^[^±!@£$%^&*_+§¡€#¢§¶•ªº«\\\\/<>?:;|=.,]{1,20}$"));
        validator.addRule(new BasicStringRule(), new Field("lastName", registerRequestBody.getLastName(), "^[^±!@£$%^&*_+§¡€#¢§¶•ªº«\\\\/<>?:;|=.,]{1,20}$"));
        validator.addRule(new BasicStringRule(), new Field("userName", registerRequestBody.getUserName(), "^[^±!@£$%^&*_+§¡€#¢§¶•ªº«\\\\/<>?:;|=.,]{1,20}$"));
        try {
            validator.validate();
        } catch (Exception e) {
            return BuildErrorResponse.buildBadRequestResponse(Response.Status.BAD_REQUEST.getStatusCode(), e);
        }

        //Email validator
        boolean valid = EmailValidator.getInstance().isValid(registerRequestBody.getEmail());
        if(!valid)
            return BuildErrorResponse.buildBadRequestResponse(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid email id sent.");

        User user = new User();
        try {
            BeanUtils.copyProperties(user, registerRequestBody);
            logger.info("Registering userName: {}", user.getUserName());
            updateUserObject(user);
            userAccessor.add(user);

            logger.info("[REGISTRATION] User: {}, {} got successfully registered.", user.getFirstName(), user.getLastName());
            String res = render(registerRequestBody);
            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON).entity(res).build();

        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Error happened while parsing data", e);
            return BuildErrorResponse.buildErrorResponse(e, IamErrors.eBeanParsingError, true);
        } catch (MongoWriteException e) {
            logger.error("Error happened while writing to db", e);
            if(e.getMessage().matches(".*userName.*") && e.getError().getCategory().name().matches("DUPLICATE_KEY") ) {
                return BuildErrorResponse.buildErrorResponse(IamErrors.eDuplicateUserName.code(), IamErrors.eDuplicateUserName.getMsg(), Response.Status.INTERNAL_SERVER_ERROR, IamErrors.eDuplicateUserName.getMsg());

            } else if(e.getMessage().matches(".*email.*") && e.getError().getCategory().name().matches("DUPLICATE_KEY")) {
                return BuildErrorResponse.buildErrorResponse(IamErrors.eDuplicateEmail.code(), IamErrors.eDuplicateEmail.getMsg(), Response.Status.INTERNAL_SERVER_ERROR, IamErrors.eDuplicateEmail.getMsg());
            } else {
                return BuildErrorResponse.buildErrorResponse(e, IamErrors.eDbError, true);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return BuildErrorResponse.buildErrorResponse(e, IamErrors.eCryptoError, true);
        } catch (Exception e) {
            logger.error("Caught error while registering user", e);
            return BuildErrorResponse.buildErrorResponse(e, IamErrors.eInternalError, true);
        }
    }

    private void updateUserObject(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {

        Map<String, LicenseTier> licenseMap = new HashMap<>();
        createDefaultLicenseMap(licenseMap);

        user.setCreateTime(Instant.now().getEpochSecond());
        user.setLastUpdatedTime(Instant.now().getEpochSecond());

        user.setLicenseMap(licenseMap);
        user.setEmailLowerCase(user.getEmail().toLowerCase());
        user.setUserName(user.getUserName().toLowerCase()); //Making userName case insensitive.

        try {
            String securedPasswordHash = generateStorngPasswordHash(user.getPassword());
            user.setPassword(securedPasswordHash);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Caught error while generating user password hash", e);
            throw e;
        } catch (InvalidKeySpecException e) {
            logger.error("Caught error while generating user password hash", e);
            throw e;
        }
    }

    //defaultLicenseMap setting
    private void createDefaultLicenseMap (Map<String, LicenseTier> licenseMap) {
        licenseMap.put(Product.QUCIKBOOKS_SELF_EMPLOYEED.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.MINT.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.TURBOTAX.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.TURBO.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.QUICKBOOKS.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.CHECKS_AND_TAXFORMS.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.QUICKBOOKS_PAYROLL.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.PAYMENTS.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.TSHEETS.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.LACERTE_TAX.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.PROCONNECT_TAX.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.PROSERIES_TAX.toString(), LicenseTier.BASIC);
        licenseMap.put(Product.QUICKBOOKS_ONLINE_ACCOUNTANTS.toString(), LicenseTier.BASIC);
    }
}