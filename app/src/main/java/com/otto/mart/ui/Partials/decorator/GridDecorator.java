package com.otto.mart.ui.Partials.decorator;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class GridDecorator extends RecyclerView.ItemDecoration {

    private int space;
    private int size;

    private boolean mNeedLeftSpacing = false;
    private int padding;
    private boolean isNeedLeftSpace = false;

    public GridDecorator(int space, int size, int padding) {
        this.padding = padding;
        this.space = space;
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int frameWidth = (int) ((parent.getWidth() - (float) space * (size - 1)) / size);
        int margin = parent.getWidth() / size - frameWidth;
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();

        if (position < size) {
            outRect.top = space;
        } else {
            outRect.top = space / 2;
        }

        if (position % size == 0) {
            outRect.left = 0;
            if (padding != 0) outRect.left = padding;
            outRect.right = margin;
            isNeedLeftSpace = true;
        } else if ((position + 1) % size == 0) {
            isNeedLeftSpace = false;
            outRect.right = 0;
            if (padding != 0) outRect.right = padding;
            outRect.left = margin;
        } else if (isNeedLeftSpace) {
            isNeedLeftSpace = false;
            outRect.left = size - margin;
            if ((position + 2) % size == 0) {
                outRect.right = space - margin;
            } else {
                outRect.right = space / 2;
            }
        } else if ((position + 2) % size == 0) {
            isNeedLeftSpace = false;
            outRect.left = space / 2;
            outRect.right = space - margin;
        } else {
            isNeedLeftSpace = false;
            outRect.left = space / 2;
            outRect.right = space / 2;
        }
        outRect.bottom = space;
    }
}