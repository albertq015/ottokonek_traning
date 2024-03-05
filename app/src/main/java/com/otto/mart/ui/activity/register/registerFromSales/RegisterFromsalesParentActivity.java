package com.otto.mart.ui.activity.register.registerFromSales;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.AuthDataModel;
import com.otto.mart.model.APIModel.Request.RegisterOtpRequestModel;
import com.otto.mart.model.APIModel.Request.ResendOtpRegisterRequestModel;
import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.model.APIModel.Request.UpdateStatusRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.SearchMerchantResponse;
import com.otto.mart.model.APIModel.Response.SignupOtpResponseModel;
import com.otto.mart.model.APIModel.Response.SignupResponseModel;
import com.otto.mart.model.localmodel.State.RegisterStateModel;
import com.otto.mart.model.localmodel.ui.BankUiModel;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.presenter.dao.EtcDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.register.IRegister;
import com.otto.mart.ui.activity.register.RegisterAddRekeningActivity;
import com.otto.mart.ui.activity.register.SecurityQuestionPickerActivity;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.activity.register.kyc.RegisterAccountActivationActivity;
import com.otto.mart.ui.component.dialog.ChainedListDialog;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.dialog.OtpDialog;
import com.otto.mart.ui.fragment.register.RegisterPinFragment;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static com.otto.mart.keys.AppKeys.KEY_PHONE_NUMBER;
import static com.otto.mart.keys.AppKeys.KEY_USER_ID;

public class RegisterFromsalesParentActivity extends AppActivity implements RegisterFromsalesParentInterface, IRegister {

    final String TAG = "RegisterFromSale";

    private Window thiswindow;
    private LinearLayout backhitbox;
    private TextView tvTitle;
    private SignupRequestModel model;
    private RegisterStateModel stateModel;
    private Fragment fragment;
    private OtpDialog otpDialog;
    private FrameLayout layout_fragmentContainer;
    private FragmentManager manager;
    private boolean isSecurityQuestionInitialized;
    private String selectedSecurityQuestion;
    private int uid = -1;
    private SearchMerchantResponse.Data.Merchant data;

    private UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest();

    private boolean isUpdateStatus = false;
    private String mPhoneNumber = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thiswindow = this.getWindow();
        setContentView(R.layout.activity_register_fromsales);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thiswindow.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
        }
        model = new SignupRequestModel();
        stateModel = new RegisterStateModel();

        initLocationPermission();
        initFragment();
        initComponent();
        initContent();
        initOtpDialog();
        buildUpgradeAccountDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initFragment() {
        fragment = new RegisterPinFragment();
    }

    private void initComponent() {
        layout_fragmentContainer = findViewById(R.id.layout_fragmentContainer);
        backhitbox = findViewById(R.id.backhitbox);
        tvTitle = findViewById(R.id.qrscan_title);
        manager = getSupportFragmentManager();
    }

    private void initContent() {
        isUpdateStatus = getIntent().getBooleanExtra(AppKeys.KEY_IS_UPDATE_STATUS, false);

        if (getIntent().getStringExtra(KEY_PHONE_NUMBER) != null) {
            mPhoneNumber = getIntent().getStringExtra(KEY_PHONE_NUMBER);
        }

        if (isUpdateStatus) {
            uid = getIntent().getIntExtra(KEY_USER_ID, -1);
            ((RegisterPinFragment) fragment).setUpdateStatus(true);
            tvTitle.setText(getString(R.string.text_edit_pin));
        }

        manager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(layout_fragmentContainer.getId(), fragment).commit();

        data = new Gson().fromJson(getIntent().getStringExtra("merchantData"), SearchMerchantResponse.Data.Merchant.class);
        if (data != null) {
            model.setBusiness_category_id(data.getBusiness_category_id());
            model.setProvince_id(data.getProvince_id());
            model.setCity_id(data.getCity_id());
            model.setDistrict_id(data.getDistrict_id());
            model.setVillage_id(data.getVillage_id());
            model.setMerchant_name(data.getMerchant_name());
            model.setComplete_address(data.getAddress());
            model.setName(data.getName());
            model.setPhone(data.getPhone_number());
            model.setDob(data.getDob());
            model.setBanks(new ArrayList<>());
            model.setFirebase_token("");
            model.setMother_name("");
            model.setReferal_number("");
            model.setLatitude(myLastLocation.getLatitude());
            model.setLongitude(myLastLocation.getLongitude());
            model.setMerchant_id(data.getMerchant_id());
            /*if (data.getMerchant_id().contains(";;;;"))
                model.setMerchant_id(data.getMerchant_id());
            else model.setMerchant_id("RTSM;;;;" + data.getMerchant_id());*/
            model.setEmail("N/A");
        }

        backhitbox.setOnClickListener(v -> {
            onBackPressed();
        });

        backhitbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initOtpDialog() {
        otpDialog = new OtpDialog(this, this, false);

        otpDialog.setOtpListener(new OtpDialog.OtpDialogListener() {
            @Override
            public void onOtpEntered(OtpDialog dialog) {

            }

            @Override
            public void onActionPressed(OtpDialog dialog) {
                if (isUpdateStatus) {
                    ProgressDialogComponent.showProgressDialog(RegisterFromsalesParentActivity.this, "Loading", false).show();

                    updateStatusRequest.setOtp_code(dialog.getOtpCode());

                    AuthDao auth = new AuthDao(this);
                    auth.updateStatusDao(updateStatusRequest, BaseDao.getInstance(RegisterFromsalesParentActivity.this, AppKeys.API_KEY_UPDATEE_STATUS).callback);
                } else {
                    RegisterOtpRequestModel requestModel = new RegisterOtpRequestModel();
                    requestModel.setQuestion_id(model.getQuestion_id());
                    requestModel.setAnswer(model.getAnswer());
                    requestModel.setUser_id(uid);
                    requestModel.setOtp_code(dialog.getOtpCode());
                    requestModel.setPhone(data.getPhone_number());
                    requestModel.setRose(true);
                    requestModel.setLatitude(myLastLocation.getLatitude());
                    requestModel.setLongitude(myLastLocation.getLongitude());
                    requestModel.setQr_merchant_id(data.getMerchant_id());
                    /*if (data.getMerchant_id().contains(";;;;")) {
                        requestModel.setQr_merchant_id(data.getMerchant_id());
                    } else {
                        requestModel.setQr_merchant_id("RTSM;;;;" + data.getMerchant_id());
                    }*/

                    callRegisterOtpApi(requestModel);
                    dialog.setLoadingState();
                }
            }

            @Override
            public void onBackPressed(OtpDialog dialog) {

            }

            @Override
            public void onResendPressed(OtpDialog dialog) {
                otpDialog.clearOTP();
                ResendOtpRegisterRequestModel requestModel = new ResendOtpRegisterRequestModel();
                requestModel.setUser_id(uid);
                reCallRegisterOtpApi(requestModel);
            }
        });

    }


    private void buildUpgradeAccountDialog() {

    }

    @Override
    public void showKycDialog() {

    }

    @Override
    public void closeKycDialog() {
    }

    @Override
    public void moveToKyc() {
        startActivity(new Intent(this, RegisterAccountActivationActivity.class));
    }

    @Override
    public void showOtpDialog() {
        otpDialog.setPadding(16);
        otpDialog.show();
    }

    @Override
    public void hideOtpDialog() {
        otpDialog.dismiss();
    }

    @Override
    public void showErrorDialog(String title, String message) {
        ErrorDialog jenk = new ErrorDialog(this, this, false, false, title, message);
        jenk.show();
    }

    @Override
    public SignupRequestModel getModel() {
        return model;
    }

    @Override
    public void updateModel(SignupRequestModel model) {
        this.model = model;
    }

    @Override
    public void moveToNext() {

    }

    @Override
    public void moveToPrev() {

    }

    @Override
    public void putSecAnswer(String question, int secid) {
        isSecurityQuestionInitialized = true;
        selectedSecurityQuestion = question;
        int selectedSecurityQuestionID = secid;
    }

    @Override
    public void callSecAnswerPicker() {
        Intent jing = new Intent(this, SecurityQuestionPickerActivity.class);
        this.startActivityForResult(jing, 123);
    }

    @Override
    public void callMap() {


    }

    @Override
    public void callAddressInput() {

    }

    @Override
    public void callBankSelector() {
        Intent jink = new Intent(this, RegisterAddRekeningActivity.class);
        this.startActivityForResult(jink, 126);
    }

    public void callBankSelector(int position, int paymentposition, int bankposition, String norek, String personName) {
        Intent jink = new Intent(this, RegisterAddRekeningActivity.class);
        jink.putExtra("position", position);
        jink.putExtra("paymentposition", paymentposition);
        jink.putExtra("bankposition", bankposition);
        jink.putExtra("norek", norek);
        jink.putExtra("name", personName);
        jink.putExtra("isEdit", true);
        this.startActivityForResult(jink, 126);
    }

    @Override
    public void callPinBullshit() {
        Intent jenk = new Intent(this, RegisterPinResetActivity.class);
        jenk.putExtra("register", true);
        startActivityForResult(jenk, 127);
    }

    @Override
    public RegisterStateModel getStateModel() {
        return stateModel;
    }

    @Override
    public List<BankUiModel> getBankUiModels() {
        return null;
    }

    private void callRegisterAPI(SignupRequestModel signupRequestModel) {
        AuthDao auth = new AuthDao(this);
        auth.signUpDao(signupRequestModel, BaseDao.getInstance(this, 444).callback);
        ProgressDialogComponent.showProgressDialog(this, "Loading", false).show();
    }

    @Override
    public void callRegister() {
        callRegisterAPI(model);
    }

    @Override
    public void callUpdateStatus(String answer) {
        updateStatusRequest.setAnswer(answer);
        if (!mPhoneNumber.equals("")) {
            otpDialog.setPhoneNumber(mPhoneNumber);
        }
        showOtpDialog();
    }

    @Override
    public ChainedListDialog getBcDialog() {
        return null;
    }

    private void callRegisterOtpApi(RegisterOtpRequestModel model) {
        AuthDao auth = new AuthDao(this);
        auth.signUpOtpDao(model, BaseDao.getInstance(this, 445).callback);
    }

    private void callBusinessListApi() {
        EtcDao dao = new EtcDao(this);
        dao.getBusinessDao(BaseDao.getInstance(this, 446).callback);
    }

    private void reCallRegisterOtpApi(ResendOtpRegisterRequestModel model) {
        AuthDao auth = new AuthDao(this);
        auth.resendOtpDao(model, BaseDao.getInstance(this, 447).callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 123:
                if (resultCode == RESULT_OK)
                    try {
                        String question = data.getExtras().getString("question");
                        int id = data.getIntExtra("id", -1);
                        if (id >= 0) {
                            model.setQuestion_id(id);
                            ((RegisterPinFragment) fragment).setQuestion(question);

                            if (isUpdateStatus) {
                                updateStatusRequest.setSecurity_question_id(id);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                break;
            case 127:
                if (resultCode == RESULT_OK) {
                    ((RegisterPinFragment) fragment).addPin(data.getStringExtra("pincode"));

                    if (isUpdateStatus) {
                        updateStatusRequest.setNew_pin(data.getStringExtra("pincode"));
                    }
                }
                break;
        }
    }

    @Override
    public boolean isSecAnswerInitialized() {
        return false;
    }

    @Override
    public void onBackPressed() {
        moveToPrev();
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (response.isSuccessful()) {
            switch (responseCode) {
                case 444: {
                    if (((SignupResponseModel) br).getMeta().getCode() == 200) {
                        uid = ((SignupResponseModel) br).getUser_id();
                        if (((SignupResponseModel) br).getPhone() != null)
                            otpDialog.setPhoneNumber(((SignupResponseModel) br).getPhone());
                        otpDialog.show();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Registrasi Gagal, mohon hubungi (021) 1500749", ((SignupResponseModel) br).getMeta().getMessage());
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                RegisterFromsalesParentActivity.this.finish();
                            }
                        });
                        dialog.show();
//       Toast.makeText(this, "Registrasi Gagal, mohon hubungi (021) 1500749\nerrcode:" + ((SignupResponseModel) br).getMeta().getMessage()
//                                , Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                    break;
                }
                case 445: {
                    otpDialog.setNotLoadingState();
                    if (((SignupOtpResponseModel) br).getMeta().getCode() == 200) {
                        otpDialog.dismiss();
                        AuthDataModel model = ((SignupOtpResponseModel) br).getData();
                        SessionManager.setPrefLogin(model);
                        SessionManager.createLoginSession(model.getUser_id(), model.getWallet_id(), model.getAccess_token(), this.model.getPin(), model);
                        startActivity(new Intent(this, DashboardActivity.class));
                        finish();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Registrasi Gagal, mohon hubungi (021) 1500749", ((SignupOtpResponseModel) br).getMeta().getMessage());
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                RegisterFromsalesParentActivity.this.finish();
                            }
                        });
                        dialog.show();
//                        Toast.makeText(this, "Registrasi Gagal, mohon hubungi (021) 1500749\nerrcode:" + ((SignupOtpResponseModel) br).getMeta().getMessage()
//                                , Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                    break;
                }
                case 446:

                    break;
                case 447:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        Toast.makeText(this, ((BaseResponseModel) br).getMeta().getMessage()
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Registrasi Gagal, mohon hubungi (021) 1500749", ((SignupResponseModel) br).getMeta().getMessage());
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                RegisterFromsalesParentActivity.this.finish();
                            }
                        });
                    }
                    break;
                case AppKeys.API_KEY_UPDATEE_STATUS:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        SessionManager.setSecondaryToken(updateStatusRequest.getNew_pin());
                        Intent intent = new Intent(RegisterFromsalesParentActivity.this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        if (otpDialog.isShowing()) {
                            otpDialog.dismiss();
                        }
                        initOtpDialog();
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
//        Toast.makeText(ac, "Terjadi kesalah pada server,  Mohon registrasi ulang beberapa saat lagi :" + message, Toast.LENGTH_SHORT).show();
//        finish();
        onApiResponseError();
    }
}
