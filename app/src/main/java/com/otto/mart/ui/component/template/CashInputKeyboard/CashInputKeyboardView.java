package com.otto.mart.ui.component.template.CashInputKeyboard;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.otto.mart.R;
import com.otto.mart.support.util.widget.MyCurrencyEditText;
import com.otto.mart.ui.component.IComponent;

import java.text.ParseException;

public class CashInputKeyboardView extends LinearLayout implements IComponent {
    private Context mContext;
    private MyCurrencyEditText cet_val;
    private View k1, k2, k3, k4, k5, k6, k7, k8, k9, k0, k000, kx;

    public CashInputKeyboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_keyboard, this);
        initComponent();
        initContent();
    }

    private void initComponent() {
        cet_val = findViewById(R.id.cet_val);
        k1 = findViewById(R.id.l_1);
        k2 = findViewById(R.id.l_2);
        k3 = findViewById(R.id.l_3);
        k4 = findViewById(R.id.l_4);
        k5 = findViewById(R.id.l_5);
        k6 = findViewById(R.id.l_6);
        k7 = findViewById(R.id.l_7);
        k8 = findViewById(R.id.l_8);
        k9 = findViewById(R.id.l_9);
        k0 = findViewById(R.id.l_0);
        k000 = findViewById(R.id.l_000);
        kx = findViewById(R.id.l_b);

    }

    private void initContent() {
        OnClickListener keypressListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.l_1:
                        cet_val.setText(cet_val.getText() + "1");
                        break;
                    case R.id.l_2:
                        cet_val.setText(cet_val.getText() + "2");
                        break;
                    case R.id.l_3:
                        cet_val.setText(cet_val.getText() + "3");
                        break;
                    case R.id.l_4:
                        cet_val.setText(cet_val.getText() + "4");
                        break;
                    case R.id.l_5:
                        cet_val.setText(cet_val.getText() + "5");
                        break;
                    case R.id.l_6:
                        cet_val.setText(cet_val.getText() + "6");
                        break;
                    case R.id.l_7:
                        cet_val.setText(cet_val.getText() + "7");
                        break;
                    case R.id.l_8:
                        cet_val.setText(cet_val.getText() + "8");
                        break;
                    case R.id.l_9:
                        cet_val.setText(cet_val.getText() + "9");
                        break;
                    case R.id.l_0:
                        cet_val.setText(cet_val.getText() + "0");
                        break;
                    case R.id.l_000:
                        if(cet_val.getText().length() < 11){
                            cet_val.setText(cet_val.getText() + "000");
                        }
                        break;
                    case R.id.l_b:
                        cet_val.setText(del(cet_val.getText() + ""));
                        break;
                }
            }
        };

        k1.setOnClickListener(keypressListener);
        k2.setOnClickListener(keypressListener);
        k3.setOnClickListener(keypressListener);
        k4.setOnClickListener(keypressListener);
        k5.setOnClickListener(keypressListener);
        k6.setOnClickListener(keypressListener);
        k7.setOnClickListener(keypressListener);
        k8.setOnClickListener(keypressListener);
        k9.setOnClickListener(keypressListener);
        k0.setOnClickListener(keypressListener);
        k000.setOnClickListener(keypressListener);
        kx.setOnClickListener(keypressListener);
    }

    public void setText(String value) {
        cet_val.setText(value);
    }

    public String getText() {
        return cet_val.getText() + "";
    }

    public String getCurrencyText() throws ParseException {
        return cet_val.getCurrencyText() + "";
    }

    public void resetCurrencyText() {
        cet_val.setText("");
    }

    @Override
    public String getTextContent() {
        try {
            return cet_val.getCurrencyText();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public View getComponent() {
        return null;
    }

    @Override
    public void setTitle(String text) {

    }

    protected String del(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        } else if (str != null && str.length() == 1) {
            str = "";
        }
        return str;
    }
}
