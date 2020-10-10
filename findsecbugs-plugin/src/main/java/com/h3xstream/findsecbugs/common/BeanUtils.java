package com.h3xstream.findsecbugs.common;


import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanUtils {
    public static void copyProperties(final Object dest,
                                      final Object orig)
            throws IllegalAccessException, InvocationTargetException, IllegalArgumentException {

        BeanUtils.copyProperties(orig,
                dest);
    }

    public void addNewUser (Map < String, String > orig)
                throws InvocationTargetException, IllegalAccessException {
            Object objToLoad = null;
            BeanUtils.copyProperties(null, orig);
        }

}
