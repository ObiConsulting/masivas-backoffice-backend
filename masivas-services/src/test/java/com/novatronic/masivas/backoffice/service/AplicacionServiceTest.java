package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroAplicacionDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpAplicacion;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import com.novatronic.masivas.backoffice.repository.AplicacionRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.hibernate.exception.ConstraintViolationException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
public class AplicacionServiceTest {

    @Mock
    private AplicacionRepository aplicacionRepository;
    @Mock
    private GenericService genericService;
    @Mock
    private CoreService coreService;
    @InjectMocks
    private AplicacionService aplicacionService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(aplicacionService, "logo", "logo.png");
    }

    private MasivasRequestDTO crearRequest() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        request.setCodigo("001");
        request.setNombre("Aplicacion Prueba");
        request.setEstado(ConstantesServices.ESTADO_INACTIVO);
        return request;
    }

    @Test
    void crearAplicacion_exito() {
        MasivasRequestDTO request = crearRequest();
        when(genericService.getIdEntidadPropietaria()).thenReturn(1l);
        when(aplicacionRepository.save(any(TpAplicacion.class)))
                .thenAnswer(invocation -> {
                    TpAplicacion aplicacionGuardada = invocation.getArgument(0);
                    ReflectionTestUtils.setField(aplicacionGuardada, "idAplicacion", 123l);
                    return aplicacionGuardada;
                });
        Long resultado = aplicacionService.crearAplicacion(request, "usuario");
        assertEquals(123, resultado);
    }

    @Test
    void crearAplicacion_error_campoUnico() {
        MasivasRequestDTO request = crearRequest();
        when(genericService.getIdEntidadPropietaria()).thenReturn(1l);

        ConstraintViolationException constraintEx = new ConstraintViolationException("", null, "");
        RollbackException rollbackEx = new RollbackException("");
        rollbackEx.initCause(constraintEx);
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(aplicacionRepository.save(any(TpAplicacion.class))).thenThrow(genericEx);

        UniqueFieldException thrown = assertThrows(UniqueFieldException.class, () -> {
            aplicacionService.crearAplicacion(request, "usuario");
        });

        assertEquals(ConstantesServices.CODIGO_ERROR_COD_APLICACION_UNICA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_COD_APLICACION_UNICA, thrown.getMessage());
    }

    @Test
    void crearAplicacion_excepcion_bd() {
        MasivasRequestDTO request = crearRequest();
        when(genericService.getIdEntidadPropietaria()).thenReturn(1l);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(aplicacionRepository.save(any(TpAplicacion.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            aplicacionService.crearAplicacion(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void crearAplicacion_excepcion_generic() {
        MasivasRequestDTO request = crearRequest();
        when(genericService.getIdEntidadPropietaria()).thenReturn(1l);

        RuntimeException genericEx = new RuntimeException("");

        when(aplicacionRepository.save(any(TpAplicacion.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            aplicacionService.crearAplicacion(request, "usuario");
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void buscarAplicacion_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("App de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        TpAplicacion aplicacion = new TpAplicacion(1l, "COD001", "App de Prueba", ConstantesServices.ESTADO_ACTIVO, LocalDateTime.now(), "usuario");
        aplicacion.setIdAplicacion(1L);
        List<TpAplicacion> aplicacionesList = Collections.singletonList(aplicacion);
        Page<TpAplicacion> paginaSimulada = new PageImpl<>(aplicacionesList, Pageable.ofSize(10), 1);

        when(aplicacionRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        CustomPaginate<DetalleConsultaAplicacionDTO> resultado = aplicacionService.buscarAplicacion(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarAplicacion_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("App de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(aplicacionRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            aplicacionService.buscarAplicacion(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void buscarAplicacion_excepcion_generic() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("App de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RuntimeException genericEx = new RuntimeException("");

        when(aplicacionRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            aplicacionService.buscarAplicacion(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void editarAplicacion_exito() {
        MasivasRequestDTO request = crearRequest();

        TpAplicacion aplicacionSimulada = new TpAplicacion();
        aplicacionSimulada.setIdAplicacion(1L);
        aplicacionSimulada.setCodigo("APP_TEST");
        Optional<TpAplicacion> optionalMok = Optional.of(aplicacionSimulada);

        when(aplicacionRepository.findById(any())).thenReturn(optionalMok);

        Long resultado = aplicacionService.editarAplicacion(request, "usuario");
        assertEquals(1, resultado);
    }

    @Test
    void editarAplicacion_error_campoUnico() {
        MasivasRequestDTO request = crearRequest();

        TpAplicacion aplicacionSimulada = new TpAplicacion();
        aplicacionSimulada.setIdAplicacion(1L);
        aplicacionSimulada.setCodigo("APP_TEST");
        Optional<TpAplicacion> optionalMok = Optional.of(aplicacionSimulada);

        when(aplicacionRepository.findById(any())).thenReturn(optionalMok);

        ConstraintViolationException constraintEx = new ConstraintViolationException("", null, "");
        RollbackException rollbackEx = new RollbackException("");
        rollbackEx.initCause(constraintEx);
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(aplicacionRepository.save(any(TpAplicacion.class))).thenThrow(genericEx);

        UniqueFieldException thrown = assertThrows(UniqueFieldException.class, () -> {
            aplicacionService.editarAplicacion(request, "usuario");
        });

        assertEquals(ConstantesServices.CODIGO_ERROR_COD_APLICACION_UNICA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_COD_APLICACION_UNICA, thrown.getMessage());
    }

    @Test
    void editarAplicacion_error_noEncontrado() {
        MasivasRequestDTO request = crearRequest();

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(aplicacionRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            aplicacionService.editarAplicacion(request, "usuario");
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void editarAplicacion_excepcion_bd() {
        MasivasRequestDTO request = crearRequest();

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(aplicacionRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            aplicacionService.editarAplicacion(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void editarAplicacion_excepcion_bd2() {
        MasivasRequestDTO request = crearRequest();

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");
        when(aplicacionRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            aplicacionService.editarAplicacion(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void editarAplicacion_excepcion_generico() {
        MasivasRequestDTO request = crearRequest();

        RuntimeException genericEx = new RuntimeException("");
        when(aplicacionRepository.findById(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            aplicacionService.editarAplicacion(request, "usuario");
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void obtenerAplicacion_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdAplicacion(12l);

        TpAplicacion aplicacionSimulada = new TpAplicacion();
        aplicacionSimulada.setIdAplicacion(1L);
        aplicacionSimulada.setCodigo("APP_TEST");
        Optional<TpAplicacion> optionalMok = Optional.of(aplicacionSimulada);

        when(aplicacionRepository.findById(any())).thenReturn(optionalMok);

        DetalleRegistroAplicacionDTO resultado = aplicacionService.obtenerAplicacion(request);
        assertEquals(1l, resultado.getIdAplicacion());
    }

    @Test
    void obtenerAplicacion_error_noEncontrado() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdAplicacion(12l);

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(aplicacionRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            aplicacionService.obtenerAplicacion(request);
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void obtenerAplicacion_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdAplicacion(12l);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");
        when(aplicacionRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            aplicacionService.obtenerAplicacion(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void obtenerAplicacion_excepcion_generico() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdAplicacion(12l);

        RuntimeException genericEx = new RuntimeException("");
        when(aplicacionRepository.findById(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            aplicacionService.obtenerAplicacion(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void cambiaEstado_Exito() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        List<Long> listaMockeada = new ArrayList<>();
        listaMockeada.add(10L);
        listaMockeada.add(20L);
        request.setIdsOperacion(listaMockeada);

        TpAplicacion aplicacionSimulada = new TpAplicacion();
        aplicacionSimulada.setIdAplicacion(1L);
        aplicacionSimulada.setCodigo("APP_TEST");
        Optional<TpAplicacion> optionalMok = Optional.of(aplicacionSimulada);

        when(aplicacionRepository.findById(any())).thenReturn(optionalMok);

        EstadoDTO resultado = aplicacionService.cambiarEstadoAplicacion(request, "usuario", ConstantesServices.ESTADO_ACTIVO);
        assertNotNull(resultado);
    }

    @Test
    void cambiaEstado_error_noEncontrado() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        List<Long> listaMockeada = new ArrayList<>();
        listaMockeada.add(10L);
        listaMockeada.add(20L);
        request.setIdsOperacion(listaMockeada);

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(aplicacionRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            aplicacionService.cambiarEstadoAplicacion(request, "usuario", ConstantesServices.ESTADO_ACTIVO);
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void cambiaEstado_excepcion_bd() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        List<Long> listaMockeada = new ArrayList<>();
        listaMockeada.add(10L);
        listaMockeada.add(20L);
        request.setIdsOperacion(listaMockeada);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");
        when(aplicacionRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            aplicacionService.cambiarEstadoAplicacion(request, "usuario", ConstantesServices.ESTADO_ACTIVO);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void cambiaEstado_excepcion_bd2() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        List<Long> listaMockeada = new ArrayList<>();
        listaMockeada.add(10L);
        listaMockeada.add(20L);
        request.setIdsOperacion(listaMockeada);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(aplicacionRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            aplicacionService.cambiarEstadoAplicacion(request, "usuario", ConstantesServices.ESTADO_ACTIVO);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void cambiaEstado_excepcion_generico() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        List<Long> listaMockeada = new ArrayList<>();
        listaMockeada.add(10L);
        listaMockeada.add(20L);
        request.setIdsOperacion(listaMockeada);

        when(aplicacionRepository.findById(any())).thenReturn(null);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            aplicacionService.cambiarEstadoAplicacion(request, "usuario", ConstantesServices.ESTADO_ACTIVO);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void descargarAplicacion_pdf() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("App de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        TpAplicacion aplicacion = new TpAplicacion(1l, "COD001", "App de Prueba", ConstantesServices.ESTADO_ACTIVO, LocalDateTime.now(), "usuario");
        aplicacion.setIdAplicacion(1L);
        List<TpAplicacion> aplicacionesList = Collections.singletonList(aplicacion);
        Page<TpAplicacion> paginaSimulada = new PageImpl<>(aplicacionesList, Pageable.ofSize(10), 1);

        when(aplicacionRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        ReporteDTO resultado = aplicacionService.descargarAplicacion(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        assertNotNull(resultado);
    }

    @Test
    void descargarAplicacion_xlsx() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("App de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        TpAplicacion aplicacion = new TpAplicacion(1l, "COD001", "App de Prueba", ConstantesServices.ESTADO_ACTIVO, LocalDateTime.now(), "usuario");
        aplicacion.setIdAplicacion(1L);
        List<TpAplicacion> aplicacionesList = Collections.singletonList(aplicacion);
        Page<TpAplicacion> paginaSimulada = new PageImpl<>(aplicacionesList, Pageable.ofSize(10), 1);

        when(aplicacionRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        ReporteDTO resultado = aplicacionService.descargarAplicacion(request, "usuario", ConstantesServices.TIPO_ARCHIVO_XLSX);
        assertNotNull(resultado);
    }

    @Test
    void descargarAplicacion_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("App de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(aplicacionRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            aplicacionService.descargarAplicacion(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void descargarAplicacion_excepcion_generic() {
        GenericException thrown = assertThrows(GenericException.class, () -> {
            aplicacionService.descargarAplicacion(null, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof GenericException);
    }
}
