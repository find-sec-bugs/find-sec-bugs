package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

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

        if (seen == INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/Runtime") &&
                getNameConstantOperand().equals("exec")) {

            bugReporter.reportBug(new BugInstance(this, COMMAND_INJECTION_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this)
                    .addString("Runtime.exec(...)"));
        } else if (seen == INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/ProcessBuilder") &&
                getNameConstantOperand().equals("command")) {

            bugReporter.reportBug(new BugInstance(this, COMMAND_INJECTION_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this)
                    .addString("ProcessBuilder.command(...)"));
        }

    }
}
