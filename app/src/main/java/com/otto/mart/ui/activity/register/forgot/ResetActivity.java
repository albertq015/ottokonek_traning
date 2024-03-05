package com.otto.mart.ui.activity.register.forgot;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.otto.mart.R;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.LazyEdittext;

public class ResetActivity extends AppActivity {

    private Window thiswindow;
    private View backBtn, action;
    private LazyEdittext pin, confirmPin;
    private LazyDialog hundDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thiswindow = this.getWindow();
        setContentView(R.layout.activity_forgot_pin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thiswindow.setStatusBarColor(ContextCompat.getColor(this, R.color.prussian_blue));
        }
        initComponent();
        initContent();
    }

    private void initComponent() {
        backBtn = findViewById(R.id.backhitbox);
        action = findViewById(R.id.action);
        pin = findViewById(R.id.pin);
        confirmPin = findViewById(R.id.pin_confirm);
        ((EditText) pin.getComponent()).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        ((EditText) confirmPin.getComponent()).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private void initContent() {
        initDialog();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hundDialog.show();
            }
        });
    }

    @Override
    protected void onPause() {
        if (hundDialog.isShowing()) {
            hundDialog.dismiss();
        }
        super.onPause();
    }

    private void initDialog() {
        hundDialog = new LazyDialog(this, this, true);
        hundDialog.setContainerView(LayoutInflater.from(this).inflate(R.layout.dialog_forgot_succ, null));
        hundDialog.setNavigationView(LayoutInflater.from(this).inflate(R.layout.cw_navigation_1btn_cust, null)
                , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent jenk = new Intent(ResetActivity.this, LoginActivity.class);
                        startActivity(jenk);
                    }
                });
    }
}