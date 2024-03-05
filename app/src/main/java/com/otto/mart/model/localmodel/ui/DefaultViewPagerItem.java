package com.otto.mart.model.localmodel.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

public class DefaultViewPagerItem {

    private int id;
    private String title;
    private Fragment fragment;
    private Bundle data;

    public DefaultViewPagerItem(int id, String title, Fragment fragment){
        this.id = id;
        this.title = title;
        this.fragment = fragment;
    }

    public DefaultViewPagerItem(int id, String title, Fragment fragment, Bundle data){
        this.id = id;
        this.title = title;
        this.fragment = fragment;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
