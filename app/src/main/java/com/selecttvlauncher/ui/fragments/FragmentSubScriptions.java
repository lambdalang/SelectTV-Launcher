package com.selecttvlauncher.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentSubScriptions extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private JSONArray m_jsonSubscriptionCategories;
public static String sContent_position= Constants.SUBSCRIPTION_MAINCONTENT;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSubScriptions.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSubScriptions newInstance(String param1, String param2) {
        FragmentSubScriptions fragment = new FragmentSubScriptions();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSubScriptions() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_sub_scriptions, container, false);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentSubScriptionList fragmentSubScriptionList = new FragmentSubScriptionList();
        fragmentTransaction.replace(R.id.fragment_subscription_list, fragmentSubScriptionList);
        fragmentTransaction.commit();

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


    class LoadingSubscriptionList extends AsyncTask<Object, Object, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {

            m_jsonSubscriptionCategories = JSONRPCAPI.getSubScriptionList();
            if ( m_jsonSubscriptionCategories  == null) return null;
            for (int i = 0; i < m_jsonSubscriptionCategories.length(); i++) {
                try {
                    JSONObject jSubscriptionCategory = m_jsonSubscriptionCategories.getJSONObject(i);
                    int nID = jSubscriptionCategory.getInt("id");
                    String mSubscription_name = jSubscriptionCategory.getString("name");

/*
                    JSONArray jsonArray = JSONRPCAPI.getSubscriptionSubCategories(nID);

                    if( jsonArray == null ) continue;
                    jSubscriptionCategory.put("bParent", true);
                    jSubscriptionCategory.put("bOpened", false);
                    jSubscriptionCategory.put("index", i);
                    //m_jsonKidItems.put(jKidCategory);

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonSubCategory = jsonArray.getJSONObject(j);
                        jsonSubCategory.put("bParent", false);
                        jsonSubCategory.put("parentId", i);
                        m_jsonSubscriptionItems.put(jsonSubCategory);

                    }
*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }
    }


}
