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

import edu.umd.cs.findbugs.ba.Location;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of taint dataflow facts (dataflow values) for each slot
 * in {@link TaintFrame}
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class Taint {

    public enum State {
        SAFE(true, false),
        NULL(true, false),
        UNKNOWN(false, false),
        TAINTED(false, true);
    
        private final boolean isSafe;
        private final boolean isTainted;

        State(boolean isSafe, boolean isTainted) {
            this.isSafe = isSafe;
            this.isTainted = isTainted;
        }
        
        public static State merge(State a, State b) {
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
    
    private State state;
    private static final int INVALID_INDEX = -1;
    private int localVariableIndex = INVALID_INDEX;
    private final Set<Location> taintLocations = new HashSet<Location>();
    
    public Taint(State state) {
        if (state == null) {
            throw new NullPointerException("state not set");
        }
        this.state = state;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        if (state == null) {
            throw new NullPointerException("state cannot be null");
        }
        this.state = state;
    }
    
    public int getLocalVariableIndex() {
        if (localVariableIndex == INVALID_INDEX) {
            throw new IllegalStateException("index not set or has been invalidated");
        }
        return localVariableIndex;
    }
    
    public boolean hasValidLocalVariableIndex() {
        return localVariableIndex != INVALID_INDEX;
    }
    
    public void setLocalVariableIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("negative index");
        }
        localVariableIndex = index;
    }
    
    public void invalidateLocalVariableIndex() {
        localVariableIndex = INVALID_INDEX;
    }
    
    public void addTaintLocation(Location location) {
        taintLocations.add(location);
    }
    
    public Set<Location> getTaintedLocations() {
        return Collections.unmodifiableSet(taintLocations);
    }
    
    public boolean isSafe() {
        return state.isSafe;
    }
    
    public boolean isTainted() {
        // in context of taint analysis, null value is safe too
        return state.isTainted;
    }
    
    public static Taint merge(Taint a, Taint b) {
        Taint result = new Taint(State.merge(a.getState(), b.getState()));
        if (a.hasValidLocalVariableIndex() && b.hasValidLocalVariableIndex()
            && a.getLocalVariableIndex() == b.getLocalVariableIndex()) {
            result.setLocalVariableIndex(a.getLocalVariableIndex());
        }
        result.taintLocations.addAll(a.getTaintedLocations());
        result.taintLocations.addAll(b.getTaintedLocations());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // consider taint state only in equals
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Taint)) {
            return false;
        }
        final Taint other = (Taint) obj;
        if (this.state != other.state) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.state != null ? this.state.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        String str = state.name().substring(0, 1);
        if (hasValidLocalVariableIndex()) {
            str += localVariableIndex;
        }
        return str;
    }
}
