package jakarta.servlet.http;

import jakarta.servlet.ServletRequestWrapper;
import java.util.Enumeration;

public class HttpServletRequestWrapper extends ServletRequestWrapper implements HttpServletRequest {

    public HttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean create) {
        return null;
    }
}
