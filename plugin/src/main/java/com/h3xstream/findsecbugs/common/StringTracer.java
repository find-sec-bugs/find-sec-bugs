package com.h3xstream.findsecbugs.common;

import edu.umd.cs.findbugs.OpcodeStack;

public class StringTracer {

	public static boolean isConstantString(OpcodeStack.Item item) {
		return !isVariableString(item);
	}

    /**
     * @param item Stack item (parameter passed to the current function)
     * @return If the given string reference is not a constant
     */
    public static boolean isVariableString(OpcodeStack.Item item) {
        String value = (String) item.getConstant();
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
