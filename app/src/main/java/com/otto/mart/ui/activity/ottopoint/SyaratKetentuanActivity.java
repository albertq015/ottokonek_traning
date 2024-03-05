package com.otto.mart.ui.activity.ottopoint;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.ottopoint.OpRegisterResponseModel;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.MessageHelper;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.component.ActionbarOttopointWhite;

import app.beelabs.com.codebase.base.response.BaseResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class SyaratKetentuanActivity extends BaseActivityOttopoint {

    private String TAG = SyaratKetentuanActivity.class.getSimpleName();

    private static final int REQUEST_PAGE_PIN = 100;

    @BindView(R.id.rd_check_one)
    CheckBox checkOne;
    @BindView(R.id.rd_check_two)
    CheckBox checkTwo;
    @BindView(R.id.btn_aktivasi)
    Button btnAktivasi;
    @BindView(R.id.tv_nomor_hp)
    TextView tvNomorHp;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;
    @BindView(R.id.webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syarat_ketentuan);
        ButterKnife.bind(this);

        // set views

        webView.loadUrl("file:///android_asset/html/syarat_dan_ketentuan.html");
        webView.setBackgroundColor(Color.TRANSPARENT);
        tvNomorHp.setText(SessionManager.getPhone());

        checkOne.setOnCheckedChangeListener((buttonView, isChecked) -> btnAktivasi.setEnabled(isRequirementIsComplete()));
        checkTwo.setOnCheckedChangeListener((buttonView, isChecked) -> btnAktivasi.setEnabled(isRequirementIsComplete()));

        viewActionbar.setActionMenuLeft(view -> onBackPressed());
    }

    @OnClick(R.id.btn_aktivasi)
    public void submit(){
        if(!isRequirementIsComplete()){
            Toast.makeText(SyaratKetentuanActivity.this, R.string.text_info_check_syarat_ketentuan, Toast.LENGTH_SHORT).show();
            return;
        }

        // do something in here
        nextStep();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_PAGE_PIN:
                checkResultPin(resultCode);
                break;
        }
    }

    private void checkResultPin(int resultCode){
        if(resultCode == RESULT_OK) callApi();
    }

    private void nextStep(){
        //old
        //startActivity(new Intent(SyaratKetentuanActivity.this, VerifikasiOtpOttopoint.class));
        //finish();

        // new 25-07-2019
        Intent intent = new Intent(SyaratKetentuanActivity.this, RegisterPinResetActivity.class);
        intent.putExtra("confirm", true);
        startActivityForResult(intent, REQUEST_PAGE_PIN);
    }

    private boolean isRequirementIsComplete(){
        return checkOne.isChecked() && checkTwo.isChecked();
    }

    private void succesRegisterAction(long point){
        callToUpdateViewDashboard();

        OttomartActivity.openPage(SyaratKetentuanActivity.this, OttomartActivity.TYPE_RESPONSE_SUCCESS_REGISTER_OTTOPOINT, point);
        finish();
    }

    private void callApi(){
        showProgress(SyaratKetentuanActivity.this,true);
        OttoPointDao.createAccountOttopoint(SyaratKetentuanActivity.this, new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                showProgress(SyaratKetentuanActivity.this,false);

                long point = 0;
                if(br instanceof OpRegisterResponseModel){
                    OpRegisterResponseModel result = (OpRegisterResponseModel) br;

                    point = result.getData().getPoint();
                }

                succesRegisterAction(point);
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                showProgress(SyaratKetentuanActivity.this,false);

                MessageHelper.userMessageDialog(SyaratKetentuanActivity.this, message);
            }
        });
    }
}
