package com.otto.mart.presenter.dao.olshop;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.OasisAPI;
import com.otto.mart.api.OttofinAPI;
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
import com.otto.mart.model.APIModel.Request.grosir.OrderReceivedRequest;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class GrosirDao extends BaseDao {
    public GrosirDao(Object obj) {
        super(obj);
    }

    public void checkOasisStatus(Callback callback) {
        OttofinAPI.checkOasisStatus(OttoMartApp.getContext(), callback);
    }

    public void checkEligibleOasis(CheckEligibleRequest checkEligibleRequest, Callback callback) {
        OasisAPI.checkEligibleOasis(OttoMartApp.getContext(), checkEligibleRequest, callback);
    }

    public void getSupplierList(String area_id,String merchant_phone, Callback callback) {
        OasisAPI.opGrosirSupplierList(OttoMartApp.getContext(), area_id,merchant_phone, callback);
    }

    public void getCheckSupplier(GrosirCheckSupplierRequest requestSupplier, Callback callback) {
        OasisAPI.opGrosirCheckSupplier(OttoMartApp.getContext(), requestSupplier, callback);
    }

    public void getRegisterSupplier(GrosirRegisterSupplierRequest requestSupplier, Callback callback) {
        OasisAPI.opGrosirRegisterSupplier(OttoMartApp.getContext(), requestSupplier, callback);
    }

    public void getSupplierListProduct(GrosirRequestListProduct model, Callback callback) {
        OasisAPI.opGrosirSupplierListProduct(OttoMartApp.getContext(), model, callback);
    }

    public void getSupplierListProductScan(GrosirRequestListProduct model, Callback callback) {
        OasisAPI.opGrosirSupplierListProductScan(OttoMartApp.getContext(), model, callback);
    }

    public void getSupplierListCategory(OasisListCategoryRequest model, Callback callback) {
        OasisAPI.opGrosirSupplierListCategory(OttoMartApp.getContext(), model, callback);
    }

    public void postGrosirItem(GrosirPostingRequest model, Callback callback) {
        OasisAPI.opGrosirPosting(OttoMartApp.getContext(), model, callback);
    }

    public void postGrosirItemV2(GrosirPostingRequestV2 model, Callback callback) {
        OasisAPI.opGrosirPostingv2(OttoMartApp.getContext(), model, callback);
    }

    public void getShipmentDetail(GrosirCostShipmentRequest model, Callback callback) {
        OasisAPI.opGrosirShipmentDetail(OttoMartApp.getContext(), model, callback);
    }

    public void getGrosirCheckStatus(String order_no, Callback callback) {
        OasisAPI.opGrosirCheckStatus(OttoMartApp.getContext(),order_no, callback);
    }

    public void getGrosirCheckStatusApproved(OasisApprovedOrderRequest model, Callback callback) {
        OasisAPI.opGrosirCheckStatusApproved(OttoMartApp.getContext(), model, callback);
    }

    public void getProvince(Callback callback) {
        OasisAPI.opGrosirGetProvince(OttoMartApp.getContext(), callback);
    }

    public void getCities(String model, Callback callback) {
        OasisAPI.opGrosirGetCities(OttoMartApp.getContext(), model, callback);
    }

    public void getDistricts(String model, Callback callback) {
        OasisAPI.opGrosirGetDistricts(OttoMartApp.getContext(), model, callback);
    }

    public void createShippingAddress(AddressCreateUpdateDeleteRequest model, Callback callback) {
        OasisAPI.CreateShippingAddressAPI(OttoMartApp.getContext(), model, callback);
    }

    public void getShippingAddressList(Callback callback) {
        OasisAPI.ShippingAddressListAPI(OttoMartApp.getContext(), callback);
    }

    public void updateShippingAddress(AddressCreateUpdateDeleteRequest model, Callback callback) {
        OasisAPI.UpdateAddressAPI(OttoMartApp.getContext(), model, callback);
    }

    public void deleteShippingAddress(AddressCreateUpdateDeleteRequest model, Callback callback) {
        OasisAPI.DeleteAddressAPI(OttoMartApp.getContext(), model, callback);
    }

    public void getVillages(String model, Callback callback) {
        OasisAPI.opGrosirGetVillages(OttoMartApp.getContext(), model, callback);
    }




}
