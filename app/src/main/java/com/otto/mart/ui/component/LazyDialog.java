package com.otto.mart.ui.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;

public class LazyDialog extends Dialog {
    private LinearLayout mainLayout;
    private View close, negative, positive, action, container, parentView, toolbarView;
    private LinearLayout content, navigation;

    public Activity parent;
    public Boolean isHideCloseBtn, isHideToolbar;

    public LazyDialog(@NonNull Context context, Activity parent) {
        super(context);
        this.parent = parent;
    }

    public LazyDialog(@NonNull Context context, Activity parent, Boolean isHideToolbar) {
        super(context);
        this.parent = parent;
        this.isHideToolbar = isHideToolbar;
    }

    public LazyDialog(@NonNull Context context, Activity parent, Boolean isHideToolbar, Boolean hideCloseBtn) {
        super(context);
        this.parent = parent;
        this.isHideCloseBtn = hideCloseBtn;
        this.isHideToolbar = isHideToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initComponent() {
        toolbarView = findViewById(R.id.toolbar);
        mainLayout = findViewById(R.id.main_layout);
        close = findViewById(R.id.close_hitbox);
        content = findViewById(R.id.contentContainer);
        navigation = findViewById(R.id.content_nav);
        parentView = findViewById(R.id.parent);
        container = findViewById(R.id.content_container);
    }

    private void initContent() {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        final Dialog hook = this;
        findViewById(R.id.close_hitbox).setOnClickListener(v -> hook.dismiss());

        if (isHideToolbar) {
            toolbarView.setVisibility(View.GONE);
        }
        if (isHideCloseBtn) {
            toolbarView.findViewById(R.id.close_hitbox).setVisibility(View.INVISIBLE);
        }
    }

    public void setContainerView(View jenk) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_base);
        try {
            initComponent();
            initContent();
        } catch (Exception e) {
            Log.e("", "setContainerView: " + e.getMessage());
        } finally {
            content.addView(jenk, 0);
            this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setNavigationView(View jenk, @Nullable View.OnClickListener negative, @Nullable View.OnClickListener positive) {
        navigation.addView(jenk, 0);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.negajing).setOnClickListener(negative);
        findViewById(R.id.posijing).setOnClickListener(positive);
    }

    public void setNavigationView(View jenk, @Nullable View.OnClickListener action) {
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        navigation.addView(jenk, 0);
        findViewById(R.id.action).setOnClickListener(action);

    }

    public void setPadding(int padding){
        float scale = parent.getResources().getDisplayMetrics().density;
        int paddingInPixels = (int) (padding * scale + 0.5f);

        mainLayout.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);
    }

    public void setTitle(String title) {
        ((TextView) toolbarView.findViewById(R.id.title)).setText(title);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public LinearLayout getContent() {
        return content;
    }

    public LinearLayout getNavigation() {
        return navigation;
    }

    public View getParentView() {
        return parentView;
    }

    public View getToolbarView() {
        return toolbarView;
    }

    public void setToolbarDarkmode() {
        toolbarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ocean_blue));
        ((ImageView) close.findViewById(R.id.close)).setImageResource(R.drawable.ic_close_white_24dp);
    }
}