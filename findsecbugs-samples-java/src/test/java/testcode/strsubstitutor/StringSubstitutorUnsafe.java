package testcode.strsubstitutor;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class StringSubstitutorUnsafe {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static final String TEMPLATE_STRING = "";


    public void addUser1Unsafe(String templateInput) {

        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("animal", "quick brown fox");
        valuesMap.put("target", "lazy dog");

        StringSubstitutor str = new StringSubstitutor(valuesMap); //valuesMap is considered safe

        jdbcTemplate.execute(str.replace(templateInput)); //Base template is unsafe (templateInput)
        jdbcTemplate.execute(templateInput); //Just to make sure the API is cover
    }

    public void addUser2Unsafe(String someInput) {

        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("animal", "quick brown fox");
        valuesMap.put("target", someInput);

        StringSubstitutor str = new StringSubstitutor(valuesMap); //The map is tainted

        String templateString = "The ${animal} jumped over the ${target}.";
        jdbcTemplate.execute(str.replace(templateString)); //StringSubstitutor is unsafe and should remain.
    }

    public void addUser3Unsafe(String someInput) {
        String result1 = StringSubstitutor.replace(someInput,new HashMap<String, String>()); //Template string is from unknown source

        Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("test",someInput);
        String result2 = StringSubstitutor.replace("",valuesMap); //Map string is from unknown source

        jdbcTemplate.execute(result1);
        jdbcTemplate.execute(result2);
    }
}
