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
