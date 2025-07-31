package com.novatronic.masivas.backoffice.exception;

/**
 *
 * @author Obi Consulting
 */
public class NoOperationExistsException extends RuntimeException {

    private String errorCode = "";

    public NoOperationExistsException(String message) {
        super(message);
    }

    public NoOperationExistsException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
