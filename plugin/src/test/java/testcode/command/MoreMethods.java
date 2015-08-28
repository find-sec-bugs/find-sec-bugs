package testcode.command;

import java.io.IOException;

public class MoreMethods {
    
    public static String tainted() {
        return System.getenv("var");
    }
    
    public String safe() {
        return "safe";
    }
    
    public static void sink(String param) throws IOException {
        Runtime.getRuntime().exec(param);
    }
}
