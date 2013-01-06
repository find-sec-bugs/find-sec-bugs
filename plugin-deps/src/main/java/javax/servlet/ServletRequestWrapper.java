package javax.servlet;

import java.util.Enumeration;
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
}
