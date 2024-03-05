package com.otto.mart.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;

public class ButtonToggleable extends LinearLayout {

    private Context mContext;
    private String title;
    private int activeTextColor, inactiveTextColor;
    private Drawable inactiveBackgroundDrawable, activeBackgroundDrawable, drawableActive, drawableInactive;
    private ViewGroup parent_view;
    private TextView tv_text;
    private ImageView imgv_icon;
    private boolean isDone;
    boolean hasHint, hasCustomTypeface, hasTitle;

    public ButtonToggleable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HintTextView);
        int count = typedArray.getIndexCount();
        int layout = R.layout.cw_toggleablebutton_default;
        activeTextColor = R.color.cpb_green;
        inactiveTextColor = R.color.ocean_blue;
        activeBackgroundDrawable = ContextCompat.getDrawable(mContext, R.drawable.border_green_line_thicker_bg_green);
        inactiveBackgroundDrawable = ContextCompat.getDrawable(mContext, R.drawable.border_blue_line_thicker_bg_blue);
        drawableActive = ContextCompat.getDrawable(mContext, R.drawable.ic_done_all_green);
        drawableInactive = ContextCompat.getDrawable(mContext, R.drawable.icon_upload);


        try {
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.ButtonToggleable_title) {
                    title = typedArray.getString(attr);
                    hasTitle = true;
                    if (title.contains("@string")) {
                        title = getResources().getString(Integer.parseInt(title));
                    }
                } else if (attr == R.styleable.ButtonToggleable_customLayout) {
                    layout = typedArray.getResourceId(attr, R.layout.cw_textview_w_hint_default);
                } else if (attr == R.styleable.ButtonToggleable_activeTextColor) {
                    activeTextColor = typedArray.getResources().getColor(attr);
                } else if (attr == R.styleable.ButtonToggleable_inactiveTextColor) {
                    inactiveTextColor = typedArray.getResources().getColor(attr);
                } else if (attr == R.styleable.ButtonToggleable_activeBackground) {
                    activeBackgroundDrawable = typedArray.getResources().getDrawable(attr);
                } else if (attr == R.styleable.ButtonToggleable_inactiveBackground) {
                    inactiveBackgroundDrawable = typedArray.getResources().getDrawable(attr);
                } else if (attr == R.styleable.ButtonToggleable_inactiveIcon) {
                    drawableInactive = typedArray.getResources().getDrawable(attr);
                } else if (attr == R.styleable.ButtonToggleable_activeIcon) {
                    drawableActive = typedArray.getResources().getDrawable(attr);
                }
            }
        } catch (Exception e) {
            Log.e("SearchEdittext", e.getMessage());
        } finally {
            typedArray.recycle();
        }
        inflater.inflate(layout, this);
        initComponent();
    }

    private void initComponent() {
        parent_view = findViewById(R.id.layout_parent);
        tv_text = findViewById(R.id.tv_text);
        imgv_icon = findViewById(R.id.imgv_icon);
        if (hasTitle) {
            tv_text.setText(title);
        }
    }

    public void setButtonState(boolean toggled) {
        if (toggled) {
            if (!isDone)
                toggleState();
        } else {
            if (isDone)
                toggleState();
        }
    }

    private void toggleState() {
        isDone = !isDone;
        if (isDone) {
            parent_view.setBackground(activeBackgroundDrawable);
            tv_text.setTextColor(activeTextColor);
            imgv_icon.setImageDrawable(drawableActive);
        } else {
            parent_view.setBackground(inactiveBackgroundDrawable);
            tv_text.setTextColor(inactiveTextColor);
            imgv_icon.setImageDrawable(drawableInactive);
        }
    }

    public boolean isDone() {
        return isDone;
    }

    public TextView getContentTextView() {
        return tv_text;
    }
}
