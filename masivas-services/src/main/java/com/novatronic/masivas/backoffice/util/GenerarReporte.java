package com.novatronic.masivas.backoffice.util;

import java.io.ByteArrayOutputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 *
 * @author Obi Consulting
 */
public class GenerarReporte {

    public static byte[] reportePDF(JasperPrint jasperPrint, String nombreArchivo) throws JRException {
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public static byte[] reporteExcel(JasperPrint jasperPrint, String nombreArchivo) throws JRException {

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
