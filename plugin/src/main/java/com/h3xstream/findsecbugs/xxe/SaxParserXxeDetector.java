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
package com.h3xstream.findsecbugs.xxe;

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Location;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

import java.util.Iterator;

/**
 * The SaxParser use the Xerces XML Parser engine.
 * <ul>
 *   <li>SAXParser/XMLReader -> Xerces 1</li>
 *   <li>DocumentBuilder -> Xerces 2</li>
 * </ul>
 *
 * 3 equivalent APIs are covered by this detector.
 * <ul>
 *   <li>SAXParser.parse()</li>
 *   <li>XMLReader.parse()</li>
 *   <li>DocumentBuilder.parse()</li>
 * </ul>
 *
 * References:
 * <ul>
 *   <li>https://www.securecoding.cert.org/confluence/pages/viewpage.action?pageId=61702260</li>
 *   <li>https://www.owasp.org/index.php/XML_External_Entity_%28XXE%29_Processing</li>
 * </ul>
 */
public class SaxParserXxeDetector extends OpcodeStackDetector {
    private static final boolean DEBUG = false;
    private static final String XXE_SAX_PARSER_TYPE = "XXE_SAXPARSER";
    private static final String XXE_XML_READER_TYPE = "XXE_XMLREADER";
    private static final String XXE_DOCUMENT_TYPE = "XXE_DOCUMENT";

    private static final String FEATURE_DISALLOW_DTD = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final String FEATURE_SECURE_PROCESSING = "http://javax.xml.XMLConstants/feature/secure-processing";

    private static final String FEATURE_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
    private static final String FEATURE_EXTERNAL_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";

    private BugReporter bugReporter;

    public SaxParserXxeDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if(seen != Constants.INVOKEVIRTUAL && seen != INVOKEINTERFACE) return;

        String fullClassName = getClassConstantOperand();
        String method = getNameConstantOperand();

        //The method call is doing XML parsing (see class javadoc)
        if ((seen == Constants.INVOKEVIRTUAL &&
                fullClassName.equals("javax/xml/parsers/SAXParser") &&
                method.equals("parse")) ||
                (seen == Constants.INVOKEINTERFACE &&
                        fullClassName.equals("org/xml/sax/XMLReader") &&
                        method.equals("parse")) ||
                (seen == Constants.INVOKEVIRTUAL &&
                        getClassConstantOperand().equals("javax/xml/parsers/DocumentBuilder") &&
                        method.equals("parse"))) {

            JavaClass javaClass = getThisClass();

            //(1rst solution for secure parsing proposed by the CERT) Sandbox in an action with limited privileges
            if (InterfaceUtils.classImplements(javaClass, "java.security.PrivilegedExceptionAction")) {
                return; //Assuming the proper right are apply to the sandbox
            }

            ClassContext classCtx = getClassContext();
            ConstantPoolGen cpg = classCtx.getConstantPoolGen();

            CFG cfg = null;
            try {
                cfg = classCtx.getCFG(getMethod());
            } catch (CFGBuilderException e) {
            }

            //The combination of the 4 following is consider safe (expand option is only available with DocumentBuilderFactory)
            boolean hasSetXIncludeAware = false;
            boolean hasExpandEntityReferences = getClassConstantOperand().equals("javax/xml/parsers/DocumentBuilder") ? false : true;
            boolean hasFeatureGeneralEntities = false;
            boolean hasFeatureExternalEntities = false;

            for (Iterator<Location> i = cfg.locationIterator(); i.hasNext();) {
                Location location = i.next();
                Instruction inst = location.getHandle().getInstruction();
                if (DEBUG) {
                    ByteCode.printOpCode(inst, cpg);
                }


                //(2nd solution for secure parsing proposed by the CERT) Look for entity custom resolver
                if (inst instanceof INVOKEINTERFACE) { //XMLReader.setEntityResolver is called
                    INVOKEINTERFACE invoke = (INVOKEINTERFACE) inst;
                    if ("setEntityResolver".equals(invoke.getMethodName(cpg))) {
                        return;
                    }
                }

                //DTD disallow
                if(inst instanceof INVOKEVIRTUAL) {
                    INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) inst;
                    if ("setFeature".equals(invoke.getMethodName(cpg))) {
                        LDC loadConst = ByteCode.getPrevInstruction(location.getHandle(), LDC.class);

                        if (loadConst != null) {
                            if (FEATURE_DISALLOW_DTD.equals(loadConst.getValue(cpg))){
                                return;
                            }
                            else if (FEATURE_SECURE_PROCESSING.equals(loadConst.getValue(cpg))){
                                return;
                            }
                            else if (FEATURE_GENERAL_ENTITIES.equals(loadConst.getValue(cpg))){
                                hasFeatureGeneralEntities = true;
                            }
                            else if (FEATURE_EXTERNAL_ENTITIES.equals(loadConst.getValue(cpg))){
                                hasFeatureExternalEntities = true;
                            }
                        }
                    }

                    else if ("setXIncludeAware".equals(invoke.getMethodName(cpg))) {
                        ICONST boolConst = ByteCode.getPrevInstruction(location.getHandle(), ICONST.class);
                        if (boolConst != null && boolConst.getValue().equals(0)) {
                            hasSetXIncludeAware = true;
                        }
                    }

                    else if ("setExpandEntityReferences".equals(invoke.getMethodName(cpg))) {
                        ICONST boolConst = ByteCode.getPrevInstruction(location.getHandle(), ICONST.class);
                        if (boolConst != null && boolConst.getValue().equals(0)) {
                            hasExpandEntityReferences = true;
                        }
                    }
                }


            }

            //Manual configuration include all the suggested settings
            if(hasFeatureExternalEntities && hasFeatureGeneralEntities && hasSetXIncludeAware && hasExpandEntityReferences) {
                return;
            }


            String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('/'));
            //Raise a bug

            if(fullClassName.equals("javax/xml/parsers/SAXParser")) {
                bugReporter.reportBug(new BugInstance(this, XXE_SAX_PARSER_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this)
                        .addString(simpleClassName + "." + method + "(...)"));
            }
            else if(fullClassName.equals("org/xml/sax/XMLReader")) {
                bugReporter.reportBug(new BugInstance(this, XXE_XML_READER_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this)
                        .addString(simpleClassName + "." + method + "(...)"));
            }
            else if(fullClassName.equals("javax/xml/parsers/DocumentBuilder")) {
                bugReporter.reportBug(new BugInstance(this, XXE_DOCUMENT_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this)
                        .addString(simpleClassName + "." + method + "(...)"));
            }
        }
    }
}
