package com.otto.mart.model.APIModel.Response.inbox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.inbox.Inbox;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InboxResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        private int unread;
        private List<Inbox> notifications;

        public int getUnread() {
            return unread;
        }

        public void setUnread(int unread) {
            this.unread = unread;
        }

        public List<Inbox> getNotifications() {
            return notifications;
        }

        public void setNotifications(List<Inbox> notifications) {
            this.notifications = notifications;
        }
    }
}
