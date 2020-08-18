package testcode.unicode;

import java.net.IDN;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Pattern;

public class ImproperUnicodeSample {

    public static void main(String[] args) throws URISyntaxException {
        stringNormalizationSuite();
        caseMappingSuite();
    }

    public static void stringNormalizationSuite() throws URISyntaxException {
        String host1 = "https://www.evil.c\u2100.microsoft.com";
        String host2 = "https://www.evil.c\u2101.microsoft.com";
        String host3 = "https://www.evil.c\u2105.microsoft.com";
        String host4 = "https://www.evil.c\u2106.microsoft.com";
        String host5 = "https://www.evil.c\u210E.microsoft.com";
        String host6 = "https://www.evil.c\u2121.microsoft.com";
        String host7 = "https://\u2116dejs.org";
        String host8 = "https://montrehac\u212A.ca";
        String host9 = "https://evil.ca\uff0f.microsoft.com";

        for(String h : Arrays.asList(host1,/*host2,host3,host4,host5,host6,*/ host7,host8,host9)) {
            System.out.println("~~~~~");
            System.out.println("Original URL : "+h);
            System.out.println("");
            System.out.println("Normalizer NFKC: "+Normalizer.normalize(h, Normalizer.Form.NFKC)); //RISKY!
            System.out.println("Normalizer NFKD: "+Normalizer.normalize(h, Normalizer.Form.NFKD)); //RISKY!
            System.out.println("Normalizer NFC: "+Normalizer.normalize(h, Normalizer.Form.NFC)); //RISKY!
            System.out.println("Normalizer NFD: "+Normalizer.normalize(h, Normalizer.Form.NFD)); //RISKY!
            System.out.println("URL.toASCIIString(): "+new URI(h).toASCIIString()); //RISKY!
            System.out.println("URL.toString(): "+new URI(h).toString());
            System.out.println("Utils.encode(): "+Utils.encode(h)); //RISKY! (not covered yet..)
            System.out.println("IDN.toASCII(): "+ IDN.toASCII(h)); //RISKY!
        }
    }

    public static void caseMappingSuite() {
        System.out.println("Case Mapping 1: "+("hac\u212A".matches("(?i:.*hack.*)"))); //OK
        System.out.println("Case Mapping 2: "+(Pattern.compile("hack", Pattern.CASE_INSENSITIVE).matcher("hac\u212A").find())); //OK

        System.out.println("Case Mapping 1: "+("ADM\u0131N".matches("(?i:.*ADMIN.*)"))); //OK
        System.out.println("Case Mapping 2: "+(Pattern.compile("ADMIN", Pattern.CASE_INSENSITIVE).matcher("ADM\u0131N").find())); //OK

        System.out.println("Case Mapping 3: "+("HACK".equalsIgnoreCase("HAC\u212A"))); //RISKY!
        System.out.println("Case Mapping 4: "+("ADM\u0131N".toUpperCase().equals("ADMIN"))); //RISKY!
        System.out.println("Case Mapping 5: "+("hac\u212A".toLowerCase().equals("hack"))); //RISKY!

    }

}
