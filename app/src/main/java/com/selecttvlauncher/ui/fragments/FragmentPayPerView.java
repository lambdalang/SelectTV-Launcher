package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.content.Context;
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

import com.selecttvlauncher.BeanClass.SideMenu;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.activities.HomeActivity;

import java.util.ArrayList;


public class FragmentPayPerView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView fragment_payperview_items;
    private ImageView fragment_payperview_prev_icon;


    ArrayList<SideMenu> payperViewList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private int mSelectedItem;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPayPerView.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPayPerView newInstance(String param1, String param2) {
        FragmentPayPerView fragment = new FragmentPayPerView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentPayPerView() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_pay_per_view, container, false);
        fragment_payperview_prev_icon = (ImageView) view.findViewById(R.id.fragment_payperview_prev_icon);
        fragment_payperview_items = (RecyclerView) view.findViewById(R.id.fragment_payperview_items);

        listDataReset();
        return view;
    }


    public void listDataReset() {
        try {

            payperViewList.clear();
            payperViewList.add(new SideMenu("", "All", "",""));
            payperViewList.add(new SideMenu("", "Movies", "",""));
            payperViewList.add(new SideMenu("", "TV Shows", "",""));
            HomeActivity.mMovieorSeriesName = payperViewList.get(0).getName();
            mLayoutManager = new LinearLayoutManager(getActivity());
            fragment_payperview_items.setLayoutManager(mLayoutManager);
            PayperviewAdapter payperviewAdapter = new PayperviewAdapter(payperViewList, getActivity());
            fragment_payperview_items.setAdapter(payperviewAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class PayperviewAdapter extends RecyclerView.Adapter<PayperviewAdapter.DataObjectHolder> {
        ArrayList<SideMenu> payperViewList;
        Context context;

        public PayperviewAdapter(ArrayList<SideMenu> payperViewList, Context context) {

            this.payperViewList = payperViewList;
            this.context = context;

        }

        @Override
        public PayperviewAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_fragment_ondemandalllist_items, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final PayperviewAdapter.DataObjectHolder holder, final int position) {

            holder.fragment_ondemandlist_items.setText(payperViewList.get(position).getName());
            final String menu_text=payperViewList.get(position).getName();
            if (position == mSelectedItem) {
                holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
            } else {
                holder.fragment_ondemandlist_items.setBackgroundResource(0);
            }


            holder.fragment_ondemandlist_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = position;

                    holder.fragment_ondemandlist_items.setBackgroundResource(R.drawable.menubg);
                    notifyDataSetChanged();
                    HomeActivity.mMovieorSeriesName = payperViewList.get(position).getName();

                    switch (menu_text) {
                        case "All":
                            if (HomeActivity.m_jsonTvshowfeaturedCarousels != null && HomeActivity.m_jsonTvshowfeaturedCarousels.length() > 0) {
                                if(HomeActivity.getmFragmentOnDemandAll()!=null){
                                    FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                                    HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowfeaturedCarousels.toString());
                                    fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                                    fragmentTransaction_list.commit();
                                }

                            } else {


                                new LoadingTvShowTVfeaturedCarousels().execute();
                            }

                            break;
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return payperViewList.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {

            LinearLayout fragment_ondemandlist_items_layout;
            TextView fragment_ondemandlist_items;

            public DataObjectHolder(View itemView) {
                super(itemView);

                fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout);
                fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items);

                Utilities.setViewFocus(getActivity(),fragment_ondemandlist_items);
            }
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public class LoadingTvShowTVfeaturedCarousels extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(HomeActivity.getmFragmentOnDemandAll()!=null){
                FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
                DialogFragment dialog_fragment = new DialogFragment();
                network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
                network_fragmentTransaction.commit();
            }

        }


        @Override
        protected Object doInBackground(Object... params) {
            HomeActivity.m_jsonTvshowfeaturedCarousels = JSONRPCAPI.getTVfeaturedCarousels(HomeActivity.mostwatched, HomeActivity.mfreeorall);
            if ( HomeActivity.m_jsonTvshowfeaturedCarousels   == null) return null;
            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonTvshowfeaturedCarousels);
            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if (HomeActivity.m_jsonTvshowfeaturedCarousels != null && HomeActivity.m_jsonTvshowfeaturedCarousels.length() > 0) {
                    if(HomeActivity.getmFragmentOnDemandAll()!=null){
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowfeaturedCarousels.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                } else {


                    new LoadingTvShowTVfeaturedCarousels().execute();
                }


            } catch (Exception e) {
                e.printStackTrace();
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


}
