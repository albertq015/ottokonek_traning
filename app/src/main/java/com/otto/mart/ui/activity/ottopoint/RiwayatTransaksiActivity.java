package com.otto.mart.ui.activity.ottopoint;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.ottopoint.OpHistoryTransactionResponseModel;
import com.otto.mart.model.localmodel.ui.ottopoint.RiwayatTransaksiPointItemModel;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.support.util.DateUtil;
import com.otto.mart.ui.Partials.adapter.ottopoint.RiwayatTransaksiPoinAdapter;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.component.ActionbarOttopointWhite;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class RiwayatTransaksiActivity extends BaseActivityOttopoint {

    private String TAG = RiwayatTransaksiActivity.class.getSimpleName();

    @BindView(R.id.view_arrow)
    View viewArrow;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.tv_poin_ottopoint)
    TextView tvPoinOttopoint;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;
    @BindView(R.id.view_refresh)
    SwipeRefreshLayout viewRefresh;

    private RiwayatTransaksiPoinAdapter adapter;
    private List<RiwayatTransaksiPointItemModel> mItems = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_transaksi);
        ButterKnife.bind(this);

        // set header view
        viewArrow.setVisibility(View.GONE);

        configureList();

        getBalanceOttoPoint((balance,metaCode) -> {
            if(tvPoinOttopoint != null)
                tvPoinOttopoint.setText(CommonHelper.currencyFormat(balance));
        });

        callApiGetHistoryTransaction(true);

        // events

        viewActionbar.setActionMenuLeft(view -> onBackPressed());
        viewRefresh.setOnRefreshListener(() -> callApiGetHistoryTransaction(false));
    }

    private void configureList(){
        if(adapter == null)
            adapter = new RiwayatTransaksiPoinAdapter(RiwayatTransaksiActivity.this, mItems);

        list.setLayoutManager(new LinearLayoutManager(RiwayatTransaksiActivity.this));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);
    }

    private void setDummyData(){
        for (int i = 0; i < 10; i++)
            mItems.add(new RiwayatTransaksiPointItemModel(
                    Integer.toString(i),
                    (i + 1) + " Mar 2019, 19.15",
                    "OttoCash Point",
                    "Tambah Poin",
                    0,
                    (i % 2 != 0 ? "+" : "-") + "550.965 poin",
                    (i % 2 != 0))
            );
    }

    private void updateList(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
        else{
            adapter = new RiwayatTransaksiPoinAdapter(RiwayatTransaksiActivity.this, mItems);
            list.setAdapter(adapter);
        }
    }

    private void setSwipeRefreshing(boolean isRefresh){
        if(viewRefresh == null) return;

        viewRefresh.setRefreshing(isRefresh);
    }

    private void callApiGetHistoryTransaction(boolean isShowProgress){
        showProgress(RiwayatTransaksiActivity.this, isShowProgress);
        OttoPointDao.historyTransaction(RiwayatTransaksiActivity.this, new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                showProgress(RiwayatTransaksiActivity.this, false);
                setSwipeRefreshing(false);

                if(br instanceof OpHistoryTransactionResponseModel){
                    OpHistoryTransactionResponseModel result = (OpHistoryTransactionResponseModel) br;

                    if(result.getData() == null) return;

                    mItems.clear();

                    for (OpHistoryTransactionResponseModel.Data.Transfers item : result.getData().getTransfers()) {
                        boolean isAdd = item.getType().equalsIgnoreCase("adding");

                        mItems.add(new RiwayatTransaksiPointItemModel(
                                item.getPointsTransferId(),
                                DateUtil.getDateAsStringFormat(item.getCreatedAt(), GLOBAL.FORMAT_DATE_TIME_DEFAULT_SERVER_TWO, GLOBAL.FORMAT_DATE_TIME_DATE_MONTH_TIME),
                                item.getComment(),
                                getString(isAdd ? R.string.label_tambah_poin : R.string.label_kurang_poin),
                                item.getValue(),
                                (isAdd ? "+" : "-") + CommonHelper.currencyFormat(item.getValue()) + " poin",
                                isAdd
                        ));
                    }

                    updateList();
                }
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                showProgress(RiwayatTransaksiActivity.this, false);
                setSwipeRefreshing(false);
            }
        });
    }
}
