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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.selecttvlauncher.BeanClass.ListenGridBean;
import com.selecttvlauncher.BeanClass.ShowBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentSubscriptionshowGrid extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentSubscriptionshowGrid() {
        // Required empty public constructor
    }

    private ImageView fragment_prev_icon;
    private RecyclerView show_fragment_list;
    private ArrayList<ListenGridBean> subList=new ArrayList<>();
    private LinearLayout fragment_app_layout;
    private ProgressBar fragment_progress;
    private ArrayList<ShowBean> showsList=new ArrayList<>();


    public static FragmentSubscriptionshowGrid newInstance(String param1, String param2) {
        FragmentSubscriptionshowGrid fragment = new FragmentSubscriptionshowGrid();
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
        View view = inflater.inflate(R.layout.fragment_fragment_subscriptionshow_grid, container, false);

        fragment_app_layout = (LinearLayout) view.findViewById(R.id.show_fragment_app_layout);
        fragment_prev_icon = (ImageView) view.findViewById(R.id.fragment_prev_icon);
        show_fragment_list = (RecyclerView) view.findViewById(R.id.show_fragment_list);
        fragment_progress = (ProgressBar) view.findViewById(R.id.show_fragment_progress);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        show_fragment_list.hasFixedSize();
        show_fragment_list.setLayoutManager(layoutManager);
        int spanCount = 4; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        show_fragment_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        new LoadingShows().execute();
        fragment_prev_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentSubscriptionNew fragmentSubScriptions = new FragmentSubscriptionNew();
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, fragmentSubScriptions);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class LoadingShows extends AsyncTask<Object, Object, Object> {



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
                JSONArray net_array = JSONRPCAPI.getTVNetworkList(Integer.parseInt(mParam1),100,0);
                String name = "", image = "", description = "", type = "";
                int id=0;
                Log.d("shows::", "::" + net_array);
                if(net_array!=null){
                    if(net_array.length()>0){
                        showsList=new ArrayList<>();
                        for (int i = 0; i < net_array.length(); i++) {
                            JSONObject object = net_array.getJSONObject(i);
                            if (object.has("id")) {
                                id = object.getInt("id");
                            }
                            if (object.has("name")) {
                                name = object.getString("name");
                            }
                            if (object.has("poster_url")) {
                                image = object.getString("poster_url");
                            }

                            if (object.has("description")) {
                                description = object.getString("description");
                            }
                            showsList.add(new ShowBean(id, name, image, description));
                        }
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

                fragment_progress.setVisibility(View.GONE);
                fragment_app_layout.setVisibility(View.VISIBLE);
                if (showsList.size()>0) {

                    ShowsListAdapter showadapter = new ShowsListAdapter(getActivity(), showsList);
                    show_fragment_list.setAdapter(showadapter);





                } else {
                    if (HomeActivity.isNetworkAvailable(getActivity()) || HomeActivity.isWifiAvailable(getActivity())) {
                        new LoadingShows().execute();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }

    public class ShowsListAdapter extends RecyclerView.Adapter<ShowsListAdapter.DataObjectHolder> {
        Context context;
        ArrayList<ShowBean> subslist;


        public ShowsListAdapter(Context context, ArrayList<ShowBean> subslist) {
            this.context = context;
            this.subslist = subslist;


        }

        @Override
        public ShowsListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final ShowsListAdapter.DataObjectHolder holder, int position) {

            final String image_url = subslist.get(position).getImage();
            final String name = subslist.get(position).getName();
            final int show_id=subslist.get(position).getId();


            Log.d("data",image_url);

            /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width - (spacing * 5), height - (spacing * 5));
            holder.more_gridview_item.setLayoutParams(layoutParams);*/



            holder.imageView.setTag(show_id);
            holder.imageView.loadImage(image_url);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int sId=Integer.parseInt(holder.imageView.getTag().toString());
                    if (sId!=0) {

                        HomeActivity.toolbarSubContent = name;
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");

                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, sId);
                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
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
            private final DynamicImageView imageView;
            public DataObjectHolder(View itemView) {
                super(itemView);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);
            }
        }
    }
}
