package com.otto.mart.presenter.dao;

import com.otto.mart.api.API;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class MerchantDao extends BaseDao {

    public MerchantDao(BaseActivity ac) {
        super(ac);
    }

    public void getNearbyMerchantDao(BaseActivity ac, String accessToken, String search, String type, double latitude,
                                     double longitude, int limit, int offset, Callback callback) {
        API.getNearbyMerchant(ac, accessToken, search, type, latitude, longitude, limit, offset, callback);
    }
}