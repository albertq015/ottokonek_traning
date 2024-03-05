package com.otto.mart.ui.Partials;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.HeaderModel;

public class HeaderViewGroup extends LinearLayout {

    private Context mContext;
    private ImageView photo;
    private TextView shopname, businessname, ownername, shopid;

    public HeaderViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setOrientation(VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IntentButtonViewGroup);
        int count = typedArray.getIndexCount();
        int layout = R.layout.partial_dashboard_header;
        try {
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.IntentButtonViewGroup_customLayout) {
                    layout = typedArray.getResourceId(attr, R.layout.cw_edittext_default);
                }
            }
        } catch (Exception e) {
            Log.e("SearchEdittext", e.getMessage());
        } finally {
            typedArray.recycle();
        }
        this.addView(LayoutInflater.from(context).inflate(layout, null));
        initComponent();
    }

    private void initComponent() {
        shopname = findViewById(R.id.s_name);
        businessname = findViewById(R.id.b_name);
        ownername = findViewById(R.id.p_name);
        shopid = findViewById(R.id.s_id);
        photo = findViewById(R.id.imgv);
    }

    public void initWidget(HeaderModel model) {
        shopname.setText(model.getShopName());
        businessname.setText(model.getProductName());
        ownername.setText(model.getOwnerName());
        shopid.setText(model.getShopId());
        try {
            photo.setImageResource(model.getImageId());
        } catch (Exception e) {
            Log.e("", "kucing " + e.getMessage());
        }
    }
}