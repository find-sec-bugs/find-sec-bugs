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
import org.apache.bcel.Const;

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

    private static final InvokeMatcherBuilder RANDOM_STRING_UTILS_METHODS = invokeInstruction()
              .atClass("org/apache/commons/lang/RandomStringUtils") //
              .atMethod("random", "randomAscii", "randomAlphabetic", "randomAlphanumeric", //
                      "randomGraph", "randomNumeric", "randomPrint");
    
    private static final InvokeMatcherBuilder RANDOM_UTILS_METHODS = invokeInstruction() //
              .atClass("org/apache/commons/lang/math/RandomUtils") //
              .atMethod("nextBoolean", "nextDouble", "nextFloat", "nextInt", "nextLong");

    private BugReporter bugReporter;

    public PredictableRandomDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    public void reportBug(String module, String error) {

        bugReporter.reportBug(new BugInstance(this, error, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString(module));
    }

    @Override
    public void sawOpcode(int seen) {
        // printOpCode(seen);

        if (seen == Const.INVOKESPECIAL && getClassConstantOperand().equals("java/util/Random")
                && getNameConstantOperand().equals("<init>")) {

            reportBug("java.util.Random", PREDICTABLE_RANDOM_TYPE);

        } else if (seen == Const.INVOKESTATIC && getClassConstantOperand().equals("java/lang/Math")
                && getNameConstantOperand().equals("random")) {

            reportBug("java.lang.Math.random()", PREDICTABLE_RANDOM_TYPE);

        } else if (seen == Const.INVOKESTATIC && getClassConstantOperand().equals("java/util/concurrent/ThreadLocalRandom")
                && getNameConstantOperand().equals("current")) {

            reportBug("java.util.concurrent.ThreadLocalRandom", PREDICTABLE_RANDOM_TYPE);

        } else if (seen == Const.INVOKESPECIAL && getClassConstantOperand().equals("scala/util/Random")
                && getNameConstantOperand().equals("<init>")) {

            reportBug("scala.util.Random", PREDICTABLE_RANDOM_SCALA_TYPE);

        } else if (seen == Const.INVOKEVIRTUAL && RANDOM_NEXT_METHODS.matches(this)) {

            reportBug("scala.util.Random." + getNameConstantOperand() + "()", PREDICTABLE_RANDOM_SCALA_TYPE);

        } else if (seen == Const.INVOKESPECIAL && getClassConstantOperand().equals("org/apache/commons/lang/math/JVMRandom")
                && getNameConstantOperand().equals("<init>")) {
             
             reportBug("org.apache.commons.lang.math.JVMRandom", PREDICTABLE_RANDOM_TYPE);
    
        // JVMRandom has specific static version of nextLong()
        } else if (seen == Const.INVOKESTATIC && getClassConstantOperand().equals("org/apache/commons/lang/math/JVMRandom")
                && getNameConstantOperand().equals("nextLong")) {

            reportBug("org.apache.commons.lang.math.JVMRandom.nextLong()", PREDICTABLE_RANDOM_TYPE);

        // RandomUtils has only static methods
        } else if (seen == Const.INVOKESTATIC && RANDOM_UTILS_METHODS.matches(this)) {
            
            reportBug("org.apache.commons.lang.math.RandomUtils" + getNameConstantOperand() + "()", PREDICTABLE_RANDOM_TYPE);

        // RandomStringUtils has only static methods
        } else if (seen == Const.INVOKESTATIC && RANDOM_STRING_UTILS_METHODS.matches(this)) {
           
            reportBug("org.apache.commons.lang.RandomStringUtils" + getNameConstantOperand() + "()", PREDICTABLE_RANDOM_TYPE);

        }
    }


}
