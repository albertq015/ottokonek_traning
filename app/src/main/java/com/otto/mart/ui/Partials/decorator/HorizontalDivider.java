package com.otto.mart.ui.Partials.decorator;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class HorizontalDivider extends RecyclerView.ItemDecoration {

    private final int HorizontalSpaceHeight;

    public HorizontalDivider(int verticalSpaceHeight) {
        this.HorizontalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if(itemPosition == 0){
            outRect.left = HorizontalSpaceHeight;
        }
        outRect.right = HorizontalSpaceHeight;
    }
}
