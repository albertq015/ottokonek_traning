package com.otto.mart.model.APIModel.Response.grosir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataResponseItemList extends BaseResponse{
    /**
     * The type Data response.
     */
        @JsonProperty("id")
        private int id;
        @JsonProperty("code")
        private String code;
        @JsonProperty("total_product")
        private int total_product;
        @JsonProperty("name")
        private String name;
        @JsonProperty("logo_path")
        private String logo_path;
        @JsonProperty("categories")
        private ArrayList<list> list_category;
        @JsonProperty("minimum_order")
        private Double minimum_order;
        private String alamat;
        private String kota;
        @JsonProperty("status")
        private String statusRegistered;
        private String payment_type;
        private String noo_type;
        private String total_category;
        private String description;
        private warehouse warehouse;

    public DataResponseItemList.warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(DataResponseItemList.warehouse warehouse) {
        this.warehouse = warehouse;
    }

    private List<OasisListCourier> courier;
    private  ArrayList<PaymentMethodOasisListGrosir> payment_method;
    private List<OasisListMethodPickUp> delivery_method;


    public ArrayList<PaymentMethodOasisListGrosir> getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(ArrayList<PaymentMethodOasisListGrosir> payment_method) {
        this.payment_method = payment_method;
    }

    public List<OasisListCourier> getCourier() {
        return courier;
    }

    public void setCourier(List<OasisListCourier> courier) {
        this.courier = courier;
    }

    public List<OasisListMethodPickUp> getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(List<OasisListMethodPickUp> delivery_method) {
        this.delivery_method = delivery_method;
    }

    public Double getMinimum_order() {
        return minimum_order;
    }

    public void setMinimum_order(Double minimum_order) {
        this.minimum_order = minimum_order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getNoo_type() {
        return noo_type;
    }

    public void setNoo_type(String noo_type) {
        this.noo_type = noo_type;
    }

    public String getTotal_category() {
        return total_category;
    }

    public void setTotal_category(String total_category) {
        this.total_category = total_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTotal_product() {
        return total_product;
    }

    public void setTotal_product(int total_product) {
        this.total_product = total_product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public ArrayList<list> getList_category() {
        return list_category;
    }

    public void setList_category(ArrayList<list> list_category) {
        this.list_category = list_category;
    }


    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getStatusRegistered() {
        return statusRegistered;
    }

    public void setStatusRegistered(String statusRegistered) {
        this.statusRegistered = statusRegistered;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class list {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class warehouse{
        private String address;
        private String code;
        private String description;
        private String latitude;
        private String longitude;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
