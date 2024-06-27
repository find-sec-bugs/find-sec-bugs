package testcode.cookie;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JakartaCookieUsage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        for (Cookie cookie : req.getCookies()) {
            cookie.getName();
            cookie.getValue();
            cookie.getPath();
        }

        resp.addCookie(new Cookie("test", "value"));
    }
}
