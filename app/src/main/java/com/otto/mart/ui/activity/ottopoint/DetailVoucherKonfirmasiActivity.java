package com.otto.mart.ui.activity.ottopoint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRedeemVoucherDealsRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRedeemVoucherSayaRequestModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpRedeemVoucherSayaResponseModel;
import com.otto.mart.model.localmodel.ui.ottopoint.TokenListrikDataModel;
import com.otto.mart.model.localmodel.ui.ottopoint.TokenListrikPreviewModel;
import com.otto.mart.model.localmodel.ui.ottopoint.VoucherPointItemModel;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.support.util.DownloadImageManager;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.support.util.MessageHelper;
import com.otto.mart.ui.Partials.adapter.ottopoint.TokenListrikPreviewAdapter;
import com.otto.mart.ui.actionView.ActionDialogThree;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.actionView.HandleResponseApiTwo;
import com.otto.mart.ui.component.ActionbarOttopointWhite;
import com.otto.mart.ui.component.dialog.SuccessUseVoucherDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class DetailVoucherKonfirmasiActivity extends BaseActivityOttopoint {

    private String TAG = DetailVoucherKonfirmasiActivity.class.getSimpleName();

    public static final String KEY_FROM = "key_from";
    public static final String KEY_DATA_TOKEN_LISTRIK = "key_data_token_listrik";

    public static final int FROM_DEFAULT = 0;
    public static final int FROM_DEALS = 1;
    public static final int FROM_DEALS_PREVIEW = 2;

    private int from = FROM_DEFAULT;

    public static final String VOUCHER_PULSA = "pulsa";
    public static final String VOUCHER_GAME = "voucher game";
    public static final String VOUCHER_TOKEN_LISTRIK = "token";

    public static final String categoryMobileLegend = "mobile_legend";
    public static final List<String> listVoucherGame = new ArrayList<>(Arrays.asList(
            categoryMobileLegend,
            "free_fire",
            "voucher game"
    ));

    public static final List<String> listVoucherPulsa = new ArrayList<>(Arrays.asList(
            "pulsa",
            "paket_data"
    ));

    public static final List<String> listVoucherListrik = new ArrayList<>(Arrays.asList(
            "token",
            "token listrik"
    ));

    private final int TRANSACTION_ERROR_CODE = 422; // dianggap gagal dan voucher hangus (akan berpindah riwayat)
    private final int TRANSACTION_REVERSAL = 500; // dianggap gagal dan bakal reversal voucher, jadi masih di my vocuher

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_jumlah)
    TextView tvJumlah;
    @BindView(R.id.edt_phone_number)
    EditText edtInputNumber;
    @BindView(R.id.btn_masuk)
    Button btnMasuk;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;
    @BindView(R.id.img_company)
    ImageView imgCompany;
    @BindView(R.id.tv_code_phone)
    TextView tvCodePhone;
    @BindView(R.id.tv_number_submit)
    TextView tvNumberSubmit;
    @BindView(R.id.img_phonebook)
    ImageView imgPhonebook;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.view_id_server)
    View viewIdServer;
    @BindView(R.id.edt_id_server)
    EditText edtIdServer;
    @BindView(R.id.list_token_listrik)
    RecyclerView listTokenListrik;
    @BindView(R.id.view_main_input)
    View viewMainInput;

    private VoucherPointItemModel dataDetail;
    private boolean isSuccesTukarVoucher = false;

    private TokenListrikPreviewAdapter tokenListrikAdapter;
    private List<TokenListrikPreviewModel> mItemTokenListrik = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_voucher_konfirmasi);
        ButterKnife.bind(this);

        getDataIntent();

        setViewByDataDetail();

        // events

        viewActionbar.setActionMenuLeft(view -> onBackPressed());

        edtInputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnMasuk.setEnabled(checkPanjangKarakter(edtInputNumber.getText().toString(), dataDetail.getTypeVoucher()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*
        Jadi ketika halaman ini dibuka dari halaman selain my voucher,
        akan melakukan proses tukar voucher (beli voucher terlebih dahulu)
        dikarenakan dari halaman preview deals voucher tidak melakukan tukar voucher (call endpoint tukar voucher)
    */
    @OnClick(R.id.btn_masuk)
    public void actionSubmit(){
        CommonHelper.hideKeyboard(DetailVoucherKonfirmasiActivity.this);

        switch (from){
            case FROM_DEALS:
                callApiTukarVoucher(dataDetail.getNumberOfVoucher());
                break;

            case FROM_DEALS_PREVIEW: // do nothing
                break;

            default:
                callApiRedeem();
                break;
        }
    }

    @OnClick(R.id.img_phonebook)
    public void phoneBookAction(){
        pickContact(AppKeys.KEY_PICK_CONTACT_REQUEST);
    }

    public static boolean checkPanjangKarakter(String value, String typeVoucher){
        if(value.isEmpty()) return false;

        boolean result;
        switch (getTypeVoucher(typeVoucher)){
            case VOUCHER_PULSA:
                result = value.length() >= 7;
                break;

            default:
                result = true;
                break;
        }

        return result;
    }

    private void getDataIntent(){
        Intent intent = getIntent();
        if(intent != null){
            Bundle data = intent.getExtras();
            if(data != null){
                dataDetail = (VoucherPointItemModel) data.getSerializable(DetailVoucherActivity.KEY_DATA);

                if(data.containsKey(KEY_FROM))
                    from = data.getInt(KEY_FROM);

                if(data.containsKey(KEY_DATA_TOKEN_LISTRIK)){
                    String dataTokenListrik = data.getString(KEY_DATA_TOKEN_LISTRIK);
                    if(dataTokenListrik != null && !dataTokenListrik.isEmpty()){
                        TokenListrikDataModel dataToken = new Gson().fromJson(dataTokenListrik, TokenListrikDataModel.class);
                        mItemTokenListrik = dataToken.getItems();
                    }
                }
            }
        }

        if(dataDetail == null) finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case AppKeys.KEY_PICK_CONTACT_REQUEST:
                if(resultCode == Activity.RESULT_OK){
                    if(data != null && data.getExtras() != null){
                        String result = CommonHelper.getPhoneNumberFromActivityResult(DetailVoucherKonfirmasiActivity.this, data);
                        edtInputNumber.setText(result);
                    }
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setViewByDataDetail(){
        if(dataDetail == null) return;

        tvTitle.setText(dataDetail.getTitle());
        tvJumlah.setText((dataDetail.getJumlahChild() == 0 ? 1 : dataDetail.getJumlahChild())+ " " + getString(R.string.label_voucher));

        // download image
        if(!dataDetail.getUrlCompanyPic().isEmpty())
            new DownloadImageManager(DetailVoucherKonfirmasiActivity.this)
                    .setPlaceHolder(R.drawable.img_placeholder_ottopoint_square)
                    .start(dataDetail.getUrlCompanyPic(), imgCompany);

        // new : dev 6-02-2020
        if(from == FROM_DEALS_PREVIEW){
            viewMainInput.setVisibility(View.GONE);
            btnMasuk.setVisibility(View.GONE);

            if(getTypeVoucher(dataDetail.getTypeVoucher()).equals(VOUCHER_TOKEN_LISTRIK)){
                listTokenListrik.setVisibility(View.VISIBLE);

                configureListTokenListrik();
            }
        }else{
            viewMainInput.setVisibility(View.VISIBLE);
            btnMasuk.setVisibility(View.VISIBLE);

            switch (getTypeVoucher(dataDetail.getTypeVoucher())){
                case VOUCHER_GAME:
                    tvCodePhone.setVisibility(View.GONE);
                    imgPhonebook.setVisibility(View.GONE);

                    viewIdServer.setVisibility(dataDetail.getTypeVoucher().equalsIgnoreCase(categoryMobileLegend) ? View.VISIBLE : View.GONE);

                    tvNumberSubmit.setText(R.string.label_id_pelanggan);
                    tvInfo.setText(R.string.text_info_konfirmasi_voucher_game);
                    break;

                case VOUCHER_TOKEN_LISTRIK:
                    tvCodePhone.setVisibility(View.GONE);
                    imgPhonebook.setVisibility(View.GONE);
                    viewIdServer.setVisibility(View.GONE);

                    tvNumberSubmit.setText(R.string.label_nomor_token);
                    tvInfo.setText(R.string.text_info_konfirmasi_voucher_token_listrik);
                    break;

                default:
                    tvCodePhone.setVisibility(View.VISIBLE);
                    imgPhonebook.setVisibility(View.VISIBLE);
                    viewIdServer.setVisibility(View.GONE);

                    tvNumberSubmit.setText(R.string.label_nomor_handphone);
                    tvInfo.setText(R.string.text_info_konfirmasi_voucher);
                    break;
            }
        }
    }

    public static String getTypeVoucher(String categoryName){
        if(listVoucherGame.contains(categoryName.toLowerCase())) {
            return VOUCHER_GAME;
        }else if(listVoucherListrik.contains(categoryName.toLowerCase())){
            return VOUCHER_TOKEN_LISTRIK;
        }else if(listVoucherPulsa.contains(categoryName.toLowerCase())){
            return VOUCHER_PULSA;
        }else
            return VOUCHER_PULSA;
    }

    private void configureListTokenListrik(){
        listTokenListrik.setLayoutManager(new LinearLayoutManager(DetailVoucherKonfirmasiActivity.this));
        listTokenListrik.setItemAnimator(new DefaultItemAnimator());

        if(tokenListrikAdapter == null) {
            tokenListrikAdapter = new TokenListrikPreviewAdapter(DetailVoucherKonfirmasiActivity.this, mItemTokenListrik);
            listTokenListrik.setAdapter(tokenListrikAdapter);
        }else
            refreshListTokenListrik();
    }

    private void refreshListTokenListrik(){
        if(tokenListrikAdapter != null)
            tokenListrikAdapter.notifyDataSetChanged();
    }

    private void dummyItemListrikToken(){
        for (int i = 0; i < 2; i++)
            mItemTokenListrik.add(new TokenListrikPreviewModel("JQJSA7A6AHD986AG3F" + i));

        new Handler().postDelayed(this::refreshListTokenListrik, 1000);
    }

    private void callApiRedeem(){
        if(dataDetail.getId() == null || (dataDetail.getId() != null && dataDetail.getId().isEmpty())) {
            MessageHelper.notFoundData(DetailVoucherKonfirmasiActivity.this);
            return;
        }

        String numberValue = edtInputNumber.getText().toString();
        if(numberValue.isEmpty()) {
            MessageHelper.emptyFormInput(DetailVoucherKonfirmasiActivity.this);
            return;
        }

        // validasi form input ini akan di gunakan ketika voucher berupa game mobile legend
        String idServer = edtIdServer.getText().toString();
        if(idServer.isEmpty() &&
                getTypeVoucher(dataDetail.getTypeVoucher()).equalsIgnoreCase(VOUCHER_GAME) &&
                dataDetail.getTypeVoucher().equalsIgnoreCase(categoryMobileLegend)){
            MessageHelper.emptyFormInput(DetailVoucherKonfirmasiActivity.this);
            return;
        }

        // empty idServer if voucher is not game && not mobile legend
        if (!getTypeVoucher(dataDetail.getTypeVoucher()).equalsIgnoreCase(VOUCHER_GAME) ||
                !dataDetail.getTypeVoucher().equalsIgnoreCase(categoryMobileLegend))
            idServer = "";

        String numberValueCustom;
        if(getTypeVoucher(dataDetail.getTypeVoucher()).equalsIgnoreCase(VOUCHER_PULSA)){
            numberValueCustom = "0" + CommonHelper.cleanPhoneNumber(numberValue);
        }else{
            numberValueCustom = numberValue;
        }

        showProgress(DetailVoucherKonfirmasiActivity.this, true);
        OttoPointDao.voucherSayaRedeem(DetailVoucherKonfirmasiActivity.this, new OpRedeemVoucherSayaRequestModel(
                dataDetail.getTypeVoucher().toLowerCase(),
                numberValueCustom,
                idServer,
                dataDetail.getId(),
                dataDetail.getProductCode()
        ), new HandleResponseApiTwo() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                showProgress(DetailVoucherKonfirmasiActivity.this, false);

                callToUpdate(DetailVoucherKonfirmasiActivity.this, AppKeys.KEY_BROADCAST_REFRESH_PAGE_VOUCHER_SAYA_BOTH, dataDetail.getId());

                String message = "";
                if(br instanceof OpRedeemVoucherSayaResponseModel){
                    OpRedeemVoucherSayaResponseModel result = (OpRedeemVoucherSayaResponseModel) br;

                    message = result.getBaseMeta().getMessage();
                }

                if(message.contains("pending") || message.contains("Pending")){
                    // old (production)
                    //MessageHelper.userMessageDialog(DetailVoucherKonfirmasiActivity.this, message, data -> finish());

                    // new 8-01-2020 (development)
                    MessageHelper.userMessageDialog(DetailVoucherKonfirmasiActivity.this, message, new ActionDialogThree() {
                        @Override
                        public void submitAction(Bundle data) {
                            //VoucherSayaMainActivity.showPageRiwayat(DetailVoucherKonfirmasiActivity.this);
                            finish();
                        }

                        @Override
                        public void closeDialog() {
                            finish();
                        }
                    });

                }else{
                    SuccessUseVoucherDialog.showDialog(DetailVoucherKonfirmasiActivity.this, data -> {
                        //VoucherSayaMainActivity.showPageRiwayat(DetailVoucherKonfirmasiActivity.this);
                        finish();
                    });
                }
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp, int metaCode) {
                LogHelper.showError(TAG, message);

                showProgress(DetailVoucherKonfirmasiActivity.this, false);

                if(message.toLowerCase().contains("timeout") || metaCode == TRANSACTION_ERROR_CODE)
                    callToUpdate(DetailVoucherKonfirmasiActivity.this, AppKeys.KEY_BROADCAST_REFRESH_PAGE_VOUCHER_SAYA_BOTH);

                MessageHelper.userMessageDialog(DetailVoucherKonfirmasiActivity.this, message, new ActionDialogThree() {
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

    private void callApiTukarVoucher(int numberVoucher){
        if(isSuccesTukarVoucher){
            callApiRedeem();
            return;
        }

        showProgress(DetailVoucherKonfirmasiActivity.this, true);
        OttoPointDao.voucherDealsRedeem(DetailVoucherKonfirmasiActivity.this, new OpRedeemVoucherDealsRequestModel(
                dataDetail.getId(),
                numberVoucher
        ), new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                isSuccesTukarVoucher = true;
                callApiRedeem();
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                showProgress(DetailVoucherKonfirmasiActivity.this, false);

                MessageHelper.userMessage(DetailVoucherKonfirmasiActivity.this, message);
            }
        });
    }

    public static void showPage(Context context, VoucherPointItemModel dataDetail){
        if(context == null) return;

        Intent intent = new Intent(context, DetailVoucherKonfirmasiActivity.class);
        intent.putExtra(DetailVoucherActivity.KEY_DATA, dataDetail);
        context.startActivity(intent);

        if(context instanceof Activity) ((Activity) context).finish();
    }

    public static void showPage(Context context, VoucherPointItemModel dataDetail, int from){
        if(context == null) return;

        Intent intent = new Intent(context, DetailVoucherKonfirmasiActivity.class);
        intent.putExtra(DetailVoucherActivity.KEY_DATA, dataDetail);
        intent.putExtra(DetailVoucherKonfirmasiActivity.KEY_FROM, from);
        context.startActivity(intent);

        if(context instanceof Activity) ((Activity) context).finish();
    }

    public static void showPage(Context context, VoucherPointItemModel dataDetail, List<TokenListrikPreviewModel> mItems, int from){
        if(context == null) return;

        Intent intent = new Intent(context, DetailVoucherKonfirmasiActivity.class);
        intent.putExtra(DetailVoucherActivity.KEY_DATA, dataDetail);
        intent.putExtra(DetailVoucherKonfirmasiActivity.KEY_FROM, from);
        intent.putExtra(DetailVoucherKonfirmasiActivity.KEY_DATA_TOKEN_LISTRIK, new Gson().toJson(new TokenListrikDataModel(mItems)));
        context.startActivity(intent);

        if(context instanceof Activity) ((Activity) context).finish();
    }
}
