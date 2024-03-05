package com.otto.mart.ui.activity.olshop;

public interface OrderStatusViewInterface {

    void closeActivity();
    void addDataForInProcess(Object model);
    void addDataForFinished(Object model);
    void onItemInProcessPressed(int id);
    void onItemFinishedPressed(int id);
    void onInProcessTabPressed();
    void onInItemDonePressed();

    void showLoadingDialog();
    void hideLoadingDialog();
}
