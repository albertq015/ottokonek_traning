package com.otto.mart.presenter.classPresenter;

import android.content.Context;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.presenter.interactor.OrderStatusInteractor;
import com.otto.mart.ui.activity.olshop.OrderStatusViewInterface;

public class OrderStatusPresenter implements OrderStatusInteractor.InteractorInterface {

    private Context mContext;
    private OrderStatusViewInterface view;
    private OrderStatusInteractor interactor;

    public OrderStatusPresenter(Context mContext, Object base, OrderStatusViewInterface view) {
        interactor = new OrderStatusInteractor(mContext, base);
        this.mContext = mContext;
        this.view = view;
    }

    @Override
    public void onInProgressRequestSuccess(BaseResponseModel model) {
        view.addDataForInProcess(model);
    }

    @Override
    public void onDoneRequestSuccess(BaseResponseModel model) {
        view.addDataForFinished(model);
    }

    @Override
    public void onApiFailed(BaseResponseModel model) {
//        view.showErrorDialog(model.getMeta().getMessage());
    }

    @Override
    public void onConnectionFailed(Throwable e) {
//        view.showErrorDialog(e.getMessage());
//        view.showErrorDialog("Terjadi kesalahan koneksi");
    }

}
