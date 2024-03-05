package com.otto.mart.presenter.dao;

import app.beelabs.com.codebase.base.BaseDao;
import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.api.OttoKonekAPI;
import retrofit2.Callback;

public class EtcDao extends BaseDao implements GLOBAL {
    public EtcDao(Object obj) {
        super(obj);
    }

    public void securityQuestionDAO(Callback callback) {
        API.SecurityQuestionAPI(OttoMartApp.getContext(), callback);
    }

    public void getProvinceDao(Callback callback) {
        API.GetProvinceAPI(OttoMartApp.getContext(), 2, callback);
    }

    public void getCityDao(long provinceid, Callback callback) {
        API.GetCityAPI(OttoMartApp.getContext(), provinceid, callback);
    }

    public void getDistrictDao(long cityid, Callback callback) {
        API.GetDistrictAPI(OttoMartApp.getContext(), cityid, callback);
    }

    public void getVillageDao(long districtid, Callback callback) {
        API.GetVillageAPI(OttoMartApp.getContext(), districtid, callback);
    }

    public void getBankDao(Callback callback) {
        API.GetBankAPI(OttoMartApp.getContext(), callback);
    }

    public void getBusinessDao(Callback callback) {
        API.GetBusinessCategoryAPI(OttoMartApp.getContext(), callback);
    }

    public void getVersionDao(Callback callback) {
        API.GetVersion(OttoMartApp.getContext(), callback);
    }

    public void versionCheckDao(Callback callback) {
        OttoKonekAPI.versionCheck(OttoMartApp.getContext(), callback);
    }
}
