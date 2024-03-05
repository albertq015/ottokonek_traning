package com.otto.mart.presenter.interactor;

import android.content.Context;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import app.beelabs.com.codebase.base.BaseDao;

public class OrderStatusInteractor {

    private final BaseDao dao;

    public OrderStatusInteractor(Context context, Object base) {
        this.dao = new BaseDao(base);
    }

    public void callOrderStatusInProgressApi(Object requstModel, InteractorInterface callback){
        callback.onInProgressRequestSuccess(null);
    }

    public void callOrderStatusDoneApi(Object requestModel, InteractorInterface callback){
        callback.onDoneRequestSuccess(null);
    }

    public interface InteractorInterface{
        void onInProgressRequestSuccess(BaseResponseModel model);
        void onDoneRequestSuccess(BaseResponseModel model);
        void onApiFailed(BaseResponseModel model);
        void onConnectionFailed(Throwable e);
    }
}
