package com.otto.mart.api;

import android.content.Context;
import android.content.Intent;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.otto.mart.BuildConfig;
import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.model.APIModel.Request.grosir.AddressCreateUpdateDeleteRequest;
import com.otto.mart.model.APIModel.Request.grosir.CheckEligibleRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirCheckSupplierRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirCostShipmentRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirPostingRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirPostingRequestV2;
import com.otto.mart.model.APIModel.Request.grosir.GrosirRegisterSupplierRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirRequestListProduct;
import com.otto.mart.model.APIModel.Request.grosir.GrosirRequestSupplier;
import com.otto.mart.model.APIModel.Request.grosir.HistoryOasisOrderRequestModel;
import com.otto.mart.model.APIModel.Request.grosir.OasisApprovedOrderRequest;
import com.otto.mart.model.APIModel.Request.grosir.OasisListCategoryRequest;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.SystemUtil;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.settings.LanguageIdConverter;

import java.util.HashMap;
import java.util.Map;

import app.beelabs.com.codebase.base.BaseApi;
import retrofit2.Callback;

public class OasisAPI extends BaseApi implements GLOBAL {

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

    private static OasisServices initApiDomainOasis(Context context) {
        getInstance().setApiDomain(BuildConfig.GROSIR_SERVER_URL);
        return (OasisServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OasisServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }
    private static OasisServices initApiDomainOasisV2(Context context) {
        getInstance().setApiDomain(BuildConfig.GROSIR_SERVER_URL_V2);
        return (OasisServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OasisServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }



    private static OasisServices initApiDomainOttoKonek(Context context) {
        getInstance().setApiDomain(BuildConfig.GROSIR_DATA_ADDRESS_URL);
        return (OasisServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OasisServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    private static OasisServices initApiDomainOasisShipment(Context context) {
        getInstance().setApiDomain(BuildConfig.GROSIR_SHIPMENT_URL);
        return (OasisServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OasisServices.class, false, 180, BuildConfig.DEBUG, new ChuckerInterceptor(context));
    }

    synchronized public static void checkEligibleOasis(Context context, CheckEligibleRequest checkEligibleRequest, Callback callback) {
        initApiDomainOasis(context).checkEligibleOasis(initHeader(), checkEligibleRequest).enqueue(callback);
    }

    synchronized public static void opGrosirSupplierList(Context context, String area_id,String merchant_phone, Callback callback) {

        initApiDomainOasisV2(context).opGrosirSupplierList(initHeader(),area_id,merchant_phone).enqueue(callback);
    }

//    synchronized public static void getOttopointCollection(Context context, Callback callback) {
//
//        if (BuildConfig.FLAVOR.equals(AppKeys.FLAVOR_PRODUCTION)) {
//            String url = "https://belanja.ottopay.id/ottopay-merchant-svc/v1/validate-status";
//            String url = BuildConfig.BASE_URL_OTTOPOINT + "ottopay-apk-driver-svc/v2/balance";
//            //  initAuthApiDomain(context).checkUserLogin(initHeaderXdeviceIdComplete(), url, request).enqueue(callback);
//        } else {
//            String url = "https://dev.ottopay.api.ottopay.id/ottopay-apk-driver-svc/v2/balance";
//            String url = BuildConfig.BASE_URL_OTTOPOINT + "ottopay-apk-driver-svc/v2/balance";
//            initApiDomainOttopointV2(context).checkOttopoint(initHeaderOttopoint(), url).enqueue(callback);
//        }
//    }


    synchronized public static void opGrosirCheckSupplier(Context context, GrosirCheckSupplierRequest model, Callback callback) {
        initApiDomainOasis(context).opGrosirCheckSupplier(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirRegisterSupplier(Context context, GrosirRegisterSupplierRequest model, Callback callback) {
        initApiDomainOasis(context).opGrosirRegisterSupplier(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirSupplierListProduct(Context context, GrosirRequestListProduct model, Callback callback) {
        initApiDomainOasis(context).opGrosirSupplierListProduct(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirSupplierListProductScan(Context context, GrosirRequestListProduct model, Callback callback) {
        initApiDomainOasis(context).opGrosirSupplierListProductScan(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirSupplierListCategory(Context context, OasisListCategoryRequest model, Callback callback) {
        initApiDomainOasis(context).opGrosirSupplierListCategory(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirPosting(Context context, GrosirPostingRequest model, Callback callback) {
        initApiDomainOasis(context).opGrosirPosting(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirPostingv2(Context context, GrosirPostingRequestV2 model, Callback callback) {

        initApiDomainOasisV2(context).opGrosirPostingv2(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirShipmentDetail(Context context, GrosirCostShipmentRequest model, Callback callback) {
        initApiDomainOasisShipment(context).opGrosirShipmentDetail(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirCheckStatus(Context context, String order_no,Callback callback) {
        initApiDomainOasisV2(context).opGrosirCheckStatus(initHeader(),order_no).enqueue(callback);
    }

    synchronized public static void opGrosirCheckStatusApproved(Context context, OasisApprovedOrderRequest model, Callback callback) {

        initApiDomainOasisV2(context).opGrosirCheckStatusApproved(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirGetProvince(Context context, Callback callback) {
        initApiDomainOttoKonek(context).opGrosirGetProvince(initHeader()).enqueue(callback);
    }

    synchronized public static void opGrosirGetCities(Context context, String model, Callback callback) {
        initApiDomainOttoKonek(context).opGrosirGetCities(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirGetDistricts(Context context, String model, Callback callback) {
        initApiDomainOttoKonek(context).opGrosirGetDistricts(initHeader(), model).enqueue(callback);
    }

    synchronized public static void opGrosirGetVillages(Context context, String model, Callback callback) {
        initApiDomainOttoKonek(context).opGrosirGetVillages(initHeader(), model).enqueue(callback);
    }

    public static void CreateShippingAddressAPI(Context context, AddressCreateUpdateDeleteRequest model, Callback callback) {
        initApiDomainOasis(context).serviceCreateShippingAddress(initHeader(), model).enqueue(callback);
    }

    public static void ShippingAddressListAPI(Context context, Callback callback) {
        initApiDomainOasis(context).serviceGetShippingAddress(initHeader()).enqueue(callback);
    }

    public static void UpdateAddressAPI(Context context, AddressCreateUpdateDeleteRequest model, Callback callback) {
        initApiDomainOasis(context).serviceUpdateShippingAddress(initHeader(), model).enqueue(callback);
    }

    public static void DeleteAddressAPI(Context context, AddressCreateUpdateDeleteRequest model, Callback callback) {
        initApiDomainOasis(context).serviceDeleteShippingAddress(initHeader(), model).enqueue(callback);
    }

}
