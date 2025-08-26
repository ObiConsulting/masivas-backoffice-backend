package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.DetalleConsultaProcesoDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroProcesoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.entity.TpProceso;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.repository.ProcesoRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.transaction.RollbackException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
public class ProcesoServiceTest {

    @Mock
    private ProcesoRepository procesoRepository;
    @InjectMocks
    private ProcesoService procesoService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void buscarProceso_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();

        TpProceso ruta = new TpProceso();
        ruta.setCodigoOperacion("DIR");
        ruta.setHorario("0 3/14 * * * *");
        List<TpProceso> procesoList = Collections.singletonList(ruta);

        when(procesoRepository.findByCodServerInAndCodigoAccionIn(any(), any())).thenReturn(procesoList);

        Map<String, List<DetalleConsultaProcesoDTO>> resultado = procesoService.buscarProceso(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarProceso_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(procesoRepository.findByCodServerInAndCodigoAccionIn(any(), any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            procesoService.buscarProceso(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void buscarProceso_excepcion_generic() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();

        RuntimeException genericEx = new RuntimeException("");

        when(procesoRepository.findByCodServerInAndCodigoAccionIn(any(), any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            procesoService.buscarProceso(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void editarProceso_exito() {

        DetalleRegistroProcesoDTO detalle = new DetalleRegistroProcesoDTO();
        detalle.setIdProceso(123l);
        detalle.setHorario("0 3/14 * * * *");
        List<DetalleRegistroProcesoDTO> request = Collections.singletonList(detalle);

        TpProceso proceso = new TpProceso();
        proceso.setIdProceso(1l);
        proceso.setHorario("0 3/14 * * * *");
        Optional<TpProceso> optionalMok = Optional.of(proceso);

        when(procesoRepository.findById(any())).thenReturn(optionalMok);

        Long resultado = procesoService.editarProceso(request, "usuario");
        assertEquals(1, resultado);
    }

    @Test
    void editarProceso_error_noEncontrado() {
        DetalleRegistroProcesoDTO detalle = new DetalleRegistroProcesoDTO();
        detalle.setIdProceso(123l);
        detalle.setHorario("0 3/14 * * * *");
        List<DetalleRegistroProcesoDTO> request = Collections.singletonList(detalle);

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(procesoRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            procesoService.editarProceso(request, "usuario");
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void editarProceso_excepcion_bd() {
        DetalleRegistroProcesoDTO detalle = new DetalleRegistroProcesoDTO();
        detalle.setIdProceso(123l);
        detalle.setHorario("0 3/14 * * * *");
        List<DetalleRegistroProcesoDTO> request = Collections.singletonList(detalle);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(procesoRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            procesoService.editarProceso(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void editarProceso_excepcion_bd2() {
        DetalleRegistroProcesoDTO detalle = new DetalleRegistroProcesoDTO();
        detalle.setIdProceso(123l);
        detalle.setHorario("0 3/14 * * * *");
        List<DetalleRegistroProcesoDTO> request = Collections.singletonList(detalle);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");
        when(procesoRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            procesoService.editarProceso(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void editarProceso_excepcion_generico() {
        DetalleRegistroProcesoDTO detalle = new DetalleRegistroProcesoDTO();
        detalle.setIdProceso(123l);
        detalle.setHorario("0 3/14 * * * *");
        List<DetalleRegistroProcesoDTO> request = Collections.singletonList(detalle);

        RuntimeException genericEx = new RuntimeException("");
        when(procesoRepository.findById(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            procesoService.editarProceso(request, "usuario");
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void obtenerProceso_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();

//        TpProceso proceso = new TpProceso();
//        proceso.setIdProceso(1l);
//        proceso.setHorario("0 3/14 * * * *");
//        Optional<TpProceso> optionalMok = Optional.of(proceso);
        TpProceso ruta = new TpProceso();
        ruta.setCodigoOperacion("DIR");
        ruta.setHorario("0 3/14 * * * *");
        List<TpProceso> procesoList = Collections.singletonList(ruta);

        when(procesoRepository.findByCodServerInAndCodigoAccionInAndCodigoOperacion(any(), any(), any())).thenReturn(procesoList);

        Map<String, List<DetalleRegistroProcesoDTO>> resultado = procesoService.obtenerProceso(request);
        assertNotNull(resultado);
    }
//
//    @Test
//    void obtenerRutaArchivo_excepcion_bd() {
//        FiltroMasivasRequest request = new FiltroMasivasRequest();
//        request.setIdRuta(1l);
//
//        RollbackException rollbackEx = new RollbackException("");
//        RuntimeException genericEx = new RuntimeException("", rollbackEx);
//        when(procesoRepository.buscarPorId(any())).thenThrow(genericEx);
//
//        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
//            procesoService.obtenerRuta(request);
//        });
//        assertTrue(thrown instanceof DataBaseException);
//    }
//
//    @Test
//    void obtenerRutaArchivo_excepcion_generico() {
//        FiltroMasivasRequest request = new FiltroMasivasRequest();
//        request.setIdRuta(1l);
//
//        RuntimeException genericEx = new RuntimeException("");
//        when(procesoRepository.buscarPorId(any())).thenThrow(genericEx);
//
//        GenericException thrown = assertThrows(GenericException.class, () -> {
//            procesoService.obtenerRuta(request);
//        });
//        assertTrue(thrown instanceof GenericException);
//    }
//
}
