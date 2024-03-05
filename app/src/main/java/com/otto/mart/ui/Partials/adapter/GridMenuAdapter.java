package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.GridMenu;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class GridMenuAdapter extends RecyclerView.Adapter<GridMenuAdapter.ViewHolder> {

    private Context mContext;
    public List<GridMenu> mDataset;

    public OnViewClickListener mOnViewClickListener;

    private boolean isFromHome = false;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout itemLayout;
        public TextView tvName;
        public ImageView imgIcon;

        public ViewHolder(View v) {
            super(v);
            itemLayout = v.findViewById(R.id.item_layout);
            tvName = v.findViewById(R.id.tv_name);
            imgIcon = v.findViewById(R.id.img_icon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GridMenuAdapter(Context context, List<GridMenu> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)x
    @Override
    public GridMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_wallet_menu;

        if(isFromHome){
            layout = R.layout.item_wallet_menu_home;
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        GridMenu item = mDataset.get(position);

        holder.imgIcon.setImageDrawable(ContextCompat.getDrawable(mContext, item.getIcon()));

        holder.tvName.setText(item.getName());

        holder.itemLayout.setOnClickListener(v -> {
            if (mOnViewClickListener != null) {
                mOnViewClickListener.onViewClickListener(item, position);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setmOnViewClickListener(OnViewClickListener mOnViewClickListener) {
        this.mOnViewClickListener = mOnViewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClickListener(GridMenu gridMenu, int position);
    }

    public void setFromHome(boolean fromHome) {
        isFromHome = fromHome;
    }
}