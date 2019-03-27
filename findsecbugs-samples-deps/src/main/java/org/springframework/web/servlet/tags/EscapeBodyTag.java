package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

public class EscapeBodyTag extends HtmlEscapingAwareTag implements BodyTag {

    @Override
    public void setBodyContent(BodyContent var1) {

    }

    @Override
    public void doInitBody() throws JspException {

    }

    @Override
    protected int doStartTagInternal() throws Exception {
        return EVAL_BODY_BUFFERED;
    }
}
