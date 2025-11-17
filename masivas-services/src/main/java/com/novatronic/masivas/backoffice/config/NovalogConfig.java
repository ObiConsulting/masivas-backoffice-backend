package com.novatronic.masivas.backoffice.config;

import com.novatronic.masivas.backoffice.util.LogUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NovalogConfig {

    @Value("${nombremodulo}")
    private String nombreMicroservicio;

    @Value("${nodo}")
    private String nodoCodigo;

    @PostConstruct
    public void init() {
        LogUtil.NOMBREMICROSERVICIO = nombreMicroservicio;
        LogUtil.NODOCODIGO = nodoCodigo;
        validateLength("modulo.codigo", nombreMicroservicio, 8);
        validateLength("modulo.nodo", nodoCodigo, 4);
    }
    public static void validateLength(String name, String value, int expectedLength) {
        if (value == null) {
            throw new IllegalStateException("La propiedad " + name + " no está definida.");
        }
        if (value.length() != expectedLength) {
            throw new IllegalStateException("La propiedad " + name + " debe tener exactamente "
                    + expectedLength + " caracteres, pero tiene " + value.length() + ": [" + value + "]");
        }
    }

}