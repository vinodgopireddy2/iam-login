package com.intuit.iam.exceptions;

public enum IamErrors {
    eDuplicateUserName(11000, "UserName already exists, please try different one"),
    eDuplicateEmail(11000, "Email already registered, please try login"),
    eDbError(1, "Unable to access the data"),
    eBeanParsingError(2, "Error in processing data"),
    eInternalError(3, "Internal server error"),
    eCryptoError(100, "Error in crypto operation"),
    eLoginFailure(300, "Login failed, username and password combination didn't work. Try again"),
    eAccountLockedOnFailedAttempts(301, "Account got locked on 3 continuous failed attempts");

    private int code;
    private String msg;

    IamErrors(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

