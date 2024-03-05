package com.otto.mart.ui.activity.ottopoint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.DefaultViewPagerItem;
import com.otto.mart.ui.Partials.adapter.DefaultViewPagerAdapter;
import com.otto.mart.ui.fragment.ottopoint.DetailKuponPageFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailKuponActivity extends BaseActivityOttopoint {

    private String TAG = DetailKuponActivity.class.getSimpleName();

    public static final String KEY_ID = "key_id";
    public static final String KEY_TITLE = "key_title";

    @BindView(R.id.view_back)
    View viewBack;
    @BindView(R.id.tv_title_page)
    TextView tvTitlePage;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private int id = -1;
    private String title = "";
    private List<DefaultViewPagerItem> mItemsFragment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kupon);
        ButterKnife.bind(this);

        getDataIntent();

        // set header view
        tvTitlePage.setVisibility(View.GONE);
        tvTitle.setText(title);

        configureViewPager();

        // events

        viewBack.setOnClickListener(view -> onBackPressed());
    }

    private void configureViewPager(){
        // create menu / page item
        Bundle dataDesc = new Bundle();
        dataDesc.putString(DetailKuponPageFragment.KEY_TITLE, getString(R.string.label_deskripsi));
        dataDesc.putString(DetailKuponPageFragment.KEY_DESC, getString(R.string.text_sample));
        mItemsFragment.add(new DefaultViewPagerItem(0, getString(R.string.label_deskripsi), DetailKuponPageFragment.newInstance(dataDesc)));

        Bundle dataSyarat = new Bundle();
        dataSyarat.putString(DetailKuponPageFragment.KEY_TITLE, getString(R.string.label_syarat_ketentuan));
        dataSyarat.putString(DetailKuponPageFragment.KEY_DESC, getString(R.string.text_sample));
        mItemsFragment.add(new DefaultViewPagerItem(1, getString(R.string.label_syarat), DetailKuponPageFragment.newInstance(dataSyarat)));

        Bundle dataCaraPakai = new Bundle();
        dataCaraPakai.putString(DetailKuponPageFragment.KEY_TITLE, getString(R.string.label_cara_pakai));
        dataCaraPakai.putString(DetailKuponPageFragment.KEY_DESC, getString(R.string.text_sample));
        mItemsFragment.add(new DefaultViewPagerItem(2, getString(R.string.label_cara_pakai), DetailKuponPageFragment.newInstance(dataCaraPakai)));

        // setup viewpager
        DefaultViewPagerAdapter adapter = new DefaultViewPagerAdapter(getSupportFragmentManager(), mItemsFragment);
        viewpager.setAdapter(adapter);
        tab.setupWithViewPager(viewpager);
    }

    public void getDataIntent(){
        Intent intent = getIntent();
        if(intent != null){
            Bundle data = intent.getExtras();
            if(data != null){
                id = data.getInt(KEY_ID);
                title = data.getString(KEY_TITLE);
            }
        }
    }

    public static void moveToHere(Context context, int id, String title){
        if(context == null) return;

        Intent intent = new Intent(context, DetailKuponActivity.class);
        intent.putExtra(DetailKuponActivity.KEY_ID, id);
        intent.putExtra(DetailKuponActivity.KEY_TITLE, title);
        context.startActivity(intent);
    }
}
