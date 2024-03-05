package com.otto.mart.ui.fragment.ottopoint;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otto.mart.R;

import app.beelabs.com.codebase.base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailInfoFragment extends BaseFragment {

    private String TAG = DetailInfoFragment.class.getSimpleName();

    public static final String KEY_TYPE = "KEY_DEALS_TYPE";
    public static final String KEY_TITLE = "KEY_DEALS_TITLE";
    public static final String KEY_DESCRIPTION = "KEY_DEALS_DESC";
    public static final int CODE_TYPE_DESC = 0;
    public static final int CODE_TYPE_SYARAT = 1;
    public static final int CODE_TYPE_CARA_PAKAI = 2;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_desc)
    TextView tvDesc;

    private Bundle argument;

    public static DetailInfoFragment newInstance(Bundle args) {
        DetailInfoFragment fragment = new DetailInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null)
            argument = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_info_deals, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set view
        tvTitle.setText(getTitle());
        tvDesc.setText(getDescription());
    }

    private int getType(){
        if(getArguments() != null && getArguments().containsKey(KEY_TITLE))
            return argument.getInt(KEY_TYPE);

        return -1;
    }

    private String getTitle(){
        if(getArguments() != null && getArguments().containsKey(KEY_TITLE) && getArguments().getString(KEY_TITLE) != null)
            return getArguments().getString(KEY_TITLE);

        return "" ;
    }

    private String getDescription(){
        if(getArguments() != null && getArguments().containsKey(KEY_DESCRIPTION) && getArguments().getString(KEY_DESCRIPTION) != null)
            return getArguments().getString(KEY_DESCRIPTION);

        return "" ;
    }
}
