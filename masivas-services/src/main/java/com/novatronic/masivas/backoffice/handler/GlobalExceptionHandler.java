package com.novatronic.masivas.backoffice.handler;

import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author Obi Consulting
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final NovaLogger LOGGER = NovaLogger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MasivasResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errores = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(new MasivasResponse<>(ConstantesServices.ERROR_BADREQUEST, ConstantesServices.MENSAJE_BAD_REQUEST + ": " + errores, null));
    }

    @ExceptionHandler(UniqueFieldException.class)
    public ResponseEntity<MasivasResponse<Object>> handleUniqueFieldException(UniqueFieldException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.OK).body(new MasivasResponse<>(ex.getErrorCode(), ex.getMessage(), null));
    }

    @ExceptionHandler(NoOperationExistsException.class)
    public ResponseEntity<MasivasResponse<Object>> NoSuchElementException(NoOperationExistsException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.OK).body(new MasivasResponse<>(ex.getErrorCode(), ex.getMessage(), null));
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<MasivasResponse<Object>> handleDataBaseException(DataBaseException ex) {
        LOGGER.error(ConstantesServices.MENSAJE_ERROR_BD, ex);
        return ResponseEntity.status(HttpStatus.OK).body(new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_BD, ConstantesServices.MENSAJE_ERROR_BD, null));
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<MasivasResponse<Object>> handleGenericException(GenericException ex) {
        LOGGER.error(ConstantesServices.MENSAJE_ERROR_GENERICO, ex);
        return ResponseEntity.status(HttpStatus.OK).body(new MasivasResponse<>(ConstantesServices.CODIGO_ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null));
    }

}
