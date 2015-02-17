package org.ojbc.util.helper;

/**
 * See here: http://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash
 * 
 * @author yogeshchawla
 *
 */
public class Hash {
    /**
     * 
     * @param txt, text in plain format
     * @param hashType MD5 OR SHA1
     * @return hash in hashType 
     */
    public static String getHash(String txt, String hashType) {
        try {
                    java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
                    byte[] array = md.digest(txt.getBytes());
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < array.length; ++i) {
                        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
                 }
                    return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
                //error action
            }
            return null;
    }

    public static String md5(String txt) {
        return Hash.getHash(txt, "MD5");
    }

    public static String sha1(String txt) {
        return Hash.getHash(txt, "SHA1");
    }

    public static String sha256(String txt) {
        return Hash.getHash(txt, "SHA-256");
    }

    
}