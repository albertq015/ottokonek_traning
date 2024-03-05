package com.otto.mart.ui.activity.transaction.withdraw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.deposit.WalletListActivity;
import com.otto.mart.ui.component.GridRadioButtton;
import com.otto.mart.ui.component.LazyTextview;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public class WithdrawOldActivity extends AppActivity {

    private TextView action;
    private ViewGroup backButton, saldoContainer;
    private LazyTextview saldoOwner, saldo;
    private GridRadioButtton nominal;
    private EditText nominalInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_old);
        initComponent();
        initContent();
    }

    private void initComponent() {
        action = findViewById(R.id.action);
        backButton = findViewById(R.id.backhitbox);
        saldo = findViewById(R.id.saldo);
        saldoOwner = findViewById(R.id.saldoOwner);
        saldoContainer = findViewById(R.id.saldoContainer);
        nominal = findViewById(R.id.rgrow1);
        nominalInput = findViewById(R.id.guk);
    }

    private void initContent() {
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WithdrawOldActivity.this, WithdrawDetailOldActivity.class));
            }
        });
        saldoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WithdrawOldActivity.this, WalletListActivity.class));
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nominal.setOnCheckedChangeListener(new GridRadioButtton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(int checkedId) {
                String data = ((RadioButton) nominal.findViewById(checkedId)).getText().toString();
                nominalInput.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(data)));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new WalletDao(this).emoneySummary(BaseDao.getInstance(this, 392).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (responseCode == 392) {
            WalletResponseModel res = (WalletResponseModel) br;
            if (res != null) {
                WalletDataModel model = res.getData().get(0);
                saldo.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(model.getBalance())));
                saldoOwner.setText(model.getOwner_name());
                saldoOwner.setTitle(DataUtil.getXXXPhone(model.getAccount_number()));
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }
}
