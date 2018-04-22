package org.sapl.commons.exception;


import java.io.Serializable;

public class ApiException extends Exception implements Serializable {

    private int errorCode;
    private String errorReason;

    public ApiException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorReason = message;
    }

    public ApiException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public Json toJson() {
        return new Json(errorCode, errorReason);
    }

    public class Json {
        public int errorCode;
        public String errorReason;

        public Json(int errorCode, String errorReason) {
            this.errorCode = errorCode;
            this.errorReason = errorReason;
        }
    }
}
