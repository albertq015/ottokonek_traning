package com.otto.mart.model.APIModel.Request.grosir;

public class GrosirCostShipmentRequest {
    private int origin_ottopay_city_code;
    private long destination_ottopay_city_code;
    private long weight;
    private long origin_ottopay_district_code;
    private long origin_ottopay_village_code;
    private long destination_ottopay_district_code;
    private long destination_ottopay_village_code;
    private long supplier_id;
    private long area_id;

    public long getArea_id() {
        return area_id;
    }

    public void setArea_id(long area_id) {
        this.area_id = area_id;
    }

    public long getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(long supplier_id) {
        this.supplier_id = supplier_id;
    }

    public long getOrigin_ottopay_district_code() {
        return origin_ottopay_district_code;
    }

    public void setOrigin_ottopay_district_code(long origin_ottopay_district_code) {
        this.origin_ottopay_district_code = origin_ottopay_district_code;
    }

    public long getOrigin_ottopay_village_code() {
        return origin_ottopay_village_code;
    }

    public void setOrigin_ottopay_village_code(long origin_ottopay_village_code) {
        this.origin_ottopay_village_code = origin_ottopay_village_code;
    }

    public long getDestination_ottopay_district_code() {
        return destination_ottopay_district_code;
    }

    public void setDestination_ottopay_district_code(long destination_ottopay_district_code) {
        this.destination_ottopay_district_code = destination_ottopay_district_code;
    }

    public long getDestination_ottopay_village_code() {
        return destination_ottopay_village_code;
    }

    public void setDestination_ottopay_village_code(long destination_ottopay_village_code) {
        this.destination_ottopay_village_code = destination_ottopay_village_code;
    }

    public int getOrigin_ottopay_city_code() {
        return origin_ottopay_city_code;
    }

    public void setOrigin_ottopay_city_code(int origin_ottopay_city_code) {
        this.origin_ottopay_city_code = origin_ottopay_city_code;
    }

    public long getDestination_ottopay_city_code() {
        return destination_ottopay_city_code;
    }

    public void setDestination_ottopay_city_code(long destination_ottopay_city_code) {
        this.destination_ottopay_city_code = destination_ottopay_city_code;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }
}
