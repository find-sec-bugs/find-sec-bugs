package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public interface Tag extends JspTag {
    int SKIP_BODY = 0;
    int EVAL_BODY_INCLUDE = 1;
    int SKIP_PAGE = 5;
    int EVAL_PAGE = 6;

    void setPageContext(PageContext var1);

    void setParent(Tag var1);

    Tag getParent();

    int doStartTag() throws JspException;

    int doEndTag() throws JspException;

    void release();
}
