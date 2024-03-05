package com.otto.mart.ui.fragment.dashboard;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.history.OmzetHistory;
import com.otto.mart.model.APIModel.Request.OmsetHistoryRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.OmzetHistoryResponseModel;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.model.localmodel.ui.GridMenu;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.ui.Partials.adapter.GridMenuAdapter;
import com.otto.mart.ui.Partials.adapter.WalletLatestHistoryAdapter;
import com.otto.mart.ui.activity.Topup.TopupActivity;
import com.otto.mart.ui.activity.transaction.history.HistoryActivity;
import com.otto.mart.ui.activity.deposit.TransferSaldoActivity;
import com.otto.mart.ui.fragment.AppFragment;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardWalletFragment extends AppFragment implements SwipeRefreshLayout.OnRefreshListener {

    private NestedScrollView nestedScrollView;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvBalance;
    private RecyclerView rvMenu;
    private ViewAnimator viewAnimator;
    private Button btnMore;
    private RecyclerView rvHistory;

    public DashboardWalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_wallet, container, false);

        initView(view);
        displayMenu();
        getWalletInfo();

        return view;
    }

    @Override
    public void onRefresh() {
        getWalletInfo();
    }

    private void initView(View view) {
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        tvBalance = view.findViewById(R.id.tv_balance);
        rvMenu = view.findViewById(R.id.rv_menu);
        rvHistory = view.findViewById(R.id.rv_history);
        viewAnimator = view.findViewById(R.id.view_animator);
        btnMore = view.findViewById(R.id.btn_more);

        nestedScrollView.setNestedScrollingEnabled(true);

        rvMenu.setHasFixedSize(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        rvMenu.setLayoutManager(gridLayoutManager);

        rvHistory.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerHistory = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvHistory.setLayoutManager(linearLayoutManagerHistory);
    }

    private void displayMenu() {
        List<GridMenu> menuList = new ArrayList();

        GridMenu bayarKeBank = new GridMenu();
        bayarKeBank.setName("Bayar");
        bayarKeBank.setIcon(R.drawable.ic_bayar);
        bayarKeBank.setIntent(new Intent(getActivity(), TransferSaldoActivity.class));
        menuList.add(bayarKeBank);

        GridMenu merchantTerdekat = new GridMenu();
        merchantTerdekat.setName("Merchant Terdekat");
        merchantTerdekat.setIcon(R.drawable.ic_nearest);
        //merchantTerdekat.setIntent(new Intent(getActivity(), TopupActivity.class));
        menuList.add(merchantTerdekat);

        GridMenu topUp = new GridMenu();
        topUp.setName("Top Up");
        topUp.setIcon(R.drawable.ic_topup);
        topUp.setIntent(new Intent(getActivity(), TopupActivity.class));
        menuList.add(topUp);

        GridMenu history = new GridMenu();
        history.setName("Riwayat Transaksi");
        history.setIcon(R.drawable.ic_history_new);
        history.setIntent(new Intent(getActivity(), HistoryActivity.class));
        menuList.add(history);

        GridMenu kirimHadiah = new GridMenu();
        kirimHadiah.setName("Kirim Hadiah");
        kirimHadiah.setIcon(R.drawable.ic_gift);
        //bayarKeBank.setIntent(new Intent(getActivity(), TransferSaldoActivity.class));
        menuList.add(kirimHadiah);

        GridMenu penarikanUang = new GridMenu();
        penarikanUang.setName("Penarikan Uang");
        penarikanUang.setIcon(R.drawable.ic_cashout);
        //bayarKeBank.setIntent(new Intent(getActivity(), TransferSaldoActivity.class));
        menuList.add(penarikanUang);

        GridMenu permintaanUang = new GridMenu();
        permintaanUang.setName("Permintaan Uang");
        permintaanUang.setIcon(R.drawable.ic_askmoney);
        //bayarKeBank.setIntent(new Intent(getActivity(), TransferSaldoActivity.class));
        menuList.add(permintaanUang);

        GridMenu TransferUang = new GridMenu();
        TransferUang.setName("Transfer Uang");
        TransferUang.setIcon(R.drawable.ic_askmoney);
        //bayarKeBank.setIntent(new Intent(getActivity(), TransferSaldoActivity.class));
        menuList.add(TransferUang);

//        GridMenu history = new GridMenu();
//        history.setName("Riwayat");
//        history.setIcon(R.drawable.ic_menu_transfer);
//        history.setIntent(new Intent(getActivity(), HistoryActivity.class));
//        menuList.add(history);

        GridMenuAdapter gridMenuAdapter = new GridMenuAdapter(getActivity(), menuList);
        rvMenu.setAdapter(gridMenuAdapter);

        gridMenuAdapter.setmOnViewClickListener((gridMenu, position) -> {
            startActivity(gridMenu.getIntent());
        });

        swipeRefresh.setOnRefreshListener(() -> {
            getWalletInfo();
        });

        btnMore.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), HistoryActivity.class));
        });
    }

    private void displayHistory(List<OmzetHistory> transactions) {
        final int LIMIT = 3;

        if (transactions.size() > 0) {
            List<OmzetHistory> displayList = new ArrayList();

            for (OmzetHistory transaction : transactions) {
                if (displayList.size() < LIMIT) {
                    displayList.add(transaction);
                }
            }

            WalletLatestHistoryAdapter adapter = new WalletLatestHistoryAdapter(getActivity(), displayList);
            rvHistory.setAdapter(adapter);
            viewAnimator.setDisplayedChild(1);
        } else {
            viewAnimator.setVisibility(View.GONE);
        }
    }


    //region API Call

    private void getWalletInfo() {
        new WalletDao(this).emoneySummary(BaseDao.getInstance(DashboardWalletFragment.this, AppKeys.API_KEY_WALLET_INFO).callback);
    }

    private void getWalletHistory() {
        OmsetHistoryRequestModel requestModel = new OmsetHistoryRequestModel();
        requestModel.setPage(1);
        requestModel.setItem_per_page(3);
        new WalletDao(this).getWalletHistory("" ,"", "", 10, 1, BaseDao.getInstance(this, AppKeys.API_KEY_WALLET_HISTORY).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (responseCode == AppKeys.API_KEY_WALLET_INFO) {
            swipeRefresh.setRefreshing(false);
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                if (((WalletResponseModel) br).getData().size() > 0) {
                    tvBalance.setText(((WalletResponseModel) br).getData().get(0).getBalance());
                }
            }
            getWalletHistory();
        } else if (responseCode == AppKeys.API_KEY_WALLET_HISTORY) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                OmzetHistoryResponseModel model = (OmzetHistoryResponseModel) br;
                if (model.getTransactions() != null) {
                    displayHistory(model.getTransactions());
                }
            } else {
                viewAnimator.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message) {
       // onApiResponseError();
    }

    //endregion API Call
}
