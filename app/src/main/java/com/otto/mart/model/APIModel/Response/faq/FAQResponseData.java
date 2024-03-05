package com.otto.mart.model.APIModel.Response.faq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FAQResponseData {
    private String description;
    private String title;
    private List<TopUpChannelsItem> top_up_channels;

    public String getDescription() {
        return description;
    }

    public FAQResponseData setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public FAQResponseData setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<TopUpChannelsItem> getTop_up_channels() {
        return top_up_channels;
    }

    public FAQResponseData setTop_up_channels(List<TopUpChannelsItem> top_up_channels) {
        this.top_up_channels = top_up_channels;
        return this;
    }
}