package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvlauncher.BeanClass.SearchBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.ui.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentSearchList extends Fragment implements SearchCouroselsFragment.onViewSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ImageView fragment_search_prev_icon;
    private RecyclerView fragment_search_RecyclerView;
    private ArrayList<SearchBean> mOnDemandlist = new ArrayList<>();
    private ArrayList<String> mOnDemand = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private OnSearchListAdapter onSearchListAdapter;
    private String mSearchList;
    private JSONObject json_searchobject;
    private JSONObject json_content;
    private JSONArray fullSearchContent;
    private GridFragment grid_fragment;
    private SearchGridFragment search_grid_fragment;
    public static String mSearchContent;
    public static int allcount = 0;
    private int mSelectedItem;


    public static FragmentSearchList newInstance(String param1, String param2) {
        FragmentSearchList fragment = new FragmentSearchList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSearchList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchList = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_search_left, container, false);
        fragment_search_prev_icon = (ImageView) view.findViewById(R.id.fragment_search_prev_icon);
        fragment_search_RecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_RecyclerView);

        ((HomeActivity) getActivity()).setmFragmentSearchList(this);

        makeDetail(HomeActivity.m_jsonObjectSearchResult);
        fragment_search_prev_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchContent.equals(Constants.SEARCH_SUBCONTENT)) {
                    mSearchContent = Constants.SEARCH_MAINCONTENT;
                    HomeActivity.selectedmenu = "";
                    HomeActivity.isSearchClick = true;

                    FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                    grid_fragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.jsonArray_searchNetwork.toString());
                    fragmentTransaction_grid.replace(R.id.fragment_search_content, grid_fragment);
                    fragmentTransaction_grid.commit();
                } else if (mSearchContent.equals(Constants.SEARCH_MAINCONTENT)) {
                    HomeActivity.selectedmenu = HomeActivity.selectedSearchmenu;
                    HomeActivity.isPayperview = HomeActivity.mPayperview;
                    ((HomeActivity) getActivity()).SwapSearchFragment(false);

                }
            }
        });
        return view;
    }


    public void makeDetail(JSONObject jsonArray) {
        try {
            HomeActivity.m_jsonArraySearchResult = new JSONArray();

            HomeActivity.toolbarMainContent = "All";
            HomeActivity.toolbarSubContent = "";
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
            mSearchContent = Constants.SEARCH_MAINCONTENT;
            mOnDemandlist.clear();
            json_searchobject = new JSONObject(mSearchList);
            json_content = json_searchobject.getJSONObject("show");

            String name = "";
            String type = "";


            if (json_searchobject.has("show")) {


                if (json_content.has("count")) {
                    mOnDemand.add("Show");

                    int count = json_content.getInt("count");
                    allcount = count;
                    name = "TV Shows" + " (" + count + ")";
                    type = "show";
                    if (count > 0) {
                        mOnDemandlist.add(new SearchBean(name, type, count));
                    }


                } else {
                    name = "TV Shows";
                    type = "show";
                    mOnDemandlist.add(new SearchBean(name, type, 0));

                }


                if (json_content.has("items")) {
                    HomeActivity.jsonArray_searchShow = json_content.getJSONArray("items");
                    for (int i = 0; i < HomeActivity.jsonArray_searchShow.length(); i++) {
                        HomeActivity.m_jsonArraySearchResult.put(HomeActivity.jsonArray_searchShow.getJSONObject(i));

                    }

                }
            }


            json_content = json_searchobject.getJSONObject("network");

            if (json_searchobject.has("network")) {
                mOnDemand.add("network");
                if (json_content.has("count")) {
                    int count = json_content.getInt("count");
                    allcount = allcount + count;
                    name = "Network" + " (" + count + ")";
                    type = "network";
                    if (count > 0) {
                        mOnDemandlist.add(new SearchBean(name, type, count));
                    }


                } else {
                    name = "Network";
                    type = "network";
                    mOnDemandlist.add(new SearchBean(name, type, 0));
                }

                if (json_content.has("items")) {
                    HomeActivity.jsonArray_searchNetwork = json_content.getJSONArray("items");
                    for (int i = 0; i < HomeActivity.jsonArray_searchNetwork.length(); i++) {
                        HomeActivity.m_jsonArraySearchResult.put(HomeActivity.jsonArray_searchNetwork.getJSONObject(i));

                    }
                }
            }

            json_content = json_searchobject.getJSONObject("movie");
            if (json_searchobject.has("movie")) {
                mOnDemand.add("movie");
                if (json_content.has("count")) {
                    int count = json_content.getInt("count");
                    allcount = allcount + count;
                    name = "Movies" + " (" + count + ")";
                    type = "movie";
                    if (count > 0) {
                        mOnDemandlist.add(new SearchBean(name, type, count));
                    }


                } else {
                    name = "Movies";
                    type = "movie";
                    mOnDemandlist.add(new SearchBean(name, type, 0));
                }
                if (json_content.has("items")) {
                    HomeActivity.jsonArray_searchMovies = json_content.getJSONArray("items");
                    for (int i = 0; i < HomeActivity.jsonArray_searchMovies.length(); i++) {
                        HomeActivity.m_jsonArraySearchResult.put(HomeActivity.jsonArray_searchMovies.getJSONObject(i));

                    }
                }
            }

            json_content = json_searchobject.getJSONObject("actor");
            if (json_searchobject.has("actor")) {
                mOnDemand.add("actor");
                if (json_content.has("count")) {
                    int count = json_content.getInt("count");
                    allcount = allcount + count;
                    name = "Actors" + " (" + count + ")";
                    type = "actor";
                    if (count > 0) {
                        mOnDemandlist.add(new SearchBean(name, type, count));
                    }


                } else {
                    name = "Actors";
                    type = "actor";
                    mOnDemandlist.add(new SearchBean(name, type, 0));

                }
                if (json_content.has("items")) {
                    HomeActivity.jsonArray_searchActor = json_content.getJSONArray("items");
                }
            }
            json_content = json_searchobject.getJSONObject("tvstation");
            if (json_searchobject.has("tvstation")) {
                mOnDemand.add("tvstation");
                if (json_content.has("count")) {
                    int count = json_content.getInt("count");
                    allcount = allcount + count;
                    name = "TV Stations" + " (" + count + ")";
                    type = "tvstation";
                    if (count > 0) {
                        mOnDemandlist.add(new SearchBean(name, type, count));
                    }


                } else {
                    name = "TV Stations";
                    type = "tvstation";
                    mOnDemandlist.add(new SearchBean(name, type, 0));

                }
                if (json_content.has("items")) {
                    HomeActivity.jsonArray_searchTvStations = json_content.getJSONArray("items");
                }
            }
            json_content = json_searchobject.getJSONObject("live");

            if (json_searchobject.has("live")) {
                mOnDemand.add("live");
                if (json_content.has("count")) {
                    int count = json_content.getInt("count");
                    allcount = allcount + count;
                    name = "Live" + " (" + count + ")";
                    type = "live";
                    if (count > 0) {
                        mOnDemandlist.add(new SearchBean(name, type, count));
                    }


                } else {
                    name = "Live";
                    type = "live";
                    mOnDemandlist.add(new SearchBean(name, type, 0));


                }
                if (json_content.has("items")) {
                    HomeActivity.jsonArray_searchLive = json_content.getJSONArray("items");
                }
            }
            json_content = json_searchobject.getJSONObject("station");
            if (json_searchobject.has("station")) {
                mOnDemand.add("station");
                if (json_content.has("count")) {
                    int count = json_content.getInt("count");
                    allcount = allcount + count;

                    name = "Channels" + " (" + count + ")";
                    type = "station";
                    if (count > 0) {
                        mOnDemandlist.add(new SearchBean(name, type, count));
                    }

                } else {
                    name = "Channels";
                    type = "station";
                    mOnDemandlist.add(new SearchBean(name, type, 0));


                }
                if (json_content.has("items")) {
                    HomeActivity.jsonArray_searchStations = json_content.getJSONArray("items");
                }
            }
            json_content = json_searchobject.getJSONObject("radio");
            if (json_searchobject.has("radio")) {
                mOnDemand.add("radio");
                if (json_content.has("count")) {
                    int count = json_content.getInt("count");
                    allcount = allcount + count;
                    name = "Radio" + " (" + count + ")";
                    type = "radio";
                    if (count > 0) {
                        mOnDemandlist.add(new SearchBean(name, type, count));
                    }
                } else {
                    name = "Radio";
                    type = "radio";
                    mOnDemandlist.add(new SearchBean(name, type, 0));


                }
                if (json_content.has("items")) {
                    HomeActivity.jsonArray_searchRadio = json_content.getJSONArray("items");
                }
            }
            json_content = json_searchobject.getJSONObject("music");
            if (json_searchobject.has("music")) {
                mOnDemand.add("music");
                if (json_content.has("count")) {
                    int count = json_content.getInt("count");
                    allcount = allcount + count;
                    name = "Music" + " (" + count + ")";
                    type = "music";
                    if (count > 0) {
                        mOnDemandlist.add(new SearchBean(name, type, count));
                    }


                } else {
                    name = "Music";
                    type = "music";
                    mOnDemandlist.add(new SearchBean(name, type, 0));


                }
                if (json_content.has("items")) {
                    HomeActivity.jsonArray_searchMusic = json_content.getJSONArray("items");
                }
            }
            mOnDemandlist.add(0, new SearchBean("All", "all", allcount));


            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_search_RecyclerView.setLayoutManager(mLayoutManager);
            onSearchListAdapter = new OnSearchListAdapter(mOnDemandlist, getActivity());
            fragment_search_RecyclerView.setAdapter(onSearchListAdapter);
            HomeActivity.selectedSearchmenu = HomeActivity.selectedmenu;
            HomeActivity.selectedmenu = "";
            /*FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
            grid_fragment = GridFragment.newInstance(Constants.CATEGORY_SHOW, HomeActivity.jsonArray_searchShow.toString());
            fragmentTransaction_grid.replace(R.id.fragment_search_content, grid_fragment);
            fragmentTransaction_grid.commit();*/
            FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
            SearchCouroselsFragment co_fragment = SearchCouroselsFragment.newInstance(Constants.CATEGORY_SHOW, mSearchList, FragmentSearchList.this);
            fragmentTransaction_grid.replace(R.id.fragment_search_content, co_fragment);
            fragmentTransaction_grid.commit();
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

    @Override
    public void onViewSelected(int id) {
        FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
        grid_fragment = GridFragment.newInstance(Constants.MOVIES, fullSearchContent.toString());
        fragmentTransaction_grid.replace(R.id.fragment_search_content, grid_fragment);
        fragmentTransaction_grid.commit();
    }

    public void onViewwSelected(String d_type, int d_count) {
        try {
            for (int k = 0; k < mOnDemandlist.size(); k++) {
                if (mOnDemandlist.get(k).getType().equals(d_type)) {
                    mSelectedItem = k;
                    fragment_search_RecyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            new LoadingFullSearchdata().execute(d_type, "" + d_count);
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


    private class OnSearchListAdapter extends RecyclerView.Adapter<OnSearchListAdapter.DataObjectHolder> {
        ArrayList<SearchBean> data;
        Context context;


        public OnSearchListAdapter(ArrayList<SearchBean> data, Context ctx) {
            this.data = data;
            this.context = ctx;
        }

        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final DataObjectHolder holder, final int position) {
            holder.fragment_ondemandlist_items.setText(data.get(position).getName());
            if (position == mSelectedItem) {
//                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
                holder.fragment_ondemandlist_items.setBackgroundColor(getResources().getColor(R.color.bg_dark_color));
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }
            holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = position;
//                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
                    holder.fragment_ondemandlist_items.setBackgroundColor(getResources().getColor(R.color.bg_dark_color));
                    notifyDataSetChanged();
                    switch (data.get(position).getType()) {


                        case Constants.SEARCH_ALL:
                            HomeActivity.toolbarMainContent = "All";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            mSearchContent = Constants.SEARCH_MAINCONTENT;
                            HomeActivity.selectedSearchmenu = HomeActivity.selectedmenu;
                            HomeActivity.selectedmenu = "";
                            FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                            SearchCouroselsFragment co_fragment = SearchCouroselsFragment.newInstance(Constants.CATEGORY_SHOW, mSearchList, FragmentSearchList.this);
                            fragmentTransaction_grid.replace(R.id.fragment_search_content, co_fragment);
                            fragmentTransaction_grid.commit();
                            break;


                        case Constants.SEARCH_SHOW:
                            HomeActivity.toolbarMainContent = "TV Shows";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            mSearchContent = Constants.SEARCH_MAINCONTENT;
                            HomeActivity.selectedSearchmenu = HomeActivity.selectedmenu;
                            HomeActivity.selectedmenu = "";
                            new LoadingFullSearchdata().execute(data.get(position).getType(), Integer.toString(data.get(position).getCount()));

                            /*fragmentTransaction_grid = getFragmentManager().beginTransaction();
                            grid_fragment = GridFragment.newInstance(Constants.CATEGORY_SHOW, HomeActivity.jsonArray_searchShow.toString());
                            fragmentTransaction_grid.replace(R.id.fragment_search_content, grid_fragment);
                            fragmentTransaction_grid.commit();*/
                            break;
                        case Constants.SEARCH_NETWORK:
                            HomeActivity.toolbarMainContent = "Network";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            HomeActivity.selectedSearchmenu = HomeActivity.selectedmenu;
                            HomeActivity.selectedmenu = "";
                            HomeActivity.isSearchClick = true;

                            new LoadingFullSearchdata().execute(data.get(position).getType(), Integer.toString(data.get(position).getCount()));
                            /*fragmentTransaction_grid = getFragmentManager().beginTransaction();
                            grid_fragment = GridFragment.newInstance(Constants.NETWORK, HomeActivity.jsonArray_searchNetwork.toString());
                            fragmentTransaction_grid.replace(R.id.fragment_search_content, grid_fragment);
                            fragmentTransaction_grid.commit();*/
                            break;
                        case Constants.SEARCH_MOVIE:
                            HomeActivity.toolbarMainContent = "Movies";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            HomeActivity.selectedSearchmenu = HomeActivity.selectedmenu;
                            mSearchContent = Constants.SEARCH_MAINCONTENT;
                            HomeActivity.selectedmenu = Constants.SEARCH_MOVIE;
                            new LoadingFullSearchdata().execute(data.get(position).getType(), Integer.toString(data.get(position).getCount()));
                            /*fragmentTransaction_grid = getFragmentManager().beginTransaction();
                            grid_fragment = GridFragment.newInstance(Constants.MOVIES, HomeActivity.jsonArray_searchMovies.toString());
                            fragmentTransaction_grid.replace(R.id.fragment_search_content, grid_fragment);
                            fragmentTransaction_grid.commit();*/
                            break;
                        case Constants.SEARCH_ACTOR:
                            HomeActivity.selectedmenu = "";
                            HomeActivity.toolbarMainContent = "Actors";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            new LoadingFullSearchdata().execute(data.get(position).getType(), Integer.toString(data.get(position).getCount()));
                            break;
                        case Constants.SEARCH_TVSTATIONS:
                            HomeActivity.selectedmenu = "";
                            HomeActivity.toolbarMainContent = "TV Stations";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            new LoadingFullSearchdata().execute(data.get(position).getType(), Integer.toString(data.get(position).getCount()));
                            break;
                        case Constants.SEARCH_LIVE:
                            HomeActivity.selectedmenu = "";
                            HomeActivity.toolbarMainContent = "Live";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            new LoadingFullSearchdata().execute(data.get(position).getType(), Integer.toString(data.get(position).getCount()));
                            break;
                        case Constants.SEARCH_STATIONS:
                            HomeActivity.selectedmenu = "";
                            HomeActivity.toolbarMainContent = "Channels";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            new LoadingFullSearchdata().execute(data.get(position).getType(), Integer.toString(data.get(position).getCount()));
                            break;
                        case Constants.SEARCH_RADIO:
                            HomeActivity.selectedmenu = "";
                            HomeActivity.toolbarMainContent = "Radio";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            new LoadingFullSearchdata().execute(data.get(position).getType(), Integer.toString(data.get(position).getCount()));
                            break;
                        case Constants.SEARCH_MUSIC:
                            HomeActivity.selectedmenu = "";
                            HomeActivity.toolbarMainContent = "Music";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            new LoadingFullSearchdata().execute(data.get(position).getType(), Integer.toString(data.get(position).getCount()));
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);
                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);
            }
        }
    }

    private class LoadingFullSearchdata extends AsyncTask<String, Integer, Object> {
        String searchtype = "";

        @Override
        protected void onPreExecute() {
            //  mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
            FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
            DialogFragment frag = new DialogFragment();
            fragmentTransaction_grid.replace(R.id.fragment_search_content, frag);
            fragmentTransaction_grid.commit();
        }

        @Override
        protected Object doInBackground(String... params) {
            searchtype = params[0];
            fullSearchContent = JSONRPCAPI.getFullSearchResult(HomeActivity.searchkey, params[0], Integer.parseInt(params[1]));
            if (fullSearchContent == null) return null;
            Log.d("fullsearch data::", ":::" + fullSearchContent);

            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            //  mProgressHUD.dismiss();

            try {
                switch (searchtype) {
                    case Constants.SEARCH_SHOW:
                        HomeActivity.toolbarMainContent = "TV Shows";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        mSearchContent = Constants.SEARCH_MAINCONTENT;
                        HomeActivity.selectedSearchmenu = HomeActivity.selectedmenu;
                        HomeActivity.selectedmenu = "";
                        FragmentTransaction fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        grid_fragment = GridFragment.newInstance(Constants.CATEGORY_SHOW, fullSearchContent.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_search_content, grid_fragment);
                        fragmentTransaction_grid.commit();
                        break;
                    case Constants.SEARCH_NETWORK:
                        HomeActivity.toolbarMainContent = "Network";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        HomeActivity.selectedSearchmenu = HomeActivity.selectedmenu;
                        HomeActivity.selectedmenu = "";
                        HomeActivity.isSearchClick = true;
                        fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        grid_fragment = GridFragment.newInstance(Constants.NETWORK, fullSearchContent.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_search_content, grid_fragment);
                        fragmentTransaction_grid.commit();
                        break;
                    case Constants.SEARCH_MOVIE:
                        HomeActivity.toolbarMainContent = "Movies";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        HomeActivity.selectedSearchmenu = HomeActivity.selectedmenu;
                        mSearchContent = Constants.SEARCH_MAINCONTENT;
                        HomeActivity.selectedmenu = Constants.SEARCH_MOVIE;
                        fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        grid_fragment = GridFragment.newInstance(Constants.MOVIES, fullSearchContent.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_search_content, grid_fragment);
                        fragmentTransaction_grid.commit();
                        break;
                    case Constants.SEARCH_ACTOR:
                        HomeActivity.selectedmenu = "";
                        HomeActivity.toolbarMainContent = "Actors";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        search_grid_fragment = SearchGridFragment.newInstance(Constants.SEARCH_ACTOR, HomeActivity.jsonArray_searchActor.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_search_content, search_grid_fragment);
                        fragmentTransaction_grid.commit();
                        break;
                    case Constants.SEARCH_TVSTATIONS:
                        HomeActivity.selectedmenu = "";
                        HomeActivity.toolbarMainContent = "TV Stations";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        search_grid_fragment = SearchGridFragment.newInstance(Constants.SEARCH_TVSTATIONS, HomeActivity.jsonArray_searchTvStations.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_search_content, search_grid_fragment);
                        fragmentTransaction_grid.commit();
                        break;
                    case Constants.SEARCH_LIVE:
                        HomeActivity.selectedmenu = "";
                        HomeActivity.toolbarMainContent = "Live";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        search_grid_fragment = SearchGridFragment.newInstance(Constants.SEARCH_LIVE, HomeActivity.jsonArray_searchLive.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_search_content, search_grid_fragment);
                        fragmentTransaction_grid.commit();
                        break;
                    case Constants.SEARCH_STATIONS:
                        HomeActivity.selectedmenu = "";
                        HomeActivity.toolbarMainContent = "Channels";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        search_grid_fragment = SearchGridFragment.newInstance(Constants.SEARCH_STATIONS, HomeActivity.jsonArray_searchStations.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_search_content, search_grid_fragment);
                        fragmentTransaction_grid.commit();
                        break;
                    case Constants.SEARCH_RADIO:
                        HomeActivity.selectedmenu = "";
                        HomeActivity.toolbarMainContent = "Radio";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        search_grid_fragment = SearchGridFragment.newInstance(Constants.SEARCH_RADIO, HomeActivity.jsonArray_searchRadio.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_search_content, search_grid_fragment);
                        fragmentTransaction_grid.commit();
                        break;
                    case Constants.SEARCH_MUSIC:
                        HomeActivity.selectedmenu = "";
                        HomeActivity.toolbarMainContent = "Music";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        fragmentTransaction_grid = getFragmentManager().beginTransaction();
                        search_grid_fragment = SearchGridFragment.newInstance(Constants.SEARCH_MUSIC, HomeActivity.jsonArray_searchMusic.toString());
                        fragmentTransaction_grid.replace(R.id.fragment_search_content, search_grid_fragment);
                        fragmentTransaction_grid.commit();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

}
