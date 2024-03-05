package com.otto.mart.ui.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.otto.mart.OttoMartApp;
import com.otto.mart.R;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.fragment.reusable.AlamatPickerFragment;

import app.beelabs.com.codebase.base.BaseActivity;

public class RegisterAddressInputActivity extends AppActivity {

    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private LinearLayout fragmentContainer;
    private View navbar, navbar_extender;
    private Button btnSubmit;

    private AlamatPickerFragment fragment;

    private String inputtedAddress;
    private long[] returnId, returnPos;

    private boolean hasState = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getaddress);

        if (getIntent().hasExtra("address")) {
            inputtedAddress = getIntent().getStringExtra("address");
        } else {
            Toast.makeText(this, "Maaf, data tidak lengkap", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().hasExtra("kkkpos")) {
            hasState = true;
            returnPos = getIntent().getLongArrayExtra("kkkpos");
        }

        initComponent();
        initContent();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        fragmentContainer = findViewById(R.id.layout_container);
        navbar = findViewById(R.id.navbar);
        navbar_extender = findViewById(R.id.jenktot2);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setVisibility(View.GONE);
    }

    private void initContent() {
        tvToolbarTitle.setText(getString(R.string.label_add_address));
        fragment = new AlamatPickerFragment();
        Bundle hundBundle = new Bundle();
        hundBundle.putString("address", inputtedAddress);
        hundBundle.putBoolean("isAddAddress",getIntent().hasExtra("isAddAddress"));
        if (hasState) {
            hundBundle.putLongArray("kkkpos", returnPos);
        }
        fragment.setArguments(hundBundle);
        fragment.setAlamatPickerCallback(new AlamatPickerFragment.AlamatPickerCallback() {

            @Override
            public void onFormComplete(String address, long[] collectionResult, long[] positionResult) {
                btnSubmit.setVisibility(View.VISIBLE);
                returnId = collectionResult;
                returnPos = positionResult;
                inputtedAddress = fragment.getAddress();
            }

            @Override
            public void onFromUncompete() {
                btnSubmit.setVisibility(View.GONE);
            }
        });
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().replace(fragmentContainer.getId(), fragment).commit();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment.isValid()) {
                    inputtedAddress = fragment.getAddress();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("address", inputtedAddress);
                    returnIntent.putExtra("kkkid", returnId);
                    returnIntent.putExtra("kkkpos", returnPos);
                    if (getIntent().hasExtra("isAddAddress"))
                        returnIntent.putExtra("addressName",fragment.getAddressName());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(RegisterAddressInputActivity.this, "Masih ada field yang masih kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!((OttoMartApp) getApplication()).isHasSoftwareKeys()) {
            navbar.setVisibility(View.GONE);
            navbar_extender.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }
}
