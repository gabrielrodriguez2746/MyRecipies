package com.myrecipes.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.myrecipes.R;
import com.myrecipes.data.models.Step;
import com.myrecipes.databinding.FragmentVideoPlayerBinding;
import com.myrecipes.utils.VideoPlayerConfig;
import com.myrecipes.viewmodels.media.RecipeStepViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class StepVideoPlayerFragment extends Fragment {

    public static String RECIPE_ID_KEY = "recipe_id";
    public static String STEP_ID_KEY = "step_id";

    private FragmentVideoPlayerBinding binding;
    private RecipeStepViewModel viewModel;

    private SimpleExoPlayer player;
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
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_player, container, false);
            if (player == null) {
                setupPlayer();
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
//        processSelectedStep();

        if (arguments.containsKey(RECIPE_ID_KEY)) {
            recipeId = arguments.getInt(RECIPE_ID_KEY);
            viewModel.getStepsByRecipeId(recipeId);
            if (arguments.containsKey(STEP_ID_KEY)) {
                viewModel.getRecipeStepInformation(recipeId, arguments.getInt(STEP_ID_KEY));
            }
        }
    }

    private void processSteps() {
        viewModel.getStepsLiveData().observe(getViewLifecycleOwner(), steps -> {
            if (steps != null) {
                buildMediaSource(steps);
                // Gabriel This should be made by activity
//                binding.setSteps(new StepWidget(steps, this::onRecipeStepClicked));
            }
        });
    }

    private void setupPlayer() {
        LoadControl loadControl = new DefaultLoadControl(
                new DefaultAllocator(true, 16),
                VideoPlayerConfig.MIN_BUFFER_DURATION,
                VideoPlayerConfig.MAX_BUFFER_DURATION,
                VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(
                binding.getRoot().getContext()), trackSelector, loadControl);
        binding.pvRecipe.setPlayer(player);
    }

    private void buildMediaSource(List<Step> videoSteps) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getContext()),
                Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);

        ArrayList<MediaSource> sources = new ArrayList<>();

        for (Step videoStep : videoSteps) {
            if (videoStep.getVideo() != null) {
                sources.add(new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(videoStep.getVideo())));
            }
        }

        MediaSource[] sourcesArray = new MediaSource[sources.size()];
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource(
                sources.toArray(sourcesArray)
        );

        player.prepare(concatenatingMediaSource);
        player.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }

    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pausePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resumePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

}
