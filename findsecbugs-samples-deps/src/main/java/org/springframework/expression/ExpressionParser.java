package org.springframework.expression;

public interface ExpressionParser {

    public Expression parseExpression(String expressionString) throws ParseException;

    public Expression parseExpression(String expressionString,ParserContext context) throws ParseException;
}
