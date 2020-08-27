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
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Location;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;

import java.util.Iterator;

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

    /**
     * Uppercase and Lowercase case mapping functions are very common.
     * To avoid massive number of false positives, we check for String comparison done in the same method.
     *
     * @return If the method String.equals() was found in the same method.
     */
    private boolean hasComparisonInMethod() {
        //boolean stringComparisonFound = false;

        ClassContext classCtx = getClassContext();
        ConstantPoolGen cpg = classCtx.getConstantPoolGen();
        CFG cfg;
        try {
            cfg = classCtx.getCFG(getMethod());
        } catch (CFGBuilderException e) {
            AnalysisContext.logError("Cannot get CFG", e);
            return false;
        }

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext();) {
            Location location = i.next();
            Instruction inst = location.getHandle().getInstruction();
//            ByteCode.printOpCode(inst,cpg);

            if(inst instanceof INVOKEVIRTUAL) {
                INVOKEVIRTUAL inv = (INVOKEVIRTUAL) inst;
                String className = inv.getClassName(cpg);
                String methodName = inv.getMethodName(cpg);
                if(className.equals("java.lang.String") &&
                        (methodName.equals("equals") || methodName.equals("indexOf"))) {
                    return true;
                }
            }
        }

        return false;
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
            if(hasComparisonInMethod()) {
                reportBug();
            }
        }
        //        ImproperHandlingUnicodeDetector: [0446]  invokevirtual   java/lang/String.equalsIgnoreCase (Ljava/lang/String;)Z
        else if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/String")
                && getNameConstantOperand().equals("equalsIgnoreCase")) {
            reportBug();
        }

    }

}
