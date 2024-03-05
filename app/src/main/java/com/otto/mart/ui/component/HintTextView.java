package com.otto.mart.ui.component;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.tooltip.Tooltip;

public class HintTextView extends LinearLayout implements IComponent {

    private Context mContext;
    private String hint, title, text;
    private int bubbleColor, bubbleTextColor;
    private TextView texts, titleview;
    private View hintHitbox;
    private Typeface customTypeface;
    private Tooltip hintBubble;
    boolean hasHint, hasCustomTypeface, hasTitle;

    public HintTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HintTextView);
        int count = typedArray.getIndexCount();
        int layout = R.layout.cw_textview_w_hint_default;
        bubbleColor = R.color.squash;
        bubbleTextColor = R.color.color_white;

        try {
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.HintTextView_text) {
                    text = typedArray.getString(attr);
                    if (text.contains("@string")) {
                        text = getResources().getString(Integer.parseInt(text));
                    }
                } else if (attr == R.styleable.HintTextView_title) {
                    hasTitle = true;
                    title = typedArray.getString(attr);
                    if (title.contains("@string")) {
                        title = getResources().getString(Integer.parseInt(text));
                    }
                } else if (attr == R.styleable.HintTextView_customLayout) {
                    layout = typedArray.getResourceId(attr, R.layout.cw_textview_w_hint_default);
                } else if (attr == R.styleable.HintTextView_typeface) {
                    hasCustomTypeface = true;
                    customTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + typedArray.getString(attr));
                } else if (attr == R.styleable.HintTextView_hintText) {
                    hint = typedArray.getString(attr);
                    if (hint.contains("@string")) {
                        hint = getResources().getString(Integer.parseInt(hint));
                    }
                    hasHint = true;
                } else if (attr == R.styleable.HintTextView_hintbubbleTextColor) {
                    bubbleTextColor = ContextCompat.getColor(mContext, typedArray.getInt(attr, R.color.color_white));
                } else if (attr == R.styleable.HintTextView_hintbubbleColor) {
                    bubbleColor = ContextCompat.getColor(mContext, typedArray.getInt(attr, R.color.colorYellow));
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
        texts = findViewById(R.id.text);
        hintHitbox = findViewById(R.id.iconinfo);
        if (hasCustomTypeface)
            texts.setTypeface(customTypeface);
        texts.setText(text);
        if (hasHint)
            setHintText(hint);
        try {
            titleview = findViewById(R.id.title);
            if (hasTitle)
                titleview.setText(title);
        } catch (Resources.NotFoundException e) {

        }

    }

    @Override
    public String getTextContent() {
        return text;
    }

    @Override
    public View getComponent() {
        return texts;
    }

    @Override
    public void setTitle(String text) {
        Log.d("", "gaadhund");
    }

    public void setHintText(String text) {

        if (!this.isInEditMode()) {
            hintBubble = new Tooltip.Builder(hintHitbox)
                    .setText(text)
                    .setTextColor(bubbleTextColor)
                    .setBackgroundColor(bubbleColor)
                    .setPadding(20f)
                    .setDismissOnClick(true).build();

            if (!hintHitbox.hasOnClickListeners())
                hintHitbox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hintBubble.isShowing())
                            hintBubble.show();
                        else
                            hintBubble.dismiss();
                    }
                });

        }
    }
}
