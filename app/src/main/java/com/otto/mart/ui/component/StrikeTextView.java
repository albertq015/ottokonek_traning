package com.otto.mart.ui.component;

import android.content.Context;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class StrikeTextView extends TextView {
    public StrikeTextView(Context context) {
        super(context);
        setPaintFlags(this.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public StrikeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setPaintFlags(this.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public StrikeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPaintFlags(this.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

}
