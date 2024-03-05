package com.otto.mart.ui.component;

import com.google.android.material.appbar.AppBarLayout;

public abstract class AppBarStateListener implements AppBarLayout.OnOffsetChangedListener {
    public static int STATE_EXPANDED = 1;
    public static int STATE_COLLAPSED = 2;
    public static int STATE_IDLE = 3;

    private int currentState = STATE_IDLE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (currentState != STATE_EXPANDED) {
                onStateChanged(appBarLayout, STATE_EXPANDED);
            }
            currentState = STATE_EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (currentState != STATE_COLLAPSED) {
                onStateChanged(appBarLayout, STATE_COLLAPSED);
            }
            currentState = STATE_COLLAPSED;
        } else {
            if (currentState != STATE_IDLE) {
                onStateChanged(appBarLayout, STATE_IDLE);
            }
            currentState = STATE_IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, int state);
}
