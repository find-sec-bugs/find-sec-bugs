package com.h3xstream.findsecbugs.taintanalysis;

import com.h3xstream.findsecbugs.injection.SinksLoader;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import static org.testng.Assert.assertNotNull;

public class MethodSummaryValidationTest {
    private static final boolean DEBUG = true;

    TaintMethodSummaryMapLoader loader = new TaintMethodSummaryMapLoader();

    @Test
    public void validateMethodSummaries() throws IOException {
        for(String directory : Arrays.asList("/taint-config", "/safe-encoders")) {
            InputStream in = getClass().getResourceAsStream(directory);
            assertNotNull(in, "Unable list the resources in the taint-config directory");


            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String file = null;
            while ((file = br.readLine()) != null) {
                if (DEBUG) System.out.println("File : " + file);

                ////////////
                // Validate annotation (list of annotation with parameters)
                if("taint-param-annotations.txt".equals(file)) {

                    BufferedReader br2 = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = br2.readLine()) != null) {
                        String annotation = line.replace('/', '.');
                        validateClass(annotation);
                    }

                }
                ////////////
                // Validate method summaries
                else {
                    InputStream inFile = getClass().getResourceAsStream(directory+"/"+ file);
                    assertNotNull(inFile, "File not found: "+ directory+ "/"+file);
                    validateFile(inFile);
                }
            }
        }

    }

    public void validateFile(InputStream inFile) throws IOException {
        loader.load(inFile, new TaintMethodSummaryMapLoader.TaintMethodSummaryReceiver() {
            @Override
            public void receiveTaintMethodSummary(String fullMethodName, TaintMethodSummary taintMethodSummary) {
                if (DEBUG) System.out.println("[?] fmn: " + fullMethodName);
                String[] methodParts = fullMethodName.split("\\.");

                //Test the validity of the class name
                String className = methodParts[0].replace('/','.');
                validateClass(className);
            }
        });
    }

    public void validateClass(String className) {
        if(className.startsWith("scala")) return;
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.println("[!] Class not found "+className); //FIXME: Replace with assert
        }
    }
}
