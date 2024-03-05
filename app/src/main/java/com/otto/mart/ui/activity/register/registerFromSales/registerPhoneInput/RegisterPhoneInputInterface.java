package com.otto.mart.ui.activity.register.registerFromSales.registerPhoneInput;

import com.otto.mart.model.APIModel.Response.SearchMerchantResponse;

public interface RegisterPhoneInputInterface {

    void backPressed();

    void showOtpDialog();

    void hideOtpDialog();

    void otpSuccess();

    void showErrorMessage(String title, String message);

    void showApiResponseError();

    void closeOtpDialog();

    void moveToRegisterParent();

    void moveToQrScan();

    String getPhoneNumber();

    void showMerchantData(SearchMerchantResponse.Data data);

    void hideProgressDialog();
}
