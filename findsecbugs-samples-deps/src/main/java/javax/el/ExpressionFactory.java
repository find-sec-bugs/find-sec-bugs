package javax.el;

public abstract class ExpressionFactory {

    public abstract ValueExpression createValueExpression(ELContext context,
                                                          java.lang.String expression,
                                                          java.lang.Class<?> expectedType);

    public abstract ValueExpression createValueExpression(java.lang.Object instance,
                                                          java.lang.Class<?> expectedType);

    public abstract MethodExpression createMethodExpression(ELContext context,
                                                            java.lang.String expression,
                                                            java.lang.Class<?> expectedReturnType,
                                                            java.lang.Class<?>[] expectedParamTypes);
}
