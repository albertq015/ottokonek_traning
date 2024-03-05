package com.otto.mart.api;

import com.otto.mart.model.APIModel.Request.grosir.AddressCreateUpdateDeleteRequest;
import com.otto.mart.model.APIModel.Request.grosir.CheckEligibleRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirCheckSupplierRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirCostShipmentRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirGetCitiesRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirGetDistrictsRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirGetVillagesRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirPostingRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirPostingRequestV2;
import com.otto.mart.model.APIModel.Request.grosir.GrosirRegisterSupplierRequest;
import com.otto.mart.model.APIModel.Request.grosir.GrosirRequestListProduct;
import com.otto.mart.model.APIModel.Request.grosir.GrosirRequestSupplier;
import com.otto.mart.model.APIModel.Request.grosir.OasisApprovedOrderRequest;
import com.otto.mart.model.APIModel.Request.grosir.OasisListCategoryRequest;
import com.otto.mart.model.APIModel.Request.grosir.OrderReceivedRequest;
import com.otto.mart.model.APIModel.Response.bogasari.BogasariPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.donasi.ProductDonasiResponse;
import com.otto.mart.model.APIModel.Response.grosir.CheckEligibleResponse;
import com.otto.mart.model.APIModel.Response.grosir.GrosirAddressListResponse;
import com.otto.mart.model.APIModel.Response.grosir.GrosirCheckSupplierRespond;
import com.otto.mart.model.APIModel.Response.grosir.GrosirCostShipmentResponse;
import com.otto.mart.model.APIModel.Response.grosir.GrosirListSupplierResponse;
import com.otto.mart.model.APIModel.Response.grosir.GrosirRegisterSupplierResponse;
import com.otto.mart.model.APIModel.Response.grosir.GrosirResponseListProduct;
import com.otto.mart.model.APIModel.Response.grosir.OasisApprovedOrderResponse;
import com.otto.mart.model.APIModel.Response.grosir.OasisListCategoryResponse;
import com.otto.mart.model.APIModel.Response.olshop.AddressResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.address.CreateAddressResponseModel;

import java.util.Map;

import com.otto.mart.model.APIModel.Request.grosir.HistoryOasisOrderRequestModel;
import com.otto.mart.model.APIModel.Response.grosir.HistoryOasisOrderResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.order.status.OrderStatusResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface OasisServices {

    /**
     * Oasis
     */

    @POST("v0.1.0/check-eligible")
    Call<CheckEligibleResponse> checkEligibleOasis(@HeaderMap Map<String, String> key,
                                                   @Body CheckEligibleRequest checkEligibleRequest);


    @POST("v0.1.0/supplier/check/merchant")
    Call<GrosirCheckSupplierRespond> opGrosirCheckSupplier(@HeaderMap Map<String, String> key, @Body GrosirCheckSupplierRequest model);

    @POST("v0.1.0/merchant/register")
    Call<GrosirRegisterSupplierResponse> opGrosirRegisterSupplier(@HeaderMap Map<String, String> key, @Body GrosirRegisterSupplierRequest model);

    @POST("v0.1.0/product/search")
    Call<GrosirResponseListProduct> opGrosirSupplierListProduct(@HeaderMap Map<String, String> key, @Body GrosirRequestListProduct model);

    @POST("v0.1.0/product/search-by-barcode")
    Call<GrosirResponseListProduct> opGrosirSupplierListProductScan(@HeaderMap Map<String, String> key, @Body GrosirRequestListProduct model);

    @POST("v0.1.0/category/list")
    Call<OasisListCategoryResponse> opGrosirSupplierListCategory(@HeaderMap Map<String, String> key, @Body OasisListCategoryRequest model);

    @POST("v0.2.0/merchant/order")
    Call<BogasariPaymentResponseModel> opGrosirPosting(@HeaderMap Map<String, String> key, @Body GrosirPostingRequest model);

    @POST("v2/oasis/order/checkout")
    Call<BogasariPaymentResponseModel> opGrosirPostingv2(@HeaderMap Map<String, String> key, @Body GrosirPostingRequestV2 model);

    @POST("get-oasis-shipping-costs")
    Call<GrosirCostShipmentResponse> opGrosirShipmentDetail(@HeaderMap Map<String, String> key, @Body GrosirCostShipmentRequest model);

    @GET("v2/oasis/order/history")
    Call<HistoryOasisOrderResponseModel> opGrosirCheckStatus(@HeaderMap Map<String, String> key,@Query("order_no") String order_no);

    @POST("v2/oasis/order/received")
    Call<OasisApprovedOrderResponse> opGrosirCheckStatusApproved(@HeaderMap Map<String, String> key, @Body OasisApprovedOrderRequest model);

    @GET("v1.0/region/all")
    Call<AddressResponseModel> opGrosirGetProvince(@HeaderMap Map<String, String> key);

    @GET("v1.0/province/region/{code}")
    Call<AddressResponseModel> opGrosirGetCities(@HeaderMap Map<String, String> key, @Path("code") String code);

    @GET("v1.0/municipality/province/{code}")
    Call<AddressResponseModel> opGrosirGetDistricts(@HeaderMap Map<String, String> key, @Path("code") String code);

    @POST("v0.3.0/shipping-address/get")
    Call<AddressResponseModel> opGrosirGetAddressList(@HeaderMap Map<String, String> key);

    @POST("v0.3.0/shipping-address/create")
    Call<AddressResponseModel> opGrosirCreateAddress(@HeaderMap Map<String, String> key);

    @POST("v0.3.0/shipping-address/update")
    Call<AddressResponseModel> opGrosirUpdateAddress(@HeaderMap Map<String, String> key);

    @POST("v0.3.0/shipping-address/delete")
    Call<AddressResponseModel> opGrosirDeleteAddress(@HeaderMap Map<String, String> key);

    @GET("v1.0/barangays/municipality/{code}")
    Call<AddressResponseModel> opGrosirGetVillages(@HeaderMap Map<String, String> key, @Path("code") String code);

    @POST("v0.1.0/shipping-address-philippines/create")
    Call<CreateAddressResponseModel> serviceCreateShippingAddress(@HeaderMap Map<String, String> token, @Body AddressCreateUpdateDeleteRequest model);

    @POST("v0.1.0/shipping-address-philippines/get")
    Call<GrosirAddressListResponse> serviceGetShippingAddress(@HeaderMap Map<String, String> token);

    @POST("v0.1.0/shipping-address-philippines/update")
    Call<CreateAddressResponseModel> serviceUpdateShippingAddress(@HeaderMap Map<String, String> token, @Body AddressCreateUpdateDeleteRequest model);

    @POST("v0.1.0/shipping-address-philippines/delete")
    Call<CreateAddressResponseModel> serviceDeleteShippingAddress(@HeaderMap Map<String, String> token, @Body AddressCreateUpdateDeleteRequest model);


    //  @GET("ottokonek/oasis/v2/supplier/list/")
    @GET("v2/oasis/supplier/list/")
    Call<GrosirListSupplierResponse> opGrosirSupplierList(@HeaderMap Map<String, String> key, @Query("area_id") String area_id, @Query("merchant_phone") String merchant_phone);



}
