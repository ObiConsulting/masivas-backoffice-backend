package com.novatronic.masivas.backoffice.util;

import com.novatronic.novalog.audit.logger.NovaLogger;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author Obi Consulting
 */
public class FuncionesUtil {

    private static final String SDF = "dd/MM/yyyy";
    private static final String SDFWS = "yyyyMMdd";
    private static final String FORMAT_FECHA_CON_HORA = "dd/MM/yyyy HH:mm:ss";

    private static final NovaLogger LOGGER = NovaLogger.getLogger(FuncionesUtil.class);

    public static String convertToDateWithoutSeparators(String fecha) {
        String resultado = null;
        Date fdate = null;

        if (fecha != null) {
            fdate = stringToDate(fecha);
            resultado = dateToStringWithoutSeparators(fdate);
        }

        return resultado;
    }

    /**
     *
     * Retorna una representacion literal de una fecha segun el formato yyyyMMdd
     *
     * @param fecha Objeto tipo date
     * @return String
     */
    public static String dateToStringWithoutSeparators(Date fecha) {
        String resultado = null;
        if (fecha != null) {
            resultado = new SimpleDateFormat(SDFWS).format(fecha);
        }
        return resultado;
    }

    public static Date stringToDate(String strFecha) {
        Date resultado = null;

        try {
            if (strFecha != null && !strFecha.isEmpty()) {
                resultado = new SimpleDateFormat(SDF).parse(strFecha);
            }
        } catch (ParseException ex) {
            //Loguea Error
        }

        return resultado;
    }

    public static Integer obtenerCantidadPaginas(int cantidad, int tamPagina) {
        int pagina = cantidad / tamPagina;
        double resto = cantidad % tamPagina;
        if (resto > 0) {
            pagina++;
        }
        return pagina;
    }

    /**
     * Metodo que permite evaluar si existen 3 parametros OUT o no
     *
     * @param cantidadParametros
     * @param indiceUltimoParametro
     * @return
     */
    public boolean verificarExisteUltimoParametrosOut(int cantidadParametros, int indiceUltimoParametro) {
        boolean existe = false;

        if (indiceUltimoParametro == cantidadParametros) {
            existe = true;
        }

        return existe;
    }

    public static String formatearLocalDateTimeToString_sinnHora(LocalDateTime fecha) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SDF);
            return fecha.format(formatter);
        } catch (Exception e) {
            return "-";
        }
    }

    public static String formatearLocalDateTimeToString_conHora(LocalDateTime fecha) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_FECHA_CON_HORA);
            return fecha.format(formatter);
        } catch (Exception e) {
            return "-";
        }
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
        LOGGER.error("{} Advertencia: Tipo de dato inesperado para la suma: {}", 9999, valor.getClass().getName());
        return BigDecimal.ZERO;
    }
}
