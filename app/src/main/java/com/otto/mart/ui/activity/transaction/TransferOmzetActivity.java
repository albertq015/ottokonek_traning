package com.otto.mart.ui.activity.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.CategoryModel;
import com.otto.mart.model.APIModel.Request.OmzetSaldoRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.OmzetStatResponseModel;
import com.otto.mart.model.APIModel.Response.bank.BankAccountListResponseModel;
import com.otto.mart.presenter.dao.ProfileDao;
import com.otto.mart.presenter.dao.TransactionDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.Partials.adapter.ItemAdapter;
import com.otto.mart.ui.Partials.adapter.SpinnerCategoryModelAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.profile.BankListActivity;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;
import com.otto.mart.ui.activity.transaction.history.HistoryActivity;
import com.otto.mart.ui.component.HideableSpinnerView;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class TransferOmzetActivity extends AppActivity implements RecyclerAdapterCallback, TextWatcher, SwipeRefreshLayout.OnRefreshListener {

    private ViewAnimator viewAnimtor;
    private RecyclerView amountPick;
    private ItemAdapter adapter;
    private ViewGroup backButton;
    private LazyEdittext amount;
    private TextView send, successMessage, allOmzet, bankName, accountNumber, beneficiaryName;
    private OmzetSaldoRequestModel model;
    private LazyTextview dailyOmzet;
    private SwitchCompat isUseAll;
    private OmzetStatResponseModel stat;
    private String statAmount;
    private HideableSpinnerView hsv_bank;
    private ConstraintLayout bankContainer;
    private List<String> transactionType;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_omzet);
        initComponent();
        initContent();
    }

    private void initComponent() {
        viewAnimtor = findViewById(R.id.view_animator);
        amountPick = findViewById(R.id.amountPick);
        backButton = findViewById(R.id.backhitbox);
        amount = findViewById(R.id.amount);
        send = findViewById(R.id.send);
        successMessage = findViewById(R.id.successMessage);
        allOmzet = findViewById(R.id.allOmzet);
        dailyOmzet = findViewById(R.id.dailyOmzet);
        isUseAll = findViewById(R.id.isUseAll);
        hsv_bank = findViewById(R.id.hsv_bank);
        bankContainer = findViewById(R.id.bankContainer);
        bankName = findViewById(R.id.bank_name);
        accountNumber = findViewById(R.id.account_number);
        beneficiaryName = findViewById(R.id.beneficiary_name);
        swipeRefresh = findViewById(R.id.swipeRefresh);

    }

    private void initContent() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show();

        transactionType = new ArrayList<>();
        transactionType.add("Dompet");
        transactionType.add("Bank");

        List<CategoryModel> placeholderValue = new ArrayList<>();
        CategoryModel placeholder;
        placeholder = new CategoryModel();
        placeholder.setId(1);
        placeholder.setTitle("Transfer ke Dompet");
        placeholderValue.add(placeholder);
//        placeholder = new CategoryModel();
//        placeholder.setId(2);
//        placeholder.setTitle("Transfer ke Bank");
//        placeholderValue.add(placeholder);


        adapter = new ItemAdapter(this);
        amountPick.setLayoutManager(new GridLayoutManager(this, 4));
        amountPick.setAdapter(adapter);

        List<String> items = new ArrayList<>();
        items.add("100.000");
        items.add("300.000");
        items.add("500.000");
        items.add("1.000.000");

        adapter.setItems(items);

        swipeRefresh.setOnRefreshListener(this);

        model = new OmzetSaldoRequestModel();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dailyOmzet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransferOmzetActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        ((EditText) amount.getComponent()).addTextChangedListener(this);
        isUseAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (stat != null && !stat.getAll_omset().equals("IDR 0")) {
                        long allOmzet = 0;
                        if (DataUtil.convertLongFromCurrency(stat.getAll_omset()) > 500) {
                            allOmzet = DataUtil.convertLongFromCurrency(stat.getAll_omset()) - 500;
                            amount.setContentText(DataUtil.convertCurrency(allOmzet));
                            isUseAll.setChecked(true);
                        } else {
                            isUseAll.setChecked(false);
                            showMessage("Saldo mengendap minimal Rp 500,-");
                        }
                    }
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!amount.getTextContent().equals("") && !amount.getTextContent().equals("0")) {
                    if (DataUtil.convertLongFromCurrency(stat.getAll_omset()) > 500) {
                        model.setAmount(DataUtil.convertLongFromCurrency(amount.getTextContent()));
                        try {
                            model.setUserId(SessionManager.getUserId());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(TransferOmzetActivity.this, TransactionDetailActivity.class);
                        intent.putExtra("omzet", model);
                        int itemPos = hsv_bank.getContent().getSelectedItemPosition();
                        if (transactionType.size() > itemPos && itemPos >= 0)
                            intent.putExtra("type", transactionType.get(itemPos));
                        startActivityForResult(intent, 1);
                        successMessage.setVisibility(View.GONE);
                    } else {
                        showMessage("Saldo mengendap minimal Rp 500,-");
                    }
                } else {
                    ErrorDialog dialog = new ErrorDialog(TransferOmzetActivity.this, TransferOmzetActivity.this, false, false, "Nominal tidak boleh 0", "");
                    dialog.show();
                }

            }
        });

        ((EditText) amount.getComponent()).setInputType(InputType.TYPE_CLASS_NUMBER);

        getData();

        hsv_bank.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                if (position == 1) {
                    bankContainer.setVisibility(View.VISIBLE);
                } else {
                    bankContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDropdownOpen() {

            }

            @Override
            public void onHide() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter) {
                adapter.deVirgin();
                view.setSelection(0);
            }
        });

        hsv_bank.addData(placeholderValue);
        hsv_bank.expandSpinner();

        bankContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TransferOmzetActivity.this, BankListActivity.class), 783);
            }
        });
    }

    private void getData() {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
        new TransactionDao(this).omzetStatus(
                BaseDao.getInstance(this, 1).callback);

        new ProfileDao(this).getBankList(BaseDao.getInstance(this, 7342).callback);
    }

    @Override
    public void onItemClick(Object objectModel, int position, RecyclerView.ViewHolder currHolder) {
        if (statAmount != null) {
            if (DataUtil.convertLongFromCurrency(stat.getAll_omset()) > 500) {
                if (DataUtil.convertLongFromCurrency((String) objectModel) <= DataUtil.convertLongFromCurrency(statAmount)) {
                    amount.setContentText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(objectModel.toString())));
                } else {
                    amount.setContentText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(statAmount)));
                }
            } else {
                showMessage("Saldo mengendap minimal Rp 500,-");
            }
        } else amount.setContentText(DataUtil.convertCurrency(objectModel));
        isUseAll.setChecked(false);
    }

    @Override
    public void onItemClick(Object objectModel, int position, int paymentTypePos, RecyclerView.ViewHolder currHolder) {

    }

    private void showMessage(String message) {
        ErrorDialog dialog = new ErrorDialog(TransferOmzetActivity.this, TransferOmzetActivity.this, false, false, message, "");
        dialog.show();
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        swipeRefresh.setRefreshing(false);
        ProgressDialogComponent.dismissProgressDialog(this);
        if (responseCode == 1) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                stat = (OmzetStatResponseModel) br;
                dailyOmzet.setText(DataUtil.convertCurrency(stat.getDaily_omset()));
                allOmzet.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(stat.getAll_omset())));
                statAmount = DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(stat.getAll_omset()));
                viewAnimtor.setDisplayedChild(1);
            }
        } else if (responseCode == 7342) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                BankAccountListResponseModel model = (BankAccountListResponseModel) br;
                if (model != null && model.getData().getAccount().size() > 0) {
                    bankName.setText(model.getData().getAccount().get(0).getBankName());
                    accountNumber.setText(model.getData().getAccount().get(0).getAccountNumber());
                    beneficiaryName.setText(model.getData().getAccount().get(0).getAccountOwner());
                }
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data.getBooleanExtra("isTransactionSuccess", false)) {
                    String message = String.format("Berhasil memindahkan <b>%s</b><br>ke " + (transactionType.get(hsv_bank.getContent().getSelectedItemPosition())).toLowerCase(),
                            DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(amount.getTextContent())));
                    successMessage.setText(Html.fromHtml(message));
                    successMessage.setVisibility(View.VISIBLE);
                    amount.setContentText("");
                    onRefresh();

                    Timer timer = new Timer();
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (successMessage.getVisibility() == View.VISIBLE)
                                                successMessage.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            },
                            8000
                    );
                }
            } else if (requestCode == 783) {
                bankName.setText(data.getStringExtra("bankName"));
                accountNumber.setText(data.getStringExtra("norek"));
                beneficiaryName.setText(data.getStringExtra("name"));
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (statAmount != null) {
            if (DataUtil.convertLongFromCurrency(s.toString()) >= DataUtil.convertLongFromCurrency(statAmount) - 500) {
                isUseAll.setChecked(true);
                ((EditText) amount.getComponent()).removeTextChangedListener(this);
                amount.setContentText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(statAmount) - 500).replace("Rp ", ""));
                ((EditText)amount.getComponent()).setSelection(amount.getTextContent().length());
                ((EditText) amount.getComponent()).addTextChangedListener(this);
            } else {
                isUseAll.setChecked(false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onRefresh() {
        getData();
    }
}
