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

/**
 * Representation of taint dataflow facts (dataflow values) for each slot
 * in {@link TaintFrame}
 * 
 * @author David Formanek
 */
public enum Taint {

    SAFE(true, false),
    NULL(true, false),
    UNKNOWN(false, false),
    TAINTED(false, true);
    
    private final boolean isSafe;
    private final boolean isTainted;

    Taint(boolean isSafe, boolean isTainted) {
        this.isSafe = isSafe;
        this.isTainted = isTainted;
    }
    
    public boolean isSafe() {
        return isSafe;
    }
    
    public boolean isTainted() {
        // in context of taint analysis, null value is safe too
        return isTainted;
    }
    
    public static Taint merge (Taint a, Taint b) {
        if (a == null || b == null) {
            throw new NullPointerException("use UKNOWN instead of null");
        }
        if (a == TAINTED || b == TAINTED) {
            return TAINTED;
        }
        if (a == UNKNOWN || b == UNKNOWN) {
            return UNKNOWN;
        }
        if (a == SAFE || b == SAFE) {
            return SAFE;
        }
        assert a == NULL && b == NULL;
        return NULL;
    }
}
