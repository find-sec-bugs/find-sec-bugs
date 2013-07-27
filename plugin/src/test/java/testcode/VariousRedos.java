package testcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariousRedos {

    public Pattern p1 = Pattern.compile("((a)?)+");
    public Pattern p2 = Pattern.compile("(a|a?)+"); //TODO: Not detected for now

    public static void main(String[] args) {

        String input = args.length > 0 ? args[0] : "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!";

        //Pattern var
        Pattern emailValidation = Pattern.compile("^([a-zA-Z0-9])(([\\-.]|[_]+)?([a-zA-Z0-9]+))*(@){1}[a-z0-9]+[.]{1}(([a-z]{2,3})|([a-z]{2,3}[.]{1}[a-z]{2,3}))$");
        Matcher m = emailValidation.matcher(input);
        if (m.find()) {
            System.out.println("Match Regex #1");
        }

        if (input.matches("(a+)+")) { //TODO: Not detected for now
            System.out.println("Match Regex #2");
        }

        if (input.matches("([a-zA-Z]+)*")) {
            System.out.println("Match Regex #2");
        }

        if (input.matches("(a|aa)+")) { //TODO: Not detected for now
            System.out.println("Match Regex #2");
        }


        //Safe regex

        if (input.matches("1(a)+1234")) {
            System.out.println("Match Regex #2");
        }

        if (input.matches("(([0-5])+(([6-9]))+)([a-z])*")) {
            System.out.println("Match Regex #2");
        }
    }
}
