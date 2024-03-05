package com.otto.mart.ui.fragment.register;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.TransitionInflater;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.ui.Partials.adapter.BankItemRecyclerAdapter;
import com.otto.mart.ui.activity.register.IRegister;
import com.tooltip.Tooltip;

import net.cachapa.expandablelayout.ExpandableLayout;

public class RegisterAddressFragment extends Fragment {

    private IRegister jenk;
    private View mView;
    private View layout_addressInputTrigger, v_anchor1;
    private TextView textview_address, textview_maptrigger, textView_map, textview_tambahbank;
    private RecyclerView rv_bankfragcontainer;
    private SignupRequestModel data;
    private ImageView imgv_arrow;
    private ExpandableLayout eLayout_expandong, eLayout_expandongB, layout_bankInputTrigger;
    private BankItemRecyclerAdapter bankRecyclerAdapter;
    private boolean isAddressConfirmEntered = false;

    private View secqBtn, action;

    private boolean isHooked = true;
    Tooltip hintBubble;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register_form2, container, false);
        try {
            this.jenk = (IRegister) getActivity();
            data = loadRequestModel();
        } catch (Exception e) {
            isHooked = false;
        }
        initComponent();
        initContent();
        return mView;
    }


    private SignupRequestModel loadRequestModel() {
        SignupRequestModel returnObject = null;
        if (isHooked) {
            returnObject = jenk.getModel();
        }
        return returnObject;
    }

    private void initComponent() {
        action = mView.findViewById(R.id.action);
        textview_address = mView.findViewById(R.id.textview_address);
        textview_maptrigger = mView.findViewById(R.id.textview_address_3);
        textView_map = mView.findViewById(R.id.textview_address_2);
        textview_tambahbank = mView.findViewById(R.id.textview_tambahbank);
        rv_bankfragcontainer = mView.findViewById(R.id.rv_bankfragcontainer);
        layout_addressInputTrigger = mView.findViewById(R.id.layout_address);
        layout_bankInputTrigger = mView.findViewById(R.id.layout_bank);
        eLayout_expandong = mView.findViewById(R.id.eLayout_expandong);
        eLayout_expandongB = mView.findViewById(R.id.eLayout_expandong_b);
        imgv_arrow = mView.findViewById(R.id.imgv_arrow);
        v_anchor1 = mView.findViewById(R.id.v_anchor1);


    }

    private void initContent() {
        eLayout_expandongB.setVisibility(View.GONE);
        bankRecyclerAdapter = new BankItemRecyclerAdapter(-1);
        rv_bankfragcontainer.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_bankfragcontainer.setAdapter(bankRecyclerAdapter);

//        textView_map.setOnClickListener(new View.OnClickListener() {
        mView.findViewById(R.id.layout_address_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                jenk.callMap();
                isAddressConfirmEntered = true;
                jenk.callAddressInput();
            }
        });
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHooked) {
                    jenk.moveToNext();
                }
            }
        });

        layout_addressInputTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jenk.callMap();
            }
        });
        layout_bankInputTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHooked) {
                    if (eLayout_expandongB.isExpanded()) {
                        eLayout_expandongB.setExpanded(false);
                        imgv_arrow.clearAnimation();
                    } else {
                        eLayout_expandongB.recomputeViewAttributes(rv_bankfragcontainer);
                        eLayout_expandongB.setExpanded(true);
                        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
                        anim.setRepeatCount(0);
                        imgv_arrow.setAnimation(anim);
                    }
                }
            }
        });

        textview_tambahbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jenk.callBankSelector();
            }
        });
        updateInterface();
    }

    public void updateInterface() {
        if (data.getComplete_address() != null) {
            if (hintBubble != null)
                hintBubble.dismiss();
            textView_map.setText(data.getComplete_address() != null ? data.getComplete_address() : "-");
            eLayout_expandong.expand();

            hintBubble = new Tooltip.Builder(v_anchor1)
                    .setText("Klik disini untuk Konfirmasi alamat")
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.squash))
                    .setTextColor(ContextCompat.getColor(getContext(), R.color.color_white))
                    .setPadding(20f)
                    .setDismissOnClick(true).build();
//            layout_bankInputTrigger.setVisibility(View.VISIBLE);
            hintBubble.show();
        }

        bankRecyclerAdapter.setModels(jenk.getBankUiModels());
//        if (jenk.getBankUiModels().size() > 0 && data.getComplete_address() != null && isAddressConfirmEntered)
//            action.setVisibility(View.VISIBLE);

        if (data.getComplete_address() != null && data.getVillage_id() > 0 && isAddressConfirmEntered) {
            action.setVisibility(View.VISIBLE);
            layout_bankInputTrigger.setExpanded(true);
            eLayout_expandongB.setExpanded(true);
        }
    }

    public BankItemRecyclerAdapter getBankRecyclerAdapter() {
        return bankRecyclerAdapter;
    }
}