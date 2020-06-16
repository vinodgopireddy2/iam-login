package com.intuit.iam.api.register;

import com.intuit.iam.model.requestbody.RegisterRequestBody;
import lombok.NonNull;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class RegisterApi {

    private final RegisterApiService delegate;

    @Inject
    public RegisterApi(@NonNull RegisterApiService delegate) {
        this.delegate = delegate;
    }

    @POST
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(RegisterRequestBody body) {
            return delegate.register(body);
    }
}