package com.otto.mart.ui.activity.register.forgot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.PaymentData;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Request.UpdatePinRequestModel;
import com.otto.mart.model.APIModel.Request.multibank.IssuerLinkedRequest;
import com.otto.mart.model.APIModel.Request.multibank.TransferMultiBankConfrimRequest;
import com.otto.mart.model.APIModel.Request.qr.QrPaymentRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.DataEmptyResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel;
import com.otto.mart.presenter.dao.ProfileDao;
import com.otto.mart.presenter.dao.TransactionDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.ApiCallback;
import com.otto.mart.support.util.Connectivity;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.inbox.MessageActivity;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.login.OtpVerificationActivity;
import com.otto.mart.ui.activity.multibank.ReceiptTransferBankActivity;
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.dialog.Popup;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import kotlin.Unit;
import retrofit2.Response;

public class RegisterPinResetActivity extends AppActivity {

    public static String LAYOUT_ID = "layoutId";
    private final int KEY_CODE_INPUT_OTP = 102;
    private final int API_KEY_RESPONSE = 103;
    final String TAG = getClass().getSimpleName();
    Window thiswindow;
    View backBtn, layout_retcontainer;
    TextView tv_text, tv_retries, tv_pin_title;
    ImageView dot1, dot2, dot3, dot4, dot5, dot6;
    View k1, k2, k3, k4, k5, k6, k7, k8, k9, k0, kb, kx;
    TextView tvTncDesc;
    Button btnForgotPin;
    boolean isTransferConfrim = false;
    boolean isQrConfrim = false;
    boolean isRegister = false, isReset = false, isNewPin = false, isReinput = false, isLogin = false, isCheck = false, isConfirm = false, isCheckSession = false, isTest = false, isAddAccountLink = false;
    String pin = "";
    String rePin = "";
    int retries;
    private boolean isPayConfirm = false;
    private boolean isInput = false;
    String bankName = "";
    String accountType = "";
    String accountNumber = "";
    String bin = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInspaymenttanceState) {
        disableScreenshot();
        super.onCreate(savedInspaymenttanceState);
        thiswindow = this.getWindow();

        /*handle oc layout*/
        int layout = getIntent().getIntExtra(LAYOUT_ID, R.layout.activity_pinpad);
        setContentView(layout);
        initComponent();
        if (getIntent().getBooleanExtra("register", false)) {
            isRegister = true;
        }
        if (getIntent().getBooleanExtra("resetFunc", false)) {
            tv_text.setText(R.string.masukkan_pin_lama_anda);
            btnForgotPin.setText(R.string.lupa_pin);
            isReset = true;
        }
        if (getIntent().getBooleanExtra("login", false)) {
            isLogin = true;
        }


        if (getIntent().getBooleanExtra("check", false)) {
            isCheck = true;
        }
        if (getIntent().getBooleanExtra("confirm", false)) {
            isConfirm = true;
        }
        if (getIntent().getBooleanExtra("AddAccountLink", false)) {

            isAddAccountLink = true;
            accountNumber = getIntent().getStringExtra("accountNumber");
            bankName = getIntent().getStringExtra("bankName");
            accountType = getIntent().getStringExtra("accountType");


        }

        if (getIntent().getBooleanExtra("confrim_transfer", false)) {
            isTransferConfrim = true;
            bin = getIntent().getStringExtra("bin");
        }
        if (getIntent().getBooleanExtra("session", false)) {
            isCheckSession = true;
        }
        if (getIntent().getBooleanExtra("paymentQr", false)) {
            isQrConfrim = true;
        }
        if (getIntent().getBooleanExtra("payment", false)) {
            isPayConfirm = true;
        }
        if (getIntent().getBooleanExtra("test", false)) {
            isTest = true;
        }
        initLocationPermission();

        initContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        disableScreenshot();
    }

    protected void initComponent() {

        tv_text = findViewById(R.id.tv_jenk);
        dot1 = findViewById(R.id.pin1);
        dot2 = findViewById(R.id.pin2);
        dot3 = findViewById(R.id.pin3);
        dot4 = findViewById(R.id.pin4);
        dot5 = findViewById(R.id.pin5);
        dot6 = findViewById(R.id.pin6);

        k1 = findViewById(R.id.l_1);
        k2 = findViewById(R.id.l_2);
        k3 = findViewById(R.id.l_3);
        k4 = findViewById(R.id.l_4);
        k5 = findViewById(R.id.l_5);
        k6 = findViewById(R.id.l_6);
        k7 = findViewById(R.id.l_7);
        k8 = findViewById(R.id.l_8);
        k9 = findViewById(R.id.l_9);
        k0 = findViewById(R.id.l_0);
        kb = findViewById(R.id.l_b);
        kx = findViewById(R.id.l_x);
        backBtn = findViewById(R.id.layout_back);
        tv_retries = findViewById(R.id.tv_pinchance);
        layout_retcontainer = findViewById(R.id.layout_pinchance);
        tvTncDesc = findViewById(R.id.tvTncDesc);
        btnForgotPin = findViewById(R.id.btn_forgot_pin);
    }

    protected void initContent() {

        tvTncDesc.setText(Html.fromHtml(getString(R.string.msg_tnc_confirmation)));
        if (isRegister) {
            tv_text.setText(R.string.text_make_new_pin);
            btnForgotPin.setVisibility(View.GONE);
        }

        if (isTest) {
            Intent jenk = new Intent();
            jenk.putExtra("pincode", SessionManager.getSecondaryToken());
            setResult(this.RESULT_OK, jenk);
            Toast.makeText(this, "a test is triggered, bypassing", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (isCheck) {
            tv_text.setText(getString(R.string.label_input_pin_confirm));
        }
        if (isConfirm && !getIntent().hasExtra(LAYOUT_ID)) {
            tv_text.setText(getString(R.string.label_input_pin));
            layout_retcontainer.setVisibility(View.VISIBLE);
        }

        if (isAddAccountLink && !getIntent().hasExtra(LAYOUT_ID)) {
            tv_text.setText(getString(R.string.label_input_pin));
            layout_retcontainer.setVisibility(View.VISIBLE);
        }
        if (isPayConfirm) {
            layout_retcontainer.setVisibility(View.VISIBLE);
        }

        if (isQrConfrim) {
            layout_retcontainer.setVisibility(View.VISIBLE);
        }
        if (isCheckSession) {
            layout_retcontainer.setVisibility(View.VISIBLE);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.l_1:
                        if (isReinput) {
                            inputRePin(1);
                        } else {
                            inputPin(1);
                        }
                        break;
                    case R.id.l_2:
                        if (isReinput) {
                            inputRePin(2);
                        } else {
                            inputPin(2);
                        }
                        break;
                    case R.id.l_3:
                        if (isReinput) {
                            inputRePin(3);
                        } else {
                            inputPin(3);
                        }
                        break;
                    case R.id.l_4:
                        if (isReinput) {
                            inputRePin(4);
                        } else {
                            inputPin(4);
                        }
                        break;
                    case R.id.l_5:
                        if (isReinput) {
                            inputRePin(5);
                        } else {
                            inputPin(5);
                        }
                        break;
                    case R.id.l_6:
                        if (isReinput) {
                            inputRePin(6);
                        } else {
                            inputPin(6);
                        }
                        break;
                    case R.id.l_7:
                        if (isReinput) {
                            inputRePin(7);
                        } else {
                            inputPin(7);
                        }
                        break;
                    case R.id.l_8:
                        if (isReinput) {
                            inputRePin(8);
                        } else {
                            inputPin(8);
                        }
                        break;
                    case R.id.l_9:
                        if (isReinput) {
                            inputRePin(9);
                        } else {
                            inputPin(9);
                        }
                        break;
                    case R.id.l_0:
                        if (isReinput) {
                            inputRePin(0);
                        } else {
                            inputPin(0);
                        }
                        break;
                    case R.id.l_b:
                        if (isReinput) {
                            rePin = del(rePin);
                            validate(rePin.length());
                        } else {
                            pin = del(pin);
                            validate(pin.length());
                        }
                        break;
                    case R.id.l_x: {
                        Intent jink = new Intent(RegisterPinResetActivity.this, ForgotPhoneActivity.class);
                        startActivity(jink);
                    }
                }
            }
        };

        k1.setOnClickListener(listener);
        k2.setOnClickListener(listener);
        k3.setOnClickListener(listener);
        k4.setOnClickListener(listener);
        k5.setOnClickListener(listener);
        k6.setOnClickListener(listener);
        k7.setOnClickListener(listener);
        k8.setOnClickListener(listener);
        k9.setOnClickListener(listener);
        k0.setOnClickListener(listener);
        kb.setOnClickListener(listener);
        kx.setOnClickListener(listener);

        backBtn.setOnClickListener(v -> onBackPressed());

        btnForgotPin.setOnClickListener(v -> {
            Intent jink = new Intent(RegisterPinResetActivity.this, ForgotPhoneActivity.class);
            String phoneNumber = getIntent().getStringExtra(AppKeys.KEY_PHONE_NUMBER);
            jink.putExtra(AppKeys.KEY_PHONE_NUMBER, phoneNumber);
            startActivity(jink);
        });
    }

    private void inputPin(int input) {
        if (pin.length() < 6) {
            pin += input + "";
            validate(pin.length());
        }
    }

    private void inputRePin(int input) {
        if (rePin.length() < 6) {
            rePin += input + "";
            validate(rePin.length());
        }
    }

    protected String del(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        } else if (str != null && str.length() == 1) {
            str = "";
        }
        return str;
    }

    protected void validate(int size) {
        switch (size) {
            case 0:
                setBlankPin(dot1);
                setBlankPin(dot2);
                setBlankPin(dot3);
                setBlankPin(dot4);
                setBlankPin(dot5);
                setBlankPin(dot6);
                break;
            case 1:
                setFillPin(dot1);
                setBlankPin(dot2);
                setBlankPin(dot3);
                setBlankPin(dot4);
                setBlankPin(dot5);
                setBlankPin(dot6);
                break;
            case 2:
                setFillPin(dot1);
                setFillPin(dot2);
                setBlankPin(dot3);
                setBlankPin(dot4);
                setBlankPin(dot5);
                setBlankPin(dot6);
                break;
            case 3:
                setFillPin(dot1);
                setFillPin(dot2);
                setFillPin(dot3);
                setBlankPin(dot4);
                setBlankPin(dot5);
                setBlankPin(dot6);
                break;
            case 4:
                setFillPin(dot1);
                setFillPin(dot2);
                setFillPin(dot3);
                setFillPin(dot4);
                setBlankPin(dot5);
                setBlankPin(dot6);
                break;
            case 5:
                setFillPin(dot1);
                setFillPin(dot2);
                setFillPin(dot3);
                setFillPin(dot4);
                setFillPin(dot5);
                setBlankPin(dot6);
                break;
            case 6:
                setFillPin(dot1);
                setFillPin(dot2);
                setFillPin(dot3);
                setFillPin(dot4);
                setFillPin(dot5);
                setFillPin(dot6);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRegister) {
                            if (isReinput) {
                                if (!pin.equals(rePin)) {
                                    tv_text.setText(getString(R.string.message_pin_invalid));
                                    validate(0);
                                    rePin = "";
                                } else {
                                    Intent jenk = new Intent();
                                    jenk.putExtra("pincode", rePin);
                                    setResult(RegisterPinResetActivity.this.RESULT_OK, jenk);
                                    finish();
                                }
                            } else {
                                if (DataUtil.isPinValid(rePin)) {
                                    Popup.showInfo(getSupportFragmentManager(), "Incorrect PIN Format", getString(R.string.message_pin_format_unaccaptable), null);
                                } else {
                                    isReinput = true;
                                    tv_text.setText(getString(R.string.label_input_pin_confirm));
                                }
                                validate(0);
                            }
                        } else if (isCheck || isCheckSession) {
                            if (!pin.equals(SessionManager.getSecondaryToken())) {
                                if (retries == 3) {
                                    SessionManager.logout();
                                    RegisterPinResetActivity.this.startActivity(new Intent(RegisterPinResetActivity.this, LoginActivity.class));
                                    finish();
                                }
                                tv_text.setText(getString(R.string.message_pin_invalid));
                                validate(0);
                                pin = "";
                                retries++;

                                if (isCheckSession) {
                                    tv_retries.setText((3 - retries) + " Kali");
                                }
                            } else {
                                setResult(RegisterPinResetActivity.this.RESULT_OK);
                                finish();
                            }
                        } else if (isConfirm) {
                            if (!pin.equals(SessionManager.getSecondaryToken())) {
                                if (retries == 3) {
                                    SessionManager.logout();
                                    RegisterPinResetActivity.this.startActivity(new Intent(RegisterPinResetActivity.this, LoginActivity.class));
                                    finish();
                                }
                                tv_text.setText(getString(R.string.message_pin_invalid));
                                validate(0);
                                pin = "";
                                retries++;
                                tv_retries.setText((3 - retries) + " Kali");
                            } else {
                                Intent jenk = new Intent();
                                jenk.putExtra("pincode", pin);
                                setResult(RegisterPinResetActivity.this.RESULT_OK, jenk);
                                finish();
                            }
                        } else if (isAddAccountLink) {
                            if (!pin.equals(SessionManager.getSecondaryToken())) {
                                if (retries == 3) {

//                                    Intent intent = new Intent();
//                                    intent.putExtra("accountNumber",accountNumber);
//                                    intent.putExtra("bin",bankName);

                                    Intent intent = new Intent(RegisterPinResetActivity.this, OtpVerificationActivity.class);
                                    intent.putExtra(OtpVerificationActivity.KEY_REQ_ADD_BANK, true);
                                    intent.putExtra(OtpVerificationActivity.KEY_DATA_PHONE, SessionManager.getPrefLogin().getPhone());
                                    intent.putExtra(OtpVerificationActivity.KEY_ACCOUNT_NUMBER, accountNumber);
                                    intent.putExtra(OtpVerificationActivity.KEY_BIN, bankName);
                                    startActivity(intent);
                                    // RegisterPinResetActivity.this.startActivity(new Intent(RegisterPinResetActivity.this, LoginActivity.class));
                                    finish();
                                }
                                tv_text.setText(getString(R.string.message_pin_invalid));
                                validate(0);
                                pin = "";
                                retries++;
                                tv_retries.setText((3 - retries) + " Kali");
                            } else {

                                postRequest();

                            }

                        } else if (isPayConfirm) {
                            if (!pin.equals(SessionManager.getSecondaryToken())) {
//                                Toast.makeText(RegisterPinResetActivity.this, "pin anda salah", Toast.LENGTH_SHORT).show();
//                                finish();
                                if (retries == 3) {
                                    SessionManager.logout();
                                    RegisterPinResetActivity.this.startActivity(new Intent(RegisterPinResetActivity.this, LoginActivity.class));
                                    finish();
                                }
                                tv_text.setText(getString(R.string.message_pin_invalid));
                                validate(0);
                                pin = "";
                                retries++;
                                tv_retries.setText((3 - retries) + " Kali");
                            } else {
                                Intent jenk = new Intent();
                                jenk.putExtra("pincode", pin);
                                setResult(RegisterPinResetActivity.this.RESULT_OK, jenk);
                                finish();
                            }
                        } else if (isTransferConfrim) {
                            if (!pin.equals(SessionManager.getSecondaryToken())) {
//                                Toast.makeText(RegisterPinResetActivity.this, "pin anda salah", Toast.LENGTH_SHORT).show();
//                                finish();
                                if (retries == 3) {
                                    SessionManager.logout();
                                    RegisterPinResetActivity.this.startActivity(new Intent(RegisterPinResetActivity.this, LoginActivity.class));
                                    finish();
                                }
                                tv_text.setText(getString(R.string.message_pin_invalid));
                                validate(0);
                                pin = "";
                                retries++;
                                tv_retries.setText((3 - retries) + " Kali");
                            } else {
                                Intent jenk = new Intent();
                                jenk.putExtra("pincode", pin);
                                setResult(RegisterPinResetActivity.this.RESULT_OK, jenk);
                                finish();
                            }
                        } else if (isQrConfrim) {

                            if (!pin.equals(SessionManager.getSecondaryToken())) {
                                if (retries == 3) {
                                    SessionManager.logout();
                                    RegisterPinResetActivity.this.startActivity(new Intent(RegisterPinResetActivity.this, LoginActivity.class));
                                    finish();
                                }
                                tv_text.setText(getString(R.string.message_pin_invalid));
                                validate(0);
                                pin = "";
                                retries++;
                                tv_retries.setText((3 - retries) + " Kali");
                            } else {
                                Intent jenk = new Intent();
                                jenk.putExtra("pincode", pin);
                                setResult(RegisterPinResetActivity.this.RESULT_OK, jenk);
                                finish();
                            }
                        } else if (isLogin) {
                            Intent jenk = new Intent();
                            jenk.putExtra("pincode", pin);
                            setResult(RegisterPinResetActivity.this.RESULT_OK, jenk);
                            finish();
                        } else if (isReset) {
                            if (isNewPin) {
                                isReinput = true;
                                isNewPin = false;
                                tv_text.setText(getString(R.string.label_input_pin_confirm));
                                validate(0);
                            } else if (isReinput) {
                                if (!pin.equals(rePin)) {
                                    tv_text.setText(getString(R.string.message_pin_invalid));
                                    validate(0);
                                    rePin = "";
                                } else {
                                    UpdatePinRequestModel model = new UpdatePinRequestModel();
                                    model.setOld_pin(SessionManager.getSecondaryToken());
                                    model.setNew_pin(rePin);
                                    ProgressDialogComponent.showProgressDialog(RegisterPinResetActivity.this, getString(R.string.loading_message), false);
                                    new ProfileDao(RegisterPinResetActivity.this).updatePin(model, BaseDao.getInstance(RegisterPinResetActivity.this, 322).callback);
                                }
                            } else if (!pin.equals(SessionManager.getSecondaryToken())) {
//                                Toast.makeText(RegisterPinResetActivity.this, "pin anda salah", Toast.LENGTH_SHORT).show();
//                                finish();
                                if (retries == 3) {
                                    SessionManager.logout();
                                    RegisterPinResetActivity.this.startActivity(new Intent(RegisterPinResetActivity.this, LoginActivity.class));
                                    finish();
                                }
                                tv_text.setText(getString(R.string.message_pin_invalid));
                                validate(0);
                                pin = "";
                                retries++;
                                tv_retries.setText((3 - retries) + " Kali");
                            } else {
                                isNewPin = true;
                                tv_text.setText(R.string.masukkan_pin_baru);
                                pin = "";
                                validate(0);
                            }

                        }
                    }
                }, 1000);

                break;
            default:
                break;
        }
    }

    private void setBlankPin(ImageView pinItem) {
        if (getIntent().hasExtra(LAYOUT_ID))
            pinItem.setImageResource(R.drawable.border_circle_absolute_white);
        else
            pinItem.setImageResource(R.drawable.border_circle_white);
    }

    private void setFillPin(ImageView pinItem) {
        if (getIntent().hasExtra(LAYOUT_ID))
            pinItem.setImageResource(R.drawable.circle_white);
        else
            pinItem.setImageResource(R.drawable.circle_dark_sky_green);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        super.onApiResponseCallback(br, responseCode, response);
        if (responseCode == 322) {
            if (((DataEmptyResponseModel) br).getMeta().getCode() == 200) {
                BaseResponseModel model = (BaseResponseModel) br;
                Popup popup = new Popup();
                popup.setTitle("Change Pin success, Please login using the New Pin");
                popup.setPositiveButton("OK");
                popup.setPositiveAction(() -> {
                    finish();
                    return Unit.INSTANCE;
                });
                popup.setHideBtnNegative(true);
                popup.show(getSupportFragmentManager(), "confirmationchangepin");
                SessionManager.setSecondaryToken(rePin);
            } else {
                ErrorDialog errorDialog = new ErrorDialog(RegisterPinResetActivity.this, RegisterPinResetActivity.this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), ((BaseResponseModel) br).getMeta().getMessage());
                errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isReset = true;
                        isReinput = false;
                        isNewPin = false;
                        tv_text.setText(R.string.masukkan_pin_lama_anda);
                        btnForgotPin.setText(R.string.lupa_pin);
                        isReset = true;
                        pin = "";
                        rePin = "";
                        validate(0);
                    }
                });
                errorDialog.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isRegister) {
            RegisterPinResetActivity.this.setResult(RegisterPinResetActivity.this.RESULT_CANCELED);
            finish();
        } else if (isReset) {
            RegisterPinResetActivity.this.setResult(RegisterPinResetActivity.this.RESULT_CANCELED);
            finish();
        } else if (isReinput) {
            RegisterPinResetActivity.this.setResult(RegisterPinResetActivity.this.RESULT_CANCELED);
            finish();
        } else if (isLogin) {
            RegisterPinResetActivity.this.setResult(RegisterPinResetActivity.this.RESULT_CANCELED);
            finish();
        } else if (isCheck) {
            RegisterPinResetActivity.this.setResult(RegisterPinResetActivity.this.RESULT_CANCELED);
            finish();
        } else if (isConfirm) {
            RegisterPinResetActivity.this.setResult(RegisterPinResetActivity.this.RESULT_CANCELED);
            finish();
        } else if (isAddAccountLink) {
            RegisterPinResetActivity.this.setResult(RegisterPinResetActivity.this.RESULT_CANCELED);
            finish();

        } else if (isCheckSession) {
            SessionManager.logout();
            Intent i = new Intent(RegisterPinResetActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } else if (isPayConfirm) {
            RegisterPinResetActivity.this.setResult(RegisterPinResetActivity.this.RESULT_CANCELED);
            finish();
        } else if (isQrConfrim) {
            RegisterPinResetActivity.this.setResult(RegisterPinResetActivity.this.RESULT_CANCELED);
        } else {
            RegisterPinResetActivity.this.setResult(RegisterPinResetActivity.this.RESULT_CANCELED);
            finish();
        }
    }

    void postRequest() {
        showProgressDialog(R.string.loading_message);
        IssuerLinkedRequest request = new IssuerLinkedRequest();
        request.setAccountName("");
        request.setAccountNumber(accountNumber);
        request.setAccountStatus("");
        request.setAccountType(accountType);
        request.setBin(bankName);
        request.setCid("");
        request.setMid(SessionManager.getPrefLogin().getMid());
        request.setStatus("");

        OttoKonekAPI.issuerLinkedRequest(this, request, new ApiCallback() {
            @Override
            public void onResponseSuccess(Object body) {
                dismissProgressDialog();
                if (isSuccess200) {
                    Intent intent = new Intent(RegisterPinResetActivity.this, OtpVerificationActivity.class);
                    intent.putExtra(OtpVerificationActivity.KEY_REQ_ADD_BANK, true);
                    intent.putExtra(OtpVerificationActivity.KEY_DATA_PHONE, SessionManager.getPrefLogin().getPhone());
                    intent.putExtra(OtpVerificationActivity.KEY_ACCOUNT_NUMBER, accountNumber);
                    intent.putExtra(OtpVerificationActivity.KEY_BIN, bankName);
                    startActivityForResult(intent, KEY_CODE_INPUT_OTP);
                } else {
                    showErrorDialog(responseMessage);
                }


//                dismissProgressDialog();
//                if (isSuccess200){
//                    Intent intent = new Intent(RegisterPinResetActivity.this, OtpVerificationActivity.class);
//                    intent.putExtra(OtpVerificationActivity.KEY_DATA_PHONE, SessionManager.getPhone());
//                    startActivityForResult(intent, KEY_CODE_INPUT_OTP);
//
//                }else {
//                    dismissProgressDialog();
//                    showErrorMessage("Something Wrong");
//                }

            }

            @Override
            public void onApiServiceFailed(Throwable throwable) {
                dismissProgressDialog();
                showErrorMessage("Something Wrong");
            }
        });
    }


    protected void showErrorMessage(String message) {
        ErrorDialog dialog = new ErrorDialog(this, this, false,
                false, message, "");
        dialog.show();
    }


    private void callApiConfrimQr(QrPaymentRequest requestModel) {


        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
        TransactionDao dao = new TransactionDao(this);
        dao.payQrPaymentNew(requestModel, BaseDao.getInstance(this, API_KEY_RESPONSE).callback);
    }

}
