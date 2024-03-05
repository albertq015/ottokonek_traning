package com.otto.mart.model.APIModel.Response.olshop.background;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BackgroundData{
    private String catalog_background;
    private String icon;
    private String homepage_background;
    private String title_small;
    private Integer id;
    private String title;
    private String status;

    public BackgroundData() {
    }

    public String getCatalog_background() {
        return catalog_background;
    }

    public void setCatalog_background(String catalog_background) {
        this.catalog_background = catalog_background;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHomepage_background() {
        return homepage_background;
    }

    public void setHomepage_background(String homepage_background) {
        this.homepage_background = homepage_background;
    }

    public String getTitle_small() {
        return title_small;
    }

    public void setTitle_small(String title_small) {
        this.title_small = title_small;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
