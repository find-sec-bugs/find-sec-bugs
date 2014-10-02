package com.h3xstream.findsecbugs.injection.script;

import com.h3xstream.findsecbugs.injection.InjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import edu.umd.cs.findbugs.BugReporter;

public class SpelInjectionDetector extends InjectionDetector {
    private static final String SPEL_INJECTION_TYPE = "SPEL_INJECTION";

    public SpelInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    public InjectionSource[] getInjectionSource() {
        return new InjectionSource[] {new SpelSource()};
    }

    @Override
    public String getBugType() {
        return SPEL_INJECTION_TYPE;
    }
}
