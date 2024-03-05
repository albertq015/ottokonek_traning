package com.otto.mart.ui.activity.ottopoint;

import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.RiwayatKuponItem;
import com.otto.mart.ui.Partials.adapter.ottopoint.RiwayatKuponAdapter;
import com.otto.mart.ui.component.ActionbarOttopointWhite;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RiwayatKuponActivity extends BaseActivity {

    private String TAG = RiwayatKuponActivity.class.getSimpleName();

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.view_empty)
    View viewEmpty;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;

    private RiwayatKuponAdapter adapter;
    private List<RiwayatKuponItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_kupon);
        ButterKnife.bind(this);

        setDataDummy();
        configureList();

        //callApiCouponHistory();

        // events

        viewActionbar.setActionMenuLeft(view -> onBackPressed());
    }

    private void configureList(){
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(RiwayatKuponActivity.this));

        adapter = new RiwayatKuponAdapter(RiwayatKuponActivity.this, mItems, data ->
                DetailKuponActivity.moveToHere(
                        RiwayatKuponActivity.this,
                        data.getInt(RiwayatKuponAdapter.KEY_ID),
                        data.getString(RiwayatKuponAdapter.KEY_TITLE)
                )
        );
        list.setAdapter(adapter);
    }

    private void setDataDummy(){
        for (int i = 1; i <= 10; i++) {
            String companyCode = i % 2 != 0 ? "Pede" : "OttoPay";
            mItems.add(new RiwayatKuponItem(i, "20 Mar 2019, 19.15", companyCode, (i * 10000), i % 2 != 0));
        }
    }

    private void showProgress(boolean isShow){
        if(isShow)
            ProgressDialogComponent.showProgressDialog(RiwayatKuponActivity.this, "Loading", false).show();
        else
            ProgressDialogComponent.dismissProgressDialog(RiwayatKuponActivity.this);
    }

    private void callApiCouponHistory(){
        showProgress(true);
    }

    private void showList(boolean isShow){
        if(isShow){
            list.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.GONE);
        }else{
            list.setVisibility(View.GONE);
            viewEmpty.setVisibility(View.VISIBLE);
        }
    }
}
