package com.otto.mart.presenter.dao.olshop;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.api.OlshopAPI;
import com.otto.mart.model.APIModel.Request.olshop.PaymentJurnalRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ReversalRequestModel;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class PaymentDao extends BaseDao {
    public PaymentDao(Object obj) {
        super(obj);
    }

    public void paymentJurnal(PaymentJurnalRequestModel model, Callback callback) {
        OlshopAPI.paymentJurnal(model, callback);
    }

    public void reversalPayment(ReversalRequestModel model, Callback callback) {
        API.reversalPaymentAPI(OttoMartApp.getContext(), model, callback);
    }
}
