package com.novatronic.masivas.backoffice.log;

import com.novatronic.novalog.audit.logger.NovaLogger;

public class LogSecurity {
    private static final NovaLogger LOG = NovaLogger.getLogger(LogSecurity.class);

    private static final String TRAMA_INF = "%s realizado por el usuario: %s";
    private static final String TRAMA_ALERT = "%s intento realizar %s: %s";

    private LogSecurity() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Metodo que registra en el log de seguridad la operacion y el usuario que
     * la realizo.
     *
     * @param operacion
     * @param usuario
     */
    public static void log(String operacion, String usuario) {
        String msgLog = String.format(TRAMA_INF, operacion, usuario);
        LOG.info(msgLog);
    }

    /**
     * Este metodo se encarga de registrar en el log de seguridad el usuario, la
     * operacion que realizo y el mensaje de de error que alerta sobre la
     * operacion realizada.
     *
     * @param usuario
     * @param operacion
     * @param mensaje
     */
    public static void alert(String usuario, String operacion, String mensaje) {
        String msgLog = String.format(TRAMA_ALERT, usuario, operacion, mensaje);
        LOG.info(msgLog);
    }
}
