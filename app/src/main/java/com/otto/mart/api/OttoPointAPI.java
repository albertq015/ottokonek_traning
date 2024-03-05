package com.otto.mart.api;

import android.content.Context;
import android.content.Intent;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.otto.mart.BuildConfig;
import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.model.APIModel.Request.ottopoint.OpCheckEligibleRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpEarningPointPulsaRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpLoginRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRedeemVoucherDealsRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRedeemVoucherSayaRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRegisterRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpVoucherComulativeRequestModel;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.SystemUtil;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.settings.LanguageIdConverter;

import java.util.HashMap;
import java.util.Map;

import app.beelabs.com.codebase.base.BaseApi;
import retrofit2.Callback;

public class OttoPointAPI extends BaseApi implements GLOBAL {

    private static Map<String, String> initHeader() {
        if (SessionManager.getAccessToken() != null &&
                SessionManager.isLoggedIn() &&
                SessionManager.getAccessToken().equals("TokenNaN")) {
            SessionManager.logout();
            OttoMartApp.getContext().startActivity(new Intent(OttoMartApp.getContext(), LoginActivity.class));
        }
        Map<String, String> map = new HashMap<>();
        map.put("Device-Id", SystemUtil.getDeviceId(OttoMartApp.getContext()));
        map.put("Authorization", "Bearer " + SessionManager.getAccessToken());
        map.put("Language-Id", new LanguageIdConverter().getLanguageId());
        return map;
    }

    private static OttoPointServices initApiDomainOttopoint(Context context) {
        getInstance().setApiDomain(BuildConfig.OTTOPOINT_SERVER_URL);
        return (OttoPointServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttoPointServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static OttoPointServices initApiDomainGrosir(Context context) {
        getInstance().setApiDomain(BuildConfig.GROSIR_SERVER_URL);
        return (OttoPointServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OttoPointServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    synchronized public static void opRegister(Context context, OpRegisterRequestModel model, Callback callback) {
        initApiDomainOttopoint(context).opRegister(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opEarningPoint(Context context, String uuid, Callback callback) {
        initApiDomainOttopoint(context).opEarningPoint(initHeader(), uuid).enqueue(callback);
    }

    synchronized public static void opLogin(Context context, OpLoginRequestModel model, Callback callback) {
        initApiDomainOttopoint(context).opLogin(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opBalancePoint(Context context, String phone, Callback callback) {
        initApiDomainOttopoint(context).opBalancePoint(initHeader()).enqueue(callback);
    }

    synchronized public static void opEarningPointFromPulsa(Context context, OpEarningPointPulsaRequestModel model, Callback callback) {
        initApiDomainOttopoint(context).opEarningPointPulsa(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opCheckEligible(Context context, OpCheckEligibleRequestModel model, Callback callback) {
        initApiDomainOttopoint(context).opCheckEligible(model).enqueue(callback);
    }

    synchronized public static void opHistoryTransaction(Context context, Callback callback) {
        initApiDomainOttopoint(context).opHistoryTransaction(initHeader()).enqueue(callback);
    }

    synchronized public static void opVoucherSayaActive(Context context, int page, String harga, String periode, Callback callback) {
        initApiDomainOttopoint(context).opVoucherSayaActive(initHeader(), page, harga, periode).enqueue(callback);
    }

    synchronized public static void opVoucherSayaHistory(Context context, int page, String harga, String periode, Callback callback) {
        initApiDomainOttopoint(context).opVoucherSayaHistory(initHeader(), page, harga, periode).enqueue(callback);
    }

    synchronized public static void opVoucherSayaDetail(Context context, String campaignId, Callback callback) {
        initApiDomainOttopoint(context).opVoucherSayaDetail(initHeader(), campaignId).enqueue(callback);
    }

    synchronized public static void opVoucherDeals(Context context,  int page, String campaignName, String campaignType, String harga, String periode, String kadaluarsa, boolean isPromo, long minHarga, long maxHarga, Callback callback) {
        initApiDomainOttopoint(context).opVoucherDeals(initHeader(), page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo, minHarga, maxHarga).enqueue(callback);
    }

    synchronized public static void opVoucherDeals(Context context,  int page, String campaignName, String campaignType, String harga, String periode, String kadaluarsa, boolean isPromo, Callback callback) {
        initApiDomainOttopoint(context).opVoucherDeals(initHeader(), page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo).enqueue(callback);
    }

    synchronized public static void opVoucherDealsPromo(Context context,  int page, String campaignName, String campaignType, String harga, String periode, String kadaluarsa, boolean isPromo, long minHarga, long maxHarga, Callback callback) {
        initApiDomainOttopoint(context).opVoucherDealsPromo(initHeader(), page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo, minHarga, maxHarga).enqueue(callback);
    }

    synchronized public static void opVoucherDealsPromo(Context context,  int page, String campaignName, String campaignType, String harga, String periode, String kadaluarsa, boolean isPromo, Callback callback) {
        initApiDomainOttopoint(context).opVoucherDealsPromo(initHeader(), page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo).enqueue(callback);
    }

    synchronized public static void opVoucherDealsDetail(Context context, String campaignId, Callback callback) {
        initApiDomainOttopoint(context).opVoucherDealsDetail(initHeader(),campaignId).enqueue(callback);
    }

    synchronized public static void opVoucherDealsRedeem(Context context, OpRedeemVoucherDealsRequestModel model, Callback callback) {
        initApiDomainOttopoint(context).opVoucherDealsRedeem(initHeader(),model).enqueue(callback);
    }

    synchronized public static void opVoucherSayaRedeem(Context context, OpRedeemVoucherSayaRequestModel model, Callback callback) {
        initApiDomainOttopoint(context).opVoucherSayaRedeem(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opVoucherComulative(Context context, OpVoucherComulativeRequestModel model, Callback callback) {
        initApiDomainOttopoint(context).opVoucherComulative(initHeader(), model).enqueue(callback);
    }
}
