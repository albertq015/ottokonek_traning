package com.otto.mart.support.util.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Base64;

import java.security.MessageDigest;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import app.beelabs.com.codebase.base.BaseActivity;

public class SharePreferenceEncryptor extends BaseActivity {

    public static final String strNamaPref = "id.ottodigital.pref";

    public static final byte[] KEYENC = "OTTODIGITALKEY01".getBytes();

    //-------------------------------------------------------------------------------------------->>> Fungsi enkripsi
    public static String ProsesEnkrip(String RawData) {
        return wrap(RawData, keygen());
    }
    //--------------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------------------->>> Keygen
    public static String keygen() {
        String gkey = "", lCode, rHead = "", charArray;
        int i = 0, length;
        Random rand;

        charArray = "zyxwvutsrqponmlkjihgfedcba0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()";
        rand = new Random();

        length = random_int(10, 15);
        lCode = Integer.toHexString(length);

        for (i = 0; i <= 4; i++)
            rHead = rHead + charArray.charAt(random_int(0, 71));

        for (i = 0; i <= length - 1; i++)
            gkey = gkey + charArray.charAt(random_int(0, 71));

        gkey = lCode + rHead + gkey;
        return gkey;
    }

    public static int random_int(int Min, int Max) {
        return (int) (Math.random() * (Max - Min)) + Min;
    }
    //------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------->>>>	AES, EncryptByteArray
    public static String EncryptByteArray(byte[] array, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String encryptedString = new String(Base64.encode(cipher.doFinal(array), Base64.DEFAULT));
        return encryptedString;
    }
    //--------------------------------------------------------------------------------------------------------

    //---------------------------------------------------------------------------->>>>>> AES, decryptByteArray
    public static byte[] decryptByteArray(String strToDecrypt, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(Base64.decode(strToDecrypt.getBytes(), Base64.DEFAULT));
    }
    //---------------------------------------------------------------------------------------------------------

    //---------------------------------------------------------------------------->>>>
    public static String wrap(String data, String vKey) {
        //---------------------Wrap Declare-------------------------
        int intLen;
        String strTrueKey, strEncrypted, strWrapped = "", strvKey2, s;
        String strWrapped64 = "";
        byte[] bytesOfMessage, thedigest;
        MessageDigest mdMD5;
        char c;
        //----------------------------------------------------------

        strvKey2 = vKey;
        c = vKey.charAt(0);
        s = Character.toString(c);
        vKey = s;

        try {
            intLen = Integer.parseInt(vKey, 16);
            strTrueKey = strvKey2.substring(6, 6 + intLen);
            mdMD5 = MessageDigest.getInstance("MD5");
            bytesOfMessage = strTrueKey.getBytes("UTF-8");
            thedigest = mdMD5.digest(bytesOfMessage);

            strEncrypted = EncryptByteArray(data.getBytes("UTF-8"), thedigest);
            strWrapped = strvKey2 + strEncrypted;
            strWrapped64 = new String(Base64.encode(strWrapped.getBytes(), Base64.DEFAULT));
        } catch (Exception e) {
        }

        return strWrapped64;
    }

    //---------------------------------------------------------------------------------------------------------
    public static String unwrap(String data) {
        //---------------------Wrap Declare-------------------------
        int intLen;
        String strTrueKey, s, data2;
        String strUnWrapped64 = "";
        byte[] bytesOfMessage, thedigest;
        MessageDigest mdMD5;
        char c;

        String strBase64 = new String(Base64.decode(data.getBytes(), Base64.DEFAULT));

        data2 = strBase64;
        byte[] bytesOfDecrypted;
        c = strBase64.charAt(0);
        s = Character.toString(c);

        try {
            intLen = Integer.parseInt(s, 16);
            strTrueKey = strBase64.substring(6, 6 + intLen);
            mdMD5 = MessageDigest.getInstance("MD5");
            bytesOfMessage = strTrueKey.getBytes("UTF-8");
            thedigest = mdMD5.digest(bytesOfMessage);

            String datanya = data2.substring(6 + intLen);

            bytesOfDecrypted = decryptByteArray(datanya, thedigest);
            strUnWrapped64 = new String(bytesOfDecrypted, "UTF-8");
        } catch (Exception e) {
        }

        return strUnWrapped64;
    }

    /*
        //************************** Bitmap to ByteArray **************************
        public static byte[] bitmapToByteArr(Bitmap bitmap)
        {
            int bytes = bitmap.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            bitmap.copyPixelsToBuffer(buffer);
            byte[] array = buffer.array();
            return array;
        }

        //************************** ByteArray to Bitmap **************************
        public static Bitmap byteArrToBitmap(byte[] bytes)
        {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            BitmapFactory.Options o = new BitmapFactory.Options();
            return BitmapFactory.decodeStream(inputStream, null, o);
        }
    */

    public static String DeviceTipe(Context context) {
        String strJenisDevice;

        // Android Tablet = 0, Android Smartphone = 1
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE)
            strJenisDevice = "5";
        else
            strJenisDevice = "4";

        return strJenisDevice;
    }

//    public static String DeviceInfo(Context context, int iKeperluan)
//    {
//        String tmDevice, androidId, tmSerial;
//
//        try
//        {
//            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//         //   tmDevice = "" + telephonyManager.getDeviceId();
//
//            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//
//            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmDevice.hashCode());
//            tmSerial = deviceUuid.toString();
//        }
//        catch (Exception e)
//        {
//            return "Device not authorized";
//        }
//
//        if(iKeperluan == 0)
//            return tmDevice + "#" + androidId + "#" + tmSerial;
//        else
//        if(iKeperluan == 1)
//            return tmDevice;
//        else
//        if(iKeperluan == 2)
//            return androidId;
//        else
//            return tmSerial;
//    }
//

    // Shared Preference
    public static void storeToSharedPref(final Context context, String value, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(strNamaPref, Context.MODE_PRIVATE).edit();
        // add encrypt // add encrypt

        editor.putString(key, value).commit();
    }

    public static void storeToSharedPref(final Context context, long value, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(strNamaPref, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value).commit();
    }

    public static void storeToSharedPref(final Context context, boolean value, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(strNamaPref, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value).commit();
    }

    public static void storeToSharedPref(final Context context, int value, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(strNamaPref, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value).commit();
    }

    public static String getFromSharedPref(final Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(strNamaPref, Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static long getLongFromSharedPref(final Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(strNamaPref, Context.MODE_PRIVATE);
        return prefs.getLong(key, 99);
    }

    public static boolean getBooleanFromSharedPref(final Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(strNamaPref, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static int getIntFromSharedPref(final Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(strNamaPref, Context.MODE_PRIVATE);
        return prefs.getInt(key, 99);
    }

    public static String executeEncrypt(String value) {
        String result = "";
        try {
            result = EncryptByteArray(value.getBytes(), SharePreferenceEncryptor.KEYENC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeDecrypt(String value) {
        String result = "";
        try {
            byte[] decyp = decryptByteArray(value, SharePreferenceEncryptor.KEYENC);
            result = new String(decyp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

