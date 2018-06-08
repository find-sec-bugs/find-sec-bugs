package javax.servlet.jsp;

import java.util.Enumeration;
import javax.el.ELContext;

public abstract class JspContext {
    abstract public void setAttribute(String name, Object value);

    abstract public void setAttribute(String name, Object value, int scope);

    abstract public Object getAttribute(String name);

    abstract public Object getAttribute(String name, int scope);

    abstract public Object findAttribute(String name);

    abstract public void removeAttribute(String name);

    abstract public void removeAttribute(String name, int scope);

    abstract public int getAttributesScope(String name);

    abstract public Enumeration<String> getAttributeNamesInScope(int scope);

    abstract public JspWriter getOut();

//    public abstract ExpressionEvaluator getExpressionEvaluator();
//
//    public abstract VariableResolver getVariableResolver();

    public abstract ELContext getELContext();

}
