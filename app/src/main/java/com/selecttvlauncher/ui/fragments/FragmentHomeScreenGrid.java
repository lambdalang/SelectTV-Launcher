package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.BeanClass.HomeBean;
import com.selecttvlauncher.BeanClass.HomeScreenArray;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.channels.ChannelTotalFragment;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.activities.WebViewActivity;
import com.selecttvlauncher.ui.dialogs.PopupDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FragmentHomeScreenGrid extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //private RecyclerView activity_homescreen_grid_recycler;
    private GridLayoutManager layoutManager;
    HomeViewAdapter homeViewAdapter;
    ArrayList<HomeScreenArray> homeScreenArrayArrayList = new ArrayList<>();
    private ImageView setting_imageview;
    private DigitalClock digitalClock1;
    private TextView date_textview, terms_textview, support_textview;
    private FragmentOnDemandAll fragmentOnDemandAll;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private LayoutInflater inflate;
    private ArrayList<HomeBean> home_list = new ArrayList<>();
    private View home_grid_layout;

    private Tracker mTracker;

    private ImageView selectTVLogo;


    // TODO: Rename and change types and number of parameters
    public static FragmentHomeScreenGrid newInstance(String param1, String param2) {
        FragmentHomeScreenGrid fragment = new FragmentHomeScreenGrid();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHomeScreenGrid() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_home_screen_grid, container, false);
        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        HomeActivity.currentPageForBackButton = "";
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        Utilities.setAnalytics(mTracker, "Dashboard");

        //final View itemlayout = 	(View) inflate.inflate(R.layout., null);
        text_font_typeface();
        ((HomeActivity) getActivity()).setFragmentHomeScreenGrid(this);
        ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
        HomeActivity.activity_homescreen_toolbar_logout.setVisibility(View.GONE);

        selectTVLogo = (ImageView) view.findViewById(R.id.selectTVLogo);
        if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.smart_guide_package_name))) {
            if (!getActivity().getPackageName().equalsIgnoreCase(getString(R.string.selecttv_package_name)))
                selectTVLogo.setVisibility(View.VISIBLE);
        }

        final LinearLayout linearLayout1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
        LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
        LinearLayout linearLayout3 = (LinearLayout) view.findViewById(R.id.linearLayout3);
        LinearLayout linearLayout4 = (LinearLayout) view.findViewById(R.id.linearLayout4);
        LinearLayout linearLayout5 = (LinearLayout) view.findViewById(R.id.linearLayout5);
        LinearLayout linearLayout6 = (LinearLayout) view.findViewById(R.id.linearLayout6);
        LinearLayout linearLayout7 = (LinearLayout) view.findViewById(R.id.linearLayout7);
        LinearLayout linearLayout8 = (LinearLayout) view.findViewById(R.id.linearLayout8);
        LinearLayout linearLayout9 = (LinearLayout) view.findViewById(R.id.linearLayout9);
        LinearLayout linearLayout10 = (LinearLayout) view.findViewById(R.id.linearLayout10);

        ImageView imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.imageView3);
        ImageView imageView4 = (ImageView) view.findViewById(R.id.imageView4);
        ImageView imageView5 = (ImageView) view.findViewById(R.id.imageView5);
        ImageView imageView6 = (ImageView) view.findViewById(R.id.imageView6);
        ImageView imageView7 = (ImageView) view.findViewById(R.id.imageView7);
        ImageView imageView8 = (ImageView) view.findViewById(R.id.imageView8);
        ImageView imageView9 = (ImageView) view.findViewById(R.id.imageView9);
        ImageView imageView10 = (ImageView) view.findViewById(R.id.imageView10);

        TextView text_View1 = (TextView) view.findViewById(R.id.text_View1);
        TextView text_View2 = (TextView) view.findViewById(R.id.text_View2);
        TextView text_View3 = (TextView) view.findViewById(R.id.text_View3);
        TextView text_View4 = (TextView) view.findViewById(R.id.text_View4);
        TextView text_View5 = (TextView) view.findViewById(R.id.text_View5);
        TextView text_View6 = (TextView) view.findViewById(R.id.text_View6);
        TextView text_View7 = (TextView) view.findViewById(R.id.text_View7);
        TextView text_View8 = (TextView) view.findViewById(R.id.text_View8);
        TextView text_View9 = (TextView) view.findViewById(R.id.text_View9);
        TextView text_View10 = (TextView) view.findViewById(R.id.text_View10);
        terms_textview = (TextView) view.findViewById(R.id.terms_textview);
        support_textview = (TextView) view.findViewById(R.id.support_textview);

        text_View1.setTypeface(OpenSans_Regular);
        text_View2.setTypeface(OpenSans_Regular);
        text_View3.setTypeface(OpenSans_Regular);
        text_View4.setTypeface(OpenSans_Regular);
        text_View5.setTypeface(OpenSans_Regular);
        text_View6.setTypeface(OpenSans_Regular);
        text_View7.setTypeface(OpenSans_Regular);
        text_View8.setTypeface(OpenSans_Regular);
        text_View9.setTypeface(OpenSans_Regular);
        text_View10.setTypeface(OpenSans_Regular);


        home_list.add(new HomeBean(linearLayout1, imageView1, text_View1));
        home_list.add(new HomeBean(linearLayout2, imageView2, text_View2));
        home_list.add(new HomeBean(linearLayout3, imageView3, text_View3));
        home_list.add(new HomeBean(linearLayout4, imageView4, text_View4));
        home_list.add(new HomeBean(linearLayout5, imageView5, text_View5));
        home_list.add(new HomeBean(linearLayout6, imageView6, text_View6));
        home_list.add(new HomeBean(linearLayout7, imageView7, text_View7));
        home_list.add(new HomeBean(linearLayout8, imageView8, text_View8));
        home_list.add(new HomeBean(linearLayout9, imageView9, text_View9));
        home_list.add(new HomeBean(linearLayout10, imageView10, text_View10));


        //activity_homescreen_grid_recycler = (RecyclerView) view.findViewById(R.id.activity_homescreen_grid_recycler);

//        if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.selecttv_package_name))) {
//       /* For SelectTV */
//            homeScreenArrayArrayList = new ArrayList<>();
//            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.channels, "Channels"));
//            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.ondemand, "On-Demand"));
//            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.ic_speaker, "Listen"));
//            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.payview, "Pay Per View"));
//            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.addons, "Subscriptions"));
//            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.cable, "Over the Air"));
//            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.interest, "My Interests"));
//            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.myaccount, "My Account"));
//            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.games, "Games"));
//            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.more, "More"));
//        }
// else
        if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.broadview_package_name))) {
        /*for BroadView*/
            homeScreenArrayArrayList = new ArrayList<>();
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.channels, "Channels"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.ondemand, "On-Demand"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.ic_speaker, "Listen"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.payview, "Pay Per View"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.addons, "Subscriptions"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.cable, "Live Local TV"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.checkin_checkout, "Info/Check-out"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.weather_icon, "Weather"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.games, "Games"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.more, "More Apps"));

        } else if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.smart_guide_package_name))) {
        /* For Smart Guide*/
            homeScreenArrayArrayList = new ArrayList<>();
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.channels, "Channels"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.ondemand, "On-Demand"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.ic_speaker, "Listen"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.payview, "Pay Per View"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.addons, "Subscriptions"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.cable, "Live TV/OTA"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.interest, "My Interests"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.myaccount, "My Account"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.games, "Games"));
            homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.more, "More Apps"));
        }

        terms_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        support_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = getString(R.string.support_url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    if (i != null) {
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ((HomeActivity) getActivity()).mHidetoolbar(false);


        for (int i = 0; i < 10; i++) {
            final int ii = i;
            LinearLayout ll = home_list.get(i).getLinearLayout();
            ImageView iv = home_list.get(i).getImageView();
            TextView tv = home_list.get(i).getTextView();

            iv.setImageResource(homeScreenArrayArrayList.get(i).getmHomescreenGridImages());
            tv.setText(homeScreenArrayArrayList.get(i).getmHomeScreenGridname());

            ll.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    try {
                        LinearLayout linearLayout = (LinearLayout) v;
                        if (hasFocus) {
                            // run scale animation and make it bigger
//                            v.setScaleX(1.1F);
//                            v.setScaleY(1.1F);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                linearLayout.getChildAt(0).setPadding(2, 2, 2, 2);
                                linearLayout.getChildAt(0).setBackgroundResource(R.drawable.sample_bg);
                            }
//                            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_in_tv);
//                            v.startAnimation(anim);
//                            anim.setFillAfter(true);
                        } else {
                            // run scale animation and make it smaller
//                            v.setScaleX(1F);
//                            v.setScaleY(1F);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                linearLayout.getChildAt(0).setPadding(0, 0, 0, 0);
                                linearLayout.getChildAt(0).setBackgroundResource(0);
                            }
//                            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_out_tv);
//                            v.startAnimation(anim);
//                            anim.setFillAfter(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if (i == 1) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeActivity.activity_homescreen_toolbar_logout.setVisibility(View.GONE);
                        HomeActivity.channelID = 145;
                        HomeActivity.isPayperview = homeScreenArrayArrayList.get(1).getmHomeScreenGridname();
                        HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(1).getmHomeScreenGridname();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
                        HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(1).getmHomeScreenGridname();
                        HomeActivity.menu_position = Constants.MAIN_MENU;
                        HomeActivity.content_position = Constants.MAIN_CONTENT;
                        HomeActivity.mfreeorall = 1;
                       /* FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentOnDemandAll = new FragmentOnDemandAll();
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                        fragmentTransaction.commit();*/

                        if (PreferenceManager.isFirstTime(getActivity())) {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            OnDemandYoutubeFragment mfragmentOnDemandAll = new OnDemandYoutubeFragment();
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, mfragmentOnDemandAll);
                            fragmentTransaction.commit();
                        } else {
                            try {
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home", "");
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                                fragmentTransaction.commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
            }
            if (i == 0) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("selected:::", "channels act");
                       /* Intent in=new Intent(getActivity(),ChannelsActivity.class);
                        startActivity(in);*/
                        HomeActivity.mfreeorall = 1;
                        HomeActivity.activity_homescreen_toolbar_logout.setVisibility(View.VISIBLE);
                        HomeActivity.isPayperview = homeScreenArrayArrayList.get(0).getmHomeScreenGridname();
                        Log.d("selected:::", "channels act" + homeScreenArrayArrayList.get(0).getmHomeScreenGridname());
                        HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(0).getmHomeScreenGridname();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");

                        HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(0).getmHomeScreenGridname();
                        HomeActivity.menu_position = Constants.MAIN_MENU;
                        HomeActivity.content_position = Constants.MAIN_CONTENT;

/*
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        ChannelFragment channelFragment = new ChannelFragment();
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, channelFragment);
                        fragmentTransaction.commit();

                        try {
                            if(Utilities.isMyServiceRunning(getActivity(), RadioService.class)){
                                getActivity().stopService(new Intent(getActivity(), RadioService.class));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        ChannelTotalFragment channelFragment = new ChannelTotalFragment();

                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, channelFragment);
                        fragmentTransaction.commit();

//                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                        ChannelTotalFragment channelFragment = new ChannelTotalFragment();
//                        com.selecttvlauncher.channels.ChannelTotalFragment
//
//                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, channelFragment);
//                        fragmentTransaction.commit();


                            /* HomeActivity.activity_homescreen_toolbar_logout.setVisibility(View.VISIBLE);
                        HomeActivity.isPayperview = homeScreenArrayArrayList.get(0).getmHomeScreenGridname();
                        Log.d("selected:::","channels act"+homeScreenArrayArrayList.get(0).getmHomeScreenGridname());
                         HomeActivity.toolbarGridContent=homeScreenArrayArrayList.get(0).getmHomeScreenGridname();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "","");

                        HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(0).getmHomeScreenGridname();
                        HomeActivity.menu_position = Constants.MAIN_MENU;
                        HomeActivity.content_position = Constants.MAIN_CONTENT;


                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        ChannelTempFragment channelFragment = new ChannelTempFragment();
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, channelFragment);
                        fragmentTransaction.commit();
                        try {
                            if(Utilities.isMyServiceRunning(getActivity(), RadioService.class)){
                                getActivity().stopService(new Intent(getActivity(), RadioService.class));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                    }
                });
            }
            if (i == 2) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeActivity.activity_homescreen_toolbar_logout.setVisibility(View.GONE);
                       /* Log.d("selected:::","channels act");
                        Intent in=new Intent(getActivity(),ChannelsActivity.class);
                        startActivity(in);*/
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                getSystemOverlaypermssions();
                            else {
                                HomeActivity.isPayperview = homeScreenArrayArrayList.get(2).getmHomeScreenGridname();

                                Log.d("selected:::", "channels act" + homeScreenArrayArrayList.get(2).getmHomeScreenGridname());
                                HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(2).getmHomeScreenGridname();
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");

                                HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(2).getmHomeScreenGridname();
                                HomeActivity.menu_position = Constants.MAIN_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                FragmentListenMain fragmentListenMain = new FragmentListenMain();
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentListenMain);
                                fragmentTransaction.commit();
                            }
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }


            if (i == 3) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            HomeActivity.isPayperview = homeScreenArrayArrayList.get(3).getmHomeScreenGridname();


                            HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(3).getmHomeScreenGridname();
                            HomeActivity.menu_position = Constants.MAIN_MENU;
                            HomeActivity.content_position = Constants.MAIN_CONTENT;
                            HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(3).getmHomeScreenGridname();
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home", "");
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                            fragmentTransaction.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            if (i == 4) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(4).getmHomeScreenGridname();
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");

                            HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(4).getmHomeScreenGridname();
                            HomeActivity.menu_position = Constants.MAIN_MENU;
                            HomeActivity.content_position = Constants.MAIN_CONTENT;
                            HomeActivity.mfreeorall = 0;
                            HomeActivity.isPayperview = homeScreenArrayArrayList.get(4).getmHomeScreenGridname();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        /*FragmentSubScriptions fragmentSubScriptions = new FragmentSubScriptions();
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentSubScriptions);
                        fragmentTransaction.commit();*/
                            MySubscriptionFragment fragmentSubScriptions = new MySubscriptionFragment();
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentSubScriptions);
                            fragmentTransaction.commit();

                            /*FragmentSubscriptionNew fragmentSubScriptions = new FragmentSubscriptionNew();
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentSubScriptions);
                            fragmentTransaction.commit();*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


            if (i == 5) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.selecttv_package_name)) || getActivity().getPackageName().equalsIgnoreCase(getString(R.string.smart_guide_package_name))) {
                             /* for SelectTV Launcher app and Smart Guide Launcher app*/
                                HomeActivity.isPayperview = homeScreenArrayArrayList.get(5).getmHomeScreenGridname();

                                Log.d("selected:::", "channels act" + homeScreenArrayArrayList.get(5).getmHomeScreenGridname());
                                HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(5).getmHomeScreenGridname();
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");

                                HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(5).getmHomeScreenGridname();
                                HomeActivity.menu_position = Constants.MAIN_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                FragmentOTAANDCABLE fragmentOTAANDCABLE = new FragmentOTAANDCABLE();
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOTAANDCABLE);
                                fragmentTransaction.commit();

                            } else if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.broadview_package_name))) {

                            /* for BroadView Launcher app*/


                            /*without EON TV*/
//                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.local_channel_url)));
//                            if (myIntent != null) {
//                                startActivity(myIntent);
//                            } else {
//                                Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
//                            }

                              /*with EON TV*/
                                if (isInstalledPackageName("com.pineone.sb")) {
                                    Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
                                    intentToResolve.addCategory(Intent.CATEGORY_HOME);
                                    intentToResolve.setPackage("com.pineone.sb");
                                    ResolveInfo ri = getActivity().getPackageManager().resolveActivity(intentToResolve, 0);
                                    if (ri != null) {
                                        Intent intent = new Intent(intentToResolve);
                                        intent.setClassName(ri.activityInfo.applicationInfo.packageName, ri.activityInfo.name);
                                        intent.setAction(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        if (intent != null) {
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getContext(), "Error in opening the app", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                } else {
                                    Toast.makeText(getContext(), "The Application is not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            if (i == 6) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        PersonalizationDialogFragment customDialogFragment = new PersonalizationDialogFragment();
//                        customDialogFragment.show(getActivity().getSupportFragmentManager(), "pesonalize_page_fragment");

                        try {
                            if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.selecttv_package_name)) || getActivity().getPackageName().equalsIgnoreCase(getString(R.string.smart_guide_package_name))) {
                            /* for SelectTV Launcher app and Smart Guide Launcher app*/
                                HomeActivity.isPayperview = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();

                                Log.d("selected:::", "channels act" + homeScreenArrayArrayList.get(ii).getmHomeScreenGridname());
                                HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
                                Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent);

                                HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();
                                HomeActivity.menu_position = Constants.MAIN_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                InterestMainFragment mInterestMainFragment = new InterestMainFragment();
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, mInterestMainFragment);
                                fragmentTransaction.commit();
                            } else if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.broadview_package_name))) {

                          /* for BroadView Launcher app*/
                                String url = "http://broadviewip.com/hotel-demo";
                                Intent newIntent = new Intent(getActivity(), WebViewActivity.class);
                                newIntent.putExtra(WebViewActivity.PARAM_URL, url);
                                getActivity().startActivity(newIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            if (i == 7) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.selecttv_package_name)) || getActivity().getPackageName().equalsIgnoreCase(getString(R.string.smart_guide_package_name))) {
                            /* for SelectTV Launcher app and Smart Guide Launcher app*/
                                HomeActivity.isPayperview = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();

                                Log.d("selected:::", "channels act" + homeScreenArrayArrayList.get(ii).getmHomeScreenGridname());
                                HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
                                Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent);

                                HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();
                                HomeActivity.menu_position = Constants.MAIN_MENU;
                                HomeActivity.content_position = Constants.MAIN_CONTENT;

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                HomeFragment mHomeFragment = new HomeFragment();
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, mHomeFragment);
                                fragmentTransaction.commit();
                            } else if (getActivity().getPackageName().equalsIgnoreCase(getString(R.string.broadview_package_name))) {

                             /* for BroadView Launcher app*/
                                String url = "http://www.accuweather.com/en/ca/toronto/m5j/weather-forecast/55488";
                                Intent newIntent = new Intent(getActivity(), WebViewActivity.class);
                                newIntent.putExtra(WebViewActivity.PARAM_URL, url);
                                getActivity().startActivity(newIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


            if (i == 8) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            HomeActivity.isPayperview = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();

                            Log.d("selected:::", "channels act" + homeScreenArrayArrayList.get(ii).getmHomeScreenGridname());
                            HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
                            Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent);

                            HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();
                            HomeActivity.menu_position = Constants.MAIN_MENU;
                            HomeActivity.content_position = Constants.MAIN_CONTENT;

                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            GameMainFragment mHomeFragment = new GameMainFragment();
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, mHomeFragment);
                            fragmentTransaction.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


            if (i == 9) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            HomeActivity.isPayperview = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();

                            Log.d("selected:::", "channels act" + homeScreenArrayArrayList.get(ii).getmHomeScreenGridname());
                            HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
                            Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent);

                            HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(ii).getmHomeScreenGridname();
                            HomeActivity.menu_position = Constants.MAIN_MENU;
                            HomeActivity.content_position = Constants.MAIN_CONTENT;

                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            MoreMainFragment moreMainFragment = new MoreMainFragment();
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, moreMainFragment);
                            fragmentTransaction.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        setting_imageview = (ImageView) view.findViewById(R.id.setting_imageview);
        digitalClock1 = (DigitalClock) view.findViewById(R.id.digitalClock1);
        date_textview = (TextView) view.findViewById(R.id.date_textview);
        setViewFocus(setting_imageview);
        try {
            setdate();

            ((HomeActivity) getActivity()).setDateRefreshListener(new HomeActivity.DateRefreshListener() {
                @Override
                public void onRefresh() {

                    // Refresh Your Fragment
                    setdate();
                }
            });


            /*layoutManager = new GridLayoutManager(getActivity(), 5);
            layoutManager.setOrientation(GridLayoutManager.VERTICAL);
            activity_homescreen_grid_recycler.hasFixedSize();
            activity_homescreen_grid_recycler.setLayoutManager(layoutManager);
            homeViewAdapter = new HomeViewAdapter(homeScreenArrayArrayList, getActivity());
            activity_homescreen_grid_recycler.setAdapter(homeViewAdapter);*/

            setting_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);

                    if (intent != null) {
                        Log.d("intent", ":::" + intent.getCategories());
                        startActivity(intent);
                    } else {
                        Log.d("no intent", ":::" + intent.getPackage());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private boolean isInstalledPackageName(String packagename) {
        if (packagename.toLowerCase().equals("google play")) return true;
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> ril = getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);
        for (ResolveInfo ri : ril) {
            Log.e("Info", "" + ri.activityInfo.parentActivityName);
            if (ri != null) {
                String key = ri.activityInfo.packageName;
                Log.e("Info", "" + key);
                if (key.equals(packagename))
                    return true;
            }
        }
        return false;
    }

    private void getSystemOverlaypermssions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(getActivity())) {
                    String PACKAGE_NAME = getActivity().getApplicationContext().getPackageName();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + PACKAGE_NAME));
                    getActivity().startActivityForResult(intent, HomeActivity.OVERLAY_PERMISSION_REQ_CODE);
                } else {
                    loadListenData();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setdate() {
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat("EEEE MMM dd,yyyy hh:mm a", Locale.US);
            Date date = new Date();
            String s = parseFormat.format(date);
            String day = (String) android.text.format.DateFormat.format("EEEE", date);
            String month = (String) android.text.format.DateFormat.format("MMM", date);
            String date_day = (String) android.text.format.DateFormat.format("dd", date);
            String date_txt = day + ", " + month + " " + date_day;
            date_textview.setText(date_txt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void loadListenData() {
        try {
            HomeActivity.isPayperview = homeScreenArrayArrayList.get(2).getmHomeScreenGridname();

            Log.d("selected:::", "channels act" + homeScreenArrayArrayList.get(2).getmHomeScreenGridname());
            HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(2).getmHomeScreenGridname();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");

            HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(2).getmHomeScreenGridname();
            HomeActivity.menu_position = Constants.MAIN_MENU;
            HomeActivity.content_position = Constants.MAIN_CONTENT;

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentListenMain fragmentListenMain = new FragmentListenMain();
            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentListenMain);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadChannelData(String cid) {

        try {
            Log.d("selected:::", "channels act");

            HomeActivity.activity_homescreen_toolbar_logout.setVisibility(View.VISIBLE);
            HomeActivity.isPayperview = homeScreenArrayArrayList.get(0).getmHomeScreenGridname();
            Log.d("selected:::", "channels act" + homeScreenArrayArrayList.get(0).getmHomeScreenGridname());
            HomeActivity.toolbarGridContent = homeScreenArrayArrayList.get(0).getmHomeScreenGridname();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");

            HomeActivity.HomeGridClick = homeScreenArrayArrayList.get(0).getmHomeScreenGridname();
            HomeActivity.menu_position = Constants.MAIN_MENU;
            HomeActivity.content_position = Constants.MAIN_CONTENT;


       /* FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        ChannelTotalFragment channelFragment = new ChannelTotalFragment();

        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, channelFragment);


        fragmentTransaction.commit();*/

            if (getActivity() != null) {
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                ChannelTotalFragment mChannelTotalFragment = ChannelTotalFragment.newInstance("search", "" + cid);
//                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, mChannelTotalFragment);
//                fragmentTransaction.commit();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                ChannelTotalFragment mChannelTotalFragment = ChannelTotalFragment.newInstance("search", "" + cid);
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, mChannelTotalFragment);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    class HomeViewAdapter extends RecyclerView.Adapter<HomeViewAdapter.DataObjectHolder> {
        ArrayList<HomeScreenArray> homeScreenArrays;
        Context context;

        public HomeViewAdapter(ArrayList<HomeScreenArray> homeScreenArrays, Context context) {
            this.homeScreenArrays = homeScreenArrays;
            this.context = context;
            text_font_typeface();
        }

        @Override
        public HomeViewAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.activity_homescreen_griddesign, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(HomeViewAdapter.DataObjectHolder holder, int position) {
            try {
                BitmapDrawable bd = (BitmapDrawable) getActivity().getResources().getDrawable(R.drawable.list_bg_top);
                BitmapDrawable bd1 = (BitmapDrawable) getActivity().getResources().getDrawable(R.drawable.list_bg_bottom);
                int height = bd.getBitmap().getHeight();
                int height1 = bd1.getBitmap().getHeight();
                int width = bd.getBitmap().getWidth();
                int width1 = bd1.getBitmap().getWidth();

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
                holder.top_layout.setLayoutParams(lp);
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width1, height1);
                holder.bottom_layout.setLayoutParams(lp1);

                holder.activity_homescreen_grid_image.setImageResource(homeScreenArrays.get(position).getmHomescreenGridImages());
                holder.activity_homescreen_grid_text.setText(homeScreenArrays.get(position).getmHomeScreenGridname());
                holder.activity_homescreen_grid_text.setTypeface(OpenSans_Regular);
                holder.activity_homescreen_grid_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home", "");
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                        fragmentTransaction.commit();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return homeScreenArrays.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private final ImageView activity_homescreen_grid_image;
            private final TextView activity_homescreen_grid_text;
            private final LinearLayout top_layout, bottom_layout;
            private final LinearLayout activity_homescreen_grid_layout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                activity_homescreen_grid_layout = (LinearLayout) itemView.findViewById(R.id.activity_homescreen_grid_layout);
                activity_homescreen_grid_image = (ImageView) itemView.findViewById(R.id.activity_homescreen_grid_image);
                activity_homescreen_grid_text = (TextView) itemView.findViewById(R.id.activity_homescreen_grid_text);
                top_layout = (LinearLayout) itemView.findViewById(R.id.top_layout);
                bottom_layout = (LinearLayout) itemView.findViewById(R.id.bottom_layout);
            }
        }
    }

    private final BroadcastReceiver m_timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                final String action = intent.getAction();

                if (action.equals(Intent.ACTION_TIME_CHANGED) ||
                        action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                    Log.d("Date changed::", "date changed");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


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

    private void setViewFocus(View mView) {
        try {
            mView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    try {
                        if (hasFocus) {
                            // run scale animation and make it bigger
                              /*  v.setScaleX(1.1F);
                                v.setScaleY(1.1F);*/
                            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_in_tv);
                            v.startAnimation(anim);
                            anim.setFillAfter(true);
                        } else {
                            // run scale animation and make it smaller
                               /* v.setScaleX(1F);
                                v.setScaleY(1F);*/
                            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_out_tv);
                            v.startAnimation(anim);
                            anim.setFillAfter(true);
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

    private void showDialog() {
        try {
            PopupDialog dlg = new PopupDialog(getActivity());
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    (float) 30, getResources().getDisplayMetrics());
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int nWidth = displayMetrics.widthPixels;
            int nHeight = displayMetrics.heightPixels;
            dlg.showAtLocation(terms_textview, nWidth - 30, nHeight / 3 * 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateOOM() {
        int it = 20;
        for (int i = 0; i < 20; i++) {
            Log.d(":::", ":::Free mem::" + Runtime.getRuntime().freeMemory());
            int loopl = 2;
            int[] mf = new int[it];
            do {
                mf[loopl] = 0;
                loopl--;
            } while (loopl > 0);
            it = it * 5;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
