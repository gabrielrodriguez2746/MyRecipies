package com.myrecipes.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewConfiguration {

    private RecyclerView.LayoutManager layoutManager;
    private boolean isNestedScroll;
    private boolean hasFixedSize;
    private RecyclerView.Adapter<?> adapter;
    @Nullable
    private RecyclerView.ItemDecoration decorator;

    public RecyclerViewConfiguration(RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter<?> adapter) {
        this.layoutManager = layoutManager;
        this.adapter = adapter;
        isNestedScroll = false;
        hasFixedSize = true;
        decorator = null;
    }

    public RecyclerViewConfiguration(RecyclerView.LayoutManager layoutManager,
                                     RecyclerView.Adapter<?> adapter,
                                     @NonNull RecyclerView.ItemDecoration decorator) {
        this.layoutManager = layoutManager;
        this.adapter = adapter;
        this.decorator = decorator;
        isNestedScroll = false;
        hasFixedSize = true;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public boolean isNestedScroll() {
        return isNestedScroll;
    }

    public boolean isHasFixedSize() {
        return hasFixedSize;
    }

    public RecyclerView.Adapter<?> getAdapter() {
        return adapter;
    }

    @Nullable
    public RecyclerView.ItemDecoration getDecorator() {
        return decorator;
    }
}
