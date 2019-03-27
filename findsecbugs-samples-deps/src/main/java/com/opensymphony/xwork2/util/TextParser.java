package com.opensymphony.xwork2.util;

public interface TextParser {
    int DEFAULT_LOOP_COUNT = 1;

    Object evaluate(char[] openChars, String expression, TextParseUtil.ParsedValueEvaluator evaluator, int maxLoopCount);

}
