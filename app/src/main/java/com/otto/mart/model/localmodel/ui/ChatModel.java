package com.otto.mart.model.localmodel.ui;
import java.util.Date;

public class ChatModel {
    private Date time;
    private boolean isRead = false;
    private String content;
    private boolean isServer = false;
    private int id;
    private String base64img;


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBase64img() {
        return base64img;
    }

    public void setBase64img(String base64img) {
        this.base64img = base64img;
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
