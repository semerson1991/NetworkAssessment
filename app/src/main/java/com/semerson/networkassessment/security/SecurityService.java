package com.semerson.networkassessment.security;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class SecurityService {


    public static String decrypt(String key, String initVector, String encrypted) {
        try {

          //  String encrypted = "pCgzZsH3+bFZYsMVl0I6ycKIOqQEDyNnJdJSGqGz9d+JanUXodLLRZcSQzZSWjkJzm4RmTphNWc+ZfXuKm3MyyuAyQJFI/q1mKGAp3HHxx5LaLncpGDkSduIdyquu6/xbMhR9o42O0sIX7YSRd49WKmB9oxfQdMnqly6VD7KbHpDPOYb4B+YPbcYIV7I77yAO0hl7EbTLHSKgPotUwlf7eRtdBWbVx4nY1y60i9d3wSYq/IxlPoSc94uhDsBmLts+8kWjHBvh6ZJKBbxcNO7wg==";

            byte[] b64decoded = Base64.decode(encrypted.getBytes(), Base64.DEFAULT);

            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec("1234567890123456".getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted.getBytes(), Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
