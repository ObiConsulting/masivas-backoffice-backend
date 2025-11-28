package com.novatronic.masivas.backoffice.log;

import com.novatronic.masivas.backoffice.util.LogUtil;
import org.apache.logging.log4j.ThreadContext;

public class LogAuditoria {

    private LogAuditoria() {}
    public static String resolveTrxId() {
        String trxIdTag = "trxId";
        boolean addedHere = false;
        String trxId;
        try {
            trxId = ThreadContext.get(trxIdTag);
            if (trxId == null || trxId.isBlank()) {
                trxId = LogUtil.getConjunto();
                ThreadContext.put(trxIdTag, trxId);
                addedHere = true;
            }
        } finally {
            if (addedHere) {
                ThreadContext.remove(trxIdTag);
            }
        }
        return trxId;
    }
}
