package com.otto.mart.ui.fragment.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel;
import com.otto.mart.model.APIModel.Misc.bank.BankListingModel;
import com.otto.mart.model.APIModel.Misc.bank.BankRequestModel;
import com.otto.mart.model.APIModel.Request.BankEditRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.model.APIModel.Response.bank.BankAccountListResponseModel;
import com.otto.mart.model.localmodel.ui.BankUiModel;
import com.otto.mart.presenter.dao.ProfileDao;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.ui.Partials.adapter.BankItemRecyclerAdapter;
import com.otto.mart.ui.Partials.adapter.CardWalletAdapter;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.dashboard.IDashboard;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;
import com.otto.mart.ui.activity.register.RegisterAddRekeningActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.fragment.AppFragment;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public class DashboardWalletOldFragment extends AppFragment implements SwipeRefreshLayout.OnRefreshListener {
    private View mView, walletPlaceholder;
    private RecyclerView walletList, bankList;
    private BankItemRecyclerAdapter bankAdapter;
    private CardWalletAdapter cardWalletAdapter;
    private NestedScrollView scroll;
    private List<WalletDataModel> wallets = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private boolean isWalledLoaded = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_dashboard_wallet_old, container, false);
        ((IDashboard) getActivity()).isMainPage(false);

        initComponent();
        initContent();
        return mView;
    }

    private void initComponent() {
        bankList = mView.findViewById(R.id.bankList);
        walletList = mView.findViewById(R.id.walletList);
        swipeRefresh = mView.findViewById(R.id.swipeRefresh);
        walletPlaceholder = mView.findViewById(R.id.walletPlaceholder);
        bankList.setNestedScrollingEnabled(false);
        walletList.setNestedScrollingEnabled(false);
    }

    private void initContent() {
        swipeRefresh.setOnRefreshListener(this);
        bankAdapter = new BankItemRecyclerAdapter(R.layout.item_bank2);
        bankList.setLayoutManager(new LinearLayoutManager(getContext()));
        bankList.setAdapter(bankAdapter);
        bankAdapter.setBankItemRecyclerAdapterListener(new RecyclerAdapterCallback() {
            @Override
            public void onItemClick(Object objectModel, int position, RecyclerView.ViewHolder currHolder) {
                Intent intent = new Intent(getActivity(), RegisterAddRekeningActivity.class);
                intent.putExtra("listmodel", ((BankUiModel) objectModel).getListModel());
                intent.putExtra("requestmodel", ((BankUiModel) objectModel).getRequestModel());
                intent.putExtra("isEdit", true);
                intent.putExtra("position", position);
                intent.putExtra("paymentposition", 1);
                intent.putExtra("bankposition", ((BankUiModel) objectModel).getSelectedBankPos());
                intent.putExtra("norek", ((BankUiModel) objectModel).getRequestModel().getAccount_number());
                intent.putExtra("name", ((BankUiModel) objectModel).getRequestModel().getAccount_name());
                intent.putExtra("bankName", ((BankUiModel) objectModel).getListModel().getName());
                intent.putExtra("isEdit", true);
                startActivityForResult(intent, 998);
                ((DashboardActivity) getActivity()).resetCounter();
            }

            @Override
            public void onItemClick(Object objectModel, int position, int paymentTypePos, RecyclerView.ViewHolder currHolder) {

            }
        });

        cardWalletAdapter = new CardWalletAdapter(getActivity());
        walletList.setLayoutManager(new LinearLayoutManager(getContext()));
        walletList.setAdapter(cardWalletAdapter);

        bankAdapter.setPopUpListener(reason -> {
            if (!reason.equals("")) {
                ErrorDialog dialog = new ErrorDialog(getActivity(), getActivity(), false, false, reason, "");
                dialog.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getWallet();
        getBankList();
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        swipeRefresh.setRefreshing(false);
        if (responseCode == 999) {
            wallets = ((WalletResponseModel) br).getData();
            if (wallets.size() != 0) {
                isWalledLoaded = true;
                cardWalletAdapter.setWallets(wallets);
                walletList.setVisibility(View.VISIBLE);
                walletPlaceholder.setVisibility(View.GONE);
            }
            getBankList();
        } else if (responseCode == 888 && ((BaseResponseModel) br).getMeta().getCode() == 200) {
            BankAccountListResponseModel model = (BankAccountListResponseModel) br;
            if (model != null) {
                BankUiModel bankUiModel;
                BankListingModel bankListingModel;
                BankRequestModel bankRequestModel;
                List<BankUiModel> banks = new ArrayList<>();
                for (BankAccountModel bankModel : model.getData().getAccount()) {
                    bankUiModel = new BankUiModel();
                    bankListingModel = new BankListingModel();
                    bankRequestModel = new BankRequestModel();
                    bankListingModel.setName(bankModel.getBankName());
                    bankListingModel.setLogo(bankModel.getBankLogo());
                    bankListingModel.setId(bankModel.getId());
                    bankListingModel.setCode(bankModel.getBankCode());
                    bankUiModel.setListModel(bankListingModel);

                    bankRequestModel.setAccount_number(bankModel.getAccountNumber());
                    bankRequestModel.setAccount_name(bankModel.getAccountOwner());
                    bankRequestModel.setBank_code(bankModel.getBankCode());
                    bankRequestModel.setApproval_status(bankModel.getApprovalStatus());
                    bankRequestModel.setReason(bankModel.getReason());
                    bankUiModel.setRequestModel(bankRequestModel);
                    bankUiModel.setSelectedBankPos(bankModel.getBankPos());

                    banks.add(bankUiModel);
                }
                bankAdapter.setModels(banks);
            }
        } else if (responseCode == 909) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                getBankList();
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message) {
        // onApiResponseError();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 998 && resultCode == Activity.RESULT_OK) {
            int id = data.getIntExtra("id", -1);
            BankEditRequestModel model = new BankEditRequestModel();
            model.setAccount_number(data.getStringExtra("accountNum"));
            model.setAccount_owner(data.getStringExtra("accountName"));
            model.setBank_id(data.getIntExtra("bankid", -1));
            model.setSet_as_main(data.getBooleanExtra("setMain", false));
            if (id != -1) {
                model.setBank_account_id(id);
                new ProfileDao(this).updateBank(model, BaseDao.getInstance(this, 909).callback);
            } else {
                new ProfileDao(this).addBank(model, BaseDao.getInstance(this, 910).callback);
            }
        }
    }

    private void getWallet() {
        new WalletDao(this).emoneySummary(BaseDao.getInstance(DashboardWalletOldFragment.this, 999).callback);
    }

    private void getBankList() {
        new ProfileDao(this).getBankList(BaseDao.getInstance(DashboardWalletOldFragment.this, 888).callback);
    }

    @Override
    public void onRefresh() {
        getWallet();
        getBankList();
    }
}