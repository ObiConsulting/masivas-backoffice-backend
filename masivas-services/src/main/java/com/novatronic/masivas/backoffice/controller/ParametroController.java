package com.novatronic.masivas.backoffice.controller;

import com.novatronic.masivas.backoffice.dto.ParametroDTO;
import com.novatronic.masivas.backoffice.dto.CustomPaginate;
import com.novatronic.masivas.backoffice.dto.DetalleConsultaParametroDTO;
import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.masivas.backoffice.dto.MasivasRequestDTO;
import com.novatronic.masivas.backoffice.dto.MasivasResponse;
import com.novatronic.masivas.backoffice.dto.DetalleRegistroParametroDTO;
import com.novatronic.masivas.backoffice.dto.EstadoDTO;
import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.log.Performance;
import com.novatronic.masivas.backoffice.security.model.UserContext;
import com.novatronic.masivas.backoffice.service.GenericService;
import com.novatronic.masivas.backoffice.service.ParametroService;
import com.novatronic.masivas.backoffice.util.ConstantesServices;
import com.novatronic.novalog.audit.util.Estado;
import com.novatronic.novalog.audit.util.Evento;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Obi Consulting
 */
@RequestMapping(value = "parametro", produces = "application/json")
@RestController
public class ParametroController {

    private final ParametroService parametroService;
    private final GenericService genericService;

    public ParametroController(ParametroService parametroService, GenericService genericService) {
        this.parametroService = parametroService;
        this.genericService = genericService;
    }

    /**
     * Endpoint que realiza la creación de un parámetro en el sistema. Si la
     * operación es exitosa, se retorna el ID del parámetro recién creado.
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/crear")
    public ResponseEntity<MasivasResponse<Object>> registrar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idParametro = parametroService.crearParametro(request, userContext.getUsername());
        parametroService.logAuditoria(request, Evento.EV_REGISTRO_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_CREATE,
                ConstantesServices.MENSAJE_EXITO_CREAR_PARAMETRO, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_CREAR_PARAMETRO, idParametro));
    }

    /**
     * Endpoing que realiza la busqueda de los parámetros en el sistema. Recibe
     * un objeto con filtros de búsqueda, y retorna una lista paginada de
     * parámetros que coincidan con dichos criterios.
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/buscar")
    public ResponseEntity<MasivasResponse<Object>> buscar(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        CustomPaginate<DetalleConsultaParametroDTO> objPageable = parametroService.buscarParametro(request);
        parametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_VIEW,
                ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_BUSCAR_OPERACION, objPageable));
    }

    /**
     * Endpoint que realiza la edición de un parámetro en el sistema. Si la
     * operación es exitosa, se retorna el ID del parámetro recién editado.
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/editar")
    public ResponseEntity<MasivasResponse<Object>> editar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        Long idParametro = parametroService.editarParametro(request, userContext.getUsername());
        parametroService.logAuditoria(request, Evento.EV_ACTUALIZA_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_UPDATE,
                ConstantesServices.MENSAJE_EXITO_EDITAR_PARAMETRO, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_EDITAR_PARAMETRO, idParametro));
    }

    /**
     * Endpoint que devuelve los detalles de un parámetro específico a partir de
     * su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/obtener")
    public ResponseEntity<MasivasResponse<Object>> obtener(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        DetalleRegistroParametroDTO parametroDTO = parametroService.obtenerParametro(request);
        parametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_READ,
                ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_OBTENER_OPERACION, parametroDTO));
    }

    /**
     * Endpoint que Activa uno o varios parámetros a partir de su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/activar")
    public ResponseEntity<MasivasResponse<Object>> activar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = parametroService.cambiarEstadoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_ACTIVO);
        parametroService.logAuditoria(request, Evento.EV_ACTUALIZA_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_GRUPO_PARAMETRO, ConstantesServices.ACCION_UPDATE,
                ConstantesServices.MENSAJE_ERROR_ACTIVAR_PARAMETROS, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    /**
     * Endpoint que Inactiva uno o varios parámetros a partir de su ID.
     *
     * @param request
     * @param userContext
     * @return
     */
    @PostMapping("/desactivar")
    public ResponseEntity<MasivasResponse<Object>> desactivar(@Valid @RequestBody MasivasRequestDTO request, @AuthenticationPrincipal UserContext userContext) {
        EstadoDTO estadoDTO = parametroService.cambiarEstadoParametro(request, userContext.getUsername(), ConstantesServices.ESTADO_INACTIVO);
        parametroService.logAuditoria(request, Evento.EV_ACTUALIZA_CONFIG_SISTEMA, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_UPDATE,
                ConstantesServices.MENSAJE_ERROR_DESACTIVAR_PARAMETROS, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, estadoDTO.getMensaje(), estadoDTO.getNumExitos()));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato PDF con la lista de
     * parámetros.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/descargarPDF")
    public ResponseEntity<MasivasResponse<Object>> descargarPDF(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = parametroService.descargarParametro(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_PDF);
        parametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_EXPORT,
                ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que genera y descarga un reporte en formato XLSX con la lista de
     * parámetros.
     *
     *
     * @param request
     * @param userContext
     * @return
     */
    @Performance
    @PostMapping("/descargarXLSX")
    public ResponseEntity<MasivasResponse<Object>> descargarXLSX(@Valid @RequestBody FiltroMasivasRequest request, @AuthenticationPrincipal UserContext userContext) {
        ReporteDTO reporteDTO = parametroService.descargarParametro(request, userContext.getUsername(), ConstantesServices.TIPO_ARCHIVO_XLSX);
        parametroService.logAuditoria(request, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_EXPORT,
                ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_DESCARGAR_OPERACION, reporteDTO));
    }

    /**
     * Endpoint que obtiene la lista completa de todos los estados de los
     * archivos.
     *
     * @param userContext
     * @return
     */
    @GetMapping("/listarEstadoArchivos")
    public ResponseEntity<MasivasResponse<Object>> listarEstadoArchivos(@AuthenticationPrincipal UserContext userContext) {
        List<ParametroDTO> listaEstado = genericService.getAllEstadoArchivo();
        parametroService.logAuditoria(null, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_VIEW,
                ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    /**
     * Endpoint que obtiene la lista completa de todas los categorías de
     * directorio.
     *
     * @param userContext
     * @return
     */
    @GetMapping("/listarCategoriaDirectorio")
    public ResponseEntity<MasivasResponse<Object>> listarCategoriaDirectorio(@AuthenticationPrincipal UserContext userContext) {
        List<ParametroDTO> listaEstado = genericService.getAllCategoriaDirectorio();
        parametroService.logAuditoria(null, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_VIEW,
                ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    /**
     * Endpoint que obtiene la lista completa de todos los tipos de archivo.
     *
     * @param userContext
     * @return
     */
    @GetMapping("/listarTipoArchivo")
    public ResponseEntity<MasivasResponse<Object>> listarTipoArchivo(@AuthenticationPrincipal UserContext userContext) {
        List<ParametroDTO> listaEstado = genericService.getAllTipoArchivo();
        parametroService.logAuditoria(null, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_VIEW,
                ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    /**
     * Endpoint que obtiene la lista completa de todas las estensiones bases.
     *
     * @param userContext
     * @return
     */
    @GetMapping("/listarExtensionBase")
    public ResponseEntity<MasivasResponse<Object>> listarExtensionBase(@AuthenticationPrincipal UserContext userContext) {
        List<ParametroDTO> listaEstado = genericService.getAllExtensionBase();
        parametroService.logAuditoria(null, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_VIEW,
                ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    /**
     * Endpoint que obtiene la lista completa de todas las extensiones de
     * control.
     *
     * @param userContext
     * @return
     */
    @GetMapping("/listarExtensionControl")
    public ResponseEntity<MasivasResponse<Object>> listarExtensionControl(@AuthenticationPrincipal UserContext userContext) {
        List<ParametroDTO> listaEstado = genericService.getAllExtensionControl();
        parametroService.logAuditoria(null, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_VIEW,
                ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    /**
     * Endpoint que obtiene la lista completa de todos los tipos de transacción.
     *
     * @param userContext
     * @return
     */
    @GetMapping("/listarTipoTransaccion")
    public ResponseEntity<MasivasResponse<Object>> listarTipoTransaccion(@AuthenticationPrincipal UserContext userContext) {
        List<ParametroDTO> listaEstado = genericService.getAllTipoTransaccion();
        parametroService.logAuditoria(null, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_VIEW,
                ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    /**
     * Endpoint que obtiene la lista completa de todos los motivos de rechazo.
     *
     * @param userContext
     * @return
     */
    @GetMapping("/listarMotivoRechazo")
    public ResponseEntity<MasivasResponse<Object>> listarMotivoRechazo(@AuthenticationPrincipal UserContext userContext) {
        List<ParametroDTO> listaEstado = genericService.getAllMotivoRechazo();
        parametroService.logAuditoria(null, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_VIEW,
                ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaEstado));
    }

    /**
     * Endpoint que obtiene la lista completa de todas las monedas
     *
     * @param userContext
     * @return
     */
    @GetMapping("/listarMoneda")
    public ResponseEntity<MasivasResponse<Object>> listarMoneda(@AuthenticationPrincipal UserContext userContext) {
        List<ParametroDTO> listaMoneda = genericService.getAllMoneda();
        parametroService.logAuditoria(null, Evento.EV_CONSULTA_REPORTE, Estado.ESTADO_EXITO, userContext, ConstantesServices.TABLA_PARAMETRO, ConstantesServices.ACCION_VIEW,
                ConstantesServices.MENSAJE_EXITO_CONSULTA_OPERACION, ConstantesServices.RESPUESTA_OK_API);
        return ResponseEntity.ok(new MasivasResponse<>(ConstantesServices.RESPUESTA_OK_API, ConstantesServices.MENSAJE_EXITO_GENERICO, listaMoneda));
    }
}
