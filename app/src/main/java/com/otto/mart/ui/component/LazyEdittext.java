package com.otto.mart.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LazyEdittext extends LinearLayout implements IComponent {
    private String title, hint, prevalue;
    private int inputtype, inputlength, inputMinLength, tvId;
    private TextView titleView;
    private EditText componentView;
    private Typeface customTypeface;
    boolean hastitle, hasHint, hasType, hasPrevalue, hasCustomTypeface, hasMinLength, hasMaxLength;

    public LazyEdittext(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LazyEdittext);
        int count = typedArray.getIndexCount();
        int layout = R.layout.cw_edittext_default;
        try {
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.LazyEdittext_customLayout) {
                    layout = typedArray.getResourceId(attr, R.layout.cw_edittext_default);
                } else if (attr == R.styleable.LazyEdittext_typeface) {
                    hasCustomTypeface = true;
                    customTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + typedArray.getString(attr));
                } else if (attr == R.styleable.LazyEdittext_hintText) {
                    hint = typedArray.getString(attr);
                    if (hint.contains("@string")) {
                        hint = getResources().getString(Integer.parseInt(hint));
                    }
                    hasHint = true;
                } else if (attr == R.styleable.LazyEdittext_let_inputType) {
                    inputtype = typedArray.getInt(attr, 0);
                    hasType = true;
                } else if (attr == R.styleable.LazyEdittext_minLength) {
                    inputMinLength = typedArray.getInt(attr, 0);
                    hasMinLength = true;
                } else if (attr == R.styleable.LazyEdittext_maxLength) {
                    inputlength = typedArray.getInt(attr, 0);
                    hasMaxLength = true;
                } else if (attr == R.styleable.LazyEdittext_title) {
                    title = typedArray.getString(attr);
                    if (title.contains("@string")) {
                        title = getResources().getString(Integer.parseInt(title));
                    }
                    hastitle = true;
                } else if (attr == R.styleable.LazyEdittext_text) {
                    prevalue = typedArray.getString(attr);
                    if (prevalue.contains("@string")) {
                        prevalue = getResources().getString(Integer.parseInt(prevalue));
                    }
                    hasPrevalue = true;
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
        tvId = generateViewId();
        titleView = findViewById(R.id.title);
        componentView = findViewById(R.id.contentContainer);
        componentView.setId(tvId);

        if (hasCustomTypeface) {
            componentView.setTypeface(customTypeface);
        }
        if (hastitle) {
            titleView.setText(title);
        } else {
            titleView.setVisibility(View.GONE);
        }
        if (hasHint) {
            componentView.setHint(hint);
        }
        if (hasType) {
            componentView.setInputType(inputtype);
        }
        if (hasMaxLength) {
            setMaxLength(componentView, inputlength);
        }
        if (hasPrevalue) {
            componentView.setText(prevalue);
        }
    }


    @Override
    public String getTextContent() {
        return componentView.getText().toString().trim();
    }

    @Override
    public View getComponent() {
        return componentView;
    }

    @Override
    public void setTitle(String text) {
        titleView.setText(text);
    }

    public void setContentText(String text) {
        if (text != null)
            componentView.setText(text);
    }

    public boolean isLengthValid() {
        int count = 1;

        if (hasMinLength) {
            count = inputMinLength;
        }
        try {
            if (componentView.getText().length() <= count) {
                componentView.setError("Mohon isi " + title);
                return false;

            } else {
                return true;
            }
        } catch (Exception e) {
            componentView.setError("Mohon isi" + title);
            return false;
        }
    }

    public boolean isEmailValid() {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(componentView.getText().toString().trim());
        if (!matcher.matches()) {
            componentView.setError("Email tidak valid");
        }
        return matcher.matches();
    }

    private void setMaxLength(EditText view, int length) {
        InputFilter curFilters[];
        InputFilter.LengthFilter lengthFilter;
        int idx;

        lengthFilter = new InputFilter.LengthFilter(length);

        curFilters = view.getFilters();
        if (curFilters != null) {
            for (idx = 0; idx < curFilters.length; idx++) {
                if (curFilters[idx] instanceof InputFilter.LengthFilter) {
                    curFilters[idx] = lengthFilter;
                    return;
                }
            }
            InputFilter newFilters[] = new InputFilter[curFilters.length + 1];
            System.arraycopy(curFilters, 0, newFilters, 0, curFilters.length);
            newFilters[curFilters.length] = lengthFilter;
            view.setFilters(newFilters);
        } else {
            view.setFilters(new InputFilter[]{lengthFilter});
        }
    }
}

