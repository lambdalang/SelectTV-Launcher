package com.selecttvlauncher.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.selecttvlauncher.Adapter.SearchGridAdapter;
import com.selecttvlauncher.BeanClass.GridBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;
import com.selecttvlauncher.tools.GridSpacingItemDecoration;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.dialogs.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/6/2016.
 */
public class SearchGridFragment extends Fragment implements SearchGridAdapter.SearchGridClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String grid_type;
    private String data;

    private RecyclerView grid;
    private GridLayoutManager layoutManager;
    private ArrayList<Integer> images=new ArrayList<Integer>();
    private boolean network=false;
    private JSONArray m_jsonNetworkList;
    private String carousel_image;
    public static JSONObject m_JsonscchedulledRelated;
    private OnSearchGridFragmentInteractionListener mListener;

    public SearchGridFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static SearchGridFragment newInstance(String param1, String param2) {
        SearchGridFragment fragment = new SearchGridFragment();
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
            grid_type = getArguments().getString(ARG_PARAM1);
            data = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_grid, container, false);

        grid=(RecyclerView)rootview.findViewById(R.id.grid);

        Log.d("grid_typegrid_type::", "grid_type" + grid_type);

        try {
            if(grid_type.equalsIgnoreCase(Constants.SEARCH_ACTOR)||grid_type.equalsIgnoreCase(Constants.SEARCH_TVSTATIONS)||grid_type.equalsIgnoreCase(Constants.SEARCH_LIVE)||grid_type.equalsIgnoreCase(Constants.SEARCH_STATIONS)||grid_type.equalsIgnoreCase(Constants.SEARCH_RADIO)||grid_type.equalsIgnoreCase(Constants.SEARCH_MUSIC)){

                ArrayList<GridBean> grid_list=new ArrayList<>();
                grid_list.clear();
                if(!TextUtils.isEmpty(data)){
                    JSONArray data_array=new JSONArray(data);


                    String id="",name="",image="",description="",type="";
                    for (int i=0;i<data_array.length();i++){
                        JSONObject object=data_array.getJSONObject(i);
                        if(object.has("id")){
                            id=object.getString("id");
                        }
                        if(object.has("name")){
                            name=object.getString("name");
                        }

                        if(object.has("carousel_image"))
                        {
                            image=object.getString("carousel_image");
                        }else
                        {
                            if(object.has("image"))
                            {
                                image=object.getString("image");
                            }
                        }


                        if(object.has("type"))
                        {
                            type=object.getString("type");

                            Log.d("type","grid"+type);

                        }

                        grid_list.add(new GridBean(id,name,image,description,type));

                    }
                }

                layoutManager = new GridLayoutManager(getActivity(), 4);
                layoutManager.setOrientation(GridLayoutManager.VERTICAL);
                grid.hasFixedSize();
                grid.setLayoutManager(layoutManager);
                int spanCount = 4; // 3 columns
                int spacing = 25; // 50px
                boolean includeEdge = true;
                grid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

                SearchGridAdapter adpater=new SearchGridAdapter(grid_list,getContext(),SearchGridFragment.this);
                grid.setAdapter(adpater);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchGridFragmentInteractionListener ) {
            mListener = (OnSearchGridFragmentInteractionListener ) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnSearchGridFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchloadChannel(String cid);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onsearchGriditemSelected(String search, String id, String type, String name) {
        try {
            if(mListener!=null){
                ((HomeActivity) getActivity()).SwapSearchFragment(false);
                mListener.onSearchloadChannel(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private class LoadingSchedulerRelatedData extends AsyncTask<String, Object, Object> {
        private ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {

            mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
        }


        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {
            try {
                m_JsonscchedulledRelated = JSONRPCAPI.getschedullerRelated(Integer.parseInt(params[0]));
                if ( m_JsonscchedulledRelated  == null) return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object params) {




            try {
                ((HomeActivity) getActivity()).SwapSearchFragment(false);
                Log.d("getschedullerRelated", "values" + m_JsonscchedulledRelated);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                ChannelFragment channelFragment = ChannelFragment.newInstance(Constants.SEARCH_STATIONS, m_JsonscchedulledRelated.toString());
                fragmentTransaction.replace(R.id.activity_homescreen_fragemnt_layout, channelFragment);
                fragmentTransaction.commit();
                mProgressHUD.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

