package com.otto.mart.ui.activity.olshop;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;
import com.otto.mart.model.APIModel.Request.AddressEditRequestModel;
import com.otto.mart.model.APIModel.Request.DeleteAddressRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.AddressRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.address.ShippingAddressRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.presenter.dao.ProfileDao;
import com.otto.mart.presenter.dao.olshop.OlshopDao;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.fragment.register.RegisterAddressFragment;
import com.otto.mart.ui.fragment.reusable.AddressPickerFragment;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class AddressOlshop extends AppActivity {

    public static final String KEY_CHECKOUT_ADD_ADDRESS = "checkout_add_address";
    private final int DEL_RC = 1;
    private final int CRE_RC = 2;
    private final int UPD_RC = 3;

    RegisterAddressFragment addressFragment;
    private TextView save, delete, confirm, dismiss, toolbarTitle, title, deleteAction;
    private LazyEdittext addressName, fullAddress, postalCode;
    private AddressEditRequestModel requestModel = new AddressEditRequestModel();
    private LazyDialog confirmDialog;
    private ViewGroup backHitBox;
    private boolean isSave = false;
    private int addressId;
    private LazyDialog changesDialog;
    private ShippingAddressData addressModel;
    private AddressRequestModel selectedAddressModel;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.white);
        setStatusBarLightMode();
        setContentView(R.layout.activity_address_olshop);

        iniDialog();
        initComponent();
        initContent();
        buildChangesDialog();
    }

    private void initComponent() {
        toolbarTitle = findViewById(R.id.title);
        save = findViewById(R.id.saveAction);
        delete = findViewById(R.id.deleteAction);
        addressName = findViewById(R.id.addressName);
        backHitBox = findViewById(R.id.backhitbox);
        confirm = confirmDialog.findViewById(R.id.posijing);
        dismiss = confirmDialog.findViewById(R.id.negajing);
        title = confirmDialog.findViewById(R.id.dialogTvTitle);
        postalCode = findViewById(R.id.postalCode);
        fullAddress = findViewById(R.id.let_alamat);
    }

    private void initContent() {
        addressFragment = new RegisterAddressFragment();
        final AddressPickerFragment alamatPickerFragment = new AddressPickerFragment();
        Bundle bundle = new Bundle();

        if (getIntent().hasExtra("address")) {
            isEdit = true;
            addressModel = new Gson().fromJson(getIntent().getStringExtra("address"), ShippingAddressData.class);
            addressName.setContentText(addressModel.getName());
            fullAddress.setContentText(addressModel.getDetail());
            postalCode.setContentText(String.valueOf(addressModel.getZip_code()));
            addressId = addressModel.getId();
            String[] state = new String[3];
            state[0] = addressModel.getProvince().getName();
            state[1] = addressModel.getCity().getName();
            state[2] = addressModel.getDistrict().getName();
            bundle.putStringArray("kkkpos", state);
            if (String.valueOf(addressModel.getZip_code()).length() == 5) {
                save.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
        }

        alamatPickerFragment.setArguments(bundle);
        alamatPickerFragment.setAlamatPickerCallback(new AddressPickerFragment.AlamatPickerCallback() {

            @Override
            public void onFormComplete(AddressRequestModel addressModel) {
                selectedAddressModel = addressModel;
                postalCode.setVisibility(View.VISIBLE);
                if (postalCode.getTextContent().length() >= 5) {
                    save.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFromUncompete() {
                save.setVisibility(View.GONE);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, alamatPickerFragment).disallowAddToBackStack().commit();

        save.setOnClickListener(v -> {
            if (addressName.getTextContent().isEmpty()) {
                ((EditText) addressName.getComponent()).setError("Nama alamat wajib diisi");
            } else ((EditText) addressName.getComponent()).setError(null);

            if (fullAddress.getTextContent().isEmpty()) {
                ((EditText) fullAddress.getComponent()).setError("Alamat lengkap wajib diisi");
            } else ((EditText) fullAddress.getComponent()).setError(null);

            if (addressName.getTextContent().isEmpty() || fullAddress.getTextContent().isEmpty())
                return;

            ShippingAddressRequestModel model = new ShippingAddressRequestModel();
            ShippingAddressData data = new ShippingAddressData();
            data.setDistrict_id(selectedAddressModel.getDistrictId());
            data.setName(addressName.getTextContent());
            data.setDetail(fullAddress.getTextContent());
            data.setZip_code(Integer.parseInt(postalCode.getTextContent()));
            model.setShipping_address(data);

            ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);
            if (isEdit) {
                new OlshopDao(this).updateShippingAddress(model, String.valueOf(addressId), BaseDao.getInstance(this, UPD_RC).callback);
            } else
                new OlshopDao(this).createShippingAddress(model, BaseDao.getInstance(this, CRE_RC).callback);
        });
        delete.setOnClickListener(v -> confirmDialog.show());

        backHitBox.setOnClickListener(v -> finish());

//        confirm.setOnClickListener(v -> {
//            if (isSave) {
//                new ProfileDao(AddressOlshop.this).updateAddress(AddressOlshop.this, requestModel,
//                        BaseDao.getInstance(AddressOlshop.this, API_KEY_UPDATE_ADDRESS).callback);
//            } else finish();
//        });
//        dismiss.setOnClickListener(v -> confirmDialog.dismiss());

        ((EditText) postalCode.getComponent()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 5) {
                    save.setVisibility(View.GONE);
                    if (isEdit)
                        delete.setVisibility(View.GONE);
                } else {
                    save.setVisibility(View.VISIBLE);
                    if (isEdit)
                        delete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void buildChangesDialog() {
        changesDialog = new LazyDialog(this, this, true);
        View changesView = LayoutInflater.from(this).inflate(R.layout.dialog_changeconfirm, null);
        changesDialog.setContainerView(changesView);
        ((TextView) changesView.findViewById(R.id.dialogTvTitle)).setText(getString(R.string.text_delete_address));
        changesView.findViewById(R.id.posijing).setOnClickListener(v -> {
            DeleteAddressRequestModel model = new DeleteAddressRequestModel();
            model.setAddress_id(addressId);
            new ProfileDao(AddressOlshop.this).deleteAddress(model, BaseDao.getInstance(AddressOlshop.this, 723).callback);
        });
        changesView.findViewById(R.id.negajing).setOnClickListener(v -> changesDialog.dismiss());
        changesDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (((BaseResponseModel) br).getMeta().getCode() == 200 || ((BaseResponseModel) br).getMeta().getCode() == 201) {
            if (responseCode == DEL_RC) {
                Toast.makeText(this, "Alamat berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        ProgressDialogComponent.dismissProgressDialog(this);
        ErrorDialog errorDialog = new ErrorDialog(this, this, true, true, "Terjadi Kesalahan", message);
        errorDialog.show();
    }

    private void iniDialog() {
        confirmDialog = new LazyDialog(this, this, true, true);
        confirmDialog.setContainerView(LayoutInflater.from(this).inflate(R.layout.dialog_changeconfirm, null));
        TextView dialogTitle = confirmDialog.findViewById(R.id.dialogTvTitle);
        dialogTitle.setText(getString(R.string.text_delete_address_confirmation));
        confirmDialog.findViewById(R.id.negajing).setOnClickListener(v -> {
            confirmDialog.dismiss();
        });
        confirmDialog.findViewById(R.id.posijing).setOnClickListener(v -> {
            ProgressDialogComponent.showProgressDialog(this, "Mohon Tunggu", false);
            new OlshopDao(this).deleteShippingAddress(String.valueOf(addressModel.getId()), BaseDao.getInstance(this, DEL_RC).callback);
            confirmDialog.dismiss();
        });
    }
}
