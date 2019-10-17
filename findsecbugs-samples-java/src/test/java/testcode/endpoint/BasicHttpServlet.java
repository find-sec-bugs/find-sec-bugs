package testcode.endpoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class BasicHttpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        useParameters(req);

        resp.getWriter().print("<!--" + req.getContentType() + "-->");
        resp.getWriter().print("<h1>Welcome to " + req.getServerName());

        String sqlQuery = "UPDATE sessions(last_visit) VALUES(now()) WHERE where sid = '" + req.getRequestedSessionId() + "')";
        resp.getWriter().print("<!--" + req.getQueryString() + "-->");

        String referrer = req.getHeader("Referer"); //Should have a higher priority
        if (referrer != null && referrer.startsWith("http://company.ca")) {
            req.getHeader("Host");
            req.getHeader("User-Agent");
            req.getHeader("X-Requested-With");
//            req.getHeader("X-Forwarded-For");
//            req.getHeader("X-ATT-DeviceId");
//            req.getHeader("X-Wap-Profile");
        }
    }

    private void useParameters(HttpServletRequest req) {

        String username = (String) req.getParameter("username");

        String[] roles = (String[]) req.getParameterValues("roles");

        String price = (String) req.getParameterMap().get("hidden_price_value");

        Enumeration parameters = req.getParameterNames();
        boolean isAdmin = false;
        while (parameters.hasMoreElements()) {
            if (parameters.nextElement().equals("admin_mode")) {
                isAdmin = true;
                break;
            }
        }

        System.out.println(username + roles.length + price + isAdmin); //Avoid used variable
    }
}
