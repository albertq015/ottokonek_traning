package com.otto.mart.ui.fragment.ottopoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.otto.mart.ui.Partials.adapter.ottopoint.VoucherPoinRiwayatAdapter;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.activity.ottopoint.DetailVoucherActivity;
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

public class VoucherRiwayatPageFragment extends BaseFragment {

    private String TAG = VoucherRiwayatPageFragment.class.getSimpleName();

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.view_empty)
    View viewEmpty;
    @BindView(R.id.view_order)
    View viewOrder;
    @BindView(R.id.view_filter)
    View viewFilter;
    @BindView(R.id.view_list)
    View viewList;
    @BindView(R.id.view_refresh)
    SwipeRefreshLayout viewRefresh;

    private int page = 1;
    private String sort = "";
    private int pageSize = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private VoucherPoinRiwayatAdapter adapter;
    private List<VoucherPointItemModel> mItems = new ArrayList<>();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null) return;
            if(intent.getAction() == null) return;

            switch (intent.getAction()){
                case AppKeys.KEY_BROADCAST_REFRESH_PAGE_VOUCHER_SAYA_BOTH:
                    resetListItem(true);
                    break;
            }
        }
    };

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

    public static VoucherRiwayatPageFragment newInstance(Bundle args) {
        VoucherRiwayatPageFragment fragment = new VoucherRiwayatPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher_riwayat_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewOrder.setVisibility(View.VISIBLE);
        viewFilter.setVisibility(View.GONE);

        configureList();
        showList(true);

        callApi(true);

        viewRefresh.setOnRefreshListener(() -> resetListItem(false));
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

        if(adapter == null)
            adapter = new VoucherPoinRiwayatAdapter(getActivity(), mItems, this::moveToDetail);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
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

    private void updateList(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
        else
            configureList();
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
            data.setExpired(i % 2 != 0);
            mItems.add(data);
        }
    }

    private void showProgress(boolean isShow){
        if(getActivity() == null) return;

        isLoading = isShow;

        if(!isShow && viewRefresh != null) viewRefresh.setRefreshing(false);

        if(isShow)
            ProgressDialogComponent.showProgressDialog(getActivity(), "Loading", false).show();
        else
            ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
    }

    private void loadMoreItem(){
        callApi(true);
    }

    private void callApi(boolean isShowProgress){
        showProgress(isShowProgress);
        OttoPointDao.voucherSayaHistory((BaseActivity) getActivity(), page, sort, new HandleResponseApi() {
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

                    for (OpVoucherSayaResponseModel.Data.Campaigns item : result.getData().getCampaigns())
                        mItems.add(setItemData(item));

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
        DetailVoucherActivity.openPageRiwayat(getActivity(), data);
    }

    private void showList(boolean isShow){
        if(isShow){
            viewList.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.GONE);
        }else{
            viewList.setVisibility(View.GONE);
            viewEmpty.setVisibility(View.VISIBLE);
        }
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

    private VoucherPointItemModel setItemData(OpVoucherSayaResponseModel.Data.Campaigns target){
        VoucherPointItemModel result = new VoucherPointItemModel();
        String expiredate = "-";
        if(target.getActiveTo() != null && !target.getActiveTo().isEmpty())
            expiredate = DateUtil.getDateAsStringFormat(target.getActiveTo(), GLOBAL.FORMAT_DATE_TIME_DEFAULT_SERVER_TWO, GLOBAL.FORMAT_DATE_TIME_DATE_MONTH_TEXT_MIN);

        String usedDate = "-";
        if(target.getVoucher_time() != null && !target.getVoucher_time().isEmpty())
            usedDate = DateUtil.getDateAsStringFormat(target.getVoucher_time(), GLOBAL.FORMAT_DATE_TIME_DEFAULT_SERVER_TWO, GLOBAL.FORMAT_DATE_TIME_DATE_MONTH_TEXT_MIN);

        String urlBanner = CommonHelper.getUrlBannerVoucher(target.getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST);
        String urlBannerDetail = CommonHelper.getUrlBannerVoucher(target.getUrl_photo(), GLOBAL.BANNER_VOUCHER_LIST);

        result.setId(target.getCampaignId());
        result.setUrlBanner(urlBanner);
        result.setUrlBannerDetail(urlBannerDetail);
        result.setUrlCompanyPic(target.getUrl_logo() != null ? target.getUrl_logo() : "");
        result.setCompanyName(target.getBrand_name());
        result.setTitle(target.getName());
        result.setExpireDate(expiredate);
        result.setUsedDate(usedDate);

        return result;
    }
}
