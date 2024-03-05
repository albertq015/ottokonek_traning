package com.otto.mart.ui.activity.ottopoint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRedeemVoucherDealsRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpVoucherComulativeRequestModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherComulativeResponseModel;
import com.otto.mart.model.localmodel.ui.ottopoint.DealsItemModel;
import com.otto.mart.model.localmodel.ui.ottopoint.TokenListrikPreviewModel;
import com.otto.mart.model.localmodel.ui.ottopoint.VoucherPointItemModel;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.support.util.DownloadImageManager;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.support.util.MessageHelper;
import com.otto.mart.ui.actionView.ActionDialogThree;
import com.otto.mart.ui.actionView.ActionDialogTwo;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.component.ActionbarOttopointWhite;
import com.otto.mart.ui.component.ViewTotalHargaPoint;
import com.otto.mart.ui.component.dialog.KonfirmasiTukarDealsDialog;
import com.otto.mart.ui.component.dialog.SuccessBeliVoucherDialog;

import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.KEY_BROADCAST_REFRESH_PAGE_DETAIL_VOUCHER;
import static com.otto.mart.ui.activity.ottopoint.DetailVoucherKonfirmasiActivity.VOUCHER_GAME;
import static com.otto.mart.ui.activity.ottopoint.DetailVoucherKonfirmasiActivity.VOUCHER_PULSA;
import static com.otto.mart.ui.activity.ottopoint.DetailVoucherKonfirmasiActivity.VOUCHER_TOKEN_LISTRIK;
import static com.otto.mart.ui.activity.ottopoint.DetailVoucherKonfirmasiActivity.categoryMobileLegend;

public class DealsPreviewActivity extends BaseActivityOttopoint {

    private String TAG = DealsPreviewActivity.class.getSimpleName();

    public static final String KEY_ID = "key_id";
    public static final String KEY_DATA = "key_data";

    @BindView(R.id.img_minus)
    View imgMinus;
    @BindView(R.id.img_plus)
    View imgPlus;
    @BindView(R.id.edt_count)
    EditText edtCount;
    @BindView(R.id.img_company)
    ImageView imgCompany;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;
    @BindView(R.id.view_total_harga_point)
    ViewTotalHargaPoint viewTotalHargaPoint;
    @BindView(R.id.view_count_voucher)
    View viewCountVoucher;
    @BindView(R.id.tv_point_ottopoint)
    TextView tvPointOttopoint;
    @BindView(R.id.btn_lanjut)
    Button btnLanjut;

    @BindView(R.id.tv_number_submit)
    TextView tvNumberSubmit;
    @BindView(R.id.tv_code_phone)
    TextView tvCodePhone;
    @BindView(R.id.img_phonebook)
    ImageView imgPhonebook;
    @BindView(R.id.edt_phone_number)
    EditText edtInputNumber;
    @BindView(R.id.view_id_server)
    View viewIdServer;
    @BindView(R.id.edt_id_server)
    EditText edtIdServer;

    private String id;
    private DealsItemModel dataDetail;

    private double balancePointUser = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_preview);
        ButterKnife.bind(this);

        getDataIntent();

        setViewByDataDetail();

        getBalanceOttoPoint((balance,metaCode) -> {
            balancePointUser = balance;

            if(tvPointOttopoint != null)
                tvPointOttopoint.setText(CommonHelper.currencyFormat(balance));

            if(btnLanjut != null)
                btnLanjut.setEnabled(validationBtnSubmit());
        });

        // events

        imgMinus.setOnClickListener(view -> calculateCount(false));
        imgPlus.setOnClickListener(view -> calculateCount(true));
        viewActionbar.setActionMenuLeft(view -> onBackPressed());
        btnLanjut.setEnabled(false);

        edtInputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnLanjut.setEnabled(validationBtnSubmit());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case AppKeys.KEY_PICK_CONTACT_REQUEST:
                if(resultCode == Activity.RESULT_OK){
                    if(data != null && data.getExtras() != null){
                        String result = CommonHelper.getPhoneNumberFromActivityResult(DealsPreviewActivity.this, data);
                        edtInputNumber.setText(result);
                    }
                }
                break;
        }
    }

    /*
        old: 4-11-2019
        Jika voucher yang di tukar lebih dari 1
        maka akan melakukan proses biasa (muncul dialog konfirmasi tukar voucher).

        Tetapi jika hanya 1 voucher saja, akan di arahkan ke halaman redeem voucher
        dan proses tukar voucher akan dilakukan berbarengan dengan proses redeem voucher :
        1. Tukar voucher
        2. Redeem Voucher

        new : 29-01-2020
        Langsung memanggil proses penggunaan voucher dengan kondisi tertentu
        1. jika voucher yang dibeli hanya 1 voucher
        2. jika voucher yang dibeli lebih dari 1, maka akan dilakukan proses looping
    */
    @OnClick(R.id.btn_lanjut)
    public void nextStep(){
        // old process (production)
        //KonfirmasiTukarDealsDialog.showDialog(DealsPreviewActivity.this, dataDetail, getNumberSelectedVoucher(), data ->
        //        callApiRedeem(getNumberSelectedVoucher())
        //);

        // new 4-11-2019 (development)
        //if(validateNumberSelectedVoucher(getNumberSelectedVoucher())){
        //    KonfirmasiTukarDealsDialog.showDialog(DealsPreviewActivity.this, dataDetail, getNumberSelectedVoucher(), data ->
        //        callApiRedeem(getNumberSelectedVoucher())
        //    );
        //    return;
        //}

        //moveToDetailVoucherKonfirmasiRedeem();

        /*
            new 29-01-2020
        */
        actionSubmit(getNumberSelectedVoucher());
    }

    @OnClick(R.id.img_phonebook)
    public void phoneBookAction(){
        pickContact(AppKeys.KEY_PICK_CONTACT_REQUEST);
    }

    private boolean validateNumberSelectedVoucher(int numberSelectedVoucher){
        // old : 04-11-2019
        return numberSelectedVoucher > 1;

        // new : 14-01-2019
        //return numberSelectedVoucher > 1 && numberSelectedVoucher < 5;
    }

    @SuppressLint("SetTextI18n")
    private void calculateCount(boolean isPlus){
        int value = Integer.parseInt(edtCount.getText().toString());

        int resultValue;
        if(isPlus)
            resultValue = value + 1;
        else
            resultValue = value != 0 ? value - 1 : 1;
        resultValue = resultValue != 0 ? resultValue : 1;

        if(resultValue > dataDetail.getJumlahVoucherAvailable()){
            MessageHelper.userMessage(DealsPreviewActivity.this, getString(R.string.text_voucher_available) + " " + dataDetail.getJumlahVoucherAvailable());
            return;
        }

        edtCount.setText(Integer.toString(resultValue));

        // set result to view
        long resultPoint = (long) resultValue * dataDetail.getPrice();
        viewTotalHargaPoint.setTextPoint(resultPoint);

        // check if result point is higher than balance user
        btnLanjut.setEnabled(validationBtnSubmit(resultPoint));
    }

    private void getDataIntent(){
        Intent intent = getIntent();
        if(intent != null){
            Bundle data = intent.getExtras();
            if(data != null){
                if(data.containsKey(KEY_ID))
                    id = data.getString(KEY_ID);

                if(data.containsKey(KEY_DATA))
                    dataDetail = (DealsItemModel) data.getSerializable(KEY_DATA);
            }
        }

        if(id == null){
            MessageHelper.userMessage(this, getString(R.string.error_request));
            finish();
        }
    }

    private void moveToVoucher(){
        startActivity(new Intent(DealsPreviewActivity.this, VoucherSayaMainActivity.class));
        finish();
    }

    private void setViewByDataDetail(){
        if(dataDetail == null) return;

        tvProductName.setText(dataDetail.getTitle());
        viewTotalHargaPoint.setTextPoint(dataDetail.getPrice());

        // download image
        if(dataDetail.getUrl_img_company_logo() != null && !dataDetail.getUrl_img_company_logo().isEmpty())
            new DownloadImageManager(DealsPreviewActivity.this)
                    .setPlaceHolder(R.drawable.img_placeholder_ottopoint_square)
                    .start(dataDetail.getUrl_img_company_logo(), imgCompany);

        switch (DetailVoucherKonfirmasiActivity.getTypeVoucher(dataDetail.getTypeVoucher())){
            case VOUCHER_GAME:
                tvCodePhone.setVisibility(View.GONE);
                imgPhonebook.setVisibility(View.GONE);

                viewIdServer.setVisibility(dataDetail.getTypeVoucher().equalsIgnoreCase(categoryMobileLegend) ? View.VISIBLE : View.GONE);

                tvNumberSubmit.setText(R.string.label_id_pelanggan);
                break;

            case VOUCHER_TOKEN_LISTRIK:
                tvCodePhone.setVisibility(View.GONE);
                imgPhonebook.setVisibility(View.GONE);
                viewIdServer.setVisibility(View.GONE);

                tvNumberSubmit.setText(R.string.label_nomor_token);
                break;

            default:
                tvCodePhone.setVisibility(View.VISIBLE);
                imgPhonebook.setVisibility(View.VISIBLE);
                viewIdServer.setVisibility(View.GONE);

                tvNumberSubmit.setText(R.string.label_nomor_handphone);
                break;
        }
    }

    public static void showPage(Context context, String id, DealsItemModel data){
        if(context == null) return;

        Intent intent = new Intent(context, DealsPreviewActivity.class);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_DATA, data);
        context.startActivity(intent);

        if(context instanceof Activity) ((Activity) context).finish();
    }

    private int getNumberSelectedVoucher(){
        int result = 0;
        EditText edtCount = viewCountVoucher.findViewById(R.id.edt_count);
        if(edtCount != null){
            if(!edtCount.getText().toString().isEmpty()){
                try {
                    result = Integer.parseInt(edtCount.getText().toString());
                }catch (Exception e){
                    LogHelper.showError(TAG, "getNumberSelectedVoucher: " + e.getMessage());
                }
            }
        }

        return result;
    }

    private void callToRefresh(){
        Intent broadcast = new Intent();
        broadcast.setAction(KEY_BROADCAST_REFRESH_PAGE_DETAIL_VOUCHER);
        sendBroadcast(broadcast);
    }

    private void callApiRedeem(int numberVoucher){
        showProgress(DealsPreviewActivity.this, true);
        OttoPointDao.voucherDealsRedeem(DealsPreviewActivity.this, new OpRedeemVoucherDealsRequestModel(
            dataDetail.getId(),
            numberVoucher
        ), new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                showProgress(DealsPreviewActivity.this, false);

                callToRefresh();

                callToUpdatePointGlobal(DealsPreviewActivity.this);

                // do something in here
                SuccessBeliVoucherDialog.showDialog(DealsPreviewActivity.this, new ActionDialogTwo() {
                    @Override
                    public void submitAction(Bundle data) {
                        moveToVoucher();
                    }

                    @Override
                    public void dismisDialog() {
                        finish();
                    }
                });
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                showProgress(DealsPreviewActivity.this, false);

                MessageHelper.userMessage(DealsPreviewActivity.this, message);
            }
        });
    }

    private void actionSubmit(int numberVoucher){
        String numberValue = edtInputNumber.getText().toString();
        if(numberValue.isEmpty()) {
            MessageHelper.emptyFormInput(DealsPreviewActivity.this);
            return;
        }

        // validasi form input ini akan di gunakan ketika voucher berupa game mobile legend
        String idServer = edtIdServer.getText().toString();
        if(idServer.isEmpty() &&
                DetailVoucherKonfirmasiActivity.getTypeVoucher(dataDetail.getTypeVoucher()).equalsIgnoreCase(VOUCHER_GAME) &&
                dataDetail.getTypeVoucher().equalsIgnoreCase(categoryMobileLegend)){
            MessageHelper.emptyFormInput(DealsPreviewActivity.this);
            return;
        }

        // empty idServer if voucher is not game && not mobile legend
        if (!DetailVoucherKonfirmasiActivity.getTypeVoucher(dataDetail.getTypeVoucher()).equalsIgnoreCase(VOUCHER_GAME) ||
                !dataDetail.getTypeVoucher().equalsIgnoreCase(categoryMobileLegend))
            idServer = "";

        String numberValueCustom;
        if(DetailVoucherKonfirmasiActivity.getTypeVoucher(dataDetail.getTypeVoucher()).equalsIgnoreCase(VOUCHER_PULSA)){
            numberValueCustom = "0" + CommonHelper.cleanPhoneNumber(numberValue);
        }else{
            numberValueCustom = numberValue;
        }

        final String finalIdServer = idServer;
        Bundle senData = new Bundle();
        senData.putString(KonfirmasiTukarDealsDialog.KEY_NOMOR_TUJUAN, numberValueCustom);
        senData.putString(KonfirmasiTukarDealsDialog.KEY_SERVER_ID, finalIdServer);
        KonfirmasiTukarDealsDialog.showDialog(DealsPreviewActivity.this, dataDetail, getNumberSelectedVoucher(), senData, data ->
                callApiBuyAndUseVoucher(new OpVoucherComulativeRequestModel(
                        dataDetail.getTitle(),
                        SessionManager.getPhone(),
                        numberVoucher,
                        dataDetail.getId(),
                        dataDetail.getTypeVoucher().toLowerCase(),
                        numberValueCustom,
                        finalIdServer,
                        dataDetail.getProductCode(),
                        ""
                ))
        );
    }

    private void callApiBuyAndUseVoucher(OpVoucherComulativeRequestModel sendData){
        if(sendData == null) return;

        showProgress(DealsPreviewActivity.this, true);
        OttoPointDao.voucherComulative(DealsPreviewActivity.this, sendData, new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                showProgress(DealsPreviewActivity.this, false);

                callToRefresh();

                callToUpdatePointGlobal(DealsPreviewActivity.this);

                int statusCode = -1;
                String message = "";
                String dataMessage = "";
                int voucherSuccess = 0;
                int voucherFailed = 0;
                if(br instanceof OpVoucherComulativeResponseModel){
                    OpVoucherComulativeResponseModel result = (OpVoucherComulativeResponseModel) br;

                    message = result.getBaseMeta().getMessage();

                    if(result.getData() != null){
                        dataMessage = result.getData().getMsg();
                        statusCode = OttoPointDao.getStatusCodeBuyAndUse(result.getData().getCode());
                        voucherSuccess = result.getData().getSuccess();
                        voucherFailed = result.getData().getFailed();
                    }
                }

                if(statusCode == OttoPointDao.ALL_FAILED || statusCode == -1){
                    String finalDataMessage = dataMessage;
                    MessageHelper.userMessageDialog(DealsPreviewActivity.this, dataMessage, new ActionDialogThree() {
                        @Override
                        public void submitAction(Bundle data) {
                            if(finalDataMessage.contains("Pending") || finalDataMessage.contains("pending"))
                                VoucherSayaMainActivity.showPageRiwayat(DealsPreviewActivity.this);

                            finish();
                        }

                        @Override
                        public void closeDialog() {
                            if(finalDataMessage.contains("Pending") || finalDataMessage.contains("pending"))
                                VoucherSayaMainActivity.showPageRiwayat(DealsPreviewActivity.this);

                            finish();
                        }
                    });
                    return;
                }

                String messageText = "";
                if(voucherFailed > 0){
                    messageText = getString(R.string.text_success_buy_voucher);
                    messageText = messageText.replace("*", Integer.toString(voucherSuccess));
                    messageText += " " + getString(R.string.text_success_buy_voucher_extend);
                    messageText = messageText.replace("*", Integer.toString(voucherFailed));
                }

                SuccessBeliVoucherDialog.showDialog(
                        DealsPreviewActivity.this,
                        DetailVoucherKonfirmasiActivity.getTypeVoucher(sendData.getCategory()).equals(VOUCHER_TOKEN_LISTRIK),
                        messageText,
                        new ActionDialogTwo() {
                            @Override
                            public void submitAction(Bundle data) {
//                                if(DetailVoucherKonfirmasiActivity.getTypeVoucher(sendData.getCategory()).equals(VOUCHER_TOKEN_LISTRIK)){
//
//                                    // dummy
//                                    List<TokenListrikPreviewModel> mItemTokenListrik = new ArrayList<>();
//                                    for (int i = 0; i < 2; i++)
//                                        mItemTokenListrik.add(new TokenListrikPreviewModel("JQJSA7A6AHD986AG3F" + i));
//
//                                    moveToDetailVoucherTokenListrik(DetailVoucherKonfirmasiActivity.FROM_DEALS_PREVIEW, mItemTokenListrik);
//                                }else
                                    VoucherSayaMainActivity.showPageRiwayat(DealsPreviewActivity.this);

                                finish();
                            }

                            @Override
                            public void dismisDialog() {
                                VoucherSayaMainActivity.showPageRiwayat(DealsPreviewActivity.this);
                                finish();
                            }
                        });
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                showProgress(DealsPreviewActivity.this, false);

                MessageHelper.userMessageDialog(DealsPreviewActivity.this, message, new ActionDialogThree() {
                    @Override
                    public void submitAction(Bundle data) {
                        finish();
                    }

                    @Override
                    public void closeDialog() {
                        finish();
                    }
                });
            }
        });
    }

    private boolean validationBtnSubmit(){
        return balancePointUser >= dataDetail.getPrice() &&
                DetailVoucherKonfirmasiActivity.checkPanjangKarakter(edtInputNumber.getText().toString(), dataDetail.getTypeVoucher());
    }

    private boolean validationBtnSubmit(long price){
        return balancePointUser >= price &&
                DetailVoucherKonfirmasiActivity.checkPanjangKarakter(edtInputNumber.getText().toString(), dataDetail.getTypeVoucher());
    }

    private void moveToDetailVoucherKonfirmasiRedeem(){
        VoucherPointItemModel dataDetailVoucherKonfirmasi = new VoucherPointItemModel();

        String expiredate = "-";

        dataDetailVoucherKonfirmasi.setId(dataDetail.getId());
        dataDetailVoucherKonfirmasi.setUrlBanner(dataDetail.getUrl_img_banner());
        dataDetailVoucherKonfirmasi.setUrlBannerDetail(dataDetail.getUrl_img_banner_detail());
        dataDetailVoucherKonfirmasi.setUrlCompanyPic(dataDetail.getUrl_img_company_logo());
        dataDetailVoucherKonfirmasi.setCompanyName(dataDetail.getCompany_name());
        dataDetailVoucherKonfirmasi.setTitle(dataDetail.getTitle());
        dataDetailVoucherKonfirmasi.setExpireDate(expiredate);
        dataDetailVoucherKonfirmasi.setProductCode(dataDetail.getProductCode());
        dataDetailVoucherKonfirmasi.setTypeVoucher(dataDetail.getTypeVoucher());
        dataDetailVoucherKonfirmasi.setNumberOfVoucher(getNumberSelectedVoucher());

        DetailVoucherKonfirmasiActivity.showPage(DealsPreviewActivity.this, dataDetailVoucherKonfirmasi, DetailVoucherKonfirmasiActivity.FROM_DEALS);
    }

    private void moveToDetailVoucherTokenListrik(int typePage, List<TokenListrikPreviewModel> mItemsTokenListrik){
        VoucherPointItemModel dataDetailVoucherKonfirmasi = new VoucherPointItemModel();

        String expiredate = "-";

        dataDetailVoucherKonfirmasi.setId(dataDetail.getId());
        dataDetailVoucherKonfirmasi.setUrlBanner(dataDetail.getUrl_img_banner());
        dataDetailVoucherKonfirmasi.setUrlBannerDetail(dataDetail.getUrl_img_banner_detail());
        dataDetailVoucherKonfirmasi.setUrlCompanyPic(dataDetail.getUrl_img_company_logo());
        dataDetailVoucherKonfirmasi.setCompanyName(dataDetail.getCompany_name());
        dataDetailVoucherKonfirmasi.setTitle(dataDetail.getTitle());
        dataDetailVoucherKonfirmasi.setExpireDate(expiredate);
        dataDetailVoucherKonfirmasi.setProductCode(dataDetail.getProductCode());
        dataDetailVoucherKonfirmasi.setTypeVoucher(dataDetail.getTypeVoucher());
        dataDetailVoucherKonfirmasi.setNumberOfVoucher(getNumberSelectedVoucher());

        DetailVoucherKonfirmasiActivity.showPage(DealsPreviewActivity.this, dataDetailVoucherKonfirmasi, mItemsTokenListrik, typePage);
    }
}
