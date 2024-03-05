package com.otto.mart.ui.fragment.kyc;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otto.mart.R;
import com.otto.mart.ui.activity.register.kyc.RegisterAccountActivationActivity;

public class UpgKYCFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kyc_upg, container, false);
        View skip = view.findViewById(R.id.skip);
        View upgrade = view.findViewById(R.id.upgrade);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterAccountActivationActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }
}
