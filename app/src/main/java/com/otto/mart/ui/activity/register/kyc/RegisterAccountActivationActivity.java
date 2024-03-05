package com.otto.mart.ui.activity.register.kyc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.RegisterUpgradeAccountRequestModel;
import com.otto.mart.model.APIModel.Response.RegisterUpgradeAccountResponseModel;
import com.otto.mart.model.localmodel.Realm.ImageBase64StoreModel;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.component.ButtonToggleable;
import com.otto.mart.ui.component.LazyDialog;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.ByteArrayOutputStream;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import io.realm.Realm;
import retrofit2.Response;


public class RegisterAccountActivationActivity extends AppActivity {

    private View backBtn;
    private ExpandableLayout eLayout;
    private ButtonToggleable bt_action1, bt_action2, bt_action3;
    private TextView tv_action1, tv_action2;

    Bitmap basedata1, basedata2, basedata3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgradeaccount);
        initComponent();
        initContent();
    }

    private void initComponent() {
        backBtn = findViewById(R.id.backhitbox);
        bt_action1 = findViewById(R.id.bt_action1);
        bt_action2 = findViewById(R.id.bt_action2);
        bt_action3 = findViewById(R.id.bt_action3);

        tv_action1 = findViewById(R.id.tv_action1);
        tv_action2 = findViewById(R.id.tv_action2);

        eLayout = findViewById(R.id.eLayout);

    }

    private void initContent() {
        initRealm();
        bt_action1.getContentTextView().setText(getString(R.string.s_ain_2));
        bt_action2.getContentTextView().setText(getString(R.string.text_upload_ktp_and_merchant_picture));
        bt_action3.getContentTextView().setText(getString(R.string.text_upload_etalase_picture));
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jenk = new Intent(RegisterAccountActivationActivity.this, RegisterAccountActivationCamUsrActivity.class);
                jenk.putExtra("jenk", 1);
                startActivityForResult(jenk, 111);
            }
        });
        bt_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jenk = new Intent(RegisterAccountActivationActivity.this, RegisterAccountActivationCamUsrActivity.class);
                jenk.putExtra("jenk", 2);
                startActivityForResult(jenk, 112);
            }
        });
        bt_action3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jenk = new Intent(RegisterAccountActivationActivity.this, RegisterAccountActivationCamUsrActivity.class);
                jenk.putExtra("jenk", 3);
                startActivityForResult(jenk, 113);
            }
        });

        tv_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent jenk = new Intent(RegisterAccountActivationActivity.this, SnKActivity.class);
//                jenk.putExtra("jenk", 3);
//                startActivity(jenk);
                String url = "https://ottopay.id/terms";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        tv_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogComponent.showProgressDialog(RegisterAccountActivationActivity.this, "Sedang memproses request", false).show();
                Realm realm = Realm.getDefaultInstance();
                ImageBase64StoreModel storemodel = realm.where(ImageBase64StoreModel.class).findFirst();
                RegisterUpgradeAccountRequestModel request = new RegisterUpgradeAccountRequestModel();
                request.setId_photo(storemodel.getKtporang());
                request.setId_card_photo(storemodel.getKtp());
                request.setProduct_photo(storemodel.getEtalase());
                callUpgradeAPI(request);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterAccountActivationActivity.this, DashboardActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.RESULT_OK) {
            switch (requestCode) {
                case 111:
                    bt_action1.setButtonState(true);
                    if (bt_action2.isDone() && bt_action3.isDone()) {
                        eLayout.setExpanded(true);
                    }
                    break;
                case 112:
                    bt_action2.setButtonState(true);
                    if (bt_action1.isDone() && bt_action3.isDone()) {
                        eLayout.setExpanded(true);
                    }
                    break;
                case 113:
                    bt_action3.setButtonState(true);
                    if (bt_action1.isDone() && bt_action2.isDone()) {
                        eLayout.setExpanded(true);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case 444:
                    if (((RegisterUpgradeAccountResponseModel) br).getMeta().getCode() == 200) {
                        LazyDialog jenk = new LazyDialog(this, RegisterAccountActivationActivity.this, true);
                        View jenkview = LayoutInflater.from(RegisterAccountActivationActivity.this).inflate(R.layout.dialog_textonly, null);
                        ((TextView) jenkview.findViewById(R.id.tv_text)).setText(R.string.dialog_upgrade);
                        View jenkbtn = LayoutInflater.from(RegisterAccountActivationActivity.this).inflate(R.layout.cw_navigation_1btn_cust, null);
                        jenk.setContainerView(jenkview);
                        jenk.setNavigationView(jenkbtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent jenktent = new Intent(RegisterAccountActivationActivity.this, DashboardActivity.class);
                                startActivity(jenktent);
                                finish();
                            }
                        });
                        jenk.show();
                    } else {
                        Toast.makeText(this, "Pengiriman Data Gagal, mohon kirim ulang", Toast.LENGTH_SHORT).show();
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

    private String toBase64(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void callUpgradeAPI(RegisterUpgradeAccountRequestModel model) {
        AuthDao dao = new AuthDao(this);
        dao.upgradeAccountDao(model, BaseDao.getInstance(this, 444).callback);
    }

    public static void initRealm() {

        DataUtil.querryRealm(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                ImageBase64StoreModel dbModel = new ImageBase64StoreModel();
                dbModel.setSessionKey(0);
                realm.copyToRealmOrUpdate(dbModel);
            }
        });
    }

}