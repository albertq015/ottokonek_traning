package com.otto.mart.ui.component;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.otto.mart.R;

import java.util.Calendar;

public class TimeSelectButton extends FrameLayout implements DatePickerDialog.OnDateSetListener {
    private String title;
    private TextView titleView, dateView;
    private View hitbox;
    private int layout;
    private boolean hastitle;
    private DatePickerDialog dpd;
    private Context mContext;

    public TimeSelectButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimeSelectButton);
        int count = typedArray.getIndexCount();
        int layout = R.layout.item_rt_time;

        try {
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.TimeSelectButton_title) {
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
        initContent();
    }

    private void initComponent() {
        titleView = findViewById(R.id.title);
        dateView = findViewById(R.id.pickerdate);
        hitbox = findViewById(R.id.itemLayout);
        if (hastitle) {
            titleView.setText(title);
        } else {
            titleView.setVisibility(View.GONE);
        }
        Calendar now = Calendar.getInstance();
        if (!this.isInEditMode()) {
            dpd = new DatePickerDialog(mContext, this, now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
        }

    }

    private void initContent() {
        if (!this.isInEditMode()) {
            hitbox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dpd.show();
                }
            });
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateView.setText(dayOfMonth + "-" + month + "-" + year);
    }

    public String getTime() {
        return dateView.getText() + "";
    }
}
