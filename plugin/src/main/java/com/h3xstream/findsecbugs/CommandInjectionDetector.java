package com.h3xstream.findsecbugs;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

/**
 * Detect the usage of Runtime and ProcessBuilder to execute system command.
 *
 * @see java.lang.ProcessBuilder
 * @see java.lang.Runtime
 */
public class CommandInjectionDetector extends OpcodeStackDetector {

    private static final String COMMAND_INJECTION_TYPE = "COMMAND_INJECTION";

    private BugReporter bugReporter;

    public CommandInjectionDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Constants.INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/Runtime") &&
                getNameConstantOperand().equals("exec")) {

            if (StringTracer.isVariableString(stack.getStackItem(0))) {
                bugReporter.reportBug(new BugInstance(this, COMMAND_INJECTION_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this)
                    .addString("Runtime.exec(...)"));
            }
        } else if (seen == Constants.INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/ProcessBuilder") &&
                getNameConstantOperand().equals("command")) {
            if (StringTracer.hasVariableString(stack)) {
                bugReporter.reportBug(new BugInstance(this, COMMAND_INJECTION_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this)
                    .addString("ProcessBuilder.command(...)"));
            }
        }

    }
}
