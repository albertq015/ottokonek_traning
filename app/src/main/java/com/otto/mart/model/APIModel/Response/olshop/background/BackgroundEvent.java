package com.otto.mart.model.APIModel.Response.olshop.background;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BackgroundEvent {
    private BackgroundData background_event;

    public BackgroundEvent() {
    }

    public BackgroundData getBackground_event() {
        return background_event;
    }

    public void setBackground_event(BackgroundData background_event) {
        this.background_event = background_event;
    }
}
