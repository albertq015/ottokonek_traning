package com.otto.mart.support.util;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.otto.mart.R;

public class PopupUtil {

    public static PopupWindow generate(View view, View parentView){
        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(view, width, height, true);
        popupWindow.setAnimationStyle(R.style.PopupShowAnimation);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

        // hide popup window when click in outside
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        return popupWindow;
    }

    public static PopupWindow generate(View view, View parentView, int gravity){
        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(view, width, height, true);
        popupWindow.setAnimationStyle(R.style.PopupShowAnimation);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(parentView, gravity, 0, 0);

        // hide popup window when click in outside
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        return popupWindow;
    }

    public static PopupWindow generate(View view, View parentView, int width, int height){
        // create the popup window
        final PopupWindow popupWindow = new PopupWindow(view, width, height, true);
        popupWindow.setAnimationStyle(R.style.PopupShowAnimation);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

        // hide popup window when click in outside
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        return popupWindow;
    }
}
