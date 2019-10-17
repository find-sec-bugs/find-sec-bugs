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
package com.h3xstream.findsecbugs.android;

import com.h3xstream.findsecbugs.common.StackUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

public class WebViewJavascriptEnabledDetector extends OpcodeStackDetector {

    private static final String ANDROID_WEB_VIEW_JAVASCRIPT_TYPE = "ANDROID_WEB_VIEW_JAVASCRIPT";
    private BugReporter bugReporter;

    public WebViewJavascriptEnabledDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("android/webkit/WebSettings") &&
                getNameConstantOperand().equals("setJavaScriptEnabled")) {
            OpcodeStack.Item item = stack.getStackItem(0); //First item on the stack is the last
            if(StackUtils.isConstantInteger(item) && (Integer) item.getConstant() == 1) {
                bugReporter.reportBug(new BugInstance(this, ANDROID_WEB_VIEW_JAVASCRIPT_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }
        }
    }
}
