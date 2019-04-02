package testcode.googlemaps;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class GoogleMapsSigUtil {

    // Note: Generally, you should store your private key someplace safe
    // and read them into your code

    private static String keyString = "vNIXE0xscrmjlyV-12Nj_BvUPaw=";

    // The URL shown in these examples is a static URL which should already
    // be URL-encoded. In practice, you will likely have code
    // which assembles your URL from user or web service input
    // and plugs those values into its parameters.
    private static String urlString = "http://maps.googleapis.com/maps/api/geocode/json?address=New+York&sensor=false&client=";

    // This variable stores the binary key, which is computed from the string (Base64) key
    private static byte[] key;

    public static void main(String[] args) throws IOException,
            InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        String inputUrl, inputKey = null;

        // For testing purposes, allow user input for the URL.
        // If no input is entered, use the static URL defined above.
        System.out.println("Enter the URL (must be URL-encoded) to sign: ");
        inputUrl = input.readLine();
        if (inputUrl.equals("")) {
            inputUrl = urlString;
        }

        // Convert the string to a URL so we can parse it
        URL url = new URL(inputUrl);

        // For testing purposes, allow user input for the private key.
        // If no input is entered, use the static key defined above.
        System.out.println("Enter the Private key to sign the URL: ");
        inputKey = input.readLine();
        if (inputKey.equals("")) {
            inputKey = keyString;
        }

        GoogleMapsSigUtil signer = new GoogleMapsSigUtil(inputKey);
        String request = signer.signRequest(url.getPath(), url.getQuery());

        System.out.println("Signed URL :" + url.getProtocol() + "://" + url.getHost() + request);
    }

    public GoogleMapsSigUtil(String keyString) throws IOException {
        // Convert the key from 'web safe' base 64 to binary
        keyString = keyString.replace('-', '+');
        keyString = keyString.replace('_', '/');
        System.out.println("Key: " + keyString);
        this.key = Base64.decode(keyString);
    }

    public String signRequest(String path, String query) throws NoSuchAlgorithmException,
            InvalidKeyException, UnsupportedEncodingException, URISyntaxException {

        // Retrieve the proper URL components to sign
        String resource = path + '?' + query;

        // Get an HMAC-SHA1 signing key from the raw key bytes
        SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

        // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(sha1Key);

        // compute the binary signature for the request
        byte[] sigBytes = mac.doFinal(resource.getBytes());

        // base 64 encode the binary signature
        String signature = Base64.encodeBytes(sigBytes);

        // convert the signature to 'web safe' base 64
        signature = signature.replace('+', '-');
        signature = signature.replace('/', '_');

        return resource + "&signature=" + signature;
    }
}