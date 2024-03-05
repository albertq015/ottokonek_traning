package id.ottodigital.data.entity.idm.response;

import java.util.List;

import id.ottodigital.data.entity.base.BaseResponse;

public class OrderHistoriesResponseModel extends BaseResponse {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<OrdersBean> orders;

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public static class OrdersBean {

            private int id;
            private String no_invoice;
            private String date;
            private Object phone;
            private Object recipient_name;
            private String status;
            private long total_price;
            private SalesmanBean salesman;
            private List<OrderItemsBean> order_items;
            private int cart_id;

            public int getCart_id() {
                return cart_id;
            }

            public void setCart_id(int cart_id) {
                this.cart_id = cart_id;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNo_invoice() {
                return no_invoice;
            }

            public void setNo_invoice(String no_invoice) {
                this.no_invoice = no_invoice;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public Object getPhone() {
                return phone;
            }

            public void setPhone(Object phone) {
                this.phone = phone;
            }

            public Object getRecipient_name() {
                return recipient_name;
            }

            public void setRecipient_name(Object recipient_name) {
                this.recipient_name = recipient_name;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public long getTotal_price() {
                return total_price;
            }

            public void setTotal_price(long total_price) {
                this.total_price = total_price;
            }

            public SalesmanBean getSalesman() {
                return salesman;
            }

            public void setSalesman(SalesmanBean salesman) {
                this.salesman = salesman;
            }

            public List<OrderItemsBean> getOrder_items() {
                return order_items;
            }

            public void setOrder_items(List<OrderItemsBean> order_items) {
                this.order_items = order_items;
            }

            public static class SalesmanBean {

                private int id;
                private String name;
                private int division_id;
                private String created_at;
                private String updated_at;
                private String sales_code;
                private int seq_id;
                private String location_code;
                private String inactive;
                private String item_division;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getDivision_id() {
                    return division_id;
                }

                public void setDivision_id(int division_id) {
                    this.division_id = division_id;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }

                public String getSales_code() {
                    return sales_code;
                }

                public void setSales_code(String sales_code) {
                    this.sales_code = sales_code;
                }

                public int getSeq_id() {
                    return seq_id;
                }

                public void setSeq_id(int seq_id) {
                    this.seq_id = seq_id;
                }

                public String getLocation_code() {
                    return location_code;
                }

                public void setLocation_code(String location_code) {
                    this.location_code = location_code;
                }

                public String getInactive() {
                    return inactive;
                }

                public void setInactive(String inactive) {
                    this.inactive = inactive;
                }

                public String getItem_division() {
                    return item_division;
                }

                public void setItem_division(String item_division) {
                    this.item_division = item_division;
                }
            }

            public static class OrderItemsBean {

                private int id;
                private int product_id;
                private double amount;
                private String date;
                private int discount_amount;
                private String item_type;
                private Object no_invoice;
                private String price_type;
                private int qty;
                private String status;
                private int voucher_amount;
                private int credit_limit_id;
                private int qty_b;
                private int net_amount;
                private int gross_amount;
                private int convert_to;
                private String uom;
                private String uom_b;
                private ProductBean product;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getProduct_id() {
                    return product_id;
                }

                public void setProduct_id(int product_id) {
                    this.product_id = product_id;
                }

                public double getAmount() {
                    return amount;
                }

                public void setAmount(double amount) {
                    this.amount = amount;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public int getDiscount_amount() {
                    return discount_amount;
                }

                public void setDiscount_amount(int discount_amount) {
                    this.discount_amount = discount_amount;
                }

                public String getItem_type() {
                    return item_type;
                }

                public void setItem_type(String item_type) {
                    this.item_type = item_type;
                }

                public Object getNo_invoice() {
                    return no_invoice;
                }

                public void setNo_invoice(Object no_invoice) {
                    this.no_invoice = no_invoice;
                }

                public String getPrice_type() {
                    return price_type;
                }

                public void setPrice_type(String price_type) {
                    this.price_type = price_type;
                }

                public int getQty() {
                    return qty;
                }

                public void setQty(int qty) {
                    this.qty = qty;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public int getVoucher_amount() {
                    return voucher_amount;
                }

                public void setVoucher_amount(int voucher_amount) {
                    this.voucher_amount = voucher_amount;
                }

                public int getCredit_limit_id() {
                    return credit_limit_id;
                }

                public void setCredit_limit_id(int credit_limit_id) {
                    this.credit_limit_id = credit_limit_id;
                }

                public int getQty_b() {
                    return qty_b;
                }

                public void setQty_b(int qty_b) {
                    this.qty_b = qty_b;
                }

                public int getNet_amount() {
                    return net_amount;
                }

                public void setNet_amount(int net_amount) {
                    this.net_amount = net_amount;
                }

                public int getGross_amount() {
                    return gross_amount;
                }

                public void setGross_amount(int gross_amount) {
                    this.gross_amount = gross_amount;
                }

                public int getConvert_to() {
                    return convert_to;
                }

                public void setConvert_to(int convert_to) {
                    this.convert_to = convert_to;
                }

                public String getUom() {
                    return uom;
                }

                public void setUom(String uom) {
                    this.uom = uom;
                }

                public String getUom_b() {
                    return uom_b;
                }

                public void setUom_b(String uom_b) {
                    this.uom_b = uom_b;
                }

                public ProductBean getProduct() {
                    return product;
                }

                public void setProduct(ProductBean product) {
                    this.product = product;
                }

                public static class ProductBean {

                    private int id;
                    private String active_status;
                    private String description;
                    private String sub_brand;
                    private String title;
                    private List<ImagesBean> images;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getActive_status() {
                        return active_status;
                    }

                    public void setActive_status(String active_status) {
                        this.active_status = active_status;
                    }

                    public String getDescription() {
                        return description;
                    }

                    public void setDescription(String description) {
                        this.description = description;
                    }

                    public String getSub_brand() {
                        return sub_brand;
                    }

                    public void setSub_brand(String sub_brand) {
                        this.sub_brand = sub_brand;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public List<ImagesBean> getImages() {
                        return images;
                    }

                    public void setImages(List<ImagesBean> images) {
                        this.images = images;
                    }

                    public static class ImagesBean {

                        private int id;
                        private String url;

                        public int getId() {
                            return id;
                        }

                        public void setId(int id) {
                            this.id = id;
                        }

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }
            }
        }
    }
}
