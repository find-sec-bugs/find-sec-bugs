package javax.servlet;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public class ServletRequestWrapper implements ServletRequest {

    public ServletRequestWrapper(ServletRequest servletRequest) {

    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public String getParameter(String name) {
        return null;
    }

    @Override
    public Enumeration getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    @Override
    public Map getParameterMap() {
        return null;
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }
}
