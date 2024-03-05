package com.otto.mart.model.APIModel.Response.olshop.background;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventData {
    private EventBackground event_background;
    private ProductEvent selected;

    public EventData() {
    }

    public EventBackground getEvent_background() {
        return event_background;
    }

    public void setEvent_background(EventBackground event_background) {
        this.event_background = event_background;
    }

    public ProductEvent getSelected() {
        return selected;
    }

    public void setSelected(ProductEvent selected) {
        this.selected = selected;
    }
}
