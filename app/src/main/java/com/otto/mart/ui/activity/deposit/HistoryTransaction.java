package com.otto.mart.ui.activity.deposit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.OmzetHistoryResponseData;
import com.otto.mart.model.APIModel.Request.OmsetHistoryRequestModel;
import com.otto.mart.model.APIModel.Request.PpobTransactionAdviceModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.history.OttoCashHistoryResponse;
import com.otto.mart.presenter.dao.PpobDao;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.ui.Partials.adapter.HistoryTransactionAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;
import com.otto.mart.ui.activity.transaction.PayQRDetailActivity;
import com.otto.mart.ui.activity.transaction.TransactionCompleteActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.ArrayList;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_GET_OMSET_HISTORY;
import static com.otto.mart.keys.AppKeys.API_KEY_TRANSACTION_ADVICE;

public class HistoryTransaction extends AppActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerAdapterCallback {
    private ViewGroup backButton;
    private ViewAnimator viewAnimator;
    private TextView tvTitle;
    private ImageView closeButton;
    private RecyclerView omsetHistoryList;
    private HistoryTransactionAdapter adapter;
    private OmsetHistoryRequestModel requestModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private TextView noTransaction;
    private TextView toolbar;
    private ImageView appBack;
    private LinearLayout toolbarHistory;

    private int mPage = 1;
    private int mLimit = 10;
    private boolean mNomoreData = false;

    private int mWalleetId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_transaction_history);

        initView();
        initContent();
    }

    private void initView() {
        tvTitle = findViewById(R.id.title);
        viewAnimator = findViewById(R.id.viewAnimator);
        backButton = findViewById(R.id.backhitbox);
        omsetHistoryList = findViewById(R.id.historyList);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        noTransaction = findViewById(R.id.noTransaction);
        toolbar = findViewById(R.id.tvToolbarTitle);
        appBack = findViewById(R.id.imgToolbarLeft);
        toolbarHistory = findViewById(R.id.toolbar_history);

        toolbarHistory.setVisibility(View.VISIBLE);
        toolbar.setText(getString(R.string.text_history_transaction));
        appBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void initContent() {
        adapter = new HistoryTransactionAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        omsetHistoryList.setLayoutManager(layoutManager);
        omsetHistoryList.setAdapter(adapter);
        omsetHistoryList.addOnScrollListener(onScrollListener);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter.setViewClickListener(omzetHistoryResponseData -> {
            checkStatus(omzetHistoryResponseData.getReferenceNumber());
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
                    mPage = 1;
                    requestModel.setPage(mPage);
                    getOttoCashHistory();
                }
        );

        initRequestModel();
        getOttoCashHistory();
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (layoutManager.findFirstVisibleItemPosition() + recyclerView.getChildCount() == layoutManager.getItemCount()) {
                if (!mNomoreData) {
                    //getOttoCashHistory();
                }
            }
        }
    };

    private void initRequestModel() {
        requestModel = new OmsetHistoryRequestModel();
        requestModel.setPage(mPage);
        requestModel.setItem_per_page(mLimit);
    }

    private void getOttoCashHistory() {
        requestModel.setWallet_id(mWalleetId);

        //Ottofin
        new WalletDao(this).ottoCashHistory("", "", mPage, BaseDao.getInstance(this, API_KEY_GET_OMSET_HISTORY).callback);
    }

    private void checkStatus(String referenceNumber) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show();

        PpobTransactionAdviceModel ppobTransactionAdviceModel = new PpobTransactionAdviceModel();
        ppobTransactionAdviceModel.setReference_number(referenceNumber);

        PpobDao dao = new PpobDao(this);
        dao.TransactionAdviceDao(ppobTransactionAdviceModel, BaseDao.getInstance(this, API_KEY_TRANSACTION_ADVICE).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        swipeRefreshLayout.setRefreshing(false);
        if (responseCode == API_KEY_GET_OMSET_HISTORY) {
            viewAnimator.setDisplayedChild(1);
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                OttoCashHistoryResponse model = (OttoCashHistoryResponse) br;
                if (model.getData() != null) {
                    if (model.getData().size() > 0) {
                        noTransaction.setVisibility(View.GONE);
                        if (mPage == 1) {
                            adapter.setOmsetHistory(model.getData());
                        } else {
                            adapter.addMore(model.getData());
                        }
                        ++mPage;
                        requestModel.setPage(mPage);
                    }
                }
            } else if (((BaseResponseModel) br).getMeta().getCode() == 400) {
                noTransaction.setVisibility(View.VISIBLE);
                ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Request Gagal", ((BaseResponseModel) br).getMeta().getMessage());
                dialog.setOnDismissListener(dialog1 -> dialog1.dismiss());
                dialog.show();
            }
        } else if (responseCode == API_KEY_TRANSACTION_ADVICE) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                PpobOttoagPaymentResponseModel model = (PpobOttoagPaymentResponseModel) br;
                if (model.getData().getKeyValueList().size() > 0) {
                    Intent intent = new Intent(this, PayQRDetailActivity.class);
                    intent.putExtra("data", (ArrayList<? extends Parcelable>) ((BasePaymentResponseModel) br).getData().getKeyValueList());
                    startActivity(intent);
                }
            } else if (((BaseResponseModel) br).getMeta().getCode() == 408) {
                ErrorDialog d = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                d.show();
            }
        }
    }

    @Override
    public void onRefresh() {
        initRequestModel();
        getOttoCashHistory();
    }

    @Override
    public void onItemClick(Object objectModel, int position, RecyclerView.ViewHolder currHolder) {
        Intent intent = new Intent(this, TransactionCompleteActivity.class);
        intent.putExtra("transaction", (OmzetHistoryResponseData) objectModel);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Object objectModel, int position, int paymentTypePos, RecyclerView.ViewHolder currHolder) {

    }
}
