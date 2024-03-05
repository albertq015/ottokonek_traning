package com.otto.mart.ui.activity.ottopoint;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.ui.component.ActionbarOttopointWhite;
import com.otto.mart.ui.component.dialog.SuccesActivatePointDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailPoinActivity extends BaseActivityOttopoint {

    private String TAG = DetailPoinActivity.class.getSimpleName();

    public static final String KEY_BROADCAST_SUCCESS_ACTIVATION_PEDE = "broadcast_success_activation_pede";

    @BindView(R.id.button_active)
    Button buttonActive;
    @BindView(R.id.view_poin)
    View viewPoin;
    @BindView(R.id.tv_poin_ottopoint)
    TextView tvPoinOttopoint;
    @BindView(R.id.tv_poin_ottopay)
    TextView tvPoinOttopay;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_poin);
        ButterKnife.bind(this);

        // register broadcast receiver
        registerReceiver(broadcastReceiver, new IntentFilter(KEY_BROADCAST_SUCCESS_ACTIVATION_PEDE));

        // events

        viewActionbar.setActionMenuLeft(view -> onBackPressed());
        buttonActive.setOnClickListener(view -> startActivity(new Intent(DetailPoinActivity.this, ActivePointMenuActivity.class)));
        viewPoin.setOnClickListener(view -> moveToRiwayatTransaksi());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            new SuccesActivatePointDialog(DetailPoinActivity.this).show();
        }
    };

    private void moveToRiwayatTransaksi(){
        startActivity(new Intent(DetailPoinActivity.this, RiwayatTransaksiActivity.class));
    }
}
