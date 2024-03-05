package com.otto.mart.support.util;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiCallback<T> implements Callback<T> {

    public boolean isSuccess200 = false;
    public String responseMessage = "";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.body() != null) {
            if (response.body() instanceof BaseResponseModel) {
                BaseResponseModel body = (BaseResponseModel) response.body();
                if (body.getMeta() != null) {
                    isSuccess200 = body.getMeta().getCode() == 200;
                    responseMessage = body.getMeta().getMessage();
                }
            }
            onResponseSuccess(response.body());
        } else {
            onApiServiceFailed(new Throwable("Body has empty"));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onApiServiceFailed(t);
    }

    public abstract void onResponseSuccess(T body);

    public abstract void onApiServiceFailed(Throwable throwable);
}
