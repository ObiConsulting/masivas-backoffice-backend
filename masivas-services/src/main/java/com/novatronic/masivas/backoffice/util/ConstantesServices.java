package com.novatronic.masivas.backoffice.util;

/**
 *
 * @author Obi Consulting
 */
public class ConstantesServices {

    public static final String MAP_PARAMETROS = "parametrosPorGrupo";
    public static final String MAP_CONCEPTOS = "conceptosCache";
    public static final String MAP_FACILIDADES = "facilidadesCache";
    public static final String MAP_CODIGOSRESPUESTA = "codigoRespuestaCache";
    public static final String MAP_LOGUEADOS = "loginCache";

    public static final String PERMISO_PRIMER_LOGIN = "PRIMER_LOGIN";
    public static final String PERMISO_USUARIO_VALIDO = "USUARIO_VALIDO";

    public static final Integer TAMANO_PAGINA_20 = 20;
    public static final Integer TAMANO_PAGINA_10 = 10;
    public static final Integer PAGINA_INICIAL = 0;

    public static final String ORDEN_ASC = "ASC";
    public static final String ORDEN_DESC = "DESC";

    public static final String BLANCO = " ";
    public static final String VACIO = "";
    public static final String COMA = ",";
    public static final String PUNTO = ".";
    public static final String PUNTOCOMA = ";";
    public static final String IGUAL = "=";
    public static final String DOSPUNTOS = ":";
    public static final String CAMBIO_LINEA = "\n";
    public static final String GION = " - ";
    public static final String NEGATIVO = "-";
    public static final String COMILLA_SIMPLE = "'";

    public static final String ESTADO_INACTIVO = "0";
    public static final String ESTADO_ACTIVO = "1";
    public static final String ESTADO_INACTIVO_DESCRIPCION = "INACTIVO";
    public static final String ESTADO_ACTIVO_DESCRIPCION = "ACTIVO";

    public static final String PREFIJO_NUM_REF_HOST_LBTR = "LBTR";
    public static final String CODIGO_OK_WS = "0000";
    public static final String CODIGO_OK = "00";

    public static final String TIPO_ARCHIVO_PDF = "PDF";
    public static final String TIPO_ARCHIVO_XLSX = "XLSX";

    public static final String OPERACION_EDITAR = "EDITAR";

    //Para auditoria
    public static final String ACCION_CREATE = "create";
    public static final String ACCION_READ = "read";
    public static final String ACCION_UPDATE = "update";
    public static final String ACCION_DELETE = "delete";
    public static final String ACCION_EXPORT = "export";
    public static final String ACCION_VIEW = "view";
    public static final String TABLA_ENTIDAD = "TP_ENTIDAD";
    public static final String TABLA_GRUPO_PARAMETRO = "TP_GRUPO_PARAMETRO";
    public static final String TABLA_PARAMETRO = "TS_DETALLE_PARAMETRO";
    public static final String TABLA_ARCHIVO_DIRECTORIO = "TP_ARCHIVO_DIR";

    //Codigos del Sistema
    public static final String RESPUESTA_OK_API = "0000";
    public static final String CODIGO_ERROR_BD = "0098";
    public static final String CODIGO_ERROR_GENERICO = "0099";
    public static final String ERROR_BADREQUEST = "0001";

    public static final String CODIGO_ERROR_COD_ENTIDAD_UNICO = "0010";
    public static final String CODIGO_ERROR_COD_GRUPO_PARAMETRO_UNICO = "0011";
    public static final String CODIGO_ERROR_COD_PARAMETRO_UNICO = "0012";

    //Mensajes del Sistema
    public static final String MENSAJE_EXITO_CREAR_OPERACION = "Operación creada correctamente";
    public static final String MENSAJE_EXITO_BUSCAR_OPERACION = "Operación buscada correctamente";
    public static final String MENSAJE_EXITO_EDITAR_OPERACION = "Operación editada correctamente";
    public static final String MENSAJE_EXITO_ACTIVAR_OPERACION = "Operación activada correctamente";
    public static final String MENSAJE_EXITO_ACTIVAR_OPERACIONES = "Operaciones activadas correctamente";
    public static final String MENSAJE_EXITO_DESACTIVAR_OPERACION = "Operación desactivada correctamente";
    public static final String MENSAJE_EXITO_DESACTIVAR_OPERACIONES = "Operaciones desactivadas correctamente";
    public static final String MENSAJE_EXITO_DESCARGAR_OPERACION = "Operación descargada correctamente";
    public static final String MENSAJE_EXITO_OBTENER_OPERACION = "Operación obtenida correctamente";
    public static final String MENSAJE_EXITO_GENERICO = "Operación completada correctamente";

    public static final String MENSAJE_ERROR_CREAR_OPERACION = "Falló al crear operación";
    public static final String MENSAJE_ERROR_BUSCAR_OPERACION = "Falló al buscar operación";
    public static final String MENSAJE_ERROR_EDITAR_OPERACION = "Falló al editar operación";
    public static final String MENSAJE_ERROR_ACTIVAR_OPERACION = "Falló al activar operación";
    public static final String MENSAJE_ERROR_ACTIVAR_OPERACIONES = "Falló al activar operaciones";
    public static final String MENSAJE_ERROR_DESACTIVAR_OPERACION = "Falló al desactivar operación";
    public static final String MENSAJE_ERROR_DESACTIVAR_OPERACIONES = "Falló al desactivar operaciones";
    public static final String MENSAJE_ERROR_DESCARGAR_OPERACION = "Falló al descargar operación";
    public static final String MENSAJE_ERROR_OBTENER_OPERACION = "Falló al obtener operación";

    public static final String MENSAJE_EXITO_PARCIAL_ACTIVAR_OPERACIONES = "Se activaron %d operaciones. %d operaciones no pudieron ser activadas.";
    public static final String MENSAJE_EXITO_PARCIAL_DESACTIVAR_OPERACIONES = "Se desactivaron %d operaciones. %d operaciones no pudieron ser desactivadas.";

    public static final String MENSAJE_ERROR_GENERICO = "Se ha producido un error inesperado. Por favor, inténtalo de nuevo en unos minutos.";
    public static final String MENSAJE_ERROR_BD = "Se ha producido un error con la base de datos. Por favor, inténtalo de nuevo en unos minutos.";
    public static final String MENSAJE_BAD_REQUEST = "Campos Inesperados";
    public static final String MENSAJE_ERROR_COD_ENTIDAD_UNICO = "El codigo entidad ya existe";
    public static final String MENSAJE_ERROR_COD_GRUPO_PARAMETRO_UNICO = "El codigo grupo parámetro ya existe";
    public static final String MENSAJE_ERROR_COD_PARAMETRO_UNICO = "El codigo parámetro ya existe";

}
