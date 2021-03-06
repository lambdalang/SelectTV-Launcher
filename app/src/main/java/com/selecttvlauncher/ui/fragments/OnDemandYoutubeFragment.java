package com.selecttvlauncher.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.gms.analytics.Tracker;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;


public class OnDemandYoutubeFragment extends Fragment implements YouTubePlayer.PlayerStateChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private YouTubePlayer YPlayer;
    private Tracker mTracker;

    private OndemandyoutubeFragmentInteractionListener mListener;
    private WebView demand_webview_layout;
    private String data;
    private int width;
    private int height;

    public OnDemandYoutubeFragment() {
        // Required empty public constructor
    }

    private Button layout_prev_button;


    public static OnDemandYoutubeFragment newInstance(String param1, String param2) {
        OnDemandYoutubeFragment fragment = new OnDemandYoutubeFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_on_demand_youtube, container, false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        /*height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;*/
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        Utilities.setAnalytics(mTracker,"AppManager-Youtube");
        layout_prev_button = (Button) view.findViewById(R.id.layout_prev_button);

        Log.d(":video id::",":::"+PreferenceManager.getwelcome_video(getActivity()));
        //demand_webview_layout=(WebView)view.findViewById(R.id.demand_webview_layout);


//        demand_webview_layout.setLayoutParams(new RelativeLayout.LayoutParams(width, height));


        /*try {
            ViewTreeObserver vto = demand_webview_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("SetJavaScriptEnabled")
                @Override
                public void onGlobalLayout() {

                    width = demand_webview_layout.getMeasuredWidth();
                    height = demand_webview_layout.getMeasuredHeight();




                    data= "<Center><iframe align='center' width='100%' height='100%' src='https://www.youtube.com/embed/videoseries?list="+PreferenceManager.getwelcome_video(getActivity())+"&rel=0&autoplay=1 frameborder='0' allowfullscreen></iframe><Center>";
//                    data= "<iframe align='center' width='100%' height='100%' src='https://www.youtube.com/embed/videoseries?list=PLNT1r49jsn3niUNZcxO1Vyj86oxuxmi7R&rel=0&autoplay=1' frameborder='0' allowfullscreen></iframe>";
//                    data= "<iframe src='http://www.youtube.com/embed/HSlD79sqHp4?autoplay=1' align='center' frameborder='0' width='100%' height='100%'></iframe>";
                    demand_webview_layout.setInitialScale(1);
                    demand_webview_layout.getSettings().setJavaScriptEnabled(true);
                    demand_webview_layout.getSettings().setLoadWithOverviewMode(true);
                    demand_webview_layout.getSettings().setUseWideViewPort(true);
                    demand_webview_layout.loadData(data, "text/html; charset=UTF-8;", null);
                    demand_webview_layout.setBackgroundColor(getResources().getColor(R.color.transparent));
                    demand_webview_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/


        ((HomeActivity) getActivity()).toolbarTextChange("App Manager", "", "", "");




        final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.demand_youtube_player_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                try {
                    if (!b) {
                        YPlayer = youTubePlayer;
                        YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                        YPlayer.loadPlaylist("PLNT1r49jsn3kMgZo6GRcBbMIz6NHiZM_9");
                        //YPlayer.loadVideo("K6AfSckWSZA");
                        YPlayer.play();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                // TODO Auto-generated method stub
                Log.d("youtube::::",":::failed");

            }
        });
        layout_prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    AppDownloadFragment mfragmentOnDemandAll = new AppDownloadFragment();
                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, mfragmentOnDemandAll);
                    fragmentTransaction.commit();
                    /*if (YPlayer != null) {
                        YPlayer.release();
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OndemandyoutubeFragmentInteractionListener) {
            mListener = (OndemandyoutubeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLoading() {
        Log.d("youtube::::",":::loading");
    }

    @Override
    public void onLoaded(String s) {
        Log.d("youtube::::",":::loaded");
    }

    @Override
    public void onAdStarted() {
        Log.d("youtube::::",":::addstarted");
    }

    @Override
    public void onVideoStarted() {
        Log.d("youtube::::",":::videostarted");
    }

    @Override
    public void onVideoEnded() {
        Log.d("youtube::::",":::videoended");
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        Log.d("youtube::::",":::error");
        String de=errorReason.toString();
        String dde=errorReason.toString();

    }


    public interface OndemandyoutubeFragmentInteractionListener {
        // TODO: Update argument type and name
        void ondemandyoutubeFragmentInteraction(Uri uri);
    }


    @Override
    public void onPause() {
        super.onPause();

       /* try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(demand_webview_layout, (Object[]) null);

        } catch(Exception cnfe) {
            cnfe.printStackTrace();

        }*/
    }


}
