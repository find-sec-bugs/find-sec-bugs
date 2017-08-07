package com.h3xstream.findsecbugs.bugs;

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.common.InterfaceUtils;
import com.h3xstream.findsecbugs.common.JspUtils;
import com.h3xstream.findsecbugs.common.StackUtils;
import org.testng.annotations.Test;

public class CoberturaCoverageTest {

    @Test
    public void coverStaticClasses() {
        new StackUtils();
        new JspUtils();
        new InterfaceUtils();
        new ByteCode();
    }
}
