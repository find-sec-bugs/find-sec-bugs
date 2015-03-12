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
package com.h3xstream.findbugs.test;

import edu.umd.cs.findbugs.AbstractBugReporter;
import edu.umd.cs.findbugs.AnalysisError;
import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.SortedBugCollection;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyBugReporter extends AbstractBugReporter {

    private BugCollection bugCollection = new SortedBugCollection();

    private int bugInstanceCount;

    private static final Logger log = LoggerFactory.getLogger(EasyBugReporter.class);


    public EasyBugReporter() {
        setPriorityThreshold(20);
    }

    @Override
    public void finish() {

    }

    @Override
    public BugCollection getBugCollection() {
        return bugCollection;
    }

    @Override
    public void observeClass(ClassDescriptor classDescriptor) {

    }

    @Override
    public void doReportBug(BugInstance bugInstance) {
        StringBuilder bugDetail = new StringBuilder();
        bugDetail
                .append("\n------------------------------------------------------")
                .append("\nNew Bug Instance: [" + ++bugInstanceCount + "]")
                .append("\n  message=" + bugInstance.getMessage())
                .append("\n  bugType=" + bugInstance.getBugPattern().getType())
                .append("  category=" + bugInstance.getCategoryAbbrev());
        if (bugInstance.getPrimaryClass() != null) {
            bugDetail.append("\n  class=" + bugInstance.getPrimaryClass().getClassName());
        }
        if (bugInstance.getPrimaryMethod() != null) {
            bugDetail.append("  method=" + bugInstance.getPrimaryMethod().getMethodName());
        }
        if (bugInstance.getPrimaryField() != null) {
            bugDetail.append("  field=" + bugInstance.getPrimaryField().getFieldName());
        }
        if (bugInstance.getPrimarySourceLineAnnotation() != null) {
            bugDetail.append("  line=" + bugInstance.getPrimarySourceLineAnnotation().getStartLine());
        }
        bugDetail
                .append("\n------------------------------------------------------");
        log.info(bugDetail.toString());
        //bugCollection.add(bugInstance);
    }

    @Override
    public void reportAnalysisError(AnalysisError error) {
        if (error.getException() != null) {
            log.error(error.getException().getMessage(), error.getException());
        } else {
            log.error(error.getMessage());
        }
    }

    @Override
    public void reportMissingClass(String className) {
        log.warn("Missing class " + className);
    }

}
