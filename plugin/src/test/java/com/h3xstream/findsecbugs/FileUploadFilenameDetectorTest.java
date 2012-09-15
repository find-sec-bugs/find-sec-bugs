package com.h3xstream.findsecbugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FileUploadFilenameDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCommandInjection() throws Exception {
        //Locate com.h3xstream.findbugs.test code
        String[] files = {
                getClassFilePath("testcode/FileUploadCommon"),
                getClassFilePath("testcode/FileUploadWicket")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("FILE_UPLOAD_FILENAME")
                        .inClass("FileUploadWicket").inMethod("handleFile").atLine(16)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("FILE_UPLOAD_FILENAME")
                        .inClass("FileUploadCommon").inMethod("handleFile").atLine(17)
                        .build()
        );
    }
}
