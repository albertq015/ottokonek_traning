package com.otto.mart.model.APIModel.Request.inbox;

public class InboxReadRequest {
    private String action;
    private int notification_id;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }
}
