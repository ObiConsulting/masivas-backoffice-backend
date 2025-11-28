package com.novatronic.masivas.backoffice.util;

import static com.novatronic.masivas.backoffice.util.ConstantesServices.RESPUESTA_ERROR_9999;

public class LogUtil {
    public static String NOMBREMICROSERVICIO;
    public static String NODOCODIGO;

    public static String getConjunto() {
        String producto  = rightPadZeros(NOMBREMICROSERVICIO, 8);
        String nodo      = rightPadZeros(NODOCODIGO,   4);
        String fecha     = DateUtil.getCurrentTimestamp();
        //String secuencial= leftPadZeros(NumberUtil.getRandomNumber(), 3);
        String secuencial= leftPadZeros(NumberUtil.nextSequence3(), 3);

        String out = producto + nodo + fecha + secuencial;

        // Verificación final (por si algún input viene con longitud inesperada)
        if (out.length() != 32) {
            out = (out.length() > 32)
                    ? out.substring(0, 32)
                    : String.format("%-32s", out).replace(' ', '0'); // completa a la derecha
        }
        return out;
    }

    private static String rightPadZeros(String s, int len) {
        if (s == null) s = "";
        if (s.length() == len) return s;
        if (s.length() > len)  return s.substring(0, len);
        return String.format("%-" + len + "s", s).replace(' ', '0'); // pad derecha
    }
    private static String leftPadZeros(String s, int len) {
        if (s == null) s = "";
        if (s.length() == len) return s;
        if (s.length() > len)  return s.substring(s.length() - len);
        return String.format("%" + len + "s", s).replace(' ', '0'); // pad izquierda
    }

    public static String padCodigoGenerico() {
        return String.format("%-6s", RESPUESTA_ERROR_9999);
    }

    public static String generarMensajeLogError(String descripcion) {
        return generarMensajeLogError(RESPUESTA_ERROR_9999, descripcion, null);
    }

    public static String generarMensajeLogError(String descripcion, String mensaje) {
        return generarMensajeLogError(RESPUESTA_ERROR_9999, descripcion, mensaje);
    }

    public static String generarMensajeLogError(String codigo, String descripcion, String mensaje) {
        return String.format("%-6.6s %s %s",
                codigo != null ? codigo : RESPUESTA_ERROR_9999,
                descripcion != null ? descripcion : "",
                mensaje != null ? mensaje : "");
    }
}
