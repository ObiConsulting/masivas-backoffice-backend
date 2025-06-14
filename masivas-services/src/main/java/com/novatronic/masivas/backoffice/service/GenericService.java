package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.ComboEstadoDTO;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.logger.NovaLogger;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Obi Consulting
 */
@Service
public class GenericService {

    private static final NovaLogger LOGGER = NovaLogger.getLogger(GenericService.class);

    @Autowired
    public GenericService() {
    }

    public List<ComboEstadoDTO> listarEstados() {
        return List.of(
                new ComboEstadoDTO(ConstantesServices.ESTADO_INACTIVO, ConstantesServices.ESTADO_INACTIVO_DESCRIPCION),
                new ComboEstadoDTO(ConstantesServices.ESTADO_ACTIVO, ConstantesServices.ESTADO_ACTIVO_DESCRIPCION)
        );
    }

    public <T> void logAuditoria(T request, Evento evento, Estado estado, UserContext userContext, String nombreTabla, String accion, String mensajeExito) {
        LOGGER.audit(null, request, evento, estado, userContext.getUsername(), userContext.getScaProfile(), nombreTabla, userContext.getIp(),
                null, accion, null, null, mensajeExito);
    }
}
