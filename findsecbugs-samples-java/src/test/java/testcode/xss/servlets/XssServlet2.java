package testcode.xss.servlets;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.esapi.ESAPI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class XssServlet2 extends HttpServlet {

    private static final String SAFE_VALUE = "This is SAFE";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");

        indirectWrite(resp.getWriter(), input1);
    }

    public void indirectWrite(PrintWriter pw, String input1) {
        pw.write(SAFE_VALUE);
        pw.write("This is also SAFE");
        pw.write(input1);

//        pw.write(ESAPI.encoder().encodeForHTML(input1));
//        pw.write(StringEscapeUtils.escapeHtml(input1));
    }
}
