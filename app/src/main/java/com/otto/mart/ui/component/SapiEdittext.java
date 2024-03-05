package com.otto.mart.ui.component;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.otto.mart.R;

import net.cachapa.expandablelayout.ExpandableLayout;

public class SapiEdittext extends LazyEdittext {
    ExpandableLayout eLayout;
    TextView tv_trigger;


    public SapiEdittext(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAdditionalComponent();
    }

    private void initAdditionalComponent() {
        eLayout = findViewById(R.id.eLayout);
        tv_trigger = findViewById(R.id.tv_trigger);

        tv_trigger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eLayout.isExpanded()) {
                    eLayout.collapse();
                    tv_trigger.setText("Klik Disini");
                } else {
                    eLayout.expand();
                    tv_trigger.setText("Tutup");

                }
            }
        });
    }


}
