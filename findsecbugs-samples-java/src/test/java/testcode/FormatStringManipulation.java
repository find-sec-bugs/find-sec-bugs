package testcode;

import java.util.Formatter;
import java.util.Locale;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

public class FormatStringManipulation extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{

    // create a new formatter
    StringBuffer buffer = new StringBuffer();
    Formatter formatter = new Formatter(buffer, Locale.US);
    String format = "The customer: %s %s" + request.getParameter("suffix");

    //test cases
    formatter.format(format, "John", "Smith", "Jr"); //BAD
    formatter.format(Locale.US, format, "John", "Smith"); //BAD
    //false positive test
    formatter.format("The customer: %s %s", "John", request.getParameter("testParam")); //OK
    
    System.out.printf(format, "John", "Smith"); //BAD
    System.out.printf(Locale.US, format, "John", "Smith"); //BAD

    System.out.format(format, "John", "Smith"); //BAD
    System.out.format(Locale.US, format, "John", "Smith"); //BAD

    String.format(format, "John", "Smith"); //BAD
    String.format(Locale.US, format, "John", "Smith"); //BAD

    }
}
