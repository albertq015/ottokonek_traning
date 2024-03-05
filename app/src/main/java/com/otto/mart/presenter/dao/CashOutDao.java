package com.otto.mart.presenter.dao;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.model.APIModel.Request.cashOut.CashOutInputConfirmationRequest;
import com.otto.mart.model.APIModel.Request.cashOut.CashOutInputRequest;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class CashOutDao extends BaseDao {

    public CashOutDao(Object obj) {
        super(obj);
    }

    public void bankAccountList(Callback callback) {
        OttoKonekAPI.bankAccountList(OttoMartApp.getContext(), callback);
    }

    public void cashOutInput(CashOutInputRequest request, Callback callback) {
        OttoKonekAPI.cashOutInput(OttoMartApp.getContext(), request, callback);
    }

    public void cashOutInputConfirmation(CashOutInputConfirmationRequest request, Callback callback) {
        OttoKonekAPI.cashOutInputConfirmation(OttoMartApp.getContext(), request, callback);
    }

    public void cashOutList(Callback callback) {
        OttoKonekAPI.cashOutList(OttoMartApp.getContext(), callback);
    }
}