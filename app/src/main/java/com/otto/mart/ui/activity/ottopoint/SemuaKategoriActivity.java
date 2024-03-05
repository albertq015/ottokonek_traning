package com.otto.mart.ui.activity.ottopoint;

import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.DealsItem;
import com.otto.mart.ui.Partials.adapter.ottopoint.DealsMenuAdapter;
import com.otto.mart.ui.component.ActionbarOttopointWhite;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SemuaKategoriActivity extends BaseActivity {

    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;
    @BindView(R.id.list)
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_kategori);
        ButterKnife.bind(this);

        viewActionbar.setActionMenuLeft(view -> onBackPressed());

        configureDealsItems();
    }

    private void configureDealsItems(){
        List<DealsItem> mItems = new ArrayList<>();
        //mItems.add(new DealsItem(0, getString(R.string.deal_near_me), getResources().getDrawable(R.drawable.vector_near_me)));
        mItems.add(new DealsItem(1, getString(R.string.deal_trending), getResources().getDrawable(R.drawable.trending_icon)));
        mItems.add(new DealsItem(15, getString(R.string.deal_pulsa), getResources().getDrawable(R.drawable.vector_pulsa)));
        mItems.add(new DealsItem(16, getString(R.string.deal_listrik), getResources().getDrawable(R.drawable.vector_listrik)));
        mItems.add(new DealsItem(17, getString(R.string.deal_games), getResources().getDrawable(R.drawable.vector_games)));
        mItems.add(new DealsItem(2, getString(R.string.deal_kesehatan), getResources().getDrawable(R.drawable.kesehatan_icon)));
        mItems.add(new DealsItem(3, getString(R.string.deal_hiburan), getResources().getDrawable(R.drawable.hiburan_icon)));
        mItems.add(new DealsItem(4, getString(R.string.deal_makanan), getResources().getDrawable(R.drawable.makanan_icon)));
        mItems.add(new DealsItem(5, getString(R.string.deal_belanja), getResources().getDrawable(R.drawable.belanja_icon)));
        mItems.add(new DealsItem(6, getString(R.string.deal_transportasi), getResources().getDrawable(R.drawable.transportasi_icon)));
        mItems.add(new DealsItem(7, getString(R.string.deal_edukasi), getResources().getDrawable(R.drawable.edukasi_icon)));
        mItems.add(new DealsItem(8, getString(R.string.deal_hadiah), getResources().getDrawable(R.drawable.hadiah_icon)));
        mItems.add(new DealsItem(9, getString(R.string.deal_travel), getResources().getDrawable(R.drawable.travel_icon)));
        mItems.add(new DealsItem(10, getString(R.string.deal_otomotif), getResources().getDrawable(R.drawable.otomotif_icon)));
        mItems.add(new DealsItem(11, getString(R.string.deal_olahraga), getResources().getDrawable(R.drawable.olahraga_icon)));
        mItems.add(new DealsItem(12, getString(R.string.deal_fashion), getResources().getDrawable(R.drawable.fashion_icon)));
        mItems.add(new DealsItem(13, getString(R.string.deal_perlengkapan_bayi), getResources().getDrawable(R.drawable.vector_perlengkapan_bayi)));
        mItems.add(new DealsItem(14, getString(R.string.deal_investasi), getResources().getDrawable(R.drawable.vector_investasi)));

        list.setLayoutManager(new GridLayoutManager(SemuaKategoriActivity.this, 4));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(new DealsMenuAdapter(SemuaKategoriActivity.this, mItems));
    }
}
