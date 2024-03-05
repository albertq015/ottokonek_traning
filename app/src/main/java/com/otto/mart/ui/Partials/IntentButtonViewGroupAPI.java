package com.otto.mart.ui.Partials;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.DashboardModel;
import com.otto.mart.ui.Partials.adapter.DashboardIconAdapterAPI;

import java.util.List;

public class IntentButtonViewGroupAPI extends LinearLayout {
    boolean hastitle, hasDrawableLeft;
    private Context mContext;
    private String title;
    private int cLayout, drawableLeft;
    private ViewGroup mainLayout;
    private DashboardIconAdapterAPI adapter;

    public IntentButtonViewGroupAPI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setOrientation(VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IntentButtonViewGroup);
        int count = typedArray.getIndexCount();
        int layout = R.layout.cw_navigation_partial;
        this.cLayout = R.layout.cw_navbutton;
        try {
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.IntentButtonViewGroup_customLayout) {
                    layout = typedArray.getResourceId(attr, R.layout.cw_edittext_default);

                } else if (attr == R.styleable.IntentButtonViewGroup_title) {
                    title = typedArray.getString(attr);
                    if (title.contains("@string")) {
                        title = getResources().getString(Integer.parseInt(title));
                    }
                    hastitle = true;
                } else if (attr == R.styleable.IntentButtonViewGroup_childLayout) {
                    this.cLayout = typedArray.getResourceId(attr, R.layout.cw_navbutton);
                } else if (attr == R.styleable.IntentButtonViewGroup_drawableLeft) {
                    drawableLeft = typedArray.getResourceId(attr, 0);
                    hasDrawableLeft = true;
                }
            }
        } catch (Exception e) {
            Log.e("SearchEdittext", e.getMessage());
        } finally {
            typedArray.recycle();
        }
        mainLayout = (ViewGroup) LayoutInflater.from(context).inflate(layout, null);
        initContent();
    }

    private void initContent() {
        TextView title = mainLayout.findViewById(R.id.title);
        if (hastitle) {
            title.setText(this.title);
            title.setVisibility(VISIBLE);
        }
        if (hasDrawableLeft) {
            Drawable tot = getResources().getDrawable(drawableLeft);
            title.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, tot, null);
            title.setCompoundDrawablePadding(10);
        }
        this.addView(mainLayout);
    }

    public void initWidget(Activity parent, List<DashboardModel> models, int rows) {
        RecyclerView rv = mainLayout.findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(mContext, rows, GridLayoutManager.VERTICAL, false));
        adapter = new DashboardIconAdapterAPI(parent, cLayout, models);
        rv.setAdapter(adapter);
    }

    public int getItemCount() {
        return adapter != null ? adapter.getItemCount() : 0;
    }

}