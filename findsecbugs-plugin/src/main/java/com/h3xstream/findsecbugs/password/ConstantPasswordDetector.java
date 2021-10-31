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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

/**
 * General detector for hard coded passwords and cryptographic keys
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
@OpcodeStack.CustomUserValue
public class ConstantPasswordDetector extends OpcodeStackDetector {

    private static final String HARD_CODE_PASSWORD_TYPE = "HARD_CODE_PASSWORD";
    private static final String HARD_CODE_KEY_TYPE = "HARD_CODE_KEY";
    private final BugReporter bugReporter;
    private boolean staticInitializerSeen = false;

    // configuration file with password methods
    private static final String CONFIG_DIR = "password-methods";
    private static final String METHODS_FILENAME = "password-methods-all.txt";

    // full method names
    private static final String GET_BYTES_STRING = "java/lang/String.getBytes(Ljava/lang/String;)[B";
    private static final String GET_BYTES = "java/lang/String.getBytes()[B";
    private static final String TO_CHAR_ARRAY = "java/lang/String.toCharArray()[C";
    private static final String BIGINTEGER_CONSTRUCTOR_STRING = "java/math/BigInteger.<init>(Ljava/lang/String;)V";
    private static final String BIGINTEGER_CONSTRUCTOR_STRING_RADIX = "java/math/BigInteger.<init>(Ljava/lang/String;I)V";
    private static final String BIGINTEGER_CONSTRUCTOR_BYTE = "java/math/BigInteger.<init>([B)V";
    private static final String BIGINTEGER_BYTE_SIGNUM = "java/math/BigInteger.<init>(I[B)V";

    // suspicious variable names with password or keys
    private static final String PASSWORD_NAMES
            = ".*(pass|pwd|psw|secret|key|cipher|crypt|des|aes|mac|private|sign|cert).*";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_NAMES, Pattern.CASE_INSENSITIVE);

    private final Map<String, Collection<Integer>> sinkMethods = new HashMap<String, Collection<Integer>>();
    private boolean isFirstArrayStore = false;
    private boolean wasToConstArrayConversion = false;
    private static final Set<String> hardCodedFields = new HashSet<String>();
    private static final Set<String> reportedFields = new HashSet<String>();
    private String calledMethod = null;

    public ConstantPasswordDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
        try {
            loadMap(METHODS_FILENAME, sinkMethods, "#");
        } catch (IOException ex) {
            throw new RuntimeException("cannot load resources", ex);
        }
    }

    @Override
    public void visit(JavaClass javaClass) {
        staticInitializerSeen = false;
        Method[] methods = javaClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(Const.STATIC_INITIALIZER_NAME)) {
                // check field initialization before visiting methods
                doVisitMethod(method);
                staticInitializerSeen = true;
                break;
            }
        }
        isFirstArrayStore = false;
        wasToConstArrayConversion = false;
    }

    @Override
    public void visitAfter(JavaClass obj) {
        Collection<String> fieldsToReport = new ArrayList<String>();
        for (String field : hardCodedFields) {
            if (isSuspiciousName(field, obj) && !reportedFields.contains(field)) {
                fieldsToReport.add(field);
            }
        }
        reportBugSource(fieldsToReport, Priorities.NORMAL_PRIORITY);
        // TODO global analysis
        hardCodedFields.clear();
        reportedFields.clear();
        super.visitAfter(obj);
    }

    private static boolean isSuspiciousName(String fullFieldName, JavaClass obj) {
        int classNameLength = obj.getClassName().length();
        // do not search pattern in class name (only the field name matter)
        if(classNameLength <= fullFieldName.length()) {
            String fieldName = fullFieldName.substring(classNameLength);
            return PASSWORD_PATTERN.matcher(fieldName).matches();
        }
        else { //Fallback to cover https://github.com/find-sec-bugs/find-sec-bugs/issues/651
            String fieldName = fullFieldName.substring(Math.max(fullFieldName.lastIndexOf('.'),0));
            return PASSWORD_PATTERN.matcher(fieldName).matches();
        }
    }

    @Override
    public void visit(Method method) {
        isFirstArrayStore = false;
        wasToConstArrayConversion = false;
    }

    @Override
    public void sawOpcode(int seen) {
        if (isAlreadyAnalyzed()) {
            return;
        }
        markHardCodedItemsFromFlow();
        if (seen == Const.NEWARRAY) {
            isFirstArrayStore = true;
        }
        if (isStoringToArray(seen)) {
            markArraysHardCodedOrNot();
            isFirstArrayStore = false;
        }
        if (wasToConstArrayConversion) {
            markTopItemHardCoded();
            wasToConstArrayConversion = false;
        }
        if (seen == Const.PUTFIELD || seen == Const.PUTSTATIC) {
            saveArrayFieldIfHardCoded();
        }
        if (isInvokeInstruction(seen)) {
            calledMethod = getCalledMethodName();
            wasToConstArrayConversion = isToConstArrayConversion();
            markBigIntegerHardCodedOrNot();
            reportBadSink();
        }
    }

    private boolean isAlreadyAnalyzed() {
        return getMethodName().equals(Const.STATIC_INITIALIZER_NAME) && staticInitializerSeen;
    }

    private void markHardCodedItemsFromFlow() {
        for (int i = 0; i < stack.getStackDepth(); i++) {
            OpcodeStack.Item stackItem = stack.getStackItem(i);
            if ((stackItem.getConstant() != null || stackItem.isNull())
                    && !stackItem.getSignature().startsWith("[")) {
                setHardCodedItem(stackItem);
            }
            if (hasHardCodedFieldSource(stackItem)) {
                setHardCodedItem(stackItem);
            }
        }
    }

    private boolean hasHardCodedFieldSource(OpcodeStack.Item stackItem) {
        XField xField = stackItem.getXField();
        if (xField == null) {
            return false;
        }
        String[] split = xField.toString().split(" ");
        int length = split.length;
        if (length < 2) {
            return false;
        }
        String fieldSignature = split[length - 1];
        if (!isSupportedSignature(fieldSignature)) {
            return false;
        }
        String fieldName = split[length - 2] + fieldSignature;
        return hardCodedFields.contains(fieldName);
    }

    private static boolean isStoringToArray(int seen) {
        return seen == Const.CASTORE || seen == Const.BASTORE || seen == Const.SASTORE || seen == Const.IASTORE;
    }

    private void markArraysHardCodedOrNot() {
        if (hasHardCodedStackItem(0) && hasHardCodedStackItem(1)) {
            if (isFirstArrayStore) {
                setHardCodedItem(2);
            }
        } else { // then array not hard coded
            stack.getStackItem(2).setUserValue(null);
        }
    }

    private void markTopItemHardCoded() {
        assert stack.getStackDepth() > 0;
        setHardCodedItem(0);
    }

    private void saveArrayFieldIfHardCoded() {
        String fieldSignature = getSigConstantOperand();
        if (isSupportedSignature(fieldSignature)
                && hasHardCodedStackItem(0)
                && !stack.getStackItem(0).isNull()) {
            String fieldName = getFullFieldName();
            hardCodedFields.add(fieldName);
        }
    }

    private static boolean isInvokeInstruction(int seen) {
        return seen >= Const.INVOKEVIRTUAL && seen <= Const.INVOKEINTERFACE;
    }

    private boolean isToConstArrayConversion() {
        return isInMethodWithConst(TO_CHAR_ARRAY, 0)
                || isInMethodWithConst(GET_BYTES, 0)
                || isInMethodWithConst(GET_BYTES_STRING, 1);
    }

    private void markBigIntegerHardCodedOrNot() {
        if (isInMethodWithConst(BIGINTEGER_CONSTRUCTOR_STRING, 0)
                || isInMethodWithConst(BIGINTEGER_CONSTRUCTOR_BYTE, 0)) {
            setHardCodedItem(1);
        } else if (isInMethodWithConst(BIGINTEGER_CONSTRUCTOR_STRING_RADIX, 1)
                || isInMethodWithConst(BIGINTEGER_BYTE_SIGNUM, 0)) {
            setHardCodedItem(2);
        }
    }

    private void reportBadSink() {
        if (!sinkMethods.containsKey(calledMethod)) {
            return;
        }
        Collection<Integer> offsets = sinkMethods.get(calledMethod);
        Collection<Integer> offsetsToReport = new ArrayList<Integer>();
        for (Integer offset : offsets) {
            if (hasHardCodedStackItem(offset) && !stack.getStackItem(offset).isNull()) {
                offsetsToReport.add(offset);
                String sourceField = getStackFieldName(offset);
                if (sourceField != null) {
                    reportedFields.add(sourceField);
                }
            }
        }
        if (!offsetsToReport.isEmpty()) {
            reportBugSink(Priorities.HIGH_PRIORITY, offsets);
        }
    }

    private String getStackFieldName(int offset) {
        XField xField = stack.getStackItem(offset).getXField();
        if (xField == null) {
            return null;
        }
        String[] split = xField.toString().split(" ");
        if (split.length < 2) {
            return null;
        }
        return split[split.length - 2] + split[split.length - 1];
    }

    private void reportBugSink(int priority, Collection<Integer> offsets) {
        String bugType = HARD_CODE_KEY_TYPE;
        for (Integer paramIndex : offsets) {
            OpcodeStack.Item stackItem = stack.getStackItem(paramIndex);
            String signature = stackItem.getSignature();
            if ("Ljava/lang/String;".equals(signature) || "[C".equals(signature)) {
                bugType = HARD_CODE_PASSWORD_TYPE;
                break;
            }
        }
        BugInstance bugInstance = new BugInstance(this, bugType, priority)
                .addClass(this).addMethod(this)
                .addSourceLine(this).addCalledMethod(this);
        for (Integer paramIndex : offsets) {
            OpcodeStack.Item stackItem = stack.getStackItem(paramIndex);
            bugInstance.addParameterAnnotation(paramIndex,
                    "Hard coded parameter number (in reverse order) is")
                    .addFieldOrMethodValueSource(stackItem);
            Object constant = stackItem.getConstant();
            if (constant != null) {
                bugInstance.addString(constant.toString());
            }
        }
        bugReporter.reportBug(bugInstance);
    }

    private void reportBugSource(Collection<String> fields, int priority) {
        if (fields.isEmpty()) {
            return;
        }
        String bugType = HARD_CODE_KEY_TYPE;
        for (String field : fields) {
            if (field.endsWith("[C")) {
                bugType = HARD_CODE_PASSWORD_TYPE;
                break;
            }
        }
        BugInstance bug = new BugInstance(this, bugType, priority).addClass(this);
        for (String field : fields) {
            bug.addString("is hard coded in field " + field + " with suspicious name");
        }
        bugReporter.reportBug(bug);
    }

    private void setHardCodedItem(int stackOffset) {
        setHardCodedItem(stack.getStackItem(stackOffset));
    }

    private void setHardCodedItem(OpcodeStack.Item stackItem) {
        stackItem.setUserValue(Boolean.TRUE);
    }

    private boolean hasHardCodedStackItem(int stackOffset) {
        return stack.getStackItem(stackOffset).getUserValue() != null;
    }

    private boolean isInMethodWithConst(String method, int stackOffset) {
        return method.equals(calledMethod) && hasHardCodedStackItem(stackOffset);
    }

    private String getFullFieldName() {
        String fieldName = getDottedClassConstantOperand() + "."
                + getNameConstantOperand() + getSigConstantOperand();
        return fieldName;
    }

    private static boolean isSupportedSignature(String signature) {
        return "[C".equals(signature)
                || "[B".equals(signature)
                || "Ljava/math/BigInteger;".equals(signature);
    }

    private String getCalledMethodName() {
        String methodNameWithSignature = getNameConstantOperand() + getSigConstantOperand();
        return getClassConstantOperand() + "." + methodNameWithSignature;
    }

    private void loadMap(String filename, Map<String, Collection<Integer>> map,
            String separator) throws IOException {
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
                int count = tuple.length - 1;
                Collection<Integer> parameters = new ArrayList<Integer>(count);
                for (int i = 0; i < count; i++) {
                    parameters.add(Integer.parseInt(tuple[i + 1]));
                }
                map.put(tuple[0], parameters);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private BufferedReader getReader(String filename) {
        String path = CONFIG_DIR + "/" + filename;
        return new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(path)
        , Charset.forName("UTF-8")));
    }
}
