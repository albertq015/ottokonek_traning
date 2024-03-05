package com.otto.mart.ui.activity.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.AddressModel;
import com.otto.mart.model.APIModel.Request.AddressEditRequestModel;
import com.otto.mart.model.APIModel.Request.DeleteAddressRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.EditAddressResponseModel;
import com.otto.mart.presenter.dao.ProfileDao;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.fragment.register.RegisterAddressFragment;
import com.otto.mart.ui.fragment.reusable.AlamatPickerFragment;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_UPDATE_ADDRESS;

public class AddEditAddressActivity extends AppActivity {

    public static final String KEY_CHECKOUT_ADD_ADDRESS = "checkout_add_address";

    RegisterAddressFragment addressFragment;

    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private TextView save, delete, confirm, dismiss, title, deleteAction;
    private LazyEdittext addressName;
    private AddressEditRequestModel requestModel = new AddressEditRequestModel();
    private Dialog confirmDialog;
    private ViewGroup backHitBox;
    private boolean isSave = false;
    private int addressId;
    private LazyDialog changesDialog;
    private CheckBox cbMainAddress;

    private boolean isFromCheckout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_address);

        isFromCheckout = getIntent().getBooleanExtra(KEY_CHECKOUT_ADD_ADDRESS, false);

        initDialog();
        initComponent();
        initContent();
        buildChangesDialog();
    }

    private void initDialog() {
        confirmDialog = new Dialog(this);
        confirmDialog.setContentView(LayoutInflater.from(this).inflate(R.layout.dialog_changeconfirm, null));
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        save = findViewById(R.id.saveAction);
        delete = findViewById(R.id.deleteAction);
        addressName = findViewById(R.id.addressName);
        backHitBox = findViewById(R.id.backhitbox);
        confirm = confirmDialog.findViewById(R.id.posijing);
        dismiss = confirmDialog.findViewById(R.id.negajing);
        title = confirmDialog.findViewById(R.id.dialogTvTitle);
        deleteAction = findViewById(R.id.deleteAction);
        cbMainAddress = findViewById(R.id.cbMainAddress);
    }

    private void initContent() {
        tvToolbarTitle.setText(getString(R.string.text_edit_alamat));

        addressFragment = new RegisterAddressFragment();
        final AlamatPickerFragment alamatPickerFragment = new AlamatPickerFragment();
        Bundle bundle = new Bundle();

        if (isFromCheckout) {
            tvToolbarTitle.setText(getString(R.string.label_add_address));
            deleteAction.setVisibility(View.GONE);
            cbMainAddress.setVisibility(View.GONE);
        }

        if (getIntent().hasExtra("address")) {
            AddressModel addressModel = getIntent().getParcelableExtra("address");
            addressName.setContentText(addressModel.getName());
            requestModel.setAddress_id(addressModel.getId());
            addressId = addressModel.getId();

            int[] address = new int[4];
            address[0] = Integer.parseInt(addressModel.getProvincePos());
            address[1] = Integer.parseInt(addressModel.getCityPos());
            address[2] = Integer.parseInt(addressModel.getDistrictPos());
            address[3] = Integer.parseInt(addressModel.getVillagePos());
            cbMainAddress.setChecked(addressModel.isSet_as_main());
            bundle.putString("address", addressModel.getCompleteAddress());
            bundle.putIntArray("kkkpos", address);
        }

        alamatPickerFragment.setArguments(bundle);
        alamatPickerFragment.setAlamatPickerCallback(new AlamatPickerFragment.AlamatPickerCallback() {
            @Override
            public void onFormComplete(String address, long[] collectionResult, long[] positionResult) {
                save.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                requestModel.setProvince_id(collectionResult[0]);
                requestModel.setCity_id(collectionResult[1]);
                requestModel.setDistrict_id(collectionResult[2]);
                requestModel.setVillage_id(collectionResult[3]);
                requestModel.setComplete_address(alamatPickerFragment.getAddress());
            }

            @Override
            public void onFromUncompete() {
                save.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, alamatPickerFragment).disallowAddToBackStack().commit();

        save.setOnClickListener(v -> {
            requestModel.setName(addressName.getTextContent());
            requestModel.setComplete_address(alamatPickerFragment.getAddress());

            if (isFromCheckout) {
                Intent data = new Intent();
                data.setData(Uri.parse(new Gson().toJson(requestModel)));
                setResult(RESULT_OK, data);
                finish();
            } else {
                showConfirmDialog();
//                LazyDialog lazyDialog=new LazyDialog(AddEditAddressActivity.this,AddEditAddressActivity.this);
//                lazyDialog.setContainerView(LayoutInflater.from(AddEditAddressActivity.this).inflate(R.layout.dialog_changeconfirm,null));
//                lazyDialog.show();
            }
        });

        btnToolbarBack.setOnClickListener(v -> finish());

        delete.setOnClickListener(v -> {

        });

        confirm.setOnClickListener(v -> {
            if (isSave) {
                requestModel.setSet_as_main(cbMainAddress.isChecked());
                new ProfileDao(AddEditAddressActivity.this).updateAddress(AddEditAddressActivity.this, requestModel,
                        BaseDao.getInstance(AddEditAddressActivity.this, API_KEY_UPDATE_ADDRESS).callback);
            } else finish();
        });

        dismiss.setOnClickListener(v -> confirmDialog.dismiss());

        deleteAction.setOnClickListener(v -> changesDialog.show());
    }

    private void buildChangesDialog() {
        changesDialog = new LazyDialog(this, this, true);
        changesDialog.setContainerView(LayoutInflater.from(this).inflate(R.layout.dialog_changeconfirm, null));
        ((TextView) changesDialog.findViewById(R.id.dialogTvTitle)).setText(getString(R.string.text_delete_address));

        changesDialog.findViewById(R.id.posijing).setOnClickListener(v -> {
            DeleteAddressRequestModel model = new DeleteAddressRequestModel();
            model.setAddress_id(addressId);
            new ProfileDao(AddEditAddressActivity.this).deleteAddress(model, BaseDao.getInstance(AddEditAddressActivity.this, 723).callback);
        });

        changesDialog.findViewById(R.id.negajing).setOnClickListener(v -> changesDialog.dismiss());
        changesDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (responseCode == API_KEY_UPDATE_ADDRESS) {
            EditAddressResponseModel responseModel = (EditAddressResponseModel) br;
            if (responseModel != null && responseModel.getMeta().getCode() == 200) {
                showNotifyDialog("Alamat kamu berhasil diubah.");
            } else {
                showNotifyDialog("Alamat kamu gagal diubah.");
            }
        } else if (responseCode == 723 && ((BaseResponseModel) br).getMeta().getCode() == 200) {
//            finish();
            ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
            dialog.setOnDismissListener(dialog1 -> finish());
            dialog.show();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    private void showConfirmDialog() {
        title.setText(getString(R.string.message_save_changes));
        dismiss.setVisibility(View.VISIBLE);
        isSave = true;
        confirm.setText(getString(R.string.text_yes));
        confirmDialog.show();
    }

    private void showNotifyDialog(String message) {
        title.setText(message);
        dismiss.setVisibility(View.GONE);
        isSave = false;
        confirm.setText(getString(R.string.text_ok));
        confirmDialog.show();
    }
}
