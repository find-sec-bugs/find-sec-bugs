package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class ArgumentTag extends BodyTagSupport {

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    // tag attribute mutators

    /**
     * Sets the value of the argument
     *
     * <p>
     * Optional. If not set, the tag's body content is evaluated.
     *
     * @param value the parameter value
     */
    public void setValue(Object value) {
    }

    @Override
    public void release() {
    }
}
