package com.otto.mart.ui.Partials.adapter.inbox;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.inbox.Inbox;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.component.LazyTextview;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    private Context mContext;
    public List<Inbox> mDataset;

    public OnViewClickListener mOnViewClickListener;

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Inbox item = mDataset.get(position);

        holder.additionalPadding.setVisibility(View.GONE);

        if ((position + 1) == mDataset.size()) {
            holder.additionalPadding.setVisibility(View.VISIBLE);
        }

        if (item.isRead()) {
            holder.imgReadStatus.setVisibility(View.GONE);
            holder.ivInboxIcon.setImageResource(R.drawable.icon_open_email);
            holder.itemLayout.setBackgroundColor(getColor(holder.itemLayout.getContext(), R.color.white));
        } else {
            holder.imgReadStatus.setVisibility(View.VISIBLE);
            holder.itemLayout.setBackgroundColor(getColor(holder.itemLayout.getContext(), R.color.green_4));
            holder.ivInboxIcon.setImageResource(R.drawable.ic_tab_menu_inbox);
        }

        holder.tvTitle.setText(item.getTitle());
        holder.tvMessage.setText(item.getDescription());
        holder.tvDate.setText(DataUtil.getDateString(item.getCreated_at(), "dd-MM-yyyy HH:mm", true, 0));

        holder.itemLayout.setOnClickListener(v -> {
            mOnViewClickListener.onViewClickListener(item, position);
        });
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public InboxAdapter(Context context, List<Inbox> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)x
    @Override
    public InboxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inbox, parent, false);
        return new ViewHolder(view);
    }

    private int getColor(Context context, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(color);
        }
        return context.getResources().getColor(color);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout itemLayout;
        public TextView tvTitle;
        public TextView tvMessage;
        public View imgReadStatus;
        public TextView tvDate;
        public View additionalPadding;
        private ImageView ivInboxIcon;

        public ViewHolder(View v) {
            super(v);
            itemLayout = v.findViewById(R.id.item_layout);
            tvTitle = v.findViewById(R.id.tv_title);
            tvMessage = v.findViewById(R.id.tv_message);
            imgReadStatus = v.findViewById(R.id.img_read_status);
            tvDate = v.findViewById(R.id.tv_date);
            additionalPadding = v.findViewById(R.id.additional_padding);
            ivInboxIcon = v.findViewById(R.id.ivInboxIcon);
        }
    }

    private void setContent(LazyTextview textview, String value) {
        String newValue = value.replaceAll("\n", "<BR>");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textview.setText(HtmlCompat.fromHtml(newValue, Html.FROM_HTML_MODE_COMPACT).toString());
        } else {
            textview.setText(Html.fromHtml(newValue));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }

    public void setmOnViewClickListener(OnViewClickListener mOnViewClickListener) {
        this.mOnViewClickListener = mOnViewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClickListener(Inbox inbox, int position);
    }
}