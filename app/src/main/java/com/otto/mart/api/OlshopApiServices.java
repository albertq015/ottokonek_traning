package com.otto.mart.api;

import com.otto.mart.model.APIModel.Request.olshop.AddCartRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ConfirmPaid;
import com.otto.mart.model.APIModel.Request.olshop.OrderConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.PaymentJurnalRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ProductListRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ShippingCostRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.address.AddressListResponseModel;
import com.otto.mart.model.APIModel.Request.olshop.address.ShippingAddressRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.cart.UpdateCartRequestModel;
import com.otto.mart.model.APIModel.Response.BannerRespomnse;
import com.otto.mart.model.APIModel.Response.olshop.AddressResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.CategoryResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.MailNoResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.OlshopInformationResponse;
import com.otto.mart.model.APIModel.Response.olshop.ProductDetailResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.ProductListResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.address.CreateAddressResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.background.EventResponse;
import com.otto.mart.model.APIModel.Response.olshop.cart.CartResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.confirm.FinalConfirmOrderResponse;
import com.otto.mart.model.APIModel.Response.olshop.order.ConfirmOrderResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.order.ShippingCostResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.order.status.OrderStatusResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.payment.PaymentJournalOlshopResponseModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface OlshopApiServices {

    @GET("category-product/2")
    Call<CategoryResponseModel> serviceGetCategories(@HeaderMap Map<String, String> token);

    @POST("product/{path}")
    Call<ProductListResponseModel> serviceGetProducts(@HeaderMap Map<String, String> token, @Path("path") String path, @Body ProductListRequestModel model);

    @GET("background-event")
    Call<EventResponse> serviceGetSpecialEventProduct(@HeaderMap Map<String, String> token);

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

//    @POST("check_mail_no")
//    Call<MailNoResponseModel> serviceGetMailNo(@HeaderMap Map<String, String> token, @Body AddressRequestModel model);

    @GET("elevenia/districts/{districtId}/shipping_codes")
    Call<MailNoResponseModel> serviceGetMailNo(@HeaderMap Map<String, String> token, @Path("districtId") long districtId);

    @POST("shipping_addresses")
    Call<CreateAddressResponseModel> serviceCreateShippingAddress(@HeaderMap Map<String, String> token, @Body ShippingAddressRequestModel model);

    @GET("shipping_addresses")
    Call<AddressListResponseModel> serviceGetShippingAddress(@HeaderMap Map<String, String> token);

    @PATCH("shipping_addresses/{addressId}")
    Call<CreateAddressResponseModel> serviceUpdateShippingAddress(@HeaderMap Map<String, String> token, @Body ShippingAddressRequestModel model, @Path("addressId") String addressId);

    @DELETE("shipping_addresses/{addressId}")
    Call<CreateAddressResponseModel> serviceDeleteShippingAddress(@HeaderMap Map<String, String> token, @Path("addressId") String addressId);

    @GET("order/statuses")
    Call<OrderStatusResponseModel> serviceOrderStatus(@HeaderMap Map<String, String> token, @QueryMap Map<String, String> params);

    @POST("payment_journals")
    Call<PaymentJournalOlshopResponseModel> paymentJurnal(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken, @Body PaymentJurnalRequestModel paymentJurnalRequestModel);

    @GET("banner-special-event")
    Call<BannerRespomnse> asGetBanner(@HeaderMap Map<String, String> token, @HeaderMap Map<String, String> accessToken);

    @GET("information")
    Call<OlshopInformationResponse> asGetInformation();
}
