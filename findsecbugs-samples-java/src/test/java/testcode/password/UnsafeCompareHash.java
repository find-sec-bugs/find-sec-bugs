package testcode.password;

import java.security.MessageDigest;
import java.util.Arrays;

public abstract class UnsafeCompareHash {

    public boolean unsafeLogin1(String username, String hash) {

        if(hash.equals(getHash(username))) {
            return true;
        }

        return false;
    }

    public boolean unsafeLogin2(String username, String hash) {

        if(getHash(username).equals(hash)) {
            return true;
        }

        return false;
    }

    public boolean unsafeLogin3(String username, byte[] md5) {

        if(Arrays.equals(getHashBytes(username), md5)) {
            return true;
        }

        return false;
    }

    public boolean unsafeLogin4(String username, byte[] sha1) {

        if(Arrays.equals(sha1, getHashBytes(username))) {
            return true;
        }

        return false;
    }

    public boolean safeLogin1(String username, byte[] hash) {

        if(MessageDigest.isEqual(hash, getHash(username).getBytes())) {
            return true;
        }

        return false;
    }

    /**
     * Function that contains the keyword Share or Shared should not be confused as SHA hash functions.
     */
    public boolean fpKeyword1(String sharedProperty, String info) {

        if(sharedProperty.equals(info)) {
            return true;
        }

        return false;
    }

    public abstract String getHash(String username);
    public abstract byte[] getHashBytes(String username);
    public abstract byte[] getSharedProperty(String username);

}
