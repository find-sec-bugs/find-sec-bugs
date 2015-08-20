package testcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class CommandInjection {

    public static void main(String[] args) throws IOException {

        String input = args.length > 0 ? args[0] : ";cat /etc/passwd";

        List<String> cmd = Arrays.asList("ls", "-l", input);

        //Runtime exec()
        Runtime r = Runtime.getRuntime();

        r.exec("ls -l " + input);

        r.exec(cmd.toArray(new String[cmd.size()]));

        //ProcessBuilder

        new ProcessBuilder()
                .command("ls", "-l", input)
                .start();

        new ProcessBuilder()
                .command(cmd)
                .start();
    }

    public void bad(String tainted) throws IOException {
        StringBuilder builder = new StringBuilder("<" + tainted + ">");
        builder.insert(3, tainted).append("");
        builder.reverse();
        StringBuilder builder2 = new StringBuilder("xxx");
        builder2.append(builder);
        String safe = "yyy";
        String unsafe = safe.replace("y", builder2.toString());
        Runtime.getRuntime().exec(unsafe.toLowerCase().substring(1).intern());
    }
    
    public void good() throws IOException {
        String hardcoded = "constant";
        StringBuilder builder = new StringBuilder("<" + hardcoded + ">");
        builder.insert(3, hardcoded).append("");
        builder.reverse();
        StringBuilder builder2 = new StringBuilder("xxx");
        builder2.append(builder);
        String safe = "yyy";
        String unsafe = safe.replace("y", builder2.toString());
        Runtime.getRuntime().exec(unsafe.toLowerCase().substring(1).intern());
    }
    
    public void badWithException() throws Exception {
        String data = "";
        File file = new File("C:\\data.txt");
        FileInputStream streamFileInput;
        InputStreamReader readerInputStream;
        BufferedReader readerBuffered;
        try {
            streamFileInput = new FileInputStream(file);
            readerInputStream = new InputStreamReader(streamFileInput, "UTF-8");
            readerBuffered = new BufferedReader(readerInputStream);
            data = readerBuffered.readLine();
        } catch (IOException ex) {
        }
        Runtime.getRuntime().exec(data);
    }
    
    public void badInterMethod() throws Exception {
        Runtime.getRuntime().exec(taintSource());
    }
    
    public void goodInterMethod() throws Exception {
        Runtime.getRuntime().exec(safeSource());
    }
    
    public void badWithTaintSink() throws Exception {
        taintSink("safe", System.getenv("x"));
    }
    
    private void taintSink(String param1, String param2) throws Exception {
        Runtime.getRuntime().exec(param1 + " safe " + param2);
    }
    
    public void badWithDoubleTaintSink() throws Exception {
        taintSinkTransfer(System.getenv("y"));
    }
    
    public void taintSinkTransfer(String str) throws Exception {
        taintSink2(str.toLowerCase());
    }
    
    public static void taintSink2(String param) throws Exception {
        Runtime.getRuntime().exec(param);
    }
    
    public void badCombo() throws Exception {
        String str = taintSourceDouble().toUpperCase();
        str = str.concat("aaa" + "bbb");
        comboSink(new StringBuilder(str).substring(1));
    }
    
    public void comboSink(String str) throws Exception {
        Runtime.getRuntime().exec(str);
    }
    
    public String taintSource() throws Exception {
        File file = new File("C:\\data.txt");
        FileInputStream streamFileInput;
        InputStreamReader readerInputStream;
        BufferedReader readerBuffered;
        streamFileInput = new FileInputStream(file);
        readerInputStream = new InputStreamReader(streamFileInput, "UTF-8");
        readerBuffered = new BufferedReader(readerInputStream);
        return readerBuffered.readLine();
    }
    
    public String taintSourceDouble() throws Exception {
        return taintSource() + safeSource();
    }
    
    public String safeSource() {
        return "not " + "tainted";
    }
}
