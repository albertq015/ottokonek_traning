package com.otto.mart.ui.activity.register;

import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.model.localmodel.State.RegisterStateModel;
import com.otto.mart.model.localmodel.ui.BankUiModel;
import com.otto.mart.ui.component.dialog.ChainedListDialog;

import java.util.List;

public interface IRegister {

    SignupRequestModel getModel();

    void updateModel(SignupRequestModel model);

    void moveToNext();

    void moveToPrev();

    void putSecAnswer(String question, int questionid);

    void callSecAnswerPicker();

    boolean isSecAnswerInitialized();

    void callMap();

    void callAddressInput();

    void callBankSelector();

    void callPinBullshit();

    RegisterStateModel getStateModel();

    List<BankUiModel> getBankUiModels();

    void callRegister();

    void callUpdateStatus(String answer);

    ChainedListDialog getBcDialog();
}
