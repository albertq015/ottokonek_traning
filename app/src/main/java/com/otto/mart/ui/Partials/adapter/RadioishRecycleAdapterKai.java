package com.otto.mart.ui.Partials.adapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ProductIconModel;
import com.otto.mart.support.util.DataUtil;

import java.util.List;

public class RadioishRecycleAdapterKai extends RecyclerView.Adapter<RadioishRecycleAdapterKai.schweinHolder> {
    private List<ProductIconModel> models;
    private int layoutID;
    private int selectedPos = -1;
    private int unselectedItemColor = R.color.colorTransparent;
    private int unselectedItemTextColor = R.color.charcoal_grey;
    private int unselectedItemText2Color = R.color.ocean_blue;
    private int selectedItemLayout = R.drawable.border_blue_line;
    private int unselectedItemLayout = R.drawable.border_gey_line;
    private int selectedItemColor;
    private int selectedItemTextColor;
    private boolean isPaketData;

    private onItemSelectedListener listener;


    public RadioishRecycleAdapterKai(List<ProductIconModel> models, int layoutID, int unselectedItemColor, int unselectedItemTextColor, int unselectedItemText2Color, int selectedItemColor, int selectedItemTextColor) {
        this.models = models;
        this.layoutID = layoutID;
        this.unselectedItemColor = unselectedItemColor == 0 ? this.unselectedItemColor : unselectedItemColor;
        this.unselectedItemTextColor = unselectedItemTextColor == 0 ? this.unselectedItemTextColor : unselectedItemTextColor;
        this.unselectedItemText2Color = unselectedItemText2Color == 0 ? this.unselectedItemText2Color : unselectedItemText2Color;
        this.selectedItemColor = selectedItemColor;
        this.selectedItemTextColor = selectedItemTextColor;
    }

    public void setListener(onItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public schweinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return new schweinHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull schweinHolder holder, int position) {
        final ProductIconModel model = models.get(position);
        if (isPaketData) {
            holder.title.setText(model.getProductPrice() + "");
            holder.productPrice.setText(model.getProductDiscountPrice() + "");
        } else {
            holder.title.setText(DataUtil.cleanDigit(model.getTitle()) + " " + DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(model.getTitle())).replace("Rp. ", ""));
            holder.productPrice.setText(DataUtil.cleanDigit(model.getProductPrice()) + " " + DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(model.getProductPrice())).replace("Rp. ", ""));
        }

        holder.productDiscountPrice.setText(DataUtil.cleanDigit(model.getProductDiscountPrice()) + " " + DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(model.getProductDiscountPrice())));
        try {
            if (model.getIconID() != 0) {
                holder.icon.setImageResource(model.getIconID());
                holder.icon.setVisibility(View.VISIBLE);
                holder.title.setVisibility(View.GONE);
                holder.productPrice.setVisibility(View.GONE);
                holder.productDiscountPrice.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("ICADPT: ", model.getTitle() + " no icon!");
        }

        final int tempPos = position;
        holder.hitbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPos != tempPos) {
                    deselectAll();
                    model.setSelected(true);
                    selectedPos = tempPos;
                    if (listener != null) {
                        listener.onItemSelected(model);
                    }
                } else {
                    model.setSelected(false);
                    selectedPos = -1;
                    if (listener != null) {
                        listener.onListDeselect();
                    }
                }
                notifyDataSetChanged();
            }

        });

        if (model.isSelected()) {
            holder.select();
        } else if (!model.isSelected()) {
            holder.deselect();
        }

    }

    public void deselectAll() {
        for (ProductIconModel model :
                models) {
            model.setSelected(false);
        }
        if (listener != null) {
            listener.onListDeselect();
        }
    }

    public ProductIconModel getSelectedItem() {
        return models.get(selectedPos);
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public ProductIconModel getItemAt(int pos) {
        return models.get(pos);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class schweinHolder extends RecyclerView.ViewHolder {
        private ViewGroup hitbox;
        private ImageView icon;
        private TextView title, productPrice, productDiscountPrice;

        public schweinHolder(View itemView) {
            super(itemView);
            hitbox = itemView.findViewById(R.id.itemLayout);
            icon = itemView.findViewById(R.id.product_imgv);
            title = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDiscountPrice = itemView.findViewById(R.id.product_discount_price);

        }

        public void select() {
            try {
                ((CardView) hitbox).setCardBackgroundColor(ContextCompat.getColor(hitbox.getContext(), selectedItemColor));
                title.setTextColor(ContextCompat.getColor(title.getContext(), selectedItemTextColor));
                productPrice.setTextColor(ContextCompat.getColor(productPrice.getContext(), selectedItemTextColor));
                productDiscountPrice.setTextColor(ContextCompat.getColor(productDiscountPrice.getContext(), selectedItemTextColor));
            } catch (Exception e) {
                ((LinearLayout) hitbox).setBackgroundResource(selectedItemLayout);
            }

        }

        public void deselect() {
            try {
                ((CardView) hitbox).setCardBackgroundColor(ContextCompat.getColor(hitbox.getContext(), unselectedItemColor));
                title.setTextColor(ContextCompat.getColor(title.getContext(), unselectedItemTextColor));
                productPrice.setTextColor(ContextCompat.getColor(productPrice.getContext(), unselectedItemTextColor));
                productDiscountPrice.setTextColor(ContextCompat.getColor(productDiscountPrice.getContext(), unselectedItemText2Color));
            } catch (Exception e) {
                ((LinearLayout) hitbox).setBackgroundResource(unselectedItemLayout);
            }

        }
    }

    public interface onItemSelectedListener {
        void onItemSelected(ProductIconModel selectedModel);

        void onListDeselect();
    }

}