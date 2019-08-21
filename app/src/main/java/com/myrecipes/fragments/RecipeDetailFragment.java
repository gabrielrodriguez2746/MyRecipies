package com.myrecipes.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.myrecipes.R;
import com.myrecipes.data.models.Ingredient;
import com.myrecipes.databinding.FragmentRecipeDetailBinding;
import com.myrecipes.listeners.OnDetailFragmentInteraction;
import com.myrecipes.utils.ListUtils;
import com.myrecipes.viewmodels.detail.RecipeDetailViewModel;
import com.myrecipes.widget.StepWidget;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class RecipeDetailFragment extends Fragment {

    public static String RECIPE_ID_KEY = "recipe_id";

    private FragmentRecipeDetailBinding binding;
    private RecipeDetailViewModel viewModel;

    private Drawable defaultDrawable;
    private String bulletString;
    private int recipeId;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);
        }
        if (defaultDrawable == null) {
            defaultDrawable = ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.ic_recipe_default);
            binding.setDefaultDrawable(defaultDrawable);
        }
        if (bulletString == null) {
            bulletString = getString(R.string.app_copy_bullet);
        }
        return binding != null ? binding.getRoot() : super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = Objects.requireNonNull(getArguments());
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this, factory).get(RecipeDetailViewModel.class);
        }
        processRecipe();
        processRecipeIngredients();
        processRecipeSteps();

        if (arguments.containsKey(RECIPE_ID_KEY)) {
            recipeId = arguments.getInt(RECIPE_ID_KEY);
            viewModel.getRecipeInformationById(recipeId);
        }
    }

    private void processRecipeSteps() {
        viewModel.getStepsLiveData().observe(getViewLifecycleOwner(), steps -> {
            if (steps != null) {
                // Gabriel This should be made by activity
                binding.setSteps(new StepWidget(steps, this::onRecipeStepClicked));
            }
        });
    }

    private void onRecipeStepClicked(int stepId) {
        ((OnDetailFragmentInteraction) Objects.requireNonNull(getActivity()))
                .onStepClicked(String.valueOf(recipeId), String.valueOf(stepId));
    }

    private void processRecipeIngredients() {
        viewModel.getIngredientsLiveData().observe(getViewLifecycleOwner(), ingredients -> {
            if (ingredients != null) {
                ArrayList<String> mappedIngredients = new ArrayList<>();
                for (Ingredient ingredient : ingredients) {
                    mappedIngredients.add(getString(R.string.app_ingredients_format, ingredient.getName(),
                            ingredient.getQuantity(), ingredient.getUnit()));
                }
                binding.setIngredients(ListUtils.mapToListedString(mappedIngredients, bulletString));
            }
        });
    }

    private void processRecipe() {
        viewModel.getRecipeLiveData().observe(getViewLifecycleOwner(), recipe -> {
            if (recipe != null) {
                adjustToolbarTitle(recipe.getName());
                binding.setImageUrl(recipe.getImage());
            }
        });
    }

    private void adjustToolbarTitle(String recipeName) {
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                .getSupportActionBar()).setTitle(recipeName);
    }
}
