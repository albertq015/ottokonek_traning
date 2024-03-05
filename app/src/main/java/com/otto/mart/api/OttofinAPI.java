package com.otto.mart.api;

import android.content.Context;
import android.content.Intent;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.otto.mart.BuildConfig;
import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.model.APIModel.Request.CheckMerchantIdRequestModel;
import com.otto.mart.model.APIModel.Request.CheckVersionRequestModel;
import com.otto.mart.model.APIModel.Request.FavoriteAddRequestModel;
import com.otto.mart.model.APIModel.Request.LoginOtpRequest;
import com.otto.mart.model.APIModel.Request.LoginRequestModel;
import com.otto.mart.model.APIModel.Request.OmzetSaldoRequestModel;
import com.otto.mart.model.APIModel.Request.PayQrInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.PaymentOfflineConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.PpobTransactionAdviceModel;
import com.otto.mart.model.APIModel.Request.QrStringRequestModel;
import com.otto.mart.model.APIModel.Request.RegisterOtpRequestModel;
import com.otto.mart.model.APIModel.Request.ResendOtpRegisterRequestModel;
import com.otto.mart.model.APIModel.Request.ResetOtpPinRequestModel;
import com.otto.mart.model.APIModel.Request.ResetPinRequestModel;
import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.model.APIModel.Request.UpdatePinRequestModel;
import com.otto.mart.model.APIModel.Request.UpdateStatusRequest;
import com.otto.mart.model.APIModel.Request.UserSecurityQuestionRequestModel;
import com.otto.mart.model.APIModel.Request.bank.AddBankDepositRequest;
import com.otto.mart.model.APIModel.Request.bogasari.BogasariInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.bogasari.BogasariPaymentRequestModel;
import com.otto.mart.model.APIModel.Request.donasi.DonasiInquiryRequest;
import com.otto.mart.model.APIModel.Request.donasi.DonasiPaymentRequest;
import com.otto.mart.model.APIModel.Request.donasi.DonasiQrStringRequest;
import com.otto.mart.model.APIModel.Request.grosir.CheckEligibleRequest;
import com.otto.mart.model.APIModel.Request.history.HistoryDetailRequest;
import com.otto.mart.model.APIModel.Request.kyc.KycUploadImageRequest;
import com.otto.mart.model.APIModel.Request.payment.AlfamartTokenRequest;
import com.otto.mart.model.APIModel.Request.ppob.PpobDenomRequest;
import com.otto.mart.model.APIModel.Request.ppob.PpobInquiryRequest;
import com.otto.mart.model.APIModel.Request.ppob.PpobPaymentRequest;
import com.otto.mart.model.APIModel.Request.ppob.PpobUserGuideRequest;
import com.otto.mart.model.APIModel.Request.refund.MerchantRefundRequest;
import com.otto.mart.model.APIModel.Request.setting.LanguageSettingRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferBankInquiryRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferBankPaymentRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferLimitRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferSknRequest;
import com.otto.mart.model.APIModel.Response.AppClonerListResponse;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.ForgotPinSecurityQuestionResponse;
import com.otto.mart.model.APIModel.Response.LoginOtpResponseModel;
import com.otto.mart.model.APIModel.Response.LoginResponseModel;
import com.otto.mart.model.APIModel.Response.MerchantStatusResponse;
import com.otto.mart.model.APIModel.Response.PpobOttoagDenomResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.ProfileResponseModel;
import com.otto.mart.model.APIModel.Response.ResetOtpPinResponseModel;
import com.otto.mart.model.APIModel.Response.ResetPinResponseModel;
import com.otto.mart.model.APIModel.Response.SignupOtpResponseModel;
import com.otto.mart.model.APIModel.Response.SignupResponseModel;
import com.otto.mart.model.APIModel.Response.UpdateStatusResponse;
import com.otto.mart.model.APIModel.Response.bank.BankAccountListDepositResponse;
import com.otto.mart.model.APIModel.Response.bogasari.BogasariInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.bogasari.BogasariPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.bogasari.BogasariResponseModel;
import com.otto.mart.model.APIModel.Response.donasi.DonasiInquiryResponse;
import com.otto.mart.model.APIModel.Response.donasi.DonasiQRPaymentResponse;
import com.otto.mart.model.APIModel.Response.donasi.DonasiQrStringResponse;
import com.otto.mart.model.APIModel.Response.donasi.ProductDonasiResponse;
import com.otto.mart.model.APIModel.Response.history.HistoryDetailResponse;
import com.otto.mart.model.APIModel.Response.history.OmzetHistoryResponse;
import com.otto.mart.model.APIModel.Response.history.OttoCashHistoryResponse;
import com.otto.mart.model.APIModel.Response.merchant.featureProduct.FeatureProductResponse;
import com.otto.mart.model.APIModel.Response.merchant.theme.MerchantThemeResponse;
import com.otto.mart.model.APIModel.Response.ppob.PpobCheckStatusQRPaymentResponse;
import com.otto.mart.model.APIModel.Response.ppob.PpobInquiryResponse;
import com.otto.mart.model.APIModel.Response.ppob.PpobUserGuideResponse;
import com.otto.mart.model.APIModel.Response.refund.CheckRefundStatusResponse;
import com.otto.mart.model.APIModel.Response.refund.MerchantRefundResponse;
import com.otto.mart.model.APIModel.Response.setting.LanguageSettingResponse;
import com.otto.mart.model.APIModel.Response.transfer.TransferBankInquiryResponse;
import com.otto.mart.model.APIModel.Response.transfer.TransferLimitResponse;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.SystemUtil;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.settings.LanguageIdConverter;

import java.util.HashMap;
import java.util.Map;

import app.beelabs.com.codebase.base.BaseApi;
import retrofit2.Callback;
import retrofit2.http.Body;

public class
OttofinAPI extends BaseApi implements GLOBAL {

    private static Map<String, String> initHeader() {
        if (SessionManager.getAccessToken() != null &&
                SessionManager.isLoggedIn() &&
                SessionManager.getAccessToken().equals("TokenNaN")) {
            SessionManager.logout();
            OttoMartApp.getContext().startActivity(new Intent(OttoMartApp.getContext(), LoginActivity.class));
        }
        Map<String, String> map = new HashMap<>();
        map.put("Device-Id", SystemUtil.getDeviceId(OttoMartApp.getContext()));
        map.put("Language-Id", new LanguageIdConverter().getLanguageId());
        return map;
    }

    private static Map<String, String> initAccessTokenHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer " + SessionManager.getAccessToken());
        return map;
    }

    private static OttofinApiServices initApiDomain(Context context) {
        getInstance().setApiDomain(BuildConfig.OTTOFIN_SERVER_URL);
        return (OttofinApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttofinApiServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static OttofinApiServices initApiDomain30s(Context context) {
        getInstance().setApiDomain(BuildConfig.OTTOFIN_SERVER_URL);
        return (OttofinApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttofinApiServices.class, false, 30, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static OttofinApiServices initApiDomain15s(Context context) {
        getInstance().setApiDomain(BuildConfig.OTTOFIN_SERVER_URL);
        return (OttofinApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttofinApiServices.class, false, 15, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static OttofinApiServices initAuthApiDomain(Context context) {
        getInstance().setApiDomain(BuildConfig.OTTOFIN_AUTH_SERVER_URL);
        return (OttofinApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttofinApiServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static OttofinApiServices initQrisApiDomain(Context context) {
        getInstance().setApiDomain(BuildConfig.OTTOFIN_QRIS_SERVER_URL);
        return (OttofinApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttofinApiServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static OttofinApiServices initOCBIApiDomain(Context context) {
        getInstance().setApiDomain(BuildConfig.OCBI_SERVER_URL);
        return (OttofinApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttofinApiServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static OttofinApiServices initOCBIApiDomain15s(Context context) {
        getInstance().setApiDomain(BuildConfig.OCBI_SERVER_URL);
        return (OttofinApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttofinApiServices.class, false, 15, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static OttofinApiServices initOasisApiDomain(Context context) {
        getInstance().setApiDomain(BuildConfig.GROSIR_SERVER_URL);
        return (OttofinApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttofinApiServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static OttofinApiServices initOasisApiDomain15s(Context context) {
        getInstance().setApiDomain(BuildConfig.GROSIR_SERVER_URL);
        return (OttofinApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttofinApiServices.class, false, 15, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }


    /**
     * App Config
     */

    //App Cloner List
    synchronized public static void AppClonerList(Context context, Callback callback) {
        initApiDomain(context).appClonerList(initHeader()).enqueue((Callback<AppClonerListResponse>) callback);
    }

    synchronized public static void versionCheck(Context context, Callback callback) {
        initApiDomain(context).versionCheck(new CheckVersionRequestModel(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_CODE)).enqueue(callback);
    }


    /**
     * Auth
     */

    //region Auth
    synchronized public static void login(Context context, LoginRequestModel requestModel, Callback callback) {
        initAuthApiDomain(context).login(initHeader(), requestModel).enqueue((Callback<LoginResponseModel>) callback);
    }

    //Login OTP
    synchronized public static void loginOTP(Context context, LoginOtpRequest loginOtpRequest, Callback callback) {
        initAuthApiDomain(context).loginOTP(initHeader(), loginOtpRequest).enqueue((Callback<LoginOtpResponseModel>) callback);
    }

    synchronized public static void logout(Context context, Callback callback) {
        initAuthApiDomain(context).logout(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void resendOtp(Context context, ResendOtpRegisterRequestModel requestModel, Callback callback) {
        initAuthApiDomain(context).resendOTP(initHeader(), requestModel).enqueue((Callback<BaseResponseModel>) callback);
    }

    synchronized public static void forgotPinSecuritytQuestion(Context context, UserSecurityQuestionRequestModel requestModel, Callback callback) {
        initAuthApiDomain(context).forogotPinSecurityQuestion(initHeader(), requestModel).enqueue((Callback<ForgotPinSecurityQuestionResponse>) callback);
    }

    synchronized public static void forgotPinOTP(Context context, ResetOtpPinRequestModel requestModel, Callback callback) {
        initAuthApiDomain(context).forgotPinOTP(initHeader(), requestModel).enqueue((Callback<ResetOtpPinResponseModel>) callback);
    }

    synchronized public static void forgotPinReset(Context context, ResetPinRequestModel requestModel, Callback callback) {
        initAuthApiDomain(context).forgotPinReset(initHeader(), requestModel).enqueue((Callback<ResetPinResponseModel>) callback);
    }



    synchronized public static void merchantTheme(Context context, Callback callback) {
        initQrisApiDomain(context).merchantTheme(initHeader(), initAccessTokenHeader()).enqueue((Callback<MerchantThemeResponse>) callback);
    }

    synchronized public static void featureProduct(Context context, Callback callback) {
        initQrisApiDomain(context).featureProduct(initHeader(), initAccessTokenHeader()).enqueue((Callback<FeatureProductResponse>) callback);
    }

    synchronized public static void languageSetting(Context context, LanguageSettingRequest languageSettingRequest, Callback callback) {
        initApiDomain(context).languageSetting(initHeader(), initAccessTokenHeader(), languageSettingRequest).enqueue((Callback<LanguageSettingResponse>) callback);
    }

    synchronized public static void UpdatePin(Context context, UpdatePinRequestModel model, Callback callback) {
        initAuthApiDomain(context).asUpdatePin(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    //endregion Auth


    /**
     * Register
     */

    //region Registor

    //Search Merchant
    synchronized public static void searchMerchant(Context context, CheckMerchantIdRequestModel requestModel, Callback callback) {
        initAuthApiDomain(context).searchMerchant(initHeader(), requestModel).enqueue(callback);
    }

    //Sign Up
    synchronized public static void signUp(Context context, SignupRequestModel requestModel, Callback callback) {
        initAuthApiDomain(context).signUp(initHeader(), requestModel).enqueue((Callback<SignupResponseModel>) callback);
    }

    //Sign Up OTP
    synchronized public static void signUpOtp(Context context, RegisterOtpRequestModel requestModel, Callback callback) {
        initAuthApiDomain(context).signUpOtp(initHeader(), requestModel).enqueue((Callback<SignupOtpResponseModel>) callback);
    }

    //endregion Register


    /**
     * Emoney
     */

    //region Emoney
    synchronized public static void omzetStatus(Context context, Callback callback) {
        initApiDomain15s(context).omzetStatus(initHeader(), initAccessTokenHeader()).enqueue((callback));
    }

    synchronized public static void emoneySummary(Context context, Callback callback) {
        initApiDomain15s(context).emoneySummary(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void transferOmzetToWallet(Context context, OmzetSaldoRequestModel model, Callback callback) {
        initOCBIApiDomain(context).transferOmzetToWallet(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    //endregion Emoney


    /**
     * Bogasari
     */

    //region Bogasari

    //Bogasari's product list
    synchronized public static void bogasariProducts(Context context, String merchantId, Callback callback) {
        initApiDomain(context).bogasariProducts(initHeader(), initAccessTokenHeader(), merchantId).enqueue((Callback<BogasariResponseModel>) callback);
    }

    //Bogasari's product inquiry
    synchronized public static void bogasariInquiry(Context context, BogasariInquiryRequestModel model, Callback callback) {
        initApiDomain(context).bogasariInquiry(initHeader(), initAccessTokenHeader(), model).enqueue((Callback<BogasariInquiryResponseModel>) callback);
    }

    //Bogasari's product payment
    synchronized public static void bogasariPayment(Context context, BogasariPaymentRequestModel model, Callback callback) {
        initApiDomain(context).bogasariPayment(initHeader(), initAccessTokenHeader(), model).enqueue((Callback<BogasariPaymentResponseModel>) callback);
    }

    //endregion Bogasari


    /**
     * Emoney
     */

    //region Emoney

    //Wallet History
    synchronized public static void omzetHistory(Context context, String dateFrom, String dateTo,
                                                 String paymentMethod, String transactionType, int limit, int page, Callback callback) {
        initOCBIApiDomain15s(context).omzetHistory(initHeader(), initAccessTokenHeader(), dateFrom, dateTo, paymentMethod, transactionType,
                limit, page).enqueue((Callback<OmzetHistoryResponse>) callback);
    }

    //Wallet History
    synchronized public static void walletHistory(Context context, String dateFrom, String dateTo, int page, Callback callback) {
        initOCBIApiDomain15s(context).walletHistory(initHeader(), initAccessTokenHeader(), dateFrom, dateTo, page).enqueue((Callback<OmzetHistoryResponse>) callback);
    }

    //OttoCash History
    synchronized public static void ottoCashHistory(Context context, String dateFrom, String dateTo, int page, Callback callback) {
        initOCBIApiDomain(context).ottoCashHistory(initHeader(), initAccessTokenHeader(), dateFrom, dateTo, page).enqueue((Callback<OttoCashHistoryResponse>) callback);
    }

    //History Detail
    synchronized public static void historyDetail(Context context, HistoryDetailRequest historyDetailRequest, Callback callback) {
        initApiDomain15s(context).historyDetail(initHeader(), initAccessTokenHeader(), historyDetailRequest).enqueue((Callback<HistoryDetailResponse>) callback);
    }

    //endregion Emoney


    /**
     * Payment QR
     */

    //region Payment QR
    synchronized public static void payQrInquiry(Context context, PayQrInquiryRequestModel model, Callback callback) {
        initApiDomain(context).payQrInqury(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void qrString(Context context, QrStringRequestModel requestModel, Callback callback) {
        initApiDomain(context).qrString(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void payQrQrisInquiry(Context context, PayQrInquiryRequestModel model, Callback callback) {
        initQrisApiDomain(context).payQrQrisInqury(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void payQrPayment(Context context, PaymentOfflineConfirmationRequestModel model, Callback callback) {
        initQrisApiDomain(context).payQrQrisPayment(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    //endregion Payment QR


    /**
     * Biller
     */

    //region Biller

    //Biller Product List
    synchronized public static void billerProductListDonasi(Context context, String productName, Callback callback) {
        initApiDomain(context).billerProductListDonasi(initHeader(), initAccessTokenHeader(), productName).enqueue((Callback<ProductDonasiResponse>) callback);
    }

    //Biller Inquiry
    synchronized public static void billerInquiry(Context context, DonasiInquiryRequest requestModel, Callback callback) {
        initApiDomain(context).billerInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<DonasiInquiryResponse>) callback);
    }

    //Biller QR String
    synchronized public static void billerQrString(Context context, DonasiQrStringRequest requestModel, Callback callback) {
        initApiDomain(context).billerQrString(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<DonasiQrStringResponse>) callback);
    }

    //Biller Payment
    synchronized public static void billerPayment(Context context, DonasiPaymentRequest requestModel, Callback callback) {
        initApiDomain(context).billerPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    //Check Status QR Payment
    synchronized public static void billerCheckStatusQrPayment(Context context, DonasiPaymentRequest requestModel, Callback callback) {
        initApiDomain(context).billerCheckStatusQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<DonasiQRPaymentResponse>) callback);
    }

    //endregion Biller


    /**
     * PPOB
     */

    //region PPOB
    synchronized public static void billerProductList(Context context, String productName, String prefix, String company, Callback callback) {
        initApiDomain(context).billerProductList(initHeader(), initAccessTokenHeader(), productName, prefix, company).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void billerProductListCashback(Context context, String productName, String prefix, String company, String gamecode, Callback callback) {
        initApiDomain(context).billerProductListCashback(initHeader(), initAccessTokenHeader(), productName, prefix, company, gamecode).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void userGuide(Context context, PpobUserGuideRequest ppobUserGuideRequest, Callback callback) {
        initApiDomain(context).userGuide(initHeader(), initAccessTokenHeader(), ppobUserGuideRequest).enqueue((Callback<PpobUserGuideResponse>) callback);
    }

    synchronized public static void ppobDenomList(Context context, String productType, PpobDenomRequest requestModel, Callback callback) {
        initApiDomain(context).ppobDenomList(initHeader(), initAccessTokenHeader(), productType, requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void ppobInquiry(Context context, PpobInquiryRequest requestModel, Callback callback) {
        initApiDomain30s(context).ppobInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobInquiryResponse>) callback);
    }

    synchronized public static void ppobPayment(Context context, PpobPaymentRequest requestModel, Callback callback) {
        initApiDomain(context).ppobPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void ppobCheckStatusQrPayment(Context context, PpobPaymentRequest requestModel, Callback callback) {
        initApiDomain(context).ppobCheckStatusQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobCheckStatusQRPaymentResponse>) callback);
    }

    synchronized public static void ppobAdvice(Context context, PpobPaymentRequest requestModel, Callback callback) {
        initApiDomain(context).ppobAdvice(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void ppobCheckPending(Context context, PpobTransactionAdviceModel requestModel, Callback callback) {
        initApiDomain(context).ppobCheckPending(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void favoriteList(Context context, String trgt, Callback callback) {
        initApiDomain(context).favoriteList(initHeader(), initAccessTokenHeader(), trgt).enqueue(callback);
    }

    synchronized public static void addFavorite(Context context, FavoriteAddRequestModel requestModel, Callback callback) {
        initApiDomain(context).addFavorite(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void deleteFavorite(Context context, int id, Callback callback) {
        initApiDomain(context).deleteFavorite(initHeader(), initAccessTokenHeader(), id).enqueue(callback);
    }

    //endregion PPOB


    /**
     * Refund
     */

    //region Refund
    synchronized public static void checkRefundStatus(Context context, @Body MerchantRefundRequest merchantRefundRequest, Callback callback) {
        initApiDomain15s(context).checkRefundStatus(initHeader(), initAccessTokenHeader(), merchantRefundRequest).enqueue((Callback<CheckRefundStatusResponse>) callback);
    }

    synchronized public static void merchantRefund(Context context, MerchantRefundRequest merchantRefundRequest, Callback callback) {
        initApiDomain(context).merchantRefund(initHeader(), initAccessTokenHeader(), merchantRefundRequest).enqueue((Callback<MerchantRefundResponse>) callback);
    }

    //endregion Refund


    /**
     * merchant
     */
    synchronized public static void merchantStatus(Context context, Callback callback) {
        initApiDomain(context).merchantStatus(initHeader(), initAccessTokenHeader()).enqueue((Callback<MerchantStatusResponse>) callback);
    }

    synchronized public static void GetProfileAPI(Context context, Callback callback) {
        initApiDomain15s(context).profile(initHeader(), initAccessTokenHeader()).enqueue((Callback<ProfileResponseModel>) callback);
    }


    /**
     * Oasis
     */

    synchronized public static void checkOasisStatus(Context context, Callback callback) {
        initApiDomain(context).checkOasisStatus(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }



    /**
     * Transfer Bank
     */

    synchronized public static void transferLimit(Context context, @Body TransferLimitRequest transferLimitRequest, Callback callback) {
        initApiDomain(context).transferLimit(initHeader(), initAccessTokenHeader(), transferLimitRequest).enqueue((Callback<TransferLimitResponse>) callback);
    }

    synchronized public static void transferBankInquiry(Context context, @Body TransferBankInquiryRequest transferBankInquiryRequest, Callback callback) {
        initQrisApiDomain(context).transferBankInquiry(initHeader(), initAccessTokenHeader(), transferBankInquiryRequest).enqueue((Callback<TransferBankInquiryResponse>) callback);
    }

    synchronized public static void transferBankPayment(Context context, @Body TransferBankPaymentRequest transferBankPaymentRequest, Callback callback) {
        initQrisApiDomain(context).transferBankPayment(initHeader(), initAccessTokenHeader(), transferBankPaymentRequest).enqueue((Callback<BasePaymentResponseModel>) callback);
    }

    synchronized public static void checkTransferStatus(Context context, @Body TransferBankPaymentRequest transferBankPaymentRequest, Callback callback) {
        initQrisApiDomain(context).checkTransferStatus(initHeader(), initAccessTokenHeader(), transferBankPaymentRequest).enqueue((Callback<BasePaymentResponseModel>) callback);
    }

    synchronized public static void transferSkn(Context context, @Body TransferSknRequest transferSknRequest, Callback callback) {
        initOCBIApiDomain(context).transferSkn(initHeader(), initAccessTokenHeader(), transferSknRequest).enqueue((Callback<BasePaymentResponseModel>) callback);
    }



    /**
     * Transfer Deposit
     */

    synchronized public static void bankListDeposit(Context context, Callback callback) {
        initQrisApiDomain(context).bankListDeposit(initHeader(), initAccessTokenHeader()).enqueue((Callback<BankAccountListDepositResponse>) callback);
    }

    synchronized public static void addBankDeposit(Context context, @Body AddBankDepositRequest addBankDepositRequest, Callback callback) {
        initQrisApiDomain(context).addBankDeposit(initHeader(), initAccessTokenHeader(), addBankDepositRequest).enqueue((Callback<BaseResponseModel>) callback);
    }

    synchronized public static void deleteBankDeposit(Context context, @Body AddBankDepositRequest addBankDepositRequest, Callback callback) {
        initQrisApiDomain(context).deleteBankDeposit(initHeader(), initAccessTokenHeader(), addBankDepositRequest).enqueue((Callback<BaseResponseModel>) callback);
    }

    synchronized public static void transferDepositInquiry(Context context, @Body TransferBankInquiryRequest transferBankInquiryRequest, Callback callback) {
        initQrisApiDomain(context).transferDepositInquiry(initHeader(), initAccessTokenHeader(), transferBankInquiryRequest).enqueue((Callback<TransferBankInquiryResponse>) callback);
    }

    synchronized public static void transferDepositPayment(Context context, @Body TransferBankPaymentRequest transferBankPaymentRequest, Callback callback) {
        initQrisApiDomain(context).transferDepositPayment(initHeader(), initAccessTokenHeader(), transferBankPaymentRequest).enqueue((Callback<BasePaymentResponseModel>) callback);
    }

    synchronized public static void checkTransferDepositStatus(Context context, @Body TransferBankPaymentRequest transferBankPaymentRequest, Callback callback) {
        initQrisApiDomain(context).checkTransferDepositStatus(initHeader(), initAccessTokenHeader(), transferBankPaymentRequest).enqueue((Callback<BasePaymentResponseModel>) callback);
    }



    /**
     * OCBI
     */

    synchronized public static void getBalance(Context context, Callback callback) {
        initOCBIApiDomain(context).balance(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void uploadImageKyc(Context context, KycUploadImageRequest imageRequest, Callback callback) {
        initOCBIApiDomain(context).kycImage(initHeader(), initAccessTokenHeader(), imageRequest).enqueue(callback);
    }

    synchronized public static void getOttocashStatus(Context context, Callback callback) {
        initOCBIApiDomain(context).ottocashStatus(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void confirmTNCOCBI(Context context, Callback callback) {
        initOCBIApiDomain(context).confirmTNCOCBI(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void alfamartToken(Context context, AlfamartTokenRequest alfamartTokenRequest, Callback callback) {
        initQrisApiDomain(context).alfamartToken(initHeader(), initAccessTokenHeader(), alfamartTokenRequest).enqueue(callback);
    }

    synchronized public static void contactUs(Context context, Callback callback) {
        initQrisApiDomain(context).contactUs(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }
}