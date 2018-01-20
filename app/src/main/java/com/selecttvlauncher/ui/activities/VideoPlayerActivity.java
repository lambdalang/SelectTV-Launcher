package com.selecttvlauncher.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Constants;

/**
 * Created by Ocs pl-79(17.2.2016) on 4/18/2016.
 */
public class VideoPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private Context context;
    YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;
    YouTubePlayer.PlaybackEventListener playbackEventListener;

    // YouTube player view
    private YouTubePlayerView youTubeView;

    private String video_url="";
    private ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_youtube);
        context=this;

        try {
            video_url=getIntent().getStringExtra("url");
            Log.d("Url:::","}}}"+video_url);

            youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
            imageBack=(ImageView)findViewById(R.id.imageBack);

            // Initializing video player with developer key
            youTubeView.initialize(Constants.DEVELOPER_KEY, this);
            playerStateChangeListener = new MyPlayerStateChangeListener();
            playbackEventListener = new MyPlaybackEventListener();

            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        try {
            if (!wasRestored) {

                player.loadVideo(video_url);

                // Hiding player controls
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                player.setPlayerStateChangeListener(playerStateChangeListener);
                player.setPlaybackEventListener(playbackEventListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        try {
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
            } else {
                String errorMessage = String.format("There was an error initializing the YouTubePlayer (%1$s)", errorReason.toString());
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == RECOVERY_DIALOG_REQUEST) {
                // Retry initialization if user performed a recovery action
                getYouTubePlayerProvider().initialize(Constants.DEVELOPER_KEY, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
        }
    }
}
