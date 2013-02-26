package com.h3xstream.findsecbugs.password;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;

public class JndiCredentialsDetector extends OpcodeStackDetector {

    private static final String HARD_CODE_PASSWORD_TYPE = "HARD_CODE_PASSWORD";

    private BugReporter bugReporter;

    public JndiCredentialsDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if(seen == Constants.INVOKEVIRTUAL && getClassConstantOperand().equals("java/util/Properties") &&
                getNameConstantOperand().equals("put") && getSigConstantOperand().equals("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")) {
            OpcodeStack.Item item1 = stack.getStackItem(1);
            OpcodeStack.Item item0 = stack.getStackItem(0); //The last argument (on the top of the stack)
            if ( "java.naming.security.credentials".equals((String) item1.getConstant()) && StringTracer.isConstantString(item0) ) {
                bugReporter.reportBug(new BugInstance(this, HARD_CODE_PASSWORD_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this)); //
            }
        }
    }
}
