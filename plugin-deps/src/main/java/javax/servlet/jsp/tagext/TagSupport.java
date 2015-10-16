package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.Serializable;
import java.util.Enumeration;

public class TagSupport implements IterationTag, Serializable {

    public TagSupport() {
    }

    public int doStartTag() throws JspException {
        return 0;
    }

    public int doEndTag() throws JspException {
        return 6;
    }

    public int doAfterBody() throws JspException {
        return 0;
    }

    public void release() {

    }

    public void setParent(Tag t) {

    }

    public Tag getParent() {
        return null;
    }

    public void setId(String id) {

    }

    public String getId() {
        return null;
    }

    public void setPageContext(PageContext pageContext) {
    }

    public void setValue(String k, Object o) {

    }

    public Object getValue(String k) {
        return null;
    }

    public void removeValue(String k) {
    }

    public Enumeration<String> getValues() {
        return null;
    }
}
