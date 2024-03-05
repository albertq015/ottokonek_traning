package com.otto.mart.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.otto.mart.R;

public class SquareImage extends ImageView {

    private Drawable drawable = null;
    private boolean isHorizontalOriented = false;

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        postInvalidate();
    }

    public SquareImage(Context context) {
        super(context);
    }

    public SquareImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareImage, defStyleAttr, 0);
        isHorizontalOriented = typedArray.getBoolean(R.styleable.SquareImage_horizontal, false);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int max = Math.max(getMeasuredWidth(), getMeasuredHeight());
        if (isHorizontalOriented) {
            max = getMeasuredWidth();
        }
        setMeasuredDimension(max, max);
    }
}
