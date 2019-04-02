package testcode.xss.servlets;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.esapi.ESAPI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class XssServlet4 extends HttpServlet {

    private static final String SAFE_VALUE = "This is SAFE";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");

        writeWithStringBuilder(resp.getWriter(), input1);
    }

    public void writeWithStringBuilder(PrintWriter pw, String input1) {
        pw.write(input1);

        StringBuilder str = new StringBuilder();
        str.append(SAFE_VALUE);
        str.append("Hello world !");
        pw.write(str.toString());
    }
}
