package javax.servlet;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public interface ServletRequest {

    String getContentType();

    String getServerName();

    String getParameter(String name);

    Enumeration getParameterNames();

    String[] getParameterValues(String name);

    Map getParameterMap();

    Locale getLocale();

    RequestDispatcher getRequestDispatcher(String path);

    ServletContext getServletContext();
}
