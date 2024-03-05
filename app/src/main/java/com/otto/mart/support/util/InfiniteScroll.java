package com.otto.mart.support.util;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class InfiniteScroll extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private boolean isLoad;
    private int visibleItemCount, totalItemCount, pastVisiblesItems;

    public InfiniteScroll(LinearLayoutManager layoutManager, SwipeRefreshLayout refreshLayout) {
        this.layoutManager = layoutManager;
        this.refreshLayout = refreshLayout;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) {
            visibleItemCount = layoutManager.getChildCount();
            totalItemCount = layoutManager.getItemCount();
            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

//            if (layoutManager.findFirstVisibleItemPosition() + recyclerView.getChildCount() == layoutManager.getItemCount()) {
//                if (!isLoad) {
//                    loadNewItem();
//                    isLoad = true;
//                }
//            }
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                if (!refreshLayout.isRefreshing()) {
                    loadNewItem();
                    isLoad = true;
                }
            }
        }
    }

    public abstract void loadNewItem();
}
