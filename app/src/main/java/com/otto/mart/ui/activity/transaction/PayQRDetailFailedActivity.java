package com.otto.mart.ui.activity.transaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;

import app.beelabs.com.codebase.base.BaseActivity;

public class PayQRDetailFailedActivity extends BaseActivity {

    ImageView closeButton;
    TextView message, tryAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_qrdetail_failed);

        initComponent();
        initContent();
    }

    private void initComponent() {
        closeButton = findViewById(R.id.closeButton);
        message = findViewById(R.id.message);
        tryAgain = findViewById(R.id.tryAgain);
    }

    private void initContent() {
        if (getIntent().hasExtra("message")) {
            message.setText(getIntent().getStringExtra("message"));
        }

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
