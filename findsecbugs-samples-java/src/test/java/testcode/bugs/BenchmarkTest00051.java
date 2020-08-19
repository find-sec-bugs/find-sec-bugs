package testcode.bugs;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BenchmarkTest00051 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // some code
        response.setContentType("text/html;charset=UTF-8");


        SeparateClassRequest scr = new SeparateClassRequest( request );
        String param = scr.getTheValue("BenchmarkTest00051");


        String a1 = "";
        String a2 = "";
        String osName = System.getProperty("os.name");
        if (osName.indexOf("Windows") != -1) {
            a1 = "cmd.exe";
            a2 = "/c";
        } else {
            a1 = "sh";
            a2 = "-c";
        }
        String[] args = {a1, a2, "echo " + param};

        ProcessBuilder pb = new ProcessBuilder(args);

        try {
            Process p = pb.start();
        } catch (IOException e) {
            System.out.println("Problem executing cmdi - java.lang.ProcessBuilder(java.lang.String[]) Test Case");
            throw new ServletException(e);
        }
    }
}
