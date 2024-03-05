package com.otto.mart.ui.activity.transaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.OmzetSaldoRequestModel;
import com.otto.mart.model.APIModel.Response.OmzetSaldoResponseModel;
import com.otto.mart.presenter.dao.TransactionDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.Random;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class TransactionDetailActivity extends AppActivity {

    private static int RESPONSE_CODE = 11;
    private static int PIN_RC = 22;

    private ViewGroup backButton;
    private LazyTextview idTransaction, transactionType, amount;
    private TextView confirm, deny;
    private OmzetSaldoRequestModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        initComponent();
        initContent();
    }

    private void initComponent() {
        backButton = findViewById(R.id.backhitbox);
        idTransaction = findViewById(R.id.idTransaction);
        transactionType = findViewById(R.id.trans_type);
        amount = findViewById(R.id.amount);
        confirm = findViewById(R.id.confirm);
        deny = findViewById(R.id.deny);
    }

    private void initContent() {
        if (getIntent().hasExtra("omzet")) {
            model = getIntent().getParcelableExtra("omzet");
            if (model != null) {
                amount.setText(DataUtil.convertCurrency(model.getAmount()));
            }
        }

        if (getIntent().hasExtra("type")) {
            transactionType.setText("Pencairan omzet ke " + getIntent().getStringExtra("type"));
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model != null) {
                    Intent intent = new Intent(TransactionDetailActivity.this, RegisterPinResetActivity.class);
                    intent.putExtra("confirm", true);
                    startActivityForResult(intent, PIN_RC);
                }
            }
        });
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idTransaction != null )
            idTransaction.setText(generateRandom(12) + "");
    }

    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.hasExtra("pincode")) {
                String pin = data.getStringExtra("pincode");
                model.setPin(pin);
                ProgressDialogComponent.showProgressDialog(this, "Memproses Permintaan", false).show();
                new TransactionDao(TransactionDetailActivity.this).transOmzetToWallet(TransactionDetailActivity.this, model,
                        BaseDao.getInstance(TransactionDetailActivity.this, RESPONSE_CODE).callback);
            }
        }

        idTransaction.setText(generateRandom(12) + "");
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(TransactionDetailActivity.this);
        OmzetSaldoResponseModel model = (OmzetSaldoResponseModel) br;
        if (model != null && model.getMeta().getCode() == 200) {
            Intent intent = new Intent();
            intent.putExtra("isTransactionSuccess", true);
            intent.putExtra("amount", this.model.getAmount());
            setResult(RESULT_OK, intent);
            finish();
        } else if (model != null && model.getMeta().getCode() == 400) {
            ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((OmzetSaldoResponseModel) br).getMeta().getMessage(), "");
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            dialog.show();
        } else {
            showToastMessage("Terjadi Kesalahan Pada Server");
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
       // super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
