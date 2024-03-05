package com.otto.mart.model.localmodel.ui;
import java.util.List;

public class TopUpModel {

    private int imageID;
    private String title;
    private String content;
    private List<TopUpModel> child;

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public TopUpModel setContent(String content) {
        this.content = content;
        return this;
    }

    public List<TopUpModel> getChild() {
        return child;
    }

    public void setChild(List<TopUpModel> child) {
        this.child = child;
    }
}
