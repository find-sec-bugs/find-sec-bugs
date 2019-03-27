package com.google.common.base;

import java.io.Serializable;

public abstract class Optional<T> implements Serializable {
    public abstract T or(T defaultValue);
}
