package org.apache.struts2.util;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.CharArrayWriter;
import java.io.IOException;

public class VelocityStrutsUtil extends StrutsUtil {

    public VelocityStrutsUtil(VelocityEngine engine, Context ctx, ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    public String evaluate(String expression) throws IOException /*, ResourceNotFoundException, MethodInvocationException, ParseErrorException */ {
        return null;
    }
}
