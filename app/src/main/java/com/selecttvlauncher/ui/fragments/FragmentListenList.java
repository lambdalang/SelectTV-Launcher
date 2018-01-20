package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.BeanClass.CauroselBean;
import com.selecttvlauncher.BeanClass.CauroselsItemBean;
import com.selecttvlauncher.BeanClass.ListenGridBean;
import com.selecttvlauncher.BeanClass.SliderCommonBean;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.dialogs.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentListenList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView fragment_listen_prev_icon;
    private RecyclerView fragment_listen_alllist_items;
    public static ArrayList<String> mListenMainList = new ArrayList<>();
    public static ArrayList<String> mListenSubList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ListenMainList listenMainList;
    ListenSubList listenSubList;
    public static int mListPosition;
    public static ArrayList<ListenGridBean> listenGenreBeans = new ArrayList<>();
    public static ArrayList<ListenGridBean> listenLocationBeans = new ArrayList<>();
    public static ArrayList<ListenGridBean> listenLanguageBeans = new ArrayList<>();

    private ArrayList<CauroselBean> listen_list = new ArrayList<>();
    private ArrayList<SliderCommonBean> slider_list = new ArrayList<>();

    private String selected_menu = Constants.LISTEN_LISTEN;
    private TextView fragment_listen_switchtext_all;
    private TextView fragment_listen_switchtext_free;
    private ImageView switch_image;
    private LinearLayout fragment_listen_layout;
    private LinearLayout fragment_listen_alllist_items_layout;
    private boolean isswitchselected;

    public static boolean isdestryed = true;
    private Tracker mTracker;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListenList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListenList newInstance(String param1, String param2) {
        FragmentListenList fragment = new FragmentListenList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentListenList() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_listen_list, container, false);
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        fragment_listen_layout = (LinearLayout) view.findViewById(R.id.fragment_listen_layout);
        fragment_listen_alllist_items_layout = (LinearLayout) view.findViewById(R.id.fragment_listen_alllist_items_layout);
        fragment_listen_prev_icon = (ImageView) view.findViewById(R.id.fragment_listen_prev_icon);
        switch_image = (ImageView) view.findViewById(R.id.switch_image);
        fragment_listen_alllist_items = (RecyclerView) view.findViewById(R.id.fragment_listen_alllist_items);
        fragment_listen_switchtext_all = (TextView) view.findViewById(R.id.fragment_listen_switchtext_all);
        fragment_listen_switchtext_free = (TextView) view.findViewById(R.id.fragment_listen_switchtext_free);
        /*isswitchselected = false;
        switch_image.setImageResource(R.drawable.off);*/

        isswitchselected = true;
        HomeActivity.mfreeorall = 1;
        switch_image.setImageResource(R.drawable.on);

        setHoverEffects();


        mListenMainList.clear();
        mListenMainList.add("All");
        mListenMainList.add("Music Videos");
        mListenMainList.add("Radio Stations");
        mListenMainList.add("Music Documentaries");
        mListenMainList.add("Podcasts");
        // mListenMainList.add("Audio Books");

        mListenSubList.clear();
        mListenSubList.add("Genre");
        //mListenSubList.add("Language");
        //mListenSubList.add("Location");
        linearLayoutManager = new LinearLayoutManager(getActivity());
        fragment_listen_alllist_items.setLayoutManager(linearLayoutManager);
        fragment_listen_alllist_items.setHasFixedSize(true);
        HomeActivity.toolbarMainContent = mListenMainList.get(0);
        FragmentListenMain.mListenSubContent = "";
        FragmentListenMain.mListentempSubContent = "";

        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
       /* listenMainList = new ListenMainList(mListenMainList, getActivity());
        fragment_listen_alllist_items.setAdapter(listenMainList);
         new LoadingListenData().execute();*/
        listenSubList = new ListenSubList(mListenSubList, getActivity());
        fragment_listen_alllist_items.setAdapter(listenSubList);

        HomeActivity.toolbarMainContent = mListenMainList.get(2);
        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
        FragmentListenMain.mRadioMainContent = Constants.RADIO_GENRE;
        FragmentListenMain.mRadioSubContent = Constants.RADIO_GENRE;
        selected_menu = Constants.LISTEN_RADIO;
        FragmentListenMain.mListenMainContent = Constants.LISTEN_RADIO;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fragment_listen_layout.getLayoutParams();
        params.setMargins(0, 20, 0, 0);
        fragment_listen_layout.setLayoutParams(params);


        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) fragment_listen_alllist_items_layout.getLayoutParams();
        params1.setMargins(32, 0, 0, 0);
        fragment_listen_alllist_items_layout.setLayoutParams(params1);

        fragment_listen_layout.setOrientation(LinearLayout.HORIZONTAL);
        switch_image.setVisibility(View.GONE);
        fragment_listen_switchtext_all.setVisibility(View.GONE);
        fragment_listen_switchtext_free.setVisibility(View.GONE);
        HomeActivity.toolbarSubContent = mListenSubList.get(0);
        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
        setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
        mListPosition = 0;
        listenSubList = new ListenSubList(mListenSubList, getActivity());
        fragment_listen_alllist_items.setAdapter(listenSubList);

        if (listenGenreBeans != null && listenGenreBeans.size() > 0) {
            if (FragmentListenMain.isdestryed) {
                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(0), "");
                fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                fragmentTransaction_list.commit();
            }


        } else {
            new LoadingRadioStation().execute();
        }

        switch_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isswitchselected) {
                    isswitchselected = false;
                    HomeActivity.mfreeorall = 0;
                    switch_image.setImageResource(R.drawable.off);

                } else {
                    isswitchselected = true;
                    HomeActivity.mfreeorall = 1;
                    switch_image.setImageResource(R.drawable.on);
                }


                switch (selected_menu) {
                    case Constants.LISTEN_LISTEN:
                        new LoadingListenData().execute();
                        break;
                    case Constants.LISTEN_PODCAST:
                        if (FragmentListenMain.mListentempSubContent.equals(Constants.LISTEN_LISTEN_CONTENT) && FragmentListenMain.mListenSubContent.equals("")) {
                            FragmentListenMain.mListenSubContent = Constants.LISTEN_LISTEN_SUBCONTENT;
                        } else if (FragmentListenMain.mListentempSubContent.equals("") && FragmentListenMain.mListenSubContent.equals(Constants.LISTEN_LISTEN_SUBCONTENT)) {
                            FragmentListenMain.mListenSubContent = Constants.LISTEN_LISTEN_SUBCONTENT;
                        } else if (FragmentListenMain.mListentempSubContent.equals(Constants.LISTEN_LISTEN_CONTENT) && FragmentListenMain.mListenSubContent.equals(Constants.LISTEN_LISTEN_SUBCONTENT)) {
                            FragmentListenMain.mListenSubContent = Constants.LISTEN_LISTEN_SUBCONTENT;
                        } else if (FragmentListenMain.mListentempSubContent.equals(Constants.LISTEN_LISTEN_CONTENT) && FragmentListenMain.mListenSubContent.equals(Constants.LISTEN_LISTEN_CONTENT)) {
                            FragmentListenMain.mListenSubContent = Constants.LISTEN_LISTEN_CONTENT;
                        } else {
                            FragmentListenMain.mListenSubContent = "";

                        }
                        new LoadingListenData().execute();
                        break;
                }


            }
        });

        fragment_listen_prev_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_LISTEN)) {

                    if (FragmentListenMain.mListenSubContent.equals(Constants.LISTEN_LISTEN_SUBCONTENT)) {
                        HomeActivity.toolbarMainContent = mListenMainList.get(0);
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                        if (FragmentListenMain.isdestryed) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            FragmentListenContent contentfragment = FragmentListenContent.newInstance(FragmentListenMain.mListenMainContent, "");
                            fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, contentfragment);
                            fragmentTransaction_list.commit();
                        }

                        FragmentListenMain.mListenSubContent = "";
                    } else {
                        HomeActivity.isPayperview = "";
                        HomeActivity.toolbarMainContent = "";
                        FragmentListenMain.mListenSubContent = "";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                        if (FragmentListenMain.isdestryed) {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                            fragmentTransaction.commit();
                        }

                    }
                } else if (FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_PODCAST)) {
                    if (FragmentListenMain.mListenSubContent.equals(Constants.LISTEN_LISTEN_CONTENT)) {
                        FragmentListenMain.mListenSubContent = Constants.LISTEN_LISTEN_SUBCONTENT;
                        HomeActivity.toolbarMainContent = mListenMainList.get(0);
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                        if (FragmentListenContent.item_list != null && FragmentListenContent.item_list.size() != 0) {
                            if (FragmentListenMain.isdestryed) {
                                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                FragmentListenGrid listfragment = FragmentListenGrid.newInstance("", "");
                                fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();
                            }

                        } else {
                            new LoadingAllTVfeaturedCarouselsById().execute();
                        }
                    } else if (FragmentListenMain.mListenSubContent.equals(Constants.LISTEN_LISTEN_SUBCONTENT)) {

                        FragmentListenMain.mListenSubContent = "";
                        HomeActivity.toolbarMainContent = mListenMainList.get(0);
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        if (FragmentListenMain.isdestryed) {
                            FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                            FragmentListenContent contentfragment = FragmentListenContent.newInstance(FragmentListenMain.mListenMainContent, "");
                            fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, contentfragment);
                            fragmentTransaction_list.commit();
                        }

                    } else {
                        HomeActivity.isPayperview = "";
                        HomeActivity.toolbarMainContent = "";
                        FragmentListenMain.mListenSubContent = "";
                        HomeActivity.toolbarSubContent = "";
                        ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                        if (FragmentListenMain.isdestryed) {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                            fragmentTransaction.commit();
                        }

                    }
                } else if (FragmentListenMain.mListenMainContent.equals(Constants.LISTEN_RADIO)) {
                    switch (FragmentListenMain.mRadioMainContent) {
                        case Constants.RADIO_GENRE: {
                            if (!FragmentListenMain.mRadioDetailsContent.equals("")) {
                                FragmentListenMain.mRadioDetailsContent = "";
                                FragmentListenMain.mRadioMainContent = "";
                                HomeActivity.toolbarSubContent = mListenSubList.get(mListPosition);
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                                if (FragmentListenMain.isdestryed) {

                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(0), "");
                                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }
                            } else {
                                HomeActivity.isPayperview = "";
                                HomeActivity.toolbarMainContent = "";
                                FragmentListenMain.mListenSubContent = "";
                                HomeActivity.toolbarSubContent = "";
                                ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                                if (FragmentListenMain.isdestryed) {
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                                    fragmentTransaction.commit();
                                }

                               /* FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fragment_listen_layout.getLayoutParams();
                                params.setMargins(0, 0, 0, 0);
                                fragment_listen_layout.setLayoutParams(params);


                                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) fragment_listen_alllist_items_layout.getLayoutParams();
                                params1.setMargins(0, 50, 0, 0);
                                fragment_listen_alllist_items_layout.setLayoutParams(params1);

                                fragment_listen_layout.setOrientation(LinearLayout.VERTICAL);
                                switch_image.setVisibility(View.VISIBLE);
                                fragment_listen_switchtext_all.setVisibility(View.VISIBLE);
                                fragment_listen_switchtext_free.setVisibility(View.VISIBLE);
                                FragmentListenMain.mListenMainContent = Constants.LISTEN_LISTEN;
                                FragmentListenMain.mListenSubContent = "";

                                HomeActivity.toolbarMainContent = mListenMainList.get(0);
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                                listenMainList = new ListenMainList(mListenMainList, getActivity());
                                fragment_listen_alllist_items.setAdapter(listenMainList);
                                if ((HomeActivity.listen_list != null && HomeActivity.listen_list.size() > 0) && (HomeActivity.slider_list != null && HomeActivity.slider_list.size() > 0)) {
                                    if (FragmentListenMain.isdestryed) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        FragmentListenContent contentfragment = FragmentListenContent.newInstance(Constants.LISTEN_LISTEN, "");
                                        fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, contentfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {
                                    new LoadingListenData().execute();
                                }*/
                            }


                            break;
                        }
                        case Constants.RADIO_LANGUAGE: {
                            if (!FragmentListenMain.mRadioDetailsContent.equals("")) {
                                FragmentListenMain.mRadioDetailsContent = "";
                                FragmentListenMain.mRadioMainContent = "";
                                HomeActivity.toolbarSubContent = mListenSubList.get(mListPosition);
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                if (FragmentListenMain.isdestryed) {

                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(1), "");
                                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }

                            } else {
                                HomeActivity.isPayperview = "";
                                HomeActivity.toolbarMainContent = "";
                                FragmentListenMain.mListenSubContent = "";
                                HomeActivity.toolbarSubContent = "";
                                ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                                if (FragmentListenMain.isdestryed) {
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                                    fragmentTransaction.commit();
                                }

                               /* FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fragment_listen_layout.getLayoutParams();
                                params.setMargins(0, 0, 0, 0);
                                fragment_listen_layout.setLayoutParams(params);


                                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) fragment_listen_alllist_items_layout.getLayoutParams();
                                params1.setMargins(0, 50, 0, 0);
                                fragment_listen_alllist_items_layout.setLayoutParams(params1);

                                fragment_listen_layout.setOrientation(LinearLayout.VERTICAL);
                                switch_image.setVisibility(View.VISIBLE);
                                fragment_listen_switchtext_all.setVisibility(View.VISIBLE);
                                fragment_listen_switchtext_free.setVisibility(View.VISIBLE);
                                FragmentListenMain.mListenMainContent = Constants.LISTEN_LISTEN;
                                FragmentListenMain.mListenSubContent = "";
                                HomeActivity.toolbarMainContent = mListenMainList.get(0);
                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                                listenMainList = new ListenMainList(mListenMainList, getActivity());
                                fragment_listen_alllist_items.setAdapter(listenMainList);
                                if ((HomeActivity.listen_list != null && HomeActivity.listen_list.size() > 0) && (HomeActivity.slider_list != null && HomeActivity.slider_list.size() > 0)) {
                                    if (FragmentListenMain.isdestryed) {
                                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                        FragmentListenContent contentfragment = FragmentListenContent.newInstance(Constants.LISTEN_LISTEN, "");
                                        fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, contentfragment);
                                        fragmentTransaction_list.commit();
                                    }

                                } else {
                                    new LoadingListenData().execute();
                                }*/
                            }

                            break;
                        }
                        case Constants.RADIO_LOCATION: {
                            if (FragmentListenMain.isdestryed) {
                                FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(2), "");
                                fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                                fragmentTransaction_list.commit();
                            }

                            break;
                        }
                        default:
                           /* FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fragment_listen_layout.getLayoutParams();
                            params.setMargins(0, 0, 0, 0);
                            fragment_listen_layout.setLayoutParams(params);


                            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) fragment_listen_alllist_items_layout.getLayoutParams();
                            params1.setMargins(0, 50, 0, 0);
                            fragment_listen_alllist_items_layout.setLayoutParams(params1);

                            fragment_listen_layout.setOrientation(LinearLayout.VERTICAL);
                            switch_image.setVisibility(View.VISIBLE);
                            fragment_listen_switchtext_all.setVisibility(View.VISIBLE);
                            fragment_listen_switchtext_free.setVisibility(View.VISIBLE);
                            FragmentListenMain.mListenMainContent = Constants.LISTEN_LISTEN;
                            FragmentListenMain.mListenSubContent = "";
                            HomeActivity.toolbarMainContent = mListenMainList.get(0);
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                            listenMainList = new ListenMainList(mListenMainList, getActivity());
                            fragment_listen_alllist_items.setAdapter(listenMainList);
                            if ((HomeActivity.listen_list != null && HomeActivity.listen_list.size() > 0) && (HomeActivity.slider_list != null && HomeActivity.slider_list.size() > 0)) {

                                if (FragmentListenMain.isdestryed) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    FragmentListenContent contentfragment = FragmentListenContent.newInstance(Constants.LISTEN_LISTEN, "");
                                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, contentfragment);
                                    fragmentTransaction_list.commit();
                                }

                            } else {
                                new LoadingListenData().execute();
                            }*/
                            HomeActivity.isPayperview = "";
                            HomeActivity.toolbarMainContent = "";
                            FragmentListenMain.mListenSubContent = "";
                            HomeActivity.toolbarSubContent = "";
                            ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                            if (FragmentListenMain.isdestryed) {
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                                fragmentTransaction.commit();
                            }



                            break;
                    }
                }


                else
                {
                    HomeActivity.isPayperview = "";
                    HomeActivity.toolbarMainContent = "";
                    FragmentListenMain.mListenSubContent = "";
                    HomeActivity.toolbarSubContent = "";
                    ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                        fragmentTransaction.commit();

                }
            }
        });

        return view;
    }

    private void setHoverEffects() {
        fragment_listen_prev_icon.setFocusable(true);
        switch_image.setFocusable(true);
        Utilities.setViewFocus(getActivity(), fragment_listen_prev_icon);
        Utilities.setViewFocus(getActivity(), switch_image);
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
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isdestryed = false;
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
    public class ListenMainList extends RecyclerView.Adapter<ListenMainList.DataObjectHolder> {
        ArrayList<String> mListenmainList;
        Context context;
        int mlistPosition = 0;

        public ListenMainList(ArrayList<String> mListenmainList, Context context) {
            this.mListenmainList = mListenmainList;
            this.context = context;
        }

        @Override
        public ListenMainList.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ListenMainList.DataObjectHolder holder, final int position) {
            holder.fragment_ondemandlist_items.setText(mListenmainList.get(position));
            if (position == mlistPosition) {
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }
            holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistPosition = position;
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
                    notifyDataSetChanged();
                    HomeActivity.toolbarMainContent = mListenmainList.get(position);
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, "", "");
                    switch (position) {
                        case 0:
                            FragmentListenMain.mListenSubContent = "";
                            selected_menu = Constants.LISTEN_LISTEN;
                            FragmentListenMain.mListenMainContent = Constants.LISTEN_LISTEN;
                            new LoadingListenData().execute();

                            break;
                        case 1:
                            FragmentListenMain.mListenSubContent = "";
                            selected_menu = Constants.LISTEN_MUSIC_VIDEOS;
                            HomeActivity.channelID=1;
                            FragmentListenMain.mListenMainContent = Constants.LISTEN_MUSIC_VIDEOS;
                            HomeActivity.isPayperview = "Channels";
                            Log.d("selected:::", "channels act" + "Channels");
                            HomeActivity.toolbarGridContent = "Channels";
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
                            setAnalyticreport(HomeActivity.toolbarMainContent,"", "");
                            HomeActivity.HomeGridClick = "Channels";
                            HomeActivity.menu_position = Constants.MAIN_MENU;
                            HomeActivity.content_position = Constants.MAIN_CONTENT;
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            ChannelFragment channelFragment = new ChannelFragment();
                            fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, channelFragment);
                            fragmentTransaction.commit();
                            break;
                        case 2:
                            FragmentListenMain.mListenSubContent = "";
                            FragmentListenMain.mRadioDetailsContent = "";
                            FragmentListenMain.mRadioMainContent = Constants.RADIO_GENRE;
                            FragmentListenMain.mRadioSubContent = Constants.RADIO_GENRE;
                            selected_menu = Constants.LISTEN_RADIO;
                            FragmentListenMain.mListenMainContent = Constants.LISTEN_RADIO;
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fragment_listen_layout.getLayoutParams();
                            params.setMargins(0, 20, 0, 0);
                            fragment_listen_layout.setLayoutParams(params);


                            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) fragment_listen_alllist_items_layout.getLayoutParams();
                            params1.setMargins(32, 0, 0, 0);
                            fragment_listen_alllist_items_layout.setLayoutParams(params1);

                            fragment_listen_layout.setOrientation(LinearLayout.HORIZONTAL);
                            switch_image.setVisibility(View.GONE);
                            fragment_listen_switchtext_all.setVisibility(View.GONE);
                            fragment_listen_switchtext_free.setVisibility(View.GONE);
                            HomeActivity.toolbarSubContent = mListenSubList.get(0);
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            mListPosition = 0;
                            listenSubList = new ListenSubList(mListenSubList, getActivity());
                            fragment_listen_alllist_items.setAdapter(listenSubList);

                            if (listenGenreBeans != null && listenGenreBeans.size() > 0) {
                                if (FragmentListenMain.isdestryed) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(0), "");
                                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }


                            } else {
                                new LoadingRadioStation().execute();
                            }

                            break;
                        case 3:
                            FragmentListenMain.mListenSubContent = "";
                            selected_menu = Constants.LISTEN_MUSIC_DOCUMENTARIES;
                            FragmentListenMain.mListenMainContent = Constants.LISTEN_MUSIC_DOCUMENTARIES;
                            break;
                        case 4:
                            FragmentListenMain.mListenSubContent = "";
                            selected_menu = Constants.LISTEN_PODCAST;
                            FragmentListenMain.mListenMainContent = Constants.LISTEN_PODCAST;
                            new LoadingListenData().execute();
                            break;
                        case 5:
                            FragmentListenMain.mListenSubContent = "";
                            selected_menu = Constants.LISTEN_AUDIOBOOK;
                            FragmentListenMain.mListenMainContent = Constants.LISTEN_AUDIOBOOK;
                            break;

                    }
                }
            });
            holder.fragment_ondemandlist_items_layout.setFocusable(true);
            Utilities.setViewFocus(context, holder.fragment_ondemandlist_items_layout);
        }

        @Override
        public int getItemCount() {
            return mListenmainList.size();
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

    public class ListenSubList extends RecyclerView.Adapter<ListenSubList.DataObjectHolder> {
        ArrayList<String> mListenSubList;
        Context context;

        int mlistposition = 0;

        public ListenSubList(ArrayList<String> mListenSubList, Context context) {
            this.mListenSubList = mListenSubList;
            this.context = context;

        }

        @Override
        public ListenSubList.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ListenSubList.DataObjectHolder holder, final int position) {
            holder.fragment_ondemandlist_items.setText(mListenSubList.get(position));
            if (position == mlistposition) {
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }
            holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistposition = position;
                    mListPosition = position;
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                    notifyDataSetChanged();
                    HomeActivity.toolbarSubContent = mListenSubList.get(position);
                    ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    setAnalyticreport(HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                    switch (position) {
                        case 0:
                            FragmentListenMain.mRadioDetailsContent = "";
                            FragmentListenMain.mRadioMainContent = Constants.RADIO_GENRE;
                            FragmentListenMain.mRadioSubContent = Constants.RADIO_GENRE;
                            if (listenGenreBeans != null && listenGenreBeans.size() > 0) {
                                if (FragmentListenMain.isdestryed) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(position), "");
                                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }


                            } else {
                                new LoadingRadioStation().execute();
                            }
                            break;
                        case 1:
                            FragmentListenMain.mRadioDetailsContent = "";
                            FragmentListenMain.mRadioMainContent = Constants.RADIO_LANGUAGE;
                            FragmentListenMain.mRadioSubContent = Constants.RADIO_LANGUAGE;
                            if (listenLanguageBeans != null && listenLanguageBeans.size() > 0) {
                                if (FragmentListenMain.isdestryed) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(position), "");
                                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }


                            } else {
                                new LoadingRadioStation().execute();
                            }
                            break;
                        case 2:
                            FragmentListenMain.mRadioDetailsContent = "";
                            FragmentListenMain.mRadioMainContent = Constants.RADIO_LOCATION;
                            if (listenLocationBeans != null && listenLocationBeans.size() > 0) {
                                if (FragmentListenMain.isdestryed) {
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(position), "");
                                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }


                            } else {
                                new LoadingRadioStation().execute();
                            }

                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mListenSubList.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);
                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);

                Utilities.setViewFocus(getActivity(), fragment_ondemandlist_items_layout);
            }
        }
    }


    private class LoadingRadioStation extends AsyncTask<Object, Object, Object> {
        ProgressHUD mProgressHUD;
        String strUrl = "";
        String strName = "";
        int strid = 0;
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            if (FragmentListenMain.isdestryed) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_listen_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }

        @Override
        protected Object doInBackground(Object... params) {
            listenGenreBeans.clear();
            listenLocationBeans.clear();
            listenLanguageBeans.clear();
            try {
               /* HomeActivity.m_jsonRadioContinent = JSONRPCAPI.getRadioContinent();
                if (HomeActivity.m_jsonRadioContinent == null) return null;*/
                HomeActivity.m_jsonRadioGenre = JSONRPCAPI.getRadioGenre();
                if (HomeActivity.m_jsonRadioGenre == null) return null;
                /*HomeActivity.m_jsonRadioLanguage = JSONRPCAPI.getRadioLanguage();
                if (HomeActivity.m_jsonRadioLanguage == null) return null;*/
                for (int i = 0; i < HomeActivity.m_jsonRadioGenre.length(); i++) {
                    try {
                        jsonObject = HomeActivity.m_jsonRadioGenre.getJSONObject(i);

                        if (jsonObject.has("id")) {
                            strid = jsonObject.getInt("id");
                        }
                        if (jsonObject.has("name")) {
                            strName = jsonObject.getString("name");

                        }
                        if (jsonObject.has("image")) {
                            strUrl = jsonObject.getString("image");
                        }

                        listenGenreBeans.add(new ListenGridBean(strid, strName, strUrl));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < HomeActivity.m_jsonRadioContinent.length(); i++) {
                    try {
                        jsonObject = HomeActivity.m_jsonRadioContinent.getJSONObject(i);

                        if (jsonObject.has("id")) {
                            strid = jsonObject.getInt("id");
                        }
                        if (jsonObject.has("name")) {
                            strName = jsonObject.getString("name");

                        }
                        if (jsonObject.has("image")) {
                            strUrl = jsonObject.getString("image");
                        }

                        listenLocationBeans.add(new ListenGridBean(strid, strName, strUrl));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < HomeActivity.m_jsonRadioLanguage.length(); i++) {
                    try {
                        jsonObject = HomeActivity.m_jsonRadioLanguage.getJSONObject(i);

                        if (jsonObject.has("id")) {
                            strid = jsonObject.getInt("id");
                        }
                        if (jsonObject.has("name")) {
                            strName = jsonObject.getString("name");

                        }
                        if (jsonObject.has("image")) {
                            strUrl = jsonObject.getString("image");
                        }

                        listenLanguageBeans.add(new ListenGridBean(strid, strName, strUrl));
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
        protected void onPostExecute(Object params) {

           /* switch (mlistPosition) {
                case 0: {*/
            try {
                if (FragmentListenMain.isdestryed) {
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(mListPosition), "");
                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                    fragmentTransaction_list.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

                    /*break;
                }
                case 1: {
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    FragmentListenGrid listfragment = FragmentListenGrid.newInstance("", HomeActivity.m_jsonRadioLanguage.toString());
                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                    fragmentTransaction_list.commit();
                    break;
                }
                case 2: {
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    FragmentListenGrid listfragment = FragmentListenGrid.newInstance("", HomeActivity.m_jsonRadioContinent.toString());
                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                    fragmentTransaction_list.commit();
                    break;
                }
            }*/
        }
    }


    private class LoadingListenData extends AsyncTask<Object, Object, Object> {


        @Override
        protected void onPreExecute() {
            if (FragmentListenMain.isdestryed) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_listen_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }

        @Override
        protected Object doInBackground(Object... params) {

            try {
                listen_list = new ArrayList<>();
                slider_list = new ArrayList<>();
                JSONArray j_array = new JSONArray();
                JSONArray slider_data = new JSONArray();
                if (selected_menu.equalsIgnoreCase(Constants.LISTEN_LISTEN)) {
                    j_array = JSONRPCAPI.getListenCarousels(HomeActivity.mfreeorall);
                    if ( j_array  == null) return null;
                    slider_data = JSONRPCAPI.getListenSlider();
                    if ( slider_data  == null) return null;
                } else if (selected_menu.equalsIgnoreCase(Constants.LISTEN_PODCAST)) {
                    j_array = JSONRPCAPI.getPodcastCarousels(HomeActivity.mfreeorall);
                    if ( j_array  == null) return null;
                    slider_data = JSONRPCAPI.getPodcastSlider();
                    if ( slider_data  == null) return null;
                }

                Log.d("Listen_carousels::", "::" + j_array.toString());

                for (int i = 0; i < j_array.length(); i++) {
                    JSONObject j_object = j_array.getJSONObject(i);
                    int id = j_object.getInt("id");
                    String title = j_object.getString("title");
                    String name = j_object.getString("name");

                    JSONArray items = j_object.getJSONArray("items");
                    ArrayList<CauroselsItemBean> item_list = new ArrayList<>();
                    for (int j = 0; j < items.length(); j++) {
                        JSONObject item_object = items.getJSONObject(j);
                        String carousel_image = item_object.getString("carousel_image");
                        String type = item_object.getString("type");
                        int item_id = item_object.getInt("id");
                        String item_name = item_object.getString("name");

                        item_list.add(new CauroselsItemBean(carousel_image, type, item_id, item_name));

                    }
                    listen_list.add(new CauroselBean(title, name, id, item_list));
                }

                for (int i = 0; i < slider_data.length(); i++) {
                    JSONObject slider_object = slider_data.getJSONObject(i);
                    String description = slider_object.getString("description");
                    String title = slider_object.getString("title");
                    String image = slider_object.getString("image");
                    String type = slider_object.getString("type");
                    int id = slider_object.getInt("id");
                    String name = slider_object.getString("name");

                    slider_list.add(new SliderCommonBean(description, title, image, type, name, id));
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {

                switch (FragmentListenMain.mListenSubContent) {
                    case Constants.LISTEN_LISTEN_CONTENT:
                        new LoadingTVNetworkList().execute();
                        break;
                    case Constants.LISTEN_LISTEN_SUBCONTENT: {

                        new LoadingAllTVfeaturedCarouselsById().execute();
                        /*FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        FragmentListenGrid listfragment = FragmentListenGrid.newInstance(String.valueOf(FragmentListenContent.listengridPosition), "");
                        fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();*/
                        break;
                    }
                    default: {
                        HomeActivity.listen_list = listen_list;
                        HomeActivity.slider_list = slider_list;
                        if (FragmentListenMain.isdestryed) {
                            FragmentTransaction fragmentTransaction_list = getActivity().getSupportFragmentManager().beginTransaction();
                            FragmentListenContent contentfragment = FragmentListenContent.newInstance(Constants.LISTEN_LISTEN, "");
                            fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, contentfragment);
                            fragmentTransaction_list.commit();

                        }


                        break;
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
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
            if (FragmentListenMain.isdestryed) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_listen_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

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

//                    grid_list.add(new GridBean(id, name, image, description, type));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {

                if (FragmentListenMain.isdestryed) {
                    if (m_jsonNetworkList.length() > 0) {
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


    public class LoadingAllTVfeaturedCarouselsById extends AsyncTask<String, Object, Object> {
        String description = "", title = "", image = "", name = "";
        int id;
        JSONArray allCarousels;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (FragmentListenMain.isdestryed) {
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_listen_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }

        @Override
        protected String doInBackground(String... params) {


            Log.d("mfreeorall::", "id::" + HomeActivity.mfreeorall);
            allCarousels = JSONRPCAPI.getAllTVfeaturedCarousels(FragmentListenMain.mListenViewallId, 1000, 0, HomeActivity.mfreeorall);
            if (allCarousels  == null) return null;
            Log.d("allCarousels::", "allCarousels::" + allCarousels);
            FragmentListenContent.item_list.clear();
            int id = 0;
            String name = "", image = "", description = "", type = "";
            try {
                for (int i = 0; i < allCarousels.length(); i++) {
                    JSONObject object = allCarousels.getJSONObject(i);
                    if (object.has("id")) {
                        id = object.getInt("id");
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
                    }
                    FragmentListenContent.item_list.add(new CauroselsItemBean(image, type, id, name));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if (FragmentListenMain.isdestryed) {
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    FragmentListenGrid listfragment = FragmentListenGrid.newInstance("", "");
                    fragmentTransaction_list.replace(R.id.fragment_listen_pagerandlist, listfragment);
                    fragmentTransaction_list.commit();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setAnalyticreport(String mainString,String SubString1,String SubString2){
        try {

                if(TextUtils.isEmpty(mainString)){
                    Utilities.setAnalytics(mTracker,"Listen");
                }else if(TextUtils.isEmpty(SubString1)){
                    Utilities.setAnalytics(mTracker,"Listen-"+mainString);
                }else if(TextUtils.isEmpty(SubString2)){
                    Utilities.setAnalytics(mTracker,"Listen-"+mainString+"-"+SubString1);
                }else {
                    Utilities.setAnalytics(mTracker,"Listen-"+mainString+"-"+SubString1+"-"+SubString2);
                }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
