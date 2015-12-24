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

import edu.umd.cs.findbugs.BugInstance;

/**
 * Used for information about indirect taint sinks
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintSink {
    
    private final Taint taint;
    private final BugInstance bugInstance;

    public TaintSink(Taint taint, BugInstance bugInstance) {
        //Invalid arguments
        if (taint == null) throw new NullPointerException("taint is null");
        if (bugInstance == null) throw new NullPointerException("bugInstance is null");

        this.taint = taint;
        this.bugInstance = bugInstance;
    }

    public Taint getTaint() {
        return taint;
    }

    public BugInstance getBugInstance() {
        return bugInstance;
    }

    @Override
    public boolean equals(Object obj) {
        //Invalid argument
        if (obj == null || !(obj instanceof TaintSink)) {
            return false;
        }
        final TaintSink other = (TaintSink) obj;
        return taint.equals(other.taint) && bugInstance.equals(other.bugInstance);
    }
    
    @Override
    public int hashCode() {
        return 3 * taint.hashCode() + 61 * bugInstance.hashCode();
    }
}
