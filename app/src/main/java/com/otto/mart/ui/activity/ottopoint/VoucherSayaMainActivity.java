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
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.localmodel.ui.DefaultViewPagerItem;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.ui.Partials.adapter.DefaultViewPagerAdapter;
import com.otto.mart.ui.component.ActionbarOttopointWhite;
import com.otto.mart.ui.fragment.ottopoint.VoucherAktifPageFragment;
import com.otto.mart.ui.fragment.ottopoint.VoucherRiwayatPageFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoucherSayaMainActivity extends BaseActivityOttopoint {

    private String TAG = VoucherSayaMainActivity.class.getSimpleName();

    private static final String KEY_SELECTED_PAGE = "KEY_SELECTED_PAGE";

    private static final int pageActive = 0;
    private static final int pageRiwayat = 1;

    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;

    private List<DefaultViewPagerItem> mItemsFragment = new ArrayList<>();

    private boolean isSetToRiwayat = false;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null) return;
            if(intent.getAction() == null) return;

            switch (intent.getAction()){
                case AppKeys.KEY_BROADCAST_REFRESH_POINT:
                    setViewPoint();
                    break;

                // untuk case ini masih belum tau bisa atau tidak, dileh makanya pasang juga di class VoucherAktifPageFragment
                case AppKeys.KEY_BROADCAST_REFRESH_PAGE_VOUCHER_SAYA_BOTH:
                    isSetToRiwayat = true;
                    break;
            }
        }
    };

    private int selectedPage = -1;

    @Override
    protected void onResume() {
        super.onResume();

        if(isSetToRiwayat){
            isSetToRiwayat = false;

            setSelectedTab(pageRiwayat); // select tab riwayat
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_saya_main);
        ButterKnife.bind(this);

        getDataIntent();

        addReceiver();

        getDataIntent();

        configureViewPager();

        // new 8-01-2020
        // set selected tab on view
        if(selectedPage != -1)
            setSelectedTab(selectedPage);

        setViewPoint();

        // events

        viewActionbar.setActionMenuLeft(view -> onBackPressed());
        viewActionbar.setActionDeals(view -> moveToDeals());
        viewActionbar.setActionPointText(view -> moveToPoint());
    }

    @Override
    protected void onDestroy() {
        if(receiver != null) unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void getDataIntent(){
        Intent intent = getIntent();
        if(intent != null){
            Bundle data = intent.getExtras();
            if(data != null){
                if(data.containsKey(KEY_SELECTED_PAGE))
                    selectedPage = data.getInt(KEY_SELECTED_PAGE);
            }
        }
    }

    private void setViewPoint(){
        getBalanceOttoPoint((balance,metaCode) -> {
            try {
                viewActionbar.setTextPoint(CommonHelper.currencyFormat(balance));
            }catch (Exception e){
                LogHelper.showError(TAG, e.getMessage());
            }
        });
    }

    private void addReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppKeys.KEY_BROADCAST_REFRESH_POINT);
        intentFilter.addAction(AppKeys.KEY_BROADCAST_REFRESH_PAGE_VOUCHER_SAYA_BOTH);
        registerReceiver(receiver, intentFilter);
    }

    private void configureViewPager(){
        // create menu / page item
        mItemsFragment.add(new DefaultViewPagerItem(0, getString(R.string.label_aktif), VoucherAktifPageFragment.newInstance(null)));
        mItemsFragment.add(new DefaultViewPagerItem(1, getString(R.string.label_riwayat), VoucherRiwayatPageFragment.newInstance(null)));

        // setup viewpager
        DefaultViewPagerAdapter adapter = new DefaultViewPagerAdapter(getSupportFragmentManager(), mItemsFragment);
        viewpager.setAdapter(adapter);
        tab.setupWithViewPager(viewpager);
    }

    private void moveToDeals(){
        startActivity(new Intent(VoucherSayaMainActivity.this, DealsMainActivtiy.class));
    }

    private void moveToPoint(){
        startActivity(new Intent(VoucherSayaMainActivity.this, RiwayatTransaksiActivity.class));
    }

    public void setSelectedTab(int tabPosition){
        if(tab == null) return;

        TabLayout.Tab targetTab = tab.getTabAt(tabPosition);
        if(targetTab != null) targetTab.select();
    }

    public static void showPage(Context context){
        if(context == null) return;

        Intent intent = new Intent(context, VoucherSayaMainActivity.class);
        context.startActivity(intent);
    }

    public static void showPageRiwayat(Context context){
        if(context == null) return;

        Intent intent = new Intent(context, VoucherSayaMainActivity.class);
        intent.putExtra(KEY_SELECTED_PAGE, pageRiwayat);
        context.startActivity(intent);
    }

    public static void showPageAktiv(Context context){
        if(context == null) return;

        Intent intent = new Intent(context, VoucherSayaMainActivity.class);
        intent.putExtra(KEY_SELECTED_PAGE, pageActive);
        context.startActivity(intent);
    }
}
