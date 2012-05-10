package edu.umd.cs.findbugs.test;

import edu.umd.cs.findbugs.AbstractBugReporter;
import edu.umd.cs.findbugs.AnalysisError;
import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.SortedBugCollection;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;

public class EasyBugReporter extends AbstractBugReporter {

	BugCollection bugCollection = new SortedBugCollection();
	
	
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
		System.out.println(bugInstance.getMessage());
		bugCollection.add(bugInstance);
	}

	@Override
	public void reportAnalysisError(AnalysisError error) {
		
	}

	@Override
	public void reportMissingClass(String string) {
		
	}

}
