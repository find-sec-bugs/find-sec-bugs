package jakarta.el;

public abstract class ExpressionFactory {

    public abstract ValueExpression createValueExpression(ELContext context, String expression, Class<?> expectedType);

    public abstract ValueExpression createValueExpression(Object instance, Class<?> expectedType);

    public abstract MethodExpression createMethodExpression(ELContext context, String expression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes);

}
