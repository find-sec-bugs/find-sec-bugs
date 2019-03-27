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
package com.h3xstream.findsecbugs.xpath;

import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;

/**
 * Detector for XPath injection
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class XPathInjectionDetector extends BasicInjectionDetector {

    public XPathInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("xpath-javax.txt", "XPATH_INJECTION");
        loadConfiguredSinks("xpath-apache.txt", "XPATH_INJECTION");
        // TODO add net.sf.saxon.xpath.XPathEvaluator
        // TODO add org.apache.commons.jxpath
        // TODO add org.jdom.xpath.XPath
        // TODO add org.jaxen.XPath
        // TODO add edu.UCL.utils.XPathAPI
        // TODO add org.xmldb.api.modules
    }

    @Override
    protected int getPriority(Taint taint) {
        if (!taint.isSafe() && taint.hasTag(Taint.Tag.XPATH_INJECTION_SAFE)) {
            return Priorities.IGNORE_PRIORITY;
        } else {
            return super.getPriority(taint);
        }
    }
}
