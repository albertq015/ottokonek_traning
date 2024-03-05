package com.otto.mart.ui.activity.deposit;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WalletType;
import com.otto.mart.model.APIModel.Request.WalletTypeResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.ui.Partials.RecyclerViewListener;
import com.otto.mart.ui.Partials.adapter.WalletTypeAdapter;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public class WalletTypeListActivity extends BaseActivity implements RecyclerViewListener {

    private RecyclerView rvWalletType;
    private WalletTypeAdapter adapter;
    private View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_type_list);
        initComponent();
        initContent();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void initComponent() {
        rvWalletType = findViewById(R.id.rvWalletType);
        back = findViewById(R.id.backhitbox);
    }

    private void initContent() {
        adapter = new WalletTypeAdapter(this);
        rvWalletType.setLayoutManager(new LinearLayoutManager(this));
        rvWalletType.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        new WalletDao(this).getWalletType(BaseDao.getInstance(this, 231).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (responseCode == 231 && ((BaseResponseModel) br).getMeta().getCode() == 200) {
            WalletTypeResponseModel model = (WalletTypeResponseModel) br;
            adapter.setWalletTypeList(model.getData().getWallet_types());
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int id, int pos, Object data) {
        if (data instanceof WalletType) {
            Intent intent = new Intent();
            intent.putExtra("walletTypeData", (WalletType) data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
