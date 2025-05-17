package com.novatronic.masivas.backoffice.dto;

import jakarta.persistence.ParameterMode;

/**
 *
 * @author Obi Consulting
 */
public class ProcedureParam {

    private final int position;
    private final Class<?> type;
    private final ParameterMode mode;
    private final Object value;

    public ProcedureParam(int position, Class<?> type, ParameterMode mode, Object value) {
        this.position = position;
        this.type = type;
        this.mode = mode;
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public Class<?> getType() {
        return type;
    }

    public ParameterMode getMode() {
        return mode;
    }

    public Object getValue() {
        return value;
    }
}
