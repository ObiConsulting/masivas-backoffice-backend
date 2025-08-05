package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroGrupoParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpGrupoParametro;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import com.novatronic.masivas.backoffice.repository.GrupoParametroRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.transaction.RollbackException;
import java.time.LocalDateTime;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
public class GrupoParametroServiceTest {

    @Mock
    private GrupoParametroRepository grupoParametroRepository;
    @Mock
    private ParametroCacheService parametroCacheService;
    @InjectMocks
    private GrupoParametroService grupoParametroService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(grupoParametroService, "logo", "logo.png");
    }

    private MasivasRequestDTO crearRequest() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        request.setCodigo("1234");
        request.setNombre("Grupo Parametro Prueba");
        request.setEstado(ConstantesServices.ESTADO_INACTIVO);
        return request;
    }

    @Test
    void crearGrupoParametro_exito() {
        MasivasRequestDTO request = crearRequest();
        when(grupoParametroRepository.save(any(TpGrupoParametro.class)))
                .thenAnswer(invocation -> {
                    TpGrupoParametro grupoGuardado = invocation.getArgument(0);
                    ReflectionTestUtils.setField(grupoGuardado, "idGrupoParametro", 123l);
                    return grupoGuardado;
                });
//        when(parametroCacheService.loadParametersGroupInCache());
        Long resultado = grupoParametroService.crearGrupoParametro(request, "usuario");
        assertEquals(123, resultado);
    }

    @Test
    void crearGrupoParametro_error_campoUnico() {
        MasivasRequestDTO request = crearRequest();

        ConstraintViolationException constraintEx = new ConstraintViolationException("", null, "");
        RollbackException rollbackEx = new RollbackException("");
        rollbackEx.initCause(constraintEx);
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(grupoParametroRepository.save(any(TpGrupoParametro.class))).thenThrow(genericEx);

        UniqueFieldException thrown = assertThrows(UniqueFieldException.class, () -> {
            grupoParametroService.crearGrupoParametro(request, "usuario");
        });

        assertEquals(ConstantesServices.CODIGO_ERROR_COD_GRUPO_PARAMETRO_UNICO, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_COD_GRUPO_PARAMETRO_UNICO, thrown.getMessage());
    }

    @Test
    void crearGrupoParametro_excepcion_bd() {
        MasivasRequestDTO request = crearRequest();

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(grupoParametroRepository.save(any(TpGrupoParametro.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            grupoParametroService.crearGrupoParametro(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void crearGrupoParametro_excepcion_generic() {
        MasivasRequestDTO request = crearRequest();

        RuntimeException genericEx = new RuntimeException("");

        when(grupoParametroRepository.save(any(TpGrupoParametro.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            grupoParametroService.crearGrupoParametro(request, "usuario");
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void buscarGrupoParametro_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        TpGrupoParametro aplicacion = new TpGrupoParametro("COD001", "Grupo de Prueba", ConstantesServices.ESTADO_ACTIVO, LocalDateTime.now(), "usuario");
        aplicacion.setIdGrupoParametro(1L);
        List<TpGrupoParametro> gruposList = Collections.singletonList(aplicacion);
        Page<TpGrupoParametro> paginaSimulada = new PageImpl<>(gruposList, Pageable.ofSize(10), 1);

        when(grupoParametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        CustomPaginate<DetalleConsultaGrupoParametroDTO> resultado = grupoParametroService.buscarGrupoParametro(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarGrupoParametro_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(grupoParametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            grupoParametroService.buscarGrupoParametro(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void buscarGrupoParametro_excepcion_generic() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RuntimeException genericEx = new RuntimeException("");

        when(grupoParametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            grupoParametroService.buscarGrupoParametro(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void editarGrupoParametro_exito() {
        MasivasRequestDTO request = crearRequest();

        TpGrupoParametro aplicacionSimulada = new TpGrupoParametro();
        aplicacionSimulada.setIdGrupoParametro(1L);
        aplicacionSimulada.setCodigo("GRUPO_TEST");
        Optional<TpGrupoParametro> optionalMok = Optional.of(aplicacionSimulada);

        when(grupoParametroRepository.findById(any())).thenReturn(optionalMok);

        Long resultado = grupoParametroService.editarGrupoParametro(request, "usuario");
        assertEquals(1, resultado);
    }

    @Test
    void editarGrupoParametro_error_campoUnico() {
        MasivasRequestDTO request = crearRequest();

        TpGrupoParametro aplicacionSimulada = new TpGrupoParametro();
        aplicacionSimulada.setIdGrupoParametro(1L);
        aplicacionSimulada.setCodigo("GRUPO_TEST");
        Optional<TpGrupoParametro> optionalMok = Optional.of(aplicacionSimulada);

        when(grupoParametroRepository.findById(any())).thenReturn(optionalMok);

        ConstraintViolationException constraintEx = new ConstraintViolationException("", null, "");
        RollbackException rollbackEx = new RollbackException("");
        rollbackEx.initCause(constraintEx);
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(grupoParametroRepository.save(any(TpGrupoParametro.class))).thenThrow(genericEx);

        UniqueFieldException thrown = assertThrows(UniqueFieldException.class, () -> {
            grupoParametroService.editarGrupoParametro(request, "usuario");
        });

        assertEquals(ConstantesServices.CODIGO_ERROR_COD_GRUPO_PARAMETRO_UNICO, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_COD_GRUPO_PARAMETRO_UNICO, thrown.getMessage());
    }

    @Test
    void editarGrupoParametro_error_noEncontrado() {
        MasivasRequestDTO request = crearRequest();

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(grupoParametroRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            grupoParametroService.editarGrupoParametro(request, "usuario");
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void editarGrupoParametro_excepcion_bd() {
        MasivasRequestDTO request = crearRequest();

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(grupoParametroRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            grupoParametroService.editarGrupoParametro(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void editarGrupoParametro_excepcion_generico() {
        MasivasRequestDTO request = crearRequest();

        RuntimeException genericEx = new RuntimeException("");
        when(grupoParametroRepository.findById(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            grupoParametroService.editarGrupoParametro(request, "usuario");
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void obtenerGrupoParametro_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdGrupoParametro(12l);

        TpGrupoParametro aplicacionSimulada = new TpGrupoParametro();
        aplicacionSimulada.setIdGrupoParametro(1L);
        aplicacionSimulada.setCodigo("GRUPO_TEST");
        Optional<TpGrupoParametro> optionalMok = Optional.of(aplicacionSimulada);

        when(grupoParametroRepository.findById(any())).thenReturn(optionalMok);

        DetalleRegistroGrupoParametroDTO resultado = grupoParametroService.obtenerGrupoParametro(request);
        assertEquals(1l, resultado.getIdGrupoParametro());
    }

    @Test
    void obtenerGrupoParametro_error_noEncontrado() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdGrupoParametro(12l);

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(grupoParametroRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            grupoParametroService.obtenerGrupoParametro(request);
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void obtenerGrupoParametro_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdGrupoParametro(12l);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(grupoParametroRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            grupoParametroService.obtenerGrupoParametro(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void obtenerGrupoParametro_excepcion_generico() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdGrupoParametro(12l);

        RuntimeException genericEx = new RuntimeException("");
        when(grupoParametroRepository.findById(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            grupoParametroService.obtenerGrupoParametro(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void descargarGrupoParametro_pdf() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        TpGrupoParametro aplicacion = new TpGrupoParametro("COD001", "Grupo de Prueba", ConstantesServices.ESTADO_ACTIVO, LocalDateTime.now(), "usuario");
        aplicacion.setIdGrupoParametro(1L);
        List<TpGrupoParametro> aplicacionesList = Collections.singletonList(aplicacion);
        Page<TpGrupoParametro> paginaSimulada = new PageImpl<>(aplicacionesList, Pageable.ofSize(10), 1);

        when(grupoParametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        ReporteDTO resultado = grupoParametroService.descargarGrupoParametro(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        assertNotNull(resultado);
    }

    @Test
    void descargarGrupoParametro_xlsx() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        TpGrupoParametro aplicacion = new TpGrupoParametro("COD001", "Grupo de Prueba", ConstantesServices.ESTADO_ACTIVO, LocalDateTime.now(), "usuario");
        aplicacion.setIdGrupoParametro(1L);
        List<TpGrupoParametro> aplicacionesList = Collections.singletonList(aplicacion);
        Page<TpGrupoParametro> paginaSimulada = new PageImpl<>(aplicacionesList, Pageable.ofSize(10), 1);

        when(grupoParametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        ReporteDTO resultado = grupoParametroService.descargarGrupoParametro(request, "usuario", ConstantesServices.TIPO_ARCHIVO_XLSX);
        assertNotNull(resultado);
    }

    @Test
    void descargarGrupoParametro_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("1593");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(grupoParametroRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            grupoParametroService.descargarGrupoParametro(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void descargarGrupoParametro_excepcion_generic() {
        GenericException thrown = assertThrows(GenericException.class, () -> {
            grupoParametroService.descargarGrupoParametro(null, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof GenericException);
    }
}
