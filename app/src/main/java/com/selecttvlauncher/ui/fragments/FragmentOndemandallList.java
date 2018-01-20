package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.BeanClass.GridBean;
import com.selecttvlauncher.BeanClass.SideMenu;
import com.selecttvlauncher.BeanClass.SliderBean;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FragmentOndemandallList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String mCategoryid = String.valueOf(0);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String mBachbuttonNavigation;

    private RecyclerView fragment_ondemand_alllist_items;
    ArrayList<SideMenu> mOnDemandlist = new ArrayList<>();
    OnDemandListAdapter onDemandListAdapter;
    private LinearLayoutManager mLayoutManager;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    TextView fragment_ondemand_switchtext_all;
    TextView fragment_ondemand_switchtext_free;
    ImageView fragment_ondemand_prev_icon;
    private FragmentHomeScreenGrid fragmentHomeScreenGrid;
    private FragmentMoviesLeftContent fragmentMoviesLeftContent;
    private FrameLayout content_layout;
    private ImageView switch_image;
    private boolean isswitchselected = false;

    ProgressBar fragment_ondemand_progress;
    TvShowsSubCategory tvShowsByAdapter;
    int mListIdbyGenre;
    String mGenreIdfromList;
    public static String mAdapter_genreordecade = "";
    private int mSelectedItem = 0;
    private int mSelectedItemgenre = 0;
    private ArrayList<SideMenu> payperViewList = new ArrayList<>();
    LinearLayout fragment_ondemand_layout;
    LinearLayout fragment_ondemand_alllist_items_layout, fragment_ondemand_switch_layout;
    FragmentTransaction fragmentTransaction;
    ArrayList<GridBean> gridBeans = new ArrayList<>();
    public static String dayofweek;
    private int mPrimeTimeSelectedItem;
    private TextView fragment_ondemand_selected_item_text;

    public static boolean isdestroyed = false;
    private RelativeLayout network_spinner_layout;
    private Spinner spinner_network;
    ArrayAdapter<String> adapter;
    ArrayList<String> arraynetworkAdapterData = new ArrayList<>();
    private NetworkListAdapter networkListAdapter;
    private int selectedmoviegenreID, selectedmovieRatingID;
    private Tracker mTracker;


    public static FragmentOndemandallList newInstance(String param1, String param2) {
        FragmentOndemandallList fragment = new FragmentOndemandallList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentOndemandallList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        isdestroyed = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_ondemandall_list, container, false);
        HomeActivity.currentPageForBackButton = "ondemandList";
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        network_spinner_layout = (RelativeLayout) view.findViewById(R.id.network_spinner_layout);
        spinner_network = (Spinner) view.findViewById(R.id.spinner_network);
        fragment_ondemand_selected_item_text = (TextView) view.findViewById(R.id.fragment_ondemand_selected_item_text);
        fragment_ondemand_layout = (LinearLayout) view.findViewById(R.id.fragment_ondemand_layout);
        fragment_ondemand_alllist_items_layout = (LinearLayout) view.findViewById(R.id.fragment_ondemand_alllist_items_layout);
        fragment_ondemand_switch_layout = (LinearLayout) view.findViewById(R.id.fragment_ondemand_switch_layout);
        fragment_ondemand_prev_icon = (ImageView) view.findViewById(R.id.fragment_ondemand_prev_icon);
        fragment_ondemand_switchtext_all = (TextView) view.findViewById(R.id.fragment_ondemand_switchtext_all);
        fragment_ondemand_switchtext_free = (TextView) view.findViewById(R.id.fragment_ondemand_switchtext_free);
        fragment_ondemand_alllist_items = (RecyclerView) view.findViewById(R.id.fragment_ondemand_alllist_items);
        fragment_ondemand_progress = (ProgressBar) view.findViewById(R.id.fragment_ondemand_progress);
        HomeActivity.menu_position = Constants.MAIN_MENU;
        HomeActivity.selectedmenu = Constants.ALL_SIDE_MENU;

        ((HomeActivity) getActivity()).setmFragmentOndemandallList(this);

        switch_image = (ImageView) view.findViewById(R.id.switch_image);
       /* isswitchselected = false;
        switch_image.setImageResource(R.drawable.off);
*/
      /*  isswitchselected = true;
        HomeActivity.mfreeorall = 1;
        switch_image.setImageResource(R.drawable.on);*/

        try {
            if (HomeActivity.mfreeorall == 1) {
                isswitchselected = true;
                switch_image.setImageResource(R.drawable.on);
                fragment_ondemand_switchtext_all.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                fragment_ondemand_switchtext_free.setTextColor(ContextCompat.getColor(getActivity(),R.color.text_light_grey));
            } else if (HomeActivity.mfreeorall == 0) {
                isswitchselected = false;
                switch_image.setImageResource(R.drawable.off);
                fragment_ondemand_switchtext_all.setTextColor(ContextCompat.getColor(getActivity(),R.color.text_light_grey));
                fragment_ondemand_switchtext_free.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setHoverEffects();


        try {
            if (HomeActivity.isPayperview.equals("Pay Per View")) {
                Utilities.setAnalytics(mTracker, "PayPerView");
                HomeActivity.mfreeorall = 2;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fragment_ondemand_layout.getLayoutParams();
                params.setMargins(0, 20, 0, 0);
                fragment_ondemand_layout.setLayoutParams(params);


                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) fragment_ondemand_alllist_items_layout.getLayoutParams();
                params1.setMargins(32, 0, 0, 0);
                fragment_ondemand_alllist_items_layout.setLayoutParams(params1);

                fragment_ondemand_layout.setOrientation(LinearLayout.HORIZONTAL);
                listPayperView();
                switch_image.setVisibility(View.GONE);
                fragment_ondemand_switchtext_all.setVisibility(View.GONE);
                fragment_ondemand_switchtext_free.setVisibility(View.GONE);
            } else if (HomeActivity.isPayperview.equals("On-Demand")) {
                Utilities.setAnalytics(mTracker, "OnDemand");
                //HomeActivity.mfreeorall = 0;
                // HomeActivity.mfreeorall = 1;
                fragment_ondemand_layout.setOrientation(LinearLayout.VERTICAL);
                if (isNetworkAvailable(getActivity()) || isWifiAvailable(getActivity())) {
                    if (mParam1.equalsIgnoreCase("moviegenre")) {
                        HomeActivity.toolbarGridContent = "On-Demand";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                        loadgenre();

                    } else if (mParam1.equalsIgnoreCase("network")) {
                        HomeActivity.toolbarGridContent = "On-Demand";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                        loadnetwork();

                    } else {
                        if (HomeActivity.m_jsonDemandListItems != null && HomeActivity.m_jsonDemandListItems.length() > 0) {
                            setListData(HomeActivity.m_jsonDemandListItems);

                        } else {
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingSideMenuTask().execute();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                }


                mBachbuttonNavigation = "OnDemandMainCategory";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragment_ondemand_switchtext_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!isswitchselected) {
                        switch_image.performClick();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        fragment_ondemand_switchtext_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isswitchselected) {
                        switch_image.performClick();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        switch_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    clearLoadeddata();
                    HomeActivity.m_jsonShowSlider = null;
                    HomeActivity.m_jsonMovieSlider = null;
                    if (isswitchselected) {
                        isswitchselected = false;
                        HomeActivity.mfreeorall = 0;
                        switch_image.setImageResource(R.drawable.off);
                        Log.d("mfreeorall::", "id::" + HomeActivity.mfreeorall);
                        fragment_ondemand_switchtext_all.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                        fragment_ondemand_switchtext_free.setTextColor(ContextCompat.getColor(getActivity(),R.color.text_light_grey));

                    } else {
                        isswitchselected = true;
                        HomeActivity.mfreeorall = 1;
                        switch_image.setImageResource(R.drawable.on);
                        Log.d("mfreeorall::", "id::" + HomeActivity.mfreeorall);
                        fragment_ondemand_switchtext_all.setTextColor(ContextCompat.getColor(getActivity(),R.color.text_light_grey));
                        fragment_ondemand_switchtext_free.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                    }

                    switch (HomeActivity.selectedmenu) {
                        case Constants.ALL_SIDE_MENU:
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingTvShowTVfeaturedCarousels().execute();
                            }

                            break;
                        case Constants.SPORTS:
                            new LoadingSportsList().execute();
                            break;
                        case Constants.PRIMETIMEANYTIME:
                            new LoadingPrimetimeCarousels().execute();

                            break;
                        case Constants.WEB_ORIGINALS_SIDE_MENU:
                            new LoadingWeboriginalList().execute();

                            break;
                        case Constants.KIDS:
                            new LoadingKidsList().execute();

                            break;

                        case Constants.TV_SHOWS_SIDE_MENU:


                            if (HomeActivity.selectedFilter.equals(Constants.NETWORK_FILTER)) {


                                //                            listDataReset();
                                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                    new LoadingNetworkList().execute();
                                }


                            } else {


                                switch (HomeActivity.selectedFilter) {
                                    case Constants.GENRE_FILTER:
                                        mGenreIdfromList = mOnDemandlist.get(mSelectedItemgenre).getId();
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingGenereListById().execute(mGenreIdfromList);
                                        }


                                        break;
                                    case Constants.DECADE_FILTER:
                                        mGenreIdfromList = mOnDemandlist.get(mSelectedItemgenre).getId();
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingDecadeListById().execute(mGenreIdfromList);
                                        }


                                        break;
                                    case Constants.CATEGORY_FILTER:
                                        new LoadingCategoryItems().execute(mCategoryid);
                                        break;
                                    case Constants.TV_NETWORKS_SIDE_MENU:
                                        break;
                                    default:

                                        listDataReset();

                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingTvShowCarousels().execute();
                                        }


                                        break;

                                }


                            }


                            break;

                        case Constants.MOVIES:

                            switch (HomeActivity.selectedFilter) {
                                case Constants.GENRE_FILTER:


                                   /* if (HomeActivity.m_jsonMovieGenre.length() > 0) {
                                        MovieGenreList(HomeActivity.m_jsonMovieGenre);

                                    } else {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingMovieGenre().execute();
                                        }

                                    }*/

                                    if (HomeActivity.m_jsonMovieGenre.length() > 0) {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                                if (mParam1.equalsIgnoreCase("moviegenre")) {
                                                    new LoadingMovieListByGenre().execute(mParam2);
                                                } else {
                                                    new LoadingMovieListByGenre().execute(mGenreIdfromList);
                                                }

                                            }
                                        }

                                    } else {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingMovieGenre().execute();
                                        }

                                    }


                                    break;
                                case Constants.RATING_FILTER:
                                    if (HomeActivity.m_jsonAllmovielistbyrating.length() > 0) {


                                        try {
                                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                                new LoadingMovieListByRating().execute(mGenreIdfromList);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        new LoadingMoviebyRating().execute();
                                    }

                                    break;
                                default:
                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingAllMovieList().execute();
                                    }

                                    break;
                            }


                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        text_font_typeface();
//        fragment_ondemand_switchtext_all.setTypeface(OpenSans_Semibold);
        fragment_ondemand_switchtext_free.setTypeface(OpenSans_Semibold);
        try {

            fragment_ondemand_prev_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (HomeActivity.menu_position.equalsIgnoreCase(Constants.MAIN_MENU) && HomeActivity.content_position.equalsIgnoreCase(Constants.MAIN_CONTENT)) {
                        Log.d("====>", "Go to home page");

                        ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                        setAnalyticreport("", "", "");
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                       /* View t_view=getView().findViewById(R.id.activity_homescreen_fragemnt_layout);
                        if(t_view!=null){

                        }*/
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                        fragmentTransaction.commit();
                    } else if (HomeActivity.menu_position.equalsIgnoreCase(Constants.MAIN_MENU) && HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_CONTENT)) {


                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                        HomeActivity.menu_position = Constants.MAIN_MENU;
                        HomeActivity.content_position = Constants.MAIN_CONTENT;
                        if (HomeActivity.isPayperview.equals("Pay Per View")) {
                            HomeActivity.toolbarMainContent = Constants.ALL_SIDE_MENU;
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            FragmentOnDemandAll fragmentOnDemandAllContent = FragmentOnDemandAll.newInstance("home", "");
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAllContent);
                            fragmentTransaction.commit();

                        } else {

                            if (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_RIGHT_CONTENT)) {
                                Log.d("====>", "Go to main content SUB_RIGHT_CONTENT");
                                HomeActivity.toolbarMainContent = "TV Networks";
                                HomeActivity.toolbarSubContent = "";
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                if (HomeActivity.allCarousels != null) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        GridFragment listfragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.allCarousels.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                        HomeActivity.content_position = Constants.SUB_CONTENT;
                                    }

                                }
                            } else {

                                if (!HomeActivity.selectedmenu.equals(Constants.SPORTS)) {
                                    HomeActivity.toolbarMainContent = "Home";
                                    HomeActivity.toolbarSubContent = "";
                                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                    if (HomeActivity.m_jsonOndemandCarousels != null && HomeActivity.m_jsonOndemandCarousels.length() > 0) {
                                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                            FragmentOnDemandAllContent fragmentOnDemandAllContent = FragmentOnDemandAllContent.newInstance(Constants.SHOWS, HomeActivity.m_jsonOndemandCarousels.toString());
                                            fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, fragmentOnDemandAllContent);
                                            fragmentTransaction.commit();
                                        }

                                    } else {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingTvShowTVfeaturedCarousels().execute();
                                        }
                                    }
                                } else {
                                    HomeActivity.toolbarMainContent = "Sports";
                                    HomeActivity.toolbarSubContent = "";
                                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                    if (HomeActivity.m_jsonSportsCarousels != null && HomeActivity.m_jsonSportsCarousels.length() > 0) {
                                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                            HorizontalListsFragment fragmentOnDemandAllContent = HorizontalListsFragment.newInstance(Constants.SPORTS, HomeActivity.m_jsonSportsCarousels.toString());
                                            fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, fragmentOnDemandAllContent);
                                            fragmentTransaction.commit();
                                        }

                                    } else {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingSportsList().execute();
                                        }
                                    }
                                }

                            }


                        }

                    } else if (HomeActivity.menu_position.equalsIgnoreCase(Constants.FILTER_MENU) && HomeActivity.content_position.equalsIgnoreCase(Constants.MAIN_CONTENT)) {

                        Log.d("====>", "Go to main menu");
                        HomeActivity.toolbarMainContent = Constants.ALL_SIDE_MENU;
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentOnDemandAll fragmentOnDemandAllContent = FragmentOnDemandAll.newInstance("home", "");
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAllContent);
                        fragmentTransaction.commit();
                        HomeActivity.menu_position = Constants.MAIN_MENU;
                        HomeActivity.content_position = Constants.MAIN_CONTENT;

                    } else if (HomeActivity.menu_position.equalsIgnoreCase(Constants.FILTER_MENU) && (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_CONTENT) || (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_RIGHT_CONTENT)))) {
                        Log.d("====>", "Go to main content");

                        if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.PRIMETIMEANYTIME)) {
                            HomeActivity.content_position = Constants.MAIN_CONTENT;
                            HomeActivity.toolbarSubContent = mOnDemandlist.get(mPrimeTimeSelectedItem).getName();
                            HomeActivity.toolbarMainContent = "Primetime";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            if (HomeActivity.m_jsonPrimeTimeList != null && HomeActivity.m_jsonPrimeTimeList.length() > 0) {
                                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.PRIMETIMEANYTIME, HomeActivity.m_jsonPrimeTimeList.toString());
                                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }

                            } else {
                                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                    new LoadingPrimetimeCarousels().execute();
                                }
                            }
                        } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.NETWORK_FILTER)) {
                            if (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_RIGHT_CONTENT)) {
                                Log.d("====>", "Go to main content SUB_RIGHT_CONTENT");
                                HomeActivity.toolbarMainContent = "TV Networks";
                                HomeActivity.toolbarSubContent = "";
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                if (HomeActivity.allCarousels != null) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        GridFragment listfragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.allCarousels.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                        HomeActivity.content_position = Constants.SUB_CONTENT;
                                    }

                                }
                            } else {
                                Log.d("====>", "Go to main content MAIN_CONTENT");
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                                setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                                if (HomeActivity.m_jsonNetworkListItems != null && HomeActivity.m_jsonNetworkListItems.length() > 0) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.BYNETWORK, HomeActivity.m_jsonNetworkListItems.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {
                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingAllNetworkList().execute();
                                    }
                                }
                            }

                        } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.TV_SHOWS_SIDE_MENU)) {

                            if (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_RIGHT_CONTENT)) {
                                Log.d("====>", "Go to main content SUB_RIGHT_CONTENT");
                                HomeActivity.toolbarMainContent = "TV Networks";
                                HomeActivity.toolbarSubContent = "";
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                if (HomeActivity.allCarousels != null) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        GridFragment listfragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.allCarousels.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                        HomeActivity.content_position = Constants.SUB_CONTENT;
                                    }

                                }
                            } else {
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                                setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

                                if (HomeActivity.m_jsonTvshowCarousels != null && HomeActivity.m_jsonTvshowCarousels.length() > 0) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowCarousels.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {

                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingTvShowCarousels().execute();
                                    }
                                }
                            }


                        } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.CATEGORY_FILTER)) {


                            HomeActivity.content_position = Constants.MAIN_CONTENT;
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

                            if (HomeActivity.m_jsonTvshowbyCategoryItems != null && HomeActivity.m_jsonTvshowbyCategoryItems.length() > 0) {
//                                    setCategoryList(HomeActivity.m_jsonTvshowbyCategoryList);
                                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.CATEGORY_SHOW, HomeActivity.m_jsonTvshowbyCategoryItems.toString());
                                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }


                            } else {
                                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                    new LoadingCategoryItems().execute(mCategoryid);
                                }


                            }


                        } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.MOVIES_SIDE_MENU)) {

                            HomeActivity.selectedmenu = Constants.MOVIES;
                            HomeActivity.menu_position = Constants.FILTER_MENU;
                            HomeActivity.content_position = Constants.MAIN_CONTENT;
                            HomeActivity.toolbarMainContent = Constants.ALL_SIDE_MENU;
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                            if (HomeActivity.isPayperview.equals("Pay Per View")) {
                                HomeActivity.selectedmenu = Constants.MOVIES;
                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.is_networkSelected = false;

                                if (HomeActivity.m_jsonPayPerViewmovielist != null && HomeActivity.m_jsonPayPerViewmovielist.length() > 0) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonPayPerViewmovielist.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {

                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingPayPerViewMovieList().execute();
                                    }

                                }
                            } else {
                                if (HomeActivity.m_jsonAllmovielist != null && HomeActivity.m_jsonAllmovielist.length() > 0) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonAllmovielist.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {
                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingAllMovieList().execute();
                                    }
                                }
                            }


                        } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.GENRE_FILTER)) {
                            HomeActivity.content_position = Constants.MAIN_CONTENT;
                            try {

                                mBachbuttonNavigation = "On-DemandSubcategoryofcategory";
                                HomeActivity.is_networkSelected = false;
                                if (isNetworkAvailable(getActivity()) || isWifiAvailable(getActivity())) {
                                    mAdapter_genreordecade = "LoadingTvshowListbyGenre";
                                    if (HomeActivity.m_jsonTvshowlistbyGenre != null && HomeActivity.m_jsonTvshowlistbyGenre.length() > 0) {
                                        tvShowByList(HomeActivity.m_jsonTvshowlistbyGenre);

                                    } else {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingTvshowListbyGenre().execute();
                                        }
                                    }

                                } else {
                                    Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    } else {


                        if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.MOVIES_SIDE_MENU) || (HomeActivity.selectedmenu.equals(Constants.MOVIES))) {
                            if (HomeActivity.selectedmenu.equals(Constants.MOVIES)) {
                                HomeActivity.selectedmenu = Constants.MOVIES;
                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.toolbarMainContent = "Movies";
                                HomeActivity.toolbarSubContent = "";
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                                listMovieSubcategories();
                                if (HomeActivity.isPayperview.equals("Pay Per View")) {
                                    HomeActivity.selectedmenu = Constants.MOVIES;
                                    HomeActivity.menu_position = Constants.FILTER_MENU;
                                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                                    HomeActivity.is_networkSelected = false;

                                    if (HomeActivity.m_jsonPayPerViewmovielist != null && HomeActivity.m_jsonPayPerViewmovielist.length() > 0) {
                                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonPayPerViewmovielist.toString());
                                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                            fragmentTransaction_list.commit();
                                        }

                                    } else {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingPayPerViewMovieList().execute();
                                        }
                                    }
                                } else {


                                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                                    setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                                    if (HomeActivity.m_jsonAllmovielist != null && HomeActivity.m_jsonAllmovielist.length() > 0) {
                                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonAllmovielist.toString());
                                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                            fragmentTransaction_list.commit();
                                        }

                                    } else {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingAllMovieList().execute();
                                        }
                                    }
                                }
                            }
                        } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.CATEGORY_FILTER)) {
                            HomeActivity.selectedFilter = "";
                            HomeActivity.content_position = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

                            if (HomeActivity.m_jsonTvshowbyCategoryItems != null && HomeActivity.m_jsonTvshowbyCategoryItems.length() > 0) {
//                                    setCategoryList(HomeActivity.m_jsonTvshowbyCategoryList);
                                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.CATEGORY_SHOW, HomeActivity.m_jsonTvshowbyCategoryItems.toString());
                                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }


                            } else {
                                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                    new LoadingCategoryItems().execute(mCategoryid);
                                }


                            }


                        } else {


                            if (HomeActivity.menu_position.equals(Constants.FILTER_MENU) || HomeActivity.menu_position.equals(Constants.CONTENT_MENU)) {
                                HomeActivity.toolbarMainContent = "TV Shows";
                                HomeActivity.toolbarSubContent = "";
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.selectedmenu = Constants.TV_SHOWS_SIDE_MENU;
                                HomeActivity.selectedFilter = "";
                                Log.d("====>", "Go to main filter menu");
                                listDataReset();
                                if (HomeActivity.m_jsonTvshowCarousels != null && HomeActivity.m_jsonTvshowCarousels.length() > 0) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowCarousels.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {

                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingTvShowCarousels().execute();
                                    }
                                }
                            } else {
                                if (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_RIGHT_CONTENT)) {
                                    Log.d("====>", "Go to main content SUB_RIGHT_CONTENT");
                                    HomeActivity.toolbarMainContent = "TV Networks";
                                    HomeActivity.toolbarSubContent = "";
                                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                    if (HomeActivity.allCarousels != null) {
                                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                            GridFragment listfragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.allCarousels.toString());
                                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                            fragmentTransaction_list.commit();
                                            HomeActivity.content_position = Constants.SUB_CONTENT;
                                        }

                                    }
                                } else {

                                    if (HomeActivity.m_jsonOndemandCarousels != null && HomeActivity.m_jsonOndemandCarousels.length() > 0) {
                                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                            FragmentOnDemandAllContent fragmentOnDemandAllContent = FragmentOnDemandAllContent.newInstance(Constants.SHOWS, HomeActivity.m_jsonOndemandCarousels.toString());
                                            fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, fragmentOnDemandAllContent);
                                            fragmentTransaction.commit();
                                        }

                                    } else {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingTvShowTVfeaturedCarousels().execute();
                                        }
                                    }
                                }
                            }


                        }


                    }


                    fragment_ondemand_switch_layout.setVisibility(View.VISIBLE);


                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void setHoverEffects() {
        Utilities.setViewFocus(getActivity(), fragment_ondemand_prev_icon);
        Utilities.setViewFocus(getActivity(), switch_image);
    }

    private void loaddemandsideMenuData() {

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            ((HomeActivity) getActivity()).setmFragmentOndemandallList(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class OnDemandListAdapter extends RecyclerView.Adapter<OnDemandListAdapter.DataObjectHolder> {
        ArrayList<SideMenu> list_data;
        Context context;
        private int mSelectedItem = 0;

        public OnDemandListAdapter(ArrayList<SideMenu> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
            text_font_typeface();
        }


        @Override
        public OnDemandListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final OnDemandListAdapter.DataObjectHolder holder, final int position) {
            try {


                final String menu_text = list_data.get(position).getName();
                final String menu_id = list_data.get(position).getId();
                final String menu_tag = list_data.get(position).getType();
                holder.fragment_ondemandlist_items.setText(menu_text);
                holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
                switch (position) {
                    case 0:
                        holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                        break;

                }


                if (position == mSelectedItem) {
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                } else {
                    holder.fragment_ondemandlist_items.setBackgroundResource(0);
                }


                holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedItem = position;
                        holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                        notifyDataSetChanged();
                        HomeActivity.toolbarMainContent = list_data.get(position).getName();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                        switch (menu_tag) {

                            case Constants.ALL_SIDE_MENU:
                                HomeActivity.menu_position = Constants.MAIN_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                FragmentOnDemandAll fragmentOnDemandAll;
                                fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home", "");
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                                fragmentTransaction.commit();
                                break;


                            case Constants.TV_SHOWS_SIDE_MENU:
                                HomeActivity.isSearchClick = false;
                                HomeActivity.is_networkSelected = false;
//                                ((HomeActivity) getActivity()).toolbarTextChange("FragmentTvShows");
                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.selectedmenu = Constants.TV_SHOWS_SIDE_MENU;
                                HomeActivity.selectedFilter = Constants.TV_SHOWS_SIDE_MENU;
                                HomeActivity.is_networkSelected = false;
                                mBachbuttonNavigation = "On-DemandSubcategory";
                                listDataReset();

                                if (HomeActivity.m_jsonTvshowCarousels != null && HomeActivity.m_jsonTvshowCarousels.length() > 0) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowCarousels.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {

                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingTvShowCarousels().execute();
                                    }
                                }
                                /*FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, "");
                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();*/
                                break;


                            case Constants.MOVIES_SIDE_MENU:
                                HomeActivity.isSearchClick = false;
//                                ((HomeActivity) getActivity()).toolbarTextChange("Movies");
                                /*FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                FragmentMovieMain  fragmentMovieMain = new FragmentMovieMain();
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentMovieMain);
                                fragmentTransaction.commit();*/
                                HomeActivity.selectedmenu = Constants.MOVIES;
                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.selectedFilter = Constants.MOVIES_SIDE_MENU;
                                HomeActivity.is_networkSelected = false;

                                listMovieSubcategories();


                                if (HomeActivity.m_jsonAllmovielist != null && HomeActivity.m_jsonAllmovielist.length() > 0) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonAllmovielist.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {
                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingAllMovieList().execute();
                                    }
                                }


                                break;
                            case Constants.TV_NETWORKS_SIDE_MENU:
                                mParam1 = "";
                                showNetworkList();
                                HomeActivity.isSearchClick = false;
//                                ((HomeActivity) getActivity()).toolbarTextChange(Constants.TV_NETWORKS_SIDE_MENU);
                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.selectedmenu = Constants.TV_SHOWS_SIDE_MENU;
                                mBachbuttonNavigation = "On-DemandSubcategory";
                                HomeActivity.is_networkSelected = true;
                                HomeActivity.selectedFilter = Constants.NETWORK_FILTER;


                                //new LoadingNetworkList().execute();


                                /*listDataReset();
                                if (HomeActivity.m_jsonNetworkListItems != null && HomeActivity.m_jsonNetworkListItems.length() > 0) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.BYNETWORK, HomeActivity.m_jsonNetworkListItems.toString());
                                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                } else {
                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingAllNetworkList().execute();
                                    }
                                }*/
                                /*FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, "");
                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();*/
                                break;


                            case Constants.PRIMETIMEANYTIME:

                                HomeActivity.isSearchClick = false;
                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.selectedmenu = Constants.PRIMETIMEANYTIME;
                                HomeActivity.selectedFilter = Constants.PRIMETIMEANYTIME;
                                mBachbuttonNavigation = "On-DemandSubcategory";
                                HomeActivity.is_networkSelected = false;
                                try {
                                    Calendar calendar = Calendar.getInstance();
                                    int day = calendar.get(Calendar.DAY_OF_WEEK);

                                    switch (day) {
                                        case Calendar.SUNDAY:
                                            mPrimeTimeSelectedItem = 5;
                                            setPrimeTimeList();
                                            dayofweek = "sat";
                                            new LoadingPrimetimeCarousels().execute();
                                            break;
                                        case Calendar.MONDAY:
                                            mPrimeTimeSelectedItem = 6;
                                            setPrimeTimeList();
                                            dayofweek = "sun";
                                            new LoadingPrimetimeCarousels().execute();
                                            break;
                                        case Calendar.TUESDAY:
                                            mPrimeTimeSelectedItem = 0;
                                            setPrimeTimeList();
                                            dayofweek = "mon";

                                            new LoadingPrimetimeCarousels().execute();
                                            break;
                                        case Calendar.WEDNESDAY:
                                            mPrimeTimeSelectedItem = 1;
                                            setPrimeTimeList();
                                            dayofweek = "tue";
                                            new LoadingPrimetimeCarousels().execute();
                                            break;
                                        case Calendar.THURSDAY:
                                            mPrimeTimeSelectedItem = 2;
                                            setPrimeTimeList();
                                            dayofweek = "wed";
                                            new LoadingPrimetimeCarousels().execute();
                                            break;
                                        case Calendar.FRIDAY:
                                            mPrimeTimeSelectedItem = 3;
                                            setPrimeTimeList();
                                            dayofweek = "thu";
                                            new LoadingPrimetimeCarousels().execute();
                                            break;
                                        case Calendar.SATURDAY:
                                            mPrimeTimeSelectedItem = 4;
                                            setPrimeTimeList();

                                            dayofweek = "fri";
                                            new LoadingPrimetimeCarousels().execute();
                                            break;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;

                            case Constants.SPORTS:
                                HomeActivity.isSearchClick = false;
//                                ((HomeActivity) getActivity()).toolbarTextChange(Constants.TV_NETWORKS_SIDE_MENU);
                                HomeActivity.menu_position = Constants.MAIN_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.selectedmenu = Constants.SPORTS;
                                mBachbuttonNavigation = "On-DemandSubcategory";
                                HomeActivity.is_networkSelected = false;
                                new LoadingSportsList().execute();
                                break;

                            case Constants.WEB_ORIGINALS_SIDE_MENU:
                                HomeActivity.isSearchClick = false;
//                                ((HomeActivity) getActivity()).toolbarTextChange(Constants.TV_NETWORKS_SIDE_MENU);
                                HomeActivity.menu_position = Constants.MAIN_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.selectedmenu = Constants.WEB_ORIGINALS_SIDE_MENU;
                                mBachbuttonNavigation = "On-DemandSubcategory";
                                HomeActivity.is_networkSelected = false;
                                new LoadingWeboriginalList().execute();
                                break;
                            case Constants.KIDS:
                                HomeActivity.isSearchClick = false;
//                                ((HomeActivity) getActivity()).toolbarTextChange(Constants.TV_NETWORKS_SIDE_MENU);
                                HomeActivity.menu_position = Constants.MAIN_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.selectedmenu = Constants.KIDS;
                                mBachbuttonNavigation = "On-DemandSubcategory";
                                HomeActivity.is_networkSelected = false;
                                new LoadingKidsList().execute();
                                break;


                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return list_data.size();
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

    private void setPrimeTimeList() {
        try {

            mOnDemandlist.clear();

            mOnDemandlist.add(new SideMenu("", "Monday", "", ""));
            mOnDemandlist.add(new SideMenu("", "Tuesday", "", ""));
            mOnDemandlist.add(new SideMenu("", "Wednesday", "", ""));
            mOnDemandlist.add(new SideMenu("", "Thursday", "", ""));
            mOnDemandlist.add(new SideMenu("", "Friday", "", ""));
            mOnDemandlist.add(new SideMenu("", "Saturday", "", ""));
            mOnDemandlist.add(new SideMenu("", "Sunday", "", ""));
            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            HomeActivity.toolbarSubContent = mOnDemandlist.get(mPrimeTimeSelectedItem).getName();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            fragment_ondemand_selected_item_text.setVisibility(View.VISIBLE);
            fragment_ondemand_selected_item_text.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            fragment_ondemand_selected_item_text.setText("Primetime Anytime");
            fragment_ondemand_selected_item_text.setVisibility(View.GONE);
            PrimeTimeAdapter primeTimeAdapter = new PrimeTimeAdapter(mOnDemandlist, getActivity());
            fragment_ondemand_alllist_items.setAdapter(primeTimeAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class PrimeTimeAdapter extends RecyclerView.Adapter<PrimeTimeAdapter.DataObjectHolder> {
        ArrayList<SideMenu> list_data;
        Context context;

        public PrimeTimeAdapter(ArrayList<SideMenu> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
            text_font_typeface();
        }


        @Override
        public PrimeTimeAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final PrimeTimeAdapter.DataObjectHolder holder, final int position) {
            final String menu_text = list_data.get(position).getName();
            final String menu_id = list_data.get(position).getId();
            final String menu_tag = list_data.get(position).getType();
            holder.fragment_ondemandlist_items.setText(menu_text);
            holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
            if (position == mPrimeTimeSelectedItem) {
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                holder.fragment_ondemandlist_items.setTextColor(getResources().getColor(R.color.white));
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
                holder.fragment_ondemandlist_items.setTextColor(getResources().getColor(R.color.white));
            }


            holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                    mPrimeTimeSelectedItem = position;
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                    holder.fragment_ondemandlist_items.setTextColor(getResources().getColor(R.color.white));
                    notifyDataSetChanged();
                    HomeActivity.toolbarSubContent = list_data.get(position).getName();

                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                    switch (menu_text) {

                        case "Sunday":

                            dayofweek = "sun";

                            new LoadingPrimetimeCarousels().execute();
                            break;
                        case "Monday":
                            dayofweek = "mon";

                            new LoadingPrimetimeCarousels().execute();
                            break;
                        case "Tuesday":
                            dayofweek = "tue";
                            HomeActivity.m_jsonPrimeTimeSlider = null;
                            new LoadingPrimetimeCarousels().execute();
                            break;
                        case "Wednesday":
                            dayofweek = "wed";

                            new LoadingPrimetimeCarousels().execute();
                            break;
                        case "Thursday":
                            dayofweek = "thu";

                            new LoadingPrimetimeCarousels().execute();
                            break;
                        case "Friday":
                            dayofweek = "fri";

                            new LoadingPrimetimeCarousels().execute();
                            break;
                        case "Saturday":
                            dayofweek = "sat";

                            new LoadingPrimetimeCarousels().execute();
                            break;
                    }


                }
            });


        }

        @Override
        public int getItemCount() {
            return list_data.size();
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


    public class LoadingSportsList extends AsyncTask<Object, Object, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }

        @Override
        protected Object doInBackground(Object... params) {

            HomeActivity.m_jsonSportsCarousels = JSONRPCAPI.getSportsCarousels(HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonSportsCarousels == null) return null;
            Log.d("Sports", "SportsTems" + HomeActivity.m_jsonSportsCarousels);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                //   if(!isdestroyed){
                if (HomeActivity.content_position.equals(Constants.SUB_RIGHT_CONTENT)) {
                    new LoadingTVNetworkList().execute(String.valueOf(HomeActivity.mNetworkid));
                } else {
                    if (HomeActivity.content_position.equals(Constants.SUB_CONTENT)) {
                        new LoadingAllTVfeaturedCarouselsById().execute(String.valueOf(HomeActivity.carousel_id));
                    } else {
                        if (HomeActivity.m_jsonSportsCarousels != null && HomeActivity.m_jsonSportsCarousels.length() > 0) {
                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.SPORTS, HomeActivity.m_jsonSportsCarousels.toString());
                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();
                            }

                        }
                    }
                }
                //   }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class LoadingWeboriginalList extends AsyncTask<Object, Object, Object> {
        ArrayList<SliderBean> slider_list = new ArrayList<>();

        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (HomeActivity.web_slider_list != null) {
                    HomeActivity.web_slider_list.clear();
                }

                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                    FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                    DialogFragment dialog_fragment = new DialogFragment();
                    network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                    network_fragmentTransaction.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Object doInBackground(Object... params) {

            HomeActivity.m_jsonWebCarousels = JSONRPCAPI.getWebCarousels(HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonWebCarousels == null) return null;
            JSONArray m_jsonWebSlider = JSONRPCAPI.getWebSliderList();
            if (m_jsonWebSlider == null) return null;
            HomeActivity.m_jsonWebSlider = m_jsonWebSlider;
            Log.d("Sports", "SportsTems" + HomeActivity.m_jsonWebCarousels);

            try {
                HomeActivity.web_slider_list.clear();
                for (int i = 0; i < m_jsonWebSlider.length(); i++) {
                    JSONObject slider_object = m_jsonWebSlider.getJSONObject(i);
                    String name = "", image = "", description = "", title = "",etype="";

                    if (slider_object.has("description")) {
                        description = slider_object.getString("description");
                    }
                    if (slider_object.has("title")) {
                        title = slider_object.getString("title");
                    }
                    if (slider_object.has("id")) {
                        id = slider_object.getInt("id");
                    }
                    if (slider_object.has("image")) {
                        image = slider_object.getString("image");
                    }
                    if (slider_object.has("name")) {
                        name = slider_object.getString("name");
                    }
                    if (slider_object.has("type")) {
                        etype= slider_object.getString("type");
                    }

                    slider_list.add(new SliderBean(id, description, title, image, name,etype));

                }
                HomeActivity.web_slider_list.addAll(slider_list);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                //  if(!isdestroyed){
                if (HomeActivity.m_jsonWebCarousels != null && HomeActivity.m_jsonWebCarousels.length() > 0) {
                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.WEB_ORIGINALS_SIDE_MENU, HomeActivity.m_jsonWebCarousels.toString());
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                }

                //    }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class LoadingKidsList extends AsyncTask<Object, Object, Object> {
        ArrayList<SliderBean> slider_list = new ArrayList<>();

        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (HomeActivity.kids_slider_list != null) {
                    HomeActivity.kids_slider_list.clear();
                }

                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                    FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                    DialogFragment dialog_fragment = new DialogFragment();
                    network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                    network_fragmentTransaction.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Object doInBackground(Object... params) {

            HomeActivity.m_jsonKidsCarousels = JSONRPCAPI.getKidsCarousels(HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonKidsCarousels == null) return null;
            JSONArray m_jsonkidSlider = JSONRPCAPI.getKidsSliderList();
            if (m_jsonkidSlider == null) return null;
            HomeActivity.m_jsonKidsSlider = m_jsonkidSlider;
            Log.d("Sports", "SportsTems" + HomeActivity.m_jsonKidsCarousels);

            try {
                for (int i = 0; i < m_jsonkidSlider.length(); i++) {
                    JSONObject slider_object = m_jsonkidSlider.getJSONObject(i);
                    String name = "", image = "", description = "", title = "",etype="";

                    if (slider_object.has("description")) {
                        description = slider_object.getString("description");
                    }
                    if (slider_object.has("title")) {
                        title = slider_object.getString("title");
                    }
                    if (slider_object.has("id")) {
                        id = slider_object.getInt("id");
                    }
                    if (slider_object.has("image")) {
                        image = slider_object.getString("image");
                    }
                    if (slider_object.has("name")) {
                        name = slider_object.getString("name");
                    }
                    if (slider_object.has("type")) {
                        etype = slider_object.getString("type");
                    }

                    slider_list.add(new SliderBean(id, description, title, image, name,etype));

                }
                HomeActivity.kids_slider_list.addAll(slider_list);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                //  if(!isdestroyed){
                if (HomeActivity.m_jsonKidsCarousels != null && HomeActivity.m_jsonKidsCarousels.length() > 0) {
                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.KIDS, HomeActivity.m_jsonKidsCarousels.toString());
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                }

                //   }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    ///////////////////// Adapter for tvshow catogories////////////////////////////////////////////////

    class TvShows extends RecyclerView.Adapter<TvShows.DataObjectHolder> {

        ArrayList<SideMenu> list_data;
        Context context;
        private int mSelectedItem = 0;

        public TvShows(ArrayList<SideMenu> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
            text_font_typeface();
        }

        public TvShows(ArrayList<SideMenu> list_data, Context context, int selected_pos) {
            this.list_data = list_data;
            this.context = context;
            this.mSelectedItem = selected_pos;
            text_font_typeface();
        }


        @Override
        public TvShows.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final TvShows.DataObjectHolder holder, final int position) {
            try {

                final String menu_text = list_data.get(position).getName();
                final String menu_id = list_data.get(position).getId();
                final String menu_tag = list_data.get(position).getType();
                holder.fragment_ondemandlist_items.setText(menu_text);
                holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);


                if (position == mSelectedItem) {
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                } else {
                    holder.fragment_ondemandlist_items.setBackgroundResource(0);
                }


                holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mSelectedItem = position;
                            holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                            notifyDataSetChanged();

                            HomeActivity.toolbarMainContent = list_data.get(position).getName();
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                            switch (menu_text) {
                                case "TV Shows":
                                    HomeActivity.isSearchClick = false;
                                    //                                ((HomeActivity) getActivity()).toolbarTextChange("FragmentTvShows");
                                    HomeActivity.is_networkSelected = false;
                                    HomeActivity.selectedFilter = Constants.TV_SHOWS_SIDE_MENU;
                                    mBachbuttonNavigation = "On-DemandSubcategory";
                                    /*try {
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTvshow = new FragmentTvShows();
                                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentTvshow);
                                        fragmentTransaction.commit();


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
    */

                                    listDataReset();

                                    if (HomeActivity.m_jsonTvshowCarousels != null && HomeActivity.m_jsonTvshowCarousels.length() > 0) {
                                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowCarousels.toString());
                                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                            fragmentTransaction_list.commit();
                                        }

                                    } else {

                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingTvShowCarousels().execute();
                                        }
                                    }
                                    /*FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.SHOWS, "");
                                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();*/
                                    break;
                                case "by Network":
                                    mParam1 = "";
                                    HomeActivity.isSearchClick = false;
                                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                                    HomeActivity.is_networkSelected = true;
                                    HomeActivity.menu_position = Constants.CONTENT_MENU;
                                    HomeActivity.selectedFilter = Constants.NETWORK_FILTER;
                                    showNetworkList();
                                    fragment_ondemand_switch_layout.setVisibility(View.INVISIBLE);

                                   /* if (HomeActivity.m_jsonNetworkListItems != null && HomeActivity.m_jsonNetworkListItems.length() > 0) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.BYNETWORK, HomeActivity.m_jsonNetworkListItems.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    } else {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingAllNetworkList().execute();
                                        }
                                    }*/


                                    break;
                                case "by Category":
                                    HomeActivity.isSearchClick = false;
                                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                                    HomeActivity.is_networkSelected = false;
                                    HomeActivity.menu_position = Constants.CONTENT_MENU;
                                    HomeActivity.selectedFilter = Constants.CATEGORY_FILTER;
                                    /*if (HomeActivity.m_jsonTvshowbyCategoryList != null && HomeActivity.m_jsonTvshowbyCategoryList.length() > 0) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.SHOWS, HomeActivity.m_jsonTvshowbyCategoryList.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    } else {*/
                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingCategoryList().execute();
                                    }

                                    //                                }


                                    break;
                                case "by Genre":
                                    mParam1 = "";
                                    HomeActivity.isSearchClick = false;
                                    HomeActivity.is_networkSelected = false;
                                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                                    HomeActivity.menu_position = Constants.CONTENT_MENU;
                                    mAdapter_genreordecade = "LoadingTvshowListbyGenre";

                                    if (HomeActivity.selectedmenu.equals(Constants.MOVIES)) {

                                        HomeActivity.selectedFilter = Constants.GENRE_FILTER;
                                        if (HomeActivity.m_jsonMovieGenre != null && HomeActivity.m_jsonMovieGenre.length() > 0) {
                                            MovieGenreList(HomeActivity.m_jsonMovieGenre);
                                            HomeActivity.selectedFilter = Constants.GENRE_FILTER;

                                        } else {
                                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                                new LoadingMovieGenre().execute();
                                            }

                                        }


                                    } else {


                                        try {
                                            mBachbuttonNavigation = "On-DemandSubcategoryofcategory";
                                            HomeActivity.selectedFilter = Constants.GENRE_FILTER;
                                            HomeActivity.is_networkSelected = false;
                                            if (isNetworkAvailable(getActivity()) || isWifiAvailable(getActivity())) {

                                                if (HomeActivity.m_jsonTvshowlistbyGenre != null && HomeActivity.m_jsonTvshowlistbyGenre.length() > 0) {
                                                    tvShowByList(HomeActivity.m_jsonTvshowlistbyGenre);

                                                } else {
                                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                                        new LoadingTvshowListbyGenre().execute();
                                                    }


                                                }

                                            } else {
                                                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                                            }

                                            //                                    listGenreDataReset();
                                    /*FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                                    GridList grid_fragment = new GridList();
                                    fragmentTransaction_grid.replace(R.id.fragment_ondemand_pagerandlist, grid_fragment );
                                    fragmentTransaction_grid.commit();*/

                                       /* FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                                        GridFragment grid_fragment = GridFragment.newInstance(Constants.SHOWS,"");
                                        fragmentTransaction_grid.replace(R.id.fragment_ondemand_pagerandlist, grid_fragment);
                                        fragmentTransaction_grid.commit();*/
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    break;
                                case "by Decade":
                                    try {
                                        HomeActivity.isSearchClick = false;
                                        HomeActivity.content_position = Constants.MAIN_CONTENT;
                                        HomeActivity.menu_position = Constants.CONTENT_MENU;
                                        HomeActivity.selectedFilter = Constants.DECADE_FILTER;

                                        mBachbuttonNavigation = "On-DemandSubcategoryofcategory";
                                        if (isNetworkAvailable(getActivity()) || isWifiAvailable(getActivity())) {
                                            mAdapter_genreordecade = "LoadingTvShowListbyDecade";
                                            if (HomeActivity.m_jsonTvshowlistbyDecade != null && HomeActivity.m_jsonTvshowlistbyDecade.length() > 0) {
                                                mAdapter_genreordecade = "LoadingTvShowListbyDecade";
                                                tvShowByList(HomeActivity.m_jsonTvshowlistbyDecade);


                                            } else {
                                                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                                    new LoadingTvShowListbyDecade().execute();
                                                }

                                            }

                                        } else {
                                            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                                        }


                                        //                                    listDecadeDataReset();
                                        HomeActivity.is_networkSelected = false;
                                       /* FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        FragmentOnDemandAllContent fragmentOnDemandAllContent = new FragmentOnDemandAllContent();
                                        fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, fragmentOnDemandAllContent);
                                        fragmentTransaction.commit();*/
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case "by Rating":
                                    HomeActivity.isSearchClick = false;
                                    mAdapter_genreordecade = "LoadingMovieListByrating";
                                    HomeActivity.is_networkSelected = false;
                                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                                    HomeActivity.menu_position = Constants.CONTENT_MENU;
                                    HomeActivity.selectedFilter = Constants.MOVIES_SIDE_MENU;
                                    HomeActivity.selectedFilter = Constants.RATING_FILTER;
                                    new LoadingMoviebyRating().execute();
                                    break;

                                case "Movies":
                                    HomeActivity.isSearchClick = false;

                                    if (HomeActivity.isPayperview.equals("Pay Per View")) {
                                        HomeActivity.selectedmenu = Constants.MOVIES;
                                        HomeActivity.menu_position = Constants.FILTER_MENU;
                                        HomeActivity.content_position = Constants.MAIN_CONTENT;
                                        HomeActivity.is_networkSelected = false;

                                        if (HomeActivity.m_jsonPayPerViewmovielist != null && HomeActivity.m_jsonPayPerViewmovielist.length() > 0) {
                                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonPayPerViewmovielist.toString());
                                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                                fragmentTransaction_list.commit();
                                            }

                                        } else {
                                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                                new LoadingPayPerViewMovieList().execute();
                                            }
                                        }
                                    } else {
                                        if (HomeActivity.m_jsonAllmovielist != null && HomeActivity.m_jsonAllmovielist.length() > 0) {
                                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonAllmovielist.toString());
                                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                                fragmentTransaction_list.commit();
                                            }

                                        } else {
                                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                                new LoadingAllMovieList().execute();
                                            }
                                        }
                                    }


                                    break;


                            }
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
            return list_data.size();
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


    public class TvShowsCategories extends RecyclerView.Adapter<TvShowsCategories.DataObjectHolder> {

        ArrayList<SideMenu> list_data;
        Context context;
        private int mSelectedItem = 0;

        public TvShowsCategories(ArrayList<SideMenu> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
            text_font_typeface();
        }


        @Override
        public TvShowsCategories.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final TvShowsCategories.DataObjectHolder holder, final int position) {
            final String menu_text = list_data.get(position).getName();
            final String menu_id = list_data.get(position).getId();
            final String menu_tag = list_data.get(position).getType();
            holder.fragment_ondemandlist_items.setText(menu_text);
            holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);


            if (position == mSelectedItem) {
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }


            holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                    mSelectedItem = position;
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                    notifyDataSetChanged();

                    HomeActivity.toolbarMainContent = list_data.get(position).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                    mCategoryid = menu_id;
                    new LoadingCategoryItems().execute(menu_id);

                }
            });
        }

        @Override
        public int getItemCount() {
            return list_data.size();
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


    public class MovieByRatingAdapter extends RecyclerView.Adapter<MovieByRatingAdapter.DataObjectHolder> {
        ArrayList<SideMenu> list_data;
        Context context;
        private int mSelectedItem;


        public MovieByRatingAdapter(ArrayList<SideMenu> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
            text_font_typeface();
        }


        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final DataObjectHolder holder, final int position) {
            final String menu_text = list_data.get(position).getName();
            final String menu_id = list_data.get(position).getId();
            final String menu_tag = list_data.get(position).getType();

            holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);


            if (position == mSelectedItem) {
                if (!mParam1.equalsIgnoreCase("moviegenre")) {
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                }

            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }

            if (!mAdapter_genreordecade.equals("LoadingMovieListByrating")) {


                holder.fragment_ondemandlist_items.setText(Utilities.stripHtml(menu_tag));


                holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedItemgenre = position;
                        mParam1 = "";
                        HomeActivity.isSearchClick = false;
                        HomeActivity.toolbarMainContent = list_data.get(position).getType();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

                        mSelectedItem = position;
                        holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                        notifyDataSetChanged();
                        mGenreIdfromList = list_data.get(position).getId();
                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                            new LoadingMovieListByGenre().execute(mGenreIdfromList);
                        }


                    }

                });

            } else {
                try {


                    holder.fragment_ondemandlist_items.setText(Utilities.stripHtml(menu_text));


                    holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HomeActivity.isSearchClick = false;
                            HomeActivity.toolbarMainContent = list_data.get(position).getName();
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

                            mSelectedItem = position;
                            holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                            notifyDataSetChanged();
                            mGenreIdfromList = list_data.get(position).getType();
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingMovieListByRating().execute(mGenreIdfromList);
                            }


                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getItemCount() {
            return list_data.size();
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


    ////////////Adapter for byGenre and byDecade////////////////////////////////////////////////////


    class TvShowsSubCategory extends RecyclerView.Adapter<TvShowsSubCategory.DataObjectHolder> {


        ArrayList<SideMenu> list_data;
        Context context;


        public TvShowsSubCategory(ArrayList<SideMenu> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
            text_font_typeface();
        }

        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final DataObjectHolder holder, final int position) {
            try {

                final String menu_text = list_data.get(position).getName();
                final String menu_id = list_data.get(position).getId();
                holder.fragment_ondemandlist_items.setText(menu_text);
                holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);


                if (position == mSelectedItemgenre) {
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                } else {
                    holder.fragment_ondemandlist_items.setBackgroundResource(0);
                }


                holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeActivity.isSearchClick = false;
                        mSelectedItemgenre = position;

                        holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                        notifyDataSetChanged();
                        Log.d("ListId", "|||||" + menu_id);
                        ////////////////////Genre/////////////////////////
                        HomeActivity.toolbarMainContent = list_data.get(position).getName();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        if (mAdapter_genreordecade.equals("LoadingTvShowListbyDecade")) {
                            mGenreIdfromList = list_data.get(position).getId();
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingDecadeListById().execute(mGenreIdfromList);
                            }

                        } else {
                            mGenreIdfromList = list_data.get(position).getId();
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingGenereListById().execute(mGenreIdfromList);
                            }


                        }


                        ///////////////////////Decade////////////////////////


                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return list_data.size();
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


    private void text_font_typeface() {
        OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
        osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
    }


    private class LoadingSideMenuTask extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            fragment_ondemand_progress.setVisibility(View.VISIBLE);
            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected Object doInBackground(Object... params) {
            try {
                HomeActivity.m_jsonDemandListItems = JSONRPCAPI.getDemandMenuList();
                if (HomeActivity.m_jsonDemandListItems == null) return null;
                Log.d("m_jsonDemandListItems::", "::" + HomeActivity.m_jsonDemandListItems);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                // if(!isdestroyed){
                fragment_ondemand_progress.setVisibility(View.GONE);
                if (HomeActivity.m_jsonDemandListItems != null && HomeActivity.m_jsonDemandListItems.length() > 0) {
                    setListData(HomeActivity.m_jsonDemandListItems);


                } else {
                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                        new LoadingSideMenuTask().execute();
                    }

                }
                //    }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isdestroyed = true;
        HomeActivity.currentPageForBackButton = "";
    }

    public void setListData(JSONArray m_jsonDemandListItemsdata) {
        try {

            mOnDemandlist.clear();
            String id = "", name = "", type = "", order = "";
            for (int i = 0; i < m_jsonDemandListItemsdata.length(); i++) {
                JSONObject demandMenuItem = m_jsonDemandListItemsdata.getJSONObject(i);
                if (demandMenuItem.has("name")) {
                    name = demandMenuItem.getString("name");
                    if (name.equalsIgnoreCase("Primetime Anytime")) {
                        name = "Primetime";
                    }
                }
                if (demandMenuItem.has("order")) {
                    order = demandMenuItem.getString("order");
                }
                if (demandMenuItem.has("id")) {
                    id = demandMenuItem.getString("id");
                }
                if (demandMenuItem.has("page")) {
                    type = demandMenuItem.getString("page");
                }
                if (!name.equalsIgnoreCase("world")) {
                    mOnDemandlist.add(new SideMenu(id, name, type, order));
                }

            }

            HomeActivity.toolbarMainContent = mOnDemandlist.get(0).getName();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

            mLayoutManager = new LinearLayoutManager(getActivity());
            HomeActivity.is_networkSelected = false;
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            onDemandListAdapter = new OnDemandListAdapter(mOnDemandlist, getActivity());
            fragment_ondemand_alllist_items.setAdapter(onDemandListAdapter);
            if (HomeActivity.isPayperview.equals("On-Demand")) {
                if (HomeActivity.m_jsonOndemandCarousels != null && HomeActivity.m_jsonOndemandCarousels.length() > 0) {
                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentOnDemandAllContent fragmentOnDemandAllContent = FragmentOnDemandAllContent.newInstance(Constants.SHOWS, HomeActivity.m_jsonOndemandCarousels.toString());
                        fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, fragmentOnDemandAllContent);
                        fragmentTransaction.commit();
                    }

                } else {
                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                        new LoadingTvShowTVfeaturedCarousels().execute();
                    }
                }
            } else if (HomeActivity.isPayperview.equals("Pay Per View")) {
                HomeActivity.mfreeorall = 2;
/*
                fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentPayPerView fragmentPayPerView = new FragmentPayPerView();
                fragmentTransaction.replace(R.id.fragment_ondemand_alllist, fragmentPayPerView);
                fragmentTransaction.commit();*/
                if (HomeActivity.m_jsonTvshowfeaturedCarousels != null && HomeActivity.m_jsonTvshowfeaturedCarousels.length() > 0) {
                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowfeaturedCarousels.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                } else {
                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                        new LoadingTvShowTVfeaturedCarousels().execute();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void listDataReset() {
        try {
            fragment_ondemand_selected_item_text.setVisibility(View.GONE);
            network_spinner_layout.setVisibility(View.GONE);
            mOnDemandlist.clear();
            mOnDemandlist.add(new SideMenu("", "TV Shows", "", ""));
            mOnDemandlist.add(new SideMenu("", "by Network", "", ""));
            mOnDemandlist.add(new SideMenu("", "by Category", "", ""));
            mOnDemandlist.add(new SideMenu("", "by Genre", "", ""));
            mOnDemandlist.add(new SideMenu("", "by Decade", "", ""));
            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            HomeActivity.toolbarMainContent = mOnDemandlist.get(0).getName();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

            TvShows tvShows = new TvShows(mOnDemandlist, getActivity());
            fragment_ondemand_alllist_items.setAdapter(tvShows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void listMovieSubcategories() {
        try {

            mOnDemandlist.clear();
            mOnDemandlist.add(new SideMenu("", "Movies", "", ""));
            mOnDemandlist.add(new SideMenu("", "by Genre", "", ""));
            mOnDemandlist.add(new SideMenu("", "by Rating", "", ""));
            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            HomeActivity.toolbarMainContent = mOnDemandlist.get(0).getName();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
            TvShows tvShows = new TvShows(mOnDemandlist, getActivity());
            fragment_ondemand_alllist_items.setAdapter(tvShows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listMovieSubcategoriesPayperview(int pos) {
        try {

            mOnDemandlist.clear();
            mOnDemandlist.add(new SideMenu("", "Movies", "", ""));
            mOnDemandlist.add(new SideMenu("", "by Genre", "", ""));
            mOnDemandlist.add(new SideMenu("", "by Rating", "", ""));
            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            HomeActivity.toolbarMainContent = mOnDemandlist.get(0).getName();
            HomeActivity.selectedFilter = Constants.GENRE_FILTER;
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
            TvShows tvShows = new TvShows(mOnDemandlist, getActivity(), pos);
            fragment_ondemand_alllist_items.setAdapter(tvShows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public void listGenreDataReset() {
        try {
            ((HomeActivity) getActivity()).toolbarTextChange("FragmentTvShows");
           *//* mOnDemandlist.clear();
            mOnDemandlist.add("Action");
            mOnDemandlist.add("Adventure");
            mOnDemandlist.add("Comedy");
            mOnDemandlist.add("Crime");
            mOnDemandlist.add("Drama");
            mOnDemandlist.add("Documentary");
            mOnDemandlist.add("Fantasy");
            JSONArray jsArray = new JSONArray(mOnDemandlist);
            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            onDemandListAdapter = new OnDemandListAdapter(jsArray, getActivity());
            fragment_ondemand_alllist_items.setAdapter(onDemandListAdapter);*//*
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listDecadeDataReset() {
        try {
            ((HomeActivity) getActivity()).toolbarTextChange("FragmentTvShows");
           *//* mOnDemandlist.clear();
            mOnDemandlist.add("Todays's Shows");
            mOnDemandlist.add("2000s TV Shows");
            mOnDemandlist.add("90's TV Shows");
            mOnDemandlist.add("80's TV Shows");
            mOnDemandlist.add("70's TV Shows");
            mOnDemandlist.add("60's TV Shows");
            JSONArray jsArray = new JSONArray(mOnDemandlist);
            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            onDemandListAdapter = new OnDemandListAdapter(jsArray, getActivity());
            fragment_ondemand_alllist_items.setAdapter(onDemandListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


    public void movieRataingList(JSONArray jsonArray) {
        try {


            fragment_ondemand_progress.setVisibility(View.GONE);
            mOnDemandlist.clear();
            String id = "", name = "", type = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject demandMenuItem = jsonArray.getJSONObject(i);
                if (demandMenuItem.has("slug")) {
                    type = demandMenuItem.getString("slug");
                }
                if (demandMenuItem.has("name")) {
                    name = demandMenuItem.getString("name");
                }


                mOnDemandlist.add(new SideMenu(id, name, type, ""));
            }

            HomeActivity.toolbarMainContent = mOnDemandlist.get(0).getType();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

            MovieByRatingAdapter movieByRatingAdapter = new MovieByRatingAdapter(mOnDemandlist, getActivity());
            fragment_ondemand_alllist_items.setAdapter(movieByRatingAdapter);


            mGenreIdfromList = mOnDemandlist.get(0).getType();
            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                new LoadingMovieListByRating().execute(mGenreIdfromList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void MovieGenreList(JSONArray m_jsonDemandListItemsdata) {
        try {
            String module;

            fragment_ondemand_progress.setVisibility(View.GONE);
            mOnDemandlist.clear();
            String id = "", name = "", type = "";
            for (int i = 0; i < m_jsonDemandListItemsdata.length(); i++) {
                JSONObject demandMenuItem = m_jsonDemandListItemsdata.getJSONObject(i);
                if (demandMenuItem.has("module")) {
                    module = demandMenuItem.getString("module");

                    if (module.equals("movies")) {
                        JSONArray itemsarray = demandMenuItem.getJSONArray("child");

                        for (int j = 0; j < itemsarray.length(); j++) {
                            JSONObject itemsobject = itemsarray.getJSONObject(j);
                            String itemsmodule = null, itemslabel = null;
                            int itemsid = 0;
                            if (itemsobject.has("id")) {
                                itemsid = itemsobject.getInt("id");
                            }
                            if (itemsobject.has("module")) {
                                itemsmodule = itemsobject.getString("module");
                            }
                            if (itemsobject.has("label")) {
                                itemslabel = itemsobject.getString("label");

                            }


                            mOnDemandlist.add(new SideMenu(String.valueOf(itemsid), itemsmodule, itemslabel, ""));
                        }
                    }


                }


            }


            HomeActivity.toolbarMainContent = mOnDemandlist.get(0).getType();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
            MovieByRatingAdapter movieByRatingAdapter = new MovieByRatingAdapter(mOnDemandlist, getActivity());


            fragment_ondemand_alllist_items.setAdapter(movieByRatingAdapter);
            mGenreIdfromList = mOnDemandlist.get(0).getId();
            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                if (mParam1.equalsIgnoreCase("moviegenre")) {
                    new LoadingMovieListByGenre().execute(mParam2);
                } else {
                    new LoadingMovieListByGenre().execute(mGenreIdfromList);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tvShowByList(JSONArray m_jsonDemandListItemsdata) {
        try {
            fragment_ondemand_progress.setVisibility(View.GONE);
            mOnDemandlist.clear();
            String id = "", name = "", type = "";
            for (int i = 0; i < m_jsonDemandListItemsdata.length(); i++) {
                JSONObject demandMenuItem = m_jsonDemandListItemsdata.getJSONObject(i);
                if (demandMenuItem.has("name")) {
                    name = demandMenuItem.getString("name");
                }
                if (demandMenuItem.has("id")) {
                    id = demandMenuItem.getString("id");
                }
                if (demandMenuItem.has("type")) {
                    type = demandMenuItem.getString("type");
                }
                mOnDemandlist.add(new SideMenu(id, name, type, ""));
            }

            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            mSelectedItemgenre = 0;
            tvShowsByAdapter = new TvShowsSubCategory(mOnDemandlist, getActivity());
            fragment_ondemand_alllist_items.setAdapter(tvShowsByAdapter);
            HomeActivity.toolbarMainContent = mOnDemandlist.get(0).getName();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
            if (mAdapter_genreordecade.equals("LoadingTvShowListbyDecade")) {
                mGenreIdfromList = mOnDemandlist.get(0).getId();
                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                    new LoadingDecadeListById().execute(mGenreIdfromList);
                }

            } else {
                mGenreIdfromList = mOnDemandlist.get(0).getId();
                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                    new LoadingGenereListById().execute(mGenreIdfromList);
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class LoadingMovieGenre extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            HomeActivity.m_jsonMovieGenre = JSONRPCAPI.getAllMenus();
            if (HomeActivity.m_jsonMovieGenre == null) return null;
            Log.d("m_jsonmovieGenre::", "::" + HomeActivity.m_jsonMovieGenre);


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
                mOnDemandlist.clear();
                fragment_ondemand_progress.setVisibility(View.VISIBLE);
            }

        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                //   if(!isdestroyed){
                if (HomeActivity.m_jsonMovieGenre.length() > 0) {
                    MovieGenreList(HomeActivity.m_jsonMovieGenre);

                }
                //    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class LoadingTvshowListbyGenre extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            HomeActivity.m_jsonTvshowlistbyGenre = JSONRPCAPI.getTvShowListbyGenre();
            if (HomeActivity.m_jsonTvshowlistbyGenre == null) return null;
            Log.d("m_jsonDemandbyGenre::", "::" + HomeActivity.m_jsonTvshowlistbyGenre);


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
                mOnDemandlist.clear();
                fragment_ondemand_progress.setVisibility(View.VISIBLE);
            }

        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                //   if(!isdestroyed){
                if (HomeActivity.m_jsonTvshowlistbyGenre.length() > 0) {
                    tvShowByList(HomeActivity.m_jsonTvshowlistbyGenre);

                }
                //  }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadingTvShowListbyDecade extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mOnDemandlist.clear();
            fragment_ondemand_progress.setVisibility(View.VISIBLE);
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            HomeActivity.m_jsonTvshowlistbyDecade = JSONRPCAPI.getTvShowListbyDecade();
            if (HomeActivity.m_jsonTvshowlistbyDecade == null) return null;
            Log.d("m_jsonTvshowbyDecade::", "::" + HomeActivity.m_jsonTvshowlistbyDecade);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                //  if(!isdestroyed){
                if (HomeActivity.m_jsonTvshowlistbyDecade.length() > 0) {
                    mAdapter_genreordecade = "LoadingTvShowListbyDecade";
                    tvShowByList(HomeActivity.m_jsonTvshowlistbyDecade);


                }
                //  }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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


    public class LoadingNetworkList extends AsyncTask<Object, Object, Object> {
        int id = 0;
        String name = "", image = "", letter = spinner_network.getSelectedItem().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment_ondemand_alllist_items.setVisibility(View.GONE);
            fragment_ondemand_progress.setVisibility(View.VISIBLE);
            FragmentOnDemandAll.hideContent(true);
            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
           /* FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
            DialogFragment dialog_fragment = new DialogFragment();
            network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
            network_fragmentTransaction.commit();*/
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                HomeActivity.m_jsonNetworkList = JSONRPCAPI.getAllNetworks();
                if (HomeActivity.m_jsonNetworkList == null) return null;
                gridBeans.clear();
                Log.d("NetworkList::", "Network list::" + HomeActivity.m_jsonNetworkList);
                for (int i = 0; i < HomeActivity.m_jsonNetworkList.length(); i++) {
                    try {
                        JSONObject jSubscriptionCategory = HomeActivity.m_jsonNetworkList.getJSONObject(i);
                        if (jSubscriptionCategory.has("name")) {
                            name = jSubscriptionCategory.getString("name");
                        }
                        if (jSubscriptionCategory.has("id")) {
                            id = jSubscriptionCategory.getInt("id");
                        }


                        if (jSubscriptionCategory.has("thumbnail")) {
                            image = jSubscriptionCategory.getString("thumbnail");
                        }

                        if (name.startsWith(letter)) {
                            gridBeans.add(new GridBean(String.valueOf(id), name, image, "", ""));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            try {
                // if(!isdestroyed){
                fragment_ondemand_progress.setVisibility(View.GONE);
                fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
                network_spinner_layout.setVisibility(View.VISIBLE);


                networkListAdapter = new NetworkListAdapter(gridBeans, getActivity());
                fragment_ondemand_alllist_items.setAdapter(networkListAdapter);
                fragment_ondemand_selected_item_text.setVisibility(View.VISIBLE);
                network_spinner_layout.setVisibility(View.VISIBLE);
                fragment_ondemand_selected_item_text.setText("Networks");
                HomeActivity.networkImage = gridBeans.get(0).getImage();
                HomeActivity.toolbarSubContent = gridBeans.get(0).getName();
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                new LoadingTVNetworkList().execute(gridBeans.get(0).getId());
                //   }


            } catch (Exception e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
            }


        }
    }


    private class LoadingAllNetworkList extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {

            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }


        @SuppressLint("NewApi")
        @Override
        protected Object doInBackground(Object... params) {
            HomeActivity.m_jsonNetworkListItems = JSONRPCAPI.getAllTVNetworks();
            if (HomeActivity.m_jsonNetworkListItems == null) return null;
            Log.d("NetworkList::", "Network list::" + HomeActivity.m_jsonNetworkListItems);


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                //if(!isdestroyed){
                if (HomeActivity.selectedmenu.equals(Constants.SUB_CONTENT)) {
                    new LoadingAllTVfeaturedCarouselsById().execute(String.valueOf(HomeActivity.carousel_id));
                } else {
                    if (HomeActivity.m_jsonNetworkListItems.length() > 0) {

                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.BYNETWORK, HomeActivity.m_jsonNetworkListItems.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }




                   /* FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                    GridFragment network_listfragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonNetworkListItems.toString());
                    network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, network_listfragment);
                    network_fragmentTransaction.commit();*/

                    }
                }
                //  }


            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


    public class LoadingPayPerViewMovieList extends AsyncTask<Object, Object, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                    FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                    DialogFragment dialog_fragment = new DialogFragment();
                    network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                    network_fragmentTransaction.commit();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                HomeActivity.m_jsonPayPerViewmovielist = JSONRPCAPI.getMovieListByPayperView();
                if (HomeActivity.m_jsonPayPerViewmovielist == null) return null;
                Log.d("MovieList::", "payperview::" + HomeActivity.m_jsonPayPerViewmovielist);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                //if(!isdestroyed){
                if (HomeActivity.m_jsonPayPerViewmovielist.length() > 0 || HomeActivity.m_jsonPayPerViewmovielist != null) {

                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonPayPerViewmovielist.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                }
                //   }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class LoadingPayperViewShowList extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {

            HomeActivity.m_jsonPayPerViewShowlist = JSONRPCAPI.getShowListByPayperView();
            if (HomeActivity.m_jsonPayPerViewShowlist == null) return null;
            Log.d("MovieList::", "payperview::" + HomeActivity.m_jsonPayPerViewShowlist);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                // if(!isdestroyed){
                if (HomeActivity.m_jsonPayPerViewShowlist != null && HomeActivity.m_jsonPayPerViewShowlist.length() > 0) {

                    if (getActivity() != null) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        if (manager != null) {
                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                FragmentTransaction fragmentTransaction_list = manager.beginTransaction();
                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonPayPerViewShowlist.toString());
                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();
                            }

                        }

                    }

                } else {
                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                        //new LoadingPayperViewShowList().execute();
                    }

                }
                //  }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadingAllMovieList extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {

            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
            try {
                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                    FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                    DialogFragment dialog_fragment = new DialogFragment();
                    network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                    network_fragmentTransaction.commit();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @SuppressLint("NewApi")
        @Override
        protected Object doInBackground(Object... params) {
            HomeActivity.m_jsonAllmovielist = JSONRPCAPI.getAllMovies(HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonAllmovielist == null) return null;
            Log.d("MovieList::", "Movie list::" + HomeActivity.m_jsonAllmovielist);


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                //  if(!isdestroyed){
                if (HomeActivity.content_position.equals(Constants.SUB_CONTENT)) {

                    new LoadingAllTVfeaturedCarouselsById().execute(String.valueOf(HomeActivity.carousel_id));

                } else {

                    if (HomeActivity.m_jsonAllmovielist.length() > 0 || HomeActivity.m_jsonAllmovielist != null) {

                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonAllmovielist.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    }
                }
                //  }


            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


    private class LoadingMovieListByGenre extends AsyncTask<String, Object, Object> {
        @Override
        protected void onPreExecute() {
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }


            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            HomeActivity.mMovieGenreId = Integer.parseInt(params[0]);
            HomeActivity.m_jsonmovieslistbygenre = JSONRPCAPI.getMovieListbyGenre(Integer.parseInt(params[0]), 0, HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonmovieslistbygenre == null) return null;

            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonmovieslistbygenre);


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                //  if(!isdestroyed){
                if (HomeActivity.m_jsonmovieslistbygenre.length() > 0) {

                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        GridFragment grid_fragment = GridFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonmovieslistbygenre.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_ondemand_pagerandlist, grid_fragment);
                        fragmentTransaction_grid.commit();
                    }


                }
                //   }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


    private class LoadingMovieListByRating extends AsyncTask<String, Object, Object> {
        @Override
        protected void onPreExecute() {
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }


            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            HomeActivity.mMovieratingid = params[0];
            HomeActivity.m_jsonmoviesbyrating = JSONRPCAPI.getMovieListByrating(params[0], 0, HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonmoviesbyrating == null) return null;
            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonmoviesbyrating);


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                //if(!isdestroyed){
                if (HomeActivity.m_jsonmoviesbyrating.length() > 0) {

                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        GridFragment grid_fragment = GridFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonmoviesbyrating.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_ondemand_pagerandlist, grid_fragment);
                        fragmentTransaction_grid.commit();
                    }


                }
                // }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


    private class LoadingGenereListById extends AsyncTask<String, Object, Object> {
        @Override
        protected void onPreExecute() {
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }


            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            HomeActivity.m_jsonGenreListbyId = JSONRPCAPI.getTVGenreDatabyId(Integer.parseInt(params[0]), 100, 0, HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonGenreListbyId == null) return null;
            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonGenreListbyId);


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                // if(!isdestroyed){
                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                    FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                    GridFragment grid_fragment = GridFragment.newInstance(Constants.SHOWS, HomeActivity.m_jsonGenreListbyId.toString());
                    fragmentTransaction_grid.replace(R.id.fragment_ondemand_pagerandlist, grid_fragment);
                    fragmentTransaction_grid.commit();
                }

                //  }


            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


    private class LoadingDecadeListById extends AsyncTask<String, Object, Object> {
        @Override
        protected void onPreExecute() {
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }


            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            HomeActivity.m_jsonDeacdeListbyId = JSONRPCAPI.getTVDecadeDatabyId(Integer.parseInt(params[0]), 1000, 0, HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonDeacdeListbyId == null) return null;
            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonDeacdeListbyId);


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                //  if(!isdestroyed){
                if (HomeActivity.m_jsonDeacdeListbyId.length() > 0) {

                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        GridFragment grid_fragment = GridFragment.newInstance(Constants.SHOWS, HomeActivity.m_jsonDeacdeListbyId.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_ondemand_pagerandlist, grid_fragment);
                        fragmentTransaction_grid.commit();

                    }


                }
                //  }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }

    public void setCategoryList(JSONArray jsonArray) {
        try {
            mOnDemandlist.clear();
            String id = "", name = "", type = "", order = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject demandMenuItem = jsonArray.getJSONObject(i);
                if (demandMenuItem.has("name")) {
                    name = demandMenuItem.getString("name");
                }
                if (demandMenuItem.has("id")) {
                    id = demandMenuItem.getString("id");
                }

                mOnDemandlist.add(new SideMenu(id, name, type, order));
            }
            fragment_ondemand_progress.setVisibility(View.GONE);
            fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            HomeActivity.toolbarMainContent = mOnDemandlist.get(0).getName();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
            fragment_ondemand_selected_item_text.setVisibility(View.VISIBLE);
            fragment_ondemand_selected_item_text.setText("Category");
            TvShowsCategories tvShowsCategories = new TvShowsCategories(mOnDemandlist, getActivity());
            fragment_ondemand_alllist_items.setAdapter(tvShowsCategories);
            mCategoryid = mOnDemandlist.get(0).getId();
            new LoadingCategoryItems().execute(mCategoryid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class LoadingCategoryList extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment_ondemand_progress.setVisibility(View.VISIBLE);
            fragment_ondemand_alllist_items.setVisibility(View.INVISIBLE);
            FragmentOnDemandAll.hideContent(true);
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                HomeActivity.m_jsonTvshowbyCategoryList = JSONRPCAPI.getTvShowListbyCategory();
                if (HomeActivity.m_jsonTvshowbyCategoryList == null) return null;
                Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonTvshowbyCategoryList);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                //  if(!isdestroyed){
                setCategoryList(HomeActivity.m_jsonTvshowbyCategoryList);

                /*if (HomeActivity.m_jsonTvshowbyCategoryList.length() > 0) {
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.SHOWS, HomeActivity.m_jsonTvshowbyCategoryList.toString());
                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                    fragmentTransaction_list.commit();
                }*/
                //   }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class LoadingTvShowTVfeaturedCarousels extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }


        @Override
        protected Object doInBackground(Object... params) {

           /* if (isswitchselected) {

                HomeActivity.mfreeorall = 0;
            } else {
                HomeActivity.mfreeorall = 1;

            }*/


            if (HomeActivity.isPayperview.equals("Pay Per View")) {
                HomeActivity.m_jsonTvshowfeaturedCarousels = JSONRPCAPI.getTVfeaturedCarousels(HomeActivity.mostwatched, HomeActivity.mfreeorall);
                if (HomeActivity.m_jsonTvshowfeaturedCarousels == null) return null;
            } else {
                HomeActivity.m_jsonOndemandCarousels = JSONRPCAPI.getDemandCarousels(HomeActivity.mfreeorall);
                if (HomeActivity.m_jsonOndemandCarousels == null) return null;
            }
            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonOndemandCarousels);
            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonTvshowfeaturedCarousels);
            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                // if(!isdestroyed){
                if (HomeActivity.selectedmenu.equalsIgnoreCase(Constants.ALL_SIDE_MENU)) {


                    if (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_CONTENT)) {
                        Log.d("====>", "Go to main content SUB_RIGHT_CONTENT");
                           /* if(HomeActivity.allCarousels != null && HomeActivity.allCarousels.length() > 0)
                            {
                                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                GridFragment listfragment = GridFragment.newInstance(Constants.SHOWS, HomeActivity.allCarousels.toString());
                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();
                            }else
                            {*/
                        new LoadingAllTVfeaturedCarouselsById().execute(String.valueOf(HomeActivity.carousel_id));
//                            }


                    } else {


                        if (HomeActivity.isPayperview.equals("Pay Per View")) {

                            if (HomeActivity.m_jsonTvshowfeaturedCarousels != null && HomeActivity.m_jsonTvshowfeaturedCarousels.length() > 0) {
                                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowfeaturedCarousels.toString());
                                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }

                            }

                        } else {
                            if (HomeActivity.m_jsonOndemandCarousels != null && HomeActivity.m_jsonOndemandCarousels.length() > 0) {
                                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    FragmentOnDemandAllContent fragmentOnDemandAllContent = FragmentOnDemandAllContent.newInstance(Constants.SHOWS, HomeActivity.m_jsonOndemandCarousels.toString());
                                    fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, fragmentOnDemandAllContent);
                                    fragmentTransaction.commit();
                                }

                            } else {
                                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                    //new LoadingTvShowTVfeaturedCarousels().execute();
                                }
                            }

                        }


                    }


                } else if (HomeActivity.selectedmenu.equals(Constants.TV_SHOWS_SIDE_MENU)) {


                    if (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_CONTENT)) {
                        Log.d("====>", "Go to main content SUB_RIGHT_CONTENT");
                           /* if(HomeActivity.allCarousels != null && HomeActivity.allCarousels.length() > 0)
                            {
                                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                GridFragment listfragment = GridFragment.newInstance(Constants.SHOWS, HomeActivity.allCarousels.toString());
                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();
                            }else
                            {*/
                        new LoadingAllTVfeaturedCarouselsById().execute(String.valueOf(HomeActivity.carousel_id));
//                            }


                    } else {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonOndemandCarousels.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    }


                }
                //}


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class LoadingPPVALLCarousels extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }


        @Override
        protected Object doInBackground(Object... params) {


            HomeActivity.m_jsonPPVALL = JSONRPCAPI.getPPVALLCarousels();
            if (HomeActivity.m_jsonPPVALL == null) return null;

            Log.d("Genre::", "m_jsonPPVALL::" + HomeActivity.m_jsonPPVALL);
            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                // if(!isdestroyed){
                if (HomeActivity.m_jsonPPVALL != null && HomeActivity.m_jsonPPVALL.length() > 0) {
                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonPPVALL.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                }
                // }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class LoadingMoviebyRating extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mOnDemandlist.clear();
            fragment_ondemand_progress.setVisibility(View.VISIBLE);
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            HomeActivity.m_jsonAllmovielistbyrating = JSONRPCAPI.getMovieByRating();
            if (HomeActivity.m_jsonAllmovielistbyrating == null) return null;
            Log.d("moviebyrating::", "::" + HomeActivity.m_jsonAllmovielistbyrating);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                // if(!isdestroyed){
                if (HomeActivity.m_jsonAllmovielistbyrating.length() > 0) {


                    movieRataingList(HomeActivity.m_jsonAllmovielistbyrating);

                }
                //    }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class LoadingAllTVfeaturedCarouselsById extends AsyncTask<String, Object, Object> {
        String description = "", title = "", image = "", name = "";
        int id;
        JSONArray allCarousels;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            HomeActivity.allCarousels = JSONRPCAPI.getAllTVfeaturedCarousels(Integer.parseInt(params[0]), 1000, 0, HomeActivity.mfreeorall);
            if (HomeActivity.allCarousels == null) return null;
            Log.d("allCarousels::", "allCarousels::" + HomeActivity.allCarousels);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if (!isdestroyed) {
                    if (HomeActivity.selectedmenu.equals(Constants.MOVIES)) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            HomeActivity.content_position = Constants.SUB_CONTENT;
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            GridFragment listfragment = GridFragment.newInstance(Constants.CATEGORY_SHOW, HomeActivity.allCarousels.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }


                    } else {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            HomeActivity.content_position = Constants.SUB_CONTENT;
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            GridFragment listfragment = GridFragment.newInstance(Constants.SHOWS, HomeActivity.allCarousels.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void listPayperView() {
        try {
            payperViewList.clear();
            //payperViewList.add(new SideMenu("", "All", "", ""));
            payperViewList.add(new SideMenu("", "Movies", "", ""));
            payperViewList.add(new SideMenu("", "-by Genre", "", ""));
            payperViewList.add(new SideMenu("", "-by Rating", "", ""));
            payperViewList.add(new SideMenu("", "TV Shows", "", ""));
            HomeActivity.mMovieorSeriesName = payperViewList.get(0).getName();
            HomeActivity.toolbarMainContent = payperViewList.get(0).getName();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            PayperviewAdapter payperviewAdapter = new PayperviewAdapter(payperViewList, getActivity());
            fragment_ondemand_alllist_items.setAdapter(payperviewAdapter);

            HomeActivity.mfreeorall = 2;

            /*if (HomeActivity.m_jsonTvshowfeaturedCarousels != null && HomeActivity.m_jsonTvshowfeaturedCarousels.length() > 0) {
                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonPPVALL.toString());
                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                fragmentTransaction_list.commit();
            } else {
                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                    new LoadingPPVALLCarousels().execute();
                }
            }*/
            HomeActivity.ppv_selected_item = "movie";
            HomeActivity.selectedmenu = Constants.MOVIES;
            //HomeActivity.menu_position = Constants.FILTER_MENU;
            HomeActivity.content_position = Constants.MAIN_CONTENT;
            HomeActivity.is_networkSelected = false;
            //listMovieSubcategories();
            if (HomeActivity.m_jsonPayPerViewmovielist != null && HomeActivity.m_jsonPayPerViewmovielist.length() > 0) {
                if (HomeActivity.getmFragmentOnDemandAll() != null) {
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonPayPerViewmovielist.toString());
                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                    fragmentTransaction_list.commit();
                }

            } else {
                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                    new LoadingPayPerViewMovieList().execute();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class PayperviewAdapter extends RecyclerView.Adapter<PayperviewAdapter.DataObjectHolder> {
        ArrayList<SideMenu> payperViewList;
        Context context;

        public PayperviewAdapter(ArrayList<SideMenu> payperViewList, Context context) {

            this.payperViewList = payperViewList;
            this.context = context;

        }

        @Override
        public PayperviewAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final PayperviewAdapter.DataObjectHolder holder, final int position) {

            holder.fragment_ondemandlist_items.setText(payperViewList.get(position).getName());
            final String menu_text = payperViewList.get(position).getName();
            if (position == mSelectedItem) {
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }


            holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mSelectedItem = position;

                        holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                        notifyDataSetChanged();
                        HomeActivity.mMovieorSeriesName = payperViewList.get(position).getName();
                        //((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.mMovieorSeriesName, HomeActivity.mSeriesSeason);

                        switch (menu_text) {
                            case "All":
                                HomeActivity.ppv_selected_item = "all";
                                if (HomeActivity.m_jsonTvshowfeaturedCarousels != null && HomeActivity.m_jsonTvshowfeaturedCarousels.length() > 0) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonPPVALL.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {


                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingPPVALLCarousels().execute();
                                    }
                                }

                                break;

                            case "Movies":
                                HomeActivity.ppv_selected_item = "movie";
                                HomeActivity.selectedmenu = Constants.MOVIES;
                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.is_networkSelected = false;
                                listMovieSubcategories();
                                if (HomeActivity.m_jsonPayPerViewmovielist != null && HomeActivity.m_jsonPayPerViewmovielist.length() > 0) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonPayPerViewmovielist.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {
                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingPayPerViewMovieList().execute();
                                    }
                                }
                                break;

                            case "-by Genre":
                                HomeActivity.ppv_selected_item = "movie";
                                HomeActivity.selectedmenu = Constants.MOVIES;
                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.is_networkSelected = false;
                                listMovieSubcategoriesPayperview(1);
                              /*  if (HomeActivity.m_jsonPayPerViewmovielist != null && HomeActivity.m_jsonPayPerViewmovielist.length() > 0) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonPayPerViewmovielist.toString());
                                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                } else {
                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingPayPerViewMovieList().execute();
                                    }
                                }*/

                                HomeActivity.isSearchClick = false;
                                HomeActivity.is_networkSelected = false;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.menu_position = Constants.CONTENT_MENU;
                                mAdapter_genreordecade = "LoadingTvshowListbyGenre";

                                if (HomeActivity.selectedmenu.equals(Constants.MOVIES)) {

                                    HomeActivity.selectedFilter = Constants.MOVIES_SIDE_MENU;
                                    if (HomeActivity.m_jsonMovieGenre != null && HomeActivity.m_jsonMovieGenre.length() > 0) {
                                        MovieGenreList(HomeActivity.m_jsonMovieGenre);

                                    } else {
                                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                            new LoadingMovieGenre().execute();
                                        }

                                    }


                                } else {


                                    try {
                                        mBachbuttonNavigation = "On-DemandSubcategoryofcategory";
                                        HomeActivity.selectedFilter = Constants.GENRE_FILTER;
                                        HomeActivity.is_networkSelected = false;
                                        if (isNetworkAvailable(getActivity()) || isWifiAvailable(getActivity())) {

                                            if (HomeActivity.m_jsonTvshowlistbyGenre != null && HomeActivity.m_jsonTvshowlistbyGenre.length() > 0) {
                                                tvShowByList(HomeActivity.m_jsonTvshowlistbyGenre);

                                            } else {
                                                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                                    new LoadingTvshowListbyGenre().execute();
                                                }


                                            }

                                        } else {
                                            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                                        }

//                                    listGenreDataReset();
                                /*FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                                GridList grid_fragment = new GridList();
                                fragmentTransaction_grid.replace(R.id.fragment_ondemand_pagerandlist, grid_fragment );
                                fragmentTransaction_grid.commit();*/

                                   /* FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                                    GridFragment grid_fragment = GridFragment.newInstance(Constants.SHOWS,"");
                                    fragmentTransaction_grid.replace(R.id.fragment_ondemand_pagerandlist, grid_fragment);
                                    fragmentTransaction_grid.commit();*/
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                break;

                            case "-by Rating":
                                HomeActivity.ppv_selected_item = "movie";
                                HomeActivity.selectedmenu = Constants.MOVIES;
                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.is_networkSelected = false;
                                listMovieSubcategoriesPayperview(2);
                               /* if (HomeActivity.m_jsonPayPerViewmovielist != null && HomeActivity.m_jsonPayPerViewmovielist.length() > 0) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonPayPerViewmovielist.toString());
                                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                } else {
                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingPayPerViewMovieList().execute();
                                    }
                                }*/
                                HomeActivity.isSearchClick = false;
                                mAdapter_genreordecade = "LoadingMovieListByrating";
                                HomeActivity.is_networkSelected = false;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.menu_position = Constants.CONTENT_MENU;
                                HomeActivity.selectedFilter = Constants.MOVIES_SIDE_MENU;
                                new LoadingMoviebyRating().execute();

                                break;

                            case "TV Shows":
                                HomeActivity.ppv_selected_item = "show";
                                HomeActivity.menu_position = Constants.FILTER_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;
                                HomeActivity.selectedmenu = Constants.TV_SHOWS_SIDE_MENU;
                                HomeActivity.is_networkSelected = false;
                                mBachbuttonNavigation = "On-DemandSubcategory";
                                listDataReset();

                                if (HomeActivity.m_jsonPayPerViewShowlist != null && HomeActivity.m_jsonPayPerViewShowlist.length() > 0) {
                                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonPayPerViewShowlist.toString());
                                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {

                                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                        new LoadingPayperViewShowList().execute();
                                    }
                                }
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return payperViewList.size();
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


    public class NetworkListAdapter extends RecyclerView.Adapter<NetworkListAdapter.DataObjectHolder> {
        ArrayList<GridBean> gridBeans;
        Context context;
        int mSelectedItem = 0;

        String string;

        public NetworkListAdapter(ArrayList<GridBean> gridBeans, Context context) {
            this.gridBeans = gridBeans;
            this.context = context;
            text_font_typeface();
        }

        @Override
        public NetworkListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final DataObjectHolder holder, final int position) {
            network_spinner_layout.setVisibility(View.VISIBLE);
            holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
            String name = gridBeans.get(position).getName();

            holder.fragment_ondemandlist_items.setText(name);


            if (position == mSelectedItem) {
                if (!mParam1.equalsIgnoreCase("network")) {
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                }

            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }
            holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mParam1 = "";
                    mSelectedItem = position;
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                    notifyDataSetChanged();
                    HomeActivity.toolbarSubContent = gridBeans.get(position).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    HomeActivity.networkImage = gridBeans.get(position).getImage();
                    new LoadingTVNetworkList().execute(gridBeans.get(position).getId());
                }
            });
        }


        @Override
        public int getItemCount() {
            return gridBeans.size();
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

    private class LoadingTVNetworkList extends AsyncTask<String, Object, Object> {
        private JSONArray m_jsonNetworkList;

        @Override
        protected void onPreExecute() {
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
                FragmentOnDemandAll.hideContent(false);
            }

            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
                m_jsonNetworkList = JSONRPCAPI.getTVNetworkList(Integer.parseInt(params[0]), 1000, 0, HomeActivity.mfreeorall);
                if (m_jsonNetworkList == null) return null;
                JSONObject network_detail = JSONRPCAPI.getNetworkDetails(Integer.parseInt(params[0]));
                if (network_detail == null) return null;
                String slogan = network_detail.getString("slogan");
                String name = network_detail.getString("name");
                String headquarters = network_detail.getString("headquarters");
                String start_time = network_detail.getString("start_time");
                String image = network_detail.getString("image");
                String description = network_detail.getString("description");
                int id = network_detail.getInt("id");
                String slug = network_detail.getString("slug");

                HashMap<String, String> networkDetails = new HashMap<>();
                networkDetails.put("slogan", slogan);
                networkDetails.put("name", name);
                networkDetails.put("headquarters", headquarters);
                networkDetails.put("start_time", start_time);
                networkDetails.put("image", image);
                networkDetails.put("description", description);
                networkDetails.put("id", "" + id);
                networkDetails.put("slug", slug);

                HomeActivity.networkDetails = networkDetails;

                Log.d("NetworkList::", "Selected Network list::" + m_jsonNetworkList);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                // if(!isdestroyed){
                if (m_jsonNetworkList!=null&&m_jsonNetworkList.length() > 0) {
                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                        GridFragment network_listfragment = GridFragment.newInstance(Constants.SHOWS, m_jsonNetworkList.toString());
                        network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, network_listfragment);
                        network_fragmentTransaction.commit();
                    }

                }else{
                    if(HomeActivity.mfreeorall==1){
                        Toast.makeText(getActivity(),"No free episodes currently available. Switch toggle from Free to All to see all available viewing options below.",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),"No episodes currently available.",Toast.LENGTH_SHORT).show();
                    }

                    FragmentManager fragmentManager=getFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    List<Fragment> allFragments = getFragmentManager().getFragments();
                    if (allFragments != null) {
                        for (Fragment f : allFragments) {
                            //You can perform additional check to remove some (not all) fragments:
                            if (f instanceof DialogFragment) {
                                fragmentTransaction.remove(f);
                            }
                        }
                    }



                    fragmentTransaction.commit();
                }
                //       }


            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }

    private class LoadingPrimetimeCarousels extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }


        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                // if(!isdestroyed){
                if (HomeActivity.content_position.equals(Constants.SUB_CONTENT)) {
                    new LoadingAllTVfeaturedCarouselsById().execute(String.valueOf(HomeActivity.carousel_id));
                } else {
                    if (HomeActivity.m_jsonPrimeTimeList != null) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.PRIMETIMEANYTIME, HomeActivity.m_jsonPrimeTimeList.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    }
                }
                //   }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected Object doInBackground(Object... params) {
            /*if (isswitchselected) {

                HomeActivity.mfreeorall = 0;
            } else {
                HomeActivity.mfreeorall = 1;

            }*/
            Log.d("selecttv:::", "day of week.." + dayofweek);
            HomeActivity.m_jsonPrimeTimeList = JSONRPCAPI.getPrimetimeCarousels(dayofweek, HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonPrimeTimeList == null) return null;
            return null;

        }


    }

    private class LoadingCategoryItems extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }


        }

        @Override
        protected Object doInBackground(Object... params) {
            HomeActivity.m_jsonTvshowbyCategoryItems = JSONRPCAPI.getShowcarouselsbycategory(String.valueOf(params[0]), HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonTvshowbyCategoryItems == null) return null;
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                //  if(!isdestroyed){
                FragmentOnDemandAll.hideContent(false);

                if (HomeActivity.content_position.equals(Constants.SUB_CONTENT)) {
                    new LoadingAllTVfeaturedCarouselsById().execute(String.valueOf(HomeActivity.carousel_id));
                } else {
                    if (HomeActivity.m_jsonTvshowbyCategoryItems != null) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.CATEGORY_SHOW, HomeActivity.m_jsonTvshowbyCategoryItems.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    }
                }
                //   }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private class LoadingTvShowCarousels extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... params) {

/*
            if (isswitchselected) {

                HomeActivity.mfreeorall = 0;
            } else {
                HomeActivity.mfreeorall = 1;

            }*/


            HomeActivity.m_jsonTvshowCarousels = JSONRPCAPI.getShowcarousels(HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonTvshowCarousels == null) return null;
            /*if ( HomeActivity.m_jsonOndemandCarousels   == null) return null;
            if ( HomeActivity.m_jsonTvshowfeaturedCarousels   == null) return null;
            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonOndemandCarousels);
            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonTvshowfeaturedCarousels);*/
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try {
                //  if(!isdestroyed){
                if (HomeActivity.content_position.equals(Constants.SUB_CONTENT)) {
                    new LoadingAllTVfeaturedCarouselsById().execute(String.valueOf(HomeActivity.carousel_id));
                } else {
                    if (HomeActivity.m_jsonTvshowCarousels != null && HomeActivity.m_jsonTvshowCarousels.length() > 0) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowCarousels.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    }
                }
                // }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    private void showNetworkList() {
        arraynetworkAdapterData.clear();
        arraynetworkAdapterData.add("A");
        arraynetworkAdapterData.add("B");
        arraynetworkAdapterData.add("C");
        arraynetworkAdapterData.add("D");
        arraynetworkAdapterData.add("E");
        arraynetworkAdapterData.add("F");
        arraynetworkAdapterData.add("G");
        arraynetworkAdapterData.add("H");
        arraynetworkAdapterData.add("I");
        arraynetworkAdapterData.add("J");
        arraynetworkAdapterData.add("K");
        arraynetworkAdapterData.add("L");
        arraynetworkAdapterData.add("M");
        arraynetworkAdapterData.add("N");
        arraynetworkAdapterData.add("O");
        arraynetworkAdapterData.add("P");
        arraynetworkAdapterData.add("Q");
        arraynetworkAdapterData.add("R");
        arraynetworkAdapterData.add("S");
        arraynetworkAdapterData.add("T");
        arraynetworkAdapterData.add("U");
        arraynetworkAdapterData.add("V");
        arraynetworkAdapterData.add("W");
        arraynetworkAdapterData.add("X");
        arraynetworkAdapterData.add("Y");
        arraynetworkAdapterData.add("Z");
        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, arraynetworkAdapterData);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_network.setAdapter(adapter);

        if (mParam1.equalsIgnoreCase("network")) {
            new LoadingInterestNetworkList().execute();
        } else {
            new LoadingNetworkList().execute();
        }
        //new LoadingNetworkList().execute();
       /* adapterActor = new SpinnerAdapter(context, arraynetworkAdapterData);
        spinner_actors.setAdapter(adapterActor);*/
        spinner_network.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mParam1.equalsIgnoreCase("network")) {

                } else {
                    new LoadingNetworkList().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_network.setFocusable(true);

    }

    private void loadgenre() {
        try {
            HomeActivity.isSearchClick = false;
            HomeActivity.is_networkSelected = false;
            HomeActivity.content_position = Constants.MAIN_CONTENT;
            HomeActivity.menu_position = Constants.CONTENT_MENU;
            mAdapter_genreordecade = "LoadingTvshowListbyGenre";


            HomeActivity.selectedFilter = Constants.MOVIES_SIDE_MENU;
            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
            if (HomeActivity.m_jsonMovieGenre != null && HomeActivity.m_jsonMovieGenre.length() > 0) {

                MovieGenreList(HomeActivity.m_jsonMovieGenre);

            } else {
                if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                    new LoadingMovieGenre().execute();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadnetwork() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        fragment_ondemand_alllist_items.setLayoutManager(mLayoutManager);
        showNetworkList();
        HomeActivity.isSearchClick = false;
//                                ((HomeActivity) getActivity()).toolbarTextChange(Constants.TV_NETWORKS_SIDE_MENU);
        HomeActivity.menu_position = Constants.FILTER_MENU;
        HomeActivity.content_position = Constants.MAIN_CONTENT;
        HomeActivity.selectedmenu = Constants.TV_SHOWS_SIDE_MENU;
        mBachbuttonNavigation = "On-DemandSubcategory";
        HomeActivity.is_networkSelected = true;
        HomeActivity.selectedFilter = Constants.NETWORK_FILTER;


        //new LoadingInterestNetworkList().execute();
    }

    public class LoadingInterestNetworkList extends AsyncTask<Object, Object, Object> {
        int id = 0;
        String name = "", image = "", letter = spinner_network.getSelectedItem().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment_ondemand_alllist_items.setVisibility(View.GONE);
            fragment_ondemand_progress.setVisibility(View.VISIBLE);
            FragmentOnDemandAll.hideContent(true);
            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
           /* FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
            DialogFragment dialog_fragment = new DialogFragment();
            network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
            network_fragmentTransaction.commit();*/
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                HomeActivity.m_jsonNetworkList = JSONRPCAPI.getAllNetworks();
                if (HomeActivity.m_jsonNetworkList == null) return null;
                gridBeans.clear();
                Log.d("NetworkList::", "Network list::" + HomeActivity.m_jsonNetworkList);
                for (int i = 0; i < HomeActivity.m_jsonNetworkList.length(); i++) {
                    try {
                        JSONObject jSubscriptionCategory = HomeActivity.m_jsonNetworkList.getJSONObject(i);
                        if (jSubscriptionCategory.has("name")) {
                            name = jSubscriptionCategory.getString("name");
                        }
                        if (jSubscriptionCategory.has("id")) {
                            id = jSubscriptionCategory.getInt("id");
                        }


                        if (jSubscriptionCategory.has("thumbnail")) {
                            image = jSubscriptionCategory.getString("thumbnail");
                        }

                        if (name.startsWith(letter)) {
                            gridBeans.add(new GridBean(String.valueOf(id), name, image, "", ""));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // if(!isdestroyed){
            try {

                fragment_ondemand_progress.setVisibility(View.GONE);
                fragment_ondemand_alllist_items.setVisibility(View.VISIBLE);
                network_spinner_layout.setVisibility(View.VISIBLE);


                networkListAdapter = new NetworkListAdapter(gridBeans, getActivity());
                fragment_ondemand_alllist_items.setAdapter(networkListAdapter);
                fragment_ondemand_selected_item_text.setVisibility(View.VISIBLE);
                network_spinner_layout.setVisibility(View.VISIBLE);
                fragment_ondemand_selected_item_text.setText("Networks");
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                new LoadingTVNetworkList().execute(mParam2);
            } catch (Exception e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
            }
            //  }


        }
    }

    public void onBackImagePressed() {
        if (HomeActivity.menu_position.equalsIgnoreCase(Constants.MAIN_MENU) && HomeActivity.content_position.equalsIgnoreCase(Constants.MAIN_CONTENT)) {
            Log.d("====>", "Go to home page");

            ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                       /* View t_view=getView().findViewById(R.id.activity_homescreen_fragemnt_layout);
                        if(t_view!=null){

                        }*/
            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
            fragmentTransaction.commit();
        } else if (HomeActivity.menu_position.equalsIgnoreCase(Constants.MAIN_MENU) && HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_CONTENT)) {


            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
            HomeActivity.menu_position = Constants.MAIN_MENU;
            HomeActivity.content_position = Constants.MAIN_CONTENT;
            if (HomeActivity.isPayperview.equals("Pay Per View")) {
                HomeActivity.toolbarMainContent = Constants.ALL_SIDE_MENU;
                HomeActivity.toolbarSubContent = "";
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentOnDemandAll fragmentOnDemandAllContent = FragmentOnDemandAll.newInstance("home", "");
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAllContent);
                fragmentTransaction.commit();

            } else {

                if (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_RIGHT_CONTENT)) {
                    Log.d("====>", "Go to main content SUB_RIGHT_CONTENT");
                    HomeActivity.toolbarMainContent = "TV Networks";
                    HomeActivity.toolbarSubContent = "";
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                    if (HomeActivity.allCarousels != null) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            GridFragment listfragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.allCarousels.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                            HomeActivity.content_position = Constants.SUB_CONTENT;
                        }

                    }
                } else {

                    if (!HomeActivity.selectedmenu.equals(Constants.SPORTS)) {
                        HomeActivity.toolbarMainContent = "Home";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                        if (HomeActivity.m_jsonOndemandCarousels != null && HomeActivity.m_jsonOndemandCarousels.length() > 0) {
                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                FragmentOnDemandAllContent fragmentOnDemandAllContent = FragmentOnDemandAllContent.newInstance(Constants.SHOWS, HomeActivity.m_jsonOndemandCarousels.toString());
                                fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, fragmentOnDemandAllContent);
                                fragmentTransaction.commit();
                            }

                        } else {
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingTvShowTVfeaturedCarousels().execute();
                            }
                        }
                    } else {
                        HomeActivity.toolbarMainContent = "Sports";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                        if (HomeActivity.m_jsonSportsCarousels != null && HomeActivity.m_jsonSportsCarousels.length() > 0) {
                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                HorizontalListsFragment fragmentOnDemandAllContent = HorizontalListsFragment.newInstance(Constants.SPORTS, HomeActivity.m_jsonSportsCarousels.toString());
                                fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, fragmentOnDemandAllContent);
                                fragmentTransaction.commit();
                            }

                        } else {
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingSportsList().execute();
                            }
                        }
                    }

                }


            }

        } else if (HomeActivity.menu_position.equalsIgnoreCase(Constants.FILTER_MENU) && HomeActivity.content_position.equalsIgnoreCase(Constants.MAIN_CONTENT)) {

            Log.d("====>", "Go to main menu");
            HomeActivity.toolbarMainContent = Constants.ALL_SIDE_MENU;
            HomeActivity.toolbarSubContent = "";
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentOnDemandAll fragmentOnDemandAllContent = FragmentOnDemandAll.newInstance("home", "");
            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAllContent);
            fragmentTransaction.commit();
            HomeActivity.menu_position = Constants.MAIN_MENU;
            HomeActivity.content_position = Constants.MAIN_CONTENT;

        } else if (HomeActivity.menu_position.equalsIgnoreCase(Constants.FILTER_MENU) && (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_CONTENT) || (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_RIGHT_CONTENT)))) {
            Log.d("====>", "Go to main content");

            if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.PRIMETIMEANYTIME)) {
                HomeActivity.content_position = Constants.MAIN_CONTENT;
                HomeActivity.toolbarSubContent = mOnDemandlist.get(mPrimeTimeSelectedItem).getName();
                HomeActivity.toolbarMainContent = "Primetime";
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                if (HomeActivity.m_jsonPrimeTimeList != null && HomeActivity.m_jsonPrimeTimeList.length() > 0) {
                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.PRIMETIMEANYTIME, HomeActivity.m_jsonPrimeTimeList.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                } else {
                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                        new LoadingPrimetimeCarousels().execute();
                    }
                }
            } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.NETWORK_FILTER)) {
                if (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_RIGHT_CONTENT)) {
                    Log.d("====>", "Go to main content SUB_RIGHT_CONTENT");
                    HomeActivity.toolbarMainContent = "TV Networks";
                    HomeActivity.toolbarSubContent = "";
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                    if (HomeActivity.allCarousels != null) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            GridFragment listfragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.allCarousels.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                            HomeActivity.content_position = Constants.SUB_CONTENT;
                        }

                    }
                } else {
                    Log.d("====>", "Go to main content MAIN_CONTENT");
                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    if (HomeActivity.m_jsonNetworkListItems != null && HomeActivity.m_jsonNetworkListItems.length() > 0) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.BYNETWORK, HomeActivity.m_jsonNetworkListItems.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    } else {
                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                            new LoadingAllNetworkList().execute();
                        }
                    }
                }

            } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.TV_SHOWS_SIDE_MENU)) {

                if (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_RIGHT_CONTENT)) {
                    Log.d("====>", "Go to main content SUB_RIGHT_CONTENT");
                    HomeActivity.toolbarMainContent = "TV Networks";
                    HomeActivity.toolbarSubContent = "";
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                    if (HomeActivity.allCarousels != null) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            GridFragment listfragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.allCarousels.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                            HomeActivity.content_position = Constants.SUB_CONTENT;
                        }

                    }
                } else {
                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

                    if (HomeActivity.m_jsonTvshowCarousels != null && HomeActivity.m_jsonTvshowCarousels.length() > 0) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowCarousels.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    } else {

                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                            new LoadingTvShowCarousels().execute();
                        }
                    }
                }


            } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.CATEGORY_FILTER)) {


                HomeActivity.content_position = Constants.MAIN_CONTENT;
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

                if (HomeActivity.m_jsonTvshowbyCategoryItems != null && HomeActivity.m_jsonTvshowbyCategoryItems.length() > 0) {
//                                    setCategoryList(HomeActivity.m_jsonTvshowbyCategoryList);
                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.CATEGORY_SHOW, HomeActivity.m_jsonTvshowbyCategoryItems.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }


                } else {
                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                        new LoadingCategoryItems().execute(mCategoryid);
                    }


                }


            } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.MOVIES_SIDE_MENU)) {

                HomeActivity.selectedmenu = Constants.MOVIES;
                HomeActivity.menu_position = Constants.FILTER_MENU;
                HomeActivity.content_position = Constants.MAIN_CONTENT;
                HomeActivity.toolbarMainContent = Constants.ALL_SIDE_MENU;
                HomeActivity.toolbarSubContent = "";
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                if (HomeActivity.isPayperview.equals("Pay Per View")) {
                    HomeActivity.selectedmenu = Constants.MOVIES;
                    HomeActivity.menu_position = Constants.FILTER_MENU;
                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                    HomeActivity.is_networkSelected = false;

                    if (HomeActivity.m_jsonPayPerViewmovielist != null && HomeActivity.m_jsonPayPerViewmovielist.length() > 0) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonPayPerViewmovielist.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    } else {

                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                            new LoadingPayPerViewMovieList().execute();
                        }

                    }
                } else {
                    if (HomeActivity.m_jsonAllmovielist != null && HomeActivity.m_jsonAllmovielist.length() > 0) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonAllmovielist.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    } else {
                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                            new LoadingAllMovieList().execute();
                        }
                    }
                }


            } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.GENRE_FILTER)) {
                HomeActivity.content_position = Constants.MAIN_CONTENT;
                try {

                    mBachbuttonNavigation = "On-DemandSubcategoryofcategory";
                    HomeActivity.is_networkSelected = false;
                    if (isNetworkAvailable(getActivity()) || isWifiAvailable(getActivity())) {
                        mAdapter_genreordecade = "LoadingTvshowListbyGenre";
                        if (HomeActivity.m_jsonTvshowlistbyGenre != null && HomeActivity.m_jsonTvshowlistbyGenre.length() > 0) {
                            tvShowByList(HomeActivity.m_jsonTvshowlistbyGenre);

                        } else {
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingTvshowListbyGenre().execute();
                            }
                        }

                    } else {
                        Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        } else {


            if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.MOVIES_SIDE_MENU) || (HomeActivity.selectedmenu.equals(Constants.MOVIES))) {
                if (HomeActivity.selectedmenu.equals(Constants.MOVIES)) {
                    HomeActivity.selectedmenu = Constants.MOVIES;
                    HomeActivity.menu_position = Constants.FILTER_MENU;
                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                    HomeActivity.toolbarMainContent = "Movies";
                    HomeActivity.toolbarSubContent = "";
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                    listMovieSubcategories();
                    if (HomeActivity.isPayperview.equals("Pay Per View")) {
                        HomeActivity.selectedmenu = Constants.MOVIES;
                        HomeActivity.menu_position = Constants.FILTER_MENU;
                        HomeActivity.content_position = Constants.MAIN_CONTENT;
                        HomeActivity.is_networkSelected = false;

                        if (HomeActivity.m_jsonPayPerViewmovielist != null && HomeActivity.m_jsonPayPerViewmovielist.length() > 0) {
                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonPayPerViewmovielist.toString());
                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();
                            }

                        } else {
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingPayPerViewMovieList().execute();
                            }
                        }
                    } else {


                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                        if (HomeActivity.m_jsonAllmovielist != null && HomeActivity.m_jsonAllmovielist.length() > 0) {
                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonAllmovielist.toString());
                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();
                            }

                        } else {
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingAllMovieList().execute();
                            }
                        }
                    }
                }
            } else if (HomeActivity.selectedFilter.equalsIgnoreCase(Constants.CATEGORY_FILTER)) {
                HomeActivity.selectedFilter = "";
                HomeActivity.content_position = "";
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

                if (HomeActivity.m_jsonTvshowbyCategoryItems != null && HomeActivity.m_jsonTvshowbyCategoryItems.length() > 0) {
//                                    setCategoryList(HomeActivity.m_jsonTvshowbyCategoryList);
                    if (HomeActivity.getmFragmentOnDemandAll() != null) {
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.CATEGORY_SHOW, HomeActivity.m_jsonTvshowbyCategoryItems.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }


                } else {
                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                        new LoadingCategoryItems().execute(mCategoryid);
                    }


                }


            } else {


                if (HomeActivity.menu_position.equals(Constants.FILTER_MENU) || HomeActivity.menu_position.equals(Constants.CONTENT_MENU)) {
                    HomeActivity.toolbarMainContent = "TV Shows";
                    HomeActivity.toolbarSubContent = "";
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

                    HomeActivity.menu_position = Constants.FILTER_MENU;
                    HomeActivity.content_position = Constants.MAIN_CONTENT;
                    HomeActivity.selectedmenu = Constants.TV_SHOWS_SIDE_MENU;
                    HomeActivity.selectedFilter = "";
                    Log.d("====>", "Go to main filter menu");
                    listDataReset();
                    if (HomeActivity.m_jsonTvshowCarousels != null && HomeActivity.m_jsonTvshowCarousels.length() > 0) {
                        if (HomeActivity.getmFragmentOnDemandAll() != null) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowCarousels.toString());
                            fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                            fragmentTransaction_list.commit();
                        }

                    } else {

                        if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                            new LoadingTvShowCarousels().execute();
                        }
                    }
                } else {
                    if (HomeActivity.content_position.equalsIgnoreCase(Constants.SUB_RIGHT_CONTENT)) {
                        Log.d("====>", "Go to main content SUB_RIGHT_CONTENT");
                        HomeActivity.toolbarMainContent = "TV Networks";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");

                        if (HomeActivity.allCarousels != null) {
                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                GridFragment listfragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.allCarousels.toString());
                                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();
                                HomeActivity.content_position = Constants.SUB_CONTENT;
                            }

                        }
                    } else {

                        if (HomeActivity.m_jsonOndemandCarousels != null && HomeActivity.m_jsonOndemandCarousels.length() > 0) {
                            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                FragmentOnDemandAllContent fragmentOnDemandAllContent = FragmentOnDemandAllContent.newInstance(Constants.SHOWS, HomeActivity.m_jsonOndemandCarousels.toString());
                                fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, fragmentOnDemandAllContent);
                                fragmentTransaction.commit();
                            }

                        } else {
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingTvShowTVfeaturedCarousels().execute();
                            }
                        }
                    }
                }


            }


        }


        fragment_ondemand_switch_layout.setVisibility(View.VISIBLE);
    }

    private void clearLoadeddata() {
        try {
            if (HomeActivity.m_jsonMovieGenre != null) {
                HomeActivity.m_jsonMovieGenre = new JSONArray();
            }
            if (HomeActivity.m_jsonAllmovielistbyrating != null)
                HomeActivity.m_jsonAllmovielistbyrating = new JSONArray();

            if (HomeActivity.m_jsonOndemandCarousels != null)
                HomeActivity.m_jsonOndemandCarousels = new JSONArray();

            if (HomeActivity.m_jsonSportsCarousels != null)
                HomeActivity.m_jsonSportsCarousels = new JSONArray();

            if (HomeActivity.m_jsonPrimeTimeList != null)
                HomeActivity.m_jsonPrimeTimeList = new JSONArray();

            if (HomeActivity.m_jsonTvshowCarousels != null)
                HomeActivity.m_jsonTvshowCarousels = new JSONArray();

            if (HomeActivity.m_jsonTvshowbyCategoryItems != null)
                HomeActivity.m_jsonTvshowbyCategoryItems = new JSONArray();

            if (HomeActivity.m_jsonAllmovielist != null)
                HomeActivity.m_jsonAllmovielist = new JSONArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAnalyticreport(String mainString, String SubString1, String SubString2) {
        try {
            if (HomeActivity.isPayperview.equals("Pay Per View")) {
                if (TextUtils.isEmpty(mainString)) {
                    Utilities.setAnalytics(mTracker, "PayPerView");
                } else if (TextUtils.isEmpty(SubString1)) {
                    Utilities.setAnalytics(mTracker, "PayPerView-" + mainString);
                } else if (TextUtils.isEmpty(SubString2)) {
                    Utilities.setAnalytics(mTracker, "PayPerView-" + mainString + "-" + SubString1);
                } else {
                    Utilities.setAnalytics(mTracker, "PayPerView-" + mainString + "-" + SubString1 + "-" + SubString2);
                }

            } else if (HomeActivity.isPayperview.equals("On-Demand")) {
                if (TextUtils.isEmpty(mainString)) {
                    Utilities.setAnalytics(mTracker, "OnDemand");
                } else if (TextUtils.isEmpty(SubString1)) {
                    Utilities.setAnalytics(mTracker, "OnDemand-" + mainString);
                } else if (TextUtils.isEmpty(SubString2)) {
                    Utilities.setAnalytics(mTracker, "OnDemand-" + mainString + "-" + SubString1);
                } else {
                    Utilities.setAnalytics(mTracker, "OnDemand-" + mainString + "-" + SubString1 + "-" + SubString2);
                }

            } else {
                if (TextUtils.isEmpty(mainString)) {
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent);
                } else if (TextUtils.isEmpty(SubString1)) {
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent + "-" + mainString);
                } else if (TextUtils.isEmpty(SubString2)) {
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent + "-" + mainString + "-" + SubString1);
                } else {
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent +"-" + mainString + "-" + SubString1 + "-" + SubString2);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


