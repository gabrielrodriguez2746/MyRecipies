package com.myrecipes.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.SparseIntArray;

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
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
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
import java.util.Objects;

public class StepsExoPlayerHandler {

    private SimpleExoPlayer player;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    private HashMap<String, Integer> mediaIndexMap = new HashMap<>();
    private SparseIntArray mediaIdIndexMap = new SparseIntArray();

    private PlayerView playerView;

    public StepsExoPlayerHandler(PlayerView playerView) {
        this.playerView = playerView;
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
        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(playerView.getContext()),
                trackSelector, loadControl);
        playerView.setPlayer(player);
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
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource(
                sources.toArray(sourcesArray)
        );

        player.prepare(concatenatingMediaSource);
        player.setPlayWhenReady(true);
    }

    public void onStepChanged(Step step) {
        if (mediaIdIndexMap.get(player.getCurrentWindowIndex()) != step.getId()) {
            String video = step.getVideo();
            if (mediaIndexMap.containsKey(video)) {
                Integer videoIndex = mediaIndexMap.get(video);
                if (videoIndex != null) {
                    player.seekTo(videoIndex, 0);
                }
            }
        }
    }

    public int getMediaId() {
        return mediaIdIndexMap.get(player.getCurrentWindowIndex());
    }

    public void addListener(Player.EventListener listener) {
        player.addListener(listener);
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }

    public void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }
}
