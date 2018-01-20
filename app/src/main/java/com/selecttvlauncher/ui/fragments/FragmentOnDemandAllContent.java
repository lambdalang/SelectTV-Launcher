package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.Adapter.GridAdapter;
import com.selecttvlauncher.Adapter.HorizontalListAdapter;
import com.selecttvlauncher.Adapter.NetworkGridAdapter;
import com.selecttvlauncher.BeanClass.HorizontalListBean;
import com.selecttvlauncher.BeanClass.HorizontalListitemBean;
import com.selecttvlauncher.BeanClass.SliderBean;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class FragmentOnDemandAllContent extends Fragment implements GridAdapter.onGridSelectedListener, NetworkGridAdapter.GridClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int NUM_PAGES;
    ArrayList<ImageView> dots = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String DeacdeType;
    private String Data;

    ViewPager fragment_ondemand_all_content_pager;
    LinearLayout fragment_ondemand_all_content_pager_dots;
    FragmentPagerItemAdapter fragmentPagerItemAdapter;
    RelativeLayout fragment_ondemand_all_content_pager_layout;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private LinearLayout dynamic_list_layout, content_layout;
    private ArrayList<Integer> images = new ArrayList<Integer>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<HorizontalListitemBean> titles_ar = new ArrayList<>();
    private LayoutInflater inflate;
    private Timer timer;
    private ArrayList<HorizontalListBean> horizontalListdata;
    ArrayList<HorizontalListitemBean> horizontalListBeans;

    private String title = null;
    private String type;
    private int itemsid;
    private String itemsname;
    private String itemscarousel_image;
    private int spacing;
    private int width;
    private int height;
    private HorizontalListAdapter adpater;
    private static ArrayList<SliderBean> slider_list = new ArrayList<>();
    private ProgressBar progressBar2;
    private String view_all_type;
    private Tracker mTracker;

    RecyclerView.RecycledViewPool mPool = new RecyclerView.RecycledViewPool() {
        @Override
        public RecyclerView.ViewHolder getRecycledView(int viewType) {
            RecyclerView.ViewHolder scrap = super.getRecycledView(viewType);
            Log.i("@@@", "get holder from pool: " + scrap);
            return scrap;
        }

        @Override
        public void putRecycledView(RecyclerView.ViewHolder scrap) {
            super.putRecycledView(scrap);
            Log.i("@@@", "put holder to pool: " + scrap);
        }

        @Override
        public String toString() {
            return "ViewPool in adapter@" + Integer.toHexString(hashCode());
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentOnDemandAllContent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentOnDemandAllContent newInstance(String param1, String param2) {
        FragmentOnDemandAllContent fragment = new FragmentOnDemandAllContent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentOnDemandAllContent() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            DeacdeType = getArguments().getString(ARG_PARAM1);
            Data = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_on_demand_all_content, container, false);
        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTracker = LauncherApplication.getInstance().getDefaultTracker();

        HomeActivity.selectedmenu = Constants.ALL_SIDE_MENU;
        fragment_ondemand_all_content_pager = (ViewPager) view.findViewById(R.id.fragment_ondemand_all_content_pager);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        fragment_ondemand_all_content_pager.setPageMargin(0);
        fragment_ondemand_all_content_pager_layout = (RelativeLayout) view.findViewById(R.id.fragment_ondemand_all_content_pager_layout);
        fragment_ondemand_all_content_pager_dots = (LinearLayout) view.findViewById(R.id.fragment_ondemand_all_content_pager_dots);
        dynamic_list_layout = (LinearLayout) view.findViewById(R.id.dynamic_list_layout);
        content_layout = (LinearLayout) view.findViewById(R.id.content_layout);


        try {
            if (HomeActivity.m_jsonHomeSlider != null && HomeActivity.m_jsonHomeSlider.length() > 0) {
                int id = 0;
                String name = "", image = "", description = "", etype = "";
                slider_list = new ArrayList<>();
                for (int i = 0; i < HomeActivity.m_jsonHomeSlider.length(); i++) {
                    JSONObject slider_object = HomeActivity.m_jsonHomeSlider.getJSONObject(i);
                    if (slider_object.has("description")) {
                        description = slider_object.getString("description");
                    }
                    if (slider_object.has("title")) {
                        title = slider_object.getString("title");
                    }
                    if (slider_object.has("id")) {
                        id = slider_object.getInt("id");
                    }
                    if (slider_object.has("image")) {
                        image = slider_object.getString("image");
                    }
                    if (slider_object.has("name")) {
                        name = slider_object.getString("name");
                    }
                    if (slider_object.has("type")) {
                        etype = slider_object.getString("type");
                    }

                    slider_list.add(new SliderBean(id, description, title, image, name, etype));

                }
                NUM_PAGES = slider_list.size();
                fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getChildFragmentManager(), slider_list);
                fragment_ondemand_all_content_pager.setAdapter(fragmentPagerItemAdapter);

                DisplayMetrics displaymetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                fragment_ondemand_all_content_pager_layout.getLayoutParams().height = height / 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    fragment_ondemand_all_content_pager.setScrollBarFadeDuration(5);
                }
                fragment_ondemand_all_content_pager.setPageMargin(slider_list.size());

                fragment_ondemand_all_content_pager.setClipChildren(false);

                //        mViewPager.setActivity(this);

                fragment_ondemand_all_content_pager.onHoverChanged(true);

                fragment_ondemand_all_content_pager.setOffscreenPageLimit(5);
                fragment_ondemand_all_content_pager_dots.removeAllViews();
                addDots();
                selectDot(0);


            } else {
                new LoadingTvShowTVfeaturedCarousels().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    /*    images.add(R.drawable.img1);
        images.add(R.drawable.img2);
        images.add(R.drawable.img3);
        images.add(R.drawable.img4);
        images.add(R.drawable.img5);
        images.add(R.drawable.img6);
        images.add(R.drawable.img7);
        images.add(R.drawable.img8);
        images.add(R.drawable.img9);
        images.add(R.drawable.img10);
        images.add(R.drawable.img11);
        images.add(R.drawable.img12);*/

        titles.add("Most Watched");
        titles.add("Web Originals");
        titles.add("Premium Shows");
        titles.add("Most Watched");
        titles.add("New Free moview");
        titles.add("Pay-per-View Movies");

        for (int i = 0; i < titles.size(); i++) {
//            addlayouts(dynamic_list_layout, i);
        }


        try {
            if (!TextUtils.isEmpty(Data)) {
                JSONArray data_array = new JSONArray(Data);
                horizontalListdata = new ArrayList<>();
                horizontalListdata.clear();
                int id = 0;
                String name = "", image = "", description = "";
                for (int i = 0; i < data_array.length(); i++) {
                    horizontalListBeans = new ArrayList<>();
                    horizontalListBeans.clear();
                    JSONObject object = data_array.getJSONObject(i);
                    if (object.has("id")) {
                        id = object.getInt("id");
                    }

                    if (object.has("title")) {
                        title = object.getString("title");
                        Log.d("title title::", ":::" + title);
                    }
                    if (TextUtils.isEmpty(title)) {
                        if (object.has("name")) {
                            title = object.getString("name");
                            Log.d("name ::", ":::" + title);
                        }
                    }


                    if (object.has("items")) {
                        JSONArray itemsarray = object.getJSONArray("items");
                        for (int j = 0; j < itemsarray.length(); j++) {
                            JSONObject itemsobject = itemsarray.getJSONObject(j);
                            if (itemsobject.has("type")) {
                                type = itemsobject.getString("type");
                            }

                            if (itemsobject.has("id")) {
                                itemsid = itemsobject.getInt("id");
                            }
                            if (itemsobject.has("name")) {
                                itemsname = itemsobject.getString("name");
                            }
                            if (itemsobject.has("carousel_image")) {
                                itemscarousel_image = itemsobject.getString("carousel_image");

                            }


                            horizontalListBeans.add(new HorizontalListitemBean(itemsid, itemscarousel_image, type, itemsname));

                        }
                        if (horizontalListBeans.size() != 0) {
                            addlayouts(dynamic_list_layout, title, id, horizontalListBeans, i);
                        }


                    }


                    //addlayouts(dynamic_horizontalViews_layout, name, id);
                    //                        images.add(Integer.valueOf(id));
                    //                        titles.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void addlayouts(final LinearLayout dynamic_horizontalViews_layout, final String name, final int id, final ArrayList<HorizontalListitemBean> listData, final int position) {
        final View itemlayout = (View) inflate.inflate(R.layout.horizontal_list_layout, null);
        TextView horizontal_listview_title = (TextView) itemlayout.findViewById(R.id.horizontal_listview_title);
        final TextView view_all_text = (TextView) itemlayout.findViewById(R.id.view_all_text);
        horizontal_listview_title.setTypeface(OpenSans_Regular);
        view_all_text.setTypeface(OpenSans_Regular);
        final LinearLayout recycler_layout = (LinearLayout) itemlayout.findViewById(R.id.recycler_layout);
        final RecyclerView horizontal_listview = (RecyclerView) itemlayout.findViewById(R.id.horizontal_listview);

        horizontal_listview.setTag(id);
        ImageView left_slide = (ImageView) itemlayout.findViewById(R.id.left_slide);
        ImageView right_slide = (ImageView) itemlayout.findViewById(R.id.right_slide);

        String name_html = Utilities.stripHtml(name);

        horizontal_listview_title.setText(name_html);


        final LinearLayoutManager linear_layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        horizontal_listview.hasFixedSize();
        horizontal_listview.setLayoutManager(linear_layoutManager);
        int spanCount = listData.size(); // 3 columns
        spacing = 20; // 50px
        linear_layoutManager.setRecycleChildrenOnDetach(true);
        if (mPool != null) {
            horizontal_listview.setRecycledViewPool(mPool);
            Log.i("@@@", "using view pool :" + mPool);
        }

        boolean includeEdge = false;
        horizontal_listview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        int layoutwidth = dynamic_horizontalViews_layout.getWidth();
        Log.d("layoutwidth::", "layoutwidth" + layoutwidth);

        try {
            ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    width = recycler_layout.getMeasuredWidth();
                    height = recycler_layout.getMeasuredHeight();

                    setlistadapter(horizontal_listview, listData, width - (spacing * 3));


                    Log.d("layoutwidth::", "width" + width);

                }
            });

            Utilities.setViewFocus(getActivity(), right_slide);
            Utilities.setViewFocus(getActivity(), left_slide);
            Utilities.setTextFocus(getActivity(), view_all_text);


            right_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int firstVisiblePosition = linear_layoutManager.findLastCompletelyVisibleItemPosition();
                    firstVisiblePosition = firstVisiblePosition + 4;
                    Log.d("list::", "::" + firstVisiblePosition + ":::" + linear_layoutManager.findFirstVisibleItemPosition() + "::" + linear_layoutManager.findLastVisibleItemPosition() + "::" + linear_layoutManager.findLastCompletelyVisibleItemPosition());
                    horizontal_listview.smoothScrollToPosition(firstVisiblePosition);
                }
            });

            left_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int firstVisiblePosition = linear_layoutManager.findFirstVisibleItemPosition();
                    firstVisiblePosition = firstVisiblePosition - 4;
                    Log.d("list::", "::" + firstVisiblePosition + ":::" + linear_layoutManager.findFirstVisibleItemPosition() + "::" + linear_layoutManager.findLastVisibleItemPosition() + "::" + linear_layoutManager.findLastCompletelyVisibleItemPosition());
                    if (firstVisiblePosition > 0) {
                        horizontal_listview.smoothScrollToPosition(firstVisiblePosition);
                    } else {
                        horizontal_listview.smoothScrollToPosition(0);
                    }


                }
            });
            view_all_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.content_position = Constants.SUB_CONTENT;
                    HomeActivity.toolbarSubContent = name;
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    /*FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    GridFragment listfragment = GridFragment.newInstance(Constants.CATEGORY_SHOW, array.toString());
                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                    fragmentTransaction_list.commit();*/
                    if (listData.get(position).getType().equalsIgnoreCase("m")) {
                        view_all_type = Constants.MOVIES;
                    } else if (listData.get(position).getType().equalsIgnoreCase("s")) {
                        view_all_type = Constants.CATEGORY_SHOW;
                    } else if (listData.get(position).getType().equalsIgnoreCase("n")) {
                        view_all_type = Constants.NETWORK;
                    }
                    HomeActivity.carousel_id = id;
                    new LoadingAllTVfeaturedCarouselsById().execute(Integer.toString(id));


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamic_horizontalViews_layout.addView(itemlayout);
    }

    private void setlistadapter(RecyclerView horizontal_listview, ArrayList<HorizontalListitemBean> listData, int i) {


        for (int j = 0; j < listData.size(); j++) {
           /* if (listData.get(j).getType().equalsIgnoreCase("s")) {
                adpater = new HorizontalListAdapter(listData, getContext(), i, this, itemsname);
                horizontal_listview.setAdapter(adpater);
            } else {*/
            NetworkGridAdapter adpater = new NetworkGridAdapter(listData, getContext(), i, FragmentOnDemandAllContent.this, FragmentOnDemandAllContent.this, itemsname);
            horizontal_listview.setAdapter(adpater);
//            }
        }


    }

    @Override
    public void onShowGridItemSelected(int id, String type, String name) {
        Log.d("Selected show::::", "id:::" + id);
        HomeActivity.toolbarSubContent = name;
        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
        setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

        if (type.equalsIgnoreCase("N") || type.equalsIgnoreCase("L")) {
            HomeActivity.toolbarMainContent = "TV Networks";
            HomeActivity.toolbarSubContent = name;
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            HomeActivity.content_position = Constants.SUB_CONTENT;

            new LoadingTVNetworkList().execute(String.valueOf(id));

        } else if (type.equalsIgnoreCase("m")) {
            HomeActivity.toolbarMainContent = "Movies";
            HomeActivity.toolbarSubContent = name;
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, id);
            fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
            fragmentTransaction.commit();
            ((HomeActivity) getActivity()).SwapMovieFragment(true);
        } else {
            if (getActivity() != null) {
                HomeActivity.toolbarMainContent = "TV Shows";
                HomeActivity.toolbarSubContent = name;
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                ((HomeActivity) getActivity()).SwapMovieFragment(true);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, id);
                fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                fragmentTransaction.commit();
            }
        }


    }

    @Override
    public void onGriditemSelected(String network, String id, String type, String name) {


        if (type.equalsIgnoreCase("N") || type.equalsIgnoreCase("L")) {
            HomeActivity.toolbarMainContent = "TV Networks";
            HomeActivity.toolbarSubContent = name;
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

            HomeActivity.content_position = Constants.SUB_CONTENT;
            new LoadingTVNetworkList().execute(String.valueOf(id));

        } else if (type.equalsIgnoreCase("m")) {
            HomeActivity.toolbarMainContent = "Movies";
            HomeActivity.toolbarSubContent = name;
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, Integer.parseInt(id));
            fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
            fragmentTransaction.commit();
            ((HomeActivity) getActivity()).SwapMovieFragment(true);
        } else {
            if (getActivity() != null) {
                HomeActivity.toolbarMainContent = "TV Shows";
                HomeActivity.toolbarSubContent = name;
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                ((HomeActivity) getActivity()).SwapMovieFragment(true);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, Integer.parseInt(id));
                fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                fragmentTransaction.commit();
            }
        }
    }


    class Blink_progress extends TimerTask {

        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {

                public void run() {

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            fragment_ondemand_all_content_pager.setScrollBarFadeDuration(5);
                        }

                        if (fragmentPagerItemAdapter != null) {
                            if (fragment_ondemand_all_content_pager.getCurrentItem() + 1 == fragmentPagerItemAdapter.getCount()) {

                                fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getChildFragmentManager(), slider_list);

                                fragment_ondemand_all_content_pager.setAdapter(fragmentPagerItemAdapter);

                            } else {

                                fragment_ondemand_all_content_pager.setCurrentItem(fragment_ondemand_all_content_pager.getCurrentItem() + 1);

                            }

                        }

//                    mViewPager.setScrollDurationFactor(5);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            fragment_ondemand_all_content_pager.setScrollBarFadeDuration(5);
                        }

                        selectDot(fragment_ondemand_all_content_pager.getCurrentItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                        // TODO Auto-generated catch block
                    }


                }

            });
        }
    }


    void stopTimer() {

        try {
            if (timer != null) {

                timer.cancel();


                timer = null;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
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
            fragment_ondemand_all_content_pager_dots.addView(dot, params);
            dots.add(dot);
        }
        fragment_ondemand_all_content_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
                FragmentViewPagerItem mFragment = (FragmentViewPagerItem) fragmentPagerItemAdapter.instantiateItem(fragment_ondemand_all_content_pager, position);
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

    private class FragmentPagerItemAdapter extends FragmentPagerAdapter {
        ArrayList<SliderBean> list;
        FragmentManager fm;

        public FragmentPagerItemAdapter(FragmentManager fm, ArrayList<SliderBean> list) {
            super(fm);
            this.fm = fm;
            this.list = list;
        }

        @Override
        public Fragment getItem(int pos) {

            return FragmentViewPagerItem.newInstance(Constants.ALL_SIDE_MENU, String.valueOf(pos));
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

   /* public static class FragmentViewPagerItem extends Fragment {

        TextView fragment_ondemand_all_pagercontent_heading;
        TextView fragment_ondemand_all_pagercontent_subheading;
        TextView fragment_ondemand_all_pagercontent_caption;
        TextView fragment_ondemand_all_pagercontent_watchnow_text;
        DynamicImageView slider_image;
        int c_id;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_viewpager_item, container, false);

            if (getArguments() != null) {
                c_id = getArguments().getInt("msg");
            }

            fragment_ondemand_all_pagercontent_subheading = (TextView) v.findViewById(R.id.fragment_ondemand_all_pagercontent_subheading);
            fragment_ondemand_all_pagercontent_heading = (TextView) v.findViewById(R.id.fragment_ondemand_all_pagercontent_heading);
            fragment_ondemand_all_pagercontent_caption = (TextView) v.findViewById(R.id.fragment_ondemand_all_pagercontent_caption);
            fragment_ondemand_all_pagercontent_watchnow_text = (TextView) v.findViewById(R.id.fragment_ondemand_all_pagercontent_watchnow_text);
            slider_image = (DynamicImageView) v.findViewById(R.id.slider_image);

            Typeface OpenSans_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
            Typeface OpenSans_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");

            fragment_ondemand_all_pagercontent_heading.setTypeface(OpenSans_bold);
            fragment_ondemand_all_pagercontent_subheading.setTypeface(OpenSans_regular);
            fragment_ondemand_all_pagercontent_caption.setTypeface(OpenSans_regular);
            fragment_ondemand_all_pagercontent_watchnow_text.setTypeface(OpenSans_regular);

            fragment_ondemand_all_pagercontent_heading.setText(slider_list.get(c_id).getName());
            fragment_ondemand_all_pagercontent_subheading.setText(slider_list.get(c_id).getTitle());
            fragment_ondemand_all_pagercontent_caption.setText(slider_list.get(c_id).getDescription());
            slider_image.loadImage(slider_list.get(c_id).getImage());

            fragment_ondemand_all_pagercontent_watchnow_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    new GetSliderEntity().execute(String.valueOf(slider_list.get(c_id).getId()));

                   *//* ((HomeActivity)getActivity()).SwapMovieFragment(true);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentMovieMain  fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, slider_list.get(c_id).getId());
                    fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                    fragmentTransaction.commit();*//*

                }
            });


            *//*fragment_ondemand_all_pagercontent_subheading
                    fragment_ondemand_all_pagercontent_heading.setTypeface(OpenSans_Bold);
            fragment_ondemand_all_pagercontent_caption.setTypeface();*//*
            return v;
        }

        public static FragmentViewPagerItem newInstance(int text) {

            FragmentViewPagerItem f = new FragmentViewPagerItem();
            Bundle b = new Bundle();
            b.putInt("msg", text);

            f.setArguments(b);

            return f;
        }
    }*/


    public void text_font_typeface() {
        OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
        osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
    }


    public class LoadingTvShowTVfeaturedCarousels extends AsyncTask<Object, Object, Object> {
        String description = "", title = "", image = "", name = "", etype = "";
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {

            HomeActivity.m_jsonHomeSlider = JSONRPCAPI.getDemandSliderList();
            if (HomeActivity.m_jsonHomeSlider == null) return null;
            Log.d("Slider::", "slidelist::" + HomeActivity.m_jsonHomeSlider);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                if (HomeActivity.m_jsonHomeSlider.length() > 0) {
                    slider_list = new ArrayList<>();
                    for (int i = 0; i < HomeActivity.m_jsonHomeSlider.length(); i++) {
                        JSONObject slider_object = HomeActivity.m_jsonHomeSlider.getJSONObject(i);
                        if (slider_object.has("description")) {
                            description = slider_object.getString("description");
                        }
                        if (slider_object.has("title")) {
                            title = slider_object.getString("title");
                        }
                        if (slider_object.has("id")) {
                            id = slider_object.getInt("id");
                        }
                        if (slider_object.has("image")) {
                            image = slider_object.getString("image");
                        }
                        if (slider_object.has("name")) {
                            name = slider_object.getString("name");
                        }
                        if (slider_object.has("type")) {
                            etype = slider_object.getString("type");
                        }

                        slider_list.add(new SliderBean(id, description, title, image, name, etype));

                    }
                    NUM_PAGES = slider_list.size();
                    fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getChildFragmentManager(), slider_list);
                    fragment_ondemand_all_content_pager.setAdapter(fragmentPagerItemAdapter);
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int height = displaymetrics.heightPixels;
                    int width = displaymetrics.widthPixels;
                    fragment_ondemand_all_content_pager_layout.getLayoutParams().height = height / 2;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        fragment_ondemand_all_content_pager.setScrollBarFadeDuration(5);
                    }
                    fragment_ondemand_all_content_pager.setPageMargin(slider_list.size());

                    fragment_ondemand_all_content_pager.setClipChildren(false);

//        mViewPager.setActivity(this);

                    fragment_ondemand_all_content_pager.onHoverChanged(true);

                    fragment_ondemand_all_content_pager.setOffscreenPageLimit(5);
                    fragment_ondemand_all_content_pager_dots.removeAllViews();
                    addDots();
                    selectDot(0);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class LoadingAllTVfeaturedCarouselsById extends AsyncTask<String, Object, Object> {
        String description = "", title = "", image = "", name = "";
        int id;
        JSONArray allCarousels;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            content_layout.setVisibility(View.GONE);
            progressBar2.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            HomeActivity.allCarousels = JSONRPCAPI.getAllTVfeaturedCarousels(Integer.parseInt(params[0]), 1000, 0, HomeActivity.mfreeorall);
            if (HomeActivity.allCarousels == null) return null;
            Log.d("allCarousels::", "allCarousels::" + HomeActivity.allCarousels);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                HomeActivity.content_position = Constants.SUB_CONTENT;
                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                GridFragment listfragment = GridFragment.newInstance(view_all_type, HomeActivity.allCarousels.toString());
                fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                fragmentTransaction_list.commit();
                progressBar2.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class LoadingTVNetworkList extends AsyncTask<String, Object, Object> {
        private JSONArray m_jsonNetworkList;

        @Override
        protected void onPreExecute() {
            progressBar2.setVisibility(View.VISIBLE);
            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
                m_jsonNetworkList = JSONRPCAPI.getTVNetworkList(Integer.parseInt(params[0]), 1000, 0, HomeActivity.mfreeorall);
                if (m_jsonNetworkList == null) return null;
                Log.d("NetworkList::", "Selected Network list::" + m_jsonNetworkList);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                progressBar2.setVisibility(View.GONE);
                if (m_jsonNetworkList.length() > 0) {

                    FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                    GridFragment network_listfragment = GridFragment.newInstance(Constants.SHOWS, m_jsonNetworkList.toString());
                    network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, network_listfragment);
                    network_fragmentTransaction.commit();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }

    private void setAnalyticreport(String mainString, String SubString1, String SubString2) {
        try {
            if (HomeActivity.isPayperview.equals("Pay Per View")) {
                if (TextUtils.isEmpty(mainString)) {
                    Utilities.setAnalytics(mTracker, "PayPerView");
                } else if (TextUtils.isEmpty(SubString1)) {
                    Utilities.setAnalytics(mTracker, "PayPerView-" + mainString);
                } else if (TextUtils.isEmpty(SubString2)) {
                    Utilities.setAnalytics(mTracker, "PayPerView-" + mainString + "-" + SubString1);
                } else {
                    Utilities.setAnalytics(mTracker, "PayPerView-" + mainString + "-" + SubString1 + "-" + SubString2);
                }

            } else if (HomeActivity.isPayperview.equals("On-Demand")) {
                if (TextUtils.isEmpty(mainString)) {
                    Utilities.setAnalytics(mTracker, "OnDemand");
                } else if (TextUtils.isEmpty(SubString1)) {
                    Utilities.setAnalytics(mTracker, "OnDemand-" + mainString);
                } else if (TextUtils.isEmpty(SubString2)) {
                    Utilities.setAnalytics(mTracker, "OnDemand-" + mainString + "-" + SubString1);
                } else {
                    Utilities.setAnalytics(mTracker, "OnDemand-" + mainString + "-" + SubString1 + "-" + SubString2);
                }

            } else {
                if (TextUtils.isEmpty(mainString)) {
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent);
                } else if (TextUtils.isEmpty(SubString1)) {
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent + "-" + mainString);
                } else if (TextUtils.isEmpty(SubString2)) {
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent + "-" + mainString + "-" + SubString1);
                } else {
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent + "-" + mainString + "-" + SubString1 + "-" + SubString2);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
