package com.novatronic.masivas.backoffice.util;

/**
 *
 * @author Obi Consulting
 */
public class ConstantesServices {

    private ConstantesServices() {
    }

    public static final String MAP_LOGUEADOS = "loginCache";
    public static final String MAP_PARAMETROS = "parametrosPorGrupo";
    public static final String MAP_GRUPO_PARAMETROS = "grupoParametroCache";
    public static final String MAP_ENTIDADES = "entidadesCache";

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
    public static final String GUION = " - ";
    public static final String NEGATIVO = "-";
    public static final String COMILLA_SIMPLE = "'";

    public static final String CRONTAB_JAVA = "0";

    public static final String TIPO_ACCION_RESPALDAR = "Backup";
    public static final String TIPO_ACCION_RESTAURAR = "Restore";

    //Para los Estados
    public static final String ESTADO_INACTIVO = "0";
    public static final String ESTADO_ACTIVO = "1";
    public static final String ESTADO_INACTIVO_DESCRIPCION = "Inactivo";
    public static final String ESTADO_ACTIVO_DESCRIPCION = "Activo";

    //Para los Estados de los archivos
    public static final String CODIGOS_ESTADOS_ARCHIVO = "0700,0701,0702,0703";
    public static final String ID_GRUPO_CATEGORIA_DIRECTORIO = "1";
    public static final String ID_GRUPO_TIPO_ARCHIVO = "3";
    public static final String ID_GRUPO_EXTENSION_BASE = "4";
    public static final Long ID_GRUPO_EXTENSION_BASE_L = 4l;
    public static final String ID_GRUPO_EXTENSION_CONTROL = "6";
    public static final Long ID_GRUPO_EXTENSION_CONTROL_L = 6l;
    public static final String ID_GRUPO_MONEDA = "7";
    public static final String ID_GRUPO_ESTADO_ARCHIVOS = "8";
    public static final String ID_GRUPO_TIPO_TRANSACCION = "9";
    public static final String ID_GRUPO_MOTIVO_RECHAZO = "10";
    public static final String ID_GRUPO_RPTA_OPERADORA = "11";

    //Respaldar/Restaurar
    public static final String COD_SERVER_MASIVAS = "3000";
    public static final String COD_ACCION_RESPALDAR = "5";
    public static final String COD_ACCION_RESTAURAR = "6";
    public static final String TIPO_DIRECTORIO = "DIR";
    public static final String TIPO_MASIVAS = "MAS";
    public static final String TIPO_TITULARIDAD = "TIT";

    public static final String CODIGO_OK_WS = "0000";
    public static final String CODIGO_OK = "00";

    public static final String TIPO_ARCHIVO_PDF = "PDF";
    public static final String TIPO_ARCHIVO_XLSX = "XLSX";

    public static final String OPERACION_EDITAR = "EDITAR";

    //Para Entidad Financiera
    public static final Long ID_PERFIL = 1L;
    public static final String NO_PROPIETARIO = "0";
    public static final String PROPIETARIO = "1";

    //Para log
    public static final String MENSAJE_TRAZABILIDAD = "Procesando [{}]. Método [{}]. Valores: {}";
    public static final String MENSAJE_TRAZABILIDAD_ACCION = "Procesando [{}]. Valores: {}";
    public static final String MENSAJE_TRAZABILIDAD_PROCESO = "Procesando [{}].";
    public static final String MENSAJE_TRAZABILIDAD_RESULTADOS = "Total de registros obtenidos: [{}]";
    public static final String MENSAJE_TRAZABILIDAD_RESULTADOS_REPORTE = "Total de registros obtenidos {}: [{}]";
    public static final String MENSAJE_TRAZABILIDAD_API_RESPONSE = "Respuesta de API {{}}: [{}]";

    public static final String METODO_REGISTRAR = "Registrar";
    public static final String METODO_CONSULTAR = "Consultar";
    public static final String METODO_ACTUALIZAR = "Actualizar";
    public static final String METODO_OBTENER = "Obtener";
    public static final String METODO_ACTIVAR_DESACTIVAR = "Activar/Desactivar";
    public static final String METODO_DESCARGAR = "Descargar";

    public static final String LOGIN = "Login";
    public static final String LOGOUT = "Logout";
    public static final String CAMBIAR_CONTRASENA = "Cambiar Contraseña";
    public static final String GRUPO_PARAMETRO = "Grupo Parámetro";
    public static final String PARAMETRO = "Parámetro";
    public static final String ENTIDAD_FINANCIERA = "Entidad Financiera";
    public static final String ARCHIVO_DIRECTORIO = "Archivo Directorio";
    public static final String ARCHIVO_MASIVAS = "Archivo Masivas";
    public static final String ARCHIVO_TITULARIDAD = "Archivo Titularidad";
    public static final String RUTA = "Ruta de Archivos";
    public static final String PROCESO = "Scheduler de procesos";
    public static final String APLICACION = "Aplicación";
    public static final String DETALLE_MASIVAS = "Detalle de Archivos Masivas";
    public static final String REPORTE_CIERRE = "Reporte Cierre";
    public static final String REPORTE_TOTALIZADO = "Reporte Totalizado";
    public static final String REPORTE_CONSOLIDADO = "Reporte Consolidado por Entidad Destino";
    public static final String RESPALDAR_ARCHIVO = "Respaldar Archivo";
    public static final String RESTAURAR_ARCHIVO = "Restaurar Archivo";
    public static final String REFRESCAR_CACHE_CORE = "Refrescar Cache del Core";

    //Para reporte
    public static final String PARAM_IN_ESTADO = "IN_ESTADO";

    //Para auditoria
    public static final String ACCION_CREATE = "create";
    public static final String ACCION_READ = "read";
    public static final String ACCION_UPDATE = "update";
    public static final String ACCION_DELETE = "delete";
    public static final String ACCION_EXPORT = "export";
    public static final String ACCION_VIEW = "view";
    public static final String ACCION_LOGIN = "login";
    public static final String ACCION_LOGOUT = "logout";
    public static final String ACCION_CHANGE_PASSWORD = "changePassword";
    public static final String ACCION_PERMISSION = "permission";
    public static final String ACCION_BACKUP_RESTORE = "respaldar/restaurar";
    public static final String TABLA_ENTIDAD = "TP_ENTIDAD";
    public static final String TABLA_GRUPO_PARAMETRO = "TP_GRUPO_PARAMETRO";
    public static final String TABLA_PARAMETRO = "TS_DETALLE_PARAMETRO";
    public static final String TABLA_ARCHIVO_DIRECTORIO = "TP_ARCHIVO_DIR";
    public static final String TABLA_ARCHIVO_MASIVAS = "TP_ARCHIVO_MAS";
    public static final String TABLA_ARCHIVO_TITULARIDAD = "TP_ARCHIVO_TIT";
    public static final String TABLA_RUTA = "TP_RUTA";
    public static final String TABLA_PROCESO = "TP_PROCESO";
    public static final String TABLA_DETALLE_MASIVAS = "TP_DETALLE_MASIVAS";
    public static final String TABLA_APLICACION = "TP_APLICACION";
    public static final String INTEGRACION_SCA = "SCA";

    public static final String AUDIT_CAMPO_GRUPO_PARAMETRO = "idGrupoParametro=";
    public static final String AUDIT_CAMPO_USUARIO = "usuario=";
    public static final String AUDIT_CAMPO_PASSWORD = ",password=";

    //Codigos del Sistema
    public static final String RESPUESTA_OK_API = "0000";
    public static final String ERROR_BADREQUEST = "0001";
    public static final String CODIGO_ERROR_API_CORE = "0096";
    public static final String CODIGO_ERROR_JASPER = "0097";
    public static final String CODIGO_ERROR_BD = "0098";
    public static final String CODIGO_ERROR_GENERICO = "0099";
    public static final String RESPUESTA_ERROR_9999 = "9999";

    public static final String CODIGO_ERROR_COD_ENTIDAD_UNICA = "0010";
    public static final String CODIGO_ERROR_COD_GRUPO_PARAMETRO_UNICO = "0011";
    public static final String CODIGO_ERROR_COD_PARAMETRO_GRUPO_PARAMETRO_UNICO = "0012";
    public static final String CODIGO_ERROR_COD_OPERACION_NO_ENCONTRADA = "0013";
    public static final String CODIGO_ERROR_COD_APLICACION_UNICA = "0014";
    public static final String CODIGO_ERROR_API_CORE_ACCION = "0015";
    public static final String CODIGO_ERROR_API_CORE_RECARGAR = "0016";

    //Mensajes del Sistema
    //Aplicación
    public static final String MENSAJE_EXITO_CREAR_APLICACION = "Aplicación creada correctamente";
    public static final String MENSAJE_EXITO_EDITAR_APLICACION = "Aplicación editada correctamente";
    public static final String MENSAJE_EXITO_ACTIVAR_APLICACION = "Aplicación activada correctamente";
    public static final String MENSAJE_EXITO_ACTIVAR_APLICACIONES = "Aplicaciones activadas correctamente";
    public static final String MENSAJE_EXITO_DESACTIVAR_APLICACION = "Aplicación desactivada correctamente";
    public static final String MENSAJE_EXITO_DESACTIVAR_APLICACIONES = "Aplicaciones desactivadas correctamente";
    public static final String MENSAJE_EXITO_PARCIAL_ACTIVAR_APLICACIONES = "Se activaron %d aplicaciones. %d aplicaciones no pudieron ser activadas.";
    public static final String MENSAJE_EXITO_PARCIAL_DESACTIVAR_APLICACIONES = "Se desactivaron %d aplicaciones. %d aplicaciones no pudieron ser desactivadas.";
    public static final String MENSAJE_ERROR_ACTIVAR_APLICACION = "Error al activar la aplicación";
    public static final String MENSAJE_ERROR_ACTIVAR_APLICACIONES = "Error al activar las aplicaciones";
    public static final String MENSAJE_ERROR_DESACTIVAR_APLICACION = "Error al desactivar la aplicación";
    public static final String MENSAJE_ERROR_DESACTIVAR_APLICACIONES = "Error al desactivar las aplicaciones";
    //Entidad
    public static final String MENSAJE_EXITO_CREAR_ENTIDAD = "Entidad creada correctamente";
    public static final String MENSAJE_EXITO_EDITAR_ENTIDAD = "Entidad editada correctamente";
    public static final String MENSAJE_EXITO_ACTIVAR_ENTIDAD = "Entidad activada correctamente";
    public static final String MENSAJE_EXITO_ACTIVAR_ENTIDADES = "Entidades activadas correctamente";
    public static final String MENSAJE_EXITO_DESACTIVAR_ENTIDAD = "Entidad desactivada correctamente";
    public static final String MENSAJE_EXITO_DESACTIVAR_ENTIDADES = "Entidades desactivadas correctamente";
    public static final String MENSAJE_EXITO_PARCIAL_ACTIVAR_ENTIDADES = "Se activaron %d entidades. %d entidades no pudieron ser activadas.";
    public static final String MENSAJE_EXITO_PARCIAL_DESACTIVAR_ENTIDADES = "Se desactivaron %d entidades. %d entidades no pudieron ser desactivadas.";
    public static final String MENSAJE_ERROR_ACTIVAR_ENTIDAD = "Error al activar la entidad";
    public static final String MENSAJE_ERROR_ACTIVAR_ENTIDADES = "Error al activar las entidades";
    public static final String MENSAJE_ERROR_DESACTIVAR_ENTIDAD = "Error al desactivar la entidad";
    public static final String MENSAJE_ERROR_DESACTIVAR_ENTIDADES = "Error al desactivar las entidades";
    //Ruta
    public static final String MENSAJE_EXITO_EDITAR_RUTA = "Ruta editada correctamente";
    //Scheduler
    public static final String MENSAJE_EXITO_EDITAR_SCHEDULER = "Scheduler editado correctamente";
    //Archivos
    public static final String MENSAJE_EXITO_RESPALDAR_ARCHIVO = "Archivo respaldado correctamente";
    public static final String MENSAJE_EXITO_RESTAURAR_ARCHIVO = "Archivo restaurado correctamente";
    //Grupo Parámetro
    public static final String MENSAJE_EXITO_CREAR_GRUPO_PARAMETRO = "Grupo Parámetro creado correctamente";
    public static final String MENSAJE_EXITO_EDITAR_GRUPO_PARAMETRO = "Grupo Parámetro editado correctamente";
    public static final String MENSAJE_EXITO_ACTIVAR_GRUPO_PARAMETRO = "Grupo Parámetro activado correctamente";
    public static final String MENSAJE_EXITO_ACTIVAR_GRUPOS_PARAMETROS = "Grupos Parámetros activados correctamente";
    public static final String MENSAJE_EXITO_DESACTIVAR_GRUPO_PARAMETRO = "Grupo Parámetro desactivado correctamente";
    public static final String MENSAJE_EXITO_DESACTIVAR_GRUPOS_PARAMETROS = "Grupos Parámetros desactivados correctamente";
    public static final String MENSAJE_EXITO_PARCIAL_ACTIVAR_GRUPOS_PARAMETROS = "Se activaron %d grupos parámetros. %d grupos parámetros no pudieron ser activados.";
    public static final String MENSAJE_EXITO_PARCIAL_DESACTIVAR_GRUPOS_PARAMETROS = "Se desactivaron %d grupos parámetros. %d grupos parámetros no pudieron ser desactivados.";
    public static final String MENSAJE_ERROR_ACTIVAR_GRUPO_PARAMETRO = "Error al activar el grupo parámetro";
    public static final String MENSAJE_ERROR_ACTIVAR_GRUPOS_PARAMETROS = "Error al activar los grupos parámetros";
    public static final String MENSAJE_ERROR_DESACTIVAR_GRUPO_PARAMETRO = "Error al desactivar el grupo parámetro";
    public static final String MENSAJE_ERROR_DESACTIVAR_GRUPOS_PARAMETROS = "Error al desactivar los grupos parámetros";
    //Parámetro
    public static final String MENSAJE_EXITO_CREAR_PARAMETRO = "Parámetro creado correctamente";
    public static final String MENSAJE_EXITO_EDITAR_PARAMETRO = "Parámetro editado correctamente";
    public static final String MENSAJE_EXITO_ACTIVAR_PARAMETRO = "Parámetro activado correctamente";
    public static final String MENSAJE_EXITO_ACTIVAR_PARAMETROS = "Parámetros activados correctamente";
    public static final String MENSAJE_EXITO_DESACTIVAR_PARAMETRO = "Parámetro desactivado correctamente";
    public static final String MENSAJE_EXITO_DESACTIVAR_PARAMETROS = "Parámetros desactivados correctamente";
    public static final String MENSAJE_EXITO_PARCIAL_ACTIVAR_PARAMETROS = "Se activaron %d parámetros. %d parámetros no pudieron ser activados.";
    public static final String MENSAJE_EXITO_PARCIAL_DESACTIVAR_PARAMETROS = "Se desactivaron %d parámetros. %d parámetros no pudieron ser desactivados.";
    public static final String MENSAJE_ERROR_ACTIVAR_PARAMETRO = "Error al activar el parámetro";
    public static final String MENSAJE_ERROR_ACTIVAR_PARAMETROS = "Error al activar los parámetros";
    public static final String MENSAJE_ERROR_DESACTIVAR_PARAMETRO = "Error al desactivar el parámetro";
    public static final String MENSAJE_ERROR_DESACTIVAR_PARAMETROS = "Error al desactivar los parámetros";

    public static final String MENSAJE_EXITO_BUSCAR_OPERACION = "Operación buscada correctamente";
    public static final String MENSAJE_EXITO_DESCARGAR_OPERACION = "Operación descargada correctamente";
    public static final String MENSAJE_EXITO_OBTENER_OPERACION = "Operación obtenida correctamente";
    public static final String MENSAJE_EXITO_CONSULTA_OPERACION = "Operación consultada correctamente";
    public static final String MENSAJE_EXITO_INICIAR_SESION = "Sesión iniciada correctamente";
    public static final String MENSAJE_EXITO_CERRAR_SESION = "Sesión cerrada correctamente";
    public static final String MENSAJE_EXITO_CAMBIAR_CONTRASENA = "Cambio de contraseña realizado correctamente";
    public static final String MENSAJE_EXITO_GENERICO = "Operación completada correctamente";

    public static final String MENSAJE_ERROR_GENERICO = "Se ha producido un error inesperado. Por favor, inténtalo de nuevo en unos minutos.";
    public static final String MENSAJE_ERROR_BD = "Se ha producido un error con la base de datos. Por favor, inténtalo de nuevo en unos minutos.";
    public static final String MENSAJE_BAD_REQUEST = "Campos Inesperados";
    public static final String MENSAJE_ERROR_COD_ENTIDAD_UNICA = "El codigo entidad ya existe";
    public static final String MENSAJE_ERROR_COD_GRUPO_PARAMETRO_UNICO = "El codigo grupo parámetro ya existe";
    public static final String MENSAJE_ERROR_COD_PARAMETRO_GRUPO_PARAMETRO_UNICO = "El código ingresado ya existe dentro del mismo grupo.";
    public static final String MENSAJE_ERROR_OPERACION_NO_ENCONTRADA = "Operación no encontrada";
    public static final String MENSAJE_ERROR_COD_APLICACION_UNICA = "El codigo aplicación ya existe";
    public static final String MENSAJE_ERROR_JASPER = "Se ha producido un error al generar el archivo jasper. Por favor, inténtalo de nuevo en unos minutos.";
    public static final String MENSAJE_ERROR_EXCEPTION = "Se ha producido un error. Por favor, inténtalo de nuevo en unos minutos.";
    public static final String MENSAJE_ERROR_API_CORE = "Se ha producido un error en la comunicación con la api core. Por favor, inténtalo de nuevo en unos minutos.";
    public static final String MENSAJE_ERROR_API_CORE_ACCION = "Se ha producido un error ejecutar la acción respaldar/restaurar. Por favor, inténtalo de nuevo en unos minutos.";
    public static final String MENSAJE_ERROR_API_CORE_RECARGAR = "Se ha producido un error ejecutar la acción recargar. Por favor, inténtalo de nuevo en unos minutos.";

}
