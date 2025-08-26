package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.ParametroDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.service.GenericService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Obi Consulting
 */
@RequestMapping(value = "generico", produces = "application/json")
@RestController
public class GenericController {

    @Autowired
    GenericService genericService;

    @GetMapping("/estados")
    public ResponseEntity<MasivasResponse<Object>> listarEstados() {
        List<ParametroDTO> lista = genericService.listarEstados();
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, lista));
    }

}
