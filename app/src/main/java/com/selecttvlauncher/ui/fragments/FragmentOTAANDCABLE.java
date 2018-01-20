package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.selecttvlauncher.Adapter.GridTempAdapter;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentOTAANDCABLE extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout layout_ota_antenna;
    private LinearLayout layout_ota_cable;
    private RelativeLayout layout_youtube_cantent, layout_channel_details;
    private FrameLayout youtube_player_layout;
    private YouTubePlayer YPlayer;
    private RelativeLayout layout_ota_content;
    private ImageView layout_ota_prev_button;

    public static String isOtaCategories = Constants.OTA_MAINCONTENT;
    public static String mOtaCategories = "";
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private TextView text_ota_cable;
    private TextView text_ota_antenna;
    private TextView text_ota_connected_how;

    private ImageView logo_imageView;
    private TextView watch_txtview, title_textView, desc_textView;
    private LinearLayout local_channel_layout;
    private RelativeLayout premium_channel_layout;
    private RecyclerView channel_list;
    private TextView get_button;
    private String selected = "";
    private ArrayList<Integer> images = new ArrayList<Integer>();
    private GridLayoutManager layoutManager;
    private Tracker mTracker;


    public static FragmentOTAANDCABLE newInstance(String param1, String param2) {
        FragmentOTAANDCABLE fragment = new FragmentOTAANDCABLE();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentOTAANDCABLE() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_otaandcable, container, false);
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        text_ota_connected_how = (TextView) view.findViewById(R.id.text_ota_connected_how);
        text_ota_antenna = (TextView) view.findViewById(R.id.text_ota_antenna);
        text_ota_cable = (TextView) view.findViewById(R.id.text_ota_cable);
        layout_ota_prev_button = (ImageView) view.findViewById(R.id.layout_ota_prev_button);
        layout_ota_antenna = (LinearLayout) view.findViewById(R.id.layout_ota_antenna);
        layout_ota_cable = (LinearLayout) view.findViewById(R.id.layout_ota_cable);
        layout_ota_content = (RelativeLayout) view.findViewById(R.id.layout_ota_content);
        layout_youtube_cantent = (RelativeLayout) view.findViewById(R.id.layout_youtube_cantent);
        layout_channel_details = (RelativeLayout) view.findViewById(R.id.layout_channel_details);
        youtube_player_layout = (FrameLayout) view.findViewById(R.id.youtube_player_layout);

        logo_imageView = (ImageView) view.findViewById(R.id.logo_imageView);
        watch_txtview = (TextView) view.findViewById(R.id.watch_txtview);
        title_textView = (TextView) view.findViewById(R.id.title_textView);
        desc_textView = (TextView) view.findViewById(R.id.desc_textView);
        local_channel_layout = (LinearLayout) view.findViewById(R.id.local_channel_layout);
        premium_channel_layout = (RelativeLayout) view.findViewById(R.id.premium_channel_layout);
        channel_list = (RecyclerView) view.findViewById(R.id.channel_list);
        get_button = (TextView) view.findViewById(R.id.get_button);

        images.add(R.drawable.ota_icon1);
        images.add(R.drawable.ota_icon2);
        images.add(R.drawable.ota_icon3);
        images.add(R.drawable.ota_icon4);
        images.add(R.drawable.ota_icon5);
        images.add(R.drawable.ota_icon6);
        images.add(R.drawable.ota_icon7);
        images.add(R.drawable.ota_icon8);
        images.add(R.drawable.ota_icon9);
        images.add(R.drawable.ota_icon10);
        images.add(R.drawable.ota_icon11);
        images.add(R.drawable.ota_icon12);
        images.add(R.drawable.ota_icon13);
        images.add(R.drawable.ota_icon14);
        images.add(R.drawable.ota_icon15);
        images.add(R.drawable.ota_icon16);
        images.add(R.drawable.ota_icon17);
        images.add(R.drawable.ota_icon18);
        images.add(R.drawable.ota_icon19);
        images.add(R.drawable.ota_icon20);
        images.add(R.drawable.ota_icon21);
        images.add(R.drawable.ota_icon22);
        images.add(R.drawable.ota_icon23);

        text_font_typeface();
        text_ota_connected_how.setTypeface(OpenSans_Regular);
        text_ota_antenna.setTypeface(OpenSans_Regular);
        text_ota_cable.setTypeface(OpenSans_Regular);

        final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_player_layout, youTubePlayerFragment).commit();

        ((HomeActivity) getActivity()).setFragmentOTAANDCABLE(this);
        Utilities.setViewFocus(getActivity(), layout_ota_prev_button);
        Utilities.setViewFocus(getActivity(), layout_ota_antenna);
        Utilities.setViewFocus(getActivity(), layout_ota_cable);

        layoutManager = new GridLayoutManager(getActivity(), 6);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        channel_list.hasFixedSize();
        channel_list.setLayoutManager(layoutManager);
        int spanCount = 6; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        channel_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        GridTempAdapter adpater = new GridTempAdapter(images, getContext());
        channel_list.setAdapter(adpater);

        watch_txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayYoutube();
                isOtaCategories = Constants.OTA_YOUTUBECONTENT;
                if (selected.equalsIgnoreCase("local")) {
                    youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                            if (!b) {


                                YPlayer = youTubePlayer;
                                YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                                YPlayer.loadVideo("1KzYpvXrLL4");
                                YPlayer.play();
                            }
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if (selected.equalsIgnoreCase("premium")) {
                    youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                            if (!b) {


                                YPlayer = youTubePlayer;
                                YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                                YPlayer.loadVideo("ZM0j2ZSLm1w");
                                YPlayer.play();
                            }
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                            // TODO Auto-generated method stub
                        }
                    });
                }
            }
        });


        layout_ota_prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOtaCategories.equals(Constants.OTA_SUBCONTENT)) {
                    isOtaCategories = Constants.OTA_MAINCONTENT;
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
                    setAnalyticreport(HomeActivity.toolbarGridContent, "", "", "");
                    displayhome();
                    if (YPlayer != null) {
                        YPlayer.release();
                    }
                } else if (isOtaCategories.equals(Constants.OTA_YOUTUBECONTENT)) {
                    isOtaCategories = Constants.OTA_SUBCONTENT;
                    if (selected.equalsIgnoreCase("local")) {
                        displayLocalChannels();
                    } else if (selected.equalsIgnoreCase("premium")) {
                        displayPremiumChannels();
                    }
                    if (YPlayer != null) {
                        YPlayer.release();
                    }
                } else {
                    HomeActivity.isPayperview = "";
                    ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                    fragmentTransaction.commit();
                }

            }
        });
        get_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected.equalsIgnoreCase("local")) {
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.local_channel_url)));
                        if (myIntent != null) {
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (selected.equalsIgnoreCase("premium")) {
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.premium_channel_url)));
                        if (myIntent != null) {
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        layout_ota_antenna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* mOtaCategories="Local HD Channels";
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, mOtaCategories, "","");
                setAnalyticreport(HomeActivity.toolbarGridContent, mOtaCategories, "","");
                isOtaCategories=Constants.OTA_SUBCONTENT;
                selected="local";

                displayLocalChannels();*/
                try {

                  /*  if(checkIsTV()){*/
                    try {

                            /*without EON TV*/
//                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.local_channel_url)));
//                        if (myIntent != null) {
//                            startActivity(myIntent);
//                        } else {
//                            Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
//                        }

                           /*with EON TV*/
                        if (isInstalledPackageName("com.pineone.sb")) {

/*
                        Intent myIntent = new Intent("com.pineone.sb");
                        if (myIntent != null) {
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                        }*/


                            Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
                            intentToResolve.addCategory(Intent.CATEGORY_LAUNCHER);
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /*}else{
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.local_channel_url)));
                        if (myIntent != null) {
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                        }
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        layout_ota_cable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOtaCategories = "Premium Channels";
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, mOtaCategories, "", "");
                setAnalyticreport(HomeActivity.toolbarGridContent, mOtaCategories, "", "");
                isOtaCategories = Constants.OTA_SUBCONTENT;
                selected = "premium";
                displayPremiumChannels();
               /* youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                        if (!b) {


                            YPlayer = youTubePlayer;
                            YPlayer.loadVideo("Nvtk0QAEuog");
                            YPlayer.play();


                        }
                    }


                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                        // TODO Auto-generated method stub

                    }
                });*/
            }
        });
        return view;
    }

    private void displayLocalChannels() {
        layout_youtube_cantent.setVisibility(View.GONE);
        layout_ota_content.setVisibility(View.GONE);
        text_ota_connected_how.setVisibility(View.GONE);
        layout_channel_details.setVisibility(View.VISIBLE);

        logo_imageView.setImageResource(R.drawable.wifi);
        title_textView.setText(R.string.local_channel_title);
        desc_textView.setText(R.string.local_channel_desc);
        local_channel_layout.setVisibility(View.VISIBLE);
        premium_channel_layout.setVisibility(View.GONE);
        get_button.setText("GET IT NOW");
    }

    private void displayPremiumChannels() {
        layout_youtube_cantent.setVisibility(View.GONE);
        layout_ota_content.setVisibility(View.GONE);
        text_ota_connected_how.setVisibility(View.GONE);
        layout_channel_details.setVisibility(View.VISIBLE);

        logo_imageView.setImageResource(R.drawable.sling_new);
        title_textView.setText(R.string.premium_channel_title);
        desc_textView.setText(R.string.premium_channel_desc);
        local_channel_layout.setVisibility(View.GONE);
        premium_channel_layout.setVisibility(View.VISIBLE);
        get_button.setText("START FREE TRIAL");


    }

    private void displayYoutube() {
        layout_youtube_cantent.setVisibility(View.VISIBLE);
        layout_ota_content.setVisibility(View.GONE);
        text_ota_connected_how.setVisibility(View.GONE);
        layout_channel_details.setVisibility(View.GONE);
    }

    private void displayhome() {
        layout_youtube_cantent.setVisibility(View.GONE);
        layout_ota_content.setVisibility(View.GONE);
        text_ota_connected_how.setVisibility(View.GONE);
        layout_channel_details.setVisibility(View.GONE);
        layout_ota_content.setVisibility(View.VISIBLE);
        text_ota_connected_how.setVisibility(View.VISIBLE);
    }


    public void mOtaToolbarClick() {
        isOtaCategories = Constants.OTA_MAINCONTENT;
        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
        setAnalyticreport(HomeActivity.toolbarGridContent, "", "", "");
        displayhome();
        if (YPlayer != null) {
            YPlayer.release();
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (YPlayer != null) {
                YPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void setAnalyticreport(String main, String mainString, String SubString1, String SubString2) {
        try {
            if (TextUtils.isEmpty(mainString)) {
                Utilities.setAnalytics(mTracker, main);
            } else if (TextUtils.isEmpty(SubString1)) {
                Utilities.setAnalytics(mTracker, main + "-" + mainString);
            } else if (TextUtils.isEmpty(SubString2)) {
                Utilities.setAnalytics(mTracker, main + "-" + mainString + "-" + SubString1);
            } else {
                Utilities.setAnalytics(mTracker, main + "-" + mainString + "-" + SubString1 + "-" + SubString2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    boolean isInstalledPackageName(String packagename) {
        if (packagename.toLowerCase().equals("google play")) return true;
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
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

    private boolean checkIsTV() {

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
            return screenInches > 12.0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
