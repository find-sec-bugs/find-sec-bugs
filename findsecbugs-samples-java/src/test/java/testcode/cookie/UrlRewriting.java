package testcode.cookie;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UrlRewriting extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        encodeURLRewrite(resp, req.getRequestURI());
    }

    private String encodeURLRewrite(HttpServletResponse resp, String url) {
        return resp.encodeURL(url);
    }

    public String encodeUrlRewrite(HttpServletResponse resp, String url) {
        return resp.encodeUrl(url); //Deprecated
    }

    public String encodeRedirectURLRewrite(HttpServletResponse resp, String url) {
        return resp.encodeRedirectURL(url);
    }
    
    public String encodeRedirectUrlRewrite(HttpServletResponse resp, String url) {
        return resp.encodeRedirectUrl(url); //Deprecated
    }
}
