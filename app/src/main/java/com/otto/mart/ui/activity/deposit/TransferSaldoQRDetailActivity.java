package com.otto.mart.ui.activity.deposit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.model.APIModel.Misc.WalletType;
import com.otto.mart.model.APIModel.Request.TfWalletConfRequestModel;
import com.otto.mart.model.APIModel.Request.TfWalletInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.WalletTypeResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.TfWalletInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.activity.transaction.PayQRDetailActivity;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.PayConfirmationDialog;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class TransferSaldoQRDetailActivity extends AppActivity {

    private ViewGroup saldoContainer, backButton;
    private LazyTextview saldoOwner, saldo, phone;
    private ImageView imgv_plus, imgv_minus;
    private EditText amount;
    private int modifier = 100;
    private String walletCode;
    private int walletId;
    private TextView walletTypeName, action;
    private ImageView walletLogo;
    private ViewGroup walletContainer;
    private PayConfirmationDialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_saldo_qrdetail);

        initComponent();
        initContent();
    }

    private void initComponent() {
        saldoContainer = findViewById(R.id.saldoContainer);
        backButton = findViewById(R.id.backhitbox);
        saldo = findViewById(R.id.saldo);
        saldoOwner = findViewById(R.id.saldoOwner);
        imgv_plus = findViewById(R.id.plus);
        imgv_minus = findViewById(R.id.minus);
        amount = findViewById(R.id.guk);
        phone = findViewById(R.id.phone);
        walletTypeName = findViewById(R.id.walletTypeName);
        walletContainer = findViewById(R.id.walletContainer);
        action = findViewById(R.id.nominalAction);
        walletLogo = findViewById(R.id.walletLogo);
    }

    private void initContent() {
        new WalletDao(this).getWalletType(BaseDao.getInstance(this, 231).callback);
        String phoneNum = getIntent().getStringExtra("phone");
        if (phoneNum != null)
            phone.setText(phoneNum);

        View.OnClickListener plusMinusListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.minus:
                        long min = DataUtil.convertLongFromCurrency(amount.getText().toString());
                        if (min - modifier > -1) {
                            amount.setText(String.valueOf(min - modifier));
                        }
                        break;

                    case R.id.plus: {
                        long max = DataUtil.convertLongFromCurrency(amount.getText().toString());
                        amount.setText(String.valueOf(max + modifier));
                        break;
                    }
                }
            }
        };
        imgv_plus.setOnClickListener(plusMinusListener);
        imgv_minus.setOnClickListener(plusMinusListener);

        if (getIntent().hasExtra("dompet")) {
            WalletDataModel model = getIntent().getParcelableExtra("dompet");
            saldo.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(model.getBalance())));
            saldoOwner.setText(model.getOwner_name());
            saldoOwner.setTitle(DataUtil.getXXXPhone(model.getAccount_number()));
        }
        saldoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransferSaldoQRDetailActivity.this, WalletListActivity.class));
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        walletContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TransferSaldoQRDetailActivity.this, WalletTypeListActivity.class), 920);
            }
        });
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWalletInquiryAPI();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new WalletDao(this).emoneySummary(BaseDao.getInstance(this, 970).callback);
    }

    private void callWalletInquiryAPI() {
        if (!walletValidation()) return;
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();

        TfWalletInquiryRequestModel model = new TfWalletInquiryRequestModel();
        model.setAmount(DataUtil.convertLongFromCurrency(amount.getText().toString()));
        model.setCustomer_reference(phone.getTextContent());
        model.setDescription("test dev");
        model.setWallet_channel_code(walletCode);
        new WalletDao(this).getTfWalletInquiry(model, BaseDao.getInstance(this, 203).callback);
    }

    private void callWalletConfirmationAPI(String phone, long amount, String pincode, int walletId, String desc) {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
        TfWalletConfRequestModel model = new TfWalletConfRequestModel();
        model.setAmount(amount);
        model.setCustomer_reference(phone);
        model.setWallet_id(walletId);
        model.setDescription(desc);
        model.setPin(pincode);
        model.setWallet_channel_code(walletCode);
        new WalletDao(this).getTfWalletConf(model, BaseDao.getInstance(this, 435).callback);
    }

    private boolean walletValidation() {
        if (walletCode != null && walletCode.equals("")) {
            Toast.makeText(this, "Pilih dompet tujuan", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(amount.getText().toString())) {
            amount.setError("Nominal tidak boleh kosong");
            return false;
        } else amount.setError(null);
        return true;
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        int baseRes = ((BaseResponseModel) br).getMeta().getCode();
        if (baseRes == 200) {
            switch (responseCode) {
                case 970: {
                    WalletResponseModel res = (WalletResponseModel) br;
                    if (res != null) {
                        WalletDataModel model = res.getData().get(0);
                        this.walletId = model.getId();
                        saldo.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(model.getBalance())));
                        saldoOwner.setText(model.getOwner_name());
                        saldoOwner.setTitle(DataUtil.getXXXPhone(model.getAccount_number()));
                    }
                    break;
                }
                case 231: {
                    WalletTypeResponseModel walletTypeResponse = (WalletTypeResponseModel) br;
                    List<WalletType> walletTypes = walletTypeResponse.getData().getWallet_types();
                    if (walletTypes != null) {
                        walletCode = walletTypes.get(0).getCode();
                        walletTypeName.setText(walletTypes.get(0).getName());
                        Glide.with(walletLogo.getContext()).load(walletTypes.get(0).getLogo_url()).into(walletLogo);
                    }
                    break;
                }
                case 203: {
                    TfWalletInquiryResponseModel inquiryWallet = ((TfWalletInquiryResponseModel) br);
                    confirmDialog = new PayConfirmationDialog(this, this, false, false);

                    confirmDialog.setData(inquiryWallet.getData().getReceiverName(), DataUtil.convertCurrency(inquiryWallet.getData().getAmount()), DataUtil.convertCurrency(0));
                    confirmDialog.setListener(new PayConfirmationDialog.payConfirmDialogListener() {
                        @Override
                        public void actionPressed() {
                            Intent intent = new Intent(TransferSaldoQRDetailActivity.this, RegisterPinResetActivity.class);
                            intent.putExtra("confirm", true);
                            startActivityForResult(intent, 324);
                            confirmDialog.dismiss();
//                                callBankConfirmationAPI(bankAccNum.getText().toString(), DataUtil.convertLongFromCurrency(amount.getText().toString()), bankCode, null, model2.getRecieverName(), walletId, "10");
                        }

                        @Override
                        public void onDialogDismiss() {

                        }
                    });
                    confirmDialog.setTitle("Konfirmasi Transfer");
                    confirmDialog.show();
                    break;
                }
                case 435: {
                    BasePaymentResponseModel PaymentModel = (BasePaymentResponseModel) br;
                    if (PaymentModel.getMeta().getCode() == 200) {
                        Intent intent = new Intent(this, PayQRDetailActivity.class);
                        intent.putExtra("data", (ArrayList<? extends Parcelable>) PaymentModel.getData().getKeyValueList());
                        startActivity(intent);
                    } else if (PaymentModel.getMeta().getCode() == 400) {
                        Toast.makeText(this, PaymentModel.getMeta().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        } else if ((baseRes == 400 && responseCode == 203)) {
            Toast.makeText(this, "Nomor tidak valid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 920) {
                WalletType walletType = null;
                try {
                    walletType = data.getParcelableExtra("walletTypeData");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (walletType != null) {
                    walletCode = walletType.getCode();
                    walletTypeName.setText(walletType.getName());
                    Glide.with(walletLogo.getContext()).load(walletType.getLogo_url()).into(walletLogo);
                }
            } else if (requestCode == 324) {
                if (data.hasExtra("pincode")) {
                    callWalletConfirmationAPI(phone.getTextContent(),
                            DataUtil.convertLongFromCurrency(amount.getText().toString()),
                            data.getStringExtra("pincode"),
                            walletId,
                            "Transfer Uang Dari wallet");
                }
            }
        }
    }
}
