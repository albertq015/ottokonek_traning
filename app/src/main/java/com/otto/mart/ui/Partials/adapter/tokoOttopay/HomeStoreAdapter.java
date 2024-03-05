package com.otto.mart.ui.Partials.adapter.tokoOttopay;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.Store;
import com.otto.mart.support.util.UIUtils;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class HomeStoreAdapter extends RecyclerView.Adapter<HomeStoreAdapter.ViewHolder> {

    private Context mContext;
    public List<Store> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName;
        public ImageView imgLogo;

        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            imgLogo = (ImageView) v.findViewById(R.id.img_logo);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeStoreAdapter(Context context, List<Store> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeStoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_store, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Store item = mDataset.get(position);

        int imageWidth = mContext.getResources().getDisplayMetrics().widthPixels / 3;

        UIUtils.resize(holder.imgLogo, imageWidth, imageWidth / 2);

        Glide.with(mContext)
                .load(item.getLogo().getBig().getUrl())
                .into(holder.imgLogo);

        holder.tvName.setText(item.getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}