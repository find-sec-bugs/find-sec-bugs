package testcode.file.permissions;

import java.io.IOException;

public class CommandExecChmod {
    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec("chmod 777 /opt/appl/script_init.sh");
        Runtime.getRuntime().exec("chmod 777 /opt/appl/script_init.sh",new String[] {});
    }
}
