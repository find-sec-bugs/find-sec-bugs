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

import edu.umd.cs.findbugs.classfile.MethodDescriptor;

/**
 * Global comparable specification of a taint source (or path node) location
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintLocation {

    private final MethodDescriptor methodDescriptor;
    private final int position;

    /**
     * Constructs a location from the specified method and position inside
     * 
     * @param methodDescriptor method of the location
     * @param position position in the method
     * @throws NullPointerException if method is null
     * @throws IllegalArgumentException if position is negative
     */
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

    /**
     * Returns the method of this location
     * 
     * @return descriptor of the method
     */
    public MethodDescriptor getMethodDescriptor() {
        return methodDescriptor;
    }

    /**
     * Returns the position in the method of this location
     * 
     * @return index of the position
     */
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

}
