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

import java.util.ArrayList;

/**
 * @author Tomas Polesovsky
 */
public interface TaintTag {

    String name();

    // There are only several items, created during static class loading and reading config files.
    ArrayList<TaintTag> registry = new ArrayList<>();
    
    static TaintTag get(String name) {
        for (Taint.Tag enumTag : Taint.Tag.values()) {
            if (enumTag.name().equals(name)) {
                return enumTag;
            }
        }

        for (TaintTag taintTag : registry) {
            if (taintTag.name().equals(name)) {
                return taintTag;
            }
        }
        
        return null;
    }
    
    static TaintTag create(String name) {
        TaintTag taintTag = get(name);
        if (taintTag != null) {
            return taintTag;
        }

        synchronized (registry) {
            taintTag = get(name);
            if (taintTag != null) {
                return taintTag;
            }
            
            taintTag = () -> name;
            registry.add(taintTag);
            return taintTag;
        }
    }
}
