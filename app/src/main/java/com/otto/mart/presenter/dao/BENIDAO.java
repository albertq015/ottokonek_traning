package com.otto.mart.presenter.dao;

import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Request.GetQrStringRequestModel;
import com.otto.mart.model.APIModel.Request.QrStringRequestModel;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class BENIDAO extends BaseDao implements GLOBAL {
    String API_URL_BNI_SERVICE = "Https://dashboard-merchant.spesolution.com/service/string-builder/request";
//    String API_URL_BNI_SERVICE = "https://fintech-dev.pactindo.com:8443/api/v1/issuer/emvcojson";

    public BENIDAO(Object obj) {
        super(obj);
    }

    public void doGetQrStringBNIDAO(GetQrStringRequestModel requestModel, Callback callback) {
        API.doGetQrString(OttoMartApp.getContext(), API_URL_BNI_SERVICE, requestModel, callback);
    }

    public void doGetQrStringOttoDAO(QrStringRequestModel requestModel, Callback callback) {
        //API.GetQrString(OttoMartApp.getContext(), requestModel, callback);
        OttofinAPI.qrString(OttoMartApp.getContext(), requestModel, callback);
    }

    public void doGetQrStringOttoDAO(Callback callback) {
        API.GetQrString(OttoMartApp.getContext(), callback);
    }

    public void qrString(QrStringRequestModel requestModel, Callback callback) {
        OttoKonekAPI.qrString(OttoMartApp.getContext(), requestModel, callback);
    }
}
