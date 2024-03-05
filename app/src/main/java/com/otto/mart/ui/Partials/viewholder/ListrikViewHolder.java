package com.otto.mart.ui.Partials.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.ListrikConfirmationModel;
import com.otto.mart.ui.Partials.adapter.KeyValueListAdapter;
import com.otto.mart.ui.component.LazyTextview;

public class ListrikViewHolder {
    private final View view;
    LazyTextview productName;
    RecyclerView rv;
    KeyValueListAdapter adapter;

    public ListrikViewHolder(Context context, int resId) {
        view = LayoutInflater.from(context).inflate(resId, null);
        rv = view.findViewById(R.id.list);
        productName = view.findViewById(R.id.productName);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void bind(ListrikConfirmationModel confirmationModel) {
        adapter = new KeyValueListAdapter(confirmationModel.getKeyValueList(), R.layout.item_key_value_list_default);
        rv.setAdapter(adapter);
        productName.setText(confirmationModel.getProductName());
    }

    public View getView() {
        return view;
    }
}
