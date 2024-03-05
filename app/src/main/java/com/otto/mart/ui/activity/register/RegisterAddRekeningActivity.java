package com.otto.mart.ui.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.otto.mart.R;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.model.APIModel.Misc.CategoryModel;
import com.otto.mart.model.APIModel.Misc.bank.BankListingModel;
import com.otto.mart.model.APIModel.Request.bank.AddBankDepositRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.GetBankResponseModel;
import com.otto.mart.model.APIModel.Response.bank.BankListItem;
import com.otto.mart.model.APIModel.Response.bank.BankListResponse;
import com.otto.mart.presenter.dao.deposit.TransferDepositDao;
import com.otto.mart.support.util.ApiCallback;
import com.otto.mart.ui.Partials.adapter.SpinnerBankModelAdapter;
import com.otto.mart.ui.Partials.adapter.SpinnerCategoryModelAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.transaction.balance.OmzetActivity;
import com.otto.mart.ui.component.HidableBankSpinnerView;
import com.otto.mart.ui.component.HideableSpinnerView;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.dialog.Popup;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class RegisterAddRekeningActivity extends AppActivity {

    final int API_KEY_ADD_BANK_DEPOSIT = 100;

    private List<CategoryModel> placeholderValue = new ArrayList<>();

    private HideableSpinnerView hsv_cair;
    private HidableBankSpinnerView hsv_bank;

    private LazyEdittext let_norek;
    private LazyTextview ltv_owner;

    private ExpandableLayout eLayout;
    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle, tvMessage;
    private Button btnSubmit;
    private CheckBox cb_main;

    private BankListingModel selectedModel;
    private int retryCount;
    private int id = -1;

    private LazyEdittext accountOwner;

    private boolean isFromDeposit = false;
    private String selectedBankCode;
    private int selectedBankPos = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahrekejing);

        if (getIntent().hasExtra(OmzetActivity.KEY_IS_FROM_DEPOSIT)) {
            isFromDeposit = getIntent().getBooleanExtra(OmzetActivity.KEY_IS_FROM_DEPOSIT, false);
        }

        initPlaceholder();
        initComponent();
        initContent();
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        hsv_cair = findViewById(R.id.hsv_jeniscair);
        hsv_bank = findViewById(R.id.hsv_bank);
        ltv_owner = findViewById(R.id.ltv_name);
        let_norek = findViewById(R.id.let_rekening);
        tvMessage = findViewById(R.id.tvMessage);
        cb_main = findViewById(R.id.cb_main);
        btnSubmit = findViewById(R.id.btnSubmit);

        ((EditText) let_norek.getComponent()).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        if (isFromDeposit) {
            cb_main.setVisibility(View.GONE);
        }

        eLayout = findViewById(R.id.eLayout_form);
        eLayout.setExpanded(false);
        btnSubmit.setEnabled(false);

        btnSubmit = findViewById(R.id.btnSubmit);
        accountOwner = findViewById(R.id.accountOwner);
    }

    private void initContent() {
        tvToolbarTitle.setText(getString(R.string.button_add_bank_account));

        hsv_cair.addData(placeholderValue);
        hsv_bank.expandLoading();
        callBankListApi();
        if (getIntent().hasExtra("listmodel")) {
            id = ((BankListingModel) getIntent().getParcelableExtra("listmodel")).getId();
        }

        if (getIntent().getIntExtra("position", -1) == 0) {
            cb_main.setChecked(true);
            cb_main.setVisibility(View.GONE);
        }

        eLayout.setExpanded(true);

        hsv_cair.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                eLayout.setExpanded(true);
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
                if (getIntent().hasExtra("isEdit") && getIntent().hasExtra("paymentposition")) {
                    adapter.deVirgin();
//                    adapter.notifyDataSetChanged();
                    view.setSelection(getIntent().getIntExtra("paymentposition", -1));
                }
            }
        });

        hsv_bank.setOnCallback(new HidableBankSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerBankModelAdapter adapter) {
                btnSubmit.setVisibility(View.VISIBLE);
                selectedModel = (BankListingModel) adapter.getItem(position);
                btnSubmit.setEnabled(true);
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
            public void onDataLoaded(Spinner view, SpinnerBankModelAdapter adapter) {
                if (getIntent().hasExtra("isEdit") && getIntent().hasExtra("bankposition")) {
                    adapter.deVirgin();
//                    adapter.notifyDataSetChanged();
//                    view.performClick();
                    view.setSelection(selectedBankPos);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!accountOwner.getTextContent().equals("")) {
                    if (getIntent().getBooleanExtra("isInternalEdit", false)) {
                        showEditConfirmation();
                    } else if (isFromDeposit) {
                        showAddConfirm();
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("bankid", selectedModel.getId());
                        returnIntent.putExtra("logoUri", selectedModel.getLogo());
                        returnIntent.putExtra("bankName", selectedModel.getName());
                        returnIntent.putExtra("bankCode", selectedModel.getCode());
                        returnIntent.putExtra("accountNum", let_norek.getTextContent());
                        returnIntent.putExtra("accountName", accountOwner.getTextContent());
                        returnIntent.putExtra("setMain", cb_main.isChecked());
                        returnIntent.putExtra("id", id);
                        returnIntent.putExtra("bankPos", hsv_bank.getSelectedItemPos());
                        returnIntent.putExtra("payPos", hsv_cair.getSelectedItemPos());
                        if (getIntent().getIntExtra("position", -1) >= 0) {
                            returnIntent.putExtra("ItemPosition", getIntent().getIntExtra("position", -1));
                        }
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                } else {
                    ((EditText) accountOwner.getComponent()).setError(getString(R.string.message_empty_field));
                }
            }
        });

        if (getIntent().getBooleanExtra("isEdit", false)) {
            tvToolbarTitle.setText(getString(R.string.label_edit_account));
            btnSubmit.setText(R.string.button_save_changes);

            if (
//                    getIntent().hasExtra("position") &&
//                    getIntent().hasExtra("bankposition") &&
                    getIntent().hasExtra("norek") &&
                            getIntent().hasExtra("name")) {
                eLayout.setExpanded(true);
                btnSubmit.setEnabled(true);
                let_norek.setContentText(getIntent().getStringExtra("norek"));
                accountOwner.setContentText(getIntent().getStringExtra("name"));
            }
        } else {
            if (!isFromDeposit) {
                tvMessage.setVisibility(View.VISIBLE);
            }
        }

        btnToolbarBack.setOnClickListener(v -> onBackPressed());
    }

    private void showAddConfirm() {
        Popup popup = Popup.getConfirmDialog(getString(R.string.button_add_bank_account), getString(R.string.message_add_bank));
        popup.setPositiveButton(getString(R.string.text_yes));
        popup.setNegativeButton(getString(R.string.button_batalkan));
        popup.setPositiveAction(() -> {
            addBankOttokonek();
            return null;
        });
        popup.setNegativeAction(() -> null);
        popup.singleShow(getSupportFragmentManager(), "addConfirm");
    }

    private void showEditConfirmation() {
        Popup popup = Popup.getConfirmDialog(getString(R.string.label_edit_account), getString(R.string.message_edit_bank));
        popup.setPositiveButton(getString(R.string.text_yes));
        popup.setNegativeButton(getString(R.string.button_batalkan));
        popup.setPositiveAction(() -> {
            editBankOttokonek();
            return null;
        });
        popup.setNegativeAction(() -> null);
        popup.singleShow(getSupportFragmentManager(), "addConfirm");
    }

    private void addBankDeposit() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show();

        AddBankDepositRequest addBankDepositRequest = new AddBankDepositRequest();
        addBankDepositRequest.setAccount_number(let_norek.getTextContent());
        addBankDepositRequest.setAccount_owner(accountOwner.getTextContent());
        addBankDepositRequest.setBank_name(selectedModel.getName());
        addBankDepositRequest.setBank_code(selectedModel.getCode());

        new TransferDepositDao(this).addBankDeposit(addBankDepositRequest, BaseDao.getInstance(this, API_KEY_ADD_BANK_DEPOSIT).callback);
    }

    private void addBankOttokonek() {
        showProgressDialog(R.string.loading_message);

        AddBankDepositRequest addBankDepositRequest = new AddBankDepositRequest();
        addBankDepositRequest.setAccount_number(let_norek.getTextContent());
        addBankDepositRequest.setAccount_owner(accountOwner.getTextContent());
        addBankDepositRequest.setBank_name(selectedModel.getName());
        addBankDepositRequest.setBank_code(selectedModel.getCode());

        OttoKonekAPI.addBank(this, addBankDepositRequest, new ApiCallback<BaseResponseModel>() {

            @Override
            public void onResponseSuccess(BaseResponseModel body) {
                dismissProgressDialog();
                if (body != null) {
                    if (isSuccess200) showSuccessPopup();
                    else showErrorDialog(body.getMeta().getMessage());
                }
            }

            @Override
            public void onApiServiceFailed(Throwable throwable) {
                dismissProgressDialog();
                onApiResponseError();
            }
        });
    }

    private void editBankOttokonek() {
        showProgressDialog(R.string.loading_message);

        AddBankDepositRequest addBankDepositRequest = new AddBankDepositRequest();
        addBankDepositRequest.setAccount_number(let_norek.getTextContent());
        addBankDepositRequest.setAccount_owner(accountOwner.getTextContent());
        addBankDepositRequest.setBank_code(selectedModel.getCode());
        addBankDepositRequest.setId(getIntent().getIntExtra("id", 0));
        addBankDepositRequest.setOld_account_number(getIntent().getStringExtra("norek"));

        OttoKonekAPI.editBank(this, addBankDepositRequest, new ApiCallback<BaseResponseModel>() {

            @Override
            public void onResponseSuccess(BaseResponseModel body) {
                dismissProgressDialog();
                if (body != null) {
                    if (isSuccess200) showSuccessPopup();
                    else showErrorDialog(body.getMeta().getMessage());
                }
            }

            @Override
            public void onApiServiceFailed(Throwable throwable) {
                dismissProgressDialog();
                onApiResponseError();
            }
        });
    }

    private void callBankListApi() {
//        EtcDao dao = new EtcDao(this);
//        dao.getBankDao(BaseDao.getInstance(this, 444).callback);

        OttoKonekAPI.bankList(this, new ApiCallback<BankListResponse>() {
            @Override
            public void onResponseSuccess(BankListResponse body) {
                dismissProgressDialog();
                if (isSuccess200) {
                    if (body != null && body.getData() != null) {
                        List<BankListingModel> banks = new ArrayList<>();
                        BankListingModel itemModel;
                        BankListItem item;

                        for (int i = 0; i < body.getData().size(); i++) {
                            item = body.getData().get(i);
                            itemModel = new BankListingModel();
                            itemModel.setCode(item.getCode());
                            itemModel.setId(i);
                            itemModel.setLogo(item.getUrlImage());
                            itemModel.setName(item.getFullName());

                            banks.add(itemModel);
                            if (itemModel.getCode().equalsIgnoreCase(selectedBankCode))
                                selectedBankPos = i;
                        }

                        hsv_bank.addData(banks);
                    }
                } else showErrorDialog(body.getMeta().getMessage());
            }

            @Override
            public void onApiServiceFailed(Throwable throwable) {
                dismissProgressDialog();
                onApiResponseError();
            }
        });
    }

    @Override
    public void onBackPressed() {
        showDiscardConfirm();
    }

    private void showDiscardConfirm() {
        Popup popup = Popup.getConfirmDialog(getString(R.string.text_confirmation), getString(R.string.message_discard));
        popup.setPositiveButton(getString(R.string.text_no));
        popup.setNegativeButton(getString(R.string.text_yes));
        popup.setPositiveAction(() -> null);
        popup.setNegativeAction(() -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
            return null;
        });
        popup.singleShow(getSupportFragmentManager(), "discardConfirm");
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case 444:
                    if (((GetBankResponseModel) br).getMeta().getCode() == 200) {
                        List<BankListingModel> models = ((GetBankResponseModel) br).getBanks();
                        if (selectedBankCode != null) {
                            for (int i = 0; i < models.size(); i++) {
                                if (models.get(i).getCode().equalsIgnoreCase(selectedBankCode)) {
                                    selectedBankPos = i;
                                    break;
                                }
                            }
                        }
                        hsv_bank.addData(models);
                    }
                    break;
                case API_KEY_ADD_BANK_DEPOSIT:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        showSuccessPopup();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.setOnDismissListener(dialog1 -> finish());
                        dialog.show();
                    }
                    break;
                default:
                    break;
            }
        }

    }

    private void showSuccessPopup() {
        Popup popup = new Popup();
        popup.setTitle(getString(R.string.text_please_wait));
        popup.setMessage(getString(R.string.message_create_bank_account));
        popup.setPositiveButton(getString(R.string.btn_close));
        popup.setHideBtnNegative(true);
        popup.setImagePopupLogo(R.drawable.ic_time_green);
        popup.setPositiveAction(() -> {
            setResult(RESULT_OK);
            finish();
            return null;
        });
        popup.singleShow(getSupportFragmentManager(), "successPopup");
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        onApiResponseError();

        if (retryCount < 6) {
            retryCount++;
            callBankListApi();
        } else {
            hsv_bank.hideLoading();
        }

        ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, message, message);
        errorDialog.show();
    }

    private void initPlaceholder() {
        CategoryModel placeholder1 = new CategoryModel();
        placeholder1.setId(1);
        placeholder1.setTitle("Rekening Bank");
        CategoryModel placeholder2 = new CategoryModel();
        placeholder2.setId(2);
        placeholder2.setTitle("Wallet");
        placeholderValue.add(placeholder1);
        placeholderValue.add(placeholder2);
    }

}
