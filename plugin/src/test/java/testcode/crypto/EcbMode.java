package testcode.crypto;

import javax.crypto.Cipher;

import sun.security.jgss.SunProvider;

public class EcbMode {

	public static void main( String[] args ) throws Exception {

		//Note : Not a realistic code sample (no encryption occurs)

		Cipher.getInstance( "AES/CBC/NoPadding");
		Cipher.getInstance( "AES/CBC/PKCS5Padding", "SunJCE" );
		Cipher.getInstance( "AES/ECB/NoPadding", "IBMJCE" );
		Cipher.getInstance( "AES/ECB/PKCS5Padding", SunProvider.INSTANCE );
		Cipher.getInstance( "DES/CBC/NoPadding", SunProvider.INSTANCE  );
		Cipher.getInstance( "DES/CBC/PKCS5Padding" );
		Cipher.getInstance( "DES/ECB/NoPadding" );
		Cipher.getInstance( "DES/ECB/PKCS5Padding" );
		Cipher.getInstance( "DESede/CBC/NoPadding" );
		Cipher.getInstance( "DESede/CBC/PKCS5Padding" );
		Cipher.getInstance( "DESede/ECB/NoPadding" );
		Cipher.getInstance( "DESede/ECB/PKCS5Padding" );
		Cipher.getInstance( "RSA/ECB/PKCS1Padding" );
		Cipher.getInstance( "RSA/ECB/OAEPWithSHA-1AndMGF1Padding" );
		Cipher.getInstance( "RSA/ECB/OAEPWithSHA-256AndMGF1Padding" );
	}
}
