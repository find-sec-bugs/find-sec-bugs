package edu.umd.cs.findbugs.test;

import edu.umd.cs.findbugs.AbstractBugReporter;
import edu.umd.cs.findbugs.AnalysisError;
import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.SortedBugCollection;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyBugReporter extends AbstractBugReporter {

    private BugCollection bugCollection = new SortedBugCollection();

    private int bugInstanceCount;

    private static final Logger log = LoggerFactory.getLogger(EasyBugReporter.class);


    public EasyBugReporter() {
        setPriorityThreshold(20);
    }

    @Override
    public void finish() {

    }

    @Override
    public BugCollection getBugCollection() {
        return bugCollection;
    }

    @Override
    public void observeClass(ClassDescriptor classDescriptor) {

    }

    @Override
    public void doReportBug(BugInstance bugInstance) {
        StringBuilder bugDetail = new StringBuilder();
        bugDetail
                .append("\n------------------------------------------------------")
                .append("\nNew Bug Instance: [" + ++bugInstanceCount + "]")
                .append("\n  message=" + bugInstance.getMessage())
                .append("\n  bugType=" + bugInstance.getBugPattern().getType())
                .append("  category=" + bugInstance.getCategoryAbbrev());
        if (bugInstance.getPrimaryClass() != null && bugInstance.getPrimaryMethod() != null &&
                bugInstance.getPrimarySourceLineAnnotation() != null) {
            bugDetail
                    .append("\n  class=" + bugInstance.getPrimaryClass().getClassName())
                    .append("  method=" + bugInstance.getPrimaryMethod().getMethodName())
                    .append("  line=" + bugInstance.getPrimarySourceLineAnnotation().getStartLine());
        }
        bugDetail
                .append("\n------------------------------------------------------");
        log.info(bugDetail.toString());
        bugCollection.add(bugInstance);
    }

    @Override
    public void reportAnalysisError(AnalysisError error) {
        if (error.getException() != null) {
            log.error(error.getException().getMessage(), error.getException());
        } else {
            log.error(error.getMessage());
        }
    }

    @Override
    public void reportMissingClass(String className) {
        log.warn("Missing class " + className);
    }

}
