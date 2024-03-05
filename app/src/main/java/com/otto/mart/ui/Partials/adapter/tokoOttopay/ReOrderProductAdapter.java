package com.otto.mart.ui.Partials.adapter.tokoOttopay;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.ReOrderProduct;
import com.otto.mart.support.util.DataUtil;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class ReOrderProductAdapter extends RecyclerView.Adapter<ReOrderProductAdapter.ViewHolder> {

    private Context mContext;
    public List<ReOrderProduct> mDataset;

    public OnCartButtonListener cartButtonListener;

    private int mMinOrder = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout headerLayout;
        public TextView tvStoreName;
        public TextView tvStoreDesc;
        public TextView tvName;
        public EditText etQty;
        public TextView tvPrice;
        public TextView tvDesc;
        public ImageView imgPlus;
        public ImageView imgMin;

        public ViewHolder(View v) {
            super(v);
            headerLayout = v.findViewById(R.id.header_layout);
            tvStoreName = v.findViewById(R.id.tv_store_name);
            tvStoreDesc = v.findViewById(R.id.tv_store_desc);
            tvName = v.findViewById(R.id.tv_name);
            etQty = v.findViewById(R.id.et_qty);
            tvPrice = v.findViewById(R.id.tv_price);
            tvDesc = v.findViewById(R.id.tv_desc);
            etQty = v.findViewById(R.id.et_qty);
            imgPlus = v.findViewById(R.id.img_plus);
            imgMin = v.findViewById(R.id.img_minus);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReOrderProductAdapter(Context context, List<ReOrderProduct> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReOrderProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reorder_product, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ReOrderProduct item = mDataset.get(position);

        if (position == 0) {
            holder.headerLayout.setVisibility(View.VISIBLE);
        } else {
            holder.headerLayout.setVisibility(View.GONE);
        }

        holder.tvStoreName.setText("Indoeskrim");
        holder.tvStoreDesc.setText(mContext.getString(R.string.text_min_order) + DataUtil.convertCurrency(mMinOrder));

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(DataUtil.convertCurrency(DataUtil.getInt(item.getPrice())) + " / Dus");
        holder.tvDesc.setText(item.getDescription());

        holder.etQty.setText("" + item.getQty());

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

    public void setmMinOrder(int mMinOrder) {
        this.mMinOrder = mMinOrder;
    }

    public interface OnCartButtonListener {
        void onPlusButtonCLicked(ReOrderProduct reOrderProduct, int qty, int position);

        void onMinButtonCLicked(ReOrderProduct reOrderProduct, int qty, int position);

        void onQtyChanged(ReOrderProduct reOrderProduct, int qty, int position);
    }

    public void setCartButtonListener(OnCartButtonListener cartButtonListener) {
        this.cartButtonListener = cartButtonListener;
    }

    public void addTextWatcher(EditText input, ReOrderProduct reOrderProduct, int position) {
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
                    cartButtonListener.onQtyChanged(reOrderProduct, Integer.valueOf(charSequence.toString()), position);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }
}