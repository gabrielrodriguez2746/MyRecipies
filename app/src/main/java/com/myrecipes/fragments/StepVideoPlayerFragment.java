package com.myrecipes.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.SparseIntArray;
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
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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
import com.myrecipes.helpers.MyExoPlayerEventListener;
import com.myrecipes.utils.VideoPlayerConfig;
import com.myrecipes.viewmodels.media.RecipeStepViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class StepVideoPlayerFragment extends Fragment {

    public static String RECIPE_ID_KEY = "recipe_id";
    public static String STEP_ID_KEY = "step_id";

    private FragmentVideoPlayerBinding binding;
    private RecipeStepViewModel viewModel;

    private SimpleExoPlayer player;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private int recipeId;
    private HashMap<String, Integer> mediaIndexMap = new HashMap<>();
    private SparseIntArray mediaIdIndexMap = new SparseIntArray();

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
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_player, container, false);
            if (player == null) {
                Timber.tag("Gabriel").d("player was null");
                setupPlayer();
                initializeMediaSession();
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
            if (mediaIdIndexMap.get(player.getCurrentWindowIndex()) != step.getId()) {
                String video = step.getVideo();
                if (mediaIndexMap.containsKey(video)) {
                    Integer videoIndex = mediaIndexMap.get(video);
                    if (videoIndex != null) {
                        player.seekTo(videoIndex, 0);
                    }
                }
            }
        });
    }

    private void processSteps() {
        viewModel.getStepsLiveData().observe(getViewLifecycleOwner(), steps -> {
            if (steps != null) {
                buildMediaSource(steps);
                processStepFromExtras();
            }
        });
    }

    private void processStepFromExtras() {
        Bundle arguments = Objects.requireNonNull(getArguments());
        if (arguments.containsKey(STEP_ID_KEY)) {
            viewModel.getRecipeStepInformation(recipeId, arguments.getInt(STEP_ID_KEY));
        }
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
        player.addListener(new MyExoPlayerEventListener() {

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                viewModel.getRecipeStepInformation(recipeId, mediaIdIndexMap.get(player.getCurrentWindowIndex()));
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if ((playbackState == Player.STATE_READY) && playWhenReady) {
                    stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                            player.getCurrentPosition(), 1f);
                } else if ((playbackState == Player.STATE_READY)) {
                    stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                            player.getCurrentPosition(), 1f);
                }
            }
        });
        binding.pvRecipe.setPlayer(player);
    }

    private void buildMediaSource(List<Step> videoSteps) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getContext()),
                Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);

        ArrayList<MediaSource> sources = new ArrayList<>();

        int notNullIndex = 0;
        for (Step videoStep : videoSteps) {
            String video = videoStep.getVideo();
            if (video != null) {
                mediaIndexMap.put(video, notNullIndex);
                mediaIdIndexMap.put(notNullIndex, videoStep.getId());
                sources.add(new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(video)));
                notNullIndex++;
            }
        }

        MediaSource[] sourcesArray = new MediaSource[sources.size()];
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource(
                sources.toArray(sourcesArray)
        );

        player.prepare(concatenatingMediaSource);
        player.setPlayWhenReady(true);
    }


    private void initializeMediaSession() {

        mediaSession = new MediaSessionCompat(Objects.requireNonNull(getContext()),
                StepVideoPlayerFragment.class.getSimpleName());

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mediaSession.setActive(true);

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
        mediaSession.setActive(false);
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            player.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            player.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            player.seekTo(0);
        }
    }

}
