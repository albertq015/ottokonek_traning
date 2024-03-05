package com.otto.mart.model.APIModel.Response.olshop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubCategory {
    private SubCategory sub_category;
    private String name;
    private int id;

    public SubCategory getSub_category() {
        return sub_category;
    }

    public void setSub_category(SubCategory sub_category) {
        this.sub_category = sub_category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
