package com.otto.mart.ui.Partials.adapter.ppob.topUpEmoney;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.topUpEmoney.TopUpEmoneyProductResponseModel;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class TopUpProductAdapter extends RecyclerView.Adapter<TopUpProductAdapter.ViewHolder> {

    private Context mContext;
    public List<TopUpEmoneyProductResponseModel.Data.Denomination> mDataset;

    private ItemClickListener itemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName;
        public ImageView imgLogo;
        public LinearLayout masterLayout;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            imgLogo = (ImageView) view.findViewById(R.id.img_logo);
            masterLayout = view.findViewById(R.id.master_layout);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TopUpProductAdapter(Context context, List<TopUpEmoneyProductResponseModel.Data.Denomination> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TopUpProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wallet_product_grid, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TopUpEmoneyProductResponseModel.Data.Denomination item = mDataset.get(position);

        if (item.isSelected()) {
            holder.masterLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border_blue_line));
        } else {
            holder.masterLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border_gey_line));
        }

        Glide.with(mContext)
                .load(item.getLogo())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(holder.imgLogo);

        holder.tvName.setText(item.getProduct_name());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface ItemClickListener{
        void itemClicked(TopUpEmoneyProductResponseModel.Data.Denomination item, int position);
    }
}