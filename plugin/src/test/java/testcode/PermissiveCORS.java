package testcode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PermissiveCORS extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        falsePositiveCORS(resp);

        resp.getWriter().print(req.getSession().getAttribute("secret"));
    }

    //False positive test
    private void falsePositiveCORS(HttpServletResponse resp) {
        resp.addHeader("Access-Control-Allow-Origin", "http://example.com");
    }
    
    //Overly permissive Cross-domain requests accepted
    public void addPermissiveCORS(HttpServletResponse resp) {
        resp.addHeader("Access-Control-Allow-Origin", "*");
    }

    public void setPermissiveCORS(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
    }
}
