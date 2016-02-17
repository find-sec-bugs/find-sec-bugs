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
package com.h3xstream.findsecbugs.taintanalysis;

import edu.umd.cs.findbugs.classfile.MethodDescriptor;

/**
 * Global specification of a taint source location
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintLocation implements Comparable<TaintLocation> {
    
    private final MethodDescriptor methodDescriptor;
    private final int position;

    public TaintLocation(MethodDescriptor methodDescriptor, int position) {
        if (methodDescriptor == null) {
            throw new NullPointerException("method not specified");
        }
        if (position < 0) {
            throw new IllegalArgumentException("postition is negative");
        }
        this.methodDescriptor = methodDescriptor;
        this.position = position;
    }

    public MethodDescriptor getMethodDescriptor() {
        return methodDescriptor;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TaintLocation)) {
            return false;
        }
        final TaintLocation other = (TaintLocation) obj;
        return methodDescriptor.equals(other.methodDescriptor) && position == other.position;
    }

    @Override
    public int hashCode() {
        return methodDescriptor.hashCode() + 11 * position;
    }

    @Override
    public String toString() {
        return methodDescriptor.toString() + " " + position;
    }

    @Override
    public int compareTo(TaintLocation other) {
        if (other == null) {
            throw new NullPointerException();
        }
        int comparison = this.methodDescriptor.compareTo(other.methodDescriptor);
        if (comparison != 0) {
            return comparison;
        }
        if (this.position < other.position) {
            return -1;
        }
        if (this.position > other.position) {
            return 1;
        }
        return 0;
    }
}
