package com.otto.mart.ui.Partials.adapter.olshop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.cart.SupplierCartItem;
import com.otto.mart.model.APIModel.Response.olshop.confirm.OrdersItem;
import com.otto.mart.model.APIModel.Response.olshop.order.ShippingItem;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.component.LazyTextview;

import java.util.ArrayList;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    private List<SupplierCartItem> mDataset;
    private ItemClickListener itemClickListener;
    private boolean isClearShipping;

    public CheckoutAdapter(List<SupplierCartItem> productList) {
        this.mDataset = productList;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckoutViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_olshop_checkout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        holder.bind(mDataset.get(position), position);
        if (position == getItemCount() - 1) {
            isClearShipping = false;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setData(List<SupplierCartItem> data) {
        mDataset = data;
        notifyDataSetChanged();
    }

    public void setShipping(ShippingItem shippingItem, int parent, int position) {
        mDataset.get(parent).setShipping(shippingItem);
        notifyItemChanged(parent);
    }

    public void clearShipping() {
        isClearShipping = true;
        notifyDataSetChanged();
    }

    public List<SupplierCartItem> getmDataset() {
        return mDataset;
    }

    public void setUpdatedData(List<OrdersItem> productList) {
        List<SupplierCartItem> newList = new ArrayList<>();
        for (SupplierCartItem currentData : mDataset) {
            for (OrdersItem product : productList) {
                if (currentData.getSku().equals(product.getItem().getSku())) {
                    currentData.setQuantity(product.getItem().getQuantity());
                    currentData.setOttomart_price(product.getItem().getOttomart_price().longValue());
                    currentData.setOttomart_discount_price(product.getItem().getOttomart_discount_price().longValue());
                    newList.add(currentData);
                    break;
                }
            }
        }
        setData(newList);
    }

    class CheckoutViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvProductName, tvProductPrice, tvProductQty, tvProductVariant;
        private ImageView imgSupplierLogo, imgProduct;
        private TextView tvSupplierName, tvShippingAgent, tvShippingAgentInfo, tvTotalOrderDesc, tvTotalOrder, changeAddress;
        private LinearLayout shippingLayout, productContainer;
        private LazyTextview shippingCost, insuranceCost, subTotalProductPrice;
        private CheckBox insuranceCostCheck;

        CheckoutViewHolder(View itemView) {
            super(itemView);
            imgSupplierLogo = itemView.findViewById(R.id.img_supplier_logo);
            tvSupplierName = itemView.findViewById(R.id.tv_supplier_name);
            tvTotalOrderDesc = itemView.findViewById(R.id.tv_total_order_desc);
            tvTotalOrder = itemView.findViewById(R.id.tv_total_order);
            productContainer = itemView.findViewById(R.id.product_container);

            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductQty = itemView.findViewById(R.id.tv_product_qty);
            tvProductVariant = itemView.findViewById(R.id.tv_product_variant);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvShippingAgent = itemView.findViewById(R.id.tv_shipping_agent);
            tvShippingAgentInfo = itemView.findViewById(R.id.tv_shipping_agent_info);
            shippingLayout = itemView.findViewById(R.id.shipping_layout);
            shippingCost = itemView.findViewById(R.id.shippingCost);
            insuranceCost = itemView.findViewById(R.id.insuranceCost);
            insuranceCostCheck = itemView.findViewById(R.id.insuranceCostCheck);
            subTotalProductPrice = itemView.findViewById(R.id.subTotalProductPrice);
            changeAddress = itemView.findViewById(R.id.changeAddress);
        }

        void bind(SupplierCartItem item, int position) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder);

//            tvSupplierName.setText(item.getSupplier_name());
//            tvTotalOrderDesc.setText("Total Pesanan (" + item.getCart_items().size() + " Produk)");
            tvTotalOrder.setText(DataUtil.convertCurrency(item.getTotal_price()));

            Glide.with(imgProduct).load(item.getImage())
                    .apply(options)
                    .into(imgProduct);

            double ottoPrice = item.getOttomart_discount_price() > 0 ? Math.min(item.getOttomart_price(), item.getOttomart_discount_price()) : item.getOttomart_price();
            double price = ottoPrice + Double.parseDouble(item.getSales_commission());
            item.setPrice((long) price);
            tvProductName.setText(item.getProduct_name());
            tvProductPrice.setText(DataUtil.convertCurrency(price));
            tvProductQty.setText(item.getQuantity() + "x");
            tvProductVariant.setText(getCapitallize(item.getVariant()));
            subTotalProductPrice.setText(DataUtil.convertCurrency(price * item.getQuantity()));

            if (item.getShipping() == null) {
                tvShippingAgent.setText(tvShippingAgent.getContext().getString(R.string.text_expedition_service));
                tvShippingAgentInfo.setText(tvShippingAgentInfo.getContext().getString(R.string.button_choose_shipping));
                tvShippingAgentInfo.setVisibility(View.VISIBLE);
                shippingCost.setText("-");
                insuranceCost.setText("-");
                insuranceCostCheck.setVisibility(View.GONE);
            } else {
                tvShippingAgent.setText(item.getShipping().getCourier_name() == null ? "Data null" : item.getShipping().getCourier_name());
                tvShippingAgentInfo.setText(DataUtil.convertCurrency(item.getShipping().getShipping_cost()));
                shippingCost.setText(DataUtil.convertCurrency((long) Double.parseDouble(item.getShipping().getShipping_cost())));
                if (!item.getShipping().getInsurance_cost().isEmpty())
                    insuranceCost.setText(DataUtil.convertCurrency((long) Double.parseDouble(item.getShipping().getInsurance_cost())));
                tvShippingAgentInfo.setVisibility(View.VISIBLE);
                insuranceCostCheck.setVisibility(View.VISIBLE);
            }

//            final int index = i;
            changeAddress.setOnClickListener(v -> {
                itemClickListener.shippingClicked(item, position, 0);
            });

//            Glide.with(imgSupplierLogo).load(item.getLogo())
//                    .apply(options)
//                    .into(imgSupplierLogo);

            insuranceCostCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setInsuranceCheck(isChecked);
                    if (isChecked) {
                        insuranceCost.setVisibility(View.VISIBLE);
                        if (item.getShipping() != null) {
                            setSubTotal(item, price, Long.parseLong(item.getShipping().getInsurance_cost()));
                            itemClickListener.isCheckedInsurance(Long.parseLong(item.getShipping().getInsurance_cost()));
                        }
                    } else {
                        insuranceCost.setVisibility(View.GONE);
                        setSubTotal(item, price, 0);
                        if (item.getShipping() != null) {
                            itemClickListener.isCheckedInsurance(0 - Long.parseLong(item.getShipping().getInsurance_cost()));
                        }
                    }
                }
            });

            if (item.isInsurance_required()) {
                insuranceCostCheck.setChecked(true);
                if (item.getShipping() != null) {
                    setSubTotal(item, price, DataUtil.convertLongFromCurrency(item.getShipping().getInsurance_cost()));
                    itemClickListener.isCheckedInsurance(Long.parseLong(item.getShipping().getInsurance_cost()));
                }
            } else {
                insuranceCostCheck.setChecked(item.isInsuranceCheck());
                if (item.isInsuranceCheck()) {
                    setSubTotal(item, price, (long) Double.parseDouble(item.getShipping().getInsurance_cost()));
                } else setSubTotal(item, price, 0);
            }
            insuranceCostCheck.setEnabled(!item.isInsurance_required());
        }

        private void setSubTotal(SupplierCartItem item, double price, long insurance) {
            long shippingCost = 0;
            if (item.getShipping() != null) {
                shippingCost = DataUtil.convertLongFromCurrency(item.getShipping().getShipping_cost());
            }
            double subTotal = (price * item.getQuantity()) + insurance + shippingCost;
            tvTotalOrder.setText(DataUtil.convertCurrency(subTotal));
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private String getCapitallize(String value) {
        if (value != null) {
            String[] strArray = value.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap + " ");
            }

            return builder.toString();
        }
        return "";
    }

    public interface ItemClickListener {
        void isCheckedInsurance(long insuranceCost);

        void shippingClicked(SupplierCartItem item, int parent, int position);
    }
}
