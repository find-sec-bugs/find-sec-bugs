package testcode.password;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

public class JschSshPassword {

    //Unsafe
    public static void hashcodedPassword() throws JSchException {
        JSch defaultJSch = new JSch();

        defaultJSch.addIdentity("some/private_key","this_password_is_hardcoded");
        defaultJSch.addIdentity("some/private_key","this_password_is_hardcoded".getBytes());
        defaultJSch.addIdentity("some/private_key","some/public.key","this_password_is_hardcoded".getBytes());
        defaultJSch.addIdentity("",new byte[0],new byte[0],"this_password_is_hardcoded".getBytes());
    }

    //OK
    public static void unknownSourcePassword(String password) throws JSchException {
        JSch defaultJSch = new JSch();

        defaultJSch.addIdentity("some/private_key",password);
        defaultJSch.addIdentity("some/private_key",password.getBytes());
        defaultJSch.addIdentity("some/private_key","some/public.key",password.getBytes());
        defaultJSch.addIdentity("",new byte[0],new byte[0],password.getBytes());
    }
}
