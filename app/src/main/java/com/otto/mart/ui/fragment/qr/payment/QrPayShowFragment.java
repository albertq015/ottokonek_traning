package com.otto.mart.ui.fragment.qr.payment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.QrStringRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.QrStringResponseModel;
import com.otto.mart.presenter.dao.BENIDAO;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.BitmapUtil;
import com.otto.mart.support.util.DeviceUtil;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.dialog.QrDynamicDialog;
import com.otto.mart.ui.fragment.AppFragment;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public class QrPayShowFragment extends AppFragment {

    private Context mContext;
    private View mView, action;
    private Bundle viewState;
    private ImageView imgv;
    private String merchid;
    private LazyEdittext let_ngutang;
    private QrDynamicDialog qdd;
    private ProgressBar pb_qr;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(R.layout.fagment_qrpay_show, container, false);
        this.viewState = savedInstanceState;
        initComponent();
        initContent();
        return mView;
    }

    private void initComponent() {
        getActivity().
                getWindow().
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        imgv = mView.findViewById(R.id.qr);

        merchid = SessionManager.getUserProfile().getMerchant_id();
        if (merchid == null) {
            Toast.makeText(mContext, "Terjadi kesalahan pada server, Mohon login ulang", Toast.LENGTH_SHORT).show();
            SessionManager.logout();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        qdd = new QrDynamicDialog(getContext(), getActivity(), false);
        pb_qr = mView.findViewById(R.id.pb_qr);
    }

    private void initContent() {
        if(SessionManager.getQrContent().equals("")){
            callGenerateQrStringApi(null, null);
        } else {
            imgv.setImageBitmap(BitmapUtil.generateBitmap(SessionManager.getQrContent(), 9, DeviceUtil.dpToPx(300), DeviceUtil.dpToPx(300)));
            pb_qr.setVisibility(View.GONE);
        }

        let_ngutang = mView.findViewById(R.id.cust_ngutang);
        action = mView.findViewById(R.id.action);
        action.setVisibility(View.GONE);

        try {
            ((EditText) let_ngutang.getComponent()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(final Editable s) {
                    if (s.length() > 2) {
                        action.setVisibility(View.VISIBLE);
                    } else {
                        action.setVisibility(View.GONE);
                    }

                }
            });
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qdd.setAmount(let_ngutang.getTextContent());
                callGenerateQrStringApi(merchid, let_ngutang.getTextContent());
                qdd.show();
            }
        });
    }

    private void callGenerateQrStringApi(String merchid, String payment) {
        BENIDAO dao = new BENIDAO(this);
        if (payment != null) {
            QrStringRequestModel requestModel = new QrStringRequestModel();
            requestModel.setAmount(Double.parseDouble(payment.replaceAll("\\.", "")));
            requestModel.setBill_ref_num("");
            if (qdd == null)
                qdd = new QrDynamicDialog(getContext(), getActivity(), false);
            dao.qrString(requestModel, BaseDao.getInstance(this, 444).callback);
        } else {
            QrStringRequestModel requestModel = new QrStringRequestModel();
            requestModel.setAmount(0.0);
            requestModel.setBill_ref_num("");
            dao.qrString(requestModel, BaseDao.getInstance(this, 445).callback);
        }
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (response.isSuccessful() && response.code() == 200) {
            switch (responseCode) {
                case 444:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        if (qdd != null)
                            qdd.setQr(((QrStringResponseModel) br).getQr_string());
                    } else {
                        if (qdd != null)
                            qdd.dismiss();
                        Toast.makeText(mContext, "gagal membuat kode qr", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 445:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        SessionManager.setQrContent(((QrStringResponseModel) br).getQr_string());
                        imgv.setImageBitmap(BitmapUtil.generateBitmap(((QrStringResponseModel) br).getQr_string(), 9, DeviceUtil.dpToPx(300), DeviceUtil.dpToPx(300)));
                        pb_qr.setVisibility(View.GONE);
                    } else
                        Toast.makeText(mContext, "gagal membuat kode qr", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message) {
        //super.onApiFailureCallback(message);
        onApiResponseError();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (contentInitialized) {
//            mScannerView.setResultHandler(scannerHandler);
//            mScannerView.startCamera();
//        }

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }
}

