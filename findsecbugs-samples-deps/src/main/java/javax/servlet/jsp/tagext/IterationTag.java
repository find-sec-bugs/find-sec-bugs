package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspException;

public interface IterationTag extends Tag {
    int EVAL_BODY_AGAIN = 2;

    int doAfterBody() throws JspException;
}
