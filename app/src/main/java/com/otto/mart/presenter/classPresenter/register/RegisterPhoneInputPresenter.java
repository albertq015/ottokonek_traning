package com.otto.mart.presenter.classPresenter.register;

import retrofit2.Response;

public interface RegisterPhoneInputPresenter {
    interface MerchantCallback{
        void onMerchantValid(Response response);

        void onMerchantInvalid(Throwable t);
    }
}
