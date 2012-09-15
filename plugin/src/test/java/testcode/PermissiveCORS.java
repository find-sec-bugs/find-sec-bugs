package testcode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PermissiveCORS extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Cross-domain request accepted
        resp.addHeader("Access-Control-Allow-Origin", "*");

        resp.getWriter().print(req.getSession().getAttribute("secret"));
    }
}
