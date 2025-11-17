package com.novatronic.masivas.backoffice.log;

import com.novatronic.masivas.backoffice.util.LogUtil;
import com.novatronic.novalog.audit.logger.NovaLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.novatronic.masivas.backoffice.util.ConstantesLog.*;

public class PerformanceInterceptor implements HandlerInterceptor {
    public static final NovaLogger LOG = NovaLogger.getLogger(PerformanceInterceptor.class);
    private long inicio = 0;
    private long fin = 0;
    private String methodName = "";
    private String className = "";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HandlerMethod hm = null;
        try {
            hm = (HandlerMethod) handler;
            inicio = System.currentTimeMillis();
            Method metodo = hm.getMethod();
            methodName = metodo.getName();
            className = metodo.getDeclaringClass().getName();

        } catch (ClassCastException e) {
            LOG.error(LogUtil.generarMensajeLogError(null,"preHandle Error PERFORMANCE",null), e);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        try {
            fin = System.currentTimeMillis();
        } catch (ClassCastException e) {
            LOG.error(LogUtil.generarMensajeLogError(null,"postHandle Error PERFORMANCE",null), e);
        }

    }

    /**
     * Metodo que genera la pista del log de performance de la operacion
     * @param request   solicitud HTTP
     * @param response  respuesta HTTP
     * @param handler   el handler que inició la ejecución asincrónica
     * @param exception cualquier excepción lanzada por el handler
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String space = " ";
        String log= String.format("%1$-16s", MODULO_ORIGEN) +
                space +
                String.format("%1$-16s", MODULO_DESTINO) +
                space +
                sdf.format(new Date(inicio)) +
                space +
                sdf.format(new Date(fin)) +
                space +
                String.format("%1$-5s", fin - inicio) +
                space +
                className +
                "." +
                methodName;
        LOG.info(log);
    }

}