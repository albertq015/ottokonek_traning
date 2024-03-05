package com.otto.mart.ui.Partials.adapter;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.BankUiModel;
import com.otto.mart.ui.activity.register.RecyclerAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class BankItemRecyclerAdapter extends RecyclerView.Adapter<BankItemRecyclerAdapter.KucingHolder> {

    private final String KEY_REJECT = "ditolak";

    Context mContext;
    boolean isDeleteState = false;

    List<BankUiModel> models = new ArrayList<>();

    RecyclerAdapterCallback listener = null;
    onDeleteListener deleteListener = null;
    OnPopUpListener popUpListener = null;

    int layoutId = R.layout.item_bank;

    public BankItemRecyclerAdapter(int layoutId) {
        if (layoutId > 0) {
            this.layoutId = layoutId;
        }
    }

    public void setBankItemRecyclerAdapterListener(RecyclerAdapterCallback listener) {
        this.listener = listener;
    }

    public void setDeleteListener(onDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setPopUpListener(OnPopUpListener popUpListener) {
        this.popUpListener = popUpListener;
    }

    @NonNull
    @Override
    public KucingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        mContext = parent.getContext();
        return new BankItemRecyclerAdapter.KucingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final KucingHolder holder, int position) {
        BankUiModel model = models.get(position);

        if (position == 0) {
            holder.setAsMain();
        } else if (position == 1) {
            holder.setAsSecondaryHeader();
        } else {
            holder.setAsDefault();
        }

        holder.tv_bankName.setText(model.getListModel().getName());
        holder.tv_bankAccountNum.setText(model.getRequestModel().getAccount_number());
        holder.tv_bank_AccountName.setText(model.getRequestModel().getAccount_name());
        if (holder.tvStatus != null) {
            holder.tvStatus.setText(String.format(holder.tvStatus.getContext().getString(R.string.text_status_format), model.getRequestModel().getApproval_status()));
            holder.tvStatus.setVisibility(View.GONE);
        }

        holder.tvStatusAccount.setText(model.getRequestModel().getApproval_status());
        setStatusAppearance(holder.tvStatusAccount, holder.statusContainer, model.getRequestModel().getApproval_status(), holder.hitbox);

        Glide.with(mContext)
                .load(model.getListModel().getLogo())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.bank_placeholder)
                        .error(R.drawable.bank_placeholder)
                        .override(180, 120))
                .into(holder.imgv_icon);

        holder.hitbox.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(model, position, holder);
            }
        });

        if (holder.hitbox1 != null) {
            holder.hitbox1.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDelete(position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isDeleteState = !isDeleteState;
                    if (isDeleteState) {
                        holder.hitbox.setVisibility(View.GONE);
                        holder.hitbox1.setVisibility(View.VISIBLE);
                    } else {
                        holder.hitbox.setVisibility(View.VISIBLE);
                        holder.hitbox1.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(model, position, 111, holder));
        if (holder.content != null) {
            holder.content.setOnClickListener(v->listener.onItemClick(model, position, 111, holder));
        }

        holder.tvStatusAccount.setOnClickListener(v -> {
            if (v != null) {
                if (model.getRequestModel().getApproval_status().equalsIgnoreCase(KEY_REJECT)) {
                    popUpListener.onShowReason(model.getRequestModel().getReason());
                }
            }
        });
    }

    private void setStatusAppearance(TextView tv, LinearLayout statusContainer, String status, View editView) {
        switch (status.toLowerCase()) {
            case "ditolak": {
                setTextAppearance(tv, statusContainer, R.color.faded_red, R.drawable.bg_red_bordered);
                setEditButtonVisibility(editView, true);
                break;
            }
            case "disetujui": {
                setTextAppearance(tv, statusContainer, R.color.dark_sky_blue, R.drawable.bg_blue_bordered);
                setEditButtonVisibility(editView, true);
                break;
            }
            default: {
                setTextAppearance(tv, statusContainer, R.color.brown_grey_three, R.drawable.bg_grey_bordered);
                setEditButtonVisibility(editView, false);
            }
        }
    }

    private void setTextAppearance(TextView tv, LinearLayout statusContainer, int textColor, int textBackground) {
        tv.setTextColor(textColor);
        //tv.setBackgroundResource(textBackground);
        statusContainer.setBackground(ContextCompat.getDrawable(mContext, textBackground));
    }

    private void setEditButtonVisibility(View editView, boolean isShow) {
//        if (isShow) {
//            editView.setVisibility(View.VISIBLE);
//        } else
//            editView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void removeItem(int pos){
        models.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos,models.size());
    }

    public void setModels(List<BankUiModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    public void addModel(BankUiModel model) {
        this.models.add(model);
        notifyDataSetChanged();
    }

    public class KucingHolder extends RecyclerView.ViewHolder {
        private ViewGroup layout_typecontainer,content;
        private View v_bottomline, mainContent, divider,hitbox,hitbox1;
        private ImageView imgv_icon,ivTypeLogo;
        private TextView tv_bankName, tv_bankAccountNum, tv_bank_AccountName, tv_rektype, tvStatus, tvStatusAccount;
        private ImageView imgInfo;
        private LinearLayout statusContainer;

        public KucingHolder(View itemView) {
            super(itemView);
            hitbox = itemView.findViewById(R.id.action);
            try {
                hitbox1 = itemView.findViewById(R.id.action1);
                divider = itemView.findViewById(R.id.divider);
                ivTypeLogo = itemView.findViewById(R.id.ivTypeLogo);
                content = itemView.findViewById(R.id.content);
            } catch (Resources.NotFoundException e) {
                Log.e("", "KucingHolder: " + "hitbox1 is missing, called outside register, skipping");
            }
            imgv_icon = itemView.findViewById(R.id.imgv_logo);
            tv_bankName = itemView.findViewById(R.id.tv_bankName);
            tv_bankAccountNum = itemView.findViewById(R.id.tv_bankAccNum);
            tv_bank_AccountName = itemView.findViewById(R.id.tv_bankAccName);
            tv_rektype = itemView.findViewById(R.id.tv_rektype);
            tvStatus = itemView.findViewById(R.id.tv_status);
            layout_typecontainer = itemView.findViewById(R.id.layout_typecontainer);
            v_bottomline = itemView.findViewById(R.id.v_bottomline);
            imgInfo = itemView.findViewById(R.id.img_info);
            tvStatusAccount = itemView.findViewById(R.id.tvStatusAccount);
            statusContainer = itemView.findViewById(R.id.statusContainer);

            layout_typecontainer.setVisibility(View.GONE);
        }

        public void setAsMain() {
            setExtraViewVisibility(true);
            tv_rektype.setText(R.string.text_main_withdraw_bank_account);
            layout_typecontainer.setVisibility(View.VISIBLE);
        }

        public void setAsSecondaryHeader() {
            setExtraViewVisibility(false);
            tv_rektype.setText(R.string.text_another_withdraw_bank_account);
            layout_typecontainer.setVisibility(View.VISIBLE);
        }

        void setAsDefault() {
            setExtraViewVisibility(false);
            layout_typecontainer.setVisibility(View.GONE);
        }

        private void setExtraViewVisibility(boolean isShow) {
            if (divider != null) {
                if (isShow) divider.setVisibility(View.VISIBLE);
                else divider.setVisibility(View.GONE);
            }

            if (ivTypeLogo != null) {
                if (isShow) ivTypeLogo.setVisibility(View.VISIBLE);
                else ivTypeLogo.setVisibility(View.GONE);
            }
        }

        public String getName() {
            return tv_bank_AccountName.getText().toString();
        }

        public String getNorek() {
            return tv_bankAccountNum.getText().toString();
        }
    }

    public interface onDeleteListener {
        void onDelete(int position);
    }

    public interface OnPopUpListener {
        void onShowReason(String reason);
    }
}