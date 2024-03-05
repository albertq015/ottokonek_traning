package com.otto.mart.presenter.dao;

import android.content.Context;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Request.donasi.DonasiInquiryRequest;
import com.otto.mart.model.APIModel.Request.donasi.DonasiPaymentRequest;
import com.otto.mart.model.APIModel.Request.donasi.DonasiQrStringRequest;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class DonasiDao extends BaseDao {

    private Context context;

    public DonasiDao(Object obj) {
        super(obj);
    }

    public DonasiDao(Object obj, Context context) {
        super(obj);
        this.context = context;
    }

    public void billerProductList(String productName, Callback callback) {
        OttofinAPI.billerProductListDonasi(OttoMartApp.getContext(), productName, callback);
    }

    public void billerInquiry(DonasiInquiryRequest requestModel, Callback callback) {
        OttofinAPI.billerInquiry(OttoMartApp.getContext(), requestModel, callback);
    }

    public void billerQrString(DonasiQrStringRequest requestModel, Callback callback) {
        OttofinAPI.billerQrString(OttoMartApp.getContext(), requestModel, callback);
    }

    public void billerPayment(DonasiPaymentRequest requestModel, Callback callback) {
        OttofinAPI.billerPayment(OttoMartApp.getContext(), requestModel, callback);
    }

    public void billerCheckStatusQrPayment(DonasiPaymentRequest requestModel, Callback callback) {
        OttofinAPI.billerCheckStatusQrPayment(OttoMartApp.getContext(), requestModel, callback);
    }
}