package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.BeanClass.SeasonsBean;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentShowSeasonsAndEpisodes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView fragment_details_shows_seasons;
    private RecyclerView fragment_details_shows__episodes;
    private GridLayoutManager mLayoutManager;
    SeasonsAndEpisodesAdapter seasonsAndEpisodesAdapter;
    public static ArrayList<SeasonsBean> seasonsBeans = new ArrayList<>();
    public static ArrayList<SeasonsBean> mEpisodeBeans = new ArrayList<>();
    public static ArrayList<SeasonsBean> FreemEpisodeBeans = new ArrayList<>();
    public static ArrayList<SeasonsBean> FreeAllmEpisodeBeans = new ArrayList<>();
    public static ArrayList<SeasonsBean> dialog_FreemEpisodeBeans = new ArrayList<>();
    public static ArrayList<SeasonsBean> dialog_AllmEpisodeBeans = new ArrayList<>();
    private JSONArray m_jsonSeasons;
    private JSONArray m_jsonEpisodes;
    private JSONArray m_jsonFreeEpisode;
    JSONObject m_jsonEpisodeDeatils;
    private String type;
    int Seasonid;
    int mEpisodeId;
    Boolean isClicked = false;
    Boolean mShowSpinner = false;
    private Spinner choose_your_episode;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ArrayList<Integer> integerArrayList = new ArrayList<>();
    int nIndex;
    private JSONObject m_jsonshowDetail;
    private JSONObject m_jsonshowLinks;
    public static String image_url = "";
    String season_name = "";
    ArrayAdapter<String> adapter;
    int isSpinnerClicked;
    Boolean mSpinnerClicked = true;
    private LinearLayout layout_show_free;
    private LinearLayout layout_all_episodes;
    public static LinearLayout layout_all_seasons;
    private View show_free_left;
    private View show_free_top;
    private View show_free_bottom;
    private View all_episodes_left;
    private View all_episodes_top;
    private View all_episodes_bottom;
    private View all_seasons_left;
    private View all_seasons_top;
    private View all_seasons_bottom;
    private View all_seasons_right;
    public static TextView fragment_season_details_watchlatest;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private TextView show_free_title;
    private TextView all_episodes_title;
    private TextView all_seasons_title;
    private EpisodeAdapter episodeAdapter;
    private RecyclerView fragment_details_shows_free;
    private ProgressBar progressload_showsandseasons;
    private LinearLayout dynamic_horzontal_list_layout;
    private LayoutInflater inflate;
    private int height, width;
    public static LinearLayout layout_latest_episodes;
    Boolean setLayout = true;
    Boolean setepisode = true;
    private TextView free_textView;
    private LinearLayout layout_showsandseasons_mainLayout;
    private Tracker mTracker;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentShowSeasonsAndEpisodes.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentShowSeasonsAndEpisodes newInstance(String param1, int param2) {
        FragmentShowSeasonsAndEpisodes fragment = new FragmentShowSeasonsAndEpisodes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentShowSeasonsAndEpisodes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
            Seasonid = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_show_seasons_and_episodes, container, false);
        ((HomeActivity) getActivity()).setFragmentShowSeasonsAndEpisodes(this);
        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog_AllmEpisodeBeans.clear();
        dialog_FreemEpisodeBeans.clear();
        mTracker = LauncherApplication.getInstance().getDefaultTracker();

        progressload_showsandseasons = (ProgressBar) view.findViewById(R.id.progressload_showsandseasons);

        fragment_season_details_watchlatest = (TextView) view.findViewById(R.id.fragment_season_details_watchlatest);

        show_free_title = (TextView) view.findViewById(R.id.show_free_title);
        all_episodes_title = (TextView) view.findViewById(R.id.all_episodes_title);
        all_seasons_title = (TextView) view.findViewById(R.id.all_seasons_title);
        free_textView = (TextView) view.findViewById(R.id.free_textView);
        choose_your_episode = (Spinner) view.findViewById(R.id.choose_your_episode);

        layout_showsandseasons_mainLayout = (LinearLayout) view.findViewById(R.id.layout_showsandseasons_mainLayout);
        layout_latest_episodes = (LinearLayout) view.findViewById(R.id.layout_latest_episodes);
        layout_show_free = (LinearLayout) view.findViewById(R.id.layout_show_free);

        layout_all_episodes = (LinearLayout) view.findViewById(R.id.layout_all_episodes);
        layout_all_seasons = (LinearLayout) view.findViewById(R.id.layout_all_seasons);
        dynamic_horzontal_list_layout = (LinearLayout) view.findViewById(R.id.dynamic_horzontal_list_layout);

        Utilities.setTextFocus(getActivity(), layout_show_free);
        Utilities.setTextFocus(getActivity(), layout_all_episodes);
        Utilities.setTextFocus(getActivity(), layout_all_seasons);

        show_free_left = (View) view.findViewById(R.id.show_free_left);
        show_free_top = (View) view.findViewById(R.id.show_free_top);
        show_free_bottom = (View) view.findViewById(R.id.show_free_bottom);
        all_episodes_top = (View) view.findViewById(R.id.all_episodes_top);
        all_episodes_left = (View) view.findViewById(R.id.all_episodes_left);
        all_episodes_bottom = (View) view.findViewById(R.id.all_episodes_bottom);
        all_seasons_left = (View) view.findViewById(R.id.all_seasons_left);
        all_seasons_top = (View) view.findViewById(R.id.all_seasons_top);
        all_seasons_bottom = (View) view.findViewById(R.id.all_seasons_bottom);
        all_seasons_right = (View) view.findViewById(R.id.all_seasons_right);

        fragment_details_shows_free = (RecyclerView) view.findViewById(R.id.fragment_details_shows_free);
        fragment_details_shows_seasons = (RecyclerView) view.findViewById(R.id.fragment_details_shows_seasons);
        fragment_details_shows__episodes = (RecyclerView) view.findViewById(R.id.fragment_details_shows__episodes);

        choose_your_episode.setVisibility(View.GONE);
        text_font_typeface();
        fragment_season_details_watchlatest.setTypeface(OpenSans_Regular);
        show_free_title.setTypeface(OpenSans_Bold);
        all_episodes_title.setTypeface(OpenSans_Regular);
        all_seasons_title.setTypeface(OpenSans_Regular);


        mLayoutManager = new GridLayoutManager(getActivity(), 4);
        fragment_details_shows_free.setLayoutManager(mLayoutManager);
        int spanCount = 4; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        fragment_details_shows_free.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


        isClicked = true;
        mShowSpinner = true;
        mEpisodeId = 0;
        new FreeEpisodeLoading().execute();

        if (HomeActivity.mfreeorall == 1) {
            layout_all_episodes.setVisibility(View.GONE);


        } else {
            new EpisodeLoading().execute();
        }


        nIndex = Seasonid;
        new SeasonLoading().execute();


        layout_show_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choose_your_episode.setVisibility(View.GONE);


                selectFreeEpisodes();
                setLayout = false;
                setFreeEpisodeList(m_jsonFreeEpisode);
            }
        });

        layout_all_episodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                choose_your_episode.setVisibility(View.GONE);
                selecvtAllEpisodes();


                if (m_jsonEpisodes != null) {
                    setEpisodeList(m_jsonEpisodes);
                }
            }
        });


        layout_all_seasons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choose_your_episode.setVisibility(View.GONE);
                selectAllSeasons();


                if (m_jsonSeasons != null) {
                    setListData(m_jsonSeasons);
                }
            }
        });

        return view;
    }

    private void selectAllSeasons() {
        fragment_details_shows__episodes.setVisibility(View.GONE);
        fragment_details_shows_seasons.setVisibility(View.GONE);
        fragment_details_shows_free.setVisibility(View.VISIBLE);
        show_free_left.setVisibility(View.GONE);
        show_free_top.setVisibility(View.GONE);
        all_episodes_left.setVisibility(View.GONE);
        all_episodes_bottom.setVisibility(View.VISIBLE);
        all_seasons_bottom.setVisibility(View.GONE);
        show_free_bottom.setVisibility(View.VISIBLE);
        all_episodes_top.setVisibility(View.GONE);
        all_seasons_left.setVisibility(View.VISIBLE);
        all_seasons_top.setVisibility(View.VISIBLE);
        all_seasons_right.setVisibility(View.VISIBLE);
        show_free_title.setTypeface(OpenSans_Regular);
        all_episodes_title.setTypeface(OpenSans_Regular);
        all_seasons_title.setTypeface(OpenSans_Bold);
    }

    private void selecvtAllEpisodes() {
        fragment_details_shows__episodes.setVisibility(View.GONE);
        fragment_details_shows_seasons.setVisibility(View.GONE);
        fragment_details_shows_free.setVisibility(View.VISIBLE);
        show_free_left.setVisibility(View.GONE);
        show_free_top.setVisibility(View.GONE);
        all_episodes_left.setVisibility(View.VISIBLE);
        all_episodes_bottom.setVisibility(View.GONE);
        all_seasons_bottom.setVisibility(View.VISIBLE);
        show_free_bottom.setVisibility(View.VISIBLE);
        all_episodes_top.setVisibility(View.VISIBLE);
        all_seasons_left.setVisibility(View.VISIBLE);
        all_seasons_top.setVisibility(View.GONE);
        all_seasons_right.setVisibility(View.GONE);
        show_free_title.setTypeface(OpenSans_Regular);
        all_episodes_title.setTypeface(OpenSans_Bold);
        all_seasons_title.setTypeface(OpenSans_Regular);
    }

    private void selectFreeEpisodes() {
        fragment_details_shows__episodes.setVisibility(View.GONE);
        fragment_details_shows_seasons.setVisibility(View.GONE);
        fragment_details_shows_free.setVisibility(View.VISIBLE);
        show_free_left.setVisibility(View.VISIBLE);
        show_free_top.setVisibility(View.VISIBLE);
        all_episodes_left.setVisibility(View.VISIBLE);
        all_episodes_bottom.setVisibility(View.VISIBLE);
        all_seasons_bottom.setVisibility(View.VISIBLE);
        show_free_bottom.setVisibility(View.GONE);
        all_episodes_top.setVisibility(View.GONE);
        all_seasons_left.setVisibility(View.GONE);
        all_seasons_top.setVisibility(View.GONE);
        all_seasons_right.setVisibility(View.GONE);
        show_free_title.setTypeface(OpenSans_Bold);
        all_episodes_title.setTypeface(OpenSans_Regular);
        all_seasons_title.setTypeface(OpenSans_Regular);
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
        try {
            ((HomeActivity) getActivity()).setFragmentShowSeasonsAndEpisodes(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void hideLatest(Boolean aBoolean) {
        if (aBoolean) {
            layout_latest_episodes.setVisibility(View.GONE);
            fragment_season_details_watchlatest.setVisibility(View.GONE);
            layout_all_seasons.setVisibility(View.GONE);
        } else {
            layout_latest_episodes.setVisibility(View.VISIBLE);
            fragment_season_details_watchlatest.setVisibility(View.VISIBLE);
        }


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


    public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.DataObjectHolder> {
        ArrayList<SeasonsBean> seasonsBeans;
        Context context;
        Boolean aBoolean;
        String mAirtime = "";

        public EpisodeAdapter(Context context, ArrayList<SeasonsBean> seasonsBeans, Boolean aBoolean) {
            this.context = context;
            this.seasonsBeans = seasonsBeans;
            this.aBoolean = aBoolean;

        }


        @Override
        public EpisodeAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.season_episode_layout, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(EpisodeAdapter.DataObjectHolder holder, final int position) {
            holder.seasons_episodes_name.setText(seasonsBeans.get(position).getName());

            try {
                mAirtime = seasonsBeans.get(position).getAir_date();
                if (!mAirtime.equals("null") && !mAirtime.equals("")) {
                    String[] separated = mAirtime.split("-");
                    String part1 = separated[0];
                    String part2 = separated[1];
                    String part3 = separated[2];


                    switch (part2) {
                        case "01":
                            holder.seasons_episodes_date.setText("(" + "Jan. " + part3 + ", " + part1 + ")");
                            break;
                        case "02":
                            holder.seasons_episodes_date.setText("(" + "Feb. " + part3 + ", " + part1 + ")");
                            break;
                        case "03":
                            holder.seasons_episodes_date.setText("(" + "Mar. " + part3 + ", " + part1 + ")");
                            break;
                        case "04":
                            holder.seasons_episodes_date.setText("(" + "Apr. " + part3 + ", " + part1 + ")");
                            break;
                        case "05":
                            holder.seasons_episodes_date.setText("(" + "May. " + part3 + ", " + part1 + ")");
                            break;
                        case "06":
                            holder.seasons_episodes_date.setText("(" + "Jun. " + part3 + ", " + part1 + ")");
                            break;
                        case "07":
                            holder.seasons_episodes_date.setText("(" + "Jul. " + part3 + ", " + part1 + ")");
                            break;
                        case "08":
                            holder.seasons_episodes_date.setText("(" + "Aug. " + part3 + ", " + part1 + ")");
                            break;
                        case "09":
                            holder.seasons_episodes_date.setText("(" + "Sep. " + part3 + ", " + part1 + ")");
                            break;
                        case "10":
                            holder.seasons_episodes_date.setText("(" + "Oct. " + part3 + ", " + part1 + ")");
                            break;
                        case "11":
                            holder.seasons_episodes_date.setText("(" + "Nov. " + part3 + ", " + part1 + ")");
                            break;
                        case "12":
                            holder.seasons_episodes_date.setText("(" + "Dec. " + part3 + ", " + part1 + ")");
                            break;


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            holder.seasons_episodes_poster.loadImage(seasonsBeans.get(position).getPoster_url());

            if (aBoolean) {
                holder.episode_free_textview.setVisibility(View.VISIBLE);
            }

            holder.seasons_episodes_poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image_url = seasonsBeans.get(position).getPoster_url();
                    mEpisodeId = seasonsBeans.get(position).getId();
                    HomeActivity.episodeId = seasonsBeans.get(position).getId();
                    HomeActivity.selecetd_season = seasonsBeans.get(position).getSeason_id();
                    HomeActivity.Selecteddetails = Constants.DEATILS_SUBCONTENT;
                    FragmentMovieMain.mProgressBar(true);
                    new EpisodeDetailsLoading().execute();
                }
            });
        }

        @Override
        public int getItemCount() {
            return seasonsBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView seasons_episodes_poster;
            TextView seasons_episodes_name;
            TextView seasons_episodes_date;
            TextView episode_free_textview;

            public DataObjectHolder(View itemView) {
                super(itemView);
                seasons_episodes_poster = (DynamicImageView) itemView.findViewById(R.id.seasons_episodes_poster);
                seasons_episodes_name = (TextView) itemView.findViewById(R.id.seasons_episodes_name);
                seasons_episodes_date = (TextView) itemView.findViewById(R.id.seasons_episodes_date);
                episode_free_textview = (TextView) itemView.findViewById(R.id.episode_free_textview);

                Utilities.setViewFocus(getActivity(), seasons_episodes_poster);
            }
        }
    }


    public class SeasonsAndEpisodesAdapter extends RecyclerView.Adapter<SeasonsAndEpisodesAdapter.DataObjectHolder> {
        ArrayList<SeasonsBean> seasonsBeans;
        Context context;
        String mAirtime = "";

        public SeasonsAndEpisodesAdapter(Context context, ArrayList<SeasonsBean> seasonsBeans) {
            this.context = context;
            this.seasonsBeans = seasonsBeans;

        }

        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.season_layout, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(DataObjectHolder holder, final int position) {

            try {
                holder.seasons_episodes_name.setText(seasonsBeans.get(position).getName());
                holder.seasons_episodes_poster.loadImage(seasonsBeans.get(position).getPoster_url());

                if (!mAirtime.equals("null") && !mAirtime.equals("")) {
                    mAirtime = seasonsBeans.get(position).getAir_date();
                    String[] separated = mAirtime.split("-");
                    String part1 = separated[0];
                    String part2 = separated[1];
                    String part3 = separated[2];


                    switch (part2) {
                        case "01":
                            holder.seasons_episodes_date.setText("(" + "Jan. " + part3 + ", " + part1 + ")");
                            break;
                        case "02":
                            holder.seasons_episodes_date.setText("(" + "Feb. " + part3 + ", " + part1 + ")");
                            break;
                        case "03":
                            holder.seasons_episodes_date.setText("(" + "Mar. " + part3 + ", " + part1 + ")");
                            break;
                        case "04":
                            holder.seasons_episodes_date.setText("(" + "Apr. " + part3 + ", " + part1 + ")");
                            break;
                        case "05":
                            holder.seasons_episodes_date.setText("(" + "May. " + part3 + ", " + part1 + ")");
                            break;
                        case "06":
                            holder.seasons_episodes_date.setText("(" + "Jun. " + part3 + ", " + part1 + ")");
                            break;
                        case "07":
                            holder.seasons_episodes_date.setText("(" + "Jul. " + part3 + ", " + part1 + ")");
                            break;
                        case "08":
                            holder.seasons_episodes_date.setText("(" + "Aug. " + part3 + ", " + part1 + ")");
                            break;
                        case "09":
                            holder.seasons_episodes_date.setText("(" + "Sep. " + part3 + ", " + part1 + ")");
                            break;
                        case "10":
                            holder.seasons_episodes_date.setText("(" + "Oct. " + part3 + ", " + part1 + ")");
                            break;
                        case "11":
                            holder.seasons_episodes_date.setText("(" + "Nov. " + part3 + ", " + part1 + ")");
                            break;
                        case "12":
                            holder.seasons_episodes_date.setText("(" + "Dec. " + part3 + ", " + part1 + ")");
                            break;


                    }
                }


                holder.seasons_episodes_poster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HomeActivity.Selecteddetails = Constants.DEATILS_SUBCONTENT;

                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                        image_url = seasonsBeans.get(position).getPoster_url();
                        season_name = seasonsBeans.get(position).getName();
                        mSpinnerClicked = false;
                        HomeActivity.mSeriesSeason = seasonsBeans.get(position).getName();
                        HomeActivity.toolbarMainContent = HomeActivity.mMovieorSeriesName;
                        HomeActivity.toolbarSubContent = seasonsBeans.get(position).getName();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        HomeActivity.is_click = false;
                        isSpinnerClicked = position;
                        choose_your_episode.setSelection(isSpinnerClicked);
                        isClicked = true;
                        mShowSpinner = true;
                        mEpisodeId = seasonsBeans.get(position).getId();
                        Log.d("seasonid", ":::" + mEpisodeId);
                        HomeActivity.selecetd_season = mEpisodeId;
                        HomeActivity.episodeId = seasonsBeans.get(position).getId();


                        if (HomeActivity.mfreeorall == 1) {
                            new FreeSeasonEpisodeLoading().execute();
                        } else {
                            FragmentMovieMain.mProgressBar(true);
                            new EpisodeLoading().execute();
                        }


                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return seasonsBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {

            DynamicImageView seasons_episodes_poster;
            TextView seasons_episodes_name;
            TextView seasons_episodes_date;

            public DataObjectHolder(View itemView) {
                super(itemView);
                seasons_episodes_poster = (DynamicImageView) itemView.findViewById(R.id.seasons_episodes_poster);
                seasons_episodes_name = (TextView) itemView.findViewById(R.id.seasons_episodes_name);
                seasons_episodes_date = (TextView) itemView.findViewById(R.id.seasons_episodes_date);

                Utilities.setViewFocus(getActivity(), seasons_episodes_poster);

            }
        }
    }


    public void setEpisodeList(JSONArray m_jsonDemandListItemsdata) {
        try {
            mEpisodeBeans.clear();
            int id = 0;
            String name = "", type = "", description = "", air_date = "", poster_url = "";
            int season_id = 0, season_number = 0;
            for (int i = 0; i < m_jsonDemandListItemsdata.length(); i++) {
                JSONObject demandMenuItem = m_jsonDemandListItemsdata.getJSONObject(i);
                if (demandMenuItem.has("name")) {
                    name = demandMenuItem.getString("name");
                }
                if (demandMenuItem.has("id")) {
                    id = demandMenuItem.getInt("id");
                }
                if (demandMenuItem.has("description")) {
                    description = demandMenuItem.getString("description");
                }
                if (demandMenuItem.has("poster_url")) {
                    poster_url = demandMenuItem.getString("poster_url");
                }
                if (demandMenuItem.has("air_date")) {
                    air_date = demandMenuItem.getString("air_date");
                }
                if (demandMenuItem.has("season_id")) {
                    season_id = demandMenuItem.getInt("season_id");
                }
                if (demandMenuItem.has("season_number")) {
                    season_number = demandMenuItem.getInt("season_number");
                }

                mEpisodeBeans.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));


            }


            episodeAdapter = new EpisodeAdapter(getActivity(), mEpisodeBeans, false);
            fragment_details_shows_free.setAdapter(episodeAdapter);

            HomeActivity.mLastEpisodeId = mEpisodeBeans.get(0).getId();
            HomeActivity.mImageUrl = mEpisodeBeans.get(0).getPoster_url();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListData(JSONArray m_jsonDemandListItemsdata) {
        try {
            seasonsBeans.clear();
            int id = 0;
            String name = "", type = "", description = "", air_date = "", poster_url = "";
            for (int i = 0; i < m_jsonDemandListItemsdata.length(); i++) {
                JSONObject demandMenuItem = m_jsonDemandListItemsdata.getJSONObject(i);
                if (demandMenuItem.has("name")) {
                    name = demandMenuItem.getString("name");
                }
                if (demandMenuItem.has("id")) {
                    id = demandMenuItem.getInt("id");
                }
                if (demandMenuItem.has("description")) {
                    description = demandMenuItem.getString("description");
                }
                if (demandMenuItem.has("poster_url")) {
                    poster_url = demandMenuItem.getString("poster_url");
                }
                if (demandMenuItem.has("air_date")) {
                    air_date = demandMenuItem.getString("air_date");
                }
                seasonsBeans.add(new SeasonsBean(id, description, air_date, poster_url, name, 0, 0));
            }


            seasonsAndEpisodesAdapter = new SeasonsAndEpisodesAdapter(getActivity(), seasonsBeans);
            fragment_details_shows_free.setAdapter(seasonsAndEpisodesAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class SeasonLoading extends AsyncTask<String, Object, Object> {


        private String name;
        int id;

        @Override
        protected void onPreExecute() {
            try {
                stringArrayList.clear();
                integerArrayList.clear();



               /* FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_list, dialog_fragment);
                network_fragmentTransaction.commit();*/

              /*  FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction .replace(R.Seasonid.fragment_tvshow_bydecade_content, dialog_fragment);
                fragmentTransaction .commit();*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                m_jsonSeasons = JSONRPCAPI.getShowSeasons(nIndex);
                if (m_jsonSeasons == null) return null;
                Log.d("SHOW ID::==>", ">>>show" + nIndex);
                Log.d("NetworkList::", "Selected sesons list::" + m_jsonSeasons);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {

              /*  if (m_jsonSeasons != null) {
                    stringArrayList.add("All Episodes");
                    integerArrayList.add(0);
                    for (int i = 0; i < m_jsonSeasons.length(); i++) {
                        JSONObject demandMenuItem = m_jsonSeasons.getJSONObject(i);
                        if (demandMenuItem.has("name")) {
                            name = demandMenuItem.getString("name");
                        }
                        if (demandMenuItem.has("id")) {
                            id = demandMenuItem.getInt("id");
                        }
                        stringArrayList.add(name);

                        integerArrayList.add(id);
                    }

                    adapter.notifyDataSetChanged();
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public class FreeEpisodeLoading extends AsyncTask<Object, Object, Object> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressload_showsandseasons.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                int nIndex = 1;
                nIndex = Seasonid;
                if (mEpisodeId == 0) {
                    m_jsonFreeEpisode = JSONRPCAPI.getShowFreeEpisodes(nIndex);
                    if (m_jsonFreeEpisode == null) return null;
                    FreemEpisodeBeans.clear();
                    FreeAllmEpisodeBeans.clear();
                    int id = 0, season_id = 0, season_number = 0;
                    String name = "", type = "", description = "", air_date = "", poster_url = "";
                    ArrayList<SeasonsBean> Freelist = new ArrayList<>();
                    for (int i = 0; i < m_jsonFreeEpisode.length(); i++) {
                        JSONObject demandMenuItem = m_jsonFreeEpisode.getJSONObject(i);
                        if (demandMenuItem.has("name")) {
                            name = demandMenuItem.getString("name");
                        }
                        if (demandMenuItem.has("id")) {
                            id = demandMenuItem.getInt("id");
                        }
                        if (demandMenuItem.has("description")) {
                            description = demandMenuItem.getString("description");
                        }
                        if (demandMenuItem.has("poster_url")) {
                            poster_url = demandMenuItem.getString("poster_url");
                        }
                        if (demandMenuItem.has("air_date")) {
                            air_date = demandMenuItem.getString("air_date");
                        }
                        if (demandMenuItem.has("season_id")) {
                            season_id = demandMenuItem.getInt("season_id");
                        }
                        if (demandMenuItem.has("season_number")) {
                            season_number = demandMenuItem.getInt("season_number");
                        }
                        if (i < 4) {
                            Freelist.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));
                        } else {
                            FreemEpisodeBeans.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));
                        }
                        FreeAllmEpisodeBeans.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));

                    }
                    Log.d("NetworkList::", "Selected sesons list::" + m_jsonFreeEpisode);
                } else {
                    m_jsonFreeEpisode = JSONRPCAPI.getShowEpisodes(nIndex, mEpisodeId, HomeActivity.mfreeorall);
                    if (m_jsonFreeEpisode == null) return null;
                    Log.d("NetworkList::", "Selected sesons list::" + m_jsonFreeEpisode);
                    Log.d("SHOW ID::==>", ">>>season" + mEpisodeId);
                }

                m_jsonshowDetail = JSONRPCAPI.getShowDetail(nIndex);
                if (m_jsonshowDetail == null) return null;
                Log.d("m_jsonShowDetail ::", "::" + m_jsonshowDetail);
                m_jsonshowLinks = JSONRPCAPI.getShowLinks(nIndex, 0, 0);
                if (m_jsonshowLinks == null) return null;
                Log.d("m_jsonShowLinks::", "::" + m_jsonshowLinks);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressload_showsandseasons.setVisibility(View.GONE);
            try {
                if (m_jsonFreeEpisode != null && m_jsonFreeEpisode.length() > 0) {
                    setLayout = true;
                    setFreeEpisodeList(m_jsonFreeEpisode);
                    free_textView.setVisibility(View.VISIBLE);
                    layout_showsandseasons_mainLayout.setVisibility(View.VISIBLE);
                    FragmentMovieMain.mProgressBar(false);
                } else {
                    selecvtAllEpisodes();
                    layout_show_free.setVisibility(View.GONE);
                    free_textView.setVisibility(View.GONE);
                    if (HomeActivity.mfreeorall == 1) {

                        layout_showsandseasons_mainLayout.setVisibility(View.GONE);
                        FragmentMoviesRightContent.mShowWatchFree(true);

                    } else {
                        FragmentMoviesRightContent.mShowWatchFree(false);

                        FragmentMovieMain.mProgressBar(false);
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class FreeSeasonEpisodeLoading extends AsyncTask<Object, Object, Object> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FragmentMovieMain.mProgressBar(true);
        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                int nIndex = 1;
                nIndex = Seasonid;
                Log.d("::", ":::ahow" + nIndex + ":::" + HomeActivity.selecetd_season);

                m_jsonFreeEpisode = JSONRPCAPI.getFreeEpisodesBySeasons(nIndex, HomeActivity.selecetd_season);
                if (m_jsonFreeEpisode == null) return null;
                FreemEpisodeBeans.clear();
                FreeAllmEpisodeBeans.clear();
                int id = 0, season_id = 0, season_number = 0;
                String name = "", type = "", description = "", air_date = "", poster_url = "";
                ArrayList<SeasonsBean> Freelist = new ArrayList<>();
                for (int i = 0; i < m_jsonFreeEpisode.length(); i++) {
                    JSONObject demandMenuItem = m_jsonFreeEpisode.getJSONObject(i);
                    if (demandMenuItem.has("name")) {
                        name = demandMenuItem.getString("name");
                    }
                    if (demandMenuItem.has("id")) {
                        id = demandMenuItem.getInt("id");
                    }
                    if (demandMenuItem.has("description")) {
                        description = demandMenuItem.getString("description");
                    }
                    if (demandMenuItem.has("poster_url")) {
                        poster_url = demandMenuItem.getString("poster_url");
                    }
                    if (demandMenuItem.has("air_date")) {
                        air_date = demandMenuItem.getString("air_date");
                    }
                    if (demandMenuItem.has("season_id")) {
                        season_id = demandMenuItem.getInt("season_id");
                    }
                    if (demandMenuItem.has("season_number")) {
                        season_number = demandMenuItem.getInt("season_number");
                    }

                    FreemEpisodeBeans.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));
                    FreeAllmEpisodeBeans.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));

                }
                Log.d("NetworkList::", "Selected sesons list::" + m_jsonFreeEpisode);


                m_jsonshowDetail = JSONRPCAPI.getShowDetail(nIndex);
                if (m_jsonshowDetail == null) return null;
                Log.d("m_jsonShowDetail ::", "::" + m_jsonshowDetail);
                m_jsonshowLinks = JSONRPCAPI.getShowLinks(nIndex, 0, 0);
                if (m_jsonshowLinks == null) return null;
                Log.d("m_jsonShowLinks::", "::" + m_jsonshowLinks);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                FragmentMovieMain.mProgressBar(false);
                if (m_jsonFreeEpisode != null && m_jsonFreeEpisode.length() > 0) {
                    setLayout = true;
                    fragment_details_shows__episodes.setVisibility(View.GONE);
                    fragment_details_shows_seasons.setVisibility(View.GONE);
                    fragment_details_shows_free.setVisibility(View.VISIBLE);
                    show_free_left.setVisibility(View.VISIBLE);
                    show_free_top.setVisibility(View.VISIBLE);
                    all_episodes_left.setVisibility(View.VISIBLE);
                    all_episodes_bottom.setVisibility(View.VISIBLE);
                    all_seasons_bottom.setVisibility(View.VISIBLE);
                    show_free_bottom.setVisibility(View.GONE);
                    all_episodes_top.setVisibility(View.GONE);
                    all_seasons_left.setVisibility(View.GONE);
                    all_seasons_top.setVisibility(View.GONE);
                    all_seasons_right.setVisibility(View.GONE);
                    layout_show_free.setVisibility(View.VISIBLE);
                    setFreeSeasonEpisodeList(m_jsonFreeEpisode);
                } else {
                    if (HomeActivity.mfreeorall != 1) {
                        layout_show_free.setVisibility(View.GONE);
                        selecvtAllEpisodes();
                        if (m_jsonEpisodes != null) {
                            setEpisodeList(m_jsonEpisodes);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void setFreeEpisodeList(JSONArray m_jsonSeasons) {
        try {
            FreemEpisodeBeans.clear();
            FreeAllmEpisodeBeans.clear();
            int id = 0, season_id = 0, season_number = 0;
            String name = "", type = "", description = "", air_date = "", poster_url = "";
            ArrayList<SeasonsBean> Freelist = new ArrayList<>();
            Freelist.clear();
            for (int i = 0; i < m_jsonSeasons.length(); i++) {
                JSONObject demandMenuItem = m_jsonSeasons.getJSONObject(i);
                if (demandMenuItem.has("name")) {
                    name = demandMenuItem.getString("name");
                }
                if (demandMenuItem.has("id")) {
                    id = demandMenuItem.getInt("id");
                }
                if (demandMenuItem.has("description")) {
                    description = demandMenuItem.getString("description");
                }
                if (demandMenuItem.has("poster_url")) {
                    poster_url = demandMenuItem.getString("poster_url");
                }
                if (demandMenuItem.has("air_date")) {
                    air_date = demandMenuItem.getString("air_date");
                }
                if (demandMenuItem.has("season_id")) {
                    season_id = demandMenuItem.getInt("season_id");
                }
                if (demandMenuItem.has("season_number")) {
                    season_number = demandMenuItem.getInt("season_number");
                }
                if (i < 4) {
                    Freelist.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));
                } else {
                    FreemEpisodeBeans.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));
                }
                FreeAllmEpisodeBeans.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));

            }
            if (setLayout) {
                dialog_FreemEpisodeBeans.clear();
                dialog_FreemEpisodeBeans.addAll(FreeAllmEpisodeBeans);
                if (Freelist.size() > 0) {
                    free_textView.setVisibility(View.VISIBLE);
                    addlayouts(dynamic_horzontal_list_layout, Freelist);
                } else {
                    free_textView.setVisibility(View.GONE);
                }

            }

            if (FreemEpisodeBeans.size() > 0) {
                layout_show_free.setVisibility(View.VISIBLE);


                fragment_details_shows__episodes.setVisibility(View.GONE);
                fragment_details_shows_seasons.setVisibility(View.GONE);
                fragment_details_shows_free.setVisibility(View.VISIBLE);
                show_free_left.setVisibility(View.VISIBLE);
                show_free_top.setVisibility(View.VISIBLE);
                all_episodes_left.setVisibility(View.VISIBLE);
                all_episodes_bottom.setVisibility(View.VISIBLE);
                all_seasons_bottom.setVisibility(View.VISIBLE);
                show_free_bottom.setVisibility(View.GONE);
                all_episodes_top.setVisibility(View.GONE);
                all_seasons_left.setVisibility(View.GONE);
                all_seasons_top.setVisibility(View.GONE);
                all_seasons_right.setVisibility(View.GONE);
                show_free_title.setTypeface(OpenSans_Bold);
                all_episodes_title.setTypeface(OpenSans_Regular);
                all_seasons_title.setTypeface(OpenSans_Regular);


                episodeAdapter = new EpisodeAdapter(getActivity(), FreemEpisodeBeans, true);
                fragment_details_shows_free.setAdapter(episodeAdapter);
            } else {
                if (HomeActivity.mfreeorall == 1) {
                    fragment_details_shows__episodes.setVisibility(View.GONE);
                    fragment_details_shows_seasons.setVisibility(View.GONE);
                    fragment_details_shows_free.setVisibility(View.GONE);
                    show_free_left.setVisibility(View.GONE);
                    show_free_top.setVisibility(View.GONE);
                    all_episodes_left.setVisibility(View.GONE);
                    all_episodes_bottom.setVisibility(View.GONE);
                    all_seasons_bottom.setVisibility(View.GONE);
                    show_free_bottom.setVisibility(View.GONE);
                    all_episodes_top.setVisibility(View.GONE);
                    all_seasons_left.setVisibility(View.VISIBLE);
                    all_seasons_top.setVisibility(View.VISIBLE);
                    all_seasons_right.setVisibility(View.VISIBLE);
                    layout_show_free.setVisibility(View.GONE);
                    show_free_title.setTypeface(OpenSans_Regular);
                    all_episodes_title.setTypeface(OpenSans_Regular);
                    all_seasons_title.setTypeface(OpenSans_Bold);

                }else{
                    layout_show_free.setVisibility(View.GONE);
                    fragment_details_shows__episodes.setVisibility(View.GONE);
                    fragment_details_shows_seasons.setVisibility(View.GONE);
                    fragment_details_shows_free.setVisibility(View.VISIBLE);
                    show_free_left.setVisibility(View.GONE);
                    show_free_top.setVisibility(View.GONE);
                    all_episodes_left.setVisibility(View.VISIBLE);
                    all_episodes_bottom.setVisibility(View.GONE);
                    all_seasons_bottom.setVisibility(View.VISIBLE);
                    show_free_bottom.setVisibility(View.GONE);
                    all_episodes_top.setVisibility(View.VISIBLE);
                    all_seasons_left.setVisibility(View.VISIBLE);
                    all_seasons_top.setVisibility(View.GONE);
                    all_seasons_right.setVisibility(View.GONE);
                }



            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFreeSeasonEpisodeList(JSONArray m_jsonSeasons) {
        try {
            FreeAllmEpisodeBeans.clear();
            int id = 0, season_id = 0, season_number = 0;
            String name = "", type = "", description = "", air_date = "", poster_url = "";
            ArrayList<SeasonsBean> Freelist = new ArrayList<>();
            Freelist.clear();
            for (int i = 0; i < m_jsonSeasons.length(); i++) {
                JSONObject demandMenuItem = m_jsonSeasons.getJSONObject(i);
                if (demandMenuItem.has("name")) {
                    name = demandMenuItem.getString("name");
                }
                if (demandMenuItem.has("id")) {
                    id = demandMenuItem.getInt("id");
                }
                if (demandMenuItem.has("description")) {
                    description = demandMenuItem.getString("description");
                }
                if (demandMenuItem.has("poster_url")) {
                    poster_url = demandMenuItem.getString("poster_url");
                }
                if (demandMenuItem.has("air_date")) {
                    air_date = demandMenuItem.getString("air_date");
                }
                if (demandMenuItem.has("season_id")) {
                    season_id = demandMenuItem.getInt("season_id");
                }
                if (demandMenuItem.has("season_number")) {
                    season_number = demandMenuItem.getInt("season_number");
                }

                FreeAllmEpisodeBeans.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));

            }
            episodeAdapter = new EpisodeAdapter(getActivity(), FreemEpisodeBeans, true);
            fragment_details_shows_free.setAdapter(episodeAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class EpisodeLoading extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            try {
                int nIndex = 1;
                nIndex = Seasonid;
                if (mEpisodeId == 0) {
                    dialog_AllmEpisodeBeans.clear();
                    m_jsonEpisodes = JSONRPCAPI.getShowAllEpisodes(nIndex, HomeActivity.mfreeorall);
                    if (m_jsonEpisodes == null) return null;
                    Log.d("NetworkList::", "Selected sesons list::" + m_jsonEpisodes);
                    mEpisodeBeans.clear();
                    int id = 0, season_id = 0, season_number = 0;
                    String name = "", type = "", description = "", air_date = "", poster_url = "";
                    for (int i = 0; i < m_jsonEpisodes.length(); i++) {
                        JSONObject demandMenuItem = m_jsonEpisodes.getJSONObject(i);
                        if (demandMenuItem.has("name")) {
                            name = demandMenuItem.getString("name");
                        }
                        if (demandMenuItem.has("id")) {
                            id = demandMenuItem.getInt("id");
                        }
                        if (demandMenuItem.has("description")) {
                            description = demandMenuItem.getString("description");
                        }
                        if (demandMenuItem.has("poster_url")) {
                            poster_url = demandMenuItem.getString("poster_url");
                        }
                        if (demandMenuItem.has("air_date")) {
                            air_date = demandMenuItem.getString("air_date");
                        }
                        if (demandMenuItem.has("season_id")) {
                            season_id = demandMenuItem.getInt("season_id");
                        }
                        if (demandMenuItem.has("season_number")) {
                            season_number = demandMenuItem.getInt("season_number");
                        }

                        mEpisodeBeans.add(new SeasonsBean(id, description, air_date, poster_url, name, season_id, season_number));


                    }
                    if (setepisode) {
                        dialog_AllmEpisodeBeans.addAll(mEpisodeBeans);
                        setepisode = false;
                    }

                } else {
                    m_jsonEpisodes = JSONRPCAPI.getShowEpisodes(nIndex, mEpisodeId, HomeActivity.mfreeorall);
                    if (m_jsonEpisodes == null) return null;
                    Log.d("NetworkList::", "Selected sesons list::" + m_jsonEpisodes);
                    Log.d("SHOW ID::==>", ">>>season" + mEpisodeId);
                }

                m_jsonshowDetail = JSONRPCAPI.getShowDetail(nIndex);
                if (m_jsonshowDetail == null) return null;
                Log.d("m_jsonShowDetail ::", "::" + m_jsonshowDetail);
                m_jsonshowLinks = JSONRPCAPI.getShowLinks(nIndex, 0, 0);
                Log.d("m_jsonShowLinks::", "::" + m_jsonshowLinks);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            try {
                /*FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_list, dialog_fragment);
                network_fragmentTransaction.commit();*/
              /*  FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction .replace(R.Seasonid.fragment_tvshow_bydecade_content, dialog_fragment);
                fragmentTransaction .commit();*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(Object params) {
            try {

                try {
                    if (m_jsonEpisodes.length() > 0) {

                        JSONObject demandMenuItem = m_jsonEpisodes.getJSONObject(0);

                        if (demandMenuItem.has("id")) {
                            int id = demandMenuItem.getInt("id");
                            HomeActivity.mLastEpisodeId = id;
                        }

                        if (FreemEpisodeBeans != null && FreemEpisodeBeans.size() == 0) {
                            choose_your_episode.setVisibility(View.GONE);
                            layout_showsandseasons_mainLayout.setVisibility(View.VISIBLE);
                            fragment_details_shows__episodes.setVisibility(View.GONE);
                            fragment_details_shows_seasons.setVisibility(View.GONE);
                            fragment_details_shows_free.setVisibility(View.VISIBLE);
                            show_free_left.setVisibility(View.GONE);
                            show_free_top.setVisibility(View.GONE);
                            all_episodes_left.setVisibility(View.VISIBLE);
                            all_episodes_bottom.setVisibility(View.GONE);
                            all_seasons_bottom.setVisibility(View.VISIBLE);
                            show_free_bottom.setVisibility(View.GONE);
                            all_episodes_top.setVisibility(View.VISIBLE);
                            all_seasons_left.setVisibility(View.VISIBLE);
                            all_seasons_top.setVisibility(View.GONE);
                            all_seasons_right.setVisibility(View.GONE);
                            show_free_title.setTypeface(OpenSans_Regular);
                            all_episodes_title.setTypeface(OpenSans_Bold);
                            all_seasons_title.setTypeface(OpenSans_Regular);
                            if (m_jsonEpisodes != null) {
                                setEpisodeList(m_jsonEpisodes);
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "No Episodes Found", Toast.LENGTH_SHORT);
                        layout_showsandseasons_mainLayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentMovieMain.mProgressBar(false);
                if (!mSpinnerClicked) {


                    choose_your_episode.setVisibility(View.GONE);

                    fragment_details_shows__episodes.setVisibility(View.VISIBLE);
                    fragment_details_shows_seasons.setVisibility(View.GONE);
                    show_free_left.setVisibility(View.GONE);
                    show_free_top.setVisibility(View.GONE);
                    all_episodes_left.setVisibility(View.VISIBLE);
                    all_episodes_bottom.setVisibility(View.GONE);
                    all_seasons_bottom.setVisibility(View.VISIBLE);
                    show_free_bottom.setVisibility(View.VISIBLE);
                    all_episodes_top.setVisibility(View.VISIBLE);
                    all_seasons_left.setVisibility(View.VISIBLE);
                    all_seasons_top.setVisibility(View.GONE);
                    all_seasons_right.setVisibility(View.GONE);
                    show_free_title.setTypeface(OpenSans_Regular);
                    all_episodes_title.setTypeface(OpenSans_Bold);
                    all_seasons_title.setTypeface(OpenSans_Regular);
//                    FragmentMovieMain.scrollViewUp();

                    if(HomeActivity.getmFragmentMovieMain()!=null){
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentMoviesLeftContent fragmentMoviesLeftContent = FragmentMoviesLeftContent.newInstance(m_jsonshowDetail.toString(), image_url);
                        fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_list, fragmentMoviesLeftContent);
                        fragmentTransaction.commit();

                    }
                    if(HomeActivity.getmFragmentMovieMain()!=null){
                        FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentMoviesRightContent network_listfragment = FragmentMoviesRightContent.newInstance(m_jsonshowDetail.toString(), m_jsonshowLinks.toString(), season_name);
                        network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_content, network_listfragment);
                        network_fragmentTransaction.commit();
                    }


                    if (m_jsonEpisodes != null) {
                        setEpisodeList(m_jsonEpisodes);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public class EpisodeDetailsLoading extends AsyncTask<Object, Object, Object> {


        @Override
        protected Object doInBackground(Object... params) {
            try {
                int nIndex = 1;

                nIndex = Seasonid;
                m_jsonEpisodeDeatils = JSONRPCAPI.getEpisodeDetail(mEpisodeId);
                if (m_jsonEpisodeDeatils == null) return null;
                Log.d("NetworkList::", "Selected episode list::" + m_jsonEpisodeDeatils);
                Log.d("SHOW ID::==>", ">>>episodeid" + mEpisodeId);
                m_jsonshowDetail = JSONRPCAPI.getShowDetail(nIndex);
                if (m_jsonshowDetail == null) return null;
                Log.d("m_jsonShowDetail ::", "::" + m_jsonshowDetail);
                m_jsonshowLinks = JSONRPCAPI.getShowLinks(nIndex, HomeActivity.selecetd_season, mEpisodeId);
                if (m_jsonshowLinks == null) return null;
                Log.d("parameters::", "::" + nIndex + ",.,." + HomeActivity.selecetd_season + ",.,.," + mEpisodeId);
                Log.d("m_jsonShowLinks::", "::" + m_jsonshowLinks);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            try {
                if(HomeActivity.getmFragmentMovieMain()!=null){
                    FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                    DialogFragment dialog_fragment = new DialogFragment();
                    network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_list, dialog_fragment);
                    network_fragmentTransaction.commit();
                }



              /*  FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction .replace(R.Seasonid.fragment_tvshow_bydecade_content, dialog_fragment);
                fragmentTransaction .commit();*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(Object params) {
            try {


               /* HomeActivity.mMovieOrSerial=Constants.SHOWS_DETAILS;
                FragmentTransaction fragmentTransactionShowSeasons = getFragmentManager().beginTransaction();
                FragmentShowSeasonsAndEpisodes fragmentShowSeasonsAndEpisodes = FragmentShowSeasonsAndEpisodes.newInstance(Constants.SHOWS_DETAILS, HomeActivity.showId);
                Log.d("SHOW ID::==>",">>>show:"+HomeActivity.showId);
                fragmentTransactionShowSeasons.replace(R.id.fragment_shows_seasonsandepisodes, fragmentShowSeasonsAndEpisodes);
                fragmentTransactionShowSeasons.commit();*/

                try {
                    if(HomeActivity.getmFragmentMovieMain()!=null){
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentMoviesLeftContent fragmentMoviesLeftContent = FragmentMoviesLeftContent.newInstance(m_jsonshowDetail.toString(), image_url);
                        fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_list, fragmentMoviesLeftContent);
                        fragmentTransaction.commit();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
              /*  FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentMoviesRightContent network_listfragment = FragmentMoviesRightContent.newInstance(m_jsonshowDetail.toString(), m_jsonshowLinks.toString());
                network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_content, network_listfragment);
                network_fragmentTransaction.commit();*/
                try {
                    if(HomeActivity.getmFragmentMovieMain()!=null){
                        FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentEpisodeorMovieDetails network_listfragment = FragmentEpisodeorMovieDetails.newInstance(m_jsonEpisodeDeatils.toString(), m_jsonshowLinks.toString(), "");
                        network_fragmentTransaction.replace(R.id.fragment_tvshow_bydecade_content, network_listfragment);
                        network_fragmentTransaction.commit();

                        ((HomeActivity) getActivity()).displayapps(true, m_jsonshowLinks.toString());
                    }





                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentMovieMain.mProgressBar(false);
                FragmentMovieMain.scrollViewUp();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void addlayouts(final LinearLayout dynamic_horizontalViews_layout, final ArrayList<SeasonsBean> listData) {
        final View itemlayout = (View) inflate.inflate(R.layout.episode_horizontalview, null);
        final HorizontalScrollView horizontal_listview = (HorizontalScrollView) itemlayout.findViewById(R.id.horizontal_listview);
        final LinearLayout recycler_layout = (LinearLayout) itemlayout.findViewById(R.id.recycler_layout);
        final LinearLayout image_view = (LinearLayout) itemlayout.findViewById(R.id.image_view);

        final LinearLayoutManager linear_layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        int spanCount = listData.size(); // 3 columns
        int spacing = 20; // 50px

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
                        final View itemmlayout = (View) inflate.inflate(R.layout.episode_hrizontal_list_item, null);

                        DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);
                        TextView episode_name_textView = (TextView) itemmlayout.findViewById(R.id.episode_name_textView);
                        TextView episode_free_textview = (TextView) itemmlayout.findViewById(R.id.episode_free_textview);
                        TextView seasons_episodes_date = (TextView) itemmlayout.findViewById(R.id.seasons_episodes_date);
                        episode_free_textview.setVisibility(View.VISIBLE);
                        RelativeLayout relative = (RelativeLayout) itemmlayout.findViewById(R.id.relative);
                        LinearLayout.LayoutParams vp;
                        RelativeLayout.LayoutParams rp;

                        vp = new LinearLayout.LayoutParams((width - 60) / 4, LinearLayout.LayoutParams.WRAP_CONTENT);
                        rp = new RelativeLayout.LayoutParams((width - 60) / 4, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        relative.getLayoutParams().width = (width - 60) / 4;

                        if (l != 0) {
                            vp.setMargins(20, 0, 0, 0);

                            relative.setPadding(20, 0, 0, 0);
                        } else {
                            relative.setPadding(0, 0, 20, 0);
                        }
                        final String image_url = listData.get(l).getPoster_url();
                        Log.d("image_url::", "image_url" + image_url);
                        horizontal_imageView.loadImage(image_url);
                        episode_name_textView.setText(listData.get(l).getName());
                        try {
                            String mAirtime = listData.get(l).getAir_date();
                            if (!mAirtime.equals("null") && !mAirtime.equals("")) {
                                String[] separated = mAirtime.split("-");
                                String part1 = separated[0];
                                String part2 = separated[1];
                                String part3 = separated[2];


                                switch (part2) {
                                    case "01":

                                        seasons_episodes_date.setText("(" + "Jan. " + part3 + ", " + part1 + ")");

                                        break;
                                    case "02":
                                        seasons_episodes_date.setText("(" + "Feb. " + part3 + ", " + part1 + ")");
                                        break;
                                    case "03":
                                        seasons_episodes_date.setText("(" + "Mar. " + part3 + ", " + part1 + ")");
                                        break;
                                    case "04":
                                        seasons_episodes_date.setText("(" + "Apr. " + part3 + ", " + part1 + ")");
                                        break;
                                    case "05":
                                        seasons_episodes_date.setText("(" + "May. " + part3 + ", " + part1 + ")");
                                        break;
                                    case "06":
                                        seasons_episodes_date.setText("(" + "Jun. " + part3 + ", " + part1 + ")");
                                        break;
                                    case "07":
                                        seasons_episodes_date.setText("(" + "Jul. " + part3 + ", " + part1 + ")");
                                        break;
                                    case "08":
                                        seasons_episodes_date.setText("(" + "Aug. " + part3 + ", " + part1 + ")");
                                        break;
                                    case "09":
                                        seasons_episodes_date.setText("(" + "Sep. " + part3 + ", " + part1 + ")");
                                        break;
                                    case "10":
                                        seasons_episodes_date.setText("(" + "Oct. " + part3 + ", " + part1 + ")");
                                        break;
                                    case "11":
                                        seasons_episodes_date.setText("(" + "Nov. " + part3 + ", " + part1 + ")");
                                        break;
                                    case "12":
                                        seasons_episodes_date.setText("(" + "Dec. " + part3 + ", " + part1 + ")");
                                        break;


                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //horizontal_imageView.setLayoutParams(vp);

                        episode_name_textView.setLayoutParams(vp);
                        seasons_episodes_date.setLayoutParams(vp);
                        final int finalL = l;
                        itemmlayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("subscription", "id" + listData.get(finalL).getId());
                                FragmentShowSeasonsAndEpisodes.image_url = image_url;
                                mEpisodeId = listData.get(finalL).getId();
                                HomeActivity.episodeId = listData.get(finalL).getId();
                                HomeActivity.selecetd_season = listData.get(finalL).getSeason_id();
                                HomeActivity.Selecteddetails = Constants.DEATILS_SUBCONTENT;
                                FragmentMovieMain.mProgressBar(true);
                                new EpisodeDetailsLoading().execute();
                            }
                        });
                        itemmlayout.setFocusable(true);
                        Utilities.setViewFocus(getActivity(), itemmlayout);
                        image_view.addView(itemmlayout);

                    }


                    Log.d("layoutwidth::", "width" + width);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamic_horizontalViews_layout.addView(itemlayout);
    }


    public void detailsFragmentTransaction(int id) {
        mEpisodeId = id;
        FragmentMovieMain.mProgressBar(true);
        new EpisodeDetailsLoading().execute(id);
    }

    private void setAnalyticreport(String mainString,String SubString1,String SubString2){
        try {
            if (HomeActivity.isPayperview.equals("Pay Per View")) {
                if(TextUtils.isEmpty(mainString)){
                    Utilities.setAnalytics(mTracker,"PayPerView");
                }else if(TextUtils.isEmpty(SubString1)){
                    Utilities.setAnalytics(mTracker,"PayPerView-"+mainString);
                }else if(TextUtils.isEmpty(SubString2)){
                    Utilities.setAnalytics(mTracker,"PayPerView-"+mainString+"-"+SubString1);
                }else{
                    Utilities.setAnalytics(mTracker,"PayPerView-"+mainString+"-"+SubString1+"-"+SubString2);
                }

            } else if (HomeActivity.isPayperview.equals("On-Demand")) {
                if(TextUtils.isEmpty(mainString)){
                    Utilities.setAnalytics(mTracker,"OnDemand");
                }else if(TextUtils.isEmpty(SubString1)){
                    Utilities.setAnalytics(mTracker,"OnDemand-"+mainString);
                }else if(TextUtils.isEmpty(SubString2)){
                    Utilities.setAnalytics(mTracker,"OnDemand-"+mainString+"-"+SubString1);
                }else{
                    Utilities.setAnalytics(mTracker,"OnDemand-"+mainString+"-"+SubString1+"-"+SubString2);
                }

            }else  {
                if(TextUtils.isEmpty(mainString)){
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent);
                }else if(TextUtils.isEmpty(SubString1)){
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent+"-"+mainString);
                }else if(TextUtils.isEmpty(SubString2)){
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent+"-"+mainString+"-"+SubString1);
                }else{
                    Utilities.setAnalytics(mTracker, HomeActivity.toolbarGridContent+"-"+mainString+"-"+SubString1+"-"+SubString2);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
