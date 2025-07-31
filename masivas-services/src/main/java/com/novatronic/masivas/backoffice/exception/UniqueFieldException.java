package com.novatronic.masivas.backoffice.exception;

/**
 *
 * @author Obi Consulting
 */
public class UniqueFieldException extends RuntimeException {

    private String errorCode = "";

    public UniqueFieldException(String message) {
        super(message);
    }

    public UniqueFieldException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
