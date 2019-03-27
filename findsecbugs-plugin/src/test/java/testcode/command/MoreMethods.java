package testcode.command;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
public class MoreMethods implements InterfaceWithSink {
    public static HttpServletRequest req;
    public static String tainted() {
        return req.getParameter("input");
    }
    
    public String safe() {
        return "safe";
    }
    
    public static void sink(String param) throws IOException {
        Runtime.getRuntime().exec(param);
    }
    
    @Override
    public void sink2(String param) throws IOException {
        Runtime.getRuntime().exec(param);
    }
    
    public void sink3(String param) throws IOException {
        Runtime.getRuntime().exec(param);
    }
    
    public static String tainted2() {
        return req.getParameter("var2");
    }
    
    public String safeParentparametricChild(String param) {
        return "safe parent";
    }
}
