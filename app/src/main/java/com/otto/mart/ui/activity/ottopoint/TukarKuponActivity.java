package com.otto.mart.ui.activity.ottopoint;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.DefaultViewPagerItem;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.ui.Partials.adapter.DefaultViewPagerAdapter;
import com.otto.mart.ui.component.ActionbarOttopointWhite;
import com.otto.mart.ui.fragment.ottopoint.TukarKuponPageFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.otto.mart.keys.AppKeys.KEY_BROADCAST_REFRESH_POINT;

public class TukarKuponActivity extends BaseActivityOttopoint {

    private String TAG = TukarKuponActivity.class.getSimpleName();

    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;

    private double poin_ottopoint = 0;

    private List<DefaultViewPagerItem> mItemsFragment = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukar_kupon);
        ButterKnife.bind(this);

        registerReceiver(broadcastReceiver, new IntentFilter(KEY_BROADCAST_REFRESH_POINT));

        getBalanceOttoPoint((balance,metaCode) -> viewActionbar.setTextPoint(CommonHelper.currencyFormat(poin_ottopoint = balance)));

        configureViewPager();

        // events

        viewActionbar.setActionMenuLeft(view -> onBackPressed());
        viewActionbar.setActionPointText(view -> moveToPointPage());
        viewActionbar.setActionTukarKupon(view -> moveToRiwayatKupon());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }

    private void configureViewPager(){
        // create menu / page item
        mItemsFragment.add(new DefaultViewPagerItem(0, getString(R.string.label_eksplore), TukarKuponPageFragment.newInstance(TukarKuponPageFragment.CODE_TYPE_EXPLORE)));
        mItemsFragment.add(new DefaultViewPagerItem(1, getString(R.string.label_kupon_saya), TukarKuponPageFragment.newInstance(TukarKuponPageFragment.CODE_TYPE_KUPONSAYA)));

        // setup viewpager
        DefaultViewPagerAdapter adapter = new DefaultViewPagerAdapter(getSupportFragmentManager(), mItemsFragment);
        viewpager.setAdapter(adapter);
        tab.setupWithViewPager(viewpager);

        viewpager.setOffscreenPageLimit(0);
    }

    private void moveToPointPage(){
        startActivity(new Intent(TukarKuponActivity.this, RiwayatTransaksiActivity.class));
    }

    private void moveToRiwayatKupon(){
        startActivity(new Intent(TukarKuponActivity.this, RiwayatKuponActivity.class));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent == null) return;

            if(intent.getAction() == null) return;

            switch (intent.getAction()){
                case KEY_BROADCAST_REFRESH_POINT:
                    getBalanceOttoPoint((balance,metaCode) -> {
                        viewActionbar.setTextPoint(CommonHelper.currencyFormat(poin_ottopoint = balance));
                    });
                    break;
            }
        }
    };

    public double getPoin_ottopoint() {
        return poin_ottopoint;
    }
}
