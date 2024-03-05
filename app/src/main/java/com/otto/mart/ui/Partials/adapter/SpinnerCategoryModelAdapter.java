package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class SpinnerCategoryModelAdapter extends BaseAdapter implements SpinnerAdapter {
    List<CategoryModel> models = new ArrayList<>();
    Context mContext;
    int spinnerLayout, dropdownLayout;
    boolean isVirgin = true;
    boolean isCallbackReady = false;
    int currpos = -1;
    TextView hook;

    public SpinnerCategoryModelAdapter(Context mContext, int spinnerLayout, int dropdownLayout) {
        this.mContext = mContext;
        this.spinnerLayout = spinnerLayout;
        this.dropdownLayout = dropdownLayout;
    }

    public void addModels(List<CategoryModel> models) {
        this.models = models;
        this.notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        CategoryModel model = models.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(dropdownLayout, null);
        TextView tv = convertView.findViewById(R.id.tv_content);
        tv.setText(model.getTitle());
        isVirgin = false;
        isCallbackReady = true;
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryModel model;
        try {
            model = models.get(position);
        } catch (Exception e) {
            model = models.get(0);
        }
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(spinnerLayout, null);
        TextView tv = convertView.findViewById(R.id.tv_content);
        tv.setText("Click here...");
        hook = tv;
        if (isVirgin == false)
            tv.setText(model.getTitle());
        currpos = position;
        return convertView;
    }

    public List<CategoryModel> getModels() {
        return models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        if (models.size() > position) {
            return models.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (models.size() > position) {
            return models.get(position).getId();
        } else {
            return 0L;
        }
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public boolean isCallbackReady() {
        return isCallbackReady;
    }

    public void resetAdapter() {
        models = new ArrayList<>();
        this.notifyDataSetChanged();
        isVirgin = true;
    }

    public void resetAdapterNotNullingData() {
        this.notifyDataSetChanged();
        isVirgin = true;
    }

    public void deVirgin() {
        isVirgin = false;
        isCallbackReady = true;
    }

    public void changeFirstItemToText() {
        if (currpos == 0) {
            hook.setText(models.get(0).getTitle());
            notifyDataSetChanged();
        }
    }
}
