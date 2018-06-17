package org.sapl.commons.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class StringUtils {

    public static String encryptDES(String value, String secretKey) throws Exception {
        byte[] utf8 = value.getBytes("UTF8");
        Cipher ecipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey.getBytes(), "DES"));
        byte[] enc = ecipher.doFinal(utf8);
        return new sun.misc.BASE64Encoder().encode(enc);
    }

    public static String decryptDES(String value, String secretKey) throws Exception {
        Cipher dcipher = Cipher.getInstance("DES");
        dcipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey.getBytes(), "DES"));
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(value);
        byte[] utf8 = dcipher.doFinal(dec);
        return new String(utf8, "UTF8");
    }

}
