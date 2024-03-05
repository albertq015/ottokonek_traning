package com.otto.mart.ui.Partials.adapter.ottopoint;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.ottopoint.ViewPagerItem;
import com.otto.mart.support.util.DownloadImageManager;
import com.otto.mart.ui.actionView.ActionListItemTwo;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;
import com.tiagosantos.enchantedviewpager.EnchantedViewPagerAdapter;

import java.util.List;

public class PromoVPAdapter extends EnchantedViewPagerAdapter {

    private Context context;
    private List<ViewPagerItem> mItems;
    private ActionListItemTwo callback;

    public PromoVPAdapter(Context context, List<ViewPagerItem> list, ActionListItemTwo callback) {
        super(list);

        this.context = context;
        this.mItems = list;
        this.callback = callback;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mItems.size() == 0) return null;

        View mCurrentView = LayoutInflater.from(context).inflate(R.layout.list_item_view_promo, container, false);

        // init view
        ImageView itemImage = mCurrentView.findViewById(R.id.item_image);

        if(mItems.get(position).getUrlImage() != null){
            new DownloadImageManager(context)
                    .start(mItems.get(position).getUrlImage(), itemImage);
        }else
            if(mItems.get(position).getImage() != null)
                itemImage.setImageDrawable(mItems.get(position).getImage());

        mCurrentView.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
        container.addView(mCurrentView);

        mCurrentView.setOnClickListener(view -> {
            Bundle sendData = new Bundle();
            sendData.putString("id", mItems.get(position).getId());
            callback.actionItem(position, sendData);
        });

        return mCurrentView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
