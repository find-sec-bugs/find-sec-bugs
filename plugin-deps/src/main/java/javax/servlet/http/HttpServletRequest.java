package javax.servlet.http;


import javax.servlet.ServletRequest;
import java.util.Enumeration;

public interface HttpServletRequest extends ServletRequest {

    String getHeader(String name);

    Enumeration<String> getHeaders(String name);

    String getQueryString();

    String getRequestedSessionId();

    String getRequestURI();

    Cookie[] getCookies();

    HttpSession getSession();

    HttpSession getSession(boolean create);
}
