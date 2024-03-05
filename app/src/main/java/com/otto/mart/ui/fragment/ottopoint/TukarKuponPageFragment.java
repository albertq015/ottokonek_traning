package com.otto.mart.ui.fragment.ottopoint;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.TukarPointItem;
import com.otto.mart.support.util.PopupUtil;
import com.otto.mart.ui.Partials.adapter.TukarKuponExploreAdapter;
import com.otto.mart.ui.Partials.adapter.TukarKuponSayaAdapter;
import com.otto.mart.ui.activity.ottopoint.DetailKuponActivity;
import com.otto.mart.ui.activity.ottopoint.TukarKuponActivity;
import com.otto.mart.ui.component.dialog.DecisionDialog;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseFragment;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.otto.mart.keys.AppKeys.KEY_BROADCAST_REFRESH_KUPON_SAYA;

public class TukarKuponPageFragment extends BaseFragment {

    private String TAG = TukarKuponPageFragment.class.getSimpleName();

    public static final String KEY_TYPE = "key_type";

    public static final int CODE_TYPE_EXPLORE = 1;
    public static final int CODE_TYPE_KUPONSAYA = 2;

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.view_empty)
    View viewEmpty;

    private TukarKuponExploreAdapter adapter;
    private TukarKuponSayaAdapter adapterKuponSaya;
    private List<TukarPointItem> mItems = new ArrayList<>();
    private Bundle arguments;

    public static TukarKuponPageFragment newInstance() {

        Bundle args = new Bundle();

        TukarKuponPageFragment fragment = new TukarKuponPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static TukarKuponPageFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);

        TukarKuponPageFragment fragment = new TukarKuponPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arguments = getArguments();

        if(getActivity() != null) getActivity().registerReceiver(broadcastReceiver, new IntentFilter(KEY_BROADCAST_REFRESH_KUPON_SAYA));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(getActivity() != null) getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tukar_kupon_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViewByType(getType());
    }

    private void configureList(){
        if(list == null) return;

        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        switch (getType()){
            case CODE_TYPE_EXPLORE:
                adapter = new TukarKuponExploreAdapter(getActivity(), mItems, data -> showPopupTukarKupon(
                        list,
                        data.getString(TukarKuponExploreAdapter.KEY_SALDO_OTTOPAY),
                        data.getString(TukarKuponExploreAdapter.KEY_HARGA),
                        data.getInt(TukarKuponExploreAdapter.KEY_ID)
                ));

                list.setAdapter(adapter);
                break;

            case CODE_TYPE_KUPONSAYA:
                adapterKuponSaya = new TukarKuponSayaAdapter(getActivity(), mItems, data ->
                        DetailKuponActivity.moveToHere(
                                getActivity(),
                                data.getInt(TukarKuponSayaAdapter.KEY_ID),
                                data.getString(TukarKuponSayaAdapter.KEY_SALDO_OTTOPAY_TEXT)
                        )
                );

                list.setAdapter(adapterKuponSaya);
                break;
        }
    }

    private void setDummyData(){
        for (int i = 1; i <= 10; i++)
            mItems.add(new TukarPointItem(i, (i * 100000), (i * 100)));
    }

    @SuppressLint("SetTextI18n")
    private void showPopupTukarKupon(View view, String saldoOttopay, String harga, int idItem){
        if(getActivity() == null) return;

        // create the popup window
        View popupView = ((LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_tukar_kupon, null);
        PopupWindow popupWindow = PopupUtil.generate(popupView, view);

        // init view
        ImageView btnClose = popupView.findViewById(R.id.view_close);
        TextView tvSaldoOttopay = popupView.findViewById(R.id.tv_saldo_ottopay);
        TextView tvHarga = popupView.findViewById(R.id.tv_harga);
        TextView tvCurrentPoin = popupView.findViewById(R.id.tv_current_poin);
        TextView btnTukar = popupView.findViewById(R.id.btn_tukar);

        // set view
        tvSaldoOttopay.setText(saldoOttopay);
        tvHarga.setText(harga);
        btnTukar.setText(getText(R.string.label_tukar) + " " + harga);
        tvCurrentPoin.setText(Double.toString(((TukarKuponActivity) getActivity()).getPoin_ottopoint()));

        // events

        btnClose.setOnClickListener(v -> {
            if(popupWindow != null)
                popupWindow.dismiss();
        });

        btnTukar.setOnClickListener(v -> {
            if(popupWindow != null)
                popupWindow.dismiss();

            DecisionDialog.showDecisionTukarPoint(getActivity(), saldoOttopay, data ->
                    actionTukarPoin(data, saldoOttopay, idItem)
            );
        });
    }

    private void actionTukarPoin(Bundle data, String harga, int idItem){
        callApiTransactionCoupon(idItem, harga);
    }

    private void callApiGetAllCoupon(){
        showProgress(true);
    }

    private void callApiTransactionCoupon(int id, String harga){
        showProgress(true);
    }

    private void setViewByType(int type){
        switch (type){
            case CODE_TYPE_EXPLORE:
                callApiGetAllCoupon();
                break;

            case CODE_TYPE_KUPONSAYA:
                callApiGetKuponSaya();
                break;

            default:
                break;
        }
    }

    private void showProgress(boolean isShow){
        if(isShow)
            ProgressDialogComponent.showProgressDialog(getActivity(), "Loading", false).show();
        else
            ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
    }

    private void callApiGetKuponSaya(){
        showProgress(true);
    }

    private String getValue(){
        return "";
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null) return;
            if(intent.getAction() == null) return;

            switch (intent.getAction()){
                case KEY_BROADCAST_REFRESH_KUPON_SAYA:
                    if(mItems != null) mItems.clear();

                    setViewByType(getType());
                    break;

                default:
                    break;
            }
        }
    };

    private int getType(){
        if(arguments != null && arguments.containsKey(KEY_TYPE))
            return arguments.getInt(KEY_TYPE);

        return -1;
    }

    private void showList(boolean isShow){
        if(isShow){
            list.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.GONE);
        }else{
            list.setVisibility(View.GONE);
            viewEmpty.setVisibility(View.VISIBLE);
        }
    }
}
