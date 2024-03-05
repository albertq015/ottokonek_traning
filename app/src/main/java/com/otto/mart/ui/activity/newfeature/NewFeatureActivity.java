package com.otto.mart.ui.activity.newfeature;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.otto.mart.R;

public class NewFeatureActivity extends AppCompatActivity {

    ViewGroup backhitbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_feature);
        backhitbox = findViewById(R.id.backhitbox);

        backhitbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}