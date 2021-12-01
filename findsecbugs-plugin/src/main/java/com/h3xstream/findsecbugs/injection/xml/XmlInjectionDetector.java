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
package com.h3xstream.findsecbugs.injection.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.h3xstream.findsecbugs.injection.AbstractTaintDetector;
import com.h3xstream.findsecbugs.injection.ClassMethodSignature;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.data.UnknownSource;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.commons.lang.ObjectUtils.Null;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.StringAnnotation;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.SignatureParser;

public class XmlInjectionDetector extends AbstractTaintDetector {

    private static final Matcher ENDS_ON_XML_OPEN_TAG = Pattern.compile("(?s).*<(\\w+)\\s*>\\s*").matcher("");
    private static final Matcher BEGINS_WITH_XML_CLOSE_TAG = Pattern.compile("(?s)\\s*</(\\w+)\\s*>.*").matcher("");

    public XmlInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    private String lastSignature = null;
    private String firstMember = null;
    private Taint secondMember = null;
    private String tagName = null;

    @Override
    protected void analyzeLocation(ClassContext classContext, Method method, InstructionHandle handle,
            ConstantPoolGen cpg, InvokeInstruction invoke, TaintFrame fact, ClassMethodSignature classMethodSignature)
            throws DataflowAnalysisException {
        if (invoke instanceof INVOKESPECIAL && "java.lang.StringBuilder".equals(invoke.getClassName(cpg))
                && Const.CONSTRUCTOR_NAME.equals(invoke.getMethodName(cpg))
                && "(Ljava/lang/String;)V".equals(invoke.getSignature(cpg))) {
            firstMember = null;
            secondMember = null;
        } else if (invoke instanceof INVOKEVIRTUAL && "java.lang.StringBuilder".equals(invoke.getClassName(cpg))
                && "append".equals(invoke.getMethodName(cpg))
                && "(Ljava/lang/String;)Ljava/lang/StringBuilder;".equals(invoke.getSignature(cpg))) {
            Taint thisValue = fact.getStackValue(fact.getStackDepth() - 1);
            if (lastSignature != null && !hasSource(thisValue, lastSignature)) {
                firstMember = null;
                secondMember = null;
            }
        } else {
            return;
        }

        lastSignature = invoke.getClassName(cpg).replace('.', '/') + "." + invoke.getMethodName(cpg)
                + invoke.getSignature(cpg);

        Taint argument = fact.getArgument(invoke, cpg, 0, new SignatureParser(invoke.getSignature(cpg)));

        String argConstVal = argument.getConstantOrPotentialValue();
        if (firstMember == null) {
            assert secondMember == null;
            if (argConstVal != null) {
                Matcher xmlTagMatcher = ENDS_ON_XML_OPEN_TAG.reset(argConstVal);
                if (xmlTagMatcher.matches()) {
                    firstMember = argConstVal;
                    tagName = xmlTagMatcher.group(1);
                }
            }
        } else if (secondMember == null) {
            if (argConstVal == null && !argument.isSafe()) {
                secondMember = argument;
            } else {
                firstMember = null;
            }
        } else {
            if (argConstVal != null) {
                Matcher xmlTagMatcher = BEGINS_WITH_XML_CLOSE_TAG.reset(argConstVal);
                if (xmlTagMatcher.matches() && tagName.equals(xmlTagMatcher.group(1))) {
                    BugInstance bug = new BugInstance(this, "POTENTIAL_XML_INJECTION",
                            secondMember.isTainted() ? NORMAL_PRIORITY : LOW_PRIORITY)
                            .addClassAndMethod(classContext.getJavaClass(), method)
                            .addSourceLine(classContext, method, handle);
                    addSourceString(bug, secondMember, method);
                    bugReporter.reportBug(bug);
                } else {
                    firstMember = null;
                    secondMember = null;
                }
            } else {
                firstMember = null;
                secondMember = null;
            }
        }
    }

    private boolean hasSource(Taint value, String signature) {
        for(UnknownSource source : value.getSources()) {
            String sig = source.getSignatureMethod();
            if (sig == null) {
                continue;
            }
            if (signature.equals(sig)) {
                return true;
            }
        }
        return false;
    }

    private void addSourceString(BugInstance bug, Taint value, Method method) {
        for(UnknownSource source : value.getSources()) {
            String role;
            switch (source.getState()) {
                case SAFE:
                case NULL:
                    continue;
                case TAINTED:
                    role = "tainted source";
                    break;
                default:
                    role = "unknown source";
            }

            String text;
            switch (source.getSourceType()) {
                case FIELD:
                    text = "Field " + source.getSignatureField();
                    break;
                case RETURN:
                    text = "Return value of " + source.getSignatureMethod();
                    break;
                case PARAMETER:
                    text = "Parameter '" + getParameterName(method, source.getParameterIndex()) + "'";
                    break;
                default:
                    continue;
            }

            bug.add(new StringAnnotation(text + " of " + role));
        }
    }

    private String getParameterName(Method method, int index) {
        return method.getLocalVariableTable().getLocalVariableTable()[index].getName();
    }
}
