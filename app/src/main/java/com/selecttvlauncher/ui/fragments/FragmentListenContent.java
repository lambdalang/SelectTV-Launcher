package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.selecttvlauncher.BeanClass.CauroselBean;
import com.selecttvlauncher.BeanClass.CauroselsItemBean;
import com.selecttvlauncher.BeanClass.SliderCommonBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentListenContent extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int spacing;
    private int width;
    private int height;
    private LayoutInflater inflate;

    private ArrayList<CauroselBean> listen_list = new ArrayList<>();
    private ArrayList<SliderCommonBean> slider_list = new ArrayList<>();
    public static ArrayList<CauroselsItemBean> item_list = new ArrayList<>();
    private ViewPager fragment_listen_all_content_pager;
    LinearLayout fragment_listen_all_content_pager_dots, dynamic_listen_layout;
    private RelativeLayout fragment_listen_all_content_pager_layout;

    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private int NUM_PAGES;
    private FragmentPagerItemAdapter fragmentPagerItemAdapter;
    public static int mListenNetworkId;
    private ProgressBar progressBar2;
    private ArrayList<ImageView> dots = new ArrayList<>();
    private Timer timer;
    private LinearLayout listen_content_layout;
    public static int listengridPosition;

    // TODO: Rename and change types and number of parameters
    public static FragmentListenContent newInstance(String param1, String param2) {
        FragmentListenContent fragment = new FragmentListenContent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentListenContent() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_listen_content, container, false);
        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        listen_list = HomeActivity.listen_list;
        slider_list = HomeActivity.slider_list;

        listen_content_layout = (LinearLayout) view.findViewById(R.id.listen_content_layout);
        dynamic_listen_layout = (LinearLayout) view.findViewById(R.id.dynamic_listen_layout);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        fragment_listen_all_content_pager = (ViewPager) view.findViewById(R.id.fragment_listen_all_content_pager);
        fragment_listen_all_content_pager.setPageMargin(0);
        fragment_listen_all_content_pager_layout = (RelativeLayout) view.findViewById(R.id.fragment_listen_all_content_pager_layout);
        fragment_listen_all_content_pager_dots = (LinearLayout) view.findViewById(R.id.fragment_listen_all_content_pager_dots);

        text_font_typeface();
        NUM_PAGES = slider_list.size();
        fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getChildFragmentManager(), slider_list);
        fragment_listen_all_content_pager.setAdapter(fragmentPagerItemAdapter);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        fragment_listen_all_content_pager.getLayoutParams().height = height / 2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            fragment_listen_all_content_pager.setScrollBarFadeDuration(5);
        }
        fragment_listen_all_content_pager.setPageMargin(slider_list.size());

        fragment_listen_all_content_pager.setClipChildren(false);

        //        mViewPager.setActivity(this);

        fragment_listen_all_content_pager.onHoverChanged(true);

        fragment_listen_all_content_pager.setOffscreenPageLimit(5);
        fragment_listen_all_content_pager.removeAllViews();
        addDots();
        selectDot(0);


        for (int i = 0; i <listen_list.size(); i++) {
            String name = listen_list.get(i).getName();
            ArrayList<CauroselsItemBean> list = listen_list.get(i).getItem_list();
            if (list.size() != 0) {
                addlayouts(dynamic_listen_layout, name, list, i);

            }


        }
        //if list count is 1 the layout are not diplayed to handle this adding a dummy layout below it
        if(listen_list.size()==1){
            try {
                for (int i = 0; i <listen_list.size(); i++) {
                    String name = listen_list.get(i).getName();
                    ArrayList<CauroselsItemBean> list = listen_list.get(i).getItem_list();
                    if (list.size() != 0) {
                        adddummylayouts(dynamic_listen_layout, name, list, i);

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    public void addDots() {
        Log.d("num_pages", "______" + NUM_PAGES);
        for (int i = 0; i < NUM_PAGES; i++) {
            ImageView dot = new ImageView(getActivity());
            dot.setImageDrawable(getResources().getDrawable(R.drawable.dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(2, 5, 2, 5);
            fragment_listen_all_content_pager_dots.addView(dot, params);
            dots.add(dot);
        }
        fragment_listen_all_content_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
                FragmentListenViewpager mFragment = (FragmentListenViewpager) fragmentPagerItemAdapter.instantiateItem(fragment_listen_all_content_pager, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*fragment_ondemand_all_content_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    public void selectDot(int idx) {
        Resources res = getResources();
        for (int i = 0; i < NUM_PAGES; i++) {
            System.out.println("dots_pos..." + idx);
            int drawableId = (i == idx) ? (R.drawable.dot) : (R.drawable.dot_active);
            Drawable drawable = res.getDrawable(drawableId);
            assert drawable != null;
            drawable.setBounds(0, 0, 0, 0);
            dots.get(i).setImageDrawable(drawable);
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


    private class Blink_progress extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {

                public void run() {

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            fragment_listen_all_content_pager.setScrollBarFadeDuration(5);
                        }

                        if (fragment_listen_all_content_pager.getCurrentItem() + 1 == fragmentPagerItemAdapter.getCount()) {

                            fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getChildFragmentManager(), slider_list);

                            fragment_listen_all_content_pager.setAdapter(fragmentPagerItemAdapter);

                        } else {

                            fragment_listen_all_content_pager.setCurrentItem(fragment_listen_all_content_pager.getCurrentItem() + 1);

                        }

//                    mViewPager.setScrollDurationFactor(5);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            fragment_listen_all_content_pager.setScrollBarFadeDuration(5);
                        }

                        selectDot(fragment_listen_all_content_pager.getCurrentItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                        // TODO Auto-generated catch block
                    }


                }

            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            timer = new Timer();

            timer.scheduleAtFixedRate(new Blink_progress(), 3000, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stoptimer();
    }

    private void stoptimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void addlayouts(final LinearLayout dynamic_horizontalViews_layout, final String name, final ArrayList<CauroselsItemBean> listData, final int position) {
        try {
            final View itemlayout = (View) inflate.inflate(R.layout.horizontal_test, null);
            final TextView horizontal_listview_title = (TextView) itemlayout.findViewById(R.id.horizontal_listview_title);
            TextView view_all_text = (TextView) itemlayout.findViewById(R.id.view_all_text);
            horizontal_listview_title.setTypeface(OpenSans_Regular);
            view_all_text.setTypeface(OpenSans_Regular);
            final HorizontalScrollView horizontal_listview = (HorizontalScrollView) itemlayout.findViewById(R.id.horizontal_listview);
            final LinearLayout recycler_layout = (LinearLayout) itemlayout.findViewById(R.id.recycler_layout);
            final LinearLayout image_view = (LinearLayout) itemlayout.findViewById(R.id.image_view);

            ImageView left_slide = (ImageView) itemlayout.findViewById(R.id.left_slide);
            ImageView right_slide = (ImageView) itemlayout.findViewById(R.id.right_slide);


            horizontal_listview_title.setText(Utilities.stripHtml(name));


            final LinearLayoutManager linear_layoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


            int spanCount = listData.size(); // 3 columns
            spacing = 20; // 50px

            boolean includeEdge = false;
            int layoutwidth = recycler_layout.getLayoutParams().width;
            Log.d("layoutwidth::", "layoutwidth" + layoutwidth);
            Log.d("listData::", "size" + listData.size());

            try {
                ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        width = recycler_layout.getMeasuredWidth();
                        height = recycler_layout.getMeasuredHeight();


                        for (int l = 0; l < listData.size(); l++) {
                            final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_list_item, null);

                            DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                            LinearLayout.LayoutParams vp;
                            if (listData.get(l).getType().equalsIgnoreCase("l") || listData.get(l).getType().equalsIgnoreCase("m")) {
                                vp = new LinearLayout.LayoutParams((width - 80) / 5, LinearLayout.LayoutParams.WRAP_CONTENT);
                            } else {
                                vp = new LinearLayout.LayoutParams((width - 60) / 4, LinearLayout.LayoutParams.WRAP_CONTENT);

                            }


                            if (l != 0) {
                                vp.setMargins(20, 0, 0, 0);
                            }
                            String image_url = listData.get(l).getCarousel_image();
                            Log.d("image_url::", "image_url" + image_url);
                            horizontal_imageView.loadImage(image_url);
                            horizontal_imageView.setLayoutParams(vp);
                            final int finalL = l;
                            Utilities.setViewFocus(getActivity(),itemmlayout);
                            itemmlayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_PODCAST))
                                    {
                                        FragmentListenMain.mListenMainContent = Constants.LISTEN_LISTEN;
                                    }

                                    if (listData.get(finalL).getType().equalsIgnoreCase("N")) {

                                        HomeActivity.toolbarSubContent = listData.get(finalL).getName();
                                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                        FragmentListenMain.mListenNetworkId = listData.get(finalL).getId();
                                        new LoadingTVNetworkList().execute();
                                    } else if (listData.get(finalL).getType().equalsIgnoreCase("M")) {
                                        HomeActivity.toolbarSubContent = listData.get(finalL).getName();
                                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, listData.get(finalL).getId());
                                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                        fragmentTransaction.commit();
                                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                                    } else if (listData.get(finalL).getType().equalsIgnoreCase("S")) {
                                        HomeActivity.toolbarSubContent = listData.get(finalL).getName();

                                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, listData.get(finalL).getId());
                                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                        fragmentTransaction.commit();
                                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                                    }

                                }
                            });
                            image_view.addView(itemmlayout);

                        }


                        Log.d("layoutwidth::", "width" + width);

                    }
                });

                Utilities.setTextFocus(getActivity(), view_all_text);
                Utilities.setViewFocus(getActivity(), right_slide);
                Utilities.setViewFocus(getActivity(),left_slide);


                right_slide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Log.d("Scroll x position::", ":::" + horizontal_listview.getScrollX());
                            horizontal_listview.post(new Runnable() {
                                public void run() {
                                    horizontal_listview.scrollTo(horizontal_listview.getScrollX() + width + 20, horizontal_listview.getScrollY());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                left_slide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Log.d("Scroll x position::", ":::" + horizontal_listview.getScrollX());
                            horizontal_listview.post(new Runnable() {
                                public void run() {
                                    horizontal_listview.scrollTo(horizontal_listview.getScrollX() - width - 20, horizontal_listview.getScrollY());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                view_all_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("podcast", "viewall" + listen_list.get(position).getId());
                        FragmentListenMain.mListenViewallId = listen_list.get(position).getId();
                        if(!FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_PODCAST))
                        {
                            FragmentListenMain.mListenMainContent = Constants.LISTEN_LISTEN;
                        }


                        HomeActivity.toolbarSubContent = Utilities.stripHtml(name);
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        new LoadingAllTVfeaturedCarouselsById().execute();
                        /*FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(position), "");
                        fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();*/
                        listengridPosition = position;

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            dynamic_horizontalViews_layout.addView(itemlayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void adddummylayouts(final LinearLayout dynamic_horizontalViews_layout, final String name, final ArrayList<CauroselsItemBean> listData, final int position) {
        try {
            final View itemlayout = (View) inflate.inflate(R.layout.horizontal_test, null);
            final TextView horizontal_listview_title = (TextView) itemlayout.findViewById(R.id.horizontal_listview_title);
            TextView view_all_text = (TextView) itemlayout.findViewById(R.id.view_all_text);
            horizontal_listview_title.setTypeface(OpenSans_Regular);
            view_all_text.setTypeface(OpenSans_Regular);
            final HorizontalScrollView horizontal_listview = (HorizontalScrollView) itemlayout.findViewById(R.id.horizontal_listview);
            final LinearLayout recycler_layout = (LinearLayout) itemlayout.findViewById(R.id.recycler_layout);
            final LinearLayout image_view = (LinearLayout) itemlayout.findViewById(R.id.image_view);

            ImageView left_slide = (ImageView) itemlayout.findViewById(R.id.left_slide);
            ImageView right_slide = (ImageView) itemlayout.findViewById(R.id.right_slide);


            horizontal_listview_title.setText(Utilities.stripHtml(name));


            final LinearLayoutManager linear_layoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


            int spanCount = listData.size(); // 3 columns
            spacing = 20; // 50px

            boolean includeEdge = false;
            int layoutwidth = recycler_layout.getLayoutParams().width;
            Log.d("layoutwidth::", "layoutwidth" + layoutwidth);
            Log.d("listData::", "size" + listData.size());

            try {
                ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        width = recycler_layout.getMeasuredWidth();
                        height = recycler_layout.getMeasuredHeight();


                        for (int l = 0; l < listData.size(); l++) {
                            final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_list_item, null);

                            DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                            LinearLayout.LayoutParams vp;
                            if (listData.get(l).getType().equalsIgnoreCase("l") || listData.get(l).getType().equalsIgnoreCase("m")) {
                                vp = new LinearLayout.LayoutParams((width - 80) / 5, LinearLayout.LayoutParams.WRAP_CONTENT);
                            } else {
                                vp = new LinearLayout.LayoutParams((width - 60) / 4, LinearLayout.LayoutParams.WRAP_CONTENT);

                            }


                            if (l != 0) {
                                vp.setMargins(20, 0, 0, 0);
                            }
                            String image_url = listData.get(l).getCarousel_image();
                            Log.d("image_url::", "image_url" + image_url);
                            horizontal_imageView.loadImage(image_url);
                            horizontal_imageView.setLayoutParams(vp);
                            final int finalL = l;
                            Utilities.setViewFocus(getActivity(),itemmlayout);
                            itemmlayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_PODCAST))
                                    {
                                        FragmentListenMain.mListenMainContent = Constants.LISTEN_LISTEN;
                                    }

                                    if (listData.get(finalL).getType().equalsIgnoreCase("N")) {

                                        HomeActivity.toolbarSubContent = listData.get(finalL).getName();
                                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                        FragmentListenMain.mListenNetworkId = listData.get(finalL).getId();
                                        new LoadingTVNetworkList().execute();
                                    } else if (listData.get(finalL).getType().equalsIgnoreCase("M")) {
                                        HomeActivity.toolbarSubContent = listData.get(finalL).getName();
                                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, listData.get(finalL).getId());
                                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                        fragmentTransaction.commit();
                                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                                    } else if (listData.get(finalL).getType().equalsIgnoreCase("S")) {
                                        HomeActivity.toolbarSubContent = listData.get(finalL).getName();

                                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, listData.get(finalL).getId());
                                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                        fragmentTransaction.commit();
                                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                                    }

                                }
                            });
                            image_view.addView(itemmlayout);

                        }


                        Log.d("layoutwidth::", "width" + width);

                    }
                });

                Utilities.setTextFocus(getActivity(), view_all_text);
                Utilities.setViewFocus(getActivity(), right_slide);
                Utilities.setViewFocus(getActivity(),left_slide);


                right_slide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Log.d("Scroll x position::", ":::" + horizontal_listview.getScrollX());
                            horizontal_listview.post(new Runnable() {
                                public void run() {
                                    horizontal_listview.scrollTo(horizontal_listview.getScrollX() + width + 20, horizontal_listview.getScrollY());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                left_slide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Log.d("Scroll x position::", ":::" + horizontal_listview.getScrollX());
                            horizontal_listview.post(new Runnable() {
                                public void run() {
                                    horizontal_listview.scrollTo(horizontal_listview.getScrollX() - width - 20, horizontal_listview.getScrollY());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                view_all_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("podcast", "viewall" + listen_list.get(position).getId());
                        FragmentListenMain.mListenViewallId = listen_list.get(position).getId();
                        if(!FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_PODCAST))
                        {
                            FragmentListenMain.mListenMainContent = Constants.LISTEN_LISTEN;
                        }


                        HomeActivity.toolbarSubContent = Utilities.stripHtml(name);
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        new LoadingAllTVfeaturedCarouselsById().execute();
                        /*FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(position), "");
                        fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();*/
                        listengridPosition = position;

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemlayout.setVisibility(View.INVISIBLE);
            dynamic_horizontalViews_layout.addView(itemlayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void text_font_typeface() {
        OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
        osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
    }

    private class FragmentPagerItemAdapter extends FragmentPagerAdapter {
        ArrayList<SliderCommonBean> list;
        FragmentManager fm;

        public FragmentPagerItemAdapter(FragmentManager fm, ArrayList<SliderCommonBean> list) {
            super(fm);
            this.fm = fm;
            this.list = list;
        }

        @Override
        public Fragment getItem(int pos) {

            return FragmentListenViewpager.newInstance(list.toString(), String.valueOf(pos));
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


    private class LoadingTVNetworkList extends AsyncTask<String, Object, Object> {
        private JSONArray m_jsonNetworkList;
        private String id;
        private String name;
        private String image;
        private String type;
        private String description;

        @Override
        protected void onPreExecute() {
            progressBar2.setVisibility(View.VISIBLE);
            dynamic_listen_layout.setVisibility(View.GONE);
            listen_content_layout.setVisibility(View.GONE);
            /*FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
            DialogFragment dialog_fragment = new DialogFragment();
            network_fragmentTransaction.replace(R.id.fragment_listen_pagerandlist, dialog_fragment);
            network_fragmentTransaction.commit();*/
            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
                m_jsonNetworkList = JSONRPCAPI.getTVNetworkList(FragmentListenMain.mListenNetworkId, 1000, 0, HomeActivity.mfreeorall);

                if (m_jsonNetworkList  == null) return null;
                Log.d("NetworkList::", "Selected Network list::" + m_jsonNetworkList);
                for (int i = 0; i < m_jsonNetworkList.length(); i++) {
                    JSONObject object = m_jsonNetworkList.getJSONObject(i);
                    if (object.has("id")) {
                        id = object.getString("id");
                    }
                    if (object.has("name")) {
                        name = object.getString("name");
                    }

                    if (object.has("carousel_image")) {
                        image = object.getString("carousel_image");
                    } else {
                        if (object.has("image")) {
                            image = object.getString("image");
                        }
                    }


                    if (object.has("type")) {
                        type = object.getString("type");

                        Log.d("type", "grid" + type);

                    }
                    if (object.has("description")) {
                        description = object.getString("description");
                    }

//                    grid_list.add(new GridBean(id, name, image, description, type));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                progressBar2.setVisibility(View.GONE);


                if (m_jsonNetworkList.length() > 0) {
                    if (FragmentListenMain.isdestryed) {


                        FragmentListenMain.mListenSubContent = Constants.LISTEN_LISTEN_CONTENT;
                        FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                        GridFragment network_listfragment = GridFragment.newInstance(Constants.SHOWS, m_jsonNetworkList.toString());
                        network_fragmentTransaction.replace(R.id.fragment_listen_pagerandlist, network_listfragment);
                        network_fragmentTransaction.commit();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            dynamic_listen_layout.setVisibility(View.VISIBLE);
            listen_content_layout.setVisibility(View.VISIBLE);
            // mProgressHUD.dismiss();
        }

    }


    public class LoadingAllTVfeaturedCarouselsById extends AsyncTask<String, Object, Object> {
        String description = "", title = "", image = "", name = "";
        int id;
        JSONArray allCarousels;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2.setVisibility(View.VISIBLE);
            dynamic_listen_layout.setVisibility(View.GONE);
            listen_content_layout.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {


            Log.d("mfreeorall::", "id::" + HomeActivity.mfreeorall);
            allCarousels = JSONRPCAPI.getAllTVfeaturedCarousels(FragmentListenMain.mListenViewallId, 1000, 0, HomeActivity.mfreeorall);
            if (allCarousels  == null) return null;
            Log.d("allCarousels::", "allCarousels::" + allCarousels);
            FragmentListenContent.item_list.clear();
            int id = 0;
            String name = "", image = "", description = "", type = "";
            try {
                for (int i = 0; i < allCarousels.length(); i++) {
                    JSONObject object = allCarousels.getJSONObject(i);
                    if (object.has("id")) {
                        id = object.getInt("id");
                    }
                    if (object.has("name")) {
                        name = object.getString("name");
                    }
                    if (object.has("poster_url")) {
                        image = object.getString("poster_url");
                    }
                    if (object.has("carousel_image")) {
                        image = object.getString("carousel_image");
                    }
                    if (object.has("image")) {
                        image = object.getString("image");
                    }
                    if (object.has("description")) {
                        description = object.getString("description");
                    }

                    if (object.has("type")) {
                        type = object.getString("type");
                        Log.d("type", "all" + type);
                    }
                    FragmentListenContent.item_list.add(new CauroselsItemBean(image, type, id, name));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                progressBar2.setVisibility(View.GONE);
                if(FragmentListenContent.item_list!=null&& FragmentListenContent.item_list.size()!=0)
                {
                    if (FragmentListenMain.isdestryed) {


                        FragmentListenMain.mListenSubContent = Constants.LISTEN_LISTEN_SUBCONTENT;
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        FragmentListenGrid listfragment = FragmentListenGrid.newInstance("", "");
                        fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }
                }

                dynamic_listen_layout.setVisibility(View.VISIBLE);
                listen_content_layout.setVisibility(View.VISIBLE);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
