package com.otto.mart.ui.Partials.adapter.ottopoint;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.TokenListrikPreviewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenListrikPreviewAdapter extends RecyclerView.Adapter<TokenListrikPreviewAdapter.ViewHolder>{

    private Context context;
    private List<TokenListrikPreviewModel> mItems;

    public TokenListrikPreviewAdapter(Context context, List<TokenListrikPreviewModel> mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_token_listrik_preview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if(mItems.get(i).getValue() != null || !mItems.get(i).getValue().isEmpty())
            viewHolder.tvValue.setText(mItems.get(i).getValue());
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_value)
        TextView tvValue;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
