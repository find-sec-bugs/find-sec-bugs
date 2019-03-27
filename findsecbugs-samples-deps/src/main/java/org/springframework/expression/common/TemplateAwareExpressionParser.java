package org.springframework.expression.common;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;

public abstract class TemplateAwareExpressionParser implements ExpressionParser {


    public Expression parseExpression(String expressionString) throws ParseException {
        return null;
    }

    //TODO: Cover this method signature
    public Expression parseExpression(String expressionString,ParserContext context) throws ParseException {
        return null;
    }
}
