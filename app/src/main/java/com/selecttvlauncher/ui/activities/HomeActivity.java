package com.selecttvlauncher.ui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.selecttvlauncher.BeanClass.CauroselBean;
import com.selecttvlauncher.BeanClass.FavoriteBean;
import com.selecttvlauncher.BeanClass.GridBean;
import com.selecttvlauncher.BeanClass.ListenGridBean;
import com.selecttvlauncher.BeanClass.SearchBean;
import com.selecttvlauncher.BeanClass.SideMenu;
import com.selecttvlauncher.BeanClass.SliderBean;
import com.selecttvlauncher.BeanClass.SliderCommonBean;
import com.selecttvlauncher.BeanClass.SubScriptionBean;
import com.selecttvlauncher.BeanClass.SubScriptionListBean;
import com.selecttvlauncher.BeanClass.SubScriptionsubList;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.channels.ChannelCategoryList;
import com.selecttvlauncher.channels.ChannelScheduler;
import com.selecttvlauncher.channels.ChannelTimelineFragment;
import com.selecttvlauncher.channels.ChannelTotalFragment;
import com.selecttvlauncher.channels.ProgramList;
import com.selecttvlauncher.network.Helper;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.service.RadioService;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.dialogs.ProgressHUD;
import com.selecttvlauncher.ui.fragments.AccountDetailsFragment;
import com.selecttvlauncher.ui.fragments.AccountFragment;
import com.selecttvlauncher.ui.fragments.AppDownloadFragment;
import com.selecttvlauncher.ui.fragments.ChannelFragment;
import com.selecttvlauncher.ui.fragments.ChannelTempFragment;
import com.selecttvlauncher.ui.fragments.ExitDialogFragment;
import com.selecttvlauncher.ui.fragments.FragmentDetailsDialog;
import com.selecttvlauncher.ui.fragments.FragmentHomeScreenGrid;
import com.selecttvlauncher.ui.fragments.FragmentListenGrid;
import com.selecttvlauncher.ui.fragments.FragmentListenList;
import com.selecttvlauncher.ui.fragments.FragmentListenMain;
import com.selecttvlauncher.ui.fragments.FragmentMovieMain;
import com.selecttvlauncher.ui.fragments.FragmentMoviesLeftContent;
import com.selecttvlauncher.ui.fragments.FragmentOTAANDCABLE;
import com.selecttvlauncher.ui.fragments.FragmentOnDemandAll;
import com.selecttvlauncher.ui.fragments.FragmentOndemandallList;
import com.selecttvlauncher.ui.fragments.FragmentRadioDetails;
import com.selecttvlauncher.ui.fragments.FragmentSearchList;
import com.selecttvlauncher.ui.fragments.FragmentSearchMain;
import com.selecttvlauncher.ui.fragments.FragmentShowSeasonsAndEpisodes;
import com.selecttvlauncher.ui.fragments.FragmentSubScriptionList;
import com.selecttvlauncher.ui.fragments.FragmentSubScriptions;
import com.selecttvlauncher.ui.fragments.GameMainFragment;
import com.selecttvlauncher.ui.fragments.GamesDetailsFragment;
import com.selecttvlauncher.ui.fragments.GridFragment;
import com.selecttvlauncher.ui.fragments.HomeFragment;
import com.selecttvlauncher.ui.fragments.HorizontalListsFragment;
import com.selecttvlauncher.ui.fragments.InterestChannelsFragment;
import com.selecttvlauncher.ui.fragments.InterestDialogFragment;
import com.selecttvlauncher.ui.fragments.InterestGenreDialogFragment;
import com.selecttvlauncher.ui.fragments.InterestLeftFragment;
import com.selecttvlauncher.ui.fragments.InterestMainFragment;
import com.selecttvlauncher.ui.fragments.InterestNetworkDialogFragment;
import com.selecttvlauncher.ui.fragments.InterestRightFragment;
import com.selecttvlauncher.ui.fragments.OnDemandFavoriteFragment;
import com.selecttvlauncher.ui.fragments.OnDemandFavoriteFragment.OnDemandFavoriteFragmentInteractionListener;
import com.selecttvlauncher.ui.fragments.OnDemandYoutubeFragment;
import com.selecttvlauncher.ui.fragments.SearchGridFragment;
import com.selecttvlauncher.ui.fragments.WelcomeVideoFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.selecttvlauncher.ui.fragments.ChannelTimelineFragment;

//import com.selecttvlauncher.ui.fragments.ChannelTotalFragment;

public class HomeActivity extends AppCompatActivity implements SearchGridFragment.OnSearchGridFragmentInteractionListener, GridFragment.OnGridFragmentInteractionListener, ChannelTotalFragment.ChannelTotalListener, ChannelTimelineFragment.TimelineFragmentListener, HomeFragment.OnHomeFragmentInteractionListener,
        AccountDetailsFragment.OnAccontDetailFragmentInteractionListener, AccountFragment.OnAccountFragmentInteractionListener, InterestLeftFragment.OnInterestLeftFragmentInteractionListener,
        InterestRightFragment.OnInterestRightFragmentInteractionListener, InterestGenreDialogFragment.OnInterestDialogFragmentInteractionListener,
        InterestNetworkDialogFragment.OnInterestNetworkDialogFragmentInteractionListener, InterestChannelsFragment.OnInterestChannelFragmentInteractionListener,
        InterestDialogFragment.OnDialogFragmentInteractionListener, OnDemandFavoriteFragmentInteractionListener, OnDemandYoutubeFragment.OndemandyoutubeFragmentInteractionListener, ExitDialogFragment.OnExitFragmentInteractionListener, WelcomeVideoFragment.WelcomeVideoFragmentInteractionListener, AppDownloadFragment.OnAppFragmentInteractionListener {

    public static boolean is_click = true;
    public static boolean ruuning_task = true;
    public static int mLastEpisodeId = 0;
    public static String mImageUrl = "";
    public static String searchkey = "";
    public static JSONArray m_jsonArrayCategories;
    public static JSONArray m_jsonArrayChannelVideos;
    public static JSONArray m_jsonLiveItems;
    public static JSONObject objSchedule;
    public static String IDs = "";
    public static JSONObject m_JsonscchedulledRelated;
    public static JSONArray mSearch_Trailors;
    public static String networkImage = "";
    public static JSONArray m_jsonRadioContinent;
    public static JSONArray m_jsonRadioGenre;
    public static JSONArray m_jsonRadioLanguage;
    public static HashMap<String, String> networkDetails = new HashMap<>();
    public static ArrayList<SideMenu> mOnDemandlist = new ArrayList<>();
    public static JSONArray m_jsonMovieSlider, m_jsonShowSlider, m_jsonSlider;
    public static JSONArray m_jsonSportsCarousels;
    public static JSONArray m_jsonWebCarousels;
    public static JSONArray m_jsonKidsCarousels;
    public static JSONArray m_jsonWebSlider;
    public static JSONArray m_jsonKidsSlider;
    public static JSONArray m_jsonSportsSlider;
    public static JSONArray m_jsonPrimeTimeList;
    public static JSONArray m_jsonPrimeTimeSlider;
    public static JSONArray m_jsonTvshowbyCategoryItems;
    public static JSONArray m_jsonCategorySlider;
    public static JSONArray m_jsonTvshowCarousels;
    public static int mNetworkid;
    public static ArrayList<SliderBean> web_slider_list = new ArrayList<>();
    public static ArrayList<SliderBean> kids_slider_list = new ArrayList<>();
    public static JSONArray m_jsonGamemore;
    public static JSONArray m_jsonSubscriptions = new JSONArray();
    public static ArrayList<ListenGridBean> subscritionList = new ArrayList<>();
    public static int channelID = 145;
    public static ArrayList<ChannelCategoryList> channelMainCategoryNew = new ArrayList<>();
    public static Activity fa;

    FragmentDetailsDialog fragmentDetailsDialog;


    public FragmentDetailsDialog getFragmentDetailsDialog() {
        return fragmentDetailsDialog;
    }

    public void setFragmentDetailsDialog(FragmentDetailsDialog fragmentDetailsDialog) {
        this.fragmentDetailsDialog = fragmentDetailsDialog;
    }


    public static FragmentHomeScreenGrid fragmentHomeScreenGrid;

    public static FragmentHomeScreenGrid getFragmentHomeScreenGrid() {
        return fragmentHomeScreenGrid;
    }

    public void setFragmentHomeScreenGrid(FragmentHomeScreenGrid fragmentHomeScreenGrid) {
        this.fragmentHomeScreenGrid = fragmentHomeScreenGrid;
    }


    FragmentShowSeasonsAndEpisodes fragmentShowSeasonsAndEpisodes;

    public FragmentShowSeasonsAndEpisodes getFragmentShowSeasonsAndEpisodes() {
        return fragmentShowSeasonsAndEpisodes;
    }

    public void setFragmentShowSeasonsAndEpisodes(FragmentShowSeasonsAndEpisodes fragmentShowSeasonsAndEpisodes) {
        this.fragmentShowSeasonsAndEpisodes = fragmentShowSeasonsAndEpisodes;
    }

    public FragmentOndemandallList getmFragmentOndemandallList() {
        return mFragmentOndemandallList;
    }

    public void setmFragmentOndemandallList(FragmentOndemandallList mFragmentOndemandallList) {
        this.mFragmentOndemandallList = mFragmentOndemandallList;
    }

    private FragmentOndemandallList mFragmentOndemandallList;

    public FragmentMoviesLeftContent getmFragmentMoviesLeftContent() {
        return mFragmentMoviesLeftContent;
    }

    public void setmFragmentMoviesLeftContent(FragmentMoviesLeftContent mFragmentMoviesLeftContent) {
        this.mFragmentMoviesLeftContent = mFragmentMoviesLeftContent;
    }

    private FragmentMoviesLeftContent mFragmentMoviesLeftContent;

    public ChannelTotalFragment getmChannelTotalFragment() {
        return mChannelTotalFragment;
    }

    public void setmChannelTotalFragment(ChannelTotalFragment mChannelTotalFragment) {
        this.mChannelTotalFragment = mChannelTotalFragment;
    }

    private ChannelTotalFragment mChannelTotalFragment;

    public ChannelTimelineFragment getmChannelTimelineFragment() {
        return mChannelTimelineFragment;
    }

    public void setmChannelTimelineFragment(ChannelTimelineFragment mChannelTimelineFragment) {
        this.mChannelTimelineFragment = mChannelTimelineFragment;
    }

    private ChannelTimelineFragment mChannelTimelineFragment;

    TextView activity_homescreen_toolbar_textview;
    TextView activity_homescreen_toolbar_ondemand_text;
    ImageView activity_homescreen_toolbar_divider, activity_homescreen_toolbar_search, activity_homescreen_toolbar_appmanager, activity_homescreen_toolbar_mail, activity_homescreen_toolbar_info, activity_homescreen_toolbar_home;
    public static ImageView activity_homescreen_toolbar_logout, activity_homescreen_toolbar_exit;
    public static MediaRouteButton activity_homescreen_chromecast;
    public static ArrayList<GridBean> gridBeans = new ArrayList<>();
    private ArrayList<String> network_titles = new ArrayList<>();
    private ArrayList<String> all_titles = new ArrayList<>();
    private ArrayList<String> tvShow_titles = new ArrayList<>();
    private ArrayList<String> tv_byCategory_titles = new ArrayList<>();
    private ArrayList<String> tv_byDecade_titles = new ArrayList<>();
    public static ArrayList<SearchBean> searchBeanArrayList = new ArrayList<>();
    public static ArrayList<SubScriptionBean> subScriptionBeans = new ArrayList<>();
    public static ArrayList<SubScriptionsubList> mSubscriptionSubList = new ArrayList<>();
    public static ArrayList<SubScriptionListBean> mSubscriptiondataList = new ArrayList<>();

    public static ArrayList<CauroselBean> listen_list = new ArrayList<>();
    public static ArrayList<SliderCommonBean> slider_list = new ArrayList<>();

    public static boolean is_networkSelected = false;
    ImageView activity_homescreen_toolbar_app_logo;

    public static int mMovieGenreId;
    public static String mMovieratingid;

    public static JSONArray m_jsonmovieslistbygenre;
    public static JSONArray m_jsonDemandListItems, m_jsonTvshowlistbyGenre, m_jsonTvshowlistbyDecade, m_jsonMovieGenre, m_jsonGameCourasals;
    public static JSONArray m_jsonNetworkList, m_jsonNetworkListItems, m_jsonGenreListbyId, m_jsonDeacdeListbyId, m_jsonTvshowbyCategoryList, m_jsonTvshowbyCategoryListAll,
            m_jsonTvshowfeaturedCarousels, m_jsonOndemandCarousels, m_jsonHomeSlider, m_jsonGameSlider, allCarousels, m_jsonAllmovielist, m_jsonPayPerViewmovielist, m_jsonPayPerViewShowlist,
            m_jsonAllmovielistbyrating, m_jsonmoviesbyrating, m_jsonPPVALL;
    public static String menu_position = Constants.MAIN_MENU, content_position =
            "", content_sub_position = Constants.SUB_RIGHT_CONTENT;
    public static String selectedmenu = "", selectedFilter = "", selectedSearchmenu = "";
    public static String Selecteddetails = "", mostwatched = "";
    public static int mfreeorall = 0;
    public static String mfiltername = "", mMovieorSeriesName = "", mSeriesSeason = "", seasonDetails = "", seasonLinks = "";
    public static int seasonId = 1, episodeId = 1, showId = 1, carousel_id = 1, selecetd_season = 0;
    public static FrameLayout activity_movie_fragemnt_layout, activity_homescreen_fragemnt_layout, activity_search_fragemnt_layout, activity_radio_fragemnt_layout;
    private ImageView activity_homescreen_maincontent_divider;
    private TextView activity_homescreen_toolbar_maincontent;
    Boolean isFilterordeatils = false;

    public static String isPayperview = "", mPayperview = "";
    public static String HomeGridClick = "", mMovieOrSerial = "";
    public static String toolbarGridContent = "", toolbarMainContent = "", toolbarSubContent = "", toolprevGridcontent = "";
    RelativeLayout homeactivity_searchbar_layout;
    TextView homeactivity_search_text;
    ImageView homeactivity_searchView_close;
    EditText homeactivity_searchView;
    public static JSONArray m_jsonArraySearchResult;
    public static JSONObject m_jsonObjectSearchResult, m_jsonGameDeatils;

    public static JSONArray jsonArray_searchShow, jsonArray_searchMovies,
            jsonArray_searchNetwork, jsonArray_searchActor, jsonArray_searchTvStations,
            jsonArray_searchLive, jsonArray_searchStations, jsonArray_searchRadio, jsonArray_searchMusic;

    public static Boolean isSearchClick = false;
    private RelativeLayout linearFullScreen;
    public String szSplashTitle;
    private RoundCornerProgressBar splash_progress;
    private TextView txt_splash_progress;
    private TextView txt_splash_title;
    private TextView activity_channel_final_list_text;
    private ImageView activity_channel_final_list_divider;
    private ImageView activity_homescreen_toolbar_fullview;
    LinearLayout layout_toolbar_main;
    public static String ppv_selected_item = "";
    private static FragmentMovieMain mFragmentMovieMain;

    public static FragmentMovieMain getmFragmentMovieMain() {
        return mFragmentMovieMain;
    }

    public void setmFragmentMovieMain(FragmentMovieMain mFragmentMovieMain) {
        this.mFragmentMovieMain = mFragmentMovieMain;
    }

    public FragmentListenGrid getFragmentListenGrid() {
        return fragmentListenGrid;
    }

    public void setFragmentListenGrid(FragmentListenGrid fragmentListenGrid) {
        this.fragmentListenGrid = fragmentListenGrid;
    }

    FragmentListenGrid fragmentListenGrid;

    public FragmentSearchList getmFragmentSearchList() {
        return mFragmentSearchList;
    }

    public void setmFragmentSearchList(FragmentSearchList mFragmentSearchList) {
        this.mFragmentSearchList = mFragmentSearchList;
    }

    private FragmentSearchList mFragmentSearchList;

    private Drawable x;


    public DateRefreshListener getDatetRefreshListener() {
        return dateRefreshListener;
    }

    public void setDateRefreshListener(DateRefreshListener fragmentRefreshListener) {
        this.dateRefreshListener = dateRefreshListener;
    }

    private DateRefreshListener dateRefreshListener;

    public static ChannelFragment getmChannelFragment() {
        return mChannelFragment;
    }

    public void setmChannelFragment(ChannelFragment mChannelFragment) {
        this.mChannelFragment = mChannelFragment;
    }

    public static ChannelFragment mChannelFragment;

    public static ChannelTempFragment getmChannelTempFragment() {
        return mChannelTempFragment;
    }

    public static void setmChannelTempFragment(ChannelTempFragment mChannelTempFragment) {
        HomeActivity.mChannelTempFragment = mChannelTempFragment;
    }

    public static ChannelTempFragment mChannelTempFragment;

    public FragmentOTAANDCABLE getFragmentOTAANDCABLE() {
        return fragmentOTAANDCABLE;
    }

    public void setFragmentOTAANDCABLE(FragmentOTAANDCABLE fragmentOTAANDCABLE) {
        this.fragmentOTAANDCABLE = fragmentOTAANDCABLE;
    }

    public FragmentOTAANDCABLE fragmentOTAANDCABLE;

    public FragmentSubScriptionList getFragmentSublist() {
        return fragmentSublist;
    }

    public void setFragmentSublist(FragmentSubScriptionList fragmentSublist) {
        this.fragmentSublist = fragmentSublist;
    }

    public FragmentSubScriptionList fragmentSublist;


    //Babin
    public static ArrayList<ChannelCategoryList> channelCategoryList;

    private String currentFragment = "";
    public static Context m_gContext;

    BroadcastReceiver playerStateReceiver;

    public FragmentRadioDetails getmFragmentRadioDetails() {
        return mFragmentRadioDetails;
    }

    public void setmFragmentRadioDetails(FragmentRadioDetails mFragmentRadioDetails) {
        this.mFragmentRadioDetails = mFragmentRadioDetails;
    }

    public InterestRightFragment getmInterestRightFragment() {
        return mInterestRightFragment;
    }

    public void setmInterestRightFragment(InterestRightFragment mInterestRightFragment) {
        this.mInterestRightFragment = mInterestRightFragment;
    }

    private InterestRightFragment mInterestRightFragment;

    public FragmentRadioDetails mFragmentRadioDetails;

    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;


    public static ArrayList<FavoriteBean> tvShowsList = new ArrayList<>();
    public static ArrayList<FavoriteBean> moviesList = new ArrayList<>();
    public static ArrayList<FavoriteBean> movieGenresList = new ArrayList<>();
    public static ArrayList<FavoriteBean> channelsList = new ArrayList<>();
    public static ArrayList<FavoriteBean> tvnetworksList = new ArrayList<>();
    public static ArrayList<FavoriteBean> videoLibrariesList = new ArrayList<>();

    public static String currentPageForBackButton = "";

    public boolean isYoutubeFullScreen() {
        return youtubeFullScreen;
    }

    public void setYoutubeFullScreen(boolean youtubeFullScreen) {
        this.youtubeFullScreen = youtubeFullScreen;
    }

    public boolean youtubeFullScreen;

    public static GameMainFragment getmGameMainFragment() {
        return mGameMainFragment;
    }

    public void setmGameMainFragment(GameMainFragment mGameMainFragment) {
        this.mGameMainFragment = mGameMainFragment;
    }

    public static GameMainFragment mGameMainFragment;

    public static FragmentOnDemandAll getmFragmentOnDemandAll() {
        return mFragmentOnDemandAll;
    }

    public void setmFragmentOnDemandAll(FragmentOnDemandAll mFragmentOnDemandAll) {
        this.mFragmentOnDemandAll = mFragmentOnDemandAll;
    }

    public static FragmentOnDemandAll mFragmentOnDemandAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        fa = this;
        LauncherApplication.getInstance().sethAct(this);
        m_gContext = this;
        mChannelFragment = new ChannelFragment();
        mChannelTempFragment = new ChannelTempFragment();
        try {

            tvShow_titles.add("TV Networks");
            tvShow_titles.add("Most Watched");
            tvShow_titles.add("popular Broadcast TV");
            tvShow_titles.add("popular Cable Shows");
            tvShow_titles.add("popular Classic Shows");

            network_titles.add("TV Networks");
            network_titles.add("Premium Networks");
            network_titles.add("Kids/Family");
            network_titles.add("Internet Channels");
            network_titles.add("Sports Channels");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        setContentView(R.layout.custom_titlebar_activity_frame_from_above);
//        View.inflate(this, getContentAreaLayoutId(), findViewById(R.id.custom_titlebar_container));
        try {
            linearFullScreen = (RelativeLayout) findViewById(R.id.linearFullScreen);
            txt_splash_title = (TextView) findViewById(R.id.txt_splash_title);
            txt_splash_progress = (TextView) findViewById(R.id.txt_splash_progress);
            splash_progress = (RoundCornerProgressBar) findViewById(R.id.splash_progress);
            layout_toolbar_main = (LinearLayout) findViewById(R.id.layout_toolbar_main);
            activity_homescreen_toolbar_app_logo = (ImageView) findViewById(R.id.activity_homescreen_toolbar_app_logo);
            activity_homescreen_toolbar_divider = (ImageView) findViewById(R.id.activity_homescreen_toolbar_divider);
            activity_homescreen_maincontent_divider = (ImageView) findViewById(R.id.activity_homescreen_maincontent_divider);
            activity_homescreen_toolbar_textview = (TextView) findViewById(R.id.activity_homescreen_toolbar_textview);
            activity_homescreen_toolbar_maincontent = (TextView) findViewById(R.id.activity_homescreen_toolbar_maincontent);
            activity_homescreen_toolbar_ondemand_text = (TextView) findViewById(R.id.activity_homescreen_toolbar_ondemand_text);
            activity_channel_final_list_text = (TextView) findViewById(R.id.activity_channel_final_list_text);
            activity_channel_final_list_divider = (ImageView) findViewById(R.id.activity_channel_final_list_divider);
            activity_homescreen_toolbar_fullview = (ImageView) findViewById(R.id.activity_homescreen_toolbar_fullview);
            activity_homescreen_toolbar_search = (ImageView) findViewById(R.id.activity_homescreen_toolbar_search);
            activity_homescreen_toolbar_info = (ImageView) findViewById(R.id.activity_homescreen_toolbar_info);
            activity_homescreen_toolbar_home = (ImageView) findViewById(R.id.activity_homescreen_toolbar_home);
            activity_homescreen_toolbar_mail = (ImageView) findViewById(R.id.activity_homescreen_toolbar_mail);
            activity_homescreen_toolbar_appmanager = (ImageView) findViewById(R.id.activity_homescreen_toolbar_appmanager);
            activity_homescreen_toolbar_logout = (ImageView) findViewById(R.id.activity_homescreen_toolbar_logout);
            activity_homescreen_toolbar_exit = (ImageView) findViewById(R.id.activity_homescreen_toolbar_exit);
            activity_homescreen_fragemnt_layout = (FrameLayout) findViewById(R.id.activity_homescreen_fragemnt_layout);
            activity_movie_fragemnt_layout = (FrameLayout) findViewById(R.id.activity_movie_fragemnt_layout);
            activity_search_fragemnt_layout = (FrameLayout) findViewById(R.id.activity_search_fragemnt_layout);
            activity_radio_fragemnt_layout = (FrameLayout) findViewById(R.id.activity_radio_fragemnt_layout);
            homeactivity_searchbar_layout = (RelativeLayout) findViewById(R.id.homeactivity_searchbar_layout);
            homeactivity_search_text = (TextView) findViewById(R.id.homeactivity_search_text);
            homeactivity_searchView_close = (ImageView) findViewById(R.id.homeactivity_searchView_close);
            homeactivity_searchView = (EditText) findViewById(R.id.homeactivity_searchView);
            activity_homescreen_toolbar_logout.setImageResource(R.drawable.chromecast);
            activity_homescreen_chromecast = (MediaRouteButton) findViewById(R.id.activity_homescreen_chromecast);
            //activity_homescreen_toolbar_logout.setVisibility(View.GONE);
           /* Utilities.setViewFocus(m_gContext,activity_homescreen_toolbar_fullview);
            Utilities.setViewFocus(m_gContext,activity_homescreen_toolbar_search);
            Utilities.setViewFocus(m_gContext,activity_homescreen_toolbar_info);
            Utilities.setViewFocus(m_gContext,activity_homescreen_toolbar_home);
            Utilities.setViewFocus(m_gContext,activity_homescreen_toolbar_mail);*/

            Utilities.setTextFocus(m_gContext, activity_homescreen_toolbar_textview);
            Utilities.setTextFocus(m_gContext, activity_homescreen_toolbar_maincontent);
            Utilities.setTextFocus(m_gContext, activity_homescreen_toolbar_ondemand_text);
            Utilities.setTextFocus(m_gContext, activity_channel_final_list_text);

            addTextChangelistener(homeactivity_searchView);
            focusChangelistener(homeactivity_searchView);
            settouchlistener(homeactivity_searchView);

            x = getResources().getDrawable(R.drawable.clear_icon);
            x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());


           /* FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentMovieMain  fragmentMovieMain = new FragmentMovieMain();
            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentMovieMain);
            fragmentTransaction.commit();*/

            HomeScreenGridLoad();
            initReceiver();

            try {
                if (checkIsTablet()) {
                    activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                    activity_homescreen_maincontent_divider.setVisibility(View.VISIBLE);
                    activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                    activity_homescreen_toolbar_maincontent.setVisibility(View.VISIBLE);
                    activity_channel_final_list_divider.setVisibility(View.VISIBLE);
                    activity_channel_final_list_text.setVisibility(View.VISIBLE);

                } else {
                    activity_homescreen_toolbar_textview.setVisibility(View.GONE);
                    activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                    activity_homescreen_toolbar_divider.setVisibility(View.GONE);
                    activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                    activity_channel_final_list_divider.setVisibility(View.GONE);
                    activity_channel_final_list_text.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ((LauncherApplication) getApplication()).setHomeActivity(this);


        activity_homescreen_toolbar_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, ""); //Enquiry from policy99.com Android app
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        activity_homescreen_toolbar_appmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                OnDemandFavoriteFragment mfragmentOnDemandAll = new OnDemandFavoriteFragment();
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, mfragmentOnDemandAll);
                fragmentTransaction.commit();
                try {
                    SwapMovieFragment(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        activity_homescreen_toolbar_fullview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getmChannelTotalFragment().mFullScreen();
                if (!getmChannelTotalFragment().isYoutubeFullscreen())
                    getmChannelTotalFragment().fullScreen(View.GONE);
                else getmChannelTotalFragment().fullScreen(View.VISIBLE);

            }
        });

        activity_homescreen_toolbar_app_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSeriesSeason = "";
                    FrameLayout fl = (FrameLayout) findViewById(R.id.activity_movie_fragemnt_layout);
                    fl.removeAllViews();

                    FrameLayout fll = (FrameLayout) findViewById(R.id.activity_homescreen_fragemnt_layout);
                    fll.removeAllViews();

                    HomeScreenGridLoad();

                    try {
                        FragmentMovieMain.hideFrames();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        activity_homescreen_toolbar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeriesSeason = "";
                FrameLayout fl = (FrameLayout) findViewById(R.id.activity_movie_fragemnt_layout);
                fl.removeAllViews();

                FrameLayout fll = (FrameLayout) findViewById(R.id.activity_homescreen_fragemnt_layout);
                fll.removeAllViews();
                HomeScreenGridLoad();

                try {
                    FragmentMovieMain.hideFrames();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        activity_homescreen_toolbar_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                toolbarGridContent = "Search";

                homeactivity_searchbar_layout.setVisibility(View.VISIBLE);

                /*if (homeactivity_searchView.getText().length() != 0) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(homeactivity_searchbar_layout.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                }*/
                homeactivity_searchView.setFocusable(true);
                homeactivity_searchView.requestFocus();
                homeactivity_searchView.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(homeactivity_searchView, InputMethodManager.SHOW_FORCED);
            }
        });

        homeactivity_searchView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeactivity_searchbar_layout.setVisibility(View.GONE);
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        homeactivity_searchbar_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        homeactivity_searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        String text = homeactivity_searchView.getText().toString();
                        homeactivity_searchbar_layout.setVisibility(View.GONE);
                        HomeActivity.searchkey = text;
                        new LoadingSearchTask().execute(text);
                    }
                }
                return false;
            }
        });

        activity_homescreen_chromecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getmChannelFragment().onCast();
                getmChannelTotalFragment().onCast();
            }
        });
        activity_homescreen_toolbar_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //clearCacheData();


                    /*PreferenceManager.setLogin(false,m_gContext);
                    PreferenceManager.setAccessToken("", m_gContext);

                    PreferenceManager.setusername("",m_gContext);
                    PreferenceManager.setcity("", m_gContext);
                    PreferenceManager.setfirst_name("", m_gContext);
                    PreferenceManager.setlast_name("", m_gContext);
                    PreferenceManager.setgender("", m_gContext);
                    PreferenceManager.setemail("", m_gContext);
                    PreferenceManager.setstate("", m_gContext);
                    PreferenceManager.setdate_of_birth("", m_gContext);
                    PreferenceManager.setlast_login("", m_gContext);
                    PreferenceManager.setaddress_1("", m_gContext);
                    PreferenceManager.setaddress_2("", m_gContext);
                    PreferenceManager.setpostal_code("", m_gContext);
                    PreferenceManager.setphone_number("", m_gContext);
                    PreferenceManager.setid(0, m_gContext);*//*
                Helper.clearCurrentDefaultLauncher(m_gContext);
                finish();*/
                    FragmentManager fm = getSupportFragmentManager();
                    ExitDialogFragment dialogFragment = new ExitDialogFragment();
                    dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");

                    // finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        activity_homescreen_toolbar_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    activity_homescreen_chromecast.performClick();
//clearCacheData();


                    /*PreferenceManager.setLogin(false,m_gContext);
                    PreferenceManager.setAccessToken("", m_gContext);

                    PreferenceManager.setusername("",m_gContext);
                    PreferenceManager.setcity("", m_gContext);
                    PreferenceManager.setfirst_name("", m_gContext);
                    PreferenceManager.setlast_name("", m_gContext);
                    PreferenceManager.setgender("", m_gContext);
                    PreferenceManager.setemail("", m_gContext);
                    PreferenceManager.setstate("", m_gContext);
                    PreferenceManager.setdate_of_birth("", m_gContext);
                    PreferenceManager.setlast_login("", m_gContext);
                    PreferenceManager.setaddress_1("", m_gContext);
                    PreferenceManager.setaddress_2("", m_gContext);
                    PreferenceManager.setpostal_code("", m_gContext);
                    PreferenceManager.setphone_number("", m_gContext);
                    PreferenceManager.setid(0, m_gContext);*/

                   /* try {
                        getPackageManager().clearPackagePreferredActivities(getPackageName());
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    /*Intent in=new Intent(HomeActivity.this,LoginActivity.class);
                    startActivity(in);
                    finish();*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (currentFragment.equalsIgnoreCase("ChannelFragment")) {
            Log.d("onKeyDown::", ":::" + currentFragment);
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(
                        Context.AUDIO_SERVICE);
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE | AudioManager.FLAG_SHOW_UI);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(
                        Context.AUDIO_SERVICE);
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE | AudioManager.FLAG_SHOW_UI);
                return true;
            }
        } else {
            Log.d("onKeyDown:nochannel:", ":::" + currentFragment);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        try {
            Log.d("onbackpressed::", ":::" + getCallingPackage());

            if (homeactivity_searchbar_layout != null && homeactivity_searchbar_layout.getVisibility() == View.VISIBLE) {
                try {
                    homeactivity_searchbar_layout.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (getmChannelTotalFragment().isYoutubeFullscreen()) {
                    getmChannelTotalFragment().fullScreen(View.VISIBLE);
                    return;
                }

                if (currentFragment.equalsIgnoreCase("ChannelFragment")) {
                    Log.d("onbackpressed::", ":::" + currentFragment);
                    getmChannelFragment().checkYoutubeFullscreen();
                } else if (currentFragment.equalsIgnoreCase("ChannelTotalFragment") || currentFragment.equalsIgnoreCase("ChannelTimelineFragment")) {
                    Log.d("onbackpressed::", ":::" + currentFragment);
                    if (getmChannelTotalFragment().isYoutubeFullscreen()) {
                        getmChannelTotalFragment().fullScreen(View.VISIBLE);
                    } else {
                        mSeriesSeason = "";
                        FrameLayout fl = (FrameLayout) findViewById(R.id.activity_movie_fragemnt_layout);
                        if (fl != null) {
                            fl.removeAllViews();
                        }
                        FrameLayout fll = (FrameLayout) findViewById(R.id.activity_homescreen_fragemnt_layout);
                        if (fll != null) {
                            fll.removeAllViews();
                        }
                        HomeScreenGridLoad();

                        try {
                            FragmentMovieMain.hideFrames();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else if (currentFragment.equalsIgnoreCase("fragmentHomeScreenGrid")) {
                    finish();
                } else {
                    if (currentPageForBackButton.equalsIgnoreCase("moviedetails")) {
                        getmFragmentMoviesLeftContent().goback();
                    } else if (currentPageForBackButton.equalsIgnoreCase("ondemandList")) {
                        getmFragmentOndemandallList().onBackImagePressed();
                    } else {
                        if (Utilities.isMyServiceRunning(HomeActivity.this, RadioService.class)) {
                            Intent radioService = new Intent(HomeActivity.this, RadioService.class);
                            stopService(radioService);
                        }
                        mSeriesSeason = "";
                        FrameLayout fl = (FrameLayout) findViewById(R.id.activity_movie_fragemnt_layout);
                        if (fl != null) {
                            fl.removeAllViews();
                        }

                        FrameLayout fll = (FrameLayout) findViewById(R.id.activity_homescreen_fragemnt_layout);
                        if (fll != null) {
                            fll.removeAllViews();
                        }
                        HomeScreenGridLoad();

                        try {
                            FragmentMovieMain.hideFrames();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        try {
            currentFragment = fragment.getClass().getSimpleName();
            Log.d("onAttachFragment::", ":::" + fragment.getClass().getSimpleName());

            if (getmGameMainFragment() != null) {
                Log.d("gameonAttachFragment::", ":not nulll::");
            } else {
                Log.d("gameonAttachFragment::", ":nulll::");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivity Result:::", "onActivity Result:::");
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    Log.d("onActivity Result:::", "notgranted");
                } else {
                    getFragmentHomeScreenGrid().loadListenData();
                    Log.d("onActivity Result:::", "granted");
                }
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            IntentFilter filter = new IntentFilter();
            filter.addAction(RadioService.RECIEVER_ACTION_PLAYING);
            filter.addAction(RadioService.RECIEVER_ACTION_PREPARING);
            filter.addAction(RadioService.RECIEVER_ACTION_STOPPED);
            filter.addAction(RadioService.RECIEVER_ACTION_CLOSE);
            filter.addAction(RadioService.RECIEVER_ACTION_PREPARE_ERROR);
            registerReceiver(playerStateReceiver, filter);
            /*if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                // Hide the status bar.
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        try {
//            unregisterReceiver(playerStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    /////////////////home screen fragment loadng////////////////////
    public void HomeScreenGridLoad() {
        try {
            //check to diplay subscription or not
            boolean sub = false;
            if (getIntent().hasExtra("welcome")) {
                if (getIntent().getBooleanExtra("welcome", false)) {
                    sub = true;
                }

            }
            if (sub) {
                ruuning_task = false;
                HomeActivity.isPayperview = "";
                activity_homescreen_fragemnt_layout.setVisibility(View.VISIBLE);
                content_position = "";
                toolbarTextChange("", "", "", "");


//                toolbarTextChange("Subscriptions", "", "", "");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                WelcomeVideoFragment welcomeVideoFragment = new WelcomeVideoFragment();
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, welcomeVideoFragment);
                fragmentTransaction.commit();

                getIntent().removeExtra("welcome");

            } else {
                ruuning_task = false;
                HomeActivity.isPayperview = "";
                activity_homescreen_fragemnt_layout.setVisibility(View.VISIBLE);
                content_position = "";
                toolbarTextChange("", "", "", "");


                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                fragmentTransaction.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final BroadcastReceiver m_timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(Intent.ACTION_TIME_CHANGED) ||
                    action.equals(Intent.ACTION_TIMEZONE_CHANGED))

            {
                Log.d("Date changed::", "date changed");
                if (getDatetRefreshListener() != null) {
                    getDatetRefreshListener().onRefresh();
                }
            }
        }
    };

    @Override
    public void onHomeFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAccontDetailFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAccountFragmentInteraction(Uri uri) {

    }

    @Override
    public void onInterestLeftFragmentInteraction(Uri uri) {

    }

    @Override
    public void onInterestRightFragmentInteraction(Uri uri) {


    }

    @Override
    public void onInterestNetworkDialogFragmentInteraction(Uri uri) {

    }


    @Override
    public void onDialogFragmentInteraction() {
        try {
            getmInterestRightFragment().refreshadapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterestDialogFragmentSkipSelected() {
        closeInterestDialog();
    }

    @Override
    public void onInterestChannelFragmentcloseDialog() {
        closeInterestDialog();
    }

    @Override
    public void onDemandFavoriteFragmentInteraction(Uri uri) {

    }

    @Override
    public void ondemandyoutubeFragmentInteraction(Uri uri) {

    }

    @Override
    public void exitlauncher() {

        clearCacheData();


        try {
                    /*PreferenceManager.setLogin(false,m_gContext);
                    PreferenceManager.setAccessToken("", m_gContext);

                    PreferenceManager.setusername("",m_gContext);
                    PreferenceManager.setcity("", m_gContext);
                    PreferenceManager.setfirst_name("", m_gContext);
                    PreferenceManager.setlast_name("", m_gContext);
                    PreferenceManager.setgender("", m_gContext);
                    PreferenceManager.setemail("", m_gContext);
                    PreferenceManager.setstate("", m_gContext);
                    PreferenceManager.setdate_of_birth("", m_gContext);
                    PreferenceManager.setlast_login("", m_gContext);
                    PreferenceManager.setaddress_1("", m_gContext);
                    PreferenceManager.setaddress_2("", m_gContext);
                    PreferenceManager.setpostal_code("", m_gContext);
                    PreferenceManager.setphone_number("", m_gContext);
                    PreferenceManager.setid(0, m_gContext);*/
            Helper.clearCurrentDefaultLauncher(m_gContext);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loadYoutubevideos(List<String> list, int offset) {
        try {
            getmChannelTotalFragment().loadYoutubevideos(list, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void minimizeTimelineView() {
        try {
            getmChannelTotalFragment().minimizeChannelView(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVideoSchedule(ArrayList<ProgramList> v_list) {
        try {
            getmChannelTotalFragment().setVideoSchedule(v_list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setChannelSchedule(ArrayList<ChannelScheduler> scheduler_list) {
        try {
            getmChannelTotalFragment().setChannelSchedule(scheduler_list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void setVideoSchedule(ArrayList<VideoBean> v_list) {
//        try {
//            getmChannelTotalFragment().setVideoSchedule(v_list);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void setChannelSchedule(ArrayList<ChannelScheduler> scheduler_list) {
//        try {
//            getmChannelTotalFragment().setChannelSchedule(scheduler_list);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void setChannellist() {
        try {
            getmChannelTotalFragment().loadChannelList();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateprogress(int progres, String progresstext, String message) {
        try {
            getmChannelTotalFragment().updateloading(progres, progresstext, message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showcontent() {
        try {
            getmChannelTotalFragment().displaycontent(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void refereshcategoryList(String cID) {
        try {
            getmChannelTotalFragment().refreshCategoryList(cID);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setPlayVideo(final ChannelScheduler channelScheduler) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getmChannelTotalFragment().setVideoType(channelScheduler);
            }
        });
    }

    @Override
    public void stopYoutubePlayer() {
        getmChannelTotalFragment().stopYoutubePlayer();
    }

    @Override
    public void stopFullscreenTimer() {
        getmChannelTotalFragment().mStopTimer();
    }

    @Override
    public void videochange(String uri) {
        try {
//            getmChannelTimelineFragment().highlightview(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeminimizeimage(boolean direction) {
        try {
            getmChannelTimelineFragment().changearrowimage(direction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadchannelbyID(String slug) {
        try {
//            getmChannelTimelineFragment().swaplistpositionsByID(slug);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadmoreYoutubeVideos() {

    }

    @Override
    public void onloadChannel(String cid) {
        try {
            getFragmentHomeScreenGrid().loadChannelData(cid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSearchloadChannel(String cid) {
        try {
            getFragmentHomeScreenGrid().loadChannelData(cid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void welcomeVideoFragmentInteractionListener(Uri uri) {

    }

    @Override
    public void onAppFragmentInteraction() {

    }

    public interface DateRefreshListener {
        void onRefresh();
    }

    private void closeInterestDialog() {
        try {
            InterestDialogFragment fragment = (InterestDialogFragment) getSupportFragmentManager().findFragmentByTag("dialogFragment");
            fragment.closeDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
//            unregisterReceiver(m_timeChangedReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toolbarTextChange(final String mtoolbarGridContent, final String mtoolbarMaincontent, final String mtoolbarSubContent, final String mtoolbarlastcontent) {

        try {
            if (checkIsTablet()) {
                if (isPayperview.equals("Channels")) {
                    if (!mtoolbarGridContent.equalsIgnoreCase("") && mtoolbarMaincontent.equalsIgnoreCase("") && mtoolbarSubContent.equalsIgnoreCase("") && mtoolbarlastcontent.equals("")) {
                        activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_ondemand_text.setVisibility(View.GONE);
                        activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_textview.setText("Channels");
                        activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.white));
                        activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                        //activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                        activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                        activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                        activity_channel_final_list_divider.setVisibility(View.GONE);
                        activity_channel_final_list_text.setVisibility(View.GONE);
                        activity_homescreen_toolbar_fullview.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_appmanager.setVisibility(View.GONE);
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
                            //activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                            activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                            activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                            activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                            activity_channel_final_list_divider.setVisibility(View.GONE);
                            activity_channel_final_list_text.setVisibility(View.GONE);
                            activity_homescreen_toolbar_appmanager.setVisibility(View.GONE);
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
                            //activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                            activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                            activity_channel_final_list_divider.setVisibility(View.GONE);
                            activity_channel_final_list_text.setVisibility(View.GONE);
                            activity_homescreen_toolbar_appmanager.setVisibility(View.GONE);
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
                            // activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                            activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                            activity_homescreen_toolbar_appmanager.setVisibility(View.GONE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {


                    if (mtoolbarGridContent.equalsIgnoreCase("On-Demand")) {
                        activity_homescreen_toolbar_appmanager.setVisibility(View.VISIBLE);

                    } else {
                        activity_homescreen_toolbar_appmanager.setVisibility(View.GONE);
                    }


                    if (mtoolbarGridContent.equalsIgnoreCase("") && mtoolbarMaincontent.equalsIgnoreCase("") && mtoolbarSubContent.equalsIgnoreCase("") && mtoolbarlastcontent.equals("")) {
                        activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_ondemand_text.setVisibility(View.GONE);
                        activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_textview.setText("Home");
                        activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.white));
                        activity_homescreen_toolbar_mail.setVisibility(View.VISIBLE);
                        // activity_homescreen_toolbar_info.setVisibility(View.GONE);
                        activity_homescreen_toolbar_home.setVisibility(View.GONE);
                        activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                        activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                        activity_channel_final_list_divider.setVisibility(View.GONE);
                        activity_channel_final_list_text.setVisibility(View.GONE);
                        activity_homescreen_toolbar_fullview.setVisibility(View.GONE);
                        activity_homescreen_toolbar_search.setVisibility(View.VISIBLE);

                        if (HomeActivity.isPayperview.equals("Over the Air")) {
                            activity_homescreen_toolbar_search.setVisibility(View.GONE);
                        } else {
                            activity_homescreen_toolbar_search.setVisibility(View.VISIBLE);
                        }
                    } else if (!mtoolbarGridContent.equalsIgnoreCase("") && mtoolbarMaincontent.equalsIgnoreCase("") && mtoolbarSubContent.equalsIgnoreCase("") && mtoolbarlastcontent.equals("")) {
                        activity_homescreen_toolbar_textview.setText(mtoolbarGridContent);
                        activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_ondemand_text.setVisibility(View.GONE);
                        activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.white));
                        activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                        activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                        activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                        activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                        activity_channel_final_list_divider.setVisibility(View.GONE);
                        activity_channel_final_list_text.setVisibility(View.GONE);
                        activity_homescreen_toolbar_fullview.setVisibility(View.GONE);
                        // activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                        if (HomeActivity.isPayperview.equals("Over the Air")) {
                            activity_homescreen_toolbar_search.setVisibility(View.GONE);
                        } else {
                            activity_homescreen_toolbar_search.setVisibility(View.VISIBLE);
                        }
                    } else if (!mtoolbarGridContent.equalsIgnoreCase("") && !mtoolbarMaincontent.equalsIgnoreCase("") && mtoolbarSubContent.equalsIgnoreCase("") && mtoolbarlastcontent.equals("")) {

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
                        activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                        activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                        activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                        activity_channel_final_list_divider.setVisibility(View.GONE);
                        activity_channel_final_list_text.setVisibility(View.GONE);
                        activity_homescreen_toolbar_fullview.setVisibility(View.GONE);
                        //activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                        if (HomeActivity.isPayperview.equals("Over the Air")) {
                            activity_homescreen_toolbar_search.setVisibility(View.GONE);
                        } else {
                            activity_homescreen_toolbar_search.setVisibility(View.VISIBLE);
                        }
                    } else if (!mtoolbarGridContent.equalsIgnoreCase("") && !mtoolbarMaincontent.equalsIgnoreCase("") && !mtoolbarSubContent.equalsIgnoreCase("") && mtoolbarlastcontent.equals("")) {
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

                        activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                        activity_channel_final_list_divider.setVisibility(View.GONE);
                        activity_channel_final_list_text.setVisibility(View.GONE);
                        activity_homescreen_toolbar_fullview.setVisibility(View.GONE);
                        // activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                        if (HomeActivity.isPayperview.equals("Over the Air")) {
                            activity_homescreen_toolbar_search.setVisibility(View.GONE);
                        } else {
                            activity_homescreen_toolbar_search.setVisibility(View.VISIBLE);
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

                            activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                            activity_homescreen_toolbar_fullview.setVisibility(View.GONE);
                            // activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                            if (HomeActivity.isPayperview.equals("Over the Air")) {
                                activity_homescreen_toolbar_search.setVisibility(View.GONE);
                            } else {
                                activity_homescreen_toolbar_search.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


                activity_homescreen_toolbar_ondemand_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        switch (HomeActivity.isPayperview) {
                            case "Search":
                                try {
                                    if (m_jsonObjectSearchResult != null) {
                                        SwapSearchFragment(true);
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        FragmentSearchMain fragmentSearchMain = new FragmentSearchMain();
                                        fragmentTransaction.replace(R.id.activity_search_fragemnt_layout, fragmentSearchMain);
                                        fragmentTransaction.commit();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case "Channels":

                                //getmChannelFragment().toolbarMainClick();
                                try {
                                    if (getmChannelTotalFragment() != null) {
                                        getmChannelTotalFragment().toolbarMainClick();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;


                            case "Over the Air":
                                getFragmentOTAANDCABLE().mOtaToolbarClick();
                                break;
                            case "Subscriptions":
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                FragmentSubScriptions fragmentSubScriptions = new FragmentSubScriptions();
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentSubScriptions);
                                fragmentTransaction.commit();
                                /*toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                MySubscriptionFragment fragmentSubScriptions = new MySubscriptionFragment();
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentSubScriptions);
                                fragmentTransaction.commit();
                                SwapMovieFragment(false);*/
                                break;

                            case "Listen":
                                if (!activity_homescreen_toolbar_textview.getText().equals("All") || !activity_homescreen_toolbar_maincontent.getText().equals("")) {


                                    try {
                                        if (Utilities.isMyServiceRunning(HomeActivity.this, RadioService.class)) {
                                            Intent radioService = new Intent(HomeActivity.this, RadioService.class);
                                            stopService(radioService);
                                        }
                                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        FragmentListenMain fragmentListenMain = new FragmentListenMain();
                                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentListenMain);
                                        fragmentTransaction.commit();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                break;
                            case "My Interests":

                                try {

                                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    InterestMainFragment mInterestMainFragment = new InterestMainFragment();
                                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, mInterestMainFragment);
                                    fragmentTransaction.commit();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;


                            case "Games":
                                boolean hasData = false;
                                for (int i = 0; i < HomeActivity.m_jsonGameCourasals.length(); i++) {
                                    try {
                                        JSONObject item_object = HomeActivity.m_jsonGameCourasals.getJSONObject(i);
                                        JSONArray item_array = item_object.getJSONArray("items");
                                        if (item_array.length() > 0) {
                                            hasData = true;
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                if (hasData) {
                                    GameMainFragment.sContent_position = Constants.GAMES_MAINCONTENT;
                                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    GamesDetailsFragment fragmentOnDemandAllContent = GamesDetailsFragment.newInstance(Constants.SHOWS, HomeActivity.m_jsonGameCourasals.toString());
                                    fragmentTransaction.replace(R.id.fragment_games_pagerandlist, fragmentOnDemandAllContent);
                                    fragmentTransaction.commit();
                                }


                                break;


                            default:
                                if (!activity_homescreen_toolbar_textview.getText().equals("All") || !activity_homescreen_toolbar_maincontent.getText().equals("")) {
                                    try {
                                        HomeActivity.menu_position = Constants.MAIN_MENU;
                                        HomeActivity.content_position = Constants.MAIN_CONTENT;
                                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        FragmentOnDemandAll fragmentOnDemandAll;
                                        fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home", "");
                                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                                        fragmentTransaction.commit();
                                        SwapMovieFragment(false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                        }
                    }
                });


                activity_homescreen_toolbar_textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            Log.d("toolbar", toolbarMainContent);

                            if (isPayperview.equals("Search")) {
                                if (!activity_homescreen_toolbar_maincontent.getText().equals("")) {
                                    SwapMovieFragment(false);
                                }

                            } else if (isPayperview.equals("Channels")) {
                                if (!mtoolbarSubContent.equals("") && !mtoolbarMaincontent.equals(mtoolbarGridContent)) {
                                    getmChannelTotalFragment().toolbarClickAction(activity_homescreen_toolbar_textview.getText().toString());
                                    //getmChannelFragment().toolbarClickAction();
                                }

                            } else if (isPayperview.equalsIgnoreCase("Subscriptions")) {
                            /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            FragmentSubScriptions fragmentSubScriptions = new FragmentSubScriptions();
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentSubScriptions);
                            fragmentTransaction.commit();*/
                                SwapMovieFragment(false);
                            } else if (isPayperview.equalsIgnoreCase("Listen")) {
                                if (!activity_homescreen_toolbar_maincontent.getText().equals("")) {
                                    swapRadioFragment(false);
                                }

                            } else {
                                if (!activity_homescreen_toolbar_maincontent.getText().equals("")) {

                                    if (selectedmenu.equalsIgnoreCase(Constants.WEB_ORIGINALS_SIDE_MENU) || selectedmenu.equalsIgnoreCase(Constants.KIDS)) {
                                        SwapMovieFragment(false);
                                        if (selectedmenu.equalsIgnoreCase(Constants.WEB_ORIGINALS_SIDE_MENU)) {
                                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                                FragmentTransaction fragmentTransaction_list = getSupportFragmentManager().beginTransaction();
                                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.WEB_ORIGINALS_SIDE_MENU, HomeActivity.m_jsonWebCarousels.toString());
                                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                                fragmentTransaction_list.commit();
                                            }

                                        } else if (selectedmenu.equalsIgnoreCase(Constants.KIDS)) {
                                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                                FragmentTransaction fragmentTransaction_list = getSupportFragmentManager().beginTransaction();
                                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.KIDS, HomeActivity.m_jsonKidsCarousels.toString());
                                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                                fragmentTransaction_list.commit();
                                            }

                                        }


                                    } else {
                                        switch (toolbarMainContent) {
                                            case Constants.ALL_SIDE_MENU: {
                                                HomeActivity.menu_position = Constants.MAIN_MENU;
                                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                                FragmentOnDemandAll fragmentOnDemandAll;
                                                fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home", "");
                                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                                                fragmentTransaction.commit();
                                                SwapMovieFragment(false);

                                                break;
                                            }
                                            case "TV Shows":
                                                if (selectedFilter.equals(Constants.PRIMETIMEANYTIME)) {
                                                    HomeActivity.toolbarMainContent = "Primetime";
                                                    toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                                } else {
                                                    if (!menu_position.equals(Constants.MAIN_MENU)) {
                                                        toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                                                        HomeActivity.menu_position = Constants.FILTER_MENU;
                                                        HomeActivity.content_position = Constants.MAIN_CONTENT;
                                                        HomeActivity.selectedmenu = Constants.TV_SHOWS_SIDE_MENU;
                                                        HomeActivity.selectedFilter = Constants.TV_SHOWS_SIDE_MENU;
                                                        HomeActivity.is_networkSelected = false;
                                                        if (HomeActivity.m_jsonTvshowfeaturedCarousels != null && HomeActivity.m_jsonTvshowfeaturedCarousels.length() > 0) {
                                                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                                                FragmentTransaction fragmentTransaction_list = getSupportFragmentManager().beginTransaction();
                                                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowfeaturedCarousels.toString());
                                                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                                                fragmentTransaction_list.commit();
                                                            }

                                                        }

                                                    } else {
                                                        HomeActivity.menu_position = Constants.MAIN_MENU;
                                                        HomeActivity.content_position = Constants.MAIN_CONTENT;
                                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                                        FragmentOnDemandAll fragmentOnDemandAll;
                                                        fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home", "");
                                                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                                                        fragmentTransaction.commit();
                                                    }
                                                }


                                                SwapMovieFragment(false);
                                                break;
                                            case "TV Networks":


                                                if (!menu_position.equals(Constants.MAIN_MENU)) {
                                                    HomeActivity.menu_position = Constants.FILTER_MENU;
                                                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                                                    HomeActivity.selectedmenu = Constants.TV_SHOWS_SIDE_MENU;

                                                    HomeActivity.is_networkSelected = true;
                                                    HomeActivity.selectedFilter = Constants.NETWORK_FILTER;

                                                    if (HomeActivity.m_jsonNetworkListItems != null && HomeActivity.m_jsonNetworkListItems.length() > 0) {
                                                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                                            FragmentTransaction fragmentTransaction_list = getSupportFragmentManager().beginTransaction();
                                                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.BYNETWORK, HomeActivity.m_jsonNetworkListItems.toString());
                                                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                                            fragmentTransaction_list.commit();
                                                        }

                                                    } else {
                                                        HomeActivity.menu_position = Constants.MAIN_MENU;
                                                        HomeActivity.content_position = Constants.MAIN_CONTENT;
                                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                                        FragmentOnDemandAll fragmentOnDemandAll;
                                                        fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home", "");
                                                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                                                        fragmentTransaction.commit();
                                                    }
                                                }
                                                SwapMovieFragment(false);
                                                break;
                                            case "Movies":
                                                if (!menu_position.equals(Constants.MAIN_MENU)) {
                                                    HomeActivity.selectedmenu = Constants.MOVIES;
                                                    HomeActivity.menu_position = Constants.FILTER_MENU;
                                                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                                                    HomeActivity.selectedFilter = Constants.MOVIES_SIDE_MENU;
                                                    HomeActivity.is_networkSelected = false;


                                                    if (HomeActivity.m_jsonAllmovielist != null && HomeActivity.m_jsonAllmovielist.length() > 0) {
                                                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                                            FragmentTransaction fragmentTransaction_list = getSupportFragmentManager().beginTransaction();
                                                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonAllmovielist.toString());
                                                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                                            fragmentTransaction_list.commit();
                                                        }

                                                    } else {
                                                        HomeActivity.menu_position = Constants.MAIN_MENU;
                                                        HomeActivity.content_position = Constants.MAIN_CONTENT;
                                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                                        FragmentOnDemandAll fragmentOnDemandAll;
                                                        fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home", "");
                                                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                                                        fragmentTransaction.commit();
                                                    }

                                                }
                                                SwapMovieFragment(false);
                                                break;
                                            default: {
                                                if (toolbarMainContent.equalsIgnoreCase("all") && HomeActivity.isPayperview.equals("Pay Per View")) {
                                                    toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                                                    HomeActivity.menu_position = Constants.MAIN_MENU;
                                                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                                                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                                    FragmentOnDemandAll fragmentOnDemandAll;
                                                    fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home", "");
                                                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                                                    fragmentTransaction.commit();
                                                    SwapMovieFragment(false);

                                                } else {
                                                    if (HomeActivity.menu_position.equals(Constants.MAIN_MENU)) {
                                                        HomeActivity.toolbarSubContent = HomeActivity.toolbarMainContent;
                                                        HomeActivity.toolbarMainContent = "TV Shows";
                                                        toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                                    } else if (HomeActivity.menu_position.equals(Constants.MAIN_CONTENT) || HomeActivity.menu_position.equals(Constants.FILTER_MENU)) {


                                                        if (selectedmenu.equals(Constants.MOVIES)) {
                                                            HomeActivity.toolbarSubContent = HomeActivity.toolbarMainContent;
                                                            HomeActivity.toolbarMainContent = "Movies";

                                                            toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                                                        } else if (selectedmenu.equals(Constants.PRIMETIMEANYTIME)) {
                                                            HomeActivity.toolbarSubContent = HomeActivity.toolbarMainContent;
                                                            HomeActivity.toolbarMainContent = "Primetime";

                                                            toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                                        } else {
                                                            if (HomeActivity.is_networkSelected) {
                                                                HomeActivity.toolbarSubContent = HomeActivity.toolbarMainContent;

                                                                HomeActivity.toolbarMainContent = "TV Networks";
                                                                toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                                            } else {
                                                                HomeActivity.toolbarSubContent = HomeActivity.toolbarMainContent;

                                                                HomeActivity.toolbarMainContent = "TV Shows";
                                                                toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                                            }

                                                        }

                                                    }
                                                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                                    FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, showId);
                                                    fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                                    fragmentTransaction.commit();
                                                }


                                                break;
                                            }
                                        }
                                    }


                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            /*if (name.equals("FragmentHomeScreenGrid")) {
                switch (name) {

                    case "FragmentHomeScreenGrid":
                        activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_ondemand_text.setVisibility(View.GONE);
                        activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_textview.setText("Home");
                        activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.text_blue));
                        activity_homescreen_toolbar_mail.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_info.setVisibility(View.GONE);
                        activity_homescreen_toolbar_home.setVisibility(View.GONE);

                        break;
                    case "FragmentTvShows":
                        activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_ondemand_text.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_textview.setText("TV Shows");
                        activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.text_lite_blue));
                        activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                        activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                        break;
                    case Constants.TV_NETWORKS_SIDE_MENU:

                        activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_ondemand_text.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_textview.setText("TV Networks");
                        activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.text_lite_blue));
                        activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                        activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                        break;


                    case "Movies":
                        activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_ondemand_text.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_textview.setText("Movies");
                        activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.text_lite_blue));
                        activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                        activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                        break;

                }
            } else {


                if (HomeGridClick.equals("Pay Per View")) {

                    if (!maincontent.equals("")) {
                        activity_homescreen_maincontent_divider.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_maincontent.setVisibility(View.VISIBLE);
                    } else {
                        activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                        activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                    }

                    activity_homescreen_toolbar_ondemand_text.setText("Pay Per View");
                    activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                    activity_homescreen_toolbar_ondemand_text.setVisibility(View.VISIBLE);
                    activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                    activity_homescreen_toolbar_textview.setText(Utilities.stripHtml(name));
                    activity_homescreen_toolbar_maincontent.setText(Utilities.stripHtml(maincontent));
                    activity_homescreen_toolbar_ondemand_text.setTextColor(getResources().getColor(R.color.text_lite_blue));
                    activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.text_lite_blue));
                    activity_homescreen_toolbar_maincontent.setTextColor(getResources().getColor(R.color.white));
                    activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                    activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                    activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);


                } else {


                    activity_homescreen_toolbar_ondemand_text.setText("On-Demand");
                    if (isFilterordeatils) {


                        if (!maincontent.equals("")) {
                            activity_homescreen_maincontent_divider.setVisibility(View.VISIBLE);
                            activity_homescreen_toolbar_maincontent.setVisibility(View.VISIBLE);
                        } else {
                            activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                            activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                        }


                        activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_ondemand_text.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_textview.setText(Utilities.stripHtml(name));
                        activity_homescreen_toolbar_maincontent.setText(Utilities.stripHtml(maincontent));
                        activity_homescreen_toolbar_ondemand_text.setTextColor(getResources().getColor(R.color.text_lite_blue));
                        activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.text_lite_blue));
                        activity_homescreen_toolbar_maincontent.setTextColor(getResources().getColor(R.color.white));
                        activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                        activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);

                    } else {


                        if (!maincontent.equals("")) {
                            activity_homescreen_maincontent_divider.setVisibility(View.VISIBLE);
                            activity_homescreen_toolbar_maincontent.setVisibility(View.VISIBLE);
                        } else {
                            activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                            activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                        }

                        activity_homescreen_toolbar_textview.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_ondemand_text.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_divider.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_textview.setText(Utilities.stripHtml(name));
                        activity_homescreen_toolbar_textview.setTextColor(getResources().getColor(R.color.text_lite_blue));
                        activity_homescreen_toolbar_mail.setVisibility(View.GONE);
                        activity_homescreen_toolbar_info.setVisibility(View.VISIBLE);
                        activity_homescreen_toolbar_home.setVisibility(View.VISIBLE);
                        activity_homescreen_maincontent_divider.setVisibility(View.GONE);
                        activity_homescreen_toolbar_maincontent.setVisibility(View.GONE);
                        mfiltername = activity_homescreen_toolbar_textview.getText().toString();
                    }
                }






               *//* if (content_position.equals(Constants.MAIN_CONTENT)||content_position.equals(Constants.SUB_RIGHT_CONTENT) ||content_position.equals(Constants.SUB_CONTENT))
                {
                }*//*


            }*/

            }

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }

    public void swapRadioFragment(boolean b) {
        if (b) {
            try {
                activity_homescreen_fragemnt_layout.setVisibility(View.GONE);
                activity_radio_fragemnt_layout.setVisibility(View.VISIBLE);
                activity_search_fragemnt_layout.setVisibility(View.GONE);
                activity_movie_fragemnt_layout.setVisibility(View.GONE);

                setmFragmentMovieMain(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                activity_homescreen_fragemnt_layout.setVisibility(View.VISIBLE);
                activity_search_fragemnt_layout.setVisibility(View.GONE);
                activity_radio_fragemnt_layout.setVisibility(View.GONE);
                activity_movie_fragemnt_layout.setVisibility(View.GONE);

                if (Utilities.isMyServiceRunning(HomeActivity.this, RadioService.class)) {
                    Intent radioService = new Intent(HomeActivity.this, RadioService.class);
                    stopService(radioService);
                }
                setmFragmentMovieMain(null);
                if (FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_RADIO)) {
                    switch (FragmentListenMain.mRadioMainContent) {
                        case Constants.RADIO_GENRE:
                            HomeActivity.toolbarSubContent = FragmentListenList.mListenSubList.get(FragmentListenList.mListPosition);
                            toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                            //                        getFragmentListenGrid().mLocationAdapterBackFunction();
                            break;
                        case Constants.RADIO_LANGUAGE:
                            HomeActivity.toolbarSubContent = FragmentListenList.mListenSubList.get(FragmentListenList.mListPosition);
                            toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                            //                        getFragmentListenGrid().mLocationAdapterBackFunction();
                            break;
                        case Constants.RADIO_LOCATION:
                            HomeActivity.toolbarSubContent = FragmentListenList.mListenSubList.get(FragmentListenList.mListPosition);
                            toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                            //                        getFragmentListenGrid().mLocationAdapterBackFunction();
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public void SwapSearchFragment(boolean b) {
        try {
            if (b) {
                try {
                    activity_homescreen_fragemnt_layout.setVisibility(View.GONE);
                    activity_search_fragemnt_layout.setVisibility(View.VISIBLE);
                    activity_movie_fragemnt_layout.setVisibility(View.GONE);
                    getmChannelTotalFragment().mStopTimer();
                    setmFragmentMovieMain(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    activity_movie_fragemnt_layout.setVisibility(View.GONE);
                    activity_search_fragemnt_layout.setVisibility(View.GONE);
                    activity_homescreen_fragemnt_layout.setVisibility(View.VISIBLE);
                    setmFragmentMovieMain(null);


                    if (isPayperview.equals("Channels")) {
                        switch (ChannelTotalFragment.mChannelList) {
                            case Constants.CHANNEL_DECADECONTENT:
                                break;
                            case Constants.CHANNEL_SUBCONTENT:

                                try {

                                    toolbarTextChange("Channels", ChannelTotalFragment.mtoolbarMaincontent, ChannelTotalFragment.mtoolbarSubContent, "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                break;
                            case Constants.CHANNEL_SUBCATEGORY:
                                try {


                                    try {

                                        if (ChannelFragment.temp.length() == 0) {
                                            toolbarTextChange("Channels", ChannelTotalFragment.mtoolbarMaincontent, ChannelTotalFragment.mtoolbarSubContent, "");

                                        } else {

                                            if (ChannelTotalFragment.mtoolbarMaincontent.equals("TV")) {
                                                toolbarTextChange("Channels", ChannelTotalFragment.mtoolbarMaincontent, "", "");


                                            }


                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                break;
                            case Constants.CHANNEL_MAINCONTENT:
                                if (ChannelFragment.temp.length() == 0) {
                                    toolbarTextChange("Channels", ChannelTotalFragment.mtoolbarMaincontent, ChannelTotalFragment.mtoolbarSubContent, "");

                                } else {

                                    if (ChannelTotalFragment.mtoolbarMaincontent.equals("TV")) {
                                        toolbarTextChange("Channels", ChannelTotalFragment.mtoolbarMaincontent, "", "");


                                    }
                                }

                                break;
                            default:
                                try {
                                    toolbarTextChange("Channels", "", "", "");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    } else if (isPayperview.equals("Over the Air")) {

                        switch (FragmentOTAANDCABLE.isOtaCategories) {
                            case Constants.OTA_SUBCONTENT:

                                toolbarTextChange(HomeActivity.isPayperview, FragmentOTAANDCABLE.mOtaCategories, "", "");

                                break;
                            default:
                                toolbarTextChange(HomeActivity.isPayperview, "", "", "");

                                break;
                        }


                    } else if (HomeActivity.menu_position.equals(Constants.MAIN_MENU)) {


                        switch (HomeActivity.content_position) {
                            case Constants.MAIN_CONTENT:
                                HomeActivity.toolbarMainContent = "Home";
                                HomeActivity.toolbarSubContent = "";
                                toolbarGridContent = toolprevGridcontent;
                                toolbarTextChange(HomeActivity.toolprevGridcontent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                                break;
                            default:
                                toolbarTextChange("", "", "", "");
                                break;
                        }


                    } else if (HomeActivity.menu_position.equals(Constants.MAIN_CONTENT) || HomeActivity.menu_position.equals(Constants.FILTER_MENU)) {


                        switch (selectedmenu) {
                            case Constants.MOVIES:
                                HomeActivity.toolbarMainContent = "Movies";
                                HomeActivity.toolbarSubContent = "";
                                toolbarGridContent = toolprevGridcontent;
                                toolbarTextChange(HomeActivity.toolprevGridcontent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                                break;
                            default:
                                if (HomeActivity.is_networkSelected) {
                                    HomeActivity.toolbarMainContent = "TV Networks";
                                    HomeActivity.toolbarSubContent = "";
                                    toolbarGridContent = toolprevGridcontent;
                                    toolbarTextChange(HomeActivity.toolprevGridcontent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                } else {
                                    HomeActivity.toolbarMainContent = "TV Shows";
                                    HomeActivity.toolbarSubContent = "";
                                    toolbarGridContent = toolprevGridcontent;
                                    toolbarTextChange(HomeActivity.toolprevGridcontent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                }

                                break;
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


    public void SwapMovieFragment(boolean b) {
        try {
            isFilterordeatils = b;
            if (b) {
                // HomeActivity.content_position = Constants.SUB_CONTENT;

                activity_homescreen_fragemnt_layout.setVisibility(View.GONE);
                activity_search_fragemnt_layout.setVisibility(View.GONE);
                activity_movie_fragemnt_layout.setVisibility(View.VISIBLE);

            } else {


                switch (HomeActivity.isPayperview) {
                    case "Search":

                        try {
                            activity_movie_fragemnt_layout.setVisibility(View.GONE);
                            activity_search_fragemnt_layout.setVisibility(View.VISIBLE);
                            activity_homescreen_fragemnt_layout.setVisibility(View.GONE);
                            setmFragmentMovieMain(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        try {
                            activity_movie_fragemnt_layout.setVisibility(View.GONE);
                            activity_search_fragemnt_layout.setVisibility(View.GONE);
                            activity_homescreen_fragemnt_layout.setVisibility(View.VISIBLE);
                            setmFragmentMovieMain(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }

                try {
                    FrameLayout fl = (FrameLayout) findViewById(R.id.activity_movie_fragemnt_layout);
                    fl.removeAllViews();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                switch (isPayperview) {
                    case "Search":
                        switch (FragmentSearchList.mSearchContent) {
                            case Constants.SEARCH_SUBCONTENT:
                                FragmentSearchList.mSearchContent = Constants.SEARCH_MAINCONTENT;
                                HomeActivity.selectedmenu = "";
                                HomeActivity.isSearchClick = true;
                                HomeActivity.toolbarSubContent = "";
                                toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                                break;
                            case Constants.SEARCH_MAINCONTENT:
                                HomeActivity.toolbarSubContent = "";
                                toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                break;
                        }
                        break;
                    case "Subscriptions":

                        toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");


                        break;
                    case "Listen":
                        HomeActivity.toolbarSubContent = "";
                        toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        break;
                    default:
                        switch (HomeActivity.menu_position) {
                            case Constants.MAIN_MENU:
                                if (selectedmenu.equalsIgnoreCase(Constants.WEB_ORIGINALS_SIDE_MENU) || selectedmenu.equalsIgnoreCase(Constants.KIDS)) {

                                    HomeActivity.toolbarSubContent = "";
                                    toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                } else {
                                    HomeActivity.toolbarMainContent = "Home";
                                    HomeActivity.toolbarSubContent = "";
                                    toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                }


                                break;
                            case Constants.MAIN_CONTENT:
                            case Constants.FILTER_MENU:


                                if (selectedmenu.equals(Constants.MOVIES)) {
                                    HomeActivity.toolbarMainContent = "Movies";
                                    HomeActivity.toolbarSubContent = "";
                                    toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                                } else if (selectedFilter.equals(Constants.PRIMETIMEANYTIME)) {
                                    HomeActivity.toolbarMainContent = "Primetime";

                                    toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                } else {
                                    if (HomeActivity.is_networkSelected) {
                                        HomeActivity.toolbarMainContent = "TV Networks";
                                        HomeActivity.toolbarSubContent = "";
                                        toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                    } else {
                                        HomeActivity.toolbarMainContent = "TV Shows";
                                        HomeActivity.toolbarSubContent = "";
                                        toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                    }

                                }

                                break;
                        }
                        break;
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void mSlidertodeatails(JSONObject jsonObject) {

        try {
            JSONObject data_object = new JSONObject(jsonObject.toString());

            int id = 1;
            String type = "";
            if (data_object.has("entity_id")) {
                id = data_object.getInt("entity_id");
            }
            if (data_object.has("type")) {
                type = data_object.getString("type");
            }


            SwapMovieFragment(true);
            if (type.equalsIgnoreCase("m") || type.equalsIgnoreCase("movie")) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, id);
                fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, id);
                fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                fragmentTransaction.commit();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void mMovieSlidertodeatails(JSONObject jsonObject) {

        try {
            JSONObject data_object = new JSONObject(jsonObject.toString());

            int id = 1;
            if (data_object.has("entity_id")) {
                id = data_object.getInt("entity_id");
            }


            SwapMovieFragment(true);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, id);
            fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
            fragmentTransaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoadingSearchTask extends AsyncTask<String, Object, Object> {
        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            mProgressHUD = ProgressHUD.show(HomeActivity.this, "Search...", true, false, null);

        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                String term = params[0];
                m_jsonObjectSearchResult = JSONRPCAPI.getSearchResult(term, 100);
                if (m_jsonObjectSearchResult == null) return null;
                Log.d("searchResult", "values" + m_jsonObjectSearchResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                mProgressHUD.dismiss();

                if (m_jsonObjectSearchResult != null) {
                    SwapSearchFragment(true);
                    toolprevGridcontent = activity_homescreen_toolbar_ondemand_text.getText().toString();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    FragmentSearchMain fragmentSearchMain = new FragmentSearchMain();
                    fragmentTransaction.replace(R.id.activity_search_fragemnt_layout, fragmentSearchMain);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to retrieve data.", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void mChannelProgress(Boolean aBoolean, String content_position, String progress, float mProgress) {
        if (aBoolean) {
            linearFullScreen.setVisibility(View.VISIBLE);
            szSplashTitle = content_position;
            txt_splash_title.setText(content_position);
            splash_progress.setProgressColor(Color.parseColor("#fce623"));
            splash_progress.setBackgroundColor(0);
            splash_progress.setProgressBackgroundColor(Color.parseColor("#cecece"));
            splash_progress.setRadius(30);
            splash_progress.setMax(100);
            splash_progress.setProgress(mProgress);
            txt_splash_progress.setText(progress);
        } else {
            linearFullScreen.setVisibility(View.GONE);
        }

    }

    private void focusChangelistener(final EditText editfocus) {


        editfocus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editfocus.setCompoundDrawables(null, null, editfocus.getText().toString().equals("") ? null : x, null);
                } else {
                    editfocus.setCompoundDrawables(null, null, null, null);
                }
            }
        });

    }

    private void addTextChangelistener(final EditText editext) {

        editext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editext.setCompoundDrawables(null, null, editext.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editext.setCompoundDrawables(null, null, editext.getText().toString().equals("") ? null : x, null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                editext.setCompoundDrawables(null, null, editext.getText().toString().equals("") ? null : x, null);

            }
        });

    }

    private void settouchlistener(final EditText edit) {
        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                if (edit.getCompoundDrawables()[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > edit.getWidth() - edit.getPaddingRight() - x.getIntrinsicWidth()) {
                    edit.setText("");
                    edit.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
    }

    public void refreshSerchListData(String type, int count) {
        getmFragmentSearchList().onViewwSelected(type, count);
    }


    public void mHidetoolbar(Boolean is_channelclicked) {
        if (is_channelclicked) {
            layout_toolbar_main.setVisibility(View.GONE);
        } else {
            layout_toolbar_main.setVisibility(View.VISIBLE);
        }
    }

    public void changeSubscriptionList(int pos, boolean b, int s) {
        getFragmentSublist().swapList(pos, b, s);
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi != null && mWifi.isConnected();
    }


    public void initReceiver() {
        playerStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Receiving bradcast::::", "::::" + intent.getAction());
                if (getmFragmentRadioDetails() != null) {
                    getmFragmentRadioDetails().setRadioBar(intent);
                }
            }
        };

    }

    private boolean checkIsTablet() {
        String inputSystem;
        inputSystem = Build.ID;
        Log.d("hai", inputSystem);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        Log.d("hai", width + "");
        Log.d("hai", height + "");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(width / dm.xdpi, 2);
        double y = Math.pow(height / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Log.d("hai", "Screen inches : " + screenInches + "");
        if (screenInches > 6.0) {
            return true;
        } else {
            return true;
        }

    }

    public void displayapps(boolean display, String data) {
        getmFragmentMovieMain().displayApps(display, data);
    }

    public void clearCacheData() {
        try {
            //PreferenceManager.setFirstTime(false, m_gContext);
            m_jsonArrayCategories = new JSONArray();
            m_jsonArrayCategories = new JSONArray();
            m_jsonLiveItems = new JSONArray();
            objSchedule = new JSONObject();
            m_jsonRadioContinent = new JSONArray();
            m_jsonRadioGenre = new JSONArray();
            m_jsonRadioLanguage = new JSONArray();
            m_jsonSportsCarousels = new JSONArray();
            m_jsonMovieSlider = new JSONArray();
            m_jsonShowSlider = new JSONArray();
            m_jsonSlider = new JSONArray();
            m_jsonWebCarousels = new JSONArray();
            m_jsonKidsCarousels = new JSONArray();
            m_jsonWebSlider = new JSONArray();
            m_jsonKidsSlider = new JSONArray();
            m_jsonSportsSlider = new JSONArray();
            m_jsonPrimeTimeList = new JSONArray();
            m_jsonPrimeTimeSlider = new JSONArray();
            m_jsonTvshowbyCategoryItems = new JSONArray();
            m_jsonCategorySlider = new JSONArray();
            m_jsonTvshowCarousels = new JSONArray();
            m_jsonDemandListItems = new JSONArray();
            m_jsonTvshowlistbyGenre = new JSONArray();
            m_jsonTvshowlistbyDecade = new JSONArray();
            m_jsonMovieGenre = new JSONArray();
            m_jsonGameCourasals = new JSONArray();
            m_jsonNetworkList = new JSONArray();
            m_jsonNetworkListItems = new JSONArray();
            m_jsonGenreListbyId = new JSONArray();
            m_jsonDeacdeListbyId = new JSONArray();
            m_jsonTvshowbyCategoryList = new JSONArray();
            m_jsonTvshowbyCategoryListAll = new JSONArray();
            m_jsonTvshowfeaturedCarousels = new JSONArray();
            m_jsonOndemandCarousels = new JSONArray();
            m_jsonHomeSlider = new JSONArray();
            m_jsonGameSlider = new JSONArray();
            allCarousels = new JSONArray();
            m_jsonAllmovielist = new JSONArray();
            m_jsonPayPerViewmovielist = new JSONArray();
            m_jsonPayPerViewShowlist = new JSONArray();
            m_jsonAllmovielistbyrating = new JSONArray();
            m_jsonmoviesbyrating = new JSONArray();
            m_jsonPPVALL = new JSONArray();

            networkDetails.clear();
            mOnDemandlist.clear();
            try {
                if (web_slider_list != null)
                    web_slider_list.clear();
                if (kids_slider_list != null)
                    kids_slider_list.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            gridBeans.clear();
            network_titles.clear();
            all_titles.clear();
            tvShow_titles.clear();
            tv_byCategory_titles.clear();
            tv_byDecade_titles.clear();
            searchBeanArrayList.clear();
            subScriptionBeans.clear();
            mSubscriptionSubList.clear();
            mSubscriptiondataList.clear();
            listen_list.clear();
            slider_list.clear();
            if (channelCategoryList != null) {
                channelCategoryList.clear();
            }

            tvShowsList.clear();
            moviesList.clear();
            movieGenresList.clear();
            channelsList.clear();
            tvnetworksList.clear();
            videoLibrariesList.clear();


            try {
                mSeriesSeason = "";
                FrameLayout fl = (FrameLayout) findViewById(R.id.activity_movie_fragemnt_layout);
                fl.removeAllViews();

                FrameLayout fll = (FrameLayout) findViewById(R.id.activity_homescreen_fragemnt_layout);
                fll.removeAllViews();
                HomeScreenGridLoad();

                try {
                    FragmentMovieMain.hideFrames();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Cache data cleared.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
