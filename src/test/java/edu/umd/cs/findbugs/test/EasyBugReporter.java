package edu.umd.cs.findbugs.test;

import edu.umd.cs.findbugs.AbstractBugReporter;
import edu.umd.cs.findbugs.AnalysisError;
import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.SortedBugCollection;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;

public class EasyBugReporter extends AbstractBugReporter {

    BugCollection bugCollection = new SortedBugCollection();

    private static int bugInstanceCount;


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
        System.out.println(bugDetail.toString());
        bugCollection.add(bugInstance);
    }

    @Override
    public void reportAnalysisError(AnalysisError error) {
        if (error.getException() != null) {
            System.err.println(error.getException().getMessage());
            error.getException().printStackTrace(System.err);
        } else {
            System.err.println(error.getMessage());
        }
    }

    @Override
    public void reportMissingClass(String className) {
        System.err.println("Missing class " + className);
    }

}
