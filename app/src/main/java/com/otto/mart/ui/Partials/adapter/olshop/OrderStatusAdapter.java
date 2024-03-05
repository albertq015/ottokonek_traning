package com.otto.mart.ui.Partials.adapter.olshop;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.otto.mart.BuildConfig;
import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.order.status.OrderStatus;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.component.LazyTextview;

import java.text.ParseException;
import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.OrderStatusViewHolder> {

    private List<OrderStatus> orderStatusList;

    public OrderStatusAdapter(List<OrderStatus> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

    @NonNull
    @Override
    public OrderStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderstatus_invoice, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusViewHolder holder, int position) {
        holder.bind(orderStatusList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderStatusList.size();
    }

    class OrderStatusViewHolder extends RecyclerView.ViewHolder {
        private LazyTextview ltv_invoiceno, subTotal, orderNumber, supplierOrderNumber, refNum, resi;
        private TextView tv_date, tv_prodname, tv_price, tv_count, supplierName;
        private TextView shippingStatus, packagingStatus, transactionStatus, paymentStatus, completeStatus, tv_sender;
        private ImageView iv_product, supplierImage, shippingStatusImg, packagingStatusImg, transactionStatusImg, paymentStatusImg, completeStatusImg;

        OrderStatusViewHolder(View itemView) {
            super(itemView);
            ltv_invoiceno = itemView.findViewById(R.id.ltv_invoiceno);
            subTotal = itemView.findViewById(R.id.subTotal);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_prodname = itemView.findViewById(R.id.tv_prodname);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_count = itemView.findViewById(R.id.tv_count);
            supplierName = itemView.findViewById(R.id.supplierName);
            shippingStatus = itemView.findViewById(R.id.shippingStatus);
            packagingStatus = itemView.findViewById(R.id.packagingStatus);
            transactionStatus = itemView.findViewById(R.id.transactionStatus);
            iv_product = itemView.findViewById(R.id.iv_product);
            supplierImage = itemView.findViewById(R.id.supplierImage);
            shippingStatusImg = itemView.findViewById(R.id.shippingStatusImg);
            packagingStatusImg = itemView.findViewById(R.id.packagingStatusImg);
            transactionStatusImg = itemView.findViewById(R.id.transactionStatusImg);
            paymentStatusImg = itemView.findViewById(R.id.paymentStatusImg);
            completeStatus = itemView.findViewById(R.id.completeStatus);
            completeStatusImg = itemView.findViewById(R.id.completeStatusImg);
            orderNumber = itemView.findViewById(R.id.orderNumber);
            supplierOrderNumber = itemView.findViewById(R.id.supplierOrderNumber);
            refNum = itemView.findViewById(R.id.refNum);
            resi = itemView.findViewById(R.id.resi);
            tv_sender = itemView.findViewById(R.id.tv_sender);
        }

        public void bind(OrderStatus orderStatus) {
            ltv_invoiceno.setText(orderStatus.getInvoice_number());
            subTotal.setText(DataUtil.convertCurrency(orderStatus.getTotal_amount()));
            try {
                tv_date.setText(DataUtil.getFormattedDateStringFromServerResponse(orderStatus.getCreated_at(), "dd-MMM-yyyy HH:mm"));
            } catch (ParseException e) {
                e.printStackTrace();

            }
            tv_prodname.setText(orderStatus.getProduct_name());
            tv_price.setText(DataUtil.convertCurrency(orderStatus.getTotal_amount()));
            tv_count.setText(String.valueOf(orderStatus.getQuantity()));
            supplierName.setText(orderStatus.getSupplier_name());
            String baseImageUrl = BuildConfig.FLAVOR.equals("development") ? GLOBAL.IMAGE_SOURCE : GLOBAL.IMAGE_SOURCE_PROD;
            Glide.with(supplierImage.getContext()).load(baseImageUrl + orderStatus.getSupplier_logo().getThumb().getUrl()).into(supplierImage);
            Glide.with(iv_product.getContext()).load(orderStatus.getMain_image_url()).into(iv_product);
            orderNumber.setText(String.valueOf(orderStatus.getPayment_id()));
            refNum.setText(orderStatus.getCustomer_ref_number());
            if (orderStatus.getElevenia_order_status() != null) {
                resi.setText(orderStatus.getElevenia_order_status().getInvoice_no());
                tv_sender.setText(orderStatus.getElevenia_order_status().getCourier_name());
                supplierOrderNumber.setText(orderStatus.getElevenia_order_status().getElevenia_ord_no());
            }
            handleStatus(orderStatus);
        }

        private void handleStatus(OrderStatus orderStatus) {
            if (orderStatus.getStatus().equalsIgnoreCase("menunggu pembayaran")) {
                setIcons(true, "a", false, false, "Selesai");
            } else if (orderStatus.getStatus().equalsIgnoreCase("transaksi sukses")) {
                setIcons(false, orderStatus.getStatus(), false, false, "Selesai");
            } else if (orderStatus.getStatus().equalsIgnoreCase("Order Gagal")) {
                setIcons(false, orderStatus.getStatus(), false, false, "transaksi gagal");
            } else if (orderStatus.getStatus().equalsIgnoreCase("transaksi gagal")) {
                setIcons(false, orderStatus.getStatus(), false, false, "transaksi gagal");
            } else if (orderStatus.getStatus().equalsIgnoreCase("dibatalkan")) {
                setIcons(false, orderStatus.getStatus(), false, false, "dibatalkan");
            } else if (orderStatus.getStatus().equalsIgnoreCase("Reversed")) {
                setIcons(false, orderStatus.getStatus(), false, false, "Reversed");
            } else if (orderStatus.getStatus().equalsIgnoreCase("dalam pengemasan")) {
                setIcons(false, orderStatus.getStatus(), true, false, "Transaksi Berhasil");
            } else if (orderStatus.getStatus().equalsIgnoreCase("sedang dikirim")) {
                setIcons(false, orderStatus.getStatus(), true, true, "Transaksi Berhasil");
            } else if (orderStatus.getStatus().equalsIgnoreCase("selesai")) {
                setIcons(false, orderStatus.getStatus(), true, true, "Selesai");
            } else if (orderStatus.getStatus().equalsIgnoreCase("retur")) {
                setIcons(false, orderStatus.getStatus(), true, true, "retur");
            }
        }

        private void setIcons(boolean isWaiting, String isTransaction, boolean isPackaging, boolean isShipping, String isComplete) {
            if (isComplete.equalsIgnoreCase("selesai") && isPackaging && isShipping) {
                setIcon("Transaksi Berhasil", R.drawable.icon_st_transaksi_sukses_on, R.color.ocean_blue,
                        R.drawable.icon_st_packaging_on, R.color.ocean_blue,
                        R.drawable.icon_st_pengirim_on, R.color.ocean_blue,
                        isComplete, R.drawable.icon_st_done_on, R.color.ocean_blue);
            } else if (isComplete.equalsIgnoreCase("retur")) {
                setIcon("Transaksi Berhasil", R.drawable.icon_st_transaksi_sukses_on, R.color.ocean_blue,
                        R.drawable.icon_st_packaging_on, R.color.ocean_blue,
                        R.drawable.icon_st_pengirim_on, R.color.ocean_blue,
                        isComplete, R.drawable.icon_st_retur, R.color.faded_red_old);
            } else if (isShipping) {
                setIcon("Transaksi Berhasil", R.drawable.icon_st_transaksi_sukses_on, R.color.ocean_blue,
                        R.drawable.icon_st_packaging_on, R.color.ocean_blue,
                        R.drawable.icon_st_pengirim_on, R.color.ocean_blue,
                        "Selesai", R.drawable.icon_st_done_off, R.color.charcoal_grey);
            } else if (isPackaging) {
                setIcon("Transaksi Berhasil", R.drawable.icon_st_transaksi_sukses_on, R.color.ocean_blue,
                        R.drawable.icon_st_packaging_on, R.color.ocean_blue,
                        R.drawable.icon_st_pengirim_off, R.color.charcoal_grey,
                        "Selesai", R.drawable.icon_st_done_off, R.color.charcoal_grey);
            } else if (isTransaction.equalsIgnoreCase("transaksi berhasil")) {
                setIcon("Or Berhasil", R.drawable.icon_st_transaksi_sukses_on, R.color.ocean_blue,
                        R.drawable.icon_st_packaging_off, R.color.charcoal_grey,
                        R.drawable.icon_st_pengirim_off, R.color.charcoal_grey,
                        isComplete, R.drawable.icon_st_done_off, R.color.charcoal_grey);
            } else if (isComplete.equalsIgnoreCase("Order Gagal")) {
                setIcon("Order Gagal", R.drawable.icon_st_transaksi_failed, R.color.faded_red_old,
                        R.drawable.icon_st_packaging_off, R.color.charcoal_grey,
                        R.drawable.icon_st_pengirim_off, R.color.charcoal_grey,
                        isComplete, R.drawable.icon_st_done_off, R.color.charcoal_grey);
            } else if (isComplete.equalsIgnoreCase("transaksi gagal")) {
                setIcon("Transaksi Gagal", R.drawable.icon_st_transaksi_failed, R.color.faded_red_old,
                        R.drawable.icon_st_packaging_off, R.color.charcoal_grey,
                        R.drawable.icon_st_pengirim_off, R.color.charcoal_grey,
                        isComplete, R.drawable.icon_st_done_off, R.color.charcoal_grey);
            } else if (isComplete.equalsIgnoreCase("dibatalkan")) {
                setIcon("Dibatalkan", R.drawable.icon_st_transaksi_failed, R.color.faded_red_old,
                        R.drawable.icon_st_packaging_off, R.color.charcoal_grey,
                        R.drawable.icon_st_pengirim_off, R.color.charcoal_grey,
                        isComplete, R.drawable.icon_st_done_off, R.color.charcoal_grey);
            } else if (isComplete.equalsIgnoreCase("Reversed")) {
                setIcon("Transaksi Gagal", R.drawable.icon_st_transaksi_failed, R.color.faded_red_old,
                        R.drawable.icon_st_packaging_off, R.color.charcoal_grey,
                        R.drawable.icon_st_pengirim_off, R.color.charcoal_grey,
                        isComplete, R.drawable.icon_st_done_off, R.color.charcoal_grey);
            } else if (isWaiting) {
                setIcon("Transaksi Berhasil", R.drawable.icon_st_transaksi_sukses_off, R.color.charcoal_grey,
                        R.drawable.icon_st_packaging_off, R.color.charcoal_grey,
                        R.drawable.icon_st_pengirim_off, R.color.charcoal_grey,
                        isComplete, R.drawable.icon_st_done_off, R.color.charcoal_grey);
            }
        }

        private void setIcon(String transactionMessage, int transactionIcon, int transactionColor,
                             int packagingIcon, int packagingColor, int shippingIcon, int shippingColor,
                             String completeMessage, int completeIcon, int completeColor) {

            setTextColor(transactionStatus, transactionColor);
            setTextColor(packagingStatus, packagingColor);
            setTextColor(shippingStatus, shippingColor);
            setTextColor(completeStatus, completeColor);

            setImage(transactionStatusImg, transactionIcon);
            setImage(packagingStatusImg, packagingIcon);
            setImage(shippingStatusImg, shippingIcon);
            setImage(completeStatusImg, completeIcon);

            transactionStatus.setText(transactionMessage);
            completeStatus.setText(completeMessage);
        }

        private void setTextColor(TextView textView, int color) {
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), color));
        }

        private void setImage(ImageView imageview, int icon) {
            imageview.setImageResource(icon);
        }
    }
}
