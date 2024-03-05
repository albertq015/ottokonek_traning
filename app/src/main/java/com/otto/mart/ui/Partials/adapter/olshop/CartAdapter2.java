package com.otto.mart.ui.Partials.adapter.olshop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.cart.SupplierCartGroup;
import com.otto.mart.model.APIModel.Response.olshop.cart.SupplierCartItem;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter2 extends RecyclerView.Adapter<CartAdapter2.CartViewHolder> {

    List<SupplierCartGroup> supplier;
    private CartItemListener listener;

    public CartAdapter2(CartItemListener listener) {
        this.listener = listener;
        supplier = new ArrayList<>();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_olshop_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(supplier.get(position));
        holder.productAdapter.setListener(listener);
    }

    @Override
    public int getItemCount() {
        return supplier.size();
    }

    public void setSupplier(List<SupplierCartGroup> supplier) {
        this.supplier = supplier;
        notifyDataSetChanged();
    }

    public void deleteItem(int parentPos, int childPos) {
        if (supplier.get(parentPos).getCart_items().size() > 1) {
            supplier.get(parentPos).getCart_items().remove(childPos);
            notifyItemChanged(parentPos);
        } else {
            supplier.remove(parentPos);
            notifyItemRemoved(parentPos);
        }
    }

    public void setChecked(boolean isChecked) {
        for (SupplierCartGroup item : supplier) {
            item.setSelected(isChecked);
        }

        notifyDataSetChanged();
    }

    public List<SupplierCartGroup> getSupplier() {
        return supplier;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView supplierName;
        private CheckBox supplierButton;
        private CircleImageView supplierImage;
        private CartProductAdapter2 productAdapter;
        private RecyclerView productList;

        CartViewHolder(View itemView) {
            super(itemView);
            supplierName = itemView.findViewById(R.id.supplierName);
            supplierButton = itemView.findViewById(R.id.supplierButton);
            supplierImage = itemView.findViewById(R.id.supplierImage);
            productList = itemView.findViewById(R.id.productList);
            productAdapter = new CartProductAdapter2(new ArrayList<>());
            productList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            productList.setHasFixedSize(true);
            productList.setAdapter(productAdapter);
            productList.setNestedScrollingEnabled(false);
        }

        void bind(SupplierCartGroup item) {
            supplierName.setText(item.getSupplierName());
            productAdapter.setProductList(item.getCart_items());
            Glide.with(supplierImage.getContext()).applyDefaultRequestOptions(
                    new RequestOptions()
                            .error(R.drawable.image_placeholder)
                            .placeholder(R.drawable.image_placeholder))
                    .load("").into(supplierImage);

            supplierButton.setChecked(item.isSelected());
            productAdapter.setParentPos(getAdapterPosition());

            supplierButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setSelected(isChecked);
                for (SupplierCartItem product : item.getCart_items()) {
                    product.setSelected(isChecked);

                }
                new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(getAdapterPosition());
                    }
                };
                productAdapter.notifyDataSetChanged();
            });

//            if (item.isSelected()) {
//                supplierButton.setChecked(true);
//            }
        }
    }

    public interface CartItemListener {
        void onItemDelete(int parentPos, int childPos, Object data);

        void onItemChanged(int cartItemId, int quantity);

        void onItemChecked(int pos, boolean isChecked, int parentPos);
    }
}
