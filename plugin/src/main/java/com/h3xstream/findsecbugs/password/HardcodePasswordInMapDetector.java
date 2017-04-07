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
package com.h3xstream.findsecbugs.password;

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

import java.util.ArrayList;
import java.util.List;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * Detect hard-code password in settings map (key value configurations constructed at runtime)
 */
public class HardcodePasswordInMapDetector extends BasicInjectionDetector {

    private static final String HARD_CODE_PASSWORD_TYPE = "HARD_CODE_PASSWORD";

    private static final InvokeMatcherBuilder HASHTABLE_PUT_METHOD = invokeInstruction().atMethod("put")
            .withArgs("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final InvokeMatcherBuilder HASHTABLE_SET_PROPERTY = invokeInstruction().atMethod("setProperty")
            .withArgs("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");

    private static List<String> PASSWORD_WORDS = new ArrayList<String>();
    static {
        //Passwords in various language
        //http://www.indifferentlanguages.com/words/password
        PASSWORD_WORDS.add("password");
        PASSWORD_WORDS.add("motdepasse");
        PASSWORD_WORDS.add("heslo");
        PASSWORD_WORDS.add("adgangskode");
        PASSWORD_WORDS.add("wachtwoord");
        PASSWORD_WORDS.add("salasana");
        PASSWORD_WORDS.add("passwort");
        PASSWORD_WORDS.add("passord");
        PASSWORD_WORDS.add("senha");
        PASSWORD_WORDS.add("geslo");
        PASSWORD_WORDS.add("clave");
        PASSWORD_WORDS.add("losenord");
        PASSWORD_WORDS.add("clave");
        PASSWORD_WORDS.add("parola");
        //Others
        PASSWORD_WORDS.add("secretkey");
        PASSWORD_WORDS.add("pwd");
    }

    public HardcodePasswordInMapDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset)
            throws DataflowAnalysisException {
        Taint valueTaint = fact.getStackValue(0);
        Taint parameterTaint = fact.getStackValue(1);

        if(valueTaint.getConstantValue() == null || parameterTaint.getConstantValue() == null) {
            return Priorities.IGNORE_PRIORITY;
        }

        String parameterValue = parameterTaint.getConstantValue().toLowerCase();
        if(parameterValue.equals("java.naming.security.credentials")) {
            return Priorities.NORMAL_PRIORITY;
        }
        for (String password : PASSWORD_WORDS) {
            if (parameterValue.contains(password)) {//Is a constant value
                return Priorities.NORMAL_PRIORITY;
            }
        }
        return Priorities.IGNORE_PRIORITY;
    }


    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
                                               InstructionHandle handle) {
        assert invoke != null && cpg != null;

        if(HASHTABLE_PUT_METHOD.matches(invoke,cpg) || HASHTABLE_SET_PROPERTY.matches(invoke,cpg)) {
            return new InjectionPoint(new int[]{0}, HARD_CODE_PASSWORD_TYPE); //Only the value is
        }
        return InjectionPoint.NONE;
    }
}
