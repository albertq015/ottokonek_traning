package com.otto.mart.ui.fragment.register;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.TransitionInflater;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.ui.activity.register.IRegister;
import com.otto.mart.ui.activity.syaratDanKetentuan.SnKActivity;
import com.otto.mart.ui.component.LazyEdittext;

import app.beelabs.com.codebase.base.BaseFragment;

public class RegisterPinFragment extends BaseFragment {
    private IRegister jenk;
    private View mView;
    private SignupRequestModel data;

    private View secqBtn, action2, layout_secqhitb, layout_pin, imgvj;
    private TextView action;
    private LazyEdittext answr;
    private TextView tv_pin, tv_hund;
    private TextView question;
    private Boolean isHooked = true;
    private String pin;
    private CheckBox tos;
    boolean isTosChecked = false;

    private boolean isUpdateStatus = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fagment_register_pin_main, container, false);
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
        secqBtn = mView.findViewById(R.id.pin_q_next);
        layout_secqhitb = mView.findViewById(R.id.sec_q_cuntainer);
        layout_pin = mView.findViewById(R.id.layout_pin);
        tv_pin = mView.findViewById(R.id.tv_pin);
        tv_hund = mView.findViewById(R.id.tv_hund);
        imgvj = mView.findViewById(R.id.imgvj);
        action = mView.findViewById(R.id.action);
        action2 = mView.findViewById(R.id.action2);
        answr = mView.findViewById(R.id.pin_answ);
        question = mView.findViewById(R.id.question);
        tos = mView.findViewById(R.id.checkbox_tos);

        answr.setVisibility(View.GONE);
    }

    private void initContent() {
        if(isUpdateStatus){
            action.setText(getString(R.string.text_save_pin));
        }

        layout_secqhitb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jenk.callSecAnswerPicker();
            }
        });
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(isUpdateStatus){
                   jenk.callUpdateStatus(answr.getTextContent());
               } else {
                   data.setPin(pin);
                   data.setPin_confirmation(pin);
                   data.setAnswer(answr.getTextContent());
                   data.setRose(true);
                   jenk.callRegister();
               }
            }
        });

        layout_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jenk.callPinBullshit();
            }
        });
        tos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isTosChecked = true;
                } else {
                    isTosChecked = false;
                }
                if (pin != null && isTosChecked && answr.getTextContent() != null && answr.getTextContent().length() > 3)
                    action.setVisibility(View.VISIBLE);
                else
                    action.setVisibility(View.INVISIBLE);
            }
        });
        action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SnKActivity.class));
            }
        });

        ((EditText) answr.getComponent()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pin != null && isTosChecked && answr.getTextContent() != null && answr.getTextContent().length() > 3)
                    action.setVisibility(View.VISIBLE);
                else
                    action.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setQuestion(String text) {
        question.setText(text);
        answr.setVisibility(View.VISIBLE);
        answr.setContentText("");
    }

    public void addPin(String pin) {
        this.pin = pin;
        tv_pin.setText(getString(R.string.text_pin_saved));
        tv_pin.setCompoundDrawables(ContextCompat.getDrawable(getActivity(), R.drawable.ic_done_all_green), null, null, null);
        imgvj.setVisibility(View.GONE);
        tv_hund.setVisibility(View.VISIBLE);
        if (pin != null && isTosChecked && answr.getTextContent() != null && answr.getTextContent().length() > 3)
            action.setVisibility(View.VISIBLE);
        else
            action.setVisibility(View.INVISIBLE);
    }

    public void setUpdateStatus(boolean updateStatus) {
        isUpdateStatus = updateStatus;
    }
}
