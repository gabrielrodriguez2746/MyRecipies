package com.myrecipes.bindings;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.myrecipes.R;
import com.myrecipes.models.RecyclerViewConfiguration;
import com.squareup.picasso.Picasso;

public class GeneralBindings {

    @BindingAdapter("configuration")
    public static void setRecyclerViewConfiguration(RecyclerView recyclerView, RecyclerViewConfiguration configuration) {
        recyclerView.setLayoutManager(configuration.getLayoutManager());
        recyclerView.setAdapter(configuration.getAdapter());
        recyclerView.setNestedScrollingEnabled(configuration.isNestedScroll());
        recyclerView.setHasFixedSize(configuration.isHasFixedSize());
        RecyclerView.ItemDecoration decorator = configuration.getDecorator();
        if (decorator != null) {
            recyclerView.addItemDecoration(decorator);
        }
    }

    @BindingAdapter(value = {"image_url", "default_drawable"}, requireAll = false)
    public static void setImageUrl(ImageView imageView, @Nullable String imageUrl, @Nullable Drawable defaultDrawable) {
        if (imageUrl == null) {
            // TODO Improve this to avoid this default image resolution
            imageView.setImageDrawable(defaultDrawable);
            imageView.setBackgroundColor(ContextCompat.getColor(imageView.getContext(), R.color.gray));
        } else {
            Picasso.get().load(imageUrl)
                    .into(imageView);
        }
    }
}
