package com.otto.mart.ui.activity.tokoOttopay;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ViewAnimator;

import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.OrderHistory;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderHistoryResponse;
import com.otto.mart.presenter.dao.TokoOttopayDao;
import com.otto.mart.support.util.widget.recyclerView.ItemClickSupport;
import com.otto.mart.ui.Partials.adapter.tokoOttopay.OrderHistoryAdapter;
import com.otto.mart.ui.activity.AppActivity;

import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_ORDER_HISTORY;

public class OrderHistoryActivity extends AppActivity {

    private LinearLayout btnBack;
    private ViewAnimator viewAnimator;
    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        initView();
        initRecyclerView();
        getOrderHistory();
    }


    private void initView() {
        btnBack = findViewById(R.id.btn_back);
        viewAnimator = findViewById(R.id.view_animator);
        recyclerView = findViewById(R.id.recyclerview);
        emptyLayout = findViewById(R.id.empty_layout);

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void displayOrderHistory(List<OrderHistory> orderHistoryList) {
        if (orderHistoryList.size() > 0) {
            OrderHistoryAdapter adapter = new OrderHistoryAdapter(this, orderHistoryList);
            recyclerView.setAdapter(adapter);

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    orderHistorySelected(orderHistoryList.get(position));
                }
            });

            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

        viewAnimator.setDisplayedChild(1);
    }

    private void orderHistorySelected(OrderHistory orderHistory) {
        Intent intent = new Intent(this, OrderHistoryDetailActivity.class);
        intent.putExtra(AppKeys.KEY_ORDER_NUMBER, orderHistory.getOrder_number());
        startActivity(intent);
    }


    //region Api Request

    private void getOrderHistory() {
        new TokoOttopayDao(this).getOrderHistory(BaseDao.getInstance(this, API_KEY_ORDER_HISTORY).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case API_KEY_ORDER_HISTORY:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        displayOrderHistory(((OrderHistoryResponse) br).getData());
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    //endregion Api Request
}