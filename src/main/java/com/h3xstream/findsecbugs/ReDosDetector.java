package com.h3xstream.findsecbugs;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ReDosDetector extends OpcodeStackDetector {

    private static final String REDOS_TYPE = "REDOS";

    private static final Set<Character> OPENING_CHAR = new HashSet<Character>(2);
    static {
        OPENING_CHAR.add('(');
        OPENING_CHAR.add('[');
    }

    private static final Set<Character> CLOSING_CHAR = new HashSet<Character>(2);
    static {
        CLOSING_CHAR.add(')');
        CLOSING_CHAR.add(']');
    }

    private static final Set<Character> PLUS_CHAR = new HashSet<Character>(2);
    static {
        PLUS_CHAR.add('+');
        PLUS_CHAR.add('*');
        PLUS_CHAR.add('?');
    }

    private BugReporter bugReporter;

    public ReDosDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if (seen == INVOKESTATIC && getClassConstantOperand().equals("java/util/regex/Pattern")
                && getNameConstantOperand().equals("compile")
                && getSigConstantOperand().equals("(Ljava/lang/String;)Ljava/util/regex/Pattern;")) {
            OpcodeStack.Item item = stack.getStackItem(0);
            if (!StringTracer.isVariableString(item)) {
                String value = (String) item.getConstant();
                analyseRegexString(value);
            }
        } else if (seen == INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/String")
                && getNameConstantOperand().equals("matches")
                && getSigConstantOperand().equals("(Ljava/lang/String;)Z")) {
            OpcodeStack.Item item = stack.getStackItem(0);
            if (!StringTracer.isVariableString(item)) {
                String value = (String) item.getConstant();
                analyseRegexString(value);
            }
        }

    }

    protected void analyseRegexString(String regexString) {
        //System.out.println(regexString);


    }
}
