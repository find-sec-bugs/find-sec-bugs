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
 * ClassMethodSignature and InjectionSink tuple
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class MethodAndSink {
    
    private final ClassMethodSignature classMethodSignature;
    private final InjectionSink sink;

    public MethodAndSink(ClassMethodSignature classMethodSignature, InjectionSink sink) {
        Objects.requireNonNull(classMethodSignature, "classMethodSignature");
        Objects.requireNonNull(sink, "sink");
        this.classMethodSignature = classMethodSignature;
        this.sink = sink;
    }

    public ClassMethodSignature getClassMethodSignature() {
        return classMethodSignature;
    }

    public InjectionSink getSink() {
        return sink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodAndSink that = (MethodAndSink) o;
        return Objects.equals(classMethodSignature, that.classMethodSignature) &&
                Objects.equals(sink, that.sink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classMethodSignature, sink);
    }
}
