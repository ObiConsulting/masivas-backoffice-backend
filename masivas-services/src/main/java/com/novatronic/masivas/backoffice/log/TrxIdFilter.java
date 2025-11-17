package com.novatronic.masivas.backoffice.log;

import com.novatronic.masivas.backoffice.util.Constantes;
import com.novatronic.masivas.backoffice.util.LogUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TrxIdFilter extends OncePerRequestFilter {
    private static final String TXN_ID="trxId";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String trxId = ThreadContext.get(TXN_ID);
        boolean addedHere = false;

        if (trxId == null || trxId.isBlank()) {
            // intenta reusar cabecera si vino de un proxy/gateway; si no, genera
            trxId = request.getHeader("X-Trx-Id");
            if (trxId == null || trxId.isBlank()) {
                trxId = LogUtil.getConjunto();
            }
            ThreadContext.put(TXN_ID, trxId);
            addedHere = true;
        }

        request.setAttribute(TXN_ID, ThreadContext.get(TXN_ID));
        response.setHeader("X-Trx-Id", ThreadContext.get(TXN_ID));

        try {
            chain.doFilter(request, response);
        } finally {
            if (addedHere) {
                ThreadContext.remove(TXN_ID);
            }
        }
    }
}
