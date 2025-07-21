package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.ComboEstadoDTO;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Obi Consulting
 */
@Service
public class GenericService {

    private static final NovaLogger LOGGER = NovaLogger.getLogger(GenericService.class);

    public List<ComboEstadoDTO> listarEstados() {
        return List.of(
                new ComboEstadoDTO(ConstantesServices.ESTADO_INACTIVO, ConstantesServices.ESTADO_INACTIVO_DESCRIPCION),
                new ComboEstadoDTO(ConstantesServices.ESTADO_ACTIVO, ConstantesServices.ESTADO_ACTIVO_DESCRIPCION)
        );
    }

}
