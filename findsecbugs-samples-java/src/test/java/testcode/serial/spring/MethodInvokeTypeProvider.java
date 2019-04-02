package testcode.serial.spring;

import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Inspired by the gadget : SerializableTypeWrapper.MethodInvokeTypeProvider.readObject
 * Ref: org.springframework:spring-beans:4.1.4.RELEASE
 */
public class MethodInvokeTypeProvider implements TypeProvider {
    private final TypeProvider provider;
    private final String methodName;
    private final int index;
    private transient Object result;

    public MethodInvokeTypeProvider(TypeProvider provider, Method method, int index) {
        this.provider = provider;
        this.methodName = method.getName();
        this.index = index;
        this.result = ReflectionUtils.invokeMethod(method, provider.getType());
    }

    public Type getType() {
        return !(this.result instanceof Type) && this.result != null?((Type[])((Type[])this.result))[this.index]:(Type)this.result;
    }

    public Object getSource() {
        return null;
    }

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        Method method = ReflectionUtils.findMethod(this.provider.getType().getClass(), this.methodName);
        this.result = ReflectionUtils.invokeMethod(method, this.provider.getType());
    }


}
