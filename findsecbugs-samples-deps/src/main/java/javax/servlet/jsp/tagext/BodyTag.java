package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspException;

public interface BodyTag extends IterationTag {
    /** @deprecated */
    int EVAL_BODY_TAG = 2;
    int EVAL_BODY_BUFFERED = 2;

    void setBodyContent(BodyContent var1);

    void doInitBody() throws JspException;
}
