package com.myrecipes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.myrecipes.R;
import com.myrecipes.data.models.Ingredient;
import com.myrecipes.databinding.FragmentMasterRecipeDetailBinding;
import com.myrecipes.helpers.MyExoPlayerEventListener;
import com.myrecipes.helpers.StepsExoPlayerHandler;
import com.myrecipes.utils.ListUtils;
import com.myrecipes.viewmodels.detail.RecipeMasterDetailViewModel;
import com.myrecipes.widget.StepWidget;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class RecipeMasterDetailFragment extends ExoPlayerRestoreStateFragment {

    public static String RECIPE_ID_KEY = "recipe_id";

    private FragmentMasterRecipeDetailBinding binding;
    private RecipeMasterDetailViewModel viewModel;

    private Toast toast = null;
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
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_master_recipe_detail, container, false);
            bulletString = getString(R.string.app_copy_bullet);
            if (playerHandler == null) {
                Timber.tag("Gabriel").d("player was null");
                setPlayerWindowPosition(savedInstanceState);
                setPlayerSeekerPosition(savedInstanceState);
                setupPlayer(getPlayerIsPlayWhenReady(savedInstanceState));
            }
        }
        return binding != null ? binding.getRoot() : super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = Objects.requireNonNull(getArguments());
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this, factory).get(RecipeMasterDetailViewModel.class);
        }
        processRecipe();
        processRecipeIngredients();
        processRecipeSteps();
        processSelectedStep();

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
                playerHandler.buildMediaSource(steps);
                processStepFromExtras();
            }
        });
    }

    private void processStepFromExtras() {
        if (getSavedWindowsPosition() != -1) {
            playerHandler.setPosition(getSavedWindowsPosition(), getSavedSeekerPosition());
        }
    }

    private void processSelectedStep() {
        viewModel.getStepLiveData().observe(getViewLifecycleOwner(), step -> {
            Timber.d("Processing step %s", step.getShortDescription());
            binding.setDescription(step.getDescription());
            playerHandler.onStepChanged(step);
        });
    }

    private void onRecipeStepClicked(int stepId, boolean hasVideo) {
        if (hasVideo) {
            viewModel.getRecipeStepInformation(recipeId, stepId);
        } else {
            if (toast != null) toast.cancel();
            toast = Toast.makeText(getContext(), getString(R.string.app_not_video_information), Toast.LENGTH_SHORT);
            toast.show();
        }

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
            }
        });
    }

    private void adjustToolbarTitle(String recipeName) {
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                .getSupportActionBar()).setTitle(recipeName);
    }

    private void setupPlayer(boolean playerIsPlayWhenReady) {
        Context context = binding.getRoot().getContext();
        Lifecycle lifecycle = getViewLifecycleOwner().getLifecycle();
        playerHandler = new StepsExoPlayerHandler(playerIsPlayWhenReady, binding.pvRecipe);
        MyExoPlayerEventListener playerListener = new MyExoPlayerEventListener(
                context, StepVideoPlayerFragment.class.getSimpleName(),
                () -> viewModel.getRecipeStepInformation(recipeId, playerHandler.getMediaId()),
                () -> playerHandler.getPlayer());
        lifecycle.addObserver(playerHandler);
        lifecycle.addObserver(playerListener);
        playerHandler.addListener(playerListener);
    }

}
