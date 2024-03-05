package com.otto.mart.ui.Partials.adapter.ottopoint;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ProductIconModel;
import com.otto.mart.support.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

public class PointDemonAdapter extends RecyclerView.Adapter<PointDemonAdapter.PointDenomViewHolder> {

    private List<ProductIconModel> models;
    private List<String> selectedPos;
    private int unselectedItemColor = R.color.colorTransparent;
    private int unselectedItemTextColor = R.color.charcoal_grey;
    private int unselectedItemText2Color = R.color.ocean_blue;
    private int selectedItemLayout = R.drawable.border_blue_line;
    private int unselectedItemLayout = R.drawable.border_gey_line;
    private int selectedItemColor = R.color.ocean_blue;
    private int selectedItemTextColor = R.color.color_white;

    public PointDemonAdapter(List<ProductIconModel> models) {
        this.models = models;
        selectedPos = new ArrayList<>();
    }

    @NonNull
    @Override
    public PointDenomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PointDenomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pulsa, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PointDenomViewHolder holder, int position) {
        holder.productPrice.setText(models.get(position).getProductPrice());
        holder.productDiscountPrice.setText(DataUtil.cleanDigit(models.get(position).getProductDiscountPrice()) + " " + DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(models.get(position).getProductDiscountPrice())));

        holder.hitbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (models.get(position).isSelected()) {
                    if (selectedPos.contains(String.valueOf(position)))
                        selectedPos.remove(String.valueOf(position));
                    holder.deselect();
                    models.get(position).setSelected(false);
                } else {
                    deselectAnotherSelectedItem(position);

                    if (!selectedPos.contains(String.valueOf(position)))
                        selectedPos.add(String.valueOf(position));

                    holder.select();
                    models.get(position).setSelected(true);
                }
            }
        });

        if (models.get(position).isSelected()) {
            holder.select();
        } else {
            holder.deselect();
        }
    }

    @Override
    public int getItemCount() {
        return models != null ? models.size() : 0;
    }

    public void setModels(List<ProductIconModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    public ProductIconModel getSelectedItem(){
        ProductIconModel result = null;

        for (ProductIconModel item: models){
            if(item.isSelected()){
                result = item;
                break;
            }
        }

        return result;
    }

    private void deselectAnotherSelectedItem(int selectedPosition){
        for (int i = 0; i < models.size(); i++)
            if(models.get(i).isSelected() && i != selectedPosition)
                models.get(i).setSelected(false);

        notifyDataSetChanged();
    }

    class PointDenomViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup hitbox;
        private ImageView icon;
        private TextView title, productPrice, productDiscountPrice;

        PointDenomViewHolder(View itemView) {
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
                hitbox.setBackgroundResource(selectedItemLayout);
            }
        }

        public void deselect() {
            try {
                ((CardView) hitbox).setCardBackgroundColor(ContextCompat.getColor(hitbox.getContext(), unselectedItemColor));
                title.setTextColor(ContextCompat.getColor(title.getContext(), unselectedItemTextColor));
                productPrice.setTextColor(ContextCompat.getColor(productPrice.getContext(), unselectedItemTextColor));
                productDiscountPrice.setTextColor(ContextCompat.getColor(productDiscountPrice.getContext(), unselectedItemText2Color));
            } catch (Exception e) {
                hitbox.setBackgroundResource(unselectedItemLayout);
            }
        }
    }
}
