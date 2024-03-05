package com.otto.mart.ui.Partials.adapter.olshop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.Category;

import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.MainCategoryViewHolder> {

    List<Category> categories;
    CategoryListener listener;

    public MainCategoryAdapter(List<Category> categories, CategoryListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainCategoryViewHolder holder, int position) {
        holder.categoryName.setText(categories.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCategorySelected(categories.get(position));
                }
            }
        });
        Glide.with(holder.categoryImage.getContext()).load("http://ottopay-mart.clappingape.com" + categories.get(position).getIcon()).into(holder.categoryImage);
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    class MainCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;

        MainCategoryViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }
    }

    public interface CategoryListener {
        void onCategorySelected(Category category);
    }
}
