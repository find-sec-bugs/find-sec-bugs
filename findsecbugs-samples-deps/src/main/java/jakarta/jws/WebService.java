package jakarta.jws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface WebService {

    String name() default "";

    String targetNamespace() default "";

    String serviceName() default "";

    String portName() default "";

    String wsdlLocation() default "";

    String endpointInterface() default "";
}
