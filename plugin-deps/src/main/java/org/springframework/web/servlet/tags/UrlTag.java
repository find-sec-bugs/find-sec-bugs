package org.springframework.web.servlet.tags;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

public class UrlTag extends HtmlEscapingAwareTag implements ParamAware {

    public void setValue(String value) {

    }

    public void setContext(String context) {

    }

    public void setVar(String var) {

    }

    public void setScope(String scope) {

    }

    public void setJavaScriptEscape(boolean javaScriptEscape) throws JspException {
    }

    @Override
    public void addParam(Param param) {

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
