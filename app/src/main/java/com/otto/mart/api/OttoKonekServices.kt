package com.otto.mart.api

import com.otto.mart.BuildConfig
import com.otto.mart.model.APIModel.Request.*
import com.otto.mart.model.APIModel.Request.bank.AddBankDepositRequest
import com.otto.mart.model.APIModel.Request.cashOut.CashOutInputConfirmationRequest
import com.otto.mart.model.APIModel.Request.cashOut.CashOutInputRequest
import com.otto.mart.model.APIModel.Request.inbox.InboxReadRequest
import com.otto.mart.model.APIModel.Request.multibank.*
import com.otto.mart.model.APIModel.Request.qr.CheckStatusQrRequest
import com.otto.mart.model.APIModel.Request.qr.QrPaymentRequest
import com.otto.mart.model.APIModel.Request.refund.MerchantRefundRequest
import com.otto.mart.model.APIModel.Request.register.SignUpRequest
import com.otto.mart.model.APIModel.Request.revenue.WithdrawRevenueRequest
import com.otto.mart.model.APIModel.Request.transfer.TransferBankInquiryRequest
import com.otto.mart.model.APIModel.Request.transfer.TransferBankPaymentRequest
import com.otto.mart.model.APIModel.Response.*
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.balance.OttoKonekBalanceResponse
import com.otto.mart.model.APIModel.Response.bank.BankAccountListOKKResponse
import com.otto.mart.model.APIModel.Response.bank.BankListResponse
import com.otto.mart.model.APIModel.Response.cashOut.CashOutInputConfirmationResponse
import com.otto.mart.model.APIModel.Response.cashOut.CashOutInputResponse
import com.otto.mart.model.APIModel.Response.cashOut.CashOutListResponse
import com.otto.mart.model.APIModel.Response.history.BankHistoryResponse
import com.otto.mart.model.APIModel.Response.history.OmzetHistoryResponse
import com.otto.mart.model.APIModel.Response.inbox.InboxResponse
import com.otto.mart.model.APIModel.Response.merchant.featureProduct.FeatureProductResponse
import com.otto.mart.model.APIModel.Response.merchant.theme.MerchantThemeResponse
import com.otto.mart.model.APIModel.Response.multibank.*
import com.otto.mart.model.APIModel.Response.profile.ProfileResponse
import com.otto.mart.model.APIModel.Response.qr.CheckStatusQrResponse
import com.otto.mart.model.APIModel.Response.qr.QrInquiryResponse
import com.otto.mart.model.APIModel.Response.refund.MerchantRefundResponse
import com.otto.mart.model.APIModel.Response.register.SearchMerchantResponse
import com.otto.mart.model.APIModel.Response.revenue.WithdrawRevenueResponse
import com.otto.mart.model.APIModel.Response.storeLocation.BarangayResponse
import com.otto.mart.model.APIModel.Response.storeLocation.MunicipalityResponse
import com.otto.mart.model.APIModel.Response.storeLocation.ProvinceResponse
import com.otto.mart.model.APIModel.Response.storeLocation.RegionalResponse
import com.otto.mart.model.APIModel.Response.transfer.TransferBankInquiryResponse
import retrofit2.Call
import retrofit2.http.*

interface OttoKonekServices {

    companion object {
        const val API_VERSION = BuildConfig.OTTOKONEK_API_VERSION
    }

    /**
     * Auth
     */

    //region Auth

    //Login
    @POST("auth/v2.0/ottokonek/login")
    fun login(
        @HeaderMap key: Map<String, String>,
        @Body model: LoginRequestModel
    ): Call<LoginResponseModel>

    //Login OTP
    @POST("auth/" + API_VERSION + "/ottokonek/login-otp")
    fun loginOTP(
        @HeaderMap key: Map<String, String>,
        @Body model: LoginOtpRequest
    ): Call<LoginOtpResponseModel>

    @POST("ottopay/" + OttofinApiServices.API_VERSION + "/ottopay-mart/check-version")
    fun versionCheck(@Body model: CheckVersionRequestModel?): Call<CheckVersionResponseModel?>?

    //Resend OTP
    @POST("auth/" + API_VERSION + "/ottokonek/resend-otp/fds")
    fun resendOTP(
        @HeaderMap key: Map<String, String>,
        @Body model: ResendOtpRegisterRequestModel
    ): Call<BaseResponseModel>

    //Forgot PIN OTP
    @POST("auth/" + API_VERSION + "/ottokonek/forgot-pin/otp")
    fun forgotPinOtp(
        @HeaderMap key: Map<String, String>,
        @Body model: ResetOtpPinRequestModel
    ): Call<ResetOtpPinResponseModel>

    //Forgot Pin Reset
    @POST("auth/" + API_VERSION + "/ottokonek/forgot-pin/reset")
    fun forgotPin(
        @HeaderMap key: Map<String, String>,
        @Body model: ResetPinRequestModel
    ): Call<ResetPinResponseModel>

    //Change PIN
    @POST("auth/" + API_VERSION + "/ottokonek/change-pin")
    fun changePin(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>,
        @Body model: UpdatePinRequestModel
    ): Call<DataEmptyResponseModel>

    @POST("auth/v1.0/ottokonek/sign-up/search-merchant")
    fun searchMerchantService(@Body body: CheckMerchantIdRequestModel): Call<SearchMerchantResponse>

    @POST("auth/v1.0/ottokonek/sign-up/posting-acquisition")
    fun postAcquisitionService(
        @HeaderMap key: Map<String, String>,
        @Body body: PostAcquisitionRequest
    ): Call<BaseResponseModel>

    @POST("auth/v1.0/ottokonek/sign-up")
    fun signUpService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: SignUpRequest
    ): Call<SignupResponseModel>

    @POST("auth/v1.0/ottokonek/sign-up-otp")
    fun signUpOTPService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: RegisterOtpRequestModel
    ): Call<SignupOtpResponseModel>

    //resend otp register
    //@POST("auth/v1.0/ottokonek/resend-otp/fds-open-api")
    @POST("auth/" + API_VERSION + "/ottokonek/resend-otp/fds")
    fun resendOtpService(
        @HeaderMap key: Map<String, String>,
        @Body body: ResendOtpRegisterRequestModel
    ): Call<BaseResponseModel>

    //resend otp register
    @POST("auth/v1.0/ottokonek/logout")
    fun logout(
        @HeaderMap key: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>
    ): Call<BaseResponseModel>

    //endregion Auth


    /**
     * Emoney
     */
    //region Emoney

    //Revenue History
    @GET("op-emoney/ottokonek/v0.1.0/transaction/history/revenue")
    fun revenueHistory(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>,
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String,
        @Query("paymentMethod") paymentMethod: String,
        @Query("transactionType") transactionType: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Call<OmzetHistoryResponse>

    // revenue history account

    @GET("op-emoney/ottokonek/v0.1.0/transaction/history/bank")
    fun revenueHistoryAccountList(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>,
        @Query("accountNumber") accountNumber: String,
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Call<HistoryAccountResponse>
    //Bank History
    @GET("op-emoney/ottokonek/v0.1.0/transaction/history/bank")
    fun bankHistory(
        @HeaderMap token: Map<String, String>, @HeaderMap accessToken: Map<String, String>,
        @Query("accountNumber") accountNumber: String,
        @Query("dateFrom") dateFrom: String, @Query("dateTo") dateTo: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Call<BankHistoryResponse>

    //endregion Emoney


    /**
     * Dashboard
     */
    //region Dashboard

    //Banner
    @GET("ottokonek/" + API_VERSION + "/dashboard/banner")
    fun banner(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>
    ): Call<BannerRespomnse>

    //Merchant Theme
    @GET("ottokonek/" + API_VERSION + "/dashboard/banner")
    fun merchantTheme(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>
    ): Call<MerchantThemeResponse>

    //Feature Product
    @GET("ottokonek/" + API_VERSION + "/merchant/feature/product")
    fun featureProduct(
        @HeaderMap key: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>
    ): Call<FeatureProductResponse>

    //Revenue
    @GET("ottokonek/" + API_VERSION + "/merchant/revenue")
    fun revenue(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>
    ): Call<OttoKonekBalanceResponse>

    //Balance
    @GET("ottokonek/" + API_VERSION + "/merchant/balance")
    fun balance(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>
    ): Call<OttoKonekBalanceResponse>

    //endregion Dashboard


    /**
     * Payment QR
     */

    @POST("ottokonek/v1.0/qr/checkstatus")
    fun qrCheckStatusService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: PpobTransactionAdviceModel
    ): Call<PpobOttoagPaymentResponseModel>


    @POST("ottokonek/v1.0/qr/refund")
    fun qrRefundService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: MerchantRefundRequest
    ): Call<MerchantRefundResponse>

    //QR String
    @POST("ottokonek/" + API_VERSION + "/qr/generate")
    fun qrString(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>,
        @Body requestModel: QrStringRequestModel
    ): Call<QrStringResponseModel>

    //Pay QR Inquiry

    @POST("ottokonek/v2.0/payment-qr/inquiry")
    fun payQrInqury(
        @HeaderMap key: Map<String?, String?>?,
        @HeaderMap accessToken: Map<String?, String?>?,
        @Body model: PayQrInquiryRequestModel?
    ): Call<QrInquiryResponse?>?

    @POST("ottokonek/v2.0/payment-qr/validate")
    //  @POST("ottokonek/" + API_VERSION + "/qr/issuer/inquiry")
    fun payQrValidate(
        @HeaderMap key: Map<String?, String?>?,
        @HeaderMap accessToken: Map<String?, String?>?,
        @Body model: ValidateQrRequest?
    ): Call<ValidateQrResponse?>?


    //Pay QR Payment
    @POST("/ottokonek/v2.0/payment-qr/confirm")
    fun payQrPayment(
        @HeaderMap key: Map<String?, String?>?,
        @HeaderMap accessToken: Map<String?, String?>?,
        @Body model: QrPaymentRequest?
    ): Call<BasePaymentResponseModel?>?

    //Check Status QR Payment
    @POST("ottokonek/" + API_VERSION + "/qr/checkstatus")
    fun checkStatusQrPayment(
        @HeaderMap key: Map<String?, String?>?,
        @HeaderMap accessToken: Map<String?, String?>?,
        @Body model: CheckStatusQrRequest?
    ): Call<CheckStatusQrResponse?>?


    /**
     * Cash Out
     */

    //Cash Out Input
    @POST("ottokonek/v2.0/cashout/inquiry")
    fun cashOutInput(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>,
        @Body requestModel: CashOutInputRequest
    ): Call<CashOutInputResponse>

    //Cash Out Input Confirmation
    @POST("/ottokonek/v2.0/cashout/confirm")
    fun cashOutInputConfirmation(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>,
        @Body requestModel: CashOutInputConfirmationRequest
    ): Call<CashOutInputConfirmationResponse>

    //Cash Out Input Confirmation
    @GET("ottokonek/v2.0/cashout/code/list")
    fun cashOutList(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>
    ): Call<CashOutListResponse>


    /**
     * Revenue
     */

    @POST("ottokonek/" + API_VERSION + "/withdraw/revenue")
    fun withdrawRevenuePayment(
        @HeaderMap token: Map<String, String>,
        @HeaderMap accessToken: Map<String, String>,
        @Body requestModel: WithdrawRevenueRequest
    ): Call<WithdrawRevenueResponse>

    /**
     * Inbox
     * */

    @GET("ottokonek/v1.0/account/inbox")
    fun notificationListService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Query("page") page: Int
    ): Call<InboxResponse>

    @PUT("ottokonek/v1.0/account/inbox/all")
    fun notificationActionAllService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Query("action") action: String
    ): Call<InboxResponse>

    @PUT("ottokonek/v1.0/account/inbox/bulk")
    fun notificationReadBulkService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: InboxReadRequest
    ): Call<InboxResponse>

    @PUT("ottokonek/v1.0/account/inbox")
    fun notificationActionService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: InboxReadRequest
    ): Call<InboxResponse>

    /**
     * Bank
     * */
    @GET("ottokonek/v1.0/account/list")
    fun bankAccountListService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Call<BankAccountListOKKResponse>

    @POST("ottokonek/v1.0/account/add")
    fun addBankService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: AddBankDepositRequest
    ): Call<BaseResponseModel>

    @POST("ottokonek/v1.0/account/edit")
    fun editBankService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: AddBankDepositRequest
    ): Call<BaseResponseModel>

    @GET("ottokonek/v1.0/bank/list")
    fun bankListService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Call<BankListResponse>

    /**
     * Transfer Bank
     * */
    @POST("ottokonek/v1.0/transfer/inquiry")
    fun transferBankInquiryService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: TransferBankInquiryRequest
    ): Call<TransferBankInquiryResponse>

    @POST("ottokonek/v1.0/transfer/payment")
    fun transferBankPaymentService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: TransferBankPaymentRequest
    ): Call<BasePaymentResponseModel>


    //Update Status UR/RR --> U/R
    @POST("auth/" + OttofinApiServices.AUTH_API_VERSION + "/ottopay/update-status")
    fun updateStatus(
        @HeaderMap key: Map<String?, String?>?,
        @HeaderMap accessToken: Map<String?, String?>?,
        @Body model: UpdateStatusRequest?
    ): Call<UpdateStatusResponse>

    /**
     *Contact US
     * */
    @GET("ottokonek/v1.0/contactus")
    fun contactUsService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Call<ContactUsResponse>

    /**
     * Profile
     * */
    @GET("ottokonek/v1.0/merchant/info/profile")
    fun profileService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Call<ProfileResponse>

    @PUT("ottokonek/v1.0/account/profile")
    fun profileUpdateService(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: UpdateProfileRequestModel
    ): Call<BaseResponseModel>


    /**
     * Store Location
     */
    //region Store Location

    //Get All Regional
    @GET("ottokonek/" + API_VERSION + "/merchant/location/region")
    fun getAllRegional(@HeaderMap key: Map<String, String>): Call<RegionalResponse>


    //Get Province
    @GET("ottokonek/" + API_VERSION + "/merchant/location/province/{code}")
    fun getProvince(
        @HeaderMap key: Map<String, String>,
        @Path("code") code: String
    ): Call<ProvinceResponse>

    //Get Municipality
    @GET("ottokonek/" + API_VERSION + "/merchant/location/municipality/{code}")
    fun getMunicipality(
        @HeaderMap key: Map<String, String>,
        @Path("code") code: String
    ): Call<MunicipalityResponse>

    //Get Barangays
    @GET("ottokonek/" + API_VERSION + "/merchant/location/barangay/{code}")
    fun getBarangays(
        @HeaderMap key: Map<String, String>,
        @Path("code") code: String
    ): Call<BarangayResponse>

    //list account bank



    @GET("/ottokonek/v2.0/issuer-linked/list")
    fun getListAccounBank(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Call<AccountListResponse>

    @GET("/ottokonek/v2.0/issuer-linked/list")
    fun getListAccounBankWithBin(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Query("bin") paymentMethod: String
    ): Call<AccountListResponse>

    @GET("/ottokonek/v2.0/partner-bank/list")
    fun getListPartnerBank(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Call<PartnerBankListResponse>


    @GET("/ottokonek/v2.0/account-type/list")
    fun getListAccountType(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Call<AccountTypeListResponse>

    @POST("/ottokonek/v2.0/issuer-linked/request")
    fun issuerLinkedRequest(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: IssuerLinkedRequest
    ): Call<BasePaymentResponseModel>


    @POST("/ottokonek/v2.0/issuer-linked/confirm")
    fun issuerLinkedConfrim(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: IssuerLinkedConfrim
    ): Call<BasePaymentResponseModel>


    @POST("/ottokonek/v2.0/issuer-linked/balance")
    fun issuerLinkedBalance(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: IssuerLinkedBalance
    ): Call<IssuerLinkedBalanceResponse>

    //ini

    @GET("/ottokonek/v2.0/transfer-bank/list")
    fun getTransferBankList(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>

    ): Call<BankTransferListResponse>

    @GET("/ottokonek/v2.0/transfer-bank/list")
    fun getTransferBankListMid(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Query("bin") accountNumber: String

    ): Call<BankTransferListResponse>


    @GET("/ottokonek/v2.0/transfer-bank/list")
    fun getTransferBankListWithBin(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Query("bin") bin: String
    ): Call<BankTransferListResponse>


    @POST("/ottokonek/v2.0/transfer-bank/add")
    fun postAddBankAccount(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: AddBankAccountRequest
    ): Call<BasePaymentResponseModel>

    @POST("/ottokonek/v2.0/transfer-bank/inquiry")
    fun postTransferBankInquiry(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: InqueryTransferRequest
    ): Call<TransferBankInquiryMultiBankResponse>

    @POST("/ottokonek/v2.0/transfer-bank/add/inquiry")
    fun postBankAddInquiry(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: AddBankInquiryRequest
    ): Call<TransferBankInquiryMultiBankResponse>



    @POST("/ottokonek/v2.0/transfer-bank/confirm")
    fun postConfrimTransferBank(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: TransferMultiBankConfrimRequest
    ): Call<BasePaymentResponseModel>

    //withdraw v.2

    @POST("/ottokonek/v2.0/withdraw")
    fun withdraw(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: WithdrawRequest
    ): Call<TransferMultiBankConfrimResponse>


    @GET("/ottokonek/v2.0/contact-receipt")
    fun getContactReceipt(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Call<ReceiptAdressResponse>

    @POST("/ottokonek/v2.0/issuer-linked/rose/inquiry")
    fun issuerLinkedAddIquiry(
        @HeaderMap key: Map<String, String>,
        @HeaderMap header: Map<String, String>,
        @Body body: IssuerLinkedInquiryRequest
    ): Call<TransferMultiBankConfrimResponse>
}