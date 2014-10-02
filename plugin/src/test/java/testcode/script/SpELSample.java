package testcode.script;


import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateAwareExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * <h3>Why do the test cases need 3 very _similar_ block of code?</h3>
 * <p>Because each of those function have distinct bytecode used for the call to "parseExpression()".
 * One calling the interface, the abstract class and finally the concrete class. We don't want to miss any of the
 * implementations.</p>
 */
public class SpelSample {

    private static PersonDTO TEST_PERSON = new PersonDTO("Benoit", "Doudou");

    public static void parseExpressionInterface(String property) {

        ExpressionParser parser = new SpelExpressionParser();

        //Static value not that useful .. but safe
        Expression exp1 = parser.parseExpression("'safe expression'");
        String constantValue = exp1.getValue(String.class);
        System.out.println("exp1="+constantValue);

        StandardEvaluationContext testContext = new StandardEvaluationContext(TEST_PERSON);
        //Unsafe if the input is control by the user..
        Expression exp2 = parser.parseExpression(property+" == 'Benoit'");
        String dynamicValue = exp2.getValue(testContext, String.class);
        System.out.println("exp2="+dynamicValue);
    }

    public static void parseSpelExpression(String property) {

        SpelExpressionParser parser = new SpelExpressionParser();

        //Static value not that useful .. but safe
        Expression exp1 = parser.parseExpression("'safe expression'");
        String constantValue = exp1.getValue(String.class);
        System.out.println("exp1="+constantValue);

        StandardEvaluationContext testContext = new StandardEvaluationContext(TEST_PERSON);
        //Unsafe if the input is control by the user..
        Expression exp2 = parser.parseExpression(property+" == 'Benoit'");
        String dynamicValue = exp2.getValue(testContext, String.class);
        System.out.println("exp2=" + dynamicValue);
    }

    public static void parseTemplateAwareExpression(String property) {

        TemplateAwareExpressionParser parser = new SpelExpressionParser();

        //Static value not that useful .. but safe
        Expression exp1 = parser.parseExpression("'safe expression'");
        String constantValue = exp1.getValue(String.class);
        System.out.println("exp1="+constantValue);

        StandardEvaluationContext testContext = new StandardEvaluationContext(TEST_PERSON);
        //Unsafe if the input is control by the user..
        Expression exp2 = parser.parseExpression(property+" == 'Benoit'");
        String dynamicValue = exp2.getValue(testContext, String.class);
        System.out.println("exp2="+dynamicValue);
    }

    public static void main(String[] args) {
        //Expected use case..
        parseExpressionInterface("firstName");
        //Malicious use case..
        parseExpressionInterface("T(java.lang.Runtime).getRuntime().exec('calc.exe')"); //start the calc virus :)
    }

    static class PersonDTO {
        public final String firstName;
        public final String lastName;

        public PersonDTO(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }
}
