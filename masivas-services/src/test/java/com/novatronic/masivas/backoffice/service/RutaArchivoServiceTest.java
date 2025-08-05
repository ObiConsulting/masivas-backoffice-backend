package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaRutaDTO;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroRutaDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.entity.TpRuta;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.exception.NoOperationExistsException;
import com.novatronic.masivas.backoffice.repository.RutaRepository;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.transaction.RollbackException;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
public class RutaArchivoServiceTest {

    @Mock
    private RutaRepository rutaArchivoRepository;
    @Mock
    private GenericService genericService;
    @InjectMocks
    private RutaService rutaArchivoService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rutaArchivoService, "logo", "logo.png");
    }

    private MasivasRequestDTO crearRequest() {
        MasivasRequestDTO request = new MasivasRequestDTO();
        request.setIdRuta(123l);
        request.setRuta("/tmp/masivas/obtener");
        request.setEstado(ConstantesServices.ESTADO_INACTIVO);
        return request;
    }

    @Test
    void buscarRutaArchivo_exito() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodTipoArchivo("DIR");
        request.setCodCategoriaDirectorio("1000");

        DetalleConsultaRutaDTO ruta = new DetalleConsultaRutaDTO();
        ruta.setIdRuta(1L);
        List<DetalleConsultaRutaDTO> rutaList = Collections.singletonList(ruta);
        Page<DetalleConsultaRutaDTO> paginaSimulada = new PageImpl<>(rutaList, Pageable.ofSize(10), 1);

        when(rutaArchivoRepository.buscarPorFiltros(any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        CustomPaginate<DetalleConsultaRutaDTO> resultado = rutaArchivoService.buscarRuta(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarRutaArchivo_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodTipoArchivo("DIR");
        request.setCodCategoriaDirectorio("1000");

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(rutaArchivoRepository.buscarPorFiltros(any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            rutaArchivoService.buscarRuta(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void buscarRutaArchivo_excepcion_generic() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodTipoArchivo("DIR");
        request.setCodCategoriaDirectorio("1000");

        RuntimeException genericEx = new RuntimeException("");

        when(rutaArchivoRepository.buscarPorFiltros(any(), any(), any(Pageable.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            rutaArchivoService.buscarRuta(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void editarRutaArchivo_exito() {
        MasivasRequestDTO request = crearRequest();

        TpRuta rutaSimulada = new TpRuta();
        rutaSimulada.setIdRuta(1L);
        Optional<TpRuta> optionalMok = Optional.of(rutaSimulada);

        when(rutaArchivoRepository.findById(any())).thenReturn(optionalMok);

        Long resultado = rutaArchivoService.editarRuta(request, "usuario");
        assertEquals(1, resultado);
    }

    @Test
    void editarRutaArchivo_error_noEncontrado() {
        MasivasRequestDTO request = crearRequest();

        NoOperationExistsException noOperationExistsException = new NoOperationExistsException(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA);

        when(rutaArchivoRepository.findById(any())).thenThrow(noOperationExistsException);

        NoOperationExistsException thrown = assertThrows(NoOperationExistsException.class, () -> {
            rutaArchivoService.editarRuta(request, "usuario");
        });
        assertEquals(ConstantesServices.CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA, thrown.getErrorCode());
        assertEquals(ConstantesServices.MENSAJE_ERROR_OPERACION_NO_ENCONTRADA, thrown.getMessage());
    }

    @Test
    void editarRutaArchivo_excepcion_bd() {
        MasivasRequestDTO request = crearRequest();

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(rutaArchivoRepository.findById(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            rutaArchivoService.editarRuta(request, "usuario");
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void editarRutaArchivo_excepcion_generico() {
        MasivasRequestDTO request = crearRequest();

        RuntimeException genericEx = new RuntimeException("");
        when(rutaArchivoRepository.findById(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            rutaArchivoService.editarRuta(request, "usuario");
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void obtenerRutaArchivo_exito() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdRuta(1l);

        DetalleRegistroRutaDTO rutaSimulada = new DetalleRegistroRutaDTO();
        rutaSimulada.setIdRuta(1L);

        when(rutaArchivoRepository.buscarPorId(any())).thenReturn(rutaSimulada);

        DetalleRegistroRutaDTO resultado = rutaArchivoService.obtenerRuta(request);
        assertEquals(1l, resultado.getIdRuta());
    }

    @Test
    void obtenerRutaArchivo_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdRuta(1l);

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);
        when(rutaArchivoRepository.buscarPorId(any())).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            rutaArchivoService.obtenerRuta(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void obtenerRutaArchivo_excepcion_generico() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setIdRuta(1l);

        RuntimeException genericEx = new RuntimeException("");
        when(rutaArchivoRepository.buscarPorId(any())).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            rutaArchivoService.obtenerRuta(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void descargarRutaArchivo_pdf() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodTipoArchivo("DIR");
        request.setCodCategoriaDirectorio("1000");

        DetalleConsultaRutaDTO ruta = new DetalleConsultaRutaDTO();
        ruta.setIdRuta(1L);
        List<DetalleConsultaRutaDTO> rutaList = Collections.singletonList(ruta);
        Page<DetalleConsultaRutaDTO> paginaSimulada = new PageImpl<>(rutaList, Pageable.ofSize(10), 1);

        when(rutaArchivoRepository.buscarPorFiltros(any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);
        Mockito.when(genericService.getNombreCategoriaDirectorio(any())).thenReturn("Cliente");
        Mockito.when(genericService.getNombreTipoArchivo(any())).thenReturn("Directorio");

        ReporteDTO resultado = rutaArchivoService.descargarRutas(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        assertNotNull(resultado);
    }

    @Test
    void descargarRutaArchivo_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setCodTipoArchivo("DIR");
        request.setCodCategoriaDirectorio("1000");

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(rutaArchivoRepository.buscarPorFiltros(any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            rutaArchivoService.descargarRutas(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void descargarRutaArchivo_excepcion_generic() {
        GenericException thrown = assertThrows(GenericException.class, () -> {
            rutaArchivoService.descargarRutas(null, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof GenericException);
    }
}
