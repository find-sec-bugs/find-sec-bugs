package org.springframework.expression;

public interface Expression {

    public <T> T getValue(Class<T> stringClass);

    public <T> T getValue(EvaluationContext context,Class<T> stringClass);
}
