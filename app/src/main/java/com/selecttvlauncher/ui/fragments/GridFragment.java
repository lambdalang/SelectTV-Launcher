package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.Adapter.GridAdapter;
import com.selecttvlauncher.Adapter.GridTempAdapter;
import com.selecttvlauncher.Adapter.MoviesGridAdapter;
import com.selecttvlauncher.Adapter.NetworkGridAdapter;
import com.selecttvlauncher.Adapter.SearchGridAdapter;
import com.selecttvlauncher.BeanClass.GridBean;
import com.selecttvlauncher.BeanClass.HorizontalListitemBean;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.GridViewItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class GridFragment extends Fragment implements NetworkGridAdapter.GridClickListener, GridAdapter.onGridSelectedListener, SearchGridAdapter.SearchGridClickListener, MoviesGridAdapter.onGridSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int LIST_COUNT = 20;
    private static final int LIST_COUNT_EXTRA = LIST_COUNT;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    // TODO: Rename and change types of parameters
    private String grid_type;
    private String data;
    GridAdapter adpater;
    MoviesGridAdapter gridAdapter;
    private OnGridFragmentInteractionListener mListener;
    private RecyclerView grid;
    private GridLayoutManager layoutManager;
    private ArrayList<Integer> images = new ArrayList<Integer>();
    private boolean network = false;
    private JSONArray m_jsonNetworkList;
    private String carousel_image;
    ProgressBar progressBar2;
    public static JSONObject m_JsonscchedulledRelated;
    private LinearLayout layout_network_details;
    private GridViewItem layout_network_image;
    private TextView layout_network_name;
    private TextView layout_network_descriptions;
    private TextView layout_network_slogan;
    private TextView layout_network_slogan_textview;
    private TextView layout_network_launchdate;
    private TextView layout_network_launchdate_textview;
    private TextView layout_network_headquarter;
    private TextView layout_network_headquarter_textview;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private LinearLayout launch_layout;
    private String mId;
    private int count = 12;
    private int countplus = 12;
    ArrayList<GridBean> moviegrid_list = new ArrayList<>();
    private String page = "1";
    private ProgressBar progressBar_bottom;
    private Tracker mTracker;

    public GridFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static GridFragment newInstance(String param1, String param2) {
        GridFragment fragment = new GridFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_grid, container, false);
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        progressBar2 = (ProgressBar) rootview.findViewById(R.id.progressBar2);
        progressBar_bottom = (ProgressBar) rootview.findViewById(R.id.progressBar_bottom);
        grid = (RecyclerView) rootview.findViewById(R.id.grid);
        layout_network_name = (TextView) rootview.findViewById(R.id.layout_network_name);
        layout_network_descriptions = (TextView) rootview.findViewById(R.id.layout_network_descriptions);
        layout_network_slogan_textview = (TextView) rootview.findViewById(R.id.layout_network_slogan_textview);
        layout_network_slogan = (TextView) rootview.findViewById(R.id.layout_network_slogan);
        layout_network_launchdate = (TextView) rootview.findViewById(R.id.layout_network_launchdate);
        layout_network_launchdate_textview = (TextView) rootview.findViewById(R.id.layout_network_launchdate_textview);
        layout_network_headquarter = (TextView) rootview.findViewById(R.id.layout_network_headquarter);
        layout_network_headquarter_textview = (TextView) rootview.findViewById(R.id.layout_network_headquarter_textview);
        layout_network_image = (GridViewItem) rootview.findViewById(R.id.layout_network_image);
        layout_network_details = (LinearLayout) rootview.findViewById(R.id.layout_network_details);
        launch_layout = (LinearLayout) rootview.findViewById(R.id.launch_layout);

        Log.d("grid_typegrid_type::", "grid_type" + grid_type);
        text_font_typeface();

        if (HomeActivity.is_networkSelected) {
            HashMap<String, String> map = HomeActivity.networkDetails;
            layout_network_details.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()
                    .getApplicationContext())
                    .load(map.get("image"))
                    .fit()
                    .placeholder(R.drawable.loader_network).into(layout_network_image);
            layout_network_name.setTypeface(OpenSans_Regular);
            layout_network_descriptions.setTypeface(OpenSans_Regular);
            layout_network_slogan.setTypeface(OpenSans_Regular);
            layout_network_slogan_textview.setTypeface(OpenSans_Regular);
            layout_network_launchdate.setTypeface(OpenSans_Regular);
            layout_network_launchdate_textview.setTypeface(OpenSans_Regular);
            layout_network_headquarter.setTypeface(OpenSans_Regular);
            layout_network_headquarter_textview.setTypeface(OpenSans_Regular);

            try {
                if (map != null) {
                    layout_network_descriptions.setText(map.get("description"));
                    layout_network_slogan.setText(map.get("slogan"));
                    layout_network_headquarter.setText(map.get("headquarters"));
                    if (!TextUtils.isEmpty(map.get("start_time"))) {
                        layout_network_launchdate.setText(map.get("start_time"));
                        launch_layout.setVisibility(View.VISIBLE);
                    } else {
                        launch_layout.setVisibility(View.VISIBLE);
                    }


                } else {
                    layout_network_details.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            layout_network_details.setVisibility(View.GONE);
        }

        try {

            if (grid_type.equalsIgnoreCase(Constants.GAMES)) {


                ArrayList<GridBean> grid_list = new ArrayList<>();
                grid_list.clear();
                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);


                    String id = "", name = "", image = "", description = "", type = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        JSONObject object = data_array.getJSONObject(i);
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

                        } else {
                            type = "s";
                        }

                        grid_list.add(new GridBean(id, name, image, description, type));

                    }


                }

                layoutManager = new GridLayoutManager(getActivity(), 5);
                layoutManager.setOrientation(GridLayoutManager.VERTICAL);
                grid.hasFixedSize();
                grid.setLayoutManager(layoutManager);
                int spanCount = 5; // 3 columns
                int spacing = 25; // 50px
                boolean includeEdge = true;
                grid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                if (TextUtils.isEmpty(data)) {
                    GridTempAdapter adpater = new GridTempAdapter(images, getContext());
                    grid.setAdapter(adpater);
                } else {
                    GridAdapter adpater = new GridAdapter(grid_list, getContext(), GridFragment.this);
                    grid.setAdapter(adpater);
                }

            } else if (grid_type.equalsIgnoreCase(Constants.CATEGORY_SHOW)) {

                ArrayList<GridBean> grid_list = new ArrayList<>();
                grid_list.clear();
                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);


                    String id = "", name = "", image = "", description = "", type = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        JSONObject object = data_array.getJSONObject(i);
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

                        } else {
                            type = "s";
                        }

                        grid_list.add(new GridBean(id, name, image, description, type));

                    }


                }

                layoutManager = new GridLayoutManager(getActivity(), 4);
                layoutManager.setOrientation(GridLayoutManager.VERTICAL);
                grid.hasFixedSize();
                grid.setLayoutManager(layoutManager);
                int spanCount = 4; // 3 columns
                int spacing = 25; // 50px
                boolean includeEdge = true;
                grid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                if (TextUtils.isEmpty(data)) {
                    GridTempAdapter adpater = new GridTempAdapter(images, getContext());
                    grid.setAdapter(adpater);
                } else {
                    GridAdapter adpater = new GridAdapter(grid_list, getContext(), GridFragment.this);
                    grid.setAdapter(adpater);
                }
            } else if (grid_type.equalsIgnoreCase(Constants.SEARCH_ACTOR) || grid_type.equalsIgnoreCase(Constants.SEARCH_TVSTATIONS) || grid_type.equalsIgnoreCase(Constants.SEARCH_LIVE) || grid_type.equalsIgnoreCase(Constants.SEARCH_STATIONS) || grid_type.equalsIgnoreCase(Constants.SEARCH_RADIO) || grid_type.equalsIgnoreCase(Constants.SEARCH_MUSIC)) {

                ArrayList<GridBean> grid_list = new ArrayList<>();
                grid_list.clear();
                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);


                    String id = "", name = "", image = "", description = "", type = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        JSONObject object = data_array.getJSONObject(i);
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

                        grid_list.add(new GridBean(id, name, image, description, type));

                    }
                }

                layoutManager = new GridLayoutManager(getActivity(), 4);
                layoutManager.setOrientation(GridLayoutManager.VERTICAL);
                grid.hasFixedSize();
                grid.setLayoutManager(layoutManager);
                int spanCount = 4; // 3 columns
                int spacing = 25; // 50px
                boolean includeEdge = true;
                grid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

                SearchGridAdapter adpater = new SearchGridAdapter(grid_list, getContext(), GridFragment.this);
                grid.setAdapter(adpater);
            } else if (!grid_type.equalsIgnoreCase(Constants.NETWORK)) {

                /*images.add(R.drawable.img1);
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
                moviegrid_list = new ArrayList<>();
                moviegrid_list.clear();
                if (!TextUtils.isEmpty(data)) {
                    JSONArray data_array = new JSONArray(data);


                    String id = "", name = "", image = "", description = "", type = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        JSONObject object = data_array.getJSONObject(i);
                        if (object.has("id")) {
                            id = object.getString("id");
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
                        } else {
                            if (grid_type.equals(Constants.MOVIES)) {
                                type = "m";
                            } else {
                                type = "s";
                            }

                        }
                        moviegrid_list.add(new GridBean(id, name, image, description, type));

                    }


                }
                layoutManager = new GridLayoutManager(getActivity(), 4);
                layoutManager.setOrientation(GridLayoutManager.VERTICAL);
                grid.hasFixedSize();
                grid.setLayoutManager(layoutManager);
                int spanCount = 4; // 3 columns
                int spacing = 25; // 50px
                boolean includeEdge = true;
                grid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));
                if (TextUtils.isEmpty(data)) {
                    GridTempAdapter adpater = new GridTempAdapter(images, getContext());
                    grid.setAdapter(adpater);
                } else {

                    if (!grid_type.equals(Constants.MOVIES)) {
                        adpater = new GridAdapter(moviegrid_list, getContext(), GridFragment.this);
                        grid.setAdapter(adpater);
                    }else
                    {
                        gridAdapter = new MoviesGridAdapter(moviegrid_list, getContext(), GridFragment.this);
                        grid.setAdapter(gridAdapter);
                    }

                }

                if (grid_type.equals(Constants.MOVIES)) {

                    grid.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            try {


                                if (dy > 0)
                                {
                                    visibleItemCount = layoutManager.getChildCount();
                                    totalItemCount = layoutManager.getItemCount();
                                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                                    if (loading) {
                                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                                            loading = false;
                                            page = String.valueOf(count);
                                            count = countplus + count;
                                            Log.d("count", page + count);
                                            progressBar_bottom.setVisibility(View.VISIBLE);

                                            if (FragmentOndemandallList.mAdapter_genreordecade.equals("LoadingMovieListByrating")) {
                                                new LoadingMovieListByRating().execute(HomeActivity.mMovieratingid, page);
                                            }else
                                            {

                                                new LoadingMovieListByGenre().execute("" + HomeActivity.mMovieGenreId, page);
                                            }
//



                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                        }
                    });
                }



            } else {

                try {

                    JSONArray data_array = new JSONArray(data);

                    final ArrayList<HorizontalListitemBean> grid_list = new ArrayList<>();
                    grid_list.clear();
                    String id = "", name = "", image = "", description = "";
                    for (int i = 0; i < data_array.length(); i++) {
                        JSONObject object = data_array.getJSONObject(i);
                        if (object.has("id")) {
                            id = object.getString("id");
                        }
                        if (object.has("name")) {
                            name = object.getString("name");
                        }
                        if (object.has("thumbnail")) {
                            image = object.getString("thumbnail");
                        }
                        if (object.has("carousel_image")) {
                            carousel_image = object.getString("carousel_image");
                        } else {
                            if (object.has("image")) {
                                carousel_image = object.getString("image");
                            }
                        }
                        if (object.has("description")) {
                            description = object.getString("description");
                        }
                        grid_list.add(new HorizontalListitemBean(Integer.parseInt(id), carousel_image, name, description));

                    }

                    layoutManager = new GridLayoutManager(getActivity(), 4);
                    layoutManager.setOrientation(GridLayoutManager.VERTICAL);
                    grid.hasFixedSize();
                    grid.setLayoutManager(layoutManager);
                    int spanCount = 4; // 3 columns
                    int spacing = 50; // 50px
                    boolean includeEdge = true;
                    int width = grid.getMeasuredWidth();
                    grid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


                    NetworkGridAdapter adpater = new NetworkGridAdapter(grid_list, getContext(), this);
                    grid.setAdapter(adpater);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGridFragmentInteractionListener) {
            mListener = (OnGridFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGriditemSelected(String network, String id, String type, String name) {
        if (network.equalsIgnoreCase(Constants.NETWORK)) {
            Log.d("Network::::", "id:::" + id);
            new LoadingTVNetworkList().execute(id);
            HomeActivity.mNetworkid = Integer.parseInt(id);
            HomeActivity.toolbarSubContent = name;
            HomeActivity.content_position = Constants.SUB_RIGHT_CONTENT;
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");


        } else if (type.equalsIgnoreCase("s")) {
            HomeActivity.toolbarMainContent = "TV Shows";
            HomeActivity.toolbarSubContent = name;
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

            ((HomeActivity) getActivity()).SwapMovieFragment(true);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, Integer.parseInt(id));
            fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
            fragmentTransaction.commit();
        } else if (type.equalsIgnoreCase("m")) {
            if (getActivity() != null) {


                HomeActivity.toolbarMainContent = "Movies";
                HomeActivity.toolbarSubContent = name;
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                ((HomeActivity) getActivity()).SwapMovieFragment(true);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, Integer.parseInt(id));
                fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                fragmentTransaction.commit();


            }
        } else if (type.equalsIgnoreCase("g")) {
            mId = id;
            new LoadingGameDeatilsById().execute();
        }

    }


    @Override
    public void onShowGridItemSelected(int id, String type, String name) {
        Log.d("Selected show::::", "id:::" + id);

        HomeActivity.toolbarSubContent = name;
        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
        setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

        if (type.equalsIgnoreCase("n") || type.equalsIgnoreCase("l")) {
            HomeActivity.toolbarMainContent = "TV Networks";
            HomeActivity.toolbarSubContent = name;
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

            HomeActivity.content_position = Constants.SUB_RIGHT_CONTENT;
            new LoadingTVNetworkList().execute(String.valueOf(id));
            HomeActivity.mNetworkid = id;

        } else if (type.equalsIgnoreCase("m") || type.equalsIgnoreCase("movie")) {
            if (getActivity() != null) {


                HomeActivity.toolbarMainContent = "Movies";
                HomeActivity.toolbarSubContent = name;
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                ((HomeActivity) getActivity()).SwapMovieFragment(true);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, id);
                fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                fragmentTransaction.commit();


            }
        } else if (type.equalsIgnoreCase("g")) {
            mId = String.valueOf(id);
            new LoadingGameDeatilsById().execute();
        } else {
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

    @Override
    public void onsearchGriditemSelected(String search, String id, String type, String name) {
        try {
            if(mListener!=null){
                ((HomeActivity) getActivity()).SwapSearchFragment(false);
                mListener.onloadChannel(id);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class LoadingSchedulerRelatedData extends AsyncTask<String, Object, Object> {
        @Override
        protected void onPreExecute() {
            progressBar2.setVisibility(View.VISIBLE);

            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
                m_JsonscchedulledRelated = JSONRPCAPI.getschedullerRelated(Integer.parseInt(params[0]));
                if (m_JsonscchedulledRelated == null) return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            progressBar2.setVisibility(View.GONE);
            // mProgressHUD.dismiss();


            Log.d("getschedullerRelated", "values" + m_JsonscchedulledRelated);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            ChannelFragment channelFragment = ChannelFragment.newInstance(Constants.SEARCH_STATIONS, m_JsonscchedulledRelated.toString());
            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, channelFragment);
            fragmentTransaction.commit();
        }

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

            HomeActivity.m_jsonGameDeatils = JSONRPCAPI.getGAMEDetail(Integer.parseInt(mId));
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


    public interface OnGridFragmentInteractionListener {
        // TODO: Update argument type and name
        void onloadChannel(String cid);
    }

    private class LoadingTVNetworkList extends AsyncTask<String, Object, Object> {
        @Override
        protected void onPreExecute() {
            progressBar2.setVisibility(View.VISIBLE);

            grid.setVisibility(View.GONE);            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
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
                progressBar2.setVisibility(View.GONE);
                if (m_jsonNetworkList.length() > 0) {

                    try {
                        if (HomeActivity.isSearchClick) {
                            FragmentSearchList.mSearchContent = Constants.SEARCH_SUBCONTENT;
                            FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                            GridFragment network_listfragment = GridFragment.newInstance(Constants.SHOWS, m_jsonNetworkList.toString());
                            network_fragmentTransaction.replace(R.id.fragment_search_content, network_listfragment);
                            network_fragmentTransaction.commit();

                        } else {
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

                }
                grid.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }

    private void text_font_typeface() {
        OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
        osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
    }


    private class LoadingMovieListByGenre extends AsyncTask<String, Object, Object> {
        @Override
        protected void onPreExecute() {

      /*      FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
            DialogFragment dialog_fragment = new DialogFragment();
            network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
            network_fragmentTransaction.commit();*/

            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            HomeActivity.m_jsonmovieslistbygenre = JSONRPCAPI.getMovieListbyGenre(Integer.parseInt(params[0]), Integer.parseInt(params[1]), HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonmovieslistbygenre == null) return null;

            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonmovieslistbygenre);


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {

                if (HomeActivity.m_jsonmovieslistbygenre.length() > 0) {


                    try {
                        if (!TextUtils.isEmpty(HomeActivity.m_jsonmovieslistbygenre.toString())) {
                            JSONArray data_array = new JSONArray(HomeActivity.m_jsonmovieslistbygenre.toString());

                            String id = "", name = "", image = "", description = "", type = "";
                            for (int i = 0; i < data_array.length(); i++) {
                                JSONObject object = data_array.getJSONObject(i);
                                if (object.has("id")) {
                                    id = object.getString("id");
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
                                } else {
                                    if (grid_type.equals(Constants.MOVIES)) {
                                        type = "m";
                                    } else {
                                        type = "s";
                                    }

                                }
                                moviegrid_list.add(new GridBean(id, name, image, description, type));

                            }

                            if (gridAdapter == null) {
                                gridAdapter = new MoviesGridAdapter(moviegrid_list, getContext(), GridFragment.this);
                                grid.setAdapter(gridAdapter);
                            } else {
                                gridAdapter.setData(moviegrid_list);
                                gridAdapter.notifyDataSetChanged();
                            }


                            loading = true;
                        /*FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        GridFragment grid_fragment = GridFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonmovieslistbygenre.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_ondemand_pagerandlist, grid_fragment);
                        fragmentTransaction_grid.commit();*/


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                progressBar_bottom.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


    private class LoadingMovieListByRating extends AsyncTask<String, Object, Object> {
        @Override
        protected void onPreExecute() {


            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            HomeActivity.m_jsonmoviesbyrating = JSONRPCAPI.getMovieListByrating(params[0], Integer.parseInt(params[1]), HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonmoviesbyrating == null) return null;
            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonmoviesbyrating);


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if (HomeActivity.m_jsonmoviesbyrating.length() > 0) {

                    try {
                        if (!TextUtils.isEmpty(HomeActivity.m_jsonmoviesbyrating.toString())) {
                            JSONArray data_array = new JSONArray(HomeActivity.m_jsonmoviesbyrating.toString());

                            String id = "", name = "", image = "", description = "", type = "";
                            for (int i = 0; i < data_array.length(); i++) {
                                JSONObject object = data_array.getJSONObject(i);
                                if (object.has("id")) {
                                    id = object.getString("id");
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
                                } else {
                                    if (grid_type.equals(Constants.MOVIES)) {
                                        type = "m";
                                    } else {
                                        type = "s";
                                    }

                                }
                                moviegrid_list.add(new GridBean(id, name, image, description, type));

                            }


                            if (gridAdapter == null) {
                                gridAdapter = new MoviesGridAdapter(moviegrid_list, getContext(), GridFragment.this);
                                grid.setAdapter(gridAdapter);
                            } else {
                                gridAdapter.setData(moviegrid_list);
                                gridAdapter.notifyDataSetChanged();
                            }


                            loading = true;
                        /*FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        GridFragment grid_fragment = GridFragment.newInstance(Constants.MOVIES, HomeActivity.m_jsonmovieslistbygenre.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_ondemand_pagerandlist, grid_fragment);
                        fragmentTransaction_grid.commit();*/


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                progressBar_bottom.setVisibility(View.GONE);


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
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent +"-" + mainString + "-" + SubString1 + "-" + SubString2);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
