package com.otto.mart.model.APIModel.Response.olshop.order.status;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class OrderStatusResponseModel extends BaseResponseModel {

    private OrderStatusData data;

    public OrderStatusData getData() {
        return data;
    }

    public void setData(OrderStatusData data) {
        this.data = data;
    }

    public class OrderStatusData {
        private List<OrderStatus> orders;

        public List<OrderStatus> getOrders() {
            return orders;
        }

        public void setOrders(List<OrderStatus> orders) {
            this.orders = orders;
        }
    }
}
