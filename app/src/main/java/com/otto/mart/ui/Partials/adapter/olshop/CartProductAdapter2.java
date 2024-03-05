package com.otto.mart.ui.Partials.adapter.olshop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.cart.SupplierCartItem;
import com.otto.mart.support.util.DataUtil;

import java.util.List;

public class CartProductAdapter2 extends RecyclerView.Adapter<CartProductAdapter2.CartProductViewHolder> {

    List<SupplierCartItem> productList;
    private CartAdapter2.CartItemListener listener;
    private int parentPos;

    public CartProductAdapter2(List<SupplierCartItem> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductList(List<SupplierCartItem> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public void setParentPos(int parentPos) {
        this.parentPos = parentPos;
    }

    public void setListener(CartAdapter2.CartItemListener listener) {
        this.listener = listener;
    }

    class CartProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView productImage, buttonPlus, buttonMinus, deleteCart;
        private TextView productName, productPrice, productVariant;
        private EditText quantity;
        private CheckBox chooseButton;
        private TextWatcher textChange;

        CartProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
            deleteCart = itemView.findViewById(R.id.deleteCart);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            chooseButton = itemView.findViewById(R.id.chooseButton);
            productVariant = itemView.findViewById(R.id.productVariant);

            buttonMinus.setOnClickListener(this);
            buttonPlus.setOnClickListener(this);
        }

        void bind(SupplierCartItem item) {
            if (textChange != null) quantity.removeTextChangedListener(textChange);
            long price;
            if (item.getOttomart_discount_price() > 0) {
                price = Math.min(item.getOttomart_price(), item.getOttomart_discount_price()) + (long) Double.parseDouble(item.getSales_commission());
            } else {
                price = item.getOttomart_discount_price() + (long) Double.parseDouble(item.getSales_commission());
            }
//            long price = (long) Double.parseDouble(item.getItem_price()) + (long) Double.parseDouble(item.getSales_commission());
            productName.setText(item.getProduct_name());
            productPrice.setText(DataUtil.convertCurrency(price));
            quantity.setText(String.valueOf(item.getQuantity()));
            Glide.with(productImage).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder)).load(item.getImage()).into(productImage);
            deleteCart.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemDelete(item.getParentPos(), getAdapterPosition(), item);
                }
            });
            chooseButton.setChecked(item.isSelected());
            chooseButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                System.out.println();
                if (listener != null) {
                    listener.onItemChecked(getAdapterPosition(), isChecked, parentPos);
                }
            });
            productVariant.setText(item.getVariant());
            setTextChange();
        }

        private void setTextChange() {
            if (textChange == null) {
                textChange = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (listener != null && s.length() > 0) {
                            listener.onItemChanged(productList.get(getAdapterPosition()).getCart_item_id(), Integer.parseInt(s.toString()));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
            }
            quantity.addTextChangedListener(textChange);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonMinus: {
                    int min = Integer.parseInt(quantity.getText().toString());
                    if (min - 1 > 0) {
                        quantity.setText(String.valueOf(min - 1));
                    }
                    break;
                }
                case R.id.buttonPlus: {
                    int max = Integer.parseInt(quantity.getText().toString()) + 1;
                    quantity.setText(String.valueOf(max));
                    break;
                }
            }
        }

        private boolean isSelectedAll() {
            for (SupplierCartItem item : productList) {
                if (!item.isSelected()) {
                    return false;
                }
            }
            return true;
        }
    }
}
