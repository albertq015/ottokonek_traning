package com.otto.mart.api;

import android.content.Context;
import android.content.Intent;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.otto.mart.BuildConfig;
import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.model.APIModel.Request.AddAddressRequestModel;
import com.otto.mart.model.APIModel.Request.AddressEditRequestModel;
import com.otto.mart.model.APIModel.Request.BankEditRequestModel;
import com.otto.mart.model.APIModel.Request.CancelQrTransactionRequestModel;
import com.otto.mart.model.APIModel.Request.CheckMerchantIdRequestModel;
import com.otto.mart.model.APIModel.Request.CheckVersionRequestModel;
import com.otto.mart.model.APIModel.Request.DeleteAddressRequestModel;
import com.otto.mart.model.APIModel.Request.FaqTopUpRequest;
import com.otto.mart.model.APIModel.Request.FavoriteAddRequestModel;
import com.otto.mart.model.APIModel.Request.GetQrStringRequestModel;
import com.otto.mart.model.APIModel.Request.LoginOtpRequestModel;
import com.otto.mart.model.APIModel.Request.LoginQrRequestModel;
import com.otto.mart.model.APIModel.Request.LoginRequestModel;
import com.otto.mart.model.APIModel.Request.OmsetHistoryRequestModel;
import com.otto.mart.model.APIModel.Request.OmzetSaldoRequestModel;
import com.otto.mart.model.APIModel.Request.PayQrInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.PaymentOfflineConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.PpobAirInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.PpobAirPaymentRequestModel;
import com.otto.mart.model.APIModel.Request.PpobAsuInquryRequestModel;
import com.otto.mart.model.APIModel.Request.PpobAsuPaymentRequestModel;
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
import com.otto.mart.model.APIModel.Request.QrStringRequestModel;
import com.otto.mart.model.APIModel.Request.RegisterOtpRequestModel;
import com.otto.mart.model.APIModel.Request.RegisterUpgradeAccountRequestModel;
import com.otto.mart.model.APIModel.Request.ResendOtpRegisterRequestModel;
import com.otto.mart.model.APIModel.Request.ResetOtpPinRequestModel;
import com.otto.mart.model.APIModel.Request.ResetPinRequestModel;
import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.model.APIModel.Request.TfWalletConfRequestModel;
import com.otto.mart.model.APIModel.Request.TfWalletInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.TransferToBankConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.TransferToBankInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.UpdatePinRequestModel;
import com.otto.mart.model.APIModel.Request.UpdateProfileRequestModel;
import com.otto.mart.model.APIModel.Request.UpdateStatusRequest;
import com.otto.mart.model.APIModel.Request.UserSecurityQuestionRequestModel;
import com.otto.mart.model.APIModel.Request.inbox.InboxReadRequest;
import com.otto.mart.model.APIModel.Request.inbox.InboxUpdateBulkRequest;
import com.otto.mart.model.APIModel.Request.olshop.AddCartRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.AddressRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ConfirmPaid;
import com.otto.mart.model.APIModel.Request.olshop.OrderConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.PaymentJurnalRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ProductListRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ReversalRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ShippingCostRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.address.ShippingAddressRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.cart.UpdateCartRequestModel;
import com.otto.mart.model.APIModel.Request.payment.AlfamartPurchaseRequest;
import com.otto.mart.model.APIModel.Request.payment.AlfamartStatusRequest;
import com.otto.mart.model.APIModel.Request.payment.IndomaretQrPurchaseRequestModel;
import com.otto.mart.model.APIModel.Request.payment.IndomaretQrStatusRequestModel;
import com.otto.mart.model.APIModel.Request.tokoOttopay.AddToCardRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderConfirmRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderHistoryDetailRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderPayOffRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderPaymentRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.ProductListRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.RemoveCardRequest;
import com.otto.mart.model.APIModel.Request.topUpEmoney.TopUpDenomRequestModel;
import com.otto.mart.model.APIModel.Request.voucherGame.VoucherGameDenomRequestModel;
import com.otto.mart.model.APIModel.Response.AppClonerListResponse;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.BusinessCategoryResponseModel;
import com.otto.mart.model.APIModel.Response.GetBankResponseModel;
import com.otto.mart.model.APIModel.Response.GetCityResponseModel;
import com.otto.mart.model.APIModel.Response.GetCountryResponseModel;
import com.otto.mart.model.APIModel.Response.GetDistrictResponseModel;
import com.otto.mart.model.APIModel.Response.GetProvinceResponsetModel;
import com.otto.mart.model.APIModel.Response.GetQrStringResponseModel;
import com.otto.mart.model.APIModel.Response.GetVillageResponseModel;
import com.otto.mart.model.APIModel.Response.LoginOtpResponseModel;
import com.otto.mart.model.APIModel.Response.LoginResponseModel;
import com.otto.mart.model.APIModel.Response.NearbyMerchantResponse;
import com.otto.mart.model.APIModel.Response.PPobPaymentQrResponseModel;
import com.otto.mart.model.APIModel.Response.PpobAirInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.PpobAirPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.PpobAirProductlistResponseModel;
import com.otto.mart.model.APIModel.Response.PpobBpjsInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.PpobBpjsPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.PpobListrikDenomResponseModel;
import com.otto.mart.model.APIModel.Response.PpobListrikInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.PpobListrikPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagDenomResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.PpobPulsaInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.PpobPulsaPascaInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.ProfileResponseModel;
import com.otto.mart.model.APIModel.Response.RegisterUpgradeAccountResponseModel;
import com.otto.mart.model.APIModel.Response.ResetOtpPinResponseModel;
import com.otto.mart.model.APIModel.Response.ResetPinResponseModel;
import com.otto.mart.model.APIModel.Response.SecurityQuestionResponseModel;
import com.otto.mart.model.APIModel.Response.SignupOtpResponseModel;
import com.otto.mart.model.APIModel.Response.SignupResponseModel;
import com.otto.mart.model.APIModel.Response.UpdateStatusResponse;
import com.otto.mart.model.APIModel.Response.UserSecurityQuestionResponseModel;
import com.otto.mart.model.APIModel.Response.topUpEmoney.TopUpEmoneyDenomResponseModel;
import com.otto.mart.model.APIModel.Response.topUpEmoney.TopUpEmoneyProductResponseModel;
import com.otto.mart.model.APIModel.Response.voucherGame.VoucherGameResponse;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.SystemUtil;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.settings.LanguageIdConverter;

import java.util.HashMap;
import java.util.Map;

import app.beelabs.com.codebase.base.BaseApi;
import retrofit2.Callback;

public class
API extends BaseApi implements GLOBAL {

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

    private static ApiServices initApiDomain(Context context) {
        getInstance().setApiDomain(BuildConfig.SERVER_URL);
        return (ApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                ApiServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static ApiServices initApiDomain30s(Context context) {
        getInstance().setApiDomain(BuildConfig.SERVER_URL);
        return (ApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                ApiServices.class, false, 30, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static ApiServices initApiDomain15s(Context context) {
        getInstance().setApiDomain(BuildConfig.SERVER_URL);
        return (ApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                ApiServices.class, false, 15, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static ApiServices initApiDomainOttofin(Context context) {
        getInstance().setApiDomain(BuildConfig.OTTOFIN_SERVER_URL + BuildConfig.OTTOFIN_API_VERSION + "/");
        return (ApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                ApiServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static ApiServices initApiDomainMap(Context context) {
        getInstance().setApiDomain(BuildConfig.MAP_URL);
        return (ApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                ApiServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    // Nearby Merchant
    synchronized public static void getNearbyMerchant(Context context, String accessToken, String search, String type, double latitude,
                                                      double longitude, int limit, int offset, Callback callback) {
        initApiDomainMap(context).getNearbyMerchant(GLOBAL.A_APIKEY, search, type, latitude, longitude,
                limit, offset).enqueue((Callback<NearbyMerchantResponse>) callback);
    }

    //App Cloner List
    synchronized public static void AppClonerList(Context context, Callback callback) {
        initApiDomain(context).appClonerList(initHeader()).enqueue((Callback<AppClonerListResponse>) callback);
    }

    //auth
    synchronized public static void SignUpAPI(Context context, SignupRequestModel requestModel, Callback callback) {
        initApiDomain(context).asSignUp(initHeader(), requestModel).enqueue((Callback<SignupResponseModel>) callback);
    }

    synchronized public static void LoginAPI(Context context, LoginRequestModel requestModel, Callback callback) {
        initApiDomainOttofin(context).asLogin(initHeader(), requestModel).enqueue((Callback<LoginResponseModel>) callback);
    }

    synchronized public static void ResetAPI(Context context, ResetPinRequestModel requestModel, Callback callback) {
        initApiDomain(context).asReset(initHeader(), requestModel).enqueue((Callback<ResetPinResponseModel>) callback);
    }

    synchronized public static void CheckMerchantidAPI(Context context, CheckMerchantIdRequestModel requestModel, Callback callback) {
        initApiDomain(context).asCheckMerchantID(initHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void SecurityQuestionAPI(Context context, Callback callback) {
        initApiDomain(context).asGetSecurityQuestion().enqueue((Callback<SecurityQuestionResponseModel>) callback);
    }

    synchronized public static void LoginOtpAPI(Context context, LoginOtpRequestModel requestModel, Callback callback) {
        initApiDomain(context).asLoginOtp(initHeader(), requestModel).enqueue((Callback<LoginOtpResponseModel>) callback);
    }

    synchronized public static void RegisterOtpAPI(Context context, RegisterOtpRequestModel requestModel, Callback callback) {
        initApiDomain(context).asRegisterOtp(initHeader(), requestModel).enqueue((Callback<SignupOtpResponseModel>) callback);
    }

    synchronized public static void UpdateStatusAPI(Context context, UpdateStatusRequest requestModel, Callback callback) {
        initApiDomain(context).updateStatus(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<UpdateStatusResponse>) callback);
    }

    synchronized public static void ResendOtpLoginAPI(Context context, ResendOtpRegisterRequestModel requestModel, Callback callback) {
        initApiDomain(context).asResendOtpLogin(initHeader(), requestModel).enqueue((Callback<BaseResponseModel>) callback);
    }

    synchronized public static void RequestRegisterOtpAPI(Context context, ResendOtpRegisterRequestModel requestModel, Callback callback) {
        initApiDomain(context).asReRegisterOtp(initHeader(), requestModel).enqueue((Callback<BaseResponseModel>) callback);
    }

    synchronized public static void getUserSecurityQuestionAPI(Context context, UserSecurityQuestionRequestModel requestModel, Callback callback) {
        initApiDomain(context).getUserSecurityQuestion(initHeader(), requestModel).enqueue((Callback<UserSecurityQuestionResponseModel>) callback);
    }

    synchronized public static void getResetPinOtpAPI(Context context, ResetOtpPinRequestModel requestModel, Callback callback) {
        initApiDomain(context).getResetOtpPin(initHeader(), requestModel).enqueue((Callback<ResetOtpPinResponseModel>) callback);
    }

    //misc
    synchronized public static void GetCountryAPI(Context context, Callback callback) {
        initApiDomain(context).asGetCountry().enqueue((Callback<GetCountryResponseModel>) callback);
    }

    synchronized public static void GetProvinceAPI(Context context, int countryid, Callback callback) {
        initApiDomain(context).asGetProvince(countryid).enqueue((Callback<GetProvinceResponsetModel>) callback);
    }

    synchronized public static void GetCityAPI(Context context, long provinceid, Callback callback) {
        initApiDomain(context).asGetCity(provinceid).enqueue((Callback<GetCityResponseModel>) callback);
    }

    synchronized public static void GetDistrictAPI(Context context, long cityid, Callback callback) {
        initApiDomain(context).asGetDistrict(cityid).enqueue((Callback<GetDistrictResponseModel>) callback);
    }

    synchronized public static void GetVillageAPI(Context context, long districtid, Callback callback) {
        initApiDomain(context).asGetVillage(districtid).enqueue((Callback<GetVillageResponseModel>) callback);
    }

    synchronized public static void GetBankAPI(Context context, Callback callback) {
        initApiDomain(context).asGetBank().enqueue((Callback<GetBankResponseModel>) callback);
    }

    synchronized public static void GetBusinessCategoryAPI(Context context, Callback callback) {
        initApiDomain(context).asGetBusiness(initHeader()).enqueue((Callback<BusinessCategoryResponseModel>) callback);
    }

    synchronized public static void GetProfileAPI(Context context, Callback callback) {
        initApiDomain(context).asGetProfile(initHeader(), initAccessTokenHeader()).enqueue((Callback<ProfileResponseModel>) callback);
    }


    /**
     * PPOB
     */

    //region PPOB

    synchronized public static void ppobDenomList(Context context, String productType, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).ppobDenomList(initHeader(), initAccessTokenHeader(), productType, requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void ppobInquiry(Context context, String productType, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain30s(context).ppobInquiry(initHeader(), initAccessTokenHeader(), productType, requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void ppobPayment(Context context, String productType, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).ppobPayment(initHeader(), initAccessTokenHeader(), productType, requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void ppobQrPayment(Context context, String productType, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).ppobQrPayment(initHeader(), initAccessTokenHeader(), productType, requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void topUpDenomList(Context context, TopUpDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).topUpDenomList(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void gameDenomList(Context context, VoucherGameDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).gameDenomList(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    //endregion PPOB


    synchronized public static void GetPpobPulsaDenomAPI(Context context, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobPulsaInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobPulsaInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobPulsaPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobPulsaQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPpobPulsaOttoagDenomAPI(Context context, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaDenom2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobPulsaOttoagInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaInquiry2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobPulsaOttoagPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobPulsaOttoagQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaQrPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

//    synchronized public static void GetPpobPulsaOttoagAdviceAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
//        initApiDomain(context).asGetPpobPulsaAdvice2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
//    }


    synchronized public static void GetPpobPascaInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaPascaInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobPulsaPascaInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobPascaPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaPascaPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobPascaQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaPascaQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    public static void GetPpobPulsaPascaOttoagProductlistAPI(Context context, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaPascaDenom2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    public static void GetPpobPulsaPascaInquiryAPI2(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaPascaInquiry2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    public static void GetPpobPulsaPascaOttoagPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaPascaPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void GetPpobPulsaPascaOttoagQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPulsaPascaQrPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPpobPaketdataDenomAPI(Context context, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPaketdataDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobPaketdataInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPaketdataInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobPulsaInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobPaketdataPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPaketDataPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobPaketdataQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPaketDataQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPpobPaketDataOttoagDenomAPI(Context context, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPaketDataDenom2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobPaketDataOttoagInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPaketDataInquiry2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobPaketDataOttoagPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPaketDataPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobPaketDataOttoagQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobPaketDataQrPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

//    synchronized public static void GetPpobPaketDataOttoagAdviceAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
//        initApiDomain(context).asGetPpobPaketDataAdvice2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
//    }

    synchronized public static void GetPpobListrikTokenDenomAPI(Context context, PpobListrikDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTokenDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobListrikDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikTokenInquiryAPI(Context context, PpobListrikInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTokenInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobListrikInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikTokenPaymentAPI(Context context, PpobListrikPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTokenPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobListrikPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikTokenQrPaymentAPI(Context context, PpobListrikPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTokenQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagTokenDenomAPI(Context context, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTokenDenom2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagTokenInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTokenInquiry2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagTokenPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTokenPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagTokenQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTokenQrPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }


    synchronized public static void GetPpobListrikNgutangInquiryAPI(Context context, PpobListrikInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikNgutangInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void GetPpobListrikNgutangPaymentAPI(Context context, PpobListrikPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikNgutangPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobListrikPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikNgutangQrPaymentAPI(Context context, PpobListrikPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikNgutangQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagNgutangDenomAPI(Context context, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikNgutangDenom2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagNgutangInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikNgutangInquiry2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagNgutangPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikNgutangPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagNgutangQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikNgutangQrPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }


    synchronized public static void GetPpobListrikNotNgutangInquiryAPI(Context context, PpobListrikInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTdkNgutangInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobListrikInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikNotNgutangPaymentAPI(Context context, PpobListrikPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTdkNgutangPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobListrikPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikNotNgutangQrPaymentAPI(Context context, PpobListrikPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTdkNgutangQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagNonNgutangDenomAPI(Context context, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTdkNgutangDenom2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagNonNgutangInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTdkNgutangInquiry2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagNonNgutangPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTdkNgutangPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobListrikOttoagNonNgutangQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobListrikTdkNgutangQrPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPPobAirDenomAPI(Context context, PpobProductlistRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAirDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobAirProductlistResponseModel>) callback);
    }

    synchronized public static void GetPpobAirInquiryAPI(Context context, PpobAirInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAirInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobAirInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobAirPaymentAPI(Context context, PpobAirPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAirPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobAirPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobAirQrPaymentAPI(Context context, PpobAirPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAirQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPPobAirOttoagDenomAPI(Context context, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAirDenom2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobAirOttoagInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAirInquiry2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobAirOttoagPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAirPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobAirOttoagQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAirQrPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

//    synchronized public static void GetPpobAirOttoagAdvicePaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
//        initApiDomain(context).asGetPpobAirAdvice2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
//    }

    synchronized public static void GetPPobBpjsDenomAPI(Context context, PpobProductlistRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobBPJSDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobBpjsInquiryAPI(Context context, PpobAsuInquryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobBPJSInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobBpjsInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobBpjsPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobBPJSPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobBpjsPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobBpjsQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobBPJSQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPPobAsuDenomAPI(Context context, PpobProductlistRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAsuDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobAsuInquiryAPI(Context context, PpobAsuInquryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAsuInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobAsuPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAsuPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobAsuQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobAsuQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPpobTelkomInquiryAPI(Context context, PpobTelkomInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTelkomInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void GetPpobTelkomPaymentAPI(Context context, PpobTelkomPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTelkomPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void GetPpobTelkomQRAPI(Context context, PpobTelkomPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTelkomQR(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void GetPpobTelkomOttoagProductlistAPI(Context context, PpobOttoagDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTelkomDenom2(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobTelkomOttoagInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTelkomInquiry2(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void GetPpobTelkomOttoagPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTelkomPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void GetPpobTelkomOttoagQRAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTelkomQrPayment2(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

//    synchronized public static void GetPpobTelkomOttoagAdviceAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
//        initApiDomain(context).asGetPpobTelkomAdvice2(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
//    }

    synchronized public static void GetPPobCableDenomAPI(Context context, PpobProductlistRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobCableDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobCableInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobCableInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobCablePaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobCablePayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobCableQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobCableQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPPobScamDenomAPI(Context context, PpobProductlistRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobScamDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobScamInquiryAPI(Context context, PpobAsuInquryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobScamInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobScamPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobScamPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobScamQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobScamQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPPobEducationDenomAPI(Context context, PpobProductlistRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobeducationDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobEducationInquiryAPI(Context context, PpobAsuInquryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobEducationInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobEducationPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobEducationPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobEducationQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobEducationQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPpobTvCableInquiryAPI(Context context, PpobAsuInquryRequestModel requestModel, Callback callback) {
//        initApiDomain(context).asGetPpobeducationDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobTvCablePaymentAPI(Context context, PpobAsuPaymentRequestModel requestModel, Callback callback) {
//        initApiDomain(context).asGetPpobEducationPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobBpjsPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobTvCableQrPaymentAPI(Context context, PpobAsuPaymentRequestModel requestModel, Callback callback) {
//        initApiDomain(context).asGetPpobScamQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetPpobQrTvCableConfirmAPI(Context context, PpobQrPaymentRequestModel requestModel, Callback callback) {
//        initApiDomain(context).asQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<BasePaymentResponseModel>) callback);
    }

    //Top Up Emoney
    synchronized public static void GetTopUpEmoneyProduct(Context context, Callback callback) {
        initApiDomain(context).asGetTopUpEmoneyProduct(initHeader(), initAccessTokenHeader()).enqueue((Callback<TopUpEmoneyProductResponseModel>) callback);
    }

    synchronized public static void GetTopUpEmoneyDenom(Context context, TopUpDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetTopUpEmoneyDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<TopUpEmoneyDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobTopUpInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTopUpInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobTopUpPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTopUpPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobTopUpQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTopUpQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }

    synchronized public static void GetFAQTopUp(Context context, FaqTopUpRequest requestModel, Callback callback) {
        initApiDomain(context).serviceFAQTopUp( initHeader(), initAccessTokenHeader(), requestModel.isOttopay_instruction()).enqueue(callback);
    }

    //Voucher Game
    synchronized public static void GetVoucherGameProductList(Context context, Callback callback) {
        initApiDomain(context).asGetVoucherGameProductList(initHeader(), initAccessTokenHeader()).enqueue((Callback<VoucherGameResponse>) callback);
    }

    synchronized public static void GetVoucherGameDenom(Context context, VoucherGameDenomRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetVoucherGameDenom(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<TopUpEmoneyDenomResponseModel>) callback);
    }

    synchronized public static void GetPpobVoucherGameInquiryAPI(Context context, PpobOttoagInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobVoucherGameInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagInquiryResponseModel>) callback);
    }

    synchronized public static void GetPpobVoucherGamePaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobVoucherGamePayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobVoucherGameQrPaymentAPI(Context context, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobVoucherGameQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PPobPaymentQrResponseModel>) callback);
    }


    //Receipt PPOB
    synchronized public static void GetReceipt(Context context, String product, PpobReceiptRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetReceipt(initHeader(), initAccessTokenHeader(), requestModel, product).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    //Advice PPOB
    synchronized public static void GetPpobOttoagAdviceAPI(Context context, String product, PpobOttoagPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobOttoagAdvice(initHeader(), initAccessTokenHeader(), requestModel, product).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    //Transaction Advice
    synchronized public static void GetPpobTransactionAdviceAPI(Context context, PpobTransactionAdviceModel requestModel, Callback callback) {
        initApiDomain(context).asGetPpobTransactionAdvice(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<PpobOttoagPaymentResponseModel>) callback);
    }

    synchronized public static void GetPpobQrPaymentConfirmAPI(Context context, PpobQrPaymentRequestModel requestModel, Callback callback) {
        initApiDomain(context).asQrPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<BasePaymentResponseModel>) callback);
    }

    synchronized public static void UpdateAddress(Context context, String auth, AddressEditRequestModel model, Callback callback) {
        initApiDomain(context).asUpdateAddress(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void getOmsetHistory(Context context, OmsetHistoryRequestModel model, Callback callback) {
        initApiDomain(context).asGetOmsetHistory(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void TransOmzetToSaldo(Context context, OmzetSaldoRequestModel model, Callback callback) {
        initApiDomain(context).asTrasnOmzetSaldo(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void UpgradeAccountAPI(Context context, RegisterUpgradeAccountRequestModel requestModel, Callback callback) {
        initApiDomain(context).asUpgradeAccount(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<RegisterUpgradeAccountResponseModel>) callback);
    }

    synchronized public static void GetOmzetStat(Context context, Callback callback) {
        initApiDomain(context).asGetOmzetStat(initHeader(), initAccessTokenHeader()).enqueue((callback));
    }

    synchronized public static void UpdateProfile(Context context, UpdateProfileRequestModel model, Callback callback) {
        initApiDomain(context).asUpdateProfile(initHeader(), initAccessTokenHeader(), model).enqueue((callback));
    }

    //QR
    synchronized public static void doGetQrString(Context context, String url, GetQrStringRequestModel requestModel, Callback callback) {
        initApiDomain(context).requestQr(url, requestModel.getPan(), requestModel.getBill_id(), requestModel.getTrx_amount(), requestModel.getTerminal_id()).enqueue((Callback<GetQrStringResponseModel>) callback);
    }

    synchronized public static void GetPayQrInquiry(Context context, PayQrInquiryRequestModel model, Callback callback) {
        initApiDomain(context).asGetPayQrInquiry(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void GetPayQrPayment(Context context, PaymentOfflineConfirmationRequestModel model, Callback callback) {
        initApiDomain(context).asGetPayQrPayment(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void doCancelQrPayment(Context context, CancelQrTransactionRequestModel model, Callback callback) {
        initApiDomain(context).asQrCancelPayment(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    //walet swalow
    synchronized public static void GetWalletDataAPI(Context context, Callback callback) {
        initApiDomain(context).asGetWallet(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void GetBankList(Context context, Callback callback) {
        initApiDomain(context).asGetBankList(initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void UpdateBank(Context context, BankEditRequestModel model, Callback callback) {
        initApiDomain(context).asUpdateBank(initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void AddBank(Context context, BankEditRequestModel model, Callback callback) {
        initApiDomain(context).asAddBank(initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void GetWalletHistory(Context context, OmsetHistoryRequestModel model, Callback callback) {
        initApiDomain(context).asGetWalletHistory(initAccessTokenHeader(), initHeader(), model).enqueue(callback);
    }

    synchronized public static void GetAddressList(Context context, Callback callback) {
        initApiDomain(context).asGetAddressList(initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void getPhone(Context context, LoginQrRequestModel model, Callback callback) {
        initApiDomain(context).asGetPhone(model).enqueue(callback);
    }

    synchronized public static void CreateAddress(Context context, AddAddressRequestModel model, Callback callback) {
        initApiDomain(context).asCreateAddress(initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void DeleteAddress(Context context, DeleteAddressRequestModel model, Callback callback) {
        initApiDomain(context).asDeleteAddress(initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void UpdatePin(Context context, UpdatePinRequestModel model, Callback callback) {
        initApiDomain(context).asUpdatePin(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void GetQrString(Context context, QrStringRequestModel model, Callback callback) {
        initApiDomain(context).asGetQrString(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void GetQrString(Context context, Callback callback) {
        initApiDomain(context).asGetQrString(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void GetVersion(Context context, Callback callback) {
        initApiDomain(context).asGetVersion(new CheckVersionRequestModel(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_CODE)).enqueue(callback);
    }

    //wallet transfer
    synchronized public static void GetTfWalletInquiry(Context context, TfWalletInquiryRequestModel model, Callback callback) {
        initApiDomain(context).asGetTfWalletInquiry(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void GetTfWalletConf(Context context, TfWalletConfRequestModel model, Callback callback) {
        initApiDomain(context).asGetTfWalletConf(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void GetTfBankInquiry(Context context, TransferToBankInquiryRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetTfBankInquiry(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void GetTfBankConf(Context context, TransferToBankConfirmationRequestModel requestModel, Callback callback) {
        initApiDomain(context).asGetTfBankConfirmation(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void GetWalletType(Context context, Callback callback) {
        initApiDomain(context).asGetWalletTypeList().enqueue(callback);
    }

    synchronized public static void GetFav(Context context, String trgt, Callback callback) {
        initApiDomain(context).asGetPpobFav(initHeader(), initAccessTokenHeader(), trgt).enqueue(callback);
    }

    synchronized public static void SaveFav(Context context, FavoriteAddRequestModel requestModel, Callback callback) {
        initApiDomain(context).asSaveFavItem(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void DeleteFav(Context context, int id, Callback callback) {
        initApiDomain(context).asDeleteFavItem(initHeader(), initAccessTokenHeader(), id).enqueue(callback);
    }

    synchronized public static void Logout(Context context, Callback callback) {
        initApiDomain(context).asLogout(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void Banner(Context context, Callback callback) {
        initApiDomain15s(context).asGetBanner(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void paymentJurnal(Context context, PaymentJurnalRequestModel requestModel, Callback callback) {
        initApiDomain(context).paymentJurnal( initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void reversalPaymentAPI(Context context, ReversalRequestModel model, Callback callback) {
        initApiDomain(context).reversalPayment(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    /**
     * Indomaret
     */

    synchronized public static void IndomaretQrPurchase(Context context, IndomaretQrPurchaseRequestModel requestModel, Callback callback) {
        initApiDomain(context).asIndomaretQrPurchase(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void IndomaretQrPurchaseCancel(Context context, IndomaretQrPurchaseRequestModel requestModel, Callback callback) {
        initApiDomain(context).asIndomaretQrPurchaseCancel(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void IndomaretQrPurchaseStatus(Context context, IndomaretQrStatusRequestModel requestModel, Callback callback) {
        initApiDomain(context).asIndomaretQrPurchaseStatus(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }


    /**
     * Alfamart
     */

    synchronized public static void AlfamartQrPurchase(Context context, AlfamartPurchaseRequest requestModel, Callback callback) {
        initApiDomain(context).asAlfamartQrPurchase(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void AlfamartQrPurchaseStatus(Context context, AlfamartStatusRequest requestModel, Callback callback) {
        initApiDomain(context).asAlfamartQrPurchaseStatus(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }



    /**
     * INBOX
     */

    //Get Inbox List
    synchronized public static void InboxList(Context context, int page, Callback callback) {
        initApiDomain(context).asInboxList(initHeader(), initAccessTokenHeader(), page).enqueue(callback);
    }

    //Inbox Read
    synchronized public static void InboxRead(Context context, InboxReadRequest inboxReadRequest, Callback callback) {
        initApiDomain(context).asInboxRead(initHeader(), initAccessTokenHeader(), inboxReadRequest).enqueue(callback);
    }

    //Inbox Read Bulk
    synchronized public static void inboxReadBulk(Context context, InboxUpdateBulkRequest inboxUpdateBulkRequest, Callback callback) {
        initApiDomain(context).inboxReadBulk(initHeader(), initAccessTokenHeader(), inboxUpdateBulkRequest).enqueue(callback);
    }

    //Inbox Delete Bulk
    synchronized public static void inboxDeleteBulk(Context context, InboxUpdateBulkRequest inboxUpdateBulkRequest, Callback callback) {
        initApiDomain(context).inboxDeleteBulk(initHeader(), initAccessTokenHeader(), inboxUpdateBulkRequest).enqueue(callback);
    }



    /**
     * TOKO OTTOPAY
     */

    //Store List
    synchronized public static void StoreList(Context context, Callback callback) {
        initApiDomain15s(context).storeList(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    //ReOrder Product List
    synchronized public static void ReOrderProductList(Context context, ProductListRequest productListRequest, Callback callback) {
        initApiDomain(context).asReOrderProductList(initHeader(), initAccessTokenHeader(), productListRequest).enqueue(callback);
    }

    //Add To Card
    synchronized public static void AddToCart(Context context, AddToCardRequest requestModel, Callback callback) {
        initApiDomain(context).addToCart(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    //Remove From Card
    synchronized public static void RemoveFromCart(Context context, String storeName, RemoveCardRequest requestModel, Callback callback) {
        initApiDomain(context).removeFromCart(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    //Card
    synchronized public static void Cart(Context context, Callback callback) {
        initApiDomain(context).cart(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    //Order Confirm
    synchronized public static void OrderConfirm(Context context, OrderConfirmRequest requestModel, Callback callback) {
        initApiDomain(context).orderConfirm(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    //Order Payment
    synchronized public static void OrderPayment(Context context, OrderPaymentRequest requestModel, Callback callback) {
        initApiDomain(context).orderPayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    //Order Pay Off
    synchronized public static void OrderPayOff(Context context, OrderPayOffRequest requestModel, Callback callback) {
        initApiDomain(context).orderPayOff(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    //Order History
    synchronized public static void OrderHistory(Context context, Callback callback) {
        initApiDomain(context).orderHistory(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    //Order History Detail
    synchronized public static void OrderHistoryDetail(Context context, OrderHistoryDetailRequest requestModel, Callback callback) {
        initApiDomain(context).orderHistoryDetail(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }


    public static void getCategoriesAPI(Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceGetCategories(initAccessTokenHeader()).enqueue(callback);
    }

    public static void getProductListAPI(String path, ProductListRequestModel model, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceGetProducts(initAccessTokenHeader(), path, model).enqueue(callback);
    }

    public static void getProductDetailAPI(int path, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceGetProductDetail(initAccessTokenHeader(), path).enqueue(callback);
    }

    public static void getCartListAPI(Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceGetCartList(initAccessTokenHeader()).enqueue(callback);
    }

    public static void AddCartAPI(AddCartRequestModel model, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceAddToCart(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void DeleteCartAPI(String cartItemId, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceDeleteCart(initAccessTokenHeader(),cartItemId).enqueue(callback);
    }

    public static void UpdateCartAPI(UpdateCartRequestModel model, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceUpdateCart(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void OrderConfirmation(OrderConfirmationRequestModel model, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceOrderConfirmation(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void ShippingCostAPI(ShippingCostRequestModel model, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceShippingCost(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void ConfirmOrderPaidAPI(ConfirmPaid model, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceConfirmOrderPaid(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void GetProvinceAPI(Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceGetProvince(initAccessTokenHeader()).enqueue(callback);
    }

    public static void GetCityAPI(String provinceId, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceGetCity(initAccessTokenHeader(), provinceId).enqueue(callback);
    }

    public static void GetDistrictAPI(String cityId, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceGetDistrict(initAccessTokenHeader(), cityId).enqueue(callback);
    }

    public static void GetMailNoAPI(AddressRequestModel model, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceGetMailNo(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void CreateShippingAddressAPI(ShippingAddressRequestModel model, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceCreateShippingAddress(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void ShippingAddressListAPI(Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceGetShippingAddress(initAccessTokenHeader()).enqueue(callback);
    }

    public static void UpdateAddressAPI(ShippingAddressRequestModel model, String addressId, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceUpdateShippingAddress(initAccessTokenHeader(), model, addressId).enqueue(callback);
    }

    public static void DeleteAddressAPI(String addressId, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceDeleteShippingAddress(initAccessTokenHeader(), addressId).enqueue(callback);
    }

    public static void GetOrderStatusAPI(Map<String, String> params, Callback callback) {
        initApiDomain(OttoMartApp.getContext()).serviceOrderStatus(initAccessTokenHeader(), params).enqueue(callback);
    }
}
