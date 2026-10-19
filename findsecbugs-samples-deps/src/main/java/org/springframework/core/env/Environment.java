package org.springframework.core.env;

public interface Environment {
    String getProperty(String key);
    String getProperty(String key, String defaultValue);
    String getRequiredProperty(String key);
}
