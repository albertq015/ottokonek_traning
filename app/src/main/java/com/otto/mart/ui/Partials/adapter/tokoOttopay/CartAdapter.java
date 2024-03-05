package com.otto.mart.ui.Partials.adapter.tokoOttopay;

import android.content.Context;
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

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.Cart;
import com.otto.mart.support.util.DataUtil;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context mContext;
    public List<Cart> mDataset;

    private OnCartButtonListener cartButtonListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CheckBox checkbox;
        public TextView tvName;
        public EditText etQty;
        public TextView tvPrice;
        public TextView tvDesc;
        public ImageView imgDelete;
        public ImageView imgPlus;
        public ImageView imgMin;

        public ViewHolder(View v) {
            super(v);
            checkbox = v.findViewById(R.id.checkbox);
            tvName = v.findViewById(R.id.tv_name);
            etQty = v.findViewById(R.id.et_qty);
            tvPrice = v.findViewById(R.id.tv_price);
            tvDesc = v.findViewById(R.id.tv_desc);
            imgDelete = v.findViewById(R.id.img_delete);
            imgPlus = v.findViewById(R.id.img_plus);
            imgMin = v.findViewById(R.id.img_minus);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CartAdapter(Context context, List<Cart> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Cart item = mDataset.get(position);

        holder.tvName.setText(item.getProduct_name());
        holder.etQty.setText("" + item.getQuantity());
        holder.tvPrice.setText(DataUtil.convertCurrency(item.getItem_price()));

        holder.checkbox.setChecked(item.isSelected());
        holder.etQty.setText("" + item.getQuantity());

        holder.checkbox.setOnClickListener(v -> {
            cartButtonListener.checkBoxClicked(item, position);
        });

        holder.imgDelete.setOnClickListener(v -> {
            cartButtonListener.deleteClicked(item, position);
        });

        holder.imgPlus.setOnClickListener(v -> {
            if (!holder.etQty.getText().toString().equals("")) {
                int newQty = Integer.valueOf(holder.etQty.getText().toString()) + 1;
                holder.etQty.setText("" + newQty);
                holder.etQty.setSelection(holder.etQty.getText().length());
            } else {
                holder.etQty.setText("0");
            }
        });

        holder.imgMin.setOnClickListener(v -> {
            if (!holder.etQty.getText().toString().equals("")) {
                int newQty = Integer.valueOf(holder.etQty.getText().toString()) - 1;
                if (newQty >= 0) {
                    holder.etQty.setText("" + newQty);
                    holder.etQty.setSelection(holder.etQty.getText().length());
                }

            } else {
                holder.etQty.setText("0");
            }
        });

        addTextWatcher(holder.etQty, item, position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setItemClickListener(OnCartButtonListener onCartButtonListener) {
        this.cartButtonListener = onCartButtonListener;
    }

    public interface OnCartButtonListener {
        void onPlusButtonCLicked(Cart cart, int qty, int position);

        void onMinButtonCLicked(Cart cart, int qty, int position);

        void deleteClicked(Cart item, int position);

        void checkBoxClicked(Cart item, int position);

        void onQtyChanged(Cart cart, int qty, int position);
    }

    public void setCartButtonListener(OnCartButtonListener cartButtonListener) {
        this.cartButtonListener = cartButtonListener;
    }

    public void addTextWatcher(EditText input, Cart cart, int position) {
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    input.setText("0");
                    input.setSelection(1);
                } else {
                    if(charSequence.length() > 1){
                        if(charSequence.toString().substring(0, 1).equals("0")){
                            input.setText(charSequence.toString().substring(1, charSequence.length()));
                            input.setSelection(charSequence.length() - 1);
                        }
                    }
                }

                if(!charSequence.toString().equals("")){
                    cartButtonListener.onQtyChanged(cart, Integer.valueOf(charSequence.toString()), position);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }
}