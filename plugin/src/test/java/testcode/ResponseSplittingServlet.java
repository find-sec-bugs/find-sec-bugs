package testcode;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.esapi.ESAPI;

public abstract class ResponseSplittingServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = new Cookie("name", unknown());
        cookie.setValue(req.getParameter("p") + "x");
        resp.setHeader("header", req.getParameter("h1"));
        resp.addHeader("header", unknown());
        callCookieSink(req.getParameter("h2"));
        String encoded = ESAPI.encoder().encodeForURL(req.getParameter("h3"));
        resp.addHeader("header", ESAPI.encoder().encodeFromURL(encoded));
        
        // false positives
        String safe = "x".concat("y");
        Cookie safeCookie = new Cookie("name", safe);
        safeCookie.setValue(safe + "x");
        resp.setHeader("header", safe);
        resp.addHeader("header", encoded.concat(safe));
    }
    
    private void cookieSink(String param) {
        System.out.println(new Cookie("name", param));
    }
    
    private void callCookieSink(String param) {
        cookieSink(param);
    }
    
    protected abstract String unknown();
}
