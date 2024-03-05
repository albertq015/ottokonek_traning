package com.otto.mart.ui.fragment.olshop;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.OlshopInformationResponse;
import com.otto.mart.model.APIModel.Response.olshop.order.status.OrderStatus;
import com.otto.mart.model.APIModel.Response.olshop.order.status.OrderStatusResponseModel;
import com.otto.mart.presenter.dao.olshop.OlshopDao;
import com.otto.mart.support.util.InfiniteScroll;
import com.otto.mart.ui.Partials.adapter.olshop.OrderStatusAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.BaseFragment;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class OrderStatusFragment extends BaseFragment {

    private final static String KEY_IS_DONE = "process";

    private RecyclerView orderStatusList;
    private SwipeRefreshLayout refreshLayout;
    private List<OrderStatus> statusList;
    private OrderStatusAdapter adapter;
    private int page = 1;
    private LinearLayoutManager layoutManager;
    private TextView tvInfo;

    public static OrderStatusFragment newInstance(boolean isDone) {

        Bundle args = new Bundle();
        args.putBoolean(KEY_IS_DONE, isDone);
        OrderStatusFragment fragment = new OrderStatusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_status, container, false);

        Map<String, String> params = new HashMap<>();
        params.put("limit", "10");
        params.put("page", String.valueOf(page));
        if (getArguments() != null && getArguments().getBoolean(KEY_IS_DONE)) {
            params.put("status", "done");
        }

        refreshLayout = view.findViewById(R.id.swipe);
        refreshLayout.setOnRefreshListener(() -> {
            page = 1;
            statusList.clear();
            callOrderStatusAPI(params);
        });
        statusList = new ArrayList<>();
        adapter = new OrderStatusAdapter(statusList);
        orderStatusList = view.findViewById(R.id.orderStatusList);
        tvInfo = view.findViewById(R.id.tvInfo);
        layoutManager = new LinearLayoutManager(getContext());
        orderStatusList.setLayoutManager(layoutManager);
        orderStatusList.setAdapter(adapter);
        orderStatusList.addOnScrollListener(new InfiniteScroll(layoutManager, refreshLayout) {
            @Override
            public void loadNewItem() {
                ++page;
                callOrderStatusAPI(params);
            }
        });

        callOrderStatusAPI(params);
        callInformationAPI();
        return view;
    }

    private void callOrderStatusAPI(Map<String, String> params) {
        params.put("page", String.valueOf(page));
        if (page == 1) {
            ProgressDialogComponent.showProgressDialog(getContext(), getString(R.string.loading_message), false);
        } else refreshLayout.setRefreshing(true);
        new OlshopDao(this).orderStatus(params, BaseDao.getInstance(this, 1).callback);
    }

    private void callInformationAPI() {
        if (getArguments() != null && !getArguments().getBoolean(KEY_IS_DONE)) {
            new OlshopDao(this).getInfo(BaseDao.getInstance(this, 213).callback);
        }
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        refreshLayout.setRefreshing(false);
        if (br != null) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                if (responseCode == 1) {
                    handlingOrderStatusResponse((OrderStatusResponseModel) br);
                } else if (responseCode == 213) {
                    handlingInfoResponse((OlshopInformationResponse) br);
                }
            }
        }
    }

    private void handlingOrderStatusResponse(OrderStatusResponseModel response) {
        statusList.addAll(response.getData().getOrders());
        adapter.notifyDataSetChanged();
    }

    private void handlingInfoResponse(OlshopInformationResponse response) {
        if (response.getData() != null && response.getData().getStatus() != null) {
            if (response.getData().getStatus().equalsIgnoreCase("Active")) {

                tvInfo.setText(response.getData().getTitle());
                tvInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message) {
//        super.onApiFailureCallback(message);
        refreshLayout.setRefreshing(false);
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
    }
}
