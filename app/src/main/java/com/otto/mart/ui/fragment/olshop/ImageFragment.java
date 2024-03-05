package com.otto.mart.ui.fragment.olshop;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.otto.mart.R;

public class ImageFragment extends Fragment {

    ImageView imageView;

    public static ImageFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        imageView = view.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.pos);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Glide.with(view.getContext()).load(getArguments().getString("url")).into(imageView);
    }
}
