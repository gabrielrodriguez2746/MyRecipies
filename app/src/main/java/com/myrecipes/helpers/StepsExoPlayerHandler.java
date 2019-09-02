package com.myrecipes.helpers;

import android.content.Context;
import android.net.Uri;
import android.util.SparseIntArray;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.myrecipes.R;
import com.myrecipes.data.models.Step;
import com.myrecipes.utils.VideoPlayerConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class StepsExoPlayerHandler implements LifecycleObserver {

    private int savedWindowsPosition = -1;
    private long savedSeekerPosition = -1L;
    private boolean playWhenReady;

    @Nullable
    private Player.EventListener listener;
    @Nullable
    private SimpleExoPlayer player;
    @Nullable
    private ConcatenatingMediaSource concatenatingMediaSource;

    private LoadControl loadControl = new DefaultLoadControl(
            new DefaultAllocator(true, 16),
            VideoPlayerConfig.MIN_BUFFER_DURATION,
            VideoPlayerConfig.MAX_BUFFER_DURATION,
            VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
            VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);
    private TrackSelector trackSelector =
            new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter()));

    private HashMap<String, Integer> mediaIndexMap = new HashMap<>();
    private SparseIntArray mediaIdIndexMap = new SparseIntArray();

    private PlayerView playerView;

    public StepsExoPlayerHandler(boolean playWhenReady, PlayerView playerView) {
        this.playWhenReady = playWhenReady;
        this.playerView = playerView;

    }

    public void buildMediaSource(List<Step> videoSteps) {
        Context context = playerView.getContext();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getString(R.string.app_name)), bandwidthMeter);

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
        concatenatingMediaSource = new ConcatenatingMediaSource(
                sources.toArray(sourcesArray)
        );
        preparePlayer();
    }

    public void onStepChanged(Step step) {
        int mediaId = player != null ? mediaIdIndexMap.get(player.getCurrentWindowIndex()) : -1;
        if (mediaId != step.getId()) {
            String video = step.getVideo();
            if (mediaIndexMap.containsKey(video)) {
                Integer videoIndex = mediaIndexMap.get(video);
                if (videoIndex != null) {
                    Timber.d("Moving to extra clicked position %s", videoIndex);
                    player.seekTo(videoIndex, 0);
                }
            }
        }
    }

    public int getMediaId() {
        return player != null ? mediaIdIndexMap.get(player.getCurrentWindowIndex()) : -1;
    }

    public void addListener(Player.EventListener listener) {
        this.listener = listener;
    }

    @Nullable
    public SimpleExoPlayer getPlayer() {
        return player;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Timber.d("initializing player");
        initializePlayer();
        preparePlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Timber.d("resume player");
        resumePlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Timber.d("pause player");
        pausePlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Timber.d("stop player");
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(playWhenReady);
        }
    }

    private void preparePlayer() {
        if (player != null && concatenatingMediaSource != null) {
            player.prepare(concatenatingMediaSource);
        }
    }

    private void pausePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            savedWindowsPosition = player.getCurrentWindowIndex();
            savedSeekerPosition = Math.max(0, player.getCurrentPosition());
        }
    }

    public int getSavedWindowsPosition() {
        return savedWindowsPosition;
    }

    public long getSavedSeekerPosition() {
        return savedSeekerPosition;
    }

    public boolean isPlayWhenReady() {
        return playWhenReady;
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(playerView.getContext()),
                trackSelector, loadControl);
        playerView.setPlayer(player);
        player.addListener(listener);
    }

    public void setPosition(int savedWindowsPosition, long savedSeekerPosition) {
        this.savedWindowsPosition = savedWindowsPosition;
        this.savedSeekerPosition = savedSeekerPosition;
        if (player != null) {
            player.seekTo(savedWindowsPosition, savedSeekerPosition);
        }
    }
}
