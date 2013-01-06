package javax.servlet.http;

import javax.servlet.ServletResponse;

public interface HttpServletResponse extends ServletResponse {

    void addCookie(Cookie cookie);

    void addHeader(String header, String value);
}
