package com.otto.mart.ui.Partials.decorator;

import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceDecorator extends RecyclerView.ItemDecoration {
    private int space;
    private int orientation;
    private int start;
    private int end;
    private int side;

    public SpaceDecorator(int space, int orientation) {
        this.space = space;
        this.orientation = orientation;
    }

    public SpaceDecorator(int space, int orientation, int side, int start, int end) {
        this.space = space;
        this.orientation = orientation;
        this.side = side;
        this.start = start;
        this.end = end;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0 && parent.getAdapter().getItemCount() == 1) {
            onlyOneItem(outRect);
        } else if (parent.getChildAdapterPosition(view) == 0) {
            firstItem(outRect);
        } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            lastItem(outRect);
        } else {
            midItem(outRect);
        }
    }

    private void firstItem(Rect outRect) {
        setOutRect(outRect, start, 0);
    }

    private void onlyOneItem(Rect outRect) {
        setOutRect(outRect, start, start);
    }

    private void midItem(Rect outRect) {
        setOutRect(outRect, space, 0);
    }

    private void lastItem(Rect outRect) {
        setOutRect(outRect, space, end);
    }

    private void setOutRect(Rect outRect, int first, int second) {
        if (orientation == LinearLayout.HORIZONTAL) {
            outRect.left = first;
            outRect.right = second;
            outRect.top = side;
            outRect.bottom = side;
        } else {
            outRect.top = first;
            outRect.bottom = second;
            outRect.left = side;
            outRect.right = side;
        }
    }
}
