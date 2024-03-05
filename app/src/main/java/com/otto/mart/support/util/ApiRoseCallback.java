package com.otto.mart.support.util;

import com.otto.mart.model.APIModel.Response.BaseModel.RoseBaseResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiRoseCallback<T> implements Callback<T> {

    public boolean isSuccess200 = false;
    public String responseMessage = "";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.body() instanceof RoseBaseResponseModel) {
            RoseBaseResponseModel body = (RoseBaseResponseModel) response.body();
            if (body.getRc() != null) {
                isSuccess200 = body.getRc().equals("00");
                responseMessage = body.getMsg();
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
