package com.otto.mart.model.APIModel.Request.inbox;

import java.util.List;

public class InboxUpdateBulkRequest {

    private List<Integer> notification_ids;

    public List<Integer> getNotification_ids() {
        return notification_ids;
    }

    public void setNotification_ids(List<Integer> notification_ids) {
        this.notification_ids = notification_ids;
    }
}
