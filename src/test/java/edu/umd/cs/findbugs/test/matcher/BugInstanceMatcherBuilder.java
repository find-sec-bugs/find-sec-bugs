package edu.umd.cs.findbugs.test.matcher;

import edu.umd.cs.findbugs.BugInstance;
import org.hamcrest.Matcher;
import org.mockito.Matchers;

/**
 * DSL to build BugInstanceMatcher
 */
public class BugInstanceMatcherBuilder {

    private String bugType;
    private String className;
    private String methodName;
    private Integer lineNumber;

    public BugInstanceMatcherBuilder bugType(String bugType) {
        this.bugType = bugType;
        return this;
    }

    public BugInstanceMatcherBuilder inClass(String className) {
        this.className = className;
        return this;
    }

    public BugInstanceMatcherBuilder inMethod(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public BugInstanceMatcherBuilder atLine(int lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    /**
     * @return Mockito Matcher
     */
    public BugInstance build() {
        return Matchers.argThat(new BugInstanceMatcher(bugType,className,methodName,lineNumber));
    }
}
