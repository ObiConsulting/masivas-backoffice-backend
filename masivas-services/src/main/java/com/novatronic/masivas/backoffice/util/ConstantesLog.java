package com.novatronic.masivas.backoffice.util;

public class ConstantesLog {
    private ConstantesLog() {}
    public static final String PRODUCT_NAME="msvbasbo";

    //Archivos de configuracion
    public static final String ARCHIVO_CONFIG_MENSAJE = "mensaje.properties";
    //Valores Configurables
    public static final Boolean PRODUCTION= false;

    public static final String MODULO_ORIGEN="HOST";
    public static final String MODULO_DESTINO="MSV-WEB";
    public static final String ESP="";
    public static final String PROP_NO_ENCONTRADA="No se encontró ninguna entidad con propietario = 1.";

    //Mensajes descarga PDF EXCEL
    public static final String EXITO_DESCARGA_MASIVAS="Descarga correcta del archivo de reporte masivas";
    public static final String EXITO_DESCARGA_DETALLE_MASIVAS="Descarga correcta del archivo de reporte detalle masivas";
    public static final String EXITO_DESCARGA_TITULARIDAD="Descarga correcta del archivo de reporte titularidad";
    public static final String EXITO_DESCARGA_DIRECTORIO="Descarga correcta del archivo de reporte directorio";
    public static final String EXITO_DESCARGA_CONSOLIDAD="Descarga correcta del archivo de reporte consolidado";

    //Mensajes descarga PDF EXCEL para administración
    public static final String EXITO_DESCARGA_APLICACION="Descarga correcta del archivo de reporte aplicacion";
    public static final String EXITO_DESCARGA_ENTIDAD="Descarga correcta del archivo de reporte entidad";
    public static final String EXITO_DESCARGA_RUTA="Descarga correcta del archivo de reporte ruta";
    public static final String EXITO_DESCARGA_PARAMETRO="Descarga correcta del archivo de reporte parámetro";
    public static final String EXITO_DESCARGA_GRUPO_PARAMETRO="Descarga correcta del archivo de reporte grupo parámetro";

    //Errores descarga PDF EXCEL
    public static final String ERROR_DESCARGA_GENERAL="Error en la descarga del archivo de reporte";
    public static final String CODIGO_ERROR_DESCARGA_GENERAL="8888";

    //Errores reporte
    public static final String ERROR_REPORTE_TOTALIZADO_GENERAL="Error en la generación del reporte totalizado";
    public static final String CODIGO_REPORTE_TOTALIZADO_GENERAL="8888";

    //Error reporte
    public static final String ERROR_BUSQUEDA_GENERAL="Error en la búsqueda";
    public static final String CODIGO_BUSQUEDA_GENERAL="8888";

}
