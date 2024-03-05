package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ActionButtonModel;

import java.util.ArrayList;
import java.util.List;

public class IntentActionAdapter extends RecyclerView.Adapter<IntentActionAdapter.hundHolder> {
    final String TAG = getClass().getSimpleName();
    private List<ActionButtonModel> models = new ArrayList<>();
    private Context mContext;
    private int LayoutID;

    public IntentActionAdapter(int layoutID, Context mContext) {
        this.LayoutID = layoutID;
        this.mContext = mContext;
    }

    public void updateAdapter(List<ActionButtonModel> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public hundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(LayoutID, parent, false);
        return new hundHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final hundHolder holder, int position) {
        final ActionButtonModel model = models.get(position);
        holder.TextContent.setText(model.getTextContent());
        try {
            if (holder.imageContent != null) {
                holder.imageContent.setImageResource(model.getImageResourceContent());
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        holder.hitbox.setOnClickListener(
                model.getOnClickActionContent()
        );

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class hundHolder extends RecyclerView.ViewHolder {
        private View hitbox;
        private TextView TextContent;
        private ImageView imageContent;

        public hundHolder(View itemView) {
            super(itemView);
            hitbox = itemView.findViewById(R.id.itemLayout);
            try {
                // TODO: 7/10/2020 make sure the usability
//                imageContent = itemView.findViewById(R.id.imageView);
            } catch (Exception e) {
            }
            try {
                TextContent = itemView.findViewById(R.id.text);
            } catch (Exception e) {
            }
        }
    }
}
