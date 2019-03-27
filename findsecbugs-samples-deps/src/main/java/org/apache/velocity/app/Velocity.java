package org.apache.velocity.app;

import org.apache.velocity.context.Context;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

public class Velocity {
    public static void init() {
    }

    public static boolean evaluate(Context context, Writer swOut, String logTag, InputStream instream) {
        return false;
    }
    public static boolean evaluate(Context context, Writer swOut, String logTag, Reader reader) {
        return false;
    }
    public static boolean evaluate(Context context, Writer swOut, String logTag, String instring) {
        return false;
    }
}
