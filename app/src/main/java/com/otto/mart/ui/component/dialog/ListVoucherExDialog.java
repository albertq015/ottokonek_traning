package com.otto.mart.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.VoucherPointItemModel;
import com.otto.mart.ui.Partials.adapter.ottopoint.VoucherPoinAktifAdapter;
import com.otto.mart.ui.activity.ottopoint.DetailVoucherActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListVoucherExDialog extends BaseBottomDialog {

    @BindView(R.id.list)
    RecyclerView list;

    private List<VoucherPointItemModel> mItems;

    private ListVoucherExDialog(@NonNull Context context, int themeResId, List<VoucherPointItemModel> mItems) {
        super(context, themeResId);

        this.mItems = mItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_voucher_ex);
        ButterKnife.bind(this);

        configureList();
    }

    @OnClick(R.id.btn_close)
    public void closeDialog(){
        dismiss();
    }

    private void configureList(){
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setItemAnimator(new DefaultItemAnimator());

        VoucherPoinAktifAdapter adapter = new VoucherPoinAktifAdapter(getContext(), mItems, false, (position, data) ->{
            dismiss();

            DetailVoucherActivity.openPageActive(getContext(), data);
        });
        list.setAdapter(adapter);
    }

    public static void showDialog(Context context, List<VoucherPointItemModel> mItems){
        new ListVoucherExDialog(context, R.style.CustomDialog_Clear_FromBottom, mItems).show();
    }
}
