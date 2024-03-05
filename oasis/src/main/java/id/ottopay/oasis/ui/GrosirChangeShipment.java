package id.ottopay.oasis.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;
import com.otto.mart.model.APIModel.Request.AddressEditRequestModel;
import com.otto.mart.model.APIModel.Request.grosir.AddressCreateUpdateDeleteRequest;
import com.otto.mart.model.APIModel.Request.olshop.AddressRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.presenter.dao.olshop.GrosirDao;
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
import com.otto.mart.R;
import retrofit2.Response;

public class GrosirChangeShipment extends AppActivity {

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
    private CheckBox isPrimary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_shipment);

        iniDialog();
        initComponent();
        initContent();
        //buildChangesDialog();
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
        isPrimary = findViewById(R.id.is_primary);
    }

    private void initContent() {
        addressFragment = new RegisterAddressFragment();
        final GrosirAddressPicker alamatPickerFragment = new GrosirAddressPicker();
        Bundle bundle = new Bundle();
        fullAddress.setVisibility(View.VISIBLE);

        if (getIntent().hasExtra("address")) {
            isEdit = true;
            addressModel = new Gson().fromJson(getIntent().getStringExtra("address"), ShippingAddressData.class);
            addressName.setContentText(addressModel.getName());
            //fullAddress.setContentText(addressModel.getDetail());
            postalCode.setContentText(String.valueOf(addressModel.getZip_code()));
            addressId = addressModel.getId();
            String[] state = new String[4];
            state[0] = addressModel.getRegion().getName();
            state[1] = addressModel.getProvince().getName();
            state[2] = addressModel.getMunicipality().getName();
            state[3] = addressModel.getDetail();
            isPrimary.setChecked(addressModel.isIs_primary());
            bundle.putStringArray("kkkpos", state);
            if (String.valueOf(addressModel.getZip_code()).length() == 3) {
                save.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }

            fullAddress.setContentText(addressModel.getDetail());

        }

        alamatPickerFragment.setArguments(bundle);
        alamatPickerFragment.setAlamatPickerCallback(new GrosirAddressPicker.AlamatPickerCallback(){

            @Override
            public void onFormComplete(AddressRequestModel addressModel) {
                selectedAddressModel = addressModel;
                postalCode.setVisibility(View.VISIBLE);
                if (postalCode.getTextContent().length() >= 3) {
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
                ((EditText) addressName.getComponent()).setError(getString(com.otto.mart.R.string.nama_alamat_wajib_diisi));
            } else ((EditText) addressName.getComponent()).setError(null);

            if (fullAddress.getTextContent().isEmpty()) {
                ((EditText) fullAddress.getComponent()).setError(getString(com.otto.mart.R.string.alamat_lengkap_wajib_diisi));
            } else ((EditText) fullAddress.getComponent()).setError(null);

            if (addressName.getTextContent().isEmpty() || fullAddress.getTextContent().isEmpty())
                return;

            AddressCreateUpdateDeleteRequest model = new AddressCreateUpdateDeleteRequest();
            //ShippingAddressData data = new ShippingAddressData();
            //data.setCity(selectedAddressModel.getCity());
            model.setDistrict_id(selectedAddressModel.getDistrictId());
            model.setName(addressName.getTextContent());
            model.setDetail(fullAddress.getTextContent());
            model.setZip_code(postalCode.getTextContent());
            model.setMunicipality_code(selectedAddressModel.getMunicipality_code());
            model.setIs_primary(isPrimary.isChecked());
            //model.setShipping_address(data);

            ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);
            if (isEdit) {
                model.setId(Long.parseLong(getIntent().getIntExtra("id",0)+""));
                new GrosirDao(this).updateShippingAddress(model, BaseDao.getInstance(this, UPD_RC).callback);
            } else
                new GrosirDao(this).createShippingAddress(model, BaseDao.getInstance(this, CRE_RC).callback);
            selectedAddressModel.setAddress(fullAddress.getTextContent());
            selectedAddressModel.setPostalCode(postalCode.getTextContent());

            getIntent().putExtra("data", selectedAddressModel);

            /*setResult(RESULT_OK, getIntent());
            finish();*/

        });
        delete.setOnClickListener(v -> confirmDialog.show());

        backHitBox.setOnClickListener(v -> finish());

        /*confirm.setOnClickListener(v -> {
            if (isSave) {
                new GrosirDao(GrosirChangeShipment.this).updateShippingAddress(GrosirChangeShipment.this, requestModel,
                        BaseDao.getInstance(GrosirChangeShipment.this, API_KEY_UPDATE_ADDRESS).callback);
            } else finish();
        });*/
        dismiss.setOnClickListener(v -> confirmDialog.dismiss());

        ((EditText) postalCode.getComponent()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 2) {
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


    /*private void buildChangesDialog() {

        changesDialog = new LazyDialog(this, this, true);
        changesDialog.setContainerView(LayoutInflater.from(this).inflate(R.layout.dialog_changeconfirm, null));
        ((TextView) changesDialog.getParentView().findViewById(R.id.titleDialog)).setText("Delete Alamat?");
        changesDialog.getParentView().findViewById(R.id.posijing).setOnClickListener(v -> {
            AddressCreateUpdateDeleteRequest model = new AddressCreateUpdateDeleteRequest();
            model.setId(addressId);
            new GrosirDao(GrosirChangeShipment.this).deleteShippingAddress(model, BaseDao.getInstance(GrosirChangeShipment.this, 723).callback);
        });
        changesDialog.getParentView().findViewById(R.id.negajing).setOnClickListener(v -> changesDialog.dismiss());
        changesDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }*/

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            if(((BaseResponseModel) br).getMeta().getCode() == 400) {
                if (responseCode == CRE_RC) {
                    ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    });
                    dialog.show();
                    return;
                }
            }
            if (responseCode == DEL_RC) {
                Toast.makeText(this, com.otto.mart.R.string.alamat_berhasil_dihapus, Toast.LENGTH_SHORT).show();
            }
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        ProgressDialogComponent.dismissProgressDialog(this);
        ErrorDialog errorDialog = new ErrorDialog(this, this, true, true, getString(R.string.error_msg_something_wrong), message);
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
            ProgressDialogComponent.showProgressDialog(this, getString(R.string.text_please_wait), false);
            AddressCreateUpdateDeleteRequest model = new AddressCreateUpdateDeleteRequest();
            model.setId(addressId);
            new GrosirDao(this).deleteShippingAddress(model, BaseDao.getInstance(this, DEL_RC).callback);
            confirmDialog.dismiss();
        });
    }
}
