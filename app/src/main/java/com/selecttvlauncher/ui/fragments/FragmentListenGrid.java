package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.selecttvlauncher.BeanClass.CauroselsItemBean;
import com.selecttvlauncher.BeanClass.GridBean;
import com.selecttvlauncher.BeanClass.ListenGridBean;
import com.selecttvlauncher.BeanClass.RadioDetailBean;
import com.selecttvlauncher.BeanClass.RadioLoactionRegion;
import com.selecttvlauncher.BeanClass.RadioLocationBean;
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

public class FragmentListenGrid extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static RecyclerView fragmentlisten_grid;
    GridLayoutManager gridLayoutManager;
    public static ArrayList<ListenGridBean> listenGridBeans = new ArrayList<>();
    public static ArrayList<RadioDetailBean> radioDetailBeans = new ArrayList<>();
    public static ArrayList<RadioDetailBean> radioLocationDetailBeans = new ArrayList<>();
    public static ArrayList<RadioLocationBean> radioLocationBeans = new ArrayList<>();
    public static ArrayList<RadioLocationBean> radioLocationRegionBeans = new ArrayList<>();
    public static ArrayList<RadioLoactionRegion> radioLoactionRegions = new ArrayList<>();
    public static ProgressBar fragmentlisten_progressBar2;
    public static JSONArray m_jsonRadioeList;
    public static int genreId;
    public static int languageId;
    public static int locationId;
    public static int locationcountryId;
    public static int locationregionId;
    public static int locationcityId;
    public static int mGenreorlanguageposition = 0;
    public static int mlocationposition = 0;
    public static int mlocationreginposition = 0;
    public static int mlocationcityposition = 0;
    public static int mListenNetworkId;
    public static ArrayList<GridBean> grid_list=new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListenGrid.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListenGrid newInstance(String param1, String param2) {
        FragmentListenGrid fragment = new FragmentListenGrid();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentListenGrid() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_listen_grid, container, false);
        fragmentlisten_progressBar2 = (ProgressBar) view.findViewById(R.id.fragmentlisten_progressBar2);
        fragmentlisten_grid = (RecyclerView) view.findViewById(R.id.fragmentlisten_grid);
        gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        fragmentlisten_grid.setLayoutManager(gridLayoutManager);
        fragmentlisten_grid.setHasFixedSize(true);
        int spanCount = 5; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;

        fragmentlisten_grid.setFocusable(true);

        fragmentlisten_grid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        ((HomeActivity) getActivity()).setFragmentListenGrid(this);
//        makeGridItems();
        if (FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_LISTEN)|| FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_PODCAST)) {
            mListenAllAdapterFunction();

        } else if (FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_RADIO)) {
            mLocationAdapterBackFunction();
        }


        return view;
    }

    private void mListenAllAdapterFunction() {

        ListenAllGridAdapter listenGridAdapter = new ListenAllGridAdapter(FragmentListenContent.item_list, getActivity());
        fragmentlisten_grid.setAdapter(listenGridAdapter);
    }


    private class ListenAllGridAdapter extends RecyclerView.Adapter<ListenAllGridAdapter.DataObjectHolder> {
        ArrayList<CauroselsItemBean> listenGridBeans;
        Context context;

        public ListenAllGridAdapter(ArrayList<CauroselsItemBean> listenGridBeans, Context context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;
        }

        @Override
        public ListenAllGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ListenAllGridAdapter.DataObjectHolder holder, final int position) {
            holder.imageView.loadImage(listenGridBeans.get(position).getCarousel_image());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("listen_type", "type" + listenGridBeans.get(position).getType());
                    HomeActivity.toolbarSubContent = Utilities.stripHtml(listenGridBeans.get(position).getName());
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                    switch (listenGridBeans.get(position).getType())
                    {
                        case "M":
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, listenGridBeans.get(position).getId());
                            fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                            fragmentTransaction.commit();
                            ((HomeActivity) getActivity()).SwapMovieFragment(true);
                            break;
                        case "N":

                            FragmentListenMain.mListenNetworkId=listenGridBeans.get(position).getId();
                            new LoadingTVNetworkList().execute();
                            break;
                        case "S":
                            ((HomeActivity) getActivity()).SwapMovieFragment(true);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, listenGridBeans.get(position).getId());
                            fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                            fragmentTransaction.commit();
                            break;

                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView imageView;

            public DataObjectHolder(View itemView) {
                super(itemView);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);

                Utilities.setViewFocus(getActivity(),imageView);
            }
        }
    }


    private void makeGridItems() {
        String strUrl = "";
        String strName = "";
        int strid = 0;
        JSONObject jsonObject;
        try {
            if (!TextUtils.isEmpty(mParam2)) {

                JSONArray data_array = new JSONArray(mParam2);
                for (int i = 0; i < data_array.length(); i++) {
                    jsonObject = data_array.getJSONObject(i);
                    if (jsonObject.has("id")) {
                        strid = jsonObject.getInt("id");
                    }
                    if (jsonObject.has("name")) {
                        strName = jsonObject.getString("name");

                    }
                    if (jsonObject.has("image")) {
                        strUrl = jsonObject.getString("image");
                    }

                    listenGridBeans.add(new ListenGridBean(strid, strName, strUrl));
                }

            }

            ListenGridAdapter listenGridAdapter = new ListenGridAdapter(listenGridBeans, getActivity());
            fragmentlisten_grid.setAdapter(listenGridAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public static void hideProgress(boolean b) {
        if (b) {
            fragmentlisten_progressBar2.setVisibility(View.VISIBLE);
        } else {
            fragmentlisten_progressBar2.setVisibility(View.GONE);
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
    public class ListenGridAdapter extends RecyclerView.Adapter<ListenGridAdapter.DataObjectHolder> {
        Context context;
        ArrayList<ListenGridBean> listenGridBeans;


        public ListenGridAdapter(ArrayList<ListenGridBean> listenGridBeans, Context context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;

        }

        @Override
        public ListenGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ListenGridAdapter.DataObjectHolder holder, final int position) {
            holder.imageView.loadImage(listenGridBeans.get(position).getImage());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGenreorlanguageposition = position;
                    HomeActivity.toolbarSubContent = listenGridBeans.get(position).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    genreId = listenGridBeans.get(position).getId();
                    new LoadingRadioSubCategory().execute();


                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView imageView;

            public DataObjectHolder(View itemView) {
                super(itemView);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);

                Utilities.setViewFocus(getActivity(),imageView);
            }
        }
    }


    public class ListenTextGridAdapter extends RecyclerView.Adapter<ListenTextGridAdapter.DataObjectHolder> {
        Context context;
        ArrayList<ListenGridBean> listenGridBeans;


        public ListenTextGridAdapter(ArrayList<ListenGridBean> listenGridBeans, Context context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;

        }

        @Override
        public ListenTextGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_listen_text_grid, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ListenTextGridAdapter.DataObjectHolder holder, final int position) {
            holder.gridview_text_item.setText(listenGridBeans.get(position).getName());
            holder.gridview_text_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlocationposition = position;
                    FragmentListenMain.mRadioMainContent = Constants.RADIO_LOCATION;
                    FragmentListenMain.mRadioDetailsContent = Constants.RADIO_LOCATIONSUBCONTENT;
                    HomeActivity.toolbarSubContent = listenGridBeans.get(position).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                    locationId = listenGridBeans.get(position).getId();
                    new LoadingRadioLocationCategory().execute();

                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            TextView gridview_text_item;

            public DataObjectHolder(View itemView) {
                super(itemView);
                gridview_text_item = (TextView) itemView.findViewById(R.id.gridview_text_item);

                Utilities.setViewFocus(getActivity(),gridview_text_item);
            }
        }
    }


    private class LoadingRadioSubCategory extends AsyncTask<Object, Object, Object> {

        int radiogenre_id = 0;
        String radiogenre_image = "";
        String radiogenre_name = "";
        String radiogenre_website = "";
        String radiogenre_slogan = "";
        String radiogenre_description = "";
        String radiogenre_stream = "";
        String radiogenre_twitter = "";
        String radiogenre_frequency = "";
        String radiogenre_phone = "";
        String radiogenre_email = "";
        String radiogenre_facebook = "";
        String radiogenre_address = "";
        String radiogenre_wikipedia = "";
        String radiogenre_bitrate = "";
        String radiogenre_slug = "";
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            m_jsonRadioeList=new JSONArray();
            radioDetailBeans.clear();
            fragmentlisten_grid.setVisibility(View.INVISIBLE);
            fragmentlisten_progressBar2.setVisibility(View.VISIBLE);

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {

                if (FragmentListenMain.mRadioSubContent.equals(Constants.RADIO_GENRE)) {

                    FragmentListenMain.mRadioDetailsContent = Constants.RADIO_GENRESUBCONENT;
                    m_jsonRadioeList = JSONRPCAPI.getRadioGenreList(genreId);
                    if (m_jsonRadioeList == null) return null;
                    radioDetailBeans.clear();
                    for (int i = 0; i < m_jsonRadioeList.length(); i++) {
                        jsonObject = m_jsonRadioeList.getJSONObject(i);
                        if (jsonObject.has("website")) {
                            radiogenre_website = jsonObject.getString("website");
                        }
                        if (jsonObject.has("slogan")) {
                            radiogenre_slogan = jsonObject.getString("slogan");
                        }
                        if (jsonObject.has("description")) {
                            radiogenre_description = jsonObject.getString("description");
                        }
                        if (jsonObject.has("stream")) {
                            radiogenre_stream = jsonObject.getString("stream");
                        }
                        if (jsonObject.has("twitter")) {
                            radiogenre_twitter = jsonObject.getString("twitter");
                        }
                        if (jsonObject.has("frequency")) {
                            radiogenre_frequency = jsonObject.getString("frequency");
                        }
                        if (jsonObject.has("wikipedia")) {
                            radiogenre_wikipedia = jsonObject.getString("wikipedia");
                        }
                        if (jsonObject.has("id")) {
                            radiogenre_id = jsonObject.getInt("id");
                        }
                        if (jsonObject.has("email")) {
                            radiogenre_email = jsonObject.getString("email");
                        }
                        if (jsonObject.has("phone")) {
                            radiogenre_phone = jsonObject.getString("phone");
                        }
                        if (jsonObject.has("facebook")) {
                            radiogenre_facebook = jsonObject.getString("facebook");
                        }
                        if (jsonObject.has("image")) {
                            radiogenre_image = jsonObject.getString("image");
                        }
                        if (jsonObject.has("bitrate")) {
                            radiogenre_bitrate = jsonObject.getString("bitrate");
                        }
                        if (jsonObject.has("address")) {
                            radiogenre_address = jsonObject.getString("address");
                        }
                        if (jsonObject.has("slug")) {
                            radiogenre_slug = jsonObject.getString("slug");
                        }
                        if (jsonObject.has("name")) {
                            radiogenre_name = jsonObject.getString("name");
                        }

                        radioDetailBeans.add(new RadioDetailBean(radiogenre_website, radiogenre_slogan, radiogenre_description, radiogenre_stream, radiogenre_twitter, radiogenre_frequency, radiogenre_wikipedia, radiogenre_phone, radiogenre_email, radiogenre_facebook, radiogenre_image, radiogenre_address, radiogenre_bitrate, radiogenre_slug, radiogenre_name, radiogenre_id));

                    }
                } else if (FragmentListenMain.mRadioSubContent.equals(Constants.RADIO_LANGUAGE)) {

                    FragmentListenMain.mRadioDetailsContent = Constants.RADIO_LANGUAGESUBCONTENT;
                    m_jsonRadioeList = JSONRPCAPI.getRadioLanguageList(genreId);
                    if (m_jsonRadioeList == null) return null;
                    radioDetailBeans.clear();
                    for (int i = 0; i < m_jsonRadioeList.length(); i++) {
                        jsonObject = m_jsonRadioeList.getJSONObject(i);
                        if (jsonObject.has("website")) {
                            radiogenre_website = jsonObject.getString("website");
                        }
                        if (jsonObject.has("slogan")) {
                            radiogenre_slogan = jsonObject.getString("slogan");
                        }
                        if (jsonObject.has("description")) {
                            radiogenre_description = jsonObject.getString("description");
                        }
                        if (jsonObject.has("stream")) {
                            radiogenre_stream = jsonObject.getString("stream");
                        }
                        if (jsonObject.has("twitter")) {
                            radiogenre_twitter = jsonObject.getString("twitter");
                        }
                        if (jsonObject.has("frequency")) {
                            radiogenre_frequency = jsonObject.getString("frequency");
                        }
                        if (jsonObject.has("wikipedia")) {
                            radiogenre_wikipedia = jsonObject.getString("wikipedia");
                        }
                        if (jsonObject.has("id")) {
                            radiogenre_id = jsonObject.getInt("id");
                        }
                        if (jsonObject.has("email")) {
                            radiogenre_email = jsonObject.getString("email");
                        }
                        if (jsonObject.has("phone")) {
                            radiogenre_phone = jsonObject.getString("phone");
                        }
                        if (jsonObject.has("facebook")) {
                            radiogenre_facebook = jsonObject.getString("facebook");
                        }
                        if (jsonObject.has("image")) {
                            radiogenre_image = jsonObject.getString("image");
                        }
                        if (jsonObject.has("bitrate")) {
                            radiogenre_bitrate = jsonObject.getString("bitrate");
                        }
                        if (jsonObject.has("address")) {
                            radiogenre_address = jsonObject.getString("address");
                        }
                        if (jsonObject.has("slug")) {
                            radiogenre_slug = jsonObject.getString("slug");
                        }
                        if (jsonObject.has("name")) {
                            radiogenre_name = jsonObject.getString("name");
                        }

                        radioDetailBeans.add(new RadioDetailBean(radiogenre_website, radiogenre_slogan, radiogenre_description, radiogenre_stream, radiogenre_twitter, radiogenre_frequency, radiogenre_wikipedia, radiogenre_phone, radiogenre_email, radiogenre_facebook, radiogenre_image, radiogenre_address, radiogenre_bitrate, radiogenre_slug, radiogenre_name, radiogenre_id));

                    }
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {

            try {

                fragmentlisten_progressBar2.setVisibility(View.GONE);
                fragmentlisten_grid.setVisibility(View.VISIBLE);
                ListenSubGridAdapter listenSubGridAdapter = new ListenSubGridAdapter(radioDetailBeans, getActivity());
                fragmentlisten_grid.setAdapter(listenSubGridAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public class ListenSubGridAdapter extends RecyclerView.Adapter<ListenSubGridAdapter.DataObjectHolder> {
        Context context;
        ArrayList<RadioDetailBean> listenGridBeans;


        public ListenSubGridAdapter(ArrayList<RadioDetailBean> listenGridBeans, Context context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;
        }

        @Override
        public ListenSubGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           /* LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);*/
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ListenSubGridAdapter.DataObjectHolder holder, final int position) {
            holder.imageView.loadMovieImage(listenGridBeans.get(position).getImage());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        HomeActivity.toolbarSubContent = listenGridBeans.get(position).getName();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        ((HomeActivity) getActivity()).swapRadioFragment(true);
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        FragmentRadioDetails listfragment = FragmentRadioDetails.newInstance(String.valueOf(position), "");
                        fragmentTransaction_list.replace(R.id.activity_radio_fragemnt_layout, listfragment);
                        fragmentTransaction_list.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView imageView;

            public DataObjectHolder(View itemView) {
                super(itemView);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);

                Utilities.setViewFocus(getActivity(),imageView);
            }
        }
    }

    private class LoadingRadioLocationCategory extends AsyncTask<Object, Object, Object> {


        JSONObject jsonObject;
        private String has_regions = "";
        private String slug = "";
        private String has_cities = "";
        private String id = "";
        private String name = "";

        @Override
        protected void onPreExecute() {
            fragmentlisten_grid.setVisibility(View.INVISIBLE);
            fragmentlisten_progressBar2.setVisibility(View.VISIBLE);
            radioLocationBeans.clear();

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {

                m_jsonRadioeList = JSONRPCAPI.getRadioCountry(locationId);
                if (m_jsonRadioeList == null) return null;
                radioLocationBeans.clear();

                for (int i = 0; i < m_jsonRadioeList.length(); i++) {
                    jsonObject = m_jsonRadioeList.getJSONObject(i);

                    if (jsonObject.has("has_regions")) {
                        has_regions = jsonObject.getString("has_regions");
                    }
                    if (jsonObject.has("slug")) {
                        slug = jsonObject.getString("slug");
                    }
                    if (jsonObject.has("has_cities")) {
                        has_cities = jsonObject.getString("has_cities");
                    }
                    if (jsonObject.has("id")) {
                        id = jsonObject.getString("id");
                    }

                    if (jsonObject.has("name")) {
                        name = jsonObject.getString("name");
                    }
                    radioLocationBeans.add(new RadioLocationBean(has_regions, slug, has_cities, id, name));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {

            try {

                fragmentlisten_progressBar2.setVisibility(View.GONE);
                fragmentlisten_grid.setVisibility(View.VISIBLE);
                ListenTextSubGridAdapter listenSubGridAdapter = new ListenTextSubGridAdapter(radioLocationBeans, getActivity());
                fragmentlisten_grid.setAdapter(listenSubGridAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public class ListenTextSubGridAdapter extends RecyclerView.Adapter<ListenTextSubGridAdapter.DataObjectHolder> {
        Context context;
        ArrayList<RadioLocationBean> listenGridBeans;


        public ListenTextSubGridAdapter(ArrayList<RadioLocationBean> listenGridBeans, Context context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;

        }

        @Override
        public ListenTextSubGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            try {
                LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.fragment_listen_text_grid, parent, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ListenTextSubGridAdapter.DataObjectHolder holder, final int position) {
            holder.gridview_text_item.setText(listenGridBeans.get(position).getName());
            holder.gridview_text_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlocationreginposition = position;
                    FragmentListenMain.mRadioMainContent = Constants.RADIO_LOCATION;
                    FragmentListenMain.mRadioDetailsContent = Constants.RADIO_LOCATIONREGINCONTENT;
                    HomeActivity.toolbarSubContent = listenGridBeans.get(position).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    if (listenGridBeans.get(position).getHas_regions().equalsIgnoreCase("true")) {

                        locationcountryId = Integer.parseInt(listenGridBeans.get(position).getId());
                        new LoadingRadioLocationSubCategory().execute();
                    } else {
                        locationcountryId = 0;
                        locationregionId = 0;
                        locationcityId = 0;
                        new LoadingRadioLocationListSubCategory().execute();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            TextView gridview_text_item;

            public DataObjectHolder(View itemView) {
                super(itemView);
                gridview_text_item = (TextView) itemView.findViewById(R.id.gridview_text_item);

                Utilities.setViewFocus(getActivity(),gridview_text_item);
            }
        }
    }


    private class LoadingRadioLocationSubCategory extends AsyncTask<Object, Object, Object> {


        JSONObject jsonObject;
        private String has_regions = "";
        private String slug = "";
        private String has_cities = "";
        private String id = "";
        private String name = "";

        @Override
        protected void onPreExecute() {
            fragmentlisten_grid.setVisibility(View.INVISIBLE);
            fragmentlisten_progressBar2.setVisibility(View.VISIBLE);
            radioLocationRegionBeans.clear();

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                m_jsonRadioeList = JSONRPCAPI.getRadioRegion(locationcountryId);
                if (m_jsonRadioeList == null) return null;

                for (int i = 0; i < m_jsonRadioeList.length(); i++) {
                    jsonObject = m_jsonRadioeList.getJSONObject(i);

                    if (jsonObject.has("has_regions")) {
                        has_regions = jsonObject.getString("has_regions");
                    }
                    if (jsonObject.has("slug")) {
                        slug = jsonObject.getString("slug");
                    }
                    if (jsonObject.has("has_cities")) {
                        has_cities = jsonObject.getString("has_cities");
                    }
                    if (jsonObject.has("id")) {
                        id = jsonObject.getString("id");
                    }

                    if (jsonObject.has("name")) {
                        name = jsonObject.getString("name");
                    }
                    radioLocationRegionBeans.add(new RadioLocationBean(has_regions, slug, has_cities, id, name));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {

            try {

                fragmentlisten_progressBar2.setVisibility(View.GONE);
                fragmentlisten_grid.setVisibility(View.VISIBLE);
                ListenTextRegionSubGridAdapter listenSubGridAdapter = new ListenTextRegionSubGridAdapter(radioLocationRegionBeans, getActivity());
                fragmentlisten_grid.setAdapter(listenSubGridAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class ListenTextRegionSubGridAdapter extends RecyclerView.Adapter<ListenTextRegionSubGridAdapter.DataObjectHolder> {
        Context context;
        ArrayList<RadioLocationBean> listenGridBeans;


        public ListenTextRegionSubGridAdapter(ArrayList<RadioLocationBean> listenGridBeans, Context context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;

        }

        @Override
        public ListenTextRegionSubGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_listen_text_grid, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ListenTextRegionSubGridAdapter.DataObjectHolder holder, final int position) {
            holder.gridview_text_item.setText(listenGridBeans.get(position).getName());
            holder.gridview_text_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlocationcityposition = position;
                    FragmentListenMain.mRadioMainContent = Constants.RADIO_LOCATION;
                    FragmentListenMain.mRadioDetailsContent = Constants.RADIO_LOCATIONCITYCONTENT;
                    HomeActivity.toolbarSubContent = listenGridBeans.get(position).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                    if (listenGridBeans.get(position).getHas_cities().equalsIgnoreCase("true")) {
                        locationregionId = Integer.parseInt(listenGridBeans.get(position).getId());
                        new LoadingRadioLocatioCitiesSubCategory().execute();
                    } else {
                        locationcountryId = 0;
                        locationregionId = 0;
                        locationcityId = 0;
                        new LoadingRadioLocationListSubCategory().execute();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            TextView gridview_text_item;

            public DataObjectHolder(View itemView) {
                super(itemView);
                gridview_text_item = (TextView) itemView.findViewById(R.id.gridview_text_item);

                Utilities.setViewFocus(getActivity(),gridview_text_item);
            }
        }
    }


    private class LoadingRadioLocatioCitiesSubCategory extends AsyncTask<Object, Object, Object> {


        JSONObject jsonObject;
        private int region_id = 0;
        private String slug = "";
        private String has_cities = "";
        private int id = 0;
        private String name = "";

        @Override
        protected void onPreExecute() {
            fragmentlisten_grid.setVisibility(View.INVISIBLE);
            fragmentlisten_progressBar2.setVisibility(View.VISIBLE);
            radioLoactionRegions.clear();

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                m_jsonRadioeList = JSONRPCAPI.getRadioCity(locationcountryId, locationregionId);
                if (m_jsonRadioeList == null) return null;


                for (int i = 0; i < m_jsonRadioeList.length(); i++) {
                    jsonObject = m_jsonRadioeList.getJSONObject(i);

                    if (jsonObject.has("region_id")) {
                        region_id = jsonObject.getInt("region_id");
                    }
                    if (jsonObject.has("slug")) {
                        slug = jsonObject.getString("slug");
                    }
                    if (jsonObject.has("id")) {
                        id = jsonObject.getInt("id");
                    }

                    if (jsonObject.has("name")) {
                        name = jsonObject.getString("name");
                    }
                    radioLoactionRegions.add(new RadioLoactionRegion(id, region_id, name, slug));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {

            try {

                fragmentlisten_progressBar2.setVisibility(View.GONE);
                fragmentlisten_grid.setVisibility(View.VISIBLE);

                ListenTextCitiesSubGridAdapter listenTextCitiesSubGridAdapter = new ListenTextCitiesSubGridAdapter(radioLoactionRegions, getActivity());
                fragmentlisten_grid.setAdapter(listenTextCitiesSubGridAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public class ListenTextCitiesSubGridAdapter extends RecyclerView.Adapter<ListenTextCitiesSubGridAdapter.DataObjectHolder> {
        Context context;
        ArrayList<RadioLoactionRegion> listenGridBeans;


        public ListenTextCitiesSubGridAdapter(ArrayList<RadioLoactionRegion> listenGridBeans, Context context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;

        }

        @Override
        public ListenTextCitiesSubGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_listen_text_grid, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ListenTextCitiesSubGridAdapter.DataObjectHolder holder, final int position) {
            holder.gridview_text_item.setText(listenGridBeans.get(position).getName());
            holder.gridview_text_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentListenMain.mRadioMainContent = Constants.RADIO_LOCATION;
                    FragmentListenMain.mRadioDetailsContent = Constants.RADIO_LOCATIONCONTENT;
                    HomeActivity.toolbarSubContent = listenGridBeans.get(position).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                    locationcityId = listenGridBeans.get(position).getId();
                    new LoadingRadioLocationListSubCategory().execute();

                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            TextView gridview_text_item;

            public DataObjectHolder(View itemView) {
                super(itemView);
                gridview_text_item = (TextView) itemView.findViewById(R.id.gridview_text_item);

                Utilities.setViewFocus(getActivity(),gridview_text_item);
            }
        }
    }


    private class LoadingRadioLocationListSubCategory extends AsyncTask<Object, Object, Object> {


        int radiogenre_id = 0;
        String radiogenre_image = "";
        String radiogenre_name = "";
        String radiogenre_website = "";
        String radiogenre_slogan = "";
        String radiogenre_description = "";
        String radiogenre_stream = "";
        String radiogenre_twitter = "";
        String radiogenre_frequency = "";
        String radiogenre_phone = "";
        String radiogenre_email = "";
        String radiogenre_facebook = "";
        String radiogenre_address = "";
        String radiogenre_wikipedia = "";
        String radiogenre_bitrate = "";
        String radiogenre_slug = "";
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            fragmentlisten_grid.setVisibility(View.INVISIBLE);
            fragmentlisten_progressBar2.setVisibility(View.VISIBLE);
            radioLocationDetailBeans.clear();
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                m_jsonRadioeList = JSONRPCAPI.getRadioLocationList(locationId, locationcountryId, locationregionId, locationcityId);
                if (m_jsonRadioeList == null) return null;


                for (int i = 0; i < m_jsonRadioeList.length(); i++) {
                    jsonObject = m_jsonRadioeList.getJSONObject(i);
                    if (jsonObject.has("website")) {
                        radiogenre_website = jsonObject.getString("website");
                    }
                    if (jsonObject.has("slogan")) {
                        radiogenre_slogan = jsonObject.getString("slogan");
                    }
                    if (jsonObject.has("description")) {
                        radiogenre_description = jsonObject.getString("description");
                    }
                    if (jsonObject.has("stream")) {
                        radiogenre_stream = jsonObject.getString("stream");
                    }
                    if (jsonObject.has("twitter")) {
                        radiogenre_twitter = jsonObject.getString("twitter");
                    }
                    if (jsonObject.has("frequency")) {
                        radiogenre_frequency = jsonObject.getString("frequency");
                    }
                    if (jsonObject.has("wikipedia")) {
                        radiogenre_wikipedia = jsonObject.getString("wikipedia");
                    }
                    if (jsonObject.has("id")) {
                        radiogenre_id = jsonObject.getInt("id");
                    }
                    if (jsonObject.has("email")) {
                        radiogenre_email = jsonObject.getString("email");
                    }
                    if (jsonObject.has("phone")) {
                        radiogenre_phone = jsonObject.getString("phone");
                    }
                    if (jsonObject.has("facebook")) {
                        radiogenre_facebook = jsonObject.getString("facebook");
                    }
                    if (jsonObject.has("image")) {
                        radiogenre_image = jsonObject.getString("image");
                    }
                    if (jsonObject.has("bitrate")) {
                        radiogenre_bitrate = jsonObject.getString("bitrate");
                    }
                    if (jsonObject.has("address")) {
                        radiogenre_address = jsonObject.getString("address");
                    }
                    if (jsonObject.has("slug")) {
                        radiogenre_slug = jsonObject.getString("slug");
                    }
                    if (jsonObject.has("name")) {
                        radiogenre_name = jsonObject.getString("name");
                    }

                    radioLocationDetailBeans.add(new RadioDetailBean(radiogenre_website, radiogenre_slogan, radiogenre_description, radiogenre_stream, radiogenre_twitter, radiogenre_frequency, radiogenre_wikipedia, radiogenre_phone, radiogenre_email, radiogenre_facebook, radiogenre_image, radiogenre_address, radiogenre_bitrate, radiogenre_slug, radiogenre_name, radiogenre_id));
                    radioDetailBeans=radioLocationDetailBeans;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {

            try {

                fragmentlisten_progressBar2.setVisibility(View.GONE);
                fragmentlisten_grid.setVisibility(View.VISIBLE);

                ListenRadioLocationListAdapter listenTextCitiesSubGridAdapter = new ListenRadioLocationListAdapter(radioLocationDetailBeans, getActivity());
                fragmentlisten_grid.setAdapter(listenTextCitiesSubGridAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class ListenRadioLocationListAdapter extends RecyclerView.Adapter<ListenRadioLocationListAdapter.DataObjectHolder> {
        Context context;
        ArrayList<RadioDetailBean> listenGridBeans;


        public ListenRadioLocationListAdapter(ArrayList<RadioDetailBean> listenGridBeans, Context context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;

        }

        @Override
        public ListenRadioLocationListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ListenRadioLocationListAdapter.DataObjectHolder holder, final int position) {
            holder.imageView.loadMovieImage(listenGridBeans.get(position).getImage());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.toolbarSubContent = listenGridBeans.get(position).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    ((HomeActivity) getActivity()).swapRadioFragment(true);
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    FragmentRadioDetails listfragment = FragmentRadioDetails.newInstance(String.valueOf(position), "");
                    fragmentTransaction_list.replace(R.id.activity_radio_fragemnt_layout, listfragment);
                    fragmentTransaction_list.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView imageView;

            public DataObjectHolder(View itemView) {
                super(itemView);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);

                Utilities.setViewFocus(getActivity(),imageView);

            }
        }
    }


    public void mLocationAdapterBackFunction() {
        switch (FragmentListenMain.mRadioDetailsContent) {
            case Constants.RADIO_LOCATIONSUBCONTENT:
                FragmentListenMain.mRadioDetailsContent = "";
                FragmentListenMain.mRadioMainContent = "";
                HomeActivity.toolbarSubContent = FragmentListenList.listenLocationBeans.get(mlocationposition).getName();
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                ListenTextGridAdapter listenGridAdapter = new ListenTextGridAdapter(FragmentListenList.listenLocationBeans, getActivity());
                fragmentlisten_grid.setAdapter(listenGridAdapter);
                break;
            case Constants.RADIO_LOCATIONREGINCONTENT:
                FragmentListenMain.mRadioDetailsContent = Constants.RADIO_LOCATIONSUBCONTENT;
                HomeActivity.toolbarSubContent = radioLocationBeans.get(mlocationreginposition).getName();
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                ListenTextSubGridAdapter listenTextSubGridAdapter = new ListenTextSubGridAdapter(radioLocationBeans, getActivity());
                fragmentlisten_grid.setAdapter(listenTextSubGridAdapter);
                break;
            case Constants.RADIO_LOCATIONCITYCONTENT:
                FragmentListenMain.mRadioDetailsContent = Constants.RADIO_LOCATIONREGINCONTENT;
                HomeActivity.toolbarSubContent = radioLocationRegionBeans.get(mlocationcityposition).getName();
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                ListenTextRegionSubGridAdapter listenSubGridAdapter = new ListenTextRegionSubGridAdapter(radioLocationRegionBeans, getActivity());
                fragmentlisten_grid.setAdapter(listenSubGridAdapter);
                break;
            case Constants.RADIO_LOCATIONCONTENT:
                FragmentListenMain.mRadioDetailsContent = Constants.RADIO_LOCATIONCITYCONTENT;
                ListenTextCitiesSubGridAdapter listenTextCitiesSubGridAdapter = new ListenTextCitiesSubGridAdapter(radioLoactionRegions, getActivity());
                fragmentlisten_grid.setAdapter(listenTextCitiesSubGridAdapter);
                break;
            default:
                switch (mParam1) {
                    case "0": {
                        FragmentListenMain.mRadioMainContent= Constants.RADIO_GENRE;
                        ListenGridAdapter gridAdapter = new ListenGridAdapter(FragmentListenList.listenGenreBeans, getActivity());
                        fragmentlisten_grid.setAdapter(gridAdapter);
                        break;
                    }
                    case "1": {
                        FragmentListenMain.mRadioMainContent= Constants.RADIO_LANGUAGE;
                        ListenGridAdapter gridAdapter = new ListenGridAdapter(FragmentListenList.listenLanguageBeans, getActivity());
                        fragmentlisten_grid.setAdapter(gridAdapter);
                        break;
                    }
                    case "2": {
                        FragmentListenMain.mRadioMainContent = Constants.RADIO_LOCATION;
                        listenGridAdapter = new ListenTextGridAdapter(FragmentListenList.listenLocationBeans, getActivity());
                        fragmentlisten_grid.setAdapter(listenGridAdapter);
                        break;
                    }
                }
                break;
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
            fragmentlisten_progressBar2.setVisibility(View.VISIBLE);
            fragmentlisten_grid.setVisibility(View.INVISIBLE);
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

                    grid_list.add(new GridBean(id, name, image, description, type));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                fragmentlisten_progressBar2.setVisibility(View.GONE);
                fragmentlisten_grid.setVisibility(View.VISIBLE);
                if (grid_list.size() > 0) {
                    if (FragmentListenMain.isdestryed) {
                        FragmentListenMain.mListenSubContent = Constants.LISTEN_LISTEN_CONTENT;
                        FragmentListenMain.mListentempSubContent = Constants.LISTEN_LISTEN_CONTENT;
                        FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                        GridFragment network_listfragment = GridFragment.newInstance(Constants.SHOWS, m_jsonNetworkList.toString());
                        network_fragmentTransaction.replace(R.id.fragment_listen_pagerandlist, network_listfragment);
                        network_fragmentTransaction.commit();
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


}
