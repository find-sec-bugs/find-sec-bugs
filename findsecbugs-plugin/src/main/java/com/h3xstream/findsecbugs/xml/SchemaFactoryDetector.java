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
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Location;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;

import javax.xml.XMLConstants;

/**
 * Detector for XML External Entity and External Schema processing in javax.xml.validation.SchemaFactory
 */
public class SchemaFactoryDetector extends OpcodeStackDetector {

    private static final String XXE_SCHEMA_FACTORY_TYPE = "XXE_SCHEMA_FACTORY";

    private static final String SCHEMA_FACTORY_CLASS_NAME = "javax/xml/validation/SchemaFactory";

    private static final String SET_FEATURE_METHOD = "setFeature";

    private static final String SET_PROPERTY_METHOD = "setProperty";

    private static final String NEW_SCHEMA_METHOD_NAME = "newSchema";

    private static final Number BOOLEAN_TRUE_VALUE = 1;

    private static final String EXTERNAL_REFERENCES_DISABLED = "";

    private final BugReporter bugReporter;

    public SchemaFactoryDetector(final BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(final int opcode) {
        if (isNotNewSchemaMethod(opcode)) {
            return;
        }

        final ClassContext classContext = getClassContext();
        final CFG cfg;
        try {
            cfg = classContext.getCFG(getMethod());
        } catch (final CFGBuilderException e) {
            AnalysisContext.logError("Cannot get CFG", e);
            return;
        }

        final ConstantPoolGen cpg = classContext.getConstantPoolGen();

        boolean secureProcessingEnabled = false;
        boolean accessExternalDtdDisabled = false;
        boolean accessExternalSchemaDisabled = false;

        for (final Location location : cfg.locations()) {
            if (isSecureProcessingEnabled(location, cpg)) {
                secureProcessingEnabled = true;
            } else if (isAccessPropertyDisabled(location, cpg, XMLConstants.ACCESS_EXTERNAL_DTD)) {
                accessExternalDtdDisabled = true;
            } else if (isAccessPropertyDisabled(location, cpg, XMLConstants.ACCESS_EXTERNAL_SCHEMA)) {
                accessExternalSchemaDisabled = true;
            }
        }

        // Enabling Secure Processing or disabling both Access External DTD and Access External Schema are solutions
        if (secureProcessingEnabled || (accessExternalDtdDisabled && accessExternalSchemaDisabled)) {
            return;
        }

        bugReporter.reportBug(new BugInstance(this, XXE_SCHEMA_FACTORY_TYPE, Priorities.HIGH_PRIORITY)
                .addClass(this).addMethod(this).addSourceLine(this));
    }

    private boolean isNotNewSchemaMethod(final int opcode) {
        boolean notValidateInvocation = true;

        if (Const.INVOKEVIRTUAL == opcode) {
            final String slashedClassName = getClassConstantOperand();
            final String methodName = getNameConstantOperand();
            if (SCHEMA_FACTORY_CLASS_NAME.equals(slashedClassName) && NEW_SCHEMA_METHOD_NAME.equals(methodName)) {
                notValidateInvocation = false;
            }
        }

        return notValidateInvocation;
    }

    private boolean isSecureProcessingEnabled(final Location location, final ConstantPoolGen cpg) {
        boolean enabled = false;
        final Instruction instruction = location.getHandle().getInstruction();

        if (instruction instanceof INVOKEVIRTUAL) {
            final InvokeInstruction invokeInstruction = (InvokeInstruction) instruction;
            final String instructionMethodName = invokeInstruction.getMethodName(cpg);
            final InstructionHandle handle = location.getHandle();
            if (SET_FEATURE_METHOD.equals(instructionMethodName)) {
                final Object ldcValue = getLdcValue(handle, cpg);
                if (XMLConstants.FEATURE_SECURE_PROCESSING.equals(ldcValue)) {
                    final ICONST constant = ByteCode.getPrevInstruction(handle, ICONST.class);
                    enabled = constant != null && BOOLEAN_TRUE_VALUE.equals(constant.getValue());
                }
            }
        }

        return enabled;
    }

    private boolean isAccessPropertyDisabled(final Location location, final ConstantPoolGen cpg, final String accessPropertyName) {
        boolean enabled = false;
        final Instruction instruction = location.getHandle().getInstruction();

        if (instruction instanceof INVOKEVIRTUAL) {
            final InvokeInstruction invokeInstruction = (InvokeInstruction) instruction;
            final String instructionMethodName = invokeInstruction.getMethodName(cpg);
            final InstructionHandle handle = location.getHandle();
            if (SET_PROPERTY_METHOD.equals(instructionMethodName)) {
                final Object propertyName = getLdcValue(handle.getPrev(), cpg);
                final Object propertyValue = getLdcValue(handle, cpg);
                if (accessPropertyName.equals(propertyName)) {
                    enabled = EXTERNAL_REFERENCES_DISABLED.equals(propertyValue);
                }
            }
        }

        return enabled;
    }

    private Object getLdcValue(final InstructionHandle instructionHandle, final ConstantPoolGen cpg) {
        final LDC ldc = ByteCode.getPrevInstruction(instructionHandle, LDC.class);
        return ldc == null ? null : ldc.getValue(cpg);
    }
}
