package com.intuit.iam.exceptions;



public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -1329985867476670234L;
    private int code;
    private String msg;

    public ServiceException(int errorCode) {
        this.code = errorCode;
    }

    public ServiceException(int errorCode, String msg) {
        this.code = errorCode;
        this.msg = msg;
    }


    public ServiceException(int errorCode, Throwable th) {
        super(th);
        this.code = errorCode;
    }

    public int getErrorCode() {
        return code;
    }

    public String getMessage() {
        return this.msg;
    }
}
