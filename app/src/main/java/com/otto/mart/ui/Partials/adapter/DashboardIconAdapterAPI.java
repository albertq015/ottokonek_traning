package com.otto.mart.ui.Partials.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.BuildConfig;
import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.DashboardModel;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.presenter.sessionManager.SessionManager;

import java.util.List;

public class DashboardIconAdapterAPI extends RecyclerView.Adapter<DashboardIconAdapterAPI.gottHolder> {
    private List<DashboardModel> models;
    private Activity parent;
    private int layoutID;

    public DashboardIconAdapterAPI(Activity parent, int layoutID, List<DashboardModel> models) {
        this.models = models;
        this.parent = parent;
        this.layoutID = layoutID;
    }

    @Override
    public gottHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return new gottHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull gottHolder holder, int position) {
        final DashboardModel model = models.get(position);
        holder.title.setText(model.getTitle());
        String baseImageUrl = BuildConfig.FLAVOR.equals("development") ? GLOBAL.IMAGE_SOURCE : GLOBAL.IMAGE_SOURCE_PROD;
        Glide.with(holder.icon.getContext())
                .applyDefaultRequestOptions(new RequestOptions()
                        .error(R.drawable.icon_button_placeholder)
                        .placeholder(R.drawable.icon_button_placeholder))
                .load(baseImageUrl + model.getIconID())
                .into(holder.icon);

        holder.hitbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getTarget() != null) {
                    Intent jenk = model.getTarget();
                    if (model.getExtradataInt() != 0) {
                        jenk.putExtra("extraInt", model.getExtradataInt());
                    }
                    if (model.getExtradataString() != null) {
                        if (model.getExtradataString().equals("logout")) {
                            new AuthDao(parent).requestLogout();
                            SessionManager.logout();
                        }
                        jenk.putExtra("extraString", model.getExtradataString());
                    }
                    parent.startActivity(jenk);
                } else {
                    Toast.makeText(parent, "no set target: " + model.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class gottHolder extends RecyclerView.ViewHolder {
        private View hitbox;
        private ImageView icon;
        private TextView title;

        public gottHolder(View itemView) {
            super(itemView);
            hitbox = itemView.findViewById(R.id.itemLayout);
            icon = itemView.findViewById(R.id.jenk);
            title = itemView.findViewById(R.id.title);
        }
    }


}