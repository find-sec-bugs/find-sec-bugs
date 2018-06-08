package javax.servlet.jsp.jstl.core;

import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

public abstract class LoopTagSupport
    extends TagSupport
    implements LoopTag, IterationTag, TryCatchFinally
{
    protected int begin;

    protected int end;

    protected int step;

    protected boolean beginSpecified;

    protected boolean endSpecified;

    protected boolean stepSpecified;

    protected String itemId, statusId;

    protected ValueExpression deferredExpression;


    public LoopTagSupport() {

    }


    protected abstract Object next() throws JspTagException;

    protected abstract boolean hasNext() throws JspTagException;

    protected abstract void prepare() throws JspTagException;


    public void release() {
    }

    public int doStartTag() throws JspException {
        exposeVariables(true);
        return 0;
    }

    public int doAfterBody() throws JspException {
        return 0;
    }

    public void doFinally() {
    }

    public void doCatch(Throwable t) throws Throwable {
    }

    public Object getCurrent() {
        return null;
    }

    public LoopTagStatus getLoopStatus() {
        return null ;
    }

    protected String getDelims() {
        return ",";
    }

    public void setVar(String id) {
        this.itemId = id;
    }

    public void setVarStatus(String statusId) {
        this.statusId = statusId;
    }


    // STUB note: implementation left for further investigation
    private void exposeVariables(boolean firstTime) throws JspTagException {
      
        pageContext.setAttribute(statusId, getLoopStatus());
    }


}
