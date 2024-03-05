package com.otto.mart.ui.activity.register.registerFromSales.registerPhoneInput;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.model.APIModel.Request.CheckMerchantIdRequestModel;
import com.otto.mart.model.APIModel.Response.register.SearchMerchantResponse;
import com.otto.mart.presenter.classPresenter.RegisterPhoneInputPresenter;
import com.otto.mart.support.util.ApiCallback;
import com.otto.mart.support.util.formValidation.FormValidation;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.inbox.MessageActivity;
import com.otto.mart.ui.activity.qr.scan.QRScanSignUpActivity;
import com.otto.mart.ui.activity.register.registerFromSales.registerConfirmation.RegisterFromsalesConfirmActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.dialog.OtpDialog;

import app.beelabs.com.codebase.component.ProgressDialogComponent;

public class RegisterPhoneInputActivity extends AppActivity implements RegisterPhoneInputInterface {

    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private EditText etPhone;
    private TextView tvPhoneError;
    private OtpDialog otpDialog;
    private RegisterPhoneInputPresenter presenter;
    private Button btnNext;

    private String mMerchantId = "";
    private FormValidation mFormValidation;
    private boolean mIsValidationEnable = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phoneinput);
        initComponent();
        initContent();
    }

    private void initComponent() {
        presenter = new RegisterPhoneInputPresenter(this);
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        etPhone = findViewById(R.id.etPhone);
        tvPhoneError = findViewById(R.id.tvPhoneError);
        otpDialog = new OtpDialog(this, this, false);
        btnNext = findViewById(R.id.btn_next);
    }

    private void initContent() {
        tvToolbarTitle.setVisibility(View.GONE);

        btnToolbarBack.setOnClickListener(v -> {
            onBackPressed();
        });

        mFormValidation = new FormValidation(this);
        addTextWatcher(etPhone);

        otpDialog.setOtpListener(new OtpDialog.OtpDialogListener() {
            @Override
            public void onOtpEntered(OtpDialog dialog) {

            }

            @Override
            public void onActionPressed(OtpDialog dialog) {
                presenter.verifyOtp(dialog.getOtpCode());
            }

            @Override
            public void onBackPressed(OtpDialog dialog) {
                closeOtpDialog();
            }

            @Override
            public void onResendPressed(OtpDialog dialog) {
                //resend
                otpDialog.setNotLoadingState();
            }
        });

        btnNext.setOnClickListener(v -> {

            if (!getCurrentLocation()) {
                return;
            }
            mIsValidationEnable = true;
            if (validateInputForm()) {
                callSearchMerchantAPI();
            }
        });

        //moveToQrScan();
    }

    private void callSearchMerchantAPI() {
        ProgressDialogComponent.showProgressDialog(RegisterPhoneInputActivity.this, getString(R.string.loading_message), false).show();
        CheckMerchantIdRequestModel model = new CheckMerchantIdRequestModel();
        model.setPhone_number(etPhone.getText().toString());

        OttoKonekAPI.searchMerchant(this, model, new ApiCallback<SearchMerchantResponse>() {
            @Override
            public void onResponseSuccess(SearchMerchantResponse body) {
                dismissProgressDialog();

                if (isSuccess200) {
                    Intent intent = new Intent(RegisterPhoneInputActivity.this, RegisterFromsalesConfirmActivity.class);
                    intent.putExtra("merchantData", new Gson().toJson(body.getData()));
                    startActivity(intent);
                } else {
                    showErrorMessage(responseMessage, "");
                }
            }

            @Override
            public void onApiServiceFailed(Throwable throwable) {
                dismissProgressDialog();
                onApiResponseError();
            }
        });
    }

    private boolean validateInputForm() {
        if (mIsValidationEnable) {
            boolean status = true;

            tvPhoneError.setVisibility(View.GONE);

            if (!mFormValidation.isRequired(etPhone.getText().toString())) {
                tvPhoneError.setText(getString(R.string.ppob_msg_phone_is_required));
                tvPhoneError.setVisibility(View.VISIBLE);
                status = false;
            } else if (!mFormValidation.isValidMobilePhone(etPhone.getText().toString())) {
                tvPhoneError.setText(getString(R.string.ppob_msg_phone_is_not_valid));
                tvPhoneError.setVisibility(View.VISIBLE);
                status = false;
            }

            if (status) {
                btnNext.setBackground(ContextCompat.getDrawable(this, R.drawable.button_primary_selector));
            } else {
                btnNext.setBackground(ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg));
            }

            return status;
        } else {
            return false;
        }
    }

    public void addTextWatcher(EditText input) {
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateInputForm();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

    @Override
    public void backPressed() {
        finish();
    }

    @Override
    public void showOtpDialog() {
        otpDialog.show();
    }

    @Override
    public void hideOtpDialog() {
        //ondismiss do what
    }

    @Override
    public void otpSuccess() {
        otpDialog.dismiss();
        moveToRegisterParent();
    }

    @Override
    public void showErrorMessage(String title, String message) {
        ErrorDialog dialog = new ErrorDialog(this,
                this, false, false,
                title, title);
        dialog.show();
    }

    @Override
    public void showApiResponseError() {
        dialogServerError(null);
    }

    @Override
    public void closeOtpDialog() {
        otpDialog.dismiss();
    }


    @Override
    public void moveToRegisterParent() {
        Intent jenk = new Intent(this, RegisterFromsalesConfirmActivity.class);
        jenk.putExtra("isSFARegister", true);
        startActivity(jenk);
    }

    @Override
    public void moveToQrScan() {
        Intent jenk = new Intent(this, QRScanSignUpActivity.class);
        jenk.putExtra("isSFARegister", true);
        startActivityForResult(jenk, 111);
    }

    @Override
    public String getPhoneNumber() {
        return etPhone.getText().toString();
    }

    @Override
    public void showMerchantData(com.otto.mart.model.APIModel.Response.SearchMerchantResponse.Data data) {
        Intent jenk = new Intent(this, RegisterFromsalesConfirmActivity.class);
        jenk.putExtra("merchantData", new Gson().toJson(data.getMerchant()));
        startActivity(jenk);
    }

    @Override
    public void hideProgressDialog() {
        ProgressDialogComponent.dismissProgressDialog(RegisterPhoneInputActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.RESULT_OK) {
            switch (requestCode) {
                case 111: {
                    mMerchantId = data.getStringExtra("merchantId");
                    btnNext.setVisibility(View.VISIBLE);
                    break;
                }
            }
        } else {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
