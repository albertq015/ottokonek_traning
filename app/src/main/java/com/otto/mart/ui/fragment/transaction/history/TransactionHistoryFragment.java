package com.otto.mart.ui.fragment.transaction.history;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.OmzetHistoryResponseData;
import com.otto.mart.model.APIModel.Misc.PaymentData;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Request.OmsetHistoryRequestModel;
import com.otto.mart.model.APIModel.Request.PpobTransactionAdviceModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.history.OmzetHistoryResponse;
import com.otto.mart.presenter.dao.PpobDao;
import com.otto.mart.presenter.dao.TransactionDao;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.Partials.adapter.TransactionHistoryAdapter;
import com.otto.mart.ui.activity.ppob.PpobPaymentDetailActivity;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;
import com.otto.mart.ui.activity.transaction.PayQRDetailActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.fragment.AppFragment;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_GET_OMSET_HISTORY;
import static com.otto.mart.keys.AppKeys.API_KEY_TRANSACTION_ADVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionHistoryFragment extends AppFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerAdapterCallback {

    private ViewGroup backButton;
    private TextView tvTitle;
    private ImageView closeButton;
    private ViewAnimator viewAnimator;
    private RecyclerView omsetHistoryList;
    private SpinKitView spinKit;
    private TransactionHistoryAdapter adapter;
    private OmsetHistoryRequestModel requestModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private TextView noTransaction;

    private int mPage = 1;
    private int mLimit = 10;
    private boolean mNomoreData = false;

    private int mWalleetId = -1;
    private int mTabPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(AppKeys.KEY_WALLET_ID)) {
            mWalleetId = getArguments().getInt(AppKeys.KEY_WALLET_ID);
        }

        if (arguments != null && arguments.containsKey(AppKeys.KEY_WALLET_ID)) {
            mTabPosition = getArguments().getInt(AppKeys.KEY_WALLET_ID);
        }

        initView(view);
        initContent();
        return view;
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.title);
        backButton = view.findViewById(R.id.backhitbox);
        omsetHistoryList = view.findViewById(R.id.historyList);
        viewAnimator = view.findViewById(R.id.viewAnimator);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        noTransaction = view.findViewById(R.id.noTransaction);
        spinKit = view.findViewById(R.id.spinKit);
    }

    private void initContent() {
        adapter = new TransactionHistoryAdapter(this);
        layoutManager = new LinearLayoutManager(getActivity());
        omsetHistoryList.setLayoutManager(layoutManager);
        omsetHistoryList.setAdapter(adapter);
        omsetHistoryList.addOnScrollListener(onScrollListener);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter.setViewClickListener(omzetHistoryResponseData -> {
            checkStatus(omzetHistoryResponseData.getBiller_reference());
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
                    if (mTabPosition == 0) {
                        mPage = 1;
                        requestModel.setPage(mPage);
                        getOmsetData();
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        initRequestModel();
        getOmsetData();
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (layoutManager.findFirstVisibleItemPosition() + recyclerView.getChildCount() == layoutManager.getItemCount()) {
                if (!mNomoreData) {
                    if (mTabPosition == 0) {
                        getOmsetData();
                        spinKit.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    };

    private void initRequestModel() {
        requestModel = new OmsetHistoryRequestModel();
        requestModel.setPage(mPage);
        requestModel.setItem_per_page(mLimit);
    }

    private void getOmsetData() {
        //swipeRefreshLayout.setRefreshing(true);

        if (mTabPosition == 0) {
            new TransactionDao(this).getOmsetHistory("", "", "", "", mLimit, mPage, BaseDao.getInstance(this, API_KEY_GET_OMSET_HISTORY).callback);
        } else {
            requestModel.setWallet_id(mWalleetId);
            //new WalletDao(this).getWalletHistory(wallet, BaseDao.getInstance(this, API_KEY_GET_OMSET_HISTORY).callback);

            //Ottofin
            new WalletDao(this).getWalletHistory("", "", "", mLimit, 1, BaseDao.getInstance(this, API_KEY_GET_OMSET_HISTORY).callback);
        }
    }

    private void checkStatus(String referenceNumber) {
        ProgressDialogComponent.showProgressDialog(getActivity(), getString(R.string.loading_message), false).show();

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
                OmzetHistoryResponse model = (OmzetHistoryResponse) br;
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

                    if (model.getData().size() < mLimit) {
                        mNomoreData = true;
                    }

                    new Handler().postDelayed(() -> spinKit.setVisibility(View.GONE), 1200);
                }
            } else if (((BaseResponseModel) br).getMeta().getCode() == 400) {
                noTransaction.setVisibility(View.VISIBLE);
                ErrorDialog dialog = new ErrorDialog(getActivity(), getActivity(), false, false, "Request Gagal", ((BaseResponseModel) br).getMeta().getMessage());
                dialog.setOnDismissListener(dialog1 -> dialog1.dismiss());
                dialog.show();
            }
        } else if (responseCode == API_KEY_TRANSACTION_ADVICE) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                PpobOttoagPaymentResponseModel model = (PpobOttoagPaymentResponseModel) br;
                if (model.getData().getKeyValueList().size() > 0) {
                    Intent intent = new Intent(getActivity(), PayQRDetailActivity.class);
                    intent.putExtra("data", (ArrayList<? extends Parcelable>) ((BasePaymentResponseModel) br).getData().getKeyValueList());
                    startActivity(intent);
                }
            } else if (((BaseResponseModel) br).getMeta().getCode() == 408) {
                ErrorDialog d = new ErrorDialog(getActivity(), getActivity(), false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                d.show();
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message) {
        onApiResponseError();
    }

    @Override
    public void onRefresh() {
        initRequestModel();
        getOmsetData();
    }

    @Override
    public void onItemClick(Object objectModel, int position, RecyclerView.ViewHolder currHolder) {

        OmzetHistoryResponseData omzetHistoryResponseData = (OmzetHistoryResponseData) objectModel;

        PpobOttoagPaymentResponseModel ppobOttoagPaymentResponseModel = new PpobOttoagPaymentResponseModel();

        List<WidgetBuilderModel> keyValueList = new ArrayList();

        WidgetBuilderModel noResi = new WidgetBuilderModel();
        noResi.setKey("Nomor Resi");
        noResi.setValue(omzetHistoryResponseData.getReferenceNumber());
        keyValueList.add(noResi);

        WidgetBuilderModel date = new WidgetBuilderModel();
        date.setKey("Tanggal Transaksi");
        date.setValue(omzetHistoryResponseData.getDateString());
        keyValueList.add(date);

        WidgetBuilderModel desc = new WidgetBuilderModel();
        desc.setKey("Deskripsi");
        if (omzetHistoryResponseData.getDescription().toLowerCase().contains("transfer ke dompet")) {
            omzetHistoryResponseData.setDescription("Transfer ke Bank");
        }
        desc.setValue(omzetHistoryResponseData.getDescription());
        keyValueList.add(desc);

        WidgetBuilderModel paymentMethod = new WidgetBuilderModel();
        paymentMethod.setKey("Metode Pembayaran");
        paymentMethod.setValue(omzetHistoryResponseData.getMethod());
        if (omzetHistoryResponseData.getDescription().equalsIgnoreCase("Transfer ke Bank")) {
            paymentMethod.setValue("Transfer");
        }
        keyValueList.add(paymentMethod);

        WidgetBuilderModel transactionType = new WidgetBuilderModel();
        transactionType.setKey("Tipe Transaksi");
        transactionType.setValue(omzetHistoryResponseData.getType());
        keyValueList.add(transactionType);

        WidgetBuilderModel direction = new WidgetBuilderModel();
        direction.setKey("Direction");
        direction.setValue(omzetHistoryResponseData.getDirection());
        keyValueList.add(direction);

        if (!omzetHistoryResponseData.getBillerReference().equalsIgnoreCase("")&&!omzetHistoryResponseData.getBillerReference().trim().equalsIgnoreCase("-")) {
            WidgetBuilderModel billRef = new WidgetBuilderModel();
            billRef.setKey("Biller Reference");
            billRef.setValue(omzetHistoryResponseData.getBillerReference());
            keyValueList.add(billRef);
        }

        if (!omzetHistoryResponseData.getStroomToken().equalsIgnoreCase("")&&!omzetHistoryResponseData.getStroomToken().trim().equalsIgnoreCase("-")) {
            WidgetBuilderModel token = new WidgetBuilderModel();
            token.setKey("Token");
            token.setValue(omzetHistoryResponseData.getStroomToken());
            keyValueList.add(token);
        }

        if (!omzetHistoryResponseData.getSerial().equalsIgnoreCase("")&&!omzetHistoryResponseData.getSerial().trim().equalsIgnoreCase("-")) {
            WidgetBuilderModel serial = new WidgetBuilderModel();
            serial.setKey("Serial");
            serial.setValue(omzetHistoryResponseData.getSerial());
            keyValueList.add(serial);
        }

        if (!omzetHistoryResponseData.getPinCode().equalsIgnoreCase("")&&!omzetHistoryResponseData.getPinCode().trim().equalsIgnoreCase("-")) {
            WidgetBuilderModel serial = new WidgetBuilderModel();
            serial.setKey("Pin Kode");
            serial.setValue(omzetHistoryResponseData.getPinCode());
            keyValueList.add(serial);
        }

        if (!omzetHistoryResponseData.getCommission().equals("") && !omzetHistoryResponseData.getCommission().trim().equals("-")
                && !omzetHistoryResponseData.getCommission().trim().equals("0")) {
            WidgetBuilderModel totalPayment = new WidgetBuilderModel();
            totalPayment.setKey("Komisi");
            totalPayment.setValue(DataUtil.convertCurrency(omzetHistoryResponseData.getCommission()));
            keyValueList.add(totalPayment);
        }

        WidgetBuilderModel totalPayment = new WidgetBuilderModel();
        totalPayment.setKey("Jumlah Total");
        totalPayment.setValue(DataUtil.convertCurrency(omzetHistoryResponseData.getAmount()));
        keyValueList.add(totalPayment);

        PaymentData paymentData = new PaymentData();
        paymentData.setKeyValueList(keyValueList);

        ppobOttoagPaymentResponseModel.setData(paymentData);

        Intent intent = new Intent(getActivity(), PpobPaymentDetailActivity.class);
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA, new Gson().toJson(ppobOttoagPaymentResponseModel));
        intent.putExtra(PpobPaymentDetailActivity.KEY_STATUS, omzetHistoryResponseData.getStatus());
        intent.putExtra(PpobPaymentDetailActivity.KEY_REFERENCE_NUMBER, omzetHistoryResponseData.getReferenceNumber());
        startActivity(intent);
    }

    @Override
    public void onItemClick(Object objectModel, int position, int paymentTypePos, RecyclerView.ViewHolder currHolder) {

    }
}