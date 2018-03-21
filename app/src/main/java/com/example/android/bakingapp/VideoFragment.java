package com.example.android.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VideoFragment extends Fragment {

    private static final int MOVIE_RATIO_SCREEN_HEIGHT = 9;
    private static final int MOVIE_RATIO_SCREEN_WIDTH = 16;
    private static final String PLAYBACK_POSITION_KEY = "playback_position_key";
    private static final String IS_PLAYING_KEY = "is_playing_key";
    private static final String IS_TWO_PANE_KEY = "is_two_pane_key";
    private static final String URL_STRING_KEY = "url_string_key";

    private Unbinder mUnbinder;
    @BindView(R.id.video_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.video_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.video_error_tv)
    TextView mErrorTextView;

    private SimpleExoPlayer mPlayer;
    private String mVideoUrl;
    private long mPlaybackPosition;
    private boolean mPlayWhenReady;
    private boolean mIsTwoPane;

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public void setIsTwoPane(boolean isTwoPane) {
        mIsTwoPane = isTwoPane;
    }

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        playerViewHeightForWidth();
        //initializePlayer();
        if (savedInstanceState != null) {
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(IS_PLAYING_KEY);
            mIsTwoPane = savedInstanceState.getBoolean(IS_TWO_PANE_KEY);
            mVideoUrl = savedInstanceState.getString(URL_STRING_KEY);
        }
        makeFullScreenWhenOnLandscapeOnPhone();
        return rootView;
    }

    void makeFullScreenWhenOnLandscapeOnPhone() {
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) && (!mIsTwoPane)) {
            mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    void playerViewHeightForWidth() {
        ViewTreeObserver vto = mPlayerView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPlayerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float width = mPlayerView.getMeasuredWidth();
                float ratioSection = width / MOVIE_RATIO_SCREEN_WIDTH;
                float height = ratioSection * MOVIE_RATIO_SCREEN_HEIGHT;
                mPlayerView.getLayoutParams().height = (int) height;
            }
        });
    }

    private void initializePlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(mPlayWhenReady);
        mPlayer.seekTo(mPlaybackPosition);
        Uri uri = Uri.parse(mVideoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.prepare(mediaSource, true, false);
        mPlayer.addListener(new ExoPlayer.DefaultEventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
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
                mErrorTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                super.onPlaybackParametersChanged(playbackParameters);
            }

            @Override
            public void onSeekProcessed() {
                super.onSeekProcessed();
            }
        });
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("BakingApp"))
                .createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlaybackPosition = mPlayer.getCurrentPosition();
        mPlayWhenReady = mPlayer.getPlayWhenReady();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPlaybackPosition = mPlayer.getCurrentPosition();
        mPlayWhenReady = mPlayer.getPlayWhenReady();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       releasePlayer();
        mUnbinder.unbind();
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putLong(PLAYBACK_POSITION_KEY, mPlaybackPosition);
        currentState.putBoolean(IS_PLAYING_KEY, mPlayWhenReady);
        currentState.putBoolean(IS_TWO_PANE_KEY, mIsTwoPane);
        currentState.putString(URL_STRING_KEY, mVideoUrl);
    }
}
