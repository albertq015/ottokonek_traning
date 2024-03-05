package com.otto.mart.ui.activity.olshop;

import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.otto.mart.R;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.fragment.olshop.OrderStatusFragment;

import app.beelabs.com.codebase.component.ProgressDialogComponent;

public class OrderStatusActivity extends AppActivity implements OrderStatusViewInterface {

    LinearLayout toolbar_back_hitbox;
    TextView tv_date,title;
    RadioGroup toggle;
    RadioButton positive;
    RadioButton negative;
    OrderStatusFragment orderProcess, orderComplete;

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.white);
        setStatusBarLightMode();
        setContentView(R.layout.activity_orderstatus);
        initComponent();
        initContent();
        initFragment();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initComponent() {
        toolbar_back_hitbox = findViewById(R.id.btnToolbarBack);
        tv_date = findViewById(R.id.tv_date);
        toggle = findViewById(R.id.toggle);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        title = findViewById(R.id.tvToolbarTitle);
    }

    private void initContent() {
        title.setText(getString(R.string.label_order_status));

        toggle.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.positive:
                    onInProcessTabPressed();
                    break;

                case R.id.negative:
                    onInItemDonePressed();
                    break;

            }
        });

        toolbar_back_hitbox.setOnClickListener(view -> closeActivity());

    }

    private void initFragment() {
        orderProcess = OrderStatusFragment.newInstance(false);
        orderComplete = OrderStatusFragment.newInstance(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, orderProcess).disallowAddToBackStack().commit();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void addDataForInProcess(Object model) {

    }

    @Override
    public void addDataForFinished(Object model) {

    }

    @Override
    public void onItemInProcessPressed(int id) {

    }

    @Override
    public void onItemFinishedPressed(int id) {

    }

    @Override
    public void onInProcessTabPressed() {
//        positive.setTextColor(ContextCompat.getColor(this, R.color.color_white));
//        negative.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue_40));
        super.onBackPressed();
    }

    @Override
    public void onInItemDonePressed() {
//        positive.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue_40));
//        negative.setTextColor(ContextCompat.getColor(this, R.color.color_white));
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, orderComplete).addToBackStack("complete").commit();
    }

    @Override
    public void showLoadingDialog() {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show();
    }

    @Override
    public void hideLoadingDialog() {
        ProgressDialogComponent.dismissProgressDialog(this);
    }

}
