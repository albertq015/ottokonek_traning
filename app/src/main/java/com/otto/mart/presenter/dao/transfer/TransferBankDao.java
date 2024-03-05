package com.otto.mart.presenter.dao.transfer;

import android.content.Context;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Request.transfer.TransferBankInquiryRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferBankPaymentRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferLimitRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferSknRequest;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class TransferBankDao extends BaseDao {

    private Context context;

    public TransferBankDao(Object obj) {
        super(obj);
    }

    public TransferBankDao(Object obj, Context context) {
        super(obj);
        this.context = context;
    }

    public void transferLimit(TransferLimitRequest transferLimitRequest, Callback callback) {
        OttofinAPI.transferLimit(OttoMartApp.getContext(), transferLimitRequest, callback);
    }

    public void transferBankInquiry(TransferBankInquiryRequest requestModel, Callback callback) {
        OttofinAPI.transferBankInquiry(OttoMartApp.getContext(), requestModel, callback);
    }

    public void transferBankPayment(TransferBankPaymentRequest requestModel, Callback callback) {
        OttofinAPI.transferBankPayment(OttoMartApp.getContext(), requestModel, callback);
    }

    public void checkTransferStatus(TransferBankPaymentRequest requestModel, Callback callback) {
        OttofinAPI.checkTransferStatus(OttoMartApp.getContext(), requestModel, callback);
    }

    public void transferSkn(TransferSknRequest requestModel, Callback callback) {
        OttofinAPI.transferSkn(OttoMartApp.getContext(), requestModel, callback);
    }
}
