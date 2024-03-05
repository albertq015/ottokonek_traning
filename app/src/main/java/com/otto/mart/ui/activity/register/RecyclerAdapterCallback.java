package com.otto.mart.ui.activity.register;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerAdapterCallback {

    void onItemClick(Object objectModel, int position, RecyclerView.ViewHolder currHolder);

    void onItemClick(Object objectModel, int position, int paymentTypePos, RecyclerView.ViewHolder currHolder);
}