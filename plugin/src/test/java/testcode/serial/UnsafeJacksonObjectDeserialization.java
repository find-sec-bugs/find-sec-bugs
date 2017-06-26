package testcode.serial;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UnsafeJacksonObjectDeserialization {

    static class ABean {
        public int id;
        public Object obj;
    }

    static class AnotherBean {
        @JsonTypeInfo (use = JsonTypeInfo.Id.CLASS)
        public Object obj;
    }

    static class YetAnotherBean {
        @JsonTypeInfo (use = JsonTypeInfo.Id.MINIMAL_CLASS)
        public Object obj;
    }

    public void exampleOne(String JSON)  throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        Object obj = mapper.readValue(JSON, ABean.class);
    }

    public void exampleTwo(String JSON)  throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
        Object obj = mapper.readValue(JSON, ABean.class);
    }

    public void exampleThree(String JSON)  throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(JSON, AnotherBean.class);
    }

    public void exampleFour(String JSON)  throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(JSON, YetAnotherBean.class);
    }

}
