package testcode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnvalidatedRedirectServlet extends HttpServlet {

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

    public void unvalidatedRedirect2(HttpServletResponse resp, String url) {
        if (url != null) {
            resp.addHeader("Location", url);
        }
    }

    ///The following cases are safe for sure

    public void falsePositiveRedirect1(HttpServletResponse resp) throws IOException {
        String url = "/Home";
        if (url != null) {
            resp.sendRedirect(url);
        }
    }

    public void falsePositiveRedirect2(HttpServletResponse resp) {
        resp.addHeader("Location", "/login.jsp");
    }
}
