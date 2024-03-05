package com.otto.mart.api;

import android.content.Context;
import android.content.Intent;

import app.beelabs.com.codebase.base.BaseApi;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.otto.mart.BuildConfig;
import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.model.APIModel.Request.*;
import com.otto.mart.model.APIModel.Request.bank.AddBankDepositRequest;
import com.otto.mart.model.APIModel.Request.cashOut.CashOutInputConfirmationRequest;
import com.otto.mart.model.APIModel.Request.cashOut.CashOutInputRequest;
import com.otto.mart.model.APIModel.Request.inbox.InboxReadRequest;
import com.otto.mart.model.APIModel.Request.multibank.AddBankAccountRequest;
import com.otto.mart.model.APIModel.Request.multibank.AddBankInquiryRequest;
import com.otto.mart.model.APIModel.Request.multibank.InqueryTransferRequest;
import com.otto.mart.model.APIModel.Request.multibank.IssuerLinkedBalance;
import com.otto.mart.model.APIModel.Request.multibank.IssuerLinkedConfrim;
import com.otto.mart.model.APIModel.Request.multibank.IssuerLinkedInquiryRequest;
import com.otto.mart.model.APIModel.Request.multibank.IssuerLinkedRequest;
import com.otto.mart.model.APIModel.Request.multibank.TransferMultiBankConfrimRequest;
import com.otto.mart.model.APIModel.Request.multibank.ValidateQrRequest;
import com.otto.mart.model.APIModel.Request.multibank.WithdrawRequest;
import com.otto.mart.model.APIModel.Request.qr.CheckStatusQrRequest;
import com.otto.mart.model.APIModel.Request.qr.QrPaymentRequest;
import com.otto.mart.model.APIModel.Request.refund.MerchantRefundRequest;
import com.otto.mart.model.APIModel.Request.register.SignUpRequest;
import com.otto.mart.model.APIModel.Request.revenue.WithdrawRevenueRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferBankInquiryRequest;
import com.otto.mart.model.APIModel.Request.transfer.TransferBankPaymentRequest;
import com.otto.mart.model.APIModel.Response.*;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.history.BankHistoryResponse;
import com.otto.mart.model.APIModel.Response.history.OmzetHistoryResponse;
import com.otto.mart.model.APIModel.Response.merchant.featureProduct.FeatureProductResponse;
import com.otto.mart.model.APIModel.Response.merchant.theme.MerchantThemeResponse;

import com.otto.mart.model.APIModel.Response.multibank.HistoryAccountResponse;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.SystemUtil;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.settings.LanguageIdConverter;

import retrofit2.Callback;

import java.util.HashMap;
import java.util.Map;

public class OttoKonekAPI extends BaseApi implements GLOBAL {

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

    private static OttoKonekServices initApiDomain(Context context) {
        getInstance().setApiDomain(BuildConfig.OTTOPOINT_SERVER_URL);
        getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttoKonekServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
        getInstance().setApiDomain(BuildConfig.OTTOKONEK_SERVER_URL);
        return (OttoKonekServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttoKonekServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }


    /**
     * Auth
     */

    //region Auth

    //Login
    synchronized public static void login(Context context, LoginRequestModel requestModel, Callback callback) {
        initApiDomain(context).login(initHeader(), requestModel).enqueue((Callback<LoginResponseModel>) callback);
    }

    synchronized public static void versionCheck(Context context, Callback callback) {
        initApiDomain(context).versionCheck(new CheckVersionRequestModel(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_CODE)).enqueue(callback);
    }

    //Login OTP
    synchronized public static void loginOTP(Context context, LoginOtpRequest loginOtpRequest, Callback callback) {
        initApiDomain(context).loginOTP(initHeader(), loginOtpRequest).enqueue((Callback<LoginOtpResponseModel>) callback);
    }

    synchronized public static void logout(Context context, Callback callback) {
        initApiDomain(context).logout(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void resendOtp(Context context, ResendOtpRegisterRequestModel requestModel, Callback callback) {
        initApiDomain(context).resendOTP(initHeader(), requestModel).enqueue((Callback<BaseResponseModel>) callback);
    }

    synchronized public static void forgotPinOTP(Context context, ResetOtpPinRequestModel requestModel, Callback callback) {
        initApiDomain(context).forgotPinOtp(initHeader(), requestModel).enqueue((Callback<ResetOtpPinResponseModel>) callback);
    }

    synchronized public static void forgotPinReset(Context context, ResetPinRequestModel requestModel, Callback callback) {
        initApiDomain(context).forgotPin(initHeader(), requestModel).enqueue((Callback<ResetPinResponseModel>) callback);
    }

    synchronized public static void changePin(Context context, UpdatePinRequestModel model, Callback callback) {
        initApiDomain(context).changePin(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    /**
     * Register
     */

    synchronized public static void searchMerchant(Context context, CheckMerchantIdRequestModel body, Callback callback) {
        initApiDomain(context).searchMerchantService(body).enqueue(callback);
    }

    synchronized public static void postAcquisition(Context context, PostAcquisitionRequest body, Callback callback) {
        initApiDomain(context).postAcquisitionService(initHeader(), body).enqueue(callback);
    }

    synchronized public static void signUp(Context context, SignUpRequest body, Callback callback) {
        initApiDomain(context).signUpService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void signUpOtp(Context context, RegisterOtpRequestModel body, Callback callback) {
        initApiDomain(context).signUpOTPService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void resendOtpRegister(Context context, ResendOtpRegisterRequestModel body, Callback callback) {
        initApiDomain(context).resendOtpService(initHeader(), body).enqueue(callback);
    }

    synchronized public static void getRegional(Context context, Callback callback) {
        initApiDomain(context).getAllRegional(initHeader()).enqueue(callback);
    }

    synchronized public static void getProvince(Context context, String code, Callback callback) {
        initApiDomain(context).getProvince(initHeader(), code).enqueue(callback);
    }

    synchronized public static void getMunicipality(Context context, String code, Callback callback) {
        initApiDomain(context).getMunicipality(initHeader(), code).enqueue(callback);
    }

    synchronized public static void getBarangays(Context context, String code, Callback callback) {
        initApiDomain(context).getBarangays(initHeader(), code).enqueue(callback);
    }

    synchronized public static void getListAccountBankWithBin(Context context, String bin, Callback callback) {
        initApiDomain(context).getListAccounBankWithBin(initHeader(), initAccessTokenHeader(), bin).enqueue(callback);
    }

    synchronized public static void getListAccountBank(Context context, Callback callback) {
        initApiDomain(context).getListAccounBank(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void getListBankPartner(Context context, Callback callback) {
        initApiDomain(context).getListPartnerBank(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void getListAccountType(Context context, Callback callback) {
        initApiDomain(context).getListAccountType(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void getTransferBankList(Context context, Callback callback) {
        initApiDomain(context).getTransferBankList(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void getTransferBankListMid(String bin, Context context, Callback callback) {
        initApiDomain(context).getTransferBankListMid(initHeader(), initAccessTokenHeader(), bin).enqueue(callback);
    }

    //endregion Auth


    /**
     * Emoney
     */

    //region Emoney

    //Revenue History
    synchronized public static void revenueHistory(Context context, String dateFrom, String dateTo,
                                                   String paymentMethod, String transactionType, int limit, int page, Callback callback) {
        initApiDomain(context).revenueHistory(initHeader(), initAccessTokenHeader(), dateFrom, dateTo, paymentMethod, transactionType,
                limit, page).enqueue((Callback<OmzetHistoryResponse>) callback);
    }

    synchronized public static void revenueHistoryAccount(Context context, String accountNumber, String dateFrom, String dateTo, int limit, int page, Callback callback) {
        initApiDomain(context).revenueHistoryAccountList(initHeader(), initAccessTokenHeader(), accountNumber, dateFrom, dateTo,
                limit, page).enqueue((Callback<HistoryAccountResponse>) callback);
    }


    //Bank History
    synchronized public static void bankHistory(Context context, String accountNumber, String dateFrom, String dateTo, int limit, int page, Callback callback) {
        initApiDomain(context).bankHistory(initHeader(), initAccessTokenHeader(), accountNumber, dateFrom, dateTo, limit, page).enqueue((Callback<BankHistoryResponse>) callback);
    }

    //endregion Emoney


    /**
     * Dashboard
     */

    //region Dashboard

    //Banner
    synchronized public static void banner(Context context, Callback callback) {
        initApiDomain(context).banner(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    //Merchant Theme
    synchronized public static void merchantTheme(Context context, Callback callback) {
        initApiDomain(context).merchantTheme(initHeader(), initAccessTokenHeader()).enqueue((Callback<MerchantThemeResponse>) callback);
    }

    //Feature Product
    synchronized public static void featureProduct(Context context, Callback callback) {
        initApiDomain(context).featureProduct(initHeader(), initAccessTokenHeader()).enqueue((Callback<FeatureProductResponse>) callback);
    }

    //Balance
    synchronized public static void revenue(Context context, Callback callback) {
        initApiDomain(context).revenue(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    //Balance
    synchronized public static void balance(Context context, Callback callback) {
        initApiDomain(context).balance(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    //endregion Dashboard


    /**
     * Payment QR
     */

    //Qr String
    synchronized public static void qrString(Context context, QrStringRequestModel requestModel, Callback callback) {
        initApiDomain(context).qrString(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void qrCheckStatus(Context context, PpobTransactionAdviceModel body, Callback callback) {
        initApiDomain(context).qrCheckStatusService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void qrRefund(Context context, MerchantRefundRequest body, Callback callback) {
        initApiDomain(context).qrRefundService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void payQrQrisInquiry(Context context, PayQrInquiryRequestModel model, Callback callback) {
        initApiDomain(context).payQrInqury(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void payQrValidate(Context context, ValidateQrRequest model, Callback callback) {
        initApiDomain(context).payQrValidate(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void payQrPayment(Context context, QrPaymentRequest model, Callback callback) {
        initApiDomain(context).payQrPayment(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }

    synchronized public static void checkStatusQrPayment(Context context, CheckStatusQrRequest model, Callback callback) {
        initApiDomain(context).checkStatusQrPayment(initHeader(), initAccessTokenHeader(), model).enqueue(callback);
    }


    /**
     * Cash Out
     */

    //Cash Out Input
    synchronized public static void cashOutInput(Context context, CashOutInputRequest requestModel, Callback callback) {
        initApiDomain(context).cashOutInput(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    //Cash Out Input Confirmation
    synchronized public static void cashOutInputConfirmation(Context context, CashOutInputConfirmationRequest requestModel, Callback callback) {
        initApiDomain(context).cashOutInputConfirmation(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    //Cash Out List
    synchronized public static void cashOutList(Context context, Callback callback) {
        initApiDomain(context).cashOutList(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }


    /**
     * Revenue
     */

    //Withdraw Revenue Payment
    synchronized public static void withdrawRevenuePayment(Context context, WithdrawRevenueRequest requestModel, Callback callback) {
        initApiDomain(context).withdrawRevenuePayment(initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    /**
     * Inbox
     */

    synchronized public static void notificationList(Context context, int page, Callback callback) {
        initApiDomain(context).notificationListService(initHeader(), initAccessTokenHeader(), page).enqueue(callback);
    }

    synchronized public static void notificationActionAll(Context context, String action, Callback callback) {
        initApiDomain(context).notificationActionAllService(initHeader(), initAccessTokenHeader(), action).enqueue(callback);
    }

    synchronized public static void notificationReadBulk(Context context, InboxReadRequest body, Callback callback) {
        initApiDomain(context).notificationReadBulkService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void notificationAction(Context context, InboxReadRequest body, Callback callback) {
        initApiDomain(context).notificationActionService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    /**
     * Bank
     */
    synchronized public static void bankList(Context context, Callback callback) {
        initApiDomain(context).bankListService(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void bankAccountList(Context context, Callback callback) {
        initApiDomain(context).bankAccountListService(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void addBank(Context context, AddBankDepositRequest body, Callback callback) {
        initApiDomain(context).addBankService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void editBank(Context context, AddBankDepositRequest body, Callback callback) {
        initApiDomain(context).editBankService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    /**
     * Transfer Bank
     */
    synchronized public static void transferBankInquiry(Context context, TransferBankInquiryRequest body, Callback callback) {
        initApiDomain(context).transferBankInquiryService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void transferBankPayment(Context context, TransferBankPaymentRequest body, Callback callback) {
        initApiDomain(context).transferBankPaymentService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void updateStatus(Context context, UpdateStatusRequest requestModel, Callback callback) {
        initApiDomain(context).updateStatus(initHeader(), initAccessTokenHeader(), requestModel).enqueue((Callback<UpdateStatusResponse>) callback);
    }

    /**
     * Contact Us
     */
    synchronized public static void contactUs(Callback callback) {
        initApiDomain(OttoMartApp.getContext()).contactUsService(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    /**
     * Profile
     */
    synchronized public static void profile(Context context, Callback callback) {
        initApiDomain(context).profileService(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void profileUpdate(Context context, UpdateProfileRequestModel body, Callback callback) {
        initApiDomain(context).profileUpdateService(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void issuerLinkedRequest(Context context, IssuerLinkedRequest body, Callback callback) {
        initApiDomain(context).issuerLinkedRequest(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void issuerLinkedConfrim(Context context, IssuerLinkedConfrim body, Callback callback) {
        initApiDomain(context).issuerLinkedConfrim(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void issuerLinkedBalance(Context context, IssuerLinkedBalance body, Callback callback) {
        initApiDomain(context).issuerLinkedBalance(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void addBankAccount(Context context, AddBankAccountRequest body, Callback callback) {
        initApiDomain(context).postAddBankAccount(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void inqueryTransferBank(Context context, InqueryTransferRequest body, Callback callback) {
        initApiDomain(context).postTransferBankInquiry(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void addBankInquery(Context context, AddBankInquiryRequest body, Callback callback) {
        initApiDomain(context).postBankAddInquiry(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void confrimTransferBank(Context context, TransferMultiBankConfrimRequest body, Callback callback) {
        initApiDomain(context).postConfrimTransferBank(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void withdraw(Context context, WithdrawRequest body, Callback callback) {
        initApiDomain(context).withdraw(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }

    synchronized public static void getContactReceipt(Context context, Callback callback) {
        initApiDomain(context).getContactReceipt(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void issuerLinkedInquiry(Context context, IssuerLinkedInquiryRequest body, Callback callback) {
        initApiDomain(context).issuerLinkedAddIquiry(initHeader(), initAccessTokenHeader(), body).enqueue(callback);
    }


}