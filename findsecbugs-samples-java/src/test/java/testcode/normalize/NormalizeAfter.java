package testcode.normalize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.Normalizer;
import java.text.Normalizer.Form;

public class NormalizeAfter {

    public static String validate(String s) {
 
        // Validate
        Pattern pattern = Pattern.compile("[<>]");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            throw new IllegalStateException();
        }
        
        return Normalizer.normalize(s, Form.NFKC);
    }

    public static void main(String[] args) {
        String s1 = "\uFE64" + "script" + "\uFE65";
        String s2 = validate(s1);
    }
}
