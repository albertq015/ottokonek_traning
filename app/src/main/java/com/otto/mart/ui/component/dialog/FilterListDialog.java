package com.otto.mart.ui.component.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.DefaultItemModel;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.ui.actionView.ActionListItemTwo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterListDialog extends BaseBottomDialog {

    @BindView(R.id.list_lokasi)
    RecyclerView listLokasi;
    @BindView(R.id.list_penawaran)
    RecyclerView listPenawaran;
    @BindView(R.id.sb_range_harga)
    RangeSeekBar sbRangeHarga;
    @BindView(R.id.edt_minimum)
    EditText edtMinimum;
    @BindView(R.id.edt_maximum)
    EditText edtMaximum;

    private int selectedPenawaranId = 0;
    private int selectedLokasiId = 0;
    private String selectedLokasi = "";
    private long minHargaFilter = 0;
    private long maxHargaFilter = 0;
    private long maxHargaFilterSeekbar = 0;

    private ActionFilterDeals callback;

    private List<DefaultItemModel> mItemsLokasi = new ArrayList<>();
    private AdapterCapsule adapterLokasi;

    private List<DefaultItemModel> mItemsPenawaran = new ArrayList<>();
    private AdapterCapsule adapterPenawaran;

    private FilterListDialog(@NonNull Context context, int themeResId, int selectedPenawaranId, long minHargaFilter, long maxHargaFilter, long maxHargaFilterSeekbar, ActionFilterDeals callback) {
        super(context, themeResId);

        this.selectedPenawaranId = selectedPenawaranId;
        this.callback = callback;
        this.minHargaFilter = minHargaFilter;
        this.maxHargaFilter = maxHargaFilter;
        this.maxHargaFilterSeekbar = maxHargaFilterSeekbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter_list);
        ButterKnife.bind(this);

        cofigureListLokasi();

        cofigureListPenawaran();

        selectPenawaran();

        sbRangeHarga.setRange(0, maxValueSeekBar());
        sbRangeHarga.setProgress(minHargaFilter, maxHargaFilter == 0 ? 1 : maxHargaFilter);

        edtMinimum.setText(CommonHelper.currencyFormat(minHargaFilter == 0 ? (long) 0 : minHargaFilter));
        edtMinimum.setTag(minHargaFilter == 0 ? (long) 0 : minHargaFilter);
        edtMaximum.setText(CommonHelper.currencyFormat(maxHargaFilter == 0 ? (long) 0 : maxHargaFilter));
        edtMaximum.setTag(maxHargaFilter == 0 ? (long) 0 : maxHargaFilter);

        sbRangeHarga.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                edtMinimum.setText(CommonHelper.currencyFormat((long) leftValue));
                edtMinimum.setTag((long) leftValue);
                edtMaximum.setText(CommonHelper.currencyFormat(rightValue == 1 ? (long) 0 : (long)rightValue));
                edtMaximum.setTag(rightValue == 1 ? (long) 0 : (long)rightValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        edtMinimum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                edtMinimum.removeTextChangedListener(this);

                String valueMaxText = CommonHelper.removeCurrencyFormat(edtMaximum.getText().toString());
                long valueMaxOri = Long.parseLong(valueMaxText);

                if(s.toString().isEmpty() || valueMaxOri == 0){
                    edtMinimum.setText(CommonHelper.currencyFormat((long) 0));
                    edtMinimum.setTag((long) 0);
                    edtMinimum.setSelection(CommonHelper.currencyFormat((long) 0).length());
                    edtMinimum.addTextChangedListener(this);
                    return;
                }

                String valueText = CommonHelper.removeCurrencyFormat(s.toString());
                long valueOri = Long.parseLong(valueText);

                // check if minimum value greater than max value
                if(valueOri >= valueMaxOri){
                    edtMinimum.setText(CommonHelper.currencyFormat(valueMaxOri - 1));
                    edtMinimum.setTag(valueMaxOri - 1);
                    edtMinimum.setSelection(CommonHelper.currencyFormat(valueMaxOri - 1).length());
                    edtMinimum.addTextChangedListener(this);
                    return;
                }

                edtMinimum.setText(CommonHelper.currencyFormat(Long.parseLong(valueText)));
                edtMinimum.setTag(Long.parseLong(valueText));
                edtMinimum.setSelection(CommonHelper.currencyFormat(Long.parseLong(valueText)).length());
                edtMinimum.addTextChangedListener(this);
            }
        });

        edtMaximum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                edtMaximum.removeTextChangedListener(this);

                if(s.toString().isEmpty()){
                    edtMaximum.setText(CommonHelper.currencyFormat((long) 0));
                    edtMaximum.setTag((long) 0);
                    edtMaximum.setSelection(CommonHelper.currencyFormat((long) 0).length());
                    edtMaximum.addTextChangedListener(this);
                    return;
                }

                String valueText = CommonHelper.removeCurrencyFormat(s.toString());

                // check if value is greater the limit
                long valueCheck = Long.parseLong(valueText);
                if(valueCheck > maxValueSeekBar()){
                    edtMaximum.setText(CommonHelper.currencyFormat(maxValueSeekBar()));
                    edtMaximum.setTag(maxValueSeekBar());
                    edtMaximum.setSelection(CommonHelper.currencyFormat(maxValueSeekBar()).length());
                    edtMaximum.addTextChangedListener(this);
                    return;
                }

                edtMaximum.setText(CommonHelper.currencyFormat(Long.parseLong(valueText)));
                edtMaximum.setTag(Long.parseLong(valueText));
                edtMaximum.setSelection(CommonHelper.currencyFormat(Long.parseLong(valueText)).length());
                edtMaximum.addTextChangedListener(this);
            }
        });
    }

    @OnClick({R.id.view_back, R.id.btn_no})
    public void closeDialog(){
        dismiss();
    }

    @OnClick(R.id.btn_yes)
    public void submitFilter(){
        long minHarga = (long) edtMinimum.getTag();
        long maxHarga = (long) edtMaximum.getTag();
        if(maxHarga == 1) maxHarga = 0;

        CommonHelper.hideKeyboard(FilterListDialog.this);

        callback.filter(selectedLokasiId, selectedLokasi, selectedPenawaranId, getValueFilterPenawaran(selectedPenawaranId), minHarga, maxHarga);

        closeDialog();
    }

    private long maxValueSeekBar(){
        return maxHargaFilterSeekbar == 0 ? 100000 : maxHargaFilterSeekbar;
    }

    private void cofigureListLokasi(){
        setDummyLokasi();

        if(adapterLokasi == null)
            adapterLokasi = new AdapterCapsule(getContext(), mItemsLokasi, (position, data) -> {
                selectedLokasiId = mItemsLokasi.get(position).getId();
                selectedLokasi = mItemsLokasi.get(position).getText();

                //closeDialog();
            });
        listLokasi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listLokasi.setItemAnimator(new DefaultItemAnimator());
        listLokasi.setAdapter(adapterLokasi);
    }

    private void refreshListLokasi(){
        if(adapterLokasi != null)
            adapterLokasi.notifyDataSetChanged();
        else
            cofigureListLokasi();
    }

    private void setDummyLokasi(){
        mItemsLokasi.add(new DefaultItemModel(0, "Jakarta"));
        mItemsLokasi.add(new DefaultItemModel(1, "Bandung"));
        mItemsLokasi.add(new DefaultItemModel(2, "Surabaya"));
        mItemsLokasi.add(new DefaultItemModel(3, "Yogyakarta"));
    }

    private void cofigureListPenawaran(){
        setDummyPenawaran();

        if(adapterPenawaran == null)
            adapterPenawaran = new AdapterCapsule(getContext(), mItemsPenawaran, (position, data) -> selectedPenawaranId = mItemsPenawaran.get(position).getId());

        listPenawaran.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listPenawaran.setItemAnimator(new DefaultItemAnimator());
        listPenawaran.setAdapter(adapterPenawaran);
    }

    private void refreshListPenawaran(){
        if(adapterPenawaran != null)
            adapterPenawaran.notifyDataSetChanged();
        else
            cofigureListPenawaran();
    }

    private void setDummyPenawaran(){
        mItemsPenawaran.add(new DefaultItemModel(0, "Semua"));
        mItemsPenawaran.add(new DefaultItemModel(R.id.ottopoint_filter_deals_discount, "Diskon"));
        mItemsPenawaran.add(new DefaultItemModel(R.id.ottopoint_filter_deals_cahshback, "Cashback"));
    }

    public static void showDialog(Context context, int selectedPenawaranId, long minHargaFilter, long maxHargaFilter, long maxHargaFilterSeekbar, ActionFilterDeals callback){
        new FilterListDialog(context, R.style.CustomDialog_Clear_FromBottom, selectedPenawaranId, minHargaFilter, maxHargaFilter, maxHargaFilterSeekbar, callback).show();
    }

    private String getValueFilterPenawaran(int penawaran){
        switch (penawaran){
            case R.id.ottopoint_filter_deals_cahshback:
                return "cashback";

            case R.id.ottopoint_filter_deals_discount:
                return "discount_code";

            default:
                return "";
        }
    }

    private void selectPenawaran(){
        for (int i = 0; i < mItemsPenawaran.size(); i++)
            if(selectedPenawaranId == mItemsPenawaran.get(i).getId())
                mItemsPenawaran.get(i).setSelected(true);

        refreshListPenawaran();
    }

    public interface ActionFilterDeals{
        void filter(int idLokasi, String lokasi, int idPenawaran, String penawaran, long minHarga, long maxHarga);
    }

    public static class AdapterCapsule extends RecyclerView.Adapter<AdapterCapsule.ViewHolder>{

        private Context context;
        private List<DefaultItemModel> mItems;
        private ActionListItemTwo callback;

        AdapterCapsule(Context context, List<DefaultItemModel> mItems, ActionListItemTwo callback){
            this.mItems = mItems;
            this.context = context;
            this.callback = callback;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_capsule, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            // set views
            viewHolder.tvText.setText(mItems.get(i).getText());
            viewHolder.viewMain.setSelected(mItems.get(i).isSelected());

            if(i == 0){
                int right = (int) context.getResources().getDimension(R.dimen.margin_super_small);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, right, 0);
                viewHolder.viewMain.setLayoutParams(params);
            }

            if(i == (getItemCount() - 1)){
                int left = (int) context.getResources().getDimension(R.dimen.margin_super_small);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(left, 0, 0, 0);
                viewHolder.viewMain.setLayoutParams(params);
            }

            viewHolder.viewMain.setOnClickListener(view -> {
                setSelectedItem(i);

                callback.actionItem(i, null);
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void setSelectedItem(int position){
            for (int i = 0; i < mItems.size(); i++)
                mItems.get(i).setSelected(!mItems.get(i).isSelected() && i == position);

            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_text)
            TextView tvText;
            View viewMain;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                this.viewMain = itemView;
            }
        }
    }
}
