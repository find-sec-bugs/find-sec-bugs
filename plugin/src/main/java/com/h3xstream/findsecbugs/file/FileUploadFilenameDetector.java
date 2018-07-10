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
package com.h3xstream.findsecbugs.file;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

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

        if (seen == Const.INVOKEINTERFACE &&
                (getClassConstantOperand().equals("org/apache/wicket/util/upload/FileItem") ||
                        getClassConstantOperand().equals("org/apache/commons/fileupload/FileItem")) &&
                getNameConstantOperand().equals("getName")) {
            bugReporter.reportBug(new BugInstance(this, FILE_UPLOAD_FILENAME_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }

    }
}
