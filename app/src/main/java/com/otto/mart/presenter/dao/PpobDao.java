package com.otto.mart.presenter.dao;

import android.content.Context;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Request.CancelQrTransactionRequestModel;
import com.otto.mart.model.APIModel.Request.FaqTopUpRequest;
import com.otto.mart.model.APIModel.Request.FavoriteAddRequestModel;
import com.otto.mart.model.APIModel.Request.PpobAirInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.PpobAirPaymentRequestModel;
import com.otto.mart.model.APIModel.Request.PpobAsuInquryRequestModel;
import com.otto.mart.model.APIModel.Request.PpobListrikDenomRequestModel;
import com.otto.mart.model.APIModel.Request.PpobListrikInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.PpobListrikPaymentRequestModel;
import com.otto.mart.model.APIModel.Request.PpobOttoagDenomRequestModel;
import com.otto.mart.model.APIModel.Request.PpobOttoagInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.PpobOttoagPaymentRequestModel;
import com.otto.mart.model.APIModel.Request.PpobProductlistRequestModel;
import com.otto.mart.model.APIModel.Request.PpobQrPaymentRequestModel;
import com.otto.mart.model.APIModel.Request.PpobReceiptRequestModel;
import com.otto.mart.model.APIModel.Request.PpobTelkomInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.PpobTelkomPaymentRequestModel;
import com.otto.mart.model.APIModel.Request.PpobTransactionAdviceModel;
import com.otto.mart.model.APIModel.Request.ppob.PpobDenomRequest;
import com.otto.mart.model.APIModel.Request.ppob.PpobInquiryRequest;
import com.otto.mart.model.APIModel.Request.ppob.PpobPaymentRequest;
import com.otto.mart.model.APIModel.Request.ppob.PpobUserGuideRequest;
import com.otto.mart.model.APIModel.Request.refund.MerchantRefundRequest;
import com.otto.mart.model.APIModel.Request.topUpEmoney.TopUpDenomRequestModel;
import com.otto.mart.model.APIModel.Request.voucherGame.VoucherGameDenomRequestModel;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class PpobDao extends BaseDao {

    private Context context;

    public PpobDao(Object obj) {
        super(obj);
    }

    public PpobDao(Object obj, Context context, FaqTopUpRequest faqTopUpRequest) {
        super(obj);
        this.context = context;
    }


    /**
     * PPOB
     */

    //region PPOB

    public void ppobDenomListOld(String productType, PpobOttoagDenomRequestModel requestModel, Callback callback) {
            API.ppobDenomList(OttoMartApp.getContext(), productType, requestModel, callback);
    }

    public void ppobInquiryOld(String productType, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
            API.ppobInquiry(OttoMartApp.getContext(), productType, requestModel, callback);
    }

    public void ppobPaymentOld(String productType, PpobOttoagPaymentRequestModel paymentRequestModel, Callback callback) {
        API.ppobPayment(OttoMartApp.getContext(), productType, paymentRequestModel, callback);
    }

    public void ppobQrPaymentOld(String productType, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.ppobQrPayment(OttoMartApp.getContext(), productType, requestModel, callback);
    }

    public void topUpDenomListOld(TopUpDenomRequestModel requestModel, Callback callback) {
        API.topUpDenomList(OttoMartApp.getContext(), requestModel, callback);
    }

    public void gameDenomListOld(VoucherGameDenomRequestModel requestModel, Callback callback) {
        API.gameDenomList(OttoMartApp.getContext(), requestModel, callback);
    }

    //endregion PPOB

    //region PPOB Ottofin

    public void billerProductList(String productName, String prefix, String company, Callback callback) {
        OttofinAPI.billerProductList(OttoMartApp.getContext(), productName, prefix, company, callback);
    }

    public void billerProductListCashback(String productName, String prefix, String company, String gamecode, Callback callback) {
        OttofinAPI.billerProductListCashback(OttoMartApp.getContext(), productName, prefix, company, gamecode, callback);
    }

    public void userGuide(PpobUserGuideRequest ppobUserGuideRequest, Callback callback) {
        OttofinAPI.userGuide(OttoMartApp.getContext(), ppobUserGuideRequest, callback);
    }

    public void ppobDenomList(String productType, PpobDenomRequest requestModel, Callback callback) {
        OttofinAPI.ppobDenomList(OttoMartApp.getContext(), productType, requestModel, callback);
    }

    public void ppobInquiry(PpobInquiryRequest requestModel, Callback callback) {
        OttofinAPI.ppobInquiry(OttoMartApp.getContext(), requestModel, callback);
    }

    public void ppobPayment(PpobPaymentRequest requestModel, Callback callback) {
        OttofinAPI.ppobPayment(OttoMartApp.getContext(), requestModel, callback);
    }

    public void ppobCheckStatusQr(PpobPaymentRequest requestModel, Callback callback) {
        OttofinAPI.ppobCheckStatusQrPayment(OttoMartApp.getContext(), requestModel, callback);
    }

    public void ppobAdvice(PpobPaymentRequest requestModel, Callback callback) {
        OttofinAPI.ppobAdvice(OttoMartApp.getContext(), requestModel, callback);
    }

    public void ppobCheckPending(PpobTransactionAdviceModel requestModel, Callback callback) {
        OttofinAPI.ppobCheckPending(OttoMartApp.getContext(), requestModel, callback);
    }

    public void favoriteList(String type, Callback callback) {
        OttofinAPI.favoriteList(OttoMartApp.getContext(), type, callback);
    }

    public void addFavorite(FavoriteAddRequestModel requestModel, Callback callback) {
        OttofinAPI.addFavorite(OttoMartApp.getContext(), requestModel, callback);
    }

    public void deleteFavorite(int favId, Callback callback) {
        OttofinAPI.deleteFavorite(OttoMartApp.getContext(), favId, callback);
    }

    //endregion PPOB Ottofin


    public void pulsaDenomDao(PpobOttoagDenomRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pulsaInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pulsaPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pulsaPaymentQrDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    //Pulsa OttoAG
    public void pulsaOttoagDenomDao(PpobOttoagDenomRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaOttoagDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pulsaOttoagInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaOttoagInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pulsaOttoagPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaOttoagPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pulsaOttoagPaymentQrDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaOttoagQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }
//
//    public void pulsaOttoagAdviceDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
//        API.GetPpobPulsaOttoagAdviceAPI(OttoMartApp.getContext(), requestModel, callback);
//    }

    public void pascaInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobPascaInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pascaPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPascaPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pascaPaymentQrDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPascaQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pulsaPascaOttoagDenomDao(PpobOttoagDenomRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaPascaOttoagProductlistAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pulsaPascaOttoagInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaPascaInquiryAPI2(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pascaOttoagPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaPascaOttoagPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pascaOttoagPaymentQrDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPulsaPascaOttoagQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void paketDataDenomDao(PpobOttoagDenomRequestModel requestModel, Callback callback) {
        API.GetPpobPaketdataDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void paketDataInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobPaketdataInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void paketDataPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPaketdataPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void paketDataPaymenQrDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPaketdataQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void paketDataOttoagDenomDao(PpobOttoagDenomRequestModel requestModel, Callback callback) {
        API.GetPpobPaketDataOttoagDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void paketDataOttoagInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobPaketDataOttoagInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void paketDataOttoagPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPaketDataOttoagPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void paketDataOttoagPaymentQrDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobPaketDataOttoagQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

//    public void paketDataOttoagAdviceDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
//        API.GetPpobPaketDataOttoagAdviceAPI(OttoMartApp.getContext(), requestModel, callback);
//    }

    public void listrikTokenDenomDao(PpobListrikDenomRequestModel requestModel, Callback callback) {
        API.GetPpobListrikTokenDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikTokenInquiryDao(PpobListrikInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobListrikTokenInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikTokenPaymentDao(PpobListrikPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikTokenPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikTokenQrPaymentDao(PpobListrikPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikTokenQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }


    public void listrikBillInquiryDao(PpobListrikInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobListrikNgutangInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikBillPaymentDao(PpobListrikPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikNgutangPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikBillQrPaymentDao(PpobListrikPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikNgutangQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikNonBillInquiryDao(PpobListrikInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobListrikNotNgutangInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikNonBillPaymentDao(PpobListrikPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikNotNgutangPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikNonBillQrPaymentDao(PpobListrikPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikNotNgutangQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagTokenDenomDao(PpobOttoagDenomRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagTokenDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagTokenInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagTokenInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagTokenPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagTokenPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagTokenQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagTokenQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagBillDenomDao(PpobOttoagDenomRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagNgutangDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagBillInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagNgutangInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagBillPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagNgutangPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagBillQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagNgutangQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagNonBillDenomDao(PpobOttoagDenomRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagNonNgutangDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagNonBillInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagNonNgutangInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagNonBillPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagNonNgutangPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void listrikOttoagNonBillQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobListrikOttoagNonNgutangQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pdamDenomDao(PpobProductlistRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPPobAirDenomAPI(context, requestModel, callback);
        else
            API.GetPPobAirDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pdamInquryDao(PpobAirInquiryRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPpobAirInquiryAPI(context, requestModel, callback);
        else
            API.GetPpobAirInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pdamPaymentDao(PpobAirPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobAirPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void pdamQrPaymentDao(PpobAirPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobAirQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void airDenomDao(PpobOttoagDenomRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPPobAirOttoagDenomAPI(context, requestModel, callback);
        else
            API.GetPPobAirOttoagDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void airInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPpobAirOttoagInquiryAPI(context, requestModel, callback);
        else
            API.GetPpobAirOttoagInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void airPaymentDao(PpobOttoagPaymentRequestModel paymentRequestModel, Callback callback) {
        API.GetPpobAirOttoagPaymentAPI(OttoMartApp.getContext(), paymentRequestModel, callback);
    }

    public void pdamOttoagQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobAirOttoagQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void bpjsDenomDao(PpobProductlistRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPPobBpjsDenomAPI(context, requestModel, callback);
        else
            API.GetPPobBpjsDenomAPI(OttoMartApp.getContext(), requestModel, callback);

    }

    public void bpjsInquiryDao(PpobAsuInquryRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPpobBpjsInquiryAPI(context, requestModel, callback);
        else
            API.GetPpobBpjsInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void bpjsPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobBpjsPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void bpjsQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobBpjsQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
//        Toast.makeText(OttoMartApp.getContext(), "Akan Segera Hadir", Toast.LENGTH_SHORT).show();
    }

    public void insuranceDenomDao(PpobProductlistRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPPobAsuDenomAPI(context, requestModel, callback);
        else
            API.GetPPobAsuDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void insuranceInquiryDao(PpobAsuInquryRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPpobAsuInquiryAPI(context, requestModel, callback);
        else
            API.GetPpobAsuInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void insurancePaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobAsuPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void insuranceQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobAsuQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void cableDenomDao(PpobProductlistRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPPobCableDenomAPI(context, requestModel, callback);
        else
            API.GetPPobCableDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void cableInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPpobCableInquiryAPI(context, requestModel, callback);
        else
            API.GetPpobCableInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void cablePaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobCablePaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void cableQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobCableQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void scamDenomDao(PpobProductlistRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPPobScamDenomAPI(context, requestModel, callback);
        else
            API.GetPPobScamDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void scamInquiryDao(PpobAsuInquryRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPpobScamInquiryAPI(context, requestModel, callback);
        else
            API.GetPpobScamInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void scamPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobScamPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void scamQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobScamQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void educationProductlistDao(PpobProductlistRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPPobEducationDenomAPI(context, requestModel, callback);
        else
            API.GetPPobEducationDenomAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void educationInquiryDao(PpobAsuInquryRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPpobEducationInquiryAPI(context, requestModel, callback);
        else
            API.GetPpobEducationInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void educationPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobEducationPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void educationQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobEducationQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void QrPaymentConfirmDao(PpobQrPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobQrPaymentConfirmAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void QrCancelDao(CancelQrTransactionRequestModel requestModel, Callback callback) {
        API.doCancelQrPayment(OttoMartApp.getContext(), requestModel, callback);
    }

    public void TelkomInquiryDao(PpobTelkomInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobTelkomInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void TelkomPaymentDao(PpobTelkomPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobTelkomPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void TelkomQRDao(PpobTelkomPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobTelkomQRAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void TelkomOttoagDenomDao(PpobOttoagDenomRequestModel requestModel, Callback callback) {
        API.GetPpobTelkomOttoagProductlistAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void TelkomOttoagInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        API.GetPpobTelkomOttoagInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void TelkomOttoagPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobTelkomOttoagPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void TelkomOttoagQRDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobTelkomOttoagQRAPI(OttoMartApp.getContext(), requestModel, callback);
    }

//    public void TelkomOttoagAdviceDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
//        API.GetPpobTelkomOttoagAdviceAPI(OttoMartApp.getContext(), requestModel, callback);
//    }

    //Top Up Emoney
    public void topUpEmoneyProduct(Callback callback) {
        if (context != null)
            API.GetTopUpEmoneyProduct(context, callback);
        else
            API.GetTopUpEmoneyProduct(OttoMartApp.getContext(), callback);
    }

    public void topUpEmoneyDenom(TopUpDenomRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetTopUpEmoneyDenom(context, requestModel, callback);
        else
            API.GetTopUpEmoneyDenom(OttoMartApp.getContext(), requestModel, callback);
    }

    public void topUpInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPpobTopUpInquiryAPI(context, requestModel, callback);
        else
            API.GetPpobTopUpInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void topUpPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobTopUpPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void topUpQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobTopUpQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void faqTopUp(FaqTopUpRequest faqTopUpRequest, Callback callback) {
        API.GetFAQTopUp(OttoMartApp.getContext(), faqTopUpRequest, callback);
    }

    //Voucher Game
    public void voucherGameProductList(Callback callback) {
        API.GetVoucherGameProductList(context, callback);
    }

    public void voucherGameDenom(VoucherGameDenomRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetVoucherGameDenom(context, requestModel, callback);
        else
            API.GetVoucherGameDenom(OttoMartApp.getContext(), requestModel, callback);
    }

    public void voucherGameInquiryDao(PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        if (context != null)
            API.GetPpobVoucherGameInquiryAPI(context, requestModel, callback);
        else
            API.GetPpobVoucherGameInquiryAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void voucherGamePaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobVoucherGamePaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void voucherGameQrPaymentDao(PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobVoucherGameQrPaymentAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    //PPOB Receipt
    public void receipt(String productPool, PpobReceiptRequestModel requestModel, Callback callback) {
        API.GetReceipt(OttoMartApp.getContext(), productPool, requestModel, callback);
    }

    //advice
    public void OttoagAdviceDao(String productPool, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        API.GetPpobOttoagAdviceAPI(OttoMartApp.getContext(), productPool, requestModel, callback);
    }

    //Transaction Advice
    public void TransactionAdviceDao(PpobTransactionAdviceModel requestModel, Callback callback) {
        API.GetPpobTransactionAdviceAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void getFavlist(String type, Callback callback) {
        API.GetFav(OttoMartApp.getContext(), type, callback);
    }

    public void saveFav(FavoriteAddRequestModel requestModel, Callback callback) {
        API.SaveFav(OttoMartApp.getContext(), requestModel, callback);
    }

    public void deleteFav(int favId, Callback callback) {
        API.DeleteFav(OttoMartApp.getContext(), favId, callback);
    }
}
