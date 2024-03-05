package com.otto.mart.presenter.dao;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Request.TfWalletConfRequestModel;
import com.otto.mart.model.APIModel.Request.TfWalletInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.TransferToBankConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.TransferToBankInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.history.HistoryDetailRequest;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class WalletDao extends BaseDao {
    public WalletDao(Object obj) {
        super(obj);
    }

    public void getWalletData(Callback callback) {
        API.GetWalletDataAPI(OttoMartApp.getContext(), callback);
    }

    public void emoneySummary(Callback callback) {
        OttofinAPI.emoneySummary(OttoMartApp.getContext(), callback);
    }

    public void balance(Callback callback) {
        OttoKonekAPI.balance(OttoMartApp.getContext(), callback);
    }

    public void getTfWalletInquiry(TfWalletInquiryRequestModel model, Callback callback) {
        API.GetTfWalletInquiry(OttoMartApp.getContext(), model, callback);
    }

    public void getTfWalletConf(TfWalletConfRequestModel model, Callback callback) {
        API.GetTfWalletConf(OttoMartApp.getContext(), model, callback);
    }

    public void getTfBankInquiry(TransferToBankInquiryRequestModel requestModel, Callback callback) {
        API.GetTfBankInquiry(OttoMartApp.getContext(), requestModel, callback);
    }

    public void getTfBankConf(TransferToBankConfirmationRequestModel requestModel, Callback callback) {
        API.GetTfBankConf(OttoMartApp.getContext(), requestModel, callback);
    }

    public void getWalletType(Callback callback) {
        API.GetWalletType(OttoMartApp.getContext(), callback);
    }

    public void getWalletHistory(String accountNumber, String dateFrom, String dateTo, int limit, int page, Callback callback) {
        OttoKonekAPI.bankHistory(OttoMartApp.getContext(), accountNumber, dateFrom, dateTo, limit, page, callback);
    }

    public void ottoCashHistory(String dateFrom, String dateTo, int page, Callback callback) {
        OttofinAPI.ottoCashHistory(OttoMartApp.getContext(), dateFrom, dateTo, page, callback);
    }

    public void historyDetail(HistoryDetailRequest historyDetailRequest, Callback callback) {
        OttofinAPI.historyDetail(OttoMartApp.getContext(), historyDetailRequest, callback);
    }
}