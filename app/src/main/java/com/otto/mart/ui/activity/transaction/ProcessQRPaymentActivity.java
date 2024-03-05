package com.otto.mart.ui.activity.transaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.model.APIModel.Request.PayQrInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.PaymentOfflineConfirmationRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.PayQrInquiryResponse;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.presenter.dao.TransactionDao;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.EmvcoUtil;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.template.CashInputKeyboard.CashInputKeyboardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class ProcessQRPaymentActivity extends BaseActivity {

    public static final String KEY_QR_CONTENT = "qr_content";

    private final int RC_PIN_COFIRMATION = 111;

    //private LazyDialog lazyDialog;
    private View backButton;
    private ViewAnimator viewAnimator;
    private ImageView imgClose;
    private TextView tvTitle;
    private TextView tv3, tv5, tv7;
    private LazyTextview let_name, let_val, ltv_name, ltv_val;
    private LazyEdittext let_cash;
    private String inquiry, tokoname, val;
    private int selectedWalletId;
    private View confirm;
    private int paybleAmount;
    private ImageView imgOttopayCode;
    private boolean isManualInput;
    private CashInputKeyboardView cikv;

    String mQRContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_qrpayment);

        if (getIntent().hasExtra(KEY_QR_CONTENT)) {
            mQRContent = getIntent().getStringExtra(KEY_QR_CONTENT);
        }

        initComponent();
        initContent();

        if (!mQRContent.equals("")) {
            PayQrInquiryRequestModel requestModel = new PayQrInquiryRequestModel();
            requestModel.setQrData(mQRContent);
            callOfflineQrInquiryApi(requestModel);
        }
    }

    private void initComponent() {

        viewAnimator = findViewById(R.id.view_animator);
        imgClose = findViewById(R.id.img_close);
        tvTitle = findViewById(R.id.tv_title);
        tv3 = findViewById(R.id.textView3);
        tv5 = findViewById(R.id.textView5);
        tv7 = findViewById(R.id.textView7);
        ltv_name = findViewById(R.id.ltv_shopname);
        ltv_val = findViewById(R.id.ltv_val);
        ltv_name.setVisibility(View.GONE);
        ltv_val.setVisibility(View.GONE);
        let_cash = findViewById(R.id.let_cash);
        confirm = findViewById(R.id.confirm);
        imgOttopayCode = findViewById(R.id.img_ottopay_code);
        cikv = findViewById(R.id.cikv);
    }

    private void initContent() {
        tvTitle.setText("Payment");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //val = ((LazyEdittext) lazyDialog.getContent().findViewById(R.id.let_cash)).getTextContent();

                val = cikv.getTextContent();
                if (val.equals("0")) {
                    val = DataUtil.cleanDigit(ltv_val.getTextContent());
                }

                if (!val.equals("")) {
                    Intent jenk = new Intent(ProcessQRPaymentActivity.this, RegisterPinResetActivity.class);
                    jenk.putExtra("confirm", true);
                    startActivityForResult(jenk, RC_PIN_COFIRMATION);
                } else {
                    ErrorDialog errorDialog = new ErrorDialog(ProcessQRPaymentActivity.this, ProcessQRPaymentActivity.this, false, false, "Jumlah harga tidak valid", "Mohon cek kembali input harga anda");
                    errorDialog.show();
                }
            }
        });

        ((EditText) ((LazyEdittext) findViewById(R.id.let_cash)).getComponent()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2) {
                    findViewById(R.id.confirm).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.confirm).setVisibility(View.GONE);
                }
            }
        });

        imgClose.setOnClickListener(v -> finish());

        getOmzetStatApi();
    }

    private void getOmzetStatApi() {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
        new WalletDao(this).emoneySummary(BaseDao.getInstance(this, 999).callback);
    }

    private void callOfflineQrInquiryApi(PayQrInquiryRequestModel requestModel) {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
        TransactionDao dao = new TransactionDao(ProcessQRPaymentActivity.this);
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

                        viewAnimator.setDisplayedChild(1);
                    } else {
                        ErrorDialog dialog = new ErrorDialog(ProcessQRPaymentActivity.this, ProcessQRPaymentActivity.this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
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
                        showErrorMessage(((BaseResponseModel) br).getMeta().getMessage(), "");
                    }
                    break;
                case 999:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        List<WalletDataModel> jenk = ((WalletResponseModel) br).getData();
                        tv7.
                                setText(DataUtil.
                                        convertCurrency(DataUtil.convertLongFromCurrency((jenk.get(0).getBalance()))));
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
        //super.onApiFailureCallback(message, ac);
        Toast.makeText(ac, "Terjadi kesalahan pada server " + message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String title, String message){
        ErrorDialog dialog = new ErrorDialog(ProcessQRPaymentActivity.this, ProcessQRPaymentActivity.this, false, false, title, message);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.RESULT_OK) {
            switch (requestCode) {
                case RC_PIN_COFIRMATION:
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
                    if(merchantIdSeparated.size() > 1){
                        requestModel.setMerchant_id(merchantIdSeparated.get(1));
                    }

                    callOfflineQrPaymentApi(requestModel);
                    break;
                default:
                    break;
            }
        } else {
            finish();
        }
    }
}