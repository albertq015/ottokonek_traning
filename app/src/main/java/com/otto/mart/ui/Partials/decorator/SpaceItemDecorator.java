package com.otto.mart.ui.Partials.decorator;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecorator extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecorator(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            outRect.bottom = space;
            outRect.top = 0;
        }
    }
}