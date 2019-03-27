package testcode.script.ognl;

import com.opensymphony.xwork2.ognl.OgnlReflectionProvider;

import javax.management.ReflectionException;
import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.Map;

public class OgnlReflectionProviderSample {

    public void unsafeOgnlReflectionProvider(String input, Map<String,String> propsInput, OgnlReflectionProvider reflectionProvider, Class type) throws IntrospectionException, ReflectionException {
        reflectionProvider.getGetMethod(type, input);
        reflectionProvider.getSetMethod(type, input);
        reflectionProvider.getField(type, input);
        reflectionProvider.setProperties(propsInput, null, null, true);
        reflectionProvider.setProperties(propsInput, null, null);
        reflectionProvider.setProperties(propsInput, null);
        reflectionProvider.setProperty( input, "test",null, null);
        reflectionProvider.setProperty( input, "test",null, null, true);
        reflectionProvider.getValue(input, null, null);
        reflectionProvider.setValue(input, null, null,null);
    }

    public void safeOgnlReflectionProvider(OgnlReflectionProvider reflectionProvider, Class type) throws IntrospectionException, ReflectionException {
        String input = "thisissafe";
        String constant1 = "";
        String constant2 = "";
        reflectionProvider.getGetMethod(type, input);
        reflectionProvider.getSetMethod(type, input);
        reflectionProvider.getField(type, input);
        reflectionProvider.setProperties(new HashMap<String,String>(), null, null, true);
        reflectionProvider.setProperties(new HashMap<String,String>(), null, null);
        reflectionProvider.setProperties(new HashMap<String,String>(), null);
        reflectionProvider.setProperty("test", constant1, null, null);
        reflectionProvider.setProperty("test", constant2, null, null, true);
        reflectionProvider.getValue(input, null, null);
        reflectionProvider.setValue(input, null, null,null);
    }

}
