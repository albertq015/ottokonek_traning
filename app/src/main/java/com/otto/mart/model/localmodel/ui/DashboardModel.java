package com.otto.mart.model.localmodel.ui;

import android.content.Intent;
import androidx.annotation.Nullable;

public class DashboardModel extends BasicUIModel {
    private String title;
    private Intent target;
    private String iconID;
    private String extradataString;
    private int extradataInt;

    public DashboardModel(String title, Intent target, String  iconID, @Nullable String extradataString, @Nullable int extradataInt) {
        this.title = title;
        this.target = target;
        this.iconID = iconID;
        this.extradataString = extradataString;
        this.extradataInt = extradataInt != 0 ? extradataInt : 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Intent getTarget() {
        return target;
    }

    public void setTarget(Intent target) {
        this.target = target;
    }

    public String getIconID() {
        return iconID;
    }

    public void setIconID(String iconID) {
        this.iconID = iconID;
    }

    public String getExtradataString() {
        return extradataString;
    }

    public void setExtradataString(String extradataString) {
        this.extradataString = extradataString;
    }

    public int getExtradataInt() {
        return extradataInt;
    }

    public void setExtradataInt(int extradataInt) {
        this.extradataInt = extradataInt;
    }
}
