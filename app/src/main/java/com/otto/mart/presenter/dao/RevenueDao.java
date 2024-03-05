package com.otto.mart.presenter.dao;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.model.APIModel.Request.cashOut.CashOutInputConfirmationRequest;
import com.otto.mart.model.APIModel.Request.cashOut.CashOutInputRequest;
import com.otto.mart.model.APIModel.Request.revenue.WithdrawRevenueRequest;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class RevenueDao extends BaseDao {

    public RevenueDao(Object obj) {
        super(obj);
    }

    public void withdrawRevenuePayment(WithdrawRevenueRequest request, Callback callback) {
        OttoKonekAPI.withdrawRevenuePayment(OttoMartApp.getContext(), request, callback);
    }
}