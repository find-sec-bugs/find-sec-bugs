package testcode.serial.groovy;

import groovy.lang.Closure;
import org.codehaus.groovy.runtime.ConversionHandler;

import java.io.Serializable;
import java.lang.reflect.Method;

public class ConvertedClosure extends ConversionHandler implements Serializable {

    private String methodName;
    private static final long serialVersionUID = 1162833713450835227L;

    public ConvertedClosure(Closure closure, String method) {
        super(closure);
        this.methodName = method;
    }

    public ConvertedClosure(Closure closure) {
        this(closure, (String)null);
    }

    public Object invokeCustom(Object proxy, Method method, Object[] args) throws Throwable {
        return this.methodName != null && !this.methodName.equals(method.getName()) ? null : ((Closure)this.getDelegate()).call(args);
    }

}
