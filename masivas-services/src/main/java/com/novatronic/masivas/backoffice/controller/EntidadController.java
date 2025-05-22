package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.TpEntidadDTO;
import com.novatronic.masivas.backoffice.entity.TpEntidad;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.EntidadService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Obi Consulting
 */
@RequestMapping(value = "entidad", produces = "application/json")
@RestController
public class EntidadController {

    @Autowired
    private EntidadService entidadService;

    @PostMapping("/crear")
    public ResponseEntity<MasivasResponse> registrarEntidad(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        try {
            entidadService.crearEntidad(request, userContext.getUsername());
            return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_ENTIDAD, null));
        } catch (Exception e) {
            MasivasResponse res = new MasivasResponse(ConstantesServices.ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse> buscarEntidad(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {

        ModelMapper modelMapper = new ModelMapper();

        Page<TpEntidad> objPegeable = entidadService.buscar(request, userContext.getUsername());

        if (objPegeable != null) {

            Page<TpEntidadDTO> dtoPage = objPegeable.map(e -> modelMapper.map(e, TpEntidadDTO.class));

            int totalPaginas = objPegeable.getTotalPages();
            long totalRegistrosLong = objPegeable.getTotalElements();

            int totalRegistros = (totalRegistrosLong > Integer.MAX_VALUE)
                    ? Integer.MAX_VALUE
                    : (int) totalRegistrosLong;

            CustomPaginate customPaginate = new CustomPaginate<>(
                    totalPaginas,
                    totalRegistros,
                    dtoPage.getContent()
            );

            return ResponseEntity.ok(new MasivasResponse(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_ENTIDAD, customPaginate));

        } else {
            MasivasResponse res = new MasivasResponse(ConstantesServices.ERROR_GENERICO, ConstantesServices.MENSAJE_ERROR_GENERICO, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }

    }

}
