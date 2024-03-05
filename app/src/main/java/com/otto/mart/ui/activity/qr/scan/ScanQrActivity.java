package com.otto.mart.ui.activity.qr.scan;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.model.APIModel.Request.PayQrInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.PaymentOfflineConfirmationRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.PayQrInquiryResponse;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.presenter.dao.TransactionDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.EmvcoUtil;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.support.util.pref.PrefConstance;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.bogasari.catalog.CatalogBogasariActivity;
import com.otto.mart.ui.activity.qr.payment.QrPaymentOttoKonekActivity;
import com.otto.mart.ui.activity.qr.show.ShowQrActivity;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.activity.transaction.IndomaretQRPaymentActivity;
import com.otto.mart.ui.activity.transaction.PayQRDetailActivity;
import com.otto.mart.ui.activity.transaction.ProcessQRPaymentActivity;
import com.otto.mart.ui.activity.transaction.alfamart.AlfamartShowPaymentCodeActivity;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.template.CashInputKeyboard.CashInputKeyboardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Response;

public class ScanQrActivity extends AppActivity {

    private final int RC_PIN_INDOMARET_PURCHASE = 2010;
    private final int RC_PIN_SHOW_QR = 2011;

    private boolean contentInitialized;

    private ZXingScannerView mScannerView;
    private LazyDialog lazyDialog;
    private View backButton;
    private TextView tv3, tv5, tv7;
    private LazyTextview let_name, let_val, ltv_name, ltv_val;
    private LazyEdittext let_cash;
    private String inquiry, tokoname, val;
    private int selectedWalletId;
    private View confirm;
    private int paybleAmount;
    private LinearLayout btnToken;
    private LinearLayout tabScanQr;
    private TextView tvTabScanQr;
    private LinearLayout tabQrCode;
    private LinearLayout tabToken;
    private boolean isManualInput;
    private CashInputKeyboardView cikv;

    private String mQRContent = "";

    private boolean isOttoCash = false;
    private boolean isfromFile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        disableScreenshot();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_pay_qr);

        if (getIntent().hasExtra(AlfamartShowPaymentCodeActivity.KEY_IS_OTTOCASH)) {
            isOttoCash = getIntent().getBooleanExtra(AlfamartShowPaymentCodeActivity.KEY_IS_OTTOCASH, false);
        }

        initPermissions();
        try {
            initComponent();
            initContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponent() {
        mScannerView = findViewById(R.id.cam_view);
        backButton = findViewById(R.id.backhitbox);
        lazyDialog = new LazyDialog(this, this, false);
        lazyDialog.setContainerView(LayoutInflater.from(this).inflate(R.layout.dialog_pay_qr, null));
        lazyDialog.setToolbarDarkmode();
        ((TextView) lazyDialog.getToolbarView().findViewById(R.id.title)).setText("Payment");
        (lazyDialog.getWindow()).setGravity(Gravity.BOTTOM);
        lazyDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        lazyDialog.getWindow().setGravity(Gravity.BOTTOM);

        tv3 = lazyDialog.getContent().findViewById(R.id.textView3);
        tv5 = lazyDialog.getContent().findViewById(R.id.textView5);
        tv7 = lazyDialog.getContent().findViewById(R.id.textView7);
        ltv_name = lazyDialog.getContent().findViewById(R.id.ltv_shopname);
        ltv_val = lazyDialog.getContent().findViewById(R.id.ltv_val);
        ltv_name.setVisibility(View.GONE);
        ltv_val.setVisibility(View.GONE);
        let_cash = lazyDialog.getContent().findViewById(R.id.let_cash);
        confirm = lazyDialog.getContent().findViewById(R.id.confirm);
        btnToken = findViewById(R.id.btnToken);
        tabScanQr = findViewById(R.id.tabScanQr);
        tvTabScanQr = findViewById(R.id.tvTabScanQr);
        tabQrCode = findViewById(R.id.tabQrCode);
        tabToken = findViewById(R.id.tabToken);
        cikv = lazyDialog.getContent().findViewById(R.id.cikv);
    }

    private void initContent() {
        contentInitialized = true;

        tvTabScanQr.setTextColor(ContextCompat.getColor(this, R.color.white));
        tabScanQr.setBackground(ContextCompat.getDrawable(this, R.drawable.blue_rounded_12_bg));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //val = ((LazyEdittext) lazyDialog.getContent().findViewById(R.id.let_cash)).getTextContent();

                val = cikv.getTextContent();
                if (val.equals("0")) {
                    val = DataUtil.cleanDigit(ltv_val.getTextContent());
                }

                if (!val.equals("")) {
                    Intent jenk = new Intent(ScanQrActivity.this, RegisterPinResetActivity.class);
                    jenk.putExtra("confirm", true);
                    startActivityForResult(jenk, 111);
                } else {
                    ErrorDialog errorDialog = new ErrorDialog(ScanQrActivity.this, ScanQrActivity.this, false, false, "Jumlah harga tidak valid", "Mohon cek kembali input harga anda");
                    errorDialog.show();
                }
            }
        });

        ((EditText) ((LazyEdittext) lazyDialog.getContent().findViewById(R.id.let_cash)).getComponent()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2) {
                    (lazyDialog.getContent().findViewById(R.id.confirm)).setVisibility(View.VISIBLE);
                } else {
                    (lazyDialog.getContent().findViewById(R.id.confirm)).setVisibility(View.GONE);
                }
            }
        });

        btnToken.setOnClickListener(v -> {
            Intent jenk = new Intent(ScanQrActivity.this, RegisterPinResetActivity.class);
            jenk.putExtra("confirm", true);
            startActivityForResult(jenk, RC_PIN_SHOW_QR);
        });

        tabQrCode.setOnClickListener(v -> {
            startActivity(new Intent(this, ShowQrActivity.class));
            finish();
        });

        mScannerView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        mScannerView.setResultHandler(scannerHandler);
        getOmzetStatApi();
    }

    private void getOmzetStatApi() {
       //  new WalletDao(this).emoneySummary(BaseDao.getInstance(this, 999).callback);
    }

    private void initPermissions() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                initComponent();
                initContent();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                finish();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.cancelPermissionRequest();
                finish();
            }
        }).check();
    }

    ZXingScannerView.ResultHandler scannerHandler = new ZXingScannerView.ResultHandler() {
        boolean isReady = false;

        @Override
        public void handleResult(Result result) {
            paybleAmount = 0;
            mScannerView.resumeCameraPreview(this);

            switch (EmvcoUtil.parseEMVCOtag01(result.toString())) {
                case 11:
                    showLoading("Mohon tunggu");
                    isManualInput = true;
                    isReady = true;
                    break;
                case 12:
                    showLoading("Mohon tunggu");
                    isManualInput = false;
                    isReady = true;
                    //   paybleAmount = Integer.parseInt(EmvcoUtil.parseEMVCOtag54(result.toString()));
                    break;
                default:
                    showLoading("Mohon tunggu");
                    isReady = true;
                    isManualInput = true;
                    break;
            }

            if (isReady) {
                mScannerView.stopCamera();

                ProgressDialogComponent.dismissProgressDialog(ScanQrActivity.this);
                String qrContent = EmvcoUtil.parseEMVCOtag62(result.getText());
                List<String> qrContentSeparated = Arrays.asList(qrContent.split("\\s*;;;;\\s*"));

                if (qrContentSeparated.size() > 0) {
                    if (qrContentSeparated.get(0).equalsIgnoreCase("IDM")) {
                        Pref.getPreference().putString(PrefConstance.STORE_ID, qrContentSeparated.get(1));
                        Pref.getPreference().putString("t59", EmvcoUtil.parseEMVCOtagByNum(59, result.getText()));
                        Pref.getPreference().putString("t60", EmvcoUtil.parseEMVCOtagByNum(60, result.getText()));
                        Pref.getPreference().putString("t62", EmvcoUtil.parseEMVCOtag62(result.getText()));

                        Intent jenk = new Intent(ScanQrActivity.this, RegisterPinResetActivity.class);
                        jenk.putExtra("confirm", true);
                        startActivityForResult(jenk, RC_PIN_INDOMARET_PURCHASE);
                    } else if (qrContentSeparated.size() > 1 && qrContentSeparated.get(1).contains("BG")) {
                        Intent intent = new Intent(ScanQrActivity.this, CatalogBogasariActivity.class);
                        intent.putExtra("qrContent", result.getText());
                        intent.putExtra("merchantId", qrContent.split(";;;;")[1]);
                        startActivity(intent);
                        finish();
                    } else {
                        mQRContent = result.getText();
                        gotoProcessQRPayment(result.getText());
                    }
                } else {
                    mQRContent = result.getText();
                    gotoProcessQRPayment(result.getText());
                }
            }
        }
    };

    private void gotoProcessQRPayment(String result) {
        //Intent intent = new Intent(this, QrPaymentActivity.class);
        Intent intent = new Intent(this, QrPaymentOttoKonekActivity.class);
        intent.putExtra(ProcessQRPaymentActivity.KEY_QR_CONTENT, result);
        startActivity(intent);
    }

    private void showLoading(String message) {
        if (!ScanQrActivity.this.isFinishing()) {
            ProgressDialogComponent.showProgressDialog(ScanQrActivity.this, message, false).show();
        }
    }

    public void qrPaymentCodeFragmentDismiss() {
        mScannerView.startCamera();
    }

    private void callOfflineQrInquiryApi(PayQrInquiryRequestModel requestModel) {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
        TransactionDao dao = new TransactionDao(ScanQrActivity.this);
        ProgressDialogComponent.showProgressDialog(this, "Mohon Menunggu", false).show();
        dao.payQrInquiry(requestModel, BaseDao.getInstance(this, 333).callback);
    }

    private void callOfflineQrPaymentApi(PaymentOfflineConfirmationRequestModel requestModel) {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
        TransactionDao dao = new TransactionDao(this);
        dao.payQrPayment(requestModel, BaseDao.getInstance(this, 334).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case 333:
                    //New API Otttofin
                    if (((PayQrInquiryResponse) br).getMeta().getCode() == 200) {
                        inquiry = ((PayQrInquiryResponse) br).getData().getInquiryData().getRrn();
                        tokoname = ((PayQrInquiryResponse) br).getData().getInquiryData().getMerchant_name();
                        ltv_name.setText(tokoname);
                        ltv_name.setVisibility(View.VISIBLE);

                        paybleAmount = ((PayQrInquiryResponse) br).getData().getInquiryData().getAmount();

                        if (paybleAmount != 0) {
                            ltv_val.setText(DataUtil.convertCurrency(paybleAmount));
                            ltv_val.setVisibility(View.VISIBLE);
                            let_cash.setVisibility(View.GONE);
                            cikv.setVisibility(View.GONE);
                        } else if (paybleAmount > 0) {
                            ltv_val.setText(DataUtil.convertCurrency(paybleAmount));
                            ltv_val.setVisibility(View.VISIBLE);
                            let_cash.setVisibility(View.GONE);
                            cikv.setVisibility(View.GONE);
                        } else {
                            ltv_val.setVisibility(View.GONE);
                            let_cash.setVisibility(View.GONE);
                            cikv.setVisibility(View.VISIBLE);
                        }

                        lazyDialog.show();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(ScanQrActivity.this, ScanQrActivity.this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                case 334:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        Intent sucessIntent = new Intent(this, PayQRDetailActivity.class);
                        sucessIntent.putExtra("data", (ArrayList<? extends Parcelable>) ((BasePaymentResponseModel) br).getData().getKeyValueList());
                        startActivity(sucessIntent);
                        finish();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(ScanQrActivity.this, ScanQrActivity.this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                case 999:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        List<WalletDataModel> jenk = ((WalletResponseModel) br).getData();
                        tv7.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency((jenk.get(0).getBalance()))));
                        try {
                            selectedWalletId = jenk.get(0).getId();
                            String name = jenk.get(0).getOwner_name();
                            String phone = jenk.get(0).getAccount_number();
                            tv5.setText(name);
                            tv3.setText(phone);
                        } catch (NullPointerException e) {
                            Log.e("", "onApiResponseCallback: " + e.getMessage());
                        }
                    } else {
                        Toast.makeText(this, "Terjadi kesalahan pada server : " + ((WalletResponseModel) br).getMeta().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        Toast.makeText(ac, "Terjadi kesalahan pada server " + message, Toast.LENGTH_SHORT).show();
        mScannerView.setResultHandler(scannerHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        disableScreenshot();
        if (contentInitialized) {
            mScannerView.startCamera();
            mScannerView.setResultHandler(scannerHandler);
            cikv.setText("0");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (contentInitialized) {
            mScannerView.stopCamera();
        }
    }

    @Override
    protected void onStop() {
        if (lazyDialog != null)
            lazyDialog.dismiss();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 111:
                    PaymentOfflineConfirmationRequestModel requestModel = new PaymentOfflineConfirmationRequestModel();
                    requestModel.setPin(data.getStringExtra("pincode"));
                    requestModel.setRrn(inquiry);
                    if (cikv.getVisibility() == View.VISIBLE) {
                        requestModel.setAmount(DataUtil.getInt(cikv.getTextContent()));
                    } else {
                        requestModel.setAmount(paybleAmount);
                    }
                    requestModel.setWallet_id(selectedWalletId);

                    //Set Merchant ID (MVCO Tag 62)
                    String tag62 = EmvcoUtil.parseEMVCOtag62(mQRContent);
                    List<String> merchantIdSeparated = Arrays.asList(tag62.split("\\s*;;;;\\s*"));
                    if (merchantIdSeparated.size() > 1) {
                        requestModel.setMerchant_id(merchantIdSeparated.get(1));
                    }

                    callOfflineQrPaymentApi(requestModel);
                    break;
                case RC_PIN_INDOMARET_PURCHASE:
                    startActivity(new Intent(this, IndomaretQRPaymentActivity.class));
                    break;
                case RC_PIN_SHOW_QR:
                    Intent intent = new Intent(ScanQrActivity.this, AlfamartShowPaymentCodeActivity.class);
                    intent.putExtra(AlfamartShowPaymentCodeActivity.KEY_IS_OTTOCASH, isOttoCash);
                    startActivity(intent);

                    finish();
                    break;
                default:
                    break;
            }
        } else {
            mScannerView.startCamera();
            mScannerView.setResultHandler(scannerHandler);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mScannerView.stopCamera();
        mScannerView.setResultHandler(null);
        super.startActivityForResult(intent, requestCode);
    }
}
