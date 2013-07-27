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

}
