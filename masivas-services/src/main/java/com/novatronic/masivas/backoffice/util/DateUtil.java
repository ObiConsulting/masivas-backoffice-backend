package com.novatronic.masivas.backoffice.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    // Fijo y thread-safe
    private static final ZoneId ZONE_LIMA = ZoneId.of("America/Lima");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    /** Devuelve timestamp de 17 chars: yyyyMMddHHmmssSSS */
    public static String getCurrentTimestamp() {
        String ts = ZonedDateTime.now(ZONE_LIMA).format(FORMATTER);
        if (ts.length() != 17) {
            // Normalizamos: si sobra, recortamos; si falta, completamos con '0' al final.
            ts = ts.length() > 17 ? ts.substring(0, 17) : String.format("%-17s", ts).replace(' ', '0');
        }
        return ts;
    }

}