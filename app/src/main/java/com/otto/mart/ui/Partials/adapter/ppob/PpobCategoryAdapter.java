package com.otto.mart.ui.Partials.adapter.ppob;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.CategoryModel;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class PpobCategoryAdapter extends RecyclerView.Adapter<PpobCategoryAdapter.ViewHolder> {

    private Context mContext;
    public List<CategoryModel> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvTitle;
        public ImageView imgLogo;

        public ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_title);
            imgLogo = v.findViewById(R.id.img_logo);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PpobCategoryAdapter(Context context, List<CategoryModel> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PpobCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ppob_category, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CategoryModel item = mDataset.get(position);

        Glide.with(mContext)
                .load(item.getImage())
                .apply(new RequestOptions().placeholder(R.drawable.border_white)
                        .error(R.drawable.border_white))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(holder.imgLogo);

        holder.tvTitle.setText(item.getTitle());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}