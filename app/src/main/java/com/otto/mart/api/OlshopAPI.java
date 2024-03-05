package com.otto.mart.api;

import android.content.Intent;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.otto.mart.BuildConfig;
import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.model.APIModel.Request.olshop.AddCartRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ConfirmPaid;
import com.otto.mart.model.APIModel.Request.olshop.OrderConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.PaymentJurnalRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ProductListRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ShippingCostRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.address.ShippingAddressRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.cart.UpdateCartRequestModel;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.SystemUtil;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.settings.LanguageIdConverter;

import java.util.HashMap;
import java.util.Map;

import app.beelabs.com.codebase.base.BaseApi;
import retrofit2.Callback;

public class OlshopAPI extends BaseApi implements GLOBAL {

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

    private static OlshopApiServices initApiDomain() {
//        getInstance().setApiDomain(isProduction ? production_tokol_server : dev_tokol_server + "x/");
//        getInstance().setupApi(OttoMartApp.getAppComponent(), OlshopApiServices.class);

        getInstance().setApiDomain(BuildConfig.TOKOL_URL);
        return (OlshopApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OlshopApiServices.class, false, 180, BuildConfig.DEBUG, null, new ChuckerInterceptor(OttoMartApp.getContext()));
    }

    private static OlshopApiServices initApiDomain15s() {
        getInstance().setApiDomain(BuildConfig.TOKOL_URL);
        return (OlshopApiServices) getInstance().setupApi(OttoMartApp.getAppComponent(),
                OlshopApiServices.class, false, 15, BuildConfig.DEBUG, null, new ChuckerInterceptor(OttoMartApp.getContext()));
    }

    public static void getCategoriesAPI(Callback callback) {
        initApiDomain15s().serviceGetCategories(initAccessTokenHeader()).enqueue(callback);
    }

    public static void getProductListAPI(String path, ProductListRequestModel model, Callback callback) {
        initApiDomain().serviceGetProducts(initAccessTokenHeader(), path, model).enqueue(callback);
    }

    public static void getSpecialProductListAPI(Callback callback) {
        initApiDomain15s().serviceGetSpecialEventProduct(initAccessTokenHeader()).enqueue(callback);
    }

    public static void getProductDetailAPI(int path, Callback callback) {
        initApiDomain().serviceGetProductDetail(initAccessTokenHeader(), path).enqueue(callback);
    }

    public static void getCartListAPI(Callback callback) {
        initApiDomain().serviceGetCartList(initAccessTokenHeader()).enqueue(callback);
    }

    public static void AddCartAPI(AddCartRequestModel model, Callback callback) {
        initApiDomain().serviceAddToCart(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void DeleteCartAPI(String cartItemId, Callback callback) {
        Map<String,String> headers=new HashMap<>(initAccessTokenHeader());
        headers.put("Connection","close");
        initApiDomain().serviceDeleteCart(headers,cartItemId).enqueue(callback);
    }

    public static void UpdateCartAPI(UpdateCartRequestModel model, Callback callback) {
        initApiDomain().serviceUpdateCart(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void OrderConfirmation(OrderConfirmationRequestModel model, Callback callback) {
        initApiDomain().serviceOrderConfirmation(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void ShippingCostAPI(ShippingCostRequestModel model, Callback callback) {
        initApiDomain().serviceShippingCost(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void ConfirmOrderPaidAPI(ConfirmPaid model, Callback callback) {
        initApiDomain().serviceConfirmOrderPaid(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void GetProvinceAPI(Callback callback) {
        initApiDomain().serviceGetProvince(initAccessTokenHeader()).enqueue(callback);
    }

    public static void GetCityAPI(String provinceId, Callback callback) {
        initApiDomain().serviceGetCity(initAccessTokenHeader(), provinceId).enqueue(callback);
    }

    public static void GetDistrictAPI(String cityId, Callback callback) {
        initApiDomain().serviceGetDistrict(initAccessTokenHeader(), cityId).enqueue(callback);
    }

    public static void GetMailNoAPI(long districtId, Callback callback) {
        initApiDomain().serviceGetMailNo(initAccessTokenHeader(), districtId).enqueue(callback);
    }

    public static void CreateShippingAddressAPI(ShippingAddressRequestModel model, Callback callback) {
        initApiDomain().serviceCreateShippingAddress(initAccessTokenHeader(), model).enqueue(callback);
    }

    public static void ShippingAddressListAPI(Callback callback) {
        initApiDomain().serviceGetShippingAddress(initAccessTokenHeader()).enqueue(callback);
    }

    public static void UpdateAddressAPI(ShippingAddressRequestModel model, String addressId, Callback callback) {
        initApiDomain().serviceUpdateShippingAddress(initAccessTokenHeader(), model, addressId).enqueue(callback);
    }

    public static void DeleteAddressAPI(String addressId, Callback callback) {
        initApiDomain().serviceDeleteShippingAddress(initAccessTokenHeader(), addressId).enqueue(callback);
    }

    public static void GetOrderStatusAPI(Map<String, String> params, Callback callback) {
        initApiDomain().serviceOrderStatus(initAccessTokenHeader(), params).enqueue(callback);
    }

    synchronized public static void paymentJurnal(PaymentJurnalRequestModel requestModel, Callback callback) {
        initApiDomain().paymentJurnal( initHeader(), initAccessTokenHeader(), requestModel).enqueue(callback);
    }

    synchronized public static void Banner(Callback callback) {
        initApiDomain().asGetBanner(initHeader(), initAccessTokenHeader()).enqueue(callback);
    }

    synchronized public static void info(Callback callback) {
        initApiDomain().asGetInformation().enqueue(callback);
    }
}
