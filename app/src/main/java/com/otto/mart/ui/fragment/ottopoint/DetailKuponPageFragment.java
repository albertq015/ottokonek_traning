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

public class DetailKuponPageFragment extends BaseFragment {

    private String TAG = DetailKuponPageFragment.class.getSimpleName();

    public static final String KEY_TITLE = "key_title";
    public static final String KEY_DESC = "key_desc";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_desc)
    TextView tvDesc;

    private Bundle arguments;

    public static DetailKuponPageFragment newInstance(Bundle args) {
        DetailKuponPageFragment fragment = new DetailKuponPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arguments = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_kupon_page, container, false);
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

    private String getTitle(){
        if(arguments != null && arguments.containsKey(KEY_TITLE))
            return arguments.getString(KEY_TITLE);

        return "";
    }

    private String getDescription(){
        if(arguments != null && arguments.containsKey(KEY_DESC))
            return arguments.getString(KEY_DESC);

        return "";
    }
}
