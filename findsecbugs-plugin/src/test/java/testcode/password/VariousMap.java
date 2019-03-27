package testcode.password;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

public abstract class VariousMap {

    public static final String CAMEL_CASE_KEY_P4SS = "KeyStore.PassWord";

    public void hardcode1() {
        Map map1 = new HashMap<>();
        map1.put("password","secret1234");
        init(map1);
    }

    public void hardcode2() {
        HashMap map2 = new HashMap<>();
        map2.put("database.password","secret1234");
        init(map2);
    }

    public void hardcode3() {
        Hashtable map3 = new Hashtable<>();
        map3.put(CAMEL_CASE_KEY_P4SS,"secret1234");
        init(map3);
    }

    public void hardcode4() {
        Properties map4 = new Properties();
        map4.setProperty(CAMEL_CASE_KEY_P4SS,"secret1234");
        init(map4);
    }

    public void hardcode5() {
        Map<String, String> map5 = new HashMap<>();
        map5.putIfAbsent("password", "secret1234");
    }

    public void safe(String input) {
        Map map1 = new HashMap<>();
        map1.put("password", input);
        init(map1);
        HashMap map2 = new HashMap<>();
        map2.put("database.password", input);
        init(map2);
        Hashtable map3 = new Hashtable<>();
        map3.put(CAMEL_CASE_KEY_P4SS, input);
        init(map3);
        Properties map4 = new Properties();
        map4.setProperty(CAMEL_CASE_KEY_P4SS, input);
        init(map4);
    }


    public abstract void init(Map map);
}
