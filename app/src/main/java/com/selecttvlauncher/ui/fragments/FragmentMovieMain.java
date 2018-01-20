package com.selecttvlauncher.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.ui.activities.HomeActivity;

import org.json.JSONObject;

public class FragmentMovieMain extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String type;
    private int id;

    private JSONObject m_jsonDetail=new JSONObject();
    private JSONObject m_jsonLinks=new JSONObject();
    public static ScrollView deatilspage_scrollview;
    public static FrameLayout fragment_tvshow_bydecade_list;
    public static ProgressBar progressBar_center;
    private static LinearLayout fragment_layout;
    private FrameLayout fragment_shows_appList;


    // TODO: Rename and change types and number of parameters
    public static FragmentMovieMain newInstance(String param1, int param2) {
        FragmentMovieMain fragment = new FragmentMovieMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMovieMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
            id = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_movie_main, container, false);
        HomeActivity.seasonDetails="";
        HomeActivity.seasonLinks="";
        ((HomeActivity) getActivity()).setmFragmentMovieMain(this);
        progressBar_center=(ProgressBar)view.findViewById(R.id.progressBar_center);
        deatilspage_scrollview = (ScrollView) view.findViewById(R.id.deatilspage_scrollview);
        fragment_tvshow_bydecade_list = (FrameLayout) view.findViewById(R.id.fragment_tvshow_bydecade_list);
        fragment_shows_appList = (FrameLayout) view.findViewById(R.id.fragment_shows_appList);
        fragment_layout=(LinearLayout)view.findViewById(R.id.fragment_layout);
//        ((HomeActivity) getActivity()).toolbarTextChange("FragmentTvShows");

        if(type.equalsIgnoreCase(Constants.SHOWS_DETAILS))
        {
            HomeActivity.mMovieOrSerial= Constants.SHOWS_DETAILS;
        }else
        {
            HomeActivity.mMovieOrSerial="";
        }

        try {
            fragment_layout.setVisibility(View.GONE);
            HomeActivity.showId = id;
            Fragment pane1 = getFragmentManager().findFragmentById(R.id.fragment_tvshow_bydecade_list);
            new LoadingDetailTask().execute();





        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

    }


    @Override
    public void onDetach() {
        super.onDetach();
        try {
            ((HomeActivity) getActivity()).setmFragmentMovieMain(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayApps(boolean display, String data) {
        try {
            if(display){
                if(HomeActivity.getmFragmentMovieMain()!=null){
                    fragment_shows_appList.setVisibility(View.VISIBLE);
                    FragmentTransaction fragmentTransactionShowSeasons = getChildFragmentManager().beginTransaction();
                    MoviesAppListing mMoviesAppListing= MoviesAppListing.newInstance(Constants.SHOWS_DETAILS, data);
                    Log.d("SHOW ID::==>",">>>show:"+id);
                    Log.d("SHOW ID::==>",">>>linkData:"+data);
                    fragmentTransactionShowSeasons.replace(R.id.fragment_shows_appList, mMoviesAppListing);
                    fragmentTransactionShowSeasons.commit();
                }

            }else{
                fragment_shows_appList.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoadingDetailTask extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            try {
                progressBar_center.setVisibility(View.VISIBLE);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                int nIndex = 1;

                nIndex = id;
                if (type.equalsIgnoreCase(Constants.MOVIE_DETAILS)) {
                    m_jsonDetail = JSONRPCAPI.getMovieDetail(nIndex, HomeActivity.mfreeorall);
                    if (m_jsonDetail == null) return null;
                    Log.d("m_jsonmovieDetail ::", "::" + m_jsonDetail);
                    m_jsonLinks = JSONRPCAPI.getMovieLinks(nIndex);
                    if (m_jsonLinks == null) return null;
                    Log.d("m_jsonmovieLinks::", "::" + m_jsonLinks);
                } else {
                    m_jsonDetail = JSONRPCAPI.getShowDetail(nIndex);
                    if (m_jsonDetail == null) return null;
                    Log.d("m_jsonShowDetail ::", "::" + m_jsonDetail);
                    m_jsonLinks = JSONRPCAPI.getShowLinks(nIndex, 0, 0);
                    if (m_jsonLinks == null) return null;
                    Log.d("m_jsonShowLinks::", "::" + m_jsonLinks);
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
                fragment_layout.setVisibility(View.VISIBLE);
                FragmentTransaction network_fragmentTransaction = null;
                try {
                    if(HomeActivity.getmFragmentMovieMain()!=null){
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        FragmentMoviesLeftContent fragmentMoviesLeftContent = FragmentMoviesLeftContent.newInstance(m_jsonDetail.toString(), "");
                        fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_list, fragmentMoviesLeftContent);
                        fragmentTransaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String linkData="";
                if(m_jsonLinks!=null){
                    linkData=m_jsonLinks.toString();
                }else{
                    linkData="";
                }
                try {
                    if(HomeActivity.getmFragmentMovieMain()!=null){
                        network_fragmentTransaction = getChildFragmentManager().beginTransaction();
                        FragmentMoviesRightContent network_listfragment = FragmentMoviesRightContent.newInstance(m_jsonDetail.toString(), linkData,"");
                        network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_content, network_listfragment);
                        network_fragmentTransaction.commit();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    HomeActivity.seasonDetails=m_jsonDetail.toString();
                    HomeActivity.seasonLinks=m_jsonLinks.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if(type.equalsIgnoreCase(Constants.SHOWS_DETAILS)){
                    HomeActivity.mMovieOrSerial= Constants.SHOWS_DETAILS;
                    if(HomeActivity.getmFragmentMovieMain()!=null){
                        FragmentTransaction fragmentTransactionShowSeasons = getChildFragmentManager().beginTransaction();
                        FragmentShowSeasonsAndEpisodes fragmentShowSeasonsAndEpisodes = FragmentShowSeasonsAndEpisodes.newInstance(Constants.SHOWS_DETAILS, id);
                        Log.d("SHOW ID::==>",">>>show:"+id);
                        fragmentTransactionShowSeasons.replace(R.id.fragment_shows_seasonsandepisodes, fragmentShowSeasonsAndEpisodes);
                        fragmentTransactionShowSeasons.commit();
                    }


                }else {
                    HomeActivity.mMovieOrSerial="";
                }







            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();

        /*Log.d("FragmentMovieMain","resume");
        deatilspage_scrollview.fullScroll(View.FOCUS_UP);*/

    }


    public static void disableScroll()
    {
        try {
            deatilspage_scrollview.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void scrollViewUp(){
        try {
            deatilspage_scrollview.post(new Runnable() {
                public void run() {
                    deatilspage_scrollview.scrollTo(0, 0);
//                    deatilspage_scrollview.fullScroll(View.FOCUS_UP);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void mProgressBar(Boolean aBoolean)
    {
        if(aBoolean)
        {
            progressBar_center.setVisibility(View.VISIBLE);
            fragment_layout.setVisibility(View.GONE);
//            deatilspage_scrollview.scrollTo(0, 0);
                   }else {
            scrollViewUp();
            progressBar_center.setVisibility(View.GONE);
            fragment_layout.setVisibility(View.VISIBLE);
//            deatilspage_scrollview.scrollTo(0, 0);
        }
    }

    public static void hideFrames(){
        try {
            if(fragment_layout!=null){
                fragment_layout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
