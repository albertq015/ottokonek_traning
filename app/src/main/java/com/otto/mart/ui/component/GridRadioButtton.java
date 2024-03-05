package com.otto.mart.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GridRadioButtton extends TableLayout implements View.OnClickListener {
    private RadioButton activeRadioButton;
    private OnCheckedChangeListener listener;

    public GridRadioButtton(Context context) {
        super(context);
    }

    public GridRadioButtton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        final RadioButton rb = (RadioButton) v;
        if (activeRadioButton != null) {
            activeRadioButton.setChecked(false);
        }
        rb.setChecked(true);
        activeRadioButton = rb;
        if (listener != null) {
            listener.onCheckedChanged(activeRadioButton.getId());
        }
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow) child);
    }


    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setChildrenOnClickListener((TableRow) child);
    }


    private void setChildrenOnClickListener(TableRow tr) {
        final int c = tr.getChildCount();
        for (int i = 0; i < c; i++) {
            final View v = tr.getChildAt(i);
            if (v instanceof RadioButton) {
                v.setOnClickListener(this);
                if (((RadioButton) v).isChecked())
                    activeRadioButton = (RadioButton) v;
            }
        }
    }

    public int getCheckedRadioButtonId() {
        if (activeRadioButton != null) {
            return activeRadioButton.getId();
        }

        return -1;
    }

    public void setOnCheckedChangeListener(GridRadioButtton.OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int checkedId);
    }

    public void setUncheck(){
        activeRadioButton.setChecked(false);
    }
}
