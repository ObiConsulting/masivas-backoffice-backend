package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroArchivoMasivasDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.exception.DataBaseException;
import com.novatronic.masivas.backoffice.exception.GenericException;
import com.novatronic.masivas.backoffice.repository.DetalleArchivoMasivasRepository;
import com.novatronic.masivas.backoffice.security.service.CryptoService;
import com.novatronic.masivas.backoffice.security.util.MaskUtil;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import jakarta.transaction.RollbackException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
public class DetalleMasivasServiceTest {

    @Mock
    private DetalleArchivoMasivasRepository detalleArchivoMasivasRepository;
    @Mock
    private CryptoService cryptoServiceImpl;
    @Mock
    private MaskUtil maskUtil;
    @Mock
    private GenericService genericService;
    @InjectMocks
    private DetalleMasivasService detalleMasivasService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(detalleMasivasService, "logo", "logo.png");
        ReflectionTestUtils.setField(detalleMasivasService, "maskActive", "1");
    }

    @Test
    void buscarDetalleMasivas_exito_mask() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaObtencion(LocalDate.now());
        request.setFechaProcesada(LocalDate.now());
        request.setCuentaOrigen("1234567890");
        request.setCuentaDestino("1234567890");

        DetalleRegistroArchivoMasivasDTO entidad = new DetalleRegistroArchivoMasivasDTO();
        List<DetalleRegistroArchivoMasivasDTO> entidadesList = Collections.singletonList(entidad);
        Page<DetalleRegistroArchivoMasivasDTO> paginaSimulada = new PageImpl<>(entidadesList, Pageable.ofSize(10), 1);

        when(detalleArchivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        when(cryptoServiceImpl.decrypt(any())).thenReturn("plain text");
        when(maskUtil.format(any())).thenReturn("******text");

        CustomPaginate<DetalleRegistroArchivoMasivasDTO> resultado = detalleMasivasService.buscarDetalleMasivas(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarDetalleMasivas_exito_unmask() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        ReflectionTestUtils.setField(detalleMasivasService, "maskActive", "0");
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaObtencion(LocalDate.now());
        request.setFechaProcesada(LocalDate.now());
        request.setCuentaOrigen("1234567890");
        request.setCuentaDestino("1234567890");

        DetalleRegistroArchivoMasivasDTO entidad = new DetalleRegistroArchivoMasivasDTO();
        List<DetalleRegistroArchivoMasivasDTO> entidadesList = Collections.singletonList(entidad);
        Page<DetalleRegistroArchivoMasivasDTO> paginaSimulada = new PageImpl<>(entidadesList, Pageable.ofSize(10), 1);

        when(detalleArchivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);

        when(cryptoServiceImpl.decrypt(any())).thenReturn("plain text");

        CustomPaginate<DetalleRegistroArchivoMasivasDTO> resultado = detalleMasivasService.buscarDetalleMasivas(request);
        assertNotNull(resultado);
    }

    @Test
    void buscarDetalleMasivas_excepcion_bd() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaObtencion(LocalDate.now());
        request.setFechaProcesada(LocalDate.now());
        request.setCuentaOrigen("1234567890");
        request.setCuentaDestino("1234567890");

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(detalleArchivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            detalleMasivasService.buscarDetalleMasivas(request);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void buscarDetalleMasivas_excepcion_generic() {

        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaObtencion(LocalDate.now());
        request.setFechaProcesada(LocalDate.now());
        request.setCuentaOrigen("1234567890");
        request.setCuentaDestino("1234567890");

        RuntimeException genericEx = new RuntimeException("");

        when(detalleArchivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        GenericException thrown = assertThrows(GenericException.class, () -> {
            detalleMasivasService.buscarDetalleMasivas(request);
        });
        assertTrue(thrown instanceof GenericException);
    }

    @Test
    void descargarDetalleMasivas_pdf() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaObtencion(LocalDate.now());
        request.setFechaProcesada(LocalDate.now());
        request.setCuentaOrigen("1234567890");
        request.setCuentaDestino("1234567890");

        DetalleRegistroArchivoMasivasDTO entidad = new DetalleRegistroArchivoMasivasDTO();
        List<DetalleRegistroArchivoMasivasDTO> entidadesList = Collections.singletonList(entidad);
        Page<DetalleRegistroArchivoMasivasDTO> paginaSimulada = new PageImpl<>(entidadesList, Pageable.ofSize(10), 1);

        when(detalleArchivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(paginaSimulada);
        Mockito.when(genericService.getNombreMotivoRechazo(any())).thenReturn("AC01");
        Mockito.when(genericService.getNombreTipoTransaccion(any())).thenReturn("321");

        ReporteDTO resultado = detalleMasivasService.descargarDetalleArchivoMasivas(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        assertNotNull(resultado);
    }

    @Test
    void descargarDetalleMasivas_excepcion_bd() {
        FiltroMasivasRequest request = new FiltroMasivasRequest();
        request.setFechaObtencion(LocalDate.now());
        request.setFechaProcesada(LocalDate.now());
        request.setCuentaOrigen("1234567890");
        request.setCuentaDestino("1234567890");

        RollbackException rollbackEx = new RollbackException("");
        RuntimeException genericEx = new RuntimeException("", rollbackEx);

        when(detalleArchivoMasivasRepository.buscarPorFiltros(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class))).thenThrow(genericEx);

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            detalleMasivasService.descargarDetalleArchivoMasivas(request, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof DataBaseException);
    }

    @Test
    void descargarDetalleMasivas_excepcion_generic() {
        GenericException thrown = assertThrows(GenericException.class, () -> {
            detalleMasivasService.descargarDetalleArchivoMasivas(null, "usuario", ConstantesServices.TIPO_ARCHIVO_PDF);
        });
        assertTrue(thrown instanceof GenericException);
    }
}
