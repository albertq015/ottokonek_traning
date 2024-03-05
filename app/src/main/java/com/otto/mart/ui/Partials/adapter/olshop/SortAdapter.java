package com.otto.mart.ui.Partials.adapter.olshop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.SortModel;
import com.otto.mart.ui.Partials.RecyclerViewListener;

import java.util.ArrayList;
import java.util.List;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.SortViewHolder> {

    private List<SortModel> sortModels = new ArrayList<>();
    private View selectedView;
    private int prevPos = -1;
    private RecyclerViewListener listener;

    public SortAdapter(int lastPos, RecyclerViewListener listener) {
        this.listener = listener;
        sortModels.add(new SortModel("Terbaru", "newest"));
//        sortModels.add(new SortModel("Terpopuler", "popular"));
//        sortModels.add(new SortModel("Penjualan", "sales"));
//        sortModels.add(new SortModel("Ulasan", "review"));
        sortModels.add(new SortModel("Harga terendah", "cheapest"));
        sortModels.add(new SortModel("Harga tertinggi", "expensive"));
        sortModels.add(new SortModel("Diskon", "discount"));
        sortModels.get(lastPos).setSelected(true);
        prevPos = lastPos;
    }

    @NonNull
    @Override
    public SortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SortViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sort, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SortViewHolder holder, int position) {
        sortModels.get(position).setPos(position);
        holder.sortName.setText(sortModels.get(position).getName());
        if (sortModels.get(position).isSelected()) {
            selectedView = holder.imageSelect;
            holder.imageSelect.setVisibility(View.VISIBLE);
        } else holder.imageSelect.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return sortModels.size();
    }

    class SortViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView sortName;
        private ImageView imageSelect;

        SortViewHolder(View itemView) {
            super(itemView);
            sortName = itemView.findViewById(R.id.sortName);
            imageSelect = itemView.findViewById(R.id.imageSelected);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (selectedView != null) {
                selectedView.setVisibility(View.GONE);
            }
            selectedView = imageSelect;
            imageSelect.setVisibility(View.VISIBLE);

            if (prevPos != -1) {
                sortModels.get(prevPos).setSelected(false);
            }
            prevPos = getAdapterPosition();
            sortModels.get(getAdapterPosition()).setSelected(true);

            if (listener != null) {
                listener.onItemClick(0, getAdapterPosition(), sortModels.get(getAdapterPosition()));
            }
        }
    }
}
