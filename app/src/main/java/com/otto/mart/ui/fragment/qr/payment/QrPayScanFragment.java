package com.otto.mart.ui.fragment.qr.payment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.otto.mart.model.APIModel.Request.payment.IndomaretQrPurchaseRequestModel;
import com.otto.mart.model.APIModel.Request.payment.IndomaretQrStatusRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.PayQrInquiryResponse;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.presenter.dao.TransactionDao;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.EmvcoUtil;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.support.util.pref.PrefConstance;
import com.otto.mart.ui.activity.bogasari.catalog.CatalogBogasariActivity;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.activity.transaction.alfamart.AlfamartShowPaymentCodeActivity;
import com.otto.mart.ui.activity.transaction.IndomaretQRPaymentActivity;
import com.otto.mart.ui.activity.transaction.PayQRDetailActivity;
import com.otto.mart.ui.activity.transaction.ProcessQRPaymentActivity;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.template.CashInputKeyboard.CashInputKeyboardView;
import com.otto.mart.ui.fragment.AppFragment;

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

import static com.otto.mart.ui.fragment.transaction.QRPaymentShowCodeFragment.RC_INDOMARET_QR_PURCHASE;
import static com.otto.mart.ui.fragment.transaction.QRPaymentShowCodeFragment.RC_INDOMARET_QR_PURCHASE_STATUS;

public class QrPayScanFragment extends AppFragment {

    private final int RC_PIN_INDOMARET_PURCHASE = 2010;
    private final int RC_PIN_SHOW_QR = 2011;

    private Context mContext;
    private View mView, confirm;
    private ZXingScannerView mScannerView;
    private boolean contentInitialized;
    private String inquiry, tokoname, val;
    private TextView tv3, tv5, tv7;
    private LazyTextview ltv_name, ltv_val;
    private LazyEdittext let_cash;
    private LazyDialog lazyDialog;
    private int selectedWalletId, paybleAmount;
    private boolean isManualInput, isPaymentCanceled;
    private CashInputKeyboardView cikv;
    private ImageView imgOttopayCode;

    private String mReferenceNumber = "";
    private String mQRContent = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(R.layout.fagment_qrpay_scan, container, false);
        initPermissions();

        initComponent();
        initContent();
        return mView;
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
                    paybleAmount = Integer.parseInt(EmvcoUtil.parseEMVCOtag54(result.toString()));
                    break;
                default:
                    showLoading("Mohon tunggu");
                    isReady = true;
                    isManualInput = true;
                    break;
            }
            if (isReady) {
                mScannerView.stopCamera();

                ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
                String qrContent = EmvcoUtil.parseEMVCOtag62(result.getText());
                List<String> qrContentSeparated = Arrays.asList(qrContent.split("\\s*;;;;\\s*"));
                if (qrContentSeparated.get(0).equalsIgnoreCase("IDM")) {

                    Pref.getPreference().putString(PrefConstance.STORE_ID, qrContentSeparated.get(1));
                    Pref.getPreference().putString("t59", EmvcoUtil.parseEMVCOtagByNum(59, result.getText()));
                    Pref.getPreference().putString("t60", EmvcoUtil.parseEMVCOtagByNum(60, result.getText()));
                    Pref.getPreference().putString("t62", EmvcoUtil.parseEMVCOtag62(result.getText()));

                    Intent jenk = new Intent(getActivity(), RegisterPinResetActivity.class);
                    jenk.putExtra("confirm", true);
                    startActivityForResult(jenk, RC_PIN_INDOMARET_PURCHASE);
                } else if (qrContentSeparated.size() > 1 && qrContentSeparated.get(1).contains("BG")) {
                    Intent intent = new Intent(getActivity(), CatalogBogasariActivity.class);
                    intent.putExtra("qrContent", result.getText());
                    intent.putExtra("merchantId", qrContent.split(";;;;")[1]);
                    startActivity(intent);
                    getActivity().finish();
                } else {
//                   PayQrInquiryRequestModel requestModel = new PayQrInquiryRequestModel();
//                   requestModel.setQrData(result.getText());
//                   callOfflineQrInquiryApi(requestModel);

                    mQRContent = result.getText();
                    gotoProcessQRPayment(result.getText());
                }
            }
        }
    };

    private void gotoProcessQRPayment(String result) {
        Intent intent = new Intent(getActivity(), ProcessQRPaymentActivity.class);
        intent.putExtra(ProcessQRPaymentActivity.KEY_QR_CONTENT, result);
        startActivity(intent);
    }

    private void showLoading(String message) {
        if (!getActivity().isFinishing()) {
            ProgressDialogComponent.showProgressDialog(getActivity(), message, false).show();
        }
    }

    private void initPermissions() {
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                initComponent();
                initContent();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                getActivity().finish();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.cancelPermissionRequest();
                getActivity().finish();
            }
        }).check();
    }

    private void initComponent() {
        mScannerView = mView.findViewById(R.id.cam_view);
        lazyDialog = new LazyDialog(getContext(), getActivity(), false);
        lazyDialog.setContainerView(LayoutInflater.from(getContext()).inflate(R.layout.dialog_pay_qr, null));
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
        cikv = lazyDialog.getContent().findViewById(R.id.cikv);
        imgOttopayCode = mView.findViewById(R.id.img_ottopay_code);
    }

    private void initContent() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //val = ((LazyEdittext) lazyDialog.getContent().findViewById(R.id.let_cash)).getTextContent();

                val = cikv.getTextContent();
                if (val.equals("0")) {
                    val = DataUtil.cleanDigit(ltv_val.getTextContent());
                }

                if (!val.equals("")) {
                    Intent jenk = new Intent(getActivity(), RegisterPinResetActivity.class);
                    jenk.putExtra("confirm", true);
                    startActivityForResult(jenk, 111);
                } else {
                    ErrorDialog errorDialog = new ErrorDialog(getContext(), getActivity(), false, false, "Jumlah harga tidak valid", "Mohon cek kembali input harga anda");
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

        imgOttopayCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jenk = new Intent(getActivity(), RegisterPinResetActivity.class);
                jenk.putExtra("confirm", true);
                startActivityForResult(jenk, RC_PIN_SHOW_QR);
            }
        });

        mScannerView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        mScannerView.setResultHandler(scannerHandler);

        contentInitialized = true;
        getOmzetStatApi();
    }

    private void callOfflineQrInquiryApi(PayQrInquiryRequestModel requestModel) {
        ProgressDialogComponent.showProgressDialog(getContext(), "Mohon Menunggu", false).show();
        TransactionDao dao = new TransactionDao(QrPayScanFragment.this);
        dao.payQrInquiry(requestModel, BaseDao.getInstance(QrPayScanFragment.this, 333).callback);
    }

    private void callOfflineQrPaymentApi(PaymentOfflineConfirmationRequestModel requestModel) {
        if (getActivity() != null && getActivity() instanceof BaseActivity)
            ProgressDialogComponent.showProgressDialog(getContext(), "Mohon Menunggu", false).show();
        TransactionDao dao = new TransactionDao(QrPayScanFragment.this);
        dao.payQrPayment(requestModel, BaseDao.getInstance(QrPayScanFragment.this, 334).callback);
    }


    private void callIndomaretQrPurchase() {
        IndomaretQrPurchaseRequestModel indomaretQrPurchaseRequestModel = new IndomaretQrPurchaseRequestModel();
        indomaretQrPurchaseRequestModel.setStore_id(Pref.getPreference().getString(PrefConstance.STORE_ID));
        indomaretQrPurchaseRequestModel.setProduct_code("OTTOMART");

        new TransactionDao(this).getIndomaretQrPurchase(indomaretQrPurchaseRequestModel, BaseDao.getInstance(this, RC_INDOMARET_QR_PURCHASE).callback);
    }

    private void callIndomaretQrPurchaseStatus(String referenceNumber) {
        IndomaretQrStatusRequestModel indomaretQrStatusRequestModel = new IndomaretQrStatusRequestModel();
        indomaretQrStatusRequestModel.setReference_number(referenceNumber);

        new TransactionDao(this).getIndomaretQrPurchaseStatus(indomaretQrStatusRequestModel, BaseDao.getInstance(this, RC_INDOMARET_QR_PURCHASE_STATUS).callback);
    }

    private void getOmzetStatApi() {
        new WalletDao(QrPayScanFragment.this).emoneySummary(BaseDao.getInstance(QrPayScanFragment.this, 999).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (getActivity() != null && getActivity() instanceof BaseActivity)
            ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        if (response.isSuccessful()) {
            switch (responseCode) {
                case 333:
//                    if (((PayQrInquiryOldResponseModel) br).getMeta().getCode() == 200) {
//                        inquiry = ((PayQrInquiryOldResponseModel) br).getInquiry_data();
//                        for (WidgetBuilderModel model :
//                                (((PayQrInquiryOldResponseModel) br).getKey_value_list())) {
//                            if (model.getKey().equals("merchantName")) {
//                                tokoname = model.getValue();
//                                ltv_name.setText(tokoname);
//                                ltv_name.setVisibility(View.VISIBLE);
//                            }
//                            if (model.getKey().equals("Jumlah") && !model.getValue().equals("0")) {
//                                paybleAmount = Integer.parseInt(model.getValue());
//                                ltv_val.setText(DataUtil.convertCurrency(model.getValue()));
//                                ltv_val.setVisibility(View.VISIBLE);
//                                let_cash.setVisibility(View.GONE);
//                                cikv.setVisibility(View.GONE);
//                            } else if (paybleAmount > 0) {
//                                ltv_val.setText(DataUtil.convertCurrency(paybleAmount));
//                                ltv_val.setVisibility(View.VISIBLE);
//                                let_cash.setVisibility(View.GONE);
//                                cikv.setVisibility(View.GONE);
//                            } else {
//                                ltv_val.setVisibility(View.GONE);
//                                let_cash.setVisibility(View.GONE);
//                                cikv.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        lazyDialog.show();
//                    } else {
//                        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
//                        dialog.show();
//                        if (contentInitialized)
//                            mScannerView.setResultHandler(scannerHandler);
//                    }

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
                        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                        if (contentInitialized)
                            mScannerView.setResultHandler(scannerHandler);
                    }
                    break;
                case 334:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        Intent sucessIntent = new Intent(getActivity(), PayQRDetailActivity.class);
                        sucessIntent.putExtra("data", (ArrayList<? extends Parcelable>) ((BasePaymentResponseModel) br).getData().getKeyValueList());
                        startActivity(sucessIntent);
                        getActivity().finish();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                        if (contentInitialized)
                            mScannerView.setResultHandler(scannerHandler);
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
                        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                        if (contentInitialized)
                            mScannerView.setResultHandler(scannerHandler);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message) {
        //super.onApiFailureCallback(message);
        onApiResponseError();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.startCamera();
        mScannerView.setResultHandler(scannerHandler);
        cikv.setText("0");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (contentInitialized) {
            mScannerView.stopCamera();
        }
    }

    @Override
    public void onStop() {
        if (lazyDialog != null)
            lazyDialog.dismiss();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case 111:
                    if (data != null) {
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
                    }
                    break;
                case RC_PIN_INDOMARET_PURCHASE:
                    if (data != null) {
//                        showQRPaymentCodeFragment();
//                        callIndomaretQrPurchase();
                        startActivity(new Intent(getActivity(), IndomaretQRPaymentActivity.class));
                    }
                    break;
                case RC_PIN_SHOW_QR:
                    startActivity(new Intent(getActivity(), AlfamartShowPaymentCodeActivity.class));
                    break;
                default:
                    break;
            }
        } else {
            if (contentInitialized)
                mScannerView.setResultHandler(scannerHandler);

        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (contentInitialized) {
            mScannerView.stopCamera();
            mScannerView.setResultHandler(null);
        }
        super.startActivityForResult(intent, requestCode);
    }
}
