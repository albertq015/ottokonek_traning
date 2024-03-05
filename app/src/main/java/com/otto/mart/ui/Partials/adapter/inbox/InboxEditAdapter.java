package com.otto.mart.ui.Partials.adapter.inbox;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.inbox.Inbox;
import com.otto.mart.support.util.DataUtil;

import java.util.List;

/**
 * Created by macari on 3/7/18.
 */

public class InboxEditAdapter extends RecyclerView.Adapter<InboxEditAdapter.ViewHolder> {

    private Context mContext;
    public List<Inbox> mDataset;

    public OnInboxButtonListener onInboxButtonListener;

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Inbox item = mDataset.get(position);

        if(item.isRead()){
            holder.imgReadStatus.setVisibility(View.GONE);
            holder.ivInboxIcon.setImageResource(R.drawable.icon_open_email);
            holder.itemLayout.setBackgroundColor(getColor(holder.itemLayout.getContext(), R.color.white));
        } else {
            holder.imgReadStatus.setVisibility(View.VISIBLE);
            holder.ivInboxIcon.setImageResource(R.drawable.ic_tab_menu_inbox);
            holder.itemLayout.setBackgroundColor(getColor(holder.itemLayout.getContext(), R.color.green_4));
        }

        holder.checkBox.setChecked(item.isSelected());

        holder.tvTitle.setText(item.getTitle());
        holder.tvMessage.setText(item.getDescription());
        holder.tvDate.setText(DataUtil.getDateString(item.getCreated_at(),  "dd-MM-yyyy HH:mm", true, 7));

        holder.checkBox.setOnClickListener(v -> {
            onInboxButtonListener.checkBoxClicked(item, position);
        });
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public InboxEditAdapter(Context context, List<Inbox> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)x
    @Override
    public InboxEditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inbox_edit, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    private int getColor(Context context, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(color);
        }
        return context.getResources().getColor(color);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvTitle;
        public TextView tvMessage;
        public View imgReadStatus, itemLayout;
        public TextView tvDate;
        public CheckBox checkBox;
        private ImageView ivInboxIcon;

        public ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_title);
            tvMessage = v.findViewById(R.id.tv_message);
            imgReadStatus = v.findViewById(R.id.img_read_status);
            tvDate = v.findViewById(R.id.tv_date);
            checkBox = v.findViewById(R.id.checkbox);
            ivInboxIcon = v.findViewById(R.id.ivInboxIcon);
            itemLayout = v.findViewById(R.id.item_layout);
        }
    }

    public void setOnInboxButtonListener(OnInboxButtonListener onInboxButtonListener) {
        this.onInboxButtonListener = onInboxButtonListener;
    }

    public interface OnInboxButtonListener {
        void checkBoxClicked(Inbox item, int position);
    }
}