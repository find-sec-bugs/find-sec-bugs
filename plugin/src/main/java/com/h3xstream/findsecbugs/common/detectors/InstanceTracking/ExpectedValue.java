package com.h3xstream.findsecbugs.common.detectors.InstanceTracking;

public class ExpectedValue {
    public ExpectedValue(Object value, int parameterIndex) {
        this.value = value;
        this.parameterIndex = parameterIndex;
    }

    private Object value;
    public Object getValue() { return value; }

    private int parameterIndex;
    public int getParameterIndex() { return parameterIndex; }
}
