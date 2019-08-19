package com.myrecipes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.myrecipes.R;
import com.myrecipes.databinding.FragmentRecipeDetailBinding;
import com.myrecipes.viewmodels.detail.RecipeDetailViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class RecipeDetailFragment extends Fragment {

    public static String RECIPE_ID_KEY = "recipe_id";

    private FragmentRecipeDetailBinding binding;
    private RecipeDetailViewModel viewModel;

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
            viewModel.getRecipeInformationById(arguments.getInt(RECIPE_ID_KEY));
        }
    }

    private void processRecipeSteps() {
        viewModel.getStepsLiveData().observe(getViewLifecycleOwner(), steps -> {

        });
    }

    private void processRecipeIngredients() {
        viewModel.getIngredientsLiveData().observe(getViewLifecycleOwner(), ingredients -> {

        });
    }

    private void processRecipe() {
        viewModel.getRecipeLiveData().observe(getViewLifecycleOwner(), recipe -> {
            if (recipe != null) {
                adjustToolbarTitle(recipe.getName());
            }
        });
    }

    private void adjustToolbarTitle(String recipeName) {
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                .getSupportActionBar()).setTitle(recipeName);
    }
}
