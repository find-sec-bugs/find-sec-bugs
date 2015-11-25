package testcode.xss.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class XssServlet5 extends HttpServlet {


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");

        testWrite(resp.getWriter(), input1, req);
        testFormatUnsafe(resp.getWriter(), input1, req);
        testFormatSafe(resp.getWriter(), input1, req);
        testPrintUnsafe(resp.getWriter(), input1, req);
        testPrintSafe(resp.getWriter(), input1, req);
        testAppend(resp.getWriter(), input1, req);
    }

    public void testWrite(PrintWriter pw, String input1,HttpServletRequest req) {
        pw.write(input1);
        pw.write(input1,0,10);
        pw.write(input1.toCharArray());
        pw.write(input1.toCharArray(), 0, 10);
    }

    public void testFormatUnsafe(PrintWriter pw, String input1,HttpServletRequest req) {
        pw.format(req.getLocale(), "%s", input1);
        pw.format("%s", input1);
        pw.format("%s %s", "SAFE", input1);
        pw.format("%s %s %s", "SAFE", "SAFE", input1);
        pw.format("%s %s %s %s", "SAFE", "SAFE", input1, "SAFE");
        pw.format(input1, "<== the actual format string can be alter");
    }

    public void testFormatSafe(PrintWriter pw, String input1,HttpServletRequest req) {
        pw.format(req.getLocale(), "Data : %s", "Constant data");
        pw.format("%s", "SAFE");
        pw.format("%s %s", "SAFE","SAFE");
        pw.format("%s %s %s", "SAFE","SAFE","SAFE");
    }

    public void testPrintUnsafe(PrintWriter pw, String input1,HttpServletRequest req) {
        pw.print(input1.toCharArray());
        pw.print(input1);
        pw.print((Object) input1);
        for(char c : input1.toCharArray()) {
            pw.print(c);
        }

        pw.println(input1.toCharArray());
        pw.println(input1);
        pw.println((Object) input1);
        for(char c : input1.toCharArray()) {
            pw.println(c);
        }
    }

    public void testPrintSafe(PrintWriter pw, String input1,HttpServletRequest req) {
        pw.print("".equals(input1)); //Boolean is consider unexploitable (safe for the other primitive type)
        pw.print(Double.parseDouble(input1));
        pw.print(Integer.parseInt(input1));
        pw.print(Float.parseFloat(input1));
        pw.print(Long.parseLong(input1));

        //pw.print("SAFE".toCharArray()); //FIXME: char array not supported yet
        pw.print("SAFE AGAIN");
        pw.print((Object) "SAFE SAFE SAFE");

        pw.println("".equals(input1)); //Boolean is consider unexploitable (safe for the other primitive type)
        pw.println(Double.parseDouble(input1));
        pw.println(Integer.parseInt(input1));
        pw.println(Float.parseFloat(input1));
        pw.println(Long.parseLong(input1));

        //pw.println("SAFE".toCharArray()); //FIXME: char array not supported yet
        pw.println("SAFE AGAIN");
        pw.println((Object) "SAFE SAFE SAFE");
    }

    public void testPrintfUnsafe(PrintWriter pw, String input1,HttpServletRequest req) {
        pw.printf(req.getLocale(),"%s",input1);
        pw.printf(req.getLocale(),input1, "<== the actual format string can be alter");
        pw.printf(req.getLocale(),input1, input1);
        pw.printf("%s",input1);
        pw.printf(input1, "<== the actual format string can be alter");
        pw.printf(input1, input1);
    }

    public void testPrintfSafe(PrintWriter pw, String input1,HttpServletRequest req) {
        pw.printf(req.getLocale(),"%s","SAFE");
        pw.printf("%s","SAFE");
    }


    public void testAppend(PrintWriter pw, String input1,HttpServletRequest req) {
        pw.append(input1);
        pw.append(input1,0,10);
        for(char c : input1.toCharArray()) {
            pw.append(c);
        }
    }


}
