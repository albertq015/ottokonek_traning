package com.otto.mart.ui.activity.ottopoint;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.localmodel.ui.DefaultViewPagerItem;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.ui.Partials.adapter.DefaultViewPagerAdapter;
import com.otto.mart.ui.component.ActionbarOttopointWhite;
import com.otto.mart.ui.fragment.ottopoint.ComingSoonDealsFragment;
import com.otto.mart.ui.fragment.ottopoint.DealsPageFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DealsMainActivtiy extends BaseActivityOttopoint {

    private String TAG = DealsMainActivtiy.class.getSimpleName();

    public static final int CODE_PAGE_ALL = 0;
    public static final int CODE_PAGE_NEAR = 1;
    public static final int CODE_PAGE_OTHERS = 3;

    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;

    private String typeVoucher = "";

    private boolean doResetList = false;

    private List<DefaultViewPagerItem> mItemsFragment = new ArrayList<>();

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            if (intent.getAction() == null) return;

            switch (intent.getAction()) {
                case AppKeys.KEY_BROADCAST_REFRESH_POINT:
                    setViewPoint();
                    break;
            }
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_main_activtiy);
        ButterKnife.bind(this);

        getDataIntent();

        addReceiver();

        configureViewPager();

        setViewPoint();

        // events

        viewActionbar.setActionMenuLeft(view -> onBackPressed());
        viewActionbar.setActionPointText(view -> moveToPointPage());
        viewActionbar.setActionVoucherSayaDetail(view -> moveToVoucher());
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                if (data.containsKey(AppKeys.KEY_OTTOPOINT_DEALS_TYPE))
                    typeVoucher = data.getString(AppKeys.KEY_OTTOPOINT_DEALS_TYPE);
            }
        }
    }

    private void moveToPointPage() {
        startActivity(new Intent(DealsMainActivtiy.this, RiwayatTransaksiActivity.class));
    }

    private void moveToVoucher() {
        startActivity(new Intent(DealsMainActivtiy.this, VoucherSayaMainActivity.class));
    }

    private void setViewPoint() {
        getBalanceOttoPoint((balance, metaCode) -> viewActionbar.setTextPoint(CommonHelper.currencyFormat(balance)));
    }

    private void addReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppKeys.KEY_BROADCAST_REFRESH_POINT);
        registerReceiver(receiver, intentFilter);
    }

    private void configureViewPager() {
        // create menu / page item
        mItemsFragment.add(new DefaultViewPagerItem(CODE_PAGE_ALL, getString(R.string.deal_all), DealsPageFragment.newInstance(CODE_PAGE_ALL, typeVoucher)));
        //mItemsFragment.add(new DefaultViewPagerItem(CODE_PAGE_NEAR, getString(R.string.deal_near_me), DealsPageFragment.newInstance(CODE_PAGE_NEAR)));
        mItemsFragment.add(new DefaultViewPagerItem(2, getString(R.string.deal_trending), ComingSoonDealsFragment.newInstance()));
        mItemsFragment.add(new DefaultViewPagerItem(3, getString(R.string.deal_kesehatan), ComingSoonDealsFragment.newInstance()));
        mItemsFragment.add(new DefaultViewPagerItem(4, getString(R.string.deal_hiburan), ComingSoonDealsFragment.newInstance()));
        mItemsFragment.add(new DefaultViewPagerItem(5, getString(R.string.deal_makanan), ComingSoonDealsFragment.newInstance()));
        mItemsFragment.add(new DefaultViewPagerItem(6, getString(R.string.deal_belanja), ComingSoonDealsFragment.newInstance()));
        mItemsFragment.add(new DefaultViewPagerItem(7, getString(R.string.deal_transportasi), ComingSoonDealsFragment.newInstance()));
        mItemsFragment.add(new DefaultViewPagerItem(8, getString(R.string.deal_hadiah), ComingSoonDealsFragment.newInstance()));
        mItemsFragment.add(new DefaultViewPagerItem(9, getString(R.string.deal_travel), ComingSoonDealsFragment.newInstance()));
        mItemsFragment.add(new DefaultViewPagerItem(10, getString(R.string.deal_otomotif), ComingSoonDealsFragment.newInstance()));
        mItemsFragment.add(new DefaultViewPagerItem(11, getString(R.string.deal_olahraga), ComingSoonDealsFragment.newInstance()));
        mItemsFragment.add(new DefaultViewPagerItem(12, getString(R.string.deal_fashion), ComingSoonDealsFragment.newInstance()));

        // setup viewpager
        DefaultViewPagerAdapter adapter = new DefaultViewPagerAdapter(getSupportFragmentManager(), mItemsFragment);
        viewpager.setAdapter(adapter);
        tab.setupWithViewPager(viewpager);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = adapter.getItem(0);

                if (fragment instanceof DealsPageFragment && tab.getPosition() != 0 && !doResetList) {
                    doResetList = true;

                    ((DealsPageFragment) fragment).resetPage();
                    ((DealsPageFragment) fragment).resetListItem(false);
                } else if (tab.getPosition() == 0) {
                    doResetList = false;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}