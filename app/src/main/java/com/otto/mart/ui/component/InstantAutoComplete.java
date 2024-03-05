package com.otto.mart.ui.component;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.otto.mart.support.util.DeviceUtil;

public class InstantAutoComplete extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    public InstantAutoComplete(Context context) {
        super(context);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        if (focused && getAdapter() != null) {
//            performFiltering(getText(), 0);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getAdapter() != null) {
            if (getAdapter().getCount() > 3)
                setDropDownHeight(DeviceUtil.dpToPx(102));
        }
        this.showDropDown();
        return super.onTouchEvent(event);
    }


}