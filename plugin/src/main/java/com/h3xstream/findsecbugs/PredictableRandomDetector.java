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
package com.h3xstream.findsecbugs;

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

public class PredictableRandomDetector extends OpcodeStackDetector {

    private static final String PREDICTABLE_RANDOM_TYPE = "PREDICTABLE_RANDOM";
    private static final String PREDICTABLE_RANDOM_SCALA_TYPE = "PREDICTABLE_RANDOM_SCALA";

    //Static method call to next method can be differentiate from instance calls
    // by the presence of a dollar sign at the end of the class name
//    PredictableRandomDetector: [0038]  pop2
//    PredictableRandomDetector: [0039]  getstatic
//    PredictableRandomDetector: [0042]  invokevirtual   scala/util/Random$.nextBoolean ()
//    [...]
//    PredictableRandomDetector: [0092]  getstatic
//    PredictableRandomDetector: [0095]  invokevirtual   scala/util/Random$.nextDouble ()D
//    PredictableRandomDetector: [0098]  pop2
//    PredictableRandomDetector: [0099]  getstatic
//    PredictableRandomDetector: [0102]  invokevirtual   scala/util/Random$.nextFloat ()F

    private static final InvokeMatcherBuilder RANDOM_NEXT_METHODS = invokeInstruction().atClass("scala/util/Random$") //
            .atMethod("nextBoolean", "nextBytes", "nextDouble", "nextFloat", "nextGaussian", "nextInt", "nextLong", //
                    "nextString","nextPrintableChar");

    private BugReporter bugReporter;

    public PredictableRandomDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);

        if (seen == Constants.INVOKESPECIAL && getClassConstantOperand().equals("java/util/Random")
                && getNameConstantOperand().equals("<init>")) {

            bugReporter.reportBug(new BugInstance(this, PREDICTABLE_RANDOM_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.util.Random"));

        } else if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("java/lang/Math")
                && getNameConstantOperand().equals("random")) {

            bugReporter.reportBug(new BugInstance(this, PREDICTABLE_RANDOM_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.lang.Math.random()"));

        } else if (seen == Constants.INVOKESPECIAL && getClassConstantOperand().equals("scala/util/Random")
                && getNameConstantOperand().equals("<init>")) {

            bugReporter.reportBug(new BugInstance(this, PREDICTABLE_RANDOM_SCALA_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("scala.util.Random"));

        } else if (seen == Constants.INVOKEVIRTUAL && RANDOM_NEXT_METHODS.matches(this)) {

            bugReporter.reportBug(new BugInstance(this, PREDICTABLE_RANDOM_SCALA_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("scala.util.Random."+getNameConstantOperand()+"()"));
        }
    }


}
