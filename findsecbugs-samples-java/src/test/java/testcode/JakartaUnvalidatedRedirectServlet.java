package testcode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JakartaUnvalidatedRedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("urlRedirect");
        unvalidatedRedirect1(resp, url);
    }

    private void unvalidatedRedirect1(HttpServletResponse resp, String url) throws IOException {
        if (url != null) {
            resp.sendRedirect(url);
        }
    }

}
