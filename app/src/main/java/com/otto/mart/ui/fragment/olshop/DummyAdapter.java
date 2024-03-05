package com.otto.mart.ui.fragment.olshop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.olshop.Product;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.Partials.RecyclerViewListener;
import com.otto.mart.ui.component.CustomGlideTarget;
import com.otto.mart.ui.component.SquareImageByWidth;

import java.util.List;

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.DummyViewHolder> {

    private int layout;
    private List<Product> productList;
    private RecyclerViewListener listener;

    public DummyAdapter(int layout, List<Product> productList, RecyclerViewListener listener) {
        this.layout = layout;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DummyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DummyViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DummyViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class DummyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView discount, price, specialPrice, name;
        SquareImageByWidth image;
        View discountBadge;

        DummyViewHolder(View itemView) {
            super(itemView);
            discount = itemView.findViewById(R.id.discount);
            price = itemView.findViewById(R.id.productPrice);
            specialPrice = itemView.findViewById(R.id.specialPrice);
            name = itemView.findViewById(R.id.productName);
            image = itemView.findViewById(R.id.productImage);
            discountBadge = itemView.findViewById(R.id.discountBadge);

            itemView.setOnClickListener(this);
        }

        void bind(Product product) {
            name.setText(product.getTitle());

            image.setImageResource(R.drawable.rounded);
            setImage(product);

            if (product.getDiscount_percentage() > 0 && product.getOttomart_discount_price() != product.getOttomart_price()) {
                price.setText(DataUtil.convertCurrency(product.getOttomart_discount_price()));
                specialPrice.setText(DataUtil.convertCurrency(product.getOttomart_price()));
                discount.setText(product.getDiscount_percentage() + "%");
                discountBadge.setVisibility(View.VISIBLE);
                specialPrice.setVisibility(View.VISIBLE);
            } else {
                price.setText(DataUtil.convertCurrency(product.getOttomart_price()));
                discountBadge.setVisibility(View.GONE);
                specialPrice.setVisibility(View.GONE);
            }
        }

        private void setImage(Product product) {
            Glide.with(image)
                    .asBitmap()
                    .load(product.getMain_image_url())
                    .into(new CustomGlideTarget(image,8,R.drawable.rounded));
        }


        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(0, getAdapterPosition(), productList.get(getAdapterPosition()));
            }
        }

        private int calculateDiscount(long price, long special) {
            return (int) ((float) ((((double) price - (double) special) / price) * 100));
        }
    }
}
