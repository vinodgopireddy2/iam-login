package com.intuit.iam.exceptions;

import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BuildErrorResponse {

    public static Response buildErrorResponse(long errorCode, String message, Response.Status status, String cause) {
        ErrorStack es = new ErrorStack();
        es.setErrorCode( errorCode);
        es.setMessage(message);
        if (null != cause) {
            es.setCause(cause);
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(es);
        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(jsonString).build();
    }

    public static Response buildErrorResponse(Exception e, IamErrors se, boolean useIAMErrorMsg) {
        int errorCode = se == null ? 0 : se.code();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        String message = "Error happened while processing the request";

        if (e instanceof ServiceException) {
            ServiceException stE = (ServiceException) e;
            errorCode = stE.getErrorCode();
            message = stE.getMessage();
        }
        if(useIAMErrorMsg)
            return buildErrorResponse(errorCode, message, status, se.getMsg());
        else
            return buildErrorResponse(errorCode, message, status, ExceptionUtils.getRootCauseMessage(e));
    }

    public static Response buildBadRequestResponse(long errorCode, Exception e) {
        ErrorStack es = new ErrorStack();
        es.setErrorCode(errorCode);
        String msg;

        if (e instanceof ServiceException) {
            ServiceException se = (ServiceException) e;
            msg = se.getMessage();
        }
        es.setMessage(e.getMessage());
        es.setCause(ExceptionUtils.getRootCauseMessage(e));

        Gson gson = new Gson();
        String jsonString = gson.toJson(es);
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON).entity(jsonString).build();
    }

    public static Response buildBadRequestResponse(long errorCode, String msg) {
        ErrorStack es = new ErrorStack();
        es.setErrorCode(errorCode);
        es.setMessage(msg);
        Gson gson = new Gson();
        String jsonString = gson.toJson(es);
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON).entity(jsonString).build();
    }

}
