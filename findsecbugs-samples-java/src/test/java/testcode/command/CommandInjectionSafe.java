package testcode.command;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.WindowsCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandInjectionSafe {

    public void safeEmptyConstructor() {
        new ProcessBuilder();
    }

    public void safeConstructorArray() {
        new ProcessBuilder("ls","-la");
        String[] cmd =new String[] {"ls","-la"};
        new ProcessBuilder(cmd);
    }

    public void safeConstructorList() {
        List<String> cmd1 = new ArrayList<String>();
        cmd1.add("ls");
        cmd1.add("-la");
        new ProcessBuilder(cmd1);

        List<String> cmd2 = Arrays.asList("ls","-la");
        new ProcessBuilder(cmd2);
    }

    public void safeCommandMethodArray() {
        new ProcessBuilder().command("ls", "-la");
        String[] cmd = new String[] {"ls","-la"};
        new ProcessBuilder().command(cmd);
    }

    public void safeCommandMethodList() {
        List<String> cmd1 = new ArrayList<String>();
        cmd1.add("ls");
        cmd1.add("-la");
        new ProcessBuilder().command(cmd1);

        List<String> cmd2 = Arrays.asList("ls","-la");
        new ProcessBuilder().command(cmd2);
    }

    public void safeCommandEcoded(String input) {
        String cmd = "ls "+ ESAPI.encoder().encodeForOS(new WindowsCodec() , input);
        new ProcessBuilder().command(cmd.split(" "));
    }

}
