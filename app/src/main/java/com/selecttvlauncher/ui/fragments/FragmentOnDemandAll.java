package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.ui.activities.HomeActivity;

public class FragmentOnDemandAll extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentOndemandallList fragmentOndemandallList;
    FragmentOnDemandAllContent fragmentOnDemandAllContent;
    private FragmentTransaction fragmentTransaction;
    public static FrameLayout fragment_ondemand_pagerandlist;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentOnDemandAll.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentOnDemandAll newInstance(String param1, String param2) {
        FragmentOnDemandAll fragment = new FragmentOnDemandAll();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentOnDemandAll() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_on_demand_all, container, false);
        ((HomeActivity) getActivity()).setmFragmentOnDemandAll(this);
        fragment_ondemand_pagerandlist = (FrameLayout) view.findViewById(R.id.fragment_ondemand_pagerandlist);
        try {
            HomeActivity.isSearchClick = false;
            if (HomeActivity.getmFragmentOnDemandAll() != null) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentOndemandallList = FragmentOndemandallList.newInstance(mParam1, mParam2);
                fragmentTransaction.replace(R.id.fragment_ondemand_alllist, fragmentOndemandallList);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public class LoadingTvShowTVfeaturedCarousels extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FragmentTransaction network_fragmentTransaction = getFragmentManager().beginTransaction();
            DialogFragment dialog_fragment = new DialogFragment();
            network_fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, dialog_fragment);
            network_fragmentTransaction.commit();
        }

        @Override
        protected Object doInBackground(Object... params) {

            HomeActivity.m_jsonTvshowfeaturedCarousels = JSONRPCAPI.getTVfeaturedCarousels(HomeActivity.mostwatched, HomeActivity.mfreeorall);
            if (HomeActivity.m_jsonTvshowfeaturedCarousels == null) return null;
            Log.d("Genre::", "Genrelist::" + HomeActivity.m_jsonTvshowfeaturedCarousels);
            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {

                if (HomeActivity.isPayperview.equals("Pay Per View")) {

                    if (HomeActivity.m_jsonTvshowfeaturedCarousels != null && HomeActivity.m_jsonTvshowfeaturedCarousels.length() > 0) {
                        FragmentTransaction fragmentTransaction_list = getFragmentManager().beginTransaction();
                        HorizontalListsFragment listfragment = HorizontalListsFragment.newInstance(Constants.NETWORK, HomeActivity.m_jsonTvshowfeaturedCarousels.toString());
                        fragmentTransaction_list.replace(R.id.fragment_ondemand_pagerandlist, listfragment);
                        fragmentTransaction_list.commit();
                    }

                } else {
                    if (HomeActivity.m_jsonTvshowfeaturedCarousels != null && HomeActivity.m_jsonTvshowfeaturedCarousels.length() > 0) {

                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentOnDemandAllContent = FragmentOnDemandAllContent.newInstance(Constants.SHOWS, HomeActivity.m_jsonTvshowfeaturedCarousels.toString());
                        fragmentTransaction.replace(R.id.fragment_ondemand_pagerandlist, fragmentOnDemandAllContent);
                        fragmentTransaction.commit();


                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
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
        try {
            ((HomeActivity) getActivity()).setmFragmentOnDemandAll(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void hideContent(boolean b) {
        try {
            if (b) {
                fragment_ondemand_pagerandlist.setVisibility(View.INVISIBLE);
            } else {
                fragment_ondemand_pagerandlist.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
