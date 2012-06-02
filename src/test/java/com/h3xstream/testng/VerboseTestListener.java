package com.h3xstream.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

import java.util.List;

public class VerboseTestListener extends TestListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(VerboseTestListener.class);

    @Override
    public void onTestFailure(ITestResult tr) {
        log.error(tr.getName() + " FAILED");
        log.error("Detail cause", tr.getThrowable());
    }
}
