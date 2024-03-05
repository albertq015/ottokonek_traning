package com.otto.mart.ui.Partials.adapter.ppob.donasiOld;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class DonasiConfirmationAdapter extends RecyclerView.Adapter<DonasiConfirmationAdapter.ViewHolder> {

    private Context mContext;
    public List<WidgetBuilderModel> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvKey;
        public TextView tvValue;

        public ViewHolder(View v) {
            super(v);
            tvKey = v.findViewById(R.id.tv_key);
            tvValue = v.findViewById(R.id.tv_value);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DonasiConfirmationAdapter(Context context, List<WidgetBuilderModel> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)x
    @Override
    public DonasiConfirmationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donasi_confirmation, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        WidgetBuilderModel item = mDataset.get(position);

        holder.tvKey.setText(item.getKey());
        holder.tvValue.setText(item.getValue());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}