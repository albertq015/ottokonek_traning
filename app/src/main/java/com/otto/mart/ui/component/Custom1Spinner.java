package com.otto.mart.ui.component;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;

public class Custom1Spinner extends androidx.appcompat.widget.AppCompatSpinner {
    OnItemSelectedListener listener;

    public Custom1Spinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            listener.onItemSelected(null, null, position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        try {
            super.onRestoreInstanceState(state);
        } catch (Exception e) {
            super.onRestoreInstanceState(onSaveInstanceState());
        }
    }

}
