package testcode.modify_validate;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
public class ModifyAfter {

    public static String validate(String str) {
        String s = Normalizer.normalize(str, Form.NFKC);
 
        Pattern pattern = Pattern.compile("<script>");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            throw new IllegalArgumentException("Invalid input");
        }
 
        s = s.replaceAll("[\\p{Cn}]", "");
        return s;
    }

    public static void main(String[] args) {
        String s1 = "<scr" + "\uFDEF" + "ipt>";
        String s2 = validate(s1);
    }
}
