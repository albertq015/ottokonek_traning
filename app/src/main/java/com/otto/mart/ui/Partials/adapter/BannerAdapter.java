package com.otto.mart.ui.Partials.adapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.otto.mart.OttoMartApp;
import com.otto.mart.R;

import java.util.Arrays;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private int widthImage;

    private List<Integer> bannerList = Arrays.asList(
            R.drawable.banner_apps_2_0__1,
            R.drawable.banner_apps_2_0__2,
            R.drawable.banner_apps_2_0__3
    );

    public BannerAdapter(int widthImage) {
        this.widthImage = widthImage;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.image.getLayoutParams();
        layoutParams.width = widthImage;
        layoutParams.height = WRAP_CONTENT;
        layoutParams.setMarginEnd(20);
        holder.image.setLayoutParams(layoutParams);

        holder.image.setImageDrawable(ContextCompat.getDrawable(OttoMartApp.getContext(), bannerList.get(position)));
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public BannerViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
