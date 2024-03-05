package com.otto.mart.presenter.dao;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Request.AddAddressRequestModel;
import com.otto.mart.model.APIModel.Request.AddressEditRequestModel;
import com.otto.mart.model.APIModel.Request.BankEditRequestModel;
import com.otto.mart.model.APIModel.Request.DeleteAddressRequestModel;
import com.otto.mart.model.APIModel.Request.UpdatePinRequestModel;
import com.otto.mart.model.APIModel.Request.UpdateProfileRequestModel;
import com.otto.mart.presenter.sessionManager.SessionManager;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class ProfileDao extends BaseDao {
    public ProfileDao(Object ac) {
        super(ac);
    }

    public void getProfile(Callback callback) {
        OttofinAPI.GetProfileAPI(OttoMartApp.getContext(), callback);
    }

    public void updateAddress(BaseActivity baseActivity, AddressEditRequestModel model, Callback callback) {
        API.UpdateAddress(baseActivity, "Bearer " + SessionManager.getAccessToken(), model, callback);
    }

    public void updateProfile(BaseActivity baseActivity, UpdateProfileRequestModel model, Callback callback) {
        API.UpdateProfile(baseActivity, model, callback);
    }

    public void getBankList(Callback callback) {
        API.GetBankList(OttoMartApp.getContext(), callback);
    }

    public void updateBank(BankEditRequestModel model, Callback callback) {
        API.UpdateBank(OttoMartApp.getContext(), model, callback);
    }

    public void addBank(BankEditRequestModel model, Callback callback) {
        API.AddBank(OttoMartApp.getContext(), model, callback);
    }


    public void getAddressList(Callback callback) {
        API.GetAddressList(OttoMartApp.getContext(), callback);
    }

    public void createAddress(AddAddressRequestModel model, Callback callback) {
        API.CreateAddress(OttoMartApp.getContext(), model, callback);
    }

    public void deleteAddress(DeleteAddressRequestModel model, Callback callback) {
        API.DeleteAddress(OttoMartApp.getContext(), model, callback);
    }

    public void updatePin(UpdatePinRequestModel model, Callback callback) {
        OttoKonekAPI.changePin(OttoMartApp.getContext(), model, callback);
    }
}
