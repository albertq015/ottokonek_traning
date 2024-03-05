package com.otto.mart.presenter.dao;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.model.APIModel.Request.inbox.InboxReadRequest;
import com.otto.mart.model.APIModel.Request.inbox.InboxUpdateBulkRequest;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;

public class InboxDao extends BaseDao {

    public InboxDao(Object obj) {
        super(obj);
    }

    public void getInboxList(int page, Callback callback) {
        API.InboxList(OttoMartApp.getContext(), page, callback);
    }

    public void inboxRead(InboxReadRequest inboxReadRequest, Callback callback) {
        API.InboxRead(OttoMartApp.getContext(), inboxReadRequest, callback);
    }

    public void inboxReadBulk(InboxUpdateBulkRequest inboxUpdateBulkRequest, Callback callback) {
        API.inboxReadBulk(OttoMartApp.getContext(), inboxUpdateBulkRequest, callback);
    }

    public void inboxDeleteBulk(InboxUpdateBulkRequest inboxUpdateBulkRequest, Callback callback) {
        API.inboxDeleteBulk(OttoMartApp.getContext(), inboxUpdateBulkRequest, callback);
    }
}