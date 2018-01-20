package com.selecttvlauncher.ui.fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.selecttvlauncher.BeanClass.AppFormatBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.ui.views.AutofitRecylerview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MoviesAppListing extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String list_data;
    private String mParam2;

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


    private TextView free_textView,buy_textView,sd_textView,hd_textView,hdx_textView,also_textView;
    private ImageView switch_image,android_imageView,desktop_imageView,apple_imageView;
    private AutofitRecylerview android_details,web_details,ios_details;

    private GridLayoutManager mLayoutManager;

    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;

    private boolean isswitchselected=false;
    private FrameLayout payment_details_container_layout;
    private AQuery aq;


    public MoviesAppListing() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesAppListing.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesAppListing newInstance(String param1, String param2) {
        MoviesAppListing fragment = new MoviesAppListing();
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
            mParam2 = getArguments().getString(ARG_PARAM1);
            list_data = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_movies_app_listing, container, false);
        aq=new AQuery(getActivity());

        free_textView=(TextView)view.findViewById(R.id.free_textView);
        buy_textView=(TextView)view.findViewById(R.id.buy_textView);
        sd_textView=(TextView)view.findViewById(R.id.sd_textView);
        hd_textView=(TextView)view.findViewById(R.id.hd_textView);
        hdx_textView=(TextView)view.findViewById(R.id.hdx_textView);
        also_textView=(TextView)view.findViewById(R.id.also_textView);

        switch_image=(ImageView)view.findViewById(R.id.switch_image);
        android_imageView=(ImageView)view.findViewById(R.id.android_imageView);
        desktop_imageView=(ImageView)view.findViewById(R.id.desktop_imageView);
        apple_imageView=(ImageView)view.findViewById(R.id.apple_imageView);

        android_details=(AutofitRecylerview)view.findViewById(R.id.android_details);
        web_details=(AutofitRecylerview)view.findViewById(R.id.web_details);
        ios_details=(AutofitRecylerview)view.findViewById(R.id.ios_details);

        payment_details_container_layout=(FrameLayout)view.findViewById(R.id.payment_details_container_layout);

        text_font_typeface();

        Log.d("::::getWidth()", "::" + payment_details_container_layout.getWidth());
        Log.d("::::getMeasuredWidth()", "::" + payment_details_container_layout.getMeasuredWidth());
        Log.d("::arams().width", "::" + payment_details_container_layout.getLayoutParams().width);

        makelistings();
        displayAndroidapps(isswitchselected);

       /* mLayoutManager = new GridLayoutManager(getActivity(), 4);
        android_details.setLayoutManager(mLayoutManager);

        mLayoutManager = new GridLayoutManager(getActivity(), 4);
        web_details.setLayoutManager(mLayoutManager);
        mLayoutManager = new GridLayoutManager(getActivity(), 4);
        ios_details.setLayoutManager(mLayoutManager);
        Utilities.setViewFocus(getActivity(), android_details);
        Utilities.setViewFocus(getActivity(), ios_details);
        Utilities.setViewFocus(getActivity(), web_details);*/

        free_textView.setTypeface(OpenSans_Bold);
        buy_textView.setTypeface(OpenSans_Bold);
        also_textView.setTypeface(OpenSans_Regular);

        switch_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swap_switch();
            }
        });

        sd_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                sd_textView.setTypeface(OpenSans_Bold);

                hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                hd_textView.setTypeface(OpenSans_Regular);

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                hdx_textView.setTypeface(OpenSans_Regular);


                if (selected_app_format.equalsIgnoreCase("android")) {
                    if(isswitchselected){
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_sd_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    }else{
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_sd_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    }

                } else if (selected_app_format.equalsIgnoreCase("ios")) {
                    if(isswitchselected){
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_sd_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    }else{
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_ios_sd_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    }

                } else if (selected_app_format.equalsIgnoreCase("web")) {
                    if(isswitchselected){
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_sd_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    }else{
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_web_sd_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    }

                }
            }
        });
        hd_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                sd_textView.setTypeface(OpenSans_Regular);

                hd_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                hd_textView.setTypeface(OpenSans_Bold);

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_lite_blue));
                hdx_textView.setTypeface(OpenSans_Regular);

                if (selected_app_format.equalsIgnoreCase("android")) {
                    if(isswitchselected){
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hd_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    }else{
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_hd_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    }

                } else if (selected_app_format.equalsIgnoreCase("ios")) {
                    if(isswitchselected){
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_hd_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    }else{
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_ios_hd_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    }

                } else if (selected_app_format.equalsIgnoreCase("web")) {
                    if(isswitchselected){
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_hd_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    }else{
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_web_hd_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    }

                }
            }
        });
        hdx_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sd_textView.setBackgroundResource(R.drawable.app_format_bg);
                sd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                sd_textView.setTypeface(OpenSans_Regular);

                hd_textView.setBackgroundResource(R.drawable.app_format_bg);
                hd_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                hd_textView.setTypeface(OpenSans_Regular);

                hdx_textView.setBackgroundResource(R.drawable.app_format_bg_selected);
                hdx_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                hdx_textView.setTypeface(OpenSans_Bold);

                if (selected_app_format.equalsIgnoreCase("android")) {
                    if(isswitchselected){
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_hdx_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    }else{
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_hdx_list);
                        android_details.setAdapter(moviePaymentAdapter);
                    }

                } else if (selected_app_format.equalsIgnoreCase("ios")) {
                    if(isswitchselected){
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_ios_hdx_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    }else{
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_ios_hdx_list);
                        ios_details.setAdapter(moviePaymentAdapter);
                    }

                } else if (selected_app_format.equalsIgnoreCase("web")) {
                    if(isswitchselected){
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), paid_web_hdx_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    }else {
                        MoviePaymentAdapter moviePaymentAdapter = new MoviePaymentAdapter(getActivity(), free_web_hdx_list);
                        web_details.setAdapter(moviePaymentAdapter);
                    }
                }
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

        return view;
    }

    private void swap_switch() {

        if (isswitchselected) {
            isswitchselected = false;
            switch_image.setImageResource(R.drawable.off);
            refreshlist();
            free_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            buy_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_light_grey));


        } else {
            isswitchselected = true;
            switch_image.setImageResource(R.drawable.on);
            refreshlist();
            buy_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            free_textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_light_grey));
        }

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
        android_imageView.setVisibility(View.VISIBLE);
        apple_imageView.setVisibility(View.VISIBLE);
        desktop_imageView.setVisibility(View.GONE);

        android_details.setVisibility(View.GONE);
        ios_details.setVisibility(View.GONE);
        web_details.setVisibility(View.VISIBLE);
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


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void makelistings() {

        try {
            JSONObject m_jsonMovieLinks = new JSONObject(list_data);

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
                            final String image = jsonObject.getString("image");
                            final boolean app_required = jsonObject.getBoolean("app_required");
                            final boolean app_link = jsonObject.getBoolean("app_link");
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
            free_sd_list.addAll(free_other);


            paid_sd_list.addAll(paid_other);
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

               /* if (m_jsonArraywebFreeItems.length() > 0) {
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

               /* if (m_jsonArraywebPaidItems.length() > 0) {
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

                /*if (strSourceName.equalsIgnoreCase("vudu")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_vudu);
                } else if (strSourceName.equalsIgnoreCase("google_play")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.icon_play);
                } else if (strSourceName.equalsIgnoreCase("hulu_plus")) {
                    holder.movie_payment_imageview.setImageResource(R.drawable.hulu_plus);
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


    private class LoadingPackageNameList extends AsyncTask<Object, Object, Object> {
        String appName;
        JSONArray objResList;
        AppFormatBean jsonObject;
        String package_name = "",downloadurl="";

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
                    downloadurl=objPackage.getString("link");
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
            makeSourceDialog(package_name, jsonObject,downloadurl);
        }
    }

    private void makeSourceDialog(String packageName, AppFormatBean obj,String download_url) {


        try {
            if (isInstalledPackageName(packageName)) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getLink()));
                if (myIntent != null) {
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (obj.getApp_name().toLowerCase().equals("google play")) {
                    Log.d("redirectlink::;","::"+Uri.parse(obj.getLink()));
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getLink()));
                    startActivity(myIntent);
                } else {
                    Log.d("download link::;", "::" + Uri.parse(obj.getApp_download_link()));
                    if(TextUtils.isEmpty(obj.getApp_download_link())||obj.getApp_download_link().equalsIgnoreCase("null")){
                        Log.d("download url::;", "::" + Uri.parse(download_url));
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(download_url));
                        startActivity(myIntent);
                    }else{
                        /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getApp_download_link()));
                        startActivity(myIntent);*/

                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance(""+obj.getApp_name(),""+obj.getImage(),obj.getApp_download_link());
                        dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    boolean isInstalledPackageName(String packagename) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
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
}
