package com.otto.mart.model.APIModel.Response.olshop.background;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventResponse extends BaseResponseModel {
    private EventData data;

    public EventResponse() {
    }

    public EventData getData() {
        return data;
    }

    public void setData(EventData data) {
        this.data = data;
    }
}
