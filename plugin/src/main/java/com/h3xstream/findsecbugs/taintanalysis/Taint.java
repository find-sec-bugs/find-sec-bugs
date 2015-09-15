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
        TAINTED(false, true, false),
        UNKNOWN(false, false, true),
        SAFE(true, false, false),
        NULL(true, false, false),
        INVALID(false, false, false);
        
        private final boolean isSafe;
        private final boolean isTainted;
        private final boolean isUnknown;

        State(boolean isSafe, boolean isTainted, boolean isUnknown) {
            this.isSafe = isSafe;
            this.isTainted = isTainted;
            this.isUnknown = isUnknown;
        }
        
        public static State merge(State a, State b) {
            if (a == null || b == null) {
                throw new NullPointerException(
                        "use Taint.State." + INVALID.name() + " instead of null"
                );
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
            if (a == NULL || b == NULL) {
            return NULL;
        }
            assert a == INVALID && b == INVALID;
            return INVALID;
    }
    }
    
    private State state;
    private static final int INVALID_INDEX = -1;
    private int variableIndex;
    private final Set<TaintLocation> taintLocations;
    private final Set<TaintLocation> unknownLocations;
    private final Set<Integer> parameters;
    private State nonParametricState = State.INVALID;

    public Taint(State state) {
        if (state == null) {
            throw new NullPointerException("state is null");
        }
        if (state == State.INVALID) {
            throw new IllegalArgumentException("state not allowed");
        }
        this.state = state;
        this.variableIndex = INVALID_INDEX;
        this.unknownLocations = new HashSet<TaintLocation>();
        this.taintLocations = new HashSet<TaintLocation>();
        this.parameters = new HashSet<Integer>();
    }
    
    public Taint(Taint taint) {
        if (taint == null) {
            throw new NullPointerException("taint is null");
        }
        this.state = taint.state;
        this.variableIndex = taint.variableIndex;
        this.taintLocations = new HashSet<TaintLocation>(taint.taintLocations);
        this.unknownLocations = new HashSet<TaintLocation>(taint.unknownLocations);
        this.parameters = new HashSet<Integer>(taint.getParameters());
        this.nonParametricState = taint.nonParametricState;
    }
    
    public State getState() {
        assert state != null && state != State.INVALID;
        return state;
    }
    
    public void setState(State state) {
        if (state == null) {
            throw new NullPointerException("state is null");
        }
        if (state == State.INVALID) {
            throw new IllegalArgumentException("state not allowed to be set");
        }
        this.state = state;
    }
    
    public int getVariableIndex() {
        if (variableIndex == INVALID_INDEX) {
            throw new IllegalStateException("index not set or has been invalidated");
        }
        assert variableIndex >= 0;
        return variableIndex;
    }
    
    public boolean hasValidVariableIndex() {
        return variableIndex != INVALID_INDEX;
    }
    
    public void setVariableIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("negative index");
        }
        variableIndex = index;
    }
    
    public void invalidateVariableIndex() {
        variableIndex = INVALID_INDEX;
    }
    
    public void addLocation(TaintLocation location, boolean isKnownTaintSource) {
        if (location == null) {
            throw new NullPointerException("location is null");
        }
        if (isKnownTaintSource) {
           taintLocations.add(location); 
        } else {
           unknownLocations.add(location); 
        }
    }
    
    public Set<TaintLocation> getLocations() {
        if (taintLocations.isEmpty()) {
            return Collections.unmodifiableSet(unknownLocations);
        }
        return Collections.unmodifiableSet(taintLocations);
    }
    
    public boolean isSafe() {
        return state.isSafe;
    }
    
    public boolean isTainted() {
        // in context of taint analysis, null value is safe too
        return state.isTainted;
    }
    
    public boolean isUnknown() {
        return state.isUnknown;
    }
    
    public void addParameter(int parameterIndex) {
        if (parameterIndex < 0) {
            throw new IllegalArgumentException("index cannot be negative");
        }
        parameters.add(parameterIndex);
    }
    
    public boolean hasParameters() {
        return !parameters.isEmpty();
    }
    
    public Set<Integer> getParameters() {
        return Collections.unmodifiableSet(parameters);
    }
    
    public State getNonParametricState() {
        return nonParametricState;
    }
    
    public static Taint valueOf(String stateName) {
        // exceptions thrown from Enum.valueOf
        return valueOf(State.valueOf(stateName));
    }
    
    public static Taint valueOf(State state) {
        if (state == null) {
            throw new NullPointerException("state is null");
        }
        if (state == State.INVALID) {
            return null;
        }
        return new Taint(state);
    }
    
    public static Taint merge(Taint a, Taint b) {
        if (a == null) {
            if (b == null) {
                return null;
            } else {
                return new Taint(b);
            }
        } else if (b == null) {
            return new Taint(a);
        }
        assert a != null && b != null;
        Taint result = new Taint(State.merge(a.getState(), b.getState()));
        if (a.variableIndex == b.variableIndex) {
            result.variableIndex = a.variableIndex;
        }
        result.taintLocations.addAll(a.taintLocations);
        result.taintLocations.addAll(b.taintLocations);
        result.unknownLocations.addAll(a.unknownLocations);
        result.unknownLocations.addAll(b.unknownLocations);
        result.parameters.addAll(a.parameters);
        result.parameters.addAll(b.parameters);
        if (a.hasParameters()) {
            if (b.hasParameters()) {
                result.nonParametricState = State.merge(a.nonParametricState, b.nonParametricState);
            } else {
                result.nonParametricState = State.merge(b.state, a.nonParametricState);
            }
        } else {
            if (b.hasParameters()) {
                result.nonParametricState = State.merge(a.state, b.nonParametricState);
        }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Taint)) {
            return false;
        }
        Taint other = (Taint) obj;
        return this.state == other.state
                && this.variableIndex == other.variableIndex
                && this.taintLocations.equals(other.taintLocations)
                && this.unknownLocations.equals(other.unknownLocations)
                && this.parameters.equals(other.parameters)
                && this.nonParametricState == other.nonParametricState;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + state.hashCode();
        hash = 31 * hash + variableIndex;
        hash = 31 * hash + taintLocations.hashCode();
        hash = 31 * hash + unknownLocations.hashCode();
        hash = 31 * hash + parameters.hashCode();
        hash = 31 * hash + nonParametricState.hashCode();
        return hash;
    }
    
    @Override
    public String toString() {
        assert state != null;
        StringBuilder sb = new StringBuilder(state.name().substring(0, 1));
        if (hasValidVariableIndex()) {
            sb.append(variableIndex);
        }
        if (!parameters.isEmpty()) {
            sb.append(parameters);
        }
        assert nonParametricState != null;
        if (nonParametricState != State.INVALID) {
            sb.append('(').append(nonParametricState.name().substring(0, 1)).append(')');
        }
        return sb.toString();
    }
}
