package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.BusinessCategoryModel;
import com.otto.mart.ui.component.dialog.IChainedDialog;

import java.util.List;

public class ChainedListAdapter extends RecyclerView.Adapter<ChainedListAdapter.KucingHolder> {

    private Context mContext;
    private IChainedDialog parent;
    private List<BusinessCategoryModel> models;
    private int layoutId = R.layout.item_filter_category;
    private int selectedColorId = R.color.color_white;
    private int unSelectedColorId = R.color.white_three;
    private int selectedSeparatorColor = R.color.lanc;
    private int unSelectedSepeparatorColor = R.color.charcoal_grey14;
    private boolean isPrimary;

    private static int checkedPos = 0;
    private boolean isFirstTime = true;

    public ChainedListAdapter(IChainedDialog parent, Context context, List<BusinessCategoryModel> models) {
        mContext = context;
        this.models = models;
        isPrimary = true;
        this.parent = parent;
    }

    public ChainedListAdapter(IChainedDialog parent, Context mContext, List<BusinessCategoryModel> models, int layoutId, int selectedColorId, int unSelectedColorId, int selectedSeparatorColor, int unSelectedSepeparatorColor) {
        this.mContext = mContext;
        this.models = models;
        this.layoutId = layoutId;
        this.selectedColorId = selectedColorId;
        this.unSelectedColorId = unSelectedColorId;
        this.selectedSeparatorColor = selectedSeparatorColor;
        this.unSelectedSepeparatorColor = unSelectedSepeparatorColor;
        isPrimary = false;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ChainedListAdapter.KucingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ChainedListAdapter.KucingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KucingHolder holder, int position) {
        BusinessCategoryModel model = models.get(position);
        if (isFirstTime) {
            isFirstTime = false;
            model.setSelected(true);
        }
        holder.tv.setText(model.getName());
        holder.hitbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAll();
                model.setSelected(true);
                checkedPos = position;
                notifyDataSetChanged();
                if (isPrimary) {
                    parent.primaryCallback(position);
                } else {
                    parent.secondaryCallback(position);
                }
            }
        });
        if (model.isSelected()) {
            holder.hitbox.setBackgroundColor(ContextCompat.getColor(mContext, selectedColorId));
            holder.separator.setBackgroundColor(ContextCompat.getColor(mContext, selectedSeparatorColor));
        } else {
            holder.hitbox.setBackgroundColor(ContextCompat.getColor(mContext, unSelectedColorId));
            holder.separator.setBackgroundColor(ContextCompat.getColor(mContext, unSelectedSepeparatorColor));
        }
    }

    private void deselectAll() {
        for (BusinessCategoryModel model :
                models) {
            model.setSelected(false);
        }
    }

    public void replaceAllItems(List<BusinessCategoryModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class KucingHolder extends RecyclerView.ViewHolder {
        private View hitbox, separator;
        private TextView tv;

        public KucingHolder(View itemView) {
            super(itemView);
            hitbox = itemView.findViewById(R.id.container);
            separator = itemView.findViewById(R.id.category_separator);
            tv = itemView.findViewById(R.id.category);
        }
    }
}
