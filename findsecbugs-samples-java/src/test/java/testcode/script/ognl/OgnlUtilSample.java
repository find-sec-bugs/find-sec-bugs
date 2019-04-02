package testcode.script.ognl;

import com.opensymphony.xwork2.ognl.OgnlUtil;
import ognl.OgnlException;

import javax.management.ReflectionException;
import java.util.HashMap;
import java.util.Map;

public class OgnlUtilSample {

    public void unsafeOgnlUtil(OgnlUtil ognlUtil, String input, Map<String, ?> propsInput) throws OgnlException, ReflectionException {
        ognlUtil.setValue(input, null, null, "12345");
        ognlUtil.getValue(input, null, null, null);
        ognlUtil.setProperty(input, "12345", null, null);
        ognlUtil.setProperty(input, "12345", null, null, true);
        ognlUtil.setProperties(propsInput, null, null);
        ognlUtil.setProperties(propsInput, null, null, true);
        ognlUtil.setProperties(propsInput, null, true);
        ognlUtil.setProperties(propsInput, null);
        ognlUtil.callMethod(input, null, null);
        ognlUtil.compile(input);
        ognlUtil.compile(input, null);
    }

    public void safeOgnlUtil(OgnlUtil ognlUtil) throws OgnlException, ReflectionException {
        String input = "thisissafe";

        ognlUtil.setValue(input, null, null, "12345");
        ognlUtil.getValue(input, null, null, null);
        ognlUtil.setProperty(input, "12345", null, null);
        ognlUtil.setProperty(input, "12345", null, null, true);
        ognlUtil.setProperties(new HashMap<String,String>(), null, null);
        ognlUtil.setProperties(new HashMap<String,String>(), null, null, true);
        ognlUtil.setProperties(new HashMap<String,String>(), null, true);
        ognlUtil.setProperties(new HashMap<String,String>(), null);
        ognlUtil.callMethod(input, null, null);
        ognlUtil.compile(input);
        ognlUtil.compile(input, null);
    }
}
