package com.otto.mart.ui.activity.ottopoint;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.otto.mart.R;
import com.otto.mart.support.util.MessageHelper;
import com.otto.mart.ui.component.ActionbarOttopointWhite;

import app.beelabs.com.codebase.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivePointMenuActivity extends BaseActivity {

    private String TAG = ActivePointMenuActivity.class.getSimpleName();

    @BindView(R.id.btn_daftar_pede)
    Button btnDaftarPede;
    @BindView(R.id.btn_active_pede)
    Button btnActivePede;
    @BindView(R.id.btn_daftar_agen)
    Button btnDaftarAgen;
    @BindView(R.id.btn_active_agen)
    Button btnActiveAgen;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_point_menu);
        ButterKnife.bind(this);

        // events

        viewActionbar.setActionMenuLeft(view -> onBackPressed());
        btnDaftarPede.setOnClickListener(view -> MessageHelper.underConstructionMessage(ActivePointMenuActivity.this));
        btnDaftarAgen.setOnClickListener(view -> MessageHelper.underConstructionMessage(ActivePointMenuActivity.this));
        btnActivePede.setOnClickListener(view -> {
            startActivity(new Intent(ActivePointMenuActivity.this, ActivePointActivity.class));
            finish();
        });
        btnActiveAgen.setOnClickListener(view -> MessageHelper.underConstructionMessage(ActivePointMenuActivity.this));
    }
}
