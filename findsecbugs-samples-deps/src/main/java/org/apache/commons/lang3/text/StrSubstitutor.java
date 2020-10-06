package org.apache.commons.lang3.text;

import java.util.Map;

/**
 * Changes might needs synchronisation with org.apache.commons.text.StringSubstitutor
 */
public class StrSubstitutor {

    public StrSubstitutor() {

    }

    public <V> StrSubstitutor(final Map<String, V> valueMap) {

    }

    public <V> StrSubstitutor(Map<String, V> valueMap, String prefix, String suffix) {

    }

    public <V> StrSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix,
                                 final char escape) {
    }


    public <V> StrSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix,
                                 final char escape, final String valueDelimiter) {

    }


    public static <V> String replace(final Object source, final Map<String, V> valueMap) {
        return null;
    }


    public String replace(final char[] source) {
        return null;
    }
    public String replace(final CharSequence source) {
        return null;
    }
    public String replace(final Object source) {
        return null;
    }
    public String replace(final String source) {
        return null;
    }
}
