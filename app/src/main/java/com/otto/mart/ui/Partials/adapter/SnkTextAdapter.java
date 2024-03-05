package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.RTItemModel;

import java.util.List;

public class SnkTextAdapter extends RecyclerView.Adapter<SnkTextAdapter.gottHolder> {
    private List<RTItemModel.FAQUModel> models;
    private Context parentContext;

    public SnkTextAdapter(Context context, List<RTItemModel.FAQUModel> models) {
        this.models = models;
        parentContext = context;
    }

    @NonNull
    @Override
    public gottHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.format_snk, parent, false);
        return new gottHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull gottHolder holder, int position) {
        RTItemModel.FAQUModel model = models.get(position);
        holder.title.setText(model.getTitle());
        holder.contentTop.setText(model.getTopContent());
        holder.contentBottm.setText(model.getBottomContent());
        SnkSlaveAdapter adapter = new SnkSlaveAdapter(model.getMidContent());
        holder.contentMidRv.setAdapter(adapter);
        holder.contentMidRv.setLayoutManager(new LinearLayoutManager(parentContext));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class gottHolder extends RecyclerView.ViewHolder {

        private TextView title, contentTop, contentBottm;
        private RecyclerView contentMidRv;

        public gottHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            contentTop = itemView.findViewById(R.id.contenttop);
            contentBottm = itemView.findViewById(R.id.contentbottom);
            contentMidRv = itemView.findViewById(R.id.contentJenk);
        }
    }

    class SnkSlaveAdapter extends RecyclerView.Adapter<SnkSlaveAdapter.AyamHolder> {
        private List<String> models;

        public SnkSlaveAdapter(List<String> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public AyamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.format_snk_bullettext, parent, false);
            return new AyamHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AyamHolder holder, int position) {
            String model = models.get(position);
            holder.content.setText(model);
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class AyamHolder extends RecyclerView.ViewHolder {
            private TextView dot, content;

            public AyamHolder(View itemView) {
                super(itemView);
                dot = itemView.findViewById(R.id.dot);
                content = itemView.findViewById(R.id.contentContainer);
            }
        }
    }
}
