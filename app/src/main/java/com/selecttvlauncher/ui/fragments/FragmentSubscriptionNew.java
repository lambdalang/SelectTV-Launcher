package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.BeanClass.ListenGridBean;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.AutofitRecylerview;
import com.selecttvlauncher.ui.views.GridViewItem;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 8/3/2016.
 */
public class FragmentSubscriptionNew extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentSubscriptionNew() {
        // Required empty public constructor
    }

    private ImageView fragment_prev_icon;
    private AutofitRecylerview subscription_fragment_list;
    private ArrayList<ListenGridBean> subList=new ArrayList<>();
    private LinearLayout fragment_app_layout;

    GridLayoutManager gridLayoutManager;
    private ProgressBar fragment_progress;
    private int width;
    private int spacing;
    private int height;
    private Tracker mTracker;

    public static FragmentSubscriptionNew newInstance(String param1, String param2) {
        FragmentSubscriptionNew fragment = new FragmentSubscriptionNew();
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
        View view = inflater.inflate(R.layout.fragment_fragment_subscription_new, container, false);
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, "", "", "");
        setAnalyticreport(HomeActivity.toolbarGridContent, "", "", "");
        fragment_app_layout = (LinearLayout) view.findViewById(R.id.fragment_app_layout);
        fragment_prev_icon = (ImageView) view.findViewById(R.id.fragment_prev_icon);
        subscription_fragment_list = (AutofitRecylerview) view.findViewById(R.id.subscription_fragment_list);
        fragment_progress = (ProgressBar) view.findViewById(R.id.fragment_progress);
        width = subscription_fragment_list.getMeasuredWidth();
        height = subscription_fragment_list.getMeasuredHeight();
        gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        subscription_fragment_list.hasFixedSize();
        subscription_fragment_list.setLayoutManager(gridLayoutManager);
        int spanCount = 5; // 3 columns
        spacing = 25; // 50px
        boolean includeEdge = true;
        subscription_fragment_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        new LoadingSubscription().execute();
        fragment_prev_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.isPayperview = "";
                HomeActivity.toolbarMainContent = "";
                HomeActivity.toolbarSubContent = "";
                ((HomeActivity) getActivity()).toolbarTextChange("", "", "", "");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentHomeScreenGrid fragmentHomeScreenGrid = new FragmentHomeScreenGrid();
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentHomeScreenGrid);
                fragmentTransaction.commit();
            }
        });

        return view;
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
        //mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class LoadingSubscription extends AsyncTask<Object, Object, Object> {



        @Override
        protected void onPreExecute() {
            fragment_app_layout.setVisibility(View.GONE);
            fragment_progress.setVisibility(View.VISIBLE);
            //mProgressHUD = ProgressHUD.show(mainActivity, "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected Object doInBackground(Object... params) {
            try {
                HomeActivity.m_jsonSubscriptions = JSONRPCAPI.getNewSubscriptionList();
                if (   HomeActivity.m_jsonSubscriptions == null) return null;
                Log.d("m_jsonSubscriptions::", "::" + HomeActivity.m_jsonSubscriptions);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {

                fragment_progress.setVisibility(View.GONE);
                fragment_app_layout.setVisibility(View.VISIBLE);
                if (HomeActivity.m_jsonSubscriptions != null && HomeActivity.m_jsonSubscriptions.length() > 0) {

                    subList = new ArrayList<>();
                    for (int i = 0; i < HomeActivity.m_jsonSubscriptions.length(); i++) {
                        String name="", image="";
                        int id=0;
                        JSONObject object = HomeActivity.m_jsonSubscriptions.getJSONObject(i);


                        if (object.has("name")) {
                            name = object.getString("name");
                        }




                        if (object.has("image")) {
                            image = object.getString("image");
                        }

                        if (object.has("id")) {
                            id = object.getInt("id");
                        }



                        subList.add(new ListenGridBean(id, name,image));
                    }

                    SubscriptionListAdapter gameMoreList = new SubscriptionListAdapter(getActivity(), subList);
                    subscription_fragment_list.setAdapter(gameMoreList);





                } else {
                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                        new LoadingSubscription().execute();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }

    public class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.DataObjectHolder> {
        Context context;
        ArrayList<ListenGridBean> subslist;


        public SubscriptionListAdapter(Context context, ArrayList<ListenGridBean> subslist) {
            this.context = context;
            this.subslist = subslist;


        }

        @Override
        public SubscriptionListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.more_list_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final SubscriptionListAdapter.DataObjectHolder holder, int position) {

            final String image_url = subslist.get(position).getImage();
            final String name = subslist.get(position).getName();
            int network_id=subslist.get(position).getId();


            Log.d("data",image_url);

            /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width - (spacing * 5), height - (spacing * 5));
            holder.more_gridview_item.setLayoutParams(layoutParams);*/


            holder.more_gridview_text.setText(name);
            holder.more_gridview_item.setTag(network_id);
            Picasso.with(context
                    .getApplicationContext())
                    .load(image_url)
                    .fit()
                    .placeholder(R.drawable.loader_network).into(holder.more_gridview_item);

            holder.more_gridview_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nId=Integer.parseInt(holder.more_gridview_item.getTag().toString());
                    if (nId!=0) {
                        HomeActivity.toolbarMainContent = name;
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        setAnalyticreport(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, "", "");
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentSubscriptionshowGrid fragmentSubScriptions = FragmentSubscriptionshowGrid.newInstance(""+nId,"");
                        fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentSubScriptions);
                        fragmentTransaction.commit();
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return subslist.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            GridViewItem more_gridview_item;
            TextView more_gridview_text;
            LinearLayout more_gridview_layout;
            public DataObjectHolder(View itemView) {
                super(itemView);
                more_gridview_layout=(LinearLayout)itemView.findViewById(R.id.more_gridview_layout);
                more_gridview_item = (GridViewItem) itemView.findViewById(R.id.more_gridview_item);
                more_gridview_text = (TextView) itemView.findViewById(R.id.more_gridview_text);
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

