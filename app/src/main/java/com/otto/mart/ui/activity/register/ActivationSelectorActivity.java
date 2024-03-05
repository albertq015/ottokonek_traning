package com.otto.mart.ui.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.otto.mart.R;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.register.registerFromSales.registerPhoneInput.RegisterPhoneInputActivity;

public class ActivationSelectorActivity extends AppActivity {

    private View back;
    private Button btnSelfActivation;
    private Button btnActivation;

    private boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_selector);

        initComponent();
        initContent();
    }

    private void initComponent() {
        back = findViewById(R.id.toolbar_back_hitbox);
        btnSelfActivation = findViewById(R.id.btn_self_activation);
        btnActivation = findViewById(R.id.btn_activation);
    }

    private void initContent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSelfActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelected){
                    isSelected = true;
                    startActivity(new Intent(ActivationSelectorActivity.this, RegisterParentActivity.class));
                }
            }
        });

        btnActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelected){
                    isSelected = true;
                    startActivity(new Intent(ActivationSelectorActivity.this, RegisterPhoneInputActivity.class));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSelected = false;
    }
}
