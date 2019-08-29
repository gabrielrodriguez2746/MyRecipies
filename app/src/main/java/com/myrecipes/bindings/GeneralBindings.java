package com.myrecipes.bindings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.myrecipes.R;
import com.myrecipes.data.models.Step;
import com.myrecipes.databinding.ItemStepBinding;
import com.myrecipes.models.RecyclerViewConfiguration;
import com.myrecipes.widget.StepWidget;
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
        } else {
            Picasso.get().load(imageUrl)
                    .into(imageView);
        }
    }

    // Gabriel Adjust later this, is taking to much responsibility
    @BindingAdapter("steps")
    public static void setSteps(ViewGroup parent, StepWidget widget) {
        if (widget != null && !widget.getSteps().isEmpty()) {
            parent.setVisibility(View.VISIBLE);
            parent.removeAllViews();
            Context context = parent.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            for (Step step : widget.getSteps()) {
                ItemStepBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_step, parent, false);
                String name = context.getString(R.string.app_steps_format, step.getIndex() + 1,
                        step.getShortDescription());
                boolean hasVideo = step.getVideo() != null;
                binding.setName(hasVideo ? name : name + " " + context.getString(R.string.app_not_video));
                View rootView = binding.getRoot();
                rootView.setOnClickListener(v -> widget.getListener().onItemClicked(step.getId(), hasVideo));
                parent.addView(rootView);
            }
        } else {
            parent.setVisibility(View.GONE);
        }
    }
}
