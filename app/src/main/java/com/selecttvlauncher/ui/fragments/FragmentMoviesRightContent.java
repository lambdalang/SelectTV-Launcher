package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.selecttvlauncher.BeanClass.AppFormatBean;
import com.selecttvlauncher.BeanClass.SeasonsBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.activities.LoginActivity;
import com.selecttvlauncher.ui.views.AutofitRecylerview;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentMoviesRightContent extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String details;
    private String list;
    ArrayList<String> moviecastarray = new ArrayList<>();
  //  private RecyclerView maovie_rentnow_recyclerview;
    private LinearLayout movies_button_tab_layout, format_layout;
    private GridLayoutManager mLayoutManager;
    private TabLayout tab_layout;
    private LinearLayout layout_movie_overview;
    private LinearLayout layout_movie_cast;
    private LinearLayout layout_movie_genre;
    private View movie_overview_bottom;
    private View movie_overview_left;
    private View movie_overview_top;
    private View movie_cast_left;
    private View movie_cast_top;
    private View movie_genre_left;
    private View movie_cast_bottom;
    private View movie_genre_top;
    private View movie_genre_bottom;
    private View movie_genre_right;
    private RecyclerView movie_cast_details, movie_genre_details;
    MovieCastAndGenreAdapter movieCastAndGenreAdapter;
    private TextView free_textView,movie_overview_details, show_title, rating_text, runtime,network_text,  movies_rent_now_button,favorite, network,runtime_text, overview_title, cast_title, genre_title;
    private boolean isMovieRent = false;
public static TextView movies_watch_free_button;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private String name;

    private TextView sd_textView, hd_textView, hdx_textView,buy_textView;

 /*   private ArrayList<AppFormatBean> free_sd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_hd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_hdx_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_other = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_sd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_hd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_hdx_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_other = new ArrayList<>();
    private String selected_app_format = "free";*/
    LinearLayout layout_shows_castand_overvew_details;
    private LinearLayout showsandepisode_container_layout;


    private View episode_free_left;
    private View episode_free_top;
    private TextView episode_free;
    private View episode_free_bottom;
    private View episode_showall_left;
    private View episode_showall_top;
    private TextView episode_showall,show_more_textview;
    private View episode_showall_bottom;
    private View movie_showall_right;
    private RecyclerView episode_free_details;
    private RecyclerView episode_showall_details;
    private String image_url;
    private int mEpisodeId;

    private JSONArray m_jsonArrayFreeItems, m_jsonArrayiosFreeItems, m_jsonArraywebFreeItems;
    private JSONArray m_jsonArrayPaidItems, m_jsonArrayiosPaidItems, m_jsonArraywebPaidItems;

    private ArrayList<AppFormatBean> free_sd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_web_sd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_ios_sd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_hd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_web_hd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_ios_hd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_hdx_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_web_hdx_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_ios_hdx_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_other = new ArrayList<>();
    private ArrayList<AppFormatBean> free_ios_other = new ArrayList<>();
    private ArrayList<AppFormatBean> free_web_other = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_sd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_ios_sd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_web_sd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_hd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_ios_hd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_web_hd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_hdx_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_ios_hdx_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_web_hdx_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_other = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_ios_other = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_web_other = new ArrayList<>();
    private String selected_app_format = "android";
    View android_left,
            android__top,
            android__bottom,
            web_left,
            web_top,
            web_bottom, ios_left,
            ios_top,
            ios_bottom,
            ios_right;

    AutofitRecylerview android_details,
            ios_details,
            web_details;
    private LinearLayout layout_payment_details, layout_ios_genre, layout_android_overview, web_cast;
    TextView android__title,
            ios_title,
            web_title;

    private boolean isswitchselected=false;
    private ImageView switch_image,apple_imageView,android_imageView,desktop_imageView;
    private AQuery aq;
    int movie_id;

    // TODO: Rename and change types and number of parameters
    public static FragmentMoviesRightContent newInstance(String param1, String param2, String param3) {
        FragmentMoviesRightContent fragment = new FragmentMoviesRightContent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMoviesRightContent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            details = getArguments().getString(ARG_PARAM1);
            list = getArguments().getString(ARG_PARAM2);
            name = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment_movies_right_content, container, false);
        aq=new AQuery(getActivity());


        try {
            layout_shows_castand_overvew_details = (LinearLayout) view.findViewById(R.id.layout_shows_castand_overvew_details);
            showsandepisode_container_layout = (LinearLayout) view.findViewById(R.id.showsandepisode_container_layout);
            movies_button_tab_layout = (LinearLayout) view.findViewById(R.id.movies_button_tab_layout);
            format_layout = (LinearLayout) view.findViewById(R.id.format_layout);
            movies_rent_now_button = (TextView) view.findViewById(R.id.movies_rent_now_button);
            favorite = (TextView) view.findViewById(R.id.favorite);
            movie_overview_details = (TextView) view.findViewById(R.id.movie_overview_details);
            show_title = (TextView) view.findViewById(R.id.show_title);
            rating_text = (TextView) view.findViewById(R.id.rating_text);
            runtime = (TextView) view.findViewById(R.id.runtime);
            network = (TextView) view.findViewById(R.id.network);
            movies_watch_free_button = (TextView) view.findViewById(R.id.movies_watch_free_button);
            movies_rent_now_button = (TextView) view.findViewById(R.id.movies_rent_now_button);
            overview_title = (TextView) view.findViewById(R.id.overview_title);
            cast_title = (TextView) view.findViewById(R.id.cast_title);
            runtime_text = (TextView) view.findViewById(R.id.runtime_text);
            network_text = (TextView) view.findViewById(R.id.network_text);
            genre_title = (TextView) view.findViewById(R.id.genre_title);
           // maovie_rentnow_recyclerview = (RecyclerView) view.findViewById(R.id.maovie_rentnow_recyclerview);
            sd_textView = (TextView) view.findViewById(R.id.sd_textView);
            hd_textView = (TextView) view.findViewById(R.id.hd_textView);
            hdx_textView = (TextView) view.findViewById(R.id.hdx_textView);
            episode_free = (TextView) view.findViewById(R.id.episode_free);
            episode_showall = (TextView) view.findViewById(R.id.episode_showall);
            show_more_textview = (TextView) view.findViewById(R.id.show_more_textview);
            free_textView = (TextView) view.findViewById(R.id.free_textView);
            buy_textView = (TextView) view.findViewById(R.id.buy_textView);

            layout_payment_details = (LinearLayout) view.findViewById(R.id.layout_payment_details);
           /* layout_android_overview = (LinearLayout) view.findViewById(R.id.layout_android_overview);
            android__title = (TextView) view.findViewById(R.id.android__title);
            ios_title = (TextView) view.findViewById(R.id.ios_title);
            web_title = (TextView) view.findViewById(R.id.web_title);
            layout_ios_genre = (LinearLayout) view.findViewById(R.id.layout_ios_genre);
            web_cast = (LinearLayout) view.findViewById(R.id.web_cast);*/
          /*  android_left = (View) view.findViewById(R.id.android_left);
            android__top = (View) view.findViewById(R.id.android__top);
            android__bottom = (View) view.findViewById(R.id.android__bottom);
            web_left = (View) view.findViewById(R.id.web_left);
            web_top = (View) view.findViewById(R.id.web_top);
            web_bottom = (View) view.findViewById(R.id.web_bottom);
            ios_left = (View) view.findViewById(R.id.ios_left);
            ios_top = (View) view.findViewById(R.id.ios_top);
            ios_bottom = (View) view.findViewById(R.id.ios_bottom);
            ios_right = (View) view.findViewById(R.id.ios_right);*/
            android_details = (AutofitRecylerview) view.findViewById(R.id.android_details);
            ios_details = (AutofitRecylerview) view.findViewById(R.id.ios_details);
            web_details = (AutofitRecylerview) view.findViewById(R.id.web_details);

            switch_image=(ImageView)view.findViewById(R.id.switch_image);
            android_imageView=(ImageView)view.findViewById(R.id.android_imageView);
            desktop_imageView=(ImageView)view.findViewById(R.id.desktop_imageView);
            apple_imageView=(ImageView)view.findViewById(R.id.apple_imageView);


            text_font_typeface();

            show_title.setTypeface(OpenSans_Bold);
            rating_text.setTypeface(OpenSans_Regular);
            movies_watch_free_button.setTypeface(OpenSans_Regular);
            movies_rent_now_button.setTypeface(OpenSans_Regular);
            runtime_text.setTypeface(OpenSans_Regular);
            network_text.setTypeface(OpenSans_Regular);
            runtime.setTypeface(OpenSans_Bold);
            network.setTypeface(OpenSans_Bold);

            overview_title.setTypeface(OpenSans_Bold);
            cast_title.setTypeface(OpenSans_Regular);
            genre_title.setTypeface(OpenSans_Regular);
            movie_overview_details.setTypeface(OpenSans_Regular);


            layout_movie_overview = (LinearLayout) view.findViewById(R.id.layout_movie_overview);

            layout_movie_cast = (LinearLayout) view.findViewById(R.id.layout_movie_cast);
            layout_movie_genre = (LinearLayout) view.findViewById(R.id.layout_movie_genre);

            Utilities.setViewFocus(getActivity(), movies_watch_free_button);
            Utilities.setViewFocus(getActivity(), movies_rent_now_button);
            Utilities.setTextFocus(getActivity(), layout_movie_overview);
            Utilities.setTextFocus(getActivity(), layout_movie_cast);
            Utilities.setTextFocus(getActivity(), layout_movie_genre);

            movie_overview_left = (View) view.findViewById(R.id.movie_overview_left);
            movie_overview_top = (View) view.findViewById(R.id.movie_overview_top);
            movie_overview_bottom = (View) view.findViewById(R.id.movie_overview_bottom);
            movie_cast_left = (View) view.findViewById(R.id.movie_cast_left);
            movie_cast_top = (View) view.findViewById(R.id.movie_cast_top);
            movie_cast_bottom = (View) view.findViewById(R.id.movie_cast_bottom);
            movie_genre_left = (View) view.findViewById(R.id.movie_genre_left);
            movie_genre_top = (View) view.findViewById(R.id.movie_genre_top);
            movie_genre_bottom = (View) view.findViewById(R.id.movie_genre_bottom);
            movie_genre_right = (View) view.findViewById(R.id.movie_genre_right);

            /*mLayoutManager = new GridLayoutManager(getActivity(), 4);
            android_details.setLayoutManager(mLayoutManager);

            mLayoutManager = new GridLayoutManager(getActivity(), 4);
            web_details.setLayoutManager(mLayoutManager);
            mLayoutManager = new GridLayoutManager(getActivity(), 4);
            ios_details.setLayoutManager(mLayoutManager);*/
            Utilities.setViewFocus(getActivity(), android_details);
            Utilities.setViewFocus(getActivity(), ios_details);
            Utilities.setViewFocus(getActivity(), web_details);

          /*  android__title.setTypeface(OpenSans_Bold);
            ios_title.setTypeface(OpenSans_Regular);
            web_title.setTypeface(OpenSans_Regular);*/


       /*     episode_free_left = (View) view.findViewById(R.id.episode_free_left);
            episode_free_top = (View) view.findViewById(R.id.episode_free_top);
            episode_free_bottom = (View) view.findViewById(R.id.episode_free_bottom);
            episode_showall_left = (View) view.findViewById(R.id.episode_showall_left);
            episode_showall_top = (View) view.findViewById(R.id.episode_showall_top);
            episode_showall_bottom = (View) view.findViewById(R.id.episode_showall_bottom);
            movie_showall_right = (View) view.findViewById(R.id.movie_showall_right);
            episode_free_details = (RecyclerView) view.findViewById(R.id.episode_free_details);
            episode_showall_details = (RecyclerView) view.findViewById(R.id.episode_showall_details);
*/

            layout_shows_castand_overvew_details.setVisibility(View.VISIBLE);

//            showsandepisode_container_layout.setVisibility(View.GONE);

            movie_cast_details = (RecyclerView) view.findViewById(R.id.movie_cast_details);
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            movie_cast_details.setLayoutManager(mLayoutManager);

            movie_genre_details = (RecyclerView) view.findViewById(R.id.movie_genre_details);
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            movie_genre_details.setLayoutManager(mLayoutManager);
            show_more_textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (show_more_textview.getText().equals("less..")) {
                        movie_overview_details.setMaxLines(1);
                        show_more_textview.setText("more..");
                    } else {
                        movie_overview_details.setMaxLines(100);
                        show_more_textview.setText("less..");
                    }

                }
            });

          /*  episode_showall_details.setVisibility(View.GONE);
            episode_free_details.setVisibility(View.VISIBLE);
            mLayoutManager = new GridLayoutManager(getActivity(), 4);
            episode_free_details.setLayoutManager(mLayoutManager);
            int spanCount = 4; // 3 columns
            int spacing = 25; // 50px
            boolean includeEdge = true;
            episode_free_details.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            mLayoutManager = new GridLayoutManager(getActivity(), 4);
            episode_showall_details.setLayoutManager(mLayoutManager);
            episode_showall_details.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/


            moviecastarray.add("name");
            moviecastarray.add("name");
            moviecastarray.add("name");
            moviecastarray.add("name");
            moviecastarray.add("name");
            moviecastarray.add("name");
            moviecastarray.add("name");
            moviecastarray.add("name");
            moviecastarray.add("name");
            moviecastarray.add("name");

            mLayoutManager = new GridLayoutManager(getActivity(), 4);
           // maovie_rentnow_recyclerview.setLayoutManager(mLayoutManager);


            makeDetail();
            makelistings();

            if (HomeActivity.isPayperview.equals("Pay Per View") || HomeActivity.isPayperview.equals("Subscriptions")) {
                movies_watch_free_button.setVisibility(View.VISIBLE);
            } else {
                movies_watch_free_button.setVisibility(View.VISIBLE);
            }


            if (HomeActivity.mMovieOrSerial.equalsIgnoreCase(Constants.SHOWS_DETAILS)) {
                movies_watch_free_button.setVisibility(View.GONE);
                movies_watch_free_button.setText("Watch Now");
                movies_rent_now_button.setVisibility(View.GONE);
            } else {
                movies_watch_free_button.setText("Watch Now");
                if(HomeActivity.toolbarGridContent.equalsIgnoreCase("On-Demand")){
                    movies_rent_now_button.setVisibility(View.GONE);
                }else{
                    movies_rent_now_button.setVisibility(View.GONE);
                }

            }

           /* layout_android_overview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    selected_app_format = "android";
                    android_left.setVisibility(View.VISIBLE);
                    android__top.setVisibility(View.VISIBLE);
                    web_left.setVisibility(View.VISIBLE);
                    ios_bottom.setVisibility(View.VISIBLE);
                    web_bottom.setVisibility(View.VISIBLE);
                    android__bottom.setVisibility(View.GONE);
                    web_top.setVisibility(View.GONE);
                    ios_left.setVisibility(View.GONE);
                    ios_top.setVisibility(View.GONE);
                    ios_right.setVisibility(View.GONE);
                    android_details.setVisibility(View.VISIBLE);
                    ios_details.setVisibility(View.GONE);
                    web_details.setVisibility(View.GONE);

                    android__title.setTypeface(OpenSans_Bold);
                    ios_title.setTypeface(OpenSans_Regular);
                    web_title.setTypeface(OpenSans_Regular);
                    if (paid_sd_list.size() > 0) {
                        sd_textView.setVisibility(View.VISIBLE);
                    } else {
                        sd_textView.setVisibility(View.GONE);
                    }

                    if (paid_hd_list.size() > 0) {
                        hd_textView.setVisibility(View.VISIBLE);
                    } else {
                        hd_textView.setVisibility(View.GONE);
                    }

                    if (paid_hdx_list.size() > 0) {
                        hdx_textView.setVisibility(View.VISIBLE);
                    } else {
                        hdx_textView.setVisibility(View.GONE);
                    }


                    if (paid_sd_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        sd_textView.setTypeface(OpenSans_Bold);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hd_textView.setTypeface(OpenSans_Regular);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hdx_textView.setTypeface(OpenSans_Regular);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_sd_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_hd_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        sd_textView.setTypeface(OpenSans_Regular);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        hd_textView.setTypeface(OpenSans_Bold);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hdx_textView.setTypeface(OpenSans_Regular);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hd_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_hdx_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        sd_textView.setTypeface(OpenSans_Regular);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hd_textView.setTypeface(OpenSans_Regular);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        hdx_textView.setTypeface(OpenSans_Bold);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hdx_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_other.size() > 0) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_other);
                        android_details.setAdapter(moviePaymentAdapter);
                    }


                }
            });


            web_cast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selected_app_format = "web";
                    android__bottom.setVisibility(View.VISIBLE);
                    web_left.setVisibility(View.VISIBLE);
                    web_top.setVisibility(View.VISIBLE);
                    ios_left.setVisibility(View.VISIBLE);
                    ios_bottom.setVisibility(View.VISIBLE);

                    android_left.setVisibility(View.GONE);
                    android__top.setVisibility(View.GONE);
                    ios_top.setVisibility(View.GONE);
                    web_bottom.setVisibility(View.GONE);
                    ios_right.setVisibility(View.GONE);
                    android_details.setVisibility(View.GONE);
                    ios_details.setVisibility(View.GONE);
                    web_details.setVisibility(View.VISIBLE);
                    android__title.setTypeface(OpenSans_Regular);

                    ios_title.setTypeface(OpenSans_Regular);
                    web_title.setTypeface(OpenSans_Bold);
                    if (paid_web_sd_list.size() > 0) {
                        sd_textView.setVisibility(View.VISIBLE);
                    } else {
                        sd_textView.setVisibility(View.GONE);
                    }

                    if (paid_web_hd_list.size() > 0) {
                        hd_textView.setVisibility(View.VISIBLE);
                    } else {
                        hd_textView.setVisibility(View.GONE);
                    }

                    if (paid_web_hdx_list.size() > 0) {
                        hdx_textView.setVisibility(View.VISIBLE);
                    } else {
                        hdx_textView.setVisibility(View.GONE);
                    }


                    if (paid_web_sd_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        sd_textView.setTypeface(OpenSans_Bold);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hd_textView.setTypeface(OpenSans_Regular);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hdx_textView.setTypeface(OpenSans_Regular);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_sd_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_web_hd_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        sd_textView.setTypeface(OpenSans_Regular);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        hd_textView.setTypeface(OpenSans_Bold);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hdx_textView.setTypeface(OpenSans_Regular);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_hd_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_web_hdx_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        sd_textView.setTypeface(OpenSans_Regular);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hd_textView.setTypeface(OpenSans_Regular);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        hdx_textView.setTypeface(OpenSans_Bold);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_hdx_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_web_other.size() > 0) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_other);
                        web_details.setAdapter(moviePaymentAdapter);
                    }


                }
            });


            layout_ios_genre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selected_app_format = "ios";
                    android__bottom.setVisibility(View.VISIBLE);
                    web_left.setVisibility(View.GONE);
                    web_top.setVisibility(View.GONE);
                    ios_left.setVisibility(View.VISIBLE);
                    ios_bottom.setVisibility(View.GONE);

                    android_left.setVisibility(View.GONE);
                    android__top.setVisibility(View.GONE);
                    ios_top.setVisibility(View.VISIBLE);
                    web_bottom.setVisibility(View.VISIBLE);
                    ios_right.setVisibility(View.VISIBLE);
                    android_details.setVisibility(View.GONE);
                    ios_details.setVisibility(View.VISIBLE);
                    web_details.setVisibility(View.GONE);

                    android__title.setTypeface(OpenSans_Regular);

                    ios_title.setTypeface(OpenSans_Bold);
                    web_title.setTypeface(OpenSans_Regular);

                    if (paid_ios_sd_list.size() > 0) {
                        sd_textView.setVisibility(View.VISIBLE);
                    } else {
                        sd_textView.setVisibility(View.GONE);
                    }

                    if (paid_ios_hd_list.size() > 0) {
                        hd_textView.setVisibility(View.VISIBLE);
                    } else {
                        hd_textView.setVisibility(View.GONE);
                    }

                    if (paid_ios_hdx_list.size() > 0) {
                        hdx_textView.setVisibility(View.VISIBLE);
                    } else {
                        hdx_textView.setVisibility(View.GONE);
                    }


                    if (paid_ios_sd_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        sd_textView.setTypeface(OpenSans_Bold);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hd_textView.setTypeface(OpenSans_Regular);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hdx_textView.setTypeface(OpenSans_Regular);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_sd_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_ios_hd_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        sd_textView.setTypeface(OpenSans_Regular);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        hd_textView.setTypeface(OpenSans_Bold);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hdx_textView.setTypeface(OpenSans_Regular);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_hd_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_ios_hdx_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        sd_textView.setTypeface(OpenSans_Regular);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hd_textView.setTypeface(OpenSans_Regular);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        hdx_textView.setTypeface(OpenSans_Bold);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_hdx_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_ios_other.size() > 0) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_other);
                        ios_details.setAdapter(moviePaymentAdapter);
                    }

                }
            });
*/

            switch_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    swap_switch();



                }
            });
            android_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayAndroidapps(isswitchselected);

                }
            });


            desktop_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayWebapps(isswitchselected);

                }
            });


            apple_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    displayIosApps(isswitchselected);
                }
            });
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(HomeActivity.mMovieOrSerial.equalsIgnoreCase(Constants.SHOWS_DETAILS))
                    {
                        new AddFavorites().execute("add", "show", String.valueOf(movie_id));
                    }else
                    {
                        new AddFavorites().execute("add", "movie", String.valueOf(movie_id));
                    }



                }
            });

            favorite.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        favorite.setAlpha(0.5f);

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        favorite.setAlpha(1f);
                    } else  {
                        favorite.setAlpha(1f);
                    }


                    return false;
                }
            });

            movies_watch_free_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isMovieRent = false;
                    HomeActivity.Selecteddetails = Constants.DEATILS_SUBCONTENT;
                    if (HomeActivity.mMovieOrSerial.equalsIgnoreCase(Constants.SHOWS_DETAILS)) {

//                        layout_shows_castand_overvew_details.setVisibility(View.GONE);

                        showPersonalizeDialog();

                        //FragmentShowSeasonsAndEpisodes.hideLatest(true);

                       /* showsandepisode_container_layout.setVisibility(View.VISIBLE);
                        episode_free_left.setVisibility(View.VISIBLE);
                        episode_free_top.setVisibility(View.VISIBLE);
                        episode_showall_left.setVisibility(View.VISIBLE);
                        episode_showall_bottom.setVisibility(View.VISIBLE);
                        movie_showall_right.setVisibility(View.GONE);
                        episode_free_bottom.setVisibility(View.GONE);
                        episode_showall_top.setVisibility(View.GONE);
                        episode_showall_details.setVisibility(View.GONE);
                        episode_free_details.setVisibility(View.VISIBLE);
                        ShowsAdapter showsAdapter = new ShowsAdapter(getActivity(), FragmentShowSeasonsAndEpisodes.FreemEpisodeBeans);
                        episode_free_details.setAdapter(showsAdapter);*/
//                        new EpisodeDetailsLoading().execute();
                    } else {
                        movies_button_tab_layout.setVisibility(View.GONE);
                        layout_payment_details.setVisibility(View.VISIBLE);

                        selected_app_format = "android";
                        displayAndroidapps(false);
                        /*android_left.setVisibility(View.VISIBLE);
                        android__top.setVisibility(View.VISIBLE);
                        web_left.setVisibility(View.VISIBLE);
                        ios_bottom.setVisibility(View.VISIBLE);
                        web_bottom.setVisibility(View.VISIBLE);
                        android__bottom.setVisibility(View.GONE);
                        web_top.setVisibility(View.GONE);
                        ios_left.setVisibility(View.GONE);
                        ios_top.setVisibility(View.GONE);
                        ios_right.setVisibility(View.GONE);
                        android_details.setVisibility(View.VISIBLE);
                        ios_details.setVisibility(View.GONE);
                        web_details.setVisibility(View.GONE);

                        android__title.setTypeface(OpenSans_Bold);
                        ios_title.setTypeface(OpenSans_Regular);
                        web_title.setTypeface(OpenSans_Regular);
                        if (paid_sd_list.size() > 0) {
                            sd_textView.setVisibility(View.VISIBLE);
                        } else {
                            sd_textView.setVisibility(View.GONE);
                        }

                        if (paid_hd_list.size() > 0) {
                            hd_textView.setVisibility(View.VISIBLE);
                        } else {
                            hd_textView.setVisibility(View.GONE);
                        }

                        if (paid_hdx_list.size() > 0) {
                            hdx_textView.setVisibility(View.VISIBLE);
                        } else {
                            hdx_textView.setVisibility(View.GONE);
                        }


                        if (paid_sd_list.size() > 0) {
                            sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                            sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                            sd_textView.setTypeface(OpenSans_Bold);

                            hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                            hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                            hd_textView.setTypeface(OpenSans_Regular);

                            hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                            hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                            hdx_textView.setTypeface(OpenSans_Regular);

                            MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_sd_list);
                            android_details.setAdapter(moviePaymentAdapter);
                        } else if (paid_hd_list.size() > 0) {
                            sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                            sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                            sd_textView.setTypeface(OpenSans_Regular);

                            hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                            hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                            hd_textView.setTypeface(OpenSans_Bold);

                            hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                            hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                            hdx_textView.setTypeface(OpenSans_Regular);

                            MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hd_list);
                            android_details.setAdapter(moviePaymentAdapter);
                        } else if (paid_hdx_list.size() > 0) {
                            sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                            sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                            sd_textView.setTypeface(OpenSans_Regular);

                            hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                            hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                            hd_textView.setTypeface(OpenSans_Regular);

                            hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                            hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                            hdx_textView.setTypeface(OpenSans_Bold);

                            MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hdx_list);
                            android_details.setAdapter(moviePaymentAdapter);
                        } else if (paid_other.size() > 0) {
                            MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_other);
                            android_details.setAdapter(moviePaymentAdapter);
                        }

*/

                    }


                }
            });


            movies_rent_now_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    movies_button_tab_layout.setVisibility(View.GONE);
                    layout_payment_details.setVisibility(View.VISIBLE);


                    selected_app_format = "android";
                    displayAndroidapps(false);
                    /*android_left.setVisibility(View.VISIBLE);
                    android__top.setVisibility(View.VISIBLE);
                    web_left.setVisibility(View.VISIBLE);
                    ios_bottom.setVisibility(View.VISIBLE);
                    web_bottom.setVisibility(View.VISIBLE);
                    android__bottom.setVisibility(View.GONE);
                    web_top.setVisibility(View.GONE);
                    ios_left.setVisibility(View.GONE);
                    ios_top.setVisibility(View.GONE);
                    ios_right.setVisibility(View.GONE);
                    android_details.setVisibility(View.VISIBLE);
                    ios_details.setVisibility(View.GONE);
                    web_details.setVisibility(View.GONE);

                    android__title.setTypeface(OpenSans_Bold);
                    ios_title.setTypeface(OpenSans_Regular);
                    web_title.setTypeface(OpenSans_Regular);
                    if (paid_sd_list.size() > 0) {
                        sd_textView.setVisibility(View.VISIBLE);
                    } else {
                        sd_textView.setVisibility(View.GONE);
                    }

                    if (paid_hd_list.size() > 0) {
                        hd_textView.setVisibility(View.VISIBLE);
                    } else {
                        hd_textView.setVisibility(View.GONE);
                    }

                    if (paid_hdx_list.size() > 0) {
                        hdx_textView.setVisibility(View.VISIBLE);
                    } else {
                        hdx_textView.setVisibility(View.GONE);
                    }


                    if (paid_sd_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        sd_textView.setTypeface(OpenSans_Bold);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hd_textView.setTypeface(OpenSans_Regular);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hdx_textView.setTypeface(OpenSans_Regular);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_sd_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_hd_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        sd_textView.setTypeface(OpenSans_Regular);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        hd_textView.setTypeface(OpenSans_Bold);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hdx_textView.setTypeface(OpenSans_Regular);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hd_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_hdx_list.size() > 0) {
                        sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        sd_textView.setTypeface(OpenSans_Regular);

                        hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                        hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                        hd_textView.setTypeface(OpenSans_Regular);

                        hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                        hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        hdx_textView.setTypeface(OpenSans_Bold);

                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hdx_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    } else if (paid_other.size() > 0) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_other);
                        android_details.setAdapter(moviePaymentAdapter);
                    }
*/
                }
            });
            sd_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    sd_textView.setTypeface(OpenSans_Bold);

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hd_textView.setTypeface(OpenSans_Regular);

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hdx_textView.setTypeface(OpenSans_Regular);


                    if (selected_app_format.equalsIgnoreCase("android")) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_sd_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    } else if (selected_app_format.equalsIgnoreCase("ios")) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_sd_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    } else if (selected_app_format.equalsIgnoreCase("web")) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_sd_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    }
                }
            });
            hd_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    sd_textView.setTypeface(OpenSans_Regular);

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    hd_textView.setTypeface(OpenSans_Bold);

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hdx_textView.setTypeface(OpenSans_Regular);

                    if (selected_app_format.equalsIgnoreCase("android")) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hd_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    } else if (selected_app_format.equalsIgnoreCase("ios")) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_hd_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    } else if (selected_app_format.equalsIgnoreCase("web")) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_hd_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    }
                }
            });
            hdx_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    sd_textView.setTypeface(OpenSans_Regular);

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hd_textView.setTypeface(OpenSans_Regular);

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    hdx_textView.setTypeface(OpenSans_Bold);

                    if (selected_app_format.equalsIgnoreCase("android")) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hdx_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    } else if (selected_app_format.equalsIgnoreCase("ios")) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_hdx_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    } else if (selected_app_format.equalsIgnoreCase("web")) {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_hdx_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    }
                }
            });


            layout_movie_overview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movie_overview_left.setVisibility(View.VISIBLE);
                    movie_overview_top.setVisibility(View.VISIBLE);
                    movie_cast_left.setVisibility(View.VISIBLE);
                    movie_genre_bottom.setVisibility(View.VISIBLE);
                    movie_cast_bottom.setVisibility(View.VISIBLE);
                    movie_overview_bottom.setVisibility(View.GONE);
                    movie_cast_top.setVisibility(View.GONE);
                    movie_genre_left.setVisibility(View.GONE);
                    movie_genre_top.setVisibility(View.GONE);
                    movie_genre_right.setVisibility(View.GONE);
                    movie_overview_details.setVisibility(View.VISIBLE);
                    movie_cast_details.setVisibility(View.GONE);
                    movie_genre_details.setVisibility(View.GONE);
                    overview_title.setTypeface(OpenSans_Bold);
                    genre_title.setTypeface(OpenSans_Regular);
                    cast_title.setTypeface(OpenSans_Regular);
                    show_more_textview.setVisibility(View.VISIBLE);

                }
            });
            layout_movie_cast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movie_overview_bottom.setVisibility(View.VISIBLE);
                    movie_cast_left.setVisibility(View.VISIBLE);
                    movie_cast_top.setVisibility(View.VISIBLE);
                    movie_genre_left.setVisibility(View.VISIBLE);
                    movie_genre_bottom.setVisibility(View.VISIBLE);

                    movie_overview_left.setVisibility(View.GONE);
                    movie_overview_top.setVisibility(View.GONE);
                    movie_genre_top.setVisibility(View.GONE);
                    movie_cast_bottom.setVisibility(View.GONE);
                    movie_genre_right.setVisibility(View.GONE);
                    movie_overview_details.setVisibility(View.GONE);
                    movie_cast_details.setVisibility(View.VISIBLE);
                    movie_genre_details.setVisibility(View.GONE);

                    overview_title.setTypeface(OpenSans_Regular);
                    genre_title.setTypeface(OpenSans_Regular);
                    cast_title.setTypeface(OpenSans_Bold);
                    show_more_textview.setVisibility(View.GONE);

                }
            });

            layout_movie_genre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movie_overview_bottom.setVisibility(View.VISIBLE);
                    movie_cast_bottom.setVisibility(View.VISIBLE);
                    movie_genre_right.setVisibility(View.VISIBLE);
                    movie_genre_top.setVisibility(View.VISIBLE);
                    movie_genre_left.setVisibility(View.VISIBLE);
                    movie_overview_left.setVisibility(View.GONE);
                    movie_overview_top.setVisibility(View.GONE);
                    movie_genre_bottom.setVisibility(View.GONE);
                    movie_cast_top.setVisibility(View.GONE);
                    movie_cast_left.setVisibility(View.GONE);
                    movie_overview_details.setVisibility(View.GONE);
                    movie_cast_details.setVisibility(View.GONE);
                    movie_genre_details.setVisibility(View.VISIBLE);

                    overview_title.setTypeface(OpenSans_Regular);
                    genre_title.setTypeface(OpenSans_Bold);
                    cast_title.setTypeface(OpenSans_Regular);
                    show_more_textview.setVisibility(View.GONE);

                }
            });


            /*episode_free.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    episode_free_left.setVisibility(View.VISIBLE);
                    episode_free_top.setVisibility(View.VISIBLE);
                    episode_showall_left.setVisibility(View.VISIBLE);
                    episode_showall_bottom.setVisibility(View.VISIBLE);
                    movie_showall_right.setVisibility(View.GONE);
                    episode_free_bottom.setVisibility(View.GONE);
                    episode_showall_top.setVisibility(View.GONE);
                    episode_showall_details.setVisibility(View.GONE);
                    episode_free_details.setVisibility(View.VISIBLE);
                    ShowsAdapter showsAdapter = new ShowsAdapter(getActivity(), FragmentShowSeasonsAndEpisodes.FreemEpisodeBeans);
                    episode_free_details.setAdapter(showsAdapter);

                }
            });

            episode_showall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    episode_free_left.setVisibility(View.GONE);
                    episode_free_top.setVisibility(View.GONE);
                    episode_showall_left.setVisibility(View.VISIBLE);
                    episode_showall_bottom.setVisibility(View.GONE);
                    movie_showall_right.setVisibility(View.VISIBLE);
                    episode_free_bottom.setVisibility(View.VISIBLE);
                    episode_showall_top.setVisibility(View.VISIBLE);
                    episode_free_details.setVisibility(View.GONE);
                    episode_showall_details.setVisibility(View.VISIBLE);

                    ShowsAdapter showsAdapter = new ShowsAdapter(getActivity(), FragmentShowSeasonsAndEpisodes.mEpisodeBeans);
                    episode_showall_details.setAdapter(showsAdapter);


                }
            });*/


        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void swap_switch() {
        if (isswitchselected) {
            isswitchselected = false;
            switch_image.setImageResource(R.drawable.off);
            free_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            buy_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_blue));
            refreshlist();


        } else {
            isswitchselected = true;
            switch_image.setImageResource(R.drawable.on);
            buy_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            free_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_blue));
            refreshlist();
        }
    }

    private void makelistings() {

        try {
            JSONObject m_jsonMovieLinks = new JSONObject(list);

            try {
                m_jsonArrayFreeItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("android").getJSONArray("free");
                m_jsonArrayPaidItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("android").getJSONArray("paid");


                try {
                    if (m_jsonArrayFreeItems.length() > 0) {
                        for (int i = 0; i < m_jsonArrayFreeItems.length(); i++) {
                            final JSONObject jsonObject = m_jsonArrayFreeItems.getJSONObject(i);
                            String source = jsonObject.getString("source");
                            String link = jsonObject.getString("link");
                            final String app_name = jsonObject.getString("app_name");
                            final String app_download_link = jsonObject.getString("app_download_link");
                            final String display_name = jsonObject.getString("display_name");
                            final boolean app_required = jsonObject.getBoolean("app_required");
                            final boolean app_link = jsonObject.getBoolean("app_link");
                            final String image = jsonObject.getString("image");
                            String subscription_code="";
                            if(jsonObject.has("subscription_code")){
                                subscription_code=jsonObject.getString("subscription_code");
                            }
                            Log.d("Source::", "Source::" + source);
                            if (jsonObject.has("formats")) {
                                Log.d("Source::", "has formats::");
                                Object o = jsonObject.get("formats");
                                if (o instanceof JSONArray) {
                                    JSONArray formats = jsonObject.getJSONArray("formats");
                                    if (formats.length() > 0) {
                                        for (int j = 0; j < formats.length(); j++) {
                                            String formats_price = formats.getJSONObject(j).getString("price");
                                            String formats_type = formats.getJSONObject(j).getString("type");
                                            String formats_format = formats.getJSONObject(j).getString("format");
                                            formats_price = "$" + formats_price;

                                            if (formats_format.equalsIgnoreCase("sd")) {
                                                free_sd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image,subscription_code));
                                            } else if (formats_format.equalsIgnoreCase("hd")) {
                                                free_hd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image,subscription_code));
                                            } else if (formats_format.equalsIgnoreCase("hdx")) {
                                                free_hdx_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image,subscription_code));
                                            }


                                        }
                                    } else {
                                        free_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link,image,subscription_code));
                                    }
                                } else {
                                    free_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link,image,subscription_code));
                                }
                            }
                        }
                        if(free_sd_list.size()==0&&free_hd_list.size()==0&&free_hdx_list.size()==0&&free_other.size()==0){
                            switch_image.setVisibility(View.GONE);
                            free_textView.setVisibility(View.GONE);
                        }
                    }else{
                        switch_image.setVisibility(View.GONE);
                        free_textView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (m_jsonArrayPaidItems.length() > 0) {
                        for (int i = 0; i < m_jsonArrayPaidItems.length(); i++) {
                            final JSONObject jsonObject = m_jsonArrayPaidItems.getJSONObject(i);
                            String source = jsonObject.getString("source");
                            String link = jsonObject.getString("link");
                            final String app_name = jsonObject.getString("app_name");
                            final String app_download_link = jsonObject.getString("app_download_link");
                            final String display_name = jsonObject.getString("display_name");
                            final boolean app_required = jsonObject.getBoolean("app_required");
                            final boolean app_link = jsonObject.getBoolean("app_link");
                            final String image = jsonObject.getString("image");
                            String subscription_code="";
                            if(jsonObject.has("subscription_code")){
                                subscription_code=jsonObject.getString("subscription_code");
                            }
                            Log.d("Source::", "Source::" + source);
                            if (jsonObject.has("formats")) {
                                Log.d("Source::", "has formats::");
                                Object o = jsonObject.get("formats");
                                if (o instanceof JSONArray) {
                                    JSONArray formats = jsonObject.getJSONArray("formats");
                                    if (formats.length() > 0) {
                                        for (int j = 0; j < formats.length(); j++) {
                                            String formats_price = formats.getJSONObject(j).getString("price");
                                            String formats_type = formats.getJSONObject(j).getString("type");
                                            String formats_format = formats.getJSONObject(j).getString("format");
                                            formats_price = "$" + formats_price;

                                            if (formats_format.equalsIgnoreCase("sd")) {
                                                paid_sd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image,subscription_code));
                                            } else if (formats_format.equalsIgnoreCase("hd")) {
                                                paid_hd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image,subscription_code));
                                            } else if (formats_format.equalsIgnoreCase("hdx")) {
                                                paid_hdx_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image,subscription_code));
                                            }
                                        }
                                    }

                                } else {
                                    free_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "Subscription", "", display_name, app_required, app_link,image,subscription_code));
                                }
                            } else {
                                free_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "Subscription", "", display_name, app_required, app_link,image,subscription_code));
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*if (m_jsonArrayFreeItems == null || m_jsonArrayFreeItems.length() == 0) {
                movies_watch_free_button.setVisibility(View.VISIBLE);
            } else {
                movies_watch_free_button.setVisibility(View.VISIBLE);
            }*/

            /*if (m_jsonArrayPaidItems == null || m_jsonArrayPaidItems.length() == 0) {
                movies_rent_now_button.setVisibility(View.GONE);
            } else {
                movies_rent_now_button.setVisibility(View.VISIBLE);
            }*/

            free_sd_list.addAll(free_other);


            paid_sd_list.addAll(paid_other);

            m_jsonArrayiosFreeItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("ios").getJSONArray("free");
            m_jsonArrayiosPaidItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("ios").getJSONArray("paid");


            try {


                /*if (m_jsonArrayiosFreeItems.length() > 0) {
                    for (int i = 0; i < m_jsonArrayiosFreeItems.length(); i++) {
                        final JSONObject jsonObject = m_jsonArrayiosFreeItems.getJSONObject(i);
                        String source = jsonObject.getString("source");
                        String link = jsonObject.getString("link");
                        final String app_name = jsonObject.getString("app_name");
                        final String app_download_link = jsonObject.getString("app_download_link");
                        final String display_name = jsonObject.getString("display_name");
                        final boolean app_required = jsonObject.getBoolean("app_required");
                        final boolean app_link = jsonObject.getBoolean("app_link");
                        final String image = jsonObject.getString("image");
                        Log.d("Source::", "Source::" + source);
                        if (jsonObject.has("formats")) {
                            Log.d("Source::", "has formats::");
                            Object o = jsonObject.get("formats");
                            if (o instanceof JSONArray) {
                                JSONArray formats = jsonObject.getJSONArray("formats");
                                if (formats.length() > 0) {
                                    for (int j = 0; j < formats.length(); j++) {
                                        String formats_price = formats.getJSONObject(j).getString("price");
                                        String formats_type = formats.getJSONObject(j).getString("type");
                                        String formats_format = formats.getJSONObject(j).getString("format");
                                        formats_price = "$" + formats_price;

                                        if (formats_format.equalsIgnoreCase("sd")) {
                                            free_ios_sd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        } else if (formats_format.equalsIgnoreCase("hd")) {
                                            free_ios_hd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        } else if (formats_format.equalsIgnoreCase("hdx")) {
                                            free_ios_hdx_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        }


                                    }
                                } else {
                                    free_ios_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link,image));
                                }
                            } else {
                                free_ios_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link,image));
                            }
                        }
                    }
                }*/

            } catch (Exception e)

            {
                e.printStackTrace();
            }


            try {
               /* if (m_jsonArrayiosPaidItems.length() > 0) {
                    for (int i = 0; i < m_jsonArrayiosPaidItems.length(); i++) {
                        final JSONObject jsonObject = m_jsonArrayiosPaidItems.getJSONObject(i);
                        String source = jsonObject.getString("source");
                        String link = jsonObject.getString("link");
                        final String app_name = jsonObject.getString("app_name");
                        final String app_download_link = jsonObject.getString("app_download_link");
                        final String display_name = jsonObject.getString("display_name");
                        final boolean app_required = jsonObject.getBoolean("app_required");
                        final boolean app_link = jsonObject.getBoolean("app_link");
                        final String image = jsonObject.getString("image");
                        Log.d("Source::", "Source::" + source);
                        if (jsonObject.has("formats")) {
                            Log.d("Source::", "has formats::");
                            Object o = jsonObject.get("formats");
                            if (o instanceof JSONArray) {
                                JSONArray formats = jsonObject.getJSONArray("formats");
                                if (formats.length() > 0) {
                                    for (int j = 0; j < formats.length(); j++) {
                                        String formats_price = formats.getJSONObject(j).getString("price");
                                        String formats_type = formats.getJSONObject(j).getString("type");
                                        String formats_format = formats.getJSONObject(j).getString("format");
                                        formats_price = "$" + formats_price;

                                        if (formats_format.equalsIgnoreCase("sd")) {
                                            paid_ios_sd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        } else if (formats_format.equalsIgnoreCase("hd")) {
                                            paid_ios_hd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        } else if (formats_format.equalsIgnoreCase("hdx")) {
                                            paid_ios_hdx_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        }
                                    }
                                }

                            } else {
                                paid_ios_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link,image));
                            }
                        } else {
                            paid_ios_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link,image));
                        }
                    }
                    free_ios_sd_list.addAll(free_ios_other);
                    paid_ios_sd_list.addAll(paid_ios_other);
                }*/


            } catch (Exception e) {
                e.printStackTrace();
            }


            m_jsonArraywebFreeItems = m_jsonMovieLinks.getJSONObject("desktop").getJSONArray("free");
            m_jsonArraywebPaidItems = m_jsonMovieLinks.getJSONObject("desktop").getJSONArray("paid");
                   /* m_jsonArrayiosFreeItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("ios").getJSONArray("free");
                    m_jsonArrayiosPaidItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("ios").getJSONArray("paid");*/


            try {

                /*if (m_jsonArraywebFreeItems.length() > 0) {
                    for (int i = 0; i < m_jsonArraywebFreeItems.length(); i++) {
                        final JSONObject jsonObject = m_jsonArraywebFreeItems.getJSONObject(i);
                        String source="";
                        if(jsonObject.has("source")){
                            source = jsonObject.getString("source");
                        }
                        String link = jsonObject.getString("link");
                        final String app_name = jsonObject.getString("app_name");
                        final String app_download_link = jsonObject.getString("app_download_link");
                        final String display_name = jsonObject.getString("display_name");
                        final boolean app_required = jsonObject.getBoolean("app_required");
                        final boolean app_link = jsonObject.getBoolean("app_link");
                        final String image = jsonObject.getString("image");
                        Log.d("Source::", "Source::" + source);
                        if (jsonObject.has("formats")) {
                            Log.d("Source::", "has formats::");
                            Object o = jsonObject.get("formats");
                            if (o instanceof JSONArray) {
                                JSONArray formats = jsonObject.getJSONArray("formats");
                                if (formats.length() > 0) {
                                    for (int j = 0; j < formats.length(); j++) {
                                        String formats_price = formats.getJSONObject(j).getString("price");
                                        String formats_type = formats.getJSONObject(j).getString("type");
                                        String formats_format = formats.getJSONObject(j).getString("format");
                                        formats_price = "$" + formats_price;

                                        if (formats_format.equalsIgnoreCase("sd")) {
                                            free_web_sd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        } else if (formats_format.equalsIgnoreCase("hd")) {
                                            free_web_hd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        } else if (formats_format.equalsIgnoreCase("hdx")) {
                                            free_web_hdx_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        }


                                    }
                                } else {
                                    free_web_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link,image));
                                }
                            } else {
                                free_web_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link,image));
                            }
                        }
                    }
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {

                /*if (m_jsonArraywebPaidItems.length() > 0) {
                    for (int i = 0; i < m_jsonArraywebPaidItems.length(); i++) {
                        final JSONObject jsonObject = m_jsonArraywebPaidItems.getJSONObject(i);
                        String source = jsonObject.getString("source");
                        String link = jsonObject.getString("link");
                        final String app_name = jsonObject.getString("app_name");
                        final String app_download_link = jsonObject.getString("app_download_link");
                        final String display_name = jsonObject.getString("display_name");
                        final boolean app_required = jsonObject.getBoolean("app_required");
                        final boolean app_link = jsonObject.getBoolean("app_link");
                        final String image = jsonObject.getString("image");
                        Log.d("Source::", "Source::" + source);
                        if (jsonObject.has("formats")) {
                            Log.d("Source::", "has formats::");
                            Object o = jsonObject.get("formats");
                            if (o instanceof JSONArray) {
                                JSONArray formats = jsonObject.getJSONArray("formats");
                                if (formats.length() > 0) {
                                    for (int j = 0; j < formats.length(); j++) {
                                        String formats_price = formats.getJSONObject(j).getString("price");
                                        String formats_type = formats.getJSONObject(j).getString("type");
                                        String formats_format = formats.getJSONObject(j).getString("format");
                                        formats_price = "$" + formats_price;

                                        if (formats_format.equalsIgnoreCase("sd")) {
                                            paid_web_sd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        } else if (formats_format.equalsIgnoreCase("hd")) {
                                            paid_web_hd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        } else if (formats_format.equalsIgnoreCase("hdx")) {
                                            paid_web_hdx_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                        }
                                    }
                                }
                                else {
                                    paid_web_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link,image));
                                }

                            } else {
                                paid_web_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link,image));
                            }
                        }
                    }
                    free_web_sd_list.addAll(free_web_other);

                    paid_web_sd_list.addAll(paid_web_other);
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }



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


    public class MovieCastAndGenreAdapter extends RecyclerView.Adapter<MovieCastAndGenreAdapter.DataObjectHolder> {
        Context context;
        JSONArray moviecast;

        public MovieCastAndGenreAdapter(Context context, JSONArray moviecast) {
            this.context = context;
            this.moviecast = moviecast;

        }

        @Override
        public MovieCastAndGenreAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieCastAndGenreAdapter.DataObjectHolder holder, int position) {

            try {
                JSONObject jsonObject = moviecast.getJSONObject(position);
                String strName = jsonObject.getString("name");
                holder.fragment_ondemandlist_items.setText(strName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return moviecast.length();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);
            }
        }
    }


    public class MoviePaymentAdapter extends RecyclerView.Adapter<MoviePaymentAdapter.DataObjectHolder> {
        Context context;
        JSONArray jsonBuyItmes;
        ArrayList<AppFormatBean> data_list;
        private String[] sub_list;

        public MoviePaymentAdapter(Context context, ArrayList<AppFormatBean> data_list) {
            this.context = context;
            this.data_list = data_list;
            sub_list= PreferenceManager.geSubscribedList(context);
            Log.d("sublist:::",":::start");
            for(int i=0;i<sub_list.length;i++){
                Log.d("sublist:::",":::"+sub_list[i]);
            }

        }

        @Override
        public MoviePaymentAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.movie_payment_design, parent, false);

            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(MoviePaymentAdapter.DataObjectHolder holder, final int position) {

            String strSourceName = "";
            String deepLink = "";
            String app_download_link = "";
            String display_name = "";
            String app_required = "";
            String app_link = "";
            String price = "";
            try {
                strSourceName = data_list.get(position).getSource();
                final String app_name = data_list.get(position).getApp_name();
                Log.d("Source::", "Source::" + strSourceName);
                price = data_list.get(position).getFormats_price();
                if(data_list.get(position).getFormats_type().equalsIgnoreCase("rent")){
                    holder.movie_payment_textview.setText("RENT "+price);
                    holder.movie_payment_textview.setBackgroundColor(ContextCompat.getColor(context, R.color.free_bg));
                }else if(data_list.get(position).getFormats_type().equalsIgnoreCase("purchase")){
                    holder.movie_payment_textview.setText("BUY "+price);
                    holder.movie_payment_textview.setBackgroundColor(ContextCompat.getColor(context, R.color.rent_btn_bg));
                }else if(data_list.get(position).getFormats_type().equalsIgnoreCase("subscription")){
                    if(Arrays.asList(sub_list).contains(data_list.get(position).getSubscription_code().toLowerCase())){
                        holder.movie_payment_textview.setText("Subscribed");
                        holder.movie_payment_textview.setBackgroundColor(ContextCompat.getColor(context, R.color.rent_btn_bg));
                    }else{
                        holder.movie_payment_textview.setText("Free Trial");
                        holder.movie_payment_textview.setBackgroundResource(R.drawable.free_trial_bg);
                    }


                }else{
                    holder.movie_payment_textview.setText(price);
                }



                aq.id(holder.movie_payment_imageview).image(data_list.get(position).getImage());

               /* if (strSourceName.equalsIgnoreCase("vudu")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_vudu);
                } else if (strSourceName.equalsIgnoreCase("google_play")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_play);
                } else if (strSourceName.equalsIgnoreCase("hulu_plus")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_bag);
                } else if (strSourceName.equalsIgnoreCase("Amazon")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_amazon);
                } else if (strSourceName.equalsIgnoreCase("M-GO")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_mgo);
                } else if (strSourceName.equalsIgnoreCase("YouTube")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_tube);
                } else if (strSourceName.equalsIgnoreCase("crackle")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.crackle);
                }else if (strSourceName.equalsIgnoreCase("itunes")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.apple);
                }

                else{

                switch (app_name){

                    case "Acorn TV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.acorn);
                        break;

                    case "Amazon Instant Video":
                        holder.movie_payment_imageview.setImageResource(R.drawable.amazon_videoplay);
                        break;

                    case "CBS All Access":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Crunchyroll Premium":
                        holder.movie_payment_imageview.setImageResource(R.drawable.crunchyroll);
                        break;

                    case "DramaFever Premium":
                        holder.movie_payment_imageview.setImageResource(R.drawable.dramafever);
                        break;

                    case "Fandor":
                        holder.movie_payment_imageview.setImageResource(R.drawable.fandor);
                        break;

                    case "Feeln":
                        holder.movie_payment_imageview.setImageResource(R.drawable.feeln);
                        break;

                    case "FlixFling":
                        holder.movie_payment_imageview.setImageResource(R.drawable.flixfling);
                        break;

                    case "Gaiam TV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.hbo);
                        break;

                    case "Hulu":
                        holder.movie_payment_imageview.setImageResource(R.drawable.hulu);
                        break;

                    case "Hulu with Showtime":
                        holder.movie_payment_imageview.setImageResource(R.drawable.indieflix);
                        break;

                    case "MUBI":
                        holder.movie_payment_imageview.setImageResource(R.drawable.mubi);
                        break;

                    case "Seeso":
                        holder.movie_payment_imageview.setImageResource(R.drawable.sesame);
                        break;

                    case "Showtime":
                        holder.movie_payment_imageview.setImageResource(R.drawable.sho);
                        break;

                    case "Shudder":
                        holder.movie_payment_imageview.setImageResource(R.drawable.sho);
                        break;

                    case "SundanceNow Doc Club":
                        holder.movie_payment_imageview.setImageResource(R.drawable.sundance);
                        break;

                    case "Warner Archive Instant":
                        holder.movie_payment_imageview.setImageResource(R.drawable.warner);
                        break;

                    case "ABC Family":
                        holder.movie_payment_imageview.setImageResource(R.drawable.abc_family);
                        break;

                    case "AMC":
                        holder.movie_payment_imageview.setImageResource(R.drawable.amc);
                        break;

                    case "BET On Demand":
                        holder.movie_payment_imageview.setImageResource(R.drawable.bet);
                        break;

                    case "Discovery GO":
                        holder.movie_payment_imageview.setImageResource(R.drawable.discovery_go);
                        break;

                    case "E!":
                        holder.movie_payment_imageview.setImageResource(R.drawable.e);
                        break;

                    case "Encore Play":
                        holder.movie_payment_imageview.setImageResource(R.drawable.encore);
                        break;

                    case "EPIX":
                        holder.movie_payment_imageview.setImageResource(R.drawable.epix);
                        break;

                    case "HBO GO":
                        holder.movie_payment_imageview.setImageResource(R.drawable.hbo_go);
                        break;

                    case "IFC":
                        holder.movie_payment_imageview.setImageResource(R.drawable.ifc);
                        break;

                    case "Lifetime":
                        holder.movie_payment_imageview.setImageResource(R.drawable.lifetime);
                        break;

                    case "LOGO":
                        holder.movie_payment_imageview.setImageResource(R.drawable.siemenslogo);
                        break;

                    case "MAX GO":
                        holder.movie_payment_imageview.setImageResource(R.drawable.max);
                        break;

                    case "Movieplex Play":
                        holder.movie_payment_imageview.setImageResource(R.drawable.movieplex);
                        break;

                    case "MSNBC":
                        holder.movie_payment_imageview.setImageResource(R.drawable.msnbc);
                        break;

                    case "MTV On Demand":
                        holder.movie_payment_imageview.setImageResource(R.drawable.nbc);
                        break;

                    case "NBC UNIVERSO":
                        holder.movie_payment_imageview.setImageResource(R.drawable.nbc);
                        break;

                    case "Oxygen":
                        holder.movie_payment_imageview.setImageResource(R.drawable.pivot);
                        break;

                    case "Showtime Anytime":
                        holder.movie_payment_imageview.setImageResource(R.drawable.sho);
                        break;

                    case "Simpsons World":
                        holder.movie_payment_imageview.setImageResource(R.drawable.spike);
                        break;

                    case "Syfy Sync":
                        holder.movie_payment_imageview.setImageResource(R.drawable.syfy);
                        break;

                    case "TBS":
                        holder.movie_payment_imageview.setImageResource(R.drawable.tbs);
                        break;

                    case "Telemundo Now":
                        holder.movie_payment_imageview.setImageResource(R.drawable.telemundo);
                        break;

                    case "The Weather Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.the_weather_channel);
                        break;

                    case "TNT":
                        holder.movie_payment_imageview.setImageResource(R.drawable.tnt);
                        break;

                    case "USA":
                        holder.movie_payment_imageview.setImageResource(R.drawable.usa);
                        break;

                    case "VH1 On Demand":
                        holder.movie_payment_imageview.setImageResource(R.drawable.watch_cooking_channel);
                        break;

                    case "Watch DIY Network":
                        holder.movie_payment_imageview.setImageResource(R.drawable.diy_network);
                        break;

                    case "Watch ESPN":
                        holder.movie_payment_imageview.setImageResource(R.drawable.watch_espn);
                        break;

                    case "Watch Food Network":
                        holder.movie_payment_imageview.setImageResource(R.drawable.watch_food_network);
                        break;

                    case "Watch HGTV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.hgtv_watch);
                        break;

                    case "Watch TCM":
                        holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                        break;

                    case "Watch Travel Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.travel_channel);
                        break;

                    case "WE tv":
                        holder.movie_payment_imageview.setImageResource(R.drawable.we);
                        break;

                    case "Al Jazeera America":
                        holder.movie_payment_imageview.setImageResource(R.drawable.al_jazeera_america);
                        break;

                    case "Amazon Prime":
                        holder.movie_payment_imageview.setImageResource(R.drawable.amazon_prime);
                        break;

                    case "American Heroes Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.american_heroes_channel);
                        break;

                    case "AOL ON":
                        holder.movie_payment_imageview.setImageResource(R.drawable.aol);
                        break;

                    case "AT&T U-verse":
                        holder.movie_payment_imageview.setImageResource(R.drawable.at_t);
                        break;

                    case "BBC America":
                        holder.movie_payment_imageview.setImageResource(R.drawable.bbc_news);
                        break;

                    case "Bloomberg":
                        holder.movie_payment_imageview.setImageResource(R.drawable.bloomberg);
                        break;

                    case "CBS":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "CBS News":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs_news);
                        break;

                    case "CMT":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cmt);
                        break;

                    case "colbertnation.com":
                        holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                        break;

                    case "Comcast":
                        holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                        break;

                    case "Cooking Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.watch_cooking_channel);
                        break;

                    case "Crackle":
                        holder.movie_payment_imageview.setImageResource(R.drawable.crackle);
                        break;

                    case "Crunchyroll":
                        holder.movie_payment_imageview.setImageResource(R.drawable.crunchyroll);
                        break;

                    case "CW Seed":
                        holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                        break;

                    case "Destination America":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "DirecTV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Discovery Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Dish":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Disney Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Disney Junior":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Disney XD":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "DIY":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "DramaFever":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "FM":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Food Network":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "FOX Sports":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Funny or Die":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Fuse":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;


                    case "Guidebox":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "HGTV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Hub Network":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Internet Archive":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Investigation Discovery":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Live Well Network":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Manga Entertainment":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Music Choice":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "My Damn Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "National Geographic":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "NBC":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Nick Jr.":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Oprah":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "PBS Kids":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Pop":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Red Bull TV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Revision3":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Science Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Shout! Factory TV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "SnagFilms":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "South Park Studios":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Spike TV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Style.com":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Syfy":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "teamcoco.com":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "The CW":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "The Golf Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "The Onion":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "thedailyshow.com":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "TMZ":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "ToonsTV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Travel Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Tubi TV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "TV LAND":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "ulive":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Velocity":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Viewster":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;


                    case "Vimeo":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Vogue":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Watch Disney Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "WGN America":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "WWE":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Yahoo Screen":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Amazon":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "CinemaNow":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Disney Movies Anywhere":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Distrify":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Flixster":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Paramount Movies":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Sony Entertainment Network":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "SundanceNow":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Wolfe On Demand":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "ABC News":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "A&E":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Adult Swim":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Cartoon Network":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "ABC":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "CNBC":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "CNN":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Comedy Central":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Esquire Network":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "FOX":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "FOX Business":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "FOX News":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "FX":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "fyi":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "HLN":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "MTV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "National Geographic Kids":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "NBC News":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Nickelodeon":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Sprout Now":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "STARZ Play":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "The History Channel":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "truTV":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "VH1":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Watch Disney Junior":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Watch Disney XD":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Xfinity":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "YouTube":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "Bravo":
                        holder.movie_payment_imageview.setImageResource(R.drawable.cbs);
                        break;

                    case "VUDU":
                        holder.movie_payment_imageview.setImageResource(R.drawable.vudu);
                        break;

                    case "Google Play":
                        holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                        break;


                    default:
                        holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                        break;

                }
                }
*/

                holder.movie_payment_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(data_list.get(position).isApp_required()){
                            new LoadingPackageNameList(app_name, data_list.get(position)).execute();
                        }else{
                            String url_link=data_list.get(position).getLink();
                            if(!TextUtils.isEmpty(url_link)){
                                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_link));
                                if (myIntent != null) {
                                    startActivity(myIntent);
                                } else {
                                    Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return data_list.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            TextView movie_payment_textview;
            ImageView movie_payment_imageview;

            public DataObjectHolder(View itemView) {
                super(itemView);
                movie_payment_textview = (TextView) itemView.findViewById(R.id.movie_payment_textview);
                movie_payment_imageview = (ImageView) itemView.findViewById(R.id.movie_payment_imageview);
            }
        }
    }

    public void makeDetail() {
        try {
            JSONObject data_object = new JSONObject(details);

            String strName = null;
            String strDescription = null;
            int nRunTime = 0;
            String strRating = null;
            String net="";
            try {
                if (data_object.has("name")) {
                    strName = data_object.getString("name");
                }

                if (data_object.has("description")) {
                    strDescription = data_object.getString("description");
                }

                if (data_object.has("poster_url")) {
                    String strPosterUrl = data_object.getString("poster_url");
                }
                if (data_object.has("rating")) {
                    strRating = data_object.getString("rating");
                }

                if (data_object.has("runtime")) {
                    nRunTime = data_object.getInt("runtime");
                }
                if (data_object.has("id")) {
                    movie_id = data_object.getInt("id");
                }
                try {
                    if(data_object.has("network")){
                        String nt=data_object.getString("network").trim();
                        if(TextUtils.isEmpty(nt)||nt.equalsIgnoreCase("null")){
                            network.setVisibility(View.GONE);
                            network_text.setVisibility(View.GONE);
                        }else{
                            JSONObject net_object=data_object.getJSONObject("network");
                            net=net_object.getString("name");
                            if(!TextUtils.isEmpty(net)&&!net.equalsIgnoreCase("null")){
                                network.setText(net);
                            }else{
                                network.setVisibility(View.GONE);
                                network_text.setVisibility(View.GONE);
                            }

                        }

                    }else{
                        network.setVisibility(View.GONE);
                        network_text.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (data_object.has("actors")) {
                    JSONArray actors = data_object.getJSONArray("actors");

                    movieCastAndGenreAdapter = new MovieCastAndGenreAdapter(getActivity(), actors);
                    movie_cast_details.setAdapter(movieCastAndGenreAdapter);
                }
                if (name.equals("")) {
                    show_title.setText(strName);
                } else {
                    show_title.setText(name);

                }


                if (nRunTime!=0) {
                    runtime.setText(nRunTime + " Minutes");
                } else {
                    runtime.setText("N/A");
                    runtime.setVisibility(View.GONE);
                    runtime_text.setVisibility(View.GONE);

                }
                if(!TextUtils.isEmpty(strRating)&&!strRating.equalsIgnoreCase("null")){
                    rating_text.setText("" + strRating);
                }else{
                    rating_text.setVisibility(View.GONE);
                }


                movie_overview_details.setText(strDescription);

                movie_overview_details.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int lineCount = movie_overview_details.getLineCount();
                            if (lineCount > 1) {
                                show_more_textview.setVisibility(View.VISIBLE);
                                movie_overview_details.setMaxLines(1);
                                show_more_textview.setText("more..");
                            } else {
                                show_more_textview.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // Use lineCount here
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoadingPackageNameList extends AsyncTask<Object, Object, Object> {
        String appName;
        JSONArray objResList;
        AppFormatBean jsonObject;
        String package_name = "";

        LoadingPackageNameList(String appName, AppFormatBean jsonObject) {
            this.appName = appName;
            objResList = new JSONArray();
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                JSONObject obj = new JSONObject();
                JSONObject objPackage = JSONRPCAPI.getPackageName(appName);
                if (objPackage == null) return null;

                Log.d("load package names::", "==>" + objPackage);
                if (objPackage != null) {
                    obj.put("app_package", objPackage.getString("package"));
                    package_name = objPackage.getString("package");
                    obj.put("app_image", objPackage.getString("image"));
                    objResList.put(obj);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            makeSourceDialog(package_name, jsonObject);
        }
    }

    private void makeSourceDialog(String packageName, AppFormatBean obj) {


        try {
           /* if( obj.isApp_required() ){
                Log.d("selecttv:::","is required:link:true");
            }
            Log.d("selecttv:::","packagename:::"+packageName);
            if (obj.isApp_required() &&isInstalledPackageName(packageName)) {
                *//*Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
                startActivity(launchIntent);*//*
                try {
                    Log.d("selecttv:::","movies:link:"+Uri.parse(obj.getLink()));
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getLink()));
                    startActivity(myIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                if (obj.getApp_name().toLowerCase().equals("google play")) {
                    Log.d("selecttv:::","movies:link:"+ Uri.parse(obj.getLink()));
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getLink()));
                    startActivity(myIntent);
                } else {
                    Log.d("selecttv:::","movies::"+Uri.parse(obj.getApp_download_link()));

                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getApp_download_link()));
                    startActivity(myIntent);
                   *//* Intent intent = new Intent(getActivity().getApplicationContext(), WebBrowserActivity.class);
                    intent.putExtra("url", obj.getString("app_download_link"));
                    intent.putExtra("name", obj.getString("app_name"));
                    startActivity(intent);*//*
                }
            }*/

            if (isInstalledPackageName(packageName)) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getLink()));
                if (myIntent != null) {
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (obj.getApp_name().toLowerCase().equals("google play")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getLink()));
                    startActivity(myIntent);
                } else {
                    /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getApp_download_link()));
                    startActivity(myIntent);
*/
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance(""+obj.getApp_name(),""+obj.getImage(),obj.getApp_download_link());
                    dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                }
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
            if (!isSystemPackage(ri)) {
                String key = ri.activityInfo.packageName;
                if (key.equals(packagename))
                    return true;
            }
        }
        return false;
    }

    private boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }


    public class EpisodeDetailsLoading extends AsyncTask<Object, Object, Object> {


        private JSONObject m_jsonshowDetail;
        private JSONObject m_jsonshowLinks;
        private JSONObject m_jsonEpisodeDeatils;

        @Override
        protected Object doInBackground(Object... params) {
            try {
                int nIndex = 1;

                nIndex = HomeActivity.showId;
                m_jsonEpisodeDeatils = JSONRPCAPI.getEpisodeDetail(HomeActivity.mLastEpisodeId);
                if (m_jsonEpisodeDeatils == null) return null;
                Log.d("NetworkList::", "Selected episode list::" + m_jsonEpisodeDeatils);
                Log.d("SHOW ID::==>", ">>>episodeid" + HomeActivity.mLastEpisodeId);
                m_jsonshowDetail = JSONRPCAPI.getShowDetail(nIndex);
                if (m_jsonshowDetail == null) return null;
                Log.d("m_jsonShowDetail ::", "::" + m_jsonshowDetail);
                m_jsonshowLinks = JSONRPCAPI.getShowLinks(nIndex, 0, 0);
                if (m_jsonshowLinks == null) return null;
                Log.d("parameters::", "::" + nIndex + ",.,." + HomeActivity.selecetd_season + ",.,.," + HomeActivity.mLastEpisodeId);
                Log.d("m_jsonShowLinks::", "::" + m_jsonshowLinks);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            try {
                if(HomeActivity.getmFragmentMovieMain()!=null){
                    FragmentTransaction network_fragmentTransaction = getChildFragmentManager().beginTransaction();
                    DialogFragment dialog_fragment = new DialogFragment();
                    network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_list, dialog_fragment);
                    network_fragmentTransaction.commit();
                }



              /*  FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction .replace(R.Seasonid.fragment_tvshow_bydecade_content, dialog_fragment);
                fragmentTransaction .commit();*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if(HomeActivity.getmFragmentMovieMain()!=null){
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    FragmentMoviesLeftContent fragmentMoviesLeftContent = FragmentMoviesLeftContent.newInstance(m_jsonshowDetail.toString(), image_url);
                    fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_list, fragmentMoviesLeftContent);
                    fragmentTransaction.commit();
                }

                if(HomeActivity.getmFragmentMovieMain()!=null){
                    FragmentTransaction network_fragmentTransaction = getChildFragmentManager().beginTransaction();
                    FragmentEpisodeorMovieDetails network_listfragment = FragmentEpisodeorMovieDetails.newInstance(m_jsonEpisodeDeatils.toString(), m_jsonshowLinks.toString(), "");
                    network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_content, network_listfragment);
                    network_fragmentTransaction.commit();

                    ((HomeActivity)getActivity()).displayapps(true,m_jsonshowLinks.toString());
                }




            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.DataObjectHolder> {
        ArrayList<SeasonsBean> seasonsBeans;
        Context context;


        public ShowsAdapter(Context context, ArrayList<SeasonsBean> seasonsBeans) {
            this.context = context;
            this.seasonsBeans = seasonsBeans;

        }


        @Override
        public ShowsAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.season_episode_layout, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ShowsAdapter.DataObjectHolder holder, final int position) {
            holder.seasons_episodes_name.setText(seasonsBeans.get(position).getName());
            holder.seasons_episodes_poster.loadImage(seasonsBeans.get(position).getPoster_url());

            holder.seasons_episodes_poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image_url = seasonsBeans.get(position).getPoster_url();
                    mEpisodeId = seasonsBeans.get(position).getId();
                    HomeActivity.episodeId = seasonsBeans.get(position).getId();
                    HomeActivity.Selecteddetails = Constants.DEATILS_SUBCONTENT;
                    FragmentMovieMain.mProgressBar(true);
                    new EpisodeDetailsLoading().execute();
                }
            });
        }

        @Override
        public int getItemCount() {
            return seasonsBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView seasons_episodes_poster;
            TextView seasons_episodes_name;
            TextView seasons_episodes_date;

            public DataObjectHolder(View itemView) {
                super(itemView);
                seasons_episodes_poster = (DynamicImageView) itemView.findViewById(R.id.seasons_episodes_poster);
                seasons_episodes_name = (TextView) itemView.findViewById(R.id.seasons_episodes_name);
                seasons_episodes_date = (TextView) itemView.findViewById(R.id.seasons_episodes_date);

                Utilities.setViewFocus(getActivity(), seasons_episodes_poster);
            }
        }
    }


    private void showPersonalizeDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentDetailsDialog dialogFragment = new FragmentDetailsDialog();
        dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
    }




    private void refreshlist() {
        if (selected_app_format.equals("android")) {
            displayAndroidapps(isswitchselected);
        } else if (selected_app_format.equals("web")) {
            displayWebapps(isswitchselected);
        } else if (selected_app_format.equals("ios")) {
            displayIosApps(isswitchselected);
        }
    }

    private void displayIosApps(boolean isswitchselected) {

        selected_app_format = "ios";
        android_imageView.setVisibility(View.VISIBLE);
        apple_imageView.setVisibility(View.GONE);
        desktop_imageView.setVisibility(View.VISIBLE);

        android_details.setVisibility(View.GONE);
        ios_details.setVisibility(View.VISIBLE);
        web_details.setVisibility(View.GONE);
        if(isswitchselected){


            if (paid_ios_sd_list.size() > 0) {
                sd_textView.setVisibility(View.VISIBLE);
            } else {
                sd_textView.setVisibility(View.GONE);
            }

            if (paid_ios_hd_list.size() > 0) {
                hd_textView.setVisibility(View.VISIBLE);
            } else {
                hd_textView.setVisibility(View.GONE);
            }

            if (paid_ios_hdx_list.size() > 0) {
                hdx_textView.setVisibility(View.VISIBLE);
            } else {
                hdx_textView.setVisibility(View.GONE);
            }


            if (paid_ios_sd_list.size() > 0) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                sd_textView.setTypeface(OpenSans_Bold);

                hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                hd_textView.setTypeface(OpenSans_Regular);

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                hdx_textView.setTypeface(OpenSans_Regular);

                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_sd_list);
                ios_details.setAdapter(moviePaymentAdapter);
            } else if (paid_ios_hd_list.size() > 0) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                sd_textView.setTypeface(OpenSans_Regular);

                hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                hd_textView.setTypeface(OpenSans_Bold);

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                hdx_textView.setTypeface(OpenSans_Regular);

                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_hd_list);
                ios_details.setAdapter(moviePaymentAdapter);
            } else if (paid_ios_hdx_list.size() > 0) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                sd_textView.setTypeface(OpenSans_Regular);

                hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                hd_textView.setTypeface(OpenSans_Regular);

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                hdx_textView.setTypeface(OpenSans_Bold);

                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_hdx_list);
                ios_details.setAdapter(moviePaymentAdapter);
            } else if (paid_ios_other.size() > 0) {
                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_other);
                ios_details.setAdapter(moviePaymentAdapter);
            }else{
                ios_details.setVisibility(View.GONE);
            }

        }else{

            if (free_ios_sd_list.size() > 0) {
                sd_textView.setVisibility(View.VISIBLE);
            } else {
                sd_textView.setVisibility(View.GONE);
            }

            if (free_ios_hd_list.size() > 0) {
                hd_textView.setVisibility(View.VISIBLE);
            } else {
                hd_textView.setVisibility(View.GONE);
            }

            if (free_ios_hdx_list.size() > 0) {
                hdx_textView.setVisibility(View.VISIBLE);
            } else {
                hdx_textView.setVisibility(View.GONE);
            }

            if((free_ios_sd_list.size() ==0)&&(free_ios_hd_list.size() ==0)&&(free_ios_hdx_list.size() == 0)){
                swap_switch();
            }else{

                if (free_ios_sd_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    sd_textView.setTypeface(OpenSans_Bold);

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hd_textView.setTypeface(OpenSans_Regular);

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hdx_textView.setTypeface(OpenSans_Regular);

                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_ios_sd_list);
                    ios_details.setAdapter(moviePaymentAdapter);
                } else if (free_ios_hd_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    sd_textView.setTypeface(OpenSans_Regular);

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    hd_textView.setTypeface(OpenSans_Bold);

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hdx_textView.setTypeface(OpenSans_Regular);

                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_ios_hd_list);
                    ios_details.setAdapter(moviePaymentAdapter);
                } else if (free_ios_hdx_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    sd_textView.setTypeface(OpenSans_Regular);

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hd_textView.setTypeface(OpenSans_Regular);

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    hdx_textView.setTypeface(OpenSans_Bold);

                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_ios_hdx_list);
                    ios_details.setAdapter(moviePaymentAdapter);
                } else if (free_ios_other.size() > 0) {
                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_ios_other);
                    ios_details.setAdapter(moviePaymentAdapter);
                }else{
                    ios_details.setVisibility(View.GONE);
                }

            }


        }
    }

    private void displayWebapps(boolean isswitchselected) {
        selected_app_format = "web";

        android_details.setVisibility(View.GONE);
        ios_details.setVisibility(View.GONE);
        web_details.setVisibility(View.VISIBLE);
        android_imageView.setVisibility(View.VISIBLE);
        apple_imageView.setVisibility(View.VISIBLE);
        desktop_imageView.setVisibility(View.GONE);
        if(isswitchselected){


            if (paid_web_sd_list.size() > 0) {
                sd_textView.setVisibility(View.VISIBLE);
            } else {
                sd_textView.setVisibility(View.GONE);
            }

            if (paid_web_hd_list.size() > 0) {
                hd_textView.setVisibility(View.VISIBLE);
            } else {
                hd_textView.setVisibility(View.GONE);
            }

            if (paid_web_hdx_list.size() > 0) {
                hdx_textView.setVisibility(View.VISIBLE);
            } else {
                hdx_textView.setVisibility(View.GONE);
            }


            if (paid_web_sd_list.size() > 0) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_sd_list);
                web_details.setAdapter(moviePaymentAdapter);
            } else if (paid_web_hd_list.size() > 0) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_hd_list);
                web_details.setAdapter(moviePaymentAdapter);
            } else if (paid_web_hdx_list.size() > 0) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_hdx_list);
                web_details.setAdapter(moviePaymentAdapter);
            } else if (paid_web_other.size() > 0) {
                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_other);
                web_details.setAdapter(moviePaymentAdapter);
            }else{
                web_details.setVisibility(View.GONE);
            }

        }else{

            if (free_web_sd_list.size() > 0) {
                sd_textView.setVisibility(View.VISIBLE);
            } else {
                sd_textView.setVisibility(View.GONE);
            }

            if (free_web_hd_list.size() > 0) {
                hd_textView.setVisibility(View.VISIBLE);
            } else {
                hd_textView.setVisibility(View.GONE);
            }

            if (free_web_hdx_list.size() > 0) {
                hdx_textView.setVisibility(View.VISIBLE);
            } else {
                hdx_textView.setVisibility(View.GONE);
            }
            if((free_web_sd_list.size()==0)&&(free_web_hd_list.size() == 0)&&(free_web_hdx_list.size() == 0)){
                swap_switch();
            }else{


                if (free_web_sd_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_web_sd_list);
                    web_details.setAdapter(moviePaymentAdapter);
                } else if (free_web_hd_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_web_hd_list);
                    web_details.setAdapter(moviePaymentAdapter);
                } else if (free_web_hdx_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_web_hdx_list);
                    web_details.setAdapter(moviePaymentAdapter);
                } else if (paid_web_other.size() > 0) {
                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_web_other);
                    web_details.setAdapter(moviePaymentAdapter);
                }else{
                    web_details.setVisibility(View.GONE);
                }
            }



        }
    }

    private void displayAndroidapps(boolean bool) {
        selected_app_format = "android";
        android_details.setVisibility(View.VISIBLE);
        ios_details.setVisibility(View.GONE);
        web_details.setVisibility(View.GONE);

        android_imageView.setVisibility(View.GONE);
        apple_imageView.setVisibility(View.VISIBLE);
        desktop_imageView.setVisibility(View.VISIBLE);

        if(bool){
            if (paid_sd_list.size() > 0) {
                sd_textView.setVisibility(View.VISIBLE);
            } else {
                sd_textView.setVisibility(View.GONE);
            }

            if (paid_hd_list.size() > 0) {
                hd_textView.setVisibility(View.VISIBLE);
            } else {
                hd_textView.setVisibility(View.GONE);
            }

            if (paid_hdx_list.size() > 0) {
                hdx_textView.setVisibility(View.VISIBLE);
            } else {
                hdx_textView.setVisibility(View.GONE);
            }


            if (paid_sd_list.size() > 0) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_sd_list);
                android_details.setAdapter(moviePaymentAdapter);
            } else if (paid_hd_list.size() > 0) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                hd_textView.setTypeface(OpenSans_Bold);

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                hdx_textView.setTypeface(OpenSans_Regular);

                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hd_list);
                android_details.setAdapter(moviePaymentAdapter);
            } else if (paid_hdx_list.size() > 0) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                sd_textView.setTypeface(OpenSans_Regular);

                hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                hd_textView.setTypeface(OpenSans_Regular);

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                hdx_textView.setTypeface(OpenSans_Bold);

                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hdx_list);
                android_details.setAdapter(moviePaymentAdapter);
            } else if (paid_other.size() > 0) {
                MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_other);
                android_details.setAdapter(moviePaymentAdapter);
            }else{
                android_details.setVisibility(View.GONE);
            }

        }else{
            if (free_sd_list.size() > 0) {
                sd_textView.setVisibility(View.VISIBLE);
            } else {
                sd_textView.setVisibility(View.GONE);
            }

            if (free_hd_list.size() > 0) {
                hd_textView.setVisibility(View.VISIBLE);
            } else {
                hd_textView.setVisibility(View.GONE);
            }

            if (free_hdx_list.size() > 0) {
                hdx_textView.setVisibility(View.VISIBLE);
            } else {
                hdx_textView.setVisibility(View.GONE);
            }

            if((free_sd_list.size() == 0)&&(free_hd_list.size() == 0)&&(free_hdx_list.size() ==0)){
                swap_switch();
            }else{
                if (free_sd_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_sd_list);
                    android_details.setAdapter(moviePaymentAdapter);
                } else if (free_hd_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    hd_textView.setTypeface(OpenSans_Bold);

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hdx_textView.setTypeface(OpenSans_Regular);

                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_hd_list);
                    android_details.setAdapter(moviePaymentAdapter);
                } else if (free_hdx_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    sd_textView.setTypeface(OpenSans_Regular);

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hd_textView.setTypeface(OpenSans_Regular);

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    hdx_textView.setTypeface(OpenSans_Bold);

                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_hdx_list);
                    android_details.setAdapter(moviePaymentAdapter);
                } else if (free_other.size() > 0) {
                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_other);
                    android_details.setAdapter(moviePaymentAdapter);
                }else{
                    android_details.setVisibility(View.GONE);
                }

            }




        }
        if(paid_sd_list.size()==0&&paid_hd_list.size()==0&&paid_hdx_list.size()==0){
            buy_textView.setVisibility(View.GONE);
            switch_image.setVisibility(View.GONE);
        }

        if(free_sd_list.size()==0&&free_hd_list.size()==0&&free_hdx_list.size()==0){
            free_textView.setVisibility(View.GONE);
            switch_image.setVisibility(View.GONE);

        }


    }

    public static void mShowWatchFree(boolean b)
    {
        if(b)
        {
            movies_watch_free_button.setVisibility(View.GONE);


            if(movies_watch_free_button.getVisibility()==View.GONE)
            {
                FragmentMovieMain.mProgressBar(false);
            }else
            {
                FragmentMovieMain.mProgressBar(true);
            }

        }else
        {
            movies_watch_free_button.setVisibility(View.GONE);
            FragmentMovieMain.mProgressBar(false);
        }

    }

    private class AddFavorites extends AsyncTask<String, Object, Object> {
        JSONObject m_response;
        String task="";
        int id;
        String name="";
        boolean sessionExpired=false;
        String message="";

        @Override
        protected Object doInBackground(String... params) {
            try {
                Log.d("accesstoken::","::"+ PreferenceManager.getAccessToken(getActivity()));

                if(params[0].equals("add")){
                    m_response = JSONRPCAPI.addToFavorite(PreferenceManager.getAccessToken(getActivity()),params[1],Integer.parseInt(params[2]));
                    if ( m_response == null) return null;
                    Log.d("m_response::", "::" + m_response);
                }else  if(params[0].equals("remove")){
                    m_response = JSONRPCAPI.removeFavorite(PreferenceManager.getAccessToken(getActivity()), params[0], Integer.parseInt(params[1]));
                    if ( m_response == null) return null;
                    Log.d("m_response::", "::" + m_response);
                }

                if(m_response.has("name")){
                    if(m_response.getString("name").equalsIgnoreCase("JSONRPCError")){
                        if(m_response.has("message")){
                            message=m_response.getString("message");
                            if(message.contains("Invalide or expired token")){
                                sessionExpired=true;
                            }
                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if(sessionExpired){
                    try {
                        Toast.makeText(getActivity(),"Invalide or expired token",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PreferenceManager.setLogin(false,getActivity());
                    PreferenceManager.setAccessToken("", getActivity());

                    PreferenceManager.setusername("",getActivity());
                    PreferenceManager.setcity("", getActivity());
                    PreferenceManager.setfirst_name("", getActivity());
                    PreferenceManager.setlast_name("", getActivity());
                    PreferenceManager.setgender("", getActivity());
                    PreferenceManager.setemail("", getActivity());
                    PreferenceManager.setstate("", getActivity());
                    PreferenceManager.setdate_of_birth("", getActivity());
                    PreferenceManager.setlast_login("", getActivity());
                    PreferenceManager.setaddress_1("", getActivity());
                    PreferenceManager.setaddress_2("", getActivity());
                    PreferenceManager.setpostal_code("", getActivity());
                    PreferenceManager.setphone_number("", getActivity());
                    PreferenceManager.setid(0, getActivity());

                    Intent in=new Intent(getActivity(),LoginActivity.class);
                    startActivity(in);
                    getActivity().finish();
                }else{
                    if(m_response.has("success")||m_response.has("sucess")){
                        if(m_response.getBoolean("success")||m_response.getBoolean("sucess")){

                            //HomeActivity.channelsList.add(new FavoriteBean("","",name,id,"","",""));
                            Toast.makeText(getActivity(),"Successfully Added",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
                        }
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
