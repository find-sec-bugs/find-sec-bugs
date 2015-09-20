package testcode.command;

import java.io.IOException;

public class MoreMethods implements InterfaceWithSink {
    
    public static String tainted() {
        return System.getenv("var");
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
}
