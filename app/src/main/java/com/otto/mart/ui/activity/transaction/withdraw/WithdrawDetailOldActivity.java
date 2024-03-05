package com.otto.mart.ui.activity.transaction.withdraw;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.otto.mart.R;

public class WithdrawDetailOldActivity extends AppCompatActivity {

    private ViewGroup backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_detail_old);
        initComponent();
        initContent();
    }

    private void initComponent() {
        backButton = findViewById(R.id.backhitbox);
    }

    private void initContent() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
