package com.otto.mart.ui.activity.ottopoint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherDealsDetailResponseModel;
import com.otto.mart.model.localmodel.ui.DefaultViewPagerItem;
import com.otto.mart.model.localmodel.ui.ottopoint.VoucherPointItemModel;
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
import butterknife.OnClick;
import retrofit2.Response;

public class DetailVoucherActivity extends BaseActivityOttopoint {

    private String TAG = DetailVoucherActivity.class.getSimpleName();

    public static final String KEY_ID = "key_id";
    public static final String KEY_TYPE = "key_type";
    public static final String KEY_DATA = "key_data";

    public static final int CODE_ID = 0;
    public static final int CODE_TYPE_AKTIF = 1;
    public static final int CODE_TYPE_RIWAYAT = 2;

    @BindView(R.id.view_back)
    View viewBack;
    @BindView(R.id.tv_title_page)
    TextView tvTitlePage;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_gunakan_voucher)
    View btnGunakanVoucher;
    @BindView(R.id.tv_expired_date)
    TextView tvExpiredDate;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointTransparentWhite viewActionbar;
    @BindView(R.id.img_company)
    ImageView imgCompany;
    @BindView(R.id.img_banner)
    ImageView imgBanner;
    @BindView(R.id.tv_date)
    TextView tvDate;

    private int type = -1;
    private VoucherPointItemModel dataDetail = null;
    private List<DefaultViewPagerItem> mItemsMenu = new ArrayList<>();
    private DefaultViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_voucher);
        ButterKnife.bind(this);

        getDataIntent();

        // set view

        changeViewByType(type);

        setViewFromDataDetail();

        //configureViewpager();

        callApi();

        // events

        viewBack.setOnClickListener(view -> onBackPressed());
    }

    @OnClick(R.id.btn_gunakan_voucher)
    public void moveToKonfirmasi(){
        DetailVoucherKonfirmasiActivity.showPage(DetailVoucherActivity.this, dataDetail);
    }

    private void getDataIntent(){
        Intent intent = getIntent();
        if(intent != null){
            Bundle data = intent.getExtras();
            if(data != null){
                if(data.containsKey(KEY_TYPE))
                    type = data.getInt(KEY_TYPE);

                if(data.containsKey(KEY_DATA))
                    dataDetail = (VoucherPointItemModel) data.getSerializable(KEY_DATA);
            }
        }

        if(dataDetail == null){
            MessageHelper.notFoundData(this);
            finish();
        }
    }

    private void changeViewByType(int type){
        switch (type){
            case CODE_TYPE_AKTIF:
                tvDate.setText(R.string.label_berlaku_hingga);
                break;

            case CODE_TYPE_RIWAYAT:
                tvDate.setText(R.string.label_tanggal_berhasil_digunakan);
                btnGunakanVoucher.setVisibility(View.GONE);
                break;

            default:
                btnGunakanVoucher.setVisibility(View.GONE);
                break;
        }
    }

    private void setViewFromDataDetail(){
        if(dataDetail == null) return;

        tvCompanyName.setText(dataDetail.getCompanyName());
        tvTitle.setText(dataDetail.getTitle());

        switch (type){
            case CODE_TYPE_AKTIF:
                tvExpiredDate.setText(dataDetail.getExpireDate());
                break;

            case CODE_TYPE_RIWAYAT:
                tvExpiredDate.setText(dataDetail.getUsedDate());
                break;
        }

        // download image
        if(dataDetail.getUrlCompanyPic() != null && !dataDetail.getUrlCompanyPic().isEmpty())
            new DownloadImageManager(DetailVoucherActivity.this)
                    .setPlaceHolder(R.drawable.img_placeholder_ottopoint_square)
                    .start(dataDetail.getUrlCompanyPic(), imgCompany);

        if(dataDetail.getUrlBanner() != null && !dataDetail.getUrlBanner().isEmpty())
            new DownloadImageManager(DetailVoucherActivity.this)
                    .start(dataDetail.getUrlBanner(), imgBanner);
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
        mItemsMenu.add(new DefaultViewPagerItem(1, getString(R.string.label_deskripsi), DetailInfoFragment.newInstance(dataDesc), dataDesc));

        Bundle dataSyarat = new Bundle();
        dataSyarat.putInt(DetailInfoFragment.KEY_TYPE, DetailInfoFragment.CODE_TYPE_DESC);
        dataSyarat.putString(DetailInfoFragment.KEY_TITLE, getString(R.string.label_syarat_ketentuan));
        dataSyarat.putString(DetailInfoFragment.KEY_DESCRIPTION, syarat);
        mItemsMenu.add(new DefaultViewPagerItem(2, getString(R.string.label_syarat), DetailInfoFragment.newInstance(dataSyarat), dataSyarat));

        Bundle datCaraPakai = new Bundle();
        datCaraPakai.putInt(DetailInfoFragment.KEY_TYPE, DetailInfoFragment.CODE_TYPE_DESC);
        datCaraPakai.putString(DetailInfoFragment.KEY_TITLE, getString(R.string.label_cara_pakai));
        datCaraPakai.putString(DetailInfoFragment.KEY_DESCRIPTION, caraPakai);
        mItemsMenu.add(new DefaultViewPagerItem(3, getString(R.string.label_cara_pakai), DetailInfoFragment.newInstance(datCaraPakai), datCaraPakai));

        refreshTabViewPager();
    }

    private void refreshTabViewPager(){
        configureViewPagerAdapter();
    }

    private void callApi(){
        if(dataDetail.getId() == null) return;
        if(dataDetail.getId().isEmpty()) return;

        showProgress(DetailVoucherActivity.this, true);
        OttoPointDao.voucherSayaDetail(DetailVoucherActivity.this, dataDetail.getId(), new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                showProgress(DetailVoucherActivity.this, false);

                if(br instanceof OpVoucherDealsDetailResponseModel){
                    OpVoucherDealsDetailResponseModel result = (OpVoucherDealsDetailResponseModel) br;

                    if(result.getData() == null) return;

                    tvCompanyName.setText(result.getData().getBrandName());
                    tvTitle.setText(result.getData().getName());
                    //tvExpiredDate.setText(DateUtil.getDateAsStringFormat(result.getData().getCampaignVisibility().getVisibleTo(), GLOBAL.FORMAT_DATE_TIME_DEFAULT_SERVER_TWO, GLOBAL.FORMAT_DATE_TIME_DATE_MONTH_TEXT_MIN));

                    setViewpagerItemFromApi(result.getData().getShortDescription(), result.getData().getConditionsDescription(), result.getData().getUsageInstruction());

                    dataDetail.setUrlCompanyPic(result.getData().getUrl_logo() != null ? result.getData().getUrl_logo() : "");
                    dataDetail.setUrlBanner(CommonHelper.getUrlBannerVoucher(result.getData().getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST));
                    dataDetail.setUrlBannerDetail(CommonHelper.getUrlBannerVoucher(result.getData().getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST));

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
                        new DownloadImageManager(DetailVoucherActivity.this)
                                .setPlaceHolder(R.drawable.img_placeholder_ottopoint_square)
                                .start(result.getData().getUrl_logo(), imgCompany);

                    String image = CommonHelper.getUrlBannerVoucher(result.getData().getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST);
                    if(image != null && !image.isEmpty())
                        new DownloadImageManager(DetailVoucherActivity.this)
                                .start(image, imgBanner);
                }
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                showProgress(DetailVoucherActivity.this, false);

                MessageHelper.userMessage(DetailVoucherActivity.this, message);
                finish();
            }
        });
    }

    /**
     * Public
     */

    public static void openPageActive(Context context, Bundle data){
        if(context == null) return;

        if(data == null) data = new Bundle();

        data.putInt(DetailVoucherActivity.KEY_TYPE, DetailVoucherActivity.CODE_TYPE_AKTIF);

        Intent intent = new Intent(context, DetailVoucherActivity.class);
        intent.putExtras(data);
        context.startActivity(intent);
    }

    public static void openPageRiwayat(Context context, Bundle data){
        if(context == null) return;

        if(data == null) data = new Bundle();

        data.putInt(DetailVoucherActivity.KEY_TYPE, DetailVoucherActivity.CODE_TYPE_RIWAYAT);

        Intent intent = new Intent(context, DetailVoucherActivity.class);
        intent.putExtras(data);
        context.startActivity(intent);
    }
}
