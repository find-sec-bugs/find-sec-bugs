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
package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findsecbugs.common.StackUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

public class JspIncludeDetector extends OpcodeStackDetector {
    private static final String JSP_INCLUDE_TYPE = "JSP_INCLUDE";

    private BugReporter bugReporter;

    public JspIncludeDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);

        //Important sample from \plugin\src\test\webapp\includes\jsp_include_1.jsp
        //org.apache.jasper.runtime.JspRuntimeLibrary
        //JspRuntimeLibrary.include(request, response, (String)PageContextImpl.evaluateExpression("${param.secret_param}", String.class, _jspx_page_context, null), out, false);
        //  JspIncludeDetector: [0119]  invokestatic   org/apache/jasper/runtime/JspRuntimeLibrary.include (Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;Ljava/lang/String;Ljavax/servlet/jsp/JspWriter;Z)V

        //Important sample from \plugin\src\test\webapp\includes\jsp_include_3.jsp
        //ImportTag _jspx_th_c_import_0 = (ImportTag)this._jspx_tagPool_c_import_url_nobody.get(ImportTag.class);
        //_jspx_th_c_import_0.setUrl((String)PageContextImpl.evaluateExpression("${param.secret_param}", String.class, _jspx_page_context, null));
        //  JspIncludeDetector: [0051]  invokevirtual   org/apache/taglibs/standard/tag/rt/core/ImportTag.setUrl (Ljava/lang/String;)V


        if (seen == Const.INVOKESTATIC && ("org/apache/jasper/runtime/JspRuntimeLibrary".equals(getClassConstantOperand()) || "org/apache/sling/scripting/jsp/jasper/runtime/JspRuntimeLibrary".equals(getClassConstantOperand()))
                && getNameConstantOperand().equals("include") && getSigConstantOperand().equals("(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;Ljava/lang/String;Ljavax/servlet/jsp/JspWriter;Z)V")) {

            if (StackUtils.isVariableString(stack.getStackItem(2))) {
                bugReporter.reportBug(new BugInstance(this, JSP_INCLUDE_TYPE, Priorities.HIGH_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }
        }
        else if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("org/apache/taglibs/standard/tag/rt/core/ImportTag")
                && getNameConstantOperand().equals("setUrl") && getSigConstantOperand().equals("(Ljava/lang/String;)V")) {

            bugReporter.reportBug(new BugInstance(this, JSP_INCLUDE_TYPE, Priorities.HIGH_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }

    }
}
