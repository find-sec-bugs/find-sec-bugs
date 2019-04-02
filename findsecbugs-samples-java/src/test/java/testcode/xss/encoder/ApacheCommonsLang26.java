package testcode.xss.encoder;

import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApacheCommonsLang26 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");

        resp.getWriter().write(input1); //To avoid placebo test

        resp.getWriter().write(StringEscapeUtils.escapeHtml(input1));
        resp.getWriter().write(StringEscapeUtils.escapeJavaScript(input1));
        resp.getWriter().write(StringEscapeUtils.escapeXml(input1));
        StringEscapeUtils.escapeHtml(resp.getWriter(), input1);
        StringEscapeUtils.escapeJavaScript(resp.getWriter(), input1);
        StringEscapeUtils.escapeXml(resp.getWriter(), input1);
    }
}
