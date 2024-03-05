package com.otto.mart.ui.Partials.adapter.ottopoint;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.DealsItem;
import com.otto.mart.support.util.MessageHelper;
import com.otto.mart.ui.actionView.ActionListItemTwo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DealsMenuAdapter extends RecyclerView.Adapter<DealsMenuAdapter.ViewHolder>{

    private Context context;
    private List<DealsItem> mItems;
    private ActionListItemTwo callback;

    public DealsMenuAdapter(Context context, List<DealsItem> mItems){
        this.context = context;
        this.mItems = mItems;
    }

    public DealsMenuAdapter(Context context, List<DealsItem> mItems, ActionListItemTwo callback){
        this.context = context;
        this.mItems = mItems;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_deals_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // set view
        holder.tvTitle.setText(mItems.get(position).getTitle());

        if(mItems.get(position).getImage() != null)
            holder.img.setImageDrawable(mItems.get(position).getImage());

        // events
        holder.view.setOnClickListener(view -> {
            if(callback != null)
                callback.actionItem(position, null);
            else
                MessageHelper.commingSoonKategoriDialog(context);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.view = itemView;
        }
    }
}
