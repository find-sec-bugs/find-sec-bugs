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
package com.h3xstream.findsecbugs.taintanalysis.extra;

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.generic.*;

import java.util.List;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * This class detect potential default value and set it to the Taint instance.
 *
 * For example in <code>props.getProperties("password","admin1234")</code>, the second parameter (admin1234) is the default value if the property is not set.
 *
 * This detector doesn't report bugs like the other detector it only enhances the taint analysis.
 * Being a detector, the behavior can be deactivated easily.
 *
 */
public class PotentialValueTracker extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {

    private static final InvokeMatcherBuilder PROPERTIES_GET_WITH_DEFAULT = invokeInstruction()
            .atClass("java/util/Properties")
            .atMethod("getProperty")
            .withArgs("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");

    private static final InvokeMatcherBuilder OPTIONAL_OR = invokeInstruction()
            .atClass("com/google/common/base/Optional")
            .atMethod("or")
            .withArgs("(Ljava/lang/Object;)Ljava/lang/Object;");

    private static final InvokeMatcherBuilder HASHMAP_GET_WITH_DEFAULT = invokeInstruction()
            .atClass("java/util/HashMap")
            .atMethod("getOrDefault")
            .withArgs("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    public PotentialValueTracker(BugReporter bugReporter) {
        super(bugReporter);
        registerVisitor(this);
    }


    @Override
    public void visitInvoke(InvokeInstruction invoke, MethodGen methodGen, TaintFrame frameType, List<Taint> parameters, ConstantPoolGen cpg) throws DataflowAnalysisException{

        if(PROPERTIES_GET_WITH_DEFAULT.matches(invoke,cpg) || OPTIONAL_OR.matches(invoke,cpg) || HASHMAP_GET_WITH_DEFAULT.matches(invoke,cpg)) {
            Taint defaultVal = parameters.get(0); //Top of the stack last arguments
            if(defaultVal.getConstantValue() != null) {
                Taint value = frameType.getTopValue();
                value.setPotentialValue(defaultVal.getConstantValue());
            }
        }

        //ByteCode.printOpCode(invoke,cpg);
    }

    @Override
    public void visitLoad(LoadInstruction load, MethodGen methodGen, TaintFrame frameType, int numProduced, ConstantPoolGen cpg) {

    }

    @Override
    public void visitField(FieldInstruction put, MethodGen methodGen, TaintFrame frameType, Taint taintFrame, int numProduced, ConstantPoolGen cpg) throws Exception {

    }

    @Override
    public void visitReturn(MethodGen methodGen, Taint returnValue, ConstantPoolGen cpg) throws Exception {

    }


}
