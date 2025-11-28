package com.novatronic.masivas.backoffice.util;

import com.novatronic.masivas.backoffice.dto.FiltroMasivasRequest;
import com.novatronic.novalog.audit.logger.NovaLogger;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.novatronic.masivas.backoffice.util.LogUtil.padCodigoGenerico;

/**
 *
 * @author Obi Consulting
 */
public final class ServicesUtil {

    private static final String FORMAT_FECHA = "dd/MM/yyyy";
    private static final String FORMAT_FECHA_HORA = "dd/MM/yyyy HH:mm:ss";

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ServicesUtil.class);

    private ServicesUtil() {
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
        LOGGER.error("{} Advertencia: Tipo de dato inesperado para la suma: {}", padCodigoGenerico(), valor.getClass().getName());
        return BigDecimal.ZERO;
    }

    public static Pageable configurarPageSort(FiltroMasivasRequest request) {

        Pageable pageable;

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

    public static String obtenerMensajeRespuestaCambioEstado(int numExito, int totalIds, String estado, String pantalla) {

        boolean esMultiplesIds = totalIds > 1;
        boolean esEstadoActivo = ConstantesServices.ESTADO_ACTIVO.equals(estado);
        String mensaje;

        if (numExito == totalIds) {
            mensaje = esEstadoActivo ? mensajeExitoActivarPorPantalla(pantalla, esMultiplesIds) : mensajeExitoDesactivarPorPantalla(pantalla, esMultiplesIds);
        } else if (numExito > 0 && numExito < totalIds) {
            mensaje = mensajeParcialPorPantalla(pantalla, estado);
            mensaje = String.format(mensaje, numExito, totalIds - numExito);
        } else {
            mensaje = esEstadoActivo ? mensajeErrorActivarPorPantalla(pantalla, esMultiplesIds) : mensajeErrorDesactivarPorPantalla(pantalla, esMultiplesIds);
        }
        return mensaje;
    }

    public static String mensajeParcialPorPantalla(String pantalla, String estado) {

        return switch (pantalla) {
            case ConstantesServices.APLICACION ->
                estado.equals(ConstantesServices.ESTADO_ACTIVO) ? ConstantesServices.MENSAJE_EXITO_PARCIAL_ACTIVAR_APLICACIONES : ConstantesServices.MENSAJE_EXITO_PARCIAL_DESACTIVAR_APLICACIONES;
            case ConstantesServices.ENTIDAD_FINANCIERA ->
                estado.equals(ConstantesServices.ESTADO_ACTIVO) ? ConstantesServices.MENSAJE_EXITO_PARCIAL_ACTIVAR_ENTIDADES : ConstantesServices.MENSAJE_EXITO_PARCIAL_DESACTIVAR_ENTIDADES;
            case ConstantesServices.GRUPO_PARAMETRO ->
                estado.equals(ConstantesServices.ESTADO_ACTIVO) ? ConstantesServices.MENSAJE_EXITO_PARCIAL_ACTIVAR_GRUPOS_PARAMETROS : ConstantesServices.MENSAJE_EXITO_PARCIAL_DESACTIVAR_GRUPOS_PARAMETROS;
            case ConstantesServices.PARAMETRO ->
                estado.equals(ConstantesServices.ESTADO_ACTIVO) ? ConstantesServices.MENSAJE_EXITO_PARCIAL_ACTIVAR_PARAMETROS : ConstantesServices.MENSAJE_EXITO_PARCIAL_DESACTIVAR_PARAMETROS;
            default ->
                "";
        };

    }

    public static String mensajeExitoActivarPorPantalla(String pantalla, boolean esMultiplesIds) {

        return switch (pantalla) {
            case ConstantesServices.APLICACION ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_EXITO_ACTIVAR_APLICACION : ConstantesServices.MENSAJE_EXITO_ACTIVAR_APLICACIONES;
            case ConstantesServices.ENTIDAD_FINANCIERA ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_EXITO_ACTIVAR_ENTIDAD : ConstantesServices.MENSAJE_EXITO_ACTIVAR_ENTIDADES;
            case ConstantesServices.GRUPO_PARAMETRO ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_EXITO_ACTIVAR_GRUPO_PARAMETRO : ConstantesServices.MENSAJE_EXITO_ACTIVAR_GRUPOS_PARAMETROS;
            case ConstantesServices.PARAMETRO ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_EXITO_ACTIVAR_PARAMETRO : ConstantesServices.MENSAJE_EXITO_ACTIVAR_PARAMETROS;
            default ->
                "";
        };

    }

    public static String mensajeErrorActivarPorPantalla(String pantalla, boolean esMultiplesIds) {

        return switch (pantalla) {
            case ConstantesServices.APLICACION ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_ERROR_ACTIVAR_APLICACION : ConstantesServices.MENSAJE_ERROR_ACTIVAR_APLICACIONES;
            case ConstantesServices.ENTIDAD_FINANCIERA ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_ERROR_ACTIVAR_ENTIDAD : ConstantesServices.MENSAJE_ERROR_ACTIVAR_ENTIDADES;
            case ConstantesServices.GRUPO_PARAMETRO ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_ERROR_ACTIVAR_GRUPO_PARAMETRO : ConstantesServices.MENSAJE_ERROR_ACTIVAR_GRUPOS_PARAMETROS;
            case ConstantesServices.PARAMETRO ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_ERROR_ACTIVAR_PARAMETRO : ConstantesServices.MENSAJE_ERROR_ACTIVAR_PARAMETROS;
            default ->
                "";
        };

    }

    public static String mensajeExitoDesactivarPorPantalla(String pantalla, boolean esMultiplesIds) {

        return switch (pantalla) {
            case ConstantesServices.APLICACION ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_EXITO_DESACTIVAR_APLICACION : ConstantesServices.MENSAJE_EXITO_DESACTIVAR_APLICACIONES;
            case ConstantesServices.ENTIDAD_FINANCIERA ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_EXITO_DESACTIVAR_ENTIDAD : ConstantesServices.MENSAJE_EXITO_DESACTIVAR_ENTIDADES;
            case ConstantesServices.GRUPO_PARAMETRO ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_EXITO_DESACTIVAR_GRUPO_PARAMETRO : ConstantesServices.MENSAJE_EXITO_DESACTIVAR_GRUPOS_PARAMETROS;
            case ConstantesServices.PARAMETRO ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_EXITO_DESACTIVAR_PARAMETRO : ConstantesServices.MENSAJE_EXITO_DESACTIVAR_PARAMETROS;
            default ->
                "";
        };

    }

    public static String mensajeErrorDesactivarPorPantalla(String pantalla, boolean esMultiplesIds) {

        return switch (pantalla) {
            case ConstantesServices.APLICACION ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_ERROR_DESACTIVAR_APLICACION : ConstantesServices.MENSAJE_ERROR_DESACTIVAR_APLICACIONES;
            case ConstantesServices.ENTIDAD_FINANCIERA ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_ERROR_DESACTIVAR_ENTIDAD : ConstantesServices.MENSAJE_ERROR_DESACTIVAR_ENTIDADES;
            case ConstantesServices.GRUPO_PARAMETRO ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_ERROR_DESACTIVAR_GRUPO_PARAMETRO : ConstantesServices.MENSAJE_ERROR_DESACTIVAR_GRUPOS_PARAMETROS;
            case ConstantesServices.PARAMETRO ->
                !esMultiplesIds ? ConstantesServices.MENSAJE_ERROR_DESACTIVAR_PARAMETRO : ConstantesServices.MENSAJE_ERROR_DESACTIVAR_PARAMETROS;
            default ->
                "";
        };

    }

    public static String hashData(String data) throws NoSuchAlgorithmException {
        if (data == null || data.isEmpty()) {
            return data;
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    public static Integer generarNumeroAleatorio() {
        return ThreadLocalRandom.current().nextInt(0, 1_000_000_000); // Genera entre 0 (inclusive) y 1,000,000,000 (exclusivo)
    }

}
