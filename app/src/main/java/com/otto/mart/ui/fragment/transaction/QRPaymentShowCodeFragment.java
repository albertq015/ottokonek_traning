package com.otto.mart.ui.fragment.transaction;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.payment.IndomaretQrPurchaseRequestModel;
import com.otto.mart.presenter.dao.TransactionDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.UIUtils;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.support.util.pref.PrefConstance;
import com.otto.mart.ui.activity.qr.scan.ScanQrActivity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.component.ProgressDialogComponent;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRPaymentShowCodeFragment extends BottomSheetDialogFragment {

    public static final int RC_INDOMARET_QR_PURCHASE = 2010;
    public static final int RC_INDOMARET_QR_PURCHASE_CANCEL = 2011;
    public static final int RC_INDOMARET_QR_PURCHASE_STATUS = 2012;
    public static final String RC_INDOMARET_QR_PURCHASE_SUCCESS_STATUS = "terbayar";

    private ViewAnimator viewAnimator;
    private LinearLayout contentLayout;
    private ImageView imgClose;
    private TextView tvTitle;
    private TextView tvTimer;
    private TextView tvProductName;
    private TextView tvPhone;
    private TextView btnCancel;
    private TextView tv_merchant;
    private TextView tv_address;
    private TextView tv_id;

    private BottomSheetBehavior<FrameLayout> behavior;
    private CountDownTimer mTimer;

    private bottomsheetDialogListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr_payment_show_code, container, false);
        initView(view);
        displayContent();
        return view;
    }

    public void setListener(bottomsheetDialogListener listener) {
        this.listener = listener;
    }

    private void initView(View view) {
        viewAnimator = view.findViewById(R.id.view_animator);
        contentLayout = view.findViewById(R.id.content_layout);
        imgClose = view.findViewById(R.id.img_close);
        tvTitle = view.findViewById(R.id.tv_merchant_name);
        tvTimer = view.findViewById(R.id.tv_timeer);
/*
        tvProductName = view.findViewById(R.id.tv_product_name);
*/
        tvPhone = view.findViewById(R.id.tv_phone);
        btnCancel = view.findViewById(R.id.btn_cancel);

        imgClose.setVisibility(View.GONE);

        tv_address = view.findViewById(R.id.tv_address);
        tv_merchant = view.findViewById(R.id.tv_merch);
        tv_id = view.findViewById(R.id.tv_id);
        UIUtils.resize(contentLayout,
                getActivity().getResources().getDisplayMetrics().widthPixels,
                getActivity().getResources().getDisplayMetrics().heightPixels);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimer.cancel();
                if (!getArguments().getBoolean("isParentFragment", false))
                    callIndomaretQrPurchaseCancel();
                else
                    callIndomaretQrPurchaseCancelFromFragmentContext();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        dialog.setCancelable(false);
        Objects.requireNonNull(bottomSheet).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void displayContent() {
        tvTitle.setText("Indomaret");
        tvTimer.setText("00:00");
        tvPhone.setText(SessionManager.getPhone() + "");

        tv_merchant.setText(getArguments().getString("t59"));
        tv_address.setText(getArguments().getString("t60"));
        tv_id.setText(getArguments().getString("t62"));
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (getActivity() != null) {
            ((ScanQrActivity) getActivity()).qrPaymentCodeFragmentDismiss();
        }
    }


    @Override
    public void dismiss() {
        mTimer.cancel();
        if (!getArguments().getBoolean("isParentFragment", false)) {
            //callIndomaretQrPurchaseCancel();
        } else {
            //callIndomaretQrPurchaseCancelFromFragmentContext();
        }
        if (listener != null)
            listener.onDismiss();
        super.dismiss();
    }

    @Override
    public void dismissAllowingStateLoss() {
        mTimer.cancel();
        if (!getArguments().getBoolean("isParentFragment", false))
            callIndomaretQrPurchaseCancel();
        else
            callIndomaretQrPurchaseCancelFromFragmentContext();
        if (listener != null)
            listener.onDismiss();
        super.dismissAllowingStateLoss();
    }

    public void callIndomaretQrPurchaseCancel() {
        ProgressDialogComponent.showProgressDialog(getActivity(), "Mohon menunggu", false).show();

        IndomaretQrPurchaseRequestModel indomaretQrPurchaseRequestModel = new IndomaretQrPurchaseRequestModel();
        indomaretQrPurchaseRequestModel.setStore_id(Pref.getPreference().getString(PrefConstance.STORE_ID));
        indomaretQrPurchaseRequestModel.setProduct_code("OTTOMART");

        new TransactionDao(this).getIndomaretQrPurchaseCancel(indomaretQrPurchaseRequestModel, BaseDao.getInstance(getActivity(), RC_INDOMARET_QR_PURCHASE_CANCEL).callback);
    }

    public void callIndomaretQrPurchaseCancelFromFragmentContext() {
        ProgressDialogComponent.showProgressDialog(getActivity(), "Mohon menunggu", false).show();

        IndomaretQrPurchaseRequestModel indomaretQrPurchaseRequestModel = new IndomaretQrPurchaseRequestModel();
        indomaretQrPurchaseRequestModel.setStore_id(Pref.getPreference().getString(PrefConstance.STORE_ID));
        indomaretQrPurchaseRequestModel.setProduct_code("OTTOMART");

        new TransactionDao(this).getIndomaretQrPurchaseCancel(indomaretQrPurchaseRequestModel, BaseDao.getInstance(getParentFragment(), RC_INDOMARET_QR_PURCHASE_CANCEL).callback);
    }

    public void purchaseSuccess() {
        mTimer = new CountDownTimer(180000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("" + String.format("%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                try {
                    callIndomaretQrPurchaseCancel();
                    dismiss();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }.start();

        viewAnimator.setDisplayedChild(1);
    }

    public interface bottomsheetDialogListener {
        void onDismiss();

        void onStart();
    }
}
