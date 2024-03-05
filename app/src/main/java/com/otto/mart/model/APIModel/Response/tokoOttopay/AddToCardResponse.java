package com.otto.mart.model.APIModel.Response.tokoOttopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.Cart;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddToCardResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data {
        private List<CartData> cart;

        public List<CartData> getCart() {
            return cart;
        }

        public void setCart(List<CartData> cart) {
            this.cart = cart;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CartData {
        private int cart_id;
        private String supplier_name;
        private String logo;
        private String sub_total;
        private int total_item;
        private List<Cart> cart_items;

        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
        }

        public String getSupplier_name() {
            return supplier_name;
        }

        public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getSub_total() {
            return sub_total;
        }

        public void setSub_total(String sub_total) {
            this.sub_total = sub_total;
        }

        public int getTotal_item() {
            return total_item;
        }

        public void setTotal_item(int total_item) {
            this.total_item = total_item;
        }

        public List<Cart> getCart_items() {
            return cart_items;
        }

        public void setCart_items(List<Cart> cart_items) {
            this.cart_items = cart_items;
        }
    }

}