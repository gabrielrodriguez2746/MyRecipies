package com.myrecipes.adapters;

import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.myrecipes.R;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.databinding.ItemRecipeBinding;

import java.util.Objects;

public class RecipesListAdapter extends ListAdapter<Recipe, RecipesListAdapter.MYRecipesViewHolder> {

    // Gabriel This should be provided as a factory
    private ViewGroup.LayoutParams defaultLayoutParameters;
    private OnRecipeClicked listener;

    private static MyRecipeDiffResolver diffResolver = new MyRecipeDiffResolver();


    public RecipesListAdapter(DisplayMetrics displayMetrics, OnRecipeClicked listener) {
        super(diffResolver);
        this.listener = listener;
        int height = (int) (Math.max(displayMetrics.heightPixels, displayMetrics.widthPixels) * 0.25);
        defaultLayoutParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
    }

    @NonNull
    @Override
    public MYRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRecipeBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_recipe, parent, false);
        View rootView = binding.getRoot();
        rootView.setLayoutParams(defaultLayoutParameters);
        return new MYRecipesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MYRecipesViewHolder holder, int position) {
        holder.bind(Objects.requireNonNull(getItem(position)));
    }

    class MYRecipesViewHolder extends RecyclerView.ViewHolder {

        private ItemRecipeBinding binding;
        private Drawable defaultDrawable;

        MYRecipesViewHolder(@NonNull ItemRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            defaultDrawable = ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.ic_recipe_default);
        }

        void bind(Recipe data) {
            binding.setDefaultDrawable(defaultDrawable);
            binding.setImageUrl(data.getImage());
            binding.setTitle(data.getName());
            binding.getRoot().setOnClickListener(v -> listener.onRecipeClicked(data.getId()));
        }
    }

    static class MyRecipeDiffResolver extends DiffUtil.ItemCallback<Recipe> {

        MyRecipeDiffResolver() {
        }

        @Override
        public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.hashCode() == newItem.hashCode();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.equals(newItem); // Gabriel Fix this with real logic
        }
    }

    public interface OnRecipeClicked {

        void onRecipeClicked(int movieId);
    }
}
