package testcode;

import java.io.IOException;
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
}
