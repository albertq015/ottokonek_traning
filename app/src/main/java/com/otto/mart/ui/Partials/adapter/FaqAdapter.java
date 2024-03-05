package com.otto.mart.ui.Partials.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.Faqmodel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.hundHolder> {
    private List<Faqmodel> models;
    private int layoutID;

    public FaqAdapter(List<Faqmodel> models, int layoutID) {
        this.models = models;
        this.layoutID = layoutID;
    }

    @NonNull
    @Override
    public hundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return new FaqAdapter.hundHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final hundHolder holder, int position) {
        Faqmodel model = models.get(position);
        holder.title.setText(model.getQuestion());
        holder.content.setText(model.getAnswer());
        holder.setIsRecyclable(false);
        holder.hitbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.icon.setRotation(holder.icon.getRotation() + 180);
                holder.hitbox.setVisibility(View.VISIBLE);
                holder.expandong.toggle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class hundHolder extends RecyclerView.ViewHolder {
        private TextView title, content;
        private ImageView icon;
        private View hitbox;
        private ExpandableLayout expandong;

        private boolean state;

        public hundHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.contentContainer);
            hitbox = itemView.findViewById(R.id.col_hitbox);
            expandong = itemView.findViewById(R.id.expandong);
            icon = itemView.findViewById(R.id.col_icon);
        }

        public boolean getState() {
            return state;
        }

        public void switchState() {
            state = !state;
        }
    }
}
