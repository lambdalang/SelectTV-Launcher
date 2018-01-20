package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import com.selecttvlauncher.Adapter.GridAdapter;
import com.selecttvlauncher.Adapter.NetworkGridAdapter;
import com.selecttvlauncher.BeanClass.HorizontalListBean;
import com.selecttvlauncher.BeanClass.HorizontalListitemBean;
import com.selecttvlauncher.BeanClass.SliderBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GamesDetailsFragment extends Fragment implements GridAdapter.onGridSelectedListener, NetworkGridAdapter.GridClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<HorizontalListBean> horizontalListdata;
    ArrayList<HorizontalListitemBean> horizontalListBeans;
    private String title = null;
    private String type;
    private int itemsid;
    private String itemsname;
    private String itemscarousel_image;
    private LayoutInflater inflate;
    private int spacing;
    private int width;
    private int height;
    private LinearLayout dynamic_list_layout, content_layout;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private static ArrayList<SliderBean> slider_list = new ArrayList<>();
    private static int NUM_PAGES;
    FragmentPagerItemAdapter fragmentPagerItemAdapter;
    private ViewPager fragment_game_all_content_pager;
    private RelativeLayout fragment_game_all_content_pager_layout;
    private LinearLayout fragment_game_all_content_pager_dots;
    private ProgressBar progressBar2;
    ArrayList<ImageView> dots = new ArrayList<>();
    private int mId;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GamesDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GamesDetailsFragment newInstance(String param1, String param2) {
        GamesDetailsFragment fragment = new GamesDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GamesDetailsFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_games_details, container, false);
        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        HomeActivity.selectedmenu = Constants.GAMES;
        fragment_game_all_content_pager = (ViewPager) view.findViewById(R.id.fragment_game_all_content_pager);
        //fragment_game_all_content_pager.setVisibility(View.GONE);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        //fragment_game_all_content_pager.setPageMargin(0);
        fragment_game_all_content_pager_layout = (RelativeLayout) view.findViewById(R.id.fragment_game_all_content_pager_layout);
        fragment_game_all_content_pager_dots = (LinearLayout) view.findViewById(R.id.fragment_game_all_content_pager_dots);
        dynamic_list_layout = (LinearLayout) view.findViewById(R.id.game_dynamic_list_layout);
        content_layout = (LinearLayout) view.findViewById(R.id.game_content_layout);

        text_font_typeface();


        /*try {
            if (HomeActivity.m_jsonGameSlider != null && HomeActivity.m_jsonGameSlider.length() > 0) {
                int id = 0;
                String name = "", image = "", description = "";
                slider_list = new ArrayList<>();
                for (int i = 0; i < HomeActivity.m_jsonGameSlider.length(); i++) {
                    JSONObject slider_object = HomeActivity.m_jsonGameSlider.getJSONObject(i);
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

                    slider_list.add(new SliderBean(id, description, title, image, name));

                }
                NUM_PAGES = slider_list.size();
                fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getChildFragmentManager(), slider_list);
                fragment_game_all_content_pager.setAdapter(fragmentPagerItemAdapter);

                DisplayMetrics displaymetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                fragment_game_all_content_pager_layout.getLayoutParams().height = height / 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    fragment_game_all_content_pager.setScrollBarFadeDuration(5);
                }
                fragment_game_all_content_pager.setPageMargin(slider_list.size());

                fragment_game_all_content_pager.setClipChildren(false);

                //        mViewPager.setActivity(this);

                fragment_game_all_content_pager.onHoverChanged(true);

                fragment_game_all_content_pager.setOffscreenPageLimit(5);
                fragment_game_all_content_pager_dots.removeAllViews();
                addDots();
                selectDot(0);


            } else {
                new LoadingGameSlider().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        try {
            if (!TextUtils.isEmpty(mParam2)) {
                JSONArray data_array = new JSONArray(mParam2);
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

            return FragmentViewPagerItem.newInstance(Constants.GAMES, String.valueOf(pos));
        }

        @Override
        public int getCount() {
            return list.size();
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
            fragment_game_all_content_pager_dots.addView(dot, params);
            dots.add(dot);
        }
        fragment_game_all_content_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
                FragmentViewPagerItem mFragment = (FragmentViewPagerItem) fragmentPagerItemAdapter.instantiateItem(fragment_game_all_content_pager, position);
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

                    setlistadapter(horizontal_listview, listData, width - (spacing * 4));


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


                    GameMainFragment.sContent_position = Constants.GAMES_SUBCONTENT;
                    HomeActivity.toolbarSubContent = name;
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    HomeActivity.carousel_id = id;
                    new LoadingAllTVfeaturedCarouselsById().execute(Integer.toString(id));


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamic_horizontalViews_layout.addView(itemlayout);
    }

    public void text_font_typeface() {
        OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
        osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
    }

    private void setlistadapter(RecyclerView horizontal_listview, ArrayList<HorizontalListitemBean> listData, int i) {


        for (int j = 0; j < listData.size(); j++) {
           /* if (listData.get(j).getType().equalsIgnoreCase("s")) {
                adpater = new HorizontalListAdapter(listData, getContext(), i, this, itemsname);
                horizontal_listview.setAdapter(adpater);
            } else {*/
            NetworkGridAdapter adpater = new NetworkGridAdapter(listData, getContext(), i, GamesDetailsFragment.this, GamesDetailsFragment.this, itemsname);
            horizontal_listview.setAdapter(adpater);
//            }
        }


    }


    @Override
    public void onShowGridItemSelected(int id, String type, String name) {

        mId=id;
        new LoadingGameDeatilsById().execute();
    }

    @Override
    public void onGriditemSelected(String network, String id, String type, String name) {
        mId= Integer.parseInt(id);
        new LoadingGameDeatilsById().execute();
    }


    public class LoadingGameDeatilsById extends AsyncTask<String, Object, Object> {
        String description = "", title = "", image = "", name = "";
        int id;

        String url = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            HomeActivity.m_jsonGameDeatils = JSONRPCAPI.getGAMEDetail(mId);
            if (HomeActivity.m_jsonGameDeatils == null) return null;
            Log.d("allCarousels::", "allCarousels::" + HomeActivity.m_jsonGameDeatils);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                JSONObject data_object = new JSONObject(HomeActivity.m_jsonGameDeatils.toString());

                if (data_object.has("url")) {
                    url = data_object.getString("url");

                } else {
                    url = "";
                }

                progressBar2.setVisibility(View.GONE);

                if (!url.equalsIgnoreCase("")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


   /* private class LoadingGameSlider extends AsyncTask<Object, Object, Object> {
        String description = "", title = "", image = "", name = "";
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {

            HomeActivity.m_jsonGameSlider = JSONRPCAPI.getGameSliderList();
            if (   HomeActivity.m_jsonGameSlider == null) return null;
            Log.d("Slider::", "slidelist::" + HomeActivity.m_jsonGameSlider);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                if (HomeActivity.m_jsonGameSlider.length() > 0) {
                    slider_list = new ArrayList<>();
                    for (int i = 0; i < HomeActivity.m_jsonGameSlider.length(); i++) {
                        JSONObject slider_object = HomeActivity.m_jsonGameSlider.getJSONObject(i);
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

                        slider_list.add(new SliderBean(id, description, title, image, name));

                    }
                    NUM_PAGES = slider_list.size();
                    fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getChildFragmentManager(), slider_list);
                    fragment_game_all_content_pager.setAdapter(fragmentPagerItemAdapter);
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int height = displaymetrics.heightPixels;
                    int width = displaymetrics.widthPixels;
                    fragment_game_all_content_pager_layout.getLayoutParams().height = height / 2;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        fragment_game_all_content_pager.setScrollBarFadeDuration(5);
                    }
                    fragment_game_all_content_pager.setPageMargin(slider_list.size());

                    fragment_game_all_content_pager.setClipChildren(false);

//        mViewPager.setActivity(this);

                    fragment_game_all_content_pager.onHoverChanged(true);

                    fragment_game_all_content_pager.setOffscreenPageLimit(5);
                    fragment_game_all_content_pager_dots.removeAllViews();
                    addDots();
                    selectDot(0);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/


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

            HomeActivity.allCarousels = JSONRPCAPI.getAllGames(Integer.parseInt(params[0]), 1000, 0, HomeActivity.mfreeorall);
            if ( HomeActivity.allCarousels  == null) return null;
            Log.d("allCarousels::", "allCarousels::" + HomeActivity.allCarousels);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                GameMainFragment.sContent_position = Constants.GAMES_SUBCONTENT;
                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                GridFragment listfragment = GridFragment.newInstance(Constants.GAMES, HomeActivity.allCarousels.toString());
                fragmentTransaction_list.replace(R.id.fragment_games_pagerandlist, listfragment);
                fragmentTransaction_list.commit();
                progressBar2.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
