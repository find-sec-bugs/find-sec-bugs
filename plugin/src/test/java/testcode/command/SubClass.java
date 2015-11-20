package testcode.command;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
public class SubClass extends MoreMethods {
    HttpServletRequest req;
    public void method() throws IOException {
        Runtime.getRuntime().exec(safe());
        Runtime.getRuntime().exec(tainted());
        Runtime.getRuntime().exec(tainted2());
        sink3(req.getParameter("dangerZone"));
    }
    
    @Override
    public String safeParentparametricChild(String param) {
        return param;
    }
}
