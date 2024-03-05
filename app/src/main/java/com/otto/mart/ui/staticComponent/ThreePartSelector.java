package com.otto.mart.ui.staticComponent;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.otto.mart.R;

public class ThreePartSelector extends LinearLayout {
    ImageView dot1, dot2, dot3;
    LinearLayout line1, line2;
    Context mContext;

    public ThreePartSelector(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
        View parent = LayoutInflater.from(context).inflate(R.layout.sl_indicator, null);
        dot1 = parent.findViewById(R.id.imageView_pointer1);
        dot2 = parent.findViewById(R.id.imageView_pointer2);
        dot3 = parent.findViewById(R.id.imageView_pointer3);
        line1 = parent.findViewById(R.id.layout_1);
        line2 = parent.findViewById(R.id.layout_2);
        this.addView(parent);
    }

    public void selectOne() {
        dot2.setImageResource(R.drawable.ic_dot_inactive);
        dot3.setImageResource(R.drawable.ic_dot_inactive);
        line1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.charcoal_grey));
        line2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.charcoal_grey));
    }

    public void selectTwo() {
        dot2.setImageResource(R.drawable.ic_dot_active);
        dot3.setImageResource(R.drawable.ic_dot_inactive);
        line1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ocean_blue));
        line2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.charcoal_grey));
    }

    public void selectThree() {
        dot2.setImageResource(R.drawable.ic_dot_active);
        dot3.setImageResource(R.drawable.ic_dot_active);
        line1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ocean_blue));
        line2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ocean_blue));
    }
}
