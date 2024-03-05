package com.otto.mart.ui.component.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.ui.actionView.ActionDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EarnPointDialog extends BaseBottomDialog {

    private static final int TYPE_DEFAULT = 1;
    private static final int TYPE_FIRST_REGISTER = 2;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_desc)
    TextView tvDesc;

    private ActionDialog callback;

    private int type = 1;
    private long point = 0;
    private String asalPembelian = "";

    private EarnPointDialog(@NonNull Context context, int themeResId, ActionDialog callback, int type, long point) {
        super(context, themeResId);

        this.callback = callback;
        this.type = type;
        this.point = point;
    }

    private EarnPointDialog(@NonNull Context context, int themeResId, ActionDialog callback, int type, long point, String asalPembelian) {
        super(context, themeResId);

        this.callback = callback;
        this.type = type;
        this.point = point;
        this.asalPembelian = asalPembelian;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_earn_poin_ottopoint);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);


        switch (type){
            case TYPE_FIRST_REGISTER:
                tvTitle.setText(R.string.text_berhasil_daftar);
                tvDesc.setText(getTextFromSource(R.string.text_berhasil_daftar_first_registration) + " " + CommonHelper.currencyFormat(point) + " " + getTextFromSource(R.string.label_poin));
                break;

            case TYPE_DEFAULT:
                tvTitle.setText(getTextFromSource(R.string.text_desc_earn_poin_one) + " +" + CommonHelper.currencyFormat(point));
                tvDesc.setText(getTextFromSource(R.string.text_desc_earn_poin_one) + " " + CommonHelper.currencyFormat(point) + " " + getTextFromSource(R.string.text_desc_earn_poin_two) + " " + asalPembelian);
                break;

            default:
                break;
        }
    }

    @OnClick(R.id.view_back)
    public void closeDialog(){
        dismiss();
    }

    @OnClick(R.id.btn_ok)
    public void submit(){
        dismiss();

        Bundle data = new Bundle();
        callback.submitAction(data);
    }

    public static void showDialog(Context context, long point, String asalPembelian, ActionDialog callback){
        new EarnPointDialog(context, R.style.CustomDialog_Clear_FromBottom, callback, TYPE_DEFAULT, point, asalPembelian).show();
    }

    public static void showFirstRegistration(Context context, long point, ActionDialog callback){
        new EarnPointDialog(context, R.style.CustomDialog_Clear_FromBottom, callback, TYPE_FIRST_REGISTER, point).show();
    }
    public static void showFirstRegistration2(Context context, long point, ActionDialog callback){
        new EarnPointDialog(context, R.style.CustomDialog_Clear_FromBottom, callback, TYPE_FIRST_REGISTER, point).show();
    }

    private String getTextFromSource(int source){
        return getContext().getString(source);
    }
}
