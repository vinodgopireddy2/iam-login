package com.intuit.iam.api.login;


import com.intuit.iam.model.requestbody.LoginRequestBody;
import lombok.NonNull;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class LoginApi {

    private final LoginApiService delegate;

    @Inject
    public LoginApi(@NonNull LoginApiService delegate) {
        this.delegate = delegate;
    }

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestBody body, @Context HttpServletRequest req, @Context HttpServletResponse res) {
            return delegate.login(body, req, res);
    }
}