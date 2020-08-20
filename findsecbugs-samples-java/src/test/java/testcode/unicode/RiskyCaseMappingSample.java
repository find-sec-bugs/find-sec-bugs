package testcode.unicode;

import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class RiskyCaseMappingSample {


    public static void main(String[] args) throws URISyntaxException {
        caseMappingSuite();
        caseMappingFalsePositive("hac\u212A");
    }

    public static void caseMappingSuite() {
        System.out.println("Case Mapping 1: "+("hac\u212A".matches("(?i:.*hack.*)"))); //OK
        System.out.println("Case Mapping 2: "+(Pattern.compile("hack", Pattern.CASE_INSENSITIVE).matcher("hac\u212A").find())); //OK

        System.out.println("Case Mapping 1: "+("ADM\u0131N".matches("(?i:.*ADMIN.*)"))); //OK
        System.out.println("Case Mapping 2: "+(Pattern.compile("ADMIN", Pattern.CASE_INSENSITIVE).matcher("ADM\u0131N").find())); //OK

        System.out.println("Case Mapping 3: "+("HACK".equalsIgnoreCase("HAC\u212A"))); //RISKY!
        System.out.println("Case Mapping 4: "+("ADM\u0131N".toUpperCase().equals("ADMIN"))); //RISKY!
        System.out.println("Case Mapping 5: "+("hac\u212A".toLowerCase().equals("hack"))); //RISKY!
    }

    public static void caseMappingFalsePositive(String input) {
        System.out.println("Hello "+input.toLowerCase());
        System.out.println("Hello "+input.toUpperCase());
    }
}
