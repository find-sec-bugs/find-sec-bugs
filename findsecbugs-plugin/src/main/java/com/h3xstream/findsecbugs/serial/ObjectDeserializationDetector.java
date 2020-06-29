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
package com.h3xstream.findsecbugs.serial;

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.Location;
import edu.umd.cs.findbugs.ba.bcp.Invoke;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * Detect Java object deserialization
 *
 * @author Alexander Minozhenko
 */
public class ObjectDeserializationDetector implements Detector {

    private static final String OBJECT_DESERIALIZATION_TYPE = "OBJECT_DESERIALIZATION";

    private BugReporter bugReporter;


    private static List<String> OBJECT_INPUTSTREAM_READ_METHODS = Arrays.asList("readObject", //
            "readUnshared", "readArray");

    public ObjectDeserializationDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        Method[] methodList = javaClass.getMethods();

        for (Method m : methodList) {

            try {
                if((OBJECT_INPUTSTREAM_READ_METHODS.contains(m.getName()) && InterfaceUtils.isSubtype(javaClass, "java.io.Serializable")) ||
                        (InterfaceUtils.isSubtype(javaClass, "java.lang.reflect.InvocationHandler"))) {
                    continue;
                }

                analyzeMethod(m,classContext);
            } catch (CFGBuilderException e) {
            } catch (DataflowAnalysisException e) {
            }
        }
    }


    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException
    {

        MethodGen methodGen = classContext.getMethodGen(m);
        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        if (methodGen == null || methodGen.getInstructionList() == null) {
            return; //No instruction .. nothing to do
        }

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();
            Instruction inst = location.getHandle().getInstruction();

            //
            if (inst instanceof InvokeInstruction) {
                InvokeInstruction invoke = (InvokeInstruction) inst;

                String className = invoke.getClassName(cpg);
                if ("java.io.ObjectInputStream".equals(className) ||
                        className.contains("InputStream") ||
                        InterfaceUtils.isSubtype(className, "java.io.ObjectInputStream") ||
                        InterfaceUtils.isSubtype(className, "java.io.ObjectInput")) {

                    if(className.equals("org.bouncycastle.asn1.ASN1InputStream")) { //This class has a readObject method
                        continue;
                    }

                    String methodName = invoke.getMethodName(cpg);
                    if (OBJECT_INPUTSTREAM_READ_METHODS.contains(methodName)) {

                        JavaClass clz = classContext.getJavaClass();
                        bugReporter.reportBug(new BugInstance(this, OBJECT_DESERIALIZATION_TYPE, HIGH_PRIORITY) //
                                .addClass(clz).addMethod(clz, m).addSourceLine(classContext,m,location));
                    }
                }

            }
        }
    }


    @Override
    public void report() {

    }

}
