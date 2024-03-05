package com.otto.mart.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;

public class LazyTextview extends LinearLayout implements IComponent {

    private String title, content;
    private TextView titleView, componentView;
    private Typeface customTypeface;
    boolean hastitle, hasCustomTypeface;

    public LazyTextview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LazyTextview);
        int count = typedArray.getIndexCount();
        int layout = R.layout.cw_textview_default;
        try {
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.LazyTextview_customLayout) {
                    layout = typedArray.getResourceId(attr, R.layout.cw_textview_default);
                } else if (attr == R.styleable.LazyTextview_typeface) {
                    hasCustomTypeface = true;
                    customTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + typedArray.getString(attr));
                } else if (attr == R.styleable.LazyTextview_text) {
                    content = typedArray.getString(attr);
                    if (content.contains("@string")) {
                        content = getResources().getString(Integer.parseInt(content));
                    }
                } else if (attr == R.styleable.LazyTextview_title) {
                    title = typedArray.getString(attr);
                    if (title.contains("@string")) {
                        title = getResources().getString(Integer.parseInt(title));
                    }
                    hastitle = true;
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
        titleView = findViewById(R.id.title);
        componentView = findViewById(R.id.contentContainer);
        if (hasCustomTypeface) {
            componentView.setTypeface(customTypeface);
        }
        if (hastitle) {
            titleView.setText(title);
        } else {
            titleView.setVisibility(View.GONE);
        }
        if (content != null) {
            componentView.setText(content);
        }
    }

    public void setText(String string) {
        if (string != null) {
            if (string.contains("@string")) {
                componentView.setText(getResources().getString(Integer.parseInt(content)));
            } else {
                componentView.setText(string);
            }
        }
    }

    public void setText(CharSequence string) {
        if (string != null) {
            if (string instanceof String) {
                if (((String) string).contains("@string")) {
                    componentView.setText(getResources().getString(Integer.parseInt(content)));
                } else {
                    componentView.setText(string);
                }
            } else {
                componentView.setText(string);
            }
        }
    }


    @Override
    public String getTextContent() {
        return componentView.getText().toString();
    }


    public View getTitleComponent() {
        return titleView;
    }

    @Override
    public View getComponent() {
        return componentView;
    }

    @Override
    public void setTitle(String text) {
        titleView.setText(text);
    }
}
