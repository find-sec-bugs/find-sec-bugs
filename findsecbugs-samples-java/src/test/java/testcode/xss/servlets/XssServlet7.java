package testcode.xss.servlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.owasp.esapi.ESAPI;

import org.apache.commons.lang.StringEscapeUtils;

public class XssServlet7 extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());

        try {
            List<FileItem> fileItems = upload.parseRequest(req);
            Iterator<FileItem> i = fileItems.iterator();
            String filename = i.next().getName();

            resp.getWriter().write(filename);

            resp.getWriter().write(ESAPI.encoder().encodeForHTML(filename));
            resp.getWriter().write(StringEscapeUtils.escapeHtml(filename));
        } catch(Exception ex) {
            System.out.println(ex);
        }
    }
}
