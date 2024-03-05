package com.otto.mart.ui.fragment.dashboard;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otto.mart.BuildConfig;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.BannerModel;
import com.otto.mart.model.APIModel.Misc.ProfileResponseData;
import com.otto.mart.model.APIModel.Misc.olshop.Product;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.Store;
import com.otto.mart.model.APIModel.Request.grosir.CheckEligibleRequest;
import com.otto.mart.model.APIModel.Response.BannerRespomnse;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.ProfileResponseModel;
import com.otto.mart.model.APIModel.Response.balance.OttoKonekBalanceResponse;
import com.otto.mart.model.APIModel.Response.grosir.CheckEligibleResponse;
import com.otto.mart.model.APIModel.Response.olshop.Category;
import com.otto.mart.model.APIModel.Response.olshop.CategoryResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.background.EventData;
import com.otto.mart.model.APIModel.Response.olshop.background.EventResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.StoreListResponse;
import com.otto.mart.model.localmodel.Realm.LoginDatastoreModel;
import com.otto.mart.model.localmodel.ui.GridMenu;
import com.otto.mart.presenter.dao.BannerDao;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.presenter.dao.TokoOttopayDao;
import com.otto.mart.presenter.dao.TransactionDao;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.presenter.dao.olshop.GrosirDao;
import com.otto.mart.presenter.dao.olshop.OlshopDao;
//import com.otto.mart.presenter.dao.ottopoin.OttoPoinDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.support.util.Connectivity;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.DeviceUtil;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.support.util.widget.recyclerView.ItemClickSupport;
import com.otto.mart.support.util.widget.viewpagerIndicator.CirclePageIndicator;
import com.otto.mart.ui.Partials.ContactUsRepo;
import com.otto.mart.ui.Partials.adapter.CategoryOlshopHomeAdapter;
import com.otto.mart.ui.Partials.adapter.FragmentPagerAdapter;
import com.otto.mart.ui.Partials.adapter.GridMenuAdapter;
import com.otto.mart.ui.Partials.adapter.ppob.PpobMenuAdapter;
import com.otto.mart.ui.Partials.adapter.tokoOttopay.HomeStoreAdapter;
import com.otto.mart.ui.actionView.HandleResponseApiTwo;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.dashboard.IDashboard;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.multibank.ListLinkedBankAccountActivity;
import com.otto.mart.ui.activity.olshop.CatalogActivity;
import com.otto.mart.ui.activity.olshop.ProductDetailActivity;
import com.otto.mart.ui.activity.ottopoint.OttomartActivity;
import com.otto.mart.ui.activity.ottopoint.SyaratKetentuanActivity;
import com.otto.mart.ui.activity.paymentMethod.PaymentMethodActivity;
import com.otto.mart.ui.activity.ppob.setup.PpobMenuSetup;
import com.otto.mart.ui.activity.qr.scan.ScanQrActivity;
import com.otto.mart.ui.activity.qr.show.ShowQrActivity;
import com.otto.mart.ui.activity.tokoOttopay.OrderHistoryActivity;
import com.otto.mart.ui.activity.tokoOttopay.ReOrderActivity;
import com.otto.mart.ui.activity.transaction.balance.OmzetActivity;
import com.otto.mart.ui.activity.transaction.history.HistoryActivity;
import com.otto.mart.ui.activity.transaction.transferToBank.TransferBankActivity;
import com.otto.mart.ui.component.CustomGlideTarget;
import com.otto.mart.ui.component.dialog.InfoEligibleOttoPointDialog;
import com.otto.mart.ui.component.dialog.Popup;
import com.otto.mart.ui.fragment.AppFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import app.beelabs.com.codebase.support.util.CacheUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Response;

import static com.otto.mart.GLOBAL.TOKO_OTTOPAY;
import static com.otto.mart.keys.AppKeys.API_KEY_GET_PROFILE;
import static com.otto.mart.ui.activity.tokoOttopay.ReOrderActivity.KEY_STORE_DATA;
import static com.otto.mart.ui.fragment.dashboard.BannerFragment.KEY_BANNER_IMAGE;
import static com.otto.mart.ui.fragment.dashboard.BannerFragment.KEY_BANNER_URL;


public class DashboardHomeFragment extends AppFragment implements SwipeRefreshLayout.OnRefreshListener {

    final int KEY_API_REVENUE = 100;
    final int KEY_API_BALANCE = 101;

    @BindView(R.id.tv_point)
    TextView tvPoinOttopoint;

    private final String CATEGORIES = "categories";

    Context mContext;
    View mView, iclSpecialEvent;
    ImageView imgLogo;
    TextView tvGroupMerchantName;
    TextView tvLabelByOttoPay;
    LinearLayout headerLayout;
    RelativeLayout btnOmzet;
    LinearLayout menuOasisLayout;
    ImageView imgBtnSearch;
    ViewGroup btnPoint;
    LinearLayout ppobContainer;
    LinearLayout tokoOnlineContainer;
    LinearLayout walletLayout;
    LinearLayout headerBg;
    TextView tvPpobMenuTitle;
    TextView tvPpobMenuDesc;
    TextView tvPoint;
    TextView tvSaldo;
    TextView tvRevenueLabel;
    TextView tvBalanceLabel;
    RecyclerView rvTopMenu, categoryOlshop;
    RecyclerView rvPpobMenu;
    TextView tv_income, tvOlShopTitle, tvOlShopSubTitle;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView rvTokoOttopay;
    ViewAnimator viewAnimator;
    ViewPager viewPager;
    CirclePageIndicator vpIndicator;
    RelativeLayout bannerLayout;
    LinearLayout tokoOttopayContainer;
    ImageView imgOrderHistory;

    LinearLayout oasisContainer;
    TextView tvOasisMenuTitle;
    TextView tvOasisMenuDesc;
    RecyclerView rvOasisMenu;

    LinearLayout ppobFinanceContainer;
    TextView tvPpobFinanceMenuTitle;
    TextView tvPpobFinanceMenuDesc;
    RecyclerView rvPpobFinanceMenu;

    int mBannerPage = 0;
    Timer mBannerTimer;
    List<Category> categories = new ArrayList<>();

    FragmentPagerAdapter mFragmentAdapter;
    CategoryOlshopHomeAdapter categoryAdapter;
    CompositeDisposable disposable;
//    OttoPoinDao poinDao;
    String pointStatus;

    //region special event
    ImageView ivBgEvent, ivEventLogo;
    TextView tvEventTitle, tvEventSubTitle;
    View btnAll;
    EventData specialEventData;
    //endregion

    RelativeLayout btn_linked_account;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AppKeys.KEY_BROADCAST_REFRESH_POINT);
            intentFilter.addAction(AppKeys.KEY_BROADCAST_REFRESH_POINT_TWO);
            intentFilter.addAction(AppKeys.KEY_BROADCAST_REFRESH_VIEW_DASHBOARD);
            intentFilter.addAction(AppKeys.KEY_BROADCAST_GET_POINT);
            getActivity().registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_dashboard_home, container, false);
        ((IDashboard) getActivity()).isMainPage(true);
        ButterKnife.bind(this, mView);
        //initPointSDK();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent();
        initContent();
        callApiProfile();
        checkOasisStatus();
        getContactUs();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initComponent() {
        headerLayout = mView.findViewById(R.id.headerLayout);
        imgLogo = mView.findViewById(R.id.imgHeaderLogo);
        tvGroupMerchantName = mView.findViewById(R.id.tvGroupMerchantName);
        tvLabelByOttoPay = mView.findViewById(R.id.tvLabelByOttoPay);
        btnOmzet = mView.findViewById(R.id.btn_omzet);
        btnPoint = mView.findViewById(R.id.btn_point);
        ppobContainer = mView.findViewById(R.id.ppobContainer);
        tokoOnlineContainer = mView.findViewById(R.id.tokoOnlineContainer);
        walletLayout = mView.findViewById(R.id.walletLayout);
        headerBg = mView.findViewById(R.id.header_bg);
        rvPpobMenu = mView.findViewById(R.id.recyclerview);
        imgBtnSearch = mView.findViewById(R.id.imgBtnSearch);
        tvPpobMenuTitle = mView.findViewById(R.id.tvPpobMenuTitle);
        tvPpobMenuDesc = mView.findViewById(R.id.tvPpobMenuDesc);
        tvSaldo = mView.findViewById(R.id.tv_saldo);
        tvRevenueLabel = mView.findViewById(R.id.tv_revenue_label);
        tvBalanceLabel = mView.findViewById(R.id.tv_balance_label);
        tvPoint = mView.findViewById(R.id.tv_point);
        rvTopMenu = mView.findViewById(R.id.rv_top_menu);
        tv_income = mView.findViewById(R.id.tv_income);
        swipeRefresh = mView.findViewById(R.id.swipeRefresh);
        viewAnimator = mView.findViewById(R.id.view_animator);
        viewPager = mView.findViewById(R.id.view_pager);
        vpIndicator = mView.findViewById(R.id.indicator);
        bannerLayout = mView.findViewById(R.id.banner_layout);
        tokoOttopayContainer = mView.findViewById(R.id.toko_ottopay_container);
        rvTokoOttopay = mView.findViewById(R.id.rv_toko_ottopay);
        imgOrderHistory = mView.findViewById(R.id.img_order_history);
        iclSpecialEvent = mView.findViewById(R.id.iclSpecialEvent);

        categoryOlshop = mView.findViewById(R.id.categoryOlshopList);
        tvOlShopTitle = mView.findViewById(R.id.tvOlShopTitle);
        tvOlShopSubTitle = mView.findViewById(R.id.tvOlShopSubTitle);
        categoryOlshop = mView.findViewById(R.id.categoryOlshopList);
        iclSpecialEvent = mView.findViewById(R.id.iclSpecialEvent);

        oasisContainer = mView.findViewById(R.id.oasisContainer);
        tvOasisMenuTitle = mView.findViewById(R.id.tvOasisMenuTitle);
        tvOasisMenuDesc = mView.findViewById(R.id.tvOasisMenuDesc);
        rvOasisMenu = mView.findViewById(R.id.rvOasisMenu);

        ppobFinanceContainer = mView.findViewById(R.id.ppobFinanceContainer);
        tvPpobFinanceMenuTitle = mView.findViewById(R.id.tvPpobFinanceMenuTitle);
        tvPpobFinanceMenuDesc = mView.findViewById(R.id.tvPpobFinanceMenuDesc);
        rvPpobFinanceMenu = mView.findViewById(R.id.rvPpobFinanceMenu);

        btn_linked_account = mView.findViewById(R.id.btn_account);


        btn_linked_account.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), ListLinkedBankAccountActivity.class);
           // Intent intent = new Intent(getActivity(), PaymentMethodActivity.class);

            startActivity(intent);
        });

        rvTopMenu.setHasFixedSize(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        rvTopMenu.setLayoutManager(gridLayoutManager);

        rvOasisMenu.setHasFixedSize(false);
        GridLayoutManager glmOasisMenu = new GridLayoutManager(getActivity(), 4);
        rvOasisMenu.setLayoutManager(glmOasisMenu);

        rvPpobMenu.setHasFixedSize(false);
        GridLayoutManager glmPpobMenu = new GridLayoutManager(getActivity(), 4);
        rvPpobMenu.setLayoutManager(glmPpobMenu);

        rvPpobFinanceMenu.setHasFixedSize(false);
        GridLayoutManager glmPpobFinanceMenu = new GridLayoutManager(getActivity(), 4);
        rvPpobFinanceMenu.setLayoutManager(glmPpobFinanceMenu);

        // set default parameter for element view ottopooint
        // tnPoint.setEnabled(false);
        initSpecialEventComponent();


        //Init Themes by Merchant Group
        if (SessionManager.getPrefLogin().getCategory().equalsIgnoreCase(MerchantAccessRules.Companion.getKEY_MERCHANT_GROUP_OP())) {
            tvLabelByOttoPay.setVisibility(View.GONE);
        } else {
            tvLabelByOttoPay.setVisibility(View.GONE);
        }

        String themeColor = AppKeys.DEFAULT_THEME_COLOR;

        if (SessionManager.getMerchantTheme() != null && SessionManager.getMerchantTheme().getThemeColor() != null) {
            if (SessionManager.getMerchantTheme().getThemeColor().contains("#")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    themeColor = SessionManager.getMerchantTheme().getThemeColor();
                }
            }
        }

        if (SessionManager.getMerchantTheme() != null && SessionManager.getMerchantTheme().getDashboardLogo() != null) {
            if (SessionManager.getMerchantTheme().getDashboardText() != null) {
                tvGroupMerchantName.setText(SessionManager.getMerchantTheme().getDashboardText());
            }

            Glide.with(this)
                    .load(SessionManager.getMerchantTheme().getDashboardLogo())
                    .error(R.drawable.ic_ottopay_logo)
                    .into(imgLogo);
        }

        //Set Header Color Bg
        headerLayout.setBackgroundColor(Color.parseColor(themeColor));

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor(themeColor));
        shape.setStroke(0, Color.BLACK);
        headerBg.setBackground(shape);
    }

    private void initContent() {
        disposable = new CompositeDisposable();

        //Validate Access by Group Merchant
        validateAccessGroupMerchant();

        tv_income.setText("-");
        tvSaldo.setText("-");
        tvPoint.setText(SessionManager.isOttopointAuthExist() ? CommonHelper.currencyFormat(SessionManager.getOttopointPoint()) : getString(R.string.text_daftar));

        displayTopMenu();
        initSpecialEventContent();

        setupBanner();
        requestBanner();
        requestEventBanner();

        if (BuildConfig.DEBUG) {
            if (TOKO_OTTOPAY) {
                getStoreList();
            }
        }

        //Get latest saved balance
        tv_income.setText(Pref.getPreference().getString(AppKeys.PREF_LAST_OMZET));
        tvSaldo.setText(Pref.getPreference().getString(AppKeys.PREF_LAST_BALANCE));

        LoginDatastoreModel model = SessionManager.getUserProfile();
        if (model == null) {
            SessionManager.logout();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        swipeRefresh.setOnRefreshListener(this);

        //Display Oasis Menu
        if (getActivity() != null) {
            if (SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().getToko_grosir() != null
                    && SessionManager.getFeatureProduct().getToko_grosir().size() > 0) {
                if (SessionManager.getFeatureProduct().getToko_grosir().get(0).getProduct_title() != null &&
                        SessionManager.getFeatureProduct().getToko_grosir().get(0).getProduct_desc() != null) {
                    tvOasisMenuTitle.setText(SessionManager.getFeatureProduct().getToko_grosir().get(0).getProduct_title());
                    tvOasisMenuDesc.setText(SessionManager.getFeatureProduct().getToko_grosir().get(0).getProduct_desc());
                }
            }

            PpobMenuAdapter ppobMenuAdapter = new PpobMenuAdapter(getActivity(), new PpobMenuSetup(getActivity()).getOasisMenu());
            rvOasisMenu.setAdapter(ppobMenuAdapter);
            ppobMenuAdapter.setmOnViewClickListener((item, position) -> {
                if (item.getIntent() != null) {
                    startActivity(item.getIntent());
                } else {
                    comingSoonDialog();
                }
            });
        }

        //Display PPOB Menu
        if (getActivity() != null) {
            if (SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().getPpob() != null
                    && SessionManager.getFeatureProduct().getPpob().size() > 0) {
                if (SessionManager.getFeatureProduct().getPpob().get(0).getProduct_title() != null &&
                        SessionManager.getFeatureProduct().getPpob().get(0).getProduct_desc() != null) {
                    tvPpobMenuTitle.setText(SessionManager.getFeatureProduct().getPpob().get(0).getProduct_title());
                    tvPpobMenuDesc.setText(SessionManager.getFeatureProduct().getPpob().get(0).getProduct_desc());
                    ppobContainer.setVisibility(View.VISIBLE);
                }
            }

            PpobMenuAdapter ppobMenuAdapter = new PpobMenuAdapter(getActivity(), new PpobMenuSetup(getActivity()).getPpobMenuForHome());
            rvPpobMenu.setAdapter(ppobMenuAdapter);
            ppobMenuAdapter.setmOnViewClickListener((item, position) -> {
                if (item.getIntent() != null) {
//                    startActivity(item.getIntent());
                    comingSoonDialog();
                } else {
                    ((DashboardActivity) getActivity()).showMoreMenuFragment();
                }
            });
        }

        //Display PPOB Finance Menu
        if (getActivity() != null) {
            if (SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().getProduct_finansial() != null
                    && SessionManager.getFeatureProduct().getProduct_finansial().size() > 0) {
                if (SessionManager.getFeatureProduct().getProduct_finansial().get(0).getProduct_title() != null &&
                        SessionManager.getFeatureProduct().getProduct_finansial().get(0).getProduct_desc() != null) {
                    tvPpobFinanceMenuTitle.setText(SessionManager.getFeatureProduct().getProduct_finansial().get(0).getProduct_title());
                    tvPpobFinanceMenuDesc.setText(SessionManager.getFeatureProduct().getProduct_finansial().get(0).getProduct_desc());
                    ppobFinanceContainer.setVisibility(View.VISIBLE);
                }
            }

            PpobMenuAdapter ppobMenuAdapter = new PpobMenuAdapter(getActivity(), new PpobMenuSetup(getActivity()).getPpobFinanceMenu());
            rvPpobFinanceMenu.setAdapter(ppobMenuAdapter);
            ppobMenuAdapter.setmOnViewClickListener((item, position) -> {
                comingSoonDialog();
            });
        }

        imgBtnSearch.setOnClickListener(v -> {
            // startActivity(new Intent(getActivity(), SearchActivity.class));
            comingSoonDialog();
        });

        btnOmzet.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OmzetActivity.class);
            intent.putExtra(OmzetActivity.KEY_IS_FROM_OMZET, true);
            startActivity(intent);
        });

//        btnPoint.setOnClickListener(v -> {
//                    if (pointStatus != null) {
//                        if (pointStatus.equalsIgnoreCase(OttoPoinDao.NOT_ELIGIBLE_KEY)) {
//                            InfoEligibleOttoPointDialog.showDialog(getContext(), "", getString(R.string.message_feature_unavailable), data -> {
//                            });
//                        } else {
//                            if (poinDao != null) {
//                                poinDao.callAction(getLongLat());
//                            }
//                        }
//                    }
//                }
//        );

        walletLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OmzetActivity.class);
            intent.putExtra(OmzetActivity.KEY_IS_FROM_OMZET, false);
            startActivity(intent);
        });

        //Toko OttoPay
        rvTokoOttopay.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        imgOrderHistory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), OrderHistoryActivity.class));
        });

        //toko online
        categoryAdapter = new CategoryOlshopHomeAdapter((id, pos, data) -> {
            Intent intent = new Intent(getContext(), CatalogActivity.class);
            intent.putExtra("category", new Gson().toJson(categories));
            intent.putExtra("selectedCategory", new Gson().toJson((Category) data));

            startActivity(intent);
        });
        categoryOlshop.setNestedScrollingEnabled(false);
//        categoryOlshop.addItemDecoration(new RealGridDecorator(DeviceUtil.dpToPx(8), 3));
        categoryOlshop.setLayoutManager(new GridLayoutManager(getContext(), 4));
//        categoryOlshop.setAdapter(categoryAdapter);
    }

    private void validateAccessGroupMerchant() {
        btnPoint.setVisibility(View.GONE);
        oasisContainer.setVisibility(View.GONE);
        ppobContainer.setVisibility(View.GONE);
        tokoOnlineContainer.setVisibility(View.GONE);
        iclSpecialEvent.setVisibility(View.GONE);
        ppobFinanceContainer.setVisibility(View.GONE);

//        if (new MerchantAccessRules().ppob()) {
//            ppobContainer.setVisibility(View.VISIBLE);
//        }

//        if (new MerchantAccessRules().tokoOnline()) {
//            tokoOnlineContainer.setVisibility(View.VISIBLE);
//        }
    }

    private void comingSoonDialog() {
        Popup dialog = new Popup();
        dialog.setHideBtnNegative(true);
        dialog.setHideBtnPositive(false);
        dialog.setPositiveButton(getString(R.string.oke));
        dialog.setTitle(getString(R.string.message_feature_coming_soon));
        dialog.show(getChildFragmentManager(), "popupInfo");
    }

    private void displayTopMenu() {
        List<GridMenu> gridMenuList = new ArrayList();

        GridMenu receive = new GridMenu();
        receive.setName(getString(R.string.text_recieve));
        receive.setIcon(R.drawable.icon_button_belanjaqr);
        receive.setIntent(new Intent(getActivity(), ShowQrActivity.class));
        gridMenuList.add(receive);

        GridMenu pay = new GridMenu();
        pay.setName(getString(R.string.button_pay));
        pay.setIcon(R.drawable.icon_button_riwayattransaksi);
        pay.setIntent(new Intent(getActivity(), ScanQrActivity.class));
        gridMenuList.add(pay);

        GridMenu transfer = new GridMenu();
        transfer.setName(getString(R.string.title_activity_transfer_bank));
        transfer.setIcon(R.drawable.transfer);
        transfer.setIntent(new Intent(getActivity(), TransferBankActivity.class));
        gridMenuList.add(transfer);

        Intent historyOmzetIntent = new Intent(getActivity(), HistoryActivity.class);
        historyOmzetIntent.putExtra(HistoryActivity.KEY_TITLE, HistoryActivity.TAB_LABEL_OMZET);

        GridMenu history = new GridMenu();
        history.setName(getString(R.string.label_riwayat));
        history.setIcon(R.drawable.icon_button_history);
        history.setIntent(historyOmzetIntent);
        gridMenuList.add(history);

        GridMenuAdapter adapter = new GridMenuAdapter(getActivity(), gridMenuList);
        adapter.setFromHome(true);
        rvTopMenu.setAdapter(adapter);

        adapter.setmOnViewClickListener((gridMenu, position) -> {
            startActivity(gridMenu.getIntent());
        });
    }

    private void displayStoreList(List<Store> storeList) {
        HomeStoreAdapter adapter = new HomeStoreAdapter(getActivity(), storeList);
        rvTokoOttopay.setAdapter(adapter);

        ItemClickSupport.addTo(rvTokoOttopay).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                gotoReOrder(storeList.get(position));
            }
        });

        tokoOttopayContainer.setVisibility(View.VISIBLE);
    }

    private void gotoReOrder(Store store) {
        Intent intent = new Intent(getActivity(), ReOrderActivity.class);
        intent.putExtra(KEY_STORE_DATA, new Gson().toJson(store));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getOmzetStatus();
        //getWalletInfo();
        checkOasisStatus();
        getSpecialEventProduct();

        Realm realm = Realm.getDefaultInstance();
        LoginDatastoreModel model = SessionManager.getUserProfile();
        if (model == null) {
            SessionManager.logout();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        // ottosdk = ((DashboardActivity)getActivity()).getOttoSdk(this);
        //initPointSDK();
        //  poinDao.callAction(getLongLat());
    }

//    private void handlePointLifeCycle() {
//        if (poinDao == null && !isPinOttoPointConfirm) {
//            initPointSDK();
//        }
//
//        if (isPinOttoPointConfirm && poinDao != null) {
//            poinDao.callAction(getLongLat());
//            poinDao = null;
//        }
//    }

    /*private void initPointSDK() {
     *//*initialize point sdk*//*
        poinDao = new OttoPoinDao();
        poinDao.init(this);
        poinDao.callAction(getLongLat());
    }*/

    private String getLongLat() {
        if (getActivity() != null)
            return ((AppActivity) getActivity()).getLongLat();
        return "";
    }

    @Override
    public void onRefresh() {
        getOmzetStatus();
     //   getWalletInfo();
//        new OlshopDao(this).getCategories(BaseDao.getInstance(this, 1).callback);
        //initPointSDK();
    }

    private void requestBanner() {
        int imgX = 651;
        int imgY = 334;

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int bannerHeight = (imgY * screenWidth) / imgX;

        bannerLayout.getLayoutParams().width = screenWidth;
        bannerLayout.getLayoutParams().height = bannerHeight - 24;

        new BannerDao(this).getBannerData(BaseDao.getInstance(this, 898).callback);
    }

    private void setupBanner() {
        //SetFragment to View Pager Adapter
        mFragmentAdapter = new FragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mFragmentAdapter);

        //Set Viewpager Indicator
        vpIndicator.setViewPager(viewPager);
    }

    private void requestEventBanner() {
//        new OlshopDao(this).getBanner(BaseDao.getInstance(this, 485).callback);
    }

    private void getOmzetStatus() {
        if (Connectivity.isConnected(getActivity())) {
            new TransactionDao(this).revenue(BaseDao.getInstance(this, KEY_API_REVENUE).callback);
        } else {
            swipeRefresh.setRefreshing(false);
            dialogNoInternetConnection();
        }
    }

    private int getWidthImage() {
        int screenWidth = getScreenWidth();
        if (screenWidth != 0) {
            int widthExpect = screenWidth - 48;

            return (int) (TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    widthExpect,
                    getResources().getDisplayMetrics()));
        }
        return 0;
    }

    private int getScreenWidth() {
        if (getActivity() != null && getActivity().getWindowManager() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            return DeviceUtil.pxToDp(width);
        } else return 0;
    }

    private void displayBanner(List<BannerModel> bannerList) {
        Fragment fragment;
        Bundle bundle;

        for (BannerModel banner : bannerList) {
            if (banner.getStatus().equalsIgnoreCase("y")) {
                fragment = new BannerFragment();
                bundle = new Bundle();
                bundle.putString(KEY_BANNER_IMAGE, banner.getAdsLink());
                bundle.putString(KEY_BANNER_URL, banner.getUrl());
                fragment.setArguments(bundle);
                mFragmentAdapter.addFragment(fragment, "");
            }
        }

        mFragmentAdapter.notifyDataSetChanged();
        viewPager.setOffscreenPageLimit(mFragmentAdapter.getCount() - 1);

        //Set Viewpager Indicator
        vpIndicator.setViewPager(viewPager);

        autoSlideBanner(mFragmentAdapter.getCount());
        viewAnimator.setDisplayedChild(1);
    }

    public void autoSlideBanner(final int numPages) {
        final long DELAY_MS = 500;
        final long PERIOD_MS = 5000;

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (mBannerPage == numPages) {
                    mBannerPage = 0;
                }
                viewPager.setCurrentItem(mBannerPage, true);
                mBannerPage++;
            }
        };

        if (mBannerTimer != null) {
            mBannerTimer.cancel();
        }

        mBannerTimer = new Timer();
        mBannerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
//        getCacheCategory();
//        new OlshopDao(this).getCategories(BaseDao.getInstance(this, 1).callback);
    }

    @SuppressLint("CheckResult")
    private void getCacheCategory() {
        if (Pref.getPreference().getString(CATEGORIES) != null) {
            disposable.add(
                    Observable.fromCallable((Callable<List<Category>>) () ->
                            new Gson().fromJson(Pref.getPreference().getString(CATEGORIES), new TypeToken<List<Category>>() {
                            }.getType()))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(categories -> {
                                if (categories.size() < 1)
                                    new OlshopDao(this).getCategories(BaseDao.getInstance(this, 1).callback);
                                showCategories(categories);
                            }, Throwable::printStackTrace)
            );
        }
    }

    private void showCategories(List<Category> categories) {
        if (categories.size() > 0) {
            categoryAdapter.setCategories(categories);
            if (specialEventData != null &&
                    specialEventData.getSelected() != null &&
                    specialEventData.getSelected().getProducts() != null &&
                    specialEventData.getSelected().getProducts().size() > 0
                    && categories != null && categories.size() > 0) {

                setSpecialEventData(specialEventData);
            }

            setOlShopMenu();
        }
    }

    private void setOlShopMenu() {
        if (getContext() != null && SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().getToko_online() != null &&
                SessionManager.getFeatureProduct().getToko_online().size() > 0) {

            if (SessionManager.getFeatureProduct().getToko_online().get(0).getProduct_title() != null)
                tvOlShopTitle.setText(SessionManager.getFeatureProduct().getToko_online().get(0).getProduct_title());
            if (SessionManager.getFeatureProduct().getToko_online().get(0).getProduct_desc() != null)
                tvOlShopSubTitle.setText(SessionManager.getFeatureProduct().getToko_online().get(0).getProduct_desc());

            PpobMenuAdapter olshopMenuAdapter = new PpobMenuAdapter(getContext(), new PpobMenuSetup(getContext()).getOlshopMenuForHome());
            categoryOlshop.setAdapter(olshopMenuAdapter);
            olshopMenuAdapter.setmOnViewClickListener((item, position) -> {
                int menuId = item.getIntent().getIntExtra("menuId", -1);
                if (menuId != -1) {
                    for (Category category : categories) {
                        if (category.getId() == menuId) {
                            item.getIntent().putExtra("category", new Gson().toJson(categories));
                            item.getIntent().putExtra("selectedCategory", new Gson().toJson(category));

                            startActivity(item.getIntent());
                            return;
                        }
                    }
                }
            });

            tokoOnlineContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }

        if (getActivity() != null)
            getActivity().unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }

    private void callApiCheckRegisteredOttopoint() {
        if (!checkContextPage()) return;

        try {
            OttoPointDao.checUserRegistered(getActivity(), (isRegistered, point) -> setViewPointOttopoint(point));
        } catch (Exception e) {
            Log.e(DashboardHomeFragment.class.getSimpleName(), e.getMessage());
        }
    }

    private void callApiProfile() {
//        new ProfileDao(this).getProfile(BaseDao.getInstance(this, API_KEY_GET_PROFILE).callback);
    }

    public void moveToOttomart() {
        if (!checkContextPage()) return;

        if (SessionManager.isOttopointAuthExist())
            OttomartActivity.openPage(getActivity());
        else
            callApiCheckEligibleCustomer();
    }

    private void showProgress(boolean isShow) {
        if (!checkContextPage()) return;

        try {
            if (isShow)
                ProgressDialogComponent.showProgressDialog(getActivity(), null, false).show();
            else
                ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        } catch (Exception e) {
            Log.e(DashboardHomeFragment.class.getSimpleName(), e.getMessage());
        }
    }

    private void callApiCheckEligibleCustomer() {
        showProgress(true);
        OttoPointDao.checkEligiblePhone((BaseActivity) getActivity(), new HandleResponseApiTwo() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                showProgress(false);

                if (checkContextPage())
                    startActivity(new Intent(getActivity(), SyaratKetentuanActivity.class));
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp, int metaCode) {
                showProgress(false);

                if (checkContextPage())
                    InfoEligibleOttoPointDialog.showDialog(getActivity(), "", getString(R.string.text_phone_not_eligible), data -> {
                    });
            }
        });
    }

    private void setViewPointOttopoint(double balance) {
        if (!checkContextPage()) return;

        if (tvPoinOttopoint != null) {
            if (SessionManager.isOttopointAuthExist()) {
                tvPoinOttopoint.setText(balance == -1 ? CommonHelper.currencyFormat(SessionManager.getOttopointPoint()) : CommonHelper.currencyFormat(balance));
            } else {
                tvPoinOttopoint.setText(getString(R.string.text_daftar));
            }
        }

        if (btnPoint != null)
            btnPoint.setEnabled(balance != -1);
    }

    private boolean checkContextPage() {
        return getActivity() != null;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getActivity() == null) return;

            if (intent == null) return;

            if (intent.getAction() == null) return;

            switch (intent.getAction()) {
                case AppKeys.KEY_BROADCAST_REFRESH_POINT:
                    OttoPointDao.getBalance(getActivity(), (balance, metaCode) -> setViewPointOttopoint(balance));
                    break;
                case AppKeys.KEY_BROADCAST_REFRESH_POINT_TWO:
                    long point = intent.getIntExtra(AppKeys.KEY_EXTRA_POINT, 0);
                    setViewPointOttopoint(point);
                    break;
                case AppKeys.KEY_BROADCAST_REFRESH_VIEW_DASHBOARD:
                    OttoPointDao.getBalance(getActivity(), (balance, metaCode) -> setViewPointOttopoint(balance));
                    break;
                case AppKeys.KEY_BROADCAST_GET_POINT:
//                    int earnPoint = intent.getIntExtra(KEY_EXTRA_POINT, 0);
//                    String productName = intent.getStringExtra(KEY_EXTRA_PRODUCT_NAME) != null ? intent.getStringExtra(KEY_EXTRA_PRODUCT_NAME) : "";
//                    Log.e(DashboardHomeFragment.class.getSimpleName(), "Hello");
//
//                    // show dialog
//                    EarnPointDialog.showDialog(getActivity(), earnPoint, productName, data -> {
//                        // do something in here
//                    });
                    break;
            }
        }
    };


    //region API Call

    private void getWalletInfo() {
        new WalletDao(this).balance(BaseDao.getInstance(DashboardHomeFragment.this, KEY_API_BALANCE).callback);
    }

    private void getStoreList() {
        new TokoOttopayDao(this).getStoreList(BaseDao.getInstance(this, AppKeys.API_KEY_STORE_LIST).callback);
    }

    private void checkOasisStatus() {
        CheckEligibleRequest request = new CheckEligibleRequest();
        request.setArea_id(SessionManager.getPrefLogin().getAddressBarangayCode());
        //new GrosirDao(this).checkOasisStatus(BaseDao.getInstance(DashboardHomeFragment.this, AppKeys.API_KEY_GET_CHECK_OASIS_STATUS).callback);
        new GrosirDao(this).checkEligibleOasis(request, BaseDao.getInstance(DashboardHomeFragment.this, AppKeys.API_KEY_GET_CHECK_OASIS_STATUS).callback);
    }

    private void getSpecialEventProduct() {
//        new OlshopDao(this).getSpecialEventProducts(BaseDao.getInstance(DashboardHomeFragment.this, AppKeys.API_KEY_GET_SPECIAL_EVENT_PRODUCT).callback);
    }

    private void getContactUs() {
        new ContactUsRepo().getContactUs(contactUsData -> null);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        System.out.println();
        if (response.isSuccessful())
            switch (responseCode) {
                case KEY_API_REVENUE:
                    swipeRefresh.setRefreshing(false);
                    if ((((BaseResponseModel) br).getMeta().getCode() == 200)) {
                        OttoKonekBalanceResponse model = (OttoKonekBalanceResponse) br;
                        if (model != null) {
                            double allOmzet = model.getData().getBalance();

                            if (getActivity() != null) {
                                tv_income.setText(model.getData().getFormatted_balance());
                            }

                            //Save Last Omzet
                            Pref.getPreference().putString(AppKeys.PREF_LAST_OMZET, tv_income.getText().toString());

                            if (allOmzet < 0.0) {
                                SessionManager.logout();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        }
                    }
                    break;
                case 898:
                    if ((((BaseResponseModel) br).getMeta().getCode() == 200)) {
                        BannerRespomnse bannerRespomnse = (BannerRespomnse) br;
                        if (bannerRespomnse.getData() != null) {
                            SessionManager.setProfileCardBg(bannerRespomnse.getData().getProfileBackgroundImage());
                            SessionManager.setProfileLogo(bannerRespomnse.getData().getDashboardLogo());
                            if (bannerRespomnse.getData().getBanner() != null) {
                                if (bannerRespomnse.getData().getBanner().size() > 0) {
                                    displayBanner(bannerRespomnse.getData().getBanner());
                                }
                            }
                        }
                    }
                    break;
                case AppKeys.API_KEY_STORE_LIST:
                    if ((((BaseResponseModel) br).getMeta().getCode() == 200)) {
                        StoreListResponse storeListResponse = (StoreListResponse) br;
                        if (storeListResponse.getData() != null) {
                            if (storeListResponse.getData().getSuppliers().size() > 0) {
                                displayStoreList(storeListResponse.getData().getSuppliers());
                            }
                        }
                    }
                    break;
                case 1: {
                    categories = ((CategoryResponseModel) br).getData().getCategories();
                    Pref.getPreference().putString(CATEGORIES, new Gson().toJson(categories));
                    showCategories(categories);
                    break;
                }
                case KEY_API_BALANCE:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        if (((OttoKonekBalanceResponse) br).getData() != null) {
                            tvSaldo.setText(((OttoKonekBalanceResponse) br).getData().getFormatted_balance());

                            //Save Last Balance
                            Pref.getPreference().putString(AppKeys.PREF_LAST_BALANCE, tvSaldo.getText().toString());
                        }
                    } else {
                        tvSaldo.setText("-");
                        Pref.getPreference().putString(AppKeys.PREF_LAST_BALANCE, "-");
                    }
                    break;
                case API_KEY_GET_PROFILE:
                    if (responseCode == AppKeys.API_KEY_GET_PROFILE) {
                        ProfileResponseModel model = (ProfileResponseModel) br;
                        ProfileResponseData data = model.getData();
                        if (data != null && data.getAddresses() != null && data.getAddresses().size() > 0) {
                            SessionManager.setDefaultAddress(data.getAddresses().get(0).getCompleteAddress());
                            if (getContext() != null) {
                                CacheUtil.putPreferenceString(AppKeys.FULL_ADDRESS, data.getAddresses().get(0).getCompleteAddress(), getContext());
                                CacheUtil.putPreferenceString(AppKeys.CITY_ID, String.valueOf(data.getAddresses().get(0).getCityId()), getContext());
                            }
                        }
                    }
                    break;
                case AppKeys.API_KEY_GET_CHECK_OASIS_STATUS:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        if (((CheckEligibleResponse) br).getData() != null) {
                            if (Objects.requireNonNull(((CheckEligibleResponse) br).getData()).isEligible()) {
                                if (getActivity() != null) {
                                    if (SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().getToko_grosir() != null) {
                                        if (SessionManager.getFeatureProduct().getToko_grosir().get(0).getProduct_title() != null &&
                                                SessionManager.getFeatureProduct().getToko_grosir().get(0).getProduct_desc() != null &&
                                                new PpobMenuSetup(getActivity()).getOasisMenu().size() > 0
                                        ) {
                                            oasisContainer.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case AppKeys.API_KEY_GET_SPECIAL_EVENT_PRODUCT:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        handleSpecialEventResponse((EventResponse) br);
                    }
                    break;
                case 485:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200)
//                        displayBanner(((BannerRespomnse) br).getData().getBanners());
                        break;
                default:
                    Log.d("", "onApiResponseCallback: banner not found");
                    break;
            }
    }

    @Override
    protected void onApiFailureCallback(String message) {
//        onApiResponseError();
    }

    //endrreegggion API Call

    //region special event
    private void handleSpecialEventResponse(EventResponse response) {
        specialEventData = response.getData();
        if ((specialEventData.getSelected() != null &&
                specialEventData.getSelected().getProducts() != null &&
                specialEventData.getSelected().getProducts().size() > 0) && (
                categoryAdapter.getCategories() != null &&
                        categoryAdapter.getCategories().size() > 0)) {

            setSpecialEventData(specialEventData);
        }
    }

    private void initSpecialEventComponent() {
        ivBgEvent = mView.findViewById(R.id.ivBgEvent);
        ivEventLogo = mView.findViewById(R.id.ivEventLogo);
        tvEventTitle = mView.findViewById(R.id.tvEventTitle);
        tvEventSubTitle = mView.findViewById(R.id.tvEventSubTitle);
        btnAll = mView.findViewById(R.id.btnAll);
    }

    private void initSpecialEventContent() {
        btnAll.setOnClickListener(v -> {
            if (categoryAdapter.getCategories() != null && categoryAdapter.getCategories().size() > 0) {
                Intent intent = new Intent(getContext(), CatalogActivity.class);
                intent.putExtra("category", new Gson().toJson(categories));
                intent.putExtra("isSpecialEvent", true);
                if (specialEventData.getEvent_background() != null && specialEventData.getEvent_background().getBackground_event() != null) {
                    intent.putExtra("background", specialEventData.getEvent_background().getBackground_event().getCatalog_background());
                    intent.putExtra("logo", specialEventData.getEvent_background().getBackground_event().getIcon());
                }

                startActivity(intent);
            }
        });
    }

    private void setSpecialEventData(EventData eventData) {
        if (getContext() != null) {
            if (eventData.getEvent_background() != null && eventData.getEvent_background().getBackground_event() != null) {
                Glide.with(ivBgEvent).load(eventData.getEvent_background().getBackground_event().getHomepage_background()).into(ivBgEvent);
                Glide.with(ivEventLogo).load(eventData.getEvent_background().getBackground_event().getIcon()).into(ivEventLogo);
                tvEventTitle.setText(eventData.getEvent_background().getBackground_event().getTitle());
                tvEventSubTitle.setText(eventData.getEvent_background().getBackground_event().getTitle_small());
            }

            if (SessionManager.getFeatureProduct() != null &&
                    SessionManager.getFeatureProduct().getToko_online() != null &&
                    SessionManager.getFeatureProduct().getToko_online().size() > 0
            ) {
                iclSpecialEvent.setVisibility(View.VISIBLE);
            }
            View parent = mView.findViewById(R.id.iclSpecialEvent);

            int itemWidth = DeviceUtil.dpToPx(100);
            int screenWidth = getScreenWidth();
            if (screenWidth != 0) {
                itemWidth = (screenWidth - 16 * 4) / 3;
            }

            View firstItemView = parent.findViewById(R.id.iclFirstItem);
            View secondItemView = parent.findViewById(R.id.iclSecondItem);
            View thirdItemView = parent.findViewById(R.id.iclThirdItem);
            setDefaultWidth(itemWidth, firstItemView, secondItemView, thirdItemView);

            for (int i = 0; i < eventData.getSelected().getProducts().size(); i++) {
                View view;
                if (i == 0) view = firstItemView;
                else if (i == 1) view = secondItemView;
                else view = thirdItemView;

                setSpecialEventProduct(view, eventData.getSelected().getProducts().get(i));
            }
        }
    }

    private void setDefaultWidth(int itemWidth, View firstItemView, View secondItemView, View thirdItemView) {
        ViewGroup.LayoutParams layoutParams = firstItemView.getLayoutParams();
        layoutParams.width = DeviceUtil.dpToPx(itemWidth);
        firstItemView.setLayoutParams(layoutParams);

        layoutParams = secondItemView.getLayoutParams();
        layoutParams.width = DeviceUtil.dpToPx(itemWidth);
        secondItemView.setLayoutParams(layoutParams);

        layoutParams = thirdItemView.getLayoutParams();
        layoutParams.width = DeviceUtil.dpToPx(itemWidth);
        thirdItemView.setLayoutParams(layoutParams);
    }

    private void setSpecialEventProduct(View itemParent, Product product) {
        itemParent.setVisibility(View.VISIBLE);

        TextView productName = itemParent.findViewById(R.id.tvName);
        TextView productDiscount = itemParent.findViewById(R.id.tvDiscount);
        TextView productPrice = itemParent.findViewById(R.id.tvPrice);
        TextView productOldPrice = itemParent.findViewById(R.id.tvOldPrice);
        ImageView productImage = itemParent.findViewById(R.id.ivProduct);
        View discountBadge = itemParent.findViewById(R.id.discountBadge);

        productName.setText(product.getTitle());
        Glide.with(productImage)
                .asBitmap()
                .placeholder(R.drawable.rounded)
                .error(R.drawable.rounded)
                .load(product.getMain_image_url())
                .into(new CustomGlideTarget(productImage, 8, R.drawable.rounded));

        if (product.getOttomart_discount_price() > 0 && product.getOttomart_discount_price() != product.getOttomart_price()) {
            productOldPrice.setText(DataUtil.convertCurrency(product.getOttomart_price()));
            productPrice.setText(DataUtil.convertCurrency(product.getOttomart_discount_price()));
            productDiscount.setText(product.getDiscount_percentage() + "%");
            productOldPrice.setVisibility(View.VISIBLE);
            discountBadge.setVisibility(View.VISIBLE);
        } else {
            productPrice.setText(DataUtil.convertCurrency(product.getOttomart_price()));
        }

        itemParent.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            startActivity(intent);
        });
    }

    //endregion

}