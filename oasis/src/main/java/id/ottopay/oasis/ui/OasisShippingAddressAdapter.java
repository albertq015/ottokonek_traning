package id.ottopay.oasis.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.Partials.RecyclerViewListener;
import com.rd.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import id.ottopay.oasis.R;

public class OasisShippingAddressAdapter extends RecyclerView.Adapter<OasisShippingAddressAdapter.ShippingAddressViewHolder> {

    private List<ShippingAddressData> addressDataList;
    private RecyclerViewListener listener;
    private int selectedId;

    public OasisShippingAddressAdapter(RecyclerViewListener listener, int selectedId) {
        this.listener = listener;
        this.selectedId = selectedId;
        addressDataList = new ArrayList<>();
    }

    public OasisShippingAddressAdapter(RecyclerViewListener listener) {
        this.listener = listener;
        addressDataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ShippingAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShippingAddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_oasis, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingAddressViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> callListener(position, v, null));
        holder.actionEdit.setOnClickListener(v -> callListener(position, v, addressDataList.get(position)));

        holder.bind(addressDataList.get(position));
    }

    private void callListener(int position, View v, ShippingAddressData shippingAddressData) {
        listener.onItemClick(v.getId(), position, addressDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return addressDataList.size();
    }

    public void setAddressDataList(List<ShippingAddressData> addressDataList) {
        this.addressDataList = addressDataList;
        notifyDataSetChanged();
    }

    class ShippingAddressViewHolder extends RecyclerView.ViewHolder {
        private View actionEdit;
        private TextView addressOwner, addressName, address, address1, is_primary;
        private RadioButton radioButton;

        ShippingAddressViewHolder(View itemView) {
            super(itemView);
            // addressLabel = itemView.findViewById(R.id.addressLabel);
            addressName = itemView.findViewById(R.id.address_name);
            address = itemView.findViewById(R.id.address);
            address1 = itemView.findViewById(R.id.address1);
            actionEdit = itemView.findViewById(R.id.actionEdit);
            addressOwner = itemView.findViewById(R.id.address_owner);
            radioButton = itemView.findViewById(R.id.radioButton);
            is_primary = itemView.findViewById(R.id.is_primary);

            //addressLabel.setVisibility(View.GONE);
            itemView.setPadding(0, DensityUtils.dpToPx(8), 0, 0);
        }

        void bind(ShippingAddressData addressData) {
            addressName.setText(addressData.getName());
            address.setText(addressData.getDetail());
            address1.setText(getAddressMapped(addressData));
            addressOwner.setText(SessionManager.getMerchantName());

            if ((addressData.getId() == selectedId) && (listener != null)) {
                listener.onItemClick(2, getAdapterPosition(), addressData);
            }

            if(addressData.isIs_primary()){
                radioButton.toggle();
                is_primary.setVisibility(View.VISIBLE);
            }
        }

        private String getAddressMapped(ShippingAddressData addressData) {
            return addressData.getRegion().getName() + ", " +
                    addressData.getProvince().getName() + ", " +
                    addressData.getMunicipality().getName() + ", " +
                    addressData.getZip_code();
        }
    }
}
