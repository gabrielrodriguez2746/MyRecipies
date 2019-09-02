package com.myrecipes.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myrecipes.helpers.StepsExoPlayerHandler;

import timber.log.Timber;

public abstract class ExoPlayerRestoreStateFragment extends Fragment {

    private static final String PLAYER_WINDOW = "PLAYER_WINDOW";
    private static final String PLAYER_SEEKER_POSITION = "PLAYER_SEEKER_POSITION";
    private static final String PLAYER_PLAY_WHEN_READY = "PLAYER_PLAY_WHEN_READY";

    private int savedWindowsPosition;
    private long savedSeekerPosition;

    protected StepsExoPlayerHandler playerHandler;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        int savedWindowsPosition = playerHandler.getSavedWindowsPosition();
        long savedSeekerPosition = playerHandler.getSavedSeekerPosition();
        boolean savedIsPlayWhenReady = playerHandler.isPlayWhenReady();
        Timber.d("saved windows position " + savedWindowsPosition + "saved seeker position " + savedSeekerPosition);
        outState.putInt(PLAYER_WINDOW, savedWindowsPosition);
        outState.putLong(PLAYER_SEEKER_POSITION, savedSeekerPosition);
        outState.putBoolean(PLAYER_PLAY_WHEN_READY, savedIsPlayWhenReady);
        super.onSaveInstanceState(outState);
    }

    protected void setPlayerSeekerPosition(@Nullable Bundle savedInstanceState) {
        savedSeekerPosition = savedInstanceState != null ?
                savedInstanceState.getLong(PLAYER_SEEKER_POSITION, 0) : 0;
    }

    protected void setPlayerWindowPosition(@Nullable Bundle savedInstanceState) {
        savedWindowsPosition = savedInstanceState != null ?
                savedInstanceState.getInt(PLAYER_WINDOW, -1) : -1;
    }

    public int getSavedWindowsPosition() {
        return savedWindowsPosition;
    }

    public long getSavedSeekerPosition() {
        return savedSeekerPosition;
    }

    protected boolean getPlayerIsPlayWhenReady(@Nullable Bundle savedInstanceState) {
        return savedInstanceState == null || savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY, true);
    }

}
