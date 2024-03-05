package com.otto.mart.ui.activity.tokoOttopay;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderHistoryDetailRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderHistoryDetailReesponse;
import com.otto.mart.presenter.dao.TokoOttopayDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.Partials.adapter.tokoOttopay.OrderHistoryDetailAdapter;
import com.otto.mart.ui.activity.AppActivity;

import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_ORDER_HISTORY_DETAIL;

public class OrderHistoryDetailActivity extends AppActivity {

    private LinearLayout btnBack;
    private ViewAnimator viewAnimator;
    private TextView tvDate;
    private TextView tvNoOrder;
    private TextView tvTotal;
    private TextView tvOrderStatus;
    private TextView tvPaymentStatus;
    private RecyclerView recyclerView;

    private String mOrderNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);

        if (getIntent().getStringExtra(AppKeys.KEY_ORDER_NUMBER) != null) {
            mOrderNumber = getIntent().getStringExtra(AppKeys.KEY_ORDER_NUMBER);
        }

        initView();
        initRecyclerView();
        getOrderHistoryDetail();
    }

    private void initView() {
        btnBack = findViewById(R.id.btn_back);
        viewAnimator = findViewById(R.id.view_animator);
        tvDate = findViewById(R.id.tv_date);
        tvNoOrder = findViewById(R.id.tv_no_order);
        tvTotal = findViewById(R.id.tv_total);
        tvOrderStatus = findViewById(R.id.tv_order_status);
        tvPaymentStatus = findViewById(R.id.tv_payment_status);
        recyclerView = findViewById(R.id.recyclerview);

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

    private void displayOrderHistoryDetail(OrderHistoryDetailReesponse.Data data) {
        double totalAmount = Double.valueOf(data.getOrder().getTotal_amount());

        tvDate.setText(data.getOrder().getCreated_at());
        tvNoOrder.setText(data.getOrder().getOrder_number());
        tvTotal.setText(DataUtil.convertCurrency(totalAmount));
        tvOrderStatus.setText(data.getOrder().getStatus());
        tvPaymentStatus.setText(data.getOrder().getPayment_status());

        displayOrderList(data.getOrder().getItems());

        viewAnimator.setDisplayedChild(1);
    }

    private void displayOrderList(List<OrderHistoryDetailReesponse.Data.Order.Items> items) {
        OrderHistoryDetailAdapter adapter = new OrderHistoryDetailAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    //region Api Request

    private void getOrderHistoryDetail() {
        OrderHistoryDetailRequest orderHistoryDetailRequest = new OrderHistoryDetailRequest();
        orderHistoryDetailRequest.setOrder_number(mOrderNumber);

        new TokoOttopayDao(this).getOrderHistoryDetail(orderHistoryDetailRequest, BaseDao.getInstance(this, API_KEY_ORDER_HISTORY_DETAIL).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case API_KEY_ORDER_HISTORY_DETAIL:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        displayOrderHistoryDetail(((OrderHistoryDetailReesponse) br).getData());
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
