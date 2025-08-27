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
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Estado 1"));
        listaParametro.add(new ParametroDTO("002", "Estado 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllEstadoArchivo();
        assertNotNull(resultado);
    }

    @Test
    void listarCategoriaDirectorio_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("1000", "Categoria 1"));
        listaParametro.add(new ParametroDTO("2000", "Categoria 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllCategoriaDirectorio();
        assertNotNull(resultado);
    }

    @Test
    void listarTipoArchivo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Tipo 1"));
        listaParametro.add(new ParametroDTO("002", "Tipo 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllTipoArchivo();
        assertNotNull(resultado);
    }

    @Test
    void listarExtensionBase_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Extension Base 1"));
        listaParametro.add(new ParametroDTO("002", "Extension Base 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllExtensionBase();
        assertNotNull(resultado);
    }

    @Test
    void listarExtensionControl_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Extension Control 1"));
        listaParametro.add(new ParametroDTO("002", "Extension Control 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllExtensionControl();
        assertNotNull(resultado);
    }

    @Test
    void listarMotivoRechazo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Motivo 1"));
        listaParametro.add(new ParametroDTO("002", "Motivo 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllMotivoRechazo();
        assertNotNull(resultado);
    }

    @Test
    void listarTipoTransaccion_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Tipo Transaccion 1"));
        listaParametro.add(new ParametroDTO("002", "Tipo Transaccion 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllTipoTransaccion();
        assertNotNull(resultado);
    }

    @Test
    void listarMoneda_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("604", "Soles"));
        listaParametro.add(new ParametroDTO("840", "Dolares"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        List<ParametroDTO> resultado = genericService.getAllMoneda();
        assertNotNull(resultado);
    }

    @Test
    void obtenerNombreEstadoArchivo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Estado 1"));
        listaParametro.add(new ParametroDTO("002", "Estado 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreEstadoArchivo("001");
        assertEquals("Estado 1", resultado);
    }

    @Test
    void obtenerNombreCategoriaDirectorio_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("1000", "Categoria 1"));
        listaParametro.add(new ParametroDTO("2000", "Categoria 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreCategoriaDirectorio("1000");
        assertEquals("Categoria 1", resultado);
    }

    @Test
    void obtenerNombreTipoArchivo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Tipo 1"));
        listaParametro.add(new ParametroDTO("002", "Tipo 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreTipoArchivo("001");
        assertEquals("Tipo 1", resultado);
    }

    @Test
    void obtenerNombreMotivoRechazo_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Motivo 1"));
        listaParametro.add(new ParametroDTO("002", "Motivo 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreMotivoRechazo("001");
        assertEquals("001 - Motivo 1", resultado);
    }

    @Test
    void obtenerNombreMotivoRechazo_sinExito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Motivo 1"));
        listaParametro.add(new ParametroDTO("002", "Motivo 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreMotivoRechazo("100");
        assertEquals("", resultado);
    }

    @Test
    void obtenerNombreTipoTransaccion_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("001", "Tipo Transaccion 1"));
        listaParametro.add(new ParametroDTO("002", "Tipo Transaccion 2"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreTipoTransaccion("001");
        assertEquals("001 - Tipo Transaccion 1", resultado);
    }

    @Test
    void obtenerNombreEntidad_exito() {
        EntidadDTO entidad = new EntidadDTO("001", "Entidad 1", "0");

        when(parametroCacheService.getEntity(any())).thenReturn(entidad);
        String resultado = genericService.getNombreEntidad("001");
        assertEquals("Entidad 1", resultado);
    }

    @Test
    void obtenerNombreEntidad_sinExito() {
        when(parametroCacheService.getEntity(any())).thenReturn(null);
        String resultado = genericService.getNombreEntidad("001");
        assertEquals("", resultado);
    }

    @Test
    void obtenerNombreGrupoParametro_exito() {
        ParametroDTO parametro = new ParametroDTO("001", "Grupo 001");

        when(parametroCacheService.getParameterGroup(any())).thenReturn(parametro);
        String resultado = genericService.getNombreGrupoParametro(1l);
        assertEquals("Grupo 001", resultado);
    }

    @Test
    void obtenerNombreGrupoParametro_sinExito() {
        when(parametroCacheService.getParameterGroup(any())).thenReturn(null);
        String resultado = genericService.getNombreGrupoParametro(1l);
        assertEquals("", resultado);
    }

    @Test
    void obtenerNombreMoneda_exito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("604", "Soles"));
        listaParametro.add(new ParametroDTO("840", "Dolares"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreMoneda("604");
        assertEquals("604 - Soles", resultado);
    }

    @Test
    void obtenerNombreMoneda_sinExito() {
        List<ParametroDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new ParametroDTO("604", "Soles"));
        listaParametro.add(new ParametroDTO("840", "Dolares"));

        when(parametroCacheService.getParametersByGroup(any())).thenReturn(listaParametro);
        String resultado = genericService.getNombreMoneda("800");
        assertEquals("", resultado);
    }

    @Test
    void obtenerCodigoEntidadPropietaria_exito() {
        List<EntidadDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new EntidadDTO("001", "Entidad 1", "0"));
        listaParametro.add(new EntidadDTO("002", "Entidad 2", "0"));
        listaParametro.add(new EntidadDTO("003", "Entidad 3", "1"));

        when(parametroCacheService.getAllEntities()).thenReturn(listaParametro);
        String resultado = genericService.getCodigoEntidadPropietaria();
        assertEquals("003", resultado);
    }

    @Test
    void obtenerCodigoEntidadPropietaria_sinExito() {
        List<EntidadDTO> listaParametro = new ArrayList<>();
        listaParametro.add(new EntidadDTO("001", "Entidad 1", "0"));
        listaParametro.add(new EntidadDTO("002", "Entidad 2", "0"));

        when(parametroCacheService.getAllEntities()).thenReturn(listaParametro);
        String resultado = genericService.getCodigoEntidadPropietaria();
        assertEquals("-1", resultado);
    }

}
