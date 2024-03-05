package com.otto.mart.ui.activity.deposit;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.model.APIModel.Misc.WalletType;
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel;
import com.otto.mart.model.APIModel.Request.TfWalletConfRequestModel;
import com.otto.mart.model.APIModel.Request.TfWalletInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.TransferToBankConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.TransferToBankInquiryRequestModel;
import com.otto.mart.model.APIModel.Request.WalletTypeResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.TfWalletInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.TransferBankInquiryResponseModel;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.model.APIModel.Response.bank.BankAccountListResponseModel;
import com.otto.mart.presenter.dao.ProfileDao;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.profile.BankListActivity;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.activity.transaction.PayQRDetailActivity;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.dialog.PayConfirmationDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static android.view.View.GONE;

public class TransferSaldoActivity extends AppActivity {

    private final String KEY_APPROVE = "approve";

    private View icon, backButton;
    private ViewGroup saldoContainer, bankContiner, emoneyContainer, walletContainer;
    private LazyTextview saldoOwner, saldo;
    private int modifier = 100;
    private long val = 0;
    private int defVal = 0;
    private int bankid = -1;
    private String bankCode = null, recieverName = null;
    private EditText amount;
    private ImageView imgv_plus, imgv_minus, arrowIcon, imgvLogo;
    private RadioGroup payType;
    private RadioButton rbBankType;
    private TextView bankName, bankAccNum, bankAccName, tvStatus, action, phoneName, walletTypeName;
    private LazyEdittext phone;
    private ImageView imgContact;
    private TfWalletInquiryResponseModel inquiryWallet;
    private int walletId;
    private String walletCode;
    private ImageView bankLogo, walletLogo;
    private PayConfirmationDialog confirmDialog;
    private List<WalletType> walletTypes;
    private boolean hasBank = false;

    private String mApprovalStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_saldo);
        iniComponent();
        iniContent();
    }

    private void iniComponent() {
        icon = findViewById(R.id.icon_qr);
        saldoContainer = findViewById(R.id.saldoContainer);
        backButton = findViewById(R.id.backhitbox);
        saldo = findViewById(R.id.saldo);
        saldoOwner = findViewById(R.id.saldoOwner);
        amount = findViewById(R.id.guk);
        imgv_plus = findViewById(R.id.plus);
        imgv_minus = findViewById(R.id.minus);
        arrowIcon = findViewById(R.id.iconArroe);
        payType = findViewById(R.id.payTypeGroup);
        rbBankType = findViewById(R.id.bankType);
        bankContiner = findViewById(R.id.bankContainer);
        emoneyContainer = findViewById(R.id.emoneyContainer);
        bankAccName = findViewById(R.id.tv_bankAccName);
        bankAccNum = findViewById(R.id.tv_bankAccNum);
        tvStatus = findViewById(R.id.tv_status);
        bankName = findViewById(R.id.tv_bankName);
        bankLogo = findViewById(R.id.imgv_logo);
        action = findViewById(R.id.nominalAction);
        phone = findViewById(R.id.phone);
        imgContact = findViewById(R.id.img_contact);
        phoneName = findViewById(R.id.name);
        walletContainer = findViewById(R.id.walletContainer);
        walletLogo = findViewById(R.id.walletLogo);
        walletTypeName = findViewById(R.id.walletTypeName);
        arrowIcon.setVisibility(GONE);
        tvStatus.setVisibility(GONE);
    }

    private void iniContent() {
        findViewById(R.id.topLine).setVisibility(GONE);
        findViewById(R.id.tv_rektype).setVisibility(GONE);
        callProfileInfoAPI();

        try {
            ((EditText) ((LazyEdittext) findViewById(R.id.phone)).getComponent()).setInputType(InputType.TYPE_CLASS_PHONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransferSaldoActivity.this, TransferSaldoQRActivity.class);
                if (getIntent().hasExtra("dompet")) {
                    intent.putExtra("dompet", (WalletDataModel) getIntent().getParcelableExtra("dompet"));
                }
                startActivity(intent);
            }
        });
        saldoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(TransferSaldoActivity.this, WalletListActivity.class));
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        payType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.eMoneyType: {
                        resetTransferForm(true);
                        bankContiner.setVisibility(GONE);
                        emoneyContainer.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.bankType: {
                        resetTransferForm(false);
                        emoneyContainer.setVisibility(GONE);
                        if (hasBank)
                            bankContiner.setVisibility(View.VISIBLE);
                        else {
                            ErrorDialog errorDialog = new ErrorDialog(TransferSaldoActivity.this, TransferSaldoActivity.this, false, false, "Anda belum memiliki Bank yang terdaftar", "Mohon daftarkan Bank anda terlebih dahulu");
                            errorDialog.show();
                            findViewById(R.id.eMoneyType).performClick();
                        }
                        break;
                    }
                }
            }
        });
        bankContiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jenk = new Intent(TransferSaldoActivity.this, BankListActivity.class);
                jenk.putExtra("isFromTransfer", true);
                startActivityForResult(jenk, 783);
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent;
//                switch (payType.getCheckedRadioButtonId()) {
//                    case R.id.eMoneyType:
//                        callWalletInquiryAPI();
//                        break;
//                    case R.id.bankType:
//                        if (bankAccNum.getText() != null && amount.getText() != null && bankCode != null)
//                            callBankInquiryAPI(bankAccNum.getText().toString(),
//                                    DataUtil.convertLongFromCurrency(amount.getText().toString()),
//                                    bankCode, "10");
//                        else
//                            Toast.makeText(TransferSaldoActivity.this, "Masih ada field yg belum lengkap\n" +
//                                    "mohon dilengkapi dahulu", Toast.LENGTH_SHORT).show();
//                }
//            }

                if (bankAccNum.getText() != null && amount.getText() != null && bankCode != null) {
                    callBankInquiryAPI(bankAccNum.getText().toString(),
                            DataUtil.convertLongFromCurrency(amount.getText().toString()),
                            bankCode, "10");

//                    if (mApprovalStatus.equalsIgnoreCase(KEY_APPROVE)) {
//                        callBankInquiryAPI(bankAccNum.getText().toString(),
//                                DataUtil.convertLongFromCurrency(amount.getText().toString()),
//                                bankCode, "10");
//                    } else {
//                        ErrorDialog dialog = new ErrorDialog(TransferSaldoActivity.this, TransferSaldoActivity.this, false, false, "No Rekening belum disetujui", "No rekening tujuan belum disetujui sebagai penerima pembayaran ke bank");
//                        dialog.show();
//                    }
                } else {
                    Toast.makeText(TransferSaldoActivity.this, "Masih ada field yg belum lengkap\n" +
                            "mohon dilengkapi dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgContact.setOnClickListener(v -> {
            pickContact();
        });

        walletContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TransferSaldoActivity.this, WalletTypeListActivity.class), 920);
            }
        });

        //Set default Transfer Type = Bank
        setDefaultType();
    }

    private void setDefaultType() {
        rbBankType.setEnabled(true);
        resetTransferForm(false);
        emoneyContainer.setVisibility(GONE);
        bankContiner.setVisibility(View.VISIBLE);
    }

    private void callProfileInfoAPI() {
        new WalletDao(this).getWalletType(BaseDao.getInstance(this, 231).callback);
        new WalletDao(this).emoneySummary(BaseDao.getInstance(this, 980).callback);
        new ProfileDao(this).getBankList(BaseDao.getInstance(this, 321).callback);
    }

    private void addTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            private Timer timer = new Timer();
            private final long DELAY = 3000;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    callWalletInquiryAPI();
                                }
                            },
                            DELAY
                    );

                } else {
                    if (timer != null) {
                        timer.cancel();
                    }
                    phoneName.setText("-");
                }
            }
        };

//        ((EditText) phone.getComponent()).addTextChangedListener(textWatcher);
//        amount.addTextChangedListener(textWatcher);
    }


    //region Pick Contact

    protected void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse(""));
        //        val pickContactIntent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"))
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, AppKeys.KEY_PICK_CONTACT_REQUEST);
    }

    private void validatePhoneNumber(String number) {
        String validNumber = number.replaceAll("\\+|\\-|\\ ", "");

        if (!("0".equals(validNumber.substring(0, 1)))) {
            String tempNumber = validNumber.substring(2);
            validNumber = "0" + tempNumber;
        }

        String[] items = validNumber.split("");
        phone.setContentText("");
        for (int i = 0; i < items.length; i++) {
            String latestPhone = phone.getTextContent().toString() + items[i];
            phone.setContentText(latestPhone);
        }
        EditText etInput = (EditText) phone.getComponent();
        etInput.setSelection(phone.getTextContent().length());
    }

    //endregion


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 783) {
                mApprovalStatus = data.getStringExtra("approval_status");
                bankid = data.getIntExtra("bankId", -1);
                bankCode = data.getStringExtra("bank_code");

                bankName.setText(data.getStringExtra("bankName"));
                bankAccNum.setText(data.getStringExtra("norek"));
                bankAccName.setText(data.getStringExtra("name"));
                tvStatus.setText(mApprovalStatus);

                Glide.with(walletLogo.getContext()).load(data.getStringExtra("bank_logo"))
                        .apply(new RequestOptions().placeholder(R.drawable.bank_placeholder)
                                .override(180, 120))
                        .into(bankLogo);

            } else if (requestCode == 324) {
                if (data.hasExtra("pincode")) {
                    callWalletConfirmationAPI(phone.getTextContent(),
                            DataUtil.convertLongFromCurrency(amount.getText().toString()),
                            data.getStringExtra("pincode"),
                            walletId,
                            "Transfer Uang Dari wallet");
                }
            } else if (requestCode == 325) {
                if (data.hasExtra("pincode")) {
                    if (bankAccNum.getText() != null && amount.getText() != null && !amount.getText().equals("") && bankCode != null)
                        callBankConfirmationAPI(bankAccNum.getText().toString(),
                                DataUtil.convertLongFromCurrency(amount.getText().toString()),
                                bankCode,
                                data.getStringExtra("pincode"),
                                recieverName,
                                walletId, "");
                }
            } else if (requestCode == 920) {
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
            } else if (requestCode == AppKeys.KEY_PICK_CONTACT_REQUEST) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);

                // Do something with the phone number...
                validatePhoneNumber(number);
            }
        }
    }


    //region API Task

    private void callWalletInquiryAPI() {
        if (!walletValidation()) return;
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();

        TfWalletInquiryRequestModel model = new TfWalletInquiryRequestModel();
        model.setAmount(DataUtil.convertLongFromCurrency(amount.getText().toString()));
        model.setCustomer_reference(phone.getTextContent());
        model.setDescription("");
        model.setWallet_channel_code(walletCode);
        new WalletDao(this).getTfWalletInquiry(model, BaseDao.getInstance(this, 203).callback);
    }

    private boolean walletValidation() {
        if (walletCode != null && walletCode.equals("")) {
            Toast.makeText(this, "Pilih dompet tujuan", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phone.getTextContent())) {
            ((EditText) phone.getComponent()).setError("No handphone tidak boleh kosong");
            return false;
        } else ((EditText) phone.getComponent()).setError(null);
        if (TextUtils.isEmpty(amount.getText().toString())) {
            amount.setError("Nominal tidak boleh kosong");
            return false;
        } else amount.setError(null);
        return true;
    }

    private void callWalletConfirmationAPI(String customerRef, long amount, String pin, int walletId, String desc) {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
        TfWalletConfRequestModel model = new TfWalletConfRequestModel();
        model.setAmount(amount);
        model.setCustomer_reference(customerRef);
        model.setWallet_id(walletId);
        model.setDescription(desc);
        model.setPin(pin);
        model.setWallet_channel_code(walletCode);
        new WalletDao(this).getTfWalletConf(model, BaseDao.getInstance(this, 435).callback);
    }

    private void callBankInquiryAPI(String customerRef, long amount, String bankCode, String desc) {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
        TransferToBankInquiryRequestModel requestModel = new TransferToBankInquiryRequestModel();
        requestModel.setAmount(amount);
        requestModel.setBank_code(bankCode);
        requestModel.setCustomer_reference(customerRef);
        requestModel.setDescription(desc);
        new WalletDao(this).getTfBankInquiry(requestModel, BaseDao.getInstance(this, 535).callback);
    }

    private void callBankConfirmationAPI(String customerRef, long amount, String bankCode, String pin, String recieverName, int walletId, String desc) {
        if (confirmDialog != null && confirmDialog.isShowing()) {
            confirmDialog.dismiss();
        }
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
        TransferToBankConfirmationRequestModel requestModel = new TransferToBankConfirmationRequestModel();
        requestModel.setAmount(amount);
        requestModel.setBank_code(bankCode);
        requestModel.setCustomer_reference(customerRef);
        requestModel.setDescription(desc);
        requestModel.setPin(pin);
        requestModel.setReceiver_name(recieverName);
        requestModel.setWallet_id(walletId);

        new WalletDao(this).getTfBankConf(requestModel, BaseDao.getInstance(this, 536).callback);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        BasePaymentResponseModel PaymentModel;
        TransferBankInquiryResponseModel inquiryModel;
        Intent intent;
        if (br != null) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                switch (responseCode) {
                    case 980:
                        WalletResponseModel res = (WalletResponseModel) br;
                        if (res.getData() != null && res.getData().size() > 0) {
                            WalletDataModel model = res.getData().get(0);
                            walletId = model.getId();
                            saldo.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(model.getBalance())));
                            saldoOwner.setText(model.getOwner_name());
                            saldoOwner.setTitle(DataUtil.getXXXPhone(model.getAccount_number()));
                        }
                        break;
                    case 203:
                        hideProgressDialog(false);
                        inquiryWallet = ((TfWalletInquiryResponseModel) br);
                        if (inquiryWallet.getMeta().getCode() == 200) {
                            confirmDialog = new PayConfirmationDialog(this, this, false, false);

                            confirmDialog.setData(inquiryWallet.getData().getReceiverName(), DataUtil.convertCurrency(inquiryWallet.getData().getAmount()), DataUtil.convertCurrency(0));
                            confirmDialog.setListener(new PayConfirmationDialog.payConfirmDialogListener() {
                                @Override
                                public void actionPressed() {
                                    Intent intent = new Intent(TransferSaldoActivity.this, RegisterPinResetActivity.class);
                                    intent.putExtra("confirm", true);
                                    startActivityForResult(intent, 324);
                                    confirmDialog.dismiss();
//                                callBankConfirmationAPI(bankAccNum.getText().toString(), DataUtil.convertLongFromCurrency(amount.getText().toString()), bankCode, null, model2.getRecieverName(), walletId, "10");
                                }

                                @Override
                                public void onDialogDismiss() {

                                }
                            });
                            confirmDialog.setTitle("Konfirmasi Pembayaran");
                            confirmDialog.show();
                        } else if (inquiryWallet.getMeta().getCode() == 400) {
                            Toast.makeText(this, "Nomor tidak valid", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 321:
                        BankAccountListResponseModel model = (BankAccountListResponseModel) br;
                        if (br != null) {
                            if (bankid > 0) {
                                hasBank = true;
                                for (BankAccountModel jenk :
                                        model.getData().getAccount()) {
                                    if (jenk.getId() == bankid) {
                                        mApprovalStatus = jenk.getApprovalStatus();
                                        bankCode = jenk.getBankCode();

                                        bankAccName.setText(jenk.getAccountOwner());
                                        bankAccNum.setText(jenk.getAccountNumber());
                                        bankName.setText(jenk.getBankName());
                                        tvStatus.setText(mApprovalStatus);

                                        Glide.with(this).load(jenk.getBankLogo())
                                                .apply(new RequestOptions().placeholder(R.drawable.bank_placeholder)
                                                        .error(R.drawable.bank_placeholder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(bankLogo);
                                        break;
                                    }
                                }
                            } else {
                                if (model.getData().getAccount() != null && model.getData().getAccount().size() > 0) {
                                    hasBank = true;
                                    mApprovalStatus = model.getData().getAccount().get(0).getApprovalStatus();
                                    bankCode = model.getData().getAccount().get(0).getBankCode();

                                    bankAccName.setText(model.getData().getAccount().get(0).getAccountOwner());
                                    bankAccNum.setText(model.getData().getAccount().get(0).getAccountNumber());
                                    bankName.setText(model.getData().getAccount().get(0).getBankName());
                                    tvStatus.setText(mApprovalStatus);

                                    Glide.with(this).load(model.getData().getAccount().get(0).getBankLogo()).apply(new RequestOptions().placeholder(R.drawable.bank_placeholder)
                                            .error(R.drawable.bank_placeholder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(bankLogo);
                                } else {
                                    hasBank = false;
                                    Toast.makeText(this, "anda belum memiliki bank, mohon daftarkan bank plihan anda terlebih dahulu", Toast.LENGTH_SHORT).show();
                                }
//                    model.getData(model.getData().getAccount().get(0).getBankLogo());
                            }
                        }
                        break;
                    case 435:
                        callProfileInfoAPI();
                        hideProgressDialog(true);
                        resetTransferForm(true);
                        PaymentModel = (BasePaymentResponseModel) br;
                        if (PaymentModel.getMeta().getCode() == 200) {
                            intent = new Intent(this, PayQRDetailActivity.class);
                            intent.putExtra("data", (ArrayList<? extends Parcelable>) PaymentModel.getData().getKeyValueList());
                            startActivity(intent);
                        } else if (PaymentModel.getMeta().getCode() == 400) {
                            Toast.makeText(this, PaymentModel.getMeta().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 535:
                        hideProgressDialog(false);
                        inquiryModel = (TransferBankInquiryResponseModel) br;
                        confirmDialog = new PayConfirmationDialog(this, this, false, false);
                        confirmDialog.setTitle("Konfirmasi Pembayaran");
                        confirmDialog.setData(inquiryModel.getRecieverName(), DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(amount.getText().toString())),
                                DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(inquiryModel.getFee())));
                        confirmDialog.setListener(new PayConfirmationDialog.payConfirmDialogListener() {
                            @Override
                            public void actionPressed() {
                                recieverName = inquiryModel.getRecieverName();
                                Intent intent = new Intent(TransferSaldoActivity.this, RegisterPinResetActivity.class);
                                intent.putExtra("confirm", true);
                                startActivityForResult(intent, 325);
//                                callBankConfirmationAPI(bankAccNum.getText().toString(), DataUtil.convertLongFromCurrency(amount.getText().toString()), bankCode, null, model2.getRecieverName(), walletId, "10");
                            }

                            @Override
                            public void onDialogDismiss() {

                            }
                        });
                        confirmDialog.show();
                        break;

                    case 536:
                        callProfileInfoAPI();
                        hideProgressDialog(true);
                        resetTransferForm(false);
                        PaymentModel = (BasePaymentResponseModel) br;
                        intent = new Intent(this, PayQRDetailActivity.class);
                        intent.putExtra("data", (ArrayList<? extends Parcelable>) PaymentModel.getData().getKeyValueList());
                        startActivity(intent);
                        break;

                    case 231:
                        WalletTypeResponseModel walletTypeResponse = (WalletTypeResponseModel) br;
                        walletTypes = walletTypeResponse.getData().getWallet_types();
                        if (walletTypes != null) {
                            walletCode = walletTypes.get(0).getCode();
                            walletTypeName.setText(walletTypes.get(0).getName());
                            Glide.with(walletLogo.getContext()).load(walletTypes.get(0).getLogo_url())
                                    .apply(new RequestOptions().placeholder(R.drawable.bank_placeholder)).into(walletLogo);
                        }
                        break;
                    default:

                        break;
                }
            } else {
                ErrorDialog hund = new ErrorDialog(this, this, false, false, "Terjadi Kesalahan pada Transaksi", ((BaseResponseModel) br).getMeta().getMessage());
                hund.show();
            }
        } else {
            Toast.makeText(this, "Maaf Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    private void resetTransferForm(boolean isWallet) {
        amount.setText("");
        phone.setContentText("");
        if (isWallet) {
            phone.requestFocus();
        } else {
            amount.requestFocus();
        }
    }

    private void hideProgressDialog(boolean isDelay) {
        if (isDelay) {
            Timer timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            ProgressDialogComponent.dismissProgressDialog(TransferSaldoActivity.this);
                        }
                    },
                    2000
            );
        } else {
            ProgressDialogComponent.dismissProgressDialog(TransferSaldoActivity.this);
        }
    }

    //endregion
}
