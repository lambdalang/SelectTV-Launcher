package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.selecttvlauncher.Adapter.HorizontalListAdapter;
import com.selecttvlauncher.Adapter.MovieListAdapter;
import com.selecttvlauncher.BeanClass.HorizontalListBean;
import com.selecttvlauncher.BeanClass.HorizontalListitemBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;
import com.selecttvlauncher.ui.views.GridViewItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/6/2016.
 */
public class SearchCouroselsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String grid_type;
    private String data;


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
    private static onViewSelectedListener llistener;
    private JSONArray m_jsonNetworkList;

    int categoryLimit = 10;

    public SearchCouroselsFragment() {
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
    public static SearchCouroselsFragment newInstance(String param1, String param2, onViewSelectedListener listener) {
        SearchCouroselsFragment fragment = new SearchCouroselsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        llistener = listener;
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

        dynamic_horizontalViews_layout = (LinearLayout) rootview.findViewById(R.id.dynamic_horizontalViews_layout);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        try {
            if (!TextUtils.isEmpty(data)) {
                JSONObject data_array = new JSONObject(data);
                horizontalListdata = new ArrayList<>();

                if (data_array.has("show")) {
                    JSONObject show_object = data_array.getJSONObject("show");
                    int count = show_object.getInt("count");
                    if (count > 0) {
                        addhorizontalScrolllayouts(dynamic_horizontalViews_layout, "TV Shows", Constants.SEARCH_SHOW, show_object.getJSONArray("items"), count);
                    }
                }

                if (data_array.has("network")) {
                    JSONObject show_object = data_array.getJSONObject("network");
                    int count = show_object.getInt("count");
                    if (count > 0) {
                        addhorizontalScrolllayouts(dynamic_horizontalViews_layout, "Networks", Constants.SEARCH_NETWORK, show_object.getJSONArray("items"), count);
                    }
                }

                if (data_array.has("movie")) {
                    JSONObject show_object = data_array.getJSONObject("movie");
                    int count = show_object.getInt("count");
                    if (count > 0) {
                        addhorizontalScrolllayouts(dynamic_horizontalViews_layout, "Movies", Constants.SEARCH_MOVIE, show_object.getJSONArray("items"), count);
                    }
                }

                if (data_array.has("actor")) {
                    JSONObject show_object = data_array.getJSONObject("actor");
                    int count = show_object.getInt("count");
                    if (count > 0) {
                        addhorizontalScrolllayouts(dynamic_horizontalViews_layout, "Actors", Constants.SEARCH_ACTOR, show_object.getJSONArray("items"), count);
                    }
                }

                if (data_array.has("tvstation")) {
                    JSONObject show_object = data_array.getJSONObject("tvstation");
                    int count = show_object.getInt("count");
                    if (count > 0) {
                        addhorizontalScrolllayouts(dynamic_horizontalViews_layout, "TV Stations", Constants.SEARCH_TVSTATIONS, show_object.getJSONArray("items"), count);
                    }
                }

                if (data_array.has("live")) {
                    JSONObject show_object = data_array.getJSONObject("live");
                    int count = show_object.getInt("count");
                    if (count > 0) {
                        addhorizontalScrolllayouts(dynamic_horizontalViews_layout, "Live", Constants.SEARCH_LIVE, show_object.getJSONArray("items"), count);
                    }
                }


                if (data_array.has("station")) {
                    JSONObject show_object = data_array.getJSONObject("station");
                    int count = show_object.getInt("count");
                    if (count > 0) {
                        addhorizontalScrolllayouts(dynamic_horizontalViews_layout, "Channels", Constants.SEARCH_STATIONS, show_object.getJSONArray("items"), count);
                    }
                }


                if (data_array.has("radio")) {
                    JSONObject show_object = data_array.getJSONObject("radio");
                    int count = show_object.getInt("count");
                    if (count > 0) {
                        addhorizontalScrolllayouts(dynamic_horizontalViews_layout, "Radio", Constants.SEARCH_RADIO, show_object.getJSONArray("items"), count);
                    }
                }


                if (data_array.has("music")) {
                    JSONObject show_object = data_array.getJSONObject("music");
                    int count = show_object.getInt("count");
                    if (count > 0) {
                        addhorizontalScrolllayouts(dynamic_horizontalViews_layout, "Music", Constants.SEARCH_MUSIC, show_object.getJSONArray("items"), count);
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootview;
    }


    private void addhorizontalScrolllayouts(final LinearLayout dynamic_horizontalViews_layout, final String name, final String type, final JSONArray array, final int count) {
        final View itemlayout = (View) inflate.inflate(R.layout.horizontal_test, null);
        final TextView horizontal_listview_title = (TextView) itemlayout.findViewById(R.id.horizontal_listview_title);
        final TextView view_all_text = (TextView) itemlayout.findViewById(R.id.view_all_text);
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

        int spanCount = array.length(); // 3 columns
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

                    if (type.equalsIgnoreCase(Constants.SEARCH_NETWORK)) {

                        for (int l = 0; l < array.length(); l++) {
                            try {
                                JSONObject obj = array.getJSONObject(l);
                                final View itemmlayout = (View) inflate.inflate(R.layout.fragment_network_grid_item, null);

                                GridViewItem gridview_item = (GridViewItem) itemmlayout.findViewById(R.id.gridview_item);
                                DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                                horizontal_imageView.setVisibility(View.GONE);

                                gridview_item.setVisibility(View.VISIBLE);

                                LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(((width - 60) / 4), LinearLayout.LayoutParams.WRAP_CONTENT);
                                if (l != 0) {
                                    vp.setMargins(20, 0, 0, 0);
                                }
                                gridview_item.setLayoutParams(vp);
                                String image_url = obj.getString("image");
                                final int show_id = obj.getInt("id");
                                final String show_name = obj.getString("name");
                                Picasso.with(getActivity()
                                        .getApplicationContext())
                                        .load(image_url)
                                        .fit()
                                        .placeholder(R.drawable.loader_network).into(gridview_item);

                                image_view.addView(itemmlayout);
                                Utilities.setViewFocus(getActivity(), itemmlayout);
                                Utilities.setTextFocus(getActivity(), view_all_text);

                                itemmlayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        HomeActivity.mfreeorall = 0;
                                        onShowGridItemSelected(show_id, "n", show_name);
                                    }
                                });


                                view_all_text.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ((HomeActivity) getActivity()).refreshSerchListData(type, count);


                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    } else if (type.equalsIgnoreCase(Constants.SEARCH_MOVIE)) {


                        for (int l = 0; l < array.length(); l++) {

                            try {
                                JSONObject obj = array.getJSONObject(l);
                                final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_list_item, null);

                                DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                                LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(((width - 60) / 4), LinearLayout.LayoutParams.WRAP_CONTENT);
                                if (l != 0) {
                                    vp.setMargins(20, 0, 0, 0);
                                }
                                horizontal_imageView.setLayoutParams(vp);
                                String image_url = obj.getString("image");
                                final int show_id = obj.getInt("id");
                                final String show_name = obj.getString("name");
                                horizontal_imageView.loadMovieImage(image_url);

                                image_view.addView(itemmlayout);
                                itemmlayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        HomeActivity.mfreeorall = 0;
                                        onShowGridItemSelected(show_id, "m", show_name);

                                    }
                                });
                                Utilities.setViewFocus(getActivity(), itemmlayout);
                                Utilities.setTextFocus(getActivity(), view_all_text);

                                view_all_text.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ((HomeActivity) getActivity()).refreshSerchListData(type, count);


                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    } else {


                        for (int l = 0; l < array.length(); l++) {

                            try {
                                JSONObject obj = array.getJSONObject(l);
                                final View itemmlayout = (View) inflate.inflate(R.layout.horizontal_list_item, null);

                                DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                                LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(((width - 60) / 4), LinearLayout.LayoutParams.WRAP_CONTENT);
                                if (l != 0) {
                                    vp.setMargins(20, 0, 0, 0);
                                }
                                horizontal_imageView.setLayoutParams(vp);
                                String image_url = obj.getString("image");
                                final int show_id = obj.getInt("id");
                                final String show_name = obj.getString("name");
                                horizontal_imageView.loadImage(image_url);

                                image_view.addView(itemmlayout);
                                itemmlayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (type.equalsIgnoreCase(Constants.SEARCH_SHOW)) {
                                            HomeActivity.mfreeorall = 0;
                                            onShowGridItemSelected(show_id, "s", show_name);
                                        } else if (type.equalsIgnoreCase(Constants.SEARCH_STATIONS)) {
                                            HomeActivity.mfreeorall = 0;
                                            onShowGridItemSelected(show_id, "c", show_name);
                                        }

                                    }
                                });
                                Utilities.setViewFocus(getActivity(), itemmlayout);
                                Utilities.setTextFocus(getActivity(), view_all_text);

                                view_all_text.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ((HomeActivity) getActivity()).refreshSerchListData(type, count);


                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }


                    Log.d("layoutwidth::", "width" + width);

                }
            });
            Utilities.setViewFocus(getActivity(), right_slide);
            Utilities.setViewFocus(getActivity(), left_slide);

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

        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamic_horizontalViews_layout.addView(itemlayout);
    }

    // TODO: Rename method, update argument and hook method into UI event


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

    public interface onViewSelectedListener {
        public void onViewSelected(int id);
    }

    public void onShowGridItemSelected(int sid, String mtype, String name) {
        try {
            HomeActivity.mfreeorall = 0;
            Log.d("Selected show::::", "id:::" + sid);

            HomeActivity.toolbarSubContent = name;
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

            if (mtype.equalsIgnoreCase("n")) {
                HomeActivity.toolbarMainContent = "TV Networks";
                HomeActivity.toolbarSubContent = name;
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                HomeActivity.content_position = Constants.SUB_RIGHT_CONTENT;
                new LoadingTVNetworkList().execute(String.valueOf(sid));

            } else if (mtype.equalsIgnoreCase("m")) {

                HomeActivity.toolbarMainContent = "Movies";
                HomeActivity.toolbarSubContent = name;
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                ((HomeActivity) getActivity()).SwapMovieFragment(true);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, sid);
                fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                fragmentTransaction.commit();
            } else if (mtype.equalsIgnoreCase("s")) {

                HomeActivity.toolbarMainContent = "TV Shows";
                HomeActivity.toolbarSubContent = name;
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                ((HomeActivity) getActivity()).SwapMovieFragment(true);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, sid);
                fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                fragmentTransaction.commit();
            } else if (mtype.equalsIgnoreCase("c")) {
                try {
                    ((HomeActivity) getActivity()).SwapSearchFragment(false);
                    HomeActivity.getFragmentHomeScreenGrid().loadChannelData("" + sid);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class LoadingTVNetworkList extends AsyncTask<String, Object, Object> {
        @Override
        protected void onPreExecute() {

            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
                m_jsonNetworkList = JSONRPCAPI.getTVNetworkList(Integer.parseInt(params[0]), 1000, 0, 0);
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
                if (m_jsonNetworkList.length() > 0) {

                    try {

                        FragmentSearchList.mSearchContent = Constants.SEARCH_SUBCONTENT;
                        FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                        GridFragment network_listfragment = GridFragment.newInstance(Constants.SHOWS, m_jsonNetworkList.toString());
                        network_fragmentTransaction.replace(R.id.fragment_search_content, network_listfragment);
                        network_fragmentTransaction.commit();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


}
