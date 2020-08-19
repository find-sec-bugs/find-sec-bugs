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

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;


public class ImproperHandlingUnicodeDetector extends OpcodeStackDetector {

    private String IMPROPER_UNICODE_TYPE = "IMPROPER_UNICODE";

    private BugReporter bugReporter;

    public ImproperHandlingUnicodeDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }


    private void reportBug() {
        BugInstance bug = new BugInstance(this, IMPROPER_UNICODE_TYPE, Priorities.LOW_PRIORITY) //
                .addClass(this).addMethod(this).addSourceLine(this);
        bugReporter.reportBug(bug);
    }

    @Override
    public void sawOpcode(int seen) {

        //printOpCode(seen);

        //        ImproperHandlingUnicodeDetector: [0398]  invokestatic   java/text/Normalizer.normalize (Ljava/lang/CharSequence;Ljava/text/Normalizer$Form;)Ljava/lang/String;
        if (seen == Const.INVOKESTATIC && getClassConstantOperand().equals("java/text/Normalizer")
                && getNameConstantOperand().equals("normalize")) {
            reportBug();
        }
        //        ImproperHandlingUnicodeDetector: [0281]  invokevirtual   java/net/URI.toASCIIString ()Ljava/lang/String;
        else if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("java/net/URI")
                && getNameConstantOperand().equals("toASCIIString")) {
            reportBug();
        }
        //        ImproperHandlingUnicodeDetector: [0375]  invokestatic   java/net/IDN.toASCII (Ljava/lang/String;)Ljava/lang/String;
        else if (seen == Const.INVOKESTATIC && getClassConstantOperand().equals("java/net/IDN")
                && getNameConstantOperand().equals("toASCII")) {
            reportBug();
        }
        //        ImproperHandlingUnicodeDetector: [0392]  invokevirtual   java/lang/String.toUpperCase ()Ljava/lang/String;
        else if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/String")
                && (getNameConstantOperand().equals("toUpperCase") || getNameConstantOperand().equals("toLowerCase"))) {
            reportBug();
        }
        //        ImproperHandlingUnicodeDetector: [0446]  invokevirtual   java/lang/String.equalsIgnoreCase (Ljava/lang/String;)Z
        else if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/String")
                && getNameConstantOperand().equals("equalsIgnoreCase")) {
            reportBug();
        }

    }

}
