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
package com.h3xstream.findsecbugs.taintanalysis.data;

import com.h3xstream.findsecbugs.taintanalysis.Taint;

public class UnknownSource {

    private UnknownSourceType sourceType;

    private int    parameterIndex  = -1;
    private String signatureMethod = "";
    private String signatureField  = "";
    private final Taint.State state;

    public UnknownSource(UnknownSourceType sourceType, Taint.State state) {
        this.sourceType = sourceType;
        this.state      = state;
    }
    /** Auto-generate getter and setter with the template Builder **/

    public Taint.State getState() {
        return state;
    }

    public UnknownSourceType getSourceType() {
        return sourceType;
    }

    public UnknownSource setSourceType(UnknownSourceType sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public UnknownSource setParameterIndex(int parameterIndex) {
        this.parameterIndex = parameterIndex;
        return this;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public UnknownSource setSignatureMethod(String signatureMethod) {
        this.signatureMethod = signatureMethod;
        return this;
    }

    public String getSignatureField() {
        return signatureField;
    }

    public UnknownSource setSignatureField(String signatureField) {
        this.signatureField = signatureField;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnknownSource that = (UnknownSource) o;

        if (parameterIndex != that.parameterIndex) return false;
        if (sourceType != that.sourceType) return false;
        if (!signatureMethod.equals(that.signatureMethod)) return false;
        return signatureField.equals(that.signatureField);
    }

    @Override
    public int hashCode() {
        int result = sourceType.hashCode();
        result = 31 * result + parameterIndex;
        result = 31 * result + signatureMethod.hashCode();
        result = 31 * result + signatureField.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if(parameterIndex != -1) {
            return "Parameter (index=" + parameterIndex + ")";
        }
        else if(!signatureMethod.equals("")) {
            return "Method "+signatureMethod;
        }
        else { //Field
            return "Field "+signatureField;
        }
    }
}
