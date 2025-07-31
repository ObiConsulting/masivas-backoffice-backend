/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.novatronic.masivas.backoffice.exception;

/**
 *
 * @author Raúl Vargas
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
