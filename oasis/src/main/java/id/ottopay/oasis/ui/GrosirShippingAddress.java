package id.ottopay.oasis.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.grosir.GrosirAddressListResponse;
import com.otto.mart.presenter.dao.olshop.GrosirDao;
import com.otto.mart.ui.Partials.RecyclerViewListener;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import id.ottopay.oasis.R;
import retrofit2.Response;

public class GrosirShippingAddress extends AppActivity implements RecyclerViewListener {

    private final int LIST_RC = 1;
    private final int EDITED = 2;

    private OasisShippingAddressAdapter adapter;
    private int addressId;
    private ShippingAddressData selectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oasis_shipping_address);

        /*if (getIntent().hasExtra("addressId")) {
            addressId = getIntent().getIntExtra("addressId", -1);
        }*/

        RecyclerView addressList = findViewById(R.id.addressList);
        CardView addAction = findViewById(R.id.addAction);
        View back = findViewById(R.id.backhitbox);
        adapter = new OasisShippingAddressAdapter(this);
        addressList.setLayoutManager(new LinearLayoutManager(this));
        addressList.setAdapter(adapter);
        addAction.setOnClickListener(v -> startActivity(new Intent(this, GrosirChangeShipment.class)));
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
        ProgressDialogComponent.showProgressDialog(this, getString(com.otto.mart.R.string.text_please_wait), false);
        new GrosirDao(this).getShippingAddressList(BaseDao.getInstance(GrosirShippingAddress.this, LIST_RC).callback);
    }

    @Override
    public void onItemClick(int id, int pos, Object data) {
        switch (id) {
            case R.id.actionEdit: {
                Intent intent = new Intent(this, GrosirChangeShipment.class);
                intent.putExtra("id",((ShippingAddressData)data).getId());
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
        intent.putExtra("addressResult", data);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        //GrosirAddressListResponse model = (GrosirAddressListResponse) br;
        if (((BaseResponseModel)br).getMeta().getCode() == 200) {
            adapter.setAddressDataList(((GrosirAddressListResponse)br).getData().getShipping_addresses());
        } else {
            ErrorDialog dialog = new ErrorDialog(this,this, false, false, ((BaseResponseModel)br).getMeta().getMessage(), "");
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    onBackPressed();
                }
            });
            dialog.show();
            return;
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        super.onApiFailureCallback(message, ac);
        ProgressDialogComponent.dismissProgressDialog(this);
    }
}
