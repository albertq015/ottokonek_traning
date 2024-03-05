package id.ottodigital.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by Henra Setia Nugraha on 10/25/2017.
 */

public class PrefHelper {
    private SharedPreferences sharedPreferences;

    public PrefHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("dasdadasd", Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public void putString(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
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
