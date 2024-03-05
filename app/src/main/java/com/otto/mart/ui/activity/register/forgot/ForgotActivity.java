package com.otto.mart.ui.activity.register.forgot;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.ResetOtpPinRequestModel;
import com.otto.mart.model.APIModel.Request.ResetPinRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.ResetOtpPinResponseModel;
import com.otto.mart.model.APIModel.Response.ResetPinResponseModel;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.dialog.OtpDialog;

import java.util.Timer;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class ForgotActivity extends AppActivity {

    private Window thiswindow;
    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private View action;
    private TextView chance, tvNotes;
    private LazyEdittext securityQ;
    private OtpDialog otpDialog;
    int retries = 3;
    int questionId;
    String phone, secQuestion, pin;
    private LazyDialog hundDialog, hundDialog2;
    private int mCustomerId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thiswindow = this.getWindow();
        setContentView(R.layout.activity_forgot_securityq);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thiswindow.setStatusBarColor(ContextCompat.getColor(this, R.color.prussian_blue));
        }
        if (getIntent().hasExtra("qid") && getIntent().hasExtra("phone") && getIntent().hasExtra("question")) {
            questionId = getIntent().getIntExtra("qid", -1);
            phone = getIntent().getStringExtra("phone");
            secQuestion = getIntent().getStringExtra("question");
        } else {
            finish();
        }
        initComponent();
        initContent();
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        action = findViewById(R.id.action);
        chance = findViewById(R.id.chance);
        securityQ = findViewById(R.id.securityQ);
        otpDialog = new OtpDialog(this, this, true);
        tvNotes = findViewById(R.id.tvNotes);
        initDialog();
        initDialog2();
    }

    private void initContent() {
        tvToolbarTitle.setText(getString(R.string.label_forgot_pin_code));

        chance.setText(retries + "X");
        securityQ.setTitle(secQuestion);

        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        action.setOnClickListener(v -> {
            ResetOtpPinRequestModel requestModel = new ResetOtpPinRequestModel();
            requestModel.setAnswer(securityQ.getTextContent());
            requestModel.setLatitude(getMyLastLocation().getLatitude());
            requestModel.setLongitude(getMyLastLocation().getLongitude());
            requestModel.setSecurity_question_id(questionId);
            requestModel.setPhone(phone);
            callForgetPinOtpApi(requestModel);
        });

        otpDialog.setOtpListener(new OtpDialog.OtpDialogListener() {
            @Override
            public void onOtpEntered(OtpDialog dialog) {

            }

            @Override
            public void onActionPressed(OtpDialog dialog) {
                ResetPinRequestModel requestModel = new ResetPinRequestModel();
                requestModel.setCustomerId(mCustomerId);
                requestModel.setCustomer_id(mCustomerId);
                requestModel.setNew_pin(pin);
                requestModel.setOtp_code(dialog.getOtpCode());
                requestModel.setPhone(phone);
                callForgotPinApi(requestModel);
            }

            @Override
            public void onBackPressed(OtpDialog dialog) {
                finish();
            }

            @Override
            public void onResendPressed(OtpDialog dialog) {

            }
        });
    }

    private void schweinanjin() {
        Intent jenk = new Intent(ForgotActivity.this, RegisterPinResetActivity.class);
        jenk.putExtra("register", true);
        startActivityForResult(jenk, 111);
    }

    private void callForgetPinOtpApi(ResetOtpPinRequestModel requestModel) {
        AuthDao dao = new AuthDao(this);
        ProgressDialogComponent.showProgressDialog(ForgotActivity.this, "Loading", false).show();
        dao.forgotPinOTP(requestModel, BaseDao.getInstance(this, 444).callback);
    }

    private void callForgotPinApi(ResetPinRequestModel requestModel) {
        AuthDao dao = new AuthDao(this);
        ProgressDialogComponent.showProgressDialog(ForgotActivity.this, "Loading", false).show();
        dao.forgotPinResetDAO(requestModel, BaseDao.getInstance(this, 445).callback);
    }

    private void initDialog() {
        hundDialog = new LazyDialog(this, this, true);
        hundDialog.setContainerView(LayoutInflater.from(this).inflate(R.layout.dialog_forgot_succ, null));
        hundDialog.setNavigationView(LayoutInflater.from(this).inflate(R.layout.cw_navigation_1btn_cust, null)
                , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SessionManager.logout();
                        Intent jenk = new Intent(ForgotActivity.this, LoginActivity.class);
                        jenk.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        jenk.addCategory(Intent.CATEGORY_HOME);
                        startActivity(jenk);
                    }
                });
    }

    private void initDialog2() {
        hundDialog2 = new LazyDialog(this, this, true);
        hundDialog2.setContainerView(LayoutInflater.from(this).inflate(R.layout.dialog_forget_fail, null));
        hundDialog2.setNavigationView(LayoutInflater.from(this).inflate(R.layout.cw_navigation_1btn_cust, null)
                , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent jenk = new Intent(ForgotActivity.this, LoginActivity.class);
                        startActivity(jenk);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 111:
                if (resultCode == this.RESULT_OK) {
                    pin = data.getStringExtra("pincode");
                    otpDialog.setPhoneNumber(DataUtil.getXXXPhone(phone));
                    otpDialog.show();
                }
                break;
        }
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(ForgotActivity.this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case 444:
                    Timer jenk = new Timer();
                    if (((ResetOtpPinResponseModel) br).getMeta().getCode() == 200) {
                        jenk.cancel();
                        mCustomerId = ((ResetOtpPinResponseModel) br).getData().getCustomerId();
                        schweinanjin();
                    } else if (((ResetOtpPinResponseModel) br).getMeta().getCode() == 401) {
                        retries--;
                        chance.setText(retries + "X");
                        if (retries == 0) {
                            hundDialog2.show();
                        } else {
                            tvNotes.setText(getString(R.string.text_wrong_answer));
                            Handler jonk = new Handler();
                            jonk.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tvNotes.setText("");
                                }
                            }, 4000);
                        }
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), " ");
                        dialog.show();
                    }
                    break;
                case 445:
                    if (((ResetPinResponseModel) br).getMeta().getCode() == 200 && ((ResetPinResponseModel) br).getMeta().getStatus()) {
                        hundDialog.show();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), " ");
                        dialog.show();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }
}