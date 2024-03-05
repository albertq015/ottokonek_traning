package com.otto.mart.ui.activity.ottopoint;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherDealsDetailResponseModel;
import com.otto.mart.model.localmodel.ui.ottopoint.DealsItemModel;
import com.otto.mart.model.localmodel.ui.DefaultViewPagerItem;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.support.util.DownloadImageManager;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.support.util.MessageHelper;
import com.otto.mart.ui.Partials.adapter.DefaultViewPagerAdapter;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.component.ActionbarOttopointTransparentWhite;
import com.otto.mart.ui.fragment.ottopoint.DetailInfoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class DetailDealsActivity extends BaseActivityOttopoint {

    private String TAG = DetailDealsActivity.class.getSimpleName();

    public static final String KEY_ID = "key_id";
    public static final String KEY_DATA = "key_data";

    @BindView(R.id.img_banner)
    ImageView imgBanner;
    @BindView(R.id.img_company)
    ImageView imgCompany;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_harga)
    TextView tvHarga;
    @BindView(R.id.tv_voucher_tersisa)
    TextView tvVoucherTersisa;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.btn_beli_voucher)
    Button btnBeliVoucher;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointTransparentWhite viewActionbar;

    private String id;
    private DealsItemModel dataDetail;

    private  DefaultViewPagerAdapter adapter;
    private List<DefaultViewPagerItem> mItemsMenu = new ArrayList<>();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null) return;
            if(intent.getAction() == null) return;

            switch (intent.getAction()){
                case AppKeys.KEY_BROADCAST_REFRESH_PAGE_DETAIL_VOUCHER:
                    callApi();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_deals);
        ButterKnife.bind(this);

        setBroadcast();

        getDataIntent();

        // set header

        setViewByDataDetail();

        //configureViewpager();

        callApi();

        // events

        viewActionbar.actionMenuLeft(view -> onBackPressed());
        viewActionbar.actionMenuRight(view -> MessageHelper.underConstructionMessage(DetailDealsActivity.this));
        btnBeliVoucher.setOnClickListener(this::actionBeliVoucher);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    private void setBroadcast(){
        registerReceiver(broadcastReceiver, new IntentFilter(AppKeys.KEY_BROADCAST_REFRESH_PAGE_DETAIL_VOUCHER));
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

    private void setViewByDataDetail(){
        if(dataDetail == null) return;

        tvCompanyName.setText(dataDetail.getCompany_name());
        tvTitle.setText(dataDetail.getTitle());
        tvHarga.setText(dataDetail.getPrice_text());

        // download image
        if(dataDetail.getUrl_img_company_logo() != null && !dataDetail.getUrl_img_company_logo().isEmpty())
            new DownloadImageManager(DetailDealsActivity.this)
                    .setPlaceHolder(R.drawable.img_placeholder_ottopoint_square)
                    .start(dataDetail.getUrl_img_company_logo(), imgCompany);

        if(dataDetail.getUrl_img_banner_detail() != null && !dataDetail.getUrl_img_banner_detail().isEmpty())
            new DownloadImageManager(DetailDealsActivity.this)
                    .start(dataDetail.getUrl_img_banner_detail(), imgBanner);
    }

    private void actionBeliVoucher(View view){
        //showPopupTukarKupon(view, null, data -> DecisionDialog.showDecisionBeliVoucher(DetailDealsActivity.this, dataDialog -> showResultBeliVoucher()));
        DealsPreviewActivity.showPage(DetailDealsActivity.this, id, dataDetail);
    }

    private void configureViewpager(){
        Bundle dataDesc = new Bundle();
        dataDesc.putInt(DetailInfoFragment.KEY_TYPE, DetailInfoFragment.CODE_TYPE_DESC);
        dataDesc.putString(DetailInfoFragment.KEY_TITLE, getString(R.string.label_deskripsi));
        mItemsMenu.add(new DefaultViewPagerItem(1, getString(R.string.label_deskripsi), DetailInfoFragment.newInstance(dataDesc)));

        Bundle dataSyarat = new Bundle();
        dataSyarat.putInt(DetailInfoFragment.KEY_TYPE, DetailInfoFragment.CODE_TYPE_DESC);
        dataSyarat.putString(DetailInfoFragment.KEY_TITLE, getString(R.string.label_syarat_ketentuan));
        mItemsMenu.add(new DefaultViewPagerItem(2, getString(R.string.label_syarat), DetailInfoFragment.newInstance(dataSyarat)));

        Bundle datCaraPakai = new Bundle();
        datCaraPakai.putInt(DetailInfoFragment.KEY_TYPE, DetailInfoFragment.CODE_TYPE_DESC);
        datCaraPakai.putString(DetailInfoFragment.KEY_TITLE, getString(R.string.label_cara_pakai));
        mItemsMenu.add(new DefaultViewPagerItem(3, getString(R.string.label_cara_pakai), DetailInfoFragment.newInstance(datCaraPakai)));

        configureViewPagerAdapter();

        // there are problems with this on 4.0.3, probably also on 4.1
        //viewpager.setPageTransformer(true, new DepthPageTransformer());
    }

    private void configureViewPagerAdapter(){
        if(adapter == null)
            adapter = new DefaultViewPagerAdapter(getSupportFragmentManager(), mItemsMenu);

        viewpager.setAdapter(adapter);
        tab.setupWithViewPager(viewpager);
    }

    private void setViewpagerItemFromApi(String desciption, String syarat, String caraPakai){
        mItemsMenu.clear();

        Bundle dataDesc = new Bundle();
        dataDesc.putInt(DetailInfoFragment.KEY_TYPE, DetailInfoFragment.CODE_TYPE_DESC);
        dataDesc.putString(DetailInfoFragment.KEY_TITLE, getString(R.string.label_deskripsi));
        dataDesc.putString(DetailInfoFragment.KEY_DESCRIPTION, desciption);
        mItemsMenu.add(new DefaultViewPagerItem(1, getString(R.string.label_deskripsi), DetailInfoFragment.newInstance(dataDesc)));

        Bundle dataSyarat = new Bundle();
        dataSyarat.putInt(DetailInfoFragment.KEY_TYPE, DetailInfoFragment.CODE_TYPE_DESC);
        dataSyarat.putString(DetailInfoFragment.KEY_TITLE, getString(R.string.label_syarat_ketentuan));
        dataSyarat.putString(DetailInfoFragment.KEY_DESCRIPTION, syarat);
        mItemsMenu.add(new DefaultViewPagerItem(2, getString(R.string.label_syarat), DetailInfoFragment.newInstance(dataSyarat)));

        Bundle datCaraPakai = new Bundle();
        datCaraPakai.putInt(DetailInfoFragment.KEY_TYPE, DetailInfoFragment.CODE_TYPE_DESC);
        datCaraPakai.putString(DetailInfoFragment.KEY_TITLE, getString(R.string.label_cara_pakai));
        datCaraPakai.putString(DetailInfoFragment.KEY_DESCRIPTION, caraPakai);
        mItemsMenu.add(new DefaultViewPagerItem(3, getString(R.string.label_cara_pakai), DetailInfoFragment.newInstance(datCaraPakai)));

        refreshTabViewPager();
    }

    private void refreshTabViewPager(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
        else
            configureViewPagerAdapter();
    }

    public static void showPage(Context context, String id, DealsItemModel data){
        if(context == null) return;

        Intent intent = new Intent(context, DetailDealsActivity.class);
        intent.putExtra(DetailDealsActivity.KEY_ID, id);
        intent.putExtra(DetailDealsActivity.KEY_DATA, data);
        context.startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void callApi(){
        if(dataDetail.getId() == null) return;
        if(dataDetail.getId().isEmpty()) return;

        showProgress(DetailDealsActivity.this, true);
        OttoPointDao.voucherDealsDetail(DetailDealsActivity.this, dataDetail.getId(), new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                showProgress(DetailDealsActivity.this, false);

                if(br instanceof OpVoucherDealsDetailResponseModel){
                    OpVoucherDealsDetailResponseModel result = (OpVoucherDealsDetailResponseModel) br;

                    if(result.getData() == null) return;

                    tvCompanyName.setText(result.getData().getBrandName());
                    tvTitle.setText(result.getData().getName());
                    tvVoucherTersisa.setText(result.getData().getUsageLeft() + " " + getString(R.string.label_voucher));
                    tvHarga.setText(CommonHelper.currencyFormat(result.getData().getCostInPoints()) + " poin");
                    btnBeliVoucher.setEnabled(result.getData().getUsageLeft() >= 1);

                    setViewpagerItemFromApi(result.getData().getShortDescription(), result.getData().getConditionsDescription(), result.getData().getUsageInstruction());

                    dataDetail.setUrl_img_company_logo(result.getData().getUrl_logo());
                    dataDetail.setUrl_img_banner(CommonHelper.getUrlBannerVoucher(result.getData().getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST));
                    dataDetail.setUrl_img_banner_detail(CommonHelper.getUrlBannerVoucher(result.getData().getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST));
                    dataDetail.setJumlahVoucherAvailable(result.getData().getUsageLeft());

                    if(result.getData().getCoupons() != null){
                        String productCode = !result.getData().getCoupons().isEmpty() ? result.getData().getCoupons().get(0) : "";
                        dataDetail.setProductCode(productCode);
                    }

                    if(result.getData().getCategoryNames() != null){
                        if(result.getData().getCategoryNames() instanceof List) return;

                        try {
                            HashMap<String,String> categoryNames = (HashMap<String,String> ) result.getData().getCategoryNames();

                            String categoryVoucherType = "";
                            for (String key: categoryNames.keySet()) {
                                categoryVoucherType = categoryNames.get(key);
                                break;
                            }

                            dataDetail.setTypeVoucher(categoryVoucherType);
                        }catch (Exception e){
                            LogHelper.showError(TAG, e.getMessage());
                        }
                    }

                    // download image
                    if(result.getData().getUrl_logo() != null && !result.getData().getUrl_logo().isEmpty())
                        new DownloadImageManager(DetailDealsActivity.this)
                                .setPlaceHolder(R.drawable.img_placeholder_ottopoint_square)
                                .start(result.getData().getUrl_logo(), imgCompany);

                    if(dataDetail.getUrl_img_banner_detail() != null && !dataDetail.getUrl_img_banner_detail().isEmpty())
                        new DownloadImageManager(DetailDealsActivity.this)
                                .start(dataDetail.getUrl_img_banner_detail(), imgBanner);
                }
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                showProgress(DetailDealsActivity.this, false);

                MessageHelper.userMessage(DetailDealsActivity.this, message);
                finish();
            }
        });
    }
}
