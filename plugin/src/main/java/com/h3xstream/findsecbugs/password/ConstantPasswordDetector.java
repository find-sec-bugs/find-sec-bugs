/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.password;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.XField;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

/**
 * General detector for hard coded passwords and cryptographic keys
 * 
 * @author David Formanek
 */
@OpcodeStack.CustomUserValue
public class ConstantPasswordDetector extends OpcodeStackDetector {

    private static final String HARD_CODE_PASSWORD_TYPE = "HARD_CODE_PASSWORD";
    private final BugReporter bugReporter;
    private boolean staticInitializerSeen = false;

    // configuration files with password methods
    private static final String PASSWORD_METHODS_DIR = "password-methods";
    private static final String STRING_METHODS_FILENAME = "password-methods-string.txt";

    // suspicious variable names with password or keys
    private static final String PASSWORD_NAMES = ".*(pass|pwd|psw|secret|key|cipher|crypt|des|aes).*";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_NAMES, Pattern.CASE_INSENSITIVE);

    private final Map<String, Integer> stringMethods = new HashMap<String, Integer>();

    private boolean isFirstArrayStore = false;
    private boolean isHardCodedTypeConversion = false;
    private Set<String> hardCodedFields = new HashSet<String>();
    
    public ConstantPasswordDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
        try {
            loadMap(STRING_METHODS_FILENAME, stringMethods, "#");
        } catch (IOException ex) {
            throw new RuntimeException("cannot load resources", ex);
        }
    }

    @Override
    public void visit(JavaClass javaClass) {
        staticInitializerSeen = false;
        Method[] methods = javaClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(STATIC_INITIALIZER_NAME)) {
                // check field initialization before visiting methods
                doVisitMethod(method);
                staticInitializerSeen = true;
                break;
            }
        }
        isFirstArrayStore = false;
        isHardCodedTypeConversion = false;
        //hardCodedFields.clear();
    }

    @Override
    public void visit(Method method) {
        isFirstArrayStore = false;
        isHardCodedTypeConversion = false;
    }

    @Override
    public void sawOpcode(int seen) {
        if (getMethodName().equals(STATIC_INITIALIZER_NAME) && staticInitializerSeen) {
            // prevent double analysis of static initializer
            return;
        }
        if (stack.getStackDepth() == 0) {
            System.out.println("empty stack");
        }
        // trace hard coded Strings (possibly null)
        for (int i = 0; i < stack.getStackDepth(); i++) {
            OpcodeStack.Item stackItem = stack.getStackItem(i);
            if (stackItem.getConstant() != null || stackItem.isNull()) {
                stackItem.setUserValue(Boolean.TRUE);
                System.out.println("setting uv to item " + i);
            }
            XField xField = stackItem.getXField();
            if (xField != null) {
                System.out.println("checking field " + xField);
                String[] split = xField.toString().split(" ");
                if (split.length > 1) { // could be 1 in Android
                    String sig = split[split.length - 1];
                    if ("[C".equals(sig) || "[B".equals(sig)
                            || "Ljava/math/BigInteger;".equals(sig)) {
                        String field = split[split.length - 2] + sig;
                        if (hardCodedFields.contains(field)) {
                            stackItem.setUserValue(Boolean.TRUE);
                            System.out.println("const field source: " + field);
                        } else {
                            System.out.println("field " + field + " not in set");
                        }
                    }
                }
            }
            System.out.println("item " + i + ": " + stackItem);
        }
        printOpCode(seen);
        
        if (seen == NEWARRAY) {
            isFirstArrayStore = true;
        }
        if (seen == CASTORE || seen == BASTORE || seen == SASTORE || seen == IASTORE) { // TODO add ICONST_X
            if (isFirstArrayStore
                    && stack.getStackItem(0).getUserValue() != null 
                    && stack.getStackItem(1).getUserValue() != null) {
                stack.getStackItem(2).setUserValue(Boolean.TRUE);
                isFirstArrayStore = false;
            }
            if (stack.getStackItem(0).getUserValue() == null
                    || stack.getStackItem(1).getUserValue() == null) {
                stack.getStackItem(2).setUserValue(null);
            }
        }
        if (isHardCodedTypeConversion && stack.getStackDepth() > 0) {
            stack.getStackItem(0).setUserValue(Boolean.TRUE);
            System.out.println("Setting uv: " + stack.getStackItem(0));
            isHardCodedTypeConversion = false;
            System.out.println("isHardCodedTypeConversion=false");
        }
        /*if (isBigIntegerInitialization && stack.getStackDepth() > 0) {
            stack.getStackItem(0).setUserValue(Boolean.TRUE);
            isBigIntegerInitialization = false;
        }*/
        if (seen == PUTFIELD || seen == PUTSTATIC) { // TODO ignore String
            if (stack.getStackItem(0).getUserValue() != null) {
                String fieldName = getDottedClassConstantOperand() + "."
                        + getNameConstantOperand() + getSigConstantOperand();
                hardCodedFields.add(fieldName);
                System.out.println("added to fields: " + fieldName);
            }
        }
        /*if (isLoadingField) {
            stack.getStackItem(0).setUserValue(Boolean.TRUE);
            isLoadingField = false;
        }
        if (seen == GETFIELD || seen == GETSTATIC) {
            String fieldName = getNameConstantOperand();
            System.out.println("fields: " + hardCodedFields);
            if (hardCodedFields.contains(fieldName)) {
                //isLoadingField = true; // TODO remove?
            }
        }*/
        
        if (seen < INVOKEVIRTUAL || seen > INVOKEINTERFACE) {
            // not a method invocation, getCalledMethodName would fail
            return;
        }
        final String calledMethod = getCalledMethodName();
        
        final String methodToCharArray = "java/lang/String.toCharArray()[C";
        if (methodToCharArray.equals(calledMethod) && hasHardCodedStackItem(0)) {
            isHardCodedTypeConversion = true;
        }
        if (calledMethod.equals("java/lang/String.getBytes()[B") && hasHardCodedStackItem(0)) {
            isHardCodedTypeConversion = true;
            System.out.println("isHardCodedTypeConversion=true");
        }
        if (calledMethod.equals("java/lang/String.getBytes(Ljava/lang/String;)[B") && hasHardCodedStackItem(1)) {
            isHardCodedTypeConversion = true;
            System.out.println("isHardCodedTypeConversion=true");
        }
        
        final String constructorString = "java/math/BigInteger.<init>(Ljava/lang/String;)V";
        final String constructorStringRadix = "java/math/BigInteger.<init>(Ljava/lang/String;I)V";
        final String constructorByte = "java/math/BigInteger.<init>([B)V";
        final String constructorByteSignum = "java/math/BigInteger.<init>(I[B)V";
        if (constructorString.equals(calledMethod) && hasHardCodedStackItem(0)) {
            stack.getStackItem(1).setUserValue(Boolean.TRUE);
        } else if (constructorStringRadix.equals(calledMethod) && hasHardCodedStackItem(1)) {
            stack.getStackItem(2).setUserValue(Boolean.TRUE);
        } else if (constructorByte.equals(calledMethod) && hasHardCodedStackItem(0)) {
            stack.getStackItem(1).setUserValue(Boolean.TRUE);
        } else if (constructorByteSignum.equals(calledMethod) && hasHardCodedStackItem(0)) {
            stack.getStackItem(2).setUserValue(Boolean.TRUE);
        }
        
        if (stringMethods.containsKey(calledMethod)) {
            int offset = stringMethods.get(calledMethod);
            if (hasHardCodedStackItem(offset) && !stack.getStackItem(offset).isNull()) {
                reportBug(calledMethod, Priorities.HIGH_PRIORITY);
            }
        }
    }

    /*private void checkFieldLoaded(int seen) {
        if ((seen != GETSTATIC && seen != GETFIELD)) {
            return;
        }
        if (!getClassName().equals(getClassConstantOperand())) {
            return;
        }
        final String sig = getSigConstantOperand();
        if ("[C".equals(sig)) {
            charArrayFieldLoaded = true;
        } else if ("[B".equals(sig)) {
            byteArrayFieldLoaded = true;
        } else if ("Ljava/math/BigInteger;".equals(sig)) {
            bigIntegerFieldLoaded = true;
        }
    }*/

    private boolean hasHardCodedStackItem(int position) {
        return stack.getStackItem(position).getUserValue() != null;
    }

    private String getCalledMethodName() {
        String methodNameWithSignature = getNameConstantOperand() + getSigConstantOperand();
        return getClassConstantOperand() + "." + methodNameWithSignature;
    }

    private void reportBug(String value, int priority) {
        bugReporter.reportBug(new BugInstance(
                this, HARD_CODE_PASSWORD_TYPE, priority)
                .addClass(this).addMethod(this).addSourceLine(this).addString(value));
    }

    private void loadMap(String filename, Map<String, Integer> map, String separator) throws IOException {
        BufferedReader reader = null;
        try {
            reader = getReader(filename);
            for (;;) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] tuple = line.split(separator);
                map.put(tuple[0], Integer.parseInt(tuple[1]));
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private BufferedReader getReader(String filename) {
        String path = PASSWORD_METHODS_DIR + "/" + filename;
        return new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(path)
        ));
    }
}
