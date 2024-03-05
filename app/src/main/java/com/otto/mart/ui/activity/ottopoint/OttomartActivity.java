package com.otto.mart.ui.activity.ottopoint;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.widget.NestedScrollView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherDealsDetailResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherDealsResponseModel;
import com.otto.mart.model.localmodel.ui.ottopoint.DealsItem;
import com.otto.mart.model.localmodel.ui.ottopoint.DealsItemModel;
import com.otto.mart.model.localmodel.ui.ottopoint.ViewPagerItem;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.support.util.MessageHelper;
import com.otto.mart.ui.Partials.adapter.ottopoint.DealsMenuAdapter;
import com.otto.mart.ui.Partials.adapter.ottopoint.PromoVPAdapter;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.component.ActionbarOttopointTransparent;
import com.otto.mart.ui.component.ViewTitleWithButtonAll;
import com.otto.mart.ui.component.dialog.EarnPointDialog;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class OttomartActivity extends BaseActivityOttopoint {

    private String TAG = OttomartActivity.class.getSimpleName();

    private static final String KEY_TYPE_RESPONSE = "KEY_TYPE_RESPONSE";

    public static final int TYPE_RESPONSE_SUCCESS_REGISTER_OTTOPOINT = 1;

    @BindView(R.id.view_poin_card)
    CardView viewPoinCard;
    @BindView(R.id.tv_poin_ottopoint)
    TextView tvPoinOttopoint;
    @BindView(R.id.view_voucher_saya)
    View viewVoucherSaya;
    @BindView(R.id.rv_deals)
    RecyclerView rvDeals;
    @BindView(R.id.vp_promo_terbaru)
    EnchantedViewPager vpPromoTerbaru;
    @BindView(R.id.vp_promo_produk_khusus)
    EnchantedViewPager vpPromoProdukKhusus;
    @BindView(R.id.indicator_promo_terbaru)
    DotsIndicator indicatorPromoTerbaru;
    @BindView(R.id.indicator_promo_produk_khusus)
    DotsIndicator indicatorPromoProdukKhusus;
    @BindView(R.id.view_promo)
    ViewTitleWithButtonAll viewPromo;
    @BindView(R.id.view_promo_produk_khusus)
    ViewTitleWithButtonAll viewPromoProdukKhusus;
    @BindView(R.id.view_deals)
    ViewTitleWithButtonAll viewDeals;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointTransparent viewActionbar;
    @BindView(R.id.pb_promo_terbaru)
    ProgressBar pbPromoTerbaru;
    @BindView(R.id.pb_promo_khusus)
    ProgressBar pbPromoKhusus;
    @BindView(R.id.parent_scroll)
    NestedScrollView parentScroll;

    private int typeResponse = -1;
    private long point = 0;

    private int pagePromo = 1;
    private int maxPagePromo = 1;

    private PromoVPAdapter adapterPromoTerbaru;
    private List<ViewPagerItem> mItemsPromoTerbaru = new ArrayList<>();

    private PromoVPAdapter adapterPromoLainnya;
    private List<ViewPagerItem> mItemsProdukKhusus = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ottomart);
        ButterKnife.bind(this);

        getDataIntent();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppKeys.KEY_BROADCAST_REFRESH_POINT);
        intentFilter.addAction(AppKeys.KEY_BROADCAST_REFRESH_POINT_TWO);
        registerReceiver(broadcastReceiver, intentFilter);

        adapterPromoTerbaru = configureViewPager(vpPromoTerbaru, indicatorPromoTerbaru, mItemsPromoTerbaru);
        adapterPromoLainnya = configureViewPager(vpPromoProdukKhusus, indicatorPromoProdukKhusus, mItemsProdukKhusus);

        moveToTop();

        configureDealsItems();

        callApiPromo();

        // events

        viewPoinCard.setOnClickListener(view -> moveToRiwayatTransaksi());
        viewVoucherSaya.setOnClickListener(view -> moveToVoucher());
        viewPromo.setActionViewAll(view -> moveToPromoTerbaru());
        viewPromoProdukKhusus.setActionViewAll(view -> moveToPromoProdukKhusus());
        viewDeals.setActionViewAll(view -> moveToDeals());
        viewActionbar.setActionMenuLeft(view -> onBackPressed());
        viewActionbar.setActionTitle(view -> onBackPressed());

        getBalanceOttoPoint((balance,metaCode) -> {
            if(tvPoinOttopoint != null)
                tvPoinOttopoint.setText(CommonHelper.currencyFormat(balance));
        });
    }

    @OnClick({R.id.view_back, R.id.tv_title_page})
    public void closePage(){
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }

    private void getDataIntent(){
        Intent intent = getIntent();
        if(intent != null){
            Bundle data = intent.getExtras();
            if(data != null){
                if(data.containsKey(KEY_TYPE_RESPONSE))
                    typeResponse = data.getInt(KEY_TYPE_RESPONSE);

                if(data.containsKey(AppKeys.KEY_EXTRA_POINT))
                    point = data.getLong(AppKeys.KEY_EXTRA_POINT);
            }
        }

        switch (typeResponse){
            case TYPE_RESPONSE_SUCCESS_REGISTER_OTTOPOINT:
                callToUpdatePointGlobalTwo(OttomartActivity.this, point);

                new Handler().postDelayed(() -> EarnPointDialog.showFirstRegistration(OttomartActivity.this, point, data -> { }), 500);
                break;

            default:
                break;
        }
    }

    private PromoVPAdapter configureViewPager(EnchantedViewPager viewPager, DotsIndicator dotsIndicator, List<ViewPagerItem> mItems){
        PromoVPAdapter adapter = new PromoVPAdapter(OttomartActivity.this, mItems, (position, data) -> {
            if(data.containsKey("id"))
                DetailDealsActivity.showPage(OttomartActivity.this, data.getString("id"), (DealsItemModel) mItems.get(position).getData());
        });
        //adapter.enableCarrousel();

        viewPager.useAlpha();
        viewPager.useScale();

        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(viewPager);

        if(mItems.size() > 2)
            viewPager.setCurrentItem(1, true);

        return adapter;
    }

    private void setPromoFocustItem(EnchantedViewPager viewPager, List<ViewPagerItem> mItems){
        if(mItems.size() > 2)
            viewPager.setCurrentItem(1, true);
    }

    private void configureDealsItems(){
        List<DealsItem> mItems = new ArrayList<>();
        //mItems.add(new DealsItem(0, getString(R.string.deal_near_me), getResources().getDrawable(R.drawable.vector_near_me)));
        mItems.add(new DealsItem(1, getString(R.string.deal_trending), getResources().getDrawable(R.drawable.trending_icon)));
        mItems.add(new DealsItem(15, getString(R.string.deal_pulsa), getResources().getDrawable(R.drawable.vector_pulsa)));
        mItems.add(new DealsItem(16, getString(R.string.deal_listrik), getResources().getDrawable(R.drawable.vector_listrik)));
        mItems.add(new DealsItem(17, getString(R.string.deal_games), getResources().getDrawable(R.drawable.vector_games)));
        mItems.add(new DealsItem(2, getString(R.string.deal_kesehatan), getResources().getDrawable(R.drawable.kesehatan_icon)));
        mItems.add(new DealsItem(3, getString(R.string.deal_hiburan), getResources().getDrawable(R.drawable.hiburan_icon)));
        mItems.add(new DealsItem(4, getString(R.string.deal_makanan), getResources().getDrawable(R.drawable.makanan_icon)));
        mItems.add(new DealsItem(5, getString(R.string.deal_belanja), getResources().getDrawable(R.drawable.belanja_icon)));
        mItems.add(new DealsItem(6, getString(R.string.deal_transportasi), getResources().getDrawable(R.drawable.transportasi_icon)));
        mItems.add(new DealsItem(7, getString(R.string.deal_edukasi), getResources().getDrawable(R.drawable.edukasi_icon)));
        mItems.add(new DealsItem(8, getString(R.string.deal_hadiah), getResources().getDrawable(R.drawable.hadiah_icon)));
        //mItems.add(new DealsItem(9, getString(R.string.deal_travel), getResources().getDrawable(R.drawable.travel_icon)));
        //mItems.add(new DealsItem(10, getString(R.string.deal_otomotif), getResources().getDrawable(R.drawable.otomotif_icon)));
        //mItems.add(new DealsItem(11, getString(R.string.deal_olahraga), getResources().getDrawable(R.drawable.olahraga_icon)));
        //mItems.add(new DealsItem(12, getString(R.string.deal_fashion), getResources().getDrawable(R.drawable.fashion_icon)));
        mItems.add(new DealsItem(-1, getString(R.string.deal_semua_kategori), getResources().getDrawable(R.drawable.vector_more_category)));

        rvDeals.setLayoutManager(new GridLayoutManager(OttomartActivity.this, 4));
        rvDeals.setItemAnimator(new DefaultItemAnimator());
        rvDeals.setAdapter(new DealsMenuAdapter(OttomartActivity.this, mItems, (position, data) -> {
            if(mItems.get(position).getId() == -1)
                moveToSemuaKategori();
            else
                MessageHelper.commingSoonKategoriDialog(OttomartActivity.this);
        }));
    }

    private void moveToSemuaKategori(){
        startActivity(new Intent(OttomartActivity.this, SemuaKategoriActivity.class));
    }

    private void moveToRiwayatTransaksi(){
        startActivity(new Intent(OttomartActivity.this, RiwayatTransaksiActivity.class));
    }

    private void moveToTukarPoint(){
        startActivity(new Intent(OttomartActivity.this, TukarKuponActivity.class));
    }

    private void moveToVoucher(){
        startActivity(new Intent(OttomartActivity.this, VoucherSayaMainActivity.class));
    }

    private void moveToPromoTerbaru(){
        Intent intent = new Intent(OttomartActivity.this, DealsMainActivtiy.class);
        intent.putExtra(AppKeys.KEY_OTTOPOINT_DEALS_TYPE, GLOBAL.VOUCHER_TYPE_PROMO_TERBARU);
        startActivity(intent);
    }

    private void moveToPromoProdukKhusus(){
        Intent intent = new Intent(OttomartActivity.this, DealsMainActivtiy.class);
        intent.putExtra(AppKeys.KEY_OTTOPOINT_DEALS_TYPE, GLOBAL.VOUCHER_TYPE_PROMO_LAINNYA);
        startActivity(intent);
    }

    private void moveToDeals(){
        startActivity(new Intent(OttomartActivity.this, DealsMainActivtiy.class));
    }

    public static void openPage(Context context, int typeResponse, long point){
        if(context == null) return;

        Intent intent = new Intent(context, OttomartActivity.class);
        intent.putExtra(OttomartActivity.KEY_TYPE_RESPONSE, typeResponse);
        intent.putExtra(AppKeys.KEY_EXTRA_POINT, point);
        context.startActivity(intent);
    }

    public static void openPage(Context context){
        if(context == null) return;

        context.startActivity(new Intent(context, OttomartActivity.class));
    }

    private void refreshListPromoTerbaru(){
        if(adapterPromoTerbaru != null)
            adapterPromoTerbaru.notifyDataSetChanged();
        else
            adapterPromoTerbaru = configureViewPager(vpPromoTerbaru, indicatorPromoTerbaru, mItemsPromoTerbaru);

        moveToTop();
    }

    private void refreshListPromoLainnya(){
        if(adapterPromoLainnya != null)
            adapterPromoLainnya.notifyDataSetChanged();
        else
            adapterPromoLainnya = configureViewPager(vpPromoProdukKhusus, indicatorPromoProdukKhusus, mItemsProdukKhusus);

        moveToTop();
    }

    private void showProgressPromoTerbaru(boolean isShow){
        if(pbPromoTerbaru == null) return;

        pbPromoTerbaru.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void showProgressPromoKhusus(boolean isShow){
        if(pbPromoKhusus == null) return;

        pbPromoKhusus.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void moveToTop(){
        if(parentScroll == null) return;

        parentScroll.post(() -> {
            parentScroll.fling(0);
            parentScroll.smoothScrollTo(0, 0);
        });
    }

    private void callApiPromo(){
        showProgressPromoTerbaru(true);
        OttoPointDao.voucherDeals(OttomartActivity.this, pagePromo, "", "", "", true, 0,0, false, new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                if(br instanceof OpVoucherDealsResponseModel){
                    OpVoucherDealsResponseModel result = (OpVoucherDealsResponseModel) br;

                    if(pagePromo == 1) mItemsPromoTerbaru.clear();

                    maxPagePromo = result.getData().getHalaman();

                    for (OpVoucherDealsDetailResponseModel.Data item: result.getData().getCampaigns()) {
                        if(!item.isFeatured()) continue;

                        //if(checkPromoTerbaruIsOverItem()) break;

                        mItemsPromoTerbaru.add(new ViewPagerItem(
                                item.getCampaignId(),
                                CommonHelper.getUrlBannerVoucher(item.getUrl_photo(), GLOBAL.BANNER_VOUCHER_OTHERS),
                                new DealsItemModel(
                                    item.getCampaignId(),
                                    CommonHelper.getUrlBannerVoucher(item.getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST),
                                    CommonHelper.getUrlBannerVoucher(item.getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST),
                                    item.getUrl_logo() != null ? item.getUrl_logo() : "",
                                    item.getBrandName(),
                                    item.getName(),
                                    CommonHelper.currencyFormat(item.getCostInPoints()) + " poin",
                                    item.getCostInPoints()
                                )
                        ));
                    }

                    if(!isLoadPromoFinished()){
                        pagePromo++;
                        callApiPromo();
                        return;
                    }

                    showProgressPromoTerbaru(false);

                    refreshListPromoTerbaru();

                    setPromoFocustItem(vpPromoTerbaru, mItemsPromoTerbaru);
                }
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                showProgressPromoTerbaru(false);

                LogHelper.showError(TAG, "http code: " + responseCodeHttp + ", message: " + message);
            }
        });
    }

    private boolean isLoadPromoFinished(){
        return pagePromo == maxPagePromo;
    }

    private boolean checkPromoTerbaruIsOverItem(){
        return mItemsPromoTerbaru.size() >= 5;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null) return;

            if(intent.getAction() == null) return;

            switch (intent.getAction()){
                case AppKeys.KEY_BROADCAST_REFRESH_POINT_TWO:
                    long point = intent.getIntExtra(AppKeys.KEY_EXTRA_POINT, 0);

                    if(tvPoinOttopoint != null)
                        tvPoinOttopoint.setText(SessionManager.isOttopointAuthExist() ? CommonHelper.currencyFormat(point) : getString(R.string.text_daftar));
                    break;

                case AppKeys.KEY_BROADCAST_REFRESH_POINT:
                    getBalanceOttoPoint((balanceOttoPoint,metaCode) -> {
                        if(tvPoinOttopoint != null)
                            tvPoinOttopoint.setText(CommonHelper.currencyFormat(balanceOttoPoint));
                    });
                    break;
            }
        }
    };
}