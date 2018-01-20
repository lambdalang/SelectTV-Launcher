package com.selecttvlauncher.channels;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.analytics.Tracker;
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
import com.selecttvlauncher.Adapter.GridAdapter;
import com.selecttvlauncher.Adapter.Video;
import com.selecttvlauncher.BeanClass.ChannelNavigation;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.activities.YoutubeDialogActivity;
import com.selecttvlauncher.ui.dialogs.ProgressHUD;
import com.selecttvlauncher.ui.fragments.FragmentHomeScreenGrid;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;


public class ChannelTotalFragment extends Fragment implements GridAdapter.onGridSelectedListener, YoutubeDataListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1 = "";
    private String mParam2;
    private String mParamSlug;
    private String mParamMainCategory;
    private boolean ismSearch;
    private String mChannelSlug;

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    private ChannelTotalListener mListener;
    public static String mtoolbarMaincontent = "", mtoolbarSubContent = "", mtoolbarcontent = "", mtoolbarlastcontent = "";
    private RelativeLayout layout_channelviewby_list, linearFullScreen, spinner_layout, arrow_layout, layout_all_channelview;
    private ImageView splash_logo, prev, list_downscroll_arrow, horizontal_listview_channel_close, left_slide, right_slide;
    public static ImageView channel_prev_button;
    private TextView txt_splash_title, txt_splash_progress, horizontal_listview_title, horizontal_listview_item_count;
    private RoundCornerProgressBar splash_progress;
    private LinearLayout content_layout, dynamic_horizontalViews_layout, horizontal_listview_mainlayout, recycler_layout, image_view, linearSubMainRight, layoutSideMenusContent;
    private Spinner spinner_actors;
    private RecyclerView fragment_ondemand_alllist_items, horizontal_channel_list;
    private ProgressBar progressBar_center, progressBar, horizontal_channel_list_progressBar;
    private Dialog dialog;
    private FrameLayout player_layout;
    private HorizontalScrollView horizontal_listview;
    private FrameLayout timeLineLayout;

    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private LinearLayoutManager mLayoutManager;
    //    YouTubePlayerSupportFragment youTubePlayerFragment;
    private PlayerYouTubeFrag playerfragment;

    public String szSplashTitle = "";
    int mMainCategorySelectedItem = 0;
    int mSubCategorySelectedItem = 0;

    private String subCategoryAffix = "";
    public static String mChannelList;

    ArrayList<String> arrayActorAdapterData = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    public static Boolean isBackClicked = false;
    private int horizontal_position = 0;
    private String mCategorySelectedItem = "";
    private String mCategorySelectedHeader = "";
    private ArrayList<ChannelCategoryList> allchannelList = new ArrayList<>();

    public static ArrayList<ChannelCategoryList> temp_list = new ArrayList<>();
    boolean bChannelUp = true;

    /*chromecast*/
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
    private Handler handler = new Handler();
    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks;
    private ConnectionFailedListener mConnectionFailedListener;
    private int nStreamingId = 0;

    public void setVideoSchedule(ArrayList<ProgramList> videoSchedule) {
        this.videoSchedule = videoSchedule;
    }

    public ArrayList<ProgramList> videoSchedule = new ArrayList<>();

    public void setChannelSchedule(ArrayList<ChannelScheduler> channelSchedule) {
        this.channelSchedule = channelSchedule;
    }

    public ArrayList<ChannelScheduler> channelSchedule = new ArrayList<>();


    public ChannelTotalFragment() {
        // Required empty public constructor
    }

    private boolean firstcall = true;

    private HashMap<String, ChannelNavigation> back_navigationData = new HashMap<>();
    private List<String> bredcrumb = new ArrayList<>();

    private Timer timer;
//    Handler menu_handler = new Handler();

    Runnable run_menu;
    private boolean isYoutubeFullscreen = false;

    private Tracker mTracker;

    public static ChannelTotalFragment newInstance(String param1, String param2) {
        ChannelTotalFragment fragment = new ChannelTotalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            mParamSlug = getArguments().getString(ARG_PARAM1);
            mParamMainCategory = getArguments().getString(ARG_PARAM2);
            mChannelSlug = getArguments().getString(ARG_PARAM4);
            ismSearch = getArguments().getBoolean(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_total, container, false);
        back_navigationData.clear();
        bredcrumb.clear();
        fragmentManager = getChildFragmentManager();

        ((HomeActivity) getActivity()).setmChannelTotalFragment(this);

        ((HomeActivity) getActivity()).toolbarTextChange("Channels", "", "", "");

        initializeCast();

        layout_channelviewby_list = (RelativeLayout) view.findViewById(R.id.layout_channelviewby_list);
        spinner_layout = (RelativeLayout) view.findViewById(R.id.spinner_layout);
        arrow_layout = (RelativeLayout) view.findViewById(R.id.arrow_layout);
        list_downscroll_arrow = (ImageView) view.findViewById(R.id.list_downscroll_arrow);
        channel_prev_button = (ImageView) view.findViewById(R.id.channel_prev_button);
        spinner_actors = (Spinner) view.findViewById(R.id.spinner_actors);
        fragment_ondemand_alllist_items = (RecyclerView) view.findViewById(R.id.fragment_ondemand_alllist_items);
        txt_splash_title = (TextView) view.findViewById(R.id.txt_splash_title);
        txt_splash_progress = (TextView) view.findViewById(R.id.txt_splash_progress);
        splash_progress = (RoundCornerProgressBar) view.findViewById(R.id.splash_progress);
        linearFullScreen = (RelativeLayout) view.findViewById(R.id.linearFullScreen);
        content_layout = (LinearLayout) view.findViewById(R.id.content_layout);
        linearSubMainRight = (LinearLayout) view.findViewById(R.id.linearSubMainRight);
        layoutSideMenusContent = (LinearLayout) view.findViewById(R.id.layoutSideMenusContent);
        txt_splash_title = (TextView) view.findViewById(R.id.txt_splash_title);
        txt_splash_progress = (TextView) view.findViewById(R.id.txt_splash_progress);
        player_layout = (FrameLayout) view.findViewById(R.id.youtube_fragment);
        timeLineLayout = (FrameLayout) view.findViewById(R.id.timeline_frame);


        dialog = new Dialog(getActivity(), R.style.MY_DIALOG);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.browse_channel_dialog);
        dialog.setCancelable(false);

        dynamic_horizontalViews_layout = (LinearLayout) dialog.findViewById(R.id.dynamic_horizontalViews_layout);
        layout_all_channelview = (RelativeLayout) dialog.findViewById(R.id.layout_all_channelview);
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

        txt_splash_title.setTypeface(OpenSans_Regular);
        txt_splash_progress.setTypeface(OpenSans_Regular);

        splash_progress = (RoundCornerProgressBar) view.findViewById(R.id.splash_progress);
        linearFullScreen = (RelativeLayout) view.findViewById(R.id.linearFullScreen);
        content_layout = (LinearLayout) view.findViewById(R.id.content_layout);

        progressBar_center = (ProgressBar) view.findViewById(R.id.progressBar_center);


        mLayoutManager = new LinearLayoutManager(getActivity());
        fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
        fragment_ondemand_alllist_items.hasFixedSize();

        splash_progress.setProgressColor(Color.parseColor("#fce623"));
        splash_progress.setBackgroundColor(0);
        splash_progress.setProgressBackgroundColor(Color.parseColor("#cecece"));
        splash_progress.setRadius(30);
        splash_progress.setMax(100);


        // ((HomeActivity) getActivity()).setmChannelFragment(this);
        szSplashTitle = "Loading Categories...";

//        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

//        youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, this);
//        playerStateChangeListener = new MyPlayerStateChangeListener();
//        playbackEventListener = new MyPlaybackEventListener();
//        mPlaylistEventListener = new MyPlaylistEventListener();
////        displaycontent(false);
//        new LoadingCategoryTask().execute();
        loadmainCategories();

        channel_prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goback();
            }
        });

        return view;
    }

    public void displaycontent(boolean dis) {
        try {
            if (dis) {
                ((HomeActivity) getActivity()).mHidetoolbar(false);
                linearFullScreen.setVisibility(View.GONE);
                content_layout.setVisibility(View.VISIBLE);
            } else {
                ((HomeActivity) getActivity()).mHidetoolbar(true);
                linearFullScreen.setVisibility(View.VISIBLE);
                content_layout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goback() {
        try {
            int dataSize = bredcrumb.size();
            if (dataSize > 0) {
                try {
                    if (dataSize == 2) {
                        ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, "", "");
                        mCategorySelectedHeader = "";
                        mtoolbarlastcontent = "";
                    } else {
                        ((HomeActivity) getActivity()).toolbarTextChange("Channels", "", "", "");
                        mCategorySelectedHeader = "";
                        mtoolbarlastcontent = "";
                        mtoolbarMaincontent = "";

                    }
                    ChannelNavigation cn = (ChannelNavigation) back_navigationData.get(bredcrumb.get(dataSize - 1));
                    if (cn.getType().equalsIgnoreCase("sub")) {
                        ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelSubCategoryAdapter((ArrayList<ChannelCategoryList>) cn.getData_object(), getActivity(), cn.getSelected_pos());
                        fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
                    } else if (cn.getType().equalsIgnoreCase("main")) {
                        showBackIcon(false);
                        ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter((ArrayList<ChannelCategoryList>) cn.getData_object(), getActivity(), cn.getSelected_pos());
                        fragment_ondemand_alllist_items.setAdapter(channelCategoryAdapter);
                    }
                    back_navigationData.remove(bredcrumb.get(dataSize - 1));
                    bredcrumb.remove(dataSize - 1);
                } catch (Exception e) {
                    e.printStackTrace();

                    ChannelNavigation cn = (ChannelNavigation) back_navigationData.get(bredcrumb.get(0));
                    if (cn.getType().equalsIgnoreCase("sub")) {
                        ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelSubCategoryAdapter((ArrayList<ChannelCategoryList>) cn.getData_object(), getActivity(), cn.getSelected_pos());
                        fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
                    } else if (cn.getType().equalsIgnoreCase("main")) {
                        ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter((ArrayList<ChannelCategoryList>) cn.getData_object(), getActivity(), cn.getSelected_pos());
                        fragment_ondemand_alllist_items.setAdapter(channelCategoryAdapter);
                    }
                    back_navigationData.clear();
                    bredcrumb.clear();
                }
            } else {
                HomeActivity.isPayperview = "";
                ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                fragmentTransaction.commit();
            }
            setAnalyticreport();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void goback(String str) {
        try {
            int dataSize = bredcrumb.size();
            if (dataSize > 0) {
                try {
                    ChannelNavigation cn = (ChannelNavigation) back_navigationData.get(str);
                    if (cn.getType().equalsIgnoreCase("sub")) {
                        ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelSubCategoryAdapter((ArrayList<ChannelCategoryList>) cn.getData_object(), getActivity(), cn.getSelected_pos());
                        fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
                    } else if (cn.getType().equalsIgnoreCase("main")) {
                        ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter((ArrayList<ChannelCategoryList>) cn.getData_object(), getActivity(), cn.getSelected_pos());
                        fragment_ondemand_alllist_items.setAdapter(channelCategoryAdapter);
                    }
                    back_navigationData.remove(bredcrumb.get(dataSize - 1));
                    bredcrumb.remove(dataSize - 1);
                } catch (Exception e) {
                    e.printStackTrace();

                    ChannelNavigation cn = (ChannelNavigation) back_navigationData.get(bredcrumb.get(0));
                    if (cn.getType().equalsIgnoreCase("sub")) {
                        ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelSubCategoryAdapter((ArrayList<ChannelCategoryList>) cn.getData_object(), getActivity(), cn.getSelected_pos());
                        fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
                    } else if (cn.getType().equalsIgnoreCase("main")) {
                        ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter((ArrayList<ChannelCategoryList>) cn.getData_object(), getActivity(), cn.getSelected_pos());
                        fragment_ondemand_alllist_items.setAdapter(channelCategoryAdapter);
                    }
                    back_navigationData.clear();
                    bredcrumb.clear();
                }
            } else {
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

    public void toolbarMainClick() {
        try {
            ((HomeActivity) getActivity()).toolbarTextChange("Channels", "", "", "");
            mChannelList = "";
            spinner_layout.setVisibility(View.GONE);
            arrow_layout.setVisibility(View.GONE);
            for (int i = bredcrumb.size() - 1; i > 0; i--) {
                back_navigationData.remove(i);
                bredcrumb.remove(i);
            }

            goback();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void toolbarClickAction(String s) {
        try {
            mChannelList = Constants.CHANNEL_MAINCONTENT;
            spinner_layout.setVisibility(View.GONE);
            arrow_layout.setVisibility(View.GONE);

            try {
                goback(s);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChannelTotalListener) {
            mListener = (ChannelTotalListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ChannelTotalListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("fragment::", "::detached");
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("fragment::", "::destroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStopTimer();
        clearFragments();
        if (handler != null)
            handler.removeCallbacks(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("fragment::", "::paused");
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            fullScreen(View.VISIBLE);
            Log.d("fragment::", "::resumed");
            mTracker = LauncherApplication.getInstance().getDefaultTracker();
            Utilities.setAnalytics(mTracker, "Channels");
            mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                    MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onShowGridItemSelected(int id, String type, String name) {

    }

    public boolean isYoutubeFullscreen() {
        return isYoutubeFullscreen;
    }

    @Override
    public void loadYoutubeVideo(String url, String title, int offset) {

    }

    @Override
    public ArrayList<ProgramList> getProgramList(String mSlug) {
        return null;
    }

    class Blink_progress extends TimerTask {
        @Override
        public void run() {
            if (getActivity() != null) {
                handler.post(new Runnable() {

                    public void run() {
                        fullScreen(View.GONE);
                    }

                });
            }
        }
    }


    public void loadYoutubevideos(List<String> list, int offset) {
    }

    public void loadChannelList() {
        try {
            if (!firstcall) {
                if (channelSchedule.size() > 0) {
                    ArrayList<ChannelScheduler> listscheduleData = new ArrayList<>();
                    listscheduleData.addAll(channelSchedule);

                    AlertChannelDialogListAdapter mAlertChannelDialogListAdapter = new AlertChannelDialogListAdapter(getActivity(), listscheduleData, 0);
                    fragment_ondemand_alllist_items.setAdapter(mAlertChannelDialogListAdapter);
                    fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
                }
            }
            firstcall = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startTimer() {
        try {
            mStopTimer();
            timer = new Timer();
            timer.schedule(new Blink_progress(), 2 * 60000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mStopTimer() {
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void watchvideo(String strUrl, String strTitle) {
        try {
            if (mApiClient != null || mMediaChannel != null) {
                castVideoFromVideoId(strUrl, strTitle);
            } else {
               /* Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                        getActivity(), Constants.DEVELOPER_KEY, strUrl, 0, true, false);
                if (intent != null) {
                    if (canResolveIntent(intent)) {
                        getActivity().startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                    } else {
                        // Could not resolve the intent - must need to install or update the YouTube API service.
                        YouTubeInitializationResult.SERVICE_MISSING
                                .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
                    }
                }*/
                Intent in = new Intent(getActivity(), YoutubeDialogActivity.class);
                in.putExtra("video_id", strUrl);
                startActivity(in);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean castVideoFromVideoId(final String VnId, final String strTitle) {


        if (mApiClient == null || mMediaChannel == null) {
            //Toast.makeText(MainActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
            //youTubeView.setVisibility(View.VISIBLE);
            //youTubePlayerFragment.getView().setAlpha(1.0f);
            Log.d("ChromeCast::::", "castVideoFromVideoId:::mApiClient");
            //setVolume(m_nVolume);
            return false;
        }


        try {

            Log.d("ChromeCast::::", "castVideoFromVideoId:::generating stream");

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
                            castVideo(strTitle, null, sThumb, downloadUrl, 0);
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

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    public void updateloading(int progres, String progresstext, String message) {
        try {

            splash_progress.setProgress(progres);
            int fPos = progres;
            txt_splash_progress.setText((int) fPos + "%");
            txt_splash_title.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshCategoryList(String cID) {
        try {
            for (int i = 0; i < HomeActivity.channelMainCategoryNew.size(); i++) {
                if (cID.equalsIgnoreCase(HomeActivity.channelMainCategoryNew.get(i).getSlug())) {
                    ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter(HomeActivity.channelMainCategoryNew, getActivity(), i);
                    fragment_ondemand_alllist_items.setAdapter(channelCategoryAdapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public interface ChannelTotalListener {
        // TODO: Update argument type and name
        void videochange(String uri);

        void changeminimizeimage(boolean direction);

        void loadchannelbyID(String slug);

        void loadmoreYoutubeVideos();
    }

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

    ProgressHUD mmProgressHUD;
    public static ArrayList<ChannelCategoryList> channelMainCategoryList = new ArrayList<>();

    private void loadmainCategories() {
        mmProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
        Thread thread = new Thread() {
            @Override
            public void run() {
                LauncherApplication.getmWebService().loadChannelsCategories(new CategoryListener() {
                    @Override
                    public void onCategoriesLoaded(ArrayList<ChannelCategoryList> categorylist) {
                        displaycontent(true);
                        if (categorylist != null && categorylist.size() > 0) {
                            MenuFilter mMainCategoryFilter = new MenuFilter(categorylist);
                            channelMainCategoryList = mMainCategoryFilter.getFilterList();
                            for (ChannelCategoryList menuItem : channelMainCategoryList) {
                                MenuFilter mSubCategoryFilter = new MenuFilter(categorylist, menuItem.getSlug());
                                menuItem.setSubCategories(mSubCategoryFilter.getFilterList());
                            }
                        }
                        if (channelMainCategoryList.size() > 0)
                            HomeActivity.channelMainCategoryNew = channelMainCategoryList;

                        setMenu(channelMainCategoryList);
                    }

                    @Override
                    public void onLoadingFailed() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mmProgressHUD != null && mmProgressHUD.isShowing()) {
                                    mmProgressHUD.dismiss();
                                }
                            }
                        });
                    }
                });
            }
        };
        thread.start();
    }

    private void setMenu(final ArrayList<ChannelCategoryList> channelMainCategoryList) {
        if (getActivity() != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mmProgressHUD.dismiss();
                    SetHorzontalMenuAdapter(channelMainCategoryList, mParamMainCategory);

                    if (!TextUtils.isEmpty(mParamSlug)) {
                        loadChannelList(mParamSlug, "", "search");
                    } else {
                        loadChannelList(channelMainCategoryList.get(0).getSlug(), "", "");
                    }
                }
            });
                /*mParamSlug-to check channel page is called from home page or click actions from left menu */

        }
    }

    private void SetHorzontalMenuAdapter(ArrayList<ChannelCategoryList> channelMainCategoryList, String selectedSlug) {
        ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter(channelMainCategoryList, getActivity(), -1);
        fragment_ondemand_alllist_items.setAdapter(channelCategoryAdapter);
    }

    private class ChannelCategoryAdapter extends RecyclerView.Adapter<ChannelCategoryAdapter.DataObjectHolder> {
        private ArrayList<ChannelCategoryList> m_channelViews;
        private Context context;
        private int mSelectedItem;
        private Drawable img;
        private ScaleDrawable sd;

        public ChannelCategoryAdapter(ArrayList<ChannelCategoryList> m_channelViews, Context context, int sel_pos) {
            this.m_channelViews = m_channelViews;
            this.context = context;
            this.mSelectedItem = sel_pos;
            try {
                text_font_typeface();
                showBackIcon(false);
                img = ContextCompat.getDrawable(getActivity(), R.drawable.spinner_icon);
                if (img != null) {
                    img.setBounds(0, 0, 12, 12);
                    img.setBounds(0, 0, (int) (img.getIntrinsicWidth() * 0.5),
                            (int) (img.getIntrinsicHeight() * 0.5));
                    sd = new ScaleDrawable(img, 0, 12, 12);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public ChannelCategoryAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.channel_list_layout, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChannelCategoryAdapter.DataObjectHolder holder, final int position) {
            try {
                holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
                holder.fragment_ondemandlist_items.setText(m_channelViews.get(position).getName());
                holder.fragment_ondemandlist_items.setCompoundDrawables(null, null, sd.getDrawable(), null);
                if (position == mSelectedItem) {
                    Log.d("drawable", "position  " + mMainCategorySelectedItem);
                    holder.fragment_ondemandlist_items_layout.setBackgroundResource(R.drawable.menubg);
                } else {
                    holder.fragment_ondemandlist_items_layout.setBackgroundResource(0);
                }

                holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isBackClicked = false;
                        mChannelList = Constants.CHANNEL_MAINCONTENT;
                        mMainCategorySelectedItem = position;
                        horizontal_position = position;
                        //minimizeChannelView();
                        showBackIcon(true);

                        try {
                            String slug = m_channelViews.get(position).getSlug();
                            temp_list = m_channelViews.get(position).getSubCategories();
                            subCategoryAffix = m_channelViews.get(position).getName();
                            if (temp_list.size() == 0) {
                                mtoolbarMaincontent = m_channelViews.get(position).getName();
                                ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, "", "");
                                back_navigationData.put("channels", new ChannelNavigation(m_channelViews.get(position).getSlug(), position, "main", m_channelViews));
                                bredcrumb.add("channels");
                                fragment_ondemand_alllist_items.setVisibility(View.GONE);
                                loadChannelList(slug, "", "");
                            } else {
                                mtoolbarMaincontent = m_channelViews.get(position).getName();
                                ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, "", "");
                                back_navigationData.put("channels", new ChannelNavigation(m_channelViews.get(position).getSlug(), position, "main", m_channelViews));
                                bredcrumb.add("channels");
                                ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelSubCategoryAdapter(temp_list, getActivity(), -1);
                                fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
                            }
                            setAnalyticreport();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return m_channelViews.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private LinearLayout fragment_ondemandlist_items_layout;
            private TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);

                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);

                Utilities.setViewFocus(getActivity(), fragment_ondemandlist_items);
            }
        }
    }

    private void loadChannelList(final String slug, String api, String data) {
        try {
            clearFragments();
            if (getActivity() != null) {
//                FragmentManager fragmentManager = getChildFragmentManager();
                try {
                    int containerId = timeLineLayout.getId();
                    Fragment oldFragment = fragmentManager.findFragmentById(containerId);
                    if (oldFragment != null) {
                        fragmentManager.beginTransaction().remove(oldFragment).commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int newContainerId = ChannelUtils.GetUniqueID();// My method
                timeLineLayout.setId(newContainerId);// Set container id
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ChannelTimelineFragment mChannelTimelineFragment = ChannelTimelineFragment.newInstance(slug, api, data);
                fragmentList.add(mChannelTimelineFragment);
                fragmentTransaction.replace(newContainerId, mChannelTimelineFragment);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ChannelSubCategoryAdapter extends RecyclerView.Adapter<ChannelSubCategoryAdapter.DataObjectHolder> {
        private ArrayList<ChannelCategoryList> arrayAdapter;
        private Context context;
        private int mSelectedItem;
        private Drawable img;
        private ScaleDrawable sd;

        public ChannelSubCategoryAdapter(ArrayList<ChannelCategoryList> arrayAdapter, Context context, int sel_pos) {
            this.arrayAdapter = arrayAdapter;
            this.context = context;
            this.mSelectedItem = sel_pos;
            text_font_typeface();
            img = context.getResources().getDrawable(
                    R.drawable.spinner_icon);
            if (img != null) {
                img.setBounds(0, 0, (int) (img.getIntrinsicWidth() * 0.5),
                        (int) (img.getIntrinsicHeight() * 0.5));
                sd = new ScaleDrawable(img, 0, 12, 12);
            }
        }

        @Override
        public ChannelSubCategoryAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.channel_list_layout, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChannelSubCategoryAdapter.DataObjectHolder holder, final int position) {
            try {
                holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
                holder.fragment_ondemandlist_items.setText(arrayAdapter.get(position).getName());

                holder.fragment_ondemandlist_items.setCompoundDrawables(null, null, sd.getDrawable(), null);
                if (mCategorySelectedItem.equals("")) {
                    if (position == mSelectedItem) {

                        holder.fragment_ondemandlist_items_layout.setBackgroundResource(R.drawable.menubg);
                    } else {
                        holder.fragment_ondemandlist_items_layout.setBackgroundResource(0);
                    }
                } else {
                    if (position == mSelectedItem) {

                        holder.fragment_ondemandlist_items_layout.setBackgroundResource(R.drawable.menubg);
                    } else {
                        holder.fragment_ondemandlist_items_layout.setBackgroundResource(0);
                    }
                }


                holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            isBackClicked = false;
                            mtoolbarcontent = arrayAdapter.get(position).getName();
                            //minimizeChannelView();
                            if (subCategoryAffix.equals("TV Decades")) {
                                try {
                                    mChannelList = Constants.CHANNEL_DECADECONTENT;
                                    mCategorySelectedItem = String.valueOf(position);
                                    mCategorySelectedHeader = arrayAdapter.get(position).getName();
                                    ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mCategorySelectedHeader, "");
                                    back_navigationData.put(mtoolbarMaincontent, new ChannelNavigation(arrayAdapter.get(position).getSlug(), position, "sub", arrayAdapter));
                                    bredcrumb.add(mtoolbarMaincontent);
                                    String name = arrayAdapter.get(position).getName();
                                    // new LoadingChannelTaskForDecadeChannel(true, name, 1).execute();
                                    loadChannelList(arrayAdapter.get(position).getSlug(), "", "");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                                fragment_ondemand_alllist_items.setVisibility(View.GONE);
                                try {
                                    if (arrayAdapter.get(position).getSlug().isEmpty()) {
                                        mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                        mSubCategorySelectedItem = position;
                                        mtoolbarSubContent = arrayAdapter.get(position).getName();
                                        ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                        back_navigationData.put(mtoolbarMaincontent, new ChannelNavigation(arrayAdapter.get(position).getSlug(), position, "sub", arrayAdapter));
                                        bredcrumb.add(mtoolbarMaincontent);
                                        new LoadingDecade(true, 1).execute();
                                    } else {
                                        String slug = arrayAdapter.get(position).getSlug();
                                        Log.d("SelectedID2::", "::::" + arrayAdapter.get(position).getSlug());
                                        ArrayList<ChannelCategoryList> subList = new ArrayList<ChannelCategoryList>();
                                        subList = getSubCategories(slug);
                                        if (subList.size() == 0) {
//                                            if (slug != 484 && slug != 482 && slug != 486) {
                                            if (!slug.isEmpty()) {
                                                mSubCategorySelectedItem = position;
                                              /*  if (m_jsonSubCategories == temp) {
                                                    mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                                    mtoolbarSubContent = arrayAdapter.get(position);
                                                    ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                                } else {*/
                                                mChannelList = Constants.CHANNEL_SUBCONTENT;
                                                mCategorySelectedHeader = arrayAdapter.get(position).getName();
                                                ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mCategorySelectedHeader, "");
                                                back_navigationData.put(mtoolbarMaincontent, new ChannelNavigation(arrayAdapter.get(position).getSlug(), position, "sub", arrayAdapter));
                                                bredcrumb.add(mtoolbarMaincontent);
                                                //}
                                                loadChannelList(slug, "", "");
                                            } else {
                                                mSubCategorySelectedItem = position;
                                                mCategorySelectedHeader = arrayAdapter.get(position).getName();
                                                ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mCategorySelectedHeader, "");
                                                back_navigationData.put(mtoolbarMaincontent, new ChannelNavigation(arrayAdapter.get(position).getSlug(), position, "sub", arrayAdapter));
                                                bredcrumb.add(mtoolbarMaincontent);
                                                mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                                showActorDialog(slug);
                                            }

                                        } else {
                                            mSubCategorySelectedItem = position;
                                            mChannelList = Constants.CHANNEL_SUBCATEGORY;
                                            mtoolbarSubContent = arrayAdapter.get(position).getName();
                                            Log.d("SelectedID::", "::::" + arrayAdapter.get(position));
                                            ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, "");
                                            back_navigationData.put(mtoolbarMaincontent, new ChannelNavigation(arrayAdapter.get(position).getSlug(), position, "sub", arrayAdapter));
                                            bredcrumb.add(mtoolbarMaincontent);
                                            fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
                                            ChannelTotalFragment.ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelTotalFragment.ChannelSubCategoryAdapter(subList, getActivity(), -1);
                                            fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            setAnalyticreport();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return arrayAdapter.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private LinearLayout fragment_ondemandlist_items_layout;
            private TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);

                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);

                Utilities.setViewFocus(getActivity(), fragment_ondemandlist_items);
            }
        }
    }

    private ArrayList<ChannelCategoryList> getSubCategories(String slug) {
        ArrayList<ChannelCategoryList> list = new ArrayList<>();
        try {
            for (int i = 0; i < channelMainCategoryList.size(); i++) {
                if (!TextUtils.isEmpty(channelMainCategoryList.get(i).getParent()) &&
                        !channelMainCategoryList.get(i).getParent().equalsIgnoreCase("null")) {
                    if (channelMainCategoryList.get(i).getParent().equalsIgnoreCase(slug)) {

                        Log.d("::::::", ":::" + channelMainCategoryList.get(i).getParent() + "::::" + slug);
                        list.add(channelMainCategoryList.get(i));
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private class LoadingDecade extends AsyncTask<Object, Object, Object> {

        private boolean showDialog;
        private int nDecadeCategory;
        ArrayList<ChannelCategoryList> decadelist = new ArrayList<>();

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

                JSONArray m_jsonSubCategories = JSONRPCAPI.getDecade(nDecadeCategory);
                if (m_jsonSubCategories == null) return null;

                if (m_jsonSubCategories.length() > 0) {
                    for (int i = 0; i < m_jsonSubCategories.length(); i++) {
                        JSONObject decade_object = m_jsonSubCategories.getJSONObject(i);
                        ChannelCategoryList cscl = new ChannelCategoryList();
                        cscl.setSlug("");
                        cscl.setName(decade_object.getString("decade"));
                        cscl.setParent("");
                        decadelist.add(cscl);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                progressBar_center.setVisibility(View.GONE);
                if (decadelist != null && decadelist.size() > 0) {
                    fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
                    subCategoryAffix = "TV Decades";
                    fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
                    ChannelSubCategoryAdapter channelSubCategoryAdapter = new ChannelSubCategoryAdapter(decadelist, getActivity(), -1);
                    fragment_ondemand_alllist_items.setAdapter(channelSubCategoryAdapter);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void showActorDialog(final String slug) {
        // nCategory = nID;

        try {
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
                    //minimizeChannelView();
                    fragment_ondemand_alllist_items.setVisibility(View.GONE);
                    //new LoadingChannelForActor(true, arrayActorAdapterData.get(position),nID).execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner_actors.setFocusable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void minimizeChannelView(boolean menuclick) {

        try {
            Log.d("touchevent::", "touchevent::");
            if (menuclick) {
                if (checkIsTablet()) {
                    float weight = 2.9f;
                    if (bChannelUp == true) {
                        mListener.changeminimizeimage(true);

                        //imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowup));
                        weight = 2.9f;
                    }
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearSubMainRight.getLayoutParams();
                    params.weight = weight;
                    linearSubMainRight.setLayoutParams(params);
                    bChannelUp = !bChannelUp;
                } else {
                    float weight = 1.8f;
                    if (bChannelUp == true) {
                        mListener.changeminimizeimage(true);
                        //imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowup));
                        weight = 1.8f;
                    }
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearSubMainRight.getLayoutParams();
                    params.weight = weight;
                    linearSubMainRight.setLayoutParams(params);
                    bChannelUp = !bChannelUp;
                }
            } else {
                float weight = 2.9f;
                if (checkIsTablet()) {
                    if (bChannelUp == false) {
                        mListener.changeminimizeimage(false);
                        //imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowdown));
                        weight = 0.5f;
                    } else {
                        mListener.changeminimizeimage(true);
                        //imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowup));
                        weight = 2.9f;
                    }
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearSubMainRight.getLayoutParams();
                    params.weight = weight;
                    linearSubMainRight.setLayoutParams(params);
                    bChannelUp = !bChannelUp;
                } else {
                    if (bChannelUp == false) {
                        mListener.changeminimizeimage(false);
                        //imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowdown));
                        weight = 1.0f;
                    } else {
                        mListener.changeminimizeimage(true);
                        //imgchannelupdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrowup));
                        weight = 1.8f;
                    }
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearSubMainRight.getLayoutParams();
                    params.weight = weight;
                    linearSubMainRight.setLayoutParams(params);
                    bChannelUp = !bChannelUp;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkIsTablet() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*Chromecast functions*/

    void initializeCast() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            mMediaRouter = MediaRouter.getInstance(getActivity());
            mMediaRouteSelector = new MediaRouteSelector.Builder().addControlCategory(CastMediaControlIntent.categoryForCast(APP_ID)).build();
            mMediaRouterCallback = new MyMediaRouterCallback();

            HomeActivity.activity_homescreen_chromecast.setRouteSelector(mMediaRouteSelector);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onCast() {
        try {
            if (mSelectedDevice == null) {
                String sNames[] = new String[mRouteNames.size()];
                for (int i = 0; i < mRouteNames.size(); i++) {
                    sNames[i] = mRouteNames.get(i);
                }
            } else {
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View volumeCastView = factory.inflate(R.layout.dialog_cast, null);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyMediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo route) {
            super.onRouteAdded(router, route);

            try {
                Log.d("HomeActivity:::", "onRouteAdded");

                // Add route to list of discovered routes
                synchronized (this) {
                    mRouteInfos.add(route);
                    mRouteNames.add(route.getName() + " (" + route.getDescription() + ")");
                }

                if (++mRouteCount == 1) {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo route) {
            super.onRouteRemoved(router, route);

            try {
                Log.d("HomeActivity:::", "onRouteRemoved");
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

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            try {
                Log.d("HomeActivity:::", "onRouteSelected");
                mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
                launchReceiver();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            Log.d("HomeActivity:::", "onRouteUnselected: info=" + info);
            mSelectedDevice = null;
        }
    }

    private void launchReceiver() {
        try {
            mCastListener = new Cast.Listener() {

                @Override
                public void onApplicationDisconnected(int errorCode) {
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
            if (mApiClient == null) {
                return;
            }

            try {
                if (mWaitingForReconnect) {
                    mWaitingForReconnect = false;

                    if ((connectionHint != null)
                            && connectionHint.getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {
                        Log.d("channel:::", "App  is no longer running");
                        teardown(true);
                    } else {
                        try {

                            Cast.CastApi.setMessageReceivedCallbacks(mApiClient,
                                    mMediaChannel.getNamespace(),
                                    mMediaChannel);

                        } catch (IOException e) {
                            Log.e("channel:::", "Exception while creating channel", e);
                        }
                    }
                } else {
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

                                                    Cast.CastApi.setMessageReceivedCallbacks(mApiClient,
                                                            mMediaChannel.getNamespace(),
                                                            mMediaChannel);
                                                } catch (IOException e) {
                                                    Log.e("channel:::", "Exception while creating channel",
                                                            e);
                                                    Toast.makeText(getActivity(), "Exception while creating channel", Toast.LENGTH_SHORT).show();
                                                }

                                                castVideoFromId(nStreamingId);

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

    private void teardown(boolean selectDefaultRoute) {
        Log.d("channel:::", "teardown");

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean castVideoFromId(final int nId) {


        try {
            if (mApiClient == null || mMediaChannel == null) {

                Log.d("ChromeCast::::", "castVideoFromId:::mApiClient");
                return false;
            }

            if (videoSchedule == null || videoSchedule.size() <= 0 || nId < 0 || videoSchedule.size() <= nId) {
                Log.d("ChromeCast::::", "castVideoFromId:::data null");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Log.d("ChromeCast::::", "castVideoFromId:::generating stream");

            final String strTitle = videoSchedule.get(nId).getName();
            String strUrl = videoSchedule.get(nId).getUuid();

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

                            castVideo(strTitle, null, sThumb, downloadUrl, 0);

                            // if( bRunningThread == false )
                            //    startTestThread();
                        }
                    }
                }
            };
            ytEx.execute("http://www.youtube.com/watch?v=" + strUrl);

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
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Exception while sending message", Toast.LENGTH_SHORT).show();
                showDialog(e.getLocalizedMessage());
            }
        }
    }

    void showDialog(String text) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("Message")
                    .setMessage(text)
                    .setPositiveButton("OK", null);
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class AlertChannelDialogListAdapter extends RecyclerView.Adapter<AlertChannelDialogListAdapter.DataObjectHolder> {
        private Context m_context;
        private ArrayList<ChannelScheduler> m_data;
        private int mPosition;
        private Boolean isClicked = false;
        private Drawable img;
        private ScaleDrawable sd;

        public AlertChannelDialogListAdapter(Context context, ArrayList<ChannelScheduler> list_data, int c_id) {
            this.m_context = context;
            this.m_data = list_data;
            this.mPosition = c_id;
            text_font_typeface();
            img = context.getResources().getDrawable(
                    R.drawable.spinner_icon);
            if (img != null) {
                img.setBounds(0, 0, 12, 12);
                img.setBounds(0, 0, (int) (img.getIntrinsicWidth() * 0.5),
                        (int) (img.getIntrinsicHeight() * 0.5));
                sd = new ScaleDrawable(img, 0, 12, 12);
            }
        }

        @Override
        public AlertChannelDialogListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.channel_actorlist_layout, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final AlertChannelDialogListAdapter.DataObjectHolder holder, final int position) {

            try {
                holder.fragment_channel_items.setTypeface(OpenSans_Regular);
                holder.fragment_channel_items_imageThumbnail.setVisibility(View.GONE);
                String strCategory = "";
                String strUrl = "";
                if (m_data != null) {

                    try {
                        strCategory = m_data.get(position).getName();
                        strUrl = m_data.get(position).getLogo();
                        if (strUrl.equals("null")) {
                            strUrl = m_data.get(position).getLogo();
                            if (strUrl != null && strUrl.startsWith("http") == false) {
                                strUrl = "https:" + strUrl;
                            }
                        }
                        Log.e("ERRORLOG", strCategory);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                holder.fragment_channel_items.setText(strCategory);
                holder.fragment_channel_items.setTag(position);

//            Picasso.with(m_context).load(strUrl).into(holder.fragment_channel_items_imageThumbnail);
                //  holder.fragment_channel_items_imageThumbnail.loadImage(strUrl);
                // Log.e("strUrl", strUrl);


                if (position == mPosition) {

                    holder.fragment_channel_items_layout.setBackgroundResource(R.drawable.menubg);
                } else {
                    holder.fragment_channel_items_layout.setBackgroundResource(0);
                }


                holder.fragment_channel_items_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            int pos = Integer.parseInt(holder.fragment_channel_items.getTag().toString());
                            Log.d("alert::", "Alertdialog selected");


                            mtoolbarlastcontent = holder.fragment_channel_items.getText().toString();
                            isClicked = true;
                            // mSubContentSelectedItem = pos;
                            mPosition = pos;

                            ((HomeActivity) getActivity()).toolbarTextChange("Channels", mtoolbarMaincontent, mtoolbarSubContent, mtoolbarlastcontent);
                            // back_navigationData.put(mtoolbarSubContent,new ChannelNavigation(m_data.get(position).getId(),position,"subsub",m_data));

                            holder.fragment_channel_items_layout.setBackgroundResource(R.drawable.menubg);
                            mListener.loadchannelbyID(m_data.get(position).getSlug());
                            notifyDataSetChanged();
                            setAnalyticreport();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return m_data.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private LinearLayout fragment_channel_items_layout;
            private TextView fragment_channel_items;
            private DynamicImageView fragment_channel_items_imageThumbnail;

            public DataObjectHolder(View itemView) {
                super(itemView);

                fragment_channel_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_channel_items_layout);
                fragment_channel_items = (TextView) itemView.findViewById(R.id.fragment_channel_items);
                fragment_channel_items_imageThumbnail = (DynamicImageView) itemView.findViewById(R.id.fragment_channel_items_imageThumbnail);

                Utilities.setViewFocus(getActivity(), fragment_channel_items);
            }
        }
    }


    public static void showBackIcon(boolean b) {
        try {
            if (b) {
                channel_prev_button.setVisibility(View.VISIBLE);
            } else {
                channel_prev_button.setVisibility(View.INVISIBLE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAnalyticreport() {
        try {
            if (TextUtils.isEmpty(mtoolbarMaincontent)) {
                Utilities.setAnalytics(mTracker, "Channels");
            } else if (TextUtils.isEmpty(mCategorySelectedHeader)) {
                Utilities.setAnalytics(mTracker, "Channels-" + mtoolbarMaincontent);
            } else if (TextUtils.isEmpty(mtoolbarlastcontent)) {
                Utilities.setAnalytics(mTracker, "Channels-" + mtoolbarMaincontent + "-" + mtoolbarlastcontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    FragmentManager fragmentManager;

    public void setVideoType(final ChannelScheduler mPrograms) {
        startTimer();
                /*loading players (webview, youtube exoplayer) based on proded type*/
        if (mPrograms != null && ((mPrograms.getPrograms() != null && mPrograms.getPrograms().getProgramlist().size() > 0) || (mPrograms.getStreams() != null && mPrograms.getStreams().size() > 0))) {
            String program_type = mPrograms.getType();
            String video_type = "";
            if (program_type.equalsIgnoreCase("live/video") || program_type.equalsIgnoreCase("live/radio")) {
                video_type = mPrograms.getStreams().get(0).getType();
            } else {
                video_type = mPrograms.getPrograms().getProgramlist().get(0).getPlaylist().get(0).getType();
            }
            int containerId = player_layout.getId();// Get container id
//            fragmentManager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
            Fragment oldFragment = fragmentManager.findFragmentById(containerId);
            if (oldFragment != null) {
                fragmentManager.beginTransaction().remove(oldFragment).commit();
            }
            int newContainerId = ChannelUtils.GetUniqueID();// My method
            player_layout.setId(newContainerId);// Set container id
            try {
//                currentPlayingChannelScheduler = mPrograms;//
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (video_type) {
                case "embed":
                    if (mPrograms.getStreams().get(0).getData().contains("youtube")) {
                        playVideoInYoutubeView(mPrograms.getStreams().get(0).getName(), mPrograms.getStreams().get(0).getData(), newContainerId, mPrograms.getSlug());
                    } else {
                        playVideoInWebView(mPrograms.getStreams().get(0).getData(), newContainerId);
                    }
                    break;
                case "stream":
                    playVideoInVideoView(mPrograms.getStreams().get(0).getData(), newContainerId);
                    break;
                case "custom":
                    playVideoInYoutubeView(mPrograms.getPrograms().getProgramlist(), newContainerId, mPrograms.getSlug());
                    break;
                case "src":
                    if (program_type.equalsIgnoreCase("live/video") || program_type.equalsIgnoreCase("live/radio")) {
                        if (mPrograms.getStreams().get(0).getData().contains(".flv")) {
                            extractStreamUrl(mPrograms.getStreams().get(0).getData());
                        } else {
                            playVideoInVideoView(mPrograms.getStreams().get(0).getData(), newContainerId);
                        }

                    } else {
                        extractStreamUrl(mPrograms.getPrograms().getProgramlist());
                    }
                    break;
                case "":
                    break;
            }
        }
    }

    private void extractStreamUrl(String data) {
    }

    private void playVideoInYoutubeView(ArrayList<ProgramList> programlist, int newContainerId, String mSlug) {
        playerfragment = PlayerYouTubeFrag.newInstance(mListener, programlist, this, mSlug);
        fragmentManager.beginTransaction().replace(newContainerId, playerfragment).commit();
    }

    private void playVideoInYoutubeView(String title, String mData, int newContainerId, String mSlug) {
        playerfragment = PlayerYouTubeFrag.newInstance(mListener, title, mData, this, mSlug);
        fragmentManager.beginTransaction().replace(newContainerId, playerfragment).commit();
    }

    private void playVideoInYoutubeView(ArrayList<ProgramList> programlist, int position, int newContainerId, String mSlug) {
        playerfragment = PlayerYouTubeFrag.newInstance(programlist, position, this, mSlug);
        fragmentManager.beginTransaction().replace(newContainerId, playerfragment).commit();
    }

    private void extractStreamUrl(final ArrayList<ProgramList> data) {

    }

    private void playVideoInVideoView(String data, int newContainerId) {
        ExoPlayerFragment fragment = ExoPlayerFragment.newInstance(data);
        fragmentList.add(fragment);
        fragmentManager.beginTransaction().replace(newContainerId, fragment).commit();
    }

    private void playVideoInWebView(String data, int newContainerId) {
        WebViewPlayerFragment fragment = WebViewPlayerFragment.newInstance(data, "");
        fragmentList.add(fragment);
        fragmentManager.beginTransaction().replace(newContainerId, fragment).commit();
    }

    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public void clearFragments() {
        if (fragmentManager == null)
            return;
        try {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            for (Fragment fragment : fragmentList) {
                if (fragment != null)
                    ft.remove(fragment);
            }
            ft.commit();
            fragmentList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopYoutubePlayer() {
        if (playerfragment != null)
            playerfragment.releasePlayer();
    }

    public void fullScreen(int visibleType) {
        if (visibleType == View.GONE) {
            isYoutubeFullscreen = true;
            ((HomeActivity) getActivity()).mHidetoolbar(true);

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            player_layout.getLayoutParams().height = dm.heightPixels;
            player_layout.getLayoutParams().width = dm.widthPixels;
            mStopTimer();
        } else {
            isYoutubeFullscreen = false;
            ((HomeActivity) getActivity()).mHidetoolbar(false);
            player_layout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            player_layout.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            startTimer();
        }
        linearFullScreen.setVisibility(View.GONE);
        linearSubMainRight.setVisibility(visibleType);
        layoutSideMenusContent.setVisibility(visibleType);
    }

}
