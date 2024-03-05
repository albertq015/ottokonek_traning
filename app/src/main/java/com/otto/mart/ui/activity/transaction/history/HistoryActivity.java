package com.otto.mart.ui.activity.transaction.history;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.ui.Partials.adapter.FragmentPagerAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.fragment.transaction.history.TransactionHistoryFragment;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public class HistoryActivity extends AppActivity {

    public static final String TAB_LABEL_TRRANSAKSI = "Transaksi";
    public static final String TAB_LABEL_OMZET = "Omzet";

    public static final String KEY_TITLE = "title";

    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private LinearLayout btnTab1;
    private LinearLayout btnTab2;
    private TextView tvTab1;
    private TextView tvTab2;
    private View tabIndicator1;
    private View tabIndicator2;
    private ViewAnimator viewAnimator;
    private TextView tvTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    int mWalletId = -1;
    boolean isFromTransaction = false;

    String mTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (getIntent().hasExtra("walletId")) {
            isFromTransaction = true;
        }

        if (getIntent().hasExtra(KEY_TITLE)) {
            mTitle = getIntent().getStringExtra(KEY_TITLE);
        }

        initView();
        //getWallet();
        setupViewPager();
    }

    private void initView() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        btnTab1 = findViewById(R.id.btnTab1);
        btnTab2 = findViewById(R.id.btnTab2);
        tvTab1 = findViewById(R.id.tvTab1);
        tvTab2 = findViewById(R.id.tvTab2);
        tabIndicator1 = findViewById(R.id.tabIndicator1);
        tabIndicator2 = findViewById(R.id.tabIndicator2);
        viewAnimator = findViewById(R.id.view_animator);
        tvTitle = findViewById(R.id.tv_title);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tvToolbarTitle.setText(getString(R.string.title_activity_history));
        tvTab1.setText(TAB_LABEL_TRRANSAKSI);
        tvTab2.setText(TAB_LABEL_OMZET);

        tvToolbarTitle.setText(getString(R.string.text_history_space) + mTitle);

        btnToolbarBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnTab1.setOnClickListener(v -> {
           tabSelected(0);
        });

        btnTab2.setOnClickListener(v -> {
            tabSelected(1);
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            public void onPageSelected(int position) {
                tabSelected(position);
            }
        });
    }

    private void tabSelected(int tabPosition) {
        tabIndicator1.setVisibility(View.INVISIBLE);
        tabIndicator2.setVisibility(View.INVISIBLE);

        if (tabPosition == 0) {
            tabIndicator1.setVisibility(View.VISIBLE);
        } else {
            tabIndicator2.setVisibility(View.VISIBLE);
        }

        viewPager.setCurrentItem(tabPosition);
    }

    private void setupViewPager() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager());

        TransactionHistoryFragment omzetHistory = new TransactionHistoryFragment();
        Bundle omzetBundle = new Bundle();
        omzetBundle.putInt(AppKeys.KEY_TAB_POSITION, 0);
        omzetHistory.setArguments(omzetBundle);

        TransactionHistoryFragment transactionHistory = new TransactionHistoryFragment();
        Bundle transactionBundle = new Bundle();
        transactionBundle.putInt(AppKeys.KEY_WALLET_ID, mWalletId);
        transactionBundle.putInt(AppKeys.KEY_TAB_POSITION, 1);
        transactionHistory.setArguments(transactionBundle);

        if(mTitle.equals(TAB_LABEL_OMZET)){
            adapter.addFragment(omzetHistory, TAB_LABEL_OMZET);
        } else {
            adapter.addFragment(transactionHistory, TAB_LABEL_TRRANSAKSI);
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

//        if (isFromTransaction) {
//            viewPager.setCurrentItem(1);
//        }

        viewAnimator.setDisplayedChild(1);
    }

    private void getWallet() {
        new WalletDao(this).emoneySummary(BaseDao.getInstance(HistoryActivity.this, 999).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (response.isSuccessful()) {
            if (responseCode == 999) {
                mWalletId = ((WalletResponseModel) br).getData().get(0).getId();
                setupViewPager();
            }
        } else {
            onApiResponseError();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        onApiResponseError();
    }
}