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
package com.h3xstream.findsecbugs.xml;

import com.h3xstream.findsecbugs.common.ByteCode;
import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Location;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import java.util.Iterator;
import org.apache.bcel.Const;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;

/**
 * Currently the detector look for a specific code sequence. If the value is not hardcoded or computed at runtime, it will
 * be consider has possible unsafe.
 *
 * Minimal effort was put in this detector to avoid giving alot of resource for code section that usually all look the
 * same.
 */
public class XmlStreamReaderDetector extends OpcodeStackDetector {

    private static final String XXE_XMLSTREAMREADER_TYPE = "XXE_XMLSTREAMREADER";;
    private static final String PROPERTY_SUPPORT_DTD = "javax.xml.stream.supportDTD";
    private static final String PROPERTY_IS_SUPPORTING_EXTERNAL_ENTITIES = "javax.xml.stream.isSupportingExternalEntities";
    private final BugReporter bugReporter;

    public XmlStreamReaderDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen != Const.INVOKEVIRTUAL) {
            return;
        }
        String fullClassName = getClassConstantOperand();
        String method = getNameConstantOperand();

        //The method call is doing XML parsing (see class javadoc)
        if (fullClassName.equals("javax/xml/stream/XMLInputFactory") &&
                (method.equals("createXMLStreamReader") || method.equals("createXMLEventReader") ||
                 method.equals("createFilteredReader"))) {
            ClassContext classCtx = getClassContext();
            ConstantPoolGen cpg = classCtx.getConstantPoolGen();
            CFG cfg;
            try {
                cfg = classCtx.getCFG(getMethod());
            } catch (CFGBuilderException e) {
                AnalysisContext.logError("Cannot get CFG", e);
                return;
            }
            for (Iterator<Location> i = cfg.locationIterator(); i.hasNext();) {
                Location location = i.next();
                Instruction inst = location.getHandle().getInstruction();

                //DTD disallow
                //XMLInputFactory.setProperty
                if (inst instanceof org.apache.bcel.generic.INVOKEVIRTUAL) {
                    InvokeInstruction invoke = (InvokeInstruction) inst;
                    if ("setProperty".equals(invoke.getMethodName(cpg))) {
                        org.apache.bcel.generic.LDC loadConst = ByteCode.getPrevInstruction(location.getHandle(), LDC.class);
                        if (loadConst != null) {
                            if (PROPERTY_SUPPORT_DTD.equals(loadConst.getValue(cpg)) || PROPERTY_IS_SUPPORTING_EXTERNAL_ENTITIES.equals(loadConst.getValue(cpg))){
                                InstructionHandle prev1 = location.getHandle().getPrev();
                                InstructionHandle prev2 = prev1.getPrev();
                                //Case where the boolean is wrapped like : Boolean.valueOf(true) : 2 instructions
                                if (invokeInstruction().atClass("java.lang.Boolean").atMethod("valueOf").matches(prev1.getInstruction(),cpg)) {
                                    if (prev2.getInstruction() instanceof ICONST) {
                                        Integer valueWrapped = ByteCode.getConstantInt(prev2);
                                        if (valueWrapped != null && valueWrapped.equals(0)) { //Value is false
                                            return; //Safe feature is disable
                                        }
                                    }
                                }
                                //Case where the boolean is declared as : Boolean.FALSE
                                else if (prev1.getInstruction() instanceof org.apache.bcel.generic.GETSTATIC) {
                                    org.apache.bcel.generic.GETSTATIC getstatic = (org.apache.bcel.generic.GETSTATIC) prev1.getInstruction();
                                    if (getstatic.getClassType(cpg).getClassName().equals("java.lang.Boolean") &&
                                            getstatic.getFieldName(cpg).equals("FALSE")) {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //Raise a bug
            bugReporter.reportBug(new BugInstance(this, XXE_XMLSTREAMREADER_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }
    }
}
