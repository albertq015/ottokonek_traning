package com.otto.mart.ui.fragment.ottopoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Response.ottopoint.OpVoucherSayaResponseModel;
import com.otto.mart.model.localmodel.ui.ottopoint.VoucherPointItemModel;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.support.util.DateUtil;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.ui.Partials.adapter.ottopoint.VoucherPoinAktifAdapter;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.activity.ottopoint.DetailVoucherActivity;
import com.otto.mart.ui.activity.ottopoint.VoucherSayaMainActivity;
import com.otto.mart.ui.component.dialog.ListVoucherExDialog;
import com.otto.mart.ui.component.dialog.OrderListDialog;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseFragment;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class VoucherAktifPageFragment extends BaseFragment {

    private String TAG = VoucherAktifPageFragment.class.getSimpleName();

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.view_empty)
    View viewEmpty;
    @BindView(R.id.view_list)
    View viewList;
    @BindView(R.id.view_order)
    View viewOrder;
    @BindView(R.id.view_filter)
    View viewFilter;
    @BindView(R.id.view_refresh)
    SwipeRefreshLayout viewRefresh;

    private VoucherPoinAktifAdapter adapter;
    private List<VoucherPointItemModel> mItems = new ArrayList<>();

    private int maxItems = 10;
    private int page = 1;
    private String sort = "";
    private int pageSize = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null) return;
            if(intent.getAction() == null) return;

            switch (intent.getAction()){
                case AppKeys.KEY_BROADCAST_REFRESH_PAGE_VOUCHER_SAYA_BOTH:
                    resetListItem(true);

                    new Handler().postDelayed(() -> {
                        if(getActivity() != null)
                            if(getActivity() instanceof VoucherSayaMainActivity)
                                ((VoucherSayaMainActivity) getActivity()).setSelectedTab(1);
                    }, 1000);
                    break;
            }
        }
    };

    public static VoucherAktifPageFragment newInstance(Bundle args) {
        VoucherAktifPageFragment fragment = new VoucherAktifPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addReceiver();
    }

    @Override
    public void onDestroy() {
        removeReceiver();

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher_aktif_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewOrder.setVisibility(View.VISIBLE);
        viewFilter.setVisibility(View.GONE);

        showList(true);

        configureList();

        callApi(true);

        viewRefresh.setOnRefreshListener(() -> {
            // hide pull up loader
            new Handler().postDelayed(() -> viewRefresh.setRefreshing(false), 250);

            resetListItem(true);
        });
    }

    @OnClick(R.id.view_order)
    public void actionOrder(){
        OrderListDialog.showDialog(getActivity(), OrderListDialog.TYPE_VOUCHER_SAYA, sort, (position, text) -> {
            setSort(text);

            resetListItem(true);
        });
    }

    private void addReceiver(){
        if(getActivity() == null) return;

        IntentFilter intentFilter = new IntentFilter();
        if(!intentFilter.hasAction(AppKeys.KEY_BROADCAST_REFRESH_PAGE_VOUCHER_SAYA_BOTH))
            intentFilter.addAction(AppKeys.KEY_BROADCAST_REFRESH_PAGE_VOUCHER_SAYA_BOTH);

        getActivity().registerReceiver(receiver, intentFilter);
    }

    private void removeReceiver(){
        if(getActivity() == null) return;
        if(receiver == null) return;

        getActivity().unregisterReceiver(receiver);
    }

    private void configureList(){
        if(getActivity() == null) return;
        if(list == null) return;

        list.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);

        adapter = new VoucherPoinAktifAdapter(getActivity(), mItems, ((position, data) -> {
            if(mItems.get(position).getChild().size() > 0) {
                if(getActivity() == null) return;

                List<VoucherPointItemModel> mItemsChild = new ArrayList<>(mItems.get(position).getChild());

                // add current item
                mItemsChild.add(mItems.get(position));

                for (VoucherPointItemModel item: mItemsChild)
                    item.setJumlahChild(mItemsChild.size());

                ListVoucherExDialog.showDialog(getActivity(), mItemsChild);
            }else
                moveToDetail(data);
        }));
        list.setAdapter(adapter);
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= pageSize) {
                        loadMoreItem();
                    }
                }
            }
        });
    }

    private void loadMoreItem(){
        callApi(true);
    }

    private void setDummyData(){
        for (int i = 0; i < 10; i++) {
            VoucherPointItemModel data = new VoucherPointItemModel();
            data.setId(Integer.toString(i));
            data.setUrlBanner("");
            data.setBanner(null);
            data.setUrlCompanyPic("");
            data.setCompanyPic(null);
            data.setCompanyName("Nama perusahaan " + i);
            data.setTitle("Judul " + i);
            data.setExpireDate( (i + 1) + " Apr 2019");
            mItems.add(data);
        }
    }

    private void showProgress(boolean isShow){
        isLoading = isShow;

        if(!isShow && viewRefresh != null) viewRefresh.setRefreshing(false);

        if(isShow)
            ProgressDialogComponent.showProgressDialog(getActivity(), "Loading", false).show();
        else
            ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
    }

    private void callApi(boolean isShowProgress){
        showProgress(isShowProgress);
        OttoPointDao.voucherSayaActive((BaseActivity) getActivity(), page, sort, new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                if(br instanceof OpVoucherSayaResponseModel){
                    OpVoucherSayaResponseModel result = (OpVoucherSayaResponseModel) br;

                    if(result.getData() == null) {
                        setParamReset();

                        if(page == 1) mItems.clear();

                        responseApiListResponse();
                        return;
                    }

                    setActionPagination(result.getData().getHalaman());

                    for (OpVoucherSayaResponseModel.Data.Campaigns item : result.getData().getCampaigns()) {
                        if(checkIfItemIsSame(item)) continue;

                        if(item.getCampaignId() != null && !item.getCampaignId().isEmpty())
                            mItems.add(setItemData(item));
                    }

                    // load more items if items is less then 10
                    if(mItems.size() < maxItems && !isLastPage){
                        loadMoreItem();
                        return;
                    }

                    responseApiListResponse();
                }
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                // original
                //showProgress(false);
                //showList(false);

                // temporary
                responseApiListResponse();

                LogHelper.showError(TAG, message);
            }
        });
    }

    private void responseApiListResponse(){
        showProgress(false);

        if(mItems.isEmpty())
            showList(false);
        else{
            showList(true);

            updateList();
        }
    }

    private void moveToDetail(Bundle data){
        if(getActivity() == null) return;

        DetailVoucherActivity.openPageActive(getActivity(), data);
    }

    private void showList(boolean isShow){
        if(isShow){
            if(viewList != null) viewList.setVisibility(View.VISIBLE);
            if(viewEmpty != null) viewEmpty.setVisibility(View.GONE);
        }else{
            if(viewList != null) viewList.setVisibility(View.GONE);
            if(viewEmpty != null) viewEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void updateList(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
        else
            configureList();
    }

    public void resetListItem(boolean isShowProgress){
        page = 1;

        setParamReset();

        callApi(isShowProgress);
    }

    private void setParamReset(){
        pageSize = 1;
        isLastPage = false;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    private void setActionPagination(int totalPage){
        this.pageSize = totalPage;

        if(!mItems.isEmpty() && page == 1) mItems.clear();

        if(page == this.pageSize)
            isLastPage = true;
        else
            page++;
    }

    private boolean checkIfItemIsSame(OpVoucherSayaResponseModel.Data.Campaigns target){
        boolean result = false;

        if(mItems.isEmpty()) return result;

        for (int i = 0; i < mItems.size(); i++) {
            if(target.getCampaignId().equals(mItems.get(i).getId()) && !target.getCoupon().getId().equals(mItems.get(i).getCouponId())){
                mItems.get(i).addChild(setItemData(target));
                result = true;
                break;
            }
        }

        return result;
    }

    private VoucherPointItemModel setItemData(OpVoucherSayaResponseModel.Data.Campaigns target){
        VoucherPointItemModel result = new VoucherPointItemModel();
        String expiredate = "-";
        if(target.getActiveTo() != null && !target.getActiveTo().isEmpty())
            expiredate = DateUtil.getDateAsStringFormat(target.getActiveTo(), GLOBAL.FORMAT_DATE_TIME_DEFAULT_SERVER_TWO, GLOBAL.FORMAT_DATE_TIME_DATE_MONTH_TEXT_MIN);

        String urlBanner = CommonHelper.getUrlBannerVoucher(target.getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST);
        String urlBannerDetail = CommonHelper.getUrlBannerVoucher(target.getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST);

        result.setId(target.getCampaignId());
        result.setUrlBanner(urlBanner);
        result.setUrlBannerDetail(urlBannerDetail);
        result.setUrlCompanyPic(target.getUrl_logo() != null ? target.getUrl_logo() : "");
        result.setCompanyName(target.getBrand_name());
        result.setTitle(target.getName());
        result.setExpireDate(expiredate);
        result.setCouponId(target.getCoupon().getId());

        return result;
    }
}
