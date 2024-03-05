package com.otto.mart.presenter.classPresenter;

import com.otto.mart.model.APIModel.Misc.AuthDataModel;
import com.otto.mart.model.APIModel.Request.CheckMerchantIdRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.SearchMerchantResponse;
import com.otto.mart.presenter.interactor.RegisterPhoneInputInteractor;
import com.otto.mart.ui.activity.register.registerFromSales.registerPhoneInput.RegisterPhoneInputInterface;

import app.beelabs.com.codebase.base.BaseActivity;
import retrofit2.Response;

public class RegisterPhoneInputPresenter implements RegisterPhoneInputInteractor.onVerifyOtpInterface, com.otto.mart.presenter.classPresenter.register.RegisterPhoneInputPresenter.MerchantCallback {

    RegisterPhoneInputInterface view;
    RegisterPhoneInputInteractor interactor;

    public RegisterPhoneInputPresenter(BaseActivity activity) {
        interactor = new RegisterPhoneInputInteractor(activity);
        view = (RegisterPhoneInputInterface) activity;
    }

    public void verifyPhone(String merchantId, String phone, String latitude, String longitude) {
        if (merchantId.contains(";;;;")) {
            merchantId = merchantId.split(";;;;")[1];
        }
        CheckMerchantIdRequestModel requestModel = new CheckMerchantIdRequestModel();
        requestModel.setMerchant_id(merchantId);
        requestModel.setPhone_number(phone);
        requestModel.setLatitude(latitude);
        requestModel.setLongitude(longitude);
        interactor.callVerifyPhoneApi(requestModel, this);
    }

    public void verifyOtp(String otp) {
        interactor.callVerifyOTPApi(this);
    }

    @Override
    public void onOtpRequestSuccess(AuthDataModel model) {
        view.moveToRegisterParent();
    }

    @Override
    public void onOtpRequestFailed(String error) {
        view.showErrorMessage("Error", error);
    }

    @Override
    public void onMerchantValid(Response response) {
        view.hideProgressDialog();
        if (response.isSuccessful()) {
            if (((BaseResponseModel) response.body()).getMeta().getCode() == 200) {
                SearchMerchantResponse model = null;
                try {
                    model = (SearchMerchantResponse) response.body();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (model != null && model.getData() != null) {
                    view.showMerchantData(model.getData());
                }
            } else {
                view.showErrorMessage(((BaseResponseModel) response.body()).getMeta().getMessage(), "");
            }
        } else {
            view.showApiResponseError();
        }
    }

    @Override
    public void onMerchantInvalid(Throwable t) {
        view.hideProgressDialog();
        view.showApiResponseError();
    }
}