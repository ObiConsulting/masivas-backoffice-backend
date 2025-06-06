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
