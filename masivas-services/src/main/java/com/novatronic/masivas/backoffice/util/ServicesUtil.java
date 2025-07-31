package com.novatronic.masivas.backoffice.util;

import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.novalog.audit.logger.NovaLogger;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static java.util.Arrays.asList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author Obi Consulting
 */
public final class ServicesUtil {

    private static final String FORMAT_HORA_MILISEGUNDOS = "HHmmssSSS";
    private static final String FORMAT_FECHA = "dd/MM/yyyy";
    private static final String FORMAT_FECHA_HORA = "dd/MM/yyyy HH:mm:ss";
    private static final String PATTERN = "(.+\\{)(.+)(\\})";
    public static final String CODIGO_LIMITE = "02";
    public static final String CODIGO_DUPLICADO = "10";
    public static final List<String> COD_APROBAR = asList("40", "50", "60", "70", "80");
    public static final List<String> COD_OPERACION_APROBADO = asList("70");
    public static final String MODIFICAR = "modificar";
    public static final String INSERTAR = "insertar";
    public static final String EXISTE = "existe";

    public static final String MENSAJE_MODIFICAR_ERROR = ".modificar.error";
    public static final String MENSAJE_INSERTAR_ERROR = ".insertar.error";
    public static final String MENSAJE_LIMITE_ERROR = ".sinlimite.error";

    public static final String ESTADO_REGISTRADO_VALOR = "R";
    public static final String ESTADO_FINALIZADO_VALOR = "4";
    public static final String ESTADO_EN_COLA_VALOR = "3";
    public static final String ESTADO_OBSERVADO_VALOR = "O";
    public static final String ESTADO_APROBADO_VALOR = "A";

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ServicesUtil.class);

    private ServicesUtil() {
    }

    public static String validateNull(String clave) {
        return clave == null ? "" : clave;
    }

//    public static String getMessageResponse(String codigo) {
//        HazelcastInstance cacheInstance = SpringContext.getBean(HazelcastInstance.class);
//        IMap<String, TsCodigoRespuesta> staticCache = cacheInstance.getMap(ConstantesServices.MAP_CODIGOSRESPUESTA);
//        if (codigo == null || staticCache == null) {
//            return ConstantesServices.MENSAJE_ERROR_GENERICO;
//        }
//        TsCodigoRespuesta cr = staticCache.get(codigo);
//        return cr != null ? cr.getDescUsuarioRspsta() : ConstantesServices.MENSAJE_ERROR_GENERICO;
//    }
    /**
     *
     * Retorna un string con la representacion de la hora actual segun el
     * formato HHmmSSsss
     *
     * @return String representacion de la fecha segun formato
     */
    public static String obtenerFechaActual() {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_HORA_MILISEGUNDOS);
        return hoy.format(formatter);
    }

    public static String dateToString(Date fecha, SimpleDateFormat sdfParam) {
        String resultado = null;
        if (fecha != null) {
            resultado = sdfParam.format(fecha);
        }
        return resultado;
    }

    /**
     *
     * Retorna un string con la representacion de la hora actual segun el
     * formato hh:mm:ss
     *
     * @return String representacion de la fecha segun formato
     */
    public static String hourToStringActual() {
        return dateToString(new Date(), new SimpleDateFormat(FORMAT_HORA_MILISEGUNDOS));
    }

    /**
     *
     * Retorna un string con la representacion de una fecha actual segun el
     * formato dd/MM/yyyy
     *
     * @return String representacion de la fecha segun formato
     */
    public static String dateToStringActual() {
        return dateToString(new Date(), new SimpleDateFormat(FORMAT_FECHA));
    }

    /**
     *
     * Metodo que devuelve una fecha en formato XMLGregorianCalendar a partir de
     * una fecha en String
     *
     * @param date Fecha en String
     * @return
     */
    public static XMLGregorianCalendar getXMLGregorianCalendar(String date) {
        return getXMLGregorianCalendar(stringToDate(date));
    }

    /**
     *
     * Metodo que devuelve una fecha en formato XMLGregorianCalendar a partir de
     * una fecha en Date
     *
     * @param fecha Fecha en String
     * @return
     */
    public static XMLGregorianCalendar getXMLGregorianCalendar(Date fecha) {
        XMLGregorianCalendar xmlCalender = null;
        GregorianCalendar calender = new GregorianCalendar();

        if (fecha == null) {
            return null;
        }

        calender.setTime(fecha);
        try {
            xmlCalender = DatatypeFactory.newInstance().newXMLGregorianCalendar(calender);
        } catch (DatatypeConfigurationException ex) {

        }
        return xmlCalender;
    }

    public static String gregorianDateToStringExt(XMLGregorianCalendar xMLGregorianDate) {
        Date fecha = null;
        if (xMLGregorianDate != null) {
            fecha = xMLGregorianDate.toGregorianCalendar().getTime();
        }

        return dateToString(fecha, new SimpleDateFormat(FORMAT_FECHA_HORA));
    }

    /**
     *
     * Retorna un string con la representacion de la hora actual segun el
     * formato HHmmSSsss
     *
     * @return String representacion de la fecha segun formato
     */
    public static String obtenerFechaActualConHora() {
        LocalDateTime hoy = LocalDateTime.now(); // 🛠️ usa LocalDateTime, no LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_FECHA_HORA);
        return hoy.format(formatter);
    }

//    public static String getMensajeProcedure(MessageSource messageSource, String codigo, String mensaje, String prefijo, String operacion) {
//        String mensajeRetornar = "";
//
//        if (COD_APROBAR.contains(codigo)) {
//            if (!"80".equals(codigo)) {
//                mensaje = messageSource.getMessage(prefijo + "." + codigo, null, Locale.getDefault());
//            }
//            return mensaje;
//        }
//        if (CODIGO_LIMITE.equals(codigo)) {
//            mensaje = getMensajeLimite(messageSource, mensaje);
//            if (mensaje.equals(PREFIJO_SIN_LIMITE)) {
//                mensaje = messageSource.getMessage(prefijo + MENSAJE_LIMITE_ERROR, null, Locale.getDefault());
//            }
//        } else {
//
//            //Validacion temporal, luego hay que mejorar el armado de los mensajes de error
////            if (!mensaje.isEmpty() && !mensaje.contains(Constantes.PUNTO) && !mensaje.contains("existe")) {
//            if (!mensaje.isEmpty() && !mensaje.contains(".") && !mensaje.contains("existe")) {
//                return mensaje;
//            } else {
////                String key = prefijo + Constantes.PUNTO + codigo;
//                String key = prefijo + "." + codigo;
//                mensaje = messageSource.getMessage(key, null, Locale.getDefault());
//                if (mensaje == null || mensaje.isEmpty()) {
//                    if (operacion.equals(MODIFICAR)) {
//                        mensaje = messageSource.getMessage(prefijo + MENSAJE_MODIFICAR_ERROR, null, Locale.getDefault());
//                    } else if (operacion.equals(INSERTAR)) {
//                        mensaje = messageSource.getMessage(prefijo + MENSAJE_INSERTAR_ERROR, null, Locale.getDefault());
//                    }
//
//                }
//            }
//        }
//        return mensaje;
//    }
    public static String getMensajeLimite(MessageSource messageSource, String mensaje) {
        return messageSource.getMessage(CODIGO_LIMITE, null, Locale.getDefault()) + mensaje.replaceAll(PATTERN, "$2");
    }

    public static String generarNombreArchivo(String nombreModulo, String extension) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formatoFecha = now.format(formatter);

        // Genera el nombre del archivo
        return nombreModulo + formatoFecha + extension;
    }

    public static Date stringToDate(String strFecha) {
        Date resultado = null;

        try {
            if (strFecha != null && !strFecha.isEmpty()) {
                resultado = new SimpleDateFormat(FORMAT_FECHA).parse(strFecha);
            }
        } catch (ParseException ex) {
            //Loguea Error
        }

        return resultado;
    }

    public static BigDecimal convertirABigDecimal(Object valor) {
        if (valor == null) {
            return BigDecimal.ZERO;
        } else if (valor instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        } else if (valor instanceof Long long1) {
            return BigDecimal.valueOf(long1);
        }
        // Si llega aquí, es un tipo inesperado
        LOGGER.error("Advertencia: Tipo de dato inesperado para la suma: " + valor.getClass().getName());
        return BigDecimal.ZERO;
    }

    public static Pageable configurarPageSort(FiltroMasivasRequest request) {

        Pageable pageable = null;

        // 1. Determinar si se requiere ordenamiento
        boolean necesitaOrdenar = !request.getCampoOrdenar().isEmpty();
        Sort sort = Sort.unsorted(); // Inicializar con un Sort sin ordenar

        if (necesitaOrdenar) {
            if (ConstantesServices.ORDEN_ASC.equals(request.getSentidoOrdenar())) {
                sort = Sort.by(request.getCampoOrdenar()).ascending();
            } else if (ConstantesServices.ORDEN_DESC.equals(request.getSentidoOrdenar())) {
                sort = Sort.by(request.getCampoOrdenar()).descending();
            }
        }

        // 2. Determinar si se requiere paginación
        boolean necesitaPaginacion = request.getRegistrosPorPagina() > 0; // O > valor_minimo_valido_para_pagina

        if (necesitaPaginacion) {
            pageable = PageRequest.of(request.getNumeroPagina(), request.getRegistrosPorPagina(), sort);
        } else {
            pageable = Pageable.unpaged(sort);
        }
        return pageable;
    }

    public static String formatearLocalDateToString(LocalDate fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_FECHA);
        return fecha.format(formatter);
    }

    public static String formatearLocalDateTimeToString(LocalDateTime fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_FECHA_HORA);
        return fecha.format(formatter);
    }

    public static String obtenerMensajeRespuestaCambioEstado(int numExito, int totalIds, String estado) {

        boolean esMultiplesIds = totalIds > 1;
        boolean esEstadoActivo = ConstantesServices.ESTADO_ACTIVO.equals(estado);
        String mensaje;

        if (numExito == totalIds) {
            mensaje = obtenerMensajeRespuesta(true, esMultiplesIds, esEstadoActivo);
        } else if (numExito > 0 && numExito < totalIds) {
            mensaje = estado.equals(ConstantesServices.ESTADO_ACTIVO) ? ConstantesServices.MENSAJE_EXITO_PARCIAL_ACTIVAR_OPERACIONES : ConstantesServices.MENSAJE_EXITO_PARCIAL_DESACTIVAR_OPERACIONES;
            mensaje = String.format(mensaje, numExito, totalIds - numExito);
        } else {
            mensaje = obtenerMensajeRespuesta(false, esMultiplesIds, esEstadoActivo);
        }
        return mensaje;
    }

    public static String obtenerMensajeRespuesta(boolean esExitoTotal, boolean esMultiplesIds, boolean esEstadoActivo) {
        String mensaje;

        if (esExitoTotal) {
            mensaje = obtenerMensajeExito(esMultiplesIds, esEstadoActivo);
        } else {
            mensaje = obtenerMensajeError(esMultiplesIds, esEstadoActivo);
        }
        return mensaje;
    }

    public static String obtenerMensajeExito(boolean esMultiplesIds, boolean esEstadoActivo) {
        String mensaje;
        // Mensajes para éxito total
        if (esMultiplesIds) {
            mensaje = esEstadoActivo ? ConstantesServices.MENSAJE_EXITO_ACTIVAR_OPERACIONES : ConstantesServices.MENSAJE_EXITO_DESACTIVAR_OPERACIONES;
        } else {
            mensaje = esEstadoActivo ? ConstantesServices.MENSAJE_EXITO_ACTIVAR_OPERACION : ConstantesServices.MENSAJE_EXITO_DESACTIVAR_OPERACION;
        }
        return mensaje;
    }

    public static String obtenerMensajeError(boolean esMultiplesIds, boolean esEstadoActivo) {
        String mensaje;
        // Mensajes para error total
        if (esMultiplesIds) {
            mensaje = esEstadoActivo ? ConstantesServices.MENSAJE_ERROR_ACTIVAR_OPERACIONES : ConstantesServices.MENSAJE_ERROR_DESACTIVAR_OPERACIONES;
        } else {
            mensaje = esEstadoActivo ? ConstantesServices.MENSAJE_ERROR_ACTIVAR_OPERACION : ConstantesServices.MENSAJE_ERROR_DESACTIVAR_OPERACION;
        }
        return mensaje;
    }

}
