package com.otto.mart.support.util.pref;

import android.content.Context;
import android.content.SharedPreferences;

import static com.otto.mart.support.util.pref.SharePreferenceEncryptor.EncryptByteArray;

/**
 * Created by Henra Setia Nugraha on 10/25/2017.
 */

public class PrefHelper {
    private SharedPreferences sharedPreferences;

    public PrefHelper(Context context, String preferenceName) {
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public void putString(String key, String value) {
        try {
            value = EncryptByteArray(value.getBytes(), SharePreferenceEncryptor.KEYENC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getEditor().putString(key, value).commit();
    }

    public String getString(String key) {
        try {
            byte[] decyp = SharePreferenceEncryptor.decryptByteArray(sharedPreferences.getString(key, null), SharePreferenceEncryptor.KEYENC);
            return new String(decyp);
        } catch (Exception e){
            return sharedPreferences.getString(key, null);
        }
    }

    public void putInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public void putLong(String key, long value) {
        getEditor().putLong(key, value).commit();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putFloat(String key, float value) {
        getEditor().putFloat(key, value).commit();
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0f);
    }

    public void remove(String key) {
        getEditor().remove(key).commit();
    }
}
