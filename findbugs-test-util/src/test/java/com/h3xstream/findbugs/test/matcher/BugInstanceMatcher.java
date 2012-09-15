package com.h3xstream.findbugs.test.matcher;

import edu.umd.cs.findbugs.*;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class BugInstanceMatcher extends BaseMatcher<BugInstance> {

    private String bugType;
    private String className;
    private String methodName;
    private Integer lineNumber;

    /**
     * All the parameters are optional. Only the non-null parameters are used.
     *
     * @param bugType    Expected bug type
     * @param className  Class name
     * @param methodName Method name
     * @param lineNumber Line number
     */
    public BugInstanceMatcher(String bugType, String className, String methodName, Integer lineNumber) {
        this.bugType = bugType;
        this.className = className;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean matches(Object obj) {
        if (obj instanceof BugInstance) {
            BugInstance bugInstance = (BugInstance) obj;

            boolean criteriaMatches = true;
            if (bugType != null) {
                criteriaMatches &= bugInstance.getType().equals(bugType);
            }
            if (className != null) {
                ClassAnnotation classAnn = extractBugAnnotation(bugInstance,ClassAnnotation.class);
                if(classAnn == null) return false;

                String fullName = classAnn.getClassName();
                String simpleName = fullName.substring(fullName.lastIndexOf(".")+1);
                criteriaMatches &= fullName.equals(className) || simpleName.equals(className);
            }
            if (methodName != null) {
                MethodAnnotation methodAnn = extractBugAnnotation(bugInstance,MethodAnnotation.class);
                if(methodAnn == null) return false;
                criteriaMatches &= methodAnn.getMethodName().equals(methodName);
            }
            if (lineNumber != null) {
                SourceLineAnnotation srcAnn = extractBugAnnotation(bugInstance,SourceLineAnnotation.class);
                if(srcAnn == null) return false;
                criteriaMatches &= srcAnn.getStartLine() <= lineNumber && lineNumber <= srcAnn.getEndLine();
            }
            return criteriaMatches;
        } else {
            return false;
        }
    }

    private <T> T extractBugAnnotation(BugInstance bugInstance,Class<T> annotationType) {
        for(BugAnnotation annotation : bugInstance.getAnnotations()) {
            if(annotation.getClass().equals(annotationType)) {
                return (T) annotation;
            }
        }
        return null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("BugInstance with:\n");
        if (bugType != null) {
            description.appendText("bugType=").appendValue(bugType).appendText(",");
        }
        if (className != null) {
            description.appendText("className=").appendValue(className).appendText(",");
        }
        if (methodName != null) {
            description.appendText("methodName=").appendValue(methodName).appendText(",");
        }
        if (lineNumber != null) {
            description.appendText("lineNumber=").appendValue(lineNumber);
        }
    }
}
