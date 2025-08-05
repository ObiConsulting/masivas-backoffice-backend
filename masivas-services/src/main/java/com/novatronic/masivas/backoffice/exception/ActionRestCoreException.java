package com.novatronic.masivas.backoffice.exception;

/**
 *
 * @author Obi Consulting
 */
public class ActionRestCoreException extends RuntimeException {

    private String errorCode = "";

    public ActionRestCoreException(String message) {
        super(message);
    }

    public ActionRestCoreException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
