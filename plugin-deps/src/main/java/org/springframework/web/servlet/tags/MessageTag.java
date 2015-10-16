package org.springframework.web.servlet.tags;


import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class MessageTag extends HtmlEscapingAwareTag {

    public void setJavaScriptEscape(boolean javaScriptEscape) throws JspException {

    }

    @Override
    protected final int doStartTagInternal() throws JspException, IOException {
        return -1;
    }

    @Override
    public int doEndTag() throws JspException {
        return -1;
    }

    @Override
    public void release() {
    }
}
