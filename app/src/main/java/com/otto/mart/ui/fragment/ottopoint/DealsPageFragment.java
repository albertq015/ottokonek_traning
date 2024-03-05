package com.otto.mart.ui.fragment.ottopoint;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherDealsDetailResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherDealsResponseModel;
import com.otto.mart.model.localmodel.ui.ottopoint.DealsItemModel;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.support.util.MessageHelper;
import com.otto.mart.ui.Partials.adapter.ottopoint.DealsAdapter;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.activity.ottopoint.DealsMainActivtiy;
import com.otto.mart.ui.activity.ottopoint.DetailDealsActivity;
import com.otto.mart.ui.component.dialog.FilterListDialog;
import com.otto.mart.ui.component.dialog.OrderListDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseFragment;
import app.beelabs.com.codebase.base.response.BaseResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class DealsPageFragment extends BaseFragment {

    private String TAG = DealsPageFragment.class.getSimpleName();

    private static final String KEY_PAGE_ID = "key_id_page";
    private static final String KEY_PAGE_TYPE = "key_voucher_type";

    @BindView(R.id.view_location)
    View viewLocation;
    @BindView(R.id.tv_current_location)
    TextView tvCurrentLocation;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.view_container_list)
    NestedScrollView viewContainerList;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.view_order)
    View viewOrder;
    @BindView(R.id.view_filter)
    View viewFilter;
    @BindView(R.id.view_refresh)
    SwipeRefreshLayout viewRefresh;

    private int page = 1;
    private String sort = "";
    private String campaignName = "";
    private String campaignType = "";
    private boolean isPromo = false;
    private int totalPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private long minHarga = 0;
    private long maxHarga = 0;
    private long maxHargaFilter = 0;

    private int selectedCampainTypeId = 0;
    private boolean isFirstLoad = true;
    private boolean isMoveToDetail = false;
    private boolean isPause = false;

    private Bundle arguments;
    private DealsAdapter adapter;
    private List<DealsItemModel> mItems = new ArrayList<>();

    public static DealsPageFragment newInstance(int id, String typeVoucher) {
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE_ID, id);
        args.putString(KEY_PAGE_TYPE, typeVoucher);

        DealsPageFragment fragment = new DealsPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arguments = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deals_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isMoveToDetail)
            isMoveToDetail = false;

        // new 23-01-2020
        //if(isPause){
        //    isPause = false;
        //    callApi(true);
        //}
    }

    @Override
    public void onPause() {
        if(!isMoveToDetail)
            actionViewResetPage();

        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set views
        viewOrder.setVisibility(View.VISIBLE);
        viewFilter.setVisibility(View.VISIBLE);

        setViewByPageId(getPageId());

        configureList();

        // load api
        if(isFirstLoad){
            isFirstLoad = false;

            callApi(true);
        }

        // events

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                actionSearch(edtSearch.getText().toString());
                return true;
            }
            return false;
        });

        viewLocation.setOnClickListener(v -> MessageHelper.underConstructionMessage(getActivity()));

        viewRefresh.setOnRefreshListener(() -> resetListItem(false));
    }

    @OnClick(R.id.view_order)
    public void actionOrder(){
        OrderListDialog.showDialog(getActivity(), OrderListDialog.TYPE_DEALS, sort, (position, text) -> {
            setSort(text);

            resetListItem(true);
        });
    }

    @OnClick(R.id.view_filter)
    public void actionFilter(){
        FilterListDialog.showDialog(getActivity(), selectedCampainTypeId, this.minHarga, this.maxHarga, this.maxHargaFilter, (idLokasi, lokasi, idPenawaran, penawaran, minHarga, maxHarga) -> {
            this.selectedCampainTypeId = idPenawaran;
            this.minHarga = minHarga;
            this.maxHarga = maxHarga;

            setCampaignType(penawaran);

            resetListItem(true);
        });
    }

    private void setViewByPageId(int id){
        switch (id){
            case DealsMainActivtiy.CODE_PAGE_ALL:
                viewLocation.setVisibility(View.GONE);
                break;

            case DealsMainActivtiy.CODE_PAGE_NEAR:
                viewLocation.setVisibility(View.VISIBLE);

                //tvCurrentLocation.setText(getLocationAddress());
                break;

            case DealsMainActivtiy.CODE_PAGE_OTHERS:
                viewLocation.setVisibility(View.GONE);
                break;

            default:
                viewLocation.setVisibility(View.GONE);
                break;
        }
    }

    private void configureList(){
        if(adapter == null)
            adapter = new DealsAdapter(getActivity(), mItems, (position, data) -> {
                if(data.containsKey("id")){
                    isMoveToDetail = true;

                    DetailDealsActivity.showPage(getActivity(), data.getString("id"), mItems.get(position));
                }
            });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);

        list.setNestedScrollingEnabled(false);
        viewContainerList.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = viewContainerList.getChildAt(viewContainerList.getChildCount() - 1);

            int diff = (view.getBottom() - (viewContainerList.getHeight() + viewContainerList.getScrollY()));

            if (!isLoading && !isLastPage)
                if (diff == 0)
                    loadMoreItem();
        });
    }

    private void refreshList(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
        else
            configureList();
    }

    private void actionSearch(String valueSearch){
        // hide keyboard
        if(getActivity() != null)
            CommonHelper.hideKeyboard(getActivity());

        // do action in here
        setCampaignName(valueSearch);

        resetListItem(true);
    }

    private int getPageId(){
        if(arguments != null && arguments.containsKey(KEY_PAGE_ID))
            return arguments.getInt(KEY_PAGE_ID);
        return -1;
    }

    private String getPageVoucherType(){
        if(arguments != null && arguments.containsKey(KEY_PAGE_TYPE))
            return arguments.getString(KEY_PAGE_TYPE);
        return "";
    }

    @Nullable
    private DealsMainActivtiy getActivityMain(){
        return (DealsMainActivtiy) getActivity();
    }

    private void showProgress(boolean isShow){
        if(getActivityMain() == null) return;

        isLoading = isShow;

        if(!isShow && viewRefresh != null) viewRefresh.setRefreshing(false);

        getActivityMain().showProgress(getActivity(), isShow);
    }

    private String getLocationAddress(){
        if(getActivity() == null) return "";

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        String result = "";
        try {
            addresses = geocoder.getFromLocation(OttoMartApp.getCoordinate().latitude, OttoMartApp.getCoordinate().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if(addresses != null && !addresses.isEmpty()){
                String address = addresses.get(0).getMaxAddressLineIndex() > 0 ? addresses.get(0).getAddressLine(0) : ""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                result += country + ", " + city + ", " + address;
            }
        } catch (IOException e) {
            //e.printStackTrace();
            LogHelper.showError(TAG, e.getMessage());
        }

        return result;
    }

    private boolean checkActivity(){
        if(getActivity() == null){
            isPause = true;

            return false;
        }else{
            if(isPause) isPause = false;

            return true;
        }
    }

    private void callApi(boolean isShowProgress){
        showProgress(isShowProgress);
        OttoPointDao.voucherDeals((BaseActivity) getActivity(), page, sort, campaignName, campaignType, isPromo, minHarga, maxHarga, isUseVoucherDealsPromo(), new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                showProgress(false);

                // new 23-01-2020
                // lakukan pengecekan jika halaman terkait sedang diminimize
                // dan ada aksi yang dijalankan untuk ui
                //if(!checkActivity()) return;

                if(br instanceof OpVoucherDealsResponseModel){
                    OpVoucherDealsResponseModel result = (OpVoucherDealsResponseModel) br;

                    if(result.getData() == null) return;

                    if(maxHargaFilter == 0) maxHargaFilter = result.getData().getMaximumCostInPoints();

                    setActionPagination(result.getData().getHalaman());

                    for (OpVoucherDealsDetailResponseModel.Data item : result.getData().getCampaigns()) {
                        if(!item.isActive()) continue;

                        mItems.add(new DealsItemModel(
                            item.getCampaignId(),
                            CommonHelper.getUrlBannerVoucher(item.getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST),
                            CommonHelper.getUrlBannerVoucher(item.getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST),
                            item.getUrl_logo() != null ? item.getUrl_logo() : "",
                            item.getBrandName(),
                            item.getName(),
                            CommonHelper.currencyFormat(item.getCostInPoints()) + " poin",
                            item.getCostInPoints()
                        ));
                    }

                    refreshList();
                }
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                showProgress(false);

                LogHelper.showError(TAG, "failled get data, message: " + message);
            }
        });
    }

    public void actionViewResetPage(){
        if(viewContainerList != null) viewContainerList.fullScroll(NestedScrollView.FOCUS_UP);
        if(edtSearch != null) edtSearch.setText("");
    }

    public void resetPage(){
        page = 1;
        totalPage = 1;
        isLastPage = false;

        sort = "";
        campaignName = "";
        campaignType = "";
        isPromo = false;
        minHarga = 0;
        maxHarga = 0;

        actionViewResetPage();
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void resetListItem(boolean isShowProgress){
        page = 1;
        totalPage = 1;
        isLastPage = false;

        callApi(isShowProgress);
    }

    private void loadMoreItem(){
        callApi(true);
    }

    private void setActionPagination(int totalPage){
        this.totalPage = totalPage;

        if(page == 1) mItems.clear();

        if(page == this.totalPage)
            isLastPage = true;
        else
            page++;
    }

    private boolean isUseVoucherDealsPromo(){
        return !getPageVoucherType().isEmpty() && getPageVoucherType().equals(GLOBAL.VOUCHER_TYPE_PROMO_TERBARU);
    }
}
