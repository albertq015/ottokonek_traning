package com.otto.mart.api;

import com.otto.mart.BuildConfig;
import com.otto.mart.model.APIModel.Request.*;
import com.otto.mart.model.APIModel.Request.bank.AddBankDepositRequest;
import com.otto.mart.model.APIModel.Request.bogasari.BogasariInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.bogasari.BogasariPaymentRequestModel;
import com.otto.mart.model.APIModel.Request.donasi.DonasiInquiryRequest;
import com.otto.mart.model.APIModel.Request.donasi.DonasiPaymentRequest;
import com.otto.mart.model.APIModel.Request.donasi.DonasiQrStringRequest;
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
import com.otto.mart.model.APIModel.Response.*;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.balance.BalanceResponse;
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
import com.otto.mart.model.APIModel.Response.oasis.CheckOasisStatusResponse;
import com.otto.mart.model.APIModel.Response.ottocash.StatusOttocashResponse;
import com.otto.mart.model.APIModel.Response.payment.AlfamartTokenResponse;
import com.otto.mart.model.APIModel.Response.payment.QrQrisInquiryResponse;
import com.otto.mart.model.APIModel.Response.ppob.PpobCheckStatusQRPaymentResponse;
import com.otto.mart.model.APIModel.Response.ppob.PpobInquiryResponse;
import com.otto.mart.model.APIModel.Response.ppob.PpobUserGuideResponse;
import com.otto.mart.model.APIModel.Response.refund.CheckRefundStatusResponse;
import com.otto.mart.model.APIModel.Response.refund.MerchantRefundResponse;
import com.otto.mart.model.APIModel.Response.setting.LanguageSettingResponse;
import com.otto.mart.model.APIModel.Response.transfer.TransferBankInquiryResponse;
import com.otto.mart.model.APIModel.Response.transfer.TransferLimitResponse;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

import static com.otto.mart.BuildConfig.OTTOFIN_QRIS_API_VERSION;

public interface OttofinApiServices {

    String API_VERSION = BuildConfig.OTTOFIN_API_VERSION;
    String AUTH_API_VERSION = BuildConfig.OTTOFIN_AUTH_API_VERSION;
    String AUTH_API_VERSION_1_1 = "v1.1";
    String QRIS_API_VERSION = OTTOFIN_QRIS_API_VERSION;
    String QR_STRING_API_VERSION = "v0.1.1";
    String SEARCH_MERCHANT_QRIS_API_VERSION = "v1.1";
    String OCBI_API_VERSION = BuildConfig.OCBI_API_VERSION;
    String OCBI_API_VERSION_2 = "v0.2.0";
    String PAYMENT_API_VERSION = "v0.1.1";
    String API_VERSION_3 = "v0.3.0";

    /**
     * App Config
     */

    //App Cloner
    @GET("ottopay/" + API_VERSION + "/ottopay-mart/app-package")
    Call<AppClonerListResponse> appClonerList(@HeaderMap Map<String, String> key);


    //Check Version
    @POST("ottopay/" + API_VERSION + "/ottopay-mart/check-version")
    Call<CheckVersionResponseModel> versionCheck(@Body CheckVersionRequestModel model);


    /**
     * Auth
     */

    //region Auth

    //Login
    @POST("auth/" + AUTH_API_VERSION_1_1 + "/ottopay/login")
    Call<LoginResponseModel> login(@HeaderMap Map<String, String> key, @Body LoginRequestModel model);

    //Login OTP
    @POST("auth/" + AUTH_API_VERSION_1_1 + "/ottopay/loginotp")
    Call<LoginOtpResponseModel> loginOTP(@HeaderMap Map<String, String> key, @Body LoginOtpRequest model);

    //Logout
    @POST("auth/" + AUTH_API_VERSION + "/ottopay/logout")
    Call<BaseResponseModel> logout(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken);

    //Resend OTP
    @POST("auth/" + AUTH_API_VERSION + "/ottopay/resend-otp")
    Call<BaseResponseModel> resendOTP(@HeaderMap Map<String, String> key, @Body ResendOtpRegisterRequestModel model);

    //Forgot Pin Security Question
    @POST("auth/" + AUTH_API_VERSION + "/ottopay/forgotpin/securityquestion")
    Call<ForgotPinSecurityQuestionResponse> forogotPinSecurityQuestion(@HeaderMap Map<String, String> key, @Body UserSecurityQuestionRequestModel model);

    //Forgot PIN OTP
    @POST("auth/" + AUTH_API_VERSION + "/ottopay/forgotpin/otp")
    Call<ResetOtpPinResponseModel> forgotPinOTP(@HeaderMap Map<String, String> key, @Body ResetOtpPinRequestModel model);

    //Forgot Pin Reset
    @POST("auth/" + AUTH_API_VERSION + "/ottopay/forgotpin/reset")
    Call<ResetPinResponseModel> forgotPinReset(@HeaderMap Map<String, String> key, @Body ResetPinRequestModel model);


    //Merchant Theme
    @GET("ottopay/" + OCBI_API_VERSION_2 + "/merchant/theme")
    Call<MerchantThemeResponse> merchantTheme(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    //Feature Product
    @GET("ottopay/" + OCBI_API_VERSION_2 + "/merchant/feature/product")
    Call<FeatureProductResponse> featureProduct(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    //Language Setting
    @POST("ottopay/" + API_VERSION + "/language/change")
    Call<LanguageSettingResponse> languageSetting(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body LanguageSettingRequest model);

    //Change PIN
    @POST("auth/" + AUTH_API_VERSION + "/ottopay/change-pin")
    Call<DataEmptyResponseModel> asUpdatePin(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body UpdatePinRequestModel model);

    //endregion Auth



    /**
     * Register
     */

    //region Register

    //Search Merchant
    @POST("auth/" + SEARCH_MERCHANT_QRIS_API_VERSION + "/ottopay/signup/searchmerchant")
    Call<SearchMerchantResponse> searchMerchant(@HeaderMap Map<String, String> key, @Body CheckMerchantIdRequestModel model);

    //Sign Up
    @POST("auth/" + AUTH_API_VERSION + "/ottopay/signup")
    Call<SignupResponseModel> signUp(@HeaderMap Map<String, String> key, @Body SignupRequestModel model);

    //Sign Up Otp
    @POST("auth/" + AUTH_API_VERSION + "/ottopay/signupotp")
    Call<SignupOtpResponseModel> signUpOtp(@HeaderMap Map<String, String> key, @Body RegisterOtpRequestModel model);

    //endregion Register


    /**
     * Emoney
     */

    //region Emoney

    //Omset Stat
    @GET("ottopay/" + API_VERSION + "/ottopay-mart/emoney/omset-stat")
    Call<OmzetStatResponseModel> omzetStatus(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    //Wallet
    @GET("ottopay/" + API_VERSION + "/ottopay-mart/emoney/summary")
    Call<WalletResponseModel> emoneySummary(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    //Transfer Omzet to Wallet
    @POST("op-emoney/" + OCBI_API_VERSION + "/omzet/transfer-to-wallet")
    Call<OmzetSaldoResponseModel> transferOmzetToWallet(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body OmzetSaldoRequestModel model);

    //Omzet History
    @GET("op-emoney/" + PAYMENT_API_VERSION + "/transaction/history/omzet")
    Call<OmzetHistoryResponse> omzetHistory(@HeaderMap Map<String, String> token, @HeaderMap Map<String,  String> accessToken,
                                                 @Query("dateFrom") String dateFrom, @Query("dateTo") String dateTo,
                                                 @Query("paymentMethod") String paymentMethod, @Query("transactionType") String transactionType,
                                                 @Query("limit") int limit, @Query("page") int page);

    //Wallet History
    @GET("op-emoney/" + PAYMENT_API_VERSION + "/transaction/history/deposit")
    Call<OmzetHistoryResponse> walletHistory(@HeaderMap Map<String, String> token, @HeaderMap Map<String,  String> accessToken,
                                             @Query("dateFrom") String dateFrom, @Query("dateTo") String dateTo,  @Query("page") int page);

    //OttoCash History
    @GET("op-emoney/" + OCBI_API_VERSION + "/transaction/history/ocbi")
    Call<OttoCashHistoryResponse> ottoCashHistory(@HeaderMap Map<String, String> token, @HeaderMap Map<String,  String> accessToken,
                                                  @Query("dateFrom") String dateFrom, @Query("dateTo") String dateTo,  @Query("page") int page);

    //History Detail
    @POST("ottopay/" + PAYMENT_API_VERSION + "/ottomart/ppob/advice-ottoag")
    Call<HistoryDetailResponse> historyDetail(@HeaderMap Map<String, String> token, @HeaderMap Map<String,  String> accessToken, @Body HistoryDetailRequest historyDetailRequest);

    //endregion Emoney



    /**
     * Bogasari
     */

    //region Bogasari

    //Bogasari's product list
    @GET("ottopay/" + API_VERSION + "/bogasari/productlist")
    Call<BogasariResponseModel> bogasariProducts(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Query("merchantId") String merchantId);

    //Bogasari's product inquiry
    @POST("ottopay/" + API_VERSION + "/bogasari/inquiry")
    Call<BogasariInquiryResponseModel> bogasariInquiry(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Body BogasariInquiryRequestModel model);

    //Bogasari's product payment
    @POST("ottopay/" + API_VERSION + "/bogasari/payment")
    Call<BogasariPaymentResponseModel> bogasariPayment(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Body BogasariPaymentRequestModel model);

    //endregion Bogasari



    /**
     * Payment QR
     */

    //QR String
    @POST("ottopay/" + QR_STRING_API_VERSION + "/ottopay-mart/qrstring")
    Call<QrStringResponseModel> qrString(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Body QrStringRequestModel requestModel);

    //Pay QR Inquiry
    @POST("ottopay/" + API_VERSION + "/ottomart/payqr/inquiry")
    Call<PayQrInquiryResponse> payQrInqury(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PayQrInquiryRequestModel model);

    //Pay QR Payment
    @POST("ottopay/" + API_VERSION + "/ottomart/payqr/payment")
    Call<BasePaymentResponseModel> payQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PaymentOfflineConfirmationRequestModel model);

    //Pay QR QRIS Inquiry
    @POST("ottopay/" + QRIS_API_VERSION + "/qris/issuer/inquiry")
    Call<QrQrisInquiryResponse> payQrQrisInqury(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PayQrInquiryRequestModel model);

    //Pay QR QRIS Payment
    @POST("ottopay/" + OCBI_API_VERSION_2    + "/qris/issuer/purchase/ocbi")
    Call<BasePaymentResponseModel> payQrQrisPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PaymentOfflineConfirmationRequestModel model);



    /**
     * Biller (Donasi)
     */

    //region Biller

    //Biller Product List
    @GET("ottopay/" + API_VERSION + "/biller/productlist")
    Call<ProductDonasiResponse> billerProductListDonasi(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Query("productname") String productName);

    //Biller Inquiry
    @POST("ottopay/" + API_VERSION + "/biller/inquiry")
    Call<DonasiInquiryResponse> billerInquiry(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Body DonasiInquiryRequest requestModel);

    //Biller QR String
    @POST("ottopay/" + API_VERSION + "/ottopay-mart/qrstring")
    Call<DonasiQrStringResponse> billerQrString(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Body DonasiQrStringRequest requestModel);

    //Biller Payment
    @POST("ottopay/" + PAYMENT_API_VERSION + "/biller/payment")
    Call<PpobOttoagPaymentResponseModel> billerPayment(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Body DonasiPaymentRequest requestModel);

    //Biller Payment QR
    @POST("ottopay/" + API_VERSION + "/ottopay-mart/checkstatus")
    Call<DonasiQRPaymentResponse> billerCheckStatusQrPayment(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Body DonasiPaymentRequest requestModel);

    //endregion Biller



    /**
     * PPOB
     */

    //region PPOB

    //Biller Product List
    @GET("ottopay/" + API_VERSION + "/biller/productlist")
    Call<PpobOttoagDenomResponseModel> billerProductList(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Query("productname") String productName, @Query("prefix") String prefix, @Query("company") String company);

    //Biller Product List Cashback
    @GET("ottopay/" + PAYMENT_API_VERSION + "/biller/productlistcashback")
    Call<PpobOttoagDenomResponseModel> billerProductListCashback(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Query("productname") String productName, @Query("prefix") String prefix,
                                                                 @Query("company") String company, @Query("gamecode") String gamecode);

    //User Guide
    @POST("ottopay/" + API_VERSION + "/ottopay-mart/ppob/user-guide/entertainment")
    Call<PpobUserGuideResponse> userGuide(@HeaderMap Map<String, String> token, @HeaderMap Map<String,
            String> accessToken, @Body PpobUserGuideRequest ppobUserGuideRequest);

    //Denom List
    @POST("ottopay/" + API_VERSION + "/ottopay-mart/ppob/product-list/{productType}")
    Call<PpobOttoagDenomResponseModel> ppobDenomList(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken,  @Path("productType") String productType, @Body PpobDenomRequest requestModel);

    //Inquiry
    @POST("ottopay/" + PAYMENT_API_VERSION + "/ottomart/ppob/inquiry")
    Call<PpobInquiryResponse> ppobInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobInquiryRequest requestModel);

    //PPOB Payment
    @POST("ottopay/" + PAYMENT_API_VERSION + "/ottomart/ppob/payment")
    Call<PpobOttoagPaymentResponseModel> ppobPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobPaymentRequest requestModel);

    //PPOB Check Status QR
    @POST("ottopay/" + API_VERSION + "/ottomart/ppob/checkstatus")
    Call<PpobCheckStatusQRPaymentResponse> ppobCheckStatusQrPayment(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body PpobPaymentRequest requestModel);

    //PPOB Advice
    @POST("ottopay/" + PAYMENT_API_VERSION + "/ottomart/ppob/advice")
    Call<PpobOttoagPaymentResponseModel> ppobAdvice(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body PpobPaymentRequest requestModel);

    //PPOB Check Pending
    @POST("ottopay/" + API_VERSION + "/ottomart/ppob/checkpending")
    Call<PpobOttoagPaymentResponseModel> ppobCheckPending(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobTransactionAdviceModel model);

    //PPOB Check Pending QRIS
    @POST("ottopay/" + QRIS_API_VERSION + "/qris/issuer/checkStatus")
    Call<PpobOttoagPaymentResponseModel> ppobCheckPendingQris(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobTransactionAdviceModel model);

    //PPOB Favorite List
    @GET("ottopay/" + API_VERSION + "/ottomart/ppob/favourite/{productType}")
    Call<FavoriteResponseModel> favoriteList(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Path("productType") String productType);

    //PPOB Add Favorite
    @POST("ottopay/" + API_VERSION + "/ottomart/ppob/favourite")
    Call<FavoriteResponseModel> addFavorite(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body FavoriteAddRequestModel requestModel);

    //PPOB Delete Favorite
    @DELETE("ottopay/" + API_VERSION + "/ottomart/ppob/favourite/{id}")
    Call<DataEmptyResponseModel> deleteFavorite(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Path("id") int id);

    //endregion PPOB



    /**
     * Refund
     */

    //region Refund

    //Check Refund Status
    @POST("ottopay/" + API_VERSION + "/qris/merchant/checkRefund")
    Call<CheckRefundStatusResponse> checkRefundStatus(@HeaderMap Map<String, String> token, @HeaderMap Map<String,  String> accessToken, @Body MerchantRefundRequest merchantRefundRequest);

    //Merchant Refund
    @POST("ottopay/" + API_VERSION + "/qris/merchant/refund")
    Call<MerchantRefundResponse> merchantRefund(@HeaderMap Map<String, String> token, @HeaderMap Map<String,  String> accessToken, @Body MerchantRefundRequest merchantRefundRequest);

    //endregion Refund


    /**
     * merchant
     */
    @GET("ottopay/" + API_VERSION + "/merchant/status")
    Call<MerchantStatusResponse> merchantStatus(@HeaderMap Map<String, String> token, @HeaderMap Map<String,  String> accessToken);

    @GET("ottopay/" + OCBI_API_VERSION + "/ottopay-mart/profile")
    Call<ProfileResponseModel> profile(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken);


    /**
     * Oasis
     */

    @GET("ottopay/" + API_VERSION + "/oasis/check-merchant")
    Call<CheckOasisStatusResponse> checkOasisStatus(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    /**
     * Transfer Bank
     */

    //Transfer Limit
    @POST("ottopay/" + API_VERSION_3 + "/ottomart/transfer/limit")
    Call<TransferLimitResponse> transferLimit(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body TransferLimitRequest requestModel);

    //Transfer Inquiry
    @POST("ottopay/" + OTTOFIN_QRIS_API_VERSION + "/transferaj/inquiry")
    Call<TransferBankInquiryResponse> transferBankInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body TransferBankInquiryRequest requestModel);

    //Transfer Payment
    @POST("ottopay/" + OTTOFIN_QRIS_API_VERSION + "/transferaj/payment/v2")
    Call<BasePaymentResponseModel> transferBankPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body TransferBankPaymentRequest requestModel);

    //Transfer Status
    @POST("ottopay/" + OTTOFIN_QRIS_API_VERSION + "/transferaj/status/v2")
    Call<BasePaymentResponseModel> checkTransferStatus(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body TransferBankPaymentRequest requestModel);

    //Transfer Bank SKN
    @POST("op-emoney/" + OCBI_API_VERSION + "/omzet/transfer-to-bank-skn")
    Call<BasePaymentResponseModel> transferSkn(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body TransferSknRequest requestModel);


    /**
     * Transfer Deposit
     */

    //List Bank Deposit
    @GET("ottopay/" + OTTOFIN_QRIS_API_VERSION + "/deposit/accounts")
    Call<BankAccountListDepositResponse> bankListDeposit(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    //Add Bank Deposit
    @POST("ottopay/" + OTTOFIN_QRIS_API_VERSION + "/deposit/account/new")
    Call<BaseResponseModel> addBankDeposit(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body AddBankDepositRequest addBankDepositRequest);

    //Delete Bank Deposit
    @POST("ottopay/" + OTTOFIN_QRIS_API_VERSION + "/deposit/account/delete")
    Call<BaseResponseModel> deleteBankDeposit(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body AddBankDepositRequest addBankDepositRequest);

    //Transfer Deposit Inquiry
    @POST("ottopay/" + OTTOFIN_QRIS_API_VERSION + "/deposit/transferaj/inquiry")
    Call<TransferBankInquiryResponse> transferDepositInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body TransferBankInquiryRequest requestModel);

    //Transfer Payment
    @POST("ottopay/" + OTTOFIN_QRIS_API_VERSION + "/deposit/transferaj/payment/v2")
    Call<BasePaymentResponseModel> transferDepositPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body TransferBankPaymentRequest requestModel);

    //Transfer Status
    @POST("ottopay/" + OTTOFIN_QRIS_API_VERSION + "/deposit/transferaj/status/v2")
    Call<BasePaymentResponseModel> checkTransferDepositStatus(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body TransferBankPaymentRequest requestModel);



    /**
     * OCBI
     */

    @GET("op-emoney/" + OCBI_API_VERSION + "/balances")
    Call<BalanceResponse> balance(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    @POST("op-emoney/" + OCBI_API_VERSION + "/ocbi-account-upgrade")
    Call<BaseResponseModel> kycImage(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body KycUploadImageRequest kycUploadImageRequest);

    @GET("op-emoney/" + OCBI_API_VERSION + "/ocbi-account-inquiry")
    Call<StatusOttocashResponse> ottocashStatus(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    @PUT("op-emoney/" + OCBI_API_VERSION + "/accept-ocbi-tnc")
    Call<BaseResponseModel> confirmTNCOCBI(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    //Alfamart Token
    @POST("ottopay/" + OCBI_API_VERSION_2 + "/generateToken")
    Call<AlfamartTokenResponse> alfamartToken(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body AlfamartTokenRequest request);

    @GET("ottopay/" + OCBI_API_VERSION_2 + "/hubungiKami")
    Call<ContactUsResponse> contactUs(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken);

}