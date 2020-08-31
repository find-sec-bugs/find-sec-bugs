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

import com.h3xstream.findsecbugs.common.TaintUtil;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This detector will find what look like password hardcode on unknown API.
 * Such as:
 * <code>MyCustomClient.setPassword("abc123!");</code></p>
 *
 * <p>It will also find API written in other language.
 * <code>MonClient.defMotDePasse("abc123!");</code></p>
 *
 * <p>It will match method that contains other keywords prefixing or suffixing.
 * <code>MyClient.setConnectionPwd("abc123!");</code></p>
 */
public class IntuitiveHardcodePasswordDetector extends BasicInjectionDetector {

    private static final String HARD_CODE_PASSWORD_TYPE = "HARD_CODE_PASSWORD";

    /**
     * Passwords in various language
     * http://www.indifferentlanguages.com/words/password
     *
     * The keyword is also used to detect variable name that are likely to be password (reused in AbstractHardcodedPassword).
     */
    @SuppressFBWarnings(value = "MS_MUTABLE_COLLECTION_PKGPROTECT",
            justification = "It is intended to be shared with AbstractHardcodedPassword. Accidental modification of this list is unlikely.")
    protected static final List<String> PASSWORD_WORDS = new ArrayList<String>();
    static {
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

    public IntuitiveHardcodePasswordDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset)
            throws DataflowAnalysisException {
        Taint stringValue = fact.getStackValue(offset);

        if (TaintUtil.isConstantValue(stringValue)) { //Is a constant value
            return Priorities.NORMAL_PRIORITY;
        } else {
            return Priorities.IGNORE_PRIORITY;
        }
    }

    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
                                               InstructionHandle handle) {
        assert invoke != null && cpg != null;

        String method = invoke.getMethodName(cpg);
        String sig    = invoke.getSignature(cpg);
        if(sig.startsWith("(Ljava/lang/String;)")) {
            if(method.startsWith("set")) { // Targeting : x.setPassword("abc123")
                String methodLowerCase = method.toLowerCase();
                for (String password : PASSWORD_WORDS) {
                    if (methodLowerCase.contains(password)) {
                        return new InjectionPoint(new int[]{0}, HARD_CODE_PASSWORD_TYPE);
                    }
                }
            } else if(PASSWORD_WORDS.contains(method.toLowerCase())) { // Targeting : DSL().user("").password(String x)
                return new InjectionPoint(new int[]{0}, HARD_CODE_PASSWORD_TYPE);
            }
        }
        return InjectionPoint.NONE;
    }
}
