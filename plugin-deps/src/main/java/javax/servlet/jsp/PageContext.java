package javax.servlet.jsp;

import java.util.Enumeration;
import javax.el.ELContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class PageContext extends JspContext {
    
    public static final int PAGE_SCOPE     = 1;
    @Override
    public void setAttribute(String name, Object value) {
        
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {
        
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Object getAttribute(String name, int scope) {
        return null;
    }

    @Override
    public Object findAttribute(String name) {
        return null;
    }

    @Override
    public void removeAttribute(String name) {
        
    }

    @Override
    public void removeAttribute(String name, int scope) {
        
    }

    @Override
    public int getAttributesScope(String name) {
        return 0;
    }

    @Override
    public Enumeration<String> getAttributeNamesInScope(int scope) {
        return null;
    }

    @Override
    public JspWriter getOut() {
        return null;
    }

    @Override
    public ELContext getELContext() {
        return null;
    }

    private ServletConfig config;

    private ServletContext context;

    public ServletConfig getServletConfig() {
        return config;
    }

    public ServletContext getServletContext() {
        return config.getServletContext();
    }    
}
