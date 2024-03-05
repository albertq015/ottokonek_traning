package com.otto.mart.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.widget.LinearLayout;

import com.otto.mart.R;
import com.otto.mart.ui.fragment.reusable.AlamatPickerFragment;

import app.beelabs.com.codebase.base.BaseActivity;

public class FragmentTestingActivity extends BaseActivity {

    private LinearLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout._debug_activity);
        initComponent();
        initContent();
    }

    private void initComponent() {
        container = findViewById(R.id.layout_fragmentContainer);
    }

    private void initContent() {
        AlamatPickerFragment fragment = new AlamatPickerFragment();
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().
                replace(container.getId(), fragment).
                commit();
    }
}
