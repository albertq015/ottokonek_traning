package com.otto.mart.ui.Partials.adapter.voucherGame;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel;
import com.otto.mart.model.APIModel.Misc.voucherGame.VoucherGameData;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class VoucherGameAdapter extends RecyclerView.Adapter<VoucherGameAdapter.ViewHolder> {

    private Context mContext;
    public List<OttoagDenomModel> mDataset;

    private ItemClickListener itemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvName;
        public ImageView imgLogo;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            imgLogo = (ImageView) view.findViewById(R.id.img_logo);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public VoucherGameAdapter(Context context, List<OttoagDenomModel> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VoucherGameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_voucher_game, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        OttoagDenomModel item = mDataset.get(position);

        Glide.with(mContext)
                .load(item.getLogo())
                .into(holder.imgLogo);

        holder.tvName.setText(item.getProduct_name());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface ItemClickListener{
        void itemClicked(VoucherGameData item, int position);
    }
}