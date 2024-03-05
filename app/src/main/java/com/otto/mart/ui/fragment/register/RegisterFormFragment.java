package com.otto.mart.ui.fragment.register;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.TransitionInflater;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.BusinessCategoryModel;
import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.model.localmodel.State.RegisterStateModel;
import com.otto.mart.ui.activity.register.IRegister;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.SapiEdittext;
import com.otto.mart.ui.component.dialog.ChainedListDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.beelabs.com.codebase.base.BaseFragment;

public class RegisterFormFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private IRegister jenk;
    private View mView, action_bc, action, let_borndate;
    private LazyEdittext let_shopname, let_ownername, let_email, let_phone;
    private TextView tv_bc, tv_borndate;
    private boolean isHooked = true;
    private SignupRequestModel data;
    private RegisterStateModel state;
    private ChainedListDialog bc;
    BusinessCategoryModel selectedModel;
    private DatePickerDialog dpd;
    private SapiEdittext mooooo;

    private Date selectedDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register_form1, container, false);
        try {
            this.jenk = (IRegister) getActivity();
            data = loadRequestModel();
            state = loadStateModel();
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

    private RegisterStateModel loadStateModel() {
        RegisterStateModel returnObject = null;
        if (isHooked) {
            returnObject = jenk.getStateModel();
        }
        return returnObject;

    }

    private void initComponent() {
        let_shopname = mView.findViewById(R.id.let_shopname);
        let_ownername = mView.findViewById(R.id.let_merchantname);
        let_email = mView.findViewById(R.id.let_email);
        let_phone = mView.findViewById(R.id.let_mobile);
        action_bc = mView.findViewById(R.id.layout_bc);
        let_borndate = mView.findViewById(R.id.let_borndate);
        action = mView.findViewById(R.id.action);
        tv_bc = mView.findViewById(R.id.tv_bc);
        tv_borndate = mView.findViewById(R.id.tv_borndate);
        dpd = new DatePickerDialog(getContext(), this, 1990, 0, 1);

        ((EditText) let_phone.getComponent()).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_PHONE);
//        ((EditText) let_borndate.getComponent()).setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);


        bc = jenk.getBcDialog();

        mooooo = mView.findViewById(R.id.sapi);
        ((EditText) mooooo.getComponent()).setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void initContent() {
        if (data.getName() != null) {
            let_shopname.setContentText(data.getName() != null ? data.getName() : "");
            let_ownername.setContentText(data.getMerchant_name() != null ? data.getMerchant_name() : "");
            let_email.setContentText(data.getEmail() != null ? data.getEmail() : "");
            let_phone.setContentText(data.getPhone() != null ? data.getPhone() : "");
            tv_borndate.setText(state.getDob() != null ? state.getDob() + "" : "");
            if (state.getBcmState() != null) {
                tv_bc.setText(state.getBcmState().getName());
                selectedModel = state.getBcmState();
            }
        }

        bc.setListener(new ChainedListDialog.ChainedListDialogListener() {
            @Override
            public void onSecondaryCallback(BusinessCategoryModel returnItem) {
                selectedModel = returnItem;
                tv_bc.setText(returnItem.getName());
                state.setBcmState(selectedModel);
            }
        });

        action_bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bc.show();
            }
        });
        tv_bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action_bc.performClick();
                // TODO: 03/09/18 need optimize
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHooked) {
                    if (validator()) {
                        data.setName(let_shopname.getTextContent());
                        data.setMerchant_name(let_ownername.getTextContent());
                        data.setEmail(let_email.getTextContent());
                        data.setPhone(let_phone.getTextContent());
                        data.setMother_name("Siti Badriah");
                        data.setDob(selectedDate.getTime());
                        data.setBusiness_category_id(selectedModel.getId());
                        jenk.updateModel(data);
                        jenk.moveToNext();
                    }
                }
            }
        });
        let_borndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
            }
        });
        tv_borndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
                // TODO: 03/09/18 need optimize
            }
        });
    }

    private boolean validator() {
        boolean isValid = true;
        if (!let_shopname.isLengthValid()) {
            isValid = false;
        }
        if (!let_ownername.isLengthValid()) {
            isValid = false;
        }
        if (!let_email.isLengthValid()) {
            isValid = false;
        }
        if (!let_email.isEmailValid()) {
            isValid = false;
        }
        if (!let_phone.isLengthValid()) {
            isValid = false;
        }
        if (selectedModel == null) {
            isValid = false;
        }
        if (selectedDate == null) {
            isValid = false;
            Toast.makeText(getContext(), "Mohon input Tanggal Lahir", Toast.LENGTH_SHORT).show();
            dpd.show();
        }
        return isValid;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar temp = Calendar.getInstance();
        temp.set(year, month, dayOfMonth);
        selectedDate = temp.getTime();
        state.setDob(new SimpleDateFormat("dd MMMM yyyy").format(selectedDate));
        tv_borndate.setText(state.getDob());
    }
}
