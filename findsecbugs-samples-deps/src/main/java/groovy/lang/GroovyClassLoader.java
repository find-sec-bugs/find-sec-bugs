package groovy.lang;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;

public class GroovyClassLoader extends URLClassLoader {

    public GroovyClassLoader(URL[] urls) {
        super(urls);
    }

    //Parses the given code source into a Java class.
    public Class parseClass(GroovyCodeSource codeSource) { return null; }
    //Parses the given file into a Java class capable of being run
    public Class parseClass(GroovyCodeSource codeSource, boolean shouldCacheSource) { return null; }

    //Prefer using methods taking a Reader rather than an InputStream to avoid wrong encoding issues.
    @Deprecated
    public Class parseClass(InputStream in, String fileName) { return null; }
    public Class parseClass(File file) { return null; }

    public Class parseClass(Reader reader, String fileName) { return null; }

    //Parses the given text into a Java class capable of being run
    public Class parseClass(String text) { return null; }
    //Parses the given text into a Java class capable of being run
    public Class parseClass(String text, String fileName) { return null; }

}
