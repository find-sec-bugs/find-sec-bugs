package org.springframework.web.servlet.tags;

import org.springframework.expression.EvaluationContext;

import javax.servlet.jsp.JspException;
import java.io.IOException;

public class EvalTag extends HtmlEscapingAwareTag {


    public void setExpression(String expression) {

    }

    public void setVar(String var) {

    }

    public void setScope(String scope) {

    }

    public void setJavaScriptEscape(boolean javaScriptEscape) throws JspException {

    }

    @Override
    public int doStartTagInternal() throws JspException {
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
