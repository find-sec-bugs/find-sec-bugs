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

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

public class StrutsValidatorFormDetector implements Detector {

    private static final String STRUTS_FORM_VALIDATION_TYPE = "STRUTS_FORM_VALIDATION";

    private BugReporter bugReporter;

    public StrutsValidatorFormDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        boolean isActionForm = InterfaceUtils.isSubtype(javaClass, "org.apache.struts.action.ActionForm");
        boolean isValidatorForm = InterfaceUtils.isSubtype(javaClass, "org.apache.struts.validator.ValidatorForm");
        boolean isDynaValidatorForm = InterfaceUtils.isSubtype(javaClass, "org.apache.struts.validator.DynaValidatorForm");

        if (!isActionForm && !isValidatorForm && !isDynaValidatorForm) return; //Not form implementation

        final String expectedSig = "(Lorg/apache/struts/action/ActionMapping;Ljavax/servlet/http/HttpServletRequest;)Lorg/apache/struts/action/ActionErrors;";
        boolean validateMethodFound = false;
        for (Method m : javaClass.getMethods()) {

            if ("validate".equals(m.getName()) && expectedSig.equals(m.getSignature())) {
                validateMethodFound = true;
            }
        }

        //ValidatorForm without a validate method is just like a regular ActionForm
        if (!validateMethodFound) {
            BugInstance bug = new BugInstance(this, STRUTS_FORM_VALIDATION_TYPE, Priorities.LOW_PRIORITY) //
                    .addClass(javaClass);
            if(isActionForm) {
                bug.addString("ActionForm");
            }
            else if(isValidatorForm) {
                bug.addString("ValidatorForm");
            }
            else if(isDynaValidatorForm) {
                bug.addString("DynaValidatorForm");
            }
            bugReporter.reportBug(bug);
        }
    }

    @Override
    public void report() {

    }
}
