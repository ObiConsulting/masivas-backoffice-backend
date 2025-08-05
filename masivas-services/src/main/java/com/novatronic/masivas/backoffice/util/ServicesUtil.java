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
        LOGGER.error("Advertencia: Tipo de dato inesperado para la suma: " + valor.getClass().getName());
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
