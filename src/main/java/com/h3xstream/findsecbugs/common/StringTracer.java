package com.h3xstream.findsecbugs.common;

import edu.umd.cs.findbugs.OpcodeStack;

public class StringTracer {

    /**
     * @param param
     * @return If the given string reference is not a constant
     */
    public static boolean isVariableString(OpcodeStack.Item param) {
        String value = (String) param.getConstant();
        return value == null;
    }

    /**
     * @param stack
     * @return If at least one parameters has a variable string.
     */
    public static boolean hasVariableString(OpcodeStack stack) {
        boolean hasStringParam = false;

        for(int i=0;i<stack.getStackDepth();i++) {
            OpcodeStack.Item item = stack.getStackItem(i);
            if("[Ljava/lang/String;".equals(item.getSignature())) {
                hasStringParam=true;
                if(isVariableString(item)) {
                    return true;
                }
            }
        }
        return !hasStringParam;
    }
}
