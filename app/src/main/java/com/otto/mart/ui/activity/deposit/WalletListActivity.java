package com.otto.mart.ui.activity.deposit;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.Partials.adapter.WalletListAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public class WalletListActivity extends AppActivity {

    private RecyclerView walletList;
    private View backButton;
    private WalletListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_list);

        initComponent();
        iniContent();
    }

    private void initComponent() {
        walletList = findViewById(R.id.walletList);
        backButton = findViewById(R.id.backhitbox);
    }

    private void iniContent() {
        new WalletDao(this).emoneySummary(BaseDao.getInstance(this).callback);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new WalletListAdapter(new RecyclerAdapterCallback() {

            @Override
            public void onItemClick(Object objectModel, int position, RecyclerView.ViewHolder currHolder) {
                finish();
            }

            @Override
            public void onItemClick(Object objectModel, int position, int paymentTypePos, RecyclerView.ViewHolder currHolder) {

            }
        });
        walletList.setLayoutManager(new LinearLayoutManager(this));
        walletList.setAdapter(adapter);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (((WalletResponseModel) br).getMeta().getCode() == 200) {
            WalletResponseModel model = ((WalletResponseModel) br);
            adapter.setWallets(model.getData());
        } else if (((WalletResponseModel) br).getMeta().getCode() == 498) {
            SessionManager.logout();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "maaf : " + ((BaseResponseModel) br).getMeta().getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
       // super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }
}
