package com.otto.mart.ui.activity.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.AddressModel;
import com.otto.mart.model.APIModel.Misc.AuthDataModel;
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel;
import com.otto.mart.model.APIModel.Misc.ProfileResponseData;
import com.otto.mart.model.APIModel.Response.ProfileResponseModel;
import com.otto.mart.model.localmodel.ui.HeaderModel;
import com.otto.mart.presenter.dao.ProfileDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.Partials.HeaderViewGroup;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.register.kyc.RegisterAccountActivationActivity;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_GET_PROFILE;

public class ProfileActivity extends AppActivity implements SwipeRefreshLayout.OnRefreshListener {

    private View back, edit, logout, changepin, upgrade, phoneContainer;
    private HeaderViewGroup header;
    private ImageView profilePicture, bankImage;
    private TextView hProdName, hProdType, merchantId, hFullName, addressName, address, address1, bankName, accountNumber, beneficiaryName;
    private LazyTextview fullName, primaryPhone, email, dob, prodType, prodName, secondaryPhone;
    private List<AddressModel> addresses;
    private List<BankAccountModel> banks;
    private ViewGroup addressActon, bankAction, alamatContainer;
    private ProfileResponseData profileData;
    private LazyDialog upgradeDialog;
    private SwipeRefreshLayout swipeRefresh;

    private LinearLayout bank_wrapper;
    private TextView bankAction_textview;
    private ViewAnimator viewAnimator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initComponent();
        initContent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //getProfile();
    }

    @Override
    public void onRefresh() {
        //getProfile();
    }

    private void initComponent() {
        back = findViewById(R.id.backhitbox);
        edit = findViewById(R.id.edit_prof);
        logout = findViewById(R.id.logout_hitbox);
        changepin = findViewById(R.id.gantipin);
        header = findViewById(R.id.header);
        profilePicture = findViewById(R.id.imgv);
        bankImage = findViewById(R.id.bank_image);
        hProdName = findViewById(R.id.s_name);
        hProdType = findViewById(R.id.b_name);
        merchantId = findViewById(R.id.s_id);
        hFullName = findViewById(R.id.p_name);
        fullName = findViewById(R.id.name);
        primaryPhone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        dob = findViewById(R.id.dob);
        prodType = findViewById(R.id.prod_type);
        prodName = findViewById(R.id.prod_name);
        addressName = findViewById(R.id.address_name);
        address = findViewById(R.id.address);
        address1 = findViewById(R.id.address1);
        bankName = findViewById(R.id.bank_name);
        accountNumber = findViewById(R.id.account_number);
        beneficiaryName = findViewById(R.id.beneficiary_name);
        addressActon = findViewById(R.id.addressAction);
        bankAction = findViewById(R.id.bankAction);
        upgrade = findViewById(R.id.hitbox_upgrade);
        phoneContainer = findViewById(R.id.phoneContainer);
        secondaryPhone = findViewById(R.id.secondaryPhone);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        upgradeDialog = new LazyDialog(this, this, false);

        bank_wrapper = findViewById(R.id.bank_wrapper);
        bankAction_textview = findViewById(R.id.bankAction_text);
        alamatContainer = findViewById(R.id.layout_alamatcontainer);
        viewAnimator = findViewById(R.id.view_animator);
    }

    private void initContent() {
        swipeRefresh.setOnRefreshListener(this);

        header.initWidget(new HeaderModel("-", "-", "-", "-", R.drawable.placeholder));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileChangeActivity.class);
                intent.putExtra("profile", profileData);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.logout();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        changepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileChangePinActivity.class);
                startActivity(intent);
            }
        });
        addressActon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AddressListActivity.class);
                intent.putParcelableArrayListExtra("addresses", (ArrayList<? extends Parcelable>) addresses);
                startActivity(intent);
            }
        });
        bankAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, BankListActivity.class);
                intent.putExtra(AppKeys.KEY_BANK_LIST_NOT_SELECTABLE, true);

                if (banks != null) {
//                    intent.putParcelableArrayListExtra("banks", (ArrayList<? extends Parcelable>) banks);
                }

                startActivity(intent);
            }
        });

        buildUpgradeAccountDialog();
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                upgradeDialog.show();
                startActivity(new Intent(ProfileActivity.this, KYCActivity.class));
            }
        });
    }

    private void getProfile() {
        new ProfileDao(this).getProfile(BaseDao.getInstance(this, API_KEY_GET_PROFILE).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        switch (responseCode) {
            case AppKeys.API_KEY_GET_PROFILE: {
                handleProfileResponse((ProfileResponseModel) br, response);
                swipeRefresh.setRefreshing(false);
                break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    private void handleProfileResponse(ProfileResponseModel br, Response response) {
        if (br != null) {
            if (response.code() == 200 || response.code() == 201 || response.code() == 202) {
                profileData = br.getData();

                //profile
                if (profileData != null && profileData.getAvatar() != null && !profileData.getAvatar().equals(""))
                    Glide
                            .with(this)
                            .load(profileData.getAvatar())
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .onlyRetrieveFromCache(false))
                            .into(profilePicture);

                hProdName.setText(profileData.getName());
                hProdType.setText(profileData.getBusinessCategoryName());
                merchantId.setText(profileData.getMerchantId());
                hFullName.setText(profileData.getMerchantName());
                fullName.setText(profileData.getMerchantName());
                primaryPhone.setText(profileData.getPhone());
                if (profileData.getEmail() != null)
                    email.setText(profileData.getEmail());

//                if (profileData.getDob() != 0)
//                    dob.setText(DataUtil.getDateString(profileData.getDob(), DataUtil.DEFAULT_FORMAT, true, 0));
//                else dob.setText("-");
                dob.setText(profileData.getDob());

                if (profileData != null && profileData.getSecondaryPhone() != null && !profileData.getSecondaryPhone().equals("")) {
                    secondaryPhone.setText(profileData.getSecondaryPhone());
                    secondaryPhone.setText(profileData.getSecondaryPhone());
                    phoneContainer.setVisibility(View.VISIBLE);
                } else phoneContainer.setVisibility(View.GONE);
                prodType.setText(profileData.getBusinessCategoryName());
                prodName.setText(profileData.getName());

                //address
                if (profileData != null && profileData.getAddresses() != null && profileData.getAddresses().size() > 0) {
                    addresses = profileData.getAddresses();
                    addressName.setText(addresses.get(0).getName());
                    address.setText(addresses.get(0).getCompleteAddress());
                    String address = addresses.get(0).getDistrictName() + " - " +
                            addresses.get(0).getCityName() + " - " +
                            addresses.get(0).getProvinceName();

                    address1.setText(address);
                } else {
                    alamatContainer.setVisibility(View.GONE);
                }

                //bank
                if (profileData.getAccounts() != null && profileData.getAccounts().size() > 0) {
                    banks = profileData.getAccounts();
                    Glide.with(this).load(banks.get(0).getBankLogo()).into(bankImage);
                    bankName.setText(banks.get(0).getBankName());
                    accountNumber.setText(banks.get(0).getAccountNumber());
                    beneficiaryName.setText(banks.get(0).getAccountOwner());
                    bank_wrapper.setVisibility(View.VISIBLE);
                    bankAction_textview.setText(getString(R.string.button_open_banks));

                } else {
                    bank_wrapper.setVisibility(View.GONE);
                    bankAction_textview.setText(getString(R.string.button_add_bank_account));
                }

                AuthDataModel authdb = new AuthDataModel();
                authdb.setName(profileData.getName());
                authdb.setMerchant_name(profileData.getMerchantName());
                authdb.setEmail(profileData.getEmail());
                authdb.setAvatar(profileData.getAvatar());
                authdb.setAvatar_thumb(profileData.getAvatarThumb());
                authdb.setSecondary_phone(profileData.getSecondaryPhone());
                authdb.setBusiness_category_name(profileData.getBusinessCategoryName());
                SessionManager.updateUserDataRealm(authdb);

            } else if (br.getMeta() != null && br.getMeta().getCode() == 498) {
                SessionManager.logout();
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            viewAnimator.setDisplayedChild(1);
        } else {
            ErrorDialog dialog = new ErrorDialog(ProfileActivity.this, ProfileActivity.this, false, false, "Terjadi kesalahan pada server", "");
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            dialog.show();
        }
    }

    private void buildUpgradeAccountDialog() {
        View mView = getLayoutInflater().inflate(R.layout.dialog_upgradeacc, null);
        mView.findViewById(R.id.action1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jenk = new Intent(ProfileActivity.this, RegisterAccountActivationActivity.class);
                startActivity(jenk);
                finish();
            }
        });

        mView.findViewById(R.id.action2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jenk = new Intent(ProfileActivity.this, DashboardActivity.class);
                startActivity(jenk);
                finish();
            }
        });

        upgradeDialog.setContainerView(mView);
        upgradeDialog.setToolbarDarkmode();
        ((TextView) upgradeDialog.getToolbarView().findViewById(R.id.title)).setText(getString(R.string.button_upgrade_accouint));
    }
}