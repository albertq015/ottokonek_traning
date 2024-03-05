package com.otto.mart.presenter.dao;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Request.OmsetHistoryRequestModel;
import com.otto.mart.model.APIModel.Request.OmzetSaldoRequestModel;
import com.otto.mart.model.APIModel.Request.PayQrInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.PaymentOfflineConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.multibank.TransferMultiBankConfrimRequest;
import com.otto.mart.model.APIModel.Request.multibank.ValidateQrRequest;
import com.otto.mart.model.APIModel.Request.payment.AlfamartPurchaseRequest;
import com.otto.mart.model.APIModel.Request.payment.AlfamartStatusRequest;
import com.otto.mart.model.APIModel.Request.payment.IndomaretQrPurchaseRequestModel;
import com.otto.mart.model.APIModel.Request.payment.IndomaretQrStatusRequestModel;
import com.otto.mart.model.APIModel.Request.qr.QrPaymentRequest;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class TransactionDao extends BaseDao {
    public TransactionDao(Object obj) {
        super(obj);
    }

    public void getOmsetHistoryOld(BaseActivity ac, OmsetHistoryRequestModel model, Callback callback) {
        API.getOmsetHistory(ac, model, callback);
    }

    public void getOmsetHistoryOld(OmsetHistoryRequestModel model, Callback callback) {
        API.getOmsetHistory(OttoMartApp.getContext(), model, callback);
    }

    public void getOmsetHistory(String dateFrom, String dateTo,
                                String paymentMethod, String transactionType, int limit, int page, Callback callback) {
        OttoKonekAPI.revenueHistory(OttoMartApp.getContext(), dateFrom, dateTo,
                paymentMethod, transactionType, limit, page, callback);
    }


    public void getHistoryAccount(String accountNumber, String dateFrom, String dateTo, int limit, int page, Callback callback) {
        OttoKonekAPI.revenueHistoryAccount(OttoMartApp.getContext(), accountNumber, dateFrom, dateTo,
                limit, page, callback);
    }


    public void transOmzetToSaldo(BaseActivity ac, OmzetSaldoRequestModel model, Callback callback) {
        API.TransOmzetToSaldo(ac, model, callback);
    }

    public void transOmzetToWallet(BaseActivity ac, OmzetSaldoRequestModel model, Callback callback) {
        OttofinAPI.transferOmzetToWallet(ac, model, callback);
    }

    public void getOmzetStat(Callback callback) {
        API.GetOmzetStat(OttoMartApp.getContext(), callback);
    }

    public void omzetStatus(Callback callback) {
        OttofinAPI.omzetStatus(OttoMartApp.getContext(), callback);
    }

    public void getPayQrInquiry(PayQrInquiryRequestModel model, Callback callback) {
        API.GetPayQrInquiry(OttoMartApp.getContext(), model, callback);
    }

    public void payQrInquiry(PayQrInquiryRequestModel model, Callback callback) {
        OttofinAPI.payQrInquiry(OttoMartApp.getContext(), model, callback);
    }

    public void getPayQrPayment(PaymentOfflineConfirmationRequestModel model, Callback callback) {
        API.GetPayQrPayment(OttoMartApp.getContext(), model, callback);
    }

    public void payQrPayment(PaymentOfflineConfirmationRequestModel model, Callback callback) {
        OttofinAPI.payQrPayment(OttoMartApp.getContext(), model, callback);
    }

    public void getIndomaretQrPurchase(IndomaretQrPurchaseRequestModel model, Callback callback) {
        API.IndomaretQrPurchase(OttoMartApp.getContext(), model, callback);
    }

    public void getIndomaretQrPurchaseCancel(IndomaretQrPurchaseRequestModel model, Callback callback) {
        API.IndomaretQrPurchaseCancel(OttoMartApp.getContext(), model, callback);
    }

    public void getIndomaretQrPurchaseStatus(IndomaretQrStatusRequestModel model, Callback callback) {
        API.IndomaretQrPurchaseStatus(OttoMartApp.getContext(), model, callback);
    }

    public void getAlfamartQrPurchase(AlfamartPurchaseRequest model, Callback callback) {
        API.AlfamartQrPurchase(OttoMartApp.getContext(), model, callback);
    }

    public void getAlfamartQrPurchaseStatus(AlfamartStatusRequest model, Callback callback) {
        API.AlfamartQrPurchaseStatus(OttoMartApp.getContext(), model, callback);
    }


    /**
     * OttoKonek
     */

    public void revenue(Callback callback) {
        OttoKonekAPI.revenue(OttoMartApp.getContext(), callback);
    }

    public void payQrQrisInquiry(PayQrInquiryRequestModel model, Callback callback) {
        OttoKonekAPI.payQrQrisInquiry(OttoMartApp.getContext(), model, callback);
    }

    public void payQrValidate(ValidateQrRequest model, Callback callback) {
        OttoKonekAPI.payQrValidate(OttoMartApp.getContext(), model, callback);
    }

    public void payQrPaymentNew(QrPaymentRequest model, Callback callback) {
        OttoKonekAPI.payQrPayment(OttoMartApp.getContext(), model, callback);
    }

    public void TransferBank(TransferMultiBankConfrimRequest model, Callback callback) {
        OttoKonekAPI.confrimTransferBank(OttoMartApp.getContext(), model, callback);
    }
}

