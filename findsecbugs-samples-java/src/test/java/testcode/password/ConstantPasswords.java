package testcode.password;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.net.PasswordAuthentication;
import java.security.KeyRep;
import java.security.KeyStore;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAMultiPrimePrivateCrtKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.KeyManagerFactory;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosTicket;
import sun.security.provider.DSAPublicKeyImpl;

public class ConstantPasswords {

    private static String PWD1 = "secret4";
    private static char[] PWD2 = {'s', 'e', 'c', 'r', 'e', 't', '5'};
    private char[] PWD3 = {'s', 'e', 'c', 'r', 'e', 't', '5'};
    private static BigInteger big = new BigInteger("1000000");
    private static final byte[] PUBLIC_KEY = new byte[]{1, 2, 3, 4, 5, 6, 7};

    public void bad1() throws Exception {
        char[] passphrase = "secret1".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("keystore"), passphrase);
    }

    public static void bad2() throws Exception {
        final String passphrase = "secret2";
        System.out.println("secret2");
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fs = new FileInputStream("keystore");
        ks.load(fs, passphrase.toCharArray());
    }

    public void bad3() throws Exception {
        char[] passphrase = {'s', 'e', 'c', 'r', 'e', 't', '3'};
        KeyStore.getInstance("JKS").load(new FileInputStream("keystore"), passphrase);
    }

    public void bad4() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("keystore"), PWD1.toCharArray());
    }

    public static void bad5a() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("keystore"), PWD2);
    }

    public void bad5b() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("keystore"), PWD3);
    }

    public void bad6() throws Exception {
        String pwdStr = "secret6";
        char[] pwd1 = pwdStr.toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        char[] pwd2 = pwd1;
        ks.load(new FileInputStream("keystore"), pwd2);
    }

    public void bad7() throws Exception {
        byte[] bytes = new byte[2];
        char[] pwd = "secret7".toCharArray();
        new PBEKeySpec(pwd);
        new PBEKeySpec(pwd, bytes, 1);
        new PBEKeySpec(pwd, bytes, 1, 1);
        PasswordAuthentication auth = new PasswordAuthentication("user", pwd);
        PasswordCallback callback = new PasswordCallback("str", true);
        callback.setPassword(pwd);
        KeyStore.PasswordProtection protection = new KeyStore.PasswordProtection(pwd);
        KerberosKey key = new KerberosKey(null, pwd, "alg");
        KeyManagerFactory.getInstance("").init(null, pwd);
    }

    public void bad8a() throws Exception {
        new DESKeySpec(null); // should not be reported
        byte[] key = {1, 2, 3, 4, 5, 6, 7, 8};
        DESKeySpec spec = new DESKeySpec(key);
        KeySpec spec2 = new DESedeKeySpec(key);
        KerberosKey kerberosKey = new KerberosKey(null, key, 0, 0);
        System.out.println(spec.getKey()[0] + kerberosKey.getKeyType());
        new SecretKeySpec(key, "alg");
        new SecretKeySpec(key, 0, 0, "alg");
        new X509EncodedKeySpec(key);
        new PKCS8EncodedKeySpec(key);
        new KeyRep(null, "alg", "format", key);
        new KerberosTicket(null, null, null, key, 0, null, null, null, null, null, null);
        new DSAPublicKeyImpl(key);
    }

    public void bad8b() {
        byte[] key = "secret8".getBytes();
        System.out.println("something");
        new SecretKeySpec(key, "alg");
    }

    public void bad9() throws SQLException {
        String pass = "secret9";
        Connection connection = DriverManager.getConnection("url", "user", PWD1);
        System.out.println(connection.getCatalog());
        connection = DriverManager.getConnection("url", "user", pass);
        System.out.println(connection.getCatalog());
    }

    public void bad10() throws Exception {
        BigInteger bigInteger = new BigInteger("12345", 5);
        new DSAPrivateKeySpec(bigInteger, null, null, null);
        new DSAPublicKeySpec(bigInteger, null, bigInteger, null); // report once
        new DHPrivateKeySpec(bigInteger, null, null);
        new DHPublicKeySpec(bigInteger, null, null);
        new ECPrivateKeySpec(bigInteger, null);
        new RSAPrivateKeySpec(bigInteger, null);
        new RSAMultiPrimePrivateCrtKeySpec(bigInteger, null, null, null, null, null, null, null, null);
        new RSAPrivateCrtKeySpec(bigInteger, null, null, null, null, null, null, null);
        new RSAPublicKeySpec(bigInteger, null);
        new DSAPublicKeyImpl(bigInteger, null, null, null);
    }

    public void bad11() {
        new DSAPrivateKeySpec(null, null, null, null); // should not be reported
        System.out.println();
        new DSAPrivateKeySpec(big, null, null, null);
    }

    public void bad12() throws Exception {
        byte[] key = "secret8".getBytes("UTF-8");
        BigInteger bigInteger = new BigInteger(key);
        new DSAPrivateKeySpec(bigInteger, null, null, null);
    }

    public void bad13() throws Exception {
        String pwd = null;
        if (PWD2[3] < 'u') { // non-trivial condition
            pwd = "hardcoded";
        }
        if (pwd != null) {
            KeyStore.getInstance("JKS").load( // should be reported
                    new FileInputStream("keystore"), pwd.toCharArray());
        }
    }
    
    public Connection bad14() throws Exception {
        String pwd;
        if (PWD2[2] % 2 == 1) { // non-trivial condition
            pwd = "hardcoded1";
        } else { // different constant but still hard coded
            pwd = "hardcoded2";
        }
        return DriverManager.getConnection("url", "user", pwd);
    }

    public void bad15(io.vertx.core.Vertx vertx) throws Exception {
        String pwd;
        if (PWD2[2] % 2 == 1) { // non-trivial condition
            pwd = "hardcoded1";
        } else { // different constant but still hard coded
            pwd = "hardcoded2";
        }
        io.vertx.ext.web.handler.CSRFHandler.create(vertx, pwd);
    }

    private byte[] pwd4; // not considered hard coded
    private char[] pwd5 = null;
    private char[] pwd6 = new char[7];
    
    public void good1() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("keystore"), getPassword());
    }

    public void good2() throws Exception {
        String pwd = "uiiii".substring(3) + "oo";
        char[] pwdArray = pwd.toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("keystore"), pwdArray);
    }

    public void good3() throws Exception {
        String key = "hard coded";
        key = new String(getPassword()); // no longer hard coded
        String message = "can be hard coded";
        byte[] byteStringToEncrypt = message.getBytes("UTF-8");
        new SecretKeySpec(key.getBytes(), "AES"); // should not report
        byte[] bytes = {0, 0, 7};
        new PBEKeySpec(getPassword(), bytes, 1); // different parameter hard coded
        byte newArray[] = new byte[1024]; // not considered hard coded
        new X509EncodedKeySpec(newArray);
    }
    
    private static char[] getPassword() {
        char[] password = new char[3];
        // some operations to simulate non-constant password
        password[0] = 'x';
        password[1] = 10;
        password[2] = ("o" + "z").charAt(1);
        return password;
    }
}
