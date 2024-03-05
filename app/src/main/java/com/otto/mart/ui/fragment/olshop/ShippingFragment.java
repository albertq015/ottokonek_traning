package com.otto.mart.ui.fragment.olshop;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.order.ShippingItem;

import java.util.List;
import java.util.Objects;

public class ShippingFragment extends BottomSheetDialogFragment {

    private RecyclerView shippingList;
    private List<ShippingItem> shipping;
    private ShippingListener listener;

    public static ShippingFragment newInstance(ShippingListener listener) {

        Bundle args = new Bundle();

        ShippingFragment fragment = new ShippingFragment();
        fragment.listener = listener;
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);
        shippingList = view.findViewById(R.id.shippingList);
        shippingList.setLayoutManager(new LinearLayoutManager(getContext()));
        shippingList.setAdapter(new ShippingAdapter(shipping, (id, pos, data) -> {
            if (listener != null)
                listener.onItemSelected(pos, (ShippingItem) data, ShippingFragment.this);
        }));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        Objects.requireNonNull(bottomSheet).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public void setShipping(List<ShippingItem> shipping) {
        this.shipping = shipping;
    }

    public interface ShippingListener {
        void onItemSelected(int pos, ShippingItem data, BottomSheetDialogFragment fragment);
    }
}