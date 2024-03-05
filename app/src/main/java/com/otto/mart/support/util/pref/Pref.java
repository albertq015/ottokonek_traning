package com.otto.mart.support.util.pref;

import android.content.Context;

/**
 * Created by Henra Setia Nugraha on 10/25/2017.
 */

public class Pref {

    public static Pref pref;
    private PrefHelper helper;

    public static Pref getInstance() {
        if (pref == null) {
            synchronized (Pref.class) {
                if (pref == null) {
                    pref = new Pref();
                }
            }
        }
        return pref;
    }

    private void setPrefHelper(Context context, String preferenceName) {
        if (helper == null) {
            helper = new PrefHelper(context, preferenceName);
        }
    }

    private PrefHelper getPrefHelper() {
        return helper;
    }

    public static void setPreference(Context context, String preferenceName) {
        getInstance().setPrefHelper(context, preferenceName);
    }

    public static PrefHelper getPreference() {
        return getInstance().getPrefHelper();
    }
}
