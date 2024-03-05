package com.otto.mart.ui.Partials.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.PulsaPaketModel;
import com.otto.mart.ui.component.LazyTextview;

public class PulsaPaketViewHolder {
    private final View view;
    LazyTextview phone, productName;

    public PulsaPaketViewHolder(Context context, int resId) {
        view = LayoutInflater.from(context).inflate(resId, null);
        phone = view.findViewById(R.id.phone);
        productName = view.findViewById(R.id.productName);
    }

    public void bind(PulsaPaketModel confirmationModel) {
        phone.setText(confirmationModel.getPhone());
        productName.setText(confirmationModel.getProductName());
    }

    public View getView() {
        return view;
    }
}
