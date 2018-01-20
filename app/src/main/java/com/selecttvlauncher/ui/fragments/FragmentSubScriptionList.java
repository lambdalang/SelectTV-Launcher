package com.selecttvlauncher.ui.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.selecttvlauncher.BeanClass.SubScriptionBean;
import com.selecttvlauncher.BeanClass.SubScriptionListBean;
import com.selecttvlauncher.BeanClass.SubScriptionsubList;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentSubScriptionList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView fragment_subscriptions_list_items,fragment_subscriptions_sub_list_items;
    private ImageView fragment_subscriptions_prev_icon;

    public static JSONArray m_jsonSubscriptionCategories;

    public static String sSelectedMenu = Constants.MOVIE_DETAILS;

    LinearLayoutManager linearLayoutManager,sublinearLayoutManager;
    private ProgressBar fragment_subscriptions_progress;
    private ArrayList<SubScriptionListBean> mSubscriptiondataList = new ArrayList<>();
    private ArrayList<SubScriptionsubList> mSubscriptionSubList = new ArrayList<>();
    private Typeface OpenSans_Regular;
    private Typeface OpenSans_Bold;
    private Typeface OpenSans_Semibold;
    private Typeface osl_ttf;
    int mSelectedItem = 0;
    int submSelectedItem = 0;
    int submListArraypos = 0;
    private int fPos;
    private float progress;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSubScriptionList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSubScriptionList newInstance(String param1, String param2) {
        FragmentSubScriptionList fragment = new FragmentSubScriptionList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSubScriptionList() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_sub_scription_list, container, false);
        fragment_subscriptions_progress = (ProgressBar) view.findViewById(R.id.fragment_subscriptions_progress);
        fragment_subscriptions_prev_icon = (ImageView) view.findViewById(R.id.fragment_subscriptions_prev_icon);
        fragment_subscriptions_list_items = (RecyclerView) view.findViewById(R.id.fragment_subscriptions_list_items);
        fragment_subscriptions_sub_list_items = (RecyclerView) view.findViewById(R.id.fragment_subscriptions_sub_list_items);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        sublinearLayoutManager = new LinearLayoutManager(getActivity());
        fragment_subscriptions_list_items.setLayoutManager(linearLayoutManager);
        fragment_subscriptions_sub_list_items.setLayoutManager(sublinearLayoutManager);
        ((HomeActivity)getActivity()).setFragmentSublist(this);

        if (HomeActivity.subScriptionBeans != null && HomeActivity.subScriptionBeans.size() != 0) {
            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.subScriptionBeans.get(0).getName(), "", "");

            SubScriptionsMainListAdapter subScriptionsMainListAdapter = new SubScriptionsMainListAdapter(HomeActivity.subScriptionBeans, getActivity());
            fragment_subscriptions_list_items.setAdapter(subScriptionsMainListAdapter);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FragmentSubScriptionHorizontalList fragmentSubScriptionHorizontalList = FragmentSubScriptionHorizontalList.newInstance(String.valueOf(0), "");
            fragmentTransaction.replace(R.id.fragment_subscription_content, fragmentSubScriptionHorizontalList);
            fragmentTransaction.commit();

        } else {
           // ((HomeActivity) getActivity()).mChannelProgress(true, "Loading Subcription categories", (int) fPos + "%", progress);
            new LoadingSubscriptionList().execute();
        }
        Utilities.setViewFocus(getActivity(),fragment_subscriptions_prev_icon);
        fragment_subscriptions_prev_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (FragmentSubScriptions.sContent_position) {
                    case Constants.SUBSCRIPTION_CONTENT:
                        FragmentSubScriptionList.sSelectedMenu = "";
                        FragmentSubScriptions.sContent_position = Constants.SUBSCRIPTION_SUBCONTENT;
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        FragmentSubscriptionsGrid fragmentSubscriptionsGrid = FragmentSubscriptionsGrid.newInstance(FragmentSubScriptionHorizontalList.list, String.valueOf(FragmentSubScriptionHorizontalList.mposition));
                        fragmentTransaction_list.replace(R.id.fragment_subscription_content, fragmentSubscriptionsGrid);
                        fragmentTransaction_list.commit();
                        break;
                    case Constants.SUBSCRIPTION_SUBCONTENT:
                        FragmentSubScriptions.sContent_position = Constants.SUBSCRIPTION_MAINCONTENT;
                        FragmentSubScriptionList.sSelectedMenu = "";
                        if (HomeActivity.subScriptionBeans != null && HomeActivity.subScriptionBeans.size() != 0) {
                            HomeActivity.toolbarMainContent = HomeActivity.subScriptionBeans.get(mSelectedItem).getName();
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            FragmentSubScriptionHorizontalList fragmentSubScriptionHorizontalList = FragmentSubScriptionHorizontalList.newInstance(String.valueOf(mSelectedItem), "");
                            fragmentTransaction.replace(R.id.fragment_subscription_content, fragmentSubScriptionHorizontalList);
                            fragmentTransaction.commit();

                            swapList(0,false,0);

                        } else {
                            new LoadingSubscriptionList().execute();
                        }
                        break;
                    case Constants.SUBSCRIPTION_MAINCONTENT:
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

    public void swapList(int pos, boolean b, int s) {
        if(b){
            submSelectedItem=s;
            fragment_subscriptions_list_items.setVisibility(View.GONE);
            fragment_subscriptions_sub_list_items.setVisibility(View.VISIBLE);

            ArrayList<SubScriptionBean> subarray= HomeActivity.subScriptionBeans;

            ArrayList<SubScriptionsubList> subData=subarray.get(pos).getSubScriptionSubLists();

            ScriptionsSubListAdapter mScriptionsSubListAdapter = new ScriptionsSubListAdapter(subData, getActivity(),pos);
            fragment_subscriptions_sub_list_items.setAdapter(mScriptionsSubListAdapter);




        }else{
            fragment_subscriptions_list_items.setVisibility(View.VISIBLE);
            fragment_subscriptions_sub_list_items.setVisibility(View.GONE);
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

    class SubScriptionsMainListAdapter extends RecyclerView.Adapter<SubScriptionsMainListAdapter.DataObjectHolder> {
        ArrayList<SubScriptionBean> subScriptionBeans;
        Context context;


        public SubScriptionsMainListAdapter(ArrayList<SubScriptionBean> subScriptionBeans, Context context) {
            this.subScriptionBeans = subScriptionBeans;
            this.context = context;
            text_font_typeface();
        }

        @Override
        public SubScriptionsMainListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.channel_actorlist_layout, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final SubScriptionsMainListAdapter.DataObjectHolder holder, final int position) {
            holder.fragment_channel_items.setTypeface(OpenSans_Regular);
            if(subScriptionBeans.get(position).getName().equalsIgnoreCase("Networks")){
                holder.fragment_channel_items.setText("Libraries");
            }else{
                holder.fragment_channel_items.setText(subScriptionBeans.get(position).getName());
            }


            if (position == mSelectedItem) {
                holder.fragment_channel_items.setBackgroundResource(R.drawable.menubg);
            } else {
                holder.fragment_channel_items.setBackgroundResource(0);
            }
            holder.fragment_channel_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = position;
                    holder.fragment_channel_items.setBackgroundResource(R.drawable.menubg);
                    notifyDataSetChanged();
                    HomeActivity.toolbarMainContent = subScriptionBeans.get(position).getName();
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentSubScriptionHorizontalList fragmentSubScriptionHorizontalList = FragmentSubScriptionHorizontalList.newInstance(String.valueOf(position), "");
                    fragmentTransaction.replace(R.id.fragment_subscription_content, fragmentSubScriptionHorizontalList);
                    fragmentTransaction.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return HomeActivity.subScriptionBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {

            TextView fragment_channel_items;

            public DataObjectHolder(View itemView) {
                super(itemView);
                fragment_channel_items = (TextView) itemView.findViewById(R.id.fragment_channel_items);

                Utilities.setViewFocus(getActivity(),fragment_channel_items);
            }
        }
    }


    class ScriptionsSubListAdapter extends RecyclerView.Adapter<ScriptionsSubListAdapter .DataObjectHolder> {
        ArrayList<SubScriptionsubList> subScriptionBeans;
        Context context;
        int list_pos;


        public ScriptionsSubListAdapter(ArrayList<SubScriptionsubList> subScriptionBeans, Context context, int pos) {
            this.subScriptionBeans = subScriptionBeans;
            this.context = context;
            list_pos=pos;
            text_font_typeface();
        }

        @Override
        public ScriptionsSubListAdapter .DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.channel_actorlist_layout, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ScriptionsSubListAdapter.DataObjectHolder holder, final int position) {
            holder.fragment_channel_items.setTypeface(OpenSans_Regular);
            holder.fragment_channel_items.setText(Utilities.stripHtml(subScriptionBeans.get(position).getName()));


            if (position == submSelectedItem) {
                holder.fragment_channel_items.setBackgroundResource(R.drawable.menubg);
            } else {
                holder.fragment_channel_items.setBackgroundResource(0);
            }
            holder.fragment_channel_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submSelectedItem= position;
                    holder.fragment_channel_items.setBackgroundResource(R.drawable.menubg);
                    notifyDataSetChanged();
                    FragmentSubScriptions.sContent_position = Constants.SUBSCRIPTION_SUBCONTENT;
                    HomeActivity.toolbarSubContent = Utilities.stripHtml(subScriptionBeans.get(position).getName());
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    FragmentSubscriptionsGrid fragmentSubscriptionsGrid = FragmentSubscriptionsGrid.newInstance(Integer.toString(list_pos), String.valueOf(position));
                    fragmentTransaction_list.replace(R.id.fragment_subscription_content, fragmentSubscriptionsGrid);
                    fragmentTransaction_list.commit();


                }
            });
        }

        @Override
        public int getItemCount() {
            return subScriptionBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {

            TextView fragment_channel_items;

            public DataObjectHolder(View itemView) {
                super(itemView);
                fragment_channel_items = (TextView) itemView.findViewById(R.id.fragment_channel_items);

                Utilities.setViewFocus(getActivity(),fragment_channel_items);
            }
        }
    }


    class LoadingSubscriptionList extends AsyncTask<Object, Integer, Object> {
        int mSubscriptionmainid, mSubscriptionsubid, mSubscriptiondataid;
        String mSubscriptionmainname = "", mSubscriptionsubname = "", mSubscriptiondataname = "", mSubscriptiondataimage = "", mSubscriptiondatatype = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment_subscriptions_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress = values[0];
            fPos = values[0];
            //((HomeActivity) getActivity()).mChannelProgress(true, "Loading Subcription categories", (int) fPos + "%", progress);

        }


        @Override
        protected Object doInBackground(Object... params) {
            try {
                HomeActivity.subScriptionBeans.clear();

            /*for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(100);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
                m_jsonSubscriptionCategories = JSONRPCAPI.getSubScriptionList();
                if ( m_jsonSubscriptionCategories  == null) return null;
                for (int i = 0; i < m_jsonSubscriptionCategories.length(); i++) {
                    try {
                        JSONObject jSubscriptionCategory = m_jsonSubscriptionCategories.getJSONObject(i);


                        if (jSubscriptionCategory.has("id")) {
                            mSubscriptionmainid = jSubscriptionCategory.getInt("id");
                        }
                        if (jSubscriptionCategory.has("name")) {
                            mSubscriptionmainname = jSubscriptionCategory.getString("name");

                        }


                        if (jSubscriptionCategory.has("subcategories")) {

                            JSONArray subList_array = jSubscriptionCategory.getJSONArray("subcategories");
                            mSubscriptionSubList = new ArrayList<>();
                            for (int j = 0; j < subList_array.length(); j++) {
                                JSONObject jSubscriptionSubCategory = subList_array.getJSONObject(j);
                                if (jSubscriptionSubCategory.has("id")) {
                                    mSubscriptionsubid = jSubscriptionSubCategory.getInt("id");
                                }


                                if (jSubscriptionSubCategory.has("title")) {
                                    mSubscriptionsubname = jSubscriptionSubCategory.getString("title");
                                    if(mSubscriptionsubname.equals(""))
                                    {
                                        if (jSubscriptionSubCategory.has("name")) {
                                            mSubscriptionsubname = jSubscriptionSubCategory.getString("name");
                                        }
                                    }
                                }



                                if (jSubscriptionSubCategory.has("items")) {

                                    JSONArray data_array = jSubscriptionSubCategory.getJSONArray("items");
                                    mSubscriptiondataList = new ArrayList<>();
                                    for (int k = 0; k < data_array.length(); k++) {
                                        JSONObject jSubscriptiondataCategory = data_array.getJSONObject(k);

                                        if (jSubscriptiondataCategory.has("id")) {
                                            mSubscriptiondataid = jSubscriptiondataCategory.getInt("id");
                                        }
                                        if (jSubscriptiondataCategory.has("name")) {
                                            mSubscriptiondataname = jSubscriptiondataCategory.getString("name");
                                        }

                                        if (jSubscriptiondataCategory.has("image")) {
                                            mSubscriptiondataimage = jSubscriptiondataCategory.getString("image");
                                        }

                                        if (jSubscriptiondataCategory.has("type")) {
                                            mSubscriptiondatatype = jSubscriptiondataCategory.getString("type");
                                        }


                                        mSubscriptiondataList.add(new SubScriptionListBean(mSubscriptiondataid, mSubscriptiondataname, mSubscriptiondataimage, mSubscriptiondatatype));

                                    }

                                }


                                mSubscriptionSubList.add(new SubScriptionsubList(mSubscriptionsubid, mSubscriptionsubname, mSubscriptiondataList));
                            }


                        }
                        if(!mSubscriptionmainname.equalsIgnoreCase("Movies")&&!mSubscriptionmainname.equalsIgnoreCase("tv shows")){
                            HomeActivity.subScriptionBeans.add(new SubScriptionBean(mSubscriptionmainid, mSubscriptionmainname, mSubscriptionSubList));
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
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
            //((HomeActivity) getActivity()).mChannelProgress(false, "Loading Subcription categories", (int) fPos + "%", progress);
            try {
                fragment_subscriptions_progress.setVisibility(View.GONE);
                HomeActivity.toolbarMainContent = HomeActivity.subScriptionBeans.get(0).getName();
                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");

                SubScriptionsMainListAdapter subScriptionsMainListAdapter = new SubScriptionsMainListAdapter(HomeActivity.subScriptionBeans, getActivity());
                fragment_subscriptions_list_items.setAdapter(subScriptionsMainListAdapter);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentSubScriptionHorizontalList fragmentSubScriptionHorizontalList = FragmentSubScriptionHorizontalList.newInstance(String.valueOf(0), "");
                fragmentTransaction.replace(R.id.fragment_subscription_content, fragmentSubScriptionHorizontalList);
                fragmentTransaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void text_font_typeface() {
        OpenSans_Bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Bold_1.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular_1.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Semibold_1.ttf");
        osl_ttf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/osl.ttf");
    }

}
