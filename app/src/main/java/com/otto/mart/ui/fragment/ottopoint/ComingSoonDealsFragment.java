package com.otto.mart.ui.fragment.ottopoint;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otto.mart.R;

import app.beelabs.com.codebase.base.BaseFragment;
import butterknife.ButterKnife;

public class ComingSoonDealsFragment extends BaseFragment {

    private String TAG = ComingSoonDealsFragment.class.getSimpleName();

    public static ComingSoonDealsFragment newInstance() {

        Bundle args = new Bundle();

        ComingSoonDealsFragment fragment = new ComingSoonDealsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coming_soon_deals, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
