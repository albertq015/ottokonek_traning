package com.otto.mart.presenter.dao;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.api.OttoKonekAPI;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class BannerDao extends BaseDao {
    public BannerDao(Object obj) {
        super(obj);
    }

    public void getBannerData(Callback callback) {
        OttoKonekAPI.banner(OttoMartApp.getContext(), callback);
    }
}