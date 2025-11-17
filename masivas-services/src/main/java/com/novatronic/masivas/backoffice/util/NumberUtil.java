package com.novatronic.masivas.backoffice.util;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberUtil {
    private static final AtomicInteger SEQ = new AtomicInteger(1);
    /**Aleatorio de 3 dígitos: */
    public static String getRandomNumber() {
        int n = ThreadLocalRandom.current().nextInt(1, 1000);
        return String.format(Locale.ROOT, "%03d", n);
    }
    /** Secuencial 000..999 y vuelve a 000 */
    public static String nextSequence3() {
        int n = Math.floorMod(SEQ.getAndIncrement(), 1000);
        return String.format("%03d", n);
    }
}