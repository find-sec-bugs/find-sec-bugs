package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspException;

public class HtmlEscapeTag extends RequestContextAwareTag {

    public void setDefaultHtmlEscape(boolean defaultHtmlEscape) {
    }

    @Override
    protected int doStartTagInternal() throws JspException {
        return -1;
    }
}
