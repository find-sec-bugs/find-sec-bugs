package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspException;

public abstract class HtmlEscapingAwareTag extends RequestContextAwareTag {

    private Boolean htmlEscape;

    public void setHtmlEscape(boolean htmlEscape) throws JspException {
        this.htmlEscape = htmlEscape;
    }

    protected boolean isHtmlEscape() {
        return false;
    }

    protected boolean isDefaultHtmlEscape() {
        return false;
    }
}
