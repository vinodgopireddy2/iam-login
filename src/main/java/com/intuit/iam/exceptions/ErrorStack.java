package com.intuit.iam.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ErrorStack   {
    @JsonProperty("Callstack")
    private List<String> callstack = new ArrayList<String>();

    @JsonProperty("Cause")
    private String cause = null;

    @JsonProperty("ErrorCode")
    private Long errorCode = null;

    @JsonProperty("Message")
    private String message = null;

    public ErrorStack callstack(List<String> callstack) {
        this.callstack = callstack;
        return this;
    }

    public ErrorStack addCallstackItem(String callstackItem) {
        this.callstack.add(callstackItem);
        return this;
    }

    @JsonProperty("Callstack")
    public List<String> getCallstack() {
        return callstack;
    }

    public void setCallstack(List<String> callstack) {
        this.callstack = callstack;
    }

    public ErrorStack cause(String cause) {
        this.cause = cause;
        return this;
    }

    @JsonProperty("Cause")
    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public ErrorStack errorCode(Long errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @JsonProperty("ErrorCode")
    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorStack message(String message) {
        this.message = message;
        return this;
    }

    @JsonProperty("Message")
    @NotNull
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorStack errorStack = (ErrorStack) o;
        return Objects.equals(this.callstack, errorStack.callstack) &&
                Objects.equals(this.cause, errorStack.cause) &&
                Objects.equals(this.errorCode, errorStack.errorCode) &&
                Objects.equals(this.message, errorStack.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callstack, cause, errorCode, message);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorStack {\n");

        sb.append("    callstack: ").append(toIndentedString(callstack)).append("\n");
        sb.append("    cause: ").append(toIndentedString(cause)).append("\n");
        sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

