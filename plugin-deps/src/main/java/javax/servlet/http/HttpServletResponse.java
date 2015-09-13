package javax.servlet.http;

import javax.servlet.ServletResponse;
import java.io.IOException;

public interface HttpServletResponse extends ServletResponse {

    void addCookie(Cookie cookie);

    void addHeader(String header, String value);

    void sendRedirect(String url) throws IOException;
}
