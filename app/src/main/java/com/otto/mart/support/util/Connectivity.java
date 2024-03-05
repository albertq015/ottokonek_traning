package com.otto.mart.support.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by macaris on 9/14/16.
 */
public class Connectivity {

    public static boolean isConnected(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        boolean isConnected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        return isConnected;
    }
}