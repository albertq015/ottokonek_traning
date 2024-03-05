package com.otto.mart.support.util.encryption;

import static android.os.Build.*;

public class AesEncryption {

    private static final String KEY = "603deb1015ca71be2b73aef0857d77811f352c073b6108d72d9810a30914dff5";

    public static String encrypt(String text) {
        try {
            // strings encryption
            String encrypted;

            if (VERSION.SDK_INT >= VERSION_CODES.O){
                encrypted = com.github.mervick.aes_everywhere.Aes256.encrypt(text, KEY);
            } else{
                encrypted = com.github.mervick.aes_everywhere.legacy.Aes256.encrypt(text, KEY);
            }

            System.out.println(encrypted);

            return encrypted;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decrypt(String encrypted) {
        try {
            // strings decryption
            String decrypted;

            if (VERSION.SDK_INT >= VERSION_CODES.O){
                decrypted = com.github.mervick.aes_everywhere.Aes256.decrypt(encrypted, KEY);
            } else{
                decrypted = com.github.mervick.aes_everywhere.legacy.Aes256.decrypt(encrypted, KEY);
            }

            System.out.println(decrypted);
            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
