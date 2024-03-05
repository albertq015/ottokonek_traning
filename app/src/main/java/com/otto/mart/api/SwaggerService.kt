package com.otto.mart.api

import com.otto.mart.model.APIModel.Request.*
import com.otto.mart.model.APIModel.Request.bank.AddBankDepositRequest
import com.otto.mart.model.APIModel.Request.inbox.InboxReadRequest
import com.otto.mart.model.APIModel.Request.refund.MerchantRefundRequest
import com.otto.mart.model.APIModel.Request.register.SignUpRequest
import com.otto.mart.model.APIModel.Request.transfer.TransferBankInquiryRequest
import com.otto.mart.model.APIModel.Request.transfer.TransferBankPaymentRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.SignupOtpResponseModel
import com.otto.mart.model.APIModel.Response.SignupResponseModel
import com.otto.mart.model.APIModel.Response.bank.BankAccountListOKKResponse
import com.otto.mart.model.APIModel.Response.inbox.InboxResponse
import com.otto.mart.model.APIModel.Response.merchant.featureProduct.FeatureProductResponse
import com.otto.mart.model.APIModel.Response.merchant.theme.MerchantThemeResponse
import com.otto.mart.model.APIModel.Response.refund.MerchantRefundResponse
import com.otto.mart.model.APIModel.Response.register.SearchMerchantResponse
import com.otto.mart.model.APIModel.Response.transfer.TransferBankInquiryResponse
import retrofit2.Call
import retrofit2.http.*

interface SwaggerService {

    @GET("ottokonek/v1.0/account/inbox")
    fun notificationListService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>, @Query("page") page: Int): Call<InboxResponse>

    @PUT("ottokonek/v1.0/account/inbox/all")
    fun notificationActionService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>, @Query("action") action: String): Call<InboxResponse>

    @PUT("ottokonek/v1.0/account/inbox/bulk")
    fun notificationReadBulkService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>, @Body body: InboxReadRequest): Call<InboxResponse>

    @POST("ottokonek/v1.0/qr/checkstatus")
    fun qrCheckStatusService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>, @Body body: PpobTransactionAdviceModel): Call<PpobOttoagPaymentResponseModel>

    @POST("ottokonek/v1.0/qr/refund")
    fun qrRefundService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>, @Body body: MerchantRefundRequest): Call<MerchantRefundResponse>

    @GET("ottokonek/v1.0/account/list")
    fun bankListService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>): Call<BankAccountListOKKResponse>

    @POST("ottokonek/v1.0/account/add")
    fun addBankService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>, @Body body: AddBankDepositRequest): Call<BaseResponseModel>

    @POST("ottokonek/v1.0/transfer/inquiry")
    fun transferBankInquiryService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>, @Body body: TransferBankInquiryRequest): Call<TransferBankInquiryResponse>

    @POST("ottokonek/v1.0/transfer/payment")
    fun transferBankPaymentService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>, @Body body: TransferBankPaymentRequest): Call<BasePaymentResponseModel>

    @POST("ottokonek/v1.0/merchant/balance")
    fun balanceService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>): Call<BaseResponseModel>

    @POST("ottokonek/v1.0/merchant/revenue")
    fun revenueService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>): Call<BaseResponseModel>

    @POST("auth/v1.0/ottokonek/sign-up/search-merchant")
    fun searchMerchantService(@Body body: CheckMerchantIdRequestModel): Call<SearchMerchantResponse>

    @POST("auth/v1.0/ottokonek/sign-up/posting-acquisition")
    fun postAcquisitionService(@HeaderMap key: Map<String, String>, @Body body: PostAcquisitionRequest): Call<BaseResponseModel>

    @POST("auth/v1.0/ottokonek/sign-up")
    fun signUpService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>, @Body body: SignUpRequest): Call<SignupResponseModel>

    @POST("auth/v1.0/ottokonek/sign-up-otp")
    fun signUpOTPService(@HeaderMap key: Map<String, String>, @HeaderMap header: Map<String, String>, @Body body: RegisterOtpRequestModel): Call<SignupOtpResponseModel>

    @POST("auth/v1.0/ottokonek/resend-otp")
    fun resendOtpService(@HeaderMap key: Map<String, String>, @Body body: ResendOtpRegisterRequestModel): Call<BaseResponseModel>

    //Merchant Theme
    @GET("ottokonek/" + OttoKonekServices.API_VERSION + "/dashboard/banner")
    fun merchantTheme(@HeaderMap token: Map<String?, String?>?, @HeaderMap accessToken: Map<String?, String?>?): Call<MerchantThemeResponse>

    //Feature Product
    @GET("ottokonek/" + OttoKonekServices.API_VERSION + "/merchant/feature/product")
    fun featureProduct(@HeaderMap key: Map<String?, String?>?, @HeaderMap accessToken: Map<String?, String?>?): Call<FeatureProductResponse>

}