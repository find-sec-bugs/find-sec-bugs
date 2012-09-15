package testcode.crypto;


import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class RsaNoPadding {

    public void rsaCipherOk() throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher.getInstance("RSA/ECB/OAEPWithMD5AndMGF1Padding");
    }

    public void rsaCipherWeak() throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher.getInstance("RSA/NONE/NoPadding");
    }
}
