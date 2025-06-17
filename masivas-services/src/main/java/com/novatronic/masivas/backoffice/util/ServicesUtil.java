package com.novatronic.masivas.backoffice.util;

import com.novatronic.novalog.audit.logger.NovaLogger;
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

/**
 *
 * @author Obi Consulting
 */
public final class ServicesUtil {

    private static final String FORMAT_HHmmSSsss = "HHmmssSSS";
    private static final String FORMAT_ddMMyyyy = "dd/MM/yyyy";
    private static final String FORMAT_fechaConHora = "dd/MM/yyyy hh:mm:ss";
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

    public static final String PREFIJO_SIN_LIMITE = "Ingresar un monto entre: No definido";

    private static final NovaLogger LOGGER = NovaLogger.getLogger(ServicesUtil.class);
    private static final int LONGITUD_CCI = 20;
    //private static final NovaMask mask = new NovaMask();

    public static final String ESTADO_REGISTRADO_VALOR = "R";
    public static final String ESTADO_FINALIZADO_VALOR = "4";
    public static final String ESTADO_EN_COLA_VALOR = "3";
    public static final String ESTADO_OBSERVADO_VALOR = "O";
    public static final String ESTADO_APROBADO_VALOR = "A";

    private ServicesUtil() {
    }

    /**
     * Validar el CCI
     *
     * @param cci
     * @param bancoCliente
     * @return
     */
    public static boolean isCCIValido(String cci, String bancoCliente) {
        String codigoBancoPlaza = null;
        String numeroCuenta = null;

        if (!cci.matches("\\d{" + LONGITUD_CCI + "}$")) {
            //LOGGER.info("Longitud de numero de cci no valido");
            return false;
        }

        if (!cci.startsWith(bancoCliente.substring(1))) {
            //LOGGER.info("CCI "+mask.maskCuentaCCI(cci)+" no pertenece al banco " + bancoCliente);
            return false;
        }

        codigoBancoPlaza = cci.substring(0, 6);
        //LOGGER.info("codigoBancoPlaza = " + codigoBancoPlaza);
        numeroCuenta = cci.substring(6, 18);
        int checkPlaza = cci.charAt(18) - '0';
        //LOGGER.info("checkPlaza = " + checkPlaza);
        int checkCuenta = cci.charAt(19) - '0';
        //LOGGER.info("checkCuenta = " + checkCuenta);
        //LOGGER.info("cci = " + mask.maskCuentaCCI(cci));

        if (isModulo10(codigoBancoPlaza, checkPlaza)) {
            return isModulo10(numeroCuenta, checkCuenta);
        }

        return false;
    }

    /**
     * Metodo que valida el CCI utilizando el algoritmo Modulo 10 (Luhn)
     *
     * @return
     */
    private static boolean isModulo10(String numeroCci, int check) {
        final int[][] sumTable = {{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, {0, 2, 4, 6, 8, 1, 3, 5, 7, 9}};
        int suma = 0;
        int flip = 0;
        int rest = 0;

        for (int i = 0; i < numeroCci.length(); i++, flip++) {
            suma += sumTable[flip & 0x1][numeroCci.charAt(i) - '0'];
        }

        rest = suma % 10;
        rest = (rest > 0) ? (10 - rest) : 0;

        return rest == check;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_HHmmSSsss);
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
        return dateToString(new Date(), new SimpleDateFormat(FORMAT_HHmmSSsss));
    }

    /**
     *
     * Retorna un string con la representacion de una fecha actual segun el
     * formato dd/MM/yyyy
     *
     * @return String representacion de la fecha segun formato
     */
    public static String dateToStringActual() {
        return dateToString(new Date(), new SimpleDateFormat(FORMAT_ddMMyyyy));
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

        return dateToString(fecha, new SimpleDateFormat(FORMAT_fechaConHora));
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_fechaConHora);
        return hoy.format(formatter);
    }

    public static String getMensajeProcedure(MessageSource messageSource, String codigo, String mensaje, String prefijo, String operacion) {
        String mensajeRetornar = "";

        if (COD_APROBAR.contains(codigo)) {
            if (!"80".equals(codigo)) {
                mensaje = messageSource.getMessage(prefijo + "." + codigo, null, Locale.getDefault());
            }
            return mensaje;
        }
        if (CODIGO_LIMITE.equals(codigo)) {
            mensaje = getMensajeLimite(messageSource, mensaje);
            if (mensaje.equals(PREFIJO_SIN_LIMITE)) {
                mensaje = messageSource.getMessage(prefijo + MENSAJE_LIMITE_ERROR, null, Locale.getDefault());
            }
        } else {

            //Validacion temporal, luego hay que mejorar el armado de los mensajes de error
//            if (!mensaje.isEmpty() && !mensaje.contains(Constantes.PUNTO) && !mensaje.contains("existe")) {
            if (!mensaje.isEmpty() && !mensaje.contains(".") && !mensaje.contains("existe")) {
                return mensaje;
            } else {
//                String key = prefijo + Constantes.PUNTO + codigo;
                String key = prefijo + "." + codigo;
                mensaje = messageSource.getMessage(key, null, Locale.getDefault());
                if (mensaje == null || mensaje.isEmpty()) {
                    if (operacion.equals(MODIFICAR)) {
                        mensaje = messageSource.getMessage(prefijo + MENSAJE_MODIFICAR_ERROR, null, Locale.getDefault());
                    } else if (operacion.equals(INSERTAR)) {
                        mensaje = messageSource.getMessage(prefijo + MENSAJE_INSERTAR_ERROR, null, Locale.getDefault());
                    }

                }
            }
        }
        return mensaje;
    }

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
                resultado = new SimpleDateFormat(FORMAT_ddMMyyyy).parse(strFecha);
            }
        } catch (ParseException ex) {
            //Loguea Error
        }

        return resultado;
    }
}
