package testcode.command;

import java.io.IOException;

public class SubClass extends MoreMethods {

    public void method() throws IOException {
        Runtime.getRuntime().exec(safe());
        Runtime.getRuntime().exec(tainted());
        Runtime.getRuntime().exec(tainted2());
        sink3(System.getenv(""));
    }
    
    @Override
    public String safeParentparametricChild(String param) {
        return param;
    }
}
