/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.injection.util;

import org.apache.bcel.generic.Type;

public class VariableState {

    private boolean tainted;
    private String debugValue;
    private Type type;

    public VariableState(boolean tainted,String debugValue,Type type) {
        this.tainted = tainted;
        this.debugValue = debugValue;
        this.type = type;
    }

    public static VariableState newSafeValue(Type type) {
        return new VariableState(false,"",type);
    }
    public static VariableState newSafeValue(String debugValue,Type type) {
        return new VariableState(false,debugValue,type);
    }

    public static VariableState newTaintedValue(Type type) {
        return new VariableState(true,"",type);
    }

    public static VariableState newTaintedValue(String debugValue,Type type) {
        return new VariableState(true,debugValue,type);
    }


    public boolean isTainted() {
        return tainted;
    }

    public void setTainted(boolean tainted) {
        this.tainted = tainted;
    }

    public String getDebugValue() {
        return debugValue;
    }

    public void setDebugValue(String debugValue) {
        this.debugValue = debugValue;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(tainted ? " TAINTED " : " OK      ");
        buffer.append(" ");
        buffer.append(type.toString());
        if(debugValue != null && !"".equals(debugValue)) {
            buffer.append(" (");
            buffer.append(debugValue);
            buffer.append(")");
        }

        return buffer.toString();
    }

    public void affectTaintedValue(VariableState appendValue) {
        if(!tainted && appendValue.isTainted()) {
            tainted = true;
        }
    }
}
