package com.otto.mart.ui.Partials.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.otto.mart.model.localmodel.ui.DefaultViewPagerItem;

import java.util.List;

public class DefaultViewPagerAdapter extends FragmentPagerAdapter {

    private List<DefaultViewPagerItem> mItems;

    public DefaultViewPagerAdapter(FragmentManager manager, List<DefaultViewPagerItem> mItems) {
        super(manager);

        this.mItems = mItems;
    }

    @Override
    public Fragment getItem(int i) {
        return mItems.get(i).getFragment();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mItems.get(position).getTitle();
    }
}
