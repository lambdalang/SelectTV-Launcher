package com.selecttvlauncher.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.selecttvlauncher.BeanClass.FavoriteBean;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.selecttvlauncher.ui.activities.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class InterestMainFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressBar load_progressBar;

    public InterestMainFragment() {
        // Required empty public constructor
    }


    public static InterestMainFragment newInstance(String param1, String param2) {
        InterestMainFragment fragment = new InterestMainFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_main, container, false);

        load_progressBar=(ProgressBar)view.findViewById(R.id.load_progressBar);

        HomeActivity.tvShowsList=new ArrayList<>();
        HomeActivity.moviesList=new ArrayList<>();
        HomeActivity.movieGenresList=new ArrayList<>();
        HomeActivity.channelsList=new ArrayList<>();
        HomeActivity.tvnetworksList=new ArrayList<>();
        HomeActivity.videoLibrariesList=new ArrayList<>();

        new getFavoriteList().execute();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private class getFavoriteList extends AsyncTask<String, Object, Object> {
        JSONObject m_response;
        boolean sessionExpired=false;

        @Override
        protected Object doInBackground(String... params) {
            try {
                JSONObject test_arry= JSONRPCAPI.getFavoriteList(PreferenceManager.getAccessToken(getActivity()));
                if ( test_arry == null) return null;
                Log.d("selecttv::==>", "favorite.list::" + test_arry);

                if(test_arry.has("tvnetworks")){
                    sessionExpired=false;
                    JSONArray tvnetworks_array=test_arry.getJSONArray("tvnetworks");
                    if(tvnetworks_array.length()>0){
                        for(int i=0;i<tvnetworks_array.length();i++){
                            JSONObject tvnetworks_object=tvnetworks_array.getJSONObject(i);
                            String slug=tvnetworks_object.getString("slug");
                            int id=tvnetworks_object.getInt("id");
                            String image=tvnetworks_object.getString("image");
                            String name=tvnetworks_object.getString("name");
                            String rating=tvnetworks_object.getString("rating");
                            String runtime=tvnetworks_object.getString("runtime");
                            String description=tvnetworks_object.getString("description");

                            HomeActivity.tvnetworksList.add(new FavoriteBean(slug, image, name, id,rating,runtime,description));

                        }
                    }
                }

                if(test_arry.has("channels")){
                    sessionExpired=false;
                    JSONArray channels_array=test_arry.getJSONArray("channels");
                    if(channels_array.length()>0){
                        for(int i=0;i<channels_array.length();i++){
                            JSONObject channels_array_object=channels_array.getJSONObject(i);
                            String slug=channels_array_object.getString("slug");
                            int id=channels_array_object.getInt("id");
                            String image=channels_array_object.getString("image");
                            String name=channels_array_object.getString("name");
                            String rating=channels_array_object.getString("rating");
                            String runtime=channels_array_object.getString("runtime");
                            String description=channels_array_object.getString("description");

                            HomeActivity.channelsList.add(new FavoriteBean(slug,image,name,id,rating,runtime,description));

                        }
                    }
                }

                if(test_arry.has("movies")){
                    sessionExpired=false;
                    JSONArray movies_array=test_arry.getJSONArray("movies");
                    if(movies_array.length()>0){
                        for(int i=0;i<movies_array.length();i++){
                            JSONObject movies_object=movies_array.getJSONObject(i);
                            String slug=movies_object.getString("slug");
                            int id=movies_object.getInt("id");
                            String image=movies_object.getString("image");
                            String name=movies_object.getString("name");
                            String rating=movies_object.getString("rating");
                            String runtime=movies_object.getString("runtime");
                            String description=movies_object.getString("description");

                            HomeActivity.moviesList.add(new FavoriteBean(slug,image,name,id,rating,runtime,description));

                        }
                    }
                }

                if(test_arry.has("videolibraries")){
                    sessionExpired=false;
                    JSONArray videolibraries_array=test_arry.getJSONArray("videolibraries");
                    if(videolibraries_array.length()>0){
                        for(int i=0;i<videolibraries_array.length();i++){
                            JSONObject videolibraries_object=videolibraries_array.getJSONObject(i);
                            String slug=videolibraries_object.getString("slug");
                            int id=videolibraries_object.getInt("id");
                            String image=videolibraries_object.getString("image");
                            String name=videolibraries_object.getString("name");
                            String rating=videolibraries_object.getString("rating");
                            String runtime=videolibraries_object.getString("runtime");
                            String description=videolibraries_object.getString("description");

                            HomeActivity.videoLibrariesList.add(new FavoriteBean(slug,image,name,id,rating,runtime,description));

                        }
                    }
                }

                if(test_arry.has("moviegenres")){
                    sessionExpired=false;
                    JSONArray moviegenres_array=test_arry.getJSONArray("moviegenres");
                    if(moviegenres_array.length()>0){
                        for(int i=0;i<moviegenres_array.length();i++){
                            JSONObject moviegenres_object=moviegenres_array.getJSONObject(i);
                            String slug=moviegenres_object.getString("slug");
                            int id=moviegenres_object.getInt("id");
                            String image=moviegenres_object.getString("image");
                            String name=moviegenres_object.getString("name");
                            String rating=moviegenres_object.getString("rating");
                            String runtime=moviegenres_object.getString("runtime");
                            String description=moviegenres_object.getString("description");

                            HomeActivity.movieGenresList.add(new FavoriteBean(slug,image,name,id,rating,runtime,description));

                        }
                    }
                }

                if(test_arry.has("shows")){
                    sessionExpired=false;
                    JSONArray shows_array=test_arry.getJSONArray("shows");
                    if(shows_array.length()>0){
                        for(int i=0;i<shows_array.length();i++){
                            JSONObject shows_object=shows_array.getJSONObject(i);
                            String slug=shows_object.getString("slug");
                            int id=shows_object.getInt("id");
                            String image=shows_object.getString("image");
                            String name=shows_object.getString("name");
                            String rating=shows_object.getString("rating");
                            String runtime=shows_object.getString("runtime");
                            String description=shows_object.getString("description");

                            HomeActivity.tvShowsList.add(new FavoriteBean(slug,image,name,id,rating,runtime,description));

                        }
                    }
                }
                if(test_arry.has("name")){
                    if(test_arry.getString("name").equalsIgnoreCase("JSONRPCError")){
                        if(test_arry.has("message")){
                            String message=test_arry.getString("message");
                            if(message.contains("Invalide or expired token")){
                                sessionExpired=true;
                            }
                        }

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
                load_progressBar.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                load_progressBar.setVisibility(View.GONE);
                if(sessionExpired){
                    try {
                        Toast.makeText(getActivity(),"Invalide or expired token",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PreferenceManager.setLogin(false,getActivity());
                    PreferenceManager.setAccessToken("", getActivity());

                    PreferenceManager.setusername("",getActivity());
                    PreferenceManager.setcity("", getActivity());
                    PreferenceManager.setfirst_name("", getActivity());
                    PreferenceManager.setlast_name("", getActivity());
                    PreferenceManager.setgender("", getActivity());
                    PreferenceManager.setemail("", getActivity());
                    PreferenceManager.setstate("", getActivity());
                    PreferenceManager.setdate_of_birth("", getActivity());
                    PreferenceManager.setlast_login("", getActivity());
                    PreferenceManager.setaddress_1("", getActivity());
                    PreferenceManager.setaddress_2("", getActivity());
                    PreferenceManager.setpostal_code("", getActivity());
                    PreferenceManager.setphone_number("", getActivity());
                    PreferenceManager.setid(0, getActivity());

                    Intent in=new Intent(getActivity(),LoginActivity.class);
                    startActivity(in);
                    getActivity().finish();
                }else{
                    try {
                        FragmentTransaction nfragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        InterestLeftFragment mInterestLeftFragment= new InterestLeftFragment();
                        nfragmentTransaction .replace(R.id.fragment_interest_left, mInterestLeftFragment);
                        nfragmentTransaction .commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    InterestRightFragment mInterestRightFragment= InterestRightFragment.newInstance("0","");
                    fragmentTransaction.replace(R.id.fragment_interest_right, mInterestRightFragment);
                    fragmentTransaction.commit();
                }




            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
