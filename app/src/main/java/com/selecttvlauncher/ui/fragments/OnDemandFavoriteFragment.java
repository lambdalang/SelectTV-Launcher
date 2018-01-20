package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.BeanClass.AppListBean;
import com.selecttvlauncher.BeanClass.CategoryBean;
import com.selecttvlauncher.BeanClass.HorizontalListAppManager;
import com.selecttvlauncher.BeanClass.HorizontalListBean;
import com.selecttvlauncher.BeanClass.HorizontalListitemBean;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OnDemandFavoriteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView back_imageView;
    private RecyclerView favorite_left_list;
    private TextView favorite_title_text;
    private Button favorite_done_button;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;

    private ArrayList<AppListBean> recommendedList = new ArrayList<>();
    private ArrayList<AppListBean> tv_breadcastersList = new ArrayList<>();
    private ArrayList<AppListBean> cable_networksList = new ArrayList<>();
    private ArrayList<AppListBean> moviesList = new ArrayList<>();
    private ArrayList<AppListBean> NewsList = new ArrayList<>();
    private ArrayList<AppListBean> subscriptionList = new ArrayList<>();
    private ArrayList<CategoryBean> appCategoriesList = new ArrayList<>();

    private HashMap<String, ArrayList<AppListBean>> app_hash = new HashMap<>();
    private ProgressBar progressBar3, center_progressBar;
    private ArrayList<HorizontalListAppManager> horizontalListdata;
    private ArrayList<HorizontalListitemBean> horizontalListitemBeans;
    public static ArrayList<HorizontalListitemBean> getHorizontalListitemBeans;

    private OnDemandFavoriteFragmentInteractionListener mListener;
    private AQuery aq;
    private LinearLayout content_linearLayout;
    private LayoutInflater mlayoutinflate, mlayoutinflater;
    private LinearLayout recyclerLinear_layout;
    private int height, width;
    private Tracker mTracker;

    public OnDemandFavoriteFragment() {
        // Required empty public constructor
    }


    public static OnDemandFavoriteFragment newInstance(String param1, String param2) {
        OnDemandFavoriteFragment fragment = new OnDemandFavoriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_on_demand_favorite, container, false);
        aq = new AQuery(getActivity());
        back_imageView = (ImageView) view.findViewById(R.id.back_imageView);
        favorite_left_list = (RecyclerView) view.findViewById(R.id.favorite_left_list);
        // content_recycletView=(RecyclerView)view.findViewById(R.id.content_recycletView);
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        Utilities.setAnalytics(mTracker,"AppManager-Youtube");
        favorite_title_text = (TextView) view.findViewById(R.id.favorite_title_text);
        favorite_done_button = (Button) view.findViewById(R.id.favorite_done_button);
        progressBar3 = (ProgressBar) view.findViewById(R.id.progressBar3);
        center_progressBar = (ProgressBar) view.findViewById(R.id.center_progressBar);
        content_linearLayout = (LinearLayout) view.findViewById(R.id.content_linearLayout);
        recyclerLinear_layout = (LinearLayout) view.findViewById(R.id.recyclerLinear_layout);

        mlayoutinflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlayoutinflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        PreferenceManager.setFirstTime(false, getActivity());

        //new getAppList().execute();
        ((HomeActivity) getActivity()).toolbarTextChange("App Manager", "", "", "");
        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentOnDemandAll fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home","");
                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        favorite_done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentOnDemandAll fragmentOnDemandAll = FragmentOnDemandAll.newInstance("home","");
                    fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentOnDemandAll);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        new getAppCatgoryList().execute();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        favorite_left_list.setLayoutManager(linearLayoutManager);
        favorite_left_list.setHasFixedSize(true);
     /*   mLinearLayoutManager= new LinearLayoutManager(getActivity());
        content_recycletView.setLayoutManager(mLinearLayoutManager);
        content_recycletView.setHasFixedSize(true);
*/





       /* gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        content_recycletView.setLayoutManager(gridLayoutManager);
        content_recycletView.setHasFixedSize(true);
        int spanCount = 4; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = true;
        content_recycletView.setFocusable(true);
        content_recycletView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/


        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDemandFavoriteFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDemandFavoriteFragmentInteractionListener) {
            mListener = (OnDemandFavoriteFragmentInteractionListener) context;
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

    public interface OnDemandFavoriteFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDemandFavoriteFragmentInteraction(Uri uri);
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.DataObjectHolder> {
        ArrayList<CategoryBean> list;
        Context context;
        int mlistPosition = 0;

        public ListAdapter(ArrayList<CategoryBean> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ListAdapter.DataObjectHolder holder, final int position) {
            holder.fragment_ondemandlist_items.setText(list.get(position).getName());
            if (position == mlistPosition) {
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }
            holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistPosition = position;
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                    notifyDataSetChanged();

                }
            });
            holder.fragment_ondemandlist_items_layout.setFocusable(true);
            holder.fragment_ondemandlist_items_layout.setTag(list.get(position).getId());
            Utilities.setViewFocus(context, holder.fragment_ondemandlist_items_layout);

            holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistPosition = position;
                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.btn_favourite);
                    notifyDataSetChanged();
                    String tag = holder.fragment_ondemandlist_items_layout.getTag().toString();
                    Log.d("selected cat:::", ":::" + tag);
                    recyclerLinear_layout.removeAllViews();
                    //new getAppList().execute("" + appCategoriesList.get(position).getId());
                    new LoadingCategoryItems().execute(list.get(position).getId());

                   /* if(app_hash.containsKey(tag)){
                        ArrayList<AppListBean> appList=app_hash.get(tag);
                        if(appList.size()>0){
                           content_recycletView.setAdapter(new DemandFavoriteAdapter(getActivity(),appList));
                        }else{
                            new getAppList().execute(""+list.get(position).getId());
                        }
                    }else{
                        new getAppList().execute(""+list.get(position).getId());
                    }*/

                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
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


   /* private class DemandFavoriteAdapter extends RecyclerView.Adapter<DemandFavoriteAdapter.DataObjectHolder> {
        ArrayList<AppListBean> recommendedListt;
        Context context;

        public DemandFavoriteAdapter(Context context, ArrayList<AppListBean> recommendedListt) {
            this.context = context;
            this.recommendedListt = recommendedListt;
        }


        @Override
        public DemandFavoriteAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.ondemand_favorite_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final DemandFavoriteAdapter.DataObjectHolder holder, final int position) {

            String name = recommendedListt.get(position).getName();
            final String app_url = recommendedListt.get(position).getLink();
            String package_name = recommendedListt.get(position).getApp_package();
            if (isInstalledPackageName(package_name)) {
                holder.get_textView.setVisibility(View.INVISIBLE);
                holder.installed_textView.setVisibility(View.VISIBLE);
            } else {
                holder.get_textView.setVisibility(View.VISIBLE);
                holder.installed_textView.setVisibility(View.GONE);
            }

            holder.content_textView.setText(name);
            Log.d("loading image:::", "::" + recommendedListt.get(position).getImage());
            //holder.content_imageView.loadImage(recommendedListt.get(position).getImage());

            holder.content_imageView.setVisibility(View.VISIBLE);
           *//* Picasso.with(context
                    .getApplicationContext())
                    .load(recommendedListt.get(position).getImage())
                    .fit()
                    .placeholder(R.drawable.square_progress).into(holder.content_imageView);*//*
            String url = recommendedListt.get(position).getImage();

            if (!TextUtils.isEmpty(url)) {
                Picasso.with(context
                        .getApplicationContext()).load(url).into(holder.content_imageView);
            } else {
                holder.content_imageView.setImageResource(R.drawable.loader_network);
            }

            //aq.id(holder.content_imageView).image(recommendedListt.get(position).getImage(), false,false);
            holder.get_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(app_url));
                        if (myIntent != null) {
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.rel_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    DemandNetworkDialogFragment dialogFragment = DemandNetworkDialogFragment.newInstance("" + recommendedListt.get(position).getNet_id(), "",""+horizontalListdata.get(iPos).getNetwork_id());

                    dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");

                    Log.d("netword id:::", ":::" + recommendedListt.get(position).getNet_id());

                }
            });
           *//* holder.content_imageView.post(new Runnable() {
                @Override
                public void run() {
                    int width=holder.content_imageView.getMeasuredWidth();
                    Log.d("::width:;",":::"+width);
                    holder.content_textView.setLayoutParams(new LinearLayout.LayoutParams(
                            width,
                            LinearLayout.LayoutParams.WRAP_CONTENT));


                }
            });*//*
        }

        @Override
        public int getItemCount() {
            return recommendedListt.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicSquareImageview content_imageView;
            TextView content_textView, installed_textView, get_textView;
            RelativeLayout rel_layout;

            public DataObjectHolder(View itemView) {
                super(itemView);

                content_imageView = (DynamicSquareImageview) itemView.findViewById(R.id.content_imageView);
                content_textView = (TextView) itemView.findViewById(R.id.content_textView);
                installed_textView = (TextView) itemView.findViewById(R.id.installed_textView);
                get_textView = (TextView) itemView.findViewById(R.id.get_textView);
                rel_layout = (RelativeLayout) itemView.findViewById(R.id.rel_layout);

            }
        }
    }
*/

    private class getAppList extends AsyncTask<String, Object, Object> {
        JSONObject m_response;

        int id;

        @Override
        protected Object doInBackground(String... params) {
            try {
                id = Integer.parseInt(params[0]);
                JSONArray test_arry = JSONRPCAPI.getAppsByCategories(Integer.parseInt(params[0]));
                if (test_arry == null) return null;
                Log.d("selecttv::==>", "apps.list::" + test_arry);

                if (test_arry.length() > 0) {
                    recommendedList = new ArrayList<>();
                    for (int i = 0; i < test_arry.length(); i++) {
                        JSONObject app_object = test_arry.getJSONObject(i);
                        String name = app_object.getString("name");
                        String app_package = app_object.getString("package");
                        String image = app_object.getString("image");
                        String link = app_object.getString("link");
                        String slug = app_object.getString("slug");
                        int id = app_object.getInt("id");
                        String network_id = "";
                        if (app_object.has("network")) {
                            try {
                                Object obj = app_object.get("network");
                                if (obj instanceof JSONObject) {
                                    JSONObject net_object = app_object.getJSONObject("network");
                                    network_id = net_object.getString("id");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            network_id = "";
                        }

                        if (!network_id.equals("")) {
                            recommendedList.add(new AppListBean(name, app_package, image, link, slug, id, network_id));
                        }
                    }
                    app_hash.put(params[0], recommendedList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressBar3.setVisibility(View.VISIBLE);
                recommendedList = new ArrayList<>();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                progressBar3.setVisibility(View.GONE);
//                new LoadingCategoryItems().execute(id);

               /* content_recycletView.setAdapter(new DemandFavoriteAdapter(getActivity(),recommendedList));
                content_recycletView.setNestedScrollingEnabled(false);*/

                /*for (int i = 0; i < recommendedList.size(); i++) {
                    if (!recommendedList.get(i).getNet_id().equals("")) {
                        new LoadingTVNetworkList().execute("" + recommendedList.get(i).getNet_id(), "" + i);
                    }

                }*/


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class LoadingTVNetworkList extends AsyncTask<String, Object, Object> {
        private JSONArray m_jsonNetworkList;
        int anInt;

        @Override
        protected void onPreExecute() {


        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
                anInt = Integer.parseInt(params[1]);
                m_jsonNetworkList = JSONRPCAPI.getTVNetworkList(Integer.parseInt(params[0]), 1000, 0, 0);
                if (m_jsonNetworkList == null) return null;

                horizontalListitemBeans = new ArrayList<>();
                for (int i = 0; i < m_jsonNetworkList.length(); i++) {
                    JSONObject network_list = m_jsonNetworkList.getJSONObject(i);
                    String name = network_list.getString("name");
                    String image = network_list.getString("poster_url");
                    String description = network_list.getString("description");
                    int id = network_list.getInt("id");
                    horizontalListitemBeans.add(new HorizontalListitemBean(id, image, description, name));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if (horizontalListitemBeans.size() > 0) {
                    //addlayouts(recommendedList, horizontalListitemBeans, anInt);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


    private class getAppCatgoryList extends AsyncTask<String, Object, Object> {
        JSONObject m_response;

        @Override
        protected Object doInBackground(String... params) {
            try {
                JSONArray test_arry = JSONRPCAPI.getAppCategories();
                //JSONArray test_arry= JSONRPCAPI.getTvShowListbyCategory();
                if (test_arry == null) return null;
                Log.d("selecttv::==>", "apps.categories::" + test_arry);

                if (test_arry.length() > 0) {
                    for (int i = 0; i < test_arry.length(); i++) {
                        JSONObject app_object = test_arry.getJSONObject(i);
                        //String slug=app_object.getString("slug");
                        int id = app_object.getInt("id");
                        String name = app_object.getString("name");
                        if(name.equalsIgnoreCase("broadcast shows")){
                            name="Recommended";
                        }
                        if(name.equalsIgnoreCase("cable shows")){
                            name="Cable Networks";
                        }
                        appCategoriesList.add(new CategoryBean("", name, id));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                center_progressBar.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                center_progressBar.setVisibility(View.GONE);
                ListAdapter ladapter = new ListAdapter(appCategoriesList, getActivity());
                favorite_left_list.setAdapter(ladapter);
                Log.d("selecttv::==>", "list 0::" + appCategoriesList.get(0).getName());
                new LoadingCategoryItems().execute(appCategoriesList.get(0).getId());
                //new getAppList().execute("" + appCategoriesList.get(0).getId());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    boolean isInstalledPackageName(String packagename) {
        try {
            if (packagename.toLowerCase().equals("google play")) return true;
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> ril = getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);
            if(ril!=null && ril.size()>0){
                for (ResolveInfo ri : ril) {
                    Log.e("Info", "" + ri.activityInfo.packageName);
                    if (!isSystemPackage(ri)) {
                        String key = ri.activityInfo.packageName;
                        if (key.equals(packagename))
                            return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }


    private class DemandContentAdapter extends RecyclerView.Adapter<DemandContentAdapter.DataObjectHolder> {
        ArrayList<HorizontalListBean> horizontalListdata;
        Context context;

        public DemandContentAdapter(Context context, ArrayList<HorizontalListBean> horizontalListdata) {
            this.context = context;
            this.horizontalListdata = horizontalListdata;

        }


        @Override
        public DemandContentAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.appmanager_list_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final DemandContentAdapter.DataObjectHolder holder, final int position) {
            holder.dynamic_image_layout.removeAllViews();
            holder.horizontal_listview_title.setText(horizontalListdata.get(position).getName());


            ViewTreeObserver vto = holder.recycler_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    holder.recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    int width = holder.recycler_layout.getMeasuredWidth();
                    int height = holder.recycler_layout.getMeasuredHeight();

                    for (int i = 0; i < horizontalListdata.size(); i++) {
                        final View itemmlayout = (View) mlayoutinflate.inflate(R.layout.horizontal_list_item, null);

                        DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);
                        horizontal_imageView.loadImage(horizontalListdata.get(position).getData_list().get(i).getImage());
                        LinearLayout.LayoutParams vp;

                        vp = new LinearLayout.LayoutParams((width - 20) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);

                        if (i != 0) {
                            vp.setMargins(20, 0, 0, 0);
                        }
                        horizontal_imageView.setLayoutParams(vp);
                        holder.dynamic_image_layout.addView(itemmlayout);

                        horizontal_imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return horizontalListdata.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private ImageView network_imageView, left_slide, right_slide;
            private TextView get_textView, view_all_text, horizontal_listview_title;
            private LinearLayout dynamic_image_layout, recycler_layout;


            public DataObjectHolder(View itemView) {
                super(itemView);

                network_imageView = (ImageView) itemView.findViewById(R.id.network_imageView);
                left_slide = (ImageView) itemView.findViewById(R.id.left_slide);
                right_slide = (ImageView) itemView.findViewById(R.id.right_slide);

                get_textView = (TextView) itemView.findViewById(R.id.get_textView);
                view_all_text = (TextView) itemView.findViewById(R.id.view_all_text);
                horizontal_listview_title = (TextView) itemView.findViewById(R.id.horizontal_listview_title);

                dynamic_image_layout = (LinearLayout) itemView.findViewById(R.id.dynamic_image_layout);
                recycler_layout = (LinearLayout) itemView.findViewById(R.id.recycler_layout);


            }
        }
    }


    /*private class LoadingAppList extends AsyncTask<Object,Object,Object>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {
            JSONArray applist_array= JSONRPCAPI.getAppList(Integer.parseInt(params[0].toString()));
            if ( applist_array== null) return null;
            else{
                try {
                    String name = "", packages = "",image="", id = "",link="",slug="",network="";


                }catch (Exception e)
                {
                    e.printStackTrace();
                }}
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);




        }
    }*/


    private class LoadingCategoryItems extends AsyncTask<Object, Object, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            horizontalListdata = new ArrayList<>();


            progressBar3.setVisibility(View.VISIBLE);
            recyclerLinear_layout.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object... params) {
            //JSONArray category_array = JSONRPCAPI.getShowcarouselsbycategory(String.valueOf(params[0]), 0);
            JSONArray category_array = JSONRPCAPI.getAppsByCategories(Integer.parseInt(String.valueOf(params[0])));
            if (category_array == null) return null;
            else {
                try {


                    for (int i = 0; i < category_array.length(); i++) {
                        int id = 0;
                        int network_id=0;
                        String name = "", image = "", description = "", title = "",network_image="",network_package="",network_link="",network_name="";
                        ArrayList<HorizontalListitemBean> itemListdata = new ArrayList<>();
                        JSONObject object = category_array.getJSONObject(i);

                        if (object.has("network")) {
                            try {
                                Object net_obj=object.get("network");
                                if(net_obj instanceof JSONObject){
                                    JSONObject network_object=object.getJSONObject("network");
                                    network_id = network_object.getInt("id");
                                    //network_image=network_object.getString("image");
                                    // network_package=network_object.getString("package");
                                    //network_link=network_object.getString("link");
                                    network_name=network_object.getString("name");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }



                        if (object.has("network_image")) {
                            network_image = object.getString("network_image");
                            //Log.d("network_image title::", ":::" + network_image);
                        }else if (object.has("image")) {
                            network_image = object.getString("image");
                            //Log.d("network_image title::", ":::" + network_image);
                        }
                        if (object.has("link")) {
                            network_link = object.getString("link");
                            //Log.d("link::", ":::" + network_image);
                        }
                        if (object.has("package")) {
                            network_package = object.getString("package");
                            //Log.d("package::", ":::" + network_image);
                        }

                        if (object.has("carousel")) {
                            JSONObject carousel_object=object.getJSONObject("carousel");

                            if (carousel_object.has("title")) {
                                title = carousel_object.getString("title");
                                //Log.d("title title::", ":::" + title);
                            }
                            if (TextUtils.isEmpty(title)) {
                                if (carousel_object.has("name")) {
                                    title = carousel_object.getString("name");
                                    // Log.d("name ::", ":::" + title);
                                }
                            }

                            if (carousel_object.has("id")) {
                                id = carousel_object.getInt("id");
                            }

                            if(carousel_object.has("items")){
                                JSONArray itemsarray = carousel_object.getJSONArray("items");
                                for (int j = 0; j < itemsarray.length(); j++) {
                                    String type = "", itemsname = "", itemscarousel_image = "";
                                    int itemsid = 0;
                                    JSONObject itemsobject = itemsarray.getJSONObject(j);
                                    if (itemsobject.has("type")) {
                                        type = itemsobject.getString("type");
                                    }

                                    if (itemsobject.has("id")) {
                                        itemsid = itemsobject.getInt("id");
                                    }
                                    if (itemsobject.has("name")) {
                                        itemsname = itemsobject.getString("name");
                                    }
                                    if (itemsobject.has("carousel_image")) {
                                        itemscarousel_image = itemsobject.getString("carousel_image");
                                    }
                                    itemListdata.add(new HorizontalListitemBean(itemsid, itemscarousel_image, type, itemsname));
                                }
                                if (itemListdata != null && itemListdata.size() != 0) {
                                    horizontalListdata.add(new HorizontalListAppManager(id,network_id, title, "", network_image,network_package,network_link,network_name,itemListdata));
                                }
                            }


                        }else if (object.has("items")) {
                            JSONArray itemsarray = object.getJSONArray("items");
                            for (int j = 0; j < itemsarray.length(); j++) {
                                String type = "", itemsname = "", itemscarousel_image = "";
                                int itemsid = 0;
                                JSONObject itemsobject = itemsarray.getJSONObject(j);
                                if (itemsobject.has("type")) {
                                    type = itemsobject.getString("type");
                                }

                                if (itemsobject.has("id")) {
                                    itemsid = itemsobject.getInt("id");
                                }
                                if (itemsobject.has("name")) {
                                    itemsname = itemsobject.getString("name");
                                }
                                if (itemsobject.has("carousel_image")) {
                                    itemscarousel_image = itemsobject.getString("carousel_image");
                                }
                                itemListdata.add(new HorizontalListitemBean(itemsid, itemscarousel_image, type, itemsname));
                            }
                            if (itemListdata != null && itemListdata.size() != 0) {
                                horizontalListdata.add(new HorizontalListAppManager(id,network_id, title, "", network_image,network_package,network_link,network_name,itemListdata));
                            }


                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressBar3.setVisibility(View.GONE);
            recyclerLinear_layout.setVisibility(View.VISIBLE);
            //content_recycletView.setAdapter(new DemandContentAdapter(getActivity(),horizontalListdata));

            addlayouts();


        }
    }



    private void addlayouts() {
        try {
            recyclerLinear_layout.removeAllViews();
            for (int i = 0; i<horizontalListdata.size()&&i<25; i++) {

                final int iPos = i;

                final View itemlayout = (View) mlayoutinflate.inflate(R.layout.appmanager_list_item, null);

                final ImageView network_imageView = (ImageView) itemlayout.findViewById(R.id.network_imageView);
                ImageView left_slide = (ImageView) itemlayout.findViewById(R.id.left_slide);
                ImageView right_slide = (ImageView) itemlayout.findViewById(R.id.right_slide);

                TextView get_textView = (TextView) itemlayout.findViewById(R.id.get_textView);
                TextView installed_textView = (TextView) itemlayout.findViewById(R.id.installed_textView);
                TextView view_all_text = (TextView) itemlayout.findViewById(R.id.view_all_text);
                TextView horizontal_listview_title = (TextView) itemlayout.findViewById(R.id.horizontal_listview_title);

                final LinearLayout dynamic_image_layout = (LinearLayout) itemlayout.findViewById(R.id.dynamic_image_layout);
                final LinearLayout recycler_layout = (LinearLayout) itemlayout.findViewById(R.id.recycler_layout);

                dynamic_image_layout.removeAllViews();
                final HorizontalScrollView horizontal_listview = (HorizontalScrollView) itemlayout.findViewById(R.id.horizontal_listview);

                horizontal_listview_title.setText(Utilities.stripHtml(horizontalListdata.get(i).getName()));
                network_imageView.setTag(i);
                get_textView.setTag(i);
                final String image_url=horizontalListdata.get(i).getNetwork_image();
                Log.d("network image:::",":::url:::"+get_textView);
                if(!TextUtils.isEmpty(image_url)){
                    aq.id(network_imageView).image(image_url);
                }




                final LinearLayoutManager linear_layoutManager
                        = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                dynamic_image_layout.removeAllViews();
                final String package_name=horizontalListdata.get(iPos).getNetwork_package();
                network_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos=Integer.parseInt(network_imageView.getTag().toString());
                        FragmentManager fm = getActivity().getSupportFragmentManager();


                        if (!isInstalledPackageName(package_name)) {

                            AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance(""+horizontalListdata.get(pos).getNetwork_name(),""+horizontalListdata.get(iPos).getNetwork_image(),horizontalListdata.get(iPos).getNetwork_link());

                            dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                        }

                    }
                });


                if (isInstalledPackageName(package_name)) {
                    get_textView.setVisibility(View.INVISIBLE);
                    installed_textView.setVisibility(View.VISIBLE);
                } else {
                    get_textView.setVisibility(View.VISIBLE);
                    installed_textView.setVisibility(View.GONE);
                }
                get_textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos=Integer.parseInt(network_imageView.getTag().toString());
                        String package_name=horizontalListdata.get(pos).getNetwork_package();
                        String app_url=horizontalListdata.get(pos).getNetwork_link();
                       /* try {
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(app_url));
                            if (myIntent != null) {
                                startActivity(myIntent);
                            } else {
                                Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        if (!isInstalledPackageName(package_name)) {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance(""+horizontalListdata.get(pos).getNetwork_name(),""+horizontalListdata.get(iPos).getNetwork_image(),horizontalListdata.get(iPos).getNetwork_link());

                            dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                        }
                    }
                });



              /*  int spanCount = listData.size(); // 3 columns
                spacing = 20; // 50px*/


                boolean includeEdge = false;
                int layoutwidth = recycler_layout.getLayoutParams().width;
                Log.d("layoutwidth::", "layoutwidth" + layoutwidth);
                Log.d("listData::", "size" + horizontalListdata.size());

                try {
                    ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            try {
                                recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                width = recycler_layout.getMeasuredWidth();
                                height = recycler_layout.getMeasuredHeight();


                                for (int l = 0; l < horizontalListdata.get(iPos).getData_list().size()&&l < 6; l++) {
                                    final int ll=l;

                                    final View itemmlayout = (View) mlayoutinflater.inflate(R.layout.horizontal_list_item, null);

                                    DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);

                                    LinearLayout.LayoutParams vp;
                                    if(checkIsTablet()){
                                        vp = new LinearLayout.LayoutParams((width - 40) / 3, LinearLayout.LayoutParams.WRAP_CONTENT);


                                        if (l != 0) {
                                            vp.setMargins(20, 0, 0, 0);
                                        }
                                    }else{

                                        if(horizontalListdata.get(iPos).getData_list().get(l).getType().equalsIgnoreCase("m"))
                                        {
                                            vp = new LinearLayout.LayoutParams((width - 20) / 3, LinearLayout.LayoutParams.WRAP_CONTENT);


                                            if (l != 0) {
                                                vp.setMargins(20, 0, 0, 0);
                                            }
                                        }else
                                        {
                                            vp = new LinearLayout.LayoutParams((width - 20) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);


                                            if (l != 0) {
                                                vp.setMargins(20, 0, 0, 0);
                                            }
                                        }

                                    }


                                    String image_url = horizontalListdata.get(iPos).getData_list().get(l).getImage();
                                    Log.d("image_url::", "image_url" + image_url);
                                    horizontal_imageView.loadImage(image_url);
                                    horizontal_imageView.setLayoutParams(vp);
                                    final int finalL = l;
                                    Utilities.setViewFocus(getActivity(), itemmlayout);
                                    itemmlayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!isInstalledPackageName(package_name)) {
                                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                                AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance(""+horizontalListdata.get(iPos).getNetwork_name(),""+horizontalListdata.get(iPos).getNetwork_image(),horizontalListdata.get(iPos).getNetwork_link());

                                                dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                                            }else{
                                                FragmentSubScriptionList.sSelectedMenu = "";
                                                HomeActivity.toolbarSubContent = horizontalListdata.get(iPos).getData_list().get(ll).getName();
                                                ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                                                ((HomeActivity) getActivity()).SwapMovieFragment(true);

                                                if(horizontalListdata.get(iPos).getData_list().get(ll).getType().equalsIgnoreCase("s")){
                                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                    FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, horizontalListdata.get(iPos).getData_list().get(ll).getId());
                                                    fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                                    fragmentTransaction.commit();
                                                }else{
                                                    if(horizontalListdata.get(iPos).getData_list().get(ll).getType().equalsIgnoreCase("m")){
                                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, horizontalListdata.get(iPos).getData_list().get(ll).getId());
                                                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                                                        fragmentTransaction.commit();
                                                    }
                                                }

                                            }

                                        }
                                    });
                                    dynamic_image_layout.addView(itemmlayout);

                                }


                                Log.d("layoutwidth::", "width" + width);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    Utilities.setTextFocus(getActivity(), view_all_text);
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
                    view_all_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            DemandNetworkDialogFragment dialogFragment = DemandNetworkDialogFragment.newInstance("" + horizontalListdata.get(iPos).getId(), "" + horizontalListdata.get(iPos).getNetwork_id());

                            dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                recyclerLinear_layout.addView(itemlayout);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkIsTablet() {

        if(getActivity()!=null)
        {
            try {
                String inputSystem;
                inputSystem = android.os.Build.ID;
                Log.d("hai", inputSystem);
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                int width = display.getWidth();  // deprecated
                int height = display.getHeight();  // deprecated
                Log.d("hai", width + "");
                Log.d("hai", height + "");
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(width / dm.xdpi, 2);
                double y = Math.pow(height / dm.ydpi, 2);
                double screenInches = Math.sqrt(x + y);
                Log.d("hai", "Screen inches : " + screenInches + "");
                return screenInches > 6.0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        return false;
    }


    /*private void addlayouts(final ArrayList<AppListBean> recommendedList, final ArrayList<HorizontalListitemBean> horizontalListitemBeans, final int anInt) {


        try {
            final View itemlayout = (View) mlayoutinflate.inflate(R.layout.appmanager_list_item, null);

            final ImageView network_imageView = (ImageView) itemlayout.findViewById(R.id.network_imageView);
            ImageView left_slide = (ImageView) itemlayout.findViewById(R.id.left_slide);
            ImageView right_slide = (ImageView) itemlayout.findViewById(R.id.right_slide);

            TextView get_textView = (TextView) itemlayout.findViewById(R.id.get_textView);
            TextView view_all_text = (TextView) itemlayout.findViewById(R.id.view_all_text);
            TextView horizontal_listview_title = (TextView) itemlayout.findViewById(R.id.horizontal_listview_title);

            final LinearLayout dynamic_image_layout = (LinearLayout) itemlayout.findViewById(R.id.dynamic_image_layout);
            final LinearLayout recycler_layout = (LinearLayout) itemlayout.findViewById(R.id.recycler_layout);

            dynamic_image_layout.removeAllViews();
            final HorizontalScrollView horizontal_listview = (HorizontalScrollView) itemlayout.findViewById(R.id.horizontal_listview);

//                horizontal_listview_title.setText(Utilities.stripHtml(horizontalListdata.get(i).getName()));


            Picasso.with(getActivity()).load(recommendedList.get(anInt).getImage())
                    .networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.loader_show).into(network_imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(getActivity())
                            .load(recommendedList.get(anInt).getImage()).placeholder(R.drawable.loader_show)
                            .into(network_imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
            });

//            network_imageView.loadImage(recommendedList.get(anInt).getImage());
            final LinearLayoutManager linear_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            dynamic_image_layout.removeAllViews();

            network_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    DemandNetworkDialogFragment dialogFragment = DemandNetworkDialogFragment.newInstance(recommendedList.get(anInt).getNet_id(), "0");

                    dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                }
            });
            get_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recommendedList.get(anInt).getLink()));
                        if (myIntent != null) {
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


*//*

                int spanCount = listData.size(); // 3 columns
                spacing = 20; // 50px
*//*

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


                        for (int l = 0; l < horizontalListitemBeans.size(); l++) {

                            final View itemmlayout = (View) mlayoutinflater.inflate(R.layout.horizontal_list_item, null);

                            DynamicImageView horizontal_imageView = (DynamicImageView) itemmlayout.findViewById(R.id.horizontal_imageView);
                            String image_url = horizontalListitemBeans.get(l).getImage();
                            horizontal_imageView.loadImage(image_url);
                            LinearLayout.LayoutParams vp;
                            if (checkIsTablet()) {
                                vp = new LinearLayout.LayoutParams((width - 40) / 3, LinearLayout.LayoutParams.WRAP_CONTENT);


                                if (l != 0) {
                                    vp.setMargins(20, 0, 0, 0);
                                }
                            } else {
                                vp = new LinearLayout.LayoutParams((width - 20) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);


                                if (l != 0) {
                                    vp.setMargins(20, 0, 0, 0);
                                }
                            }



                            Log.d("image_url::", "image_url" + image_url);
                            horizontal_imageView.setLayoutParams(vp);
                            Utilities.setViewFocus(getActivity(), itemmlayout);
                            final int finalL1 = l;
                            itemmlayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    DemandNetworkDialogFragment dialogFragment = DemandNetworkDialogFragment.newInstance("" + recommendedList.get(anInt).getNet_id(), "0");
                                    dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");

                                }
                            });
                            dynamic_image_layout.addView(itemmlayout);

                        }


                        Log.d("layoutwidth::", "width" + width);

                    }
                });

                Utilities.setTextFocus(getActivity(), view_all_text);
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

                view_all_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        DemandNetworkDialogFragment dialogFragment = DemandNetworkDialogFragment.newInstance("" + recommendedList.get(anInt).getNet_id(), "0");
                        dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            recyclerLinear_layout.addView(itemlayout);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
