package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.faq.TopUpChannelsItem;
import com.otto.mart.model.APIModel.Response.faq.TopUpInstructionsItem;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class TopupDoubleAdapter extends RecyclerView.Adapter<TopupDoubleAdapter.HundHolder> {
    private List<TopUpChannelsItem> models;
    private Context mContext;

    public TopupDoubleAdapter(List<TopUpChannelsItem> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public HundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topup, parent, false);
        mContext = parent.getContext();
        return new HundHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final HundHolder holder, int position) {
        TopUpChannelsItem model = models.get(position);
        final TopupSlaveAdapter adapter = new TopupSlaveAdapter(model.getTop_up_instructions());
        holder.rv.setAdapter(adapter);
        holder.rv.setLayoutManager(new LinearLayoutManager(mContext));
        holder.setIsRecyclable(false);
        holder.hitbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.icon.setRotation(holder.icon.getRotation() + 180);
                holder.hitbox.setVisibility(View.VISIBLE);
                holder.expandong.toggle();
            }
        });

        Glide.with(holder.imgv).load(model.getLogo_url()).into(holder.imgv);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class HundHolder extends RecyclerView.ViewHolder {
        private RecyclerView rv;
        private ImageView imgv;
        private View hitbox;
        private ImageView icon;
        private ExpandableLayout expandong;


        public HundHolder(View itemView) {
            super(itemView);
            expandong = itemView.findViewById(R.id.expandong);
            rv = itemView.findViewById(R.id.rv);
            imgv = itemView.findViewById(R.id.imgv);
            hitbox = itemView.findViewById(R.id.col_hitbox);
            icon = itemView.findViewById(R.id.col_icon);
        }
    }
}

class TopupSlaveAdapter extends RecyclerView.Adapter<TopupSlaveAdapter.SchweinAdapter> {
    private List<TopUpInstructionsItem> models;
    private Context mContext;

    public TopupSlaveAdapter(List<TopUpInstructionsItem> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public SchweinAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topup_child, parent, false);
        mContext = parent.getContext();
        return new SchweinAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SchweinAdapter holder, int position) {
        TopUpInstructionsItem model = models.get(position);
        holder.title.setText(model.getTitle());
//        String strng = "";
//        for (String tot :
//                model.getContent()) {
//            strng += tot + "<br>";
//        }
//        String html;
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            html = Html.fromHtml(strng, Html.FROM_HTML_MODE_LEGACY).toString();
//        } else {
//            html = Html.fromHtml(strng).toString();
//        }
        holder.content.setText(HtmlCompat.fromHtml(model.getInstruction(), HtmlCompat.FROM_HTML_MODE_LEGACY));
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

    public class SchweinAdapter extends RecyclerView.ViewHolder {
        private TextView title, content;
        private View hitbox;
        private ImageView icon;
        private ExpandableLayout expandong;

        public SchweinAdapter(View itemView) {
            super(itemView);
            expandong = itemView.findViewById(R.id.expandong);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.contentContainer);
            hitbox = itemView.findViewById(R.id.col_hitbox);
            icon = itemView.findViewById(R.id.col_icon);
        }
    }
}
