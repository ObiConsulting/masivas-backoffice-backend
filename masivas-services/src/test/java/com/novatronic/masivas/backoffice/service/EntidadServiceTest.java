package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaEntidadDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroEntidadDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpEntidad;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.exception.UniqueFieldException;
import com.novatronic.masivas.backoffice.repository.EntidadRepository;
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
public class EntidadServiceTest {

    @Mock
    private EntidadRepository entidadRepository;
    @InjectMocks
    private EntidadService entidadService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(entidadService, "logo", "logo.png");
    }

    private MasivasRequestDTO crearRequest() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        request.setCodigo("001");
        request.setNombre("Entidad Prueba");
        request.setEstado(ConstantesServices.ESTADO_INACTIVO);
        return request;
    }

    @Test
    void crearEntidad_exito() {
        MasivasRequestDTO request = crearRequest();
        when(entidadRepository.save(any(TpEntidad.class)))
                .thenAnswer(invocation -> {
                    TpEntidad entidadGuardada = invocation.getArgument(0);
                    ReflectionTestUtils.setField(entidadGuardada, "idEntidad", 123l);
                    return entidadGuardada;
                });
        Long resultado = entidadService.crearEntidad(request, "usuario");
        assertEquals(123, resultado);
    }

    @Test
    void crearEntidad_error_campoUnico() {
        MasivasRequestDTO request = crearRequest();

        ConstraintViolationException constraintEx = new ConstraintViolationException("", null, "");
        RollbackException rollbackEx = new RollbackException("");
        rollbackEx.initCause(constraintEx);
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(entidadRepository.save(any(TpEntidad.class))).thenThrow(genericEx);

        UniqueFieldException thrown = assertThrows(UniqueFieldException.class, () -> {
            entidadService.crearEntidad(request, "usuario");
        });

        assertEquals(ConstantesServices.CODIGO_ERROR_COD_ENTIDAD_UNICA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_COD_ENTIDAD_UNICA, thrown.getMessage());
    }

    @Test
    void crearEntidad_excepcion_bd() {
        MasivasRequestDTO request = crearRequest();

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(entidadRepository.save(any(TpEntidad.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            entidadService.crearEntidad(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void crearEntidad_excepcion_generic() {
        MasivasRequestDTO request = crearRequest();

        RuntimeException genericEx = new RuntimeException("");

        when(entidadRepository.save(any(TpEntidad.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            entidadService.crearEntidad(request, "usuario");
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void buscarEntidad_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("App de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        TpEntidad entidad = new TpEntidad("COD001", "Entidad de Prueba", ConstantesServices.ESTADO_ACTIVO, ConstantesServices.ID_PERFIL, ConstantesServices.NO_PROPIETARIO, 0l, 0l, LocalDateTime.now(), "usuario");
        entidad.setIdEntidad(1L);
        List<TpEntidad> entidadesList = Collections.singletonList(entidad);
        Page<TpEntidad> paginaSimulada = new PageImpl<>(entidadesList, Pageable.ofSize(10), 1);

        when(entidadRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        CustomPaginate<DetalleConsultaEntidadDTO> resultado = entidadService.buscarEntidad(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarEntidad_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("Entidad de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(entidadRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            entidadService.buscarEntidad(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void buscarEntidad_excepcion_generic() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("App de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RuntimeException genericEx = new RuntimeException("");

        when(entidadRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            entidadService.buscarEntidad(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void editarEntidad_exito() {
        MasivasRequestDTO request = crearRequest();

        TpEntidad entidadSimulada = new TpEntidad();
        entidadSimulada.setIdEntidad(1L);
        entidadSimulada.setCodigo("APP_TEST");
        Optional<TpEntidad> optionalMok = Optional.of(entidadSimulada);

        when(entidadRepository.findById(any())).thenReturn(optionalMok);

        Long resultado = entidadService.editarEntidad(request, "usuario");
        assertEquals(1, resultado);
    }

    @Test
    void editarEntidad_error_campoUnico() {
        MasivasRequestDTO request = crearRequest();

        TpEntidad entidadSimulada = new TpEntidad();
        entidadSimulada.setIdEntidad(1L);
        entidadSimulada.setCodigo("APP_TEST");
        Optional<TpEntidad> optionalMok = Optional.of(entidadSimulada);

        when(entidadRepository.findById(any())).thenReturn(optionalMok);

        ConstraintViolationException constraintEx = new ConstraintViolationException("", null, "");
        RollbackException rollbackEx = new RollbackException("");
        rollbackEx.initCause(constraintEx);
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(entidadRepository.save(any(TpEntidad.class))).thenThrow(genericEx);

        UniqueFieldException thrown = assertThrows(UniqueFieldException.class, () -> {
            entidadService.editarEntidad(request, "usuario");
        });

        assertEquals(ConstantesServices.CODIGO_ERROR_COD_ENTIDAD_UNICA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_COD_ENTIDAD_UNICA, thrown.getMessage());
    }

    @Test
    void editarEntidad_error_noEncontrado() {
        MasivasRequestDTO request = crearRequest();

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(entidadRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            entidadService.editarEntidad(request, "usuario");
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void editarEntidad_excepcion_bd() {
        MasivasRequestDTO request = crearRequest();

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(entidadRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            entidadService.editarEntidad(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void editarEntidad_excepcion_generico() {
        MasivasRequestDTO request = crearRequest();

        RuntimeException genericEx = new RuntimeException("");
        when(entidadRepository.findById(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            entidadService.editarEntidad(request, "usuario");
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void obtenerEntidad_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);

        TpEntidad entidadSimulada = new TpEntidad();
        entidadSimulada.setIdEntidad(1L);
        entidadSimulada.setCodigo("APP_TEST");
        Optional<TpEntidad> optionalMok = Optional.of(entidadSimulada);

        when(entidadRepository.findById(any())).thenReturn(optionalMok);

        DetalleRegistroEntidadDTO resultado = entidadService.obtenerEntidad(request);
        assertEquals(1l, resultado.getIdEntidad());
    }

    @Test
    void obtenerEntidad_error_noEncontrado() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(entidadRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            entidadService.obtenerEntidad(request);
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void obtenerEntidad_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(entidadRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            entidadService.obtenerEntidad(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void obtenerEntidad_excepcion_generico() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdEntidad(12l);

        RuntimeException genericEx = new RuntimeException("");
        when(entidadRepository.findById(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            entidadService.obtenerEntidad(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void descargarEntidad_pdf() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("App de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        TpEntidad entidad = new TpEntidad("COD001", "Entidad de Prueba", ConstantesServices.ESTADO_ACTIVO, ConstantesServices.ID_PERFIL, ConstantesServices.NO_PROPIETARIO, 0l, 0l, LocalDateTime.now(), "usuario");
        entidad.setIdEntidad(1L);
        List<TpEntidad> entidadesList = Collections.singletonList(entidad);
        Page<TpEntidad> paginaSimulada = new PageImpl<>(entidadesList, Pageable.ofSize(10), 1);

        when(entidadRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        ReporteDTO resultado = entidadService.descargarEntidades(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        assertNotNull(resultado);
    }

    @Test
    void descargarEntidad_xlsx() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("Entidad de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        TpEntidad entidad = new TpEntidad("COD001", "Entidad de Prueba", ConstantesServices.ESTADO_ACTIVO, ConstantesServices.ID_PERFIL, ConstantesServices.NO_PROPIETARIO, 0l, 0l, LocalDateTime.now(), "usuario");
        entidad.setIdEntidad(1L);
        List<TpEntidad> entidadesList = Collections.singletonList(entidad);
        Page<TpEntidad> paginaSimulada = new PageImpl<>(entidadesList, Pageable.ofSize(10), 1);

        when(entidadRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        ReporteDTO resultado = entidadService.descargarEntidades(request, "usuario", ConstantesServices.TIPO_ARCHIVO_XLSX);
        assertNotNull(resultado);
    }

    @Test
    void descargarEntidad_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodigo("COD001");
        request.setNombre("Entidad de Prueba");
        request.setEstado(ConstantesServices.ESTADO_ACTIVO);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(entidadRepository.buscarPorFiltros(any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            entidadService.descargarEntidades(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void descargarEntidad_excepcion_generic() {
        GenericException thrown = assertThrows(GenericException.class, () -> {
            entidadService.descargarEntidades(null, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof GenericException);
    }
}
