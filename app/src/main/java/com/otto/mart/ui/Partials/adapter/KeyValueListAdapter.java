package com.otto.mart.ui.Partials.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.ui.component.LazyTextview;

import java.util.ArrayList;
import java.util.List;

public class KeyValueListAdapter extends RecyclerView.Adapter<KeyValueListAdapter.KudaAdapter> {
    private List<WidgetBuilderModel> models = new ArrayList<>();
    private int layoutID;

    public KeyValueListAdapter(int layoutID) {
        this.layoutID = layoutID;
    }

    public KeyValueListAdapter(List<WidgetBuilderModel> models, int layoutID) {
        if (models != null)
            this.models = models;
        this.layoutID = layoutID;
    }

    @NonNull
    @Override
    public KudaAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return new KeyValueListAdapter.KudaAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KudaAdapter holder, int position) {
        WidgetBuilderModel model = models.get(position);

        holder.let_tv.setTitle(model.getKey() != null ? model.getKey() : "");
        holder.let_tv.setText(model.getValue() != null ? model.getValue() : "");

        //Petunjuk Penggunaan Voucher Game
        if (holder.verticalContent != null) {
            holder.verticalContent.setGravity(Gravity.RIGHT);
            if (model.getKey().equalsIgnoreCase("petunjuk penggunaan")) {
                holder.verticalContent.setGravity(Gravity.LEFT);
                holder.tvVerticalKey.setText(model.getKey());
                holder.tvVerticalValue.setText(model.getValue());
                holder.tvVerticalValue.setGravity(Gravity.LEFT);

                holder.let_tv.setVisibility(View.GONE);
                holder.verticalContent.setVisibility(View.VISIBLE);
            } else if (model.getKey().equalsIgnoreCase("commission")) {
                holder.tvVerticalKey.setText(holder.tvVerticalKey.getContext().getString(R.string.text_comission));
                holder.tvVerticalValue.setText(model.getValue());
            } else {
                holder.let_tv.setTitle(model.getKey() != null ? model.getKey() : "");
                holder.let_tv.setText(model.getValue() != null ? model.getValue() : "");

                holder.let_tv.setVisibility(View.VISIBLE);
                holder.verticalContent.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void replaceModel(List<WidgetBuilderModel> models) {
        this.models = models;
        this.notifyDataSetChanged();
    }

    public void clearModel() {
        this.models.clear();
        this.notifyDataSetChanged();
    }

    public class KudaAdapter extends RecyclerView.ViewHolder {
        LazyTextview let_tv;
        LinearLayout verticalContent;
        TextView tvVerticalKey;
        TextView tvVerticalValue;

        public KudaAdapter(View itemView) {
            super(itemView);
            let_tv = itemView.findViewById(R.id.let_item);
            verticalContent = itemView.findViewById(R.id.vertical_content);
            tvVerticalKey = itemView.findViewById(R.id.tv_vertical_key);
            tvVerticalValue = itemView.findViewById(R.id.tv_vertical_value);
        }
    }
}
