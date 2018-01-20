package com.selecttvlauncher.ui.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvlauncher.BeanClass.SeasonsBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentDialogShows extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView fragment_season_details_watchlatest;
    private LinearLayout layout_show_free;
    private LinearLayout layout_all_episodes;
    private TextView show_free_title, all_episodes_title;
    private RecyclerView fragment_details_shows_free, fragment_details_shows__episodes;
    View show_free_left,
            show_free_top,
            show_free_bottom,
            all_episodes_top,
            all_episodes_left,
            all_episodes_bottom,
            all_episodes_right;


    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private GridLayoutManager mLayoutManager;
    private String image_url;
    private int mEpisodeId;
    private EpisodeAdapter episodeAdapter;
    private int Seasonid = 0;

    public FragmentDialogShows() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDialogShows.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDialogShows newInstance(String param1, String param2) {
        FragmentDialogShows fragment = new FragmentDialogShows();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_dialog_shows, container, false);
        fragment_season_details_watchlatest = (TextView) view.findViewById(R.id.fragment_season_details_watchlatest);
        layout_show_free = (LinearLayout) view.findViewById(R.id.layout_show_free);
        layout_all_episodes = (LinearLayout) view.findViewById(R.id.layout_all_episodes);
        show_free_title = (TextView) view.findViewById(R.id.show_free_title);
        all_episodes_title = (TextView) view.findViewById(R.id.all_episodes_title);
        fragment_details_shows_free = (RecyclerView) view.findViewById(R.id.fragment_details_shows_free);
        fragment_details_shows__episodes = (RecyclerView) view.findViewById(R.id.fragment_details_shows__episodes);

        show_free_left = (View) view.findViewById(R.id.show_free_left);
        show_free_top = (View) view.findViewById(R.id.show_free_top);
        show_free_bottom = (View) view.findViewById(R.id.show_free_bottom);
        all_episodes_top = (View) view.findViewById(R.id.all_episodes_top);
        all_episodes_left = (View) view.findViewById(R.id.all_episodes_left);
        all_episodes_bottom = (View) view.findViewById(R.id.all_episodes_bottom);
        all_episodes_right = (View) view.findViewById(R.id.all_episodes_right);
        text_font_typeface();
        fragment_season_details_watchlatest.setTypeface(OpenSans_Regular);
        show_free_title.setTypeface(OpenSans_Bold);
        all_episodes_title.setTypeface(OpenSans_Regular);

        mLayoutManager = new GridLayoutManager(getActivity(), 4);
        fragment_details_shows_free.setLayoutManager(mLayoutManager);
        int spanCount = 4; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;

        fragment_details_shows_free.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        if(FragmentShowSeasonsAndEpisodes.dialog_FreemEpisodeBeans.size()>0){
            episodeAdapter = new EpisodeAdapter(getActivity(), FragmentShowSeasonsAndEpisodes.dialog_FreemEpisodeBeans, true);
            fragment_details_shows_free.setAdapter(episodeAdapter);
            if(HomeActivity.mfreeorall==1){
                all_episodes_title.setVisibility(View.GONE);
                layout_all_episodes.setVisibility(View.GONE);
            }
        }else{
            if(HomeActivity.mfreeorall!=1){
                fragment_details_shows_free.setVisibility(View.GONE);
                show_free_title.setVisibility(View.GONE);
                fragment_details_shows_free.setVisibility(View.GONE);
                fragment_details_shows__episodes.setVisibility(View.GONE);
                show_free_left.setVisibility(View.GONE);
                show_free_top.setVisibility(View.GONE);
                all_episodes_left.setVisibility(View.VISIBLE);
                all_episodes_bottom.setVisibility(View.GONE);
                show_free_bottom.setVisibility(View.GONE);
                all_episodes_top.setVisibility(View.VISIBLE);
                all_episodes_right.setVisibility(View.VISIBLE);
                show_free_title.setTypeface(OpenSans_Regular);
                all_episodes_title.setTypeface(OpenSans_Bold);
                fragment_details_shows_free.setVisibility(View.VISIBLE);
                episodeAdapter = new EpisodeAdapter(getActivity(), FragmentShowSeasonsAndEpisodes.dialog_AllmEpisodeBeans, false);
                fragment_details_shows_free.setAdapter(episodeAdapter);
            }else{
                fragment_details_shows__episodes.setVisibility(View.GONE);

                fragment_details_shows_free.setVisibility(View.VISIBLE);
                show_free_left.setVisibility(View.VISIBLE);
                show_free_top.setVisibility(View.VISIBLE);
                all_episodes_left.setVisibility(View.VISIBLE);
                all_episodes_bottom.setVisibility(View.VISIBLE);
                all_episodes_title.setVisibility(View.GONE);
                show_free_bottom.setVisibility(View.GONE);
                all_episodes_top.setVisibility(View.GONE);
                all_episodes_right.setVisibility(View.GONE);
                show_free_title.setTypeface(OpenSans_Bold);
                all_episodes_title.setTypeface(OpenSans_Regular);
                layout_all_episodes.setVisibility(View.GONE);
                episodeAdapter = new EpisodeAdapter(getActivity(), FragmentShowSeasonsAndEpisodes.dialog_FreemEpisodeBeans, true);
                fragment_details_shows_free.setAdapter(episodeAdapter);
            }

        }

        layout_show_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fragment_details_shows__episodes.setVisibility(View.GONE);

                fragment_details_shows_free.setVisibility(View.VISIBLE);
                show_free_left.setVisibility(View.VISIBLE);
                show_free_top.setVisibility(View.VISIBLE);
                all_episodes_left.setVisibility(View.VISIBLE);
                all_episodes_bottom.setVisibility(View.VISIBLE);

                show_free_bottom.setVisibility(View.GONE);
                all_episodes_top.setVisibility(View.GONE);
                all_episodes_right.setVisibility(View.GONE);
                show_free_title.setTypeface(OpenSans_Bold);
                all_episodes_title.setTypeface(OpenSans_Regular);
                episodeAdapter = new EpisodeAdapter(getActivity(), FragmentShowSeasonsAndEpisodes.dialog_FreemEpisodeBeans, true);
                fragment_details_shows_free.setAdapter(episodeAdapter);

            }
        });

        layout_all_episodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fragment_details_shows__episodes.setVisibility(View.GONE);
                fragment_details_shows_free.setVisibility(View.VISIBLE);
                show_free_left.setVisibility(View.GONE);
                show_free_top.setVisibility(View.GONE);
                all_episodes_left.setVisibility(View.VISIBLE);
                all_episodes_bottom.setVisibility(View.GONE);
                show_free_bottom.setVisibility(View.VISIBLE);
                all_episodes_top.setVisibility(View.VISIBLE);
                all_episodes_right.setVisibility(View.VISIBLE);
                show_free_title.setTypeface(OpenSans_Regular);
                all_episodes_title.setTypeface(OpenSans_Bold);
                episodeAdapter = new EpisodeAdapter(getActivity(), FragmentShowSeasonsAndEpisodes.dialog_AllmEpisodeBeans, false);
                fragment_details_shows_free.setAdapter(episodeAdapter);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

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
        String mAirtime;

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
            holder.seasons_episodes_poster.loadImage(seasonsBeans.get(position).getPoster_url());
            mAirtime = seasonsBeans.get(position).getAir_date();

            Log.d("selecttv::","::mAirtime::"+mAirtime);

            if (!mAirtime.equals("null") && !mAirtime.equals("")) {
                String[] separated = mAirtime.split("-");
                String part1 = separated[0];
                String part2 = separated[1];
                String part3 = separated[2];


                switch (part2) {
                    case "01":

                        holder.seasons_episodes_date.setText("(" + "Jan." + part3 + "," + part1 + ")");

                        break;
                    case "02":
                        holder.seasons_episodes_date.setText("(" + "Feb." + part3 + "," + part1 + ")");
                        break;
                    case "03":
                        holder.seasons_episodes_date.setText("(" + "Mar." + part3 + "," + part1 + ")");
                        break;
                    case "04":
                        holder.seasons_episodes_date.setText("(" + "Apr." + part3 + "," + part1 + ")");
                        break;
                    case "05":
                        holder.seasons_episodes_date.setText("(" + "May." + part3 + "," + part1 + ")");
                        break;
                    case "06":
                        holder.seasons_episodes_date.setText("(" + "Jun." + part3 + "," + part1 + ")");
                        break;
                    case "07":
                        holder.seasons_episodes_date.setText("(" + "Jul." + part3 + "," + part1 + ")");
                        break;
                    case "08":
                        holder.seasons_episodes_date.setText("(" + "Aug." + part3 + "," + part1 + ")");
                        break;
                    case "09":
                        holder.seasons_episodes_date.setText("(" + "Sep." + part3 + "," + part1 + ")");
                        break;
                    case "10":
                        holder.seasons_episodes_date.setText("(" + "Oct." + part3 + "," + part1 + ")");
                        break;
                    case "11":
                        holder.seasons_episodes_date.setText("(" + "Nov." + part3 + "," + part1 + ")");
                        break;
                    case "12":
                        holder.seasons_episodes_date.setText("(" + "Dec." + part3 + "," + part1 + ")");
                        break;


                }
            }


            if (aBoolean) {
                holder.episode_free_textview.setVisibility(View.VISIBLE);
            }

            holder.seasons_episodes_poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image_url = seasonsBeans.get(position).getPoster_url();
                    mEpisodeId = seasonsBeans.get(position).getId();
                    Log.d("selecttv::",":::selected episode::"+mEpisodeId);
                    HomeActivity.episodeId = seasonsBeans.get(position).getId();
                    HomeActivity.selecetd_season=seasonsBeans.get(position).getSeason_id();
                    HomeActivity.Selecteddetails = Constants.DEATILS_SUBCONTENT;
                    ((HomeActivity) getActivity()).getFragmentDetailsDialog().closeDialog();
                    FragmentMovieMain.mProgressBar(true);
                    FragmentShowSeasonsAndEpisodes.image_url = image_url;
                    ((HomeActivity) getActivity()).getFragmentShowSeasonsAndEpisodes().detailsFragmentTransaction(mEpisodeId);
//                    new EpisodeDetailsLoading().execute();

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class EpisodeDetailsLoading extends AsyncTask<Object, Object, Object> {


        private JSONObject m_jsonEpisodeDeatils;
        private JSONObject m_jsonshowDetail;
        private JSONObject m_jsonshowLinks;

        @Override
        protected Object doInBackground(Object... params) {
            try {
                int nIndex = 1;

                nIndex = HomeActivity.showId;
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


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
