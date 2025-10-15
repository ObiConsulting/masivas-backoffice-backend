package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaParametroDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpParametro;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import com.novatronic.masivas.backoffice.repository.ParametroRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.transaction.RollbackException;
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
import org.mockito.Mockito;
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
public class ParametroServiceTest {

    @Mock
    private ParametroRepository parametroRepository;
    @Mock
    private ParametroCacheService parametroCacheService;
    @Mock
    private GenericService genericService;
    @Mock
    private CoreService coreService;
    @InjectMocks
    private ParametroService parametroService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(parametroService, "logo", "logo.png");
    }

    private MasivasRequestDTO crearRequest() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        request.setIdParametro(123l);
        request.setCodigo("1234");
        request.setNombre("Parametro Prueba");
        request.setEstado(ConstantesServices.ESTADO_INACTIVO);
        return request;
    }

    @Test
    void crearParametro_exito() {
        MasivasRequestDTO request = crearRequest();
        when(parametroRepository.save(any(TpParametro.class)))
                .thenAnswer(invocation -> {
                    TpParametro parametroGuardado = invocation.getArgument(0);
                    ReflectionTestUtils.setField(parametroGuardado, "idParametro", 123l);
                    return parametroGuardado;
                });
        Long resultado = parametroService.crearParametro(request, "usuario");
        assertEquals(123, resultado);
    }

    @Test
    void crearParametro_error_campoUnico() {
        MasivasRequestDTO request = crearRequest();

        ConstraintViolationException constraintEx = new ConstraintViolationException("", null, "");
        RollbackException rollbackEx = new RollbackException("");
        rollbackEx.initCause(constraintEx);
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(parametroRepository.save(any(TpParametro.class))).thenThrow(genericEx);

        UniqueFieldException thrown = assertThrows(UniqueFieldException.class, () -> {
            parametroService.crearParametro(request, "usuario");
        });

        assertEquals(ConstantesServices.CODIGO_ERROR_COD_PARAMETRO_GRUPO_PARAMETRO_UNICO, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_COD_PARAMETRO_GRUPO_PARAMETRO_UNICO, thrown.getMessage());
    }

    @Test
    void crearParametro_excepcion_bd() {
        MasivasRequestDTO request = crearRequest();

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(parametroRepository.save(any(TpParametro.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            parametroService.crearParametro(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void crearParametro_excepcion_generic() {
        MasivasRequestDTO request = crearRequest();

        RuntimeException genericEx = new RuntimeException("");

        when(parametroRepository.save(any(TpParametro.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            parametroService.crearParametro(request, "usuario");
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void buscarParametro_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        DetalleConsultaParametroDTO parametro = new DetalleConsultaParametroDTO();
        parametro.setIdParametro(1L);
        List<DetalleConsultaParametroDTO> parametrosList = Collections.singletonList(parametro);
        Page<DetalleConsultaParametroDTO> paginaSimulada = new PageImpl<>(parametrosList, Pageable.ofSize(10), 1);

        when(parametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        CustomPaginate<DetalleConsultaParametroDTO> resultado = parametroService.buscarParametro(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarParametro_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(parametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            parametroService.buscarParametro(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void buscarParametro_excepcion_generic() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RuntimeException genericEx = new RuntimeException("");

        when(parametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            parametroService.buscarParametro(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void editarParametro_exito() {
        MasivasRequestDTO request = crearRequest();

        TpParametro parametro = new TpParametro();
        parametro.setIdParametro(1L);
        parametro.setCodigo("PARAM_TEST");
        Optional<TpParametro> optionalMok = Optional.of(parametro);

        when(parametroRepository.findById(any())).thenReturn(optionalMok);

        Long resultado = parametroService.editarParametro(request, "usuario");
        assertEquals(1, resultado);
    }

    @Test
    void editarParametro_error_campoUnico() {
        MasivasRequestDTO request = crearRequest();

        TpParametro parametro = new TpParametro();
        parametro.setIdParametro(1L);
        parametro.setCodigo("PARAM_TEST");
        Optional<TpParametro> optionalMok = Optional.of(parametro);

        when(parametroRepository.findById(any())).thenReturn(optionalMok);

        ConstraintViolationException constraintEx = new ConstraintViolationException("", null, "");
        RollbackException rollbackEx = new RollbackException("");
        rollbackEx.initCause(constraintEx);
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(parametroRepository.save(any(TpParametro.class))).thenThrow(genericEx);

        UniqueFieldException thrown = assertThrows(UniqueFieldException.class, () -> {
            parametroService.editarParametro(request, "usuario");
        });

        assertEquals(ConstantesServices.CODIGO_ERROR_COD_PARAMETRO_GRUPO_PARAMETRO_UNICO, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_COD_PARAMETRO_GRUPO_PARAMETRO_UNICO, thrown.getMessage());
    }

    @Test
    void editarParametro_error_noEncontrado() {
        MasivasRequestDTO request = crearRequest();

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(parametroRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            parametroService.editarParametro(request, "usuario");
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void editarParametro_excepcion_bd() {
        MasivasRequestDTO request = crearRequest();

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(parametroRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            parametroService.editarParametro(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void editarParametro_excepcion_bd2() {
        MasivasRequestDTO request = crearRequest();

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");
        when(parametroRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            parametroService.editarParametro(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void editarParametro_excepcion_generico() {
        MasivasRequestDTO request = crearRequest();

        RuntimeException genericEx = new RuntimeException("");
        when(parametroRepository.findById(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            parametroService.editarParametro(request, "usuario");
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void obtenerParametro_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdParametro(12l);

        TpParametro parametro = new TpParametro();
        parametro.setIdParametro(1L);
        parametro.setCodigo("PARAM_TEST");
        Optional<TpParametro> optionalMok = Optional.of(parametro);

        when(parametroRepository.findById(any())).thenReturn(optionalMok);

        DetalleRegistroParametroDTO resultado = parametroService.obtenerParametro(request);
        assertEquals(1l, resultado.getIdParametro());
    }

    @Test
    void obtenerParametro_error_noEncontrado() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdParametro(12l);

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(parametroRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            parametroService.obtenerParametro(request);
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void obtenerParametro_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdParametro(12l);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");
        when(parametroRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            parametroService.obtenerParametro(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void obtenerParametro_excepcion_generico() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdParametro(12l);

        RuntimeException genericEx = new RuntimeException("");
        when(parametroRepository.findById(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            parametroService.obtenerParametro(request);
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

        TpParametro aplicacionSimulada = new TpParametro();
        aplicacionSimulada.setIdParametro(1L);
        aplicacionSimulada.setCodigo("APP_TEST");
        Optional<TpParametro> optionalMok = Optional.of(aplicacionSimulada);

        when(parametroRepository.findById(any())).thenReturn(optionalMok);

        EstadoDTO resultado = parametroService.cambiarEstadoParametro(request, "usuario", ConstantesServices.ESTADO_ACTIVO);
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

        when(parametroRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            parametroService.cambiarEstadoParametro(request, "usuario", ConstantesServices.ESTADO_ACTIVO);
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
        when(parametroRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            parametroService.cambiarEstadoParametro(request, "usuario", ConstantesServices.ESTADO_ACTIVO);
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
        when(parametroRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            parametroService.cambiarEstadoParametro(request, "usuario", ConstantesServices.ESTADO_ACTIVO);
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

        when(parametroRepository.findById(any())).thenReturn(null);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            parametroService.cambiarEstadoParametro(request, "usuario", ConstantesServices.ESTADO_ACTIVO);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void descargarParametro_pdf() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        DetalleConsultaParametroDTO parametro = new DetalleConsultaParametroDTO();
        parametro.setIdParametro(1L);
        List<DetalleConsultaParametroDTO> parametroList = Collections.singletonList(parametro);
        Page<DetalleConsultaParametroDTO> paginaSimulada = new PageImpl<>(parametroList, Pageable.ofSize(10), 1);

        when(parametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);
        Mockito.when(genericService.getNombreGrupoParametro(any())).thenReturn("Grupo 1234");

        ReporteDTO resultado = parametroService.descargarParametro(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        assertNotNull(resultado);
    }

    @Test
    void descargarParametro_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(parametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            parametroService.descargarParametro(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void descargarParametro_excepcion_generic() {
        GenericException thrown = assertThrows(GenericException.class, () -> {
            parametroService.descargarParametro(null, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof GenericException);
    }
}
