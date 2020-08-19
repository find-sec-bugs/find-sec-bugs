package groovy.lang;


import java.io.File;
import java.io.Reader;
import java.net.URI;

public class GroovyShell {

    //Evaluates some script against the current Binding and returns the result
    public Object evaluate(GroovyCodeSource codeSource) {
        return null;
    }
    //Evaluates some script against the current Binding and returns the result
    public Object evaluate(File file) {
        return null;
    }
    //Evaluates some script against the current Binding and returns the result
    public Object evaluate(Reader in) {
        return null;
    }
    //Evaluates some script against the current Binding and returns the result
    public Object evaluate(Reader in, String fileName) {
        return null;
    }
    //Evaluates some script against the current Binding and returns the result
    public Object evaluate(String scriptText) {
        return null;
    }
    //Evaluates some script against the current Binding and returns the result
    public Object evaluate(String scriptText, String fileName) {
        return null;
    }
    //Evaluates some script against the current Binding and returns the result.
    public Object evaluate(String scriptText, String fileName, String codeBase) {
        return null;
    }
    //    Evaluates some script against the current Binding and returns the result
    public Object evaluate(URI uri) {
        return null;
    }



    //Parses the given script and returns it ready to be run.
    public Script parse(File file) {
        return null;
    }
    //Parses the given script and returns it ready to be run
    public Script parse(GroovyCodeSource codeSource) {
        return null;
    }
    //Parses the given script and returns it ready to be run
    public Script parse(Reader reader, String fileName) {
        return null;
    }
    //Parses the given script and returns it ready to be run
    public Script parse(Reader in) {
        return null;
    }
    //Parses the given script and returns it ready to be run
    public Script parse(String scriptText) {
        return null;
    }
    public Script parse(String scriptText, String fileName) {
        return null;
    }
    //Parses the given script and returns it ready to be run
    public Script parse(URI uri) {
        return null;
    }
}
