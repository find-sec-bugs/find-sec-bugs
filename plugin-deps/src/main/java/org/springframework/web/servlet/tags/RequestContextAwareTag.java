package org.springframework.web.servlet.tags;


import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

public abstract class RequestContextAwareTag extends TagSupport implements TryCatchFinally {


    @Override
    public final int doStartTag() throws JspException {
        return -1;
    }

    /**
     * Return the current RequestContext.
     */
    protected final RequestContext getRequestContext() {
        return null;
    }

    /**
     * Called by doStartTag to perform the actual work.
     * @return same as TagSupport.doStartTag
     * @throws Exception any exception, any checked one other than
     * a JspException gets wrapped in a JspException by doStartTag
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag
     */
    protected abstract int doStartTagInternal() throws Exception;


    @Override
    public void doCatch(Throwable throwable) throws Throwable {
        throw throwable;
    }

    @Override
    public void doFinally() {

    }
}
