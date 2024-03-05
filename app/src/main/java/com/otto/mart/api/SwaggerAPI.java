package com.otto.mart.api;

import android.content.Context;
import android.content.Intent;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.otto.mart.BuildConfig;
import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.SystemUtil;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.settings.LanguageIdConverter;

import java.util.HashMap;
import java.util.Map;

import app.beelabs.com.codebase.base.BaseApi;

public class
SwaggerAPI extends BaseApi implements GLOBAL {

    private static Map<String, String> initHeader() {
        if (SessionManager.getAccessToken() != null &&
                SessionManager.isLoggedIn() &&
                SessionManager.getAccessToken().equals("TokenNaN")) {
            SessionManager.logout();
            OttoMartApp.getContext().startActivity(new Intent(OttoMartApp.getContext(), LoginActivity.class));
        }
        Map<String, String> map = new HashMap<>();
        map.put("Device-Id", SystemUtil.getDeviceId(OttoMartApp.getContext()));
        map.put("Language-Id", new LanguageIdConverter().getLanguageId());
        return map;
    }

    private static Map<String, String> initAccessTokenHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer " + SessionManager.getAccessToken());
//        map.put("Authorization", "Bearer ");
        return map;
    }

    private static SwaggerService initApiDomain(Context context) {
        getInstance().setApiDomain(BuildConfig.OTTOKONEK_SERVER_URL);
        return (SwaggerService) getInstance().setupApi(OttoMartApp.getAppComponent(),
                SwaggerService.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }
}