package com.novatronic.masivas.backoffice.log;

import com.novatronic.novalog.audit.logger.NovaLogger;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.novatronic.masivas.backoffice.util.ConstantesLog.PRODUCT_NAME;

public class LogPerformance {
    private static final String LONGITUD_PROD_NAME="%1$-16s";
    private static final NovaLogger LOG = NovaLogger.getLogger(LogPerformance.class);
    private static final String DATE_FORMAT = "HH:mm:ss.SSS";
    private static final String SPACE=" ";

    public static String showTrx(String className, String methodName, long init) {
        long end = System.currentTimeMillis();
        return showTrx(className, methodName, init, end);
    }

    private static String showTrx(String className, String methodName, long init, long end) {
        long total = end - init;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        LOG.info(String.format(LONGITUD_PROD_NAME, PRODUCT_NAME) + SPACE + String.format(LONGITUD_PROD_NAME, PRODUCT_NAME) + SPACE
                + sdf.format(new Date(init)) + SPACE + sdf.format(new Date(end)) + SPACE + String.format("%1$-5s", total) + SPACE
                + className + "." + methodName);

        return String.format(LONGITUD_PROD_NAME, PRODUCT_NAME) + SPACE + String.format(LONGITUD_PROD_NAME, PRODUCT_NAME) + SPACE
                + sdf.format(new Date(init)) + SPACE + sdf.format(new Date(end)) + SPACE + String.format("%1$-5s", total) + SPACE
                + className + "." + methodName;
    }
}
