package testcode.xss.encoder;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApacheCommonsLang3  extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");

        resp.getWriter().write(input1); //To avoid placebo test

        resp.getWriter().write(StringEscapeUtils.escapeHtml3(input1));
        resp.getWriter().write(StringEscapeUtils.escapeHtml4(input1));
        resp.getWriter().write(StringEscapeUtils.escapeXml(input1));
        resp.getWriter().write(StringEscapeUtils.escapeJson(input1));
        resp.getWriter().write(StringEscapeUtils.escapeXml10(input1));
        resp.getWriter().write(StringEscapeUtils.escapeXml11(input1));
    }
}
