package org.springframework.expression;

public interface Expression {

    Object getValue(EvaluationContext var1);

    public <T> T getValue(Class<T> stringClass);

    public <T> T getValue(EvaluationContext context,Class<T> stringClass);
}
