/**
 * Find Security Bugs
 * Copyright (c) 2013, Philippe Arteau, All rights reserved.
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

import edu.umd.cs.findbugs.*;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class BugInstanceMatcher extends BaseMatcher<BugInstance> {

    private String bugType;
    private String className;
    private String methodName;
    private String fieldName;
    private Integer lineNumber;
    private Integer lineNumberApprox;

    /**
     * All the parameters are optional. Only the non-null parameters are used.
     *
     * @param bugType    Expected bug type
     * @param className  Class name
     * @param methodName Method name
     * @param fieldName  Field name
     * @param lineNumber Line number
     */
    public BugInstanceMatcher(String bugType, String className, String methodName, String fieldName, Integer lineNumber, Integer lineNumberApprox) {
        this.bugType = bugType;
        this.className = className;
        this.methodName = methodName;
        this.fieldName = fieldName;
        this.lineNumber = lineNumber;
        this.lineNumberApprox = lineNumberApprox;
    }

    @Override
    public boolean matches(Object obj) {
        if (obj instanceof BugInstance) {
            BugInstance bugInstance = (BugInstance) obj;

            boolean criteriaMatches = true;
            if (bugType != null) {
                criteriaMatches &= bugInstance.getType().equals(bugType);
            }
            if (className != null) {
                ClassAnnotation classAnn = extractBugAnnotation(bugInstance, ClassAnnotation.class);
                if (classAnn == null) return false;

                String fullName = classAnn.getClassName();
                String simpleName = fullName.substring(fullName.lastIndexOf(".") + 1);
                criteriaMatches &= fullName.equals(className) || simpleName.equals(className);
            }
            if (methodName != null) {
                MethodAnnotation methodAnn = extractBugAnnotation(bugInstance, MethodAnnotation.class);
                if (methodAnn == null) return false;
                criteriaMatches &= methodAnn.getMethodName().equals(methodName);
            }
            if (fieldName != null) {
                FieldAnnotation fieldAnn = extractBugAnnotation(bugInstance, FieldAnnotation.class);
                if (fieldAnn == null) return false;
                criteriaMatches &= fieldAnn.getFieldName().equals(fieldName);
            }
            if (lineNumber != null) {
                SourceLineAnnotation srcAnn = extractBugAnnotation(bugInstance, SourceLineAnnotation.class);
                if (srcAnn == null) return false;
                criteriaMatches &= srcAnn.getStartLine() <= lineNumber && lineNumber <= srcAnn.getEndLine();
            }
            if (lineNumberApprox != null) {
                SourceLineAnnotation srcAnn = extractBugAnnotation(bugInstance, SourceLineAnnotation.class);
                if (srcAnn == null) return false;
                criteriaMatches &= srcAnn.getStartLine()-1 <= lineNumberApprox && lineNumberApprox <= srcAnn.getEndLine()+1;
            }
            return criteriaMatches;
        } else {
            return false;
        }
    }

    private <T> T extractBugAnnotation(BugInstance bugInstance, Class<T> annotationType) {
        for (BugAnnotation annotation : bugInstance.getAnnotations()) {
            if (annotation.getClass().equals(annotationType)) {
                return (T) annotation;
            }
        }
        return null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("BugInstance with:\n");
        if (bugType != null) {
            description.appendText("bugType=").appendValue(bugType).appendText(",");
        }
        if (className != null) {
            description.appendText("className=").appendValue(className).appendText(",");
        }
        if (methodName != null) {
            description.appendText("methodName=").appendValue(methodName).appendText(",");
        }
        if (fieldName != null) {
            description.appendText("fieldName=").appendValue(fieldName).appendText(",");
        }
        if (lineNumber != null) {
            description.appendText("lineNumber=").appendValue(lineNumber);
        }
    }
}
