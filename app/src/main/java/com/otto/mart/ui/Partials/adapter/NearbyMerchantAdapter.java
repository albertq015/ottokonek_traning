package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.nearbyMerchant.NearbyMerchant;

import java.util.List;

public class NearbyMerchantAdapter extends RecyclerView.Adapter<NearbyMerchantAdapter.ViewHolder> {

    private Context mContext;
    public List<NearbyMerchant> mDataset;
    private Listener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvDistance;
        public TextView tvName;
        public TextView tvAddress;
        public TextView tvAddress2;
        public Button btnLocation;
        public Button btnDetail;

        public ViewHolder(View v) {
            super(v);
            tvDistance = (TextView) v.findViewById(R.id.tv_distance);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvAddress = (TextView) v.findViewById(R.id.tv_address);
            //tvAddress2 = (TextView) v.findViewById(R.id.tv_address2);
            btnLocation = (Button) v.findViewById(R.id.btn_location);
            btnDetail = (Button) v.findViewById(R.id.btn_detail);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NearbyMerchantAdapter(Context context, List<NearbyMerchant> myDataset, Listener listener) {
        mContext = context;
        mDataset = myDataset;
        mListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NearbyMerchantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nearby_merchant, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NearbyMerchant item = mDataset.get(position);

        holder.tvName.setText(item.getCustomerName());
        holder.tvDistance.setText(String.format("%.1f", item.getDistance()) + " Km");
        holder.tvAddress.setText(item.getAddress());
        //holder.tvAddress2.setText(item.getAddress2());

        holder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLocationClick(item);
            }
        });

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDetailClick(item);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface Listener {
        void onLocationClick(NearbyMerchant merchant);
        void onDetailClick(NearbyMerchant merchant);
    }
}
