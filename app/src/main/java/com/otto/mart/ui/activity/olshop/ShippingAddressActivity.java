package com.otto.mart.ui.activity.olshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;
import com.otto.mart.model.APIModel.Request.olshop.address.AddressListResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.presenter.dao.olshop.OlshopDao;
import com.otto.mart.ui.Partials.RecyclerViewListener;
import com.otto.mart.ui.Partials.adapter.olshop.ShippingAddressAdapter;
import com.otto.mart.ui.activity.AppActivity;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class ShippingAddressActivity extends AppActivity implements RecyclerViewListener {

    private final int LIST_RC = 1;
    private final int EDITED = 2;

    private ShippingAddressAdapter adapter;
    private int addressId;
    private ShippingAddressData selectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.white);
        setStatusBarLightMode();
        setContentView(R.layout.activity_shipping_address);

        if (getIntent().hasExtra("addressId")) {
            addressId = getIntent().getIntExtra("addressId", -1);
        }

        RecyclerView addressList = findViewById(R.id.addressList);
        FloatingActionButton addAction = findViewById(R.id.addAction);
        View back = findViewById(R.id.backhitbox);
        adapter = new ShippingAddressAdapter(this, addressId);
        addressList.setLayoutManager(new LinearLayoutManager(this));
        addressList.setAdapter(adapter);
        addAction.setOnClickListener(v -> startActivity(new Intent(this, AddressOlshop.class)));
        back.setOnClickListener(v -> onBackPressed());

    }

    @Override
    protected void onStart() {
        super.onStart();
        callAddressListAPI();
    }

    @Override
    public void onBackPressed() {
        if (selectedAddress != null) {
            setResult(selectedAddress);
        } else finish();
    }

    private void callAddressListAPI() {
        ProgressDialogComponent.showProgressDialog(this, "Harap Tunggu", false);
        new OlshopDao(this).getShippingAddressList(BaseDao.getInstance(this, LIST_RC).callback);
    }

    @Override
    public void onItemClick(int id, int pos, Object data) {
        switch (id) {
            case R.id.actionEdit: {
                Intent intent = new Intent(this, AddressOlshop.class);
                intent.putExtra("address", new Gson().toJson((ShippingAddressData) data));
                startActivity(intent);
                break;
            }
            case EDITED: {
                selectedAddress = (ShippingAddressData) data;
                break;
            }
            default: {
                setResult((ShippingAddressData) data);
            }
        }
    }

    private void setResult(ShippingAddressData data) {
        Intent intent = new Intent();
        intent.putExtra("addressResult", new Gson().toJson(data));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (((BaseResponseModel) br).getMeta().getCode() == 200) {
            adapter.setAddressDataList(((AddressListResponseModel) br).getData().getShipping_addresses());
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        super.onApiFailureCallback(message, ac);
        ProgressDialogComponent.dismissProgressDialog(this);
    }
}
