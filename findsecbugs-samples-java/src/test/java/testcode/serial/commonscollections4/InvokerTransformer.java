package testcode.serial.commonscollections4;

import org.apache.commons.collections4.Transformer;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InvokerTransformer<I, O> implements Transformer<I, O>, Serializable {
    private static final long serialVersionUID = -8653385846894047688L;
    private final String iMethodName;
    private final Class<?>[] iParamTypes;
    private final Object[] iArgs;

    public static <I, O> Transformer<I, O> invokerTransformer(String methodName) {
        if(methodName == null) {
            throw new IllegalArgumentException("The method to invoke must not be null");
        } else {
            return new InvokerTransformer(methodName);
        }
    }

    public static <I, O> Transformer<I, O> invokerTransformer(String methodName, Class<?>[] paramTypes, Object[] args) {
        if(methodName == null) {
            throw new IllegalArgumentException("The method to invoke must not be null");
        } else if(paramTypes == null && args != null || paramTypes != null && args == null || paramTypes != null && args != null && paramTypes.length != args.length) {
            throw new IllegalArgumentException("The parameter types must match the arguments");
        } else {
            return paramTypes != null && paramTypes.length != 0?new InvokerTransformer(methodName, paramTypes, args):new InvokerTransformer(methodName);
        }
    }

    private InvokerTransformer(String methodName) {
        this.iMethodName = methodName;
        this.iParamTypes = null;
        this.iArgs = null;
    }

    public InvokerTransformer(String methodName, Class<?>[] paramTypes, Object[] args) {
        this.iMethodName = methodName;
        this.iParamTypes = paramTypes != null?(Class[])paramTypes.clone():null;
        this.iArgs = args != null?(Object[])args.clone():null;
    }

    public O transform(Object input) {
        if(input == null) {
            return null;
        } else {
            try {
                Class ex = input.getClass();
                Method method = ex.getMethod(this.iMethodName, this.iParamTypes);
                return (O) method.invoke(input, this.iArgs);
            } catch (NoSuchMethodException var4) {
                throw new RuntimeException("InvokerTransformer: The method \'" + this.iMethodName + "\' on \'" + input.getClass() + "\' does not exist");
            } catch (IllegalAccessException var5) {
                throw new RuntimeException("InvokerTransformer: The method \'" + this.iMethodName + "\' on \'" + input.getClass() + "\' cannot be accessed");
            } catch (InvocationTargetException var6) {
                throw new RuntimeException("InvokerTransformer: The method \'" + this.iMethodName + "\' on \'" + input.getClass() + "\' threw an exception", var6);
            }
        }
    }
}
