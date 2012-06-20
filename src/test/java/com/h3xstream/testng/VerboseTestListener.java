package com.h3xstream.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * This listener is useful to add more detail about test failure.
 * (Intended for Travis-CI)
 */
public class VerboseTestListener extends TestListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(VerboseTestListener.class);

    @Override
    public void onTestFailure(ITestResult tr) {
        log.error(tr.getName()+" failed",tr.getThrowable());
    }
}
