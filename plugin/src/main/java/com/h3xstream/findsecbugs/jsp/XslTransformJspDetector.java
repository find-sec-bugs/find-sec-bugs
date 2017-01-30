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
import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;
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
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;

public class XslTransformJspDetector implements Detector {
    
    private static final String JSP_XSLT = "JSP_XSLT";
    private final BugReporter bugReporter;
    private static final InvokeMatcherBuilder TRANSFORM_TAG_XSLT = invokeInstruction()
            .atClass("org.apache.taglibs.standard.tag.rt.xml.TransformTag") //
            .atMethod("setXslt") //
            .withArgs("(Ljava/lang/Object;)V");

    public XslTransformJspDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        try {
            if (!Hierarchy.isSubtype(javaClass.getClassName(), "javax.servlet.http.HttpServlet")) {
                return;
            }
        } catch (ClassNotFoundException e) {
            AnalysisContext.reportMissingClass(e);
        }
        for (Method m : javaClass.getMethods()) {
            try {
                analyzeMethod(m, classContext);
            } catch (CFGBuilderException | DataflowAnalysisException e) {
                AnalysisContext.logError("Cannot analyze method", e);
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {

        //Bytecode representation of the TransformTag instantiation
//        XslTransformJspDetector: [0035]  ldc   "${param.xml}"
//        XslTransformJspDetector: [0037]  ldc   java/lang/Object
//        XslTransformJspDetector: [0039]  aload_1
//        XslTransformJspDetector: [0040]  aconst_null
//        XslTransformJspDetector: [0041]  invokestatic   org/apache/jasper/runtime/PageContextImpl.evaluateExpression (Ljava/lang/String;Ljava/lang/Class;Ljavax/servlet/jsp/PageContext;Lorg/apache/jasper/runtime/ProtectedFunctionMapper;)Ljava/lang/Object;
//        XslTransformJspDetector: [0044]  invokevirtual   org/apache/taglibs/standard/tag/rt/xml/TransformTag.setXml (Ljava/lang/Object;)V
//        XslTransformJspDetector: [0047]  aload   4
//        XslTransformJspDetector: [0049]  ldc   "${param.xslt}"
//        XslTransformJspDetector: [0051]  ldc   java/lang/Object
//        XslTransformJspDetector: [0053]  aload_1
//        XslTransformJspDetector: [0054]  aconst_null
//        XslTransformJspDetector: [0055]  invokestatic   org/apache/jasper/runtime/PageContextImpl.evaluateExpression (Ljava/lang/String;Ljava/lang/Class;Ljavax/servlet/jsp/PageContext;Lorg/apache/jasper/runtime/ProtectedFunctionMapper;)Ljava/lang/Object;
//        XslTransformJspDetector: [0058]  invokevirtual   org/apache/taglibs/standard/tag/rt/xml/TransformTag.setXslt (Ljava/lang/Object;)V
//        XslTransformJspDetector: [0061]  aload   4
//        XslTransformJspDetector: [0063]  invokevirtual   org/apache/taglibs/standard/tag/rt/xml/TransformTag.doStartTag ()I
//        XslTransformJspDetector: [0066]  istore
//        XslTransformJspDetector: [0068]  aload   4
//        XslTransformJspDetector: [0070]  invokevirtual   org/apache/taglibs/standard/tag/rt/xml/TransformTag.doEndTag ()I

        //Conditions that needs to fill to identify the vulnerability
        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();
            Instruction inst = location.getHandle().getInstruction();
            //ByteCode.printOpCode(inst,cpg);
            if (TRANSFORM_TAG_XSLT.matches(inst,cpg)) {
                String value = ByteCode.getConstantLDC(location.getHandle().getPrev(),cpg,String.class);
                if (value == null) {
                    JavaClass clz = classContext.getJavaClass();
                    bugReporter.reportBug(new BugInstance(this, JSP_XSLT, Priorities.HIGH_PRIORITY) //
                            .addClass(clz)
                            .addMethod(clz, m)
                            .addSourceLine(classContext, m, location));
                }
            }
        }
    }

    @Override
    public void report() {
    }
}
