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
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import com.h3xstream.findsecbugs.taintanalysis.TaintTag;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.MethodGen;

import javax.xml.XMLConstants;
import java.util.List;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * Detector for XML External Entity and External Schema processing in javax.xml.validation.SchemaFactory
 */
public class SchemaFactoryDetector extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {

    private static final String XXE_SCHEMA_FACTORY_TYPE = "XXE_SCHEMA_FACTORY";

    private static final String SET_FEATURE_METHOD = "setFeature";

    private static final String SET_PROPERTY_METHOD = "setProperty";

    private static final Number BOOLEAN_TRUE_VALUE = 1;

    private static final String EXTERNAL_REFERENCES_DISABLED = "";

    private static final TaintTag XXE_SCHEMA_FACTORY_SECURE_PROCESSING_SAFE = () -> "XXE_SCHEMA_FACTORY_SECURE_PROCESSING_SAFE";

    private static final TaintTag XXE_SCHEMA_FACTORY_EXTERNAL_DTD_DISABLED = () -> "XXE_SCHEMA_FACTORY_EXTERNAL_DTD_DISABLED";

    private static final TaintTag XXE_SCHEMA_FACTORY_EXTERNAL_SCHEMA_DISABLED = () -> "XXE_SCHEMA_FACTORY_EXTERNAL_SCHEMA_DISABLED";

    private static final InvokeMatcherBuilder SET_PROPERTY = invokeInstruction()
            .atClass("javax/xml/validation/SchemaFactory")
            .atMethod("setProperty")
            .withArgs("(Ljava/lang/String;Ljava/lang/Object;)V");

    private static final InvokeMatcherBuilder SET_FEATURE = invokeInstruction()
            .atClass("javax/xml/validation/SchemaFactory")
            .atMethod("setFeature")
            .withArgs("(Ljava/lang/String;Z)V");

    public SchemaFactoryDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("schema-factory.txt", XXE_SCHEMA_FACTORY_TYPE);

        registerVisitor(this);
    }


    @Override
    public void visitInvoke(InvokeInstruction invoke, MethodGen methodGen, TaintFrame frameType, Taint instanceTaint, List<Taint> parameters, ConstantPoolGen cpg, Location location) {
        boolean setFeatureMatches = SET_FEATURE.matches(invoke, cpg);
        boolean setPropertyMatches = SET_PROPERTY.matches(invoke, cpg);
        if (!setFeatureMatches && !setPropertyMatches) {
            return;
        }

        if (isSecureProcessingEnabled(location, cpg)) {
            instanceTaint.addTag(XXE_SCHEMA_FACTORY_SECURE_PROCESSING_SAFE);
        } else if (isAccessPropertyDisabled(location, cpg, XMLConstants.ACCESS_EXTERNAL_DTD)) {
            instanceTaint.addTag(XXE_SCHEMA_FACTORY_EXTERNAL_DTD_DISABLED);
        } else if (isAccessPropertyDisabled(location, cpg, XMLConstants.ACCESS_EXTERNAL_SCHEMA)) {
            instanceTaint.addTag(XXE_SCHEMA_FACTORY_EXTERNAL_SCHEMA_DISABLED);
        }
    }

    @Override
    protected int getPriority(Taint taint) {
        boolean secureProcessingEnabled = taint.hasTag(XXE_SCHEMA_FACTORY_SECURE_PROCESSING_SAFE);
        boolean accessExternalDtdDisabled = taint.hasTag(XXE_SCHEMA_FACTORY_EXTERNAL_DTD_DISABLED);
        boolean accessExternalSchemaDisabled = taint.hasTag(XXE_SCHEMA_FACTORY_EXTERNAL_SCHEMA_DISABLED);

        // Enabling Secure Processing or disabling both Access External DTD and Access External Schema are solutions
        if (secureProcessingEnabled || (accessExternalDtdDisabled && accessExternalSchemaDisabled)) {
            return Priorities.IGNORE_PRIORITY;
        } else {
            return Priorities.HIGH_PRIORITY;
        }
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
