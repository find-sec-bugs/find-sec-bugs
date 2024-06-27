package testcode.cors;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JakartaPermissiveCORS extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addPermissiveCORS(resp);
    }

    // Overly permissive Cross-domain requests accepted
    public void addPermissiveCORS(HttpServletResponse resp) {
        resp.addHeader("Access-Control-Allow-Origin", "*");
    }

}
