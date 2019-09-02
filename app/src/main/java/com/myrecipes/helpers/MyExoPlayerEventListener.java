package com.myrecipes.helpers;

import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

public class MyExoPlayerEventListener implements Player.EventListener, LifecycleObserver {

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    private VoidSupplier onTrackChange;
    private GenericSupplier<SimpleExoPlayer> playerSupplier;

    public MyExoPlayerEventListener(
            Context context, String name, VoidSupplier onTrackChange,
            GenericSupplier<SimpleExoPlayer> playerSupplier
    ) {
        this.onTrackChange = onTrackChange;
        this.playerSupplier = playerSupplier;
        mediaSession = new MediaSessionCompat(context, name);

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
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        mediaSession.setActive(false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        mediaSession.setActive(true);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        onTrackChange.invoke();
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        SimpleExoPlayer player = playerSupplier.invoke();
        if (player != null) {
            if ((playbackState == Player.STATE_READY) && playWhenReady) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        player.getCurrentPosition(), 1f);
            } else if ((playbackState == Player.STATE_READY)) {
                stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        player.getCurrentPosition(), 1f);
            }
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            SimpleExoPlayer player = playerSupplier.invoke();
            if (player != null) {
                player.setPlayWhenReady(true);
            }
        }

        @Override
        public void onPause() {
            SimpleExoPlayer player = playerSupplier.invoke();
            if (player != null) {
                player.setPlayWhenReady(false);
            }
        }

        @Override
        public void onSkipToPrevious() {
            SimpleExoPlayer player = playerSupplier.invoke();
            if (player != null) {
                player.seekTo(0);
            }
        }
    }
}
