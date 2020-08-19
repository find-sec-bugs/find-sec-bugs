package testcode.groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class GroovyShellUsage {

    public static void eval(String uri, String file, String script) throws URISyntaxException, FileNotFoundException {
        GroovyShell shell = new GroovyShell();

        shell.evaluate(new GroovyCodeSource(new File(file)));
        shell.evaluate(new File(file));
        shell.evaluate(new InputStreamReader(new FileInputStream(file)));
        shell.evaluate(new InputStreamReader(new FileInputStream(file)), "script1.groovy");
        shell.evaluate(script);
        shell.evaluate(script, "script1.groovy");
        shell.evaluate(script, "script1.groovy", "test");
        shell.evaluate(new URI(uri));
    }

    public static void parse(String uri, String file, String script) throws URISyntaxException, FileNotFoundException {
        GroovyShell shell = new GroovyShell();

        shell.parse(new File(file));
        shell.parse(new GroovyCodeSource(new File(file)));
        shell.parse(new InputStreamReader(new FileInputStream(file)), "test.groovy");
        shell.parse(new InputStreamReader(new FileInputStream(file)));
        shell.parse(script);
        shell.parse(script, "test.groovy");
        shell.parse(new URI(uri));
    }

    public static void parseClass(String uri, String file, String script, ClassLoader loader) throws URISyntaxException, FileNotFoundException {
        GroovyClassLoader groovyLoader = (GroovyClassLoader) loader;

        groovyLoader.parseClass(new GroovyCodeSource(new File(file)));
        groovyLoader.parseClass(new GroovyCodeSource(new File(file)),false);
        groovyLoader.parseClass(new FileInputStream(file), "test.groovy");
        groovyLoader.parseClass(new File(file));
        groovyLoader.parseClass(new InputStreamReader(new FileInputStream(file)), "test.groovy");
        groovyLoader.parseClass(script);
        groovyLoader.parseClass(script,"test.groovy");
    }
}