package testcode.xss.servlets;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.esapi.ESAPI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class XssServlet3 extends HttpServlet {

    private static final String SAFE_VALUE = "This is SAFE";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");

        writeWithEncoders(resp.getWriter(), input1);
    }

    public void writeWithEncoders(PrintWriter pw, String input1) {
        pw.write(input1);
        String encoded = ESAPI.encoder().encodeForHTML(input1);
        pw.write(encoded.toLowerCase() + SAFE_VALUE);
        pw.write(StringEscapeUtils.escapeHtml(input1));
        pw.write(ESAPI.encoder().decodeForHTML(encoded) + SAFE_VALUE);
    }
}
