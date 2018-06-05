package javax.servlet;

import java.util.Enumeration;

public interface ServletConfig {

    public String getServletName();

    public ServletContext getServletContext();

    public String getInitParameter(String name);

    public Enumeration<String> getInitParameterNames();
}
