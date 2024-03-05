package com.otto.mart.ui.activity.ppob.product.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagDenomResponseModel;
import com.otto.mart.presenter.dao.PpobDao;
import com.otto.mart.support.util.widget.recyclerView.ItemClickSupport;
import com.otto.mart.ui.Partials.adapter.voucherGame.VoucherGameAdapter;
import com.otto.mart.ui.Partials.adapter.voucherGame.VoucherGamePopularAdapter;
import com.otto.mart.ui.activity.PpobActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class VoucherGameActivity extends PpobActivity {

    private final int RC_GET_VOUCHER_GAME_LIST = 100;

    private LinearLayout btnToolbarTitle;
    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private LinearLayout btnToolbarRight;
    private ImageView imgToolbarRight;
    private ViewAnimator viewAnimator;
    private RecyclerView rvPopularProduct;
    private RecyclerView rvAllProduct;
    private LinearLayout searchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_game);

        initView();
        initRecyclerView();
        callGetVoucherGameProductList();
    }

    private void initView() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        btnToolbarTitle = findViewById(R.id.btnToolbarTitle);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        btnToolbarRight = findViewById(R.id.btnToolbarRight);
        imgToolbarRight = findViewById(R.id.imgToolbarRight);
        viewAnimator = findViewById(R.id.view_animator);
        rvPopularProduct = findViewById(R.id.rv_popular_product);
        rvAllProduct = findViewById(R.id.rv_all_product);
        searchLayout = findViewById(R.id.search_layout);

        tvToolbarTitle.setText(getString(R.string.title_activity_voucher_game));
        imgToolbarRight.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_all_menu_ppob));
        btnToolbarRight.setVisibility(View.VISIBLE);

        btnToolbarRight.setOnClickListener(v -> {
            showAllPpobMenu(getString(R.string.title_activity_voucher_game));
        });

        btnToolbarBack.setOnClickListener(v -> {
            onBackPressed();
        });

        searchLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchVoucherGameActivity.class));
        });
    }

    private void initRecyclerView() {
        rvPopularProduct.setHasFixedSize(true);
        rvAllProduct.setHasFixedSize(true);

        final GridLayoutManager mainMenuLayoutManager = new GridLayoutManager(this, 2);
        rvPopularProduct.setLayoutManager(mainMenuLayoutManager);
        rvPopularProduct.setNestedScrollingEnabled(false);

        final LinearLayoutManager menuListLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAllProduct.setLayoutManager(menuListLayoutManager);
        rvAllProduct.setNestedScrollingEnabled(false);
    }

    private void displayPopularProduct(List<OttoagDenomModel> productList) {
        List<OttoagDenomModel> ottoagDenomModelList = new ArrayList();

        for (OttoagDenomModel voucherGameData : productList) {
            if(voucherGameData.isDirect()){
                ottoagDenomModelList.add(voucherGameData);
            }
        }

        VoucherGamePopularAdapter adapter = new VoucherGamePopularAdapter(this, ottoagDenomModelList);
        rvPopularProduct.setAdapter(adapter);

        ItemClickSupport.addTo(rvPopularProduct).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                gotoVoucherGamePayment(ottoagDenomModelList.get(position));
            }
        });

        viewAnimator.setDisplayedChild(1);
    }

    private void displayAllProduct(List<OttoagDenomModel> productList) {
        VoucherGameAdapter adapter = new VoucherGameAdapter(this, productList);
        rvAllProduct.setAdapter(adapter);

        ItemClickSupport.addTo(rvAllProduct).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                gotoVoucherGamePayment(productList.get(position));
            }
        });

        viewAnimator.setDisplayedChild(1);
    }

    private void gotoVoucherGamePayment(OttoagDenomModel voucherGame) {
        Intent intent = new Intent(VoucherGameActivity.this, PpobGameProductInputActivity.class);
        intent.putExtra(AppKeys.KEY_PPOB_GAME_DATA, new Gson().toJson(voucherGame));
        startActivity(intent);
    }

    private void callGetVoucherGameProductList() {
        //new PpobDao(this).voucherGameProductList(BaseDao.getInstance(this, RC_GET_VOUCHER_GAME_LIST).callback);

        new PpobDao(this).billerProductListCashback("GAME", "", "", "", BaseDao.getInstance(this, RC_GET_VOUCHER_GAME_LIST).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case RC_GET_VOUCHER_GAME_LIST:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        displayPopularProduct(((PpobOttoagDenomResponseModel) br).getData().getDirect());
                        displayAllProduct(((PpobOttoagDenomResponseModel) br).getData().getPod());
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        onApiResponseError();
    }
}
