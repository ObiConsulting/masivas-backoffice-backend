package com.novatronic.masivas.backoffice.service;

import com.novatronic.masivas.backoffice.dto.EntidadDTO;
import com.novatronic.masivas.backoffice.dto.ParametroDTO;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.masivas.backoffice.util.LogUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

import static com.novatronic.masivas.backoffice.util.ConstantesLog.PROP_NO_ENCONTRADA;

/**
 *
 * @author Obi Consulting
 */
@Service
public class GenericService {

    private final ParametroCacheService parametroCacheService;

    private static final NovaLogger LOGGER = NovaLogger.getLogger(GenericService.class);

    public GenericService(ParametroCacheService parametroCacheService) {
        this.parametroCacheService = parametroCacheService;
    }

    /**
     * Método que devuelve la lista de estado (activo|inactivo)
     *
     * @return
     */
    public List<ParametroDTO> listarEstados() {
        return List.of(new ParametroDTO(ConstantesServices.ESTADO_INACTIVO, ConstantesServices.ESTADO_INACTIVO_DESCRIPCION),
                new ParametroDTO(ConstantesServices.ESTADO_ACTIVO, ConstantesServices.ESTADO_ACTIVO_DESCRIPCION)
        );
    }

    /**
     * Método que devuelve la lista de estado de los archivos.
     *
     * @return
     */
    public List<ParametroDTO> getAllEstadoArchivo() {
        List<ParametroDTO> listaEstadoArchivo = parametroCacheService.getParametersByGroup(ConstantesServices.ID_GRUPO_ESTADO_ARCHIVOS);
        return listaEstadoArchivo.stream()
                .sorted(Comparator.comparing(ParametroDTO::getCodigo))
                .toList();
    }

    /**
     * Método que devuelve la lista de categoria directorio.
     *
     * @return
     */
    public List<ParametroDTO> getAllCategoriaDirectorio() {
        List<ParametroDTO> listaCategoriaDirectorio = parametroCacheService.getParametersByGroup(ConstantesServices.ID_GRUPO_CATEGORIA_DIRECTORIO);
        return listaCategoriaDirectorio.stream()
                .filter(estado -> "1000".equals(estado.getCodigo()) || "2000".equals(estado.getCodigo()))
                .sorted(Comparator.comparing(ParametroDTO::getCodigo))
                .toList();
    }

    /**
     * Método que devuelve la lista de tipos de archivo.
     *
     * @return
     */
    public List<ParametroDTO> getAllTipoArchivo() {
        List<ParametroDTO> listaTipoArchivo = parametroCacheService.getParametersByGroup(ConstantesServices.ID_GRUPO_TIPO_ARCHIVO);
        return listaTipoArchivo.stream()
                .sorted(Comparator.comparing(ParametroDTO::getCodigo))
                .toList();
    }

    /**
     * Método que devuelve la lista de extensiones base.
     *
     * @return
     */
    public List<ParametroDTO> getAllExtensionBase() {
        List<ParametroDTO> listaExtensionBase = parametroCacheService.getParametersByGroup(ConstantesServices.ID_GRUPO_EXTENSION_BASE);
        return listaExtensionBase.stream()
                .sorted(Comparator.comparing(ParametroDTO::getCodigo))
                .toList();
    }

    /**
     * Método que devuelve la lista de extensiones de control.
     *
     * @return
     */
    public List<ParametroDTO> getAllExtensionControl() {
        List<ParametroDTO> listaExtensionControl = parametroCacheService.getParametersByGroup(ConstantesServices.ID_GRUPO_EXTENSION_CONTROL);
        return listaExtensionControl.stream()
                .sorted(Comparator.comparing(ParametroDTO::getCodigo))
                .toList();
    }

    /**
     * Método que devuelve la lista de motivo de rechazo.
     *
     * @return
     */
    public List<ParametroDTO> getAllMotivoRechazo() {
        List<ParametroDTO> listaMotivoRechazo = parametroCacheService.getParametersByGroup(ConstantesServices.ID_GRUPO_MOTIVO_RECHAZO);
        List<ParametroDTO> listaRptaOperadora = parametroCacheService.getParametersByGroup(ConstantesServices.ID_GRUPO_RPTA_OPERADORA);
        return Stream.concat(listaMotivoRechazo.stream(), listaRptaOperadora.stream())
                .sorted(Comparator.comparing(ParametroDTO::getCodigo))
                .toList();
    }

    /**
     * Método que devuelve la lista de tipo de transacciones.
     *
     * @return
     */
    public List<ParametroDTO> getAllTipoTransaccion() {
        List<ParametroDTO> listaTipoTransaccion = parametroCacheService.getParametersByGroup(ConstantesServices.ID_GRUPO_TIPO_TRANSACCION);
        return listaTipoTransaccion.stream()
                .sorted(Comparator.comparing(ParametroDTO::getCodigo))
                .toList();
    }

    public List<ParametroDTO> getAllMoneda() {
        List<ParametroDTO> listaTipoTransaccion = parametroCacheService.getParametersByGroup(ConstantesServices.ID_GRUPO_MONEDA);
        return listaTipoTransaccion.stream()
                .sorted(Comparator.comparing(ParametroDTO::getCodigo))
                .toList();
    }

    public String getNombreEstadoArchivo(String codEstado) {
        List<ParametroDTO> lista = getAllEstadoArchivo();
        return getGenericName(lista, codEstado);
    }

    public String getNombreCategoriaDirectorio(String codEstado) {
        List<ParametroDTO> lista = getAllCategoriaDirectorio();
        return getGenericName(lista, codEstado);
    }

    public String getNombreTipoArchivo(String codEstado) {
        List<ParametroDTO> lista = getAllTipoArchivo();
        return getGenericName(lista, codEstado);
    }

    public String getNombreGrupoParametro(Long codGrupo) {
        ParametroDTO grupo = parametroCacheService.getParameterGroup(String.valueOf(codGrupo));
        return grupo != null ? grupo.getDescripcion() : "";
    }

    public String getNombreEntidad(String codigoEntidad) {
        EntidadDTO entidad = parametroCacheService.getEntity(codigoEntidad);
        return entidad != null ? entidad.getDescripcion() : "";
    }

    public String getNombreMoneda(String codigoMoneda) {
        List<ParametroDTO> lista = getAllMoneda();
        String nombre = getGenericName(lista, codigoMoneda);
        return nombre.isEmpty() ? nombre : codigoMoneda + ConstantesServices.GUION + nombre;
    }

    public String getCodigoEntidadPropietaria() {
        List<EntidadDTO> listaEntidad = parametroCacheService.getAllEntities();
        Optional<EntidadDTO> propietaria = listaEntidad.stream()
                .filter(entidad -> entidad.getPropietario().equals(ConstantesServices.PROPIETARIO))
                .findFirst();
        if (propietaria.isPresent()) {
            return propietaria.get().getCodigo();
        } else {
            String log=LogUtil.generarMensajeLogError(PROP_NO_ENCONTRADA);
            LOGGER.error(log);
            return "-1";
        }
    }

    public Long getIdEntidadPropietaria() {
        List<EntidadDTO> listaEntidad = parametroCacheService.getAllEntities();
        Optional<EntidadDTO> propietaria = listaEntidad.stream()
                .filter(entidad -> entidad.getPropietario().equals(ConstantesServices.PROPIETARIO))
                .findFirst();
        if (propietaria.isPresent()) {
            return propietaria.get().getIdEntidad();
        } else {
            String log=LogUtil.generarMensajeLogError(PROP_NO_ENCONTRADA);
            LOGGER.error(log);
            return null;
        }
    }

    public String getNombreMotivoRechazo(String codMotivoRechazo) {
        List<ParametroDTO> lista = getAllMotivoRechazo();
        String nombre = getGenericName(lista, codMotivoRechazo);
        return nombre.isEmpty() ? nombre : codMotivoRechazo + ConstantesServices.GUION + nombre;
    }

    public String getNombreTipoTransaccion(String codTipoTransaccion) {
        List<ParametroDTO> lista = getAllTipoTransaccion();
        String nombre = getGenericName(lista, codTipoTransaccion);
        return nombre.isEmpty() ? nombre : codTipoTransaccion + ConstantesServices.GUION + nombre;
    }

    public String getGenericName(List<ParametroDTO> lista, String genericCode) {
        String genericName;
        genericName = lista.stream()
                .filter(parameter -> parameter.getCodigo().equals(genericCode))
                .findFirst()
                .map(ParametroDTO::getDescripcion)
                .orElse("");
        return genericName;
    }

}
