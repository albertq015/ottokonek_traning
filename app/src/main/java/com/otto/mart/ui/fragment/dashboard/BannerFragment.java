package com.otto.mart.ui.fragment.dashboard;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Response.olshop.Category;
import com.otto.mart.model.localmodel.WebViewContent;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.ui.activity.olshop.CatalogActivity;
import com.otto.mart.ui.activity.webView.WebViewActivity;

import java.util.List;

import app.beelabs.com.codebase.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BannerFragment extends BaseFragment {

    public static final String KEY_BANNER_IMAGE = "banner_image";
    public static final String KEY_BANNER_URL = "banner_url";

    private ImageView imgBanner;
    private String mBannerUrl = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        String imageUrl = getArguments().getString(KEY_BANNER_IMAGE);
        mBannerUrl = getArguments().getString(KEY_BANNER_URL);

        initView(view);
        displayBanner(imageUrl);
        return view;
    }

    private void initView(View view) {
        imgBanner = view.findViewById(R.id.img_banner);

        imgBanner.setOnClickListener(v -> {
            if ((mBannerUrl != null) && (!mBannerUrl.equals(""))) {
                Uri uri = Uri.parse(mBannerUrl);

                /*handle banner for olshop event*/
                if (uri.getScheme() != null) {
                    if (uri.getScheme().equalsIgnoreCase("app")) {
                        List<Category> categories = getCategoriesData();
                        if (categories != null && !categories.isEmpty()) {
                            Intent intent = new Intent(getContext(), CatalogActivity.class);
                            intent.putExtra("category", new Gson().toJson(categories));
                            intent.putExtra("isSpecialEvent", true);

                            startActivity(intent);
                            return;
                        } else
                            Toast.makeText(getContext(), "Kategori kosong", Toast.LENGTH_SHORT).show();
                    }
                }

                /*usual banner handler*/
                gotoWebView(new WebViewContent("Banner Detail", mBannerUrl));
            }
        });
    }

    private List<Category> getCategoriesData() {
        String categoriesData = Pref.getPreference().getString("categories");
        if (categoriesData != null && !categoriesData.isEmpty()) {
            return new Gson().fromJson(categoriesData, new TypeToken<List<Category>>() {
            }.getType());
        }
        return null;
    }

    private void displayBanner(String imageUrl) {
        Glide.with(getActivity())
                .load(imageUrl)
                .apply(new RequestOptions().placeholder(R.drawable.border_white)
                        .error(R.drawable.border_white))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(imgBanner);
    }

    private void gotoWebView(WebViewContent webViewContent) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(AppKeys.KEY_WEBVIEW_CONTENT, webViewContent);
        startActivity(intent);
    }
}