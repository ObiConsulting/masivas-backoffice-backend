package com.novatronic.masivas.backoffice.util;

import com.novatronic.masivas.backoffice.dto.ReporteDTO;
import com.novatronic.masivas.backoffice.exception.JasperReportException;
import com.novatronic.novalog.audit.logger.NovaLogger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author Obi Consulting
 */
public class GenerarReporte {

    private static final NovaLogger LOGGER = NovaLogger.getLogger(GenerarReporte.class);

    private GenerarReporte() {
    }

    public static <T> ReporteDTO generarReporte(List<T> datos, Map<String, Object> parameters, String usuario, String tipoArchivo, String nombreReporte, String nombreArchivo, String logo) throws IOException {

        InputStream imgLogo = new ClassPathResource(logo).getInputStream();

        //Cabecera
        parameters.put("LOGO", imgLogo);
        parameters.put("IN_GENERADO", usuario);

        //Contenido
        parameters.put("IN_DATA_SOURCE", new JRBeanCollectionDataSource(datos));
        //General
        parameters.put("IN_TOTAL", datos.size());
        parameters.put("IN_FILE_TYPE", tipoArchivo);

        return GenerarReporte.exportJasper(nombreReporte, parameters, nombreArchivo, tipoArchivo);

    }

    public static ReporteDTO exportJasper(String jasperFile, Map<String, Object> parameters, String fileName, String tipoArchivo) {

        try {

            Resource classpathResourceJRXML = new ClassPathResource(jasperFile);
            InputStream inputStreamFile = classpathResourceJRXML.getInputStream();

            JasperDesign jasperDesign = JRXmlLoader.load(inputStreamFile);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            //Le agregamos la hora
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
            String hora = now.format(formatter12Hour).toUpperCase();
            parameters.put("IN_HORA", hora);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            byte[] reporte;
            String nombreArchivo;

            // Tipo Excel "XLSX"
            if (tipoArchivo.equals(ConstantesServices.TIPO_ARCHIVO_XLSX)) {
                nombreArchivo = generarNombreArchivo(fileName, ConstantesServices.PUNTO + ConstantesServices.TIPO_ARCHIVO_XLSX.toLowerCase());
                reporte = GenerarReporte.reporteExcel(jasperPrint);
                // Tipo PDF "PDF"
            } else {
                nombreArchivo = generarNombreArchivo(fileName, ConstantesServices.PUNTO + ConstantesServices.TIPO_ARCHIVO_PDF.toLowerCase());
                reporte = GenerarReporte.reportePDF(jasperPrint);
            }

            return new ReporteDTO(nombreArchivo, reporte);

        } catch (IOException | JRException e) {
            LOGGER.error(LogUtil.generarMensajeLogError(ConstantesServices.CODIGO_ERROR_JASPER,"Ocurrió un error al generar el reporte " + jasperFile,null), e);
            throw new JasperReportException(ConstantesServices.CODIGO_ERROR_JASPER, ConstantesServices.MENSAJE_ERROR_JASPER);
        }
    }

    public static String generarNombreArchivo(String nombreModulo, String extension) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formatoFecha = now.format(formatter);

        // Genera el nombre del archivo
        return nombreModulo + formatoFecha + extension;
    }

    public static byte[] reportePDF(JasperPrint jasperPrint) throws JRException {
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public static byte[] reporteExcel(JasperPrint jasperPrint) throws JRException {

        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
        JRXlsxExporter exporterXLS = new JRXlsxExporter();

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setCollapseRowSpan(true);
        configuration.setIgnoreCellBorder(false);
        configuration.setShowGridLines(true);
        configuration.setOnePagePerSheet(false);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        configuration.setRemoveEmptySpaceBetweenColumns(true);
        configuration.setWhitePageBackground(false);
        configuration.setDetectCellType(true);

        exporterXLS.setConfiguration(configuration);

        exporterXLS.setExporterInput(new SimpleExporterInput(jasperPrint));

        exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(excelStream));
        exporterXLS.exportReport();

        return excelStream.toByteArray();
    }

}
