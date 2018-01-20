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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
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
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentEpisodeorMovieDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView episodes_name, sd_textView, hd_textView, hdx_textView, rating_text, runtime,network_text,network,runtime_text;
    private TextView episodes_detail;
    private TextView episodes_description;
    private View episode_free_left;
    private View episode_free_top;
    private TextView episode_free;
    private View episode_free_bottom;
    private View episode_showall_left;
    private View episode_showall_top;
    private TextView episode_showall;
    private View episode_showall_bottom;
    private View movie_showall_right;
    private RecyclerView episode_free_details;
    private RecyclerView episode_showall_details;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private String details;
    private String list;
    private JSONArray m_jsonArrayFreeItems, m_jsonArrayiosFreeItems, m_jsonArraywebFreeItems;
    private JSONArray m_jsonArrayPaidItems, m_jsonArrayiosPaidItems, m_jsonArraywebPaidItems;
    private GridLayoutManager mLayoutManager;
    private String episode;
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

    RecyclerView android_details,
            ios_details,
            web_details;
    private LinearLayout layout_payment_details, layout_ios_genre, layout_android_overview, web_cast;
    TextView android__title,
            ios_title,
            web_title;
    private TextView show_more_textview;
    private AQuery aq;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEpisodeorMovieDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentEpisodeorMovieDetails newInstance(String param1, String param2, String episode) {
        FragmentEpisodeorMovieDetails fragment = new FragmentEpisodeorMovieDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, episode);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentEpisodeorMovieDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            details = getArguments().getString(ARG_PARAM1);
            list = getArguments().getString(ARG_PARAM2);
            episode = getArguments().getString(ARG_PARAM3);
            Log.d("details:::",":::::"+details);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_episodeor_movie_details, container, false);
        aq=new AQuery(getActivity());

        episodes_name = (TextView) view.findViewById(R.id.episodes_name);
        rating_text = (TextView) view.findViewById(R.id.rating_text);
        runtime_text = (TextView) view.findViewById(R.id.runtime_text);
        runtime = (TextView) view.findViewById(R.id.runtime);
        network_text = (TextView) view.findViewById(R.id.network_text);
        network = (TextView) view.findViewById(R.id.network);
        episodes_detail = (TextView) view.findViewById(R.id.episodes_detail);
        episodes_description = (TextView) view.findViewById(R.id.episodes_description);
        episode_free = (TextView) view.findViewById(R.id.episode_free);
        episode_showall = (TextView) view.findViewById(R.id.episode_showall);
        sd_textView = (TextView) view.findViewById(R.id.sd_textView);
        hd_textView = (TextView) view.findViewById(R.id.hd_textView);
        hdx_textView = (TextView) view.findViewById(R.id.hdx_textView);
        episode_free_left = (View) view.findViewById(R.id.episode_free_left);
        episode_free_top = (View) view.findViewById(R.id.episode_free_top);
        episode_free_bottom = (View) view.findViewById(R.id.episode_free_bottom);
        episode_showall_left = (View) view.findViewById(R.id.episode_showall_left);
        episode_showall_top = (View) view.findViewById(R.id.episode_showall_top);
        episode_showall_bottom = (View) view.findViewById(R.id.episode_showall_bottom);
        movie_showall_right = (View) view.findViewById(R.id.movie_showall_right);
        episode_free_details = (RecyclerView) view.findViewById(R.id.episode_free_details);
        episode_showall_details = (RecyclerView) view.findViewById(R.id.episode_showall_details);
        episodes_description.setMovementMethod(new ScrollingMovementMethod());


        layout_payment_details = (LinearLayout) view.findViewById(R.id.layout_payment_details);
        layout_android_overview = (LinearLayout) view.findViewById(R.id.layout_android_overview);
        android__title = (TextView) view.findViewById(R.id.android__title);
        ios_title = (TextView) view.findViewById(R.id.ios_title);
        web_title = (TextView) view.findViewById(R.id.web_title);
        layout_ios_genre = (LinearLayout) view.findViewById(R.id.layout_ios_genre);
        web_cast = (LinearLayout) view.findViewById(R.id.web_cast);
        android_left = (View) view.findViewById(R.id.android_left);
        android__top = (View) view.findViewById(R.id.android__top);
        android__bottom = (View) view.findViewById(R.id.android__bottom);
        web_left = (View) view.findViewById(R.id.web_left);
        web_top = (View) view.findViewById(R.id.web_top);
        web_bottom = (View) view.findViewById(R.id.web_bottom);
        ios_left = (View) view.findViewById(R.id.ios_left);
        ios_top = (View) view.findViewById(R.id.ios_top);
        ios_bottom = (View) view.findViewById(R.id.ios_bottom);
        ios_right = (View) view.findViewById(R.id.ios_right);
        android_details = (RecyclerView) view.findViewById(R.id.android_details);
        ios_details = (RecyclerView) view.findViewById(R.id.ios_details);
        web_details = (RecyclerView) view.findViewById(R.id.web_details);

        show_more_textview=(TextView)view.findViewById(R.id.show_more_textview);

        text_font_typeface();
        episodes_description.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                episodes_description.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });
        show_more_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_more_textview.getText().equals("less..")) {
                    episodes_description.setMaxLines(1);
                    show_more_textview.setText("more..");
                } else {
                    episodes_description.setMaxLines(100);
                    show_more_textview.setText("less..");
                }

            }
        });



        episodes_name.setTypeface(OpenSans_Bold);
        rating_text.setTypeface(OpenSans_Regular);
        runtime.setTypeface(OpenSans_Regular);
        network_text.setTypeface(OpenSans_Regular);
        network.setTypeface(OpenSans_Regular);
        episode_free.setTypeface(OpenSans_Regular);
        episode_showall.setTypeface(OpenSans_Regular);
        episodes_detail.setTypeface(OpenSans_Regular);
        episodes_description.setTypeface(OpenSans_Regular);

        makeDetail();
        makelistings();
        /*if(free_sd_list.size()>0){
            sd_textView.setVisibility(View.VISIBLE);
        }else{
            sd_textView.setVisibility(View.GONE);
        }

        if(free_hd_list.size()>0){
            hd_textView.setVisibility(View.VISIBLE);
        }else{
            hd_textView.setVisibility(View.GONE);
        }

        if(free_hdx_list.size()>0){
            hdx_textView.setVisibility(View.VISIBLE);
        }else{
            hdx_textView.setVisibility(View.GONE);
        }

        if(free_sd_list.size()>0){
            sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
            sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sd_textView.setTypeface(OpenSans_Bold);

            hd_textView.setBackgroundResource(R.drawable.app_format_bg);
            hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
            hd_textView.setTypeface(OpenSans_Regular);

            hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
            hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
            hdx_textView.setTypeface(OpenSans_Regular);

            MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_sd_list);
            episode_free_details.setAdapter(moviePaymentAdapter);
        }else if(free_hd_list.size()>0){
            sd_textView.setBackgroundResource(R.drawable.app_format_bg);
            sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
            sd_textView.setTypeface(OpenSans_Regular);

            hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
            hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            hd_textView.setTypeface(OpenSans_Bold);

            hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
            hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
            hdx_textView.setTypeface(OpenSans_Regular);

            MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_hd_list);
            episode_free_details.setAdapter(moviePaymentAdapter);
        }else  if(free_hdx_list.size()>0){
            sd_textView.setBackgroundResource(R.drawable.app_format_bg);
            sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
            sd_textView.setTypeface(OpenSans_Regular);

            hd_textView.setBackgroundResource(R.drawable.app_format_bg);
            hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
            hd_textView.setTypeface(OpenSans_Regular);

            hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
            hdx_textView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            hdx_textView.setTypeface(OpenSans_Bold);

            MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_hdx_list);
            episode_free_details.setAdapter(moviePaymentAdapter);
        }else  if(free_other.size()>0){
            MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_other);
            episode_free_details.setAdapter(moviePaymentAdapter);
        }*/

        episode_showall_details.setVisibility(View.GONE);
        episode_free_details.setVisibility(View.VISIBLE);
        mLayoutManager = new GridLayoutManager(getActivity(), 4);
        episode_free_details.setLayoutManager(mLayoutManager);

        mLayoutManager = new GridLayoutManager(getActivity(), 4);
        episode_showall_details.setLayoutManager(mLayoutManager);

        Utilities.setViewFocus(getActivity(), episode_free);
        Utilities.setViewFocus(getActivity(), episode_showall);


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


        ios_details.setVisibility(View.GONE);
        web_details.setVisibility(View.GONE);
        android_details.setVisibility(View.VISIBLE);
        mLayoutManager = new GridLayoutManager(getActivity(), 4);
        android_details.setLayoutManager(mLayoutManager);

        mLayoutManager = new GridLayoutManager(getActivity(), 4);
        web_details.setLayoutManager(mLayoutManager);
        mLayoutManager = new GridLayoutManager(getActivity(), 4);
        ios_details.setLayoutManager(mLayoutManager);
        Utilities.setViewFocus(getActivity(), android_details);
        Utilities.setViewFocus(getActivity(), ios_details);
        Utilities.setViewFocus(getActivity(), web_details);
        android__title.setTypeface(OpenSans_Bold);
        ios_title.setTypeface(OpenSans_Regular);
        web_title.setTypeface(OpenSans_Regular);

        layout_android_overview.setOnClickListener(new View.OnClickListener() {
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


        episode_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_app_format = "free";

                episode_free_left.setVisibility(View.VISIBLE);
                episode_free_top.setVisibility(View.VISIBLE);
                episode_showall_left.setVisibility(View.VISIBLE);
                episode_showall_bottom.setVisibility(View.VISIBLE);
                movie_showall_right.setVisibility(View.GONE);
                episode_free_bottom.setVisibility(View.GONE);
                episode_showall_top.setVisibility(View.GONE);

                episode_showall_details.setVisibility(View.GONE);
                episode_free_details.setVisibility(View.VISIBLE);

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

                if (free_sd_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    sd_textView.setTypeface(OpenSans_Bold);

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hd_textView.setTypeface(OpenSans_Regular);

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hdx_textView.setTypeface(OpenSans_Regular);
                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_sd_list);
                    episode_free_details.setAdapter(moviePaymentAdapter);
                } else if (free_hd_list.size() > 0) {
                    sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                    sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    sd_textView.setTypeface(OpenSans_Regular);

                    hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                    hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    hd_textView.setTypeface(OpenSans_Bold);

                    hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                    hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                    hdx_textView.setTypeface(OpenSans_Regular);

                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_hd_list);
                    episode_free_details.setAdapter(moviePaymentAdapter);
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
                    episode_free_details.setAdapter(moviePaymentAdapter);
                } else if (free_other.size() > 0) {
                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_other);
                    episode_free_details.setAdapter(moviePaymentAdapter);
                }
            }
        });

        episode_showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                selected_app_format = "all";
                episode_free_left.setVisibility(View.GONE);
                episode_free_top.setVisibility(View.GONE);
                episode_showall_left.setVisibility(View.VISIBLE);
                episode_showall_bottom.setVisibility(View.GONE);
                movie_showall_right.setVisibility(View.VISIBLE);
                episode_free_bottom.setVisibility(View.VISIBLE);
                episode_showall_top.setVisibility(View.VISIBLE);

                episode_free_details.setVisibility(View.GONE);
                episode_showall_details.setVisibility(View.VISIBLE);


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
                    episode_showall_details.setAdapter(moviePaymentAdapter);
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
                    episode_showall_details.setAdapter(moviePaymentAdapter);
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
                    episode_showall_details.setAdapter(moviePaymentAdapter);
                } else if (free_other.size() > 0) {
                    MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_other);
                    episode_free_details.setAdapter(moviePaymentAdapter);
                }
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

        return view;
    }

    private void makelistings() {
        try {
            JSONObject m_jsonMovieLinks = new JSONObject(list);

            try {
                if (m_jsonMovieLinks == null) {
                    m_jsonArrayFreeItems = null;
                    m_jsonArrayPaidItems = null;
                    m_jsonArraywebFreeItems = null;
                    m_jsonArraywebPaidItems = null;
                    m_jsonArrayiosFreeItems = null;
                    m_jsonArrayiosPaidItems = null;

                } else {
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
                                Log.d("Source::", "Source::" + source);
                                String subscription_code="";
                                if(jsonObject.has("subscription_code")){
                                    subscription_code=jsonObject.getString("subscription_code");
                                }
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
                                        Log.d("Source1::", "Source::" + source);
                                    }
                                } else {
                                    free_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "Subscription", "", display_name, app_required, app_link,image,subscription_code));
                                    Log.d("Source2::", "Source::" + source);
                                }
                            }
                            paid_sd_list.addAll(free_sd_list);
                            paid_hd_list.addAll(free_hd_list);
                            paid_hdx_list.addAll(free_hdx_list);
                            paid_other.addAll(free_other);

                            paid_sd_list.addAll(paid_other);
                            paid_hd_list.addAll(paid_other);
                            paid_hdx_list.addAll(paid_other);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    m_jsonArrayiosFreeItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("ios").getJSONArray("free");
                    m_jsonArrayiosPaidItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("ios").getJSONArray("paid");


                    try {


                       /* if (m_jsonArrayiosFreeItems.length() > 0) {
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
                                            free_ios_other.add(new AppFormatBean(source, link, app_name, app_download_link, "$0.0", "", "", display_name, app_required, app_link,image));
                                        }
                                    } else {
                                        free_ios_other.add(new AppFormatBean(source, link, app_name, app_download_link, "$0.0", "", "", display_name, app_required, app_link,image));
                                    }
                                }
                            }
                        }*/

                    } catch (Exception e)

                    {
                        e.printStackTrace();
                    }


                    try {
                        /*if (m_jsonArrayiosPaidItems.length() > 0) {
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
                                        paid_ios_other.add(new AppFormatBean(source, link, app_name, app_download_link, "$0.0", "", "", display_name, app_required, app_link,image));
                                    }
                                } else {
                                    paid_ios_other.add(new AppFormatBean(source, link, app_name, app_download_link, "$0.0", "", "", display_name, app_required, app_link,image));
                                }
                            }
                            paid_ios_sd_list.addAll(free_ios_sd_list);
                            paid_ios_hd_list.addAll(free_ios_hd_list);
                            paid_ios_hdx_list.addAll(free_ios_hdx_list);
                            paid_ios_other.addAll(free_ios_other);

                            paid_ios_sd_list.addAll(paid_ios_other);
                            paid_ios_hd_list.addAll(paid_ios_other);
                            paid_ios_hdx_list.addAll(paid_ios_other);
                        }
*/

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
                                                    free_web_sd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                                } else if (formats_format.equalsIgnoreCase("hd")) {
                                                    free_web_hd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                                } else if (formats_format.equalsIgnoreCase("hdx")) {
                                                    free_web_hdx_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link,image));
                                                }


                                            }
                                        } else {
                                            free_web_other.add(new AppFormatBean(source, link, app_name, app_download_link, "$0.0", "", "", display_name, app_required, app_link,image));
                                        }
                                    } else {
                                        free_web_other.add(new AppFormatBean(source, link, app_name, app_download_link, "$0.0", "", "", display_name, app_required, app_link,image));
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

                                    } else {
                                        paid_web_other.add(new AppFormatBean(source, link, app_name, app_download_link, "$0.0", "", "", display_name, app_required, app_link,image));
                                    }
                                } else {
                                    paid_web_other.add(new AppFormatBean(source, link, app_name, app_download_link, "$0.0", "", "", display_name, app_required, app_link,image));
                                }
                            }
                            paid_web_sd_list.addAll(free_web_sd_list);
                            paid_web_hd_list.addAll(free_web_hd_list);
                            paid_web_hdx_list.addAll(free_web_hdx_list);
                            paid_web_other.addAll(free_web_other);

                            paid_web_sd_list.addAll(paid_web_other);
                            paid_web_hd_list.addAll(paid_web_other);
                            paid_web_hdx_list.addAll(paid_web_other);
                        }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeDetail() {
        try {
            JSONObject data_object = new JSONObject(details);

            String strName = null;
            String strDescription = null,net="";
            int nRunTime = 0;
            String strRating = null;
            try {
                if (data_object.has("name")) {
                    strName = data_object.getString("name").trim();
                }

                if (data_object.has("description")) {
                    strDescription = data_object.getString("description").trim();
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

                /*if(data_object.has("actors"))
                {
                    JSONArray actors = data_object.getJSONArray("actors");

                    movieCastAndGenreAdapter=new MovieCastAndGenreAdapter(getActivity(),actors);
                    movie_cast_details.setAdapter(movieCastAndGenreAdapter);
                }
*/
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

                episodes_name.setText(strName);
                episodes_description.setText(strDescription);
                episodes_description.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int lineCount = episodes_description.getLineCount();
                            if(lineCount>1){
                                show_more_textview.setVisibility(View.VISIBLE);
                                episodes_description.setMaxLines(1);
                                show_more_textview.setText("more..");
                            }else{
                                show_more_textview.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // Use lineCount here
                    }
                });
                if(!TextUtils.isEmpty(strRating)&&!strRating.equalsIgnoreCase("null")){
                    rating_text.setText("" + strRating);
                }else{
                    rating_text.setVisibility(View.GONE);
                }


                if (nRunTime != 0) {
                    runtime.setText(nRunTime + " Minutes");
                } else {
                    runtime.setText("N/A");
                    runtime.setVisibility(View.GONE);
                    runtime_text.setVisibility(View.GONE);
                }


            } catch (JSONException e) {
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


    public class MoviePaymentAdapter extends RecyclerView.Adapter<MoviePaymentAdapter.DataObjectHolder> {
        Context context;
        JSONArray jsonBuyItmes;
        private ArrayList<AppFormatBean> list_data;

        public MoviePaymentAdapter(Context context, ArrayList<AppFormatBean> list_data) {
            this.context = context;
            this.list_data = list_data;

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
                strSourceName = list_data.get(position).getSource();
                final String app_name = list_data.get(position).getApp_name();
                Log.d("Source::", "Source::" + strSourceName);
                price = list_data.get(position).getFormats_price();
                holder.movie_payment_textview.setText(price);

                aq.id(holder.movie_payment_imageview).image(list_data.get(position).getImage());

               /* if (strSourceName.equalsIgnoreCase("vudu")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_vudu);
                } else if (strSourceName.equalsIgnoreCase("google_play")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_play);
                } else if (strSourceName.equalsIgnoreCase("hulu_plus")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_bag);
                } else if (strSourceName.equalsIgnoreCase("amazon_buy")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_amazon);
                } else if (strSourceName.equalsIgnoreCase("M-GO")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_mgo);
                } else if (strSourceName.equalsIgnoreCase("youtube_purchase")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_tube);
                } else if (strSourceName.equalsIgnoreCase("crackle")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.crackle);
                } else if (strSourceName.equalsIgnoreCase("itunes")) {
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
                            holder.movie_payment_imageview.setImageResource(R.drawable.xfinity);
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
                            holder.movie_payment_imageview.setImageResource(R.drawable.cw_seed);
                            break;

                        case "Destination America":
                            holder.movie_payment_imageview.setImageResource(R.drawable.destination_america);
                            break;

                        case "DirecTV":
                            holder.movie_payment_imageview.setImageResource(R.drawable.directv);
                            break;

                        case "Discovery Channel":
                            holder.movie_payment_imageview.setImageResource(R.drawable.discovery_channel);
                            break;

                        case "Dish":
                            holder.movie_payment_imageview.setImageResource(R.drawable.dish);
                            break;

                        case "Disney Channel":
                            holder.movie_payment_imageview.setImageResource(R.drawable.disney_channel);
                            break;

                        case "Disney Junior":
                            holder.movie_payment_imageview.setImageResource(R.drawable.disney_junior);
                            break;

                        case "Disney XD":
                            holder.movie_payment_imageview.setImageResource(R.drawable.disney_xd);
                            break;

                        case "DIY":
                            holder.movie_payment_imageview.setImageResource(R.drawable.diy_network);
                            break;

                        case "DramaFever":
                            holder.movie_payment_imageview.setImageResource(R.drawable.dramafever);
                            break;

                        case "FM":
                            holder.movie_payment_imageview.setImageResource(R.drawable.fm);
                            break;

                        case "Food Network":
                            holder.movie_payment_imageview.setImageResource(R.drawable.watch_food_network);
                            break;

                        case "FOX Sports":
                            holder.movie_payment_imageview.setImageResource(R.drawable.fox_sports);
                            break;

                        case "Funny or Die":
                            holder.movie_payment_imageview.setImageResource(R.drawable.funnyordie);
                            break;

                        case "Fuse":
                            holder.movie_payment_imageview.setImageResource(R.drawable.fuse);
                            break;


                        case "Guidebox":
                            holder.movie_payment_imageview.setImageResource(R.drawable.guidebox);
                            break;

                        case "HGTV":
                            holder.movie_payment_imageview.setImageResource(R.drawable.hgtv);
                            break;

                        case "Hub Network":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
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
                            holder.movie_payment_imageview.setImageResource(R.drawable.music_choice);
                            break;

                        case "My Damn Channel":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "National Geographic":
                            holder.movie_payment_imageview.setImageResource(R.drawable.national_geographic);
                            break;

                        case "NBC":
                            holder.movie_payment_imageview.setImageResource(R.drawable.nbc);
                            break;

                        case "Nick Jr.":
                            holder.movie_payment_imageview.setImageResource(R.drawable.nick_jr);
                            break;

                        case "Oprah":
                            holder.movie_payment_imageview.setImageResource(R.drawable.oprah);
                            break;

                        case "PBS Kids":
                            holder.movie_payment_imageview.setImageResource(R.drawable.pbs);
                            break;

                        case "Pop":
                            holder.movie_payment_imageview.setImageResource(R.drawable.pop);
                            break;

                        case "Red Bull TV":
                            holder.movie_payment_imageview.setImageResource(R.drawable.redbull);
                            break;

                        case "Revision3":
                            holder.movie_payment_imageview.setImageResource(R.drawable.revision);
                            break;

                        case "Science Channel":
                            holder.movie_payment_imageview.setImageResource(R.drawable.science_channel);
                            break;

                        case "Shout! Factory TV":
                            holder.movie_payment_imageview.setImageResource(R.drawable.shout_factory);
                            break;

                        case "SnagFilms":
                            holder.movie_payment_imageview.setImageResource(R.drawable.snag);
                            break;

                        case "South Park Studios":
                            holder.movie_payment_imageview.setImageResource(R.drawable.south_park);
                            break;

                        case "Spike TV":
                            holder.movie_payment_imageview.setImageResource(R.drawable.spike);
                            break;

                        case "Style.com":
                            holder.movie_payment_imageview.setImageResource(R.drawable.sundance);
                            break;

                        case "Syfy":
                            holder.movie_payment_imageview.setImageResource(R.drawable.apple);
                            break;

                        case "teamcoco.com":
                            holder.movie_payment_imageview.setImageResource(R.drawable.teamcoco);
                            break;

                        case "The CW":
                            holder.movie_payment_imageview.setImageResource(R.drawable.the_cw);
                            break;

                        case "The Golf Channel":
                            holder.movie_payment_imageview.setImageResource(R.drawable.golf);
                            break;

                        case "The Onion":
                            holder.movie_payment_imageview.setImageResource(R.drawable.the_onion);
                            break;

                        case "thedailyshow.com":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "TMZ":
                            holder.movie_payment_imageview.setImageResource(R.drawable.tmz);
                            break;

                        case "ToonsTV":
                            holder.movie_payment_imageview.setImageResource(R.drawable.toons);
                            break;

                        case "Travel Channel":
                            holder.movie_payment_imageview.setImageResource(R.drawable.travel_channel);
                            break;

                        case "Tubi TV":
                            holder.movie_payment_imageview.setImageResource(R.drawable.tubi);
                            break;

                        case "TV LAND":
                            holder.movie_payment_imageview.setImageResource(R.drawable.tv_land);
                            break;

                        case "ulive":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "Velocity":
                            holder.movie_payment_imageview.setImageResource(R.drawable.velocity);
                            break;

                        case "Viewster":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;


                        case "Vimeo":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "Vogue":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "Watch Disney Channel":
                            holder.movie_payment_imageview.setImageResource(R.drawable.watch_disney_channel);
                            break;

                        case "WGN America":
                            holder.movie_payment_imageview.setImageResource(R.drawable.wgn);
                            break;

                        case "WWE":
                            holder.movie_payment_imageview.setImageResource(R.drawable.wwe);
                            break;

                        case "Yahoo Screen":
                            holder.movie_payment_imageview.setImageResource(R.drawable.yahoo);
                            break;

                        case "Amazon":
                            holder.movie_payment_imageview.setImageResource(R.drawable.amazon_videoplay);
                            break;

                        case "CinemaNow":
                            holder.movie_payment_imageview.setImageResource(R.drawable.cinemanow);
                            break;

                        case "Disney Movies Anywhere":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "Distrify":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "Flixster":
                            holder.movie_payment_imageview.setImageResource(R.drawable.untitled_5);
                            break;

                        case "Paramount Movies":
                            holder.movie_payment_imageview.setImageResource(R.drawable.paramount_movies);
                            break;

                        case "Sony Entertainment Network":
                            holder.movie_payment_imageview.setImageResource(R.drawable.sony);
                            break;

                        case "SundanceNow":
                            holder.movie_payment_imageview.setImageResource(R.drawable.sundance);
                            break;

                        case "Wolfe On Demand":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "ABC News":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "A&E":
                            holder.movie_payment_imageview.setImageResource(R.drawable.ae);
                            break;

                        case "Adult Swim":
                            holder.movie_payment_imageview.setImageResource(R.drawable.as);
                            break;

                        case "Cartoon Network":
                            holder.movie_payment_imageview.setImageResource(R.drawable.cn);
                            break;

                        case "ABC":
                            holder.movie_payment_imageview.setImageResource(R.drawable.abc);
                            break;

                        case "CNBC":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "CNN":
                            holder.movie_payment_imageview.setImageResource(R.drawable.cnn);
                            break;

                        case "Comedy Central":
                            holder.movie_payment_imageview.setImageResource(R.drawable.comedy_central);
                            break;

                        case "Esquire Network":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "FOX":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "FOX Business":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "FOX News":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "FX":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "fyi":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "HLN":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "MTV":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "National Geographic Kids":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "NBC News":
                            holder.movie_payment_imageview.setImageResource(R.drawable.nbc);
                            break;

                        case "Nickelodeon":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "Sprout Now":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "STARZ Play":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "The History Channel":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "truTV":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
                            break;

                        case "VH1":
                            holder.movie_payment_imageview.setImageResource(R.drawable.vh1_logo);
                            break;

                        case "Watch Disney Junior":
                            holder.movie_payment_imageview.setImageResource(R.drawable.disney_junior);
                            break;

                        case "Watch Disney XD":
                            holder.movie_payment_imageview.setImageResource(R.drawable.disney_xd);
                            break;

                        case "Xfinity":
                            holder.movie_payment_imageview.setImageResource(R.drawable.xfinity);
                            break;

                        case "YouTube":
                            holder.movie_payment_imageview.setImageResource(R.drawable.youtube);
                            break;

                        case "Bravo":
                            holder.movie_payment_imageview.setImageResource(R.drawable.playstore);
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
                        new LoadingPackageNameList(app_name, list_data.get(position)).execute();
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
            TextView movie_payment_textview;
            ImageView movie_payment_imageview;

            public DataObjectHolder(View itemView) {
                super(itemView);
                movie_payment_textview = (TextView) itemView.findViewById(R.id.movie_payment_textview);
                movie_payment_imageview = (ImageView) itemView.findViewById(R.id.movie_payment_imageview);

                Utilities.setViewFocus(getActivity(), movie_payment_imageview);
            }
        }
    }

    private class LoadingPackageNameList extends AsyncTask<Object, Object, Object> {
        String appName;
        JSONArray objResList;
        JSONObject jsonObject;
        AppFormatBean appbean;
        String package_name = "";

        LoadingPackageNameList(String appName, AppFormatBean appbean) {
            this.appName = appName;
            objResList = new JSONArray();
            this.appbean = appbean;
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
            makeSourceDialog(package_name, appbean);
        }
    }


    private void makeSourceDialog(String packageName, AppFormatBean obj) {


        try {
            /*if (obj.isApp_required() && isInstalledPackageName(packageName)) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
                startActivity(launchIntent);
            } else {
                if (obj.getApp_name().toLowerCase().equals("google play")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getLink()));
                    if(myIntent!=null){
                        startActivity(myIntent);
                    }else{
                        Toast.makeText(getContext(),"No Application found to perform this action",Toast.LENGTH_SHORT).show();
                    }

                } else {


                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getApp_download_link()));
                    startActivity(myIntent);
                  *//*  Intent intent = new Intent(getActivity().getApplicationContext(), WebBrowserActivity.class);
                    intent.putExtra("url", obj.getString("app_download_link"));
                    intent.putExtra("name", obj.getString("app_name"));
                    startActivity(intent);*//*
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

                }else{
                    /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getApp_download_link()));
                    startActivity(myIntent);*/

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance(""+obj.getApp_name(),""+obj.getImage(),obj.getApp_download_link());
                    dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                }

            }


            //}
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

}

