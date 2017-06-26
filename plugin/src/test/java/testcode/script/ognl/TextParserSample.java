package testcode.script.ognl;

import com.opensymphony.xwork2.util.OgnlTextParser;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.TextParser;
import com.opensymphony.xwork2.util.ValueStack;

public class TextParserSample {

    public void unsafeTextParser(String input, TextParser textParser, TextParseUtil.ParsedValueEvaluator parser) {
        textParser.evaluate(null, input, parser, 0);
    }

    public void safeTextParser(TextParser textParser, TextParseUtil.ParsedValueEvaluator parser) {
        String input = "1+1";
        textParser.evaluate(null, input, parser, 0);
    }


    public void unsafeOgnlTextParser(String input, OgnlTextParser textParser, TextParseUtil.ParsedValueEvaluator evaluator) {
        textParser.evaluate(new char[]{'a'}, input, evaluator, 0);
    }

    public void safeOgnlTextParser(OgnlTextParser textParser, TextParseUtil.ParsedValueEvaluator evaluator) {
        String input = "1+1";
        textParser.evaluate(new char[]{'a'}, input, evaluator, 0);
    }

    public void unsafeTextParseUtil(String input) {
        TextParseUtil.translateVariables(input, null);
        TextParseUtil.translateVariables(input, null, null);
        TextParseUtil.translateVariables('a', input, null);
        TextParseUtil.translateVariables('a', input, null, null);
        TextParseUtil.translateVariables(new char[]{'a'}, input, null, null, null);
        TextParseUtil.translateVariables(new char[]{'a'}, input, null, null, null, 0);
        TextParseUtil.translateVariables('a', input, null, null, null, 0);
    }

    public void safeTextParseUtil(ValueStack stack, TextParseUtil.ParsedValueEvaluator parsedValueEvaluator, Class type) {
        String input = "1+1";
        TextParseUtil.translateVariables(input, stack);
        TextParseUtil.translateVariables(input, stack, parsedValueEvaluator);
        TextParseUtil.translateVariables('a', input, stack);
        TextParseUtil.translateVariables('a', input, stack, type);
        TextParseUtil.translateVariables(new char[]{'a'}, input, stack, type, parsedValueEvaluator);
        TextParseUtil.translateVariables(new char[]{'a'}, input, stack, type, parsedValueEvaluator, 0);
        TextParseUtil.translateVariables('a', input, stack, type, parsedValueEvaluator, 0);
    }
}
