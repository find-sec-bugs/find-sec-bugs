package org.springframework.web.servlet.tags;

import org.springframework.validation.Errors;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class BindErrorsTag extends HtmlEscapingAwareTag {

    /**
     * Set the name of the bean that this tag should check.
     */
    public void setName(String name) {
    }

    /**
     * Return the name of the bean that this tag checks.
     */
    public String getName() {
        return null;
    }


    @Override
    protected final int doStartTagInternal() throws ServletException, JspException {
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    /**
     * Retrieve the Errors instance that this tag is currently bound to.
     * <p>Intended for cooperating nesting tags.
     */
    public final Errors getErrors() {
        return null;
    }


    @Override
    public void doFinally() {
    }
}
