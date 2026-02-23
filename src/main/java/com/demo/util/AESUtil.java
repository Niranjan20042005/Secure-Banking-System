package com.demo.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {

    private static final String key = "1234567812345678";

    public static String encrypt(String data){
        try{
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(),"AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        }catch(Exception e){
            return null;
        }
    }
}

