package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * Few methods from <i>org.apache.commons.io.FilenameUtils</i> have a common weakness
 * of not filtering properly null byte.
 * <ul>
 * <li>normalize</li>
 * <li>getExtension</li>
 * <li>isExtension</li>
 * <li>getName</li>
 * <li>getBaseName</li>
 * </ul>
 *
 * <p>
 * In practice, it has limited risk see example in WeakFilenameUtils.
 *
 * @see org.apache.commons.io.FilenameUtils
 */
public class WeakFilenameUtilsMethodDetector extends OpcodeStackDetector {

    private static final String WEAK_FILENAMEUTILS_TYPE = "WEAK_FILENAMEUTILS";

    private BugReporter bugReporter;

    public WeakFilenameUtilsMethodDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == INVOKESTATIC && getClassConstantOperand().equals("org/apache/commons/io/FilenameUtils") &&
                (getNameConstantOperand().equals("normalize") ||
                        getNameConstantOperand().equals("getExtension") ||
                        getNameConstantOperand().equals("isExtension") ||
                        getNameConstantOperand().equals("getName") ||
                        getNameConstantOperand().equals("getBaseName")
                )) {

            bugReporter.reportBug(new BugInstance(this, WEAK_FILENAMEUTILS_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this)
                    .addString(getNameConstantOperand()));
        }
    }
}
