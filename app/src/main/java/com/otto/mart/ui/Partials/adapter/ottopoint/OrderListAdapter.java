package com.otto.mart.ui.Partials.adapter.ottopoint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.OrderListItemModel;
import com.otto.mart.ui.actionView.ActionListItemTwo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private List<OrderListItemModel> mItems;
    private ActionListItemTwo callback;

    public OrderListAdapter(List<OrderListItemModel> mItems, ActionListItemTwo callback){
        this.mItems = mItems;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_order_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvTitle.setText(mItems.get(i).getTitle());
        viewHolder.img.setSelected(mItems.get(i).isSelected());

        viewHolder.view.setOnClickListener(view -> {
            setSelectedItem(i);

            callback.actionItem(i, null);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.img)
        ImageView img;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.view = itemView;
        }
    }

    public void setSelectedItem(int position){
        for (int i = 0; i < mItems.size(); i++)
            mItems.get(i).setSelected(!mItems.get(i).isSelected() && i == position);

        notifyDataSetChanged();
    }
}
