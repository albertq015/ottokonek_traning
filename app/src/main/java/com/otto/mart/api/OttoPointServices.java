package com.otto.mart.api;

import com.otto.mart.model.APIModel.Request.ottopoint.OpCheckEligibleRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpEarningPointPulsaRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpLoginRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRedeemVoucherDealsRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRedeemVoucherSayaRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRegisterRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpVoucherComulativeRequestModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpBalancePointResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpCheckEligibleResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpEarningPointPulsaResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpEarningResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpHistoryTransactionResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpLoginResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpRedeemVoucherDealsResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpRedeemVoucherSayaResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpRegisterResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherComulativeResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherDealsDetailResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherDealsResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherSayaResponseModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OttoPointServices {

    /**
     * OttoPoint
     */

    @POST("register")
    Call<OpRegisterResponseModel> opRegister(@HeaderMap Map<String, String> key, @Body OpRegisterRequestModel model);

    @GET("admin/customer/{customerId}/status")
    Call<OpEarningResponseModel> opEarningPoint(@HeaderMap Map<String, String> key, @Path("customerId") String customerId);

    @POST("customer/login_check")
    Call<OpLoginResponseModel> opLogin(@HeaderMap Map<String, String> key, @Body OpLoginRequestModel model);

    @GET("check_balance")
    Call<OpBalancePointResponseModel> opBalancePoint(@HeaderMap Map<String, String> key);

    @POST("rule_point")
    Call<OpEarningPointPulsaResponseModel> opEarningPointPulsa(@HeaderMap Map<String, String> key, @Body OpEarningPointPulsaRequestModel model);

    @POST("check_eligible")
    Call<OpCheckEligibleResponseModel> opCheckEligible(@Body OpCheckEligibleRequestModel model);

    @GET("history")
    Call<OpHistoryTransactionResponseModel> opHistoryTransaction(@HeaderMap Map<String, String> key);

    @GET("voucher/myvoucher")
    Call<OpVoucherSayaResponseModel> opVoucherSayaActive(@HeaderMap Map<String, String> key, @Query("page") int page, @Query("harga") String harga, @Query("periode") String periode);

    @GET("voucher/history")
    Call<OpVoucherSayaResponseModel> opVoucherSayaHistory(@HeaderMap Map<String, String> key,  @Query("page") int page, @Query("harga") String harga, @Query("periode") String periode);

    @GET("voucher/detail")
    Call<OpVoucherDealsDetailResponseModel> opVoucherSayaDetail(@HeaderMap Map<String, String> key, @Query("campaign") String campaign);

    @GET("voucher/list")
    Call<OpVoucherDealsResponseModel> opVoucherDeals(
            @HeaderMap Map<String, String> key,
            @Query("page") int page,
            @Query("campaignName") String campaignName,
            @Query("campaignType") String campaignType,
            @Query("harga") String harga,
            @Query("periode") String periode,
            @Query("kadaluarsa") String kadaluarsa,
            @Query("promo") boolean promo,
            @Query("minHarga") long minHarga,
            @Query("maxHarga") long maxHarga
    );

    @GET("voucher/list")
    Call<OpVoucherDealsResponseModel> opVoucherDeals(
            @HeaderMap Map<String, String> key,
            @Query("page") int page,
            @Query("campaignName") String campaignName,
            @Query("campaignType") String campaignType,
            @Query("harga") String harga,
            @Query("periode") String periode,
            @Query("kadaluarsa") String kadaluarsa,
            @Query("promo") boolean promo
    );

    @GET("voucher/list/promo")
    Call<OpVoucherDealsResponseModel> opVoucherDealsPromo(
            @HeaderMap Map<String, String> key,
            @Query("page") int page,
            @Query("campaignName") String campaignName,
            @Query("campaignType") String campaignType,
            @Query("harga") String harga,
            @Query("periode") String periode,
            @Query("kadaluarsa") String kadaluarsa,
            @Query("promo") boolean promo,
            @Query("minHarga") long minHarga,
            @Query("maxHarga") long maxHarga
    );

    @GET("voucher/list/promo")
    Call<OpVoucherDealsResponseModel> opVoucherDealsPromo(
            @HeaderMap Map<String, String> key,
            @Query("page") int page,
            @Query("campaignName") String campaignName,
            @Query("campaignType") String campaignType,
            @Query("harga") String harga,
            @Query("periode") String periode,
            @Query("kadaluarsa") String kadaluarsa,
            @Query("promo") boolean promo
    );

    @GET("voucher/detail")
    Call<OpVoucherDealsDetailResponseModel> opVoucherDealsDetail(@HeaderMap Map<String, String> key, @Query("campaign") String campaign);

    @POST("voucher/redeem")
    Call<OpRedeemVoucherDealsResponseModel> opVoucherDealsRedeem(@HeaderMap Map<String, String> key, @Body OpRedeemVoucherDealsRequestModel campaign);

    @POST("voucher/use_coupon")
    Call<OpRedeemVoucherSayaResponseModel> opVoucherSayaRedeem(@HeaderMap Map<String, String> key, @Body OpRedeemVoucherSayaRequestModel campaign);

    @POST("voucher/use_voucher_comulative")
    Call<OpVoucherComulativeResponseModel> opVoucherComulative(@HeaderMap Map<String, String> key, @Body OpVoucherComulativeRequestModel campaign);
}
