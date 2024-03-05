package com.otto.mart.ui.activity.ppob.product.game;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.core.content.ContextCompat;
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
import com.otto.mart.ui.activity.PpobActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class SearchVoucherGameActivity extends PpobActivity {

    private final int RC_GET_VOUCHER_GAME_LIST = 100;

    private LinearLayout btnToolbarBack;
    private LinearLayout btnToolbarTitle;
    private TextView tvToolbarTitle;
    private LinearLayout btnToolbarRight;
    private ImageView imgToolbarRight;
    private ViewAnimator viewAnimator;
    private RecyclerView recyclerview;
    private EditText etSearch;
    private TextView tvEmpty;
    private LinearLayout contentLayout;
    private LinearLayout emptyLayout;

    private List<OttoagDenomModel> mProductList = new ArrayList();
    private List<OttoagDenomModel> mSearchProductList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_voucher_game);

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
        recyclerview = findViewById(R.id.recyclerview);
        etSearch = findViewById(R.id.et_search);
        contentLayout = findViewById(R.id.contentLayout);
        emptyLayout = findViewById(R.id.emptyLayout);
        tvEmpty = findViewById(R.id.tvEmpty);

        addTextWatcher(etSearch);

        tvToolbarTitle.setText(getString(R.string.title_activity_voucher_game));
        imgToolbarRight.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_button_more));
        btnToolbarRight.setVisibility(View.VISIBLE);

        btnToolbarRight.setOnClickListener(v -> {
            showAllPpobMenu(getString(R.string.title_activity_voucher_game));
        });

        btnToolbarBack.setOnClickListener(v -> {
            onBackPressed();
        });

        // request keyboard
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setNestedScrollingEnabled(false);
    }

    private void displayAllProduct() {
        VoucherGameAdapter adapter = new VoucherGameAdapter(this, mProductList);
        recyclerview.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerview).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                gotoVoucherGamePayment(mProductList.get(position));
            }
        });

        viewAnimator.setDisplayedChild(1);
    }

    private void displaySearchProduct(String keyword) {
        mSearchProductList.clear();

        for(OttoagDenomModel product: mProductList){
            if(product.getProduct_name().toLowerCase().contains(keyword.toLowerCase())){
                mSearchProductList.add(product);
            }
        }

        if (mSearchProductList.size() > 0) {
            VoucherGameAdapter adapter = new VoucherGameAdapter(this, mSearchProductList);
            recyclerview.setAdapter(adapter);

            ItemClickSupport.addTo(recyclerview).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    gotoVoucherGamePayment(mSearchProductList.get(position));
                }
            });

            contentLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        } else {
            contentLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    private void gotoVoucherGamePayment(OttoagDenomModel voucherGame) {
        Intent intent = new Intent(SearchVoucherGameActivity.this, PpobGameProductInputActivity.class);
        intent.putExtra(AppKeys.KEY_PPOB_GAME_DATA, new Gson().toJson(voucherGame));
        startActivity(intent);
    }

    public void addTextWatcher(EditText input) {
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    displayAllProduct();
                } else {
                    displaySearchProduct(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

    private void callGetVoucherGameProductList() {
        new PpobDao(this).billerProductListCashback("GAME", "", "", "", BaseDao.getInstance(this, RC_GET_VOUCHER_GAME_LIST).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case RC_GET_VOUCHER_GAME_LIST:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        mProductList.addAll(((PpobOttoagDenomResponseModel) br).getData().getDirect());
                        mProductList.addAll(((PpobOttoagDenomResponseModel) br).getData().getPod());
                        displayAllProduct();
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
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }
}
