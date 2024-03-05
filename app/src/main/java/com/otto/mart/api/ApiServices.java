package com.otto.mart.api;

import com.otto.mart.model.APIModel.Request.AddAddressRequestModel;
import com.otto.mart.model.APIModel.Request.AddressEditRequestModel;
import com.otto.mart.model.APIModel.Request.BankEditRequestModel;
import com.otto.mart.model.APIModel.Request.CancelQrTransactionRequestModel;
import com.otto.mart.model.APIModel.Request.CheckMerchantIdRequestModel;
import com.otto.mart.model.APIModel.Request.CheckVersionRequestModel;
import com.otto.mart.model.APIModel.Request.DeleteAddressRequestModel;
import com.otto.mart.model.APIModel.Request.FavoriteAddRequestModel;
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
import com.otto.mart.model.APIModel.Request.ProductRequestModel;
import com.otto.mart.model.APIModel.Request.QrStringRequestModel;
import com.otto.mart.model.APIModel.Request.RegisterOtpRequestModel;
import com.otto.mart.model.APIModel.Request.RegisterUpgradeAccountRequestModel;
import com.otto.mart.model.APIModel.Request.ResendOtpRegisterRequestModel;
import com.otto.mart.model.APIModel.Request.ResetOtpPinRequestModel;
import com.otto.mart.model.APIModel.Request.ResetPinRequestModel;
import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.model.APIModel.Request.TfWalletConfRequestModel;
import com.otto.mart.model.APIModel.Request.TfWalletInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.TokenRefreshRequestModel;
import com.otto.mart.model.APIModel.Request.TokenRequestModel;
import com.otto.mart.model.APIModel.Request.TransferToBankConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.TransferToBankInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.UpdatePinRequestModel;
import com.otto.mart.model.APIModel.Request.UpdateProfileRequestModel;
import com.otto.mart.model.APIModel.Request.UpdateStatusRequest;
import com.otto.mart.model.APIModel.Request.UserSecurityQuestionRequestModel;
import com.otto.mart.model.APIModel.Request.WalletTypeResponseModel;
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
import com.otto.mart.model.APIModel.Response.AddressListResponseModel;
import com.otto.mart.model.APIModel.Response.AppClonerListResponse;
import com.otto.mart.model.APIModel.Response.BannerRespomnse;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.BusinessCategoryResponseModel;
import com.otto.mart.model.APIModel.Response.CancelQrTransactionResponseModel;
import com.otto.mart.model.APIModel.Response.CheckMerchantResponseModel;
import com.otto.mart.model.APIModel.Response.CheckVersionResponseModel;
import com.otto.mart.model.APIModel.Response.DataEmptyResponseModel;
import com.otto.mart.model.APIModel.Response.EditAddressResponseModel;
import com.otto.mart.model.APIModel.Response.FavoriteResponseModel;
import com.otto.mart.model.APIModel.Response.GetBankResponseModel;
import com.otto.mart.model.APIModel.Response.GetCityResponseModel;
import com.otto.mart.model.APIModel.Response.GetCountryResponseModel;
import com.otto.mart.model.APIModel.Response.GetDistrictResponseModel;
import com.otto.mart.model.APIModel.Response.GetProvinceResponsetModel;
import com.otto.mart.model.APIModel.Response.GetQrStringResponseModel;
import com.otto.mart.model.APIModel.Response.GetVillageResponseModel;
import com.otto.mart.model.APIModel.Response.LoginOtpResponseModel;
import com.otto.mart.model.APIModel.Response.LoginQrResponseModel;
import com.otto.mart.model.APIModel.Response.LoginResponseModel;
import com.otto.mart.model.APIModel.Response.NearbyMerchantResponse;
import com.otto.mart.model.APIModel.Response.OmzetHistoryResponseModel;
import com.otto.mart.model.APIModel.Response.OmzetSaldoResponseModel;
import com.otto.mart.model.APIModel.Response.OmzetStatResponseModel;
import com.otto.mart.model.APIModel.Response.PPobPaymentQrResponseModel;
import com.otto.mart.model.APIModel.Response.PayQrInquiryOldResponseModel;
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
import com.otto.mart.model.APIModel.Response.PpobTelkomPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.ProductResponseModel;
import com.otto.mart.model.APIModel.Response.ProfileResponseModel;
import com.otto.mart.model.APIModel.Response.QrStringResponseModel;
import com.otto.mart.model.APIModel.Response.RegisterUpgradeAccountResponseModel;
import com.otto.mart.model.APIModel.Response.ResetOtpPinResponseModel;
import com.otto.mart.model.APIModel.Response.ResetPinResponseModel;
import com.otto.mart.model.APIModel.Response.SecurityQuestionResponseModel;
import com.otto.mart.model.APIModel.Response.SignupOtpResponseModel;
import com.otto.mart.model.APIModel.Response.SignupResponseModel;
import com.otto.mart.model.APIModel.Response.TelkomInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.TfWalletInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.TokenResponseModel;
import com.otto.mart.model.APIModel.Response.TransferBankInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.UpdateStatusResponse;
import com.otto.mart.model.APIModel.Response.UserSecurityQuestionResponseModel;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.model.APIModel.Response.bank.BankAccountListResponseModel;
import com.otto.mart.model.APIModel.Response.bank.BankEditResponseModel;
import com.otto.mart.model.APIModel.Response.faq.FAQResponseModel;
import com.otto.mart.model.APIModel.Response.inbox.InboxReadResponse;
import com.otto.mart.model.APIModel.Response.inbox.InboxResponse;
import com.otto.mart.model.APIModel.Response.olshop.AddressResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.CategoryResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.MailNoResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.PaymentJurnalResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.ProductDetailResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.ProductListResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.ReversalResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.address.CreateAddressResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.cart.CartResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.confirm.FinalConfirmOrderResponse;
import com.otto.mart.model.APIModel.Response.olshop.order.ConfirmOrderResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.order.ShippingCostResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.order.status.OrderStatusResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.payment.PaymentJournalOlshopResponseModel;
import com.otto.mart.model.APIModel.Response.payment.AlfamartPurchaseResponse;
import com.otto.mart.model.APIModel.Response.payment.IndomaretQrPurchaseCancelResponseModel;
import com.otto.mart.model.APIModel.Response.payment.IndomaretQrPurchaseResponseModel;
import com.otto.mart.model.APIModel.Response.tokoOttopay.AddToCardResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.CartResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderConfirmResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderHistoryDetailReesponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderHistoryResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderPayOffResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderPaymentResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.ReOrderProductResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.StoreListResponse;
import com.otto.mart.model.APIModel.Response.topUpEmoney.TopUpEmoneyDenomResponseModel;
import com.otto.mart.model.APIModel.Response.topUpEmoney.TopUpEmoneyProductResponseModel;
import com.otto.mart.model.APIModel.Response.voucherGame.VoucherGameResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiServices {

    //App Cloner
    @GET("packages")
    Call<AppClonerListResponse> appClonerList(@HeaderMap Map<String, String> key);

    //Nearby Merchant
    @Headers({
            "Content-Type:" + "application/json"
    })
    @GET("v1/merchant")
    Call<NearbyMerchantResponse> getNearbyMerchant(
            @Header("x-apikey") String accessToken,
            @Query("search") String search,
            @Query("explorationType") String type,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("limit") int limit,
            @Query("offset") int offset);

    //token
    @POST("oauth/token/request")
    Call<TokenResponseModel> asRequestToken(@HeaderMap Map<String, String> key, @Body TokenRequestModel model);

    @POST("oauth/token/refresh")
    Call<TokenResponseModel> asRefreshToken(@HeaderMap Map<String, String> key, @Body TokenRefreshRequestModel model);

    //auth
    @POST("auth/signup")
    Call<SignupResponseModel> asSignUp(@HeaderMap Map<String, String> key, @Body SignupRequestModel model);

    @POST("auth/login")
    Call<LoginResponseModel> asLogin(@HeaderMap Map<String, String> key, @Body LoginRequestModel model);

    @POST("auth/pin/reset")
    Call<ResetPinResponseModel> asReset(@HeaderMap Map<String, String> key, @Body ResetPinRequestModel model);

    @POST("auth/signup/search-merchant")
    Call<CheckMerchantResponseModel> asCheckMerchantID(@HeaderMap Map<String, String> key, @Body CheckMerchantIdRequestModel model);

    @GET("auth/security-question")
    Call<SecurityQuestionResponseModel> asGetSecurityQuestion();

    @POST("auth/login/otp-verification")
    Call<LoginOtpResponseModel> asLoginOtp(@HeaderMap Map<String, String> key, @Body LoginOtpRequestModel model);

    @POST("auth/signup/otp-verification")
    Call<SignupOtpResponseModel> asRegisterOtp(@HeaderMap Map<String, String> key, @Body RegisterOtpRequestModel model);

    @POST("auth/update-status")
    Call<UpdateStatusResponse> updateStatus(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body UpdateStatusRequest model);

    @POST("auth/resend-otp")
    Call<BaseResponseModel> asResendOtpLogin(@HeaderMap Map<String, String> key, @Body ResendOtpRegisterRequestModel model);

    @POST("auth/resend-otp")
    Call<BaseResponseModel> asReRegisterOtp(@HeaderMap Map<String, String> key, @Body ResendOtpRegisterRequestModel model);

    @POST("auth/security-question")
    Call<UserSecurityQuestionResponseModel> getUserSecurityQuestion(@HeaderMap Map<String, String> key, @Body UserSecurityQuestionRequestModel model);

    @POST("profile/upgrade-request")
    Call<RegisterUpgradeAccountResponseModel> asUpgradeAccount(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body RegisterUpgradeAccountRequestModel model);

    @POST("auth/pin/otp")
    Call<ResetOtpPinResponseModel> getResetOtpPin(@HeaderMap Map<String, String> key, @Body ResetOtpPinRequestModel model);

    //misc
    @GET("country")
    Call<GetCountryResponseModel> asGetCountry();

    @GET("province/{countryid}")
    Call<GetProvinceResponsetModel> asGetProvince(@Path("countryid") int Countryid);

    @GET("city/{provinceid}")
    Call<GetCityResponseModel> asGetCity(@Path("provinceid") long provinceid);

    @GET("district/{cityid}")
    Call<GetDistrictResponseModel> asGetDistrict(@Path("cityid") long cityid);

    @GET("village/{districtid}")
    Call<GetVillageResponseModel> asGetVillage(@Path("districtid") long districtid);

    @GET("bank")
    Call<GetBankResponseModel> asGetBank();

    @GET("business/type")
    Call<BusinessCategoryResponseModel> asGetBusiness(@HeaderMap Map<String, String> key);

    //product
    @POST("product/all")
    Call<ProductResponseModel> asGetProductAll(@HeaderMap Map<String, String> key, @Body ProductRequestModel model);

    @POST("product/promo")
    Call<ProductResponseModel> asGetProductPromo(@HeaderMap Map<String, String> key, @Body ProductRequestModel model);

    @POST("product/newest")
    Call<ProductResponseModel> asGetProductNew(@HeaderMap Map<String, String> key, @Body ProductRequestModel model);

    @POST("product/last-buy")
    Call<ProductResponseModel> asGetProductBought(@HeaderMap Map<String, String> key, @Body ProductRequestModel model);

    @GET("product/{productid}")
    Call<ProductResponseModel> asGetProductByID(@HeaderMap Map<String, String> key, @Path("productid") int productid);

    //user
    @GET("profile")
    Call<ProfileResponseModel> asGetProfile(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    @POST("profile/edit")
    Call<ProfileResponseModel> asUpdateProfile(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body UpdateProfileRequestModel model);

    /**
     * PPOB
     */

    //region PPOB

    @POST("ppob/product-list/{productType}")
    Call<PpobOttoagDenomResponseModel> ppobDenomList(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken,
                                                     @Path("productType") String productType, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/{productType}")
    Call<PpobOttoagInquiryResponseModel> ppobInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken,
                                                     @Path("productType") String productType, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/{productType}")
    Call<PpobOttoagPaymentResponseModel> ppobPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken,
                                                     @Path("productType") String productType, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/{productType}")
    Call<PPobPaymentQrResponseModel> ppobQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken,
                                                   @Path("productType") String productType, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/denom-list/top-up")
    Call<PpobOttoagDenomResponseModel> topUpDenomList(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body TopUpDenomRequestModel model);

    @POST("ppob/denom-list/voucher-game")
    Call<PpobOttoagDenomResponseModel> gameDenomList(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body VoucherGameDenomRequestModel model);

    //endregion PPOB

    //ppob
    //pulsa
    @POST("ppob/denom-list/phone-prepaid")
    Call<PpobOttoagDenomResponseModel> asGetPpobPulsaDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/phone-prepaid")
    Call<PpobPulsaInquiryResponseModel> asGetPpobPulsaInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/phone-prepaid")
    Call<PpobOttoagPaymentResponseModel> asGetPpobPulsaPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/phone-prepaid")
    Call<PPobPaymentQrResponseModel> asGetPpobPulsaQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/product-list/phone-prepaid2")
    Call<PpobOttoagDenomResponseModel> asGetPpobPulsaDenom2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/phone-prepaid2")
    Call<PpobOttoagInquiryResponseModel> asGetPpobPulsaInquiry2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/phone-prepaid2")
    Call<PpobOttoagPaymentResponseModel> asGetPpobPulsaPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/phone-prepaid2")
    Call<PPobPaymentQrResponseModel> asGetPpobPulsaQrPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

//    @POST("ppob/advice/phone-prepaid2")
//    Call<PpobOttoagPaymentResponseModel> asGetPpobPulsaAdvice2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //pascapaid
    @POST("ppob/inquiry/phone-postpaid")
    Call<PpobPulsaPascaInquiryResponseModel> asGetPpobPulsaPascaInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/phone-postpaid")
    Call<PpobOttoagPaymentResponseModel> asGetPpobPulsaPascaPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/phone-postpaid")
    Call<PPobPaymentQrResponseModel> asGetPpobPulsaPascaQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //pascapaid-ottoag
    @POST("ppob/product-list/phone-postpaid2")
    Call<PpobOttoagDenomResponseModel> asGetPpobPulsaPascaDenom2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/phone-postpaid2")
    Call<PpobOttoagInquiryResponseModel> asGetPpobPulsaPascaInquiry2(@HeaderMap Map<String, String> stringStringMap, @HeaderMap Map<String, String> stringStringMap1, @Body PpobOttoagInquiryRequestModel requestModel);

    @POST("ppob/payment/phone-postpaid2")
    Call<PpobOttoagPaymentResponseModel> asGetPpobPulsaPascaPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/phone-postpaid2")
    Call<PPobPaymentQrResponseModel> asGetPpobPulsaPascaQrPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //paket
    @POST("ppob/denom-list/phone-data")
    Call<PpobOttoagDenomResponseModel> asGetPpobPaketdataDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/phone-data")
    Call<PpobPulsaInquiryResponseModel> asGetPpobPaketdataInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/phone-data")
    Call<PpobOttoagPaymentResponseModel> asGetPpobPaketDataPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/phone-data")
    Call<PPobPaymentQrResponseModel> asGetPpobPaketDataQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/product-list/phone-data2")
    Call<PpobOttoagDenomResponseModel> asGetPpobPaketDataDenom2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/phone-data2")
    Call<PpobOttoagInquiryResponseModel> asGetPpobPaketDataInquiry2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/phone-data2")
    Call<PpobOttoagPaymentResponseModel> asGetPpobPaketDataPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/phone-data2")
    Call<PPobPaymentQrResponseModel> asGetPpobPaketDataQrPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

//    @POST("ppob/advice/phone-data2")
//    Call<PpobOttoagPaymentResponseModel> asGetPpobPaketDataAdvice2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //listrik-token
    @POST("ppob/denom-list/electricity-token")
    Call<PpobListrikDenomResponseModel> asGetPpobListrikTokenDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobListrikDenomRequestModel model);

    @POST("ppob/inquiry/electricity-token")
    Call<PpobListrikInquiryResponseModel> asGetPpobListrikTokenInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobListrikInquiryRequestModel model);

    @POST("ppob/payment/electricity-token")
    Call<PpobListrikPaymentResponseModel> asGetPpobListrikTokenPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobListrikPaymentRequestModel model);

    @POST("ppob/qr/electricity-token")
    Call<PPobPaymentQrResponseModel> asGetPpobListrikTokenQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobListrikPaymentRequestModel model);

    @POST("ppob/product-list/electricity-token2")
    Call<PpobOttoagDenomResponseModel> asGetPpobListrikTokenDenom2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/electricity-token2")
    Call<PpobOttoagInquiryResponseModel> asGetPpobListrikTokenInquiry2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/electricity-token2")
    Call<PpobOttoagPaymentResponseModel> asGetPpobListrikTokenPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/electricity-token2")
    Call<PPobPaymentQrResponseModel> asGetPpobListrikTokenQrPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //listrik-tagihan
    @POST("ppob/inquiry/electricity-bill")
    Call<PpobListrikInquiryResponseModel> asGetPpobListrikNgutangInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobListrikInquiryRequestModel model);

    @POST("ppob/payment/electricity-bill")
    Call<PpobListrikPaymentResponseModel> asGetPpobListrikNgutangPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobListrikPaymentRequestModel model);

    @POST("ppob/qr/electricity-bill")
    Call<PPobPaymentQrResponseModel> asGetPpobListrikNgutangQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobListrikPaymentRequestModel model);

    @POST("ppob/product-list/electricity-bill2")
    Call<PpobOttoagDenomResponseModel> asGetPpobListrikNgutangDenom2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/electricity-bill2")
    Call<PpobOttoagInquiryResponseModel> asGetPpobListrikNgutangInquiry2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/electricity-bill2")
    Call<PpobOttoagPaymentResponseModel> asGetPpobListrikNgutangPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/electricity-bill2")
    Call<PPobPaymentQrResponseModel> asGetPpobListrikNgutangQrPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //listrik-!tagihan
    @POST("ppob/inquiry/electricity-nonbill")
    Call<PpobListrikInquiryResponseModel> asGetPpobListrikTdkNgutangInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobListrikInquiryRequestModel model);

    @POST("ppob/payment/electricity-nonbill")
    Call<PpobListrikPaymentResponseModel> asGetPpobListrikTdkNgutangPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobListrikPaymentRequestModel model);

    @POST("ppob/qr/electricity-nonbill")
    Call<PPobPaymentQrResponseModel> asGetPpobListrikTdkNgutangQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobListrikPaymentRequestModel model);

    @POST("ppob/product-list/electricity-nonbill2")
    Call<PpobOttoagDenomResponseModel> asGetPpobListrikTdkNgutangDenom2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/electricity-nonbill2")
    Call<PpobOttoagInquiryResponseModel> asGetPpobListrikTdkNgutangInquiry2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/electricity-nonbill2")
    Call<PpobOttoagPaymentResponseModel> asGetPpobListrikTdkNgutangPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/electricity-nonbill2")
    Call<PPobPaymentQrResponseModel> asGetPpobListrikTdkNgutangQrPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);


    //air
    @POST("ppob/product-list/80")
    Call<PpobAirProductlistResponseModel> asGetPpobAirDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobProductlistRequestModel odel);

    @POST("ppob/inquiry/water")
    Call<PpobAirInquiryResponseModel> asGetPpobAirInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobAirInquiryRequestModel model);

    @POST("ppob/payment/water")
    Call<PpobAirPaymentResponseModel> asGetPpobAirPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobAirPaymentRequestModel model);

    @POST("ppob/qr/water")
    Call<PPobPaymentQrResponseModel> asGetPpobAirQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobAirPaymentRequestModel model);

    //air-ottoag
    @POST("ppob/product-list/pdam2")
    Call<PpobOttoagDenomResponseModel> asGetPpobAirDenom2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/pdam2")
    Call<PpobOttoagInquiryResponseModel> asGetPpobAirInquiry2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/pdam2")
    Call<PpobOttoagPaymentResponseModel> asGetPpobAirPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/pdam2")
    Call<PPobPaymentQrResponseModel> asGetPpobAirQrPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

//    @POST("ppob/advice/pdam2")
//    Call<PpobOttoagPaymentResponseModel> asGetPpobAirAdvice2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);
//

    //BPJS
    @POST("ppob/product-list/bpjs")
    Call<PpobOttoagDenomResponseModel> asGetPpobBPJSDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobProductlistRequestModel odel);

    @POST("ppob/inquiry/bpjs")
    Call<PpobBpjsInquiryResponseModel> asGetPpobBPJSInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobAsuInquryRequestModel model);

    @POST("ppob/payment/bpjs")
    Call<PpobBpjsPaymentResponseModel> asGetPpobBPJSPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/bpjs")
    Call<PPobPaymentQrResponseModel> asGetPpobBPJSQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //telkom
    @POST("ppob/inquiry/telkom-bill")
    Call<TelkomInquiryResponseModel> asGetPpobTelkomInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobTelkomInquiryRequestModel model);

    @POST("ppob/payment/telkom-bill")
    Call<PpobTelkomPaymentResponseModel> asGetPpobTelkomPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobTelkomPaymentRequestModel model);

    @POST("ppob/qr/telkom-bill")
    Call<PPobPaymentQrResponseModel> asGetPpobTelkomQR(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobTelkomPaymentRequestModel model);

    //telkom-ottoag
    @POST("ppob/product-list/telkom2")
    Call<PpobOttoagDenomResponseModel> asGetPpobTelkomDenom2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagDenomRequestModel model);

    @POST("ppob/inquiry/telkom2")
    Call<PpobOttoagInquiryResponseModel> asGetPpobTelkomInquiry2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/telkom2")
    Call<PpobOttoagPaymentResponseModel> asGetPpobTelkomPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/telkom2")
    Call<PPobPaymentQrResponseModel> asGetPpobTelkomQrPayment2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

//    @POST("ppob/advice/telkom2")
//    Call<PpobOttoagPaymentResponseModel> asGetPpobTelkomAdvice2(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //ASURANSI
    @POST("ppob/product-list/insurance")
    Call<PpobOttoagDenomResponseModel> asGetPpobAsuDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobProductlistRequestModel odel);

    @POST("ppob/inquiry/insurance")
    Call<PpobOttoagInquiryResponseModel> asGetPpobAsuInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobAsuInquryRequestModel model);

    @POST("ppob/payment/insurance")
    Call<PpobOttoagPaymentResponseModel> asGetPpobAsuPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/insurance")
    Call<PPobPaymentQrResponseModel> asGetPpobAsuQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //cabletv
    @POST("ppob/product-list/tv-cable-internet")
    Call<PpobOttoagDenomResponseModel> asGetPpobCableDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobProductlistRequestModel odel);

    @POST("ppob/inquiry/tv-cable-internet")
    Call<PpobOttoagInquiryResponseModel> asGetPpobCableInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/tv-cable-internet")
    Call<PpobOttoagPaymentResponseModel> asGetPpobCablePayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/tv-cable-internet")
    Call<PPobPaymentQrResponseModel> asGetPpobCableQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //multifinace
    @POST("ppob/product-list/multi-finance")
    Call<PpobOttoagDenomResponseModel> asGetPpobScamDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobProductlistRequestModel odel);

    @POST("ppob/inquiry/multi-finance")
    Call<PpobOttoagInquiryResponseModel> asGetPpobScamInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobAsuInquryRequestModel model);

    @POST("ppob/payment/multi-finance")
    Call<PpobOttoagPaymentResponseModel> asGetPpobScamPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/multi-finance")
    Call<PPobPaymentQrResponseModel> asGetPpobScamQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //education
    @POST("ppob/product-list/education")
    Call<PpobOttoagDenomResponseModel> asGetPpobeducationDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobProductlistRequestModel odel);

    @POST("ppob/inquiry/education")
    Call<PpobOttoagInquiryResponseModel> asGetPpobEducationInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobAsuInquryRequestModel model);

    @POST("ppob/payment/education")
    Call<PpobOttoagPaymentResponseModel> asGetPpobEducationPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/education")
    Call<PPobPaymentQrResponseModel> asGetPpobEducationQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //Top Up eMoney
    @POST("ppob/product-list/top-up")
    Call<TopUpEmoneyProductResponseModel> asGetTopUpEmoneyProduct(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    @POST("ppob/denom-list/top-up")
    Call<TopUpEmoneyDenomResponseModel> asGetTopUpEmoneyDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body TopUpDenomRequestModel model);

    @POST("ppob/inquiry/top-up")
    Call<PpobOttoagInquiryResponseModel> asGetPpobTopUpInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/top-up")
    Call<PpobOttoagPaymentResponseModel> asGetPpobTopUpPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/top-up")
    Call<PPobPaymentQrResponseModel> asGetPpobTopUpQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //Voucher Game
    @POST("ppob/product-list/voucher-game")
    Call<VoucherGameResponse> asGetVoucherGameProductList(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    @POST("ppob/denom-list/voucher-game")
    Call<TopUpEmoneyDenomResponseModel> asGetVoucherGameDenom(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body VoucherGameDenomRequestModel model);

    @POST("ppob/inquiry/voucher-game")
    Call<PpobOttoagInquiryResponseModel> asGetPpobVoucherGameInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagInquiryRequestModel model);

    @POST("ppob/payment/voucher-game")
    Call<PpobOttoagPaymentResponseModel> asGetPpobVoucherGamePayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    @POST("ppob/qr/voucher-game")
    Call<PPobPaymentQrResponseModel> asGetPpobVoucherGameQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model);

    //Ppob Receipt
    @POST("ppob/receipt/{pool}")
    Call<PpobOttoagPaymentResponseModel> asGetReceipt(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobReceiptRequestModel model, @Path(value = "pool") String pool);

    //advice
    @POST("ppob/advice/{pool}")
    Call<PpobOttoagPaymentResponseModel> asGetPpobOttoagAdvice(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobOttoagPaymentRequestModel model, @Path(value = "pool") String pool);

    //Transaction Advice
    @POST("ppob/transaction/advice")
    Call<PpobOttoagPaymentResponseModel> asGetPpobTransactionAdvice(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobTransactionAdviceModel model);

    //transaction
    @POST("emoney/omset-history")
    Call<OmzetHistoryResponseModel> asGetOmsetHistory(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body OmsetHistoryRequestModel model);

    @POST("emoney/omset-to-saldo")
    Call<OmzetSaldoResponseModel> asTrasnOmzetSaldo(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body OmzetSaldoRequestModel model);

    @POST("qr/payment")
    Call<BasePaymentResponseModel> asQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PpobQrPaymentRequestModel model);

    @POST("qr/cancel")
    Call<CancelQrTransactionResponseModel> asQrCancelPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body CancelQrTransactionRequestModel model);

    @POST("profile/address/edit")
    Call<EditAddressResponseModel> asUpdateAddress(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body AddressEditRequestModel model);

    @GET("emoney/omset-stat")
    Call<OmzetStatResponseModel> asGetOmzetStat(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    @FormUrlEncoded
    @POST
    Call<GetQrStringResponseModel> requestQr(
            @Url String url, @Field(value = "pan") String pan, @Field(value = "bill_id") String billid, @Field(value = "trx_amount", encoded = true) int amount, @Field(value = "terminal_id") String terminalid);

    @POST("emoney/inquiry/offline-payment")
    Call<PayQrInquiryOldResponseModel> asGetPayQrInquiry(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PayQrInquiryRequestModel model);

    @POST("emoney/confirmation/offline-payment")
    Call<BasePaymentResponseModel> asGetPayQrPayment(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken, @Body PaymentOfflineConfirmationRequestModel model);

    //walletdata
    @GET("wallet")
    Call<WalletResponseModel> asGetWallet(@HeaderMap Map<String, String> key, @HeaderMap Map<String, String> accessToken);

    @GET("profile/bank-account/")
    Call<BankAccountListResponseModel> asGetBankList(@HeaderMap Map<String, String> accessToken);

    @POST("profile/bank-account/edit")
    Call<BankEditResponseModel> asUpdateBank(@HeaderMap Map<String, String> accessToken, @Body BankEditRequestModel model);

    @POST("profile/bank-account/create")
    Call<BankAccountListResponseModel> asAddBank(@HeaderMap Map<String, String> accessToken, @Body BankEditRequestModel model);

    @POST("emoney/wallet-history")
    Call<OmzetHistoryResponseModel> asGetWalletHistory(@HeaderMap Map<String, String> accessToken, @HeaderMap Map<String, String> header, @Body OmsetHistoryRequestModel model);

    @GET("profile/address/")
    Call<AddressListResponseModel> asGetAddressList(@HeaderMap Map<String, String> accessToken);

    @POST("auth/check-merchant")
    Call<LoginQrResponseModel> asGetPhone(@Body LoginQrRequestModel model);

    @POST("profile/address/create")
    Call<AddressListResponseModel> asCreateAddress(@HeaderMap Map<String, String> accessToken, @Body AddAddressRequestModel model);

    @POST("profile/address/delete")
    Call<BaseResponseModel> asDeleteAddress(@HeaderMap Map<String, String> accessToken, @Body DeleteAddressRequestModel model);

    @POST("auth/pin/update")
    Call<DataEmptyResponseModel> asUpdatePin(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body UpdatePinRequestModel model);

    @POST("qrstring")
    Call<QrStringResponseModel> asGetQrString(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body QrStringRequestModel model);

    @POST("qrstring")
    Call<QrStringResponseModel> asGetQrString(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken);

    @POST("check-version")
    Call<CheckVersionResponseModel> asGetVersion(@Body CheckVersionRequestModel model);

    //wallet transfer
    @POST("transfer/inquiry/wallet")
    Call<TfWalletInquiryResponseModel> asGetTfWalletInquiry(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body TfWalletInquiryRequestModel model);

    @POST("transfer/confirmation/wallet")
    Call<BasePaymentResponseModel> asGetTfWalletConf(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body TfWalletConfRequestModel model);

    @POST("transfer/inquiry/bank")
    Call<TransferBankInquiryResponseModel> asGetTfBankInquiry(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body TransferToBankInquiryRequestModel model);

    @POST("transfer/confirmation/bank")
    Call<BasePaymentResponseModel> asGetTfBankConfirmation(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body TransferToBankConfirmationRequestModel model);

    @GET("wallet/types")
    Call<WalletTypeResponseModel> asGetWalletTypeList();

    //ppob favorite
    @GET("favourite/ppob/{target}")
    Call<FavoriteResponseModel> asGetPpobFav(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Path("target") String target);

    @POST("favourite/ppob/create")
    Call<FavoriteResponseModel> asSaveFavItem(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body FavoriteAddRequestModel requestModel);

    @POST("favourite/ppob/delete/{id}")
    Call<DataEmptyResponseModel> asDeleteFavItem(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Path("id") int itemid);

    //logout
    @POST("auth/logout")
    Call<BaseResponseModel> asLogout(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken);

    //banner
    @GET("banner")
    Call<BannerRespomnse> asGetBanner(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken);

    @POST("emoney/payment/journal")
    Call<PaymentJurnalResponseModel> paymentJurnals(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body PaymentJurnalRequestModel paymentJurnalRequestModel);

    @POST("payment_journals")
    Call<PaymentJournalOlshopResponseModel> paymentJurnal(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body PaymentJurnalRequestModel paymentJurnalRequestModel);

    @POST("emoney/reversal/journal")
    Call<ReversalResponseModel> reversalPayment(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body ReversalRequestModel model);

    //Indomaret QR Purchase
    @POST("indomaret/qr/purchase")
    Call<IndomaretQrPurchaseResponseModel> asIndomaretQrPurchase(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body IndomaretQrPurchaseRequestModel request);

    //Indomaret QR Purchase Cancel
    @POST("indomaret/qr/cancel")
    Call<IndomaretQrPurchaseCancelResponseModel> asIndomaretQrPurchaseCancel(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body IndomaretQrPurchaseRequestModel request);

    //Indomaret QR Status
    @POST("indomaret/qr/status")
    Call<BasePaymentResponseModel> asIndomaretQrPurchaseStatus(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body IndomaretQrStatusRequestModel request);

    //Alfamart QR Purchase
    @POST("alfamart/qr/purchase")
    Call<AlfamartPurchaseResponse> asAlfamartQrPurchase(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body AlfamartPurchaseRequest request);

    //Alfamart QR Status
    @POST("alfamart/qr/status")
    Call<BasePaymentResponseModel> asAlfamartQrPurchaseStatus(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body AlfamartStatusRequest request);


    /**
     * Inbox
     */

    //Get Inbox List
    @GET("inbox")
    Call<InboxResponse> asInboxList(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Query("page") int page);

    //Update Read
    @POST("inbox/read")
    Call<InboxReadResponse> asInboxRead(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body InboxReadRequest inboxReadRequest);

    //Read Bulk
    @POST("inbox/read/bulk")
    Call<DataEmptyResponseModel> inboxReadBulk(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body InboxUpdateBulkRequest inboxUpdateBulkRequest);

    //Delete Bulk
    @POST("inbox/delete/bulk")
    Call<DataEmptyResponseModel> inboxDeleteBulk(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body InboxUpdateBulkRequest inboxUpdateBulkRequest);



    /**
     * TOKO OTTOPAY
     */

    //Store List
    @GET("store/suppliers")
    Call<StoreListResponse> storeList(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken);

    //ReOrder Product List
    @POST("store/products")
    Call<ReOrderProductResponse> asReOrderProductList(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body ProductListRequest productListRequest);

    //Add To Cart
    @POST("store/cart/add")
    Call<AddToCardResponse> addToCart(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body AddToCardRequest addToCardRequest);

    //Remove From Cart
    @POST("store/cart/remove")
    Call<AddToCardResponse> removeFromCart(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body RemoveCardRequest addToCardRequest);

    //Cart
    @POST("store/cart")
    Call<CartResponse> cart(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken);

    //Order Confirm
    @POST("store/order/confirm")
    Call<OrderConfirmResponse> orderConfirm(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body OrderConfirmRequest orderConfirmRequest);

    //Order Payment
    @POST("store/order/payment")
    Call<OrderPaymentResponse> orderPayment(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body OrderPaymentRequest orderPaymentRequest);

    //Order Pay Off
    @POST("store/order/pay-off")
    Call<OrderPayOffResponse> orderPayOff(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body OrderPayOffRequest orderPayOffRequest);

    //Order History
    @GET("store/order/history")
    Call<OrderHistoryResponse> orderHistory(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken);

    //Order History Detail
    @POST("store/order/detail")
    Call<OrderHistoryDetailReesponse> orderHistoryDetail(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body OrderHistoryDetailRequest orderHistoryDetailRequest);

    @GET("category-product/1466")
    Call<CategoryResponseModel> serviceGetCategories(@HeaderMap Map<String, String> token);

    @POST("product/{path}")
    Call<ProductListResponseModel> serviceGetProducts(@HeaderMap Map<String, String> token, @Path("path") String path, @Body ProductListRequestModel model);

    @GET("product/{productId}")
    Call<ProductDetailResponseModel> serviceGetProductDetail(@HeaderMap Map<String, String> token, @Path("productId") int path);

    @GET("cart")
    Call<CartResponseModel> serviceGetCartList(@HeaderMap Map<String, String> token);

    @POST("cart/add")
    Call<CartResponseModel> serviceAddToCart(@HeaderMap Map<String, String> token, @Body AddCartRequestModel model);

    //    @HTTP(method = "DELETE", path = "cart/delete", hasBody = true)
    @DELETE("cart/delete")
    Call<CartResponseModel> serviceDeleteCart(@HeaderMap Map<String, String> token, @Query("cart_item_id") String cartItemId);

    @POST("cart/edit")
    Call<CartResponseModel> serviceUpdateCart(@HeaderMap Map<String, String> token, @Body UpdateCartRequestModel model);

    @POST("order/confirm-order")
    Call<ConfirmOrderResponseModel> serviceOrderConfirmation(@HeaderMap Map<String, String> token, @Body OrderConfirmationRequestModel model);

    @POST("product/shipping-cost")
    Call<ShippingCostResponseModel> serviceShippingCost(@HeaderMap Map<String, String> token, @Body ShippingCostRequestModel model);

    @POST("order/confirm-paid-order")
    Call<FinalConfirmOrderResponse> serviceConfirmOrderPaid(@HeaderMap Map<String, String> token, @Body ConfirmPaid model);

    @GET("elevenia/provinces")
    Call<AddressResponseModel> serviceGetProvince(@HeaderMap Map<String, String> token);

    @GET("elevenia/provinces/{provinceId}/cities")
    Call<AddressResponseModel> serviceGetCity(@HeaderMap Map<String, String> token, @Path("provinceId") String provinceId);

    @GET("elevenia/cities/{cityId}/districts")
    Call<AddressResponseModel> serviceGetDistrict(@HeaderMap Map<String, String> token, @Path("cityId") String cityId);

    @POST("check_mail_no")
    Call<MailNoResponseModel> serviceGetMailNo(@HeaderMap Map<String, String> token, @Body AddressRequestModel model);

    @POST("shipping_addresses")
    Call<CreateAddressResponseModel> serviceCreateShippingAddress(@HeaderMap Map<String, String> token, @Body ShippingAddressRequestModel model);

    @GET("shipping_addresses")
    Call<com.otto.mart.model.APIModel.Request.olshop.address.AddressListResponseModel> serviceGetShippingAddress(@HeaderMap Map<String, String> token);

    @PATCH("shipping_addresses/{addressId}")
    Call<CreateAddressResponseModel> serviceUpdateShippingAddress(@HeaderMap Map<String, String> token, @Body ShippingAddressRequestModel model, @Path("addressId") String addressId);

    @DELETE("shipping_addresses/{addressId}")
    Call<CreateAddressResponseModel> serviceDeleteShippingAddress(@HeaderMap Map<String, String> token, @Path("addressId") String addressId);

    @GET("order/statuses")
    Call<OrderStatusResponseModel> serviceOrderStatus(@HeaderMap Map<String, String> token, @QueryMap Map<String, String> params);

    @GET("emoney/top-up-instruction")
    Call<FAQResponseModel> serviceFAQTopUp(@HeaderMap Map<String, String> token, @QueryMap Map<String, String> params, @Query("ottopay_instruction") boolean ottopayStatus);

}
