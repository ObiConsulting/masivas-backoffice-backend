package com.novatronic.masivas.backoffice.handler;

import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.exception.ActionRestCoreException;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.masivas.backoffice.util.LogUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author Obi Consulting
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final NovaLogger LOGGER = NovaLogger.getLogger(GlobalExceptionHandler.class);
    private String log="";
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MasivasResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        log=LogUtil.generarMensajeLogError(null,"Se produjo una Validation Exception",null);
        LOGGER.error(log,ex);
        String errores = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(new MasivasResponse<>(ConstantesServices.ERROR_BADREQUEST, ConstantesServices.MENSAJE_BAD_REQUEST + ": " + errores, null));
    }

    @ExceptionHandler({UniqueFieldException.class, NoOperationExistsException.class, ActionRestCoreException.class})
    public ResponseEntity<MasivasResponse<Object>> handleBusinessExceptions(RuntimeException ex) {
        log=LogUtil.generarMensajeLogError(null,"Se produjo una Business Exception",null);
        LOGGER.error(log,ex);
        String errorCode = "";

        if (ex instanceof UniqueFieldException uniqueFieldException) {
            errorCode = uniqueFieldException.getErrorCode();
        }
        if (ex instanceof NoOperationExistsException noOperationExistsException) {
            errorCode = noOperationExistsException.getErrorCode();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new MasivasResponse<>(errorCode, ex.getMessage(), null));
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<MasivasResponse<Object>> handleDataBaseException(DataBaseException ex) {
        log=LogUtil.generarMensajeLogError(null,ConstantesServices.MENSAJE_ERROR_BD,null);
        LOGGER.error(log, ex);
        return ResponseEntity.status(HttpStatus.OK).body(new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_BD, ConstantesServices.MENSAJE_ERROR_BD, null));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<MasivasResponse<Object>> handleRestClientException(RestClientException ex) {
        log=LogUtil.generarMensajeLogError(null,ConstantesServices.MENSAJE_ERROR_API_CORE,null);
        LOGGER.error(log, ex);
        return ResponseEntity.status(HttpStatus.OK).body(new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_API_CORE, ConstantesServices.MENSAJE_ERROR_API_CORE, null));
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<MasivasResponse<Object>> handleGenericException(GenericException ex) {
        log=LogUtil.generarMensajeLogError(null,ConstantesServices.MENSAJE_ERROR_GENERICO,null);
        LOGGER.error(log, ex);
        return ResponseEntity.status(HttpStatus.OK).body(new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null));
    }

}
