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

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import com.h3xstream.findsecbugs.taintanalysis.data.TaintLocation;
import com.h3xstream.findsecbugs.taintanalysis.data.UnknownSource;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.util.ClassName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.bcel.generic.ObjectType;

/**
 * Representation of taint dataflow facts (dataflow values) for each slot in
 * {@link TaintFrame}
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

        /**
         * Returns the "more dangerous" state (TAINTED &gt; UNKNOWN &gt; SAFE
         * &gt; NULL &gt; INVALID) as a merge of two states
         *
         * @param a first state to merge
         * @param b second state to merge
         * @return one of the values a, b
         */
        public static State merge(State a, State b) {
            if (a == null || b == null) {
                throw new NullPointerException("use Taint.State." + INVALID.name() + " instead of null");
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

    public enum Tag {
        XSS_SAFE,
        SQL_INJECTION_SAFE,
        COMMAND_INJECTION_SAFE,
        LDAP_INJECTION_SAFE,
        XPATH_INJECTION_SAFE,
        HTTP_POLLUTION_SAFE,
        CR_ENCODED,
        LF_ENCODED,
        QUOTE_ENCODED,
        APOSTROPHE_ENCODED,
        LT_ENCODED,
        SENSITIVE_DATA,
        CUSTOM_INJECTION_SAFE,
        URL_ENCODED,
        PATH_TRAVERSAL_SAFE,
        REDIRECT_SAFE,

        CREDIT_CARD_VARIABLE,
        PASSWORD_VARIABLE,
        HASH_VARIABLE;
    }

    private State state;
    private static final int INVALID_INDEX = -1;
    private int variableIndex;
    private final Set<TaintLocation> taintLocations;
    private final Set<TaintLocation> unknownLocations;
    private final Set<Integer> parameters;
    private State nonParametricState;
    private ObjectType realInstanceClass;
    private final Set<Tag> tags;
    private final Set<Tag> tagsToRemove;
    private String constantValue;
    private String potentialValue;
    private String debugInfo = null;
    private Set<UnknownSource> sources = new HashSet<>();

    /**
     * Constructs a new empty instance of Taint with the specified state
     *
     * @param state state of the fact
     * @throws NullPointerException if argument is null
     * @throws IllegalArgumentException if argument is INVALID
     */
    public Taint(State state) {
        Objects.requireNonNull(state, "state is null");
        if (state == State.INVALID) {
            throw new IllegalArgumentException("state not allowed");
        }
        this.state = state;
        this.variableIndex = INVALID_INDEX;
        this.unknownLocations = new HashSet<TaintLocation>();
        this.taintLocations = new HashSet<TaintLocation>();
        this.parameters = new HashSet<Integer>();
        this.nonParametricState = State.INVALID;
        this.realInstanceClass = null;
        this.tags = EnumSet.noneOf(Tag.class);
        this.tagsToRemove = EnumSet.noneOf(Tag.class);
        this.constantValue = null;
        if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
            this.debugInfo = "?";
        }
    }

    /**
     * Creates a hard copy of the specified Taint instance
     *
     * @param taint instance to copy
     * @throws NullPointerException if argument is null
     */
    public Taint(Taint taint) {
        Objects.requireNonNull(taint, "taint is null");
        assert taint.state != null;
        this.state = taint.state;
        this.variableIndex = taint.variableIndex;
        this.taintLocations = new HashSet<TaintLocation>(taint.taintLocations);
        this.unknownLocations = new HashSet<TaintLocation>(taint.unknownLocations);
        this.parameters = new HashSet<Integer>(taint.getParameters());
        this.nonParametricState = taint.nonParametricState;
        this.realInstanceClass = taint.realInstanceClass;
        this.tags = EnumSet.copyOf(taint.tags);
        this.tagsToRemove = EnumSet.copyOf(taint.tagsToRemove);
        this.constantValue = taint.constantValue;
        this.potentialValue = taint.potentialValue;
        if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
            this.debugInfo = taint.debugInfo;
        }
        this.sources.addAll(taint.sources);
    }

    /**
     * Checks if there is any valuable information derived by the taint analysis.
     *
     * @return true if the Taint contains any useful information, false otherwise
     */
    public boolean isInformative() {
        if (!isUnknown()) {
            return true;
        }
        if (hasParameters()) {
            return true;
        }
        if (getRealInstanceClass() != null) {
            return true;
        }
        if (hasTags() || isRemovingTags()) {
            return true;
        }

        return false;
    }

    /**
     * Returns the taint state of this fact
     *
     * @return taint state
     */
    public State getState() {
        assert state != null && state != State.INVALID;
        return state;
    }

    void setState(State state) {
        Objects.requireNonNull(state, "state is null");
        if (state == State.INVALID) {
            throw new IllegalArgumentException("state not allowed to be set");
        }
        this.state = state;
    }

    /**
     * If known (check first), returns the index of the local variable,
     * where the value matching this fact is stored
     *
     * @return the index in the frame
     * @throws IllegalStateException if index is uknown
     */
    public int getVariableIndex() {
        if (variableIndex == INVALID_INDEX) throw new IllegalStateException("index not set or has been invalidated");

        assert variableIndex >= 0;
        return variableIndex;
    }

    /**
     * Checks if the index of the local variable matching this fact is known
     *
     * @return true if index is known, false otherwise
     */
    public boolean hasValidVariableIndex() {
        return variableIndex != INVALID_INDEX;
    }

    void setVariableIndex(int index) {
        if (index < 0) throw new IllegalArgumentException("negative index");
        variableIndex = index;
    }

    void invalidateVariableIndex() {
        variableIndex = INVALID_INDEX;
    }

    /**
     * Adds location for a taint source or path to remember for reporting
     *
     * @param location location to remember
     * @param isKnownTaintSource true for tainted value, false if just not safe
     * @throws NullPointerException if location is null
     */
    public void addLocation(TaintLocation location, boolean isKnownTaintSource) {
        Objects.requireNonNull(location, "location is null");
        if (isKnownTaintSource) {
            taintLocations.add(location);
        } else {
            unknownLocations.add(location);
        }
    }

    /**
     * Returns locations with taint sources or nodes on path from those
     * sources, if there are some locations confirmed to be tainted,
     * only those are returned
     *
     * @return unmodifiable set of locations
     */
    public Set<TaintLocation> getTaintedLocations() {
        return Collections.unmodifiableSet(taintLocations);
    }

    /**
     * @return All the location of tainted and unknown locations.
     */
    public Collection<TaintLocation> getUnknownLocations() {
        return Collections.unmodifiableSet(unknownLocations);
    }

    /**
     * @return All the location of tainted and unknown locations.
     */
    public Collection<TaintLocation> getAllLocations() {
        List<TaintLocation> allLocations = new ArrayList<>();
        allLocations.addAll(unknownLocations);
        allLocations.addAll(taintLocations);
        return allLocations;
    }

    /**
     * Checks whether values matching this fact are always trusted
     * 
     * @return true if the taint state is safe (or null), false otherwise
     */
    public boolean isSafe() {
        return state.isSafe;
    }

    /**
     * Checks whether values matching this fact are probably untrusted
     * 
     * @return true for the state TAINTED, false otherwise
     */
    public boolean isTainted() {
        return state.isTainted;
    }

    /**
     * Checks whether values matching this fact can be untrusted but also safe
     * 
     * @return true for the state UNKNOWN, false otherwise
     */
    public boolean isUnknown() {
        return state.isUnknown;
    }

    void addParameter(int parameterIndex) {
        if (parameterIndex < 0) {
            throw new IllegalArgumentException("index cannot be negative");
        }
        parameters.add(parameterIndex);
    }

    /**
     * Checks if the taint state of this fact depends on the method arguments
     * 
     * @return true if there is an influence, false otherwise
     */
    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    /**
     * Returns the method arguments influencing the taint state of this fact
     * 
     * @return unmodifiable set of parameter indices
     */
    public Set<Integer> getParameters() {
        return Collections.unmodifiableSet(parameters);
    }

    /**
     * Gets the state influencing the state of this fact if dependant on method
     * arguments, final state is given by merge of that state and arguments
     * 
     * @return 
     */
    public State getNonParametricState() {
        return nonParametricState;
    }
    
    void setNonParametricState(State state) {
        Objects.requireNonNull(state, "state is null");
        if (state == State.INVALID) {
            throw new IllegalArgumentException("state not allowed to be set");
        }
        nonParametricState = state;
    }

    /**
     * Finds out the real type of instance matching this fact if possible
     * 
     * @return type of the instance or null if uknown
     */
    public ObjectType getRealInstanceClass() {
        return realInstanceClass;
    }

    void setRealInstanceClass(ObjectType objectType) {
        // can be null
        realInstanceClass = objectType;
    }

    /**
     * Finds out the real class name of instance matching this fact if possible
     * 
     * @return class name of the instance or null if uknown
     */
    public String getRealInstanceClassName() {
        if (realInstanceClass == null) {
            return null;
        }
        return ClassName.toSlashedClassName(realInstanceClass.getClassName());
    }

    /**
     * Adds the specified taint tag to this fact or marks this tag to add
     * if this fact acts like a derivation of taint transfer behaviour
     * 
     * @param tag tag to add
     * @return true if this tag was not present before, false otherwise
     */
    public boolean addTag(Tag tag) {
        return tags.add(tag);
    }
    
    /**
     * Checks whether the specified taint tag is present for this fact
     * 
     * @param tag tag to check
     * @return true if it is present, false otherwise
     */
    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }

    /**
     * Checks whether one of the specified taint tag is present for this fact
     *
     * @param tags Tags to test
     * @return true if at least one is present, false otherwise
     */
    public boolean hasOneTag(Tag... tags) {
        for(Tag t : tags) {
            if (this.tags.contains(t)) return true;
        }
        return false;
    }
    
    /**
     * Checks if there are any taint tags for this fact
     * 
     * @return true if number of tags is &gt; 0, false otherwise
     */
    public boolean hasTags() {
        return !tags.isEmpty();
    }
    
    /**
     * Returns all present taint tags for this fact
     * 
     * @return unmodifiable set of all present taint tags
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }
    
    /**
     * Removes the specified tag (if present) or marks this tag to remove
     * if this fact acts like a derivation of taint transfer behaviour
     * 
     * @param tag tag to remove
     * @return true if the tag was present, false otherwise
     */
    public boolean removeTag(Tag tag) {
        tagsToRemove.add(tag);
        return tags.remove(tag);
    }
    
    /**
     * Checks if there are some tags to remove
     * (if this fact acts like a taint derivation spec.)
     * 
     * @return true if there are some, false otherwise
     */
    public boolean isRemovingTags() {
        return !tagsToRemove.isEmpty();
    }
    
    /**
     * Returns tags to remove (if this fact acts like a taint derivation spec.)
     * 
     * @return unmodifiable set of tags
     */
    public Set<Tag> getTagsToRemove() {
        return Collections.unmodifiableSet(tagsToRemove);
    }
    
    /**
     * Returns the constant value of the string or char if known
     * 
     * @return constant value or null if unknown
     */
    public String getConstantValue() {
        return constantValue;
    }
    
    public void setConstantValue(String value) {
        this.constantValue = value;
    }

    /**
     * Returns the constant value that will be set under a specific condition
     *
     * @return constant value or null if unknown
     */
    public String getPotentialValue() {
        return potentialValue;
    }

    public void setPotentialValue(String value) {
        this.potentialValue = value;
    }

    public String getConstantOrPotentialValue() {
        return constantValue != null ? constantValue : potentialValue;
    }

    /**
     * Constructs a new instance of taint from the specified state name
     * 
     * @param stateName name of the state
     * @return the constructed instance
     * @throws IllegalArgumentException if the name does not match any state
     */
    public static Taint valueOf(String stateName) {
        // exceptions thrown from Enum.valueOf
        return valueOf(State.valueOf(stateName));
    }

    /**
     * Constructs a new instance of taint from the specified state
     * 
     * @param state the specified state
     * @return the constructed instance
     * @throws NullPointerException if state is null
     */
    public static Taint valueOf(State state) {
        Objects.requireNonNull(state, "state is null");
        if (state == State.INVALID) {
            return null;
        }
        return new Taint(state);
    }

    /**
     * Returns the merge of the facts such that it can represent any of them
     * 
     * @param a first state to merge
     * @param b second state to merge
     * @return constructed merge of the specified facts
     */
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
        if (!result.isTainted()) {
           mergeParameters(a, b, result); 
        }
        mergeRealInstanceClass(a, b, result);
        mergeTags(a, b, result);
        if (a.constantValue != null && a.constantValue.equals(b.constantValue)) {
            result.constantValue = a.constantValue;
        }
        if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
            result.setDebugInfo("[" + a.getDebugInfo() + "]+[" + b.getDebugInfo() + "]");
        }
        assert !result.hasParameters() || result.isUnknown();
        if(a.potentialValue != null) {
            result.potentialValue = a.potentialValue;
        }
        else if(b.potentialValue != null) {
            result.potentialValue = b.potentialValue;
        }
        result.addAllSources(a.sources);
        result.addAllSources(b.sources);
        return result;
    }

    private static void mergeParameters(Taint a, Taint b, Taint result) {
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
    }

    private static void mergeRealInstanceClass(Taint a, Taint b, Taint result) {
        if (a.realInstanceClass != null && b.realInstanceClass != null) {
            try {
                if (a.realInstanceClass.equals(b.realInstanceClass)
                        || b.realInstanceClass.subclassOf(a.realInstanceClass)) {
                    result.realInstanceClass = a.realInstanceClass;
                } else if (a.realInstanceClass.subclassOf(b.realInstanceClass)) {
                    result.realInstanceClass = b.realInstanceClass;
                }
            } catch (ClassNotFoundException ex) {
                AnalysisContext.reportMissingClass(ex);
            }
        }
    }
    
    private static void mergeTags(Taint a, Taint b, Taint result) {
        if (a.isSafe()) {
            result.tags.addAll(b.tags);
        } else if (b.isSafe()) {
            result.tags.addAll(a.tags);
        } else {
            result.tags.addAll(a.tags);
            result.tags.retainAll(b.tags);
        }
        result.tagsToRemove.addAll(a.tagsToRemove);
        result.tagsToRemove.addAll(b.tagsToRemove);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
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
                && this.nonParametricState == other.nonParametricState
                && Objects.equals(this.realInstanceClass, other.realInstanceClass)
                //&& this.tags.equals(other.tags)
                //&& Objects.equals(this.constantValue, other.constantValue)
        ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, variableIndex, taintLocations, unknownLocations,
                parameters, nonParametricState, realInstanceClass, tags, constantValue);
    }

    /**
     * Gets the info for debugging merged from all used facts
     * 
     * @return previousle set info
     */
    public String getDebugInfo() {
        return debugInfo;
    }

    /**
     * Sets info for debugging purposes (consumes much memory)
     * 
     * @param debugInfo info to store
     * @return the modified instance itself
     */
    public Taint setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
        return this;
    }

    public Set<UnknownSource> getSources() {
        return sources;
    }

    public void addSource(UnknownSource source) {
        this.sources.add(source);
    }

    protected void addAllSources(Set<UnknownSource> sources) {
        this.sources.addAll(sources);
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
        if (sources != null && sources.size() > 0) {
            StringBuilder b = new StringBuilder();
            for(UnknownSource source : sources) {
                switch (source.getSourceType()) {
                    case FIELD:
                        b.append("field["+source.getSignatureField()+"]");
                        break;
                    case RETURN:
                        b.append("method["+source.getSignatureMethod()+"]");
                        break;
                    case PARAMETER:
                        b.append("parameter["+source.getParameterIndex()+"]");
                        break;
                }
            }
            sb.append(" source={").append(b.toString()).append('}');
        }
        if (constantValue != null) {
            sb.append(" constant=").append(constantValue);
        }
        if (potentialValue != null) {
            sb.append(" potential=").append(potentialValue);
        }
        if (tags.size() > 0) {
            sb.append(" tags=").append(Arrays.toString(tags.toArray()));
        }
        return sb.toString();
    }
}
