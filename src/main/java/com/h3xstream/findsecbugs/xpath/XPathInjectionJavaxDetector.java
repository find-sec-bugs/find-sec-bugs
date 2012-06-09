package com.h3xstream.findsecbugs.xpath;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class XPathInjectionJavaxDetector extends OpcodeStackDetector {

    public static final String XPATH_INJECTION_TYPE = "XPATH_INJECTION";

    private BugReporter bugReporter;

    public XPathInjectionJavaxDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if (seen == INVOKEINTERFACE && getClassConstantOperand().equals("javax/xml/xpath/XPath")) {

            if (getNameConstantOperand().equals("compile")
                    && getSigConstantOperand().equals("(Ljava/lang/String;)Ljavax/xml/xpath/XPathExpression;")) {

                if (StringTracer.isVariableString(stack.getStackItem(0))) {
                    bugReporter.reportBug(new BugInstance(this, XPATH_INJECTION_TYPE, NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("XPath.compile()"));
                }
            } else if (getNameConstantOperand().equals("evaluate")
                    && getSigConstantOperand().equals("(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;")) {

                if (StringTracer.isVariableString(stack.getStackItem(0))) {
                    bugReporter.reportBug(new BugInstance(this, XPATH_INJECTION_TYPE, NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("XPath.evaluate()"));
                }
            }
        }
    }

}
