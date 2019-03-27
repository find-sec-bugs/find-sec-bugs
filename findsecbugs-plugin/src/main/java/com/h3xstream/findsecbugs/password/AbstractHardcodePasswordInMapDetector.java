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

import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;

import java.util.ArrayList;
import java.util.List;

/**
 * Detect hard-code password in settings map (key value configurations constructed at runtime)
 */
public abstract class AbstractHardcodePasswordInMapDetector extends BasicInjectionDetector {

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

    public AbstractHardcodePasswordInMapDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset)
            throws DataflowAnalysisException {
        Taint valueTaint = fact.getStackValue(0);
        Taint parameterTaint = fact.getStackValue(1);

        if (valueTaint.getConstantValue() == null || parameterTaint.getConstantValue() == null) {
            return Priorities.IGNORE_PRIORITY;
        }

        String parameterValue = parameterTaint.getConstantValue().toLowerCase();
        if (parameterValue.equals("java.naming.security.credentials")) {
            return Priorities.NORMAL_PRIORITY;
        }
        for (String password : PASSWORD_WORDS) {
            if (parameterValue.contains(password)) {//Is a constant value
                return Priorities.NORMAL_PRIORITY;
            }
        }
        return Priorities.IGNORE_PRIORITY;
    }
}
