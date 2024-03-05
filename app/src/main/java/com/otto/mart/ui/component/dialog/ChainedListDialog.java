package com.otto.mart.ui.component.dialog;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.BusinessCategoryModel;
import com.otto.mart.ui.Partials.adapter.ChainedListAdapter;
import com.otto.mart.ui.component.LazyDialog;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

public class ChainedListDialog extends LazyDialog implements IChainedDialog {


    View contentView;
    Context mContext;
    LazyDialog mDialog;
    ChainedListAdapter primaryAdapter, secondaryAdapter;

    ChainedListDialogListener listener;
    List<BusinessCategoryModel> filterModel, filterSubModel;
    ExpandableLayout elayout_content, elayout_loading;
    RecyclerView rv_category, rv_subcategory;

    public ChainedListDialog(@NonNull Context context, Activity parent, Boolean hideCloseBtn) {
        super(context, parent, false, hideCloseBtn);
        mContext = context;
        mDialog = this;
        initComponent();
        initContent();
    }

    public ChainedListDialog(@NonNull Context context, Activity parent, List<BusinessCategoryModel> filterModel, Boolean hideCloseBtn) {
        super(context, parent, hideCloseBtn);
        mContext = context;
        mDialog = this;
        this.filterModel = filterModel;
        initComponent();
        initContent();
    }

    public void setListener(ChainedListDialogListener listener) {
        this.listener = listener;
    }

    private void initComponent() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_shopfilter, null);
        this.setContainerView(contentView);
        rv_category = contentView.findViewById(R.id.rv_category);
        rv_subcategory = contentView.findViewById(R.id.rv_subcategory);
        elayout_content = contentView.findViewById(R.id.eLayout_content);
        elayout_loading = contentView.findViewById(R.id.eLayout_loading);
        filterModel = new ArrayList<>();
        filterSubModel = new ArrayList<>();
        this.getToolbarView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.ocean_blue));
        this.setToolbarDarkmode();

        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        TextView titleTv = this.getToolbarView().findViewById(R.id.title);
        titleTv.setText(contentView.getContext().getString(R.string.label_company_type));
    }

    private void initContent() {
        if (filterModel.size() == 0) {
            setLoadingState();
        }
        primaryAdapter = new ChainedListAdapter(this, getContext(), filterModel);
        secondaryAdapter = new ChainedListAdapter(this, getContext(), filterSubModel, R.layout.item_filter_subcategory, R.color.color_white, R.color.color_white, R.color.white_six, R.color.white_six);

        rv_category.setAdapter(primaryAdapter);
        rv_category.setLayoutManager(new LinearLayoutManager(mContext));

        rv_subcategory.setAdapter(secondaryAdapter);
        rv_subcategory.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void setLoadingState() {
        elayout_loading.setExpanded(true);
        elayout_content.setExpanded(false);
    }

    public void setNotLoadingState() {
        elayout_loading.setExpanded(false);
        elayout_content.setExpanded(true);
        filterSubModel = filterModel.get(0).getBusiness_categories();
        secondaryAdapter.replaceAllItems(filterSubModel);
    }

    @Override
    public void primaryCallback(int val) {
        filterSubModel = filterModel.get(val).getBusiness_categories();
        secondaryAdapter.replaceAllItems(filterSubModel);
    }

    @Override
    public void secondaryCallback(int val) {
        if (listener != null) {
            listener.onSecondaryCallback(filterSubModel.get(val));
        }
        this.dismiss();
    }

    public interface ChainedListDialogListener {
        void onSecondaryCallback(BusinessCategoryModel returnItem);
    }

    @Override
    public void show() {
        super.show();
    }

    public void addCategoryData(List<BusinessCategoryModel> models) {
        filterModel = models;
        primaryAdapter.replaceAllItems(models);
        setNotLoadingState();
    }

}
