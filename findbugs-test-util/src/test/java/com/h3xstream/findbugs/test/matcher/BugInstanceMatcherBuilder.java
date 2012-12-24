/**
 * Find Security Bugs
 * Copyright (c) 2012, Philippe Arteau, All rights reserved.
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
package com.h3xstream.findbugs.test.matcher;

import edu.umd.cs.findbugs.BugInstance;
import org.hamcrest.Matcher;
import org.mockito.Matchers;

/**
 * DSL to build BugInstanceMatcher
 */
public class BugInstanceMatcherBuilder {

    private String bugType;
    private String className;
    private String methodName;
    private String fieldName;
    private Integer lineNumber;

    public BugInstanceMatcherBuilder bugType(String bugType) {
        this.bugType = bugType;
        return this;
    }

    public BugInstanceMatcherBuilder inClass(String className) {
        this.className = className;
        return this;
    }

    public BugInstanceMatcherBuilder inMethod(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public BugInstanceMatcherBuilder atField(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public BugInstanceMatcherBuilder atLine(int lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    /**
     * @return Mockito Matcher
     */
    public BugInstance build() {
//        if(fieldName != null && lineNumber != null) {
//            throw new RuntimeException("The field position is not kept after compilation. " +
//                    "The lineNumber can be set when a fieldName is defined.");
//        }
        return Matchers.argThat(new BugInstanceMatcher(bugType,className,methodName,fieldName,lineNumber));
    }
}
