package com.h3xstream.findsecbugs.injection;

import org.apache.bcel.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 */
public class SinksLoader {

    private static final String SINK_CONFIG_PATH = "injection-sinks/";

    protected SinksLoader() {}

    protected void loadConfiguredSinks(String filename, String bugType, InjectionPointReceiver receiver) {
        assert filename != null && bugType != null && !bugType.isEmpty();
        InputStream stream = null;
        try {
            stream = getClass().getClassLoader()
                    .getResourceAsStream(SINK_CONFIG_PATH.concat(filename));
            loadSinks(stream, bugType, receiver);
        } catch (IOException ex) {
            throw new RuntimeException("cannot load resources", ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    throw new RuntimeException("cannot close stream", ex);
                }
            }
        }
    }

    private void loadSinks(InputStream input, String bugType, InjectionPointReceiver receiver) throws IOException {
        assert input != null && bugType != null && !bugType.isEmpty();
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
        String classAndMethodName = fullMethodName.substring(0, fullMethodName.indexOf('('));
        int slashIndex = classAndMethodName.lastIndexOf('/');
        String shortName = classAndMethodName.substring(slashIndex + 1);
        if (shortName.endsWith(Constants.CONSTRUCTOR_NAME)) {
            shortName = shortName.substring(0, shortName.indexOf('.'));
        }
        injectionPoint.setInjectableMethod(shortName.concat("(...)"));
        receiver.receiveInjectionPoint(fullMethodName, injectionPoint);
    }

    /**
     * Interface that imitate lambda pattern.
     */
    public interface InjectionPointReceiver {
        void receiveInjectionPoint(String fullMethodName, InjectionPoint injectionPoint);
    }
}


