//package com.selecttvlauncher.ui.fragments;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.selecttvlauncher.R;
//import com.selecttvlauncher.service.RadioService;
//import com.selecttvlauncher.tools.Constants;
//import com.selecttvlauncher.tools.Utilities;
//import com.selecttvlauncher.ui.activities.HomeActivity;
//import com.selecttvlauncher.ui.activities.VideoPlayerActivity;
//import com.selecttvlauncher.ui.activities.WebBrowserActivity;
//import com.selecttvlauncher.ui.views.DynamicImageView;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//public class FragmentMoviesLeftContent_old extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String details;
//    private String lists;
//    private LinearLayout trailer_layout;
//
//    private DynamicImageView banner_image;
//    private TextView trailer_1, trailer_2;
//
//    private JSONArray m_jsonArrayFreeItems, m_jsonArrayPaidItems;
//    private ImageView fragment_movie_left_prev_iconn;
//    private LayoutInflater inflate;
//
//
//    // TODO: Rename and change types and number of parameters
//    public static FragmentMoviesLeftContent_old newInstance(String param1, String param2) {
//        FragmentMoviesLeftContent_old fragment = new FragmentMoviesLeftContent_old();
//        Log.d("new Instance::",":new Instance:");
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public FragmentMoviesLeftContent_old() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            details = getArguments().getString(ARG_PARAM1);
//            lists = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        final View view = inflater.inflate(R.layout.fragment_movies, container, false);
//        ((HomeActivity)getActivity()).setmFragmentMoviesLeftContent(this);
//        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        trailer_1 = (TextView) view.findViewById(R.id.trailer_1);
//        trailer_2 = (TextView) view.findViewById(R.id.trailer_2);
//        fragment_movie_left_prev_iconn = (ImageView) view.findViewById(R.id.fragment_movie_left_prev_iconn);
//        banner_image = (DynamicImageView) view.findViewById(R.id.banner_image);
//        trailer_layout=(LinearLayout)view.findViewById(R.id.trailer_layout);
//        HomeActivity.currentPageForBackButton="moviedetails";
//        try {
//
//            makeDetail();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //Utilities.setViewFocus(getActivity(),fragment_movie_left_prev_iconn);
//        Utilities.setViewFocus(getActivity(),trailer_1);
//        Utilities.setViewFocus(getActivity(),trailer_2);
//
//        fragment_movie_left_prev_iconn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goback();
//
//               /* try {
//
//                     if (HomeActivity.Selecteddetails.equals(Constants.DEATILS_SUBCONTENT)) {
//                         HomeActivity.Selecteddetails="";
//                         HomeActivity.mSeriesSeason = "";
//                         FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
//                         FragmentMoviesRightContent network_listfragment = FragmentMoviesRightContent.newInstance(HomeActivity.seasonDetails, HomeActivity.seasonLinks,"");
//                         network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_content, network_listfragment);
//                         network_fragmentTransaction.commit();
//
//
//                         if(HomeActivity.mMovieOrSerial.equalsIgnoreCase(Constants.SHOWS_DETAILS)){
//                             FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                             FragmentMoviesLeftContent fragmentMoviesLeftContent = FragmentMoviesLeftContent.newInstance(HomeActivity.seasonDetails, "");
//                             fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_list, fragmentMoviesLeftContent);
//                             fragmentTransaction.commit();
//
//                             FragmentTransaction fragmentTransactionShowSeasons = getFragmentManager().beginTransaction();
//                             FragmentShowSeasonsAndEpisodes fragmentShowSeasonsAndEpisodes = FragmentShowSeasonsAndEpisodes.newInstance(Constants.SHOWS_DETAILS, HomeActivity.showId);
//                             Log.d("SHOW ID::==>",">>>"+HomeActivity.showId);
//                             fragmentTransactionShowSeasons.replace(R.id.fragment_shows_seasonsandepisodes, fragmentShowSeasonsAndEpisodes);
//                             fragmentTransactionShowSeasons.commit();
//
//                             ((HomeActivity)getActivity()).displayapps(false, "");
//                         }
//
//
//
//
//
//                    } else {
//                        Log.d("FragmentMovieMain", "removed");
//
//                        ((HomeActivity) getActivity()).SwapMovieFragment(false);
//                        FragmentMovieMain.hideFrames();
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }*/
//            }
//        });
//        return view;
//    }
//
//    public void makeDetail() {
//        try {
//            JSONObject data_object = new JSONObject(details);
//            trailer_1.setVisibility(View.GONE);
//            trailer_2.setVisibility(View.GONE);
//
//            String strName = null;
//            String strPosterUrl = null;
//            try {
//                if (data_object.has("name")) {
//                    strName = data_object.getString("name");
//                }
//                HomeActivity.mMovieorSeriesName = strName;
//
//                if (data_object.has("description")) {
//                    String strDescription = data_object.getString("description");
//                }
//
//                if (data_object.has("poster_url")) {
//                    strPosterUrl = data_object.getString("poster_url");
//                }
//
//                if (data_object.has("rating")) {
//                    String strRating = data_object.getString("rating");
//                }
//
//                if (data_object.has("runtime")) {
//                    int nRunTime = data_object.getInt("runtime");
//                }
//                if (lists.equals("")) {
//                    if(HomeActivity.mMovieOrSerial.equalsIgnoreCase(Constants.SHOWS_DETAILS)){
//                        banner_image.loadImage(strPosterUrl);
//                    }else{
//                        Picasso.with(getContext()
//                                .getApplicationContext())
//                                .load(strPosterUrl)
//
//                                .placeholder(R.drawable.loader_movie).into(banner_image);
//                    }
//                } else {
//                    if(HomeActivity.mMovieOrSerial.equalsIgnoreCase(Constants.SHOWS_DETAILS)){
//                        banner_image.loadImage(lists);
//                    }else{
//                        Picasso.with(getContext()
//                                .getApplicationContext())
//                                .load(lists)
//
//                                .placeholder(R.drawable.loader_movie).into(banner_image);
//                    }
//
//                }
//
//                if (data_object.has("trailers")) {
//                    final JSONArray vTrailors = data_object.getJSONArray("trailers");
//                    if (vTrailors.length() > 0) {
//                        for (int i = 0; i < vTrailors.length(); i++) {
//                            final String url = vTrailors.getString(i);
//                            if (!TextUtils.isEmpty(url)) {
//                                final View itemlayout = (View) inflate.inflate(R.layout.trailor_item, null);
//                                final TextView trailer_textview=(TextView)itemlayout.findViewById(R.id.trailer_textview);
//                                trailer_textview.setTag(url);
//                                int p=i+1;
//                                trailer_textview.setText("Watch Trailer #"+p);
//                                trailer_textview.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        String tag=trailer_textview.getTag().toString();
//                                        try {
//                                            if (tag.contains("youtube") || tag.contains("Youtube")) {
//                                                Log.d("playing::", "::Youtube");
//
//                                                Intent in = new Intent(getActivity(), VideoPlayerActivity.class);
//                                                in.putExtra("url", getVideoId(tag));
//                                                startActivity(in);
//
//                                            } else {
//                                                if(!TextUtils.isEmpty(tag)){
//                                                    Log.d("playing::", "::browser");
//                                                    Intent intent = new Intent(getActivity().getApplicationContext(), WebBrowserActivity.class);
//                                                    intent.putExtra("url", tag);
//                                                    intent.putExtra("name", "");
//                                                    startActivity(intent);
//                                                }
//                                            }
//
//                                            try {
//                                                if(Utilities.isMyServiceRunning(getActivity(), RadioService.class)){
//                                                    getActivity().stopService(new Intent(getActivity(), RadioService.class));
//                                                }
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//                                trailer_layout.addView(itemlayout);
//
//
//                                /*trailer_1.setVisibility(View.VISIBLE);
//                                trailer_2.setVisibility(View.GONE);
//                                trailer_1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//
//                                        try {
//                                            if (url.contains("youtube") || url.contains("Youtube")) {
//                                                Log.d("playing::", "::Youtube");
//
//                                                Intent in = new Intent(getActivity(), VideoPlayerActivity.class);
//                                                in.putExtra("url", getVideoId(url));
//                                                startActivity(in);
//
//                                            } else {
//                                                if(!TextUtils.isEmpty(url)){
//                                                    Log.d("playing::", "::browser");
//                                                    Intent intent = new Intent(getActivity().getApplicationContext(), WebBrowserActivity.class);
//                                                    intent.putExtra("url", url);
//                                                    intent.putExtra("name", "");
//                                                    startActivity(intent);
//                                                }
//                                            }
//
//                                            try {
//                                                if(Utilities.isMyServiceRunning(getActivity(), RadioService.class)){
//                                                    getActivity().stopService(new Intent(getActivity(), RadioService.class));
//                                                }
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });*/
//                            }
//                            /*if (i == 1 && !TextUtils.isEmpty(url)) {
//                                trailer_2.setVisibility(View.VISIBLE);
//                                trailer_2.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//
//                                    }
//                                });
//                            }*/
//                        }
//                    } else {
//                        trailer_1.setVisibility(View.GONE);
//                        trailer_2.setVisibility(View.GONE);
//                    }
//                }
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//    }
//
//    private String getVideoId(String strUrl) {
//
//        String strVideoId = "";
//
//        if (strUrl.contains("v=")) {
//            String[] separated = strUrl.split("v=");
//            strVideoId = separated[separated.length - 1];
//        } else if (strUrl.contains("embed/")) {
//            String[] separated = strUrl.split("embed/");
//            strVideoId = separated[separated.length - 1];
//        }
//
//        return strVideoId;
//    }
//    public void goback(){
//        try {
//
//            if (HomeActivity.Selecteddetails.equals(Constants.DEATILS_SUBCONTENT)) {
//                HomeActivity.Selecteddetails="";
//                HomeActivity.mSeriesSeason = "";
//                if(HomeActivity.getmFragmentMovieMain()!=null){
//                    FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
//                    FragmentMoviesRightContent network_listfragment = FragmentMoviesRightContent.newInstance(HomeActivity.seasonDetails, HomeActivity.seasonLinks,"");
//                    network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_content, network_listfragment);
//                    network_fragmentTransaction.commit();
//                }
//
//
//
//                if(HomeActivity.mMovieOrSerial.equalsIgnoreCase(Constants.SHOWS_DETAILS)){
//                    if(HomeActivity.getmFragmentMovieMain()!=null){
//                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                        FragmentMoviesLeftContent_old fragmentMoviesLeftContent = FragmentMoviesLeftContent_old.newInstance(HomeActivity.seasonDetails, "");
//                        fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_list, fragmentMoviesLeftContent);
//                        fragmentTransaction.commit();
//                    }
//                    if(HomeActivity.getmFragmentMovieMain()!=null){
//                        FragmentTransaction fragmentTransactionShowSeasons = getFragmentManager().beginTransaction();
//                        FragmentShowSeasonsAndEpisodes fragmentShowSeasonsAndEpisodes = FragmentShowSeasonsAndEpisodes.newInstance(Constants.SHOWS_DETAILS, HomeActivity.showId);
//                        Log.d("SHOW ID::==>",">>>"+ HomeActivity.showId);
//                        fragmentTransactionShowSeasons.replace(R.id.fragment_shows_seasonsandepisodes, fragmentShowSeasonsAndEpisodes);
//                        fragmentTransactionShowSeasons.commit();
//                        ((HomeActivity)getActivity()).displayapps(false, "");
//                    }
//
//
//
//
//                }
//
//
//
//
//
//            } else {
//                Log.d("FragmentMovieMain", "removed");
//
//                ((HomeActivity) getActivity()).SwapMovieFragment(false);
//                FragmentMovieMain.hideFrames();
//                HomeActivity.currentPageForBackButton="ondemandList";
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
