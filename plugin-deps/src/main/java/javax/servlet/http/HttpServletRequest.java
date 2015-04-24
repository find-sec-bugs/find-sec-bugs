package javax.servlet.http;


import javax.servlet.ServletRequest;

public interface HttpServletRequest extends ServletRequest {

    String getHeader(String name);

    String getQueryString();

    String getRequestedSessionId();

    Cookie[] getCookies();

    HttpSession getSession();

    HttpSession getSession(boolean create);
}
