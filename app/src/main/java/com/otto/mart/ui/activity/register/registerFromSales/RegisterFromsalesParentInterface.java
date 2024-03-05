package com.otto.mart.ui.activity.register.registerFromSales;

public interface RegisterFromsalesParentInterface {
    void showKycDialog();

    void closeKycDialog();

    void moveToKyc();

    void showOtpDialog();

    void hideOtpDialog();

    void showErrorDialog(String title, String message);

}
