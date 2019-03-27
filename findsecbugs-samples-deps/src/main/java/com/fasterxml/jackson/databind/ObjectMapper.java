package com.fasterxml.jackson.databind;

public class ObjectMapper {

    public ObjectMapper enableDefaultTyping() {
        return null;
    }

    public ObjectMapper enableDefaultTyping(DefaultTyping typing) {
        return null;
    }

    public <T> T readValue(String p, Class<T> valueType) {
        return null;
    }

    public enum DefaultTyping {
        NON_CONCRETE_AND_ARRAYS
    }

}