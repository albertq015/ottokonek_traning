package com.otto.mart.ui.activity.qr;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.otto.mart.R;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.fragment.qr.payment.QrPayScanFragment;
import com.otto.mart.ui.fragment.qr.payment.QrPayShowFragment;

//activity not in use
public class QrReadScanOldActivity extends AppActivity {

    private View back;
    private RadioGroup fragment_switcher;
    private FragmentManager fm;
    private Fragment child1, child2;
    private Window thiswindow;
    private boolean isHome = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_qr_multifucc);

        thiswindow = this.getWindow();

        initPermissions();
        try {
            initComponent();
            initFragments();
            initContent();
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        if (isHome) {
            finish();
        } else {
            ((RadioButton) findViewById(R.id.positive)).setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initComponent() {
        fragment_switcher = findViewById(R.id.toggle);
        back = findViewById(R.id.backhitbox);
    }

    private void initFragments() {
        child1 = new QrPayShowFragment();
        child2 = new QrPayScanFragment();
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragmentContainer, child1).commit();
    }

    private void initContent() {
        final int unselectedColor = ContextCompat.getColor(this, R.color.ocean_blue_40);
        final int selectedColor = ContextCompat.getColor(this, R.color.color_white);

        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);

        fragment_switcher.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.positive) {
                    //fm.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(R.id.fragment_container, child1).commit();
                    showFragment(child1, R.id.fragmentContainer);
                    ((RadioButton) findViewById(R.id.positive)).setTextColor(selectedColor);
                    ((RadioButton) findViewById(R.id.negative)).setTextColor(unselectedColor);
                    isHome = true;

                } else if (checkedId == R.id.negative) {
                    if (QrReadScanOldActivity.this.getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(
                                QrReadScanOldActivity.this.getCurrentFocus().getWindowToken(), 0);
                    //fm.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(R.id.fragment_container, child2).commit();
                    showFragment(child2, R.id.fragmentContainer);
                    ((RadioButton) findViewById(R.id.positive)).setTextColor(unselectedColor);
                    ((RadioButton) findViewById(R.id.negative)).setTextColor(selectedColor);
                    isHome = false;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initPermissions() {
//        Nammu.init(getApplicationContext());
//        if (!Nammu.checkPermission(Manifest.permission.CAMERA)) {
//            Nammu.askForPermission(this, Manifest.permission.CAMERA, new PermissionCallback() {
//                @Override
//                public void permissionGranted() {
//                    initComponent();
//                    initContent();
//                }
//
//                @Override
//                public void permissionRefused() {
//                    finish();
//                }
//            });
//        }
    }
}