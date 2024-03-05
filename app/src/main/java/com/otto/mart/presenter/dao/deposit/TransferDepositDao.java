package com.otto.mart.presenter.dao.deposit;

import android.content.Context;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Request.bank.AddBankDepositRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferBankInquiryRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferBankPaymentRequest;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class TransferDepositDao extends BaseDao {

    private Context context;

    public TransferDepositDao(Object obj) {
        super(obj);
    }

    public TransferDepositDao(Object obj, Context context) {
        super(obj);
        this.context = context;
    }

    public void bankListDeposit(Callback callback) {
        OttofinAPI.bankListDeposit(OttoMartApp.getContext(), callback);
    }

    public void addBankDeposit(AddBankDepositRequest addBankDepositRequest, Callback callback) {
        OttofinAPI.addBankDeposit(OttoMartApp.getContext(), addBankDepositRequest, callback);
    }

    public void deleteBankDeposit(AddBankDepositRequest addBankDepositRequest, Callback callback) {
        OttofinAPI.deleteBankDeposit(OttoMartApp.getContext(), addBankDepositRequest, callback);
    }

    public void transferDepositInquiry(TransferBankInquiryRequest requestModel, Callback callback) {
        OttofinAPI.transferDepositInquiry(OttoMartApp.getContext(), requestModel, callback);
    }

    public void transferDepositPayment(TransferBankPaymentRequest requestModel, Callback callback) {
        OttofinAPI.transferDepositPayment(OttoMartApp.getContext(), requestModel, callback);
    }

    public void checkTransferDepositStatus(TransferBankPaymentRequest requestModel, Callback callback) {
        OttofinAPI.checkTransferDepositStatus(OttoMartApp.getContext(), requestModel, callback);
    }

}