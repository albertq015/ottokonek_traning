package com.otto.mart.ui.activity.register.registerFromSales.registerConfirmation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.AuthDataModel;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Request.RegisterOtpRequestModel;
import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.SignupOtpResponseModel;
import com.otto.mart.model.APIModel.Response.SignupResponseModel;
import com.otto.mart.model.APIModel.Response.merchant.featureProduct.FeatureProductResponse;
import com.otto.mart.model.APIModel.Response.merchant.theme.MerchantThemeResponse;
import com.otto.mart.model.APIModel.Response.register.SearchMerchantData;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.Partials.adapter.ppob.PpobPaymentDetailAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.login.OtpVerificationActivity;
import com.otto.mart.ui.activity.register.registerFromSales.ActivationConfirmationActivity;
import com.otto.mart.ui.activity.register.registerFromSales.ActivationInputPinActivity;
import com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput.RegisterPhotoMerchantActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class RegisterFromsalesConfirmActivity extends AppActivity implements RegisterFromsalesConfirmInterface {

    private static final int KEY_TAC_OTTOPAY = 1;
    private SearchMerchantData dataMerchant;
    private SignupRequestModel model;

    private final int KEY_API_SIGN_UP_OTP = 445;
    private final int KEY_API_MERCHANT_THEME = 446;
    private final int KEY_API_FEATURE_MERCHANT = 447;

    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private RecyclerView recyclerview;
    private Button btnNext;
    private int uid = -1;

    private final int KEY_CODE_INPUT_PIN = 100;
    private final int KEY_CODE_INPUT_OTP = 101;


    private String mPin = "";
    private String mOtp = "";

    private final int KEY_API_RESET_OTP = 200;
    private final int KEY_API_RESET = 201;

    private String mPhone = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fromsales_confirmation);
        initLocationPermission();
        initContent();
        initRecyclerView();
        initComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == KEY_CODE_INPUT_PIN) {
//                if (data != null) {
//                    mPin = data.getStringExtra(ActivationInputPinActivity.KEY_DATA_PIN);
//
//                    if (data != null) {
//                        model.setBusiness_category_id(dataMerchant.getBusiness_category_id());
//                        model.setProvince_id(dataMerchant.getProvince_id());
//                        model.setCity_id(dataMerchant.getCity_id());
//                        model.setDistrict_id(dataMerchant.getDistrict_id());
//                        model.setVillage_id(dataMerchant.getVillage_id());
//                        model.setMerchant_name(dataMerchant.getMerchant_name());
//                        model.setComplete_address(dataMerchant.getAddress());
//                        model.setName(dataMerchant.getName());
//                        model.setPhone(dataMerchant.getPhone_number());
//                        model.setDob(dataMerchant.getDob());
//                        model.setBanks(new ArrayList<>());
//                        model.setFirebase_token("");
//                        model.setMother_name("");
//                        model.setReferal_number("");
//                        model.setPin(mPin);
//                        model.setPin_confirmation(mPin);
//                        model.setRose(true);
//                        model.setLatitude(myLastLocation.getLatitude());
//                        model.setLongitude(myLastLocation.getLongitude());
//                        if (dataMerchant.getMerchant_id().contains(";;;;"))
//                            model.setMerchant_id(dataMerchant.getMerchant_id());
//                        else model.setMerchant_id("RTSM;;;;" + dataMerchant.getMerchant_id());
//                        model.setEmail("N/A");
//                    }
//
//                    callRegisterAPI(model);
//                }
//            } else if (requestCode == KEY_CODE_INPUT_OTP) {
//                if (data != null) {
//                    mOtp = data.getStringExtra(OtpVerificationActivity.KEY_DATA_OTP);
//
//                    RegisterOtpRequestModel requestModel = new RegisterOtpRequestModel();
//                    //requestModel.setQuestion_id(0);
//                    //requestModel.setAnswer(model.getAnswer());
//                    requestModel.setUser_id(uid);
//                    requestModel.setOtp_code(mOtp);
//                    requestModel.setPhone(dataMerchant.getPhone_number());
//                    requestModel.setRose(true);
//                    requestModel.setLatitude(myLastLocation.getLatitude());
//                    requestModel.setLongitude(myLastLocation.getLongitude());
//
//                    if (dataMerchant.getMerchant_id().contains(";;;;")) {
//                        requestModel.setQr_merchant_id(dataMerchant.getMerchant_id());
//                    } else {
//                        requestModel.setQr_merchant_id("RTSM;;;;" + dataMerchant.getMerchant_id());
//                    }
//
//                    callRegisterOtpApi(requestModel);
//                }
//            } else if (requestCode == KEY_TAC_OTTOPAY) {
//                if (getCurrentLocation()) {
//                    moveToRegisterActivity();
//                }
//            }
//        }
    }

    private void initContent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        recyclerview = findViewById(R.id.recyclerview);
        btnNext = findViewById(R.id.btnNext);
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(linearLayoutManager);
    }

    private void initComponent() {

        model = new SignupRequestModel();

        tvToolbarTitle.setText(R.string.title_merchant_phone_confirm);

        btnToolbarBack.setOnClickListener(v -> onBackPressed());

//        btnNext.setOnClickListener(v -> {
//            moveToRegisterActivity();
//            Intent jenk = new Intent(RegisterFromsalesConfirmActivity.this, RegisterPhotoMerchantActivity.class);
//            jenk.putExtra("merchantData", getIntent().getStringExtra("merchantData"));
//            startActivity(jenk);
//        });

        btnNext.setOnClickListener(v -> {
            if (getCurrentLocation()) {
                gotoTakePhoto();
            }
        });

        if (getIntent().hasExtra("merchantData")) {
            dataMerchant = new Gson().fromJson(getIntent().getStringExtra("merchantData"), SearchMerchantData.class);

            mPhone = dataMerchant.getPhoneNumber();

            List<WidgetBuilderModel> dataTokoList = new ArrayList();

            WidgetBuilderModel keyValue;

            keyValue = new WidgetBuilderModel();
            keyValue.setKey(getString(R.string.label_merchant_id));
            keyValue.setValue(dataMerchant.getMid() == null ? "-" : dataMerchant.getMid());
            dataTokoList.add(keyValue);

            keyValue = new WidgetBuilderModel();
            keyValue.setKey(getString(R.string.label_company_name));
            keyValue.setValue(dataMerchant.getMerchantName() == null ? "-" : dataMerchant.getMerchantName());
            dataTokoList.add(keyValue);

            keyValue = new WidgetBuilderModel();
            keyValue.setKey(getString(R.string.label_company_type));
            keyValue.setValue(dataMerchant.getBusinessCategoryName());
            dataTokoList.add(keyValue);

            keyValue = new WidgetBuilderModel();
            keyValue.setKey(getString(R.string.label_full_name));
            keyValue.setValue(dataMerchant.getOwnerName() == null ? "-" : dataMerchant.getOwnerName());
            dataTokoList.add(keyValue);

            keyValue = new WidgetBuilderModel();
            keyValue.setKey(getString(R.string.label_phone_number));
            keyValue.setValue(dataMerchant.getPhoneNumber() == null ? "-" : dataMerchant.getPhoneNumber());
            dataTokoList.add(keyValue);

            keyValue = new WidgetBuilderModel();
            keyValue.setKey(getString(R.string.label_email));
            keyValue.setValue("-");
            dataTokoList.add(keyValue);

            keyValue = new WidgetBuilderModel();
            keyValue.setKey(getString(R.string.label_business_address));
            keyValue.setValue(dataMerchant.getMerchantAddress() == null ? "-" : dataMerchant.getMerchantAddress());
            dataTokoList.add(keyValue);

            displayDataToko(dataTokoList);
        }
    }

    private void displayDataToko(List<WidgetBuilderModel> dataTokoList) {
        PpobPaymentDetailAdapter adapter = new PpobPaymentDetailAdapter(this, dataTokoList);
        adapter.setOrientation(1);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void moveToRegisterActivity() {
//        Intent intent = new Intent(this, RegisterFromsalesParentActivity.class);
//        intent.putExtra("merchantData", getIntent().getStringExtra("merchantData"));
//        startActivity(intent);

        Intent intent = new Intent(this, ActivationInputPinActivity.class);
        //intent.putExtra("merchantData", getIntent().getStringExtra("merchantData"));
        //intent.putExtra(ActivationConfirmationActivity.KEY_PHONE_DATA, mPhone);
        intent.putExtra(ActivationInputPinActivity.KEY_DATA_TYPE, ActivationInputPinActivity.KEY_TYPE_RESET_PIN);
//        intent.putExtra(ActivationInputPinActivity.KEY_HEADER_MESSAGE, "Masukan PIN \n" +
//                "PIN ini akan digunakan untuk seluruh aktivitas pada aplikasi OttoPay dan berlaku juga sebagai PIN OttoCash kamu.");
        startActivityForResult(intent, KEY_CODE_INPUT_PIN);
    }

    private void gotoSNK() {
        Intent intent = new Intent(this, ActivationConfirmationActivity.class);
        intent.putExtra(ActivationConfirmationActivity.KEY_PHONE_DATA, dataMerchant.getPhoneNumber());
        startActivityForResult(intent, KEY_TAC_OTTOPAY);
    }

    private void gotoOtp() {
        Intent intent = new Intent(this, OtpVerificationActivity.class);
        intent.putExtra(OtpVerificationActivity.KEY_DATA_PHONE, mPhone);
        startActivityForResult(intent, KEY_CODE_INPUT_OTP);
    }

    private void gotoTakePhoto() {
        Intent intent = new Intent(this, RegisterPhotoMerchantActivity.class);
        intent.putExtra("merchantData", getIntent().getStringExtra("merchantData"));
        startActivity(intent);
    }

    private void callRegisterAPI(SignupRequestModel signupRequestModel) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.text_loading), false).show();

        AuthDao auth = new AuthDao(this);
        auth.signUpDao(signupRequestModel, BaseDao.getInstance(this, 444).callback);
    }

    private void callRegisterOtpApi(RegisterOtpRequestModel model) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.text_loading), false).show();

        AuthDao auth = new AuthDao(this);
        auth.signUpOtpDao(model, BaseDao.getInstance(this, KEY_API_SIGN_UP_OTP).callback);
    }

    private void getMerchantTheme() {
        new AuthDao(this).merchantTheme(BaseDao.getInstance(this, KEY_API_MERCHANT_THEME).callback);
    }

    private void getFeatureProduct() {
        new AuthDao(this).featureProduct(BaseDao.getInstance(this, KEY_API_FEATURE_MERCHANT).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (responseCode != KEY_API_SIGN_UP_OTP && responseCode != KEY_API_MERCHANT_THEME) {
            ProgressDialogComponent.dismissProgressDialog(this);
        }

        if (response.isSuccessful()) {
            switch (responseCode) {
                case 444: {
                    if (((SignupResponseModel) br).getMeta().getCode() == 200) {
                        uid = ((SignupResponseModel) br).getUser_id();
                        if (((SignupResponseModel) br).getPhone() != null)
                            gotoOtp();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((SignupResponseModel) br).getMeta().getMessage(), ((SignupResponseModel) br).getMeta().getMessage());
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                RegisterFromsalesConfirmActivity.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    break;
                }
                case KEY_API_SIGN_UP_OTP: {
                    if (((SignupOtpResponseModel) br).getMeta().getCode() == 200) {
                        AuthDataModel model = ((SignupOtpResponseModel) br).getData();
                        SessionManager.setPrefLogin(model);
                        SessionManager.createLoginSession(model.getUser_id(), model.getWallet_id(), model.getAccess_token(), this.model.getPin(), model);
                        getMerchantTheme();
//                        startActivity(new Intent(this, DashboardActivity.class));
//                        finish();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((SignupOtpResponseModel) br).getMeta().getMessage(), ((SignupOtpResponseModel) br).getMeta().getMessage());
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                RegisterFromsalesConfirmActivity.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    break;
                }
                case KEY_API_MERCHANT_THEME: {
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        SessionManager.setMerchantTheme(((MerchantThemeResponse) br).getData());
                        getFeatureProduct();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, br.getBaseMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                }
                case KEY_API_FEATURE_MERCHANT: {
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        SessionManager.setFeatureProduct(((FeatureProductResponse) br).getData());

                        startActivity(new Intent(this, DashboardActivity.class));
                        finish();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, br.getBaseMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
