package com.otto.mart.presenter.dao;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Request.bogasari.BogasariInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.bogasari.BogasariPaymentRequestModel;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class BogasariDao extends BaseDao {
    public BogasariDao(Object obj) {
        super(obj);
    }

    public void getBogasariProducts(String merchantId, Callback callback) {
        OttofinAPI.bogasariProducts(OttoMartApp.getContext(), merchantId, callback);
    }

    public void getBogasariInquiry(BogasariInquiryRequestModel model, Callback callback) {
        OttofinAPI.bogasariInquiry(OttoMartApp.getContext(), model, callback);
    }

    public void getBogasariPayment(BogasariPaymentRequestModel model, Callback callback) {
        OttofinAPI.bogasariPayment(OttoMartApp.getContext(), model, callback);
    }
}
