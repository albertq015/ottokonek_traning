package com.otto.mart.ui.activity.ottopoint;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.support.util.EditTextHelper;
import com.otto.mart.ui.component.ActionbarOttopointWhite;

import app.beelabs.com.codebase.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivePointActivity extends BaseActivity {

    private String TAG = ActivePointActivity.class.getSimpleName();

    @BindView(R.id.edt_phone_number)
    EditText edtPhoneNumber;
    @BindView(R.id.btn_masuk)
    Button btnMasuk;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_point);
        ButterKnife.bind(this);

        // events

        viewActionbar.setActionMenuLeft(view -> onBackPressed());

        edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                btnMasuk.setEnabled(edtPhoneNumber.getText().toString().length() >= 11);
            }
        });

        btnMasuk.setOnClickListener(view -> actionSubmit());
    }

    private void actionSubmit(){
        if(formInputValidation()){
            Toast.makeText(this, R.string.error_empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        // do action in here
        moveToPinPage();
    }

    private boolean formInputValidation(){
        if(EditTextHelper.getText(edtPhoneNumber).isEmpty())
            return false;

        return true;
    }

    private void moveToPinPage(){
        startActivity(new Intent(ActivePointActivity.this, ActivePointPinActivity.class));
        finish();
    }
}
