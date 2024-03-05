package com.otto.mart.ui.Partials.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.DashboardIconModel;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.ppob.product.game.VoucherGameActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.List;

public class DashboardIconAdapter extends RecyclerView.Adapter<DashboardIconAdapter.gottHolder> {
    private List<DashboardIconModel> models;
    private Activity parent;
    private int layoutID;

    public DashboardIconAdapter(Activity parent, int layoutID, List<DashboardIconModel> models) {
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
        final DashboardIconModel model = models.get(position);
        holder.title.setText(model.getTitle());
        try {
            holder.icon.setImageResource(model.getIconID());
        } catch (Exception e) {
            Log.e("ICADPT: ", model.getTitle() + " no icon!");
        }

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
                            new AuthDao(parent).logout();
                            SessionManager.logout();
                        }
                        jenk.putExtra("extraString", model.getExtradataString());
                    }

                    PackageManager packageManager = parent.getPackageManager();

                    if (jenk.resolveActivity(packageManager) != null) {
                        if(models.get(position).getExtradataInt()==-1){
                            //Show more menu
                            ((DashboardActivity) parent).showMoreMenuFragment();
                        } else if(models.get(position).getExtradataInt()==-2){
                            try {
                                ((DashboardActivity) parent).hideMoreMenuFragment();
                            } catch (Exception e){
                                e.printStackTrace();
                            }

                            parent.startActivity(new Intent(parent, VoucherGameActivity.class));
                        } else {
                            parent.startActivity(jenk);

                            try {
                                ((DashboardActivity) parent).hideMoreMenuFragment();
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } else {
                        new ErrorDialog(parent, parent, false, false,
                                parent.getString(R.string.err_nophone_title),
                                parent.getString(R.string.err_nophone_content) +
                                        "").show();
                    }


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