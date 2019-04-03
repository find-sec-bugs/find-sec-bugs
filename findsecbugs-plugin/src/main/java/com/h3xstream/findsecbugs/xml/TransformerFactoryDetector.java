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
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.Location;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;

import java.util.Iterator;

/**
 * Currently the detector look for a specific code sequence. If the value is not hardcoded or computed at runtime, it will
 * be consider as possibly unsafe.
 *
 * Minimal effort was put in this detector to avoid giving a lot of resource for code section that usually all look the
 * same.
 *
 * Do note that the "safe" state for the TransformerFactory doesn't prevent denial of service attack like the LoL Bomb.
 */
public class TransformerFactoryDetector extends OpcodeStackDetector {

    private static final String XXE_DTD_TRANSFORM_FACTORY_TYPE = "XXE_DTD_TRANSFORM_FACTORY";
    private static final String XXE_XSLT_TRANSFORM_FACTORY_TYPE = "XXE_XSLT_TRANSFORM_FACTORY";

    private static final String PROPERTY_SUPPORT_DTD = "http://javax.xml.XMLConstants/property/accessExternalDTD";
    private static final String PROPERTY_SUPPORT_STYLESHEET = "http://javax.xml.XMLConstants/property/accessExternalStylesheet";
    private static final String PROPERTY_SECURE_PROCESSING = "http://javax.xml.XMLConstants/feature/secure-processing";

    private final BugReporter bugReporter;

    public TransformerFactoryDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen != Const.INVOKEVIRTUAL && seen != Const.INVOKEINTERFACE && seen != Const.INVOKESTATIC) {
            return;
        }
        String fullClassName = getClassConstantOperand();
        String method = getNameConstantOperand();
        //The method call is doing XML parsing (see class javadoc)
        if (seen == Const.INVOKESTATIC &&
                (fullClassName.equals("javax/xml/transform/TransformerFactory") ||
                fullClassName.equals("javax/xml/transform/sax/SAXTransformerFactory"))
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

                //DTD and Stylesheet disallow
                //factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                //factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
                if(inst instanceof INVOKEVIRTUAL || inst instanceof INVOKEINTERFACE) {
                    InvokeInstruction invoke = (InvokeInstruction) inst;
                    if ("setAttribute".equals(invoke.getMethodName(cpg))) {
                        LDC propertyConst = ByteCode.getPrevInstruction(location.getHandle().getPrev(), LDC.class);
                        LDC loadConst = ByteCode.getPrevInstruction(location.getHandle(), LDC.class);
                        if (propertyConst != null && loadConst != null) {
                            if (PROPERTY_SUPPORT_DTD.equals(propertyConst.getValue(cpg))) {
                                // All values other than "all", "http" and "jar" will disable external DTD processing.
                                // Since other vulnerable values could be added, we do not want to use a blacklist mechanism.
                                hasFeatureDTD = ( "".equals(loadConst.getValue(cpg)) );
                            } else if (PROPERTY_SUPPORT_STYLESHEET.equals(propertyConst.getValue(cpg))){
                                // All values other than "all", "http" and "jar" will disable external DTD processing.
                                // Since other vulnerable values could be added, we do not want to use a blacklist mechanism.
                                hasFeatureStylesheet = ( "".equals(loadConst.getValue(cpg)) );
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
