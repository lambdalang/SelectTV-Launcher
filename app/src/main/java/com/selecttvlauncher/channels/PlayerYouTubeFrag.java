package com.selecttvlauncher.channels;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.selecttvlauncher.tools.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by babin on 7/8/2017.
 */

public class PlayerYouTubeFrag extends YouTubePlayerSupportFragment {
    private String currentVideoID = "video_id";
    private static YouTubePlayer activePlayer;
    private static int currentPlayingIndex = 0;
    private static int offset = 0;
    private static ArrayList< ProgramList> newProgramList = new ArrayList<>();
    private static Runnable runable;
    private static Handler handler;
    private String currentId = "";
    private static YoutubeDataListener mYoutubeDataListener;
    private static String selectedSlug = "";
    private static boolean isPaused = false;
    private static ChannelTotalFragment.ChannelTotalListener mListener;

    public static PlayerYouTubeFrag newInstance(ChannelTotalFragment.ChannelTotalListener mListener, ArrayList< ProgramList> programlist, YoutubeDataListener mYoutubeDataListener, String mSlug) {
        PlayerYouTubeFrag.mYoutubeDataListener = mYoutubeDataListener;
        PlayerYouTubeFrag.mListener = mListener;

        newProgramList.clear();
        newProgramList.addAll(programlist);
        PlayerYouTubeFrag playerYouTubeFrag = new PlayerYouTubeFrag();
        int index = getCurrentplaylistIndex(programlist);
        PlayerYouTubeFrag.currentPlayingIndex = index;
        ArrayList<String> youtube_playlist = new ArrayList<>();
        youtube_playlist = getCurrentPlaylist(programlist);
        offset = getCurrentPlaylistOffset(programlist);

        Bundle bundle = new Bundle();
        bundle.putString("url", new Gson().toJson(youtube_playlist));
        bundle.putInt("offset", offset);
        bundle.putInt("pos", 0);
        bundle.putInt("type", 1);
        bundle.putString("slug", mSlug);

        playerYouTubeFrag.setArguments(bundle);
        /*playerYouTubeFrag.init();

        setBackgroundtask();*/

        return playerYouTubeFrag;
    }

    public static PlayerYouTubeFrag newInstance(ArrayList< ProgramList> programlist, int pos,  YoutubeDataListener mYoutubeDataListener, String mSlug) {
        PlayerYouTubeFrag.mYoutubeDataListener = mYoutubeDataListener;

        newProgramList.clear();
        newProgramList.addAll(programlist);
        PlayerYouTubeFrag playerYouTubeFrag = new PlayerYouTubeFrag();
        PlayerYouTubeFrag.currentPlayingIndex = pos;
        ArrayList<String> youtube_playlist = new ArrayList<>();
        youtube_playlist = getCurrentAllPlaylist(programlist);

        Bundle bundle = new Bundle();
        bundle.putString("url", new Gson().toJson(youtube_playlist));
        bundle.putInt("offset", 0);
        bundle.putInt("pos", pos);
        bundle.putInt("type", 2);
        bundle.putString("slug", mSlug);

        playerYouTubeFrag.setArguments(bundle);
        //playerYouTubeFrag.init();


        return playerYouTubeFrag;
    }

    public static PlayerYouTubeFrag newInstance(ChannelTotalFragment.ChannelTotalListener mListener, String title, String mData,  YoutubeDataListener mYoutubeDataListener, String mSlug) {
        PlayerYouTubeFrag.mYoutubeDataListener = mYoutubeDataListener;
        PlayerYouTubeFrag playerYouTubeFrag = new PlayerYouTubeFrag();
        PlayerYouTubeFrag.mListener = mListener;

        Bundle bundle = new Bundle();
        bundle.putString("data", mData);
        bundle.putString("title", title);
        bundle.putInt("type", 3);
        bundle.putString("slug", mSlug);

        playerYouTubeFrag.setArguments(bundle);
        // playerYouTubeFrag.initEmbedStream();

        return playerYouTubeFrag;

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initEmbedStream() {
        initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
            }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                activePlayer = player;
                activePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                activePlayer.setShowFullscreenButton(true);
                activePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
                activePlayer.setManageAudioFocus(true);
                activePlayer.setFullscreen(false);
                activePlayer.setPlayerStateChangeListener(new MyPlayerStateChangeListener());
                activePlayer.setPlaybackEventListener(new MyPlayerEventChangeListener());
                if (!wasRestored) {
                    String data = getArguments().getString("data");
                    String title = getArguments().getString("title");
                    if (data.contains("videoseries")) {
                        String id = ChannelUtils.getYoutubePlaylistId(data);
                        activePlayer.loadPlaylist(id);
                        mYoutubeDataListener.loadYoutubeVideo(id, title, 0);
                        currentId = id;
                    } else {
                        String id = ChannelUtils.getYoutubeId(data);
                        activePlayer.loadVideo(id);
                        mYoutubeDataListener.loadYoutubeVideo(id, title, 0);
                        currentId = id;
                    }
                    playPausePlayer();


                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null && runable != null) {
            handler.removeCallbacks(runable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedSlug = getArguments().getString("slug");
        int type = getArguments().getInt("type");
        if (type == 1) {
            init();
            setBackgroundtask();
        } else if (type == 2) {
            init();
        }
        if (type == 3) {
            initEmbedStream();
        }
        Log.d("test:::", "::type==" + type);
    }

    private static void setBackgroundtask() {
        if (handler != null)
            handler.removeCallbacks(null);
        handler = new Handler();
        runable = new Runnable() {
            @Override
            public void run() {
                if (runable != null) {
                    runbgTask();
                }

            }
        };
        if (runable != null) {
            handler.postDelayed(runable, 0);
        }

    }

    private static void runbgTask() {
        int newIndex = VideoFilter.getCurentVideoPosition(newProgramList);
        //seekoffset!=0 to check whether its called first time or not
        //to check current video playing for the time
        Log.d("test:::", "::CurrentIndex==" + currentPlayingIndex + ":::NewIndes==" + newIndex);

        if (newIndex > 0 && newIndex != currentPlayingIndex) {
            currentPlayingIndex = newIndex;
            if (activePlayer != null) {
                activePlayer.loadVideos(getCurrentPlaylist(newProgramList), 0, 0);
                playPausePlayer();
                mYoutubeDataListener.loadYoutubeVideo(getCurrentPlaylist(newProgramList).get(0), newProgramList.get(newIndex).getName(), 0);
            }

            if (handler != null)
                handler.postDelayed(runable, 1000);
        } else if (newIndex < 0 && currentPlayingIndex >= newProgramList.size() - 1) {
            ArrayList< ProgramList> updatedList = mYoutubeDataListener.getProgramList(selectedSlug);
            newProgramList.clear();
            newProgramList.addAll(updatedList);
            if (handler != null)
                handler.postDelayed(runable, 10);
        } else {
            if (handler != null)
                handler.postDelayed(runable, 1000);
        }
    }

    private void init() {
        initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
            }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                try {
                    activePlayer = player;
                    activePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    activePlayer.setShowFullscreenButton(true);
                    activePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
                    activePlayer.setManageAudioFocus(true);
                    activePlayer.setFullscreen(false);
                    activePlayer.setPlayerStateChangeListener(new MyPlayerStateChangeListener());
                    activePlayer.setPlaybackEventListener(new MyPlayerEventChangeListener());
                    if (!wasRestored) {
                        String data = getArguments().getString("url");
                        int off = getArguments().getInt("offset");
                        int pos = getArguments().getInt("pos");
                        ArrayList<String> youtube_playlis = new Gson().fromJson(data, new TypeToken<List<String>>() {
                        }.getType());
                        activePlayer.loadVideos(youtube_playlis, pos, off);
                        playPausePlayer();
                        mYoutubeDataListener.loadYoutubeVideo(youtube_playlis.get(0), newProgramList.get(pos).getName(), off);
                        currentId = youtube_playlis.get(0);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static ArrayList<String> getCurrentPlaylist(ArrayList< ProgramList> programlist) {
        ArrayList<String> youtube_data = new ArrayList<>();
        long nowAsPerDeviceTimeZone = ChannelUtils.GetUnixTime();
        if (currentPlayingIndex >= 0) {
            ArrayList< com.selecttvlauncher.channels.PlayList> playList = programlist.get(currentPlayingIndex).getPlaylist();
            for (int j = 0; j < playList.size(); j++) {
                youtube_data.add(playList.get(j).getData());
            }
        }
        return youtube_data;
    }

    private static ArrayList<String> getCurrentAllPlaylist(ArrayList< ProgramList> programlist) {
        ArrayList<String> youtube_data = new ArrayList<>();
        long nowAsPerDeviceTimeZone = ChannelUtils.GetUnixTime();
        for (int i = 0; i < programlist.size(); i++) {
            ArrayList< com.selecttvlauncher.channels.PlayList> playList = programlist.get(i).getPlaylist();
            for (int j = 0; j < playList.size(); j++) {
                youtube_data.add(playList.get(j).getData());
            }
        }

        return youtube_data;
    }

    private static int getCurrentPlaylistOffset(ArrayList< ProgramList> programlist) {
        if (currentPlayingIndex < 0)
            return 0;
        return (int) (ChannelUtils.GetUnixTime() - ChannelUtils.getDurationFromDate(programlist.get(currentPlayingIndex).getStart_at()));
    }

    private static int getCurrentplaylistIndex(ArrayList< ProgramList> programlist) {
        return VideoFilter.getCurentVideoPosition(programlist);
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayerandHandler();
    }

    public void releasePlayer() {
        try {
            if(activePlayer!=null){
                activePlayer.release();
                activePlayer=null;
            }
            if(handler!=null){
                handler.removeCallbacks(runable);
                handler=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void releasePlayerandHandler() {
        if (activePlayer != null) {
            activePlayer.release();
            activePlayer = null;
        }
        if (handler != null) {
            handler.removeCallbacks(runable);
            handler = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayerandHandler();
    }

    public void updatePlayer(int position, ArrayList< ProgramList> mProgramList) {
        if (activePlayer != null) {
            activePlayer.loadVideos(getCurrentAllPlaylist(mProgramList), position, 0);
            playPausePlayer();
        }

    }

    public void playPlayer(boolean isPlay) {
        if (activePlayer != null) {
            if (isPlay)
                activePlayer.play();
            else
                activePlayer.pause();
        }

    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {
            try {
                Log.d("youtube:::", "::::::loaded" + s);
                if (!currentId.equalsIgnoreCase(s)) {
                    mListener.videochange(s);
                    mYoutubeDataListener.loadYoutubeVideo(s, "", 0);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
//            if (!currentId.isEmpty())
//                mListener.videochange(currentId);
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
            if (handler != null) {
                runbgTask();
            }
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
            Log.d("youtube:::", "::::::error" + errorReason.name());
        }
    }


    private class MyPlayerEventChangeListener implements YouTubePlayer.PlaybackEventListener {
        @Override
        public void onPlaying() {
            isPaused = false;
        }

        @Override
        public void onPaused() {
            isPaused = true;

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    }

    private static void playPausePlayer() {
        if (isPaused)
            activePlayer.pause();
    }

    public void setFullScreen(final boolean isFullScreen) {
        if (activePlayer != null) {
            activePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
            activePlayer.setFullscreen(isFullScreen);
        }

    }
}