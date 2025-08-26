package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoDirectorioDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaArchivoTitularidadDTO;
import com.novatronic.masivas.backoffice.dto.ArchivoResponseDTO;
import com.novatronic.masivas.backoffice.dto.EjecutarResponseDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpArchivoDirectorio;
import com.novatronic.masivas.backoffice.entity.TpArchivoMasivas;
import com.novatronic.masivas.backoffice.entity.TpArchivoTitularidad;
import com.novatronic.masivas.backoffice.exception.ActionRestCoreException;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.repository.ArchivoDirectorioRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoMasivasRepository;
import com.novatronic.masivas.backoffice.repository.ArchivoTitularidadRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.transaction.RollbackException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
public class ArchivoServiceTest {

    @Mock
    private ArchivoDirectorioRepository archivoDirectorioRepository;
    @Mock
    private ArchivoMasivasRepository archivoMasivasRepository;
    @Mock
    private ArchivoTitularidadRepository archivoTitularidadRepository;
    @Mock
    private GenericService genericService;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ArchivoService archivoService;

    @BeforeEach
    void setUp() {
        archivoService = new ArchivoService(archivoDirectorioRepository, archivoMasivasRepository, archivoTitularidadRepository, genericService, restTemplate);
        ReflectionTestUtils.setField(archivoService, "logo", "logo.png");
        ReflectionTestUtils.setField(archivoService, "apiCoreUrl", "http://172.29.42.30:8408/gestor/tareas/ejecutar");
    }

    @Test
    void buscarArchivoDirectorio_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicio(LocalDate.now());
        request.setFechaFin(LocalDate.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        DetalleConsultaArchivoDirectorioDTO parametro = new DetalleConsultaArchivoDirectorioDTO();
        parametro.setIdArchivo(1L);
        List<DetalleConsultaArchivoDirectorioDTO> archivoList = Collections.singletonList(parametro);
        Page<DetalleConsultaArchivoDirectorioDTO> paginaSimulada = new PageImpl<>(archivoList, Pageable.ofSize(10), 1);

        when(archivoDirectorioRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        CustomPaginate<DetalleConsultaArchivoDirectorioDTO> resultado = archivoService.buscarArchivoDirectorio(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarArchivoDirectorio_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicio(LocalDate.now());
        request.setFechaFin(LocalDate.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(archivoDirectorioRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            archivoService.buscarArchivoDirectorio(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void buscarArchivoDirectorio_excepcion_generic() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicio(LocalDate.now());
        request.setFechaFin(LocalDate.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RuntimeException genericEx = new RuntimeException("");

        when(archivoDirectorioRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            archivoService.buscarArchivoDirectorio(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void buscarArchivoMasivas_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicioObtencion(LocalDateTime.now());
        request.setFechaFinObtencion(LocalDateTime.now());
        request.setFechaInicioProcesada(LocalDateTime.now());
        request.setFechaFinProcesada(LocalDateTime.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        DetalleConsultaArchivoMasivasDTO parametro = new DetalleConsultaArchivoMasivasDTO();
        parametro.setIdArchivo(1L);
        List<DetalleConsultaArchivoMasivasDTO> archivoList = Collections.singletonList(parametro);
        Page<DetalleConsultaArchivoMasivasDTO> paginaSimulada = new PageImpl<>(archivoList, Pageable.ofSize(10), 1);

        when(archivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        CustomPaginate<DetalleConsultaArchivoMasivasDTO> resultado = archivoService.buscarArchivoMasivas(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarArchivoMasivas_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicioObtencion(LocalDateTime.now());
        request.setFechaFinObtencion(LocalDateTime.now());
        request.setFechaInicioProcesada(LocalDateTime.now());
        request.setFechaFinProcesada(LocalDateTime.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(archivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            archivoService.buscarArchivoMasivas(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void buscarArchivoMasivas_excepcion_generic() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicioObtencion(LocalDateTime.now());
        request.setFechaFinObtencion(LocalDateTime.now());
        request.setFechaInicioProcesada(LocalDateTime.now());
        request.setFechaFinProcesada(LocalDateTime.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RuntimeException genericEx = new RuntimeException("");

        when(archivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            archivoService.buscarArchivoMasivas(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void buscarArchivoTitularidad_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicioObtencion(LocalDateTime.now());
        request.setFechaFinObtencion(LocalDateTime.now());
        request.setFechaInicioProcesada(LocalDateTime.now());
        request.setFechaFinProcesada(LocalDateTime.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        DetalleConsultaArchivoTitularidadDTO parametro = new DetalleConsultaArchivoTitularidadDTO();
        parametro.setIdArchivo(1L);
        List<DetalleConsultaArchivoTitularidadDTO> archivoList = Collections.singletonList(parametro);
        Page<DetalleConsultaArchivoTitularidadDTO> paginaSimulada = new PageImpl<>(archivoList, Pageable.ofSize(10), 1);

        when(archivoTitularidadRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        CustomPaginate<DetalleConsultaArchivoTitularidadDTO> resultado = archivoService.buscarArchivoTitularidad(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarArchivoTitularidad_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicioObtencion(LocalDateTime.now());
        request.setFechaFinObtencion(LocalDateTime.now());
        request.setFechaInicioProcesada(LocalDateTime.now());
        request.setFechaFinProcesada(LocalDateTime.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(archivoTitularidadRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            archivoService.buscarArchivoTitularidad(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void buscarArchivoTitularidad_excepcion_generic() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicioObtencion(LocalDateTime.now());
        request.setFechaFinObtencion(LocalDateTime.now());
        request.setFechaInicioProcesada(LocalDateTime.now());
        request.setFechaFinProcesada(LocalDateTime.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RuntimeException genericEx = new RuntimeException("");

        when(archivoTitularidadRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            archivoService.buscarArchivoTitularidad(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void descargarArchivoDirectorio() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicio(LocalDate.now());
        request.setFechaFin(LocalDate.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        DetalleConsultaArchivoDirectorioDTO parametro = new DetalleConsultaArchivoDirectorioDTO();
        parametro.setIdArchivo(1L);
        List<DetalleConsultaArchivoDirectorioDTO> archivoList = Collections.singletonList(parametro);
        Page<DetalleConsultaArchivoDirectorioDTO> paginaSimulada = new PageImpl<>(archivoList, Pageable.ofSize(10), 1);

        when(archivoDirectorioRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);
        Mockito.when(genericService.getNombreEstadoArchivo(any())).thenReturn("Enviado a Cliente");

        ReporteDTO resultado = archivoService.descargarArchivoDirectorio(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        assertNotNull(resultado);
    }

    @Test
    void descargarArchivoDirectorio_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicio(LocalDate.now());
        request.setFechaFin(LocalDate.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(archivoDirectorioRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            archivoService.descargarArchivoDirectorio(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void descargardescargarArchivoDirectorio_excepcion_generic() {
        GenericException thrown = assertThrows(GenericException.class, () -> {
            archivoService.descargarArchivoDirectorio(null, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void descargarArchivoMasivas() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicioObtencion(LocalDateTime.now());
        request.setFechaFinObtencion(LocalDateTime.now());
        request.setFechaInicioProcesada(LocalDateTime.now());
        request.setFechaFinProcesada(LocalDateTime.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        DetalleConsultaArchivoMasivasDTO parametro = new DetalleConsultaArchivoMasivasDTO();
        parametro.setIdArchivo(1L);
        List<DetalleConsultaArchivoMasivasDTO> archivoList = Collections.singletonList(parametro);
        Page<DetalleConsultaArchivoMasivasDTO> paginaSimulada = new PageImpl<>(archivoList, Pageable.ofSize(10), 1);

        when(archivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);
        Mockito.when(genericService.getNombreEstadoArchivo(any())).thenReturn("Enviado a Cliente");

        ReporteDTO resultado = archivoService.descargarArchivoMasivas(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        assertNotNull(resultado);
    }

    @Test
    void descargarArchivoMasivas_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicioObtencion(LocalDateTime.now());
        request.setFechaFinObtencion(LocalDateTime.now());
        request.setFechaInicioProcesada(LocalDateTime.now());
        request.setFechaFinProcesada(LocalDateTime.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(archivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            archivoService.descargarArchivoMasivas(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void descargardescargarArchivoMasivas_excepcion_generic() {
        GenericException thrown = assertThrows(GenericException.class, () -> {
            archivoService.descargarArchivoMasivas(null, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void descargarArchivoTitularidad() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicioObtencion(LocalDateTime.now());
        request.setFechaFinObtencion(LocalDateTime.now());
        request.setFechaInicioProcesada(LocalDateTime.now());
        request.setFechaFinProcesada(LocalDateTime.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        DetalleConsultaArchivoTitularidadDTO parametro = new DetalleConsultaArchivoTitularidadDTO();
        parametro.setIdArchivo(1L);
        List<DetalleConsultaArchivoTitularidadDTO> archivoList = Collections.singletonList(parametro);
        Page<DetalleConsultaArchivoTitularidadDTO> paginaSimulada = new PageImpl<>(archivoList, Pageable.ofSize(10), 1);

        when(archivoTitularidadRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);
        Mockito.when(genericService.getNombreEstadoArchivo(any())).thenReturn("Enviado a Cliente");

        ReporteDTO resultado = archivoService.descargarArchivoTitularidad(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        assertNotNull(resultado);
    }

    @Test
    void descargarArchivoTitularidad_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaInicioObtencion(LocalDateTime.now());
        request.setFechaFinObtencion(LocalDateTime.now());
        request.setFechaInicioProcesada(LocalDateTime.now());
        request.setFechaFinProcesada(LocalDateTime.now());
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(archivoTitularidadRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            archivoService.descargarArchivoTitularidad(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void descargardescargarArchivoTitularidad_excepcion_generic() {
        GenericException thrown = assertThrows(GenericException.class, () -> {
            archivoService.descargarArchivoTitularidad(null, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void respaldarDirectorio() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        TpArchivoDirectorio directorio = new TpArchivoDirectorio();
        directorio.setNombre("ArchivoTest");
        directorio.setTrace(123456l);
        directorio.setIdArchivo(1l);
        Optional<TpArchivoDirectorio> optionalMok = Optional.of(directorio);

        when(archivoDirectorioRepository.findById(any())).thenReturn(optionalMok);

        ArchivoResponseDTO respuestaSimulada = new ArchivoResponseDTO();
        respuestaSimulada.setEstado(1L);
        respuestaSimulada.setFechaProceso(LocalDateTime.now());
        List<ArchivoResponseDTO> listaRespuesta = Collections.singletonList(respuestaSimulada);

        EjecutarResponseDTO response = new EjecutarResponseDTO();
        response.setArchivos(listaRespuesta);
        response.setCodigoRespuesta(ConstantesServices.CODIGO_OK_WS);

        ResponseEntity<EjecutarResponseDTO> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        Long resultado = archivoService.gestionarOperacionDirectorio(request);
        assertEquals(1l, resultado);
    }

    @Test
    void restaurarDirectorio() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESTAURAR);

        TpArchivoDirectorio directorio = new TpArchivoDirectorio();
        directorio.setNombre("ArchivoTest");
        directorio.setTrace(123456l);
        directorio.setIdArchivo(1l);
        Optional<TpArchivoDirectorio> optionalMok = Optional.of(directorio);

        when(archivoDirectorioRepository.findById(any())).thenReturn(optionalMok);

        ArchivoResponseDTO respuestaSimulada = new ArchivoResponseDTO();
        respuestaSimulada.setEstado(1L);
        respuestaSimulada.setFechaProceso(LocalDateTime.now());
        List<ArchivoResponseDTO> listaRespuesta = Collections.singletonList(respuestaSimulada);

        EjecutarResponseDTO response = new EjecutarResponseDTO();
        response.setArchivos(listaRespuesta);
        response.setCodigoRespuesta(ConstantesServices.CODIGO_OK_WS);

        ResponseEntity<EjecutarResponseDTO> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        Long resultado = archivoService.gestionarOperacionDirectorio(request);
        assertEquals(1l, resultado);
    }

    @Test
    void respaldarDirectorio_errorNoOperacion() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion("07");
        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            archivoService.gestionarOperacionDirectorio(request);
        });
        assertTrue(thrown instanceof NoOperationExistsException);
    }

    @Test
    void respaldarDirectorio_errorNoOperacion_2() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        Optional<TpArchivoDirectorio> optionalMok = Optional.empty();

        when(archivoDirectorioRepository.findById(any())).thenReturn(optionalMok);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            archivoService.gestionarOperacionDirectorio(request);
        });
        assertTrue(thrown instanceof NoOperationExistsException);
    }

    @Test
    void respaldarDirectorio_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        InvalidDataAccessResourceUsageException genericEx = new InvalidDataAccessResourceUsageException("");

        when(archivoDirectorioRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            archivoService.gestionarOperacionDirectorio(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void respaldarDirectorio_excepcion_generic() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        when(archivoDirectorioRepository.findById(any())).thenReturn(null);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            archivoService.gestionarOperacionDirectorio(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void restaurarDirectorio_excepcion_body_null() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESTAURAR);

        TpArchivoDirectorio directorio = new TpArchivoDirectorio();
        directorio.setNombre("ArchivoTest");
        directorio.setTrace(123456l);
        directorio.setIdArchivo(1l);
        Optional<TpArchivoDirectorio> optionalMok = Optional.of(directorio);

        when(archivoDirectorioRepository.findById(any())).thenReturn(optionalMok);

        ResponseEntity<EjecutarResponseDTO> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        ActionRestCoreException thrown = assertThrows(ActionRestCoreException.class, () -> {
            archivoService.gestionarOperacionDirectorio(request);
        });
        assertTrue(thrown instanceof ActionRestCoreException);
    }

    @Test
    void restaurarDirectorio_error_response() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESTAURAR);

        TpArchivoDirectorio directorio = new TpArchivoDirectorio();
        directorio.setNombre("ArchivoTest");
        directorio.setTrace(123456l);
        directorio.setIdArchivo(1l);
        Optional<TpArchivoDirectorio> optionalMok = Optional.of(directorio);

        when(archivoDirectorioRepository.findById(any())).thenReturn(optionalMok);

        ArchivoResponseDTO respuestaSimulada = new ArchivoResponseDTO();
        respuestaSimulada.setEstado(null);
        respuestaSimulada.setFechaProceso(LocalDateTime.now());

        List<ArchivoResponseDTO> listaRespuesta = Collections.singletonList(respuestaSimulada);

        EjecutarResponseDTO response = new EjecutarResponseDTO();
        response.setCodigoRespuesta("9999");
        response.setArchivos(listaRespuesta);

        ResponseEntity<EjecutarResponseDTO> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        ActionRestCoreException thrown = assertThrows(ActionRestCoreException.class, () -> {
            archivoService.gestionarOperacionDirectorio(request);
        });
        assertTrue(thrown instanceof ActionRestCoreException);
    }

    @Test
    void respaldarMasivas() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        TpArchivoMasivas masivas = new TpArchivoMasivas();
        masivas.setNombre("ArchivoTest");
        masivas.setTrace(123456l);
        masivas.setIdArchivo(1l);
        Optional<TpArchivoMasivas> optionalMok = Optional.of(masivas);

        when(archivoMasivasRepository.findById(any())).thenReturn(optionalMok);

        ArchivoResponseDTO respuestaSimulada = new ArchivoResponseDTO();
        respuestaSimulada.setEstado(1L);
        respuestaSimulada.setFechaProceso(LocalDateTime.now());
        List<ArchivoResponseDTO> listaRespuesta = Collections.singletonList(respuestaSimulada);

        EjecutarResponseDTO response = new EjecutarResponseDTO();
        response.setArchivos(listaRespuesta);
        response.setCodigoRespuesta(ConstantesServices.CODIGO_OK_WS);

        ResponseEntity<EjecutarResponseDTO> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        Long resultado = archivoService.gestionarOperacionMasivas(request);
        assertEquals(1l, resultado);
    }

    @Test
    void restaurarMasivas() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESTAURAR);

        TpArchivoMasivas masivas = new TpArchivoMasivas();
        masivas.setNombre("ArchivoTest");
        masivas.setTrace(123456l);
        masivas.setIdArchivo(1l);
        Optional<TpArchivoMasivas> optionalMok = Optional.of(masivas);

        when(archivoMasivasRepository.findById(any())).thenReturn(optionalMok);

        ArchivoResponseDTO respuestaSimulada = new ArchivoResponseDTO();
        respuestaSimulada.setEstado(1L);
        respuestaSimulada.setFechaProceso(LocalDateTime.now());
        List<ArchivoResponseDTO> listaRespuesta = Collections.singletonList(respuestaSimulada);

        EjecutarResponseDTO response = new EjecutarResponseDTO();
        response.setArchivos(listaRespuesta);
        response.setCodigoRespuesta(ConstantesServices.CODIGO_OK_WS);

        ResponseEntity<EjecutarResponseDTO> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        Long resultado = archivoService.gestionarOperacionMasivas(request);
        assertEquals(1l, resultado);
    }

    @Test
    void respaldarMasivas_errorNoOperacion() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion("07");
        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            archivoService.gestionarOperacionMasivas(request);
        });
        assertTrue(thrown instanceof NoOperationExistsException);
    }

    @Test
    void respaldarMasivas_errorNoOperacion_2() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        Optional<TpArchivoMasivas> optionalMok = Optional.empty();

        when(archivoMasivasRepository.findById(any())).thenReturn(optionalMok);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            archivoService.gestionarOperacionMasivas(request);
        });
        assertTrue(thrown instanceof NoOperationExistsException);
    }

    @Test
    void respaldarMasivas_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(archivoMasivasRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            archivoService.gestionarOperacionMasivas(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void respaldarMasivas_excepcion_generic() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        when(archivoMasivasRepository.findById(any())).thenReturn(null);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            archivoService.gestionarOperacionMasivas(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void respaldarTitularidad() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        TpArchivoTitularidad titularidad = new TpArchivoTitularidad();
        titularidad.setNombre("ArchivoTest");
        titularidad.setTrace(123456l);
        titularidad.setIdArchivo(1l);
        Optional<TpArchivoTitularidad> optionalMok = Optional.of(titularidad);

        when(archivoTitularidadRepository.findById(any())).thenReturn(optionalMok);

        ArchivoResponseDTO respuestaSimulada = new ArchivoResponseDTO();
        respuestaSimulada.setEstado(1L);
        respuestaSimulada.setFechaProceso(LocalDateTime.now());
        List<ArchivoResponseDTO> listaRespuesta = Collections.singletonList(respuestaSimulada);

        EjecutarResponseDTO response = new EjecutarResponseDTO();
        response.setArchivos(listaRespuesta);
        response.setCodigoRespuesta(ConstantesServices.CODIGO_OK_WS);

        ResponseEntity<EjecutarResponseDTO> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        Long resultado = archivoService.gestionarOperacionTitularidad(request);
        assertEquals(1l, resultado);
    }

    @Test
    void restaurarTitularidad() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESTAURAR);

        TpArchivoTitularidad titularidad = new TpArchivoTitularidad();
        titularidad.setNombre("ArchivoTest");
        titularidad.setTrace(123456l);
        titularidad.setIdArchivo(1l);
        Optional<TpArchivoTitularidad> optionalMok = Optional.of(titularidad);

        when(archivoTitularidadRepository.findById(any())).thenReturn(optionalMok);

        ArchivoResponseDTO respuestaSimulada = new ArchivoResponseDTO();
        respuestaSimulada.setEstado(1L);
        respuestaSimulada.setFechaProceso(LocalDateTime.now());
        List<ArchivoResponseDTO> listaRespuesta = Collections.singletonList(respuestaSimulada);

        EjecutarResponseDTO response = new EjecutarResponseDTO();
        response.setArchivos(listaRespuesta);
        response.setCodigoRespuesta(ConstantesServices.CODIGO_OK_WS);

        ResponseEntity<EjecutarResponseDTO> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        Long resultado = archivoService.gestionarOperacionTitularidad(request);
        assertEquals(1l, resultado);
    }

    @Test
    void respaldarTitularidad_errorNoOperacion() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion("07");
        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            archivoService.gestionarOperacionTitularidad(request);
        });
        assertTrue(thrown instanceof NoOperationExistsException);
    }

    @Test
    void respaldarTitularidad_errorNoOperacion_2() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        Optional<TpArchivoTitularidad> optionalMok = Optional.empty();

        when(archivoTitularidadRepository.findById(any())).thenReturn(optionalMok);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            archivoService.gestionarOperacionTitularidad(request);
        });
        assertTrue(thrown instanceof NoOperationExistsException);
    }

    @Test
    void respaldarTitularidad_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(archivoTitularidadRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            archivoService.gestionarOperacionTitularidad(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void respaldarTitularidad_excepcion_generic() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);
        request.setTipoAccion(ConstantesServices.TIPO_ACCION_RESPALDAR);

        when(archivoTitularidadRepository.findById(any())).thenReturn(null);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            archivoService.gestionarOperacionTitularidad(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

}
