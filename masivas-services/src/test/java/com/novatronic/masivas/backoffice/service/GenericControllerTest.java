package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.controller.GenericController;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.ParametroDTO;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
public class GenericControllerTest {

    @Mock
    private GenericService genericService;
    @InjectMocks
    private GenericController genericController;

    @Test
    void listarEstados_exito() {
        List<ParametroDTO> response = new ArrayList();

        when(genericService.listarEstados()).thenReturn(response);

        ResponseEntity<MasivasResponse<Object>> resultado = genericController.listarEstados();
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(ConstantesServices.RESPUESTA_OK_API, ((MasivasResponse<?>) resultado.getBody()).getCodigo());
    }

}
