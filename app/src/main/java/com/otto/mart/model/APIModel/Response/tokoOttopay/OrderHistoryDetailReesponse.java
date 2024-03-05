package com.otto.mart.model.APIModel.Response.tokoOttopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderHistoryDetailReesponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private Order order;

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Order {
            private int id;
            private String order_number;
            private String total_amount;
            private String status;
            private String payment_status;
            private String created_at;
            private Invoice invoice;
            private List<Items> items;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrder_number() {
                return order_number;
            }

            public void setOrder_number(String order_number) {
                this.order_number = order_number;
            }

            public String getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(String total_amount) {
                this.total_amount = total_amount;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getPayment_status() {
                return payment_status;
            }

            public void setPayment_status(String payment_status) {
                this.payment_status = payment_status;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public Invoice getInvoice() {
                return invoice;
            }

            public void setInvoice(Invoice invoice) {
                this.invoice = invoice;
            }

            public List<Items> getItems() {
                return items;
            }

            public void setItems(List<Items> items) {
                this.items = items;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Invoice {
                private int id;
                private String invoice_number;
                private String down_payment;
                private String remaining_payment;
                private String total_payment;
                private String payment_reference_number;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getInvoice_number() {
                    return invoice_number;
                }

                public void setInvoice_number(String invoice_number) {
                    this.invoice_number = invoice_number;
                }

                public String getDown_payment() {
                    return down_payment;
                }

                public void setDown_payment(String down_payment) {
                    this.down_payment = down_payment;
                }

                public String getRemaining_payment() {
                    return remaining_payment;
                }

                public void setRemaining_payment(String remaining_payment) {
                    this.remaining_payment = remaining_payment;
                }

                public String getTotal_payment() {
                    return total_payment;
                }

                public void setTotal_payment(String total_payment) {
                    this.total_payment = total_payment;
                }

                public String getPayment_reference_number() {
                    return payment_reference_number;
                }

                public void setPayment_reference_number(String payment_reference_number) {
                    this.payment_reference_number = payment_reference_number;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Items {
                private int cart_item_id;
                private int product_id;
                private String sku;
                private String name;
                private int quantity;
                private String item_price;
                private String total_price;
                private String image;

                public int getCart_item_id() {
                    return cart_item_id;
                }

                public void setCart_item_id(int cart_item_id) {
                    this.cart_item_id = cart_item_id;
                }

                public int getProduct_id() {
                    return product_id;
                }

                public void setProduct_id(int product_id) {
                    this.product_id = product_id;
                }

                public String getSku() {
                    return sku;
                }

                public void setSku(String sku) {
                    this.sku = sku;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getQuantity() {
                    return quantity;
                }

                public void setQuantity(int quantity) {
                    this.quantity = quantity;
                }

                public String getItem_price() {
                    return item_price;
                }

                public void setItem_price(String item_price) {
                    this.item_price = item_price;
                }

                public String getTotal_price() {
                    return total_price;
                }

                public void setTotal_price(String total_price) {
                    this.total_price = total_price;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }
            }
        }
    }
}
