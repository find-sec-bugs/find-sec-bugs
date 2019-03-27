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
package com.h3xstream.findsecbugs.injection;

import java.util.Objects;

/**
 * String and InjectionSink tuple
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class MethodAndSink {
    
    private final String method;
    private final InjectionSink sink;

    public MethodAndSink(String method, InjectionSink sink) {
        Objects.requireNonNull(method, "method");
        if (method.isEmpty()) {
            throw new IllegalArgumentException("empty method name");
        }
        Objects.requireNonNull(sink, "sink");
        this.method = method;
        this.sink = sink;
    }

    public String getMethod() {
        return method;
    }

    public InjectionSink getSink() {
        return sink;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MethodAndSink)) {
            return false;
        }
        final MethodAndSink other = (MethodAndSink) obj;
        return this.method.equals(other.method) && this.sink.equals(other.sink);
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + method.hashCode();
        hash = 71 * hash + sink.hashCode();
        return hash;
    }
}
