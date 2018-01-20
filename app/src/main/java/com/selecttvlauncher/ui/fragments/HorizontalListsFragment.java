package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
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

import com.selecttvlauncher.Adapter.GridAdapter;
import com.selecttvlauncher.Adapter.HorizontalListAdapter;
import com.selecttvlauncher.Adapter.MovieListAdapter;
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
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HorizontalListsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HorizontalListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HorizontalListsFragment extends Fragment implements GridAdapter.onGridSelectedListener, NetworkGridAdapter.GridClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String grid_type;
    private String data;

    private OnFragmentInteractionListener mListener;

    private LinearLayout dynamic_horizontalViews_layout;
    private LayoutInflater inflate;
    private ArrayList<Integer> images = new ArrayList<Integer>();
    private ArrayList<String> titles = new ArrayList<>();
    private Typeface OpenSans_Regular;
    private JSONArray m_jsonTvshowbyCategoryList;
    int mCategoryListId;
    private HorizontalListAdapter horiadpater;
    private MovieListAdapter movieListAdapter;
    private int spacing;
    private int width;
    private int height;
    private ArrayList<HorizontalListBean> horizontalListdata;
    private String title = null;
    private String type;
    private int itemsid;
    private String itemsname;
    ArrayList<HorizontalListitemBean> horizontalListBeans;
    private String itemscarousel_image;
    private int networkcall = 0;
    private ProgressBar progressBar;

    int categoryLimit = 10;
    private ViewPager fragment_ondemand_all_content_pager;
    private RelativeLayout fragment_ondemand_all_content_pager_layout;
    private LinearLayout content_layout;
    private LinearLayout fragment_ondemand_all_content_pager_dots;
    private ArrayList<SliderBean> slider_list = new ArrayList<>();
    private int NUM_PAGES;
    private FragmentPagerItemAdapter fragmentPagerItemAdapter,fragmentPagerItemAdapter1;
    private FragmentShowPagerItemAdapter fragmentShowPagerItemAdapter;
    private ArrayList<ImageView> dots = new ArrayList<>();
    private Timer timer;

    RecyclerView.RecycledViewPool mPool = new RecyclerView.RecycledViewPool(){
        @Override
        public RecyclerView.ViewHolder getRecycledView(int viewType) {
            RecyclerView.ViewHolder scrap = super.getRecycledView(viewType);
            Log.i("@@@", "get holder from pool: "+scrap );
            return scrap;
        }

        @Override
        public void putRecycledView(RecyclerView.ViewHolder scrap) {
            super.putRecycledView(scrap);
            Log.i("@@@", "put holder to pool: " + scrap);
        }

        @Override
        public String toString() {
            return "ViewPool in adapter@"+Integer.toHexString(hashCode());
        }
    };


    public HorizontalListsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HorizontalListsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HorizontalListsFragment newInstance(String param1, String param2) {
        HorizontalListsFragment fragment = new HorizontalListsFragment();
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
            grid_type = getArguments().getString(ARG_PARAM1);
            data = getArguments().getString(ARG_PARAM2);
            networkcall = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_horizontal_lists, container, false);
        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        fragment_ondemand_all_content_pager_layout = (RelativeLayout) rootview.findViewById(R.id.fragment_ondemand_all_content_pager_layout);
        content_layout = (LinearLayout) rootview.findViewById(R.id.content_layout);
        fragment_ondemand_all_content_pager_dots = (LinearLayout) rootview.findViewById(R.id.fragment_ondemand_all_content_pager_dots);
        fragment_ondemand_all_content_pager = (ViewPager) rootview.findViewById(R.id.fragment_ondemand_all_content_pager);
        fragment_ondemand_all_content_pager.removeAllViews();
        dynamic_horizontalViews_layout = (LinearLayout) rootview.findViewById(R.id.dynamic_horizontalViews_layout);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        try {
            if (grid_type.equalsIgnoreCase(Constants.NETWORK)) {
                fragment_ondemand_all_content_pager_layout.setVisibility(View.VISIBLE);

                if(HomeActivity.isPayperview.equals("Pay Per View")){
                    if( HomeActivity.ppv_selected_item.equalsIgnoreCase("show")){

                        new LoadingSlider().execute("show");
                    }else{
                        new LoadingSlider().execute("all");
                    }


                }else{
                    if (HomeActivity.m_jsonShowSlider != null && HomeActivity.m_jsonShowSlider.length() > 0) {
                        int id = 0;
                        String name = "", image = "", description = "",etype="";
                        slider_list = new ArrayList<>();
                        for (int i = 0; i < HomeActivity.m_jsonShowSlider.length(); i++) {
                            JSONObject slider_object = HomeActivity.m_jsonShowSlider.getJSONObject(i);
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

                            slider_list.add(new SliderBean(id, description, title, image, name,etype));

                        }
                        NUM_PAGES = slider_list.size();
                        fragmentShowPagerItemAdapter = new FragmentShowPagerItemAdapter(getChildFragmentManager(), slider_list, Constants.NETWORK);
                        fragment_ondemand_all_content_pager.setAdapter(fragmentShowPagerItemAdapter);

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
                        addDotsShows();
                        selectDotShows(0);


                    } else {
                        new LoadingShowSlider().execute();
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            if (grid_type.equalsIgnoreCase(Constants.MOVIES)) {
                fragment_ondemand_all_content_pager_layout.setVisibility(View.VISIBLE);

                if(HomeActivity.isPayperview.equals("Pay Per View")){
                    new LoadingSlider().execute("movie");

                }else{
                    if (HomeActivity.m_jsonMovieSlider != null && HomeActivity.m_jsonMovieSlider.length() > 0) {
                        int id = 0;
                        String name = "", image = "", description = "",etype="";
                        slider_list = new ArrayList<>();
                        for (int i = 0; i < HomeActivity.m_jsonMovieSlider.length(); i++) {
                            JSONObject slider_object = HomeActivity.m_jsonMovieSlider.getJSONObject(i);
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
                                etype= slider_object.getString("type");
                            }

                            slider_list.add(new SliderBean(id, description, title, image, name,etype));

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
                        new LoadingMoviesSlider().execute();
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (grid_type.equalsIgnoreCase(Constants.SPORTS)) {


                fragment_ondemand_all_content_pager_layout.setVisibility(View.VISIBLE);
                if (HomeActivity.m_jsonSportsSlider != null && HomeActivity.m_jsonSportsSlider.length() > 0) {
                    int id = 0;
                    String name = "", image = "", description = "",etype="";
                    slider_list = new ArrayList<>();
                    for (int i = 0; i < HomeActivity.m_jsonSportsSlider.length(); i++) {
                        JSONObject slider_object = HomeActivity.m_jsonSportsSlider.getJSONObject(i);
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

                        slider_list.add(new SliderBean(id, description, title, image, name,etype));

                    }
                    NUM_PAGES = slider_list.size();
                    fragmentShowPagerItemAdapter = new FragmentShowPagerItemAdapter(getChildFragmentManager(), slider_list, Constants.SPORTS);
                    fragment_ondemand_all_content_pager.setAdapter(fragmentShowPagerItemAdapter);

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
                    addDotsShows();
                    selectDotShows(0);


                } else {
                    new LoadingSportsSlider().execute();
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            if (grid_type.equalsIgnoreCase(Constants.SPORTS)) {
                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);
                    horizontalListdata = new ArrayList<>();


                    int id = 0;
                    String name = "", image = "", description = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        horizontalListBeans = new ArrayList<>();
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


                            if (horizontalListBeans != null && horizontalListBeans.size() != 0) {
                                addlayouts(dynamic_horizontalViews_layout, title, id, horizontalListBeans, itemsarray);
                            }

                            //addhorizontalScrolllayouts(dynamic_horizontalViews_layout, title, id, horizontalListBeans, itemsarray);


                        }


                        //addlayouts(dynamic_horizontalViews_layout, name, id);
//                        images.add(Integer.valueOf(id));
//                        titles.add(name);
                    }
                }
            } else if (grid_type.equalsIgnoreCase(Constants.NETWORK)||grid_type.equalsIgnoreCase(Constants.PRIMETIMEANYTIME)) {
               /* images.add(R.drawable.abc_logo);
                images.add(R.drawable.cw_logo);
                images.add(R.drawable.fox_logo);
                images.add(R.drawable.pbs_logo);
                images.add(R.drawable.tvland_logo);
                images.add(R.drawable.vh1_logo);
                images.add(R.drawable.abc_logo);
                images.add(R.drawable.cw_logo);
                images.add(R.drawable.fox_logo);
                images.add(R.drawable.pbs_logo);
                images.add(R.drawable.tvland_logo);
                images.add(R.drawable.vh1_logo);*/


                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);
                    horizontalListdata = new ArrayList<>();


                    int id = 0;
                    String name = "", image = "", description = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        horizontalListBeans = new ArrayList<>();
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


                            if (horizontalListBeans != null && horizontalListBeans.size() != 0) {
                                addlayouts(dynamic_horizontalViews_layout, title, id, horizontalListBeans, itemsarray);
                            }

                            //addhorizontalScrolllayouts(dynamic_horizontalViews_layout, title, id, horizontalListBeans, itemsarray);

                        }


                        //addlayouts(dynamic_horizontalViews_layout, name, id);
//                        images.add(Integer.valueOf(id));
//                        titles.add(name);
                    }
                }

                try {

                    if (grid_type.equalsIgnoreCase(Constants.PRIMETIMEANYTIME)) {

                        new LoadingPrimeTimeSlider().execute();

                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            } else if (grid_type.equalsIgnoreCase(Constants.BYNETWORK))

            {

               /* images.add(R.drawable.abc_logo);
                images.add(R.drawable.cw_logo);
                images.add(R.drawable.fox_logo);
                images.add(R.drawable.pbs_logo);
                images.add(R.drawable.tvland_logo);
                images.add(R.drawable.vh1_logo);
                images.add(R.drawable.abc_logo);
                images.add(R.drawable.cw_logo);
                images.add(R.drawable.fox_logo);
                images.add(R.drawable.pbs_logo);
                images.add(R.drawable.tvland_logo);
                images.add(R.drawable.vh1_logo);
*/

                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);
                    horizontalListdata = new ArrayList<>();


                    int id = 0;
                    String name = "", image = "", description = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        horizontalListBeans = new ArrayList<>();
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

                            if (horizontalListBeans != null && horizontalListBeans.size() != 0) {
                                addlayouts(dynamic_horizontalViews_layout, title, id, horizontalListBeans, itemsarray);
                            }


                        }


                        //addlayouts(dynamic_horizontalViews_layout, name, id);
//                        images.add(Integer.valueOf(id));
//                        titles.add(name);
                    }
                }


            } else if (grid_type.equalsIgnoreCase(Constants.MOVIES)) {


                /*images.add(R.drawable.abc_logo);
                images.add(R.drawable.cw_logo);
                images.add(R.drawable.fox_logo);
                images.add(R.drawable.pbs_logo);
                images.add(R.drawable.tvland_logo);
                images.add(R.drawable.vh1_logo);
                images.add(R.drawable.abc_logo);
                images.add(R.drawable.cw_logo);
                images.add(R.drawable.fox_logo);
                images.add(R.drawable.pbs_logo);
                images.add(R.drawable.tvland_logo);
                images.add(R.drawable.vh1_logo);
*/

                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);
                    horizontalListdata = new ArrayList<>();


                    int id = 0;
                    String name = "", image = "", description = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        horizontalListBeans = new ArrayList<>();
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

                            if (horizontalListBeans != null && horizontalListBeans.size() != 0) {
                                addlayouts(dynamic_horizontalViews_layout, title, id, horizontalListBeans, itemsarray);
                            }


                        }


                        //addlayouts(dynamic_horizontalViews_layout, name, id);
//                        images.add(Integer.valueOf(id));
//                        titles.add(name);
                    }
                }


            } else if(grid_type.equals(Constants.CATEGORY_SHOW)){
                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);
                    horizontalListdata = new ArrayList<>();


                    int id = 0;
                    String name = "", image = "", description = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        horizontalListBeans = new ArrayList<>();
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


                            if (horizontalListBeans != null && horizontalListBeans.size() != 0) {
                                addlayouts(dynamic_horizontalViews_layout, title, id, horizontalListBeans, itemsarray);
                            }


                        }



                    }
                }
                new LoadingCategorySlider().execute(FragmentOndemandallList.mCategoryid);
            }
            else if(grid_type.equals(Constants.WEB_ORIGINALS_SIDE_MENU)) {

                try {
                    if (HomeActivity.web_slider_list.size() > 0) {
                        fragment_ondemand_all_content_pager_layout.setVisibility(View.VISIBLE);

                        NUM_PAGES = HomeActivity.web_slider_list.size();
                        fragmentShowPagerItemAdapter = new FragmentShowPagerItemAdapter(getChildFragmentManager(), HomeActivity.web_slider_list, Constants.WEB_ORIGINALS_SIDE_MENU);
                        fragment_ondemand_all_content_pager.setAdapter(fragmentShowPagerItemAdapter);

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
                        addDotsShows();
                        selectDotShows(0);
                    } else {
                        fragment_ondemand_all_content_pager_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);
                    horizontalListdata = new ArrayList<>();


                    int id = 0;
                    String name = "", image = "", description = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        horizontalListBeans = new ArrayList<>();
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
                            if (horizontalListBeans != null && horizontalListBeans.size() != 0) {
                                addlayouts(dynamic_horizontalViews_layout, title, id, horizontalListBeans, itemsarray);
                            }
                        }

                    }


                }
            }
            else if(grid_type.equals(Constants.KIDS)) {

                try {
                    if (HomeActivity.kids_slider_list.size() > 0) {
                        fragment_ondemand_all_content_pager_layout.setVisibility(View.VISIBLE);

                        NUM_PAGES = HomeActivity.kids_slider_list.size();
                        fragmentShowPagerItemAdapter = new FragmentShowPagerItemAdapter(getChildFragmentManager(), HomeActivity.kids_slider_list, Constants.KIDS);
                        fragment_ondemand_all_content_pager.setAdapter(fragmentShowPagerItemAdapter);

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
                        addDotsShows();
                        selectDotShows(0);
                    } else {
                        fragment_ondemand_all_content_pager_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);
                    horizontalListdata = new ArrayList<>();


                    int id = 0;
                    String name = "", image = "", description = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        horizontalListBeans = new ArrayList<>();
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
                            if (horizontalListBeans != null && horizontalListBeans.size() != 0) {
                                addlayouts(dynamic_horizontalViews_layout, title, id, horizontalListBeans, itemsarray);
                            }
                        }

                    }


                }
            }



            else {
               /* images.add(R.drawable.img1);
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


                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);
                    horizontalListdata = new ArrayList<>();


                    int id = 0;
                    String name = "", image = "", description = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        JSONObject object = data_array.getJSONObject(i);
                        if (object.has("id")) {
                            id = object.getInt("id");
                        }
                        if (object.has("name")) {
                            name = object.getString("name");
                        }
                        new LoadingCategoryList().execute(String.valueOf(id), name, "category");
                        //addlayouts(dynamic_horizontalViews_layout, name, id);
//                        images.add(Integer.valueOf(id));
//                        titles.add(name);
                    }
                }
            }


           /* titles.add("TV Networks");
            titles.add("Most Watched");
            titles.add("Popular Broadcast Networks");
            titles.add("Popular Broadcast Networks");*/

            for (int i = 0; i < images.size(); i++) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootview;
    }

    private void addDotsShows() {

        Log.d("num_pages", "______" + NUM_PAGES);
        for (int i = 0; i < NUM_PAGES; i++) {
            try {
                ImageView dot = new ImageView(getActivity());
                dot.setImageDrawable(getResources().getDrawable(R.drawable.dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(2, 5, 2, 5);
                fragment_ondemand_all_content_pager_dots.addView(dot, params);
                dots.add(dot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fragment_ondemand_all_content_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    selectDotShows(position);
                    if(fragmentShowPagerItemAdapter!=null){
                        FragmentViewPagerItem mFragment = (FragmentViewPagerItem) fragmentShowPagerItemAdapter.instantiateItem(fragment_ondemand_all_content_pager, position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

    private void selectDotShows(int idx) {
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
                FragmentMovieViewPagerItem mFragment = (FragmentMovieViewPagerItem) fragmentPagerItemAdapter.instantiateItem(fragment_ondemand_all_content_pager, position);
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

    private void addlayouts(final LinearLayout dynamic_horizontalViews_layout, final String name, final int id, final ArrayList<HorizontalListitemBean> listData, final JSONArray array) {
        final View itemlayout = (View) inflate.inflate(R.layout.horizontal_list_layout, null);
        final TextView horizontal_listview_title = (TextView) itemlayout.findViewById(R.id.horizontal_listview_title);
        TextView view_all_text = (TextView) itemlayout.findViewById(R.id.view_all_text);
        horizontal_listview_title.setTypeface(OpenSans_Regular);
        view_all_text.setTypeface(OpenSans_Regular);
        final LinearLayout recycler_layout = (LinearLayout) itemlayout.findViewById(R.id.recycler_layout);
        final RecyclerView horizontal_listview = (RecyclerView) itemlayout.findViewById(R.id.horizontal_listview);

        horizontal_listview.setTag(id);
        ImageView left_slide = (ImageView) itemlayout.findViewById(R.id.left_slide);
        ImageView right_slide = (ImageView) itemlayout.findViewById(R.id.right_slide);


        horizontal_listview_title.setText(Utilities.stripHtml(name));


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
        int layoutwidth = recycler_layout.getLayoutParams().width;
        Log.d("layoutwidth::", "layoutwidth" + layoutwidth);

        try {
            ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    width = recycler_layout.getMeasuredWidth();
                    height = recycler_layout.getMeasuredHeight();

                    if (grid_type.equalsIgnoreCase(Constants.BYNETWORK)) {
                        NetworkGridAdapter adpater = new NetworkGridAdapter(listData, getContext(), width - (spacing * 3), HorizontalListsFragment.this, HorizontalListsFragment.this, name);
                        horizontal_listview.setAdapter(adpater);
                    } else if (grid_type.equalsIgnoreCase(Constants.MOVIES)) {


                        Log.d("MOVIES", "List" + listData);
                        movieListAdapter = new MovieListAdapter(listData, getContext(), width - (spacing * 3), HorizontalListsFragment.this, name);
                        horizontal_listview.setAdapter(movieListAdapter);


                    } else {


//                        for (int i = 0; i < listData.size(); i++) {
//                            if (listData.get(i).getType().equalsIgnoreCase("s")) {
                              /*  horiadpater = new HorizontalListAdapter(listData, getContext(), width - (spacing * 3), HorizontalListsFragment.this, name);
                                horizontal_listview.setAdapter(horiadpater);*/
//                            } else {
                        NetworkGridAdapter adpater = new NetworkGridAdapter(listData, getContext(), width - (spacing * 3), HorizontalListsFragment.this, HorizontalListsFragment.this, name);
                        horizontal_listview.setAdapter(adpater);
//                            }
//                        }


                    }


                    Log.d("layoutwidth::", "width" + width);

                }
            });

            Utilities.setViewFocus(getActivity(), right_slide);
            Utilities.setViewFocus(getActivity(),left_slide);
            Utilities.setTextFocus(getActivity(),view_all_text);


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

                    HomeActivity.toolbarSubContent = name;
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                    /*if (HomeActivity.selectedFilter.equals(Constants.CATEGORY_FILTER)) {

                        categoryLimit = 1000;

                        new LoadingCategoryList().execute(String.valueOf(id), name, "category");

                        //addlayouts(dynamic_horizontalViews_layout, name, id);
//                        images.add(Integer.valueOf(id));
//                        titles.add(name);

                    } else {*/

                        new LoadingAllTVfeaturedCarouselsById().execute(Integer.toString(id));

                        HomeActivity.carousel_id = id;
//                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamic_horizontalViews_layout.addView(itemlayout);
    }

    private void addhorizontalScrolllayouts(final LinearLayout dynamic_horizontalViews_layout, final String name, final int id, final ArrayList<HorizontalListitemBean> listData, final JSONArray array) {
        final View itemlayout = (View) inflate.inflate(R.layout.horizontal_test, null);
        final TextView horizontal_listview_title = (TextView) itemlayout.findViewById(R.id.horizontal_listview_title);
        TextView view_all_text = (TextView) itemlayout.findViewById(R.id.view_all_text);
        horizontal_listview_title.setTypeface(OpenSans_Regular);
        view_all_text.setTypeface(OpenSans_Regular);
        final LinearLayout recycler_layout = (LinearLayout) itemlayout.findViewById(R.id.recycler_layout);
        final LinearLayout image_view = (LinearLayout) itemlayout.findViewById(R.id.image_view);

        ImageView left_slide = (ImageView) itemlayout.findViewById(R.id.left_slide);
        ImageView right_slide = (ImageView) itemlayout.findViewById(R.id.right_slide);

        if (listData.size() != 0) {
            horizontal_listview_title.setText(Utilities.stripHtml(name));
        }


        final LinearLayoutManager linear_layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        int spanCount = listData.size(); // 3 columns
        spacing = 20; // 50px

        boolean includeEdge = false;
        int layoutwidth = recycler_layout.getLayoutParams().width;
        Log.d("layoutwidth::", "layoutwidth" + layoutwidth);

        try {
            ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    width = recycler_layout.getMeasuredWidth();
                    height = recycler_layout.getMeasuredHeight();

                    if (grid_type.equalsIgnoreCase(Constants.BYNETWORK)) {

                        for (int l = 0; l < listData.size(); l++) {
                            final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_list_item, null);

                            DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams((width / 4) - 60, LinearLayout.LayoutParams.WRAP_CONTENT);
                            if (l != 0) {
                                vp.setMargins(20, 0, 0, 0);
                            }
                            horizontal_imageView.setLayoutParams(vp);
                            String image_url = listData.get(l).getImage();
                            horizontal_imageView.loadImage(image_url);
                            final String type = listData.get(l).getType();

                            image_view.addView(itemmlayout);

                        }

                    } else if (grid_type.equalsIgnoreCase(Constants.MOVIES)) {


                        for (int l = 0; l < listData.size(); l++) {
                            final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_list_item, null);

                            DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams((width / 4) - 60, LinearLayout.LayoutParams.WRAP_CONTENT);
                            if (l != 0) {
                                vp.setMargins(20, 0, 0, 0);
                            }
                            horizontal_imageView.setLayoutParams(vp);
                            String image_url = listData.get(l).getImage();
                            horizontal_imageView.loadImage(image_url);
                            final String type = listData.get(l).getType();

                            image_view.addView(itemmlayout);

                        }


                    } else {


                        for (int l = 0; l < listData.size(); l++) {
                            final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_list_item, null);

                            DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams((width / 4) - 60, LinearLayout.LayoutParams.WRAP_CONTENT);
                            if (l != 0) {
                                vp.setMargins(20, 0, 0, 0);
                            }
                            horizontal_imageView.setLayoutParams(vp);
                            String image_url = listData.get(l).getImage();
                            horizontal_imageView.loadImage(image_url);
                            final String type = listData.get(l).getType();

                            image_view.addView(itemmlayout);

                        }


                    }


                    Log.d("layoutwidth::", "width" + width);

                }
            });


            right_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int firstVisiblePosition = linear_layoutManager.findLastCompletelyVisibleItemPosition();
                    firstVisiblePosition = firstVisiblePosition + 4;
                    Log.d("list::", "::" + firstVisiblePosition + ":::" + linear_layoutManager.findFirstVisibleItemPosition() + "::" + linear_layoutManager.findLastVisibleItemPosition() + "::" + linear_layoutManager.findLastCompletelyVisibleItemPosition());
                }
            });

            left_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int firstVisiblePosition = linear_layoutManager.findFirstVisibleItemPosition();
                    firstVisiblePosition = firstVisiblePosition - 4;
                    Log.d("list::", "::" + firstVisiblePosition + ":::" + linear_layoutManager.findFirstVisibleItemPosition() + "::" + linear_layoutManager.findLastVisibleItemPosition() + "::" + linear_layoutManager.findLastCompletelyVisibleItemPosition());


                }
            });
            view_all_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HomeActivity.toolbarSubContent = name;
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                  /*  if (HomeActivity.selectedFilter.equals(Constants.CATEGORY_FILTER)) {

                        categoryLimit = 1000;

                        new LoadingCategoryList().execute(String.valueOf(id), name, "category");

                        //addlayouts(dynamic_horizontalViews_layout, name, id);
//                        images.add(Integer.valueOf(id));
//                        titles.add(name);

                    } else {
*/
                        new LoadingAllTVfeaturedCarouselsById().execute(Integer.toString(id));

                        HomeActivity.carousel_id = id;
//                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamic_horizontalViews_layout.addView(itemlayout);
    }

    private void setListadapter(RecyclerView horizontal_listview, ArrayList<HorizontalListitemBean> listData) {
        horiadpater = new HorizontalListAdapter(listData, getContext(), width - (spacing * 3), HorizontalListsFragment.this, itemsname);
        horizontal_listview.setAdapter(horiadpater);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

    @Override
    public void onShowGridItemSelected(int id, String type, String name) {
        Log.d("Selected show::::", "id:::" + id);
        HomeActivity.toolbarSubContent = name;

                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


        if (type.equalsIgnoreCase("N") || type.equalsIgnoreCase("L")) {
            HomeActivity.content_position = Constants.SUB_RIGHT_CONTENT;

            new LoadingTVNetworkList().execute(String.valueOf(id));
            HomeActivity.mNetworkid=id;

        } else {
            if (getActivity() != null) {
                if(type.equalsIgnoreCase("m")){
                    ((HomeActivity) getActivity()).SwapMovieFragment(true);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, id);
                    fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                    fragmentTransaction.commit();
                }else{
                    ((HomeActivity) getActivity()).SwapMovieFragment(true);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, id);
                    fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                    fragmentTransaction.commit();
                }

            }
        }


    }

    @Override
    public void onGriditemSelected(String network, String id, String type, String name) {

        HomeActivity.toolbarSubContent = name;
        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
        if (HomeActivity.selectedmenu.equalsIgnoreCase(Constants.MOVIES)) {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, Integer.parseInt(id));
            fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
            fragmentTransaction.commit();
            ((HomeActivity) getActivity()).SwapMovieFragment(true);
        } else if (network.equalsIgnoreCase(Constants.NETWORK)) {

            HomeActivity.content_position = Constants.SUB_RIGHT_CONTENT;
            new LoadingTVNetworkList().execute(id);
            HomeActivity.mNetworkid= Integer.parseInt(id);
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class LoadingCategoryList extends AsyncTask<String, Object, Object> {
        private String name = "", carousel_image = "", type = "";
        private int response_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            dynamic_horizontalViews_layout.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(final String... params) {

            try {


                if (categoryLimit != 1000) {
                    m_jsonTvshowbyCategoryList = JSONRPCAPI.getTVshowCategoryImagesbyId(Integer.parseInt(params[0]), categoryLimit, 0, HomeActivity.mfreeorall);
                    if (m_jsonTvshowbyCategoryList  == null) return null;
                    Log.d("Category::", "Category::" + m_jsonTvshowbyCategoryList);
                    Log.d("mCategoryListId::", "mCategoryListId::" + Integer.parseInt(params[0]));
                } else {
                    HomeActivity.m_jsonTvshowbyCategoryListAll = JSONRPCAPI.getTVshowCategoryImagesbyId(Integer.parseInt(params[0]), categoryLimit, 0, HomeActivity.mfreeorall);
                    if (HomeActivity.m_jsonTvshowbyCategoryListAll  == null) return null;
                    Log.d("Category::", "Category::" + HomeActivity.m_jsonTvshowbyCategoryListAll);
                    Log.d("mCategoryListId::", "mCategoryListId::" + Integer.parseInt(params[0]));
                }


                final ArrayList<HorizontalListitemBean> list = new ArrayList<>();
                if (categoryLimit != 1000) {
                    if (m_jsonTvshowbyCategoryList.length() > 0) {
                        for (int i = 0; i < m_jsonTvshowbyCategoryList.length(); i++) {

                            JSONObject object = null;
                            try {
                                object = m_jsonTvshowbyCategoryList.getJSONObject(i);

                                if (object.has("carousel_image")) {
                                    carousel_image = object.getString("carousel_image");
                                }
                                if (object.has("type")) {
                                    type = object.getString("type");
                                }
                                if (object.has("id")) {
                                    response_id = object.getInt("id");
                                }
                                if (object.has("name")) {
                                    name = object.getString("name");
                                }

                                list.add(new HorizontalListitemBean(response_id, carousel_image, type, name));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            dynamic_horizontalViews_layout.setVisibility(View.VISIBLE);
                            if (list.size() != 0) {
                                addlayouts(dynamic_horizontalViews_layout, params[1], Integer.parseInt(params[0]), list, m_jsonTvshowbyCategoryList);
                            }


                        }
                    });
                } else {
                    if(HomeActivity.getmFragmentOnDemandAll()!=null){
                        HomeActivity.selectedFilter = Constants.CATEGORY_FILTER;
                        HomeActivity.content_position = Constants.SUB_CONTENT;
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction_list = manager.beginTransaction();
                        GridFragment listfragment = GridFragment.newInstance(Constants.CATEGORY_SHOW, HomeActivity.m_jsonTvshowbyCategoryListAll.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressBar.setVisibility(View.GONE);
            dynamic_horizontalViews_layout.setVisibility(View.VISIBLE);
           /* try {
                if (m_jsonTvshowbyCategoryList.length() > 0) {
                    for (int i = 0; i < m_jsonTvshowbyCategoryList.length(); i++) {
                        JSONObject object = null;
                        try {
                            object = m_jsonTvshowbyCategoryList.getJSONObject(i);

                            if (object.has("carousel_image")) {
                                name = object.getString("carousel_image");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    titles.add(name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    public class LoadingAllTVfeaturedCarouselsById extends AsyncTask<String, Object, Object> {
        String description = "", title = "", image = "", name = "";
        int id;
        JSONArray allCarousels;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            dynamic_horizontalViews_layout.setVisibility(View.GONE);
            fragment_ondemand_all_content_pager_layout.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {


            Log.d("mfreeorall::", "id::" + HomeActivity.mfreeorall);
            HomeActivity.allCarousels = JSONRPCAPI.getAllTVfeaturedCarousels(Integer.parseInt(params[0]), 1000, 0, HomeActivity.mfreeorall);
            if ( HomeActivity.allCarousels  == null) return null;
            Log.d("allCarousels::", "allCarousels::" + HomeActivity.allCarousels);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                progressBar.setVisibility(View.GONE);
                dynamic_horizontalViews_layout.setVisibility(View.VISIBLE);
                fragment_ondemand_all_content_pager_layout.setVisibility(View.VISIBLE);
                if (grid_type.equalsIgnoreCase(Constants.BYNETWORK)) {
                    if(HomeActivity.getmFragmentOnDemandAll()!=null){
                        HomeActivity.content_position = Constants.SUB_RIGHT_CONTENT;
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        GridFragment listfragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.allCarousels.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                } else if (grid_type.equalsIgnoreCase(Constants.MOVIES)) {
                    if(HomeActivity.getmFragmentOnDemandAll()!=null){
                        HomeActivity.content_position = Constants.SUB_CONTENT;

                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        GridFragment listfragment = GridFragment.newInstance(Constants.MOVIES, HomeActivity.allCarousels.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }


                } else

                {
                    if(HomeActivity.getmFragmentOnDemandAll()!=null){
                        HomeActivity.content_position = Constants.SUB_CONTENT;
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        GridFragment listfragment = GridFragment.newInstance(Constants.CATEGORY_SHOW, HomeActivity.allCarousels.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class LoadingTVNetworkList extends AsyncTask<String, Object, Object> {
        private JSONArray m_jsonNetworkList;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            dynamic_horizontalViews_layout.setVisibility(View.GONE);
            fragment_ondemand_all_content_pager_layout.setVisibility(View.GONE);
            /*FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
            DialogFragment dialog_fragment = new DialogFragment();
            network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
            network_fragmentTransaction.commit();*/
            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
                m_jsonNetworkList = JSONRPCAPI.getTVNetworkList(Integer.parseInt(params[0]), 1000, 0, HomeActivity.mfreeorall);
                if (m_jsonNetworkList  == null) return null;
                Log.d("NetworkList::", "Selected Network list::" + m_jsonNetworkList);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                progressBar.setVisibility(View.GONE);
                dynamic_horizontalViews_layout.setVisibility(View.VISIBLE);
                fragment_ondemand_all_content_pager_layout.setVisibility(View.VISIBLE);
                if (m_jsonNetworkList.length() > 0) {
                    if(HomeActivity.getmFragmentOnDemandAll()!=null){
                        FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                        GridFragment network_listfragment = GridFragment.newInstance(Constants.SHOWS, m_jsonNetworkList.toString());
                        network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, network_listfragment);
                        network_fragmentTransaction.commit();

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
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

            return FragmentMovieViewPagerItem.newInstance(list.toString(), String.valueOf(pos));
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    private class FragmentShowPagerItemAdapter extends FragmentPagerAdapter {
        ArrayList<SliderBean> list;
        FragmentManager fm;
        String slidertype;

        public FragmentShowPagerItemAdapter(FragmentManager fm, ArrayList<SliderBean> list, String slidertype) {
            super(fm);
            this.fm = fm;
            this.list = list;
            this.slidertype = slidertype;
        }

        @Override
        public Fragment getItem(int pos) {

            return FragmentViewPagerItem.newInstance(slidertype, String.valueOf(pos));
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    public class LoadingMoviesSlider extends AsyncTask<Object, Object, Object> {
        String description = "", title = "", image = "", name = "",etype="";
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {

            HomeActivity.m_jsonMovieSlider = JSONRPCAPI.getMovieSliderList();
            if (HomeActivity.m_jsonHomeSlider  == null) return null;
            Log.d("Slider::", "slidelist::" + HomeActivity.m_jsonMovieSlider);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                if (HomeActivity.m_jsonMovieSlider.length() > 0) {
                    slider_list = new ArrayList<>();
                    for (int i = 0; i < HomeActivity.m_jsonMovieSlider.length(); i++) {
                        JSONObject slider_object = HomeActivity.m_jsonMovieSlider.getJSONObject(i);
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

                        slider_list.add(new SliderBean(id, description, title, image, name,etype));

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

    private class LoadingShowSlider extends AsyncTask<Object, Object, Object> {
        String description = "", title = "", image = "", name = "",etype="";
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {

            HomeActivity.m_jsonShowSlider = JSONRPCAPI.getShowSliderList();
            if (HomeActivity.m_jsonShowSlider  == null) return null;
            Log.d("Slider::", "slidelist::" + HomeActivity.m_jsonShowSlider);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                if (HomeActivity.m_jsonShowSlider.length() > 0) {
                    slider_list = new ArrayList<>();
                    for (int i = 0; i < HomeActivity.m_jsonShowSlider.length(); i++) {
                        JSONObject slider_object = HomeActivity.m_jsonShowSlider.getJSONObject(i);
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

                        slider_list.add(new SliderBean(id, description, title, image, name,etype));

                    }
                    NUM_PAGES = slider_list.size();
                    fragmentShowPagerItemAdapter = new FragmentShowPagerItemAdapter(getChildFragmentManager(), slider_list, Constants.NETWORK);
                    fragment_ondemand_all_content_pager.setAdapter(fragmentShowPagerItemAdapter);
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
                    addDotsShows();
                    selectDotShows(0);


                }else{
                    fragment_ondemand_all_content_pager_layout.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadingSportsSlider extends AsyncTask<Object, Object, Object> {
        String description = "", title = "", image = "", name = "";
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {

            HomeActivity.m_jsonSportsSlider = JSONRPCAPI.getSportsSliderList();
            if (HomeActivity.m_jsonSportsSlider  == null) return null;
            Log.d("Slider::", "slidelist::" + HomeActivity.m_jsonSportsSlider);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if (HomeActivity.m_jsonSportsSlider != null && HomeActivity.m_jsonSportsSlider.length() > 0) {
                    int id = 0;
                    String name = "", image = "", description = "",etype="";
                    slider_list = new ArrayList<>();
                    for (int i = 0; i < HomeActivity.m_jsonSportsSlider.length(); i++) {
                        JSONObject slider_object = HomeActivity.m_jsonSportsSlider.getJSONObject(i);
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

                        slider_list.add(new SliderBean(id, description, title, image, name,etype));

                    }
                    NUM_PAGES = slider_list.size();
                    fragmentShowPagerItemAdapter = new FragmentShowPagerItemAdapter(getChildFragmentManager(), slider_list, Constants.SPORTS);
                    fragment_ondemand_all_content_pager.setAdapter(fragmentShowPagerItemAdapter);

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
                    addDotsShows();
                    selectDotShows(0);


                }else{
                    fragment_ondemand_all_content_pager_layout.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
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
    public void onResume() {
        super.onResume();
        try {
                timer = new Timer();

                timer.scheduleAtFixedRate(new Blink_progress(), 3000, 5000);
        } catch (Exception e) {
            e.printStackTrace();
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

                        if(fragmentPagerItemAdapter!=null)
                        {
                            if (fragment_ondemand_all_content_pager.getCurrentItem() + 1 == fragmentPagerItemAdapter.getCount()) {

                                fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getChildFragmentManager(), slider_list);

                                fragment_ondemand_all_content_pager.setAdapter(fragmentPagerItemAdapter);

                            } else {

                                fragment_ondemand_all_content_pager.setCurrentItem(fragment_ondemand_all_content_pager.getCurrentItem() + 1);

                            }

                        }else if(fragmentPagerItemAdapter1!=null)
                        {
                            if (fragment_ondemand_all_content_pager.getCurrentItem() + 1 == fragmentPagerItemAdapter1.getCount()) {

                                fragmentPagerItemAdapter1 = new FragmentPagerItemAdapter(getChildFragmentManager(), slider_list);

                                fragment_ondemand_all_content_pager.setAdapter(fragmentPagerItemAdapter1);

                            } else {

                                fragment_ondemand_all_content_pager.setCurrentItem(fragment_ondemand_all_content_pager.getCurrentItem() + 1);

                            }

                        }else if(fragmentShowPagerItemAdapter!=null)
                        {
                            if (fragment_ondemand_all_content_pager.getCurrentItem() + 1 == fragmentShowPagerItemAdapter.getCount()) {

                                fragmentShowPagerItemAdapter = new FragmentShowPagerItemAdapter(getChildFragmentManager(), slider_list,Constants.NETWORK);

                                fragment_ondemand_all_content_pager.setAdapter(fragmentShowPagerItemAdapter);

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

    private class LoadingPrimeTimeSlider extends AsyncTask<Object, Object, Object> {
        String description = "", title = "", image = "", name = "";
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                HomeActivity.m_jsonPrimeTimeSlider = JSONRPCAPI.primetimeAnytimeSlider(FragmentOndemandallList.dayofweek);
                if (HomeActivity.m_jsonPrimeTimeSlider  == null) return null;
                Log.d("Slider::", "slidelist::" + HomeActivity.m_jsonPrimeTimeSlider);
                if (HomeActivity.m_jsonPrimeTimeSlider != null && HomeActivity.m_jsonPrimeTimeSlider.length() > 0) {
                    int id = 0;
                    String name = "", image = "", description = "",etype="";
                    slider_list = new ArrayList<>();
                    for (int i = 0; i < HomeActivity.m_jsonPrimeTimeSlider.length(); i++) {
                        JSONObject slider_object = HomeActivity.m_jsonPrimeTimeSlider.getJSONObject(i);
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

                        slider_list.add(new SliderBean(id, description, title, image, name,etype));

                    }}
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if(slider_list.size()>0){
                    fragment_ondemand_all_content_pager_layout.setVisibility(View.VISIBLE);

                    NUM_PAGES = slider_list.size();
                    fragmentShowPagerItemAdapter = new FragmentShowPagerItemAdapter(getChildFragmentManager(), slider_list, Constants.PRIMETIMEANYTIME);
                    fragment_ondemand_all_content_pager.setAdapter(fragmentShowPagerItemAdapter);

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
                    addDotsShows();
                    selectDotShows(0);
                }else{
                    fragment_ondemand_all_content_pager_layout.setVisibility(View.GONE);
                }




            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadingCategorySlider extends AsyncTask<Object, Object, Object> {
        String description = "", title = "", image = "", name = "";
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                HomeActivity.m_jsonCategorySlider = JSONRPCAPI.getCategoriesSlider(String.valueOf(params[0]));
                if (HomeActivity.m_jsonCategorySlider  == null) return null;
                Log.d("Slider::", "slidelist::" + HomeActivity.m_jsonCategorySlider);
                if (HomeActivity.m_jsonCategorySlider != null && HomeActivity.m_jsonCategorySlider.length() > 0) {
                    int id = 0;
                    String name = "", image = "", description = "",etype="";
                    slider_list = new ArrayList<>();
                    for (int i = 0; i < HomeActivity.m_jsonCategorySlider.length(); i++) {
                        JSONObject slider_object = HomeActivity.m_jsonCategorySlider.getJSONObject(i);
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

                        slider_list.add(new SliderBean(id, description, title, image, name,etype));

                    }}
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if(slider_list.size()>0){
                    fragment_ondemand_all_content_pager_layout.setVisibility(View.VISIBLE);

                    NUM_PAGES = slider_list.size();
                    fragmentShowPagerItemAdapter = new FragmentShowPagerItemAdapter(getChildFragmentManager(), slider_list, Constants.CATEGORY_SHOW);
                    fragment_ondemand_all_content_pager.setAdapter(fragmentShowPagerItemAdapter);

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
                    addDotsShows();
                    selectDotShows(0);
                }else{
                    fragment_ondemand_all_content_pager_layout.setVisibility(View.GONE);
                }




            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private class LoadingSlider extends AsyncTask<String, Object, Object> {
        String description = "", title = "", image = "", name = "",etype="";
        String type="";
        int id;
        JSONArray m_ppvjsonShowSlider=new JSONArray();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(String... params) {

            type=params[0];
            if(params[0].equalsIgnoreCase("all")){
                m_ppvjsonShowSlider= JSONRPCAPI.getPPVSliderList();
                if ( m_ppvjsonShowSlider  == null) return null;
            }else if(params[0].equalsIgnoreCase("movie")){
                m_ppvjsonShowSlider=JSONRPCAPI.getPPVmoviesSliderList();
                if ( m_ppvjsonShowSlider  == null) return null;

            }else if(params[0].equalsIgnoreCase("show")){
                m_ppvjsonShowSlider=JSONRPCAPI.getPPVShowsSliderList();
                if ( m_ppvjsonShowSlider  == null) return null;

            }
            HomeActivity.m_jsonSlider=m_ppvjsonShowSlider;



            Log.d("Slider::", "slidelist::" + m_ppvjsonShowSlider);

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                if (m_ppvjsonShowSlider.length() > 0) {
                    slider_list = new ArrayList<>();
                    for (int i = 0; i < m_ppvjsonShowSlider.length(); i++) {
                        JSONObject slider_object = m_ppvjsonShowSlider.getJSONObject(i);
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

                        slider_list.add(new SliderBean(id, description, title, image, name,etype));

                    }
                    NUM_PAGES = slider_list.size();
                    if(type.equalsIgnoreCase("movie")){
                        fragmentPagerItemAdapter1 = new FragmentPagerItemAdapter(getChildFragmentManager(), slider_list);
                        fragment_ondemand_all_content_pager.setAdapter(fragmentPagerItemAdapter1);
                    }else{
                        fragmentShowPagerItemAdapter = new FragmentShowPagerItemAdapter(getChildFragmentManager(), slider_list,Constants.NETWORK);
                        fragment_ondemand_all_content_pager.setAdapter(fragmentShowPagerItemAdapter);
                    }

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
                    addDotsShows();
                    selectDotShows(0);





                }else{
                    fragment_ondemand_all_content_pager_layout.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
