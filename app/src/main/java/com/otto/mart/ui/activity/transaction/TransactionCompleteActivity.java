package com.otto.mart.ui.activity.transaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.OmzetHistoryResponseData;
import com.otto.mart.model.APIModel.Request.PpobTransactionAdviceModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel;
import com.otto.mart.presenter.dao.PpobDao;
import com.otto.mart.support.util.CryptoUtil;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_TRANSACTION_ADVICE;

public class TransactionCompleteActivity extends AppActivity {

    private ImageView closeButton;
    private TextView amount, idTransaction, tv_status, dialogTitle;
    private LazyTextview let_resi, let_date, let_transactionName, let_method, let_type, let_direction;
    private LazyTextview letBillerReference, letStroomToken, letCustomerName, letCommission, letSerial, letPinKode, paymentId;
    private Button btnCheckStatus;

    private OmzetHistoryResponseData omzetHistoryResponseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_complete);

        initComponent();
        initContent();
    }

    private void initComponent() {
        amount = findViewById(R.id.amount);
        idTransaction = findViewById(R.id.idTransaction);
        closeButton = findViewById(R.id.closeButton);
        let_resi = findViewById(R.id.let_resi);
        let_date = findViewById(R.id.let_date);
        let_transactionName = findViewById(R.id.let_transaction_name);
        let_method = findViewById(R.id.let_method);
        let_type = findViewById(R.id.let_type);
        let_direction = findViewById(R.id.let_direction);
        tv_status = findViewById(R.id.tv_status);
        dialogTitle = findViewById(R.id.dialogTitle);

        letBillerReference = findViewById(R.id.let_biller_reference);
        letStroomToken = findViewById(R.id.let_stroom_token);
        letCustomerName = findViewById(R.id.let_customer_name);
        letCommission = findViewById(R.id.let_commission);
        letSerial = findViewById(R.id.let_serial);
        letPinKode = findViewById(R.id.let_pin_kode);
        btnCheckStatus = findViewById(R.id.btn_check_status);
        paymentId = findViewById(R.id.paymentNo);
    }

    private void initContent() {
        if (getIntent().hasExtra("transaction")) {
            omzetHistoryResponseData = getIntent().getParcelableExtra("transaction");
            if (omzetHistoryResponseData != null) {

                String status = omzetHistoryResponseData.getStatus();

                if (status.equalsIgnoreCase("success")) {
                    status = "Sukses";
                } else if (status.equalsIgnoreCase("failed")) {
                    status = "Gagal";
                } else if (status.equalsIgnoreCase(getString(R.string.text_in_process))) {
                    btnCheckStatus.setVisibility(View.VISIBLE);
                }

                dialogTitle.setText(getString(R.string.text_transaction) + status);
                amount.setText(DataUtil.convertCurrency(omzetHistoryResponseData.getAmount()));
                idTransaction.setText(omzetHistoryResponseData.getReferenceNumber());
                let_resi.setText(omzetHistoryResponseData.getReferenceNumber());
                //let_date.setText(DataUtil.getDateString(omzetHistoryResponseData.getDate(), "EEEE, dd MMMM yyyy HH:mm",true, 0));
                let_date.setText(omzetHistoryResponseData.getDateString());
                let_transactionName.setText(omzetHistoryResponseData.getDescription());
                let_method.setText(omzetHistoryResponseData.getMethod());
                let_type.setText(omzetHistoryResponseData.getType());
                let_direction.setText(omzetHistoryResponseData.getDirection());

                if (omzetHistoryResponseData.getBillerReference() != null) {
                    if (!omzetHistoryResponseData.getBillerReference().equals("") && !omzetHistoryResponseData.getBillerReference().equals("-")) {
                        letBillerReference.setText(omzetHistoryResponseData.getBillerReference());
                        letBillerReference.setVisibility(View.VISIBLE);
                    }
                }

                if (omzetHistoryResponseData.getStroomToken() != null) {
                    if (!omzetHistoryResponseData.getStroomToken().equals("") && !omzetHistoryResponseData.getStroomToken().equals("-")) {
                        letStroomToken.setText(omzetHistoryResponseData.getStroomToken());
                        letStroomToken.setVisibility(View.VISIBLE);
                    }
                }

                if (omzetHistoryResponseData.getCustomerName() != null) {
                    if (!omzetHistoryResponseData.getCustomerName().equals("") && !omzetHistoryResponseData.getCustomerName().equals("-")) {
                        letCustomerName.setText(omzetHistoryResponseData.getCustomerName());
                        letCustomerName.setVisibility(View.VISIBLE);
                    }
                }

                if (omzetHistoryResponseData.getCommission() != null) {
                    if (!omzetHistoryResponseData.getCommission().equals("") && !omzetHistoryResponseData.getCommission().equals("-")) {
                        letCommission.setText(omzetHistoryResponseData.getCommission());
                        letCommission.setVisibility(View.VISIBLE);
                    }
                }

                if (omzetHistoryResponseData.getSerial() != null) {
                    if (!omzetHistoryResponseData.getSerial().equals("") && !omzetHistoryResponseData.getSerial().equals("-")) {
                        letSerial.setText(omzetHistoryResponseData.getSerial());
                        letSerial.setVisibility(View.VISIBLE);
                    }
                }

                if (omzetHistoryResponseData.getPinCode() != null) {
                    if (!omzetHistoryResponseData.getPinCode().equals("") && !omzetHistoryResponseData.getPinCode().equals("-")) {
                        letPinKode.setText(omzetHistoryResponseData.getPinCode());
                        letPinKode.setVisibility(View.VISIBLE);
                    }
                }

                if (omzetHistoryResponseData.getPaymentId() >= 0) {
                    paymentId.setText(String.valueOf(omzetHistoryResponseData.getPaymentId()));
                    paymentId.setVisibility(View.VISIBLE);
                }

            }
        }
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCheckStatus.setOnClickListener(v -> {
            checkStatus(omzetHistoryResponseData.getReferenceNumber());
        });
    }

    private void checkStatus(String referenceNumber) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show();

        PpobTransactionAdviceModel ppobTransactionAdviceModel = new PpobTransactionAdviceModel();
        ppobTransactionAdviceModel.setReference_number(CryptoUtil.encryptRSA(referenceNumber.getBytes()));

        PpobDao dao = new PpobDao(this);
        dao.ppobCheckPending(ppobTransactionAdviceModel, BaseDao.getInstance(this, API_KEY_TRANSACTION_ADVICE).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (responseCode == API_KEY_TRANSACTION_ADVICE) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                PpobOttoagPaymentResponseModel model = (PpobOttoagPaymentResponseModel) br;
                if (model.getData().getKeyValueList().size() > 0) {

//                    Intent intent = new Intent(this, PayQRDetailActivity.class);
//                    intent.putExtra("data", (ArrayList<? extends Parcelable>) ((BasePaymentResponseModel) br).getData().getKeyValueList());
//                    startActivity(intent);
//                    finish();

                    dialogTitle.setText(getString(R.string.text_transaction_success));
                    btnCheckStatus.setVisibility(View.GONE);
                } else {
                    ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                    dialog.show();
                }
            } else if (((BaseResponseModel) br).getMeta().getCode() == 408) {
                ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                dialog.show();
            } else {
                ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                dialog.show();
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        // super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }
}
