package com.otto.mart.ui.activity.transaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.PaymentData;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Request.payment.IndomaretQrPurchaseRequestModel;
import com.otto.mart.model.APIModel.Request.payment.IndomaretQrStatusRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.payment.IndomaretQrPurchaseResponseModel;
import com.otto.mart.model.localmodel.ppob.PpobPayment;
import com.otto.mart.model.localmodel.ppob.PpobProductType;
import com.otto.mart.presenter.dao.TransactionDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.support.util.pref.PrefConstance;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.concurrent.TimeUnit;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class IndomaretQRPaymentActivity extends AppActivity {

    public static final int RC_INDOMARET_QR_PURCHASE = 2010;
    public static final int RC_INDOMARET_QR_PURCHASE_CANCEL = 2011;
    public static final int RC_INDOMARET_QR_PURCHASE_STATUS = 2012;
    public static final int RC_INDOMARET_QR_PURCHASE_STATUS_AND_CANCEL = 2013;
    public static final String RC_INDOMARET_QR_PURCHASE_SUCCESS_STATUS = "terbayar";

    private final String KEY_INDOMARET_PRODUCT_CODE = "OTTOPAY";

    private ViewAnimator viewAnimator;
    private LinearLayout btnToolbarBack;
    private ImageView imgToolbarLeft;
    private TextView tvToolbarTitle;
    private LinearLayout contentLayout;
    private ImageView imgClose;
    private TextView tvTimer;
    private TextView tvPhone;
    private TextView btnCancel;
    private TextView tv_merchant;
    private TextView tv_address;
    private TextView tv_id;

    private CountDownTimer mTimer;
    private boolean isPaymentCanceled = false;
    private String mReferenceNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indomaret_qrpayment);

        initView();
        callIndomaretQrPurchase();
    }

    @Override
    public void onBackPressed() {
        callIndomaretQrPurchaseCancel();
    }

    private void initView() {
        viewAnimator = findViewById(R.id.view_animator);
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        imgToolbarLeft = findViewById(R.id.imgToolbarLeft);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        contentLayout = findViewById(R.id.content_layout);
        tvTimer = findViewById(R.id.tv_timeer);

        tvPhone = findViewById(R.id.tv_phone);
        btnCancel = findViewById(R.id.btn_cancel);

        tv_address = findViewById(R.id.tv_address);
        tv_merchant = findViewById(R.id.tv_merch);
        tv_id = findViewById(R.id.tv_id);

        imgToolbarLeft.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close));

        btnToolbarBack.setOnClickListener(v -> callIndomaretQrPurchaseStatus(mReferenceNumber, true));

        btnCancel.setOnClickListener(v -> {
            mTimer.cancel();
            callIndomaretQrPurchaseStatus(mReferenceNumber, true);
        });
    }

    private void displayContent() {
        tvToolbarTitle.setText("Indomaret");
        tvTimer.setText("00:00");
        tvPhone.setText(SessionManager.getPhone() + "");

        tv_merchant.setText(Pref.getPreference().getString("t59"));
        tv_address.setText(Pref.getPreference().getString("t60"));
        tv_id.setText(Pref.getPreference().getString("t62"));
    }

    private void callIndomaretQrPurchase() {
        IndomaretQrPurchaseRequestModel indomaretQrPurchaseRequestModel = new IndomaretQrPurchaseRequestModel();
        indomaretQrPurchaseRequestModel.setStore_id(Pref.getPreference().getString(PrefConstance.STORE_ID));
        indomaretQrPurchaseRequestModel.setProduct_code(KEY_INDOMARET_PRODUCT_CODE);

        new TransactionDao(this).getIndomaretQrPurchase(indomaretQrPurchaseRequestModel, BaseDao.getInstance(this, RC_INDOMARET_QR_PURCHASE).callback);
    }

    private void callIndomaretQrPurchaseCancel() {
        IndomaretQrPurchaseRequestModel indomaretQrPurchaseRequestModel = new IndomaretQrPurchaseRequestModel();
        indomaretQrPurchaseRequestModel.setStore_id(Pref.getPreference().getString(PrefConstance.STORE_ID));
        indomaretQrPurchaseRequestModel.setProduct_code(KEY_INDOMARET_PRODUCT_CODE);
        indomaretQrPurchaseRequestModel.setReference_number(mReferenceNumber);

        new TransactionDao(this).getIndomaretQrPurchaseCancel(indomaretQrPurchaseRequestModel, BaseDao.getInstance(this, RC_INDOMARET_QR_PURCHASE_CANCEL).callback);
    }

    private void callIndomaretQrPurchaseStatus(String referenceNumber, boolean isLastTime) {
        IndomaretQrStatusRequestModel indomaretQrStatusRequestModel = new IndomaretQrStatusRequestModel();
        indomaretQrStatusRequestModel.setReference_number(referenceNumber);

        if (isLastTime) {
            new TransactionDao(this).getIndomaretQrPurchaseStatus(indomaretQrStatusRequestModel, BaseDao.getInstance(this, RC_INDOMARET_QR_PURCHASE_STATUS_AND_CANCEL).callback);
        } else {
            new TransactionDao(this).getIndomaretQrPurchaseStatus(indomaretQrStatusRequestModel, BaseDao.getInstance(this, RC_INDOMARET_QR_PURCHASE_STATUS).callback);
        }
    }

    public void purchaseSuccess() {
        mTimer = new CountDownTimer(180000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("" + String.format("%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                try {
                    callIndomaretQrPurchaseStatus(mReferenceNumber, true);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        viewAnimator.setDisplayedChild(1);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case RC_INDOMARET_QR_PURCHASE:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        displayContent();
                        purchaseSuccess();
                        mReferenceNumber = ((IndomaretQrPurchaseResponseModel) br).getData().getReference_number();
                        callIndomaretQrPurchaseStatus(((IndomaretQrPurchaseResponseModel) br).getData().getReference_number(), false);
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                case RC_INDOMARET_QR_PURCHASE_CANCEL:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        isPaymentCanceled = true;
                        finish();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                        dialog.show();
                    }
                    break;
                case RC_INDOMARET_QR_PURCHASE_STATUS:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        for (WidgetBuilderModel widgetBuilderModel : ((BasePaymentResponseModel) br).getData().getKeyValueList()) {
                            if (widgetBuilderModel.getKey().equalsIgnoreCase("status")) {
                                if (widgetBuilderModel.getValue().equalsIgnoreCase(RC_INDOMARET_QR_PURCHASE_SUCCESS_STATUS)) {
                                    Intent intent = new Intent(this, DashboardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    //Goto Success Page
                                    PpobProductType ppobPaymentType = new PpobProductType();
                                    ppobPaymentType.setType(PpobProductType.QR_PAYMENT);

                                    PpobPayment ppobPayment = new PpobPayment();
                                    ppobPayment.setProductName("Pembayaran Indomaret");
                                    ppobPayment.setPpobProductType(ppobPaymentType);
                                    ppobPayment.setTotalPayment(0.0);

                                    PpobOttoagPaymentResponseModel ppobOttoagPaymentResponseModel = new PpobOttoagPaymentResponseModel();
                                    PaymentData data = new PaymentData();
                                    data.setKeyValueList(((BasePaymentResponseModel) br).getData().getKeyValueList());
                                    ppobOttoagPaymentResponseModel.setData(data);

                                    Intent intentSuccess = new Intent(this, PpobPaymentSuccessActivity.class);
                                    intentSuccess.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment);
                                    intentSuccess.putExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA, new Gson().toJson(ppobOttoagPaymentResponseModel));
                                    startActivity(intentSuccess);
                                } else {
                                    if (!isPaymentCanceled) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(() -> callIndomaretQrPurchaseStatus(mReferenceNumber, false), 5000);
                                    }
                                }
                            }
                        }
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                callIndomaretQrPurchaseCancel();
                            }
                        });
                        dialog.show();
                    }
                    break;
                case RC_INDOMARET_QR_PURCHASE_STATUS_AND_CANCEL:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        callIndomaretQrPurchaseCancel();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                callIndomaretQrPurchaseCancel();
                            }
                        });
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

