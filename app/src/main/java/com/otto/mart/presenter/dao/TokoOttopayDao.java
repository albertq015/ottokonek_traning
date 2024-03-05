package com.otto.mart.presenter.dao;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.model.APIModel.Request.tokoOttopay.AddToCardRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderConfirmRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderHistoryDetailRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderPayOffRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderPaymentRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.ProductListRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.RemoveCardRequest;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class TokoOttopayDao extends BaseDao {

    public TokoOttopayDao(Object obj) {
        super(obj);
    }

    public void getStoreList(Callback callback) {
        API.StoreList(OttoMartApp.getContext(), callback);
    }

    public void getReOrderProductList(ProductListRequest productListRequest, Callback callback) {
        API.ReOrderProductList(OttoMartApp.getContext(), productListRequest, callback);
    }

    public void getAddToCart(String storeName, AddToCardRequest addToCardRequest, Callback callback) {
        API.AddToCart(OttoMartApp.getContext(), addToCardRequest, callback);
    }

    public void getRemoveFromCart(String storeName, RemoveCardRequest removeCardRequest, Callback callback) {
        API.RemoveFromCart(OttoMartApp.getContext(), storeName, removeCardRequest, callback);
    }

    public void getCart(Callback callback) {
        API.Cart(OttoMartApp.getContext(), callback);
    }

    public void getOrderConfirm(OrderConfirmRequest orderConfirmRequest, Callback callback) {
        API.OrderConfirm(OttoMartApp.getContext(), orderConfirmRequest, callback);
    }

    public void getOrderPayment(OrderPaymentRequest orderPaymentRequest, Callback callback) {
        API.OrderPayment(OttoMartApp.getContext(), orderPaymentRequest, callback);
    }

    public void getOrderPayOff(OrderPayOffRequest orderPayOffRequest, Callback callback) {
        API.OrderPayOff(OttoMartApp.getContext(), orderPayOffRequest, callback);
    }

    public void getOrderHistory(Callback callback) {
        API.OrderHistory(OttoMartApp.getContext(), callback);
    }

    public void getOrderHistoryDetail(OrderHistoryDetailRequest orderHistoryDetailRequest, Callback callback) {
        API.OrderHistoryDetail(OttoMartApp.getContext(), orderHistoryDetailRequest, callback);
    }
}