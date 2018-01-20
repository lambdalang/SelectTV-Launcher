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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.selecttvlauncher.BeanClass.SubScriptionListBean;
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


public class FragmentSubscriptionsGrid extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView grid;
    GridLayoutManager gridLayoutManager;
    SubScriptionGridAdapter subScriptionGridAdapter;
    ArrayList<SubScriptionListBean> subScriptionListBeans = new ArrayList<>();
    public static int networkid=0;
    private ProgressBar progressBar2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSubscriptionsGrid.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSubscriptionsGrid newInstance(String param1, String param2) {
        FragmentSubscriptionsGrid fragment = new FragmentSubscriptionsGrid();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSubscriptionsGrid() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_subscriptions_grid, container, false);
        progressBar2=(ProgressBar)view.findViewById(R.id.progressBar2);
        grid = (RecyclerView) view.findViewById(R.id.grid);
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        grid.setHasFixedSize(true);
        grid.setLayoutManager(gridLayoutManager);
        int spanCount = 4; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        grid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        if (FragmentSubScriptionList.sSelectedMenu.equals(Constants.NETWORK)) {
            subScriptionListBeans = HomeActivity.mSubscriptiondataList;
        } else {
            subScriptionListBeans = HomeActivity.subScriptionBeans.get(Integer.parseInt(mParam1)).getSubScriptionSubLists().get(Integer.parseInt(mParam2)).getSubScriptionSubLists();

        }

        subScriptionGridAdapter = new SubScriptionGridAdapter(subScriptionListBeans, getActivity());
        grid.setAdapter(subScriptionGridAdapter);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public class SubScriptionGridAdapter extends RecyclerView.Adapter<SubScriptionGridAdapter.DataObjectHolder> {
        ArrayList<SubScriptionListBean> subScriptionListBeans;
        Context context;

        public SubScriptionGridAdapter(ArrayList<SubScriptionListBean> subScriptionListBeans, Context context) {
            this.subScriptionListBeans = subScriptionListBeans;
            this.context = context;

        }

        @Override
        public SubScriptionGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(SubScriptionGridAdapter.DataObjectHolder holder, final int position) {
            holder.imageView.loadImage(subScriptionListBeans.get(position).getImage());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("subscription", "id" + subScriptionListBeans.get(position).getId());


                    if (subScriptionListBeans.get(position).getType().equalsIgnoreCase("n")||subScriptionListBeans.get(position).getType().equalsIgnoreCase("l")) {
                        FragmentSubScriptions.sContent_position = Constants.SUBSCRIPTION_CONTENT;
                        networkid=subScriptionListBeans.get(position).getId();
                        new LoadingTVNetworkList().execute();
                    }  else if (subScriptionListBeans.get(position).getType().equalsIgnoreCase("m")) {

                            FragmentSubScriptionList.sSelectedMenu ="";
                            ((HomeActivity) getActivity()).SwapMovieFragment(true);
                            HomeActivity.toolbarSubContent = subScriptionListBeans.get(position).getName();
                            ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.MOVIE_DETAILS, subScriptionListBeans.get(position).getId());
                            fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                            fragmentTransaction.commit();

                    }else {

                        FragmentSubScriptionList.sSelectedMenu ="";
                        ((HomeActivity) getActivity()).SwapMovieFragment(true);
                        HomeActivity.toolbarSubContent = subScriptionListBeans.get(position).getName();
                        ((HomeActivity) getActivity()).toolbarTextChange(HomeActivity.toolbarGridContent, HomeActivity.toolbarMainContent, HomeActivity.toolbarSubContent, "");
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentMovieMain fragmentMovieMain = FragmentMovieMain.newInstance(Constants.SHOWS_DETAILS, subScriptionListBeans.get(position).getId());
                        fragmentTransaction.replace(R.id.activity_movie_fragemnt_layout, fragmentMovieMain);
                        fragmentTransaction.commit();

                    }





                }
            });
        }

        @Override
        public int getItemCount() {
            return subScriptionListBeans.size();
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


    private class LoadingTVNetworkList extends AsyncTask<String, Object, Object> {
        private JSONArray m_jsonNetworkList;
        int id;
        String name, poster_url, description;

        @Override
        protected void onPreExecute() {
            progressBar2.setVisibility(View.VISIBLE);

            grid.setVisibility(View.INVISIBLE);

        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
                m_jsonNetworkList = JSONRPCAPI.getTVNetworkList(networkid, 1000, 0, 2);
                if (m_jsonNetworkList  == null) return null;
                Log.d("NetworkList::", "Selected Network list::" + m_jsonNetworkList);
                HomeActivity.mSubscriptiondataList.clear();
                for (int i = 0; i < m_jsonNetworkList.length(); i++) {
                    try {
                        JSONObject jSubscriptionCategory = m_jsonNetworkList.getJSONObject(i);

                        if (jSubscriptionCategory.has("id")) {
                            id = jSubscriptionCategory.getInt("id");
                        }
                        if (jSubscriptionCategory.has("name")) {
                            name = jSubscriptionCategory.getString("name");
                        }
                        if (jSubscriptionCategory.has("description")) {
                            description = jSubscriptionCategory.getString("description");
                        }

                        if (jSubscriptionCategory.has("poster_url")) {
                            poster_url = jSubscriptionCategory.getString("poster_url");
                        }
                        HomeActivity.mSubscriptiondataList.add(new SubScriptionListBean(id, name, poster_url, description));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                progressBar2.setVisibility(View.GONE);
                grid.setVisibility(View.VISIBLE);
                if (m_jsonNetworkList.length() > 0) {
                    FragmentSubScriptionList.sSelectedMenu = Constants.NETWORK;
                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                    FragmentSubscriptionsGrid fragmentSubscriptionsGrid = FragmentSubscriptionsGrid.newInstance("", "");
                    fragmentTransaction_list.replace(R.id.fragment_subscription_content, fragmentSubscriptionsGrid);
                    fragmentTransaction_list.commit();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mProgressHUD.dismiss();
        }

    }


}
