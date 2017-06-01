package testcode.serial;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class JacksonSerialisationFalsePositive implements Serializable {

    static class Bean {
        @JsonTypeInfo (use = JsonTypeInfo.Id.NAME)
        public Object obj;
    }

    public void exampleOne(String JSON)  throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(JSON, JacksonSerialisationFalsePositive.class);
    }

    public void exampleTwo(String JSON)  throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(JSON, Bean.class);
    }
}
