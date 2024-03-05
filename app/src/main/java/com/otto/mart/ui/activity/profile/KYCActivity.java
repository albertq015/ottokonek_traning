package com.otto.mart.ui.activity.profile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.otto.mart.R;
import com.otto.mart.ui.Partials.adapter.PagerAdapter;
import com.otto.mart.ui.fragment.kyc.IntroKYCFragment;
import com.otto.mart.ui.fragment.kyc.UpgKYCFragment;

public class KYCActivity extends AppCompatActivity {

    ViewPager pager;
    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);
        initComponent();
        initContent();
    }

    private void initComponent() {
        pager = findViewById(R.id.pager);
    }

    private void initContent() {
        adapter = new PagerAdapter(getSupportFragmentManager());

        Fragment intro = new IntroKYCFragment();
        Fragment upg = new UpgKYCFragment();

        adapter.addFragment(intro);
        adapter.addFragment(upg);

        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if (position == 0) {
//                    StatusBarUtil.setColor(KYCActivity.this, ContextCompat.getColor(KYCActivity.this, android.R.color.white));
//                } else if (position == 1) {
//                    StatusBarUtil.setColor(KYCActivity.this, ContextCompat.getColor(KYCActivity.this, R.color.ocean_blue));
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
