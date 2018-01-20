package com.selecttvlauncher.channels;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.selecttvlauncher.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExoPlayerFragment extends Fragment {
    private static final String PARAM_VIDEO_URI = "video_uri";

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private ExoPlayer.EventListener exoPlayerEventListener;

    public ExoPlayerFragment() {
    }

    public static ExoPlayerFragment newInstance(String videoURI) {
        ExoPlayerFragment playerFragment = new ExoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_VIDEO_URI, videoURI);
        playerFragment.setArguments(bundle);
        return playerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exo_player, container, false);

        String videoURI = "";
        if (getArguments() != null) {
            videoURI = getArguments().getString(PARAM_VIDEO_URI);
        }

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        simpleExoPlayerView = new SimpleExoPlayerView(getContext());
        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);
        simpleExoPlayerView.setUseController(false);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);
        Uri mp4VideoUri = Uri.parse(videoURI);


        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeterA);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();


        MediaSource videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
        final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);

        player.prepare(loopingSource);

        player.addListener(new ExoPlayer.EventListener() {
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
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d("Error:::::",":::"+error.getCause()+"::::"+error.type);
                player.stop();
                player.prepare(loopingSource);
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPositionDiscontinuity() {
            }
        });

        player.setPlayWhenReady(true); //run file/link when ready to play.

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null)
            player.release();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null)
            player.release();
    }
}
