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
import com.h3xstream.findsecbugs.common.InterfaceUtils;
import com.h3xstream.findsecbugs.common.StackUtils;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.password.IntuitiveHardcodePasswordDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.*;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

import java.util.Iterator;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * Currently the detector look for a specific code sequence. If the value is not hardcoded or computed at runtime, it will
 * be consider has possible unsafe.
 *
 * Minimal effort was put in this detector to avoid giving alot of resource for code section that usually all look the
 * same.
 */
public class TransformerFactoryDetector extends OpcodeStackDetector {

    private static final String XXE_DTD_TRANSFORM_FACTORY_TYPE = "XXE_DTD_TRANSFORM_FACTORY";
    private static final String XXE_XSLT_TRANSFORM_FACTORY_TYPE = "XXE_XSLT_TRANSFORM_FACTORY";

    private static final String PROPERTY_SUPPORT_DTD = "http://javax.xml.XMLConstants/property/accessExternalDTD";
    private static final String PROPERTY_SUPPORT_STYLESHEET = "http://javax.xml.XMLConstants/property/accessExternalStylesheet";
    private static final String PROPERTY_SECURE_PROCESSING = "http://javax.xml.XMLConstants/feature/secure-processing";

    private static final InvokeMatcherBuilder TRANSFORM_METHOD = invokeInstruction() //
            .atClass("javax/xml/transform/Transformer").atMethod("transform").withArgs("(Ljavax/xml/transform/Source;Ljavax/xml/transform/Result;)V");

    private final BugReporter bugReporter;

    public TransformerFactoryDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen != Constants.INVOKEVIRTUAL && seen != INVOKEINTERFACE && seen != INVOKESTATIC) {
            return;
        }
        String fullClassName = getClassConstantOperand();
        String method = getNameConstantOperand();
        //The method call is doing XML parsing (see class javadoc)
        if (seen == Constants.INVOKESTATIC && fullClassName.equals("javax/xml/transform/TransformerFactory")
                && method.equals("newInstance")) {
            ClassContext classCtx = getClassContext();
            ConstantPoolGen cpg = classCtx.getConstantPoolGen();
            CFG cfg;
            try {
                cfg = classCtx.getCFG(getMethod());
            } catch (CFGBuilderException e) {
                AnalysisContext.logError("Cannot get CFG", e);
                return;
            }

            //The combination of the 2 following is consider safe
            boolean hasFeatureDTD = false;
            boolean hasFeatureStylesheet = false;

            boolean hasSecureProcessing = false;

            for (Iterator<Location> i = cfg.locationIterator(); i.hasNext();) {
                Location location = i.next();
                Instruction inst = location.getHandle().getInstruction();
                //ByteCode.printOpCode(inst, cpg);

                //DTD disallow
                //factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                //factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
                if(inst instanceof INVOKEVIRTUAL || inst instanceof INVOKEINTERFACE) {
                    InvokeInstruction invoke = (InvokeInstruction) inst;
                    if ("setAttribute".equals(invoke.getMethodName(cpg))) {
                        LDC propertyConst = ByteCode.getPrevInstruction(location.getHandle().getPrev(), LDC.class);
                        LDC loadConst = ByteCode.getPrevInstruction(location.getHandle(), LDC.class);
                        if (propertyConst != null && loadConst != null) {
                            if (PROPERTY_SUPPORT_DTD.equals(propertyConst.getValue(cpg))) {
                                // Values "" and "all" disable external DTD processing. All other
                                // values are considered vulnerable
                                hasFeatureDTD = ("".equals(loadConst.getValue(cpg)) || "all".equals(loadConst.getValue(cpg)));
                            } else if (PROPERTY_SUPPORT_STYLESHEET.equals(propertyConst.getValue(cpg))){
                                // Values "" and "all" disable external Stylesheet processing. All other
                                // values are considered vulnerable
                                hasFeatureStylesheet = ("".equals(loadConst.getValue(cpg)) || "all".equals(loadConst.getValue(cpg)));
                            }
                        }
                    } else if ("setFeature".equals(invoke.getMethodName(cpg))) {
                        LDC propertyConst = ByteCode.getPrevInstruction(location.getHandle().getPrev(), LDC.class);
                        ICONST loadConst = ByteCode.getPrevInstruction(location.getHandle(), ICONST.class);
                        if (propertyConst != null && loadConst != null
                                && PROPERTY_SECURE_PROCESSING.equals(propertyConst.getValue(cpg))){
                            // If SecureProcessing is set to true (loadConst == 1), the call is not vulnerable
                            hasSecureProcessing = loadConst.getValue().equals(1);
                        }
                    }
                }
            }

            // Secure Processing includes all the suggested settings
            if (hasSecureProcessing) {
                return;
            }

            String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('/') + 1);

            //Raise a bug
            if (!hasFeatureDTD) {
                bugReporter.reportBug(new BugInstance(this, XXE_DTD_TRANSFORM_FACTORY_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this)
                        .addString(simpleClassName + "." + method + "(...)"));
            }

            if (!hasFeatureStylesheet) {
                bugReporter.reportBug(new BugInstance(this, XXE_XSLT_TRANSFORM_FACTORY_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this)
                        .addString(simpleClassName + "." + method + "(...)"));
            }
        }
    }
}
