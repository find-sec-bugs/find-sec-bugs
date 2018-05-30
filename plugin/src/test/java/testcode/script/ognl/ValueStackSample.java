package testcode.script.ognl;

import com.opensymphony.xwork2.util.ValueStack;

public class ValueStackSample {

    public void testExpression(String expression, ValueStack vs) {
        vs.findString(expression);
        vs.findString(expression,false);
        vs.findValue(expression);
        vs.findValue(expression, false);
        vs.findValue(expression,null);
        vs.findValue(expression,null, false);
        vs.setValue(expression, null);
        vs.setValue(expression, null, false);
        vs.setParameter(expression, null);
    }
}
