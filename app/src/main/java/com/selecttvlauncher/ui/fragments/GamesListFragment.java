package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.BeanClass.SideMenu;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class GamesListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentTransaction fragmentTransaction;
    private RecyclerView fragment_game_alllist_items;
    private LinearLayoutManager mLayoutManager;
    ArrayList<SideMenu> mOnDemandlist = new ArrayList<>();
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    private int mPrimeTimeSelectedItem;
    private ImageView fragment_game_prev_icon;
    private ProgressBar fragment_game_progress;
    private LinearLayout fragment_game_layout;
    private LinearLayout fragment_game_alllist_items_layout;
    private TextView fragment_game_switchtext_all;
    private TextView fragment_game_switchtext_free;
    private ImageView game_switch_image;
    private boolean hasData=false;;
    private Tracker mTracker;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GamesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GamesListFragment newInstance(String param1, String param2) {
        GamesListFragment fragment = new GamesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GamesListFragment() {
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
        View view=inflater.inflate(R.layout.fragment_games_list, container, false);
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        fragment_game_alllist_items = (RecyclerView) view.findViewById(R.id.fragment_game_alllist_items);
        fragment_game_prev_icon = (ImageView) view.findViewById(R.id.fragment_game_prev_icon);
        fragment_game_progress=(ProgressBar)view.findViewById(R.id.fragment_game_progress);
        fragment_game_layout = (LinearLayout) view.findViewById(R.id.fragment_game_layout);
        fragment_game_alllist_items_layout = (LinearLayout) view.findViewById(R.id.fragment_game_alllist_items_layout);
       /* fragment_game_switchtext_all=(TextView)view.findViewById(R.id.fragment_game_switchtext_all);
        fragment_game_switchtext_free=(TextView)view.findViewById(R.id.fragment_game_switchtext_free);
        game_switch_image=(ImageView)view.findViewById(R.id.game_switch_image);*/
       /* FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fragment_game_layout.getLayoutParams();
        params.setMargins(0, 20, 0, 0);
        fragment_game_layout.setLayoutParams(params);


        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) fragment_game_alllist_items_layout.getLayoutParams();
        params1.setMargins(32, 0, 0, 0);
        fragment_game_alllist_items_layout.setLayoutParams(params1);

        fragment_game_layout.setOrientation(LinearLayout.HORIZONTAL);
        game_switch_image.setVisibility(View.GONE);
        fragment_game_switchtext_all.setVisibility(View.GONE);
        fragment_game_switchtext_free.setVisibility(View.GONE);*/



        mLayoutManager = new LinearLayoutManager(getActivity());
        fragment_game_alllist_items.setLayoutManager(mLayoutManager);
        if(HomeActivity.m_jsonGameCourasals!=null&& HomeActivity.m_jsonGameCourasals.length()!=0)
        {
            hasData=false;
            for(int i = 0; i< HomeActivity.m_jsonGameCourasals.length(); i++){
                try {
                    JSONObject item_object= HomeActivity.m_jsonGameCourasals.getJSONObject(i);
                    JSONArray item_array=item_object.getJSONArray("items");
                    if(item_array.length()>0){
                        hasData=true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if(hasData){
                if(HomeActivity.getmGameMainFragment()!=null){
                    setListData(HomeActivity.m_jsonGameCourasals);
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    GamesDetailsFragment fragmentOnDemandAllContent = GamesDetailsFragment.newInstance(Constants.SHOWS, HomeActivity.m_jsonGameCourasals.toString());
                    fragmentTransaction.replace(R.id.fragment_games_pagerandlist, fragmentOnDemandAllContent);
                    fragmentTransaction.commit();
                }

            }else{

                setListDataGrid(HomeActivity.m_jsonGameCourasals);
            }


        }else
        {

            new LoadingGameSideMenuTask().execute();
        }


        fragment_game_prev_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    switch (GameMainFragment.sContent_position) {
                        case Constants.GAMES_CONTENT:

                            GameMainFragment.sContent_position = Constants.GAMES_SUBCONTENT;
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            FragmentSubscriptionsGrid fragmentSubscriptionsGrid = FragmentSubscriptionsGrid.newInstance(FragmentSubScriptionHorizontalList.list, String.valueOf(FragmentSubScriptionHorizontalList.mposition));
                            fragmentTransaction_list.replace(R.id.fragment_subscription_content, fragmentSubscriptionsGrid);
                            fragmentTransaction_list.commit();
                            break;
                        case Constants.GAMES_SUBCONTENT:
                            GameMainFragment.sContent_position = Constants.GAMES_MAINCONTENT;

                            if(HomeActivity.m_jsonGameCourasals!=null&& HomeActivity.m_jsonGameCourasals.length()!=0){
                                if(HomeActivity.getmGameMainFragment()!=null){
                                    setListData(HomeActivity.m_jsonGameCourasals);
                                    fragmentTransaction = getFragmentManager().beginTransaction();
                                    GamesDetailsFragment fragmentOnDemandAllContent = GamesDetailsFragment.newInstance(Constants.SHOWS, HomeActivity.m_jsonGameCourasals.toString());
                                    fragmentTransaction.replace(R.id.fragment_games_pagerandlist, fragmentOnDemandAllContent);
                                    fragmentTransaction.commit();
                                }


                            }else
                            {

                                new LoadingGameSideMenuTask().execute();
                            }
                            break;
                        case Constants.GAMES_MAINCONTENT:
                            HomeActivity.isPayperview = "";
                            HomeActivity.toolbarMainContent = "";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                            fragmentTransaction.commit();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    private class LoadingGameSideMenuTask extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            fragment_game_progress.setVisibility(View.VISIBLE);
            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected Object doInBackground(Object... params) {
            try {
                HomeActivity.m_jsonGameCourasals = JSONRPCAPI.getGamecarousels();
                if (   HomeActivity.m_jsonGameCourasals == null) return null;
                Log.d("m_jsonDemandListItems::", "::" + HomeActivity.m_jsonGameCourasals);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                fragment_game_progress.setVisibility(View.GONE);

                    if (HomeActivity.m_jsonGameCourasals != null && HomeActivity.m_jsonGameCourasals.length() > 0) {

                        hasData=false;
                        for(int i = 0; i< HomeActivity.m_jsonGameCourasals.length(); i++){
                            try {
                                JSONObject item_object= HomeActivity.m_jsonGameCourasals.getJSONObject(i);
                                JSONArray item_array=item_object.getJSONArray("items");
                                if(item_array.length()>0){
                                    hasData=true;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        if(hasData){
                            if(HomeActivity.getmGameMainFragment()!=null){
                                setListData(HomeActivity.m_jsonGameCourasals);
                                fragmentTransaction = getFragmentManager().beginTransaction();
                                GamesDetailsFragment fragmentOnDemandAllContent = GamesDetailsFragment.newInstance(Constants.SHOWS, HomeActivity.m_jsonGameCourasals.toString());
                                fragmentTransaction.replace(R.id.fragment_games_pagerandlist, fragmentOnDemandAllContent);
                                fragmentTransaction.commit();
                            }

                        }else{
                            setListDataGrid(HomeActivity.m_jsonGameCourasals);
                        }




                    }else {
                        try {
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                Toast.makeText(getActivity(),"No data found",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"No Network",Toast.LENGTH_SHORT).show();
                            }
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

    private void setListData(JSONArray m_jsonDemandListItems) {
        try {
            String module;


            mOnDemandlist.clear();
            if(hasData){
                mOnDemandlist.add(new SideMenu("", "Games", "", ""));
            }

            String id = "", name = "", title = "";
            for (int i = 0; i < m_jsonDemandListItems.length(); i++) {

                JSONObject object = m_jsonDemandListItems.getJSONObject(i);
                if (object.has("id")) {
                    id = object.getString("id");
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


                mOnDemandlist.add(new SideMenu(id, title, "", ""));
            }

            mPrimeTimeSelectedItem=0;
            HomeActivity.toolbarMainContent = mOnDemandlist.get(0).getName();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            GameListAdapter movieByRatingAdapter = new GameListAdapter(mOnDemandlist, getActivity());
            fragment_game_alllist_items.setAdapter(movieByRatingAdapter);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListDataGrid(JSONArray m_jsonDemandListItems) {
        try {
            String module;


            mOnDemandlist.clear();
            if(hasData){
                mOnDemandlist.add(new SideMenu("", "Games", "", ""));
            }

            String id = "", name = "", title = "";
            for (int i = 0; i < m_jsonDemandListItems.length(); i++) {

                JSONObject object = m_jsonDemandListItems.getJSONObject(i);
                if (object.has("id")) {
                    id = object.getString("id");
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


                mOnDemandlist.add(new SideMenu(id, title, "", ""));
            }

            mPrimeTimeSelectedItem=0;
            HomeActivity.toolbarMainContent = mOnDemandlist.get(0).getName();
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            setAnalyticreport(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
            GameListAdapter movieByRatingAdapter = new GameListAdapter(mOnDemandlist, getActivity());
            fragment_game_alllist_items.setAdapter(movieByRatingAdapter);

            String m_id=mOnDemandlist.get(0).getId();

            if(!m_id.equalsIgnoreCase(""))
            {
                new LoadingAllTVfeaturedCarouselsById().execute(m_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void text_font_typeface() {
        OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
        osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
    }
    private class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.DataObjectHolder> {
        ArrayList<SideMenu> list_data;
        Context context;

        public GameListAdapter(ArrayList<SideMenu> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
            text_font_typeface();
        }


        @Override
        public GameListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final GameListAdapter.DataObjectHolder holder, final int position) {
            final String menu_text = list_data.get(position).getName();
            final String menu_id = list_data.get(position).getId();
            final String menu_tag = list_data.get(position).getType();
            holder.fragment_ondemandlist_items.setSingleLine(false);
            holder.fragment_ondemandlist_items.setText(menu_text);
            holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
            if (position == mPrimeTimeSelectedItem) {
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                holder.fragment_ondemandlist_items.setTextColor(getResources().getColor(R.color.white));
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
                holder.fragment_ondemandlist_items.setTextColor(getResources().getColor(R.color.text_light_grey));
            }


            holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPrimeTimeSelectedItem = position;
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                    notifyDataSetChanged();
                    HomeActivity.toolbarMainContent = list_data.get(position).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                    setAnalyticreport(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");

                    if(menu_id.equalsIgnoreCase(""))
                    {

                        if (HomeActivity.m_jsonGameCourasals != null && HomeActivity.m_jsonGameCourasals.length() > 0) {
                            if(HomeActivity.getmGameMainFragment()!=null){
                                fragmentTransaction = getFragmentManager().beginTransaction();
                                GamesDetailsFragment fragmentOnDemandAllContent = GamesDetailsFragment.newInstance(Constants.SHOWS, HomeActivity.m_jsonGameCourasals.toString());
                                fragmentTransaction.replace(R.id.fragment_games_pagerandlist, fragmentOnDemandAllContent);
                                fragmentTransaction.commit();
                            }



                        }else {
                            if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                                new LoadingGameSideMenuTask().execute();
                            }
                        }




                    }else
                    {



                        new LoadingAllTVfeaturedCarouselsById().execute(menu_id);
                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return list_data.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);
                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);
                Utilities.setViewFocus(getActivity(), fragment_ondemandlist_items);
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
                HomeActivity.content_position = Constants.SUB_CONTENT;
                if(HomeActivity.getmGameMainFragment()!=null){
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    GridFragment listfragment = GridFragment.newInstance(Constants.GAMES, HomeActivity.allCarousels.toString());
                    fragmentTransaction_list.replace(R.id.fragment_games_pagerandlist, listfragment);
                    fragmentTransaction_list.commit();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setAnalyticreport(String main,String mainString,String SubString1,String SubString2){
        try {
            if(TextUtils.isEmpty(mainString)){
                Utilities.setAnalytics(mTracker,main);
            }else if(TextUtils.isEmpty(SubString1)){
                Utilities.setAnalytics(mTracker,main+"-"+mainString);
            }else if(TextUtils.isEmpty(SubString2)){
                Utilities.setAnalytics(mTracker,main+"-"+mainString+"-"+SubString1);
            }else{
                Utilities.setAnalytics(mTracker,main+"-"+mainString+"-"+SubString1+"-"+SubString2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
