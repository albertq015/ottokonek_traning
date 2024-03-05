package com.otto.mart.ui.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.AddressModel;
import com.otto.mart.model.APIModel.Request.AddAddressRequestModel;
import com.otto.mart.model.APIModel.Response.AddressListResponseModel;
import com.otto.mart.presenter.dao.ProfileDao;
import com.otto.mart.ui.Partials.adapter.AddressAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;
import com.otto.mart.ui.activity.register.RegisterAddressInputActivity;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public class

AddressListActivity extends AppActivity implements RecyclerAdapterCallback {

    public static final String KEY_IS_ADD_ADDRESS = "is_add_address";

    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private RecyclerView addressList;
    private TextView addressName, address, address1, actionEdit, addAddress;
    private List<AddressModel> addresses = new ArrayList<>();
    private AddressAdapter addressAdapter;
    private ViewGroup backHitBox;
    private FloatingActionButton fabAdd;
    private boolean isAddAddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        setContentView(R.layout.activity_address_list);

        if (getIntent().hasExtra(KEY_IS_ADD_ADDRESS)) {
            isAddAddress = getIntent().getBooleanExtra(KEY_IS_ADD_ADDRESS, false);
        }

        iniComponent();
        initContent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAddressData();
    }

    private void getAddressData() {
        new ProfileDao(this).getAddressList(BaseDao.getInstance(this, 840).callback);
    }

    private void iniComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        addressList = findViewById(R.id.addressList);
        addressName = findViewById(R.id.address_name);
        address = findViewById(R.id.address);
        address1 = findViewById(R.id.address1);
        actionEdit = findViewById(R.id.actionEdit);
        backHitBox = findViewById(R.id.backhitbox);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void initContent() {
        tvToolbarTitle.setText(getString(R.string.text_address));
        actionEdit.setVisibility(View.GONE);

        addressAdapter = new AddressAdapter(this);
        addressList.setLayoutManager(new LinearLayoutManager(this));
        addressList.setAdapter(addressAdapter);

        btnToolbarBack.setOnClickListener(v -> finish());

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AddressListActivity.this, RegisterAddressInputActivity.class);
            intent.putExtra("address", "");
            intent.putExtra("isAddAddress", true);
            startActivityForResult(intent, 342);
        });

        if (isAddAddress) {
            fabAdd.performClick();
        }
    }

    @Override
    public void onItemClick(Object objectModel, int position, RecyclerView.ViewHolder currHolder) {
        Intent intent = new Intent(this, AddEditAddressActivity.class);
        intent.putExtra("address", (AddressModel) objectModel);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Object objectModel, int position, int paymentTypePos, RecyclerView.ViewHolder currHolder) {

    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (responseCode == 840) {
            AddressListResponseModel model = (AddressListResponseModel) br;
            if (model != null && model.getData().getAddresses().size() > 0) {
                addressAdapter.setAddresses(model.getData().getAddresses());
            }
        } else if (responseCode == 290) {
            getAddressData();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        onApiResponseError();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 342 && resultCode == RESULT_OK) {
            AddAddressRequestModel model = new AddAddressRequestModel();

            long[] kkkid = data.getLongArrayExtra("kkkid");

            model.setComplete_address(data.getStringExtra("address"));
            model.setProvince_id(kkkid[0]);
            model.setCity_id(kkkid[1]);
            model.setDistrict_id(kkkid[2]);
            model.setVillage_id(kkkid[3]);
            model.setName(data.getStringExtra("addressName"));

            new ProfileDao(this).createAddress(model, BaseDao.getInstance(this, 290).callback);
        }
    }
}
