package com.h3xstream.findsecbugs.injection;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.testng.Assert.assertNotNull;

public class SinkFilesValidationTest {
    private static final boolean DEBUG = false;

    @Test
    public void validateSinks() throws IOException {
        InputStream in = getClass().getResourceAsStream("/injection-sinks");
        assertNotNull(in, "Unable list the resources in the injection-sinks directory");

        SinksLoader loader = new SinksLoader();

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while((line = br.readLine()) != null) {
            if(DEBUG) System.out.println("File :"+line);

            loader.loadConfiguredSinks(line, "DUMMY",
                    new SinksLoader.InjectionPointReceiver() {
                        @Override
                        public void receiveInjectionPoint(String fullMethodName, InjectionPoint injectionPoint) {
                            if(DEBUG) System.out.println("[+] fmn: "+fullMethodName);
                            String[] methodParts = fullMethodName.split("\\.");

                            //Test the validity of the class name
                            String className = methodParts[0].replace('/','.');
                            validateClass(className);
                        }
                    }
            );
        }
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
