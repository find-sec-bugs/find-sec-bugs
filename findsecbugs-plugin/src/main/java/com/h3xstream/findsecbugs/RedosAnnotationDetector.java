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
package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Field;

import java.util.Arrays;
import java.util.List;

/**
 * Detect REDOS in validation annotation.
 * It reuse the same validation for Regex string as <code>ReDosDetector</code>.
 */
public class RedosAnnotationDetector implements Detector {

    private static final String REDOS_TYPE = "REDOS";

    private static final String REGEX_ANNOTATION_TYPES = "Ljavax/validation/constraints/Pattern;";

    private BugReporter bugReporter;

    public RedosAnnotationDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        field : for (Field f : javaClass.getFields()) {

            for (AnnotationEntry ae : f.getAnnotationEntries()) {

                if (REGEX_ANNOTATION_TYPES.equals(ae.getAnnotationType())) {

                    ElementValuePair[] values = ae.getElementValuePairs();

                    for(ElementValuePair valuePair : values) {
                        if("regexp".equals(valuePair.getNameString())) {

                            String regex = valuePair.getValue().stringifyValue();

                            RegexRedosAnalyzer analyzer = new RegexRedosAnalyzer();
                            analyzer.analyseRegexString(regex);

                            //Reporting the issue
                            if (analyzer.isVulnerable()) {

                                bugReporter.reportBug(new BugInstance(this, REDOS_TYPE, Priorities.LOW_PRIORITY) //
                                        .addClass(javaClass)
                                        .addField(javaClass.getClassName(), f.getName(), f.getSignature(), false));
                            }
                        }
                    }



                    continue field; //No need to check the other annotation, We already found the one.
                }
            }
        }
    }

    @Override
    public void report() {

    }

}
