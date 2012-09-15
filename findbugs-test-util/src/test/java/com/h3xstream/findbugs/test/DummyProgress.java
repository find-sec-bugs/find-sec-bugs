package com.h3xstream.findbugs.test;

import edu.umd.cs.findbugs.FindBugsProgress;

public class DummyProgress implements FindBugsProgress {

	@Override
	public void reportNumberOfArchives(int numArchives) {
		
	}

	@Override
	public void startArchive(String name) {
		
	}

	@Override
	public void finishArchive() {
		
	}

	@Override
	public void predictPassCount(int[] classesPerPass) {
		
	}

	@Override
	public void startAnalysis(int numClasses) {
		
	}

	@Override
	public void finishClass() {
		
	}

	@Override
	public void finishPerClassAnalysis() {
		
	}

}
