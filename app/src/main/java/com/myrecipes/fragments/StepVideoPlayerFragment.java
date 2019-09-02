package com.myrecipes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.myrecipes.R;
import com.myrecipes.databinding.FragmentVideoPlayerBinding;
import com.myrecipes.helpers.MyExoPlayerEventListener;
import com.myrecipes.helpers.StepsExoPlayerHandler;
import com.myrecipes.viewmodels.media.RecipeStepViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class StepVideoPlayerFragment extends ExoPlayerRestoreStateFragment {

    public static String RECIPE_ID_KEY = "recipe_id";
    public static String STEP_ID_KEY = "step_id";

    private FragmentVideoPlayerBinding binding;
    private RecipeStepViewModel viewModel;

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
            Timber.tag("Gabriel").d("binding was null");
            if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.FullScreen);
                LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
                binding = DataBindingUtil.inflate(localInflater, R.layout.fragment_video_player, container, false);
            } else {
                binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_player, container, false);
            }
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
            viewModel = ViewModelProviders.of(this, factory).get(RecipeStepViewModel.class);
        }
        processSteps();
        processSelectedStep();

        if (arguments.containsKey(RECIPE_ID_KEY)) {
            recipeId = arguments.getInt(RECIPE_ID_KEY);
            viewModel.getStepsByRecipeId(recipeId);
        }
    }

    private void processSelectedStep() {
        viewModel.getStepLiveData().observe(getViewLifecycleOwner(), step -> {
            Timber.d("Processing step %s", step.getShortDescription());
            binding.setDescription(step.getDescription());
            playerHandler.onStepChanged(step);
        });
    }

    private void processSteps() {
        viewModel.getStepsLiveData().observe(getViewLifecycleOwner(), steps -> {
            if (steps != null) {
                playerHandler.buildMediaSource(steps);
                processStepFromExtras();
            }
        });
    }

    private void processStepFromExtras() {
        Bundle arguments = Objects.requireNonNull(getArguments());
        if (getSavedWindowsPosition() != -1) {
            playerHandler.setPosition(getSavedWindowsPosition(), getSavedSeekerPosition());
        } else if (arguments.containsKey(STEP_ID_KEY)) {
            viewModel.getRecipeStepInformation(recipeId, arguments.getInt(STEP_ID_KEY));
        }
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
