package testcode.cookie;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JakartaUrlRewriting extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        encodeURLRewrite(resp, req.getRequestURI());
    }

    private String encodeURLRewrite(HttpServletResponse resp, String url) {
        return resp.encodeURL(url);
    }

}
