package com.otto.mart.model.localmodel.ui;

import androidx.fragment.app.Fragment;

public class FragmentSwitchModel extends ActionButtonModel {
    private Fragment targetFragment;
    private int containerID;

    public Fragment getTargetFragment() {
        return targetFragment;
    }

    public void setTargetFragment(Fragment targetFragment) {
        this.targetFragment = targetFragment;
    }

    public int getContainerID() {
        return containerID;
    }

    public void setContainerID(int containerID) {
        this.containerID = containerID;
    }
}
