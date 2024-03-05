package com.otto.mart.ui.Partials.adapter.olshop;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    int level;
    View selectedList;
    View selectedImage;
    private List<Category> categories = new ArrayList<>();
    OnItemCategorySelected listener;
    private int selectedPos = -1;

    public CategoryAdapter(int level) {
        this.level = level;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.item_category_1;
        switch (level) {
            case 1:
                layout = R.layout.item_category_1;
                break;
            case 2:
                layout = R.layout.item_category_2;
                break;
            case 3:
                layout = R.layout.item_category_3;
                break;
        }

        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        switch (level) {
            case 1:
                setupLevelOne(holder, position);
                break;
            case 2:
                setupLevelTwo(holder, position);
                break;
            case 3:
                setupLevelThree(holder, position);
                break;
        }
    }

    private void setupLevelOne(@NonNull CategoryViewHolder holder, int position) {
        if (categories.get(position).isSelected()) {
            if (selectedList != null) {
                selectedList.setBackgroundColor(ContextCompat.getColor(selectedList.getContext(), R.color.white_three));
            }
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorWhite));
            selectedList = holder.itemView;
            selectedPos = position;
            if (listener != null) {
                listener.onItemSelected(categories.get(position));
            }
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white_three));
        }

        holder.catName.setText(categories.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedList != null) {
                    selectedList.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.white_three));
                }
                holder.itemView.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorWhite));
                selectedList = holder.itemView;

                if (selectedPos != -1) {
                    categories.get(selectedPos).setSelected(false);
                }

                categories.get(position).setSelected(true);
                selectedPos = position;

                Category selected = categories.get(position);
                Category all = new Category(new ArrayList<>(), "Semua " + selected.getName(), selected.getIcon(), selected.getId(), false);
                if (categories.get(position).getSub_categories().size() > 0) {
                    if (categories.get(position).getSub_categories().get(0).getId() != all.getId()) {
                        categories.get(position).getSub_categories().add(0, all);
                    }
                } else categories.get(position).getSub_categories().add(0, all);
                listener.onItemSelected(categories.get(position));
            }
        });
    }

    private void setupLevelTwo(@NonNull CategoryViewHolder holder, int position) {
        if (categories.get(position).isSelected()) {
            selectedList = holder.catChild;
        }

        holder.catName.setText(categories.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedList != null) {
                    selectedList.setVisibility(View.GONE);
                }

                if (selectedImage != null) {
                    selectedImage.setRotation(0);
                }

                if (holder.catChild.getVisibility() == View.GONE) {
                    selectedImage = holder.catImage;
                    selectedList = holder.catChild;
                    holder.catImage.setRotation(180);
                    holder.catChild.setVisibility(View.VISIBLE);
                } else {
                    holder.catChild.setVisibility(View.GONE);
                }

                if (categories.get(holder.getAdapterPosition()).getSub_categories().size() == 0) {
                    if (listener != null) {
                        listener.onItemSelected(categories.get(holder.getAdapterPosition()));
                    }
                }
            }
        });

        holder.adapter = new CategoryAdapter(3);
        Category selected = categories.get(position);
        String catName = selected.getName();
        Category all = new Category(new ArrayList<>(), catName.contains("Semua") ? catName : "Semua " + catName, selected.getIcon(), selected.getId(), false);
        if (categories.get(position).getSub_categories().size() > 0) {
            if (categories.get(position).getSub_categories().get(0).getId() != all.getId()) {
                categories.get(position).getSub_categories().add(0, all);
            }
        } else categories.get(position).getSub_categories().add(0, all);
        holder.adapter.setListener(listener);
        holder.catChild.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.catChild.setAdapter(holder.adapter);
        holder.adapter.setCategories(categories.get(position).getSub_categories());
    }

    private void setupLevelThree(@NonNull CategoryViewHolder holder, int position) {
        holder.catName.setText(categories.get(position).getName());
        if (categories.get(position).isSelected()) holder.catImage.setVisibility(View.VISIBLE);
        else holder.catImage.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemSelected(categories.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setListener(OnItemCategorySelected listener) {
        this.listener = listener;
    }

    public void reset(Context context) {
        System.out.println();
        switch (level) {
            case 1: {
                if (selectedPos != -1) {
                    categories.get(selectedPos).setSelected(false);
                    selectedList = null;
                    notifyItemChanged(selectedPos);
                    categories.get(0).setSelected(true);
                    selectedPos = 0;
                    notifyItemChanged(0);
                }
            }
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView catName;
        ImageView catImage;
        RecyclerView catChild;
        CategoryAdapter adapter;

        CategoryViewHolder(View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.catName);
            catImage = itemView.findViewById(R.id.catImage);
            catChild = itemView.findViewById(R.id.catChild);
        }
    }

    public interface OnItemCategorySelected {
        void onItemSelected(Category category);
    }
}
