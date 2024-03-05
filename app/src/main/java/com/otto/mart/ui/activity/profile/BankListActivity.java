package com.otto.mart.ui.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel;
import com.otto.mart.model.APIModel.Misc.bank.BankListingModel;
import com.otto.mart.model.APIModel.Misc.bank.BankRequestModel;
import com.otto.mart.model.APIModel.Request.BankEditRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.bank.BankAccountListResponseModel;
import com.otto.mart.model.localmodel.ui.BankUiModel;
import com.otto.mart.presenter.dao.ProfileDao;
import com.otto.mart.support.util.DeviceUtil;
import com.otto.mart.ui.Partials.adapter.BankItemRecyclerAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;
import com.otto.mart.ui.activity.register.RegisterAddRekeningActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public class BankListActivity extends AppActivity implements RecyclerAdapterCallback, BankItemRecyclerAdapter.onDeleteListener {

    private FloatingActionButton fabAdd;
    private LinearLayout btnToolbarBack;
    private LinearLayout btnToolbarRight;
    private ImageView imgToolbarRight;
    private TextView tvToolbarTitle;
    private RecyclerView bankList;
    private BankItemRecyclerAdapter bankAdapter;
    private List<BankUiModel> banks = new ArrayList<>();
    private boolean isNotSelectableItem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        initComponent();
        initContent();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        btnToolbarRight = findViewById(R.id.btnToolbarRight);
        imgToolbarRight = findViewById(R.id.imgToolbarRight);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        bankList = findViewById(R.id.bankList);
        fabAdd = findViewById(R.id.fabAdd);

        if (getIntent().hasExtra("isAddNew") && getIntent().getBooleanExtra("isAddNew", false)) {
            addBankAccount();
        }
    }

    private void initContent() {
        tvToolbarTitle.setText(getString(R.string.text_bank_account));

        fabAdd.setVisibility(View.GONE);
        imgToolbarRight.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_toolbar));
        btnToolbarRight.setVisibility(View.VISIBLE);

        isNotSelectableItem = getIntent().getBooleanExtra(AppKeys.KEY_BANK_LIST_NOT_SELECTABLE, false);

        bankAdapter = new BankItemRecyclerAdapter(R.layout.item_bank_4);
        bankAdapter.setBankItemRecyclerAdapterListener(this);
        bankAdapter.setDeleteListener(this);
        bankList.setLayoutManager(new LinearLayoutManager(this));
        bankList.setAdapter(bankAdapter);

        int space = DeviceUtil.dpToPx(16);
//        bankList.addItemDecoration(new SpaceDecorator(space, LinearLayout.VERTICAL, space, 0, 0));
        callListBankAccountAPI();

        btnToolbarBack.setOnClickListener(v -> finish());

        fabAdd.setOnClickListener(v -> {
            addBankAccount();
        });

        btnToolbarRight.setOnClickListener(v -> {
            addBankAccount();
        });

        bankAdapter.setPopUpListener(reason -> {
            if (!reason.equals("")) {
                ErrorDialog dialog = new ErrorDialog(BankListActivity.this, BankListActivity.this, false, false, reason, "");
                dialog.show();
            }
        });
    }

    private void addBankAccount() {
        Intent intent = new Intent(BankListActivity.this, RegisterAddRekeningActivity.class);
        startActivityForResult(intent, 888);
    }

    private void convertModel(List<BankAccountModel> banks) {
        BankUiModel bankUiModel;
        BankListingModel bankListingModel;
        BankRequestModel bankRequestModel;
        this.banks = new ArrayList<>();
        for (BankAccountModel model : banks) {
            bankUiModel = new BankUiModel();
            bankListingModel = new BankListingModel();
            bankRequestModel = new BankRequestModel();
            bankListingModel.setName(model.getBankName());
            bankListingModel.setLogo(model.getBankLogo());
            bankListingModel.setId(model.getId());
            bankListingModel.setCode(model.getBankCode());
            bankUiModel.setListModel(bankListingModel);

            bankRequestModel.setAccount_number(model.getAccountNumber());
            bankRequestModel.setAccount_name(model.getAccountOwner());
            bankRequestModel.setBank_code(model.getBankCode());
            bankRequestModel.setApproval_status(model.getApprovalStatus());
            bankRequestModel.setReason(model.getReason());
            bankUiModel.setRequestModel(bankRequestModel);
            bankUiModel.setSelectedBankPos(model.getBankPos());
            this.banks.add(bankUiModel);
        }

        bankAdapter.setModels(this.banks);
    }

    public BankItemRecyclerAdapter getBankAdapter() {
        return bankAdapter;
    }

    @Override
    public void onItemClick(Object objectModel, int position, RecyclerView.ViewHolder currHolder) {
        String approvalStatus = ((BankUiModel) objectModel).getRequestModel().getApproval_status();
        if (!approvalStatus.equalsIgnoreCase("ditolak") && !approvalStatus.equalsIgnoreCase(getString(R.string.text_approved))) {
            new ErrorDialog(this, this, true, false, getString(R.string.text_on_verification), "").show();
            return;
        }

        Intent intent = new Intent(this, RegisterAddRekeningActivity.class);
        intent.putExtra("listmodel", ((BankUiModel) objectModel).getListModel());
        intent.putExtra("requestmodel", ((BankUiModel) objectModel).getRequestModel());
        intent.putExtra("position", position);
        intent.putExtra("paymentposition", 1);
        intent.putExtra("bankposition", ((BankUiModel) objectModel).getSelectedBankPos());
        intent.putExtra("norek", ((BankUiModel) objectModel).getRequestModel().getAccount_number());
        intent.putExtra("name", ((BankUiModel) objectModel).getRequestModel().getAccount_name());
        intent.putExtra("bankName", ((BankUiModel) objectModel).getListModel().getName());
        intent.putExtra("isEdit", true);
        intent.putExtra("bankId", ((BankUiModel) objectModel).getListModel().getId());
        intent.putExtra("bank_code", ((BankUiModel) objectModel).getListModel().getCode());
        intent.putExtra("bank_logo", ((BankUiModel) objectModel).getListModel().getLogo());
        intent.putExtra("approval_status", ((BankUiModel) objectModel).getRequestModel().getApproval_status());

        startActivityForResult(intent, 888);
    }

    @Override
    public void onItemClick(Object objectModel, int position, int paymentTypePos, RecyclerView.ViewHolder currHolder) {
        if (!isNotSelectableItem) {
            Intent intent = new Intent(this, RegisterAddRekeningActivity.class);
            intent.putExtra("listmodel", ((BankUiModel) objectModel).getListModel());
            intent.putExtra("requestmodel", ((BankUiModel) objectModel).getRequestModel());
            intent.putExtra("position", position);
            intent.putExtra("paymentposition", 1);
            intent.putExtra("bankposition", ((BankUiModel) objectModel).getSelectedBankPos());
            intent.putExtra("norek", ((BankUiModel) objectModel).getRequestModel().getAccount_number());
            intent.putExtra("name", ((BankUiModel) objectModel).getRequestModel().getAccount_name());
            intent.putExtra("bankName", ((BankUiModel) objectModel).getListModel().getName());
            intent.putExtra("isEdit", true);
            intent.putExtra("bankId", ((BankUiModel) objectModel).getListModel().getId());
            intent.putExtra("bank_code", ((BankUiModel) objectModel).getListModel().getCode());
            intent.putExtra("bank_logo", ((BankUiModel) objectModel).getListModel().getLogo());
            intent.putExtra("approval_status", ((BankUiModel) objectModel).getRequestModel().getApproval_status());

            if (paymentTypePos == 111) {
                setResult(RESULT_OK, intent);
                finish();
                return;
            }

            startActivityForResult(intent, 888);
        } else {
            Intent intent = new Intent(this, BankDetailActivity.class);
            intent.putExtra(BankDetailActivity.accountIntentData, new Gson().toJson((BankUiModel) objectModel));
            intent.putExtra(BankDetailActivity.listPos, position);
            startActivityForResult(intent,111);
        }
    }

    private void callListBankAccountAPI() {
        new ProfileDao(this).getBankList(BaseDao.getInstance(this, 987).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (response.isSuccessful()) {
            switch (responseCode) {
                case 987:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        BankAccountListResponseModel model = (BankAccountListResponseModel) br;
                        convertModel(model.getData().getAccount());
                    }
                    break;
                case 909:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        callListBankAccountAPI();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                case 910:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        callListBankAccountAPI();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                default:
                    break;
            }
        } else {
            ErrorDialog dialog = new ErrorDialog(this, this, false, false, getString(R.string.error_server), ((BaseResponseModel) br).getMeta().getMessage());
            dialog.show();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        onApiResponseError();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 888) {
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
            } else if (requestCode == 101 && data.hasExtra("isUpdated")) {
                if (data.getBooleanExtra("isUpdated", false)) callListBankAccountAPI();
            }
        }
    }

    @Override
    public void onDelete(int position) {
        bankAdapter.removeItem(position);
    }
}
