package com.otto.mart.ui.activity.profile;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.UpdatePinRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.DataEmptyResponseModel;
import com.otto.mart.presenter.dao.ProfileDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.activity.BaseSessioncheckActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class ProfileChangePinActivity extends BaseSessioncheckActivity {

    private Window thiswindow;
    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private Dialog confirmDialog;
    private EditText etPinOld;
    private EditText etNewPin;
    private EditText etNewPinConfirm;
    private Button btnSubmit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thiswindow = this.getWindow();
        setContentView(R.layout.activity_change_pin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thiswindow.setStatusBarColor(ContextCompat.getColor(this, R.color.prussian_blue));
        }
        initComponent();
        initContent();
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        etPinOld = findViewById(R.id.etPinOld);
        etNewPin = findViewById(R.id.etNewPin);
        etNewPinConfirm = findViewById(R.id.etNewPinConfirm);
        btnSubmit = findViewById(R.id.btnSubmit);
        confirmDialog = new Dialog(this);

        confirmDialog.setContentView(LayoutInflater.from(this).inflate(R.layout.dialog_changeconfirm, null));
    }

    private void initContent() {
        tvToolbarTitle.setText(getString(R.string.label_reset_pin));

        btnToolbarBack.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            UpdatePinRequestModel model = new UpdatePinRequestModel();
            model.setOld_pin(etPinOld.getText().toString());
            model.setNew_pin(etNewPin.getText().toString());
            if (model.getNew_pin().equals(etNewPinConfirm.getText().toString())) {
                if (model.getNew_pin() != null && model.getNew_pin().length() > 5) {
                    ProgressDialogComponent.showProgressDialog(ProfileChangePinActivity.this, getString(R.string.loading_message), false);
                    new ProfileDao(ProfileChangePinActivity.this).updatePin(model, BaseDao.getInstance(ProfileChangePinActivity.this, 322).callback);
                } else {
                    Toast.makeText(ProfileChangePinActivity.this, "PIN kurang dari 6 karakter, mohon input ulang PIN anda", Toast.LENGTH_SHORT).show();
                    etPinOld.setText("");
                    etNewPin.setText("");
                    etNewPinConfirm.setText("");
                }
            } else {
                Toast.makeText(ProfileChangePinActivity.this, "Input ulang PIN anda tidak sesuai, mohon input ulang", Toast.LENGTH_SHORT).show();
                etNewPinConfirm.setText("");
            }
        });
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(ProfileChangePinActivity.this);
        if (response.isSuccessful()) {
            if (responseCode == 322) {
                if (((DataEmptyResponseModel) br).getMeta().getCode() == 200) {
                    BaseResponseModel model = (BaseResponseModel) br;
                    showNotifyDialog("Change Pin success, Please login using the New Pin");
                    SessionManager.setSecondaryToken(etNewPinConfirm.getText().toString());
                } else {
                    ErrorDialog errorDialog = new ErrorDialog(ProfileChangePinActivity.this, ProfileChangePinActivity.this, false, false, "Gagal Mengubah PIN", ((BaseResponseModel) br).getMeta().getMessage());
                    errorDialog.show();
                }
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
       // super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    private void showNotifyDialog(String message) {
        ((TextView) confirmDialog.findViewById(R.id.dialogTvTitle)).setText(message);
        ((TextView) confirmDialog.findViewById(R.id.negajing)).setVisibility(View.GONE);
        ((TextView) confirmDialog.findViewById(R.id.posijing)).setText(getString(R.string.text_ok));
        confirmDialog.show();
        ((TextView) confirmDialog.findViewById(R.id.posijing)).setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        if (confirmDialog != null) {
            confirmDialog.dismiss();
        }
        super.onDestroy();
    }
}
