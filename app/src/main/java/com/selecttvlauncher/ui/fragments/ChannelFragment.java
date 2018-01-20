package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.RemoteMediaPlayer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.images.WebImage;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.selecttvlauncher.Adapter.GridAdapter;
import com.selecttvlauncher.Adapter.JSONAdapter;
import com.selecttvlauncher.Adapter.Video;
import com.selecttvlauncher.BeanClass.ChannelCategoryBean;
import com.selecttvlauncher.BeanClass.ChannelModel;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.dialogs.OnDemandDialog;
import com.selecttvlauncher.ui.dialogs.ProgressHUD;
import com.selecttvlauncher.ui.views.DynamicImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;


public class ChannelFragment extends Fragment implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaylistEventListener, GridAdapter.onGridSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static String mtoolbarMaincontent = "", mtoolbarSubContent = "", mtoolbarcontent = "", mtoolbarlastcontent = "";
    public static Boolean isBackClicked = false;
    private TextView txt_splash_title, txt_splash_progress, allchannel_textView, browse_textView;
    private RoundCornerProgressBar splash_progress;
    public String szSplashTitle = "";
    public Boolean szSplashDisplay;
    public int nBackgroundLoading = 0;
    public float fPos = 0;
    public float progress = 0;
    private JSONArray m_jsonArrayCategories;
    public static JSONArray m_jsonArrayParentCategories, m_jsonArrayStreams;
    public static JSONArray m_jsonArrayChannels;
    private int nCategory = 0;
    private JSONArray m_jsonLiveItems;
    public ArrayList<ChannelModel> m_channelSchedule = new ArrayList<>();
    public int nTimeOffset = 0;
    public int nTimeLimit = 600;
    private int nChannelID = -1;
    private RelativeLayout linearFullScreen;
    private LinearLayout content_layout;

    int mMainCategorySelectedItem = 0;
    int mSubCategorySelectedItem = 0;
    int mSubContentSelectedItem = 0;
    public static JSONArray m_jsonArrayStreamsForCasting = new JSONArray();


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
    ProgressBar bottom_progress;
    TextView bottom_textview;
    TextView left_loadingchannel_textview;
    ScrollView listChannelTitle, overall_scroll;
    LinearLayout linearScrollChannelTitle;
    ScrollView listChannelDesc;
    LinearLayout linearScrollChannelDesc;
    ImageView imgchannelupdown;
    RelativeLayout layoutchannelupdown;
    boolean bChannelUp = true;
    public boolean bNoActive = true;
    boolean bTouchChannelDesc = false;
    View firstRow;
    View lastRow;
    ImageView imgScrollNext, imgScrollPrev;
    private boolean bExistChannel = false;
    private ArrayList<ChannelCategoryBean> m_channelViews = new ArrayList<>();
    private RecyclerView fragment_ondemand_alllist_items;
    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private String subCategoryAffix = "";
    public static ImageView channel_prev_button;
    public static String mChannelList;
    ArrayList<String> arrayActorAdapterData = new ArrayList<>();
    public static RelativeLayout spinner_layout;
    Spinner spinner_actors;
    public static RelativeLayout arrow_layout;
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
    public static JSONArray temp;
    private JSONArray subCategoreis;
    private ImageView activity_channel_final_list_divider;
    private TextView activity_channel_final_list_text;

    long nMinStartTime = 0;
    int nMaxWidth = 0;
    int loadingtimeOffset = 0;
    private YouTubePlayer YPlayer;
    private LinearLayout layoutchannel;
    private Timer timer;
    private LayoutInflater inflate;
    private int spacing;
    private int width;
    private int height;
    private LinearLayout dynamic_horizontalViews_layout;
    private RelativeLayout layout_channelviewby_list;
    private RelativeLayout layout_all_channelview;
    private int horizontal_position = 0;
    private int horizontal_prevposition = 0;
    private RecyclerView horizontal_channel_list;
    private AllChannelAdapter allChannelAdapter;
    private ProgressBar horizontal_channel_list_progressBar;
    Boolean aBoolean = true;
    private TextView horizontal_listview_item_count;
    private HorizontalScrollView horizontal_listview;
    private AllChannelListAdapter horiadpater;
    private AlertChannelDialogListAdapter alertChannelDialogListAdapter;
    private LinearLayout recycler_layout;
    private LinearLayout image_view;
    private ImageView left_slide;
    private ImageView right_slide;
    private ImageView horizontal_listview_channel_close;
    private String type = "";
    String arra_list = "";
    public static JSONArray m_jsonArrayChannelVideos;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    public static JSONArray mSearch_Trailors;

    private int horizontalListdisplayCount = 0;

    private OnDemandDialog demanddialog;
    private boolean isYoutubeFullscreen = false;
    private boolean channelSelected = false;
    Dialog dialog;


    private static final String APP_ID = CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouter.Callback mMediaRouterCallback;
    private CastDevice mSelectedDevice;
    private int mRouteCount = 0;
    private GoogleApiClient mApiClient;
    private Cast.Listener mCastListener;
    private RemoteMediaPlayer mMediaChannel;
    private boolean mApplicationStarted;
    private boolean mWaitingForReconnect;
    private String mSessionId;
    private ArrayList<String> mRouteNames = new ArrayList<String>();
    private ArrayList<String> primetimeSpinnerArray = new ArrayList<String>();
    private final ArrayList<MediaRouter.RouteInfo> mRouteInfos = new ArrayList<MediaRouter.RouteInfo>();

    private ConnectionCallbacks mConnectionCallbacks;
    private ConnectionFailedListener mConnectionFailedListener;

    private int nStreamingId = 0;

    private YouTubePlayer.PlaylistEventListener mPlaylistEventListener;
    private int cast=0;
    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    private int scroll_offset=0;
    private boolean firstload=true;
    private boolean firstloadvideo=true;
    ViewGroup.LayoutParams layoutParams,layoutParams1 ;
    long nPrevStartTime;
    int m_nVolume = 0;
    int nTempWidth=0;
    private boolean loadpage=false;



    public static ChannelFragment newInstance(String param1, String param2) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChannelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
            arra_list = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ((HomeActivity) getActivity()).toolbarTextChange("Channels", "", "", "");
        layout_channelviewby_list = (RelativeLayout) view.findViewById(R.id.layout_channelviewby_list);
        bottom_progress = (ProgressBar) view.findViewById(R.id.bottom_progress);
        left_loadingchannel_textview = (TextView) view.findViewById(R.id.left_loadingchannel_textview);
        bottom_textview = (TextView) view.findViewById(R.id.bottom_textview);

//chromecast
        initCast();
        m_nVolume = getVolume();



        spinner_layout = (RelativeLayout) view.findViewById(R.id.spinner_layout);
        arrow_layout = (RelativeLayout) view.findViewById(R.id.arrow_layout);
        list_downscroll_arrow = (ImageView) view.findViewById(R.id.list_downscroll_arrow);
        spinner_actors = (Spinner) view.findViewById(R.id.spinner_actors);
        channel_prev_button = (ImageView) view.findViewById(R.id.channel_prev_button);
        fragment_ondemand_alllist_items = (RecyclerView) view.findViewById(R.id.fragment_ondemand_alllist_items);
        txt_splash_title = (TextView) view.findViewById(R.id.txt_splash_title);
        txt_splash_progress = (TextView) view.findViewById(R.id.txt_splash_progress);
        splash_progress = (RoundCornerProgressBar) view.findViewById(R.id.splash_progress);
        linearFullScreen = (RelativeLayout) view.findViewById(R.id.linearFullScreen);
        content_layout = (LinearLayout) view.findViewById(R.id.content_layout);
        scroll_arrow_layout = (LinearLayout) view.findViewById(R.id.scroll_arrow_layout);

        txt_splash_title = (TextView) view.findViewById(R.id.txt_splash_title);
        txt_splash_progress = (TextView) view.findViewById(R.id.txt_splash_progress);
        channel_tab_textview = (TextView) view.findViewById(R.id.channel_tab_textview);
        show_tab_textview = (TextView) view.findViewById(R.id.show_tab_textview);
        browse_textView = (TextView) view.findViewById(R.id.browse_textView);

        dialog = new Dialog(getActivity(), R.style.MY_DIALOG);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.browse_channel_dialog);
        dialog.setCancelable(false);

        dynamic_horizontalViews_layout = (LinearLayout) dialog.findViewById(R.id.dynamic_horizontalViews_layout);
        layout_all_channelview = (RelativeLayout) dialog.findViewById(R.id.layout_all_channelview);
        allchannel_textView = (TextView) view.findViewById(R.id.allchannel_textView);
        horizontal_listview_item_count = (TextView) dialog.findViewById(R.id.horizontal_listview_item_count);
        recycler_layout = (LinearLayout) dialog.findViewById(R.id.recycler_layout);
        image_view = (LinearLayout) dialog.findViewById(R.id.image_view);
        left_slide = (ImageView) dialog.findViewById(R.id.left_slide);
        right_slide = (ImageView) dialog.findViewById(R.id.right_slide);
        horizontal_listview_channel_close = (ImageView) dialog.findViewById(R.id.horizontal_listview_channel_close);
        horizontal_channel_list = (RecyclerView) dialog.findViewById(R.id.horizontal_channel_list);
        horizontal_channel_list_progressBar = (ProgressBar) dialog.findViewById(R.id.horizontal_channel_list_progressBar);
        horizontal_listview = (HorizontalScrollView) dialog.findViewById(R.id.horizontal_listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        horizontal_channel_list.setLayoutManager(gridLayoutManager);


        text_font_typeface();
        txt_splash_title.setTypeface(OpenSans_Regular);
        txt_splash_progress.setTypeface(OpenSans_Regular);
        channel_tab_textview.setTypeface(OpenSans_Regular);
        show_tab_textview.setTypeface(OpenSans_Regular);
        allchannel_textView.setTypeface(OpenSans_Regular);
        browse_textView.setTypeface(OpenSans_Bold);


        splash_progress = (RoundCornerProgressBar) view.findViewById(R.id.splash_progress);
        linearFullScreen = (RelativeLayout) view.findViewById(R.id.linearFullScreen);
        content_layout = (LinearLayout) view.findViewById(R.id.content_layout);

        progressBar_center = (ProgressBar) view.findViewById(R.id.progressBar_center);
        listStreams = (ListView) view.findViewById(R.id.listStreams);

//        linearFullScreen.setVisibility(View.VISIBLE);
     /*   szSplashDisplay = true;
        ((HomeActivity) getActivity()).mChannelProgress(szSplashDisplay, szSplashTitle, "", 0);
        content_layout.setVisibility(View.GONE);*/
        frameMenu = (FrameLayout) view.findViewById(R.id.frameMenu);

        linearShowTab = (LinearLayout) view.findViewById(R.id.linearShowTab);
        linearChannelTab = (LinearLayout) view.findViewById(R.id.linearChannelTab);
        channelTabContent = (HorizontalScrollView) view.findViewById(R.id.channelTabContent);

        overall_scroll = (ScrollView) view.findViewById(R.id.overall_scroll);
        listChannelTitle = (ScrollView) view.findViewById(R.id.listChannelTitle);
        linearScrollChannelTitle = (LinearLayout) view.findViewById(R.id.linearscrollChannelTitle);
        listChannelDesc = (ScrollView) view.findViewById(R.id.listChannelDesc);
        linearScrollChannelDesc = (LinearLayout) view.findViewById(R.id.linearscrollChannelDesc);
        linearSubMainRight = (LinearLayout) view.findViewById(R.id.linearSubMainRight);

        imgchannelupdown = (ImageView) view.findViewById(R.id.imgchannelupdown);
        layoutchannelupdown = (RelativeLayout) view.findViewById(R.id.layoutchannelupdown);
        layoutchannel = (LinearLayout) view.findViewById(R.id.layoutchannel);
        imgScrollNext = (ImageView) view.findViewById(R.id.imgscrollnext);
        imgScrollPrev = (ImageView) view.findViewById(R.id.imgscrollprev);


        imgScrollNext = (ImageView) view.findViewById(R.id.imgscrollnext);
        imgScrollPrev = (ImageView) view.findViewById(R.id.imgscrollprev);
        mLayoutManager = new LinearLayoutManager(getActivity());
        fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
        fragment_ondemand_alllist_items.hasFixedSize();


        ((HomeActivity) getActivity()).setmChannelFragment(this);
        Utilities.setViewFocus(getActivity(), imgScrollNext);
        Utilities.setViewFocus(getActivity(), imgScrollPrev);
        Utilities.setViewFocus(getActivity(), imgchannelupdown);
        Utilities.setViewFocus(getActivity(), linearShowTab);
        Utilities.setViewFocus(getActivity(), linearChannelTab);
        Utilities.setViewFocus(getActivity(), channel_prev_button);
        Utilities.setTextFocus(getActivity(), layoutchannel);

        imgScrollNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if(channelTabContent.getScrollX()%channelTabContent.getMeasuredWidth()==0){
                try {
                    channelTabContent.setScrollX(channelTabContent.getScrollX() + channelTabContent.getMeasuredWidth() / 2);
                    addtoChannelDescList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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


        layoutchannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channelSelected = false;
               /* ((HomeActivity) getActivity()).mHidetoolbar(true);
                layout_channelviewby_list.setVisibility(View.GONE);
                layout_all_channelview.setVisibility(View.VISIBLE);*/
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                image_view.removeAllViews();
//                addlayouts(dynamic_horizontalViews_layout, m_channelViews);
                horizontal_position=mMainCategorySelectedItem;
                addhorizontalScrolllayouts(dynamic_horizontalViews_layout, m_channelViews,horizontal_position);
                dialog.show();


            }
        });


        imgchannelupdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = 2.9f;
                if (checkIsTablet()) {
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
                } else {
                    if (bChannelUp == false) {
                        imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowdown));
                        weight = 1.0f;
                    } else {
                        imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowup));
                        weight = 1.8f;
                    }
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearSubMainRight.getLayoutParams();
                    params.weight = weight;
                    linearSubMainRight.setLayoutParams(params);
                    bNoActive = false;
                    bChannelUp = !bChannelUp;
                }


                /*if( player != null )
                    player.play();*/
            }
        });

        linearShowTab.setOnClickListener(m_ShowTabClickListener);
        linearChannelTab.setOnClickListener(m_ChannelTabClickListener);

        szSplashTitle = "Loading Data";
//        ((HomeActivity) getActivity()).mChannelProgress(true, szSplashTitle, "", 0);

        /*splash_progress.setProgressColor(Color.parseColor("#fce623"));
        splash_progress.setBackgroundColor(Color.parseColor("#1C86EE"));
        splash_progress.setProgressBackgroundColor(Color.parseColor("#cecece"));
        splash_progress.setRadius(8);
        splash_progress.setMax(100);
        splash_progress.setProgress(0);*/


        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

        /*youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {


                    YPlayer = youTubePlayer;
                    YPlayer.setFullscreen(true);
                    YPlayer.loadVideo("2zNSgSzhBfM");
                    YPlayer.play();

                    YPlayer = player;
                    YPlayer.setPlaylistEventListener(this);
                    YPlayer = player;

                    //player.loadVideo(video_url);

                    // Hiding player controls
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    YPlayer.setPlayerStateChangeListener(playerStateChangeListener);
                    YPlayer.setPlaybackEventListener(playbackEventListener);

                    try {
                        run_menu = new Runnable() {
                            public void run() {
                                if (YPlayer != null && YPlayer.isPlaying()) {

                                    YPlayer.setFullscreen(true);
                                }
                                menu_handler.removeCallbacks(run_menu);

                            }
                        };
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                // TODO Auto-generated method stub

            }
        });*/


        youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, this);
        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();
        mPlaylistEventListener=new MyPlaylistEventListener();

        loadCategoriesAndShow();
        showBackIcon(false);
        channel_prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBackClicked = true;
                try {
                    if(mChannelList!=null&&mChannelList.length()>0){
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
                                subCategoryAffix = "";
                                ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
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
                                        ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                        aBoolean = true;
                                        loadChannel(nID);
                                    } else {

                                        if (mtoolbarMaincontent.equals("TV")) {
                                            ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, "", "");

                                            subCategoryAffix = "TV";
                                        }

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
                                ((HomeActivity) getActivity()).toolbarTextChange("Channels", "", "", "");
                                showBackIcon(false);
                                mChannelList = "";
                                spinner_layout.setVisibility(View.GONE);
                                arrow_layout.setVisibility(View.GONE);
                                makeChannelInterface();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                HomeActivity.isPayperview = "";
                                ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                                fragmentTransaction.commit();
                            /*Intent in = new Intent(ChannelsActivity.this, HomeActivity.class);
                            startActivity(in);
                            finish();*/
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }else{
                        HomeActivity.isPayperview = "";
                        ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                        fragmentTransaction.commit();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
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

        channelTabContent.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = channelTabContent.getScrollY(); // For ScrollView
                int scrollX = channelTabContent.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
                Log.d("scroll:::",":::::scroll"+scrollX+":::"+scrollY);

                if(lastRow!=null){


                    final LinearLayout linear = (LinearLayout) lastRow.findViewById(R.id.item_channel_desc);
                    Log.d("linear.getChildCount()", ":::" + linear.getChildCount());
                    final View childItem = linear.getChildAt(linear.getChildCount()-1);
                    final View childItem1 = linear.getChildAt(linear.getChildCount()-2);
                    Log.d("getChildAt:::",":::::getChildAt"+childItem.getX());
                    int lastchidpos=(int)childItem.getX();
                    int prevlastchidpos=(int)childItem1.getX();
                    int i=1;
                    if(scrollX>=prevlastchidpos&&scrollX<=lastchidpos){
                        if(i==1){
                            if(loadpage){
                                new PaginationLoadingTask().execute();
                            }else{
                                addtoChannelDescList();
                            }

                            i++;
                        }

                    }
                }

            }
        });

        return view;
    }

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
                if (HomeActivity.channelID == 1) {
                    if (strid == 1) {
                        mMainCategorySelectedItem = i;
                    }
                }
            }


            ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter(m_channelViews, getActivity());
            fragment_ondemand_alllist_items.setAdapter(channelCategoryAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void loadChannel(int nID) {
        Log.d("SelectedID2::","::::loadChannel"+nID);
        nCategory = nID;
        new LoadingChannelTask(true).execute();
    }

    private JSONArray getSubCategories(int categoryID) {
        subCategoreis = new JSONArray();
        for (int i = 0; i < HomeActivity.m_jsonArrayCategories.length(); i++) {
            try {
                JSONObject objItem = HomeActivity.m_jsonArrayCategories.getJSONObject(i);
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
        ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelSubCategoryAdapter(arrayAdapter, getActivity());
        fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
    }


    private void loadCategoriesAndShow() {


        try {
            if (HomeActivity.m_jsonArrayCategories != null&& HomeActivity.m_jsonArrayCategories.length()>0 ) {

                m_jsonArrayParentCategories = new JSONArray();
                for (int i = 0; i < HomeActivity.m_jsonArrayCategories.length(); i++) {
                    try {
                        JSONObject objItem = HomeActivity.m_jsonArrayCategories.getJSONObject(i);

                        boolean b = objItem.isNull("parent_id");
                        if (b) {
                            if (!objItem.getString("id").equals("0")) {
                                m_jsonArrayParentCategories.put(objItem);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                mChannelList = "";
                makeChannelInterface();

                showFirView();

            } else {
                nBackgroundLoading = 0;
                szSplashTitle = "Loading Channel Categories...";
                txt_splash_title.setText(szSplashTitle);
                ((HomeActivity) getActivity()).mChannelProgress(true, szSplashTitle, (int) fPos + "%", progress);
                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                    new LoadingCategoryTask().execute();
                }else{
                    try {
                        ((HomeActivity) getActivity()).mChannelProgress(false, szSplashTitle, (int) fPos + "%", progress);
                        //setMainViewData(HomeActivity.m_jsonLiveItems, 0);
                        Toast.makeText(getActivity(),"No Network",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

        linearShowTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_selecetd_bg));
        linearChannelTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_title_header_bg));

        show_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        channel_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.channels_unselecetd_texcolor));

    }

    public void onClickChannelTab() {
        scroll_arrow_layout.setVisibility(View.VISIBLE);
        frameMenu.setVisibility(View.GONE);
        channelTabContent.setVisibility(View.VISIBLE);
        listChannelTitle.setVisibility(View.VISIBLE);
        overall_scroll.setVisibility(View.VISIBLE);

        linearChannelTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_selecetd_bg));
        linearShowTab.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.channels_title_header_bg));

        channel_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        show_tab_textview.setTextColor(ContextCompat.getColor(getActivity(), R.color.channels_unselecetd_texcolor));
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

    public void removePrevChannel() {
        if (m_jsonArrayChannelVideos == null) return;
        if (m_jsonArrayChannelVideos.length() == 0) return;
        JSONArray retArray = new JSONArray();
        for (int i = 1; i < m_jsonArrayChannelVideos.length(); i++) {
            try {
                JSONObject jsonObject = m_jsonArrayChannelVideos.getJSONObject(i);
                retArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        m_jsonArrayChannelVideos = retArray;
    }

    public void checkYoutubeFullscreen() {
        try {
            if (isYoutubeFullscreen) {
                if (player != null) {
                    player.setFullscreen(false);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void watchvideo(String strUrl, String strTitle) {
        try {
            if (mApiClient != null || mMediaChannel != null) {
                castVideoFromVideoId(strUrl,strTitle);
            }else{
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                        getActivity(), Constants.DEVELOPER_KEY, strUrl, 0, true, false);
                if (intent != null) {
                    if (canResolveIntent(intent)) {
                        getActivity().startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                    } else {
                        // Could not resolve the intent - must need to install or update the YouTube API service.
                        YouTubeInitializationResult.SERVICE_MISSING
                                .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
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


if(!firstloadvideo){
    addtoChannelDescList();

}
            firstloadvideo=false;

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
            Log.d("youtube:::", "::::::error" + errorReason.name());
        }
    }

    private final class MyPlaylistEventListener implements YouTubePlayer.PlaylistEventListener{

        @Override
        public void onPrevious() {
            try {
                Log.d("Youtube::", "prevoius selected");
                nStreamingId--;
                if (m_jsonArrayStreams!= null) {
                    try {
                        m_jsonArrayStreamsForCasting = new JSONArray(m_jsonArrayStreams.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                castVideoFromId(nStreamingId);
                cast=0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNext() {
            try {
                Log.d("Youtube::", "next selected");
                nStreamingId++;
                if (m_jsonArrayStreams!= null) {
                    try {
                        m_jsonArrayStreamsForCasting = new JSONArray(m_jsonArrayStreams.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    castVideoFromId(nStreamingId);
                    cast=0;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onPlaylistEnded() {

        }
    }


    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
        }

        @Override
        public void onPaused() {
            Log.d("youtube:::", "::::::paused");
            // Called when playback is paused, either due to user action or call to pause().
        }

        @Override
        public void onStopped() {
            Log.d("youtube:::", "::::::stopped");
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


    public void showFirView() {
        try {

            if (type.equals(Constants.SEARCH_STATIONS)) {
                ProgressHUD mmProgressHUD = new ProgressHUD(getActivity());
               /* szSplashDisplay = false;
                ((HomeActivity) getActivity()).mChannelProgress(false, szSplashTitle, (int) fPos + "%", progress);*/
                try {

                    mmProgressHUD.show(getActivity(), "Please Wait...", true, false, null);

                    mShowsChannelList();
                    makeChannelList();
                    makeChannelTitleList();
                    Log.d("::::::::","making first view");
                    makeChannelDescList();
                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    mmProgressHUD.dismiss();
                }


            } else {

                /*if (HomeActivity.m_jsonLiveItems != null)

                {


                    m_channelSchedule.clear();
                    if (HomeActivity.objSchedule != null) {
                        for (int i = 0; i < HomeActivity.m_jsonLiveItems.length(); i++) {
                            JSONObject obj = HomeActivity.m_jsonLiveItems.getJSONObject(i);
                            HomeActivity.IDs = obj.getInt("id") + "";
                            ChannelModel model = new ChannelModel();
                            model.channelId = obj.getInt("id");
                            model.channelName = obj.getString("name");
                            model.channelLogo = obj.getString("big_logo_url");
                            model.channelVideos = HomeActivity.objSchedule.getJSONObject(HomeActivity.IDs).getJSONArray("videos").getJSONArray(0);
                            m_channelSchedule.add(model);


                        }
                    } else {
                        m_channelSchedule.clear();
                    }

                    setMainViewData(HomeActivity.m_jsonLiveItems, 0);
                } else {*/
                JSONObject jsonObject = HomeActivity.m_jsonArrayCategories.getJSONObject(0);
                //int nID = 145;//jsonObject.getInt("id");
                int nID = HomeActivity.channelID;//jsonObject.getInt("id");
                nCategory = nID;
                nBackgroundLoading = 1;
                szSplashTitle = "Loading Channels Data...";
                ((HomeActivity) getActivity()).mChannelProgress(true, szSplashTitle, (int) fPos + "%", progress);
                txt_splash_title.setText(szSplashTitle);
                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                    new FirstLoadingTask().execute();
                }else{
                    try {
                        ((HomeActivity) getActivity()).mChannelProgress(false, szSplashTitle, (int) fPos + "%", progress);
                        //setMainViewData(HomeActivity.m_jsonLiveItems, 0);
                        Toast.makeText(getActivity(),"No Network",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                // }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class FirstLoadingTask extends AsyncTask<Object, Integer, Object> {
        ProgressHUD mProgressHUD;
        int channelId;
                String channelName,
                channelLogo;
                      JSONArray  channelVideos;
        @Override
        protected void onPreExecute() {
            loadingtimeOffset=0;
            //  mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {


                if (nCategory != -1)
                    HomeActivity.m_jsonLiveItems = JSONRPCAPI.getChannels(nCategory);

                if (HomeActivity.m_jsonLiveItems == null){

                    return null;
                }else if(HomeActivity.m_jsonLiveItems.length()<=0){
                    return null;

                }
                Log.d("getChannels::", ":::" + HomeActivity.m_jsonLiveItems.toString());
                for (int i = 34; i <= 60; i++) {
                    try {
                        Thread.sleep(100);
                        publishProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String IDs = "";

                for (int i = 0; i < HomeActivity.m_jsonLiveItems.length(); i++) {
                    JSONObject obj = HomeActivity.m_jsonLiveItems.getJSONObject(i);
                    if (HomeActivity.IDs.length() == 0)
                        HomeActivity.IDs = obj.getInt("id") + "";
                    else
                        HomeActivity.IDs = HomeActivity.IDs + "," + obj.getInt("id");
                }
                nTimeOffset = 0;
                HomeActivity.objSchedule = JSONRPCAPI.getScheduller(HomeActivity.IDs, loadingtimeOffset, nTimeLimit);
                if (HomeActivity.objSchedule == null) return null;
                Log.d("getScheduller1::", ":::" + HomeActivity.objSchedule.toString());
                m_channelSchedule=new ArrayList<>();
                if (HomeActivity.objSchedule != null) {
                    for (int i = 0; i < HomeActivity.m_jsonLiveItems.length(); i++) {
                        JSONObject obj = HomeActivity.m_jsonLiveItems.getJSONObject(i);
                        HomeActivity.IDs = obj.getInt("id") + "";
                        ChannelModel model = new ChannelModel();
                        model.channelId = obj.getInt("id");
                        model.channelName = obj.getString("name");
                        model.channelLogo = obj.getString("big_logo_url");
                        model.channelVideos = HomeActivity.objSchedule.getJSONObject(HomeActivity.IDs).getJSONArray("videos").getJSONArray(0);

                        if (HomeActivity.channelID == 1) {
                            //for Music videos from listen section
                            if (obj.getInt("id") == 329) {
                                m_channelSchedule.add(0, model);
                            } else {
                                m_channelSchedule.add(model);
                            }
                        } else {
                            m_channelSchedule.add(model);
                        }


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
            if(HomeActivity.m_jsonLiveItems!=null&& HomeActivity.m_jsonLiveItems.length()>0){
                if (type.equals(Constants.SEARCH_STATIONS)) {
                /*szSplashDisplay = false;
                ((HomeActivity) getActivity()).mChannelProgress(false, szSplashTitle, (int) fPos + "%", progress);*/
                    mShowsChannelList();
                    makeChannelList();
                    makeChannelTitleList();
                    Log.d("::::::::","making first view");
                    makeChannelDescList();

                } else {
                    setMainViewData(HomeActivity.m_jsonLiveItems, 0);
                }
            }else{
                try {
                    setMainViewData(HomeActivity.m_jsonLiveItems, 0);
                    Toast.makeText(getActivity(),"No data found",Toast.LENGTH_SHORT).show();
                    ((HomeActivity) getActivity()).mChannelProgress(false, szSplashTitle, (int) fPos + "%", progress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            if (values == null) return;
            progress = values[0];


            try {
                splash_progress.setProgress(values[0]);
                fPos = values[0];
                ((HomeActivity) getActivity()).mChannelProgress(true, szSplashTitle, (int) fPos + "%", progress);
                txt_splash_progress.setText((int) fPos + "%");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


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
        szSplashTitle = "Loading Channel Schedules...";
        ((HomeActivity) getActivity()).mChannelProgress(firstload, szSplashTitle, (int) fPos + "%", progress);
        setLiveData(nID, strChannelName);
    }

    public void setLiveData(int nID, String strName) {

        nChannelID = nID;
        szSplashDisplay = false;
        ((HomeActivity) getActivity()).mChannelProgress(firstload, szSplashTitle, (int) fPos + "%", progress);
        //change
        if(firstload){
            linearFullScreen.setVisibility(View.VISIBLE);
            content_layout.setVisibility(View.GONE);
        }else{
            linearFullScreen.setVisibility(View.GONE);
            content_layout.setVisibility(View.VISIBLE);
        }

        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
            new LoadingOnDemandTasks().execute();
        }

        //categoryMenu.showContent();


        //makeChannelList();
    }

    private class LoadingOnDemandTasks extends AsyncTask<Object, Integer, Object> {

        LoadingOnDemandTasks() {

        }

        private boolean showDialog = false;

        LoadingOnDemandTasks(boolean showDialog) {
            this.showDialog = false;
        }

        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            listChannelTitle.setVisibility(View.GONE);
            channelTabContent.setVisibility(View.GONE);
            bottom_progress.setVisibility(View.GONE);
            bottom_textview.setVisibility(View.VISIBLE);
            left_loadingchannel_textview.setVisibility(View.VISIBLE);

            //change
            if(firstload){
                szSplashDisplay = true;
                ((HomeActivity) getActivity()).mChannelProgress(true, szSplashTitle, (int) fPos + "%", progress);
                linearFullScreen.setVisibility(View.VISIBLE);
            }else{
                ((HomeActivity) getActivity()).mChannelProgress(false, szSplashTitle, (int) fPos + "%", progress);
                linearFullScreen.setVisibility(View.GONE);
            }

            if (!szSplashDisplay)
                mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                m_jsonArrayStreams = JSONRPCAPI.getStreams(nChannelID);
                if (m_jsonArrayStreams == null) return null;
                Log.d("getStreams::", ":::" + m_jsonArrayStreams.toString());

                if(firstload){
                    for (int i = 61; i < 101; i++) {
                        try {
                            publishProgress(i);
                            Thread.sleep(50);



                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            try {
                if (values == null) return;
                progress = values[0];


                splash_progress.setProgress(values[0]);
                fPos = values[0];
                if(progress==98){
                    szSplashTitle="Loading Views...";
                }
                ((HomeActivity) getActivity()).mChannelProgress(true, szSplashTitle, (int) fPos + "%", progress);
                txt_splash_progress.setText((int) fPos + "%");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Object params) {


            try {


                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                timer.schedule(new Blink_progress(), 120000, 1200000);
                //timer.schedule(new Blink_progress(), 50000, 1200000);
                // menu_handler.postDelayed(run_menu, 120000);
                if (m_jsonArrayStreams == null) return;

                ProgressHUD mmProgressHUD = new ProgressHUD(getActivity());
                try {
                    //mmProgressHUD.show(getActivity(), "Please Wait...", true, false, null);

                    makeChannelList();
                    makeChannelTitleList();
                    Log.d("::::::::","making first view");
                    makeChannelDescList();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //mmProgressHUD.dismiss();
                    bottom_textview.setVisibility(View.GONE);
                    left_loadingchannel_textview.setVisibility(View.GONE);
                    listChannelTitle.setVisibility(View.VISIBLE);
                    channelTabContent.setVisibility(View.VISIBLE);
                    bottom_progress.setVisibility(View.GONE);
                }


                if (m_jsonArrayStreams != null) {

                    streamAdapter = new JSONAdapter(getActivity(), m_jsonArrayStreams);
                    listStreams.setAdapter(streamAdapter);

                } else {
                    m_channelSchedule.clear();
                }


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

                    try {
                        player.loadVideos(currentVideoList, 0, timeOffset * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        m_jsonArrayStreamsForCasting = new JSONArray(m_jsonArrayStreams.toString());
                        nStreamingId=0;
                        castVideoFromId(nStreamingId);
                        cast=1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        prevStartTime = m_jsonArrayStreams.getJSONObject(0).getInt("unix_time");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //m_jsonArrayStreams.remove(0);
                    //removeFirstChannel();
                   /* if (m_jsonArrayStreams != null) {
                        streamAdapter = new JSONAdapter(getActivity(), m_jsonArrayStreams);
                        listStreams.setAdapter(streamAdapter);
                    }*/
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
                                    try {
                                        m_jsonArrayStreamsForCasting = new JSONArray(m_jsonArrayStreams.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (!TextUtils.isEmpty(strUrl)) {
                                        if (demanddialog != null && demanddialog.isShowing()) {
                                            demanddialog.dismiss();
                                        }

                                        demanddialog = new OnDemandDialog(getActivity());
                                        demanddialog.setInfo(strTitle, strUrl, position);
                                        demanddialog.setActivity(getActivity());
                                        demanddialog.show();
                                    } else {
                                        Toast.makeText(getActivity(), "No url Found", Toast.LENGTH_SHORT).show();
                                    }
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
                /*if(channelSelected){
                    displaySelectedchannel();
                }
*/


            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (!szSplashDisplay)
                    mProgressHUD.dismiss();
                ((HomeActivity) getActivity()).mChannelProgress(false, szSplashTitle, (int) fPos + "%", progress);
                //change
                linearFullScreen.setVisibility(View.GONE);
                content_layout.setVisibility(View.VISIBLE);
                firstload=false;
            }

        }

        private boolean canResolveIntent(Intent intent) {
            List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
            return resolveInfo != null && !resolveInfo.isEmpty();
        }




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


    private void makeChannelList() {

        try {
            int maxCount = 0;
            if (m_channelSchedule.size() == 0) return;
            long nMaxEndTime = 0;
            nMinStartTime = 0;
            for (int i = 0; i < m_channelSchedule.size(); i++) {
                ChannelModel item = m_channelSchedule.get(i);
                try {
                    JSONObject objStart = item.channelVideos.getJSONObject(0);
                    JSONObject objEnd = item.channelVideos.getJSONObject(item.channelVideos.length() - 1);

                    if (nMinStartTime == 0)
                        nMinStartTime = objStart.getLong("unix_time");
                    if (objStart.getLong("unix_time") < nMinStartTime)
                        nMinStartTime = objStart.getLong("unix_time");

                    if (nMaxEndTime < (objEnd.getLong("unix_time") + objEnd.getLong("duration")))
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
                if (maxCount < model.channelVideos.length()) {
                    maxCount = model.channelVideos.length();
                }
                for (int i = 0; i < model.channelVideos.length(); i++) {
                    try {
                        JSONObject obj = model.channelVideos.getJSONObject(i);
                        long nDeltaTime = obj.getLong("unix_time") - nPrevStartTime;
                        if (nDeltaTime > 0) {
                            nWidth += (int) nDeltaTime;
                        }

                        if ((obj.getLong("duration")) < 100) {
                            nWidth += 130;
                        }/*else if( (obj.getLong("duration") ) > 500 ){
                        nWidth += 500;
                    }*/ else
                            nWidth += (int) (obj.getLong("duration") + 30);

                        nPrevStartTime = obj.getLong("unix_time") + obj.getLong("duration");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (nMaxWidth < nWidth)
                    nMaxWidth = nWidth;

            }
            ViewGroup.LayoutParams layoutParams = listChannelDesc.getLayoutParams();
            Log.d("maxCount::::///", "maxCount::::???" + maxCount);
            layoutParams.width = nMaxWidth + 5 + (maxCount * 400);
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
            final LayoutInflater m_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            linearScrollChannelTitle.removeAllViews();
            scroll_offset=0;

            if (m_channelSchedule.size() != 0) {
                for (int k = 0; k < m_channelSchedule.size(); k++) {
                    final int kk=k;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ChannelModel model = m_channelSchedule.get(kk);
                            View row = m_inflater.inflate(R.layout.item_channel_grid, null);
                            LinearLayout linear = (LinearLayout) row.findViewById(R.id.item_grid);
                            DynamicImageView imageView = (DynamicImageView) row.findViewById(R.id.imageShowThumbnail);
                            TextView txtChannelName = (TextView) row.findViewById(R.id.txtChannelName);
                            txtChannelName.setTypeface(OpenSans_Regular);

                            imageView.loadImage(model.channelLogo);
                            txtChannelName.setText(model.channelName);
                            if (kk == 0)
                                linear.setBackgroundColor(Color.parseColor("#0D76BC"));
                            else
                                linear.setBackgroundColor(Color.parseColor("#004A7C"));

                            final int position = kk;
                            row.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        scroll_offset=0;


                                        if (!type.equals(Constants.SEARCH_STATIONS)) {
                                            if (m_channelSchedule.size() != 0) {
                                                bNoActive = false;
                                                ChannelModel model1 = m_channelSchedule.get(position);
                                                m_channelSchedule.remove(position);
                                                m_channelSchedule.add(0, model1);
                                                nTimeOffset = -nTimeLimit;
                                                setLiveData(model1.channelId, model1.channelName);
                                                nStreamingId=position;
                                                castVideoFromId(nStreamingId);

                                                try {
                                                    for(int l=0;l<m_jsonLiveItems.length();l++){
                                                        try {
                                                            JSONObject jObj = m_jsonLiveItems.getJSONObject(l);
                                                            int nID = jObj.getInt("id");
                                                            if(nID==model1.getChannelId()){
                                                                mSubContentSelectedItem=l;
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    mtoolbarlastcontent=model1.getChannelName();
                                                    ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, mtoolbarlastcontent);
                                                    alertChannelDialogListAdapter = new AlertChannelDialogListAdapter(getActivity(), m_jsonLiveItems);
                                                    fragment_ondemand_alllist_items.setAdapter(alertChannelDialogListAdapter);
//} else {
                                                    alertChannelDialogListAdapter.notifyDataSetChanged();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } else {

                                            currentVideoList.clear();

                                            for (int i = 0; i < m_channelSchedule.get(position).channelVideos.length(); i++) {
                                                try {
                                                    String curUrl = m_channelSchedule.get(position).channelVideos.getJSONObject(i).getString("url");
                                                    if (!TextUtils.isEmpty(curUrl)) {
                                                        currentVideoList.add(curUrl);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            int timeOffset = getTimeOffset();

                                            if (player != null) {

                                                try {
                                                    player.loadVideos(currentVideoList, 0, timeOffset * 1000);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    prevStartTime = m_channelSchedule.get(position).channelVideos.getJSONObject(0).getInt("unix_time");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                removeFirstChannel();
                                                if (m_channelSchedule.get(position).channelVideos != null) {
                                                    streamAdapter = new JSONAdapter(getActivity(), m_channelSchedule.get(position).channelVideos);
                                                    listStreams.setAdapter(streamAdapter);
                                                }


                                            }


                                            nStreamingId=position;
                                            castVideoFromId(nStreamingId);
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            linearScrollChannelTitle.addView(row);
                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeChannelDescRefresh(final String s) {
        try {
            final LinearLayout linear = (LinearLayout) firstRow.findViewById(R.id.item_channel_desc);
            Log.d("linear.getChildCount()", ":::" + linear.getChildCount());
            for (int i = 0; i < linear.getChildCount(); i++) {
                final int ii=i;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final View childItem = linear.getChildAt(ii);
                        String key = (String) childItem.getTag();
                        if (key.equals(s)) {
                            childItem.setBackgroundColor(Color.parseColor("#3e99f2"));
                            Log.d("row item:::::","width::::"+childItem.getScrollX());
                            Log.d("row item:::::","width22::::"+childItem.getX());
                            scroll_offset=(int)childItem.getX();
                            bExistChannel = true;

                            try {
                                if(scroll_offset!=0){

                                    // channelTabContent.setScrollX(channelTabContent.getScrollX() + scroll_offset);
                                    channelTabContent.post(new Runnable() {

                                        public void run() {
                                            channelTabContent.scrollTo((int)childItem.getX(), 0);
                                        }
                                    });
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            childItem.setBackgroundColor(Color.parseColor("#01395E"));
                        }
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeChannelDescList() {
        try {
            horizontalListdisplayCount = 2;
            channelTabContent.setScrollX(0);

           final LayoutInflater m_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            linearScrollChannelDesc.removeAllViews();
            //int nWidth = channelTabContent.getMeasuredWidth() / 2;


            if (m_channelSchedule.size() != 0) {
                for (int k = 0; k < m_channelSchedule.size(); k++) {
                    final int kk=k;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View row = m_inflater.inflate(R.layout.item_channel_desc, null);
                            final LinearLayout linear = (LinearLayout) row.findViewById(R.id.item_channel_desc);
                            nTempWidth = 0;

                            try {
                                linear.removeAllViews();

                                nPrevStartTime = nMinStartTime;

                                final ChannelModel model = m_channelSchedule.get(kk);
                                for (int i = 0; i < 2 && i < model.channelVideos.length(); i++) {
                                    final int ii=i;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject obj = model.channelVideos.getJSONObject(ii);

                                                View childItem = m_inflater.inflate(R.layout.item_channel_desc_item, null);

                                                //DynamicImageView imageView = (DynamicImageView) childItem.findViewById(R.id.imageShowThumbnail);
                                                TextView txtChannelName = (TextView) childItem.findViewById(R.id.txtChannelName);
                                                TextView txtChannel_time = (TextView) childItem.findViewById(R.id.txtChannel_time);
                                                txtChannelName.setTypeface(OpenSans_Regular);
                                                txtChannel_time.setTypeface(OpenSans_Regular);
                                                //imageView.setVisibility(View.GONE);

                                                long nDeltaTime = obj.getLong("unix_time") - nPrevStartTime;
                                                if (ii == 0) nDeltaTime = 0;
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
                                                    Log.d("TiTle///::::", ":::" + "///");
                                                    Log.d("Width///::::", ":::" + nWidth);
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
                                                txtChannel_time.setText("Start Time: " + strTextTime);
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
                                                final int nPos = ii;
                                                childItem.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        try {
                                                            String strUrl = null;
                                                            String strTitle = null;
                                                            bNoActive = false;
                                                            try {
                                                                strTitle = jObj.getString("title");
                                                                strUrl = jObj.getString("url");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            try {
                                                                m_jsonArrayStreamsForCasting = new JSONArray(model.channelVideos.toString());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                            if (!TextUtils.isEmpty(strUrl)) {
                                                                if (demanddialog != null && demanddialog.isShowing()) {
                                                                    demanddialog.dismiss();
                                                                }

                                                                demanddialog = new OnDemandDialog(getActivity());
                                                                demanddialog.setInfo(strTitle, strUrl, nPos);
                                                                demanddialog.setActivity(getActivity());
                                                                demanddialog.show();
                                                            } else {
                                                                Toast.makeText(getActivity(), "No url Found", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }


                                                    }
                                                });
                                                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                int nWidth = 100;
                                                Log.d("childitem ::::::::",":::::"+childItem.getX());
                                                if(lastRow!=null)
                                                Log.d("childitem ::::::::","last:::::"+lastRow.getX());
                                                if ((obj.getLong("duration")) < 100) {
                                                    nWidth = 130;
                                                } /*else if ((obj.getLong("duration")) > 500) {
                                    nWidth = 500;
                                }*/ else
                                                    nWidth = (int) (obj.getLong("duration") + 30);
                                                nTempWidth += nWidth;
                                                if (ii == model.channelVideos.length() - 1) {
                                                    nWidth += nMaxWidth - nTempWidth;
                                                }
                                                Log.d("TiTle::::", ":::" + strText);
                                                Log.d("Width::::", ":::" + nWidth);

                                                layoutParams.width = nWidth + 400;
                                                childItem.setLayoutParams(layoutParams);

                                                linear.addView(childItem);
                                                nPrevStartTime = obj.getLong("unix_time") + obj.getLong("duration");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });



                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            row.setTag(kk);
                            if (kk == 0) firstRow = row;
                            lastRow=row;
                            linearScrollChannelDesc.addView(row);
                        }
                    });



                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void addtoChannelDescList() {
        try {

            final LayoutInflater m_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //int nWidth = channelTabContent.getMeasuredWidth() / 2;

            loadpage=false;
            if (m_channelSchedule.size() != 0) {
                for (int k = 0; k < m_channelSchedule.size(); k++) {
                    final int kk=k;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View row = linearScrollChannelDesc.getChildAt(kk);
                            LinearLayout linear = (LinearLayout) row.findViewById(R.id.item_channel_desc);

                            int nTempWidth = 0;

                            try {
                                //linear.removeAllViews();

                                long nPrevStartTime = nMinStartTime;

                                final ChannelModel model = m_channelSchedule.get(kk);
                                Log.d("ListdisplayCount", "::::" + horizontalListdisplayCount);
                                for (int i = horizontalListdisplayCount; (i < horizontalListdisplayCount + 1) && (i < model.channelVideos.length()); i++) {

                                    if(horizontalListdisplayCount==model.channelVideos.length()-1){
                                        loadpage=true;
                                    }


                                    JSONObject obj = model.channelVideos.getJSONObject(i);

                                    View childItem = m_inflater.inflate(R.layout.item_channel_desc_item, null);

                                    //DynamicImageView imageView = (DynamicImageView) childItem.findViewById(R.id.imageShowThumbnail);
                                    TextView txtChannelName = (TextView) childItem.findViewById(R.id.txtChannelName);
                                    TextView txtChannel_time = (TextView) childItem.findViewById(R.id.txtChannel_time);
                                    txtChannelName.setTypeface(OpenSans_Regular);
                                    txtChannel_time.setTypeface(OpenSans_Regular);
                                    //imageView.setVisibility(View.GONE);

                                    long nDeltaTime = obj.getLong("unix_time") - nPrevStartTime;
                            /*if (i == 0) nDeltaTime = 0;
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
                                Log.d("TiTle///::::", ":::" + "///");
                                Log.d("Width///::::", ":::" + nWidth);
                                view.setLayoutParams(layoutParams);
                                linear.addView(view);
                            }*/
                                    nTempWidth += nDeltaTime;



                           /* layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = nWidth;
                            Log.d("selecttv::", "Width:::" + nWidth);
                            childItem.setLayoutParams(layoutParams);*/

                                    String strText = obj.getString("title");
                                    String strTextTime = obj.getString("time");
                                    txtChannelName.setText(strText);
                                    txtChannel_time.setText("Start Time: " + strTextTime);
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
                                            if (!TextUtils.isEmpty(strUrl)) {
                                                if (demanddialog != null && demanddialog.isShowing()) {
                                                    demanddialog.dismiss();
                                                }

                                                demanddialog = new OnDemandDialog(getActivity());
                                                demanddialog.setInfo(strTitle, strUrl, nPos);
                                                demanddialog.setActivity(getActivity());
                                                demanddialog.show();
                                            } else {
                                                Toast.makeText(getActivity(), "No url Found", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });

                                    layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    int nWidth = 100;
                                    if ((obj.getLong("duration")) < 100) {
                                        nWidth = 130;
                                    } /*else if ((obj.getLong("duration")) > 500) {
                                nWidth = 500;
                            }*/ else
                                        nWidth = (int) (obj.getLong("duration") + 30);
                                    nTempWidth += nWidth;
                                    if (i == model.channelVideos.length() - 1) {
                                        nWidth += nMaxWidth - nTempWidth;
                                    }
                                    Log.d("TiTle::::", ":::" + strText);
                                    Log.d("Width::::", ":::" + nWidth);

                                    layoutParams1.width = nWidth + 400;
                                    childItem.setLayoutParams(layoutParams1);

                                    linear.addView(childItem);
                                    nPrevStartTime = obj.getLong("unix_time") + obj.getLong("duration");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            row.setTag(kk);
                            if (kk == 0) firstRow = row;

//                    linearScrollChannelDesc.addView(row);

                        }
                    });


                }
                horizontalListdisplayCount = horizontalListdisplayCount + 1;
            }else{
                Log.d("m_channelSchedule",""+m_channelSchedule.size());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {
        try {
            if (!wasRestored) {

                ChannelFragment.this.player = player;
                //player.loadVideo(video_url);


                // Hiding player controls
                ChannelFragment.this.player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                ChannelFragment.this.player.setPlayerStateChangeListener(playerStateChangeListener);
                ChannelFragment.this.player.setPlaybackEventListener(playbackEventListener);
                ChannelFragment.this.player.setPlaylistEventListener(mPlaylistEventListener);
                ChannelFragment.this.player.setShowFullscreenButton(true);
                ChannelFragment.this.player.addFullscreenControlFlag(1);
                ChannelFragment.this.player.setManageAudioFocus(true);

                ChannelFragment.this.player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {

                    @Override
                    public void onFullscreen(boolean _isFullScreen) {
                        Log.d("isYoutubeFullscreen::", ":::" + _isFullScreen);
                        isYoutubeFullscreen = _isFullScreen;
                    }
                });

                try {
                    run_menu = new Runnable() {
                        public void run() {
                            try {
                                if (player != null && player.isPlaying()) {
                                    player.setFullscreen(true);
                                }
                                if (timer != null) {
                                    timer.cancel();
                                    timer = null;
                                }
                                menu_handler.removeCallbacks(run_menu);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    };


                    if (type.equals(Constants.SEARCH_STATIONS)) {


                        int timeOffset = 0;
                        try {
                            long u_time=m_jsonArrayChannelVideos.getJSONObject(0).getInt("unix_time");
                            timeOffset = getTimeOffsetbyunixtime(u_time);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                                player.loadVideos(currentVideoList, 0, timeOffset * 1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                        try {
                            prevStartTime = m_jsonArrayChannelVideos.getJSONObject(0).getInt("unix_time");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        removeFirstChannel();
                        if (m_jsonArrayChannelVideos != null) {
                            streamAdapter = new JSONAdapter(getActivity(), m_jsonArrayChannelVideos);
                            listStreams.setAdapter(streamAdapter);
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getTimeOffsetbyunixtime(long u_time) {


        long curTime = 0;
        int timeOffset = 0;

        curTime = System.currentTimeMillis() / 1000;

        long t1 = System.currentTimeMillis() / 1000;
        Date dt = new Date();
        long t2 = dt.getTime() / 1000;
        int h = dt.getHours();
        int m = dt.getMinutes();
        int s = dt.getSeconds();
        long t3 = Calendar.getInstance().getTimeInMillis() / 1000;

        Log.e("TIMEOFFSET", String.format("unix_Time = %d, curTime = %d", u_time, curTime));
        timeOffset = (int) (curTime - u_time);

        if (timeOffset > 0)
            return timeOffset;
        else
            return 0;
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        try {
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
            } else {
                String errorMessage = String.format("There was an error initializing the YouTubePlayer (%1$s)", errorReason.toString());
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrevious() {
        Log.d("Youtube::", "prevoius selected");
        nStreamingId--;
        if (m_jsonArrayStreams!= null) {
            try {
                m_jsonArrayStreamsForCasting = new JSONArray(m_jsonArrayStreams.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        castVideoFromId(nStreamingId);

    }

    @Override
    public void onNext() {
        Log.d("Youtube::", "next selected");
        if (m_jsonArrayStreams!= null) {
            try {
                m_jsonArrayStreamsForCasting = new JSONArray(m_jsonArrayStreams.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            castVideoFromId(nStreamingId);

        }

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
            try {
                HomeActivity.m_jsonArrayCategories = JSONRPCAPI.getCategories();

                if (HomeActivity.m_jsonArrayCategories != null) {
                    Log.d("jsonArrayCategories ::", ":::" + HomeActivity.m_jsonArrayCategories.toString());
                }

                for (int i = 0; i < 33; i++) {
                    try {
                        Thread.sleep(100);
                        publishProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            // mProgressHUD.dismiss();

            try {
                if (HomeActivity.m_jsonArrayCategories != null&& HomeActivity.m_jsonArrayCategories .length()>0) {
                    m_jsonArrayParentCategories = new JSONArray();
                    for (int i = 0; i < HomeActivity.m_jsonArrayCategories.length(); i++) {
                        try {
                            JSONObject objItem = HomeActivity.m_jsonArrayCategories.getJSONObject(i);

                            boolean b = objItem.isNull("parent_id");
                            if (b) {
                                if (!objItem.getString("id").equals("0")) {
                                    m_jsonArrayParentCategories.put(objItem);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    mChannelList = "";
                    makeChannelInterface();

                    showFirView();
                } else {
                    try {
                        ((HomeActivity) getActivity()).mChannelProgress(false, szSplashTitle, (int) fPos + "%", progress);
                        setMainViewData(HomeActivity.m_jsonLiveItems, 0);
                        Toast.makeText(getActivity(),"No data found",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            try {
                if (values == null) return;
                progress = values[0];

                splash_progress.setProgress(values[0]);
                fPos = values[0];
                ((HomeActivity) getActivity()).mChannelProgress(true, szSplashTitle, (int) fPos + "%", progress);
                txt_splash_progress.setText((int) fPos + "%");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            try {
                if (nCategory != -1)
                    m_jsonSubCategories = JSONRPCAPI.getDecade(nDecadeCategory);
                if (m_jsonSubCategories == null) return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                //String count = "(" + jsonObject.getInt("channels_count") + ")";
                // arrayAdapter.add(strName + count);
                arrayAdapter.add(strName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
        ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelSubCategoryAdapter(arrayAdapter, getActivity());
        fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
    }

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
                    horizontal_position=position;
                    minimizeChannelView();
                    showBackIcon(true);
                    try {
                        JSONObject jsonObject = m_jsonArrayParentCategories.getJSONObject(position);
                        int nID = jsonObject.getInt("id");
                        temp = getSubCategories(nID);
                        subCategoryAffix = m_channelViews.get(position).getmChannelCategoryName();
                        if (temp.length() == 0) {
                            mtoolbarMaincontent = m_channelViews.get(position).getmChannelCategoryName();
                            ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, "", "");
                            fragment_ondemand_alllist_items.setVisibility(View.GONE);
                            aBoolean = true;
                            loadChannel(nID);
                        } else {
                            mtoolbarMaincontent = m_channelViews.get(position).getmChannelCategoryName();
                            ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, "", "");
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

                Utilities.setViewFocus(getActivity(), fragment_ondemandlist_items);
            }
        }
    }

    private void minimizeChannelView() {

        try {
            Log.d("touchevent::", "touchevent::");
            if (checkIsTablet()) {
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
            } else {
                float weight = 1.8f;
                if (bChannelUp == true) {

                    imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowup));
                    weight = 1.8f;
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearSubMainRight.getLayoutParams();
                params.weight = weight;
                linearSubMainRight.setLayoutParams(params);
                bNoActive = false;
                bChannelUp = !bChannelUp;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

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
                loadingtimeOffset=0;
                /*if(showDialog) {
                    mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
                }*/
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                if (nCategory != -1)
                    m_jsonLiveItems = JSONRPCAPI.getChannels(nCategory);


                if (m_jsonLiveItems == null) return null;
                Log.d("getChannels::", ":::" + m_jsonLiveItems.toString());
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
                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, loadingtimeOffset, nTimeLimit);
                if (objSchedule == null) return null;
                Log.d("getScheduller2::", ":::" + objSchedule.toString());

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
                        //if (alertChannelDialogListAdapter == null) {
                        mSubContentSelectedItem=-1;
                        alertChannelDialogListAdapter = new AlertChannelDialogListAdapter(getActivity(), m_jsonLiveItems);
                        fragment_ondemand_alllist_items.setAdapter(alertChannelDialogListAdapter);
                        //} else {
                        alertChannelDialogListAdapter.notifyDataSetChanged();
                        //}

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
                            ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mCategorySelectedHeader, "");
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
                                ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                new LoadingDecade(true, 1).execute();
                            } else {
                                JSONObject jsonObject = m_jsonSubCategories.getJSONObject(position);
                                int nID = jsonObject.getInt("id");
                                Log.d("SelectedID2::","::::"+jsonObject.getInt("id"));
                                subCategoreis = getSubCategories(nID);
                                if (subCategoreis.length() == 0) {
                                    if (nID != 484 && nID != 482 && nID != 486) {
                                        mSubCategorySelectedItem = position;
                                        if (m_jsonSubCategories == temp) {
                                            mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                            mtoolbarSubContent = arrayAdapter.get(position);
                                            ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                        } else {
                                            mChannelList = Constants.CHANNEL_SUBCONTENT;
                                            mCategorySelectedHeader = arrayAdapter.get(position);
                                            ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mCategorySelectedHeader, "");
                                        }
                                        aBoolean = true;
                                        loadChannel(nID);
                                    } else {
                                        mSubCategorySelectedItem = position;
                                        mCategorySelectedHeader = arrayAdapter.get(position);
                                        ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mCategorySelectedHeader, "");
                                        mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                        showActorDialog(nID);
                                    }

                                } else {
                                    mSubCategorySelectedItem = position;
                                    mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                    m_jsonSubCategories = subCategoreis;
                                    mtoolbarSubContent = arrayAdapter.get(position);
                                    Log.d("SelectedID::","::::"+arrayAdapter.get(position));
                                    ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
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

                Utilities.setViewFocus(getActivity(), fragment_ondemandlist_items);
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
        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, arrayActorAdapterData);
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
        spinner_actors.setFocusable(true);

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
                loadingtimeOffset=0;
                /*if(showDialog) {
                    mProgressHUD = ProgressHUD.show(context, "Please Wait...", true, false, null);
                }*/
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                if (nCategory != -1)
                    m_jsonLiveItems = JSONRPCAPI.getChannelsForActor(nCategory, filter);
                if (m_jsonLiveItems == null) return null;
                String IDs = "";

                for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                    JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                    if (IDs.length() == 0)
                        IDs = obj.getInt("id") + "";
                    else
                        IDs = IDs + "," + obj.getInt("id");
                }
                nTimeOffset = 0;
                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, loadingtimeOffset, nTimeLimit);
                if (objSchedule == null) return null;
                Log.d("getScheduller:3:", ":::" + objSchedule.toString());
                m_channelSchedule=new ArrayList<>();
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
                    mSubContentSelectedItem=-1;
                    AlertChannelDialogListAdapter adapter = new AlertChannelDialogListAdapter(getActivity(), m_jsonLiveItems);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    private void text_font_typeface() {
        try {
            OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
            OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
            OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
            osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class AlertChannelDialogListAdapter extends RecyclerView.Adapter<AlertChannelDialogListAdapter.DataObjectHolder> {
        private Context m_context;
        private JSONArray m_data;
        int mPosition;
        Boolean isClicked = false;

        public AlertChannelDialogListAdapter(Context context, JSONArray m_jsonLiveItems) {
            this.m_context = context;
            this.m_data = m_jsonLiveItems;
            text_font_typeface();
        }

        @Override
        public AlertChannelDialogListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            holder.fragment_channel_items.setTag(position);

//            Picasso.with(m_context).load(strUrl).into(holder.fragment_channel_items_imageThumbnail);
            holder.fragment_channel_items_imageThumbnail.loadImage(strUrl);
            Log.e("strUrl", strUrl);



                if (position == mSubContentSelectedItem) {

                    holder.fragment_channel_items.setBackgroundResource(R.drawable.menubg);
                } else {
                    holder.fragment_channel_items.setBackgroundResource(0);
                }




            holder.fragment_channel_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int pos=Integer.parseInt(holder.fragment_channel_items.getTag().toString());
                        Log.d("alert::", "Alertdialog selected");

                        linearScrollChannelTitle.removeAllViews();
                        linearScrollChannelDesc.removeAllViews();

                        mtoolbarlastcontent = holder.fragment_channel_items.getText().toString();
                        isClicked = true;
                        mSubContentSelectedItem = pos;
                        JSONObject jsonObject = m_jsonLiveItems.getJSONObject(pos);
                        minimizeChannelView();
                        int nID = jsonObject.getInt("id");
                        String strName = jsonObject.getString("name");
                        Log.d("alert::", "Alertdialog selected:::"+strName);
                        if (position < m_channelSchedule.size()) {
                            for(int l=0;l<m_channelSchedule.size();l++){
                                ChannelModel model = m_channelSchedule.get(l);
                                if(model.channelId==nID){
                                    m_channelSchedule.remove(l);
                                    m_channelSchedule.add(0, model);
                                }
                            }



                        }

                        setLiveData(nID, strName);

                        ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, mtoolbarlastcontent);

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

                Utilities.setViewFocus(getActivity(), fragment_channel_items);
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
            loadingtimeOffset=0;
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

            try {
                if (nCategory != -1)
                    m_jsonLiveItems = JSONRPCAPI.getChannelsByDecade(decade, nDecadeCategory);
                if (m_jsonLiveItems == null) return null;
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
                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, loadingtimeOffset, nTimeLimit);
                if (objSchedule == null) return null;
                Log.d("getScheduller4::", ":::" + objSchedule.toString());

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
                    mSubContentSelectedItem=-1;
                    AlertChannelDialogListAdapter adapter = new AlertChannelDialogListAdapter(getActivity(), m_jsonLiveItems);
                    //adapter.setBlack(false);
                    fragment_ondemand_alllist_items.setAdapter(adapter);
                }

                //progressLoad.setVisibility(View.GONE);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (player != null) {
                player.release();
            }
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            menu_handler.removeCallbacks(run_menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void toolbarMainClick() {
        ((HomeActivity) getActivity()).toolbarTextChange("Channels", "", "", "");
        mChannelList = "";
        spinner_layout.setVisibility(View.GONE);
        arrow_layout.setVisibility(View.GONE);
        makeChannelInterface();
    }


    public void toolbarClickAction() {
        try {
            mChannelList = Constants.CHANNEL_MAINCONTENT;
            spinner_layout.setVisibility(View.GONE);
            arrow_layout.setVisibility(View.GONE);

            try {
                JSONObject jsonObject = m_jsonArrayParentCategories.getJSONObject(mMainCategorySelectedItem);
                int nID = jsonObject.getInt("id");
                temp = getSubCategories(nID);
                if (temp.length() == 0) {
                    ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                    aBoolean = true;
                    loadChannel(nID);
                } else {

                    if (mtoolbarMaincontent.equals("TV")) {
                        subCategoryAffix = "TV";
                    }
                    ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, "", "");
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

    public void mFullScreen() {
        try {
            if (ChannelFragment.this.player != null) {

                ChannelFragment.this.player.setFullscreen(true);

            }
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            menu_handler.removeCallbacks(run_menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class Blink_progress extends TimerTask {

        @Override
        public void run() {


            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {

                    public void run() {

                        try {

                            if (ChannelFragment.this.player != null) {

                                ChannelFragment.this.player.setFullscreen(true);

                            }
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }

                            menu_handler.removeCallbacks(run_menu);

                        } catch (Exception e) {
                            e.printStackTrace();
                            // TODO Auto-generated catch block
                        }


                    }

                });
            }

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    private void addhorizontalScrolllayouts(final LinearLayout dynamic_horizontalViews_layout, final ArrayList<ChannelCategoryBean> listData, final int position) {



        horizontal_listview_channel_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();

            }
        });


        left_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontal_listview.
                        post(new Runnable() {
                            public void run() {
                                horizontal_listview.scrollTo(horizontal_listview.getScrollX() - ((width / 5) * 4), horizontal_listview.getScrollY());


                            }
                        });

            }
        });


        right_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontal_listview.
                        post(new Runnable() {
                            public void run() {
                                horizontal_listview.scrollTo(horizontal_listview.getScrollX() + ((width / 5) * 4), horizontal_listview.getScrollY());


                            }
                        });

            }
        });

        Utilities.setViewFocus(getActivity(), horizontal_listview_channel_close);
        Utilities.setViewFocus(getActivity(), left_slide);
        Utilities.setViewFocus(getActivity(), right_slide);


        horizontal_listview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                Log.d("scrollvalue", "++" + horizontal_listview.getScrollX());
                View view = horizontal_listview.getChildAt(horizontal_listview.getChildCount() - 1);
                // Calculate the scrolldiff
                int diff = (view.getRight() - (horizontal_listview.getWidth() + horizontal_listview.getScrollX()));
                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    left_slide.setImageResource(R.drawable.prev_active);
                    right_slide.setImageResource(R.drawable.next_inactive);
                    // notify that we have reached the bottom
                    Log.d("ScrollTest.LOG_TAG", "MyScrollView: Bottom has been reached");
                } else if (horizontal_listview.getScrollX() == 0) {
                    left_slide.setImageResource(R.drawable.prev_inactive);
                    right_slide.setImageResource(R.drawable.next_active);
                } else {
                    left_slide.setImageResource(R.drawable.prev_active);
                    right_slide.setImageResource(R.drawable.next_active);
                }
            }
        });


        int spanCount = listData.size(); // 3 columns
        spacing = 20; // 50px

        boolean includeEdge = false;
        int layoutwidth = recycler_layout.getLayoutParams().width;
        Log.d("layoutwidth::", "layoutwidth" + layoutwidth);

        try {
            ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    width = recycler_layout.getMeasuredWidth();
                    height = recycler_layout.getMeasuredHeight();



                    Log.d("layoutwidth::", "width" + width);










                  /*  horizontal_listview.
                            post(new Runnable() {
                                public void run() {
//                                    horizontal_listview.scrollTo(horizontal_listview.getScrollX() - ((width / 5) * position), horizontal_listview.getScrollY());

                                    horizontal_listview.smoothScrollTo(horizontal_listview.getScrollX() - ((width / 5) * position), horizontal_listview.getScrollY());
                                }
                            });*/
                    for (int l = 0; l < listData.size(); l++) {
                        final int l1=l;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_channel_list, null);
                                final TextView horizontal_imageView = (TextView) itemmlayout.findViewById(R.id.horizontal_imageView);
                                final LinearLayout ll = (LinearLayout) itemmlayout.findViewById(R.id.ll);

                                horizontal_imageView.setText(listData.get(l1).getmChannelCategoryName());
                                LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams((width / 5), LinearLayout.LayoutParams.WRAP_CONTENT);

                                itemmlayout.setTag(ll);

                                if (l1 != 0) {
                                    vp.setMargins(0, 0, 0, 0);
                                }


                                if (l1 == position) {

                                    horizontal_imageView.setBackgroundResource(R.drawable.menu_bg);
                                    int x, y;
                                    x = horizontal_imageView.getLeft();
                                    y = horizontal_imageView.getTop();


                                }




                                final int finalL = l1;
                                itemmlayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            String tag = itemmlayout.getTag().toString();

                                            View lv = (View) dynamic_horizontalViews_layout.findViewWithTag(horizontal_position);

                                            if (lv != null) {
                                                final TextView horizontal_imageViewww = (TextView) lv.findViewById(R.id.horizontal_imageView);
                                                horizontal_imageViewww.setBackgroundResource(0);
                                            }

                                            horizontal_imageView.setBackgroundResource(R.drawable.menu_bg);
                                            horizontal_position = Integer.parseInt(tag);
//                                horizontal_position = tag;
                                            horizontal_listview_item_count.setVisibility(View.INVISIBLE);
                                            horizontal_channel_list.setVisibility(View.INVISIBLE);
                                            horizontal_channel_list_progressBar.setVisibility(View.VISIBLE);
                                            aBoolean = false;
                                            loadallChannel(listData.get(finalL).getmChannelcategoryId());
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });

                                Utilities.setViewFocus(getActivity(), itemmlayout);


                                horizontal_imageView.setLayoutParams(vp);

                                image_view.addView(itemmlayout);
                            }
                        });


                    }

                    aBoolean = false;
                    horizontal_listview_item_count.setVisibility(View.INVISIBLE);
                    horizontal_channel_list.setVisibility(View.INVISIBLE);
                    horizontal_channel_list_progressBar.setVisibility(View.VISIBLE);

                    loadallChannel(listData.get(horizontal_position).getmChannelcategoryId());


                    horizontal_listview.postDelayed(new Runnable() {
                        public void run() {
//                            horizontal_listview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                            horizontal_listview.scrollTo(horizontal_listview.getScrollX() + ((width / 5) * (position)), horizontal_listview.getScrollY());

                        }
                    }, 100L);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*private void addlayouts(final LinearLayout dynamic_horizontalViews_layout, final ArrayList<ChannelCategoryBean> listData) {
        final View itemlayout = (View) inflate.inflate(R.layout.allchannels_layout, null);
        horizontal_listview_item_count = (TextView) itemlayout.findViewById(R.id.horizontal_listview_item_count);
        final LinearLayout recycler_layout = (LinearLayout) itemlayout.findViewById(R.id.recycler_layout);
        final LinearLayout image_view = (LinearLayout) itemlayout.findViewById(R.id.image_view);
        final ImageView left_slide = (ImageView) itemlayout.findViewById(R.id.left_slide);
        final ImageView right_slide = (ImageView) itemlayout.findViewById(R.id.right_slide);
        ImageView horizontal_listview_channel_close = (ImageView) itemlayout.findViewById(R.id.horizontal_listview_channel_close);
        horizontal_channel_list = (RecyclerView) itemlayout.findViewById(R.id.horizontal_channel_list);
        horizontal_channel_list_progressBar = (ProgressBar) itemlayout.findViewById(R.id.horizontal_channel_list_progressBar);
        horizontal_listview = (RecyclerView) itemlayout.findViewById(R.id.horizontal_listview);
        final LinearLayoutManager linear_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_listview.hasFixedSize();
        horizontal_listview.setLayoutManager(linear_layoutManager);
        horizontal_listview_channel_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).mHidetoolbar(false);
                layout_channelviewby_list.setVisibility(View.VISIBLE);
                layout_all_channelview.setVisibility(View.GONE);
            }
        });
        listData.remove(0);

        int spanCount = listData.size(); // 3 columns
        spacing = 20; // 50px


        boolean includeEdge = false;
        horizontal_listview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        int layoutwidth = recycler_layout.getLayoutParams().width;
        Log.d("layoutwidth::", "layoutwidth" + layoutwidth);

        try {
            ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    width = recycler_layout.getMeasuredWidth();
                    height = recycler_layout.getMeasuredHeight();
                    if (horiadpater == null) {
                        horiadpater = new AllChannelListAdapter(listData, getContext(), width - (spacing * 3), ChannelFragment.this);
                        horizontal_listview.setAdapter(horiadpater);
                    } else {
                        horiadpater.notifyDataSetChanged();
                    }

                }


            });


            aBoolean = false;
            horizontal_listview_item_count.setVisibility(View.INVISIBLE);
            horizontal_channel_list.setVisibility(View.INVISIBLE);
            horizontal_channel_list_progressBar.setVisibility(View.VISIBLE);
            loadallChannel(listData.get(0).getmChannelcategoryId());


            right_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int firstVisiblePosition = linear_layoutManager.findLastCompletelyVisibleItemPosition();
                    firstVisiblePosition = firstVisiblePosition + 4;
                    Log.d("list::", "::" + firstVisiblePosition + ":::" + linear_layoutManager.findFirstVisibleItemPosition() + "::" + linear_layoutManager.findLastVisibleItemPosition() + "::" + linear_layoutManager.findLastCompletelyVisibleItemPosition());
                    horizontal_listview.smoothScrollToPosition(firstVisiblePosition);
                    if (firstVisiblePosition - 4 == linear_layoutManager.findLastVisibleItemPosition()) {

                        left_slide.setImageResource(R.drawable.prev_active);
                        right_slide.setImageResource(R.drawable.next_inactive);
                    } else {
                        left_slide.setImageResource(R.drawable.prev_active);
                        right_slide.setImageResource(R.drawable.next_active);
                    }
                }
            });

            left_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int firstVisiblePosition = linear_layoutManager.findFirstVisibleItemPosition();
                    firstVisiblePosition = firstVisiblePosition - 4;
                    Log.d("list::", "::" + firstVisiblePosition + ":::" + linear_layoutManager.findFirstVisibleItemPosition() + "::" + linear_layoutManager.findLastVisibleItemPosition() + "::" + linear_layoutManager.findLastCompletelyVisibleItemPosition());
                    if (firstVisiblePosition > 0) {
                        horizontal_listview.smoothScrollToPosition(firstVisiblePosition);
                        left_slide.setImageResource(R.drawable.prev_active);
                        right_slide.setImageResource(R.drawable.next_active);
                    } else {
                        horizontal_listview.smoothScrollToPosition(0);
                        left_slide.setImageResource(R.drawable.prev_inactive);
                        right_slide.setImageResource(R.drawable.next_active);

                    }


                }
            });
            horizontal_channel_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int firstVisiblePosition = linear_layoutManager.findLastCompletelyVisibleItemPosition();
                    firstVisiblePosition = firstVisiblePosition + 4;
                    Log.d("dx", "====" + firstVisiblePosition);
                    if (firstVisiblePosition > 0) {
                        left_slide.setImageResource(R.drawable.prev_active);
                        right_slide.setImageResource(R.drawable.next_active);
                    } else {
                        left_slide.setImageResource(R.drawable.prev_inactive);
                        right_slide.setImageResource(R.drawable.next_active);

                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamic_horizontalViews_layout.addView(itemlayout);
    }*/


    public class AllChannelAdapter extends RecyclerView.Adapter<AllChannelAdapter.DataObjectHolder> {
        ArrayList<ChannelModel> channelModels;
        Context context;

        public AllChannelAdapter(ArrayList<ChannelModel> channelModels, Context context) {
            this.channelModels = channelModels;
            this.context = context;

        }

        public void setItem(ArrayList<ChannelModel> channelModels) {

            this.channelModels = channelModels;
        }

        @Override
        public AllChannelAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.layout_allchannel_grid, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(AllChannelAdapter.DataObjectHolder holder, final int position) {
            Log.d("cha_images",channelModels.get(position).getChannelLogo());
            Log.d("cha_images", channelModels.get(position).getChannelName());
//            holder.imageView.loadImage(channelModels.get(position).getChannelLogo());
            holder.txtChannelName.setText(channelModels.get(position).getChannelName());
            horizontal_listview_item_count.setVisibility(View.VISIBLE);
            horizontal_listview_item_count.setText(m_channelSchedule.size() + " " + "Channels found");

            Picasso.with(getContext()
                    .getApplicationContext())
                    .load(channelModels.get(position).getChannelLogo())
                    .fit().centerInside()
                    .placeholder(R.drawable.loader_show).into(holder.imageView);

            holder.item_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*((HomeActivity) getActivity()).mHidetoolbar(false);*/
                   /* layout_all_channelview.setVisibility(View.INVISIBLE);
                    layout_channelviewby_list.setVisibility(View.VISIBLE);*/
                    dialog.dismiss();

                    try {
                        channelSelected = true;


                        for (int l = 0; l < m_channelSchedule.size(); l++) {
                            if (channelModels.get(position).getChannelId() == m_channelSchedule.get(l).getChannelId()) {
                                if (m_channelSchedule.size() != 0) {
                                    ChannelModel model1 = m_channelSchedule.get(l);
                                    m_channelSchedule.remove(l);
                                    m_channelSchedule.add(0, model1);
                                    channelSelected = false;
                                    //setLiveData(model1.channelId, model1.channelName);
                                    setLiveData(channelModels.get(position).getChannelId(), channelModels.get(position).getChannelName());
                                }
                            }
                        }
                        if (channelSelected) {
                            setLiveData(channelModels.get(position).channelId, channelModels.get(position).getChannelName());
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return channelModels.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView imageView;
            TextView txtChannelName;
            LinearLayout item_grid;

            public DataObjectHolder(View itemView) {
                super(itemView);
                item_grid = (LinearLayout) itemView.findViewById(R.id.item_grid);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageShowThumbnail);
                txtChannelName = (TextView) itemView.findViewById(R.id.txtChannelName);

                Utilities.setViewFocus(getActivity(), item_grid);
            }
        }
    }


    public class AllChannelListAdapter extends RecyclerView.Adapter<AllChannelListAdapter.DataObjectHolder> {

        private final ArrayList<ChannelCategoryBean> listData;
        private final Context context;
        private final int width;
        GridAdapter.onGridSelectedListener listener;

        public AllChannelListAdapter(ArrayList<ChannelCategoryBean> listData, Context context, int i, GridAdapter.onGridSelectedListener listener) {
            this.listData = listData;
            this.context = context;
            this.width = i;
            this.listener = listener;
        }

        @Override
        public AllChannelListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.horizontal_channel_list, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final AllChannelListAdapter.DataObjectHolder holder, final int position) {
            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(width / 5, LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.horizontal_imageView.setLayoutParams(vp);
            holder.horizontal_imageView.setText(listData.get(position).getmChannelCategoryName());


            if (position == horizontal_position) {
                holder.horizontal_imageView.setBackgroundResource(R.drawable.menubg);
//                horizontal_listview.smoothScrollToPosition(horizontal_position);
            } else {
                holder.horizontal_imageView.setBackgroundResource(0);
            }

            holder.horizontal_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    horizontal_position = position;
                    holder.horizontal_imageView.setBackgroundResource(R.drawable.menubg);
                    notifyDataSetChanged();
//                    notifyItemChanged(position);

//                    notifyItemChanged(horizontal_position);
                    horizontal_listview_item_count.setVisibility(View.INVISIBLE);
                    horizontal_channel_list.setVisibility(View.INVISIBLE);
                    horizontal_channel_list_progressBar.setVisibility(View.VISIBLE);
                    /*int positionTemp = horizontal_position;
                    horizontal_position = position;
                    notifyItemChanged(positionTemp);
                    notifyItemChanged(horizontal_position);*/


                    loadallChannel(listData.get(position).getmChannelcategoryId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            TextView horizontal_imageView;

            public DataObjectHolder(View itemView) {
                super(itemView);
                horizontal_imageView = (TextView) itemView.findViewById(R.id.horizontal_imageView);

                Utilities.setViewFocus(getActivity(), horizontal_imageView);
            }
        }
    }

    @Override
    public void onShowGridItemSelected(int id, String type, String name) {

    }


    private void loadallChannel(int nID) {
        nCategory = nID;
        new LoadingAllChannelTask(true).execute();
    }


    private class LoadingAllChannelTask extends AsyncTask<Object, Object, Object> {
        private boolean showDialog;
        int channelId;
              String  channelName,
                channelLogo;
                       JSONArray channelVideos;

        public LoadingAllChannelTask() {
        }

        public LoadingAllChannelTask(boolean showDialog) {
            this.showDialog = showDialog;
        }

        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            try {
                loadingtimeOffset=0;
                /*if(showDialog) {
                    mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
                }*/
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                if (nCategory != -1)
                    m_jsonLiveItems = JSONRPCAPI.getChannels(nCategory);
                if (m_jsonLiveItems == null) return null;


                Log.d("getChannels::", ":::" + m_jsonLiveItems.toString());
                String IDs = "";

                for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                    JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                    if (IDs.length() == 0)
                        IDs = obj.getInt("id") + "";
                    else
                        IDs = IDs + "," + obj.getInt("id");
                }
                nTimeOffset = 0;
                JSONObject objSchedule = JSONRPCAPI.getScheduller(IDs, loadingtimeOffset, nTimeLimit);
                if (objSchedule == null) return null;
                Log.d("getScheduller5::", ":::" + objSchedule.toString());
                m_channelSchedule=new ArrayList<>();
                for (int i = 0; i < m_jsonLiveItems.length(); i++) {
                    JSONObject obj = m_jsonLiveItems.getJSONObject(i);
                    IDs = obj.getInt("id") + "";
                    ChannelModel model = new ChannelModel();
                    channelId = obj.getInt("id");
                    channelName = obj.getString("name");
                    channelLogo = obj.getString("big_logo_url");
                    channelVideos = objSchedule.getJSONObject(IDs).getJSONArray("videos").getJSONArray(0);
                    m_channelSchedule.add(new ChannelModel(channelName,channelId,channelLogo,channelVideos));


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try {
                horizontal_channel_list.setVisibility(View.VISIBLE);
                horizontal_channel_list_progressBar.setVisibility(View.GONE);

                if(m_channelSchedule!=null&&m_channelSchedule.size()!=0)
                {



                        allChannelAdapter = new AllChannelAdapter(m_channelSchedule, getActivity());
                        horizontal_channel_list.setAdapter(allChannelAdapter);

                }



            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }


    public void mShowsChannelList() {

        try {

            JSONObject data_object = new JSONObject(arra_list);
            m_channelSchedule=new ArrayList<>();
            if (data_object.has("data")) {
                mSearch_Trailors = data_object.getJSONArray("data");
                for (int i = 0; i < mSearch_Trailors.length(); i++) {
                    JSONObject url = mSearch_Trailors.getJSONObject(i);
                    ChannelModel model = new ChannelModel();
                    if (url.has("id")) {
                        model.channelId = url.getInt("id");
                    }

                    if (url.has("image")) {
                        model.channelLogo = url.getString("image");
                    }

                    if (url.has("name")) {
                        model.channelName = url.getString("name");

                    }
                    if (url.has("videos")) {

                        model.channelVideos = url.getJSONArray("videos");
                        m_jsonArrayChannelVideos = model.channelVideos;

                    }


                    m_channelSchedule.add(model);

                }
            }

            if (player != null) {

                int timeOffset = 0;
                try {
                    long u_time=m_jsonArrayChannelVideos.getJSONObject(0).getInt("unix_time");
                    timeOffset = getTimeOffsetbyunixtime(u_time);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    player.loadVideos(currentVideoList, 0, timeOffset * 1000);
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
                    prevStartTime = m_jsonArrayChannelVideos.getJSONObject(0).getInt("unix_time");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //m_jsonArrayStreams.remove(0);

                removeFirstChannel();
                if (m_jsonArrayChannelVideos != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            streamAdapter = new JSONAdapter(getActivity(), m_jsonArrayChannelVideos);
                            listStreams.setAdapter(streamAdapter);
                        }
                    });

                }
                //streamAdapter.notifyDataSetChanged();
            }


            streamAdapter = new JSONAdapter(getActivity(), m_jsonArrayChannelVideos);
            listStreams.setAdapter(streamAdapter);

            currentVideoList.clear();

            for (int i = 0; i < m_jsonArrayChannelVideos.length(); i++) {
                try {
                    String curUrl = m_jsonArrayChannelVideos.getJSONObject(i).getString("url");
                    if (!TextUtils.isEmpty(curUrl)) {
                        currentVideoList.add(curUrl);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            listStreams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        String strUrl = null;
                        String strTitle = null;
                        try {
                            if (m_jsonArrayChannelVideos != null) {
                                JSONObject jObj = m_jsonArrayChannelVideos.getJSONObject(position);
                                strTitle = jObj.getString("title");
                                strUrl = jObj.getString("url");

                                if (!TextUtils.isEmpty(strUrl)) {
                                    if (demanddialog != null && demanddialog.isShowing()) {
                                        demanddialog.dismiss();
                                    }

                                    demanddialog = new OnDemandDialog(getActivity());
                                    demanddialog.setInfo(strTitle, strUrl, position);
                                    demanddialog.setActivity(getActivity());
                                    demanddialog.show();
                                } else {
                                    Toast.makeText(getActivity(), "No url Found", Toast.LENGTH_SHORT).show();
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void mStopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onResume() {
        try {
            mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                    MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
            youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private boolean checkIsTablet() {
        String inputSystem;
        inputSystem = android.os.Build.ID;
        Log.d("hai", inputSystem);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        Log.d("hai", width + "");
        Log.d("hai", height + "");
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(width / dm.xdpi, 2);
        double y = Math.pow(height / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Log.d("hai", "Screen inches : " + screenInches + "");
        if (screenInches > 6.0) {
            return true;
        } else {
            return false;
        }

    }



    public void onCast() {


        if (mSelectedDevice == null) {
            String sNames[] = new String[mRouteNames.size()];
            for (int i = 0; i < mRouteNames.size(); i++) {
                sNames[i] = mRouteNames.get(i);
            }
            /*Dialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Choose a device")
                    .setNegativeButton("Cancel", null)
                    .setItems(sNames, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMediaRouter.selectRoute(mRouteInfos.get(which));

                            *//*imageCast.setBackground(getResources().getDrawable(R.drawable.icon_cast_pending));
                            AnimationDrawable background = (AnimationDrawable) imageCast.getBackground();
                            background.start();
*//*

//                            mCastButton.setBackgroundResource(R.drawable.icon_cast_pending);
                            // Get the background, which has been compiled to an AnimationDrawable object.
//                            AnimationDrawable frameAnimation = (AnimationDrawable) mCastButton.getBackground();
//
//                            // Start the animation (looped playback by default).
//                            frameAnimation.start();
                        }
                    }).create();
            dialog.show();*/
        } else {


            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View volumeCastView = factory.inflate(
                    R.layout.dialog_cast, null);
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(volumeCastView);
            dialog.setTitle("Cast Dialog");

            dialog.findViewById(R.id.btn_cast_disconnect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    teardown(true);
                }
            });
            dialog.findViewById(R.id.btn_cast_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.cast_volume_seek);
            seekBar.setMax(100);
            seekBar.setProgress((int) (Cast.CastApi.getVolume(mApiClient) * 100));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int pos, boolean b) {
                    //pos
                    try {
                        Cast.CastApi.setVolume(mApiClient, (double) pos / 100.0);
                        mMediaChannel.setStreamVolume(mApiClient, (double) pos / 100.0);
                        //setVolume(pos);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
           // dialog.show();

        }
    }

    private class MyMediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo route) {
            super.onRouteAdded(router, route);

            Log.d("HomeActivity:::", "onRouteAdded");

            // Add route to list of discovered routes
            synchronized (this) {
                mRouteInfos.add(route);
                mRouteNames.add(route.getName() + " (" + route.getDescription() + ")");
            }

            //Toast.makeText(MainActivity.this, "Router Count:" + (mRouteCount + 1), Toast.LENGTH_LONG).show();
            if (++mRouteCount == 1) {
                // Show the button when a device is discovered.
                //mMediaRouteButton.setVisibility(View.VISIBLE);
//                mCastButton.setVisibility(View.VISIBLE);
                //   Toast.makeText(MainActivity.this, "cast icon", Toast.LENGTH_LONG).show();
                //   MenuItem item = mainTopMenu.findItem(R.id.action_cast);
                //   item.setVisible(true);
                //  invalidateOptionsMenu();
            }
        }

        @Override
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo route) {
            super.onRouteRemoved(router, route);

            Log.d("HomeActivity:::", "onRouteRemoved");
            //  Toast.makeText(MainActivity.this, "Router Removed", Toast.LENGTH_LONG).show();;
            HomeActivity.activity_homescreen_toolbar_logout.setImageResource(R.drawable.chromecast);
            teardown(true);
            synchronized (this) {
                for (int i = 0; i < mRouteInfos.size(); i++) {
                    MediaRouter.RouteInfo routeInfo = mRouteInfos.get(i);
                    if (routeInfo.equals(route)) {
                        mRouteInfos.remove(i);
                        mRouteNames.remove(i);
                        return;
                    }
                }
            }

            if (--mRouteCount == 0) {
                //    Toast.makeText(MainActivity.this, "router removed", Toast.LENGTH_LONG).show();
                // Hide the button if there are no devices discovered.
                //mMediaRouteButton.setVisibility(View.GONE);
//                mCastButton.setVisibility(View.GONE);
                // MenuItem item = mainTopMenu.findItem(R.id.action_cast);
                // item.setVisible(false);
                //invalidateOptionsMenu();
            }

        }

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            Log.d("HomeActivity:::", "onRouteSelected");
            // Handle route selection.
            //  Toast.makeText(MainActivity.this, "Route Selected", Toast.LENGTH_LONG).show();;
            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            launchReceiver();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            Log.d("HomeActivity:::", "onRouteUnselected: info=" + info);
            //   Toast.makeText(MainActivity.this, "Route Un Selected", Toast.LENGTH_LONG).show();;
            mSelectedDevice = null;
        }
    }

    void initCast() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mMediaRouter = MediaRouter.getInstance(getActivity());
        // Create a MediaRouteSelector for the type of routes your app supports
        mMediaRouteSelector = new MediaRouteSelector.Builder().addControlCategory(CastMediaControlIntent.categoryForCast(APP_ID)).build();
        // Create a MediaRouter callback for discovery events
        mMediaRouterCallback = new MyMediaRouterCallback();




        HomeActivity.activity_homescreen_chromecast.setRouteSelector(mMediaRouteSelector);


    }

    private void teardown(boolean selectDefaultRoute) {
        Log.d("channel:::", "teardown");
//        mCastButton.setBackgroundResource(R.drawable.icon_cast_identified);
        //MenuItem item = mainTopMenu.findItem(R.id.action_cast);
        //item.setIcon(getResources().getDrawable(R.drawable.icon_cast_identified));
        //imageCast.setBackground(getResources().getDrawable(R.drawable.icon_cast_identified));

        //  invalidateOptionsMenu();

        //youTubePlayerFragment.getView().setAlpha(1.0f);
        //youTubeView.setAlpha(1.0f);
        //setVolume(m_nVolume);
        if (mApiClient != null) {
            if (mApplicationStarted) {
                if (mApiClient.isConnected() || mApiClient.isConnecting()) {
                    try {
                        Cast.CastApi.stopApplication(mApiClient, mSessionId);
                        /*if (mHelloWorldChannel != null) {
                            Cast.CastApi.removeMessageReceivedCallbacks(
                                    mApiClient,
                                    mHelloWorldChannel.getNamespace());
                            mHelloWorldChannel = null;
                        }*/
                        if (mMediaChannel != null) {
                            Cast.CastApi.removeMessageReceivedCallbacks(
                                    mApiClient,
                                    mMediaChannel.getNamespace());
                            mMediaChannel = null;
                        }
                    } catch (IOException e) {
                        Log.e("channel:::", "Exception while removing channel", e);
                    }
                    mApiClient.disconnect();
                }
                mApplicationStarted = false;
            }
            mApiClient = null;
        }
        if (selectDefaultRoute) {
            mMediaRouter.selectRoute(mMediaRouter.getDefaultRoute());
        }
        mSelectedDevice = null;
        mWaitingForReconnect = false;
        mSessionId = null;

    }

    private void launchReceiver() {
        try {
            mCastListener = new Cast.Listener() {

                @Override
                public void onApplicationDisconnected(int errorCode) {
//                    mCastButton.setBackgroundResource(R.drawable.icon_cast_identified);
                    // MenuItem item = mainTopMenu.findItem(R.id.action_cast);
                    //item.setIcon(getResources().getDrawable(R.drawable.icon_cast_identified));
                    //imageCast.setBackground(getResources().getDrawable(R.drawable.icon_cast_identified));
                    // invalidateOptionsMenu();

                    //youTubePlayerFragment.getView().setAlpha(1.0f);
                    //youTubeView.setAlpha(1.0f);
                    //setVolume(m_nVolume);
                    Log.d("channel:::", "application has stopped");
                    teardown(true);
                }

            };
            // Connect to Google Play services
            mConnectionCallbacks = new ConnectionCallbacks();

            mConnectionFailedListener = new ConnectionFailedListener();
            Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
                    .builder(mSelectedDevice, mCastListener);
            mApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Cast.API, apiOptionsBuilder.build())
                    .addConnectionCallbacks(mConnectionCallbacks)
                    .addOnConnectionFailedListener(mConnectionFailedListener)
                    .build();

            mApiClient.connect();
        } catch (Exception e) {
            Log.e("channel:::", "Failed launchReceiver", e);
        }
    }

    private class ConnectionCallbacks implements
            GoogleApiClient.ConnectionCallbacks {

        @Override
        public void onConnected(Bundle connectionHint) {
            Log.d("channel:::", "onConnected");
            HomeActivity.activity_homescreen_toolbar_logout.setImageResource(R.drawable.chromecast_fill);
//            mCastButton.setBackgroundResource(R.drawable.icon_cast_casting);
            //  MenuItem item = mainTopMenu.findItem(R.id.action_cast);
            //imageCast.setBackground(getResources().getDrawable(R.drawable.icon_cast_casting));
            //item.setIcon(getResources().getDrawable(R.drawable.icon_cast_casting));

            //  invalidateOptionsMenu();


            if (mApiClient == null) {
                // We got disconnected while this runnable was pending
                // execution.
                return;
            }

            try {
                if (mWaitingForReconnect) {
                    mWaitingForReconnect = false;

                    // Check if the receiver app is still running
                    if ((connectionHint != null)
                            && connectionHint.getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {
                        Log.d("channel:::", "App  is no longer running");
                        teardown(true);
                    } else {
                        // Re-create the custom message channel
                        try {
                            /*Cast.CastApi.setMessageReceivedCallbacks(mApiClient,
                                    mApiClient,
                                    mHelloWorldChannel.getNamespace(),
                                    mHelloWorldChannel);*/
                            Cast.CastApi.setMessageReceivedCallbacks(mApiClient,
                                    mMediaChannel.getNamespace(),
                                    mMediaChannel);

                        } catch (IOException e) {
                            Log.e("channel:::", "Exception while creating channel", e);
                        }
                    }
                } else {
                    // Launch the receiver app
                    Cast.CastApi.launchApplication(mApiClient, APP_ID, false)
                            .setResultCallback(
                                    new ResultCallback<Cast.ApplicationConnectionResult>() {
                                        @Override
                                        public void onResult(
                                                Cast.ApplicationConnectionResult result) {
                                            Status status = result.getStatus();
                                            Log.d("channel:::",
                                                    "ApplicationConnectionResultCallback.onResult:"
                                                            + status.getStatusCode());
                                            if (status.isSuccess()) {
                                                ApplicationMetadata applicationMetadata = result
                                                        .getApplicationMetadata();
                                                mSessionId = result.getSessionId();
                                                String applicationStatus = result
                                                        .getApplicationStatus();
                                                boolean wasLaunched = result.getWasLaunched();
                                                Log.d("channel:::", "application name: "
                                                        + applicationMetadata.getName()
                                                        + ", status: " + applicationStatus
                                                        + ", sessionId: " + mSessionId
                                                        + ", wasLaunched: " + wasLaunched);
                                                mApplicationStarted = true;

                                                // Create the custom message
                                                // channel
                                                //mHelloWorldChannel = new HelloWorldChannel();
                                                mMediaChannel = new RemoteMediaPlayer();
                                                try {
                                                    /*Cast.CastApi.setMessageReceivedCallbacks(
                                                            mApiClient,
                                                            mHelloWorldChannel.getNamespace(),
                                                            mHelloWorldChannel);*/
                                                    Cast.CastApi.setMessageReceivedCallbacks(mApiClient,
                                                            mMediaChannel.getNamespace(),
                                                            mMediaChannel);
                                                } catch (IOException e) {
                                                    Log.e("channel:::", "Exception while creating channel",
                                                            e);
                                                    Toast.makeText(getActivity(), "Exception while creating channel", Toast.LENGTH_SHORT).show();
                                                }

                                                castVideoFromId(nStreamingId);
                                                cast=0;
                                                /*if (VIEW_MODE == VIEW_RADIOSTATION && nRadioType == RADIO_DETAIL_SCREEN) {

                                                    String stream = null, title = null, thumb = null, slug = null;
                                                    try {
                                                        stream = objSelectedRadio.getString("stream");
                                                        title = objSelectedRadio.getString("name");
                                                        thumb = objSelectedRadio.getString("image");
                                                        slug = objSelectedRadio.getString("slug");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    castRadio(title, slug, thumb, stream);

                                                } else
                                                    castVideoFromId(nStreamingId);*/
                                                // set the initial instructions
                                                // on the receiver
                                                //sendMessage(getString(R.string.instructions));
                                            } else {
                                                Log.e("channel::", "application could not launch");
                                                teardown(true);
                                            }
                                        }
                                    });
                }
            } catch (Exception e) {
                Log.e("channel::", "Failed to launch application", e);
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
//            mCastButton.setBackgroundResource(R.drawable.icon_cast_identified);
            // MenuItem item = mainTopMenu.findItem(R.id.action_cast);
            // item.setIcon(getResources().getDrawable(R.drawable.icon_cast_identified));
           // imageCast.setBackground(getResources().getDrawable(R.drawable.icon_cast_identified));
            // invalidateOptionsMenu();

            //youTubePlayerFragment.getView().setAlpha(1.0f);
          //  youTubeView.setAlpha(1.0f);
            //setVolume(m_nVolume);
            Log.d("channel:::", "onConnectionSuspended");
            mWaitingForReconnect = true;
        }
    }

    private class ConnectionFailedListener implements
            GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnectionFailed(ConnectionResult result) {
            Log.e("channel", "onConnectionFailed ");
            teardown(false);
        }
    }

    public boolean castVideoFromId(final int nId) {


        if (mApiClient == null || mMediaChannel == null) {
            //Toast.makeText(MainActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
            //youTubeView.setVisibility(View.VISIBLE);
            //youTubePlayerFragment.getView().setAlpha(1.0f);
            //setVolume(m_nVolume);
            Log.d("ChromeCast::::","castVideoFromId:::mApiClient");
            return false;
        }

        if (m_jsonArrayStreamsForCasting == null || m_jsonArrayStreamsForCasting.length() <= 0 || nId < 0 || m_jsonArrayStreamsForCasting.length() <= nId){
            Log.d("ChromeCast::::","castVideoFromId:::data null");
            return false;
        }


        // Toast.makeText(this, nId + " Video", Toast.LENGTH_LONG).show();
        //int nVolume = getVolume();
        /*if (nVolume > 0)
            m_nVolume = nVolume;
        setVolume(0);*/
        //youTubeView.setVisibility(View.INVISIBLE);
        // youTubePlayerFragment.getView().setAlpha(0);
        //youTubeView.setAlpha(0.0f);
        try {
            Log.d("ChromeCast::::","castVideoFromId:::generating stream");
            JSONObject jObj = m_jsonArrayStreamsForCasting.getJSONObject(nId);
            final String strTitle = jObj.getString("title");
            String strUrl = jObj.getString("url");

            final String sThumb = "http://img.youtube.com/vi/" + strUrl + "/default.jpg";

            Video video = new Video(strTitle, "http://www.youtube.com/watch?v=" + strUrl);


            YouTubeUriExtractor ytEx = new YouTubeUriExtractor(getActivity()) {
                @Override
                public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> sparseArray) {
                    if (sparseArray != null) {
                        int itag = 22;
                        String downloadUrl = "";
                        if (sparseArray.get(22) != null) {
                            downloadUrl = sparseArray.get(22).getUrl();
                        } else if (sparseArray.get(18) != null) {
                            downloadUrl = sparseArray.get(18).getUrl();
                        } else if (sparseArray.get(17) != null) {
                            downloadUrl = sparseArray.get(17).getUrl();
                        }
                        if (downloadUrl.length() > 0) {
                            if(cast==1){
                                castVideo(strTitle, null, sThumb, downloadUrl, 0);
                            }else{
                                castVideo(strTitle, null, sThumb, downloadUrl, getTimeOffsetFromId(nId) * 1000);
                            }
                            // if( bRunningThread == false )
                            //    startTestThread();
                        }
                    }
                }
            };
            ytEx.execute("http://www.youtube.com/watch?v=" + strUrl);

            /*VideoInfoSearch viSearch = new VideoInfoSearch(video);
            Map<String, String>videos = viSearch.getVideos();
            String sUrlVideo = "";
            if(videos != null && videos.size() > 0)
            {
                if( sUrlVideo == null || sUrlVideo.isEmpty() ){
                    sUrlVideo = videos.get("hd720");
                }
                if(sUrlVideo == null || sUrlVideo.isEmpty())
                {
                    sUrlVideo = videos.get("hd");
                }
                if(sUrlVideo == null || sUrlVideo.isEmpty())
                {
                    sUrlVideo = videos.get("medium");
                }



            }
            if( videos == null ){
                showDialog("videoID" + video.getVideoId());
                return false;
            }
            if( videos.size() <= 0 ){
                showDialog("Video Size < 0");
                return false;
            }
            if( sUrlVideo == null ){
                showDialog("sUrlVideo == null");;
                return false;
            }
            if( sUrlVideo.isEmpty() )
            {
                showDialog("sUrlVideo is Empty");
                return false;
            }
            if(videos == null || videos.size() <= 0 || sUrlVideo == null || sUrlVideo.isEmpty())
            {
                showDialog("Sorry, You can't cast this video.");
                setVolume(m_nVolume);
                youTubeView.setVisibility(View.VISIBLE);
                //youTubeView.setAlpha(1.0f);
                return false;
            }

            //String sVideo = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
            castVideo(strTitle, null, sThumb, sUrlVideo, getTimeOffsetFromId(nId) * 1000);*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean castVideoFromVideoId(final String VnId, final String strTitle) {


        if (mApiClient == null || mMediaChannel == null) {
            //Toast.makeText(MainActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
            //youTubeView.setVisibility(View.VISIBLE);
            //youTubePlayerFragment.getView().setAlpha(1.0f);
            Log.d("ChromeCast::::","castVideoFromVideoId:::mApiClient");
            //setVolume(m_nVolume);
            return false;
        }


        try {

            Log.d("ChromeCast::::","castVideoFromVideoId:::generating stream");

            final String sThumb = "http://img.youtube.com/vi/" + VnId + "/default.jpg";

            Video video = new Video(strTitle, "http://www.youtube.com/watch?v=" + VnId);


            YouTubeUriExtractor ytEx = new YouTubeUriExtractor(getActivity()) {
                @Override
                public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> sparseArray) {
                    if (sparseArray != null) {
                        int itag = 22;
                        String downloadUrl = "";
                        if (sparseArray.get(22) != null) {
                            downloadUrl = sparseArray.get(22).getUrl();
                        } else if (sparseArray.get(18) != null) {
                            downloadUrl = sparseArray.get(18).getUrl();
                        } else if (sparseArray.get(17) != null) {
                            downloadUrl = sparseArray.get(17).getUrl();
                        }
                        if (downloadUrl.length() > 0) {
                            if(cast==1){
                                castVideo(strTitle, null, sThumb, downloadUrl, 0);
                            }else{
                                castVideo(strTitle, null, sThumb, downloadUrl, 0);
                            }
                            // if( bRunningThread == false )
                            //    startTestThread();
                        }
                    }
                }
            };
            ytEx.execute("http://www.youtube.com/watch?v=" + VnId);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void castVideo(String sTitle, String sSubTitle, String sThumbUrl, String sUrlVideo, int nTimeOffset) {
        try {
            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
            if (sSubTitle != null && !sSubTitle.isEmpty())
                movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, sSubTitle);
            if (sTitle != null && !sTitle.isEmpty())
                movieMetadata.putString(MediaMetadata.KEY_TITLE, sTitle);
            movieMetadata.addImage(new WebImage(Uri.parse(sThumbUrl)));

            MediaInfo media = new MediaInfo.Builder(sUrlVideo)
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType("video/webM")
                    .setStreamDuration(0)
                    .setMetadata(movieMetadata)
                    .build();
            mMediaChannel.load(mApiClient, media, true, nTimeOffset, null, null);
        } catch (Exception e) {
            Log.e("channels::", "Exception while sending message", e);
            Toast.makeText(getActivity(), "Exception while sending message", Toast.LENGTH_SHORT).show();
            showDialog(e.getLocalizedMessage());
        }
    }
    void showDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Message")
                .setMessage(text)
                .setPositiveButton("OK", null);
        builder.create().show();
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

    void setVolume(int nVolume) {
        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        am.setStreamVolume(AudioManager.STREAM_MUSIC, nVolume, 0);
    }

    int getVolume() {
        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        return am.getStreamVolume(AudioManager.STREAM_MUSIC);
    }



    private class PaginationLoadingTask extends AsyncTask<Object, Integer, Object> {
        ProgressHUD mProgressHUD;
        int channelId;
        String channelName,
                channelLogo;
        JSONArray channelVideos;

        @Override
        protected void onPreExecute() {
            //  mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
            loadingtimeOffset+=nTimeLimit;
            loadingtimeOffset++;
            loadpage=false;
            HomeActivity.IDs="";
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {



                Log.d("Channels::", ":::" + HomeActivity.m_jsonLiveItems.toString());


                String IDs = "";

                for (int i = 0; i < HomeActivity.m_jsonLiveItems.length(); i++) {
                    JSONObject obj = HomeActivity.m_jsonLiveItems.getJSONObject(i);
                    if (HomeActivity.IDs.length() == 0)
                        HomeActivity.IDs = obj.getInt("id") + "";
                    else
                        HomeActivity.IDs = HomeActivity.IDs + "," + obj.getInt("id");
                }
                nTimeOffset = 0;
                JSONObject scheduller_array= JSONRPCAPI.getScheduller(HomeActivity.IDs, loadingtimeOffset, nTimeLimit);
                if (scheduller_array== null) return null;
                Log.d("getScheduller1::", ":::" + scheduller_array.toString());
                if (scheduller_array!= null) {
                    for (int i = 0; i < HomeActivity.m_jsonLiveItems.length(); i++) {
                        JSONObject obj = HomeActivity.m_jsonLiveItems.getJSONObject(i);
                        HomeActivity.IDs = obj.getInt("id") + "";
                        ChannelModel model = new ChannelModel();
                        model.channelId = obj.getInt("id");
                        int c_id=obj.getInt("id");




                        model.channelName = obj.getString("name");
                        model.channelLogo = obj.getString("big_logo_url");
                        model.channelVideos = scheduller_array.getJSONObject(HomeActivity.IDs).getJSONArray("videos").getJSONArray(0);
                        JSONArray data_arr=scheduller_array.getJSONObject(HomeActivity.IDs).getJSONArray("videos").getJSONArray(0);

                        for(int j=0;j<m_channelSchedule.size();j++){
                            if(m_channelSchedule.get(j).getChannelId()==c_id){
                                if (HomeActivity.channelID == 1) {
                                    //for Music videos from listen section
                                    if (obj.getInt("id") == 329) {
                                        for(int k=0;k<data_arr.length();k++){
                                            m_channelSchedule.get(j).getChannelVideos().put(0,data_arr.getJSONObject(k));
                                        }
                                    } else {
                                        for(int k=0;k<data_arr.length();k++){
                                            m_channelSchedule.get(j).getChannelVideos().put(data_arr.getJSONObject(k));
                                        }
                                    }
                                } else {
                                    for(int k=0;k<data_arr.length();k++){
                                        m_channelSchedule.get(j).getChannelVideos().put(data_arr.getJSONObject(k));
                                    }

                                }


                            }
                        }





                    }

makeChannelList();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            //  mProgressHUD.dismiss();
            addtoChannelDescList();



        }
    }


    public static void showBackIcon(boolean b)
    {
        if(b)
        {
            channel_prev_button.setVisibility(View.VISIBLE);
        }else
        {
            channel_prev_button.setVisibility(View.INVISIBLE);

        }

    }





}
