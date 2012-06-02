package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * The filename given in FileUpload API is directly taken from the HTTP request.
 * <p>
 * The use without proper filtering can lead to Path traversal
 */
public class FileUploadFilenameDetector extends OpcodeStackDetector {
    private static String FILE_UPLOAD_FILENAME_TYPE = "FILE_UPLOAD_FILENAME";

    private BugReporter bugReporter;

    public FileUploadFilenameDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == INVOKEINTERFACE &&
                (getClassConstantOperand().equals("org/apache/wicket/util/upload/FileItem") ||
                getClassConstantOperand().equals("org/apache/commons/fileupload/FileItem")) &&
                        getNameConstantOperand().equals("getName")) {
            bugReporter.reportBug(new BugInstance(this, FILE_UPLOAD_FILENAME_TYPE, NORMAL_PRIORITY) //
                                    .addClass(this).addMethod(this).addSourceLine(this));
        }

    }
}
