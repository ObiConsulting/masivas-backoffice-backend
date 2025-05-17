/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.novatronic.masivas.backoffice.util;

/**
 *
 * @author obi
 */
public class Constantes {

    public static final String IN_TAM_PAGINA = "IN_TAM_PAGINA";

    public static final Integer TAMANHO_PAGINA_DEFAULT = 10;
    public static final Integer PAGINA_ACTUAL_DEFAULT = 1;

    public static final int CERO = 0;
    public static final int UNO = 1;
    public static final int DOS = 2;
    public static final int TRES = 3;
    public static final int CUATRO = 4;
    public static final int CINCO = 5;
    public static final int SEIS = 6;

    public static final String IN_ID_APROBACION = "IN_ID_APROBACION";
    public static final String IN_COD_ENT_FINANCIERA = "IN_COD_ENT_FINANCIERA";

    public static final String PACKAGE_BD = "PKG_BACKOFFICE_LBTR";
    public static final String OUT_CURSOR = "OUT_CURSOR";
    public static final String OUT_ID_OPERACION = "OUT_ID_OPERACION";
    public static final String OUT_CODIGO_RESULT = "OUT_CODIGO_RESULT";
    public static final String OUT_CODIGO_SQLERROR = "OUT_CODIGO_SQLERROR";
    public static final String OUT_CODIGO_SEQ = "OUT_CODIGO_SEQ";

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

    public static final String OK_BD = "00";
    public static final String ERROR_SP_BD = "99";
    public static final String ERROR_VALIDAMONTO = "88";

    public static final String OK_WS = "0000";
    public static final String MENSAJE_ERROR_VALIDAMONTO = "El monto total del REPO debe ser mayor o igual al monto adjudicado";

    public static final String ERROR_BD = "ERR_DB";
    public static final String MENSAJE_ERROR_BD_USUARIO = "Ocurrio un Problema, Revise Log";

    // ## SP NAME INTERBANCARIA
    public static final String SPI_INSERTAR_CLIENTE = "SPI_INSERTAR_CLIENTE";
    public static final String SPS_DATA_CLIENTE = "SPS_DATA_CLIENTE";

    public static final String SPI_INSERTAR_INTERBANCARIA = "SPI_INSERTAR_INTERBANCARIA";
    public static final String SPU_MODIFICAR_INTERBANCARIA = "SPU_MODIFICAR_INTERBANCARIA";
    public static final String SPS_BUSCAR_INTERBANCARIA = "SPS_BUSCAR_INTERBANCARIA";
    public static final String SPS_DATA_INTERBANCARIA = "SPS_DATA_INTERBANCARIA";
    public static final String SPU_ANULAR_INTERBANCARIA = "SPU_ANULAR_INTERBANCARIA";

    // ## SP NAME MONEDA
    public static final String SPS_BUSCAR_COMPRA_MONEDA = "SPS_BUSCAR_COMPRA_MONEDA";
    public static final String SPI_INSERTAR_COMPRA_MONEDA = "SPI_INSERTAR_COMPRA_MONEDA";
    public static final String SPU_MODIFICAR_COMPRA_MONEDA = "SPU_MODIFICAR_COMPRA_MONEDA";
    public static final String SPS_DATA_COMPRA_MONEDA = "SPS_DATA_COMPRA_MONEDA";
    public static final String SPU_ANULAR_COMPRA_MONEDA = "SPU_ANULAR_COMPRA_MONEDA";

    // # VENTA MONEDA
    public static final String SPS_BUSCAR_VENTA_MONEDA = "SPS_BUSCAR_VENTA_MONEDA";
    public static final String SPS_DATA_VENTA_MONEDA = "SPS_DATA_VENTA_MONEDA";
    
    
    
    // ## SP NAME REPOOVERNIGHT
    public static final String SPI_INSERTAR_REPOOVERNIGHT = "SPI_INSERTAR_REPOOVERNIGHT";
    public static final String SPU_MODIFICAR_REPOOVERNIGHT = "SPU_MODIFICAR_REPOOVERNIGHT";
    public static final String SPU_ANULAR_REPOOVERNIGHT = "SPU_ANULAR_REPOOVERNIGHT";
    public static final String SPS_BUSCAR_REPOOVERNIGHT = "SPS_BUSCAR_REPOOVERNIGHT";
    public static final String SPS_DATA_REPOOVERNIGHT = "SPS_DATA_REPOOVERNIGHT";

    // ## SP NAME REPOINTRADIA
    public static final String SPI_INSERTAR_REPOINTRADIA = "SPI_INSERTAR_REPOINTRADIA";
    public static final String SPU_MODIFICAR_REPOINTRADIA = "SPU_MODIFICAR_REPOINTRADIA";
    public static final String SPU_ANULAR_REPOINTRADIA = "SPU_ANULAR_REPOINTRADIA";
    public static final String SPS_BUSCAR_REPOINTRADIA = "SPS_BUSCAR_REPOINTRADIA";
    public static final String SPS_DATA_REPOINTRADIA = "SPS_DATA_REPOINTRADIA";
    
    // ## SP NAME FACILIDADINTRADIA
    public static final String SPI_INSERTAR_FACILIDADINTRADIA = "SPI_INSERTAR_FACILIDADINTRA";
    public static final String SPU_MODIFICAR_FACILIDADINTRADIA = "SPU_MODIFICAR_FACILIDADINTRA";
    public static final String SPU_ANULAR_FACILIDADINTRADIA = "SPU_ANULAR_FACILIDADINTRA";
    public static final String SPS_BUSCAR_FACILIDADINTRA = "SPS_BUSCAR_FACILIDADINTRA";
    public static final String SPS_DATA_FACILIDADINTRA = "SPS_DATA_FACILIDADINTRA";

    // ## SP NAME CAVALI
    public static final String SPS_BUSCAR_TRANSFCAVALI = "SPS_BUSCAR_TRANSFCAVALI";
    public static final String SPI_INSERTAR_TRANSFCAVALI = "SPI_INSERTAR_TRANSFCAVALI";
    public static final String SPU_MODIFICAR_TRANSFCAVALI = "SPU_MODIFICAR_TRANSFCAVALI";
    public static final String SPS_DATA_TRANSFCAVALI = "SPS_DATA_TRANSFCAVALI";
    public static final String SPU_ANULAR_TRANSFCAVALI = "SPU_ANULAR_TRANSFCAVALI";
    
    // ## SP NAME SUBASTA Y TENENCIA
    public static final String SPI_INSERTAR_SUBASTA = "SPI_INSERTAR_SUBASTA";
    public static final String SPI_INSERTAR_TENENCIA = "SPI_INSERTAR_TENENCIA";
    // Elimina las tenencias de una operacion
    public static final String SPU_ANULAR_TENENCIA = "SPU_ANULAR_TENENCIA";
    // Elimia las subastas de una operacion
    public static final String SPU_ANULAR_SUBASTA = "SPU_ANULAR_SUBASTA";
    public static final String SPS_BUSCAR_TENENCIA = "SPS_BUSCAR_TENENCIA";
    public static final String SPS_DATA_OP_SUBASTA = "SPS_DATA_OP_SUBASTA";

    // ## SP APROBACIONES
    public static final String SPS_BUSCAR_APROBACION = "SPS_BUSCAR_APROBACION";
    public static final String SPS_BUSCAR_OPERACION_APROB = "SPS_BUSCAR_OPERACION_APROB";
    public static final String SPS_BUSCAR_CONFIRMAR_INTER = "SPS_BUSCAR_CONFIRMAR_INTER";
    public static final String SPI_INSERTAR_OPERACION_APROB = "SPI_INSERTAR_OPERACION_APROB";

    // ## SP NAME OVERNIGHT
    public static final String SPI_INSERTAR_OVERNIGHT = "SPI_INSERTAR_OVERNIGHT";
    public static final String SPU_MODIFICAR_OVERNIGHT = "SPU_MODIFICAR_OVERNIGHT";
    public static final String SPU_ANULAR_OVERNIGHT = "SPU_ANULAR_OVERNIGHT";
    public static final String SPS_BUSCAR_OVERNIGHT = "SPS_BUSCAR_OVERNIGHT";
    public static final String SPS_DATA_OVERNIGHT = "SPS_DATA_OVERNIGHT";

}
