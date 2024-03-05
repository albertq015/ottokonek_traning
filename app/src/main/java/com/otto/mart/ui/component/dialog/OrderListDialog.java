package com.otto.mart.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.OrderListItemModel;
import com.otto.mart.ui.Partials.adapter.ottopoint.OrderListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListDialog extends BaseBottomDialog {

    public static final int TYPE_DEALS = 1;
    public static final int TYPE_VOUCHER_SAYA = 2;

    @BindView(R.id.list)
    RecyclerView list;

    private String selectedShort = "";
    private int type = -1;
    private ActionOrder callback;

    private OrderListAdapter adapter;
    private List<OrderListItemModel> mItems = new ArrayList<>();

    private OrderListDialog(@NonNull Context context, int themeResId, int type, String selectedShort, ActionOrder callback) {
        super(context, themeResId);

        this.type = type;
        this.selectedShort = selectedShort;
        this.callback = callback;
    }

    private OrderListDialog(@NonNull Context context, int themeResId, int type, ActionOrder callback) {
        super(context, themeResId);

        this.type = type;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_list);
        ButterKnife.bind(this);

        setItems();
        configureList();
        setSelectedShort();
    }

    @OnClick(R.id.view_back)
    public void closeDialog(){
        dismiss();
    }

    private void setItems(){
        if(this.type == TYPE_DEALS){
            int id = 1;
            for (String item: getContext().getResources().getStringArray(R.array.items_order_deals))
                mItems.add(new OrderListItemModel(id++, item, false));
        } else if (this.type == TYPE_VOUCHER_SAYA) {
            int id = 1;
            for (String item: getContext().getResources().getStringArray(R.array.items_order_voucher_saya))
                mItems.add(new OrderListItemModel(id++, item, false));
        }
    }

    private void configureList(){
        if(adapter == null)
            adapter = new OrderListAdapter(mItems, (position, data) -> {
                callback.resultOrder(position, mItems.get(position).getTitle());

                closeDialog();
            });

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);
    }

    private void refreshList(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
        else
            configureList();
    }

    private void setSelectedShort(){
        if(selectedShort != null && !selectedShort.isEmpty()){
            for (int i = 0; i < mItems.size(); i++)
                if(mItems.get(i).getTitle().equalsIgnoreCase(selectedShort)){
                    mItems.get(i).setSelected(true);
                    break;
                }

            refreshList();
        }
    }

    public static void showDialog(Context context, int type, String selectedShort, ActionOrder callback){
        new OrderListDialog(context, R.style.CustomDialog_Clear_FromBottom, type, selectedShort, callback).show();
    }

    public static void showDialog(Context context, int type, ActionOrder callback){
        new OrderListDialog(context, R.style.CustomDialog_Clear_FromBottom, type, callback).show();
    }

    public interface ActionOrder{

        void resultOrder(int position, String text);
    }
}
