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
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
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
    private static final String CHAR_ARRAY_METHODS_FILENAME = "password-methods-char.txt";
    private static final String BYTE_ARRAY_METHODS_FILENAME = "password-methods-byte.txt";
    private static final String BIG_INTEGER_METHODS_FILENAME = "password-methods-biginteger.txt";
    private static final String STRING_METHODS_FILENAME = "password-methods-string.txt";

    // suspicious variable names with password or keys
    private static final String PASSWORD_NAMES = ".*(pass|pwd|psw|secret|key|cipher|crypt|des|aes).*";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_NAMES, Pattern.CASE_INSENSITIVE);

    // keys hardcoded in char arrays
    private boolean constCharArraySeenLocally = false;
    private boolean charArrayFieldLoaded = false;
    private boolean constCharArrayFieldDefined = false;
    private Set<String> charMethods = new HashSet<String>();

    // keys hardcoded in byte arrays
    private boolean constByteArraySeenLocally = false;
    private boolean byteArrayFieldLoaded = false;
    private boolean constByteArrayFieldDefined = false;
    private final Set<String> byteMethods = new HashSet<String>();

    // constant (char or byte) array detection
    private static final int MIN_CONST_ARRAY_LENGTH = 4;
    private int constArrayState = -1;
    private boolean isByteArray = false;

    // keys hardcoded in BigInteger classes
    private boolean constBigIntegerSeenLocally = false;
    private boolean bigIntegerFieldLoaded = false;
    private boolean constBigIntegerFieldDefined = false;
    private final Set<String> bigIntegerMethods = new HashSet<String>();

    // keys hardcoded in String constants
    private final Map<String, Integer> stringMethods = new HashMap<String, Integer>();

    public ConstantPasswordDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
        try {
            loadResources();
        } catch (IOException ex) {
            throw new RuntimeException("cannot load resources", ex);
        }
    }

    @Override
    public void visit(JavaClass javaClass) {
        staticInitializerSeen = false;
        constCharArrayFieldDefined = false;
        constByteArrayFieldDefined = false;
        constBigIntegerFieldDefined = false;
        Method[] methods = javaClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(STATIC_INITIALIZER_NAME)) {
                // check field initialization before visiting methods
                doVisitMethod(method);
                staticInitializerSeen = true;
                break;
            }
        }
    }

    @Override
    public void visit(Method method) {
        constCharArraySeenLocally = false;
        charArrayFieldLoaded = false;
        constByteArraySeenLocally = false;
        byteArrayFieldLoaded = false;
        constArrayState = -1;
        constBigIntegerSeenLocally = false;
        bigIntegerFieldLoaded = false;
    }

    @Override
    public void sawOpcode(int seen) {
        // trace hard coded Strings (possibly null)
        for (int i = 0; i < stack.getStackDepth(); i++) {
            OpcodeStack.Item stackItem = stack.getStackItem(i);
            if (stackItem.getConstant() != null || stackItem.isNull()) {
                stackItem.setUserValue(Boolean.TRUE);
            }
        }
        
        if (getMethodName().equals(STATIC_INITIALIZER_NAME) && staticInitializerSeen) {
            // prevent double analysis of static initializer
            return;
        }
        checkArrayDeclaration(seen);
        checkFieldLoaded(seen);
        if (seen == PUTFIELD || seen == PUTSTATIC /*&& isInConstructor()*/) {
            if ("Ljava/math/BigInteger;".equals(getSigConstantOperand())
                    && constBigIntegerSeenLocally) {
                constBigIntegerFieldDefined = true;
            }
        }
        if (seen < INVOKEVIRTUAL || seen > INVOKEINTERFACE) {
            // not a method invocation, getCalledMethodName would fail
            return;
        }
        final String calledMethod = getCalledMethodName();
        checkArrayConversion(calledMethod);
        checkBigIntegerDeclaration(calledMethod);
        checkMethods(calledMethod);
    }

    private void checkArrayDeclaration(int seen) {
        if (constArrayState == -1) { // no new array seen
            if (seen == NEWARRAY) {
                constArrayState = 0;
            }
            return;
        }
        if (constArrayState >= MIN_CONST_ARRAY_LENGTH) {
            setArraySeen(seen);
        }
        if (seen == CASTORE) {
            isByteArray = false;
            constArrayState++;
            return;
        } else if (seen == BASTORE) {
            isByteArray = true;
            constArrayState++;
            return;
        }
        if (seen != DUP
                && seen != BIPUSH
                && seen != SIPUSH
                && seen > 8 // ICONST_X
                ) {
            constArrayState = -1;
        }
    }

    private void setArraySeen(int seen) {
        if (isByteArray) {
            constByteArraySeenLocally = true;
        } else {
            constCharArraySeenLocally = true;
        }
        if (seen == PUTFIELD || seen == PUTSTATIC) {
            String fieldName = getNameConstantOperand();
            if (PASSWORD_PATTERN.matcher(fieldName).matches()) {
                reportBug(fieldName, Priorities.NORMAL_PRIORITY);
            }
            constArrayState = -1;
            if (isByteArray) {
                constByteArrayFieldDefined = true;
            } else {
                constCharArrayFieldDefined = true;
            }
        }
    }

    private void checkFieldLoaded(int seen) {
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
    }

    private void checkArrayConversion(final String currentMethod) {
        final String methodToCharArray = "java/lang/String.toCharArray()[C";
        if (methodToCharArray.equals(currentMethod) && hasHardCodedStackItem(0)) {
            constCharArraySeenLocally = true;
        }
        if (currentMethod.startsWith("java/lang/String.getBytes(") && hasHardCodedStackItem(0)) {
            constByteArraySeenLocally = true;
        }
    }

    private void checkBigIntegerDeclaration(final String currentMethod) {
        final String constructorString = "java/math/BigInteger.<init>(Ljava/lang/String;)V";
        final String constructorStringRadix = "java/math/BigInteger.<init>(Ljava/lang/String;I)V";
        if ((constructorString.equals(currentMethod) && hasHardCodedStackItem(0))
                || (constructorStringRadix.equals(currentMethod)) && hasHardCodedStackItem(1)) {
            constBigIntegerSeenLocally = true;
        }
        if ("java/math/BigInteger.<init>([B)V".equals(currentMethod)
                || "java/math/BigInteger.<init>(I[B)V".equals(currentMethod)) {
            if (hasConstByteArray()) {
                constBigIntegerSeenLocally = true;
            }
        }
    }

    private void checkMethods(final String calledMethod) {
        if (hasConstCharArray()) {
            reportIfInSet(calledMethod, charMethods);
        }
        if (hasConstByteArray()) {
            reportIfInSet(calledMethod, byteMethods);
        }
        if (hasBigInteger()) {
            reportIfInSet(calledMethod, bigIntegerMethods);
        }
        if (stringMethods.containsKey(calledMethod)) {
            if (hasHardCodedStackItem(stringMethods.get(calledMethod))) {
                reportBug(calledMethod, Priorities.HIGH_PRIORITY);
            }
        }
    }

    private boolean hasConstCharArray() {
        return constCharArraySeenLocally || (charArrayFieldLoaded && constCharArrayFieldDefined);
    }

    private boolean hasConstByteArray() {
        return constByteArraySeenLocally || (byteArrayFieldLoaded && constByteArrayFieldDefined);
    }
    
    private boolean hasBigInteger() {
        return constBigIntegerSeenLocally || (bigIntegerFieldLoaded && constBigIntegerFieldDefined);
    }

    private boolean hasHardCodedStackItem(int position) {
        return stack.getStackItem(position).getUserValue() != null;
    }

    private String getCalledMethodName() {
        String methodNameWithSignature = getNameConstantOperand() + getSigConstantOperand();
        return getClassConstantOperand() + "." + methodNameWithSignature;
    }

    private void reportIfInSet(String method, Set<String> set) {
        if (set.contains(method)) {
            reportBug(method, Priorities.HIGH_PRIORITY);
        }
    }

    private void reportBug(String value, int priority) {
        bugReporter.reportBug(new BugInstance(
                this, HARD_CODE_PASSWORD_TYPE, priority)
                .addClass(this).addMethod(this).addSourceLine(this).addString(value));
    }

    private void loadResources() throws IOException {
        // methods using char/byte array or BigInteger as password or key
        loadCollection(CHAR_ARRAY_METHODS_FILENAME, charMethods);
        loadCollection(BYTE_ARRAY_METHODS_FILENAME, byteMethods);
        loadCollection(BIG_INTEGER_METHODS_FILENAME, bigIntegerMethods);
        
        // methods using String at given parameter position for password
        loadMap(STRING_METHODS_FILENAME, stringMethods, "#");
    }

    private void loadCollection(String filename, Collection<String> collection) throws IOException {
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
                collection.add(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
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
