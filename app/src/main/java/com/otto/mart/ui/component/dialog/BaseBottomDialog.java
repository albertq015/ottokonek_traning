package com.otto.mart.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import android.view.Gravity;
import android.view.Window;
import android.widget.LinearLayout;

public class BaseBottomDialog extends Dialog {

    BaseBottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setCanceledOnTouchOutside(false);

        // set layout window match with windows phone
        Window window = getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            // show dialog in bottom
            window.setGravity(Gravity.BOTTOM);

            // set background dialog transparent
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}
