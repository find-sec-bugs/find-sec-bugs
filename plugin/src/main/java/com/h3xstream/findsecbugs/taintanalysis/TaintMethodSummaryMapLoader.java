package com.h3xstream.findsecbugs.taintanalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.TreeSet;

public class TaintMethodSummaryMapLoader {


    public void load(InputStream input, TaintMethodSummaryReceiver receiver) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        for (;;) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            putFromLine(line, receiver);
        }
    }


    private void putFromLine(String line, TaintMethodSummaryReceiver receiver) throws IOException {
        if (line.startsWith("-")) {
            // for comments or removing summary temporarily
            return;
        }
        String[] tuple = line.split("\\:");
        if (tuple.length != 2) {
            throw new IOException("Line format is not 'method name:summary info'");
        }

        receiver.receiveTaintMethodSummary(tuple[0].trim(), TaintMethodSummary.load(tuple[1]));
    }

    public interface TaintMethodSummaryReceiver {
        void receiveTaintMethodSummary(String fullMethodName, TaintMethodSummary taintMethodSummary);
    }
}
