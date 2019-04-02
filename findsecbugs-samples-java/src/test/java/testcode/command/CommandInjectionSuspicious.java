package testcode.command;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandInjectionSuspicious {

    public void insecureConstructorArray(String input) {
        new ProcessBuilder("ls",input);
        String[] cmd = new String[] {"ls",input};
        new ProcessBuilder(cmd);

    }

    public void insecureConstructorList(String input) {
        List<String> cmd1 = new ArrayList<String>();
        cmd1.add(input);
        new ProcessBuilder(cmd1);

        List<String> cmd2 = Arrays.asList("ls", input);
        new ProcessBuilder(cmd2);
    }

    public void insecureCommandMethodArray(String input) {
        new ProcessBuilder().command("ls", input);
        String[] cmd = new String[] {"ls",input};
        new ProcessBuilder().command(cmd);
    }

    public void insecureCommandMethodList(String input) {
        List<String> cmd1 = new ArrayList<String>();
        cmd1.add(input);
        new ProcessBuilder().command(cmd1);

        List<String> cmd2 = Arrays.asList("ls",input);
        new ProcessBuilder().command(cmd2);
    }
}
