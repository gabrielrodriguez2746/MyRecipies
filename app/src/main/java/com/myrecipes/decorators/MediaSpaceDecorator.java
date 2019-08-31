package com.myrecipes.decorators;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class MediaSpaceDecorator extends RecyclerView.ItemDecoration {

    private int spacing;

    public MediaSpaceDecorator(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int totalSpanCount = getTotalSpanCount(parent);
        int spanSize = getItemSpanSize(parent, position);
        if (totalSpanCount != spanSize) {
            if (isInTheFirstRow(position, totalSpanCount)) outRect.top = 0;
            else outRect.top = spacing;
            if (isFirstInRow(position, totalSpanCount)) outRect.left = 0;
            else outRect.left = spacing /2;
            if (isLastInRow(position, totalSpanCount)) outRect.right = 0;
            else outRect.right = spacing /2;
            outRect.bottom = 0;
        }


    }

    private boolean isLastInRow(int position, int spanCount) {
        return isFirstInRow(position + 1, spanCount);
    }

    private boolean isFirstInRow(int position, int spanCount) {
        return position % spanCount == 0;
    }

    private boolean isInTheFirstRow(int position, int spanCount) {
        return position < spanCount;
    }

    private int getTotalSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return ((GridLayoutManager) Objects.requireNonNull(layoutManager)).getSpanCount();
    }

    private int getItemSpanSize(RecyclerView parent, int position) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        return ((GridLayoutManager) Objects.requireNonNull(layoutManager))
                .getSpanSizeLookup().getSpanSize(position);
    }
}