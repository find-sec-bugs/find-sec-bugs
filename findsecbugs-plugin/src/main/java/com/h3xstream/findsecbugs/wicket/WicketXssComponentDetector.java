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
package com.h3xstream.findsecbugs.wicket;

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;

import java.util.Iterator;
import java.util.LinkedList;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * Detect calls to the method <code>setEscapeModelStrings</code> on various Wicket component.
 * The class name is not validate to avoid missing an unknown component.
 */
public class WicketXssComponentDetector implements Detector {
    private static final boolean DEBUG = false;
    private static final String WIC_XSS = "WICKET_XSS1";

    /**
     * For simplicity we don't look at the class name. The method args signature is precise enough.
     */
    private static final InvokeMatcherBuilder COMPONENT_ESCAPE_MODEL_STRINGS = invokeInstruction()
            .atMethod("setEscapeModelStrings").withArgs("(Z)Lorg/apache/wicket/Component;");

    private final BugReporter bugReporter;

    public WicketXssComponentDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        for (Method m : javaClass.getMethods()) {
            try {
                analyzeMethod(m, classContext);
            } catch (CFGBuilderException e) {
            } catch (DataflowAnalysisException e) {
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {

        //Conditions that needs to fill to identify the vulnerability
        boolean escapeModelStringsSetToFalse = false;
        boolean escapeModelStringsValueUnknown = false;
        Location locationWeakness = null;

        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();

            Instruction inst = location.getHandle().getInstruction();

            //ByteCode.printOpCode(location.getHandle(),classContext.getConstantPoolGen());

            if (inst instanceof InvokeInstruction) {
                InvokeInstruction invoke = (InvokeInstruction) inst;

                if (COMPONENT_ESCAPE_MODEL_STRINGS.matches(invoke, cpg)) {
                    Integer booleanValue = ByteCode.getConstantInt(location.getHandle().getPrev());
                    if (booleanValue != null && booleanValue == 0) {
                        escapeModelStringsSetToFalse = true;
                        locationWeakness = location;
                    } else if (booleanValue == null) {
                        escapeModelStringsValueUnknown = true;
                        locationWeakness = location;
                    }
                }
            }
        }

        //Both condition have been found in the same method
        if (escapeModelStringsSetToFalse) {
            JavaClass clz = classContext.getJavaClass();
            bugReporter.reportBug(new BugInstance(this, WIC_XSS, Priorities.NORMAL_PRIORITY) //
                    .addClass(clz)
                    .addMethod(clz, m)
                    .addSourceLine(classContext, m, locationWeakness));
        } else if (escapeModelStringsValueUnknown) {
            JavaClass clz = classContext.getJavaClass();
            bugReporter.reportBug(new BugInstance(this, WIC_XSS, Priorities.LOW_PRIORITY) //
                    .addClass(clz)
                    .addMethod(clz, m)
                    .addSourceLine(classContext, m, locationWeakness));
        }
    }


    @Override
    public void report() {
    }
}
