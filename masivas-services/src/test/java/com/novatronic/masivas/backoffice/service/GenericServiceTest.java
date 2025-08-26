package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.EntidadDTO;
import com.novatronic.masivas.backoffice.dto.ParametroDTO;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Obi Consulting
 */
@ExtendWith(MockitoExtension.class)
public class GenericServiceTest {

    @Mock
    private ParametroCacheService parametroCacheService;
    @InjectMocks
    private GenericService genericService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void listarEstado_exito() {
        List<ParametroDTO> resultado = genericService.listarEstados();
        assertNotNull(resultado);
    }

    @Test
    void listarEstadoArchivo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("001", "Estado 1"));
        listaParametro.add(new ParametroDTO("002", "Estado 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllEstadoArchivo();
        assertNotNull(resultado);
    }

    @Test
    void listarCategoriaDirectorio_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("1000", "Categoria 1"));
        listaParametro.add(new ParametroDTO("2000", "Categoria 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllCategoriaDirectorio();
        assertNotNull(resultado);
    }

    @Test
    void listarTipoArchivo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("001", "Tipo 1"));
        listaParametro.add(new ParametroDTO("002", "Tipo 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllTipoArchivo();
        assertNotNull(resultado);
    }

    @Test
    void listarExtensionBase_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("001", "Extension Base 1"));
        listaParametro.add(new ParametroDTO("002", "Extension Base 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllExtensionBase();
        assertNotNull(resultado);
    }

    @Test
    void listarExtensionControl_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("001", "Extension Control 1"));
        listaParametro.add(new ParametroDTO("002", "Extension Control 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllExtensionControl();
        assertNotNull(resultado);
    }

    @Test
    void listarMotivoRechazo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("001", "Motivo 1"));
        listaParametro.add(new ParametroDTO("002", "Motivo 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllMotivoRechazo();
        assertNotNull(resultado);
    }

    @Test
    void listarTipoTransaccion_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("001", "Tipo Transaccion 1"));
        listaParametro.add(new ParametroDTO("002", "Tipo Transaccion 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllTipoTransaccion();
        assertNotNull(resultado);
    }

    @Test
    void obtenerNombreEstadoArchivo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("001", "Estado 1"));
        listaParametro.add(new ParametroDTO("002", "Estado 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreEstadoArchivo("001");
        assertEquals("Estado 1", resultado);
    }

    @Test
    void obtenerNombreCategoriaDirectorio_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("1000", "Categoria 1"));
        listaParametro.add(new ParametroDTO("2000", "Categoria 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreCategoriaDirectorio("1000");
        assertEquals("Categoria 1", resultado);
    }

    @Test
    void obtenerNombreTipoArchivo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("001", "Tipo 1"));
        listaParametro.add(new ParametroDTO("002", "Tipo 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreTipoArchivo("001");
        assertEquals("Tipo 1", resultado);
    }

    @Test
    void obtenerNombreMotivoRechazo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("001", "Motivo 1"));
        listaParametro.add(new ParametroDTO("002", "Motivo 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreMotivoRechazo("001");
        assertEquals("001 - Motivo 1", resultado);
    }

    @Test
    void obtenerNombreTipoTransaccion_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<ParametroDTO>();
        listaParametro.add(new ParametroDTO("001", "Tipo Transaccion 1"));
        listaParametro.add(new ParametroDTO("002", "Tipo Transaccion 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreTipoTransaccion("001");
        assertEquals("001 - Tipo Transaccion 1", resultado);
    }

    @Test
    void obtenerNombreEntidad_exito() {
        EntidadDTO parametro = new EntidadDTO("001", "Entidad 1", "0");

        when(parametroCacheService.getEntity(any())).thenReturn(parametro);
        String resultado = genericService.getNombreEntidad("001");
        assertEquals("Entidad 1", resultado);
    }
}
