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
package com.h3xstream.findsecbugs.injection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.bcel.Const;

/**
 * The sanity of the sinks file is crucial (a typo == missed API == missed vulnerability).
 * The loader was extracted from the BasicInjectionDetector class in order to reuse the parsing logic.
 *
 * @see com.h3xstream.findsecbugs.injection.SinkFilesValidationTest
 */
public class SinksLoader {

    private static final String SINK_CONFIG_PATH = "injection-sinks/";

    protected SinksLoader() {}

    protected void loadConfiguredSinks(String filename, String bugType, InjectionPointReceiver receiver) {
        assert filename != null && bugType != null && !bugType.isEmpty();

        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(SINK_CONFIG_PATH.concat(filename))) {
            loadSinks(stream, bugType, receiver);
        } catch (IOException ex) {
            throw new RuntimeException("cannot load resources", ex);
        }
    }

    protected void loadSinks(InputStream input, String bugType, InjectionPointReceiver receiver) throws IOException {
        assert input != null && bugType != null && !bugType.isEmpty() : "Sinks file not found";
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        for (;;) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            line = line.trim();
            if (line.isEmpty() || line.startsWith("-")) {
                continue;
            }
            loadSink(line, bugType,receiver);
        }
    }

    protected void loadSink(String line, String bugType, InjectionPointReceiver receiver) {
        assert line != null && bugType != null && !bugType.isEmpty();
        String[] split = line.split("\\:");
        if (split.length != 2) {
            throw new IllegalArgumentException("There must be exactly one ':' in " + line);
        }
        String[] stringArguments = split[1].split(",");
        int length = stringArguments.length;
        if (length == 0) {
            throw new IllegalArgumentException("no injectable parameters specified in " + line);
        }

        int[] injectableParameters = new int[length];
        for (int i = 0; i < length; i++) {
            try {
                injectableParameters[i] = Integer.parseInt(stringArguments[i]);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("cannot parse " + stringArguments[i], ex);
            }
        }
        String fullMethodName = split[0];
        assert fullMethodName != null && !fullMethodName.isEmpty();
        assert injectableParameters != null && injectableParameters.length != 0;
        addSink(split[0], injectableParameters, bugType, receiver);
    }

    protected void addSink(String fullMethodName, int[] injectableParameters, String bugType, InjectionPointReceiver receiver) {
        InjectionPoint injectionPoint = new InjectionPoint(injectableParameters, bugType);
//        String classAndMethodName = fullMethodName.substring(0, fullMethodName.indexOf('('));
//        int slashIndex = classAndMethodName.lastIndexOf('/');
//        String shortName = classAndMethodName.substring(slashIndex + 1);
//        if (shortName.endsWith(Const.CONSTRUCTOR_NAME)) {
//            shortName = shortName.substring(0, shortName.indexOf('.'));
//        }
//        injectionPoint.setInjectableMethod(shortName.concat("(...)"));
        injectionPoint.setInjectableMethod(fullMethodName);
        receiver.receiveInjectionPoint(fullMethodName, injectionPoint);
    }

    /**
     * Interface that imitate lambda pattern.
     */
    public interface InjectionPointReceiver {
        void receiveInjectionPoint(String fullMethodName, InjectionPoint injectionPoint);
    }
}


