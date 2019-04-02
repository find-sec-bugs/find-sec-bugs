package testcode.script.ognl;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.dispatcher.HttpParameters;

public class StrutsTaintedApi {

    public void handleRequest1(HttpParameters params, ValueStack vs) {
        String expr = params.get("test").getValue();
        vs.findValue(expr);
    }
}
