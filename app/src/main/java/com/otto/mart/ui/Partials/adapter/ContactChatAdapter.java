package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ChatModel;

import java.text.SimpleDateFormat;
import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class ContactChatAdapter extends RecyclerView.Adapter<ContactChatAdapter.gottHolder> {

    List<ChatModel> models;
    int currentDoM = 0;

    public ContactChatAdapter(List<ChatModel> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public gottHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_wrapper, parent, false);
        return new ContactChatAdapter.gottHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull gottHolder holder, int position) {
        ChatModel model = models.get(position);
        try {
            holder.initView(model.isServer());
        } catch (Exception e) {
            Log.e("", "obhvw: " + e.getMessage());
        } finally {
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("dMMyyyy");

            if (currentDoM != Integer.parseInt(sdf.format(model.getTime()))) {
                currentDoM = Integer.parseInt(sdf.format(model.getTime()));
                sdf = new SimpleDateFormat("dd MMM yyyy");
                holder.dateContainer.setVisibility(View.VISIBLE);
                holder.date.setText(sdf.format(model.getTime()));
            }

            sdf = new SimpleDateFormat("HH:mm");
            holder.time.setText(sdf.format(model.getTime()));
            holder.chatbox.setText(model.getContent());
            if (model.getBase64img() != null) {
                //TODO put base64img to holder.imgv
//                holder.imgv.setImageResource();
            }

            if (model.isRead()) {
                holder.checkbox.setVisibility(View.VISIBLE);
            } else {
                holder.checkbox.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public class gottHolder extends RecyclerView.ViewHolder {
        private View mView;
        private EmojiconTextView chatbox;
        private TextView date, time;
        private ImageView imgv;
        private View dateContainer, checkbox;
        private Context mContext;

        public gottHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();

        }

        public void initView(boolean isServer) {
            if (!isServer) {
                mView = View.inflate(mContext, R.layout.item_chat_client, null);
            } else {
                mView = View.inflate(mContext, R.layout.item_chat_server, null);
            }
            chatbox = mView.findViewById(R.id.chatitem_text);
            date = mView.findViewById(R.id.datetv);
            dateContainer = mView.findViewById(R.id.date_cunt);
            time = mView.findViewById(R.id.chatitem_timestamp);
            imgv = mView.findViewById(R.id.imgv);
            checkbox = mView.findViewById(R.id.chatitem_checker);
            ((ViewGroup)itemView).addView(mView);

        }
    }
}
