package com.selecttvlauncher.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.selecttvlauncher.Adapter.JSONAdapter;
import com.selecttvlauncher.BeanClass.ChannelCategoryBean;
import com.selecttvlauncher.BeanClass.ChannelModel;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.dialogs.OnDemandDialog;
import com.selecttvlauncher.ui.dialogs.ProgressHUD;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ocs pl-79(17.2.2016) on 4/27/2016.
 */
public class ChannelsActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaylistEventListener {


    public static String mtoolbarMaincontent, mtoolbarSubContent, mtoolbarcontent, mtoolbarlastcontent;
    public static Boolean isBackClicked = false;
    private TextView txt_splash_title, txt_splash_progress, allchannel_textView, browse_textView;
    private RoundCornerProgressBar splash_progress;
    public String szSplashTitle;
    public int nBackgroundLoading = 0;
    public float fPos = 0;
    private JSONArray m_jsonArrayCategories;
    private JSONArray m_jsonArrayParentCategories, m_jsonArrayStreams;
    public static JSONArray m_jsonArrayChannels;
    private int nCategory = 0;
    private JSONArray m_jsonLiveItems;
    public ArrayList<ChannelModel> m_channelSchedule = new ArrayList<ChannelModel>();
    public int nTimeOffset = 0;
    public int nTimeLimit = 2400;
    private int nChannelID = -1;
    private RelativeLayout linearFullScreen;
    private LinearLayout content_layout;

    int mMainCategorySelectedItem = 0;
    int mSubCategorySelectedItem = 0;
    int mSubContentSelectedItem = 0;
    private TextView activity_homescreen_toolbar_textview;
    private TextView activity_homescreen_toolbar_ondemand_text, activity_homescreen_toolbar_maincontent;
    private ImageView activity_homescreen_toolbar_fullview, activity_homescreen_toolbar_divider, activity_homescreen_toolbar_search, activity_homescreen_toolbar_mail, activity_homescreen_toolbar_info, activity_homescreen_toolbar_home;
    private ImageView activity_homescreen_toolbar_app_logo, activity_homescreen_maincontent_divider;
    YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;
    YouTubePlayer.PlaybackEventListener playbackEventListener;

    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private ListView listStreams;
    private JSONAdapter streamAdapter;
    private List<String> currentVideoList = new ArrayList<>();
    long prevStartTime = 0;
    FrameLayout frameMenu;
    DynamicImageView ui_row_video_now_btn_playing;
    LinearLayout linearShowTab, linearChannelTab, linearSubMainRight, scroll_arrow_layout;
    TextView channel_tab_textview, show_tab_textview;
    HorizontalScrollView channelTabContent;
    ScrollView listChannelTitle, overall_scroll;
    LinearLayout linearScrollChannelTitle;
    ScrollView listChannelDesc;
    LinearLayout linearScrollChannelDesc;
    ImageView imgchannelupdown;
    RelativeLayout layoutchannelupdown;
    boolean bChannelUp = false;
    public boolean bNoActive = true;
    boolean bTouchChannelDesc = false;
    View firstRow;
    ImageView imgScrollNext, imgScrollPrev;
    private boolean bExistChannel = false;
    private ArrayList<ChannelCategoryBean> m_channelViews = new ArrayList<>();
    private RecyclerView fragment_ondemand_alllist_items;
    private Context context = this;
    private LinearLayoutManager mLayoutManager;
    private String subCategoryAffix = "";
    private ImageView channel_prev_button;
    public static String mChannelList;
    ArrayList<String> arrayActorAdapterData = new ArrayList<>();
    private RelativeLayout spinner_layout;
    private Spinner spinner_actors;
    private RelativeLayout arrow_layout;
    private ArrayAdapter<String> adapter;
    private JSONArray m_jsonSubCategories;
    private ProgressBar progressBar_center;
    ImageView list_downscroll_arrow;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;

    Handler menu_handler = new Handler();

    Runnable run_menu;
    private String mCategorySelectedItem = "";
    private String mCategorySelectedHeader = "";
    private JSONArray temp;
    private JSONArray subCategoreis;
    private ImageView activity_channel_final_list_divider;
    private TextView activity_channel_final_list_text;

    long nMinStartTime = 0;
    int nMaxWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_channels);
        text_font_typeface();
        initializeToolbar();
        toolbarTextChange("Channels", "", "", "");
        spinner_layout = (RelativeLayout) findViewById(R.id.spinner_layout);
        arrow_layout = (RelativeLayout) findViewById(R.id.arrow_layout);
        list_downscroll_arrow = (ImageView) findViewById(R.id.list_downscroll_arrow);
        spinner_actors = (Spinner) findViewById(R.id.spinner_actors);
        channel_prev_button = (ImageView) findViewById(R.id.channel_prev_button);
        fragment_ondemand_alllist_items = (RecyclerView) findViewById(R.id.fragment_ondemand_alllist_items);
        txt_splash_title = (TextView) findViewById(R.id.txt_splash_title);
        txt_splash_progress = (TextView) findViewById(R.id.txt_splash_progress);
        splash_progress = (RoundCornerProgressBar) findViewById(R.id.splash_progress);
        linearFullScreen = (RelativeLayout) findViewById(R.id.linearFullScreen);
        content_layout = (LinearLayout) findViewById(R.id.content_layout);
        scroll_arrow_layout = (LinearLayout) findViewById(R.id.scroll_arrow_layout);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        txt_splash_title = (TextView) findViewById(R.id.txt_splash_title);
        txt_splash_progress = (TextView) findViewById(R.id.txt_splash_progress);
        channel_tab_textview = (TextView) findViewById(R.id.channel_tab_textview);
        show_tab_textview = (TextView) findViewById(R.id.show_tab_textview);
        browse_textView = (TextView) findViewById(R.id.browse_textView);
        allchannel_textView = (TextView) findViewById(R.id.allchannel_textView);

        txt_splash_title.setTypeface(OpenSans_Regular);
        txt_splash_progress.setTypeface(OpenSans_Regular);
        channel_tab_textview.setTypeface(OpenSans_Regular);
        show_tab_textview.setTypeface(OpenSans_Regular);
        allchannel_textView.setTypeface(OpenSans_Regular);

        browse_textView.setTypeface(OpenSans_Bold);


        splash_progress = (RoundCornerProgressBar) findViewById(R.id.splash_progress);
        linearFullScreen = (RelativeLayout) findViewById(R.id.linearFullScreen);
        content_layout = (LinearLayout) findViewById(R.id.content_layout);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        progressBar_center = (ProgressBar) findViewById(R.id.progressBar_center);
        listStreams = (ListView) findViewById(R.id.listStreams);

        linearFullScreen.setVisibility(View.VISIBLE);
        content_layout.setVisibility(View.GONE);
        frameMenu = (FrameLayout) findViewById(R.id.frameMenu);

        linearShowTab = (LinearLayout) findViewById(R.id.linearShowTab);
        linearChannelTab = (LinearLayout) findViewById(R.id.linearChannelTab);
        channelTabContent = (HorizontalScrollView) findViewById(R.id.channelTabContent);
        overall_scroll = (ScrollView) findViewById(R.id.overall_scroll);
        listChannelTitle = (ScrollView) findViewById(R.id.listChannelTitle);
        linearScrollChannelTitle = (LinearLayout) findViewById(R.id.linearscrollChannelTitle);
        listChannelDesc = (ScrollView) findViewById(R.id.listChannelDesc);
        linearScrollChannelDesc = (LinearLayout) findViewById(R.id.linearscrollChannelDesc);
        linearSubMainRight = (LinearLayout) findViewById(R.id.linearSubMainRight);

        imgchannelupdown = (ImageView) findViewById(R.id.imgchannelupdown);
        layoutchannelupdown = (RelativeLayout) findViewById(R.id.layoutchannelupdown);

        imgScrollNext = (ImageView) findViewById(R.id.imgscrollnext);
        imgScrollPrev = (ImageView) findViewById(R.id.imgscrollprev);


        imgScrollNext = (ImageView) findViewById(R.id.imgscrollnext);
        imgScrollPrev = (ImageView) findViewById(R.id.imgscrollprev);
        mLayoutManager = new LinearLayoutManager(context);
        fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
        fragment_ondemand_alllist_items.hasFixedSize();
        imgScrollNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if(channelTabContent.getScrollX()%channelTabContent.getMeasuredWidth()==0){
                channelTabContent.setScrollX(channelTabContent.getScrollX() + channelTabContent.getMeasuredWidth());
                /*}else{
                    channelTabContent.setScrollX((channelTabContent.getScrollX() -(channelTabContent.getScrollX()-channelTabContent.getMeasuredWidth()))+ (channelTabContent.getMeasuredWidth()));
                }*/


            }
        });
        imgScrollPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                channelTabContent.setScrollX(channelTabContent.getScrollX() - channelTabContent.getMeasuredWidth());
            }
        });


        layoutchannelupdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = 2.9f;
                if (bChannelUp == false) {
                    imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowdown));
                    weight = 0.5f;
                } else {
                    imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowup));
                    weight = 2.9f;
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearSubMainRight.getLayoutParams();
                params.weight = weight;
                linearSubMainRight.setLayoutParams(params);
                bNoActive = false;
                bChannelUp = !bChannelUp;

                /*if( player != null )
                    player.play();*/
            }
        });

        linearShowTab.setOnClickListener(m_ShowTabClickListener);
        linearChannelTab.setOnClickListener(m_ChannelTabClickListener);

        szSplashTitle = "Loading Data";
        splash_progress.setProgressColor(Color.parseColor("#fce623"));
        splash_progress.setBackgroundColor(Color.parseColor("#1C86EE"));
        splash_progress.setProgressBackgroundColor(Color.parseColor("#cecece"));
        splash_progress.setRadius(8);
        splash_progress.setMax(100);
        splash_progress.setProgress(0);

        youTubeView.initialize(Constants.DEVELOPER_KEY, this);
        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();

        loadCategoriesAndShow();
        channel_prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBackClicked = true;
                if (mChannelList.equals(Constants.CHANNEL_DECADECONTENT)) {


                    try {
                        spinner_layout.setVisibility(View.GONE);
                        arrow_layout.setVisibility(View.GONE);
                        mChannelList = Constants.CHANNEL_SUBCATEGORY;

                        new LoadingDecade(true, 1).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (mChannelList.equals(Constants.CHANNEL_SUBCONTENT)) {

                    try {
                        spinner_layout.setVisibility(View.GONE);
                        arrow_layout.setVisibility(View.GONE);
                        fragment_ondemand_alllist_items.setVisibility(View.GONE);
                        mChannelList = Constants.CHANNEL_SUBCATEGORY;

                        /*JSONObject jsonObject = m_jsonSubCategories.getJSONObject(mSubCategorySelectedItem);
                        int nID = jsonObject.getInt("id");
                        JSONArray subCategoreis = getSubCategories(nID);
                        m_jsonSubCategories = subCategoreis;*/
                        mCategorySelectedItem = "";
                        toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");

                        subCategoryAffix = "";


                        showSubCategoryDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (mChannelList.equals(Constants.CHANNEL_SUBCATEGORY)) {
                    try {
                        mChannelList = Constants.CHANNEL_MAINCONTENT;
                        spinner_layout.setVisibility(View.GONE);
                        arrow_layout.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = m_jsonArrayParentCategories.getJSONObject(mMainCategorySelectedItem);
                            int nID = jsonObject.getInt("id");
                            temp = getSubCategories(nID);
                            if (temp.length() == 0) {
                                toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                loadChannel(nID);
                            } else {

                                if (mtoolbarMaincontent.equals("TV")) {
                                    subCategoryAffix = "TV";
                                }
                                toolbarTextChange("Channels", mtoolbarMaincontent, "", "");
                                mCategorySelectedItem = "";
                                m_jsonSubCategories = temp;
                                showSubCategoryDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (mChannelList.equals(Constants.CHANNEL_MAINCONTENT)) {
                    try {
                        toolbarTextChange("Channels", "", "", "");
                        mChannelList = "";
                        spinner_layout.setVisibility(View.GONE);
                        arrow_layout.setVisibility(View.GONE);
                        makeChannelInterface();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Intent in = new Intent(ChannelsActivity.this, HomeActivity.class);
                        startActivity(in);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });


        list_downscroll_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    int firstVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                    firstVisiblePosition = firstVisiblePosition + 1;
                    fragment_ondemand_alllist_items.smoothScrollToPosition(firstVisiblePosition);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                // Hide the status bar.
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void minimizeChannelView() {

        try {
            Log.d("touchevent::", "touchevent::");
            float weight = 2.9f;
            if (bChannelUp == true) {

                imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowup));
                weight = 2.9f;
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearSubMainRight.getLayoutParams();
            params.weight = weight;
            linearSubMainRight.setLayoutParams(params);
            bNoActive = false;
            bChannelUp = !bChannelUp;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initializeToolbar() {
        try {
            activity_homescreen_toolbar_app_logo = (ImageView) findViewById(R.id.activity_homescreen_toolbar_app_logo);
            activity_homescreen_toolbar_divider = (ImageView) findViewById(R.id.activity_homescreen_toolbar_divider);
            activity_homescreen_maincontent_divider = (ImageView) findViewById(R.id.activity_homescreen_maincontent_divider);
            activity_channel_final_list_divider = (ImageView) findViewById(R.id.activity_channel_final_list_divider);
            activity_homescreen_toolbar_textview = (TextView) findViewById(R.id.activity_homescreen_toolbar_textview);
            activity_channel_final_list_text = (TextView) findViewById(R.id.activity_channel_final_list_text);
            activity_homescreen_toolbar_maincontent = (TextView) findViewById(R.id.activity_homescreen_toolbar_maincontent);
            activity_homescreen_toolbar_ondemand_text = (TextView) findViewById(R.id.activity_homescreen_toolbar_ondemand_text);
            activity_homescreen_toolbar_search = (ImageView) findViewById(R.id.activity_homescreen_toolbar_search);
            activity_homescreen_toolbar_info = (ImageView) findViewById(R.id.activity_homescreen_toolbar_info);
            activity_homescreen_toolbar_home = (ImageView) findViewById(R.id.activity_homescreen_toolbar_home);
            activity_homescreen_toolbar_mail = (ImageView) findViewById(R.id.activity_homescreen_toolbar_mail);
            activity_homescreen_toolbar_fullview = (ImageView) findViewById(R.id.activity_homescreen_toolbar_fullview);

            activity_homescreen_toolbar_textview.setTypeface(OpenSans_Regular);
            activity_homescreen_toolbar_maincontent.setTypeface(OpenSans_Regular);
            activity_homescreen_toolbar_ondemand_text.setTypeface(OpenSans_Regular);

            activity_homescreen_toolbar_textview.setText("Channels");
            activity_homescreen_toolbar_mail.setVisibility(View.GONE);
            activity_homescreen_toolbar_search.setVisibility(View.VISIBLE);
            activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
            activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
            activity_homescreen_toolbar_fullview.setVisibility(View.VISIBLE);

            activity_homescreen_toolbar_fullview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (ChannelsActivity.this.player != null) {

                            ChannelsActivity.this.player.setFullscreen(true);

                        }
                        menu_handler.removeCallbacks(run_menu);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            activity_homescreen_toolbar_app_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(ChannelsActivity.this, HomeActivity.class);
                    startActivity(in);
                    finish();
                }
            });

            activity_homescreen_toolbar_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(ChannelsActivity.this, HomeActivity.class);
                    startActivity(in);
                    finish();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (player != null) {
                player.release();
            }
            menu_handler.removeCallbacks(run_menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (ChannelsActivity.this.player != null && ChannelsActivity.this.player.isPlaying()) {

                ChannelsActivity.this.player.setFullscreen(false);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadCategoriesAndShow() {
        nBackgroundLoading = 0;
        szSplashTitle = "Loading Channel Categories...";
        txt_splash_title.setText(szSplashTitle);
        new LoadingCategoryTask().execute();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        try {
            if (!wasRestored) {

                ChannelsActivity.this.player = player;
                ChannelsActivity.this.player.setPlaylistEventListener(ChannelsActivity.this);

                //player.loadVideo(video_url);

                // Hiding player controls
                ChannelsActivity.this.player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                ChannelsActivity.this.player.setPlayerStateChangeListener(playerStateChangeListener);
                ChannelsActivity.this.player.setPlaybackEventListener(playbackEventListener);

                try {
                    run_menu = new Runnable() {
                        public void run() {
                            if (ChannelsActivity.this.player != null && ChannelsActivity.this.player.isPlaying()) {

                                ChannelsActivity.this.player.setFullscreen(true);
                            }
                            menu_handler.removeCallbacks(run_menu);

                        }
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    public void onPrevious() {
        Log.d("Youtube::", "prevoius selected");
    }

    @Override
    public void onNext() {
        Log.d("Youtube::", "next selected");

    }

    @Override
    public void onPlaylistEnded() {
        Log.d("Youtube::", "playlist selected");
    }

    private class LoadingCategoryTask extends AsyncTask<Object, Integer, Object> {

        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            //  mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }

        @Override
        protected Object doInBackground(Object... params) {
            m_jsonArrayCategories = JSONRPCAPI.getCategories();
            if (m_jsonArrayCategories == null) return null;
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep(100);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            // mProgressHUD.dismiss();

            if (m_jsonArrayCategories != null) {
                m_jsonArrayParentCategories = new JSONArray();
                for (int i = 0; i < m_jsonArrayCategories.length(); i++) {
                    try {
                        JSONObject objItem = m_jsonArrayCategories.getJSONObject(i);

                        boolean b = objItem.isNull("parent_id");
                        if (b) {
                            m_jsonArrayParentCategories.put(objItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                mChannelList = "";
                makeChannelInterface();

                showFirView();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            splash_progress.setProgress(values[0]);
            fPos = values[0];
            txt_splash_progress.setText((int) fPos + "%");
        }
    }


    ///////////////////Channel MAin categorylist//////////////////////////////////


    private void makeChannelInterface() {
        m_channelViews.clear();

        try {
            for (int i = 0; i < m_jsonArrayParentCategories.length(); i++) {
                JSONObject objCategoryItem = m_jsonArrayParentCategories.getJSONObject(i);
                String strName = "", strparent_id = "", strimage = "";
                int strid = 0;
                if (objCategoryItem.has("id")) {
                    strid = objCategoryItem.getInt("id");
                }
                if (objCategoryItem.has("name")) {
                    strName = objCategoryItem.getString("name");
                }
                if (objCategoryItem.has("parent_id")) {
                    strparent_id = objCategoryItem.getString("parent_id");
                }

                if (objCategoryItem.has("image")) {
                    strimage = objCategoryItem.getString("image");
                }
                m_channelViews.add(new ChannelCategoryBean(strid, strparent_id, strimage, strName));
            }

            ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter(m_channelViews, context);
            fragment_ondemand_alllist_items.setAdapter(channelCategoryAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showFirView() {
        try {
            JSONObject jsonObject = m_jsonArrayCategories.getJSONObject(0);
            int nID = 145;//jsonObject.getInt("id");
            nCategory = nID;

            nBackgroundLoading = 1;
            szSplashTitle = "Loading Channels Data...";
            txt_splash_title.setText(szSplashTitle);
            new FirstLoadingTask().execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class FirstLoadingTask extends AsyncTask<Object, Integer, Object> {
        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            //  mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }

        @Override
        protected Object doInBackground(Object... params) {
            if (nCategory != -1)
                m_jsonLiveItems = JSONRPCAPI.getChannels(nCategory);
            if (m_jsonLiveItems == null) return null;
            try {
                for (int i = 50; i <= 100; i++) {
                    try {
                        Thread.sleep(100);
                        publishProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String IDs = "";
                m_channelSchedule.clear();
                for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                    JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                    if (IDs.length() == 0)
                        IDs = obj.getInt("id") + "";
                    else
                        IDs = IDs + "," + obj.getInt("id");
                }
                nTimeOffset = 0;
                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, 0, nTimeLimit);

                if (objSchedule != null) {
                    for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                        JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                        IDs = obj.getInt("id") + "";
                        ChannelModel model = new ChannelModel();
                        model.channelId = obj.getInt("id");
                        model.channelName = obj.getString("name");
                        model.channelLogo = obj.getString("big_logo_url");
                        model.channelVideos = objSchedule.getJSONObject(IDs).getJSONArray("videos").getJSONArray(0);
                        m_channelSchedule.add(model);


                    }
                } else {
                    m_channelSchedule.clear();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            //  mProgressHUD.dismiss();

            setMainViewData(m_jsonLiveItems, 0);


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            splash_progress.setProgress(values[0]);
            fPos = values[0];
            txt_splash_progress.setText((int) fPos + "%");
        }

        public void setMainViewData(JSONArray jsonArray, int nChannelId) {
            if (jsonArray == null) return;

            m_jsonArrayChannels = jsonArray;

            JSONObject jObj = null;
            int nID = -1;
            String strChannelName = null;
            try {
                jObj = m_jsonArrayChannels.getJSONObject(nChannelId);
                nID = jObj.getInt("id");
                strChannelName = jObj.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            nBackgroundLoading = 2;
            szSplashTitle = "Loading Channel Shows...";

            setLiveData(nID, strChannelName);
        }
    }

    public void setLiveData(int nID, String strName) {

        nChannelID = nID;
        linearFullScreen.setVisibility(View.GONE);
        content_layout.setVisibility(View.VISIBLE);
        new LoadingOnDemandTasks().execute();
        //categoryMenu.showContent();


        //makeChannelList();
    }

    private class LoadingOnDemandTasks extends AsyncTask<Object, Object, Object> {

        LoadingOnDemandTasks() {

        }

        private boolean showDialog = false;

        LoadingOnDemandTasks(boolean showDialog) {
            this.showDialog = false;
        }

        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {

            linearFullScreen.setVisibility(View.GONE);
            if (linearFullScreen.getVisibility() == View.GONE)
                mProgressHUD = ProgressHUD.show(ChannelsActivity.this, "Please Wait...", true, false, null);
        }

        @Override
        protected Object doInBackground(Object... params) {
            m_jsonArrayStreams = JSONRPCAPI.getStreams(nChannelID);
            if (m_jsonArrayStreams == null) return null;
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {


            if (linearFullScreen.getVisibility() == View.GONE)
                mProgressHUD.dismiss();
            linearFullScreen.setVisibility(View.GONE);

            menu_handler.postDelayed(run_menu, 60000);
            if (m_jsonArrayStreams == null) return;


            if (m_jsonArrayStreams != null) {

                streamAdapter = new JSONAdapter(ChannelsActivity.this, m_jsonArrayStreams);
                listStreams.setAdapter(streamAdapter);

            } else {
                m_channelSchedule.clear();
            }

            makeChannelList();
            makeChannelTitleList();
            makeChannelDescList();

            currentVideoList.clear();

            for (int i = 0; i < m_jsonArrayStreams.length(); i++) {
                try {
                    String curUrl = m_jsonArrayStreams.getJSONObject(i).getString("url");
                    if (!TextUtils.isEmpty(curUrl)) {
                        currentVideoList.add(curUrl);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            int timeOffset = getTimeOffset();

            if (player != null) {

                if (currentVideoList.size() == 0) {
                    return;
                }

                try {
                    player.loadVideos(currentVideoList, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*try {
                    m_jsonArrayStreamsForCasting = new JSONArray(m_jsonArrayStreams.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setNowItem(true);*/

                try {
                    prevStartTime = m_jsonArrayStreams.getJSONObject(0).getInt("unix_time");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //m_jsonArrayStreams.remove(0);
                removeFirstChannel();
                if (m_jsonArrayStreams != null) {
                    streamAdapter = new JSONAdapter(ChannelsActivity.this, m_jsonArrayStreams);
                    listStreams.setAdapter(streamAdapter);
                }
                //streamAdapter.notifyDataSetChanged();
            }


            listStreams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        String strUrl = null;
                        String strTitle = null;
                        try {
                            if (m_jsonArrayStreams != null) {
                                JSONObject jObj = m_jsonArrayStreams.getJSONObject(position);
                                strTitle = jObj.getString("title");
                                strUrl = jObj.getString("url");
                                OnDemandDialog demanddialog = new OnDemandDialog(ChannelsActivity.this);
                                demanddialog.setInfo(strTitle, strUrl, position);
                                demanddialog.setActivity(ChannelsActivity.this);
                                demanddialog.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*try {
                        m_jsonArrayStreamsForCasting = new JSONArray(m_jsonArrayStreams.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    demanddialog = new OnDemandDialog(MainActivity.this);
                    demanddialog.setInfo(strTitle, strUrl, position);
                    demanddialog.setActivity(MainActivity.this);
                    demanddialog.show();*/
                }
            });

        }

        private boolean canResolveIntent(Intent intent) {
            List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(intent, 0);
            return resolveInfo != null && !resolveInfo.isEmpty();
        }


    }

    private void makeChannelList() {

        try {
            int maxCount=0;
            if (m_channelSchedule.size() == 0) return;
            long nMaxEndTime = 0;
            nMinStartTime = 0;
            for (int i = 0; i < m_channelSchedule.size(); i++) {
                ChannelModel item = m_channelSchedule.get(i);
                try {
                    JSONObject objStart = item.channelVideos.getJSONObject(0);
                    JSONObject objEnd = item.channelVideos.getJSONObject(item.channelVideos.length() - 1);

                    if( nMinStartTime == 0 )
                        nMinStartTime = objStart.getLong("unix_time");
                    if( objStart.getLong("unix_time") < nMinStartTime )
                        nMinStartTime = objStart.getLong("unix_time");

                    if( nMaxEndTime < (objEnd.getLong("unix_time") + objEnd.getLong("duration")) )
                        nMaxEndTime = (objEnd.getLong("unix_time") + objEnd.getLong("duration"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            nMaxWidth = 0;
            for (int k = 0; k < m_channelSchedule.size(); k++) {
                ChannelModel model = m_channelSchedule.get(k);
                long nPrevStartTime = nMinStartTime;
                int nWidth = 0;
                if(maxCount<model.channelVideos.length()){
                    maxCount=model.channelVideos.length();
                }
                for (int i = 0; i < model.channelVideos.length(); i++) {
                    try {
                        JSONObject obj = model.channelVideos.getJSONObject(i);
                        long nDeltaTime = obj.getLong("unix_time") - nPrevStartTime;
                        if(  nDeltaTime > 0 ){
                            nWidth += (int) nDeltaTime;
                        }

                        if((obj.getLong("duration")) < 100){
                            nWidth += 130;
                        }/*else if( (obj.getLong("duration") ) > 500 ){
                        nWidth += 500;
                    }*/else
                            nWidth += (int) (obj.getLong("duration") + 30);

                        nPrevStartTime = obj.getLong("unix_time") + obj.getLong("duration");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if( nMaxWidth < nWidth )
                    nMaxWidth = nWidth;

            }
            ViewGroup.LayoutParams layoutParams = listChannelDesc.getLayoutParams();
            Log.d("maxCount::::///","maxCount::::???"+maxCount);
            layoutParams.width = nMaxWidth + 5+(maxCount*400);
            listChannelDesc.setLayoutParams(layoutParams);
            listChannelTitle.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    bNoActive = false;
                    if (!bTouchChannelDesc)
                        listChannelDesc.setScrollY(listChannelTitle.getScrollY());
                }
            });
            listChannelDesc.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    bNoActive = false;
                    if (bTouchChannelDesc)
                        listChannelTitle.setScrollY(listChannelDesc.getScrollY());
                }
            });

            listChannelDesc.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    bTouchChannelDesc = true;
                    return false;
                }
            });
            listChannelTitle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    bTouchChannelDesc = false;
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeChannelTitleList() {
        try {
            LayoutInflater m_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            linearScrollChannelTitle.removeAllViews();

            if (m_channelSchedule.size() != 0) {
                for (int k = 0; k < m_channelSchedule.size(); k++) {
                    ChannelModel model = m_channelSchedule.get(k);
                    View row = m_inflater.inflate(R.layout.item_channel_grid, null);
                    LinearLayout linear = (LinearLayout) row.findViewById(R.id.item_grid);
                    DynamicImageView imageView = (DynamicImageView) row.findViewById(R.id.imageShowThumbnail);
                    TextView txtChannelName = (TextView) row.findViewById(R.id.txtChannelName);
                    txtChannelName.setTypeface(OpenSans_Regular);

                    imageView.loadImage(model.channelLogo);
                    txtChannelName.setText(model.channelName);
                    if (k == 0)
                        linear.setBackgroundColor(Color.parseColor("#0D76BC"));
                    else
                        linear.setBackgroundColor(Color.parseColor("#004A7C"));

                    final int position = k;
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (m_channelSchedule.size() != 0) {
                                    bNoActive = false;
                                    ChannelModel model1 = m_channelSchedule.get(position);
                                    m_channelSchedule.remove(position);
                                    m_channelSchedule.add(0, model1);
                                    nTimeOffset = -nTimeLimit;
                                    setLiveData(model1.channelId, model1.channelName);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    linearScrollChannelTitle.addView(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeChannelDescRefresh(String s) {
        try {
            LinearLayout linear = (LinearLayout) firstRow.findViewById(R.id.item_channel_desc);
            for (int i = 0; i < linear.getChildCount(); i++) {
                View childItem = linear.getChildAt(i);
                String key = (String) childItem.getTag();
                if (key.equals(s)) {
                    childItem.setBackgroundColor(Color.parseColor("#3e99f2"));
                    bExistChannel = true;
                } else {
                    childItem.setBackgroundColor(Color.parseColor("#01395E"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeChannelDescList() {
        try {
            ViewGroup.LayoutParams layoutParams = null;
            LayoutInflater m_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            linearScrollChannelDesc.removeAllViews();
            //int nWidth = channelTabContent.getMeasuredWidth() / 2;


            if (m_channelSchedule.size() != 0) {
                for (int k = 0; k < m_channelSchedule.size(); k++) {

                    View row = m_inflater.inflate(R.layout.item_channel_desc, null);
                    LinearLayout linear = (LinearLayout) row.findViewById(R.id.item_channel_desc);

                    int nTempWidth = 0;

                    try {
                        linear.removeAllViews();

                        long nPrevStartTime = nMinStartTime;

                        final ChannelModel model = m_channelSchedule.get(k);
                        for (int i = 0; i < model.channelVideos.length(); i++) {

                            JSONObject obj = model.channelVideos.getJSONObject(i);

                            View childItem = m_inflater.inflate(R.layout.item_channel_desc_item, null);

                            //DynamicImageView imageView = (DynamicImageView) childItem.findViewById(R.id.imageShowThumbnail);
                            TextView txtChannelName = (TextView) childItem.findViewById(R.id.txtChannelName);
                            TextView txtChannel_time = (TextView) childItem.findViewById(R.id.txtChannel_time);
                            txtChannelName.setTypeface(OpenSans_Regular);
                            txtChannel_time.setTypeface(OpenSans_Regular);
                            //imageView.setVisibility(View.GONE);

                            long nDeltaTime = obj.getLong("unix_time") - nPrevStartTime;
                            if( i == 0 ) nDeltaTime = 0;
                            if (nDeltaTime > 0) {
                                View view = m_inflater.inflate(R.layout.item_channel_desc_item, null);
                                TextView txtChannelName1 = (TextView) view.findViewById(R.id.txtChannelName);
                                TextView txtChannel_time1 = (TextView) view.findViewById(R.id.txtChannel_time);
                                txtChannelName1.setText("");
                                txtChannel_time1.setText("");
                                view.setBackgroundColor(Color.parseColor("#3e99f2"));
                                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                int nWidth = (int) nDeltaTime;
                                layoutParams.width = nWidth;
                                Log.d("TiTle///::::",":::"+"///");
                                Log.d("Width///::::",":::"+nWidth);
                                view.setLayoutParams(layoutParams);
                                linear.addView(view);
                            }
                            nTempWidth += nDeltaTime;



                           /* layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = nWidth;
                            Log.d("selecttv::", "Width:::" + nWidth);
                            childItem.setLayoutParams(layoutParams);*/

                            String strText = obj.getString("title");
                            String strTextTime = obj.getString("time");
                            txtChannelName.setText(strText);
                            txtChannel_time.setText("Start Time: "+strTextTime);
                            String youtuve_video_id = obj.getString("url");
                            childItem.setTag(youtuve_video_id);
                       /* childItem.setTag(strText + strTime);
                        if (k == 0 && strText.equals(textNowTitle.getText().toString()) && strTime.equals(textNowTime.getText().toString())) {
                            childItem.setBackgroundColor(Color.parseColor("#3e99f2"));
                            bExistChannel = true;
                        } else {
                            childItem.setBackgroundColor(Color.parseColor("#252525"));
                        }
    */
                            childItem.setBackgroundColor(Color.parseColor("#01395E"));
                            final JSONObject jObj = obj;
                            final int nPos = i;
                            childItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String strUrl = null;
                                    String strTitle = null;
                                    bNoActive = false;
                                    try {
                                        strTitle = jObj.getString("title");
                                        strUrl = jObj.getString("url");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    OnDemandDialog demanddialog = new OnDemandDialog(ChannelsActivity.this);
                                    demanddialog.setInfo(strTitle, strUrl, nPos);
                                    demanddialog.setActivity(ChannelsActivity.this);
                                    demanddialog.show();


                                }
                            });
                            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            int nWidth = 100;
                            if ((obj.getLong("duration")) < 100) {
                                nWidth = 130;
                            } /*else if ((obj.getLong("duration")) > 500) {
                                nWidth = 500;
                            }*/ else
                                nWidth = (int) (obj.getLong("duration") + 30);
                            nTempWidth += nWidth;
                            if( i == model.channelVideos.length() - 1){
                                nWidth += nMaxWidth - nTempWidth;
                            }
                            Log.d("TiTle::::",":::"+strText);
                            Log.d("Width::::",":::"+nWidth);

                            layoutParams.width = nWidth+400;
                            childItem.setLayoutParams(layoutParams);

                            linear.addView(childItem);
                            nPrevStartTime = obj.getLong("unix_time") + obj.getLong("duration");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    row.setTag(k);
                    if (k == 0) firstRow = row;
                    linearScrollChannelDesc.addView(row);

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onClickShowTab() {
        scroll_arrow_layout.setVisibility(View.GONE);
        frameMenu.setVisibility(View.VISIBLE);
        channelTabContent.setVisibility(View.GONE);
        listChannelTitle.setVisibility(View.GONE);
        overall_scroll.setVisibility(View.GONE);

        linearShowTab.setBackgroundColor(ContextCompat.getColor(context, R.color.channels_selecetd_bg));
        linearChannelTab.setBackgroundColor(ContextCompat.getColor(context, R.color.channels_title_header_bg));

        show_tab_textview.setTextColor(ContextCompat.getColor(context, R.color.white));
        channel_tab_textview.setTextColor(ContextCompat.getColor(context, R.color.channels_unselecetd_texcolor));

    }

    public void onClickChannelTab() {
        scroll_arrow_layout.setVisibility(View.VISIBLE);
        frameMenu.setVisibility(View.GONE);
        channelTabContent.setVisibility(View.VISIBLE);
        listChannelTitle.setVisibility(View.VISIBLE);
        overall_scroll.setVisibility(View.VISIBLE);

        linearChannelTab.setBackgroundColor(ContextCompat.getColor(context, R.color.channels_selecetd_bg));
        linearShowTab.setBackgroundColor(ContextCompat.getColor(context, R.color.channels_title_header_bg));

        channel_tab_textview.setTextColor(ContextCompat.getColor(context, R.color.white));
        show_tab_textview.setTextColor(ContextCompat.getColor(context, R.color.channels_unselecetd_texcolor));
    }

    View.OnClickListener m_ShowTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickShowTab();
        }

    };
    View.OnClickListener m_ChannelTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickChannelTab();
        }

    };

    public void removeFirstChannel() {
        if (m_jsonArrayStreams == null) return;
        if (m_jsonArrayStreams.length() == 0) return;
        JSONArray retArray = new JSONArray();
        for (int i = 1; i < m_jsonArrayStreams.length(); i++) {
            try {
                JSONObject jsonObject = m_jsonArrayStreams.getJSONObject(i);
                retArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        m_jsonArrayStreams = retArray;
    }

    private int getTimeOffset() {
        if (m_jsonArrayStreams == null) return 0;

        JSONObject firstObj = null;
        long unixTime = 0;
        long curTime = 0;
        int timeOffset = 0;
        try {
            firstObj = m_jsonArrayStreams.getJSONObject(0);
            unixTime = firstObj.getInt("unix_time");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        curTime = System.currentTimeMillis() / 1000;

        long t1 = System.currentTimeMillis() / 1000;
        Date dt = new Date();
        long t2 = dt.getTime() / 1000;
        int h = dt.getHours();
        int m = dt.getMinutes();
        int s = dt.getSeconds();
        long t3 = Calendar.getInstance().getTimeInMillis() / 1000;

        Log.e("TIMEOFFSET", String.format("unix_Time = %d, curTime = %d", unixTime, curTime));
        timeOffset = (int) (curTime - unixTime);

        if (timeOffset > 0)
            return timeOffset;
        else
            return 0;
    }

    private int getTimeOffsetFromId(int nId) {
        if (m_jsonArrayStreams == null) return 0;

        JSONObject firstObj = null;
        long unixTime = 0;
        long curTime = 0;
        int timeOffset = 0;
        try {
            firstObj = m_jsonArrayStreams.getJSONObject(nId);
            unixTime = firstObj.getInt("unix_time");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        curTime = System.currentTimeMillis() / 1000;

        long t1 = System.currentTimeMillis() / 1000;
        Date dt = new Date();
        long t2 = dt.getTime() / 1000;
        int h = dt.getHours();
        int m = dt.getMinutes();
        int s = dt.getSeconds();
        long t3 = Calendar.getInstance().getTimeInMillis() / 1000;

        Log.e("TIMEOFFSET", String.format("unix_Time = %d, curTime = %d", unixTime, curTime));
        timeOffset = (int) (curTime - unixTime);

        if (timeOffset > 0)
            return timeOffset;
        else
            return 0;
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
            Log.d("youtube:::", "::::::loaded" + s);
            makeChannelDescRefresh(s);
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


    ///////////////////Channel MAin categorylist Adapter//////////////////////////////////

    private class ChannelCategoryAdapter extends RecyclerView.Adapter<ChannelCategoryAdapter.DataObjectHolder> {
        ArrayList<ChannelCategoryBean> m_channelViews;
        Context context;
        int mSelectedItem;

        public ChannelCategoryAdapter(ArrayList<ChannelCategoryBean> m_channelViews, Context context) {
            this.m_channelViews = m_channelViews;
            this.context = context;
            text_font_typeface();
        }


        @Override
        public ChannelCategoryAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChannelCategoryAdapter.DataObjectHolder holder, final int position) {
            holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
            holder.fragment_ondemandlist_items.setText(m_channelViews.get(position).getmChannelCategoryName());


            if (position == mMainCategorySelectedItem) {
                Log.d("drawable", "position  " + mMainCategorySelectedItem);
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }


            holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isBackClicked = false;
                    mChannelList = Constants.CHANNEL_MAINCONTENT;
                    mMainCategorySelectedItem = position;
                    minimizeChannelView();

                    try {
                        JSONObject jsonObject = m_jsonArrayParentCategories.getJSONObject(position);
                        int nID = jsonObject.getInt("id");
                        temp = getSubCategories(nID);
                        subCategoryAffix = m_channelViews.get(position).getmChannelCategoryName();
                        if (temp.length() == 0) {
                            mtoolbarMaincontent = m_channelViews.get(position).getmChannelCategoryName();
                            toolbarTextChange("Channels", mtoolbarMaincontent, "", "");
                            fragment_ondemand_alllist_items.setVisibility(View.GONE);
                            loadChannel(nID);
                        } else {
                            mtoolbarMaincontent = m_channelViews.get(position).getmChannelCategoryName();
                            toolbarTextChange("Channels", mtoolbarMaincontent, "", "");
                            m_jsonSubCategories = temp;
                            showSubCategoryDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

        }

        @Override
        public int getItemCount() {
            return m_channelViews.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);

                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);
            }
        }
    }

    /*public void loadChannel(int nID){
        nCategory = nID;
        new LoadingChannelTask(true).execute();
    }*/


    private JSONArray getSubCategories(int categoryID) {
        subCategoreis = new JSONArray();
        for (int i = 0; i < m_jsonArrayCategories.length(); i++) {
            try {
                JSONObject objItem = m_jsonArrayCategories.getJSONObject(i);
                if (!objItem.isNull("parent_id")) {
                    if (objItem.getInt("parent_id") == categoryID)
                        subCategoreis.put(objItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return subCategoreis;
    }

    ///////////////////Channel sub categorylist//////////////////////////////////

    private void showSubCategoryDialog() {
        ArrayList<String> arrayAdapter = new ArrayList<>();
        for (int i = 0; i < m_jsonSubCategories.length(); i++) {
            try {
                JSONObject jsonObject = m_jsonSubCategories.getJSONObject(i);
                String strName = jsonObject.getString("name");

                arrayAdapter.add(strName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (subCategoryAffix.equals("TV")) {
            arrayAdapter.add("Decade");
            subCategoryAffix = "";
        }
        fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
        ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelSubCategoryAdapter(arrayAdapter, context);
        fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
    }

    ///////////////////Channel sub categorylist Adapter//////////////////////////////////


    private class ChannelSubCategoryAdapter extends RecyclerView.Adapter<ChannelSubCategoryAdapter.DataObjectHolder> {
        ArrayList<String> arrayAdapter;
        Context context;
        int mSelectedItem;

        public ChannelSubCategoryAdapter(ArrayList<String> arrayAdapter, Context context) {
            this.arrayAdapter = arrayAdapter;
            this.context = context;
            text_font_typeface();
        }

        @Override
        public ChannelSubCategoryAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChannelSubCategoryAdapter.DataObjectHolder holder, final int position) {
            holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
            holder.fragment_ondemandlist_items.setText(arrayAdapter.get(position));
            if (isBackClicked) {


                if (mCategorySelectedItem.equals("")) {
                    if (position == mSubCategorySelectedItem) {

                        holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
                    } else {
                        holder.fragment_ondemandlist_items.setBackgroundResource(0);
                    }
                } else {
                    if (position == Integer.parseInt(mCategorySelectedItem)) {

                        holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
                    } else {
                        holder.fragment_ondemandlist_items.setBackgroundResource(0);
                    }
                }


            }


            holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isBackClicked = false;
                    mtoolbarcontent = arrayAdapter.get(position);
                    minimizeChannelView();
                    if (subCategoryAffix.equals("TV Decades")) {
                        try {
                            mChannelList = Constants.CHANNEL_DECADECONTENT;
                            mCategorySelectedItem = String.valueOf(position);
                            mCategorySelectedHeader = arrayAdapter.get(position);
                            toolbarTextChange("Channels", mtoolbarMaincontent, mCategorySelectedHeader, "");
                            JSONObject jsonObject = m_jsonSubCategories.getJSONObject(position);
                            String name = jsonObject.getString("decade");
                            new LoadingChannelTaskForDecadeChannel(true, name, 1).execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        fragment_ondemand_alllist_items.setVisibility(View.GONE);
                        try {
                            if (m_jsonSubCategories.length() <= position) {
                                mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                mSubCategorySelectedItem = position;
                                mtoolbarSubContent = arrayAdapter.get(position);
                                toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                new LoadingDecade(true, 1).execute();
                            } else {
                                JSONObject jsonObject = m_jsonSubCategories.getJSONObject(position);
                                int nID = jsonObject.getInt("id");
                                subCategoreis = getSubCategories(nID);
                                if (subCategoreis.length() == 0) {
                                    if (nID != 484 && nID != 482 && nID != 486) {
                                        mSubCategorySelectedItem = position;
                                        if (m_jsonSubCategories == temp) {
                                            mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                            mtoolbarSubContent = arrayAdapter.get(position);
                                            toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                        } else {
                                            mChannelList = Constants.CHANNEL_SUBCONTENT;
                                            mCategorySelectedHeader = arrayAdapter.get(position);
                                            toolbarTextChange("Channels", mtoolbarMaincontent, mCategorySelectedHeader, "");
                                        }

                                        loadChannel(nID);
                                    } else {
                                        mSubCategorySelectedItem = position;
                                        mCategorySelectedHeader = arrayAdapter.get(position);
                                        toolbarTextChange("Channels", mtoolbarMaincontent, mCategorySelectedHeader, "");
                                        mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                        showActorDialog(nID);
                                    }

                                } else {
                                    mSubCategorySelectedItem = position;
                                    mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                    m_jsonSubCategories = subCategoreis;
                                    mtoolbarSubContent = arrayAdapter.get(position);
                                    toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                    showSubCategoryDialog();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayAdapter.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);

                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);
            }
        }
    }

    private void showActorDialog(int nID) {
        nCategory = nID;

        spinner_layout.setVisibility(View.VISIBLE);
        arrow_layout.setVisibility(View.VISIBLE);
        arrayActorAdapterData.clear();
        arrayActorAdapterData.add("A");
        arrayActorAdapterData.add("B");
        arrayActorAdapterData.add("C");
        arrayActorAdapterData.add("D");
        arrayActorAdapterData.add("E");
        arrayActorAdapterData.add("F");
        arrayActorAdapterData.add("G");
        arrayActorAdapterData.add("H");
        arrayActorAdapterData.add("I");
        arrayActorAdapterData.add("J");
        arrayActorAdapterData.add("K");
        arrayActorAdapterData.add("L");
        arrayActorAdapterData.add("M");
        arrayActorAdapterData.add("N");
        arrayActorAdapterData.add("O");
        arrayActorAdapterData.add("P");
        arrayActorAdapterData.add("Q");
        arrayActorAdapterData.add("R");
        arrayActorAdapterData.add("S");
        arrayActorAdapterData.add("T");
        arrayActorAdapterData.add("U");
        arrayActorAdapterData.add("V");
        arrayActorAdapterData.add("W");
        arrayActorAdapterData.add("X");
        arrayActorAdapterData.add("Y");
        arrayActorAdapterData.add("Z");
        adapter = new ArrayAdapter<>(context, R.layout.spinnertext, arrayActorAdapterData);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_actors.setAdapter(adapter);

       /* adapterActor = new SpinnerAdapter(context, arrayActorAdapterData);
        spinner_actors.setAdapter(adapterActor);*/
        spinner_actors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                minimizeChannelView();
                fragment_ondemand_alllist_items.setVisibility(View.GONE);
                new LoadingChannelForActor(true, arrayActorAdapterData.get(position)).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadChannel(int nID) {
        nCategory = nID;
        new LoadingChannelTask(true).execute();
    }

    private class LoadingDecade extends AsyncTask<Object, Object, Object> {

        private boolean showDialog;
        private int nDecadeCategory;

        public LoadingDecade(boolean showDialog, int nDecadeCategory) {
            this.showDialog = showDialog;
            this.nDecadeCategory = nDecadeCategory;
        }

        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            try {
                progressBar_center.setVisibility(View.VISIBLE);
                /*if(showDialog) {
                    mProgressHUD = ProgressHUD.show(context, "Please Wait...", true, false, null);
                }*/
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            if (nCategory != -1)
                m_jsonSubCategories = JSONRPCAPI.getDecade(nDecadeCategory);
            if (m_jsonSubCategories == null) return null;
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                progressBar_center.setVisibility(View.GONE);
                if (m_jsonSubCategories != null) {
                    fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
                    subCategoryAffix = "TV Decades";
                    showDecadeDialog();
                }
                //progressLoad.setVisibility(View.GONE);
            } catch (Exception exception) {
                exception.printStackTrace();
            } /*finally {
                if (showDialog ){
                    mProgressHUD.dismiss();
                }
            }*/
        }
    }

    private void showDecadeDialog() {
        fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
        ArrayList<String> arrayAdapter = new ArrayList<>();
        for (int i = 0; i < m_jsonSubCategories.length(); i++) {
            try {
                JSONObject jsonObject = m_jsonSubCategories.getJSONObject(i);
                String strName = jsonObject.getString("decade");
                String count = "(" + jsonObject.getInt("channels_count") + ")";
                arrayAdapter.add(strName + count);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
        ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelSubCategoryAdapter(arrayAdapter, context);
        fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
    }

    private class LoadingChannelTask extends AsyncTask<Object, Object, Object> {

        private boolean showDialog;


        public LoadingChannelTask() {
        }

        public LoadingChannelTask(boolean showDialog) {
            this.showDialog = showDialog;
        }

        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            try {
                progressBar_center.setVisibility(View.VISIBLE);
                /*if(showDialog) {
                    mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
                }*/
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            if (nCategory != -1)
                m_jsonLiveItems = JSONRPCAPI.getChannels(nCategory);
            if (m_jsonLiveItems == null) return null;
            try {

                /*String IDs = "";
                mainActivity.m_channelSchedule.clear();
                for(int i = 0; i < m_jsonLiveItems.length(); i++){
                    JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                    IDs = obj.getInt("id") + "";
                    mainActivity.nTimeOffset = 0;
                    JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, 0, mainActivity.nTimeLimit);
                    if( objSchedule != null ){
                        ChannelModel model = new ChannelModel();
                        model.channelId = obj.getInt("id");
                        model.channelName = obj.getString("name");
                        model.channelLogo = obj.getString("big_logo_url");
                        model.channelVideos = objSchedule.getJSONObject(IDs).getJSONArray("videos").getJSONArray(0);
                        mainActivity.m_channelSchedule.add(model);


                    }
                }*/
                String IDs = "";
                m_channelSchedule.clear();
                for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                    JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                    if (IDs.length() == 0)
                        IDs = obj.getInt("id") + "";
                    else
                        IDs = IDs + "," + obj.getInt("id");
                }
                nTimeOffset = 0;
                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, 0, nTimeLimit);
                if (objSchedule == null) return null;
                if (objSchedule != null) {
                    for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                        JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                        IDs = obj.getInt("id") + "";
                        ChannelModel model = new ChannelModel();
                        model.channelId = obj.getInt("id");
                        model.channelName = obj.getString("name");
                        model.channelLogo = obj.getString("big_logo_url");
                        model.channelVideos = objSchedule.getJSONObject(IDs).getJSONArray("videos").getJSONArray(0);
                        m_channelSchedule.add(model);


                    }
                } else {
                    m_channelSchedule.clear();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                progressBar_center.setVisibility(View.GONE);

                if (m_jsonLiveItems == null) {
                    fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
                    makeChannelInterface();
                    Log.d("reload", "makeChannelInterface");
                } else {
                    if (m_jsonLiveItems.length() != 0) {
                        fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
                        AlertChannelDialogListAdapter adapter = new AlertChannelDialogListAdapter(context, m_jsonLiveItems);
                        fragment_ondemand_alllist_items.setAdapter(adapter);
                    }
                }


                //progressLoad.setVisibility(View.GONE);
            } catch (Exception exception) {
                exception.printStackTrace();
            } /*finally {
                if (showDialog ){
                    mProgressHUD.dismiss();
                }
            }*/
        }
    }


    private class LoadingChannelForActor extends AsyncTask<Object, Object, Object> {

        private boolean showDialog;
        private String filter;

        public LoadingChannelForActor() {
        }

        public LoadingChannelForActor(boolean showDialog, String filter) {
            this.showDialog = showDialog;
            this.filter = filter;
        }

        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            try {
                progressBar_center.setVisibility(View.VISIBLE);
                /*if(showDialog) {
                    mProgressHUD = ProgressHUD.show(context, "Please Wait...", true, false, null);
                }*/
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            if (nCategory != -1)
                m_jsonLiveItems = JSONRPCAPI.getChannelsForActor(nCategory, filter);
            if (m_jsonLiveItems == null) return null;
            try {

                String IDs = "";
                m_channelSchedule.clear();
                for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                    JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                    if (IDs.length() == 0)
                        IDs = obj.getInt("id") + "";
                    else
                        IDs = IDs + "," + obj.getInt("id");
                }
                nTimeOffset = 0;
                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, 0, nTimeLimit);
                if (objSchedule == null) return null;
                if (objSchedule != null) {
                    for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                        JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                        IDs = obj.getInt("id") + "";
                        ChannelModel model = new ChannelModel();
                        model.channelId = obj.getInt("id");
                        model.channelName = obj.getString("name");
                        model.channelLogo = obj.getString("big_logo_url");
                        if (model.channelLogo.length() == 0) {
                            model.channelLogo = obj.getString("logo_url");
                        }
                        if (model.channelLogo.startsWith("http") == false) {
                            model.channelLogo = "https:" + model.channelLogo;
                        }
                        model.channelVideos = objSchedule.getJSONObject(IDs).getJSONArray("videos").getJSONArray(0);
                        m_channelSchedule.add(model);
                    }
                } else {
                    m_channelSchedule.clear();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                progressBar_center.setVisibility(View.GONE);
                fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
                if (m_jsonLiveItems.length() != 0) {
                    AlertChannelDialogListAdapter adapter = new AlertChannelDialogListAdapter(context, m_jsonLiveItems);
                    fragment_ondemand_alllist_items.setAdapter(adapter);
                }


                //AlertDailogListAdapter adapter = new AlertDailogListAdapter(mainActivity, arrayAdapter);
                //listActor.setAdapter(adapter);


            } catch (Exception exception) {
                exception.printStackTrace();
            } /*finally {
                if (showDialog ){
                    mProgressHUD.dismiss();
                }
            }*/
        }
    }

    private class AlertChannelDialogListAdapter extends RecyclerView.Adapter<AlertChannelDialogListAdapter.DataObjectHolder> {
        private Context m_context;
        private JSONArray m_data;
        int mPosition;

        public AlertChannelDialogListAdapter(Context context, JSONArray m_jsonLiveItems) {
            this.m_context = context;
            this.m_data = m_jsonLiveItems;
            text_font_typeface();
        }

        @Override
        public AlertChannelDialogListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.channel_actorlist_layout, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final AlertChannelDialogListAdapter.DataObjectHolder holder, final int position) {
            holder.fragment_channel_items.setTypeface(OpenSans_Regular);


            holder.fragment_channel_items_imageThumbnail.setVisibility(View.GONE);
            String strCategory = null;
            String strUrl = "";
            if (m_data != null) {

                try {
                    JSONObject jObj = m_data.getJSONObject(position);
                    strCategory = jObj.getString("name");
                    strUrl = jObj.getString("big_logo_url");
                    if (strUrl.equals("null")) {
                        strUrl = jObj.getString("logo_url");
                        if (strUrl != null && strUrl.startsWith("http") == false) {
                            strUrl = "https:" + strUrl;
                        }
                    }
                    Log.e("ERRORLOG", strCategory);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            holder.fragment_channel_items.setText(strCategory);

//            Picasso.with(m_context).load(strUrl).into(holder.fragment_channel_items_imageThumbnail);
            holder.fragment_channel_items_imageThumbnail.loadImage(strUrl);
            Log.e("strUrl", strUrl);
            final String finalStrCategory = strCategory;

            if (position == mPosition) {

                holder.fragment_channel_items.setBackgroundResource(R.drawable.menubg);
            } else {
                holder.fragment_channel_items.setBackgroundResource(0);
            }


            holder.fragment_channel_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mPosition = position;

                        JSONObject jsonObject = m_jsonLiveItems.getJSONObject(position);
                        minimizeChannelView();
                        int nID = jsonObject.getInt("id");
                        String strName = jsonObject.getString("name");

                        if (position < m_channelSchedule.size()) {
                            ChannelModel model = m_channelSchedule.get(position);
                            m_channelSchedule.remove(position);
                            m_channelSchedule.add(0, model);
                        }

                        setLiveData(nID, strName);
                        mtoolbarlastcontent = finalStrCategory;
                        toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, mtoolbarlastcontent);
                        /*if(m_jsonLiveItems!=null)
                        {
                            AlertChannelDialogListAdapter adapter = new AlertChannelDialogListAdapter(context, m_jsonLiveItems);
                            fragment_ondemand_alllist_items.setAdapter(adapter);
                        }*/

                        holder.fragment_channel_items.setBackgroundResource(R.drawable.menubg);
                        notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return m_data.length();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_channel_items_layout;
            TextView fragment_channel_items;
            DynamicImageView fragment_channel_items_imageThumbnail;

            public DataObjectHolder(View itemView) {
                super(itemView);

                fragment_channel_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_channel_items_layout);
                fragment_channel_items = (TextView) itemView.findViewById(R.id.fragment_channel_items);
                fragment_channel_items_imageThumbnail = (DynamicImageView) itemView.findViewById(R.id.fragment_channel_items_imageThumbnail);
            }
        }
    }

    private class LoadingChannelTaskForDecadeChannel extends AsyncTask<Object, Object, Object> {

        private boolean showDialog;
        private String decade;
        private int nDecadeCategory;

        public LoadingChannelTaskForDecadeChannel(boolean showDialog, String decade, int nDecadeCategory) {
            this.showDialog = showDialog;
            this.decade = decade;
            this.nDecadeCategory = nDecadeCategory;
        }

        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            fragment_ondemand_alllist_items.setVisibility(View.GONE);
            progressBar_center.setVisibility(View.VISIBLE);
            /*try {
                if(showDialog) {
                    mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }*/
        }

        @Override
        protected Object doInBackground(Object... params) {
            if (nCategory != -1)
                m_jsonLiveItems = JSONRPCAPI.getChannelsByDecade(decade, nDecadeCategory);
            if (m_jsonLiveItems == null) return null;
            try {

               /* String IDs = "";
                mainActivity.m_channelSchedule.clear();
                for(int i = 0; i < m_jsonLiveItems.length(); i++){
                    JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                    IDs = obj.getInt("id") + "";
                    mainActivity.nTimeOffset = 0;
                    JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, 0, mainActivity.nTimeLimit);
                    if( objSchedule != null ){
                        ChannelModel model = new ChannelModel();
                        model.channelId = obj.getInt("id");
                        model.channelName = obj.getString("name");
                        model.channelLogo = obj.getString("big_logo_url");
                        model.channelVideos = objSchedule.getJSONObject(IDs).getJSONArray("videos").getJSONArray(0);
                        mainActivity.m_channelSchedule.add(model);
                    }
                }*/
                String IDs = "";
                m_channelSchedule.clear();
                for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                    JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                    if (IDs.length() == 0)
                        IDs = obj.getInt("id") + "";
                    else
                        IDs = IDs + "," + obj.getInt("id");
                }
                nTimeOffset = 0;
                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, 0, nTimeLimit);
                if (objSchedule == null) return null;
                if (objSchedule != null) {
                    for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                        JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                        IDs = obj.getInt("id") + "";
                        ChannelModel model = new ChannelModel();
                        model.channelId = obj.getInt("id");
                        model.channelName = obj.getString("name");
                        model.channelLogo = obj.getString("big_logo_url");
                        model.channelVideos = objSchedule.getJSONObject(IDs).getJSONArray("videos").getJSONArray(0);
                        m_channelSchedule.add(model);


                    }
                } else {
                    m_channelSchedule.clear();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                progressBar_center.setVisibility(View.GONE);
//                showLiveAlertDialog();
                fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);

                if (m_jsonLiveItems.length() != 0) {
                    AlertChannelDialogListAdapter adapter = new AlertChannelDialogListAdapter(context, m_jsonLiveItems);
                    //adapter.setBlack(false);
                    fragment_ondemand_alllist_items.setAdapter(adapter);
                }

                //progressLoad.setVisibility(View.GONE);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

   /* public class SpinnerAdapter extends BaseAdapter {

        private Context m_context;
        private ArrayList<String> m_data;
        private LayoutInflater m_inflater;

        public SpinnerAdapter(Context ctx, ArrayList<String> data) {

            m_context = ctx;
            m_data = data;
            m_inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return m_data.size();
        }

        @Override
        public Object getItem(int position) {
            return m_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                row = m_inflater.inflate(R.layout.spinnertext, parent, false);
            }

            String strCategory = null;

            strCategory = m_data.get(position);

            CheckedTextView textCategory = (CheckedTextView) row.findViewById(R.id.spinnertext);
            textCategory.setText(strCategory);

            return row;
        }
    }*/

    private void text_font_typeface() {
        try {
            OpenSans_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_1.ttf");
            OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_1.ttf");
            OpenSans_Semibold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold_1.ttf");
            osl_ttf = Typeface.createFromAsset(getAssets(), "fonts/osl.ttf");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void toolbarTextChange(final String mtoolbarGridContent, final String mtoolbarMaincontent, final String mtoolbarSubContent, final String mtoolbarlastcontent) {


        if (!mtoolbarGridContent.equalsIgnoreCase("") && mtoolbarMaincontent.equalsIgnoreCase("") && mtoolbarSubContent.equalsIgnoreCase("") && mtoolbarlastcontent.equals("")) {
            activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
            activity_homescreen_toolbar_ondemand_text.setVisibility(View.GONE);
            activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
            activity_homescreen_toolbar_textview.setText("Channels");
            activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.text_blue));
            activity_homescreen_toolbar_mail.setVisibility(View.GONE);
            activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
            activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
            activity_homescreen_maincontent_divider.setVisibility(View.GONE);
            activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
            activity_channel_final_list_divider.setVisibility(View.GONE);
            activity_channel_final_list_text.setVisibility(View.GONE);
        } else if (!mtoolbarGridContent.equalsIgnoreCase("") && !mtoolbarMaincontent.equalsIgnoreCase("") && mtoolbarSubContent.equalsIgnoreCase("") && mtoolbarlastcontent.equals("")) {

            try {
                activity_homescreen_toolbar_ondemand_text.setText(mtoolbarGridContent);
                activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_ondemand_text.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_textview.setText(Utilities.stripHtml(mtoolbarMaincontent));
                activity_homescreen_toolbar_maincontent.setText(Utilities.stripHtml(mtoolbarSubContent));
                activity_homescreen_toolbar_ondemand_text.setTextColor(getResources().getColor(R.color.white));
                activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.white));
                activity_homescreen_toolbar_maincontent.setTextColor(getResources().getColor(R.color.white));
                activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                activity_channel_final_list_divider.setVisibility(View.GONE);
                activity_channel_final_list_text.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!mtoolbarGridContent.equalsIgnoreCase("") && !mtoolbarMaincontent.equalsIgnoreCase("") && !mtoolbarSubContent.equalsIgnoreCase("") && mtoolbarlastcontent.equals("")) {
            try {
                activity_homescreen_maincontent_divider.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_maincontent.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_ondemand_text.setText(mtoolbarGridContent);
                activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_ondemand_text.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_textview.setText(Utilities.stripHtml(mtoolbarMaincontent));
                activity_homescreen_toolbar_maincontent.setText(Utilities.stripHtml(mtoolbarSubContent));
                activity_homescreen_toolbar_ondemand_text.setTextColor(getResources().getColor(R.color.white));
                activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.white));
                activity_homescreen_toolbar_maincontent.setTextColor(getResources().getColor(R.color.white));
                activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                activity_channel_final_list_divider.setVisibility(View.GONE);
                activity_channel_final_list_text.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!mtoolbarGridContent.equalsIgnoreCase("") && !mtoolbarMaincontent.equalsIgnoreCase("") && !mtoolbarSubContent.equalsIgnoreCase("") && !mtoolbarlastcontent.equals("")) {

            try {
                activity_homescreen_maincontent_divider.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_maincontent.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_ondemand_text.setText(mtoolbarGridContent);
                activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_ondemand_text.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_textview.setText(Utilities.stripHtml(mtoolbarMaincontent));
                activity_homescreen_toolbar_maincontent.setText(Utilities.stripHtml(mtoolbarSubContent));
                activity_channel_final_list_text.setText(Utilities.stripHtml(mtoolbarlastcontent));
                activity_homescreen_toolbar_ondemand_text.setTextColor(getResources().getColor(R.color.white));
                activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.white));
                activity_homescreen_toolbar_maincontent.setTextColor(getResources().getColor(R.color.white));
                activity_channel_final_list_text.setTextColor(getResources().getColor(R.color.white));
                activity_channel_final_list_divider.setVisibility(View.VISIBLE);
                activity_channel_final_list_text.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        activity_homescreen_toolbar_ondemand_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarTextChange("Channels", "", "", "");
                mChannelList = "";
                spinner_layout.setVisibility(View.GONE);
                arrow_layout.setVisibility(View.GONE);
                makeChannelInterface();
            }
        });


        activity_homescreen_toolbar_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mtoolbarSubContent.equals("") && !mtoolbarMaincontent.equals(mtoolbarGridContent)) {
                    try {
                        mChannelList = Constants.CHANNEL_MAINCONTENT;
                        spinner_layout.setVisibility(View.GONE);
                        arrow_layout.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = m_jsonArrayParentCategories.getJSONObject(mMainCategorySelectedItem);
                            int nID = jsonObject.getInt("id");
                            temp = getSubCategories(nID);
                            if (temp.length() == 0) {
                                toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                loadChannel(nID);
                            } else {

                                if (mtoolbarMaincontent.equals("TV")) {
                                    subCategoryAffix = "TV";
                                }
                                toolbarTextChange("Channels", mtoolbarMaincontent, "", "");
                                mCategorySelectedItem = "";
                                m_jsonSubCategories = temp;
                                showSubCategoryDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        });

        /*activity_homescreen_toolbar_maincontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subCategoryAffix.equals("TV Decades"))
                {
                    try {
                        spinner_layout.setVisibility(View.GONE);
                        arrow_layout.setVisibility(View.GONE);
                        mChannelList = Constants.CHANNEL_SUBCATEGORY;
                        toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent);
                        new LoadingDecade(true, 1).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {

                }

            }
        });*/


    }



}
