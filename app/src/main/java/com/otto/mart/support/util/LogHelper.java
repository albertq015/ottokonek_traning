package com.otto.mart.support.util;

import android.util.Log;

import com.otto.mart.BuildConfig;

public class LogHelper {

    public static void showError(String TAG, String message){
        if(isApplicationDebug())
            Log.e(TAG, message);
    }

    private static boolean isApplicationDebug(){
        return BuildConfig.FLAVOR.equals("development");
    }
}
