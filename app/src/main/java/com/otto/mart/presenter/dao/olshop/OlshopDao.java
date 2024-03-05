package com.otto.mart.presenter.dao.olshop;

import com.otto.mart.api.OlshopAPI;
import com.otto.mart.model.APIModel.Request.olshop.AddCartRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ConfirmPaid;
import com.otto.mart.model.APIModel.Request.olshop.OrderConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ProductListRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ShippingCostRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.address.ShippingAddressRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.cart.UpdateCartRequestModel;

import java.util.Map;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class OlshopDao extends BaseDao {

    public OlshopDao(Object obj) {
        super(obj);
    }

    public void getCategories(Callback callback) {
        OlshopAPI.getCategoriesAPI(callback);
    }

    public void getProductList(Callback callback, String path, ProductListRequestModel model) {
        OlshopAPI.getProductListAPI(path, model, callback);
    }
    public void getSpecialEventProducts(Callback callback) {
        OlshopAPI.getSpecialProductListAPI(callback);
    }

    public void getProductDetail(Callback callback, int path) {
        OlshopAPI.getProductDetailAPI(path, callback);
    }

    public void getCartList(Callback callback) {
        OlshopAPI.getCartListAPI(callback);
    }

    public void addCart(AddCartRequestModel model, Callback callback) {
        OlshopAPI.AddCartAPI(model, callback);
    }

    public void deleteCart(String cartItemId, Callback callback) {
        OlshopAPI.DeleteCartAPI(cartItemId, callback);
    }

    public void updateCart(UpdateCartRequestModel model, Callback callback) {
        OlshopAPI.UpdateCartAPI(model, callback);
    }

    public void orderConfirmation(OrderConfirmationRequestModel model, Callback callback) {
        OlshopAPI.OrderConfirmation(model, callback);
    }

    public void shippingCost(ShippingCostRequestModel model, Callback callback) {
        OlshopAPI.ShippingCostAPI(model, callback);
    }

    public void confirmOrderPaid(ConfirmPaid model, Callback callback) {
        OlshopAPI.ConfirmOrderPaidAPI(model, callback);
    }

    public void getProvince(Callback callback) {
        OlshopAPI.GetProvinceAPI(callback);
    }

    public void getCity(String provinceId, Callback callback) {
        OlshopAPI.GetCityAPI(provinceId, callback);
    }

    public void getDistrict(String cityId, Callback callback) {
        OlshopAPI.GetDistrictAPI(cityId, callback);
    }

    public void getMailNo(long districtId, Callback callback) {
        OlshopAPI.GetMailNoAPI(districtId, callback);
    }

    public void createShippingAddress(ShippingAddressRequestModel model, Callback callback) {
        OlshopAPI.CreateShippingAddressAPI(model, callback);
    }

    public void getShippingAddressList(Callback callback) {
        OlshopAPI.ShippingAddressListAPI(callback);
    }

    public void updateShippingAddress(ShippingAddressRequestModel model, String addressId, Callback callback) {
        OlshopAPI.UpdateAddressAPI(model, addressId, callback);
    }

    public void deleteShippingAddress(String addressId, Callback callback) {
        OlshopAPI.DeleteAddressAPI(addressId, callback);
    }

    public void orderStatus(Map<String, String> params, Callback callback) {
        OlshopAPI.GetOrderStatusAPI(params, callback);
    }

    public void getBanner(Callback callback) {
        OlshopAPI.Banner(callback);
    }

    public void getInfo(Callback callback) {
        OlshopAPI.info(callback);
    }
}
