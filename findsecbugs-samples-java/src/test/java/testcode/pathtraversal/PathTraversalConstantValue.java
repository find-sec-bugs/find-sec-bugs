package testcode.pathtraversal;

import org.apache.commons.codec.binary.Hex;


import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class PathTraversalConstantValue {

    /**
     * All the call below should be consider safe because their inputs are the combination of :
     *  - Constant values
     *  - SAFE API
     *  - SAFE type (int or long)
     * @throws IOException
     */
    public void safeFileHandle() throws IOException, NoSuchAlgorithmException {
        Locale locale = Locale.getDefault();

        new File(System.getProperty("abc"));
        new File("/tmp/"+ Calendar.getInstance().get(Calendar.YEAR)+"/");
        new File("/tmp/"+ Calendar.getInstance().get(Calendar.MONTH)+"/");
        new File("/tmp/"+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"/");
        new File("/tmp/"+ Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+"/");
        new File("/tmp/"+ Calendar.getInstance().get(Calendar.DAY_OF_YEAR)+"/");
        new File("/tmp/"+ Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale)+"/");
        new File("/tmp/"+ Calendar.getInstance().getDisplayNames(Calendar.DAY_OF_WEEK, Calendar.LONG, locale)+"/");
        new File("c:/" + UUID.randomUUID().toString());

        new File(File.createTempFile("","").getCanonicalPath());
        new File(File.createTempFile("","",null).getCanonicalPath());
        new File(Files.createTempDirectory("").toFile(),"safe.txt");
        new File(Files.createTempDirectory(Paths.get(""),"").toFile(),"safe.txt");
        new File("c:/" + System.currentTimeMillis());
        new File("c:/" + System.nanoTime());

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] out = md.digest("test".getBytes());
        //Typical bytes to hex #1 https://stackoverflow.com/a/41787842/89769
        new File("c:/" + String.format("%032x", new BigInteger(1, out)));
        //Typical bytes to hex #2
        new File(Hex.encodeHexString(out));
        new File(String.valueOf(Hex.encodeHex(out)));
        new File(String.valueOf(Hex.encodeHex(out,true)));
        new File(Hex.encodeHexString(out));
    }
}
