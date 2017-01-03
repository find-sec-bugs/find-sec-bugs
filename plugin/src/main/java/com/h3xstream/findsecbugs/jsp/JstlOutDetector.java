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
package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.common.JspUtils;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.Hierarchy;
import edu.umd.cs.findbugs.ba.Location;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

public class JstlOutDetector implements Detector {
    private static final boolean DEBUG = false;
    private static final String JSP_JSTL_OUT = "JSP_JSTL_OUT";

    private static final InvokeMatcherBuilder OUT_TAG_ESCAPE_XML = invokeInstruction().atClass("org.apache.taglibs.standard.tag.rt.core.OutTag",
            "org.apache.taglibs.standard.tag.el.core.OutTag",
            "com.caucho.jstl.el.CoreOutTag",
            "com.caucho.jstl.rt.OutTag",
            "org.apache.taglibs.standard.tag.compat.core.OutTag",
            "org.appfuse.webapp.taglib.OutTag")
            .atMethod("setEscapeXml").withArgs("(Z)V","(Ljava/lang/String;)V");

    private final BugReporter bugReporter;



    public JstlOutDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        try {
            if(!Hierarchy.isSubtype(javaClass.getClassName(), "javax.servlet.http.HttpServlet")) {
                return;
            }
        } catch (ClassNotFoundException e) {
            AnalysisContext.reportMissingClass(e);
        }
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
        boolean escapeXmlSetToFalse = false;
        boolean escapeXmlValueUnknown = false;
        Location locationWeakness = null;

        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        LinkedList<Instruction> instructionVisited = new LinkedList<Instruction>();

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();

            Instruction inst = location.getHandle().getInstruction();
            //ByteCode.printOpCode(inst, cpg);

            instructionVisited.add(inst);

//     JspSpringEvalDetector: [0047]  aload   4
//     JspSpringEvalDetector: [0049]  iconst_1
//     JspSpringEvalDetector: [0050]  invokevirtual   org/apache/taglibs/standard/tag/rt/core/OutTag.setEscapeXml (Z)V

            if (inst instanceof InvokeInstruction) {
                InvokeInstruction invoke = (InvokeInstruction) inst;

                if(OUT_TAG_ESCAPE_XML.matches(invoke,cpg)) {
                    Integer booleanValue = ByteCode.getConstantInt(location.getHandle().getPrev());
                    if (booleanValue != null && booleanValue == 0) {
                        escapeXmlSetToFalse = true;
                        locationWeakness = location;
                    } else {
                        //Some JSP compiler convert boolean value at runtime (WebLogic)
                        String stringValue = JspUtils.getContanstBooleanAsString(instructionVisited, cpg);
                        if (stringValue != null && stringValue.equals("false")) {
                            escapeXmlSetToFalse = true;
                            locationWeakness = location;
                        }

                        if(booleanValue == null && stringValue == null) {
                            escapeXmlValueUnknown = true;
                            locationWeakness = location;
                        }
                    }
                }
            }
        }

        //Both condition have been found in the same method
        if (escapeXmlSetToFalse) {
            JavaClass clz = classContext.getJavaClass();
            bugReporter.reportBug(new BugInstance(this, JSP_JSTL_OUT, Priorities.NORMAL_PRIORITY) //
                    .addClass(clz)
                    .addMethod(clz, m)
                    .addSourceLine(classContext, m, locationWeakness));
        } else if (escapeXmlValueUnknown) {
            JavaClass clz = classContext.getJavaClass();
            bugReporter.reportBug(new BugInstance(this, JSP_JSTL_OUT, Priorities.LOW_PRIORITY) //
                    .addClass(clz)
                    .addMethod(clz, m)
                    .addSourceLine(classContext, m, locationWeakness));
        }
    }


    @Override
    public void report() {
    }

}
