package com.otto.mart.model.APIModel.Request.olshop;

public class ProductListRequestModel {
    private Integer category_id;
    private String query;
    private Long high_price;
    private int page = 1;
    private String sort="newest";
    private Long low_price;
    private int items = 10;

    public ProductListRequestModel() {
    }

    public ProductListRequestModel(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getHigh_price() {
        return high_price;
    }

    public void setHigh_price(Long high_price) {
        this.high_price = high_price;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Long getLow_price() {
        return low_price;
    }

    public void setLow_price(Long low_price) {
        this.low_price = low_price;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}
