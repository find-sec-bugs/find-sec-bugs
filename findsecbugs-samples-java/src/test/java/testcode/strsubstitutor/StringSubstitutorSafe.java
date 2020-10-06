package testcode.strsubstitutor;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class StringSubstitutorSafe {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static final String TEMPLATE_STRING = "";


    public void addUser1Safe(String templateInput) {

        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("animal", "quick brown fox");
        valuesMap.put("target", "lazy dog");

        StringSubstitutor str = new StringSubstitutor(valuesMap); //valuesMap is considered safe

        String templateString = "The ${animal} jumped over the ${target}.";
        jdbcTemplate.execute(str.replace(templateString)); //StringSubstitutor and templateString are safe (templateInput)
    }

    public void staticSubstitutor(Map<String, String> valuesMap,String input) {
        String result = StringSubstitutor.replace("",new HashMap<String, String>());

        jdbcTemplate.execute(result);
    }

    public void otherStringSubstitutorSignature() {
        StringSubstitutor str1 = new StringSubstitutor(new HashMap<String, String>());
        StringSubstitutor str2 = new StringSubstitutor(new HashMap<String, String>(),"","");
        StringSubstitutor str3 = new StringSubstitutor(new HashMap<String, String>(),"","",'\\');
        StringSubstitutor str4 = new StringSubstitutor(new HashMap<String, String>(),"","",'\\', ":-");

        String templateString = "The ${animal} jumped over the ${target}.";
        jdbcTemplate.execute(str1.replace(templateString));
        jdbcTemplate.execute(str2.replace((Object) templateString));
        jdbcTemplate.execute(str3.replace(new StringBuilder(templateString)));
        jdbcTemplate.execute(str4.replace(templateString.toCharArray()));
    }



}
