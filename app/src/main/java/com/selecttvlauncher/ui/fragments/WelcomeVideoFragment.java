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
import com.selecttvlauncher.R;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.ui.activities.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WelcomeVideoFragment.WelcomeVideoFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WelcomeVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeVideoFragment extends Fragment implements YouTubePlayer.PlayerStateChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private WelcomeVideoFragmentInteractionListener mListener;

    public WelcomeVideoFragment() {
        // Required empty public constructor
    }
    private YouTubePlayer YPlayer;
    private Tracker mTracker;

    private WebView demand_webview_layout;
    private String data;
    private int width;
    private int height;


    private Button layout_prev_button;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WelcomeVideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WelcomeVideoFragment newInstance(String param1, String param2) {
        WelcomeVideoFragment fragment = new WelcomeVideoFragment();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_welcome_video, container, false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        /*height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;*/
//        mTracker = LauncherApplication.getInstance().getDefaultTracker();
//        Utilities.setAnalytics(mTracker,"AppManager-Youtube");
        layout_prev_button = (Button) view.findViewById(R.id.layout_prev_button);

        ((HomeActivity) getActivity()).toolbarTextChange("Welcome Screen", "", "", "");




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
                        YPlayer.loadPlaylist("PLNT1r49jsn3ke-nQ3NRSTnFCi5AlMkpyB");
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
                    ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentHomeScreenGrid   fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return view;

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
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }


    public interface WelcomeVideoFragmentInteractionListener {
        // TODO: Update argument type and name
        void welcomeVideoFragmentInteractionListener(Uri uri);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.welcomeVideoFragmentInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WelcomeVideoFragmentInteractionListener) {
            mListener = (WelcomeVideoFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
