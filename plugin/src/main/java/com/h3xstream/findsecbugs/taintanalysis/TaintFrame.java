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

import edu.umd.cs.findbugs.ba.Frame;

/**
 * Representation of the dataflow value (fact) modeling taint state of local
 * variables and values on stack, consists of {@link Taint} values
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintFrame extends Frame<Taint> {

    public TaintFrame(int numLocals) {
        super(numLocals);
    }
}
