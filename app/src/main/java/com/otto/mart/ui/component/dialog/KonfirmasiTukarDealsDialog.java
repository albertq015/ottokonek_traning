package com.otto.mart.ui.component.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.DealsItemModel;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.support.util.DownloadImageManager;
import com.otto.mart.support.util.MessageHelper;
import com.otto.mart.ui.actionView.ActionDialog;
import com.otto.mart.ui.activity.ottopoint.DetailVoucherKonfirmasiActivity;
import com.otto.mart.ui.component.ViewTotalHargaPoint;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.otto.mart.ui.activity.ottopoint.DetailVoucherKonfirmasiActivity.VOUCHER_GAME;
import static com.otto.mart.ui.activity.ottopoint.DetailVoucherKonfirmasiActivity.VOUCHER_TOKEN_LISTRIK;
import static com.otto.mart.ui.activity.ottopoint.DetailVoucherKonfirmasiActivity.categoryMobileLegend;

public class KonfirmasiTukarDealsDialog extends BaseBottomDialog {

    public static final String KEY_NOMOR_TUJUAN = "key_nomor_tujuan";
    public static final String KEY_SERVER_ID = "key_server_id";

    @BindView(R.id.img_company)
    ImageView imgCompany;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.view_total_harga_point)
    ViewTotalHargaPoint viewTotalHargaPoint;
    @BindView(R.id.tv_jumlah_product)
    TextView tvJumlahProduct;
    @BindView(R.id.tv_point_ottopoint)
    TextView tvPointOttopoint;
    @BindView(R.id.btn_yes)
    Button btnYes;
    @BindView(R.id.tv_info_number)
    TextView tvInfoNumber;
    @BindView(R.id.tv_nomor_tujuan)
    TextView tvNomorTujuan;
    @BindView(R.id.view_server_id)
    View viewServerId;
    @BindView(R.id.tv_server_id)
    TextView tvServerId;
    @BindView(R.id.view_nomor_tujuan)
    View viewNomorTujuan;
    @BindView(R.id.progress_button_yes)
    View progressButtonYes;

    private int numberVoucher;
    private ActionDialog callback;
    private DealsItemModel dataDetail;
    private double currentPoint = 0;
    private Bundle extraData;

    private KonfirmasiTukarDealsDialog(@NonNull Context context, int themeResId, DealsItemModel dataDetail, int numberVoucher, ActionDialog callback) {
        super(context, themeResId);

        this.callback = callback;
        this.dataDetail = dataDetail;
        this.numberVoucher = numberVoucher;
    }

    private KonfirmasiTukarDealsDialog(@NonNull Context context, int themeResId, DealsItemModel dataDetail, int numberVoucher, Bundle data, ActionDialog callback) {
        super(context, themeResId);

        this.callback = callback;
        this.dataDetail = dataDetail;
        this.numberVoucher = numberVoucher;
        this.extraData = data;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_konfirmasi_tukar_deals);
        ButterKnife.bind(this);

        tvProductName.setText(dataDetail.getTitle());
        tvJumlahProduct.setText(numberVoucher + " " + getContext().getString(R.string.label_voucher));
        btnYes.setEnabled(false);
        showProgressButtonYes(true);

        if(extraData != null){
            String nomorTujuan = "";
            String serverId = "";

            if(extraData.containsKey(KEY_NOMOR_TUJUAN))
                nomorTujuan = extraData.getString(KEY_NOMOR_TUJUAN);

            if(extraData.containsKey(KEY_SERVER_ID))
                serverId = extraData.getString(KEY_SERVER_ID);

            tvNomorTujuan.setText(nomorTujuan);
            tvServerId.setText(serverId);

            switch (DetailVoucherKonfirmasiActivity.getTypeVoucher(dataDetail.getTypeVoucher())){
                case VOUCHER_GAME:
                    viewServerId.setVisibility(dataDetail.getTypeVoucher().equalsIgnoreCase(categoryMobileLegend) ? View.VISIBLE : View.GONE);

                    tvInfoNumber.setText(R.string.label_id_pelanggan);
                    //tvInfo.setText(R.string.text_info_konfirmasi_voucher_game);
                    break;

                case VOUCHER_TOKEN_LISTRIK:
                    viewServerId.setVisibility(View.GONE);

                    tvInfoNumber.setText(R.string.label_nomor_token);
                    //tvInfo.setText(R.string.text_info_konfirmasi_voucher_token_listrik);
                    break;

                default:
                    viewServerId.setVisibility(View.GONE);

                    tvInfoNumber.setText(R.string.label_nomor_handphone);
                    //tvInfo.setText(R.string.text_info_konfirmasi_voucher);
                    break;
            }
        }else{
            viewNomorTujuan.setVisibility(View.GONE);
            viewServerId.setVisibility(View.GONE);
        }

        // download image
        if(dataDetail.getUrl_img_company_logo() != null && !dataDetail.getUrl_img_company_logo().isEmpty())
            new DownloadImageManager(getContext())
                    .setPlaceHolder(R.drawable.img_placeholder_ottopoint_square)
                    .start(dataDetail.getUrl_img_company_logo(), imgCompany);

        viewTotalHargaPoint.setTextPoint((long) numberVoucher * dataDetail.getPrice());

        callApiGetBallance();
    }

    @OnClick({R.id.view_back, R.id.btn_no})
    public void closeDialog(){
        dismiss();
    }

    @OnClick(R.id.btn_yes)
    public void submit(){
        dismiss();

        if(currentPoint < ((long) numberVoucher * dataDetail.getPrice())){
            MessageHelper.userMessage(getContext(), getContext().getString(R.string.text_point_tidak_cukup));
            return;
        }

        callback.submitAction(null);
    }

    private void showProgressButtonYes(boolean isShow){
        progressButtonYes.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public void callApiGetBallance(){
        OttoPointDao.getBalance(getContext(), (balance,metaCode) -> {
            tvPointOttopoint.setText(CommonHelper.currencyFormat(currentPoint = balance));

            btnYes.setEnabled(true);
            showProgressButtonYes(false);
        });
    }

    public static void showDialog(Context context, DealsItemModel dataDetail, int numberVoucher, ActionDialog callback){
        new KonfirmasiTukarDealsDialog(context, R.style.CustomDialog_Clear_FromBottom, dataDetail, numberVoucher, callback).show();
    }

    public static void showDialog(Context context, DealsItemModel dataDetail, int numberVoucher, Bundle extraData, ActionDialog callback){
        new KonfirmasiTukarDealsDialog(context, R.style.CustomDialog_Clear_FromBottom, dataDetail, numberVoucher, extraData, callback).show();
    }
}
