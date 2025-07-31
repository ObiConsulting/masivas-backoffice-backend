package com.novatronic.masivas.backoffice.exception;

/**
 *
 * @author Obi Consulting
 */
public class JasperReportException extends RuntimeException {

    private String errorCode = "";

    public JasperReportException(String message) {
        super(message);
    }

    public JasperReportException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
