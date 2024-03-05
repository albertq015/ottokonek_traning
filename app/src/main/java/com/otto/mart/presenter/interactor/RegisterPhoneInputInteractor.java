package com.otto.mart.presenter.interactor;

import com.otto.mart.model.APIModel.Misc.AuthDataModel;
import com.otto.mart.model.APIModel.Request.CheckMerchantIdRequestModel;
import com.otto.mart.presenter.classPresenter.register.RegisterPhoneInputPresenter;
import com.otto.mart.presenter.dao.AuthDao;

import app.beelabs.com.codebase.base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPhoneInputInteractor {

    private final AuthDao authDao;

    public RegisterPhoneInputInteractor(BaseActivity baseActivity) {
        authDao = new AuthDao(baseActivity);
        initContent();
    }

    public void initContent() {

    }

    public void callVerifyPhoneApi(CheckMerchantIdRequestModel requestModel, RegisterPhoneInputPresenter.MerchantCallback callback) {
        authDao.searchMerchant(requestModel, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                callback.onMerchantValid(response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                callback.onMerchantInvalid(t);
            }
        });
    }


    public void callVerifyOTPApi(onVerifyOtpInterface listener) {
        listener.onOtpRequestSuccess(null);
        //listener.onOtpRequestFailed("");
    }

    public interface onVerifyOtpInterface {
        void onOtpRequestSuccess(AuthDataModel model);

        void onOtpRequestFailed(String error);
    }
}
