package com.novatronic.masivas.backoffice.repository;

import com.novatronic.masivas.backoffice.dao.StoredProcedurePaginator;
import com.novatronic.masivas.backoffice.dto.ClienteDTO;
import com.novatronic.masivas.backoffice.dto.ProcedureParam;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacion;
import com.novatronic.masivas.backoffice.dto.ResultadoOperacionConsulta;
import com.novatronic.masivas.backoffice.util.Constantes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author obi
 */
@Repository
public class ClienteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultadoOperacion insertarCliente(
            String codTipoDoc,
            String nomCliente,
            String direccionCliente,
            String numDocumento,
            String numCuentaCci,
            String usuarioCreacion
    ) {

        StoredProcedurePaginator spDao = new StoredProcedurePaginator(entityManager);
        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, String.class, ParameterMode.IN, codTipoDoc),
                new ProcedureParam(2, String.class, ParameterMode.IN, nomCliente),
                new ProcedureParam(3, String.class, ParameterMode.IN, direccionCliente),
                new ProcedureParam(4, String.class, ParameterMode.IN, numDocumento),
                new ProcedureParam(5, String.class, ParameterMode.IN, numCuentaCci),
                new ProcedureParam(6, String.class, ParameterMode.IN, usuarioCreacion),
                new ProcedureParam(7, BigDecimal.class, ParameterMode.OUT, null),
                new ProcedureParam(8, String.class, ParameterMode.OUT, null),
                new ProcedureParam(9, String.class, ParameterMode.OUT, null)
        );

        try {
            Map<Integer, Object> out = spDao.execute(Constantes.PACKAGE_BD + "." + Constantes.SPI_INSERTAR_CLIENTE, params);
            return new ResultadoOperacion(
                    (BigDecimal) out.get(7),
                    (String) out.get(8),
                    (String) out.get(9)
            );

        } catch (Exception e) {
            return new ResultadoOperacion(null, Constantes.ERROR_BD, Constantes.MENSAJE_ERROR_BD_USUARIO);
        }

    }

    public ResultadoOperacionConsulta<ClienteDTO> obtenerCliente(String idOperacion) {
        StoredProcedurePaginator dao = new StoredProcedurePaginator(entityManager);

        List<ProcedureParam> params = List.of(
                new ProcedureParam(1, String.class, ParameterMode.IN, idOperacion),
                new ProcedureParam(2, void.class, ParameterMode.REF_CURSOR, null)
        );

        return dao.executeSingleResult(
                Constantes.PACKAGE_BD + "." + Constantes.SPS_DATA_CLIENTE,
                params,
                row -> {
                    ClienteDTO dto = new ClienteDTO();
                    dto.setCodTipoDocumento((String) row[1]);
                    dto.setNombre((String) row[3]);
                    dto.setDireccion((String) row[4]);
                    dto.setNumDocumento((String) row[5]);
                    dto.setNumCuentaCCI((String) row[6]);
                    return dto;
                },
                2 // posición REF_CURSOR
        );
    }

}
