package com.otto.mart.ui.component;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;

public class SquareViewPager extends ViewPager {
    public SquareViewPager(@NonNull Context context) {
        super(context);
    }

    public SquareViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int max = Math.max(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(max, max);
    }
}
