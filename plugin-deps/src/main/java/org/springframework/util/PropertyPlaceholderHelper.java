package org.springframework.util;

public class PropertyPlaceholderHelper {
    public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix) {

    }


    public interface PlaceholderResolver {
        String resolvePlaceholder(String placeHolder);
    }

    public String replacePlaceholders(String value, PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver) {
        return null;
    }
}